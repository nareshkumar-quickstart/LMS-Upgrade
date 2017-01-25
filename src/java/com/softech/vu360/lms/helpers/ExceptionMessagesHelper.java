package com.softech.vu360.lms.helpers;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.util.HtmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author ramiz.uddin
 * @since 0.1
 */
public class ExceptionMessagesHelper {

    // non-initiatable
    private ExceptionMessagesHelper() {
    }

    // exception messages file (along with classpath)
    private static final String EXCEPTION_MESSAGES_XML = "exception_messages.xml";
    private static final String EXCEPTION_MESSAGES_PARENT_ELEMENT =
            "exception";
    private static final String EXCEPTION_CLASS_ATTRIBUTE_NAME = "class";
    private static final String EXCEPTION_MESSAGE_TAG_NAME = "message";

    private static Document document;

    static {

        try {
            ClassPathResource resource = new ClassPathResource
                    (EXCEPTION_MESSAGES_XML);
            File file = new File(resource.getURI());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getExceptionMessageByClass(String className) {

        NodeList exceptionList = document.getElementsByTagName
                (EXCEPTION_MESSAGES_PARENT_ELEMENT);
        for (int index = 0; index < exceptionList.getLength(); index++) {
            Node exception = exceptionList.item(index);
            if (exception instanceof Element) {
                Element element = (Element) exception;
                if (element
                        .getAttribute(EXCEPTION_CLASS_ATTRIBUTE_NAME)
                        .equals(className)) {
                    return HtmlUtils.htmlEscape(
                            element
                                .getElementsByTagName(EXCEPTION_MESSAGE_TAG_NAME)
                                .item(0).getTextContent()
                            );
                }
            }
        }
        return StringUtils.EMPTY;
    }

}
