package com.softech.vu360.lms.webservice.client;

import java.io.IOException;

public interface RestOperations {
	public String postForObject(Object obj, String servicePath)	throws IOException;
	public String postForObject(Object obj, String servicePath,int connTimeOut) throws IOException;
}
