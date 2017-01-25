package com.softech.vu360.lms.web.controller.model;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class FileUploadBean  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private MultipartFile file;

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

}