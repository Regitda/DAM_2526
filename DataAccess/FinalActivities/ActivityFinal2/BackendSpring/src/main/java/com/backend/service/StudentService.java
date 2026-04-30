package com.backend.service;

import com.backend.dao.StudentRepository;
import com.backend.dto.student.StudentImportDto;
import com.backend.entities.Student;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    /*
    The -a / --add option will add students to the database. The student information will
    be delivered to the application in a XML file (you can check the example file provided in
    the annex). If an error occurs when importing, the user will be notified (for example,
    duplicated ID card, or incorrect phone number format). No students will be imported if
    an error occurs
    */

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void addStudent(StudentImportDto student ) {
        studentRepository.save(toEntity(student));
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

    public void updateStudent(String id, StudentImportDto student) {

        var old = studentRepository.findById(id);
        if (old.isPresent()) {
            studentRepository.save(toEntity(student));
        }else {
            throw new ServiceException("Student not found, edit failed.");
        }
    }
}
