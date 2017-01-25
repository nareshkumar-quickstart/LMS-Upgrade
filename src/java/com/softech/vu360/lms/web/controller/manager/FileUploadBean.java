package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.SearchableKey;

public class FileUploadBean implements SearchableKey {

    private String file;
    private List<CustomField> fileHeader = new ArrayList<CustomField>();
    private Map<Integer,CustomField> cfHash = new HashMap<Integer,CustomField>();
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

	public List<CustomField> getFileHeader() {
		return fileHeader;
	}

	public void setFileHeader(List<CustomField> fileHeader) {
		this.fileHeader = fileHeader;
	}

	public Map<Integer, CustomField> getCfHash() {
		return cfHash;
	}

	public void setCfHash(Map<Integer, CustomField> cfHash) {
		this.cfHash = cfHash;
	}

    @Override
    public String getKey() {
        return this.id.toString();
    }
 
}