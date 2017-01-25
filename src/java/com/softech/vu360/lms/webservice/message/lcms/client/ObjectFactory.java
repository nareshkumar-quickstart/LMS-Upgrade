
package com.softech.vu360.lms.webservice.message.lcms.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.softech.vu360.lms.webservice.message.lcms.ArrayOfLong;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.softech.vu360.lms.webservice.message.lcms.client package. 
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

    private final static QName _Int_QNAME = new QName("http://360training.com/", "int");
    private final static QName _ArrayOfUnsignedByte_QNAME = new QName("http://360training.com/", "ArrayOfUnsignedByte");
    private final static QName _ArrayOfString_QNAME = new QName("http://360training.com/", "ArrayOfString");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.softech.vu360.lms.webservice.message.lcms.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UploadAssetInChunk }
     * 
     */
    public UploadAssetInChunk createUploadAssetInChunk() {
        return new UploadAssetInChunk();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     * 
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link ArrayOfUnsignedByte }
     * 
     */
    public ArrayOfUnsignedByte createArrayOfUnsignedByte() {
        return new ArrayOfUnsignedByte();
    }

    /**
     * Create an instance of {@link DownloadAssetInChunk }
     * 
     */
    public DownloadAssetInChunk createDownloadAssetInChunk() {
        return new DownloadAssetInChunk();
    }

    /**
     * Create an instance of {@link GetAssetFileInfoResponse }
     * 
     */
    public GetAssetFileInfoResponse createGetAssetFileInfoResponse() {
        return new GetAssetFileInfoResponse();
    }

    /**
     * Create an instance of {@link SaveAsset }
     * 
     */
    public SaveAsset createSaveAsset() {
        return new SaveAsset();
    }

    /**
     * Create an instance of {@link SaveAssetResponse }
     * 
     */
    public SaveAssetResponse createSaveAssetResponse() {
        return new SaveAssetResponse();
    }

    /**
     * Create an instance of {@link ArrayOfLong }
     * 
     */
    public ArrayOfLong createArrayOfLong() {
        return new ArrayOfLong();
    }

    /**
     * Create an instance of {@link GetAssetFileInfo }
     * 
     */
    public GetAssetFileInfo createGetAssetFileInfo() {
        return new GetAssetFileInfo();
    }

    /**
     * Create an instance of {@link DownloadAssetInChunkResponse }
     * 
     */
    public DownloadAssetInChunkResponse createDownloadAssetInChunkResponse() {
        return new DownloadAssetInChunkResponse();
    }

    /**
     * Create an instance of {@link UpdateAssetStatus }
     * 
     */
    public UpdateAssetStatus createUpdateAssetStatus() {
        return new UpdateAssetStatus();
    }

    /**
     * Create an instance of {@link UpdateAssetStatusResponse }
     * 
     */
    public UpdateAssetStatusResponse createUpdateAssetStatusResponse() {
        return new UpdateAssetStatusResponse();
    }

    /**
     * Create an instance of {@link UploadAssetInChunkResponse }
     * 
     */
    public UploadAssetInChunkResponse createUploadAssetInChunkResponse() {
        return new UploadAssetInChunkResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://360training.com/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfUnsignedByte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://360training.com/", name = "ArrayOfUnsignedByte")
    public JAXBElement<ArrayOfUnsignedByte> createArrayOfUnsignedByte(ArrayOfUnsignedByte value) {
        return new JAXBElement<ArrayOfUnsignedByte>(_ArrayOfUnsignedByte_QNAME, ArrayOfUnsignedByte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://360training.com/", name = "ArrayOfString")
    public JAXBElement<ArrayOfString> createArrayOfString(ArrayOfString value) {
        return new JAXBElement<ArrayOfString>(_ArrayOfString_QNAME, ArrayOfString.class, null, value);
    }


}
