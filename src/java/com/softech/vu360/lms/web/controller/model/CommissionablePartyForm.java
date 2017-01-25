package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CommissionablePartyForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String name;
    private Distributor distributor;
	private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return String.valueOf(id);
    }
}
