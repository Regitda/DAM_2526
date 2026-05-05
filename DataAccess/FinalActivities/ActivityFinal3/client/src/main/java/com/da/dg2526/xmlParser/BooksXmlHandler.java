package com.da.dg2526.xmlParser;

import com.da.dg2526.models.dto.BookXmlDto;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class BooksXmlHandler extends DefaultHandler {


    private final List<BookXmlDto> books = new ArrayList<>();
    private final StringBuilder text = new StringBuilder();


    private final String bookTag = "book";
    private final String titleTag = "title";
    private final String isbnTag = "isbn";
    private final String copiesTag = "copies";
    private final String publisherTag = "publisher";
    private final String categoryTag = "category";
    private final String outlineTag = "outline";


    private boolean insideBook = false;

    private String isbn;
    private String title;
    private String copies;
    private String outline;
    private String publisher;
    private String category;

    List<BookXmlDto> getBooks() {
        return books;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        text.append(ch, start, length);
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        text.setLength(0);

        if (bookTag.equals(qName)) {
            insideBook = true;
            isbn = null;
            title = null;
            copies = null;
            outline = null;
            publisher = null;
            category = null;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (!insideBook) return;

        var value = clean(text.toString());

        switch (qName) {
            case isbnTag -> isbn = value;
            case titleTag -> title = value;
            case copiesTag -> copies = value;
            case publisherTag -> publisher = value;
            case categoryTag -> category = value;
            case outlineTag -> outline = value;
            case bookTag -> {
                books.add(new BookXmlDto(isbn, title, copies, outline, publisher, category));
                insideBook = false;
            }
        }
        text.setLength(0);
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }


}
