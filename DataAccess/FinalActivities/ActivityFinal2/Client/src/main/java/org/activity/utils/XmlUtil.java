package org.activity.utils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlUtil {

    // Ensures only direct child elements are read, avoiding the deep-search
    // behavior of getElementsByTagName which may return nested tags
    // with the same name.
    public static String directText(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();

        // Iterate through direct children and find the first matching element
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element el &&
                    el.getTagName().equals(tagName)) {

                String value = el.getTextContent();
                if (value == null) return null;

                value = value.trim();
                return value.isEmpty() ? null : value;
            }
        }
        return null;
    }


}
