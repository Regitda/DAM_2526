package org.activity.xmlParser;

import org.activity.dto.student.StudentImportDto;
import org.activity.utils.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StudentXmlParser {
    private StudentXmlParser() {}

    public static List<StudentImportDto> parse(Path xmlPath) {
        try {
            //DOM parser, loads file.
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlPath.toFile());

            //Cleans white spaces
            doc.getDocumentElement().normalize();

            //Gets all nodes with <student></student>
            NodeList studentNodes = doc.getElementsByTagName("student");

            //Creates list for results
            List<StudentImportDto> result = new ArrayList<>();

            for (int i = 0; i < studentNodes.getLength(); i++) {
                Element studentEl = (Element) studentNodes.item(i);

                String idCard = XmlUtil.directText(studentEl, "idcard");
                String firstName = XmlUtil.directText(studentEl, "firstname");
                String lastName = XmlUtil.directText(studentEl, "lastname");
                String email = XmlUtil.directText(studentEl, "email");   // may be null
                String phone = XmlUtil.directText(studentEl, "phone");   // may be null

                result.add(new StudentImportDto(idCard, firstName, lastName, email, phone));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XML: " + e.getMessage(), e);
        }
    }
}
