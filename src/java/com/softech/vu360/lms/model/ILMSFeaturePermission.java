package com.softech.vu360.lms.model;

/**
 * Top level contract for LMSFeature permissions.
 * @author sm.humayun
 * @since 4.13 {LMS-8108}
 */
public interface ILMSFeaturePermission extends SearchableKey {

	/**
	 * Return id
	 * @return id
	 */
	Long getId();

	/**
	 * Set id
	 * @param id
	 */
	void setId(Long id);

	/**
	 * Return enabled
	 * @return enabled
	 */
	Boolean getEnabled();

	/**
	 * Set enabled
	 * @param enabled
	 */
	void setEnabled(Boolean enabled);

	/**
	 * Return lmsFeature
	 * @return lmsFeature
	 * @see {@link LMSFeature)
	 */
	LMSFeature getLmsFeature();

	/**
	 * Set lmsFeature
	 * @param lmsFeature
	 * @see {@link LMSFeature)
	 */
	void setLmsFeature(LMSFeature lmsFeature);

	/**
	 * Return locked
	 * @return locked
	 */
	public  Boolean isLocked();

	/**
	 * Set locked
	 * @param isLocked
	 */
	public void setLocked(Boolean isLocked);

}
