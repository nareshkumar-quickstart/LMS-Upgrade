package com.softech.vu360.lms.web.controller;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.web.controller.model.FileUploadBean;

public class FileUploadController extends SimpleFormController {

	protected ModelAndView onSubmit(
			HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			BindException errors) throws ServletException, Exception {

		// cast the bean
		FileUploadBean bean = (FileUploadBean) command;

        
       MultipartFile file = bean.getFile();

		if (file == null) {
			// hmm, that's strange, the user did not upload anything
		}

		// well, let's do nothing with the bean for now and return
		return super.onSubmit(request, response, command, errors);
	}

	

}
