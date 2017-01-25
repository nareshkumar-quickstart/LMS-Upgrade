package com.softech.vu360.lms.web.controller.model.accreditation;

import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Dyutiman
 * created on 26-june-2009
 *
 */
public class RegulatorDocument  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private Document document = null;
	private boolean selected = false;
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}