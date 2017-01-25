
package com.softech.vu360.lms.webservice.message.vcs.dfc.forum;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.softech.vu360.lms.webservice.message.vcs.dfc.forum package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.softech.vu360.lms.webservice.message.vcs.dfc.forum
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Category }
     * 
     */
    public Category createCategory() {
        return new Category();
    }

    /**
     * Create an instance of {@link Forum }
     * 
     */
    public Forum createForum() {
        return new Forum();
    }

    /**
     * Create an instance of {@link DeleteForumResponse }
     * 
     */
    public DeleteForumResponse createDeleteForumResponse() {
        return new DeleteForumResponse();
    }

    /**
     * Create an instance of {@link WSResponse }
     * 
     */
    public WSResponse createWSResponse() {
        return new WSResponse();
    }

    /**
     * Create an instance of {@link CreateForum }
     * 
     */
    public CreateForum createCreateForum() {
        return new CreateForum();
    }

    /**
     * Create an instance of {@link DeleteForum }
     * 
     */
    public DeleteForum createDeleteForum() {
        return new DeleteForum();
    }

    /**
     * Create an instance of {@link CreateForumResponse }
     * 
     */
    public CreateForumResponse createCreateForumResponse() {
        return new CreateForumResponse();
    }

}
