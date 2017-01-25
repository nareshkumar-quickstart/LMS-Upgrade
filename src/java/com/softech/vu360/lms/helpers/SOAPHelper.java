package com.softech.vu360.lms.helpers;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.softech.vu360.lms.exception.InvalidSOAPMessageException;
import com.softech.vu360.lms.util.ResourceResolver;

/**
 * Helper class for SOAP client
 *
 * @author ramiz.uddin
 * @since 0.1
 */
public final class SOAPHelper {

    // non-instanciable class
    private SOAPHelper() {
    }

    private static final Logger LOGGER =
            Logger.getLogger(SOAPHelper.class.getName());

    /**
     * Object marhshalling
     * <p/>
     * <p/>
     * To "marshal" an object means to record its state and codebase(s) in such
     * a way that when the marshalled object is "unmarshalled", a copy of the
     * original object is obtained, possibly by automatically loading the class
     * definitions of the object. You can marshal any object that is
     * serializable or remote. Marshalling is like serialization, except
     * marshalling also records codebases. Marshalling is different from
     * serialization in that marshalling treats remote objects specially.
     *
     * @param objectToMarshall T
     * @return String
     * @throws JAXBException
     * @see <a href="http://en.wikipedia
     *      .org/wiki/Marshalling_%28computer_science%29">Marshalling)</a>
     *      <p/>
     *      </p>
     */
    public static <T> String marshaller(T objectToMarshall) throws
            JAXBException {

        JAXBContext context = JAXBContext.newInstance(objectToMarshall
                .getClass());
        Marshaller marshaller = context.createMarshaller();

        StringWriter swObjectToMarshall = new StringWriter();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(objectToMarshall, swObjectToMarshall);

        return swObjectToMarshall.toString();

    }

    /**
     * Object unmarhshalling
     *
     * @param type           Class<T>
     * @param marshallString String
     * @return T
     * @throws JAXBException
     * @throws SAXException
     */
    public static <T> T unmarshaller(
            Class<T> type,
            String marshallString)
            throws
            JAXBException, SAXException {

        /*
        * Instead of directly referenced unmarshall object into T
        * we are getting it into JAXBElement. This avoid exception when trying
        * to unmarshall object which does not annotated with @XmlRootElement

        * Exception:
        * javax.xml.bind.MarshalException - with linked exception: unable to
        * marshal type "com.softech.vu360.lms.selfservice..." as an
        * element because it is missing an @XmlRootElement annotation]
        *
        * @see http://stackoverflow.com/a/5870064
        */
        JAXBElement<T> element;

        JAXBContext context = JAXBContext.newInstance(type);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        element = unmarshaller.unmarshal(
                (XMLStreamReader) new StringReader(marshallString),
                type);

        return element.getValue();

    }

    /**
     * Validate SOAP message against a schema
     *
     * @param xml           String
     * @param schemaName    String
     * @throws InvalidSOAPMessageException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @see <a href="http://stackoverflow.com/a/2342859">Validate
     *      against Schema</a>
     */
    public static <T> void validate(String xml, String schemaName)
            throws com.softech.vu360.lms.exception.InvalidSOAPMessageException, IOException, SAXException,
            ParserConfigurationException {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);

        DocumentBuilder parser = builderFactory.newDocumentBuilder();

        // parse the XML into a document object
        InputSource is = new InputSource(new StringReader(xml));
        Document document = parser.parse(is);

        SchemaFactory factory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // associate the schema factory with the resource resolver, which is
        // responsible for resolving the imported XSD's
        factory.setResourceResolver(new ResourceResolver());

        // note that if your XML already declares the XSD to which it has to
        // conform, then there's no need to create a validator from a Schema
        // object
        Source schemaFile = new StreamSource(SOAPHelper.class.getClassLoader()
                .getResourceAsStream(schemaName));
        Schema schema = factory.newSchema(schemaFile);

        Validator validator = schema.newValidator();

        try {
            validator.validate(new DOMSource(document));
        } catch (SAXException e) {
            throw new com.softech.vu360.lms.exception.InvalidSOAPMessageException( e.getMessage());
        }
    }


}
