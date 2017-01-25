package com.softech.vu360.lms.web.controller.accreditation;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.DocumentForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.CertificateValidator;
import com.softech.vu360.util.CertificateSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.StoreDocument;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Dyutian
 * created on 2-july-2009
 */
public class ManageAndEditCertificateController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageAndEditCertificateController.class.getName());

	private AccreditationService accreditationService;
//	HttpSession session = null;

	private String searchCertificateTemplate = null;
	private String editCertificateTemplate = null;

	public ManageAndEditCertificateController() {
		super();
	}

	public ManageAndEditCertificateController(Object delegate) {
		super(delegate);
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		request.setAttribute("newPage","true");
		log.debug(" IN handleNoSuch Method ");
		return new ModelAndView(searchCertificateTemplate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof DocumentForm ){
			DocumentForm form = (DocumentForm)command;
			if( methodName.equals("editCertificate") ) {
				Certificate certificate = null;
				String id = request.getParameter("id");
				if( id != null ) {
					certificate = accreditationService.getCertificateById(Long.parseLong(id));
					form.setDocument(certificate);
				} else {
					certificate = (Certificate) form.getDocument();
				}
			} else if( methodName.equals("saveCertificate") || methodName.equals("previewCertificate") ) {
				if( request.getParameter("id") != null ) {
					String CertId = request.getParameter("id");
					Certificate certificate = accreditationService.loadForUpdateCertificate(Long.parseLong(CertId));
					form.setDocument(certificate);
				}
				MultipartFile file = form.getFile();
				if ( file == null || file.isEmpty() ) {
					log.debug(" file is null ");
				} else {
					byte[] filedata = file.getBytes();
					form.setFileData(filedata);
				}
			}
		}	
	}

	public ModelAndView searchCertificate( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
//			session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			Map<String, String> PagerAttributeMap = new HashMap <String, String>();
			//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			String name = (request.getParameter("name") == null) ? "" : request.getParameter("name");

			List<Certificate> certificates = accreditationService.getCertficatesByName(name);
			
			name = HtmlEncoder.escapeHtmlFull(name).toString();
			context.put("name", name);
			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			context.put("showAll", showAll);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CertificateSort certificateSort = new CertificateSort();
						certificateSort.setSortBy("name");
						certificateSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(certificates,certificateSort);
						context.put("sortDirection", 0);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						CertificateSort certificateSort = new CertificateSort();
						certificateSort.setSortBy("name");
						certificateSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(certificates,certificateSort);
						context.put("sortDirection", 1);
						context.put("sortColumnIndex", 0);
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
				} 
			}
			context.put("certificates", certificates);
			return new ModelAndView(searchCertificateTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchCertificateTemplate);
	}

	public ModelAndView editCertificate( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			// do nothing
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(editCertificateTemplate);
	}

	public ModelAndView deleteCertificate( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {

		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			String name = (request.getParameter("name") == null) ? "" : request.getParameter("name");

			if( request.getParameterValues("certs") != null ) {
				StoreDocument store = new StoreDocument();
				long[] id = new long[request.getParameterValues("certs").length];
				String []checkID = request.getParameterValues("certs");
				for ( int i = 0 ; i < id.length ; i++ ) {
					id[i] = Long.valueOf(checkID[i]);
					Certificate cert = accreditationService.loadForUpdateCertificate(id[i]);
					store.delete( cert.getFileName() );
					cert.setActive(false);
					accreditationService.saveCertificate(cert);
				}
				log.debug("TO BE DELETED ::- "+id.length);
			}
			List<Certificate> certificates = accreditationService.getCertficatesByName(name);
			context.put("certificates", certificates);
			return new ModelAndView(searchCertificateTemplate, "context", context);
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchCertificateTemplate);
	}

	public ModelAndView saveCertificate( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			if( errors.hasErrors() ) {
				return new ModelAndView(editCertificateTemplate);
			}
			DocumentForm form = (DocumentForm) command;
			Certificate certificate = accreditationService.loadForUpdateCertificate(form.getDocument().getId());
			certificate.setName(form.getDocument().getName().trim());
			MultipartFile file = form.getFile();
			if ( file == null || file.isEmpty() ) {
				log.debug(" file is null ");
			} else {
				certificate.setFileName(file.getOriginalFilename());
				log.debug(" DELETING OLD FILE ");
				StoreDocument store = new StoreDocument();
				store.delete( certificate.getFileName() );
				store.createFile( file.getOriginalFilename(), form.getFileData());
			}
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());
			certificate.setContentowner(contentOwner);
			accreditationService.saveCertificate(certificate);
			form.setFile(null);
			return new ModelAndView(searchCertificateTemplate);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(searchCertificateTemplate);
	}

	public ModelAndView previewCertificate( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		try {
			
			DocumentForm form = (DocumentForm) command;
			String documentPath = VU360Properties.getVU360Property("document.saveLocation");
			File file = new File(documentPath + form.getDocument().getFileName());
			FileInputStream fis = new FileInputStream(file);
			byte[] filedata = null;

			if( form.getFileData() != null ) {
				filedata = form.getFileData();
			} else {
				filedata = new byte[(int)file.length()];
				// Read in the bytes
				int offset = 0;
				int numRead = 0;
				while (offset < filedata.length
						&& (numRead = fis.read(filedata, offset, filedata.length-offset)) >= 0) {
					offset += numRead;
				}
				/*String nextLine;
				// getting file info in bites
				while( ( nextLine = is.readLine() ) != null ) {
					//nextLine = nextLine + "\n";
					filedata = nextLine.getBytes();
					allBytes.add(filedata);
				}*/
			}
			fis.read();
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength((int)file.length());
			response.setHeader("Content-Disposition", "inline; filename="+form.getDocument().getFileName());
			ServletOutputStream outputStream = response.getOutputStream();

			if( form.getFileData() != null ) {
				outputStream.write(form.getFileData());
				outputStream.flush();
			} else {
				//for( byte[] bite : allBytes ) {
				outputStream.write(filedata);
				outputStream.flush();
				//}
			}
			outputStream.close();

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return null;//new ModelAndView(editCertificateTemplate);
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {

		CertificateValidator validator = (CertificateValidator)this.getValidator();
		DocumentForm form = (DocumentForm)command;
		if( methodName.equals("saveCertificate")) {
			validator.validateEditPage(form, errors);
		}
	}

	public String getSearchCertificateTemplate() {
		return searchCertificateTemplate;
	}

	public void setSearchCertificateTemplate(String searchCertificateTemplate) {
		this.searchCertificateTemplate = searchCertificateTemplate;
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getEditCertificateTemplate() {
		return editCertificateTemplate;
	}

	public void setEditCertificateTemplate(String editCertificateTemplate) {
		this.editCertificateTemplate = editCertificateTemplate;
	}
}