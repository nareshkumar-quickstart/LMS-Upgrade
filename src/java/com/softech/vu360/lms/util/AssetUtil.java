package com.softech.vu360.lms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.util.VU360Properties;

public class AssetUtil {
	
	/**
	 * @author muhammad.ashrafi
	 * @param asset
	 * @return Asset file location
	 * @throws URISyntaxException 
	 * @throws MalformedURLException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String getAssetFilePath(Asset asset){
		StringBuffer filePath = new StringBuffer();
		
		filePath.append(VU360Properties.getVU360Property("lcms.server.fileSystemPath"));
		filePath.append(VU360Properties.getVU360Property("lcms.server.assetsDirectoryPath"));
		filePath.append(File.separator).append(asset.getId());
		filePath.append(File.separator).append(asset.getCurrentAssetVersionId());
		if(asset instanceof Document){
			filePath.append(File.separator).append((((Document)asset).getFileName()));
		}
		return filePath.toString();
	}

	public static URL getAssetFilePathURL(Asset asset) throws URISyntaxException, MalformedURLException{
		return getEncodedURL(getAssetFilePath(asset));
	}
	
	public static URL getEncodedURL(String filePath)throws URISyntaxException, MalformedURLException{

		URL url = new URL(filePath);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		url = uri.toURL();

		return url;
	}
	
	public static void serialize(VU360User user)
	{
		try {
		    // Serialize to a file
		    ObjectOutput out = new ObjectOutputStream(new FileOutputStream("filename.ser"));
		    out.writeObject(user);
		    out.close();

		    // Serialize to a byte array
		    ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
		    out = new ObjectOutputStream(bos) ;
		    out.writeObject(user);
		    out.close();

		    // Get the bytes of the serialized object
		    byte[] buf = bos.toByteArray();
		    deserialize(buf);
		    
		} catch (IOException e) {
		}
	}
	
	public static void deserialize( byte[] buf){
		try {
		    // Deserialize from a file
		    File file = new File("filename.ser");
		    ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buf));
		    // Deserialize the object
		    VU360User user = (VU360User) in.readObject();
		    in.close();

		    // Get some byte array data
		    byte[] bytes = buf;
		    // see Reading a File into a Byte Array for the implementation of this method

		    // Deserialize from a byte array
		    in = new ObjectInputStream(new ByteArrayInputStream(bytes));
		    user = (VU360User) in.readObject();
		    in.close();
		    
		    
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		}
	}

}
