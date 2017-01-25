package com.softech.vu360.lms.web.controller.model.survey;

import java.io.Serializable;

public class SurveyResultAnswerFile implements Serializable {

	private static final long serialVersionUID = 7245716463145001543L;

	private Long id;
	private String fileName;
	private String filePath;
	
	public SurveyResultAnswerFile() {
	}
	
	public SurveyResultAnswerFile(com.softech.vu360.lms.model.SurveyResultAnswerFile answerFile) {
		id = answerFile.getId();
		fileName = answerFile.getFileName();
		filePath = answerFile.getFilePath();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
