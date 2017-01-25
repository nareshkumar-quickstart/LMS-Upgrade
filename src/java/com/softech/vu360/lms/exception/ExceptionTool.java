package com.softech.vu360.lms.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * ReportNotExecutableException class... enough said.
 *
 * @author  jason
 */
public class ExceptionTool {

	public String stackTrace(Exception exception) {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    PrintStream printStream = new PrintStream(byteArrayOutputStream);
	    exception.printStackTrace(printStream);
	    printStream.close();
	    // Closing a ByteArrayOutputStream has no effect, so don't do it which avoids the need to try/catch the IOException
	    return byteArrayOutputStream.toString();
	}
	
}
