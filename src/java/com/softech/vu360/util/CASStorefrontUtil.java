package com.softech.vu360.util;

import com.softech.vu360.lms.vo.VU360User;

public class CASStorefrontUtil {

	protected static final String STOREFRONT_MEGASITE_DASHBOARD_URL = VU360Properties.getVU360Property("lms.storefront.megasite.ls360dashboard.url");
	protected static final String STOREFRONT_VAR_DASHBOARD_URL = VU360Properties.getVU360Property("lms.storefront.var.ls360dashboard.url");
	protected static final String STOREFRONT_MEGASITE_LOGOUT_URL = VU360Properties.getVU360Property("lms.cas.logout.megasite.success.url");
	protected static final String STOREFRONT_VAR_LOGOUT_URL = VU360Properties.getVU360Property("lms.cas.logout.var.success.url");
	protected static final String STOREFRONT_MEGASITE_LOGIN_URL = VU360Properties.getVU360Property("lms.storefront.megasite.login.url");
	protected static final String STOREFRONT_VAR_LOGIN_URL = VU360Properties.getVU360Property("lms.storefront.var.login.url");
	protected static final String DISTRUBTOR_CODE_MEGASITE = VU360Properties.getVU360Property("lms.distributor.code.megasite");
	
	public synchronized static String getLogInUrl(VU360User user) {
		return appendDistributorCode(user, isMegaSiteUser(user) ? STOREFRONT_MEGASITE_LOGIN_URL : STOREFRONT_VAR_LOGIN_URL);
	}

	
	public synchronized static String getLogOutUrl(VU360User user) {
		return appendDistributorCode(user, isMegaSiteUser(user) ? STOREFRONT_MEGASITE_LOGOUT_URL : STOREFRONT_VAR_LOGOUT_URL);
	}
	
	public synchronized static String getLS360DashboardUrl(VU360User user) {
		return appendDistributorCode(user, isMegaSiteUser(user) ? STOREFRONT_MEGASITE_DASHBOARD_URL : STOREFRONT_VAR_DASHBOARD_URL);
	}

	/**
	 * @param user
	 * @param url
	 * @return
	 */
	private static String appendDistributorCode(VU360User user,
			String url) {
		String distributorCode = getDistributorCode(user);
		if(!url.contains(DISTRUBTOR_CODE_MEGASITE) && distributorCode != null) {
			url = url.replace("xxxx", getDistributorCode(user));
		}
		return url;
	}


	/**
	 * @param VU360User
	 * @return boolean
	 */
	private static boolean isMegaSiteUser(VU360User user) {
		return DISTRUBTOR_CODE_MEGASITE.equals(getDistributorCode(user));
	}


	/**
	 * @param user
	 * @return
	 */
	private static String getDistributorCode(VU360User user) {
		String distributorCode = null;
		if(user != null
				&& user.getLearner() != null
				&& user.getLearner().getCustomer() != null
				&& user.getLearner().getCustomer().getDistributor() != null
				&& user.getLearner().getCustomer().getDistributor().getDistributorCode() != null
				&& !user.getLearner().getCustomer().getDistributor().getDistributorCode().isEmpty()
				) {
			distributorCode = user.getLearner().getCustomer().getDistributor().getDistributorCode();
		}
		return distributorCode;
	}
}
