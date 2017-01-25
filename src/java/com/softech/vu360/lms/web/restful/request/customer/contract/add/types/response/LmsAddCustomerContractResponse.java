package com.softech.vu360.lms.web.restful.request.customer.contract.add.types.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.softech.vu360.lms.web.restful.request.AbstractResponse;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "addCustomerContractResponse"
})
@XmlRootElement(name = "json")
public class LmsAddCustomerContractResponse extends AbstractResponse {

	@XmlElement(name = "AddCustomerContractResponse")
	private AddCustomerContractResponse addCustomerContractResponse;

	public AddCustomerContractResponse getAddCustomerContractResponse() {
		return addCustomerContractResponse;
	}

	public void setAddCustomerContractResponse(AddCustomerContractResponse addCustomerContractResponse) {
		this.addCustomerContractResponse = addCustomerContractResponse;
	}
	
}
