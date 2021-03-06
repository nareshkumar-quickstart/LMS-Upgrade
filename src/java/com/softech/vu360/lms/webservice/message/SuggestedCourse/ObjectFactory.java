//
// Generated By:JAX-WS RI IBM 2.1.6 in JDK 6 (JAXB RI IBM JAXB 2.1.10 in JDK 6)
//


package com.softech.vu360.lms.webservice.message.SuggestedCourse;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.suggestedcoursesdisplayservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SuggestedCoursesDisplayResponse_QNAME = new QName("http://www.example.org/SuggestedCoursesDisplayService", "SuggestedCoursesDisplayResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.suggestedcoursesdisplayservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SuggestedCoursesDisplayResponseType }
     * 
     */
    public SuggestedCoursesDisplayResponseType createSuggestedCoursesDisplayResponseType() {
        return new SuggestedCoursesDisplayResponseType();
    }

    /**
     * Create an instance of {@link SuggestedCoursesDisplayRequestType }
     * 
     */
    public SuggestedCoursesDisplayRequestType createSuggestedCoursesDisplayRequestType() {
        return new SuggestedCoursesDisplayRequestType();
    }

    /**
     * Create an instance of {@link SuggestedCoursesDisplayRequest }
     * 
     */
    public SuggestedCoursesDisplayRequest createSuggestedCoursesDisplayRequest() {
        return new SuggestedCoursesDisplayRequest();
    }

    /**
     * Create an instance of {@link CourseInfo }
     * 
     */
    public CourseInfo createCourseInfo() {
        return new CourseInfo();
    }

    /**
     * Create an instance of {@link SuggestedCoursesList }
     * 
     */
    public SuggestedCoursesList createSuggestedCoursesList() {
        return new SuggestedCoursesList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuggestedCoursesDisplayResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/SuggestedCoursesDisplayService", name = "SuggestedCoursesDisplayResponse")
    public JAXBElement<SuggestedCoursesDisplayResponseType> createSuggestedCoursesDisplayResponse(SuggestedCoursesDisplayResponseType value) {
        return new JAXBElement<SuggestedCoursesDisplayResponseType>(_SuggestedCoursesDisplayResponse_QNAME, SuggestedCoursesDisplayResponseType.class, null, value);
    }

}
