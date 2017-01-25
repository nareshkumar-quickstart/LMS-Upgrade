package com.softech.vu360.lms.web.controller.comparator;

import java.util.Comparator;

import com.softech.vu360.lms.model.CommissionableParty;

public class CommissionablePartyComparator implements
		Comparator<CommissionableParty> {
	private int sortDirection=0;
	private int sortColumnIndex=0;
	
	private CommissionablePartyComparator(){}
	
	public CommissionablePartyComparator(int sortDirection,int sortColumnIndex){
		this.sortDirection=sortDirection;
		this.sortColumnIndex= sortColumnIndex;
	}
	
	@Override
	public int compare(CommissionableParty o1, CommissionableParty o2) {
		return this.sortDirection==0 && o1.getName().compareTo(o2.getName())>=0?1:0;
		
	}

}
