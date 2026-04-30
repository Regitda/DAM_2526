package org.activity.service;

import org.activity.dao.StudentDao;
import org.activity.dto.student.StudentImportDto;
import org.activity.exceptions.ServiceValidationException;
import org.activity.restapi.RestApiConnection;
import org.activity.utils.HibernateUtil;
import org.activity.xmlParser.StudentXmlParser;
import org.entities.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class StudentService {
    /*
    The -a / --add option will add students to the database. The student information will
    be delivered to the application in a XML file (you can check the example file provided in
    the annex). If an error occurs when importing, the user will be notified (for example,
    duplicated ID card, or incorrect phone number format). No students will be imported if
    an error occurs
    */

    private final StudentDao studentDAO = new StudentDao();

    public StudentService(RestApiConnection connection) {
    }

    public int importStudentsFromXml(Path path) {

        var dtos = StudentXmlParser.parse(path);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {


            // Validation. If any fails do not commit to transaction.
            String errors = validateDtoData(dtos, session);
            if (!errors.isBlank())  throw new ServiceValidationException(errors);


            // Transaction.
            Transaction tx = session.beginTransaction();
            try {
                for (var dto : dtos) {
                    Student entity = toEntity(dto);
                    studentDAO.save(session, entity);
                }
                tx.commit();
                return dtos.size();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }




    private String validateDtoData(Iterable<StudentImportDto> dtoInput, Session session) {
        StringBuilder errors = new StringBuilder();
        Set<String> idCardsInFile = new HashSet<>();

        for (var dto : dtoInput) {

            String id = dto.idCard();

            // REQUIRED: FIRST NAME
            if (dto.firstName() == null || dto.firstName().isBlank()) {
                errors.append("First name is missing for idCard=").append(id).append("\n");
            } else if (dto.firstName().length() > 50) {
                errors.append("First name too long (>50) for idCard=").append(id).append("\n");
            }

            // REQUIRED: LAST NAME
            if (dto.lastName() == null || dto.lastName().isBlank()) {
                errors.append("Last name is missing for idCard=").append(id).append("\n");
            } else if (dto.lastName().length() > 100) {
                errors.append("Last name too long (>100) for idCard=").append(id).append("\n");
            }

            // REQUIRED: ID CARD
            if (id == null || id.isBlank()) {
                errors.append("idCard is missing\n");
            } else if (id.length() > 12) {
                errors.append("idCard too long (>12): ").append(id).append("\n");
            } else {
                // Duplicate in XML
                if (!idCardsInFile.add(id)) {
                    errors.append("Duplicate idCard in XML file: ").append(id).append("\n");
                }
                // Duplicate in DB
                if (studentDAO.existsByIdCard(session, id)) {
                    errors.append("idCard already exists in DB: ").append(id).append("\n");
                }
            }

            // OPTIONAL: PHONE
            if (dto.phone() != null && !dto.phone().isBlank()) {
                if (!dto.phone().matches("\\d{9,12}")) {
                    errors.append("Invalid phone format for idCard=").append(id)
                            .append(" phone=").append(dto.phone()).append("\n");
                }
            }

            // OPTIONAL: EMAIL
            if (dto.email() != null && !dto.email().isBlank()) {
                if (!dto.email().contains("@")) {
                    errors.append("Invalid email format for idCard=").append(id)
                            .append(" email=").append(dto.email()).append("\n");
                }
            }
        }

        return errors.toString();
    }

    private Student toEntity(StudentImportDto dto) {
        Student s = new Student();
        s.setIdcard(dto.idCard());
        s.setFirstname(dto.firstName());
        s.setLastname(dto.lastName());
        s.setEmail(dto.email());
        s.setPhone(dto.phone());
        return s;
    }
}
