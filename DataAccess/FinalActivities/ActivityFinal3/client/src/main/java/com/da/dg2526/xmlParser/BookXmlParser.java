package com.da.dg2526.xmlParser;

import com.da.dg2526.models.dto.BookInputDto;
import com.da.dg2526.models.dto.BookXmlDto;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BookXmlParser {
    public static List<BookXmlDto> parse(Path xmlPath) {
        try {
            //DOM parser, loads file.
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlPath.toFile());

            //Cleans white spaces
            doc.getDocumentElement().normalize();

            //Gets all nodes with <student></student>
            NodeList studentNodes = doc.getElementsByTagName("book");

            //Creates list for results
            List<BookXmlDto> result = new ArrayList<>();

            for (int i = 0; i < studentNodes.getLength(); i++) {
                Element bookElement = (Element) studentNodes.item(i);

                String isbn = directText(bookElement, "isbn");
                String title = directText(bookElement, "title");
                String copies = directText(bookElement, "copies");
                String outline = directText(bookElement, "outline");   // may be null
                String publisher = directText(bookElement, "publisher");   // may be null
                String category = directText(bookElement, "category");

                result.add(new BookXmlDto(isbn, title, copies, outline, publisher, category));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XML: " + e.getMessage(), e);
        }
    }

    public static String directText(Element parent, String tagName) {
        var children = parent.getChildNodes();

        // Iterate through direct children and find the first matching element
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element el && el.getTagName().equals(tagName)) {

                String value = el.getTextContent();
                if (value == null) return null;

                value = value.trim();
                return value.isEmpty() ? null : value;
            }
        }
        return null;
    }
}
