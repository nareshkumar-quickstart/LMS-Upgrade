/**
 * 
 */
package com.softech.vu360.util;

/**
 * @author syed.mahmood
 *
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import sun.util.ResourceBundleEnumeration;



public class ResourceBundleWrapper extends ResourceBundle implements Serializable {
    
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceBundleWrapper() {
		super();
	}


	public ResourceBundleWrapper (InputStream stream) throws IOException {
        Properties properties = new Properties();
        properties.load(stream);
        lookup = new HashMap(properties);
    }

    
    public ResourceBundleWrapper (Reader reader) throws IOException {
        Properties properties = new Properties();
        properties.load(reader);
        lookup = new HashMap(properties);
    }

    
    public Object handleGetObject(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return lookup.get(key);
    }

    
    public Enumeration<String> getKeys() {
        ResourceBundle parent = this.parent;
        return new ResourceBundleEnumeration(lookup.keySet(),
                (parent != null) ? parent.getKeys() : null);
    }

    
    protected Set<String> handleKeySet() {
        return lookup.keySet();
    }

    // ==================privates====================

    private Map<String,Object> lookup;
}
