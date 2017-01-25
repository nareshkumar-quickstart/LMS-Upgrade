package com.softech.vu360.lms.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.rpc.holders.ByteArrayHolder;
import javax.xml.rpc.holders.StringHolder;
import javax.xml.soap.SOAPException;

import org.apache.axis.message.SOAPHeaderElement;
import org.apache.commons.httpclient.auth.AuthPolicy;

import com.microsoft.schemas.sqlserver._2005._06._30.reporting.reportingservices.ExecutionInfo;
import com.microsoft.schemas.sqlserver._2005._06._30.reporting.reportingservices.ParameterValue;
import com.microsoft.schemas.sqlserver._2005._06._30.reporting.reportingservices.ReportExecutionServiceLocator;
import com.microsoft.schemas.sqlserver._2005._06._30.reporting.reportingservices.ReportExecutionServiceSoapStub;
import com.microsoft.schemas.sqlserver._2005._06._30.reporting.reportingservices.holders.ArrayOfStringHolder;
import com.microsoft.schemas.sqlserver._2005._06._30.reporting.reportingservices.holders.ArrayOfWarningHolder;
import com.softech.vu360.util.VU360Properties;

public class SSRSUtility
{
	private static final String ssrsWebServiceLoginId  = VU360Properties.getVU360Property("ssrs.login.userid");
	private static final String ssrsWebServicePassword = VU360Properties.getVU360Property("ssrs.login.password");
	private static final String ssrsWebServiceEndPoint = VU360Properties.getVU360Property("ssrs.webservice.endpoint");
	private static final String htmlDeviceInfo = VU360Properties.getVU360Property("ssrs.deviceinfo.html");
	private static final String exportedReportsFolder = VU360Properties.getVU360Property("ssrs.export.folder");
	private static final String reportImagesFolderAbsolute = VU360Properties.getVU360Property("ssrs.images.folderAbsolute");

	public static String loadReport(String reportPath, ParameterValue[] parameters)
	{
		AuthPolicy.registerAuthScheme(AuthPolicy.NTLM, JCIFS_NTLMScheme.class);
		ReportExecutionServiceSoapStub service = getService();

		String reportHTML = "";

		try
		{
			ExecutionInfo info = service.loadReport(reportPath, null); //Load report
			setExecutionId(service, info.getExecutionID()); //You must set the session id before continuing
			service.setExecutionParameters(parameters, "en-us"); //Set report parameters
			
			String format = "HTML4.0"; //Valid options are HTML4.0, MHTML, EXCEL, CSV, PDF, etc
			String deviceInfo = htmlDeviceInfo; 
			ByteArrayHolder result = new ByteArrayHolder();
			ByteArrayHolder image = new ByteArrayHolder();
			StringHolder extension = new StringHolder();
			StringHolder mimeType = new StringHolder();
			StringHolder encoding = new StringHolder();
			ArrayOfWarningHolder warnings = new ArrayOfWarningHolder();
			ArrayOfStringHolder streamIDs = new ArrayOfStringHolder();  
			service.render(format, deviceInfo, result, extension, mimeType, encoding, warnings, streamIDs); //Render report to HTML
			
			FileOutputStream stream;
			for (String streamID:streamIDs.value){
				stream = new FileOutputStream(reportImagesFolderAbsolute + streamID);
				service.renderStream(format, streamID, deviceInfo, image, encoding, mimeType);
			    stream.write(image.value); 
			    stream.close();
			}

			reportHTML = new String(result.value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return reportHTML;
	}

	public static String exportReport(String reportPath, ParameterValue[] parameters, String format)
	{
		AuthPolicy.registerAuthScheme(AuthPolicy.NTLM, JCIFS_NTLMScheme.class);
		ReportExecutionServiceSoapStub service = getService();

		String exporedFilePath = "";
		
		try
		{
			ExecutionInfo info = service.loadReport(reportPath, null); //Load report
			setExecutionId(service, info.getExecutionID()); //You must set the session id before continuing
			service.setExecutionParameters(parameters, "en-us"); //Set report parameters
			
			String deviceInfo = " <DeviceInfo></DeviceInfo>"; 
			ByteArrayHolder result = new ByteArrayHolder();
			StringHolder extension = new StringHolder();
			StringHolder mimeType = new StringHolder();
			StringHolder encoding = new StringHolder();
			ArrayOfWarningHolder warnings = new ArrayOfWarningHolder();
			ArrayOfStringHolder streamIDs = new ArrayOfStringHolder();  
			service.render(format, deviceInfo, result, extension, mimeType, encoding, warnings, streamIDs); //Render report to HTML

			exporedFilePath = saveFile(result, reportPath, exportedReportsFolder, format);

			//reportHTML = reportHTML.replace("http://360-sms-report", "http://LMSApplicationUser:" + ssrsWebServicePassword + "@360-sms-report:80");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return exporedFilePath;
	}

	private static String saveFile(ByteArrayHolder result, String reportPath, String outPutFolder, String format){
		String reportName = reportPath.substring(reportPath.lastIndexOf("/")+1);
		if (format.equalsIgnoreCase("EXCEL")) format = "XLS";
		String fileName = new StringBuilder().append(reportName).append("-").append(new SimpleDateFormat("dd-MMM-yyyy-HH_mm_ss").format(new Date())).append(".").append(format).toString();
		String fullFilePath = outPutFolder + fileName;
		try {
			FileOutputStream stream = new FileOutputStream(new File(fileName));
			stream.write(result.value);
			stream.close();
		} catch (Exception e) {
		}
		
		return fileName;
	}

	private static void setExecutionId(ReportExecutionServiceSoapStub service, String id) throws SOAPException
	{
		SOAPHeaderElement sessionHeader = new SOAPHeaderElement("http://schemas.microsoft.com/sqlserver/2005/06/30/reporting/reportingservices", "ExecutionHeader");
		sessionHeader.addChildElement("ExecutionID").addTextNode(id);
		service.setHeader(sessionHeader);
	}

	private static ReportExecutionServiceSoapStub getService()
	{
		try
		{
			ReportExecutionServiceSoapStub service = (ReportExecutionServiceSoapStub)new ReportExecutionServiceLocator(getEngineConfiguration()).getReportExecutionServiceSoap(new URL(ssrsWebServiceEndPoint));
			service.setUsername(ssrsWebServiceLoginId);
			service.setPassword(ssrsWebServicePassword);
			return service;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private static org.apache.axis.EngineConfiguration getEngineConfiguration()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		sb.append("<deployment name=\"defaultClientConfig\"\r\n");
		sb.append("xmlns=\"http://xml.apache.org/axis/wsdd/\"\r\n");
		sb.append("xmlns:java=\"http://xml.apache.org/axis/wsdd/providers/java\">\r\n");
		sb.append("<globalConfiguration>\r\n");
		sb.append("<parameter name=\"disablePrettyXML\" value=\"true\"/>\r\n");
		sb.append("<parameter name=\"enableNamespacePrefixOptimization\" value=\"true\"/>\r\n");
		sb.append("</globalConfiguration>\r\n");
		sb.append("<transport name=\"http\" pivot=\"java:org.apache.axis.transport.http.CommonsHTTPSender\"/>\r\n");
		sb.append("<transport name=\"local\" pivot=\"java:org.apache.axis.transport.local.LocalSender\"/>\r\n");
		sb.append("<transport name=\"java\" pivot=\"java:org.apache.axis.transport.java.JavaSender\"/>\r\n");
		sb.append("</deployment>\r\n");

		return new org.apache.axis.configuration.XMLStringProvider(sb.toString());
	}
} 