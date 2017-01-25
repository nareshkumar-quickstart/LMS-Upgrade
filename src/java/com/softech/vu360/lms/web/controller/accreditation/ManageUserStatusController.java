package com.softech.vu360.lms.web.controller.accreditation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.ManageUserStatus;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.ManageUserStatusService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.util.FtpClientHandler;
import com.softech.vu360.lms.util.IFtpClient;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageUserStatusForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.ManageUserStatusSort;
import com.softech.vu360.util.UserStatusUpdateUtil;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * 
 * @author haider.ali
 * 
 */
public class ManageUserStatusController extends VU360BaseMultiActionController {

	private static final Logger log = Logger
			.getLogger(ManageUserStatusController.class.getName());
	public String manageUserStatusTemplate = StringUtils.EMPTY;
	public String manageUserStatusAjaxTemplate = StringUtils.EMPTY;
	public static final String CUSTOMFIELD_ALL = "All";
	public ManageUserStatusService manageUserStatusService = null;

	private StatisticsService statisticsService = null;
	public static final String CONTEXT = "context";

	private AccreditationService accreditationService;
	private EntitlementService entitlementService = null;

	private UserStatusUpdateUtil userStatusUpdateUtil = null;

	private String assetType = Asset.ASSET_TYPE_AFFIDAVIT;

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		if (methodName.equals("changeUserStatus")) {
			this.getValidator().validate(command, errors);
		}
	}

	public ModelAndView initializePage(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		ManageUserStatusForm form = (ManageUserStatusForm) command;
		form.resetForm();
		form.setManageUserStatus(null);

		HashMap context = initializePageContext(form, isShowAll(request));
		context.put("sortDirection", 1);
		context.put("sortColumnIndex", 0);

        context.put("isAdmin", isAdminMode());

		ModelAndView modelAndView = new ModelAndView(manageUserStatusTemplate,
				CONTEXT, context);

		return modelAndView;

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView sortUserStatus(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		List<ManageUserStatus> mangeUserStatusList = null;
		ManageUserStatusForm form = (ManageUserStatusForm) command;
		form.setManageUserStatus(new ManageUserStatus());
		form.getManageUserStatus().setFirstName(form.getFirstName());
		form.getManageUserStatus().setLastName(form.getLastName());
		form.getManageUserStatus().setEmailAddress(form.getEmailAddress());
		form.getManageUserStatus().setCourseId(form.getCourseId());
		form.getManageUserStatus().setHoldingRegulatorId(
				form.getHoldingRegulatorId());
		form.getManageUserStatus().setRegulatoryCategoryId(
				form.getRegulatorCategoryId());
		form.getManageUserStatus().setCourseTypeId(form.getCourseTypeId());
		form.getManageUserStatus().setCourseStatus(form.getCourseStatus());
		form.getManageUserStatus().setAffidavitType(form.getAffidavitType());
		form.getManageUserStatus().setStartDate(form.getStartDate());
		form.getManageUserStatus().setEndDate(form.getEndDate());

		HashMap<String, Object> context = new HashMap<String, Object>();
		if (form.isNotEmptyForm())
			mangeUserStatusList = manageUserStatusService
					.getUserStatusList(form.getManageUserStatus());

		if (mangeUserStatusList != null && !mangeUserStatusList.isEmpty()) {
			// performSortingOnCollection();
			if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("0")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("firstName");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}

				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("1")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("lastName");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);

			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("2")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("emailAddress1");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);

			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("3")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("phoneNumber");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);

			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("4")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("courseName");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);

			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("5")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("courseId");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("7")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("courseStatus");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("8")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("courseType");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("9")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("completeDate");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("21")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("enrollmentDate");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("22")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("firstAccessDate");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("10")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("holdingRegulatorName");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("11")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("regulatoryCategory");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("12")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("affidavitType");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("13")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("lastUserStatusChange");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("14")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("lastUserStatusChangeDate");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("15")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("lastUserAffidavitUpload");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("16")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("lastUserAffidavitUploadDate");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("17")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("address1");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("18")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("city1");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("19")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("state");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			} else if (getRequestParam("sortColumnIndex", request) != null
					&& getRequestParam("sortColumnIndex", request).equals("20")) {
				ManageUserStatusSort compartor = new ManageUserStatusSort();
				compartor.setSortBy("zipCode");
				if (getRequestParam("sortDirection", request) != null
						&& getRequestParam("sortDirection", request)
								.equals("1")) {
					compartor.setSortDirection(1);
				} else {
					compartor.setSortDirection(0);
				}
				Collections.sort(mangeUserStatusList, compartor);
			}
		}
		context.put("sortDirection", getRequestParam("sortDirection", request));
		context.put("sortColumnIndex",
				getRequestParam("sortColumnIndex", request));

		context.put("showAll", isShowAll(request));
		context.put("userstatuses", mangeUserStatusList);
		List<Regulator> holdingRegulators = accreditationService
				.findRegulator();
		context.put("holdingRegulators", holdingRegulators);

		context.put("isAdmin", isAdminMode());

		ModelAndView modelAndView = new ModelAndView(manageUserStatusTemplate,
				CONTEXT, context);

		return modelAndView;

	}

	public ModelAndView exportResult(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {

		ManageUserStatusForm form = (ManageUserStatusForm) command;
		List<ManageUserStatus> mangeUserStatusList = null;

		if (form.isNotEmptyForm())
			mangeUserStatusList = manageUserStatusService
					.getUserStatusList(form.getManageUserStatus());

		if (mangeUserStatusList == null || mangeUserStatusList.size() <= 0) {
			errors.reject("error.export.result.empty");
			ModelAndView modelAndView = new ModelAndView(
					manageUserStatusTemplate, CONTEXT, initializePageContext(
							form, isShowAll(request)));
			return modelAndView;
		}

		try {

			ByteArrayOutputStream csvText = (ByteArrayOutputStream) getManageUserStatusCSV(
					VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(
							VU360Branding.BRAND), new Language()), mangeUserStatusList);

			response.reset();
			response.setContentType("text/csv");
			response.setContentLength((int) csvText.toByteArray().length);
			response.setHeader("Content-Disposition", "attachment; filename="
					+ "ManageUserStatus-SearchResult.csv;");

			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(csvText.toByteArray());
			outputStream.flush();
			outputStream.close();
		} catch (IOException ioe) {
			log.error("exception", ioe);
		}

		return null;
	}

	public ModelAndView printAffidavits(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		ManageUserStatusForm form = (ManageUserStatusForm) command;
		List<Long> userStatuses = form.getUserstatuses();
		List<byte[]> fileStreamList = new ArrayList<byte[]>();
		IFtpClient ftpClient = null;

		try {

			ftpClient = new FtpClientHandler(null);
			ftpClient.connect();

			for (Long userStatus : userStatuses) {
				String fileName = userStatus.toString() + "_DocuSign.pdf";
				File f = ftpClient.perfromReadFileTask(fileName);
				try {
					URL url = f.toURI().toURL();
					InputStream is = url.openStream();
					byte[] filedata = IOUtils.toByteArray(is);
					if (filedata != null && filedata.length > 0) {
						fileStreamList.add(filedata);
					}
					is.close();
				} catch (Exception ex) {
					log.error(ex);
				}
			}

			if (fileStreamList.size() > 0) {

				log.info("Total " + fileStreamList.size()
						+ "files read from FTP.");

				ServletOutputStream outputStream = null;
				response.reset();

				outputStream = response.getOutputStream();
				String password = VU360Properties
						.getVU360Property("lms.server.certificate.SecurityKey");
				PdfCopyFields copy = new PdfCopyFields(outputStream);
				copy.setEncryption(null, password.getBytes(),
						PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
				PdfReader reader = null;
				int contentLenght = 0;

				for (byte[] fileStream : fileStreamList) {
					if (fileStream != null && fileStream.length > 0) {
						contentLenght = contentLenght + fileStream.length;
						reader = new PdfReader(fileStream, password.getBytes());
						copy.addDocument(reader);
						reader.close();
					}
				}
				copy.close();
				response.setHeader("Expires", "0");
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition",
						"attachment; filename=affidavit.pdf;");
				response.setContentLength(contentLenght);
				outputStream.flush();
				outputStream.close();
				return null;

			} else {

				log.info("Total 0 file read from FTP.");
				errors.reject("error." + getAssetType().toLowerCase()
						+ "_s.preview.filenotfound");
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			errors.reject("error." + getAssetType().toLowerCase()
					+ "_s.preview.filenotfound");

		} finally {
			ftpClient.disconnect();
		}

		//ModelAndView modelAndView = new ModelAndView(manageUserStatusTemplate,CONTEXT, initializePageContext(form, isShowAll(request)));
		
		HashMap context = initializePageContext(form, isShowAll(request));
		context.put("sortDirection", 1);
		context.put("sortColumnIndex", 0);
		
		ModelAndView modelAndView = new ModelAndView(manageUserStatusTemplate,CONTEXT, context);
		return modelAndView;
	}

	public ModelAndView viewAffidavit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ManageUserStatusForm form = null;
		try {

			form = (ManageUserStatusForm) command;

			// URL url = AssetUtil.getAssetFilePathURL(form.getAsset());
			String fname = form.getLearnerEnrollmentId() + "_DocuSign.pdf";
			IFtpClient client = new FtpClientHandler(null);
			client.connect();
			File f = client.perfromReadFileTask(fname);

			// File f = new File("D:/AffidavitStore/testPDF.pdf");
			URL url = f.toURI().toURL();
			InputStream is = url.openStream();

			byte[] filedata = IOUtils.toByteArray(is);

			if (filedata != null && filedata.length > 0) {
				form.setFileData(filedata);
				response.reset();
				response.setContentType("application/pdf");
				response.setContentLength(filedata.length);
				// response.setHeader("Content-Disposition",
				// "inline; filename="+((Document)form.getAsset()).getFileName());
				response.setHeader("Content-Disposition", "inline; filename="
						+ f.getName());
				ServletOutputStream outputStream = response.getOutputStream();

				outputStream.write(filedata);
				outputStream.flush();

				outputStream.close();
				is.close();
			} else {
				errors.reject("error." + getAssetType().toLowerCase()
						+ ".preview.filenotfound");
			}
			client.disconnect();

		} catch (Exception e) {
			log.debug("exception", e);
			errors.reject("error." + getAssetType().toLowerCase()
					+ ".preview.filenotfound");
		}

		ModelAndView modelAndView = new ModelAndView(manageUserStatusTemplate,
				CONTEXT, initializePageContext(form, isShowAll(request)));
		return modelAndView;
	}

	public ModelAndView changeUserStatus(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		ManageUserStatusForm form = (ManageUserStatusForm) command;

		if (!errors.hasErrors()) {
			Brander brander = VU360Branding.getInstance()
					.getBrander((String) request.getSession().getAttribute(VU360Branding.BRAND), new Language());
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			userStatusUpdateUtil.updateStatuses(form.getUserstatuses(),
					form.getUpdate_courseStatus(), brander, loggedInUser.getId(),
					form.isReversalPermission());
		}

		HashMap context = initializePageContext(form, isShowAll(request));
		context.put("sortDirection", 1);
		context.put("sortColumnIndex", 0);
		
		ModelAndView modelAndView = new ModelAndView(manageUserStatusTemplate,
				CONTEXT, context);

		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	public ModelAndView searchUserStatus(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		// List<ManageUserStatus> mangeUserStatusList = null;
		ManageUserStatusForm form = (ManageUserStatusForm) command;
		form.setManageUserStatus(new ManageUserStatus());
		form.getManageUserStatus().setFirstName(form.getFirstName());
		form.getManageUserStatus().setLastName(form.getLastName());
		form.getManageUserStatus().setEmailAddress(form.getEmailAddress());
		form.getManageUserStatus().setCourseId(form.getCourseId());
		form.getManageUserStatus().setHoldingRegulatorId(
				form.getHoldingRegulatorId());
		form.getManageUserStatus().setRegulatoryCategoryId(
				form.getRegulatorCategoryId());
		// should be implemented.
		if(form.getRegulatorCategoryId2() !=null &&  form.getRegulatorCategoryId2() > 0)
		{
			form.getManageUserStatus().setRegulatoryCategoryId(form.getRegulatorCategoryId2());	
		}
		form.getManageUserStatus().setCourseTypeId(form.getCourseTypeId());
		form.getManageUserStatus().setCourseStatus(form.getCourseStatus());
		form.getManageUserStatus().setAffidavitType(form.getAffidavitType());
		form.getManageUserStatus().setStartDate(form.getStartDate());
		form.getManageUserStatus().setEndDate(form.getEndDate());

		HashMap context = initializePageContext(form, isShowAll(request));
		ManageUserStatusSort compartor = new ManageUserStatusSort();
		compartor.setSortBy("firstName");
		compartor.setSortDirection(1);
		List<ManageUserStatus> recordList = (List<ManageUserStatus>) context
				.get("userstatuses");
		if (recordList != null && !recordList.isEmpty())
			Collections.sort(recordList, compartor);

		context.put("sortDirection", 1);
		context.put("sortColumnIndex", 0);
        context.put("isAdmin", isAdminMode());
        
        form.setHoldingRegulatorId(Long.parseLong("0"));
        form.setRegulatorCategoryId(Long.parseLong("0"));
        form.setRegulatorCategoryId2(Long.parseLong("0"));

		ModelAndView modelAndView = new ModelAndView(manageUserStatusTemplate,
				CONTEXT, context);

		return modelAndView;
	}

	public ModelAndView uploadAffidavit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		ManageUserStatusForm form = (ManageUserStatusForm) command;

		// let's see if there's content there
		String msg = "Error occured";
		MultipartFile file = form.getAffidavitFile();
		if (file == null || file.isEmpty()) {
			msg = " Affidavit file cannot be empty.";
		}
		if (file != null && file.getSize() > 0
				&& file.getContentType().endsWith("pdf")) {
			log.debug(" Affidavit FILE SIZE - " + file.getSize());
			form.setAffidavitFileName(file.getOriginalFilename());
			byte[] filed = file.getBytes();
			form.setAffidavitFileData(filed);

			File file1 = new File(form.getLearnerEnrollmentId()
					+ "_DocuSign.pdf");
			FileOutputStream fop = new FileOutputStream(file1);
			fop.write(filed);
			fop.flush();
			fop.close();

			IFtpClient client = new FtpClientHandler(null);
			client.connect();
			client.perfromCopyTask(file1);
			client.disconnect();
			msg = "File uploaded successfully (" + form.getAffidavitFileName()
					+ ")";
		}
		if (file != null && file.getSize() > 0
				&& !file.getContentType().endsWith("pdf")) {
			msg = " Affidavit file should be PDF";
		}

		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");
		pw.write(msg);
		pw.flush();
		pw.close();
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap initializePageContext(ManageUserStatusForm form, String showAll)	{
		List<ManageUserStatus> mangeUserStatusList = null;
		
		if(form.isNotEmptyForm())
			mangeUserStatusList = manageUserStatusService.getUserStatusList(form.getManageUserStatus());

		//form.setManageUserStatusList(mangeUserStatusList);
		HashMap context = new HashMap();
		context.put("userstatuses", mangeUserStatusList);

		List<Regulator> holdingRegulators = accreditationService.findAllActiveRegulator();
		context.put("holdingRegulators", holdingRegulators);
		
		if(form.getHoldingRegulatorId() != null && form.getHoldingRegulatorId() > 0)	{		
			List<RegulatorCategory> regulatorCategories = accreditationService.findRegulatorCategories(form.getHoldingRegulatorId());	
			context.put("regulatorCategories", regulatorCategories);
		}
		
		//for rc only search
		List<RegulatorCategory> regulatorCategories2 = accreditationService.findRegulatorCategories(-1);
		context.put("regulatorCategories2", regulatorCategories2);

		context.put("isAdmin", isAdminMode());

		context.put("showAll", showAll);
		
		return context;
	}

	public Boolean isAdminMode(){
		Boolean isAdminMode = null;
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		isAdminMode = user.isAdminMode();
		return isAdminMode;
	}
	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(
			AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public UserStatusUpdateUtil getUserStatusUpdateUtil() {
		return userStatusUpdateUtil;
	}

	public void setUserStatusUpdateUtil(
			UserStatusUpdateUtil userStatusUpdateUtil) {
		this.userStatusUpdateUtil = userStatusUpdateUtil;
	}

	public String getManageUserStatusTemplate() {
		return manageUserStatusTemplate;
	}

	public void setManageUserStatusTemplate(String manageUserStatusTemplate) {
		this.manageUserStatusTemplate = manageUserStatusTemplate;
	}

	public String getManageUserStatusAjaxTemplate() {
		return manageUserStatusAjaxTemplate;
	}

	public void setManageUserStatusAjaxTemplate(
			String manageUserStatusAjaxTemplate) {
		this.manageUserStatusAjaxTemplate = manageUserStatusAjaxTemplate;
	}

	public ManageUserStatusService getManageUserStatusService() {
		return manageUserStatusService;
	}

	public void setManageUserStatusService(
			ManageUserStatusService manageUserStatusService) {
		this.manageUserStatusService = manageUserStatusService;
	}

	private static String getRequestParam(String paramName,
			HttpServletRequest request) {
		String paramValue = request.getParameter(paramName);
		if (StringUtils.isNotBlank(paramValue)
				&& StringUtils.isNotEmpty(paramValue))
			return paramValue;
		return null;
	}

	/**
	 * 
	 * @param brander
	 * @param mangeUserStatusList
	 * @return
	 * @throws IOException
	 *             source:http://opencsv.sourceforge.net/apidocs/overview-
	 *             summary.html
	 */
	private ByteArrayOutputStream getManageUserStatusCSV(Brander brander,
			List<ManageUserStatus> mangeUserStatusList) throws IOException {

		StringBuffer csvHeading = new StringBuffer();
		char csv_separator = ',';
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		CSVWriter writer = new CSVWriter(
				new OutputStreamWriter(byteArrayStream), csv_separator);

		csvHeading
				.append(brander
						.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.firstName")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.lastName")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.emailAddress")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.phoneNumber")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.courseName")
						+ ", "
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.courseId")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.courseStatus")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.courseType")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.completeDate")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.enrollmentDate")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.firstAccessDate")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.holdingRegulatorName")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.regulatoryCategory")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.affidavitType")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.lastUserStatusChange")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.lastUserStatusChangeDate")
						+ csv_separator
						+
						// brander.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.lastUserAffidavitUpload")
						// + CSV_SAPRATOR +
						// brander.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.lastUserAffidavitUploadDate")
						// + CSV_SAPRATOR +
						brander.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.address1")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.city")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.state")
						+ csv_separator
						+ brander
								.getBrandElement("lms.accraditation.ReportingTool.manageUserStatus.gridheader.zipCode")
						+ "\r\n");

		String[] headingEntries = csvHeading.toString().split(
				csv_separator + "");
		writer.writeNext(headingEntries);
		String[] values = null;

		for (ManageUserStatus manageUserStatus : mangeUserStatusList) {
			values = new String[20];
			values[0] = nullCheck(manageUserStatus.getFirstName());
			values[1] = (nullCheck(manageUserStatus.getLastName()));
			values[2] = (nullCheck(manageUserStatus.getEmailAddress()));
			values[3] = (nullCheck(manageUserStatus.getPhoneNumber()));
			values[4] = (nullCheck(manageUserStatus.getCourseName()));
			values[5] = (nullCheck(manageUserStatus.getCourseId()));
			values[6] = (nullCheck(manageUserStatus.getCourseStatus()));
			values[7] = (nullCheck(manageUserStatus.getCourseType()));
			values[8] = (nullCheck(manageUserStatus.getCompleteDate()));
			values[9] = (nullCheck(manageUserStatus.getFirstAccessDate()));
			values[10] = (nullCheck(manageUserStatus.getEnrollmentDate()));
			values[11] = (nullCheck(manageUserStatus.getHoldingRegulatorName()));
			values[12] = (nullCheck(manageUserStatus.getRegulatoryCategory()));
			values[13] = (nullCheck(manageUserStatus.getAffidavitType()));
			values[14] = (nullCheck(manageUserStatus.getLastUserStatusChange()));
			values[15] = (nullCheck(manageUserStatus
					.getLastUserStatusChangeDate()));
			// values[0] = (nullCheck(
			// manageUserStatus.getLastUserAffidavitUpload().toString().replace(',',
			// '.') )+ CSV_SAPRATOR +
			// values[0] = (nullCheck(
			// manageUserStatus.getLastUserAffidavitUploadDate() )+ CSV_SAPRATOR
			// +
			values[16] = (nullCheck(manageUserStatus.getAddress1()));
			values[17] = (nullCheck(manageUserStatus.getCity()));
			values[18] = (nullCheck(manageUserStatus.getState()));
			values[19] = (nullCheck(manageUserStatus.getZipCode()));
			writer.writeNext(values);

		}

		writer.close();

		return byteArrayStream;
	}

	/*
	 * AJAX Methods
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ModelAndView getRegulatorCategoriesByHolder(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {

		ManageUserStatusForm form = (ManageUserStatusForm) command;
		long regulatorId = form.getHoldingRegulatorId();
		List<RegulatorCategory> regulatorCategories = accreditationService
				.findRegulatorCategories(regulatorId);

		HashMap context = new HashMap();
		
		if(regulatorId >0)
		{
			context.put("regulatorCategories", regulatorCategories);
		}
		else
		{
			context.put("regulatorCategories2", regulatorCategories);
		}

		return new ModelAndView(getManageUserStatusAjaxTemplate(), CONTEXT,
				context);
	}

	private static String isShowAll(HttpServletRequest request) {

		String sa = request.getParameter("showAll");
		if (StringUtils.isEmpty(sa) || sa.equalsIgnoreCase("false")) {
			return "false";
		} else {
			return "true";
		}
	}

	private static String nullCheck(String txt) {

		if (txt == null) {
			return StringUtils.EMPTY;
		}
		return txt;
	}

}
