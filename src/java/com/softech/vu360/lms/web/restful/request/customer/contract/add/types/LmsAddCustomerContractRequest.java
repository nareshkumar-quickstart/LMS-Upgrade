package com.softech.vu360.lms.web.restful.request.customer.contract.add.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.softech.vu360.lms.web.restful.request.AbstractRequest;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "addCustomerContractRequest"
})
@XmlRootElement(name="json")
public class LmsAddCustomerContractRequest extends AbstractRequest {

	@XmlElement(name = "AddCustomerContractRequest", required = true)
	private AddCustomerContractRequest addCustomerContractRequest;

	public AddCustomerContractRequest getAddCustomerContractRequest() {
		return addCustomerContractRequest;
	}

	public void setAddCustomerContractRequest(AddCustomerContractRequest addCustomerContractRequest) {
		this.addCustomerContractRequest = addCustomerContractRequest;
	}

	@Override
	public String toString() {
		return "LmsAddCustomerContractRequest [addCustomerContractRequest="
				+ addCustomerContractRequest + "]";
	}
		
}
