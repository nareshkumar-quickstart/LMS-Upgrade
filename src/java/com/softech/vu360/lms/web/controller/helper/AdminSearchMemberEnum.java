/**
 * 
 */
package com.softech.vu360.lms.web.controller.helper;

/**
 * @author marium.saud
 *
 */
public enum AdminSearchMemberEnum {

	CUSTOMER{

		@Override
		public String getSortBy(String sortColumnIndex) {
			
			String sortBy="";
			
			if(sortColumnIndex.equalsIgnoreCase("0")){
				sortBy="customerName";
			}
			else if(sortColumnIndex.equalsIgnoreCase("2")){
				sortBy="customerEmail";
			}
			else if(sortColumnIndex.equalsIgnoreCase("3")){
				sortBy="customerStatus";
			}
			else if(sortColumnIndex.equalsIgnoreCase("4")){
				sortBy="originalContractCreationDate";
			}
			else if(sortColumnIndex.equalsIgnoreCase("5")){
				sortBy="recentContractCreationDate";
			}
			return sortBy;
		}},
	RESELLER{

		@Override
		public String getSortBy(String sortColumnIndex) {
			
			String sortBy="";
			
			if(sortColumnIndex.equalsIgnoreCase("0")){
				sortBy="name";
			}
			else if(sortColumnIndex.equalsIgnoreCase("2")){
				sortBy="distributorEmail";
			}
			else if(sortColumnIndex.equalsIgnoreCase("3")){
				sortBy="active";
			}

			return sortBy;
		}},
	LEARNER{
		
		@Override
		public String getSortBy(String sortColumnIndex) {
			
			String sortBy="";
			
			if(sortColumnIndex.equalsIgnoreCase("0")){
				if(CONSTRAINT_LEARNER_SEARCH){
					sortBy="firstName";
				}else{
					sortBy="lastName";
				}
			}else if(sortColumnIndex.equalsIgnoreCase("2")){
				sortBy="emailAddress";
			}else if(sortColumnIndex.equalsIgnoreCase("3")){
				sortBy="accountNonLocked";
			}else if(sortColumnIndex.equalsIgnoreCase("4")){
				sortBy="Name";
			}else if(sortColumnIndex.equalsIgnoreCase("5")){
				sortBy="Name";
			}
			return sortBy;
		}};
	
	//The method will receive sortColumnIndex and return respective sortBy depending 'Type of Search' Selected
	public abstract String getSortBy(String sortColumnIndex);
	
	//For Learner Search sort By is different for SortColumnIndex=0;depending on Boolean 'constrainedLearnerSearch'
	public Boolean CONSTRAINT_LEARNER_SEARCH = false;

	/**
	 * @return the cONSTRAINT_LEARNER_SEARCH
	 */
	public Boolean getCONSTRAINT_LEARNER_SEARCH() {
		return CONSTRAINT_LEARNER_SEARCH;
	}

	/**
	 * @param cONSTRAINT_LEARNER_SEARCH the cONSTRAINT_LEARNER_SEARCH to set
	 */
	public void setCONSTRAINT_LEARNER_SEARCH(Boolean CONSTRAINT_LEARNER_SEARCH) {
		this.CONSTRAINT_LEARNER_SEARCH = CONSTRAINT_LEARNER_SEARCH;
	} 
	
	
	
}
