package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.DistributorPreferences;
import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.LMSAdministratorAllowedDistributor;
import com.softech.vu360.lms.model.LMSFeature;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.AddressRepository;
import com.softech.vu360.lms.repositories.DistributorGroupRepository;
import com.softech.vu360.lms.repositories.DistributorLMSFeatureRepository;
import com.softech.vu360.lms.repositories.DistributorPreferencesRepository;
import com.softech.vu360.lms.repositories.DistributorRepository;
import com.softech.vu360.lms.repositories.LMSAdministratorAllowedDistributorRepository;
import com.softech.vu360.lms.repositories.LMSAdministratorRepository;
import com.softech.vu360.lms.repositories.LMSFeatureRepository;
import com.softech.vu360.lms.repositories.RepositorySpecificationsBuilder;
import com.softech.vu360.lms.repositories.VU360UserRepository;
import com.softech.vu360.lms.repositories.VU360UserRepositoryImpl;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.BrandService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.web.controller.model.AddCustomerForm;
import com.softech.vu360.lms.web.controller.model.AddDistributorForm;
import com.softech.vu360.lms.web.controller.model.AddDistributorGroups;
import com.softech.vu360.lms.web.controller.model.EditDistributorForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Raja Wajahat Ali
 *
 */
public class DistributorServiceImpl implements DistributorService {

	private static final Logger log = Logger.getLogger(DistributorServiceImpl.class
			.getName());
	@Inject
	AddressRepository addressRepository;
	@Inject
	private DistributorPreferencesRepository distributorPreferencesRepository;
	@Inject
	private DistributorGroupRepository distributorGroupRepository;
	@Inject
	private DistributorRepository distributorRepository;
	@Inject
	private LMSAdministratorRepository lmsAdministratorRepository;
	@Inject
	private DistributorLMSFeatureRepository distributorLMSFeatureRepository;
	@Inject
	private LMSFeatureRepository lMSFeatureRepository;
	@Inject
	private VU360UserRepository vU360UserRepository;
	@Inject
	private LMSAdministratorAllowedDistributorRepository lMSAdministratorAllowedDistributorRepository;

	private CustomerService customerService = null;
	private AuthorService authorService = null;
	private BrandService brandService = null;

	public List<Distributor> findDistributorsByName(String name,
			VU360User loggedInUser, boolean isExact, int pageIndex,
			int retrieveRowCount, ResultSet resultSet, String sortBy,
			int sortDirection) {

		Collection<String> distIds = getAdminRestrictionExpression(loggedInUser);

		List<Distributor> distributorList = new ArrayList<Distributor>();

		RepositorySpecificationsBuilder<Distributor> specificationBuilder = new RepositorySpecificationsBuilder<Distributor>();
		if (isExact) {
			specificationBuilder.with("name", specificationBuilder.EQUALS_TO,
					name, "AND");
		} else {
			specificationBuilder.with("name",
					specificationBuilder.LIKE_IGNORE_CASE, name, "AND");
		}

		if (distIds != null && distIds.size() > 0) {
			specificationBuilder.with("id", specificationBuilder.IN,
					distIds.toArray(), "AND");
		}
		
		Sort sortSpec = null;
		if (StringUtils.isBlank(sortBy)) {
			sortSpec = orderBy(0, "name");
		} else {
			sortSpec = orderBy(sortDirection, sortBy);
		}

		PageRequest pageRequest = null;
		if (retrieveRowCount != -1 && pageIndex >= 0) {
			pageRequest = new PageRequest(pageIndex / retrieveRowCount,
					retrieveRowCount, sortSpec);
			Page<Distributor> page = distributorRepository.findAll(
					specificationBuilder.build(), pageRequest);
			resultSet.total = ((int) (long) page.getTotalElements());
			distributorList = page.getContent();
		} else {
			distributorList = distributorRepository.findAll(
					specificationBuilder.build(), sortSpec);
			resultSet.total = distributorList.size();
		}
		return distributorList;
	}

	public List<LMSAdministrator> findLMSAdministrators(String firstName,
			String lastName, String email, VU360User loggedInUser) {
		return lmsAdministratorRepository.findLMSAdministrators(firstName,
				lastName, email, loggedInUser);
	}

	@Deprecated
	// not usage found.
	public List<LMSAdministrator> findLMSAdministrators(String searchCriteria,
			VU360User loggedInUser) {

		// return
		// lmsAdministratorRepository.findLMSAdministrators(searchCriteria,
		// loggedInUser);
		return null;
	}

	public void assignLMSAdministratorsToDistributorGroup(
			Long[] lmsAdministratorIdArray, Long distributorGroupId,
			String[] allowResellers) {

		List<LMSAdministratorAllowedDistributor> allowedResellerOfSelectedGroup = new ArrayList<LMSAdministratorAllowedDistributor>();

		List<LMSAdministrator> lmsadmin = lmsAdministratorRepository
				.findByIdIn(Arrays.asList(lmsAdministratorIdArray));
		DistributorGroup distributorGroup = distributorGroupRepository
				.findById(distributorGroupId);

		for (Iterator<LMSAdministrator> iterator = lmsadmin.iterator(); iterator
				.hasNext();) {
			LMSAdministrator lmsAdministrator = (LMSAdministrator) iterator
					.next();

			// multiple insertion executing because first JPA delete all
			// DistributorGroup and again insert then one by one including given
			// group id.
			// @TODO WE SHOUL OPTIMIZE THIS THIS LOGIC.
			lmsAdministrator.getDistributorGroups().add(distributorGroup);

			// select Distributor Group's reseller
			List<Distributor> groupResellerList = distributorGroup
					.getDistributors();
			for (int i = 0; i < groupResellerList.size(); i++) {
				for (int j = 0; j < allowResellers.length; j++) {
					if (groupResellerList.get(i).getId() == Long
							.parseLong(allowResellers[j])) {
						LMSAdministratorAllowedDistributor allowedDistributor = new LMSAdministratorAllowedDistributor();
						allowedDistributor
								.setAllowedDistributorId(groupResellerList.get(
										i).getId());
						allowedDistributor
								.setDistributorGroupId(distributorGroup.getId());
						allowedDistributor
								.setLmsAdministratorId(lmsAdministrator.getId());
						allowedResellerOfSelectedGroup.add(allowedDistributor);
					}
				}

			}
		}
		lmsAdministratorRepository
				.assignLMSAdministratorsToDistributorGroup(allowedResellerOfSelectedGroup);
	}

	@Transactional
	public List<LMSAdministrator> unassignLMSAdministratorsToDistributorGroup(
			Long[] lmsAdminIds, Long distributorGroupId) {

		List<LMSAdministrator> lmsAdminList = lmsAdministratorRepository
				.findByIdIn(Arrays.asList(lmsAdminIds));
		DistributorGroup distributorGroup = distributorGroupRepository
				.findById(distributorGroupId);

		for (Iterator<LMSAdministrator> iterator = lmsAdminList.iterator(); iterator
				.hasNext();) {
			LMSAdministrator lmsAdministrator = (LMSAdministrator) iterator
					.next();
			lmsAdministrator.getDistributorGroups().remove(distributorGroup);
		}

		// delete from LMSADMINISTRATOR_ALLOWED_DISTRIBUTOR table
		if (lmsAdminIds != null && lmsAdminIds.length > 0) {
			for (int i = 0; i < lmsAdminIds.length; i++) {
				Long adminId = lmsAdminIds[i];
				if (adminId != null && adminId > 0) {
					lMSAdministratorAllowedDistributorRepository
							.deleteByDistributorGroupIdAndLmsAdministratorId(
									distributorGroupId, adminId);
				}
			}
		}

		return lmsAdminList;
	}

	public List<LMSAdministrator> getLMSAdministratorsByDistributorGroupId(
			long distributorGroupId) {

		/*List<LMSAdministrator> lmsadmin = (List<LMSAdministrator>) lmsAdministratorRepository
				.findByIdIn(Arrays.asList(distributorGroupId));*/
		List<LMSAdministrator> lmsadmin = (List<LMSAdministrator>) lmsAdministratorRepository
				.findByDistributorGroups_IdIn(Arrays.asList(distributorGroupId));
		
		return lmsadmin;

	}

	public DistributorGroup addDistributorGroup(
			DistributorGroup distributorGroup) {
		return distributorGroupRepository.save(distributorGroup);
	}

	public DistributorGroup getDistributorGroupById(long distributorGroupId) {

		return distributorGroupRepository.findById(distributorGroupId);
	}

	public Distributor getDistributorById(long distributorId) {
		return distributorRepository.findOne(distributorId);
	}

	public DistributorGroup saveDistributorGroup(
			DistributorGroup distributorGroup) {

		return distributorGroupRepository.save(distributorGroup);
	}

	public Distributor findDistibutorByDistributorCode(String distributorCode) {
		return distributorRepository.findByDistributorCode(distributorCode);
	}

	private Sort orderBy(int sortDirection, String sortBy) {
		Sort sort = null;
		if (sortDirection != -1) {
			if (sortDirection == 0) {
				sort = new Sort(Sort.Direction.ASC, sortBy);
			} else {
				sort = new Sort(Sort.Direction.DESC, sortBy);
			}
		}
		return sort;
	}

	public Map<Object, Object> findDistributorsByName(String distributorName,
			VU360User loggedInUser, int pageIndex, int pageSize, String sortBy,
			int sortDirection) {

		Collection<String> distIds = getAdminRestrictionExpression(loggedInUser);

		List<Distributor> distributorList = new ArrayList<Distributor>();

		RepositorySpecificationsBuilder<Distributor> specificationBuilder = new RepositorySpecificationsBuilder<Distributor>();
		specificationBuilder.with("name", ":IC", distributorName, "AND");
		if (distIds != null && distIds.size() > 0) {
			specificationBuilder.with("id", specificationBuilder.IN,
					distIds.toArray(), "AND");
		}

		int recordSize = 0;
		Sort sortSpec = null;
		if (sortDirection != -1) {
			sortSpec = orderBy(sortDirection, sortBy);
		}

		PageRequest pageRequest = null;
		if (pageSize != -1 && pageIndex != -1) {
			pageRequest = new PageRequest(pageIndex, pageSize, sortSpec);
			Page<Distributor> page = distributorRepository.findAll(
					specificationBuilder.build(), pageRequest);
			recordSize = recordSize + ((int) (long) page.getTotalElements());
			distributorList = page.getContent();
		} else {
			distributorList = distributorRepository.findAll(
					specificationBuilder.build(), sortSpec);
			recordSize = recordSize + distributorList.size();
			// for (Distributor dist : distributorlist)
			// distributorList.add(dist);
		}

		Map<Object, Object> results = new HashMap<Object, Object>();
		results.put("recordSize", recordSize);
		results.put("list", distributorList);

		return results;
	}

	private Collection<String> getAdminRestrictionExpression(
			VU360User loggedInUser) {

		List<Distributor> distributorList = new ArrayList<Distributor>();
		Collection<String> distIds=new ArrayList<String>();

		// Highly optimized
		if (loggedInUser.getLmsAdministrator() != null
				&& !loggedInUser.getLmsAdministrator().isGlobalAdministrator()) {

			List<DistributorGroup> ls = loggedInUser.getLmsAdministrator()
					.getDistributorGroups();
			Set<DistributorGroup> uniqueDistributorGroups = null;

			if(ls!=null){
				uniqueDistributorGroups = new HashSet<DistributorGroup>(ls);
			}
			else{
				uniqueDistributorGroups = new HashSet<DistributorGroup>();
			}

			// get id from collection.
			Collection<Long> dgIds = Collections2.transform(
					uniqueDistributorGroups,
					new Function<DistributorGroup, Long>() {
						public Long apply(DistributorGroup arg0) {
							return Long.parseLong(arg0.getId().toString());
						}
					});

			List<LMSAdministratorAllowedDistributor> allowedDistributors = lMSAdministratorAllowedDistributorRepository
					.findByDistributorGroupIdInAndLmsAdministratorId(dgIds,
							loggedInUser.getLmsAdministrator().getId());

			// get id from collection.
			Collection<Long> ids = Collections2.transform(allowedDistributors,
					new Function<LMSAdministratorAllowedDistributor, Long>() {
						public Long apply(
								LMSAdministratorAllowedDistributor arg0) {
							return Long.parseLong(arg0
									.getAllowedDistributorId().toString());
						}
					});

			if (ids != null && !ids.isEmpty()) {
				Set<Long> uniqueIds = new HashSet<Long>(ids);
				distributorList = (List<Distributor>) distributorRepository
						.findAll(uniqueIds);
				
				// get id from collection.
				distIds = Collections2.transform(distributorList,
						new Function<Distributor, String>() {
							public String apply(Distributor arg0) {
								return String.valueOf(arg0.getId());
							}
						});
			}
			else{
				// If the user is Admin (Not Global Admin) and is not associated with any reseller group then default distributor id will be set to '-1' inorder to prevent user
				// to view all reseller
				distIds = Arrays.asList("-1");
			}
		}
		return distIds;
	}

	public List<Distributor> findResellersWithCustomFields(
			VU360User loggedInUser) {
		// Find All Distributor having Custom Fields.
		return distributorRepository.findResellersWithCustomFields();
	}

	public void deleteDistributorGroups(Long distributorGroupIdArray[]) {

		distributorGroupRepository
				.deleteDistributorGroups(distributorGroupIdArray);
	}

	public void assignDistributorsInDistributorGroup(Long distributorGroupId,
			Long distributorIdArray[]) {

		distributorGroupRepository.assignDistributorsInDistributorGroup(
				distributorGroupId, distributorIdArray);
	}

	public List<Distributor> getAllowedDistributorByGroupId(String groupId,
			String administratorId) {
		return distributorRepository.getAllowedDistributorByGroupId(groupId,
				administratorId);
	}

	public void deleteDistributorByGroupIdAndAdministratorId(String groupId,
			String administratorId, String distributorId) {
		distributorRepository
				.deleteDistributorByGroupIdAndAdministratorIdAndAllowedDistributorId(
						groupId, administratorId, distributorId);
	}

	public void addDistributorWithGroupIdAndAdministratorId(
			LMSAdministratorAllowedDistributor allowedDistributor) {
		lMSAdministratorAllowedDistributorRepository.save(allowedDistributor);
	}

	public List<DistributorGroup> findDistributorGroupsByName(String name,
			com.softech.vu360.lms.vo.VU360User loggedInUser, boolean isExact) {
		List<DistributorGroup> listOfDistGroups = new ArrayList<DistributorGroup>();
		List<DistributorGroup> searchedDistGroups = new ArrayList<DistributorGroup>();
		LMSAdministrator admin = lmsAdministratorRepository.findOne(loggedInUser.getLmsAdministrator().getId());
		List<DistributorGroup> adminDistGroups = admin.getDistributorGroups();
		if(isExact){
			searchedDistGroups = distributorGroupRepository
					.findByNameLikeIgnoreCaseOrderByNameAsc(name);
		}
		else{
			searchedDistGroups = distributorGroupRepository
					.findByNameLikeIgnoreCaseOrderByNameAsc('%'+name+'%');
		}
		
		/*
		 * testing if the searched Reseller Group is among those in which logged
		 * in user is defined as administrator.
		 */
		if (admin.isGlobalAdministrator()) {
			return searchedDistGroups;
		} else {
			if(searchedDistGroups!=null && adminDistGroups!=null){
				for (DistributorGroup searchedDistGrp : searchedDistGroups) {
					for (DistributorGroup adminDistGrp : adminDistGroups) {
						if (searchedDistGrp.getId().equals(adminDistGrp.getId())) {
							listOfDistGroups.add(searchedDistGrp);
						}
					}
				}
			}
			return listOfDistGroups;
		}
	}

	@Transactional
	public Distributor addDistributor(com.softech.vu360.lms.vo.VU360User loggedInUser,AddDistributorForm addDistributorForm) {
		Distributor distributor = new Distributor();
		distributor.setName(addDistributorForm.getDistributorName());
		distributor.setDistributorCode(addDistributorForm.getDistributorCode());
		distributor.setFirstName(addDistributorForm.getFirstName());
		distributor.setLastName(addDistributorForm.getLastName());
		distributor.setWebsiteUrl(addDistributorForm.getWesiteURL());
		distributor.setDistributorEmail(addDistributorForm.getEmailAdd());
		distributor.setOfficePhone(addDistributorForm.getPhone());
		distributor.setOfficePhoneExtn(addDistributorForm.getExt());
		distributor.setActive(addDistributorForm.isAccountStatus());
		Brand brand = brandService.getBrandById(addDistributorForm.getBrandId());
		distributor.setBrand(brand);
		distributor.setBrandName(brand == null ? "" : brand.getBrandKey());
		distributor.setSelfReporting(addDistributorForm.isSelfReporting()); 
		distributor.setCorporateAuthorVar(addDistributorForm.getIsCorporateAuthorVar());
		distributor.setLmsApiEnabledTF(addDistributorForm.getLmsApiEnabledTF());
		distributor.setCallLoggingEnabled(addDistributorForm.getCallLoggingEnabled());
		distributor.setMarkedPrivate(addDistributorForm.isMarkedPrivate());
		distributor.setType(addDistributorForm.getType());

		Address address1 = new Address();
		address1.setStreetAddress(addDistributorForm.getAddress1Line1());
		address1.setStreetAddress2(addDistributorForm.getAddress1Line2());
		address1.setCity(addDistributorForm.getCity1());
		address1.setCountry(addDistributorForm.getCountry1());
		address1.setState(addDistributorForm.getState1());
		address1.setZipcode(addDistributorForm.getZip1());
		distributor.setDistributorAddress(address1);

		Address address2 = new Address();
		address2.setStreetAddress(addDistributorForm.getAddress2Line1());
		address2.setStreetAddress2(addDistributorForm.getAddress2Line2());
		address2.setCity(addDistributorForm.getCity2());
		address2.setCountry(addDistributorForm.getCountry2());
		address2.setState(addDistributorForm.getState2());
		address2.setZipcode(addDistributorForm.getZip2());
		distributor.setDistributorAddress2(address2);

		DistributorPreferences distributorPreferences = new DistributorPreferences();
		distributorPreferences.setAudioEnabled(addDistributorForm.isAudio());
		distributorPreferences.setAudioLocked(addDistributorForm.isAudioLocked());
		distributorPreferences.setVolume(addDistributorForm.getVolume());
		distributorPreferences.setVolumeLocked(addDistributorForm.isVolumeLocked());
		distributorPreferences.setCaptioningEnabled(addDistributorForm.isCaptioning());
		distributorPreferences.setCaptioningLocked(addDistributorForm.isCaptioningLocked());
		if (addDistributorForm.isBandwidth()){
			distributorPreferences.setBandwidth(DistributorPreferences.BANDWIDTH_HIGH);
		}else{
			distributorPreferences.setBandwidth(DistributorPreferences.BANDWIDTH_LOW);
		}
		distributorPreferences.setBandwidthLocked(addDistributorForm.isBandwidthLocked());
		distributorPreferences.setVedioEnabled(addDistributorForm.isVideo());
		distributorPreferences.setVideoLocked(addDistributorForm.isVideoLocked());
		distributorPreferences.setEnableRegistrationEmailsForNewCustomers(addDistributorForm.isRegistrationEmails());
		distributorPreferences.setRegistrationEmailLocked(addDistributorForm.isRegistrationEmailsLocked());
		distributorPreferences.setEnableEnrollmentEmailsForNewCustomers(addDistributorForm.isEnrollmentEmails());
		distributorPreferences.setEnrollmentEmailLocked(addDistributorForm.isEnrollmentEmailsLocked());
		distributorPreferences.setCourseCompletionCertificateEmailEnabled(addDistributorForm.isCourseCompCertificateEmails());
		distributorPreferences.setCourseCompletionCertificateEmailLocked(addDistributorForm.isCourseCompCertificateEmailsLocked());

		distributor.setDistributorPreferences(distributorPreferences);
		distributorPreferences.setDistributor(distributor);

		distributor = distributorRepository.save(distributor);

		// JIRA ID: LMS-3899. Now, we need to enable all the features for this
		// distributor
		// FMK Jan 23 2010
		enableFeaturesForDistributor(distributor); // @TODO OPTIMIZE IT TAKING
													// TIME IN INSERT
													// PERMISSION.

		for (AddDistributorGroups addDistributorGroups : addDistributorForm.getDistributors()) {
			if (addDistributorGroups.isSelected()) {
				DistributorGroup dg = addDistributorGroups.getDistributorGroup();
				DistributorGroup dbDistGroup = distributorGroupRepository.findById(dg.getId());
				dbDistGroup.addDistributor(distributor);
				distributorGroupRepository.save(dbDistGroup);
			}
		}

		// ---- code added for creating reseller/distributor representative customer ---------
		AddCustomerForm customerVO = createCustomerVO(addDistributorForm);
		customerVO.setDefault(true);

		Customer resellerRepresentative = customerService.addCustomer(loggedInUser.getId(), distributor, customerVO);
		distributor.setMyCustomer(resellerRepresentative);

		// ------ Self Authoring Code starts here --------
		if (addDistributorForm.isSelfAuthor()) {
			ContentOwner co = authorService.addContentOwnerIfNotExist(
					resellerRepresentative, loggedInUser.getId());
			distributor.setContentOwner(co);
		}
		// ------ Self Authoring Code ends here --------

		distributor = distributorRepository.save(distributor);
		// --- code ended for creating reseller/distributor representative
		// customer ---------

		return distributor;
	}

	// LMS-15215
	// Because of Continuous changes in CrowdSourcing distributor creation & its
	// logic, I am creating separating function for CrowdSourcing webservice.
	@Transactional
	public Distributor addDistributorForSelfService(Long userId,AddDistributorForm addDistributorForm) {
		Distributor distributor = new Distributor();

		distributor.setName(addDistributorForm.getDistributorName());
		distributor.setFirstName(addDistributorForm.getFirstName());
		distributor.setLastName(addDistributorForm.getLastName());
		distributor.setWebsiteUrl(addDistributorForm.getWesiteURL());
		distributor.setDistributorEmail(addDistributorForm.getEmailAdd());
		
		
		if(StringUtils.isEmpty(addDistributorForm.getDistributorCode())) 
			distributor.setDistributorCode(null);
		else
			distributor.setDistributorCode(addDistributorForm.getDistributorCode());
	
		
		distributor.setDistributorCode(addDistributorForm.getDistributorCode());
		
		distributor.setOfficePhone(addDistributorForm.getPhone());
		distributor.setOfficePhoneExtn(addDistributorForm.getExt());
		distributor.setActive(addDistributorForm.isAccountStatus());
		Brand brand = brandService.getBrandById(addDistributorForm.getBrandId());
		distributor.setBrand(brand);
		distributor.setBrandName(brand == null ? "" : brand.getBrandKey());
		distributor.setSelfReporting(addDistributorForm.isSelfReporting()); 
		distributor.setCorporateAuthorVar(addDistributorForm.getIsCorporateAuthorVar());
		distributor.setCallLoggingEnabled(addDistributorForm.getCallLoggingEnabled());
		distributor.setMarkedPrivate(addDistributorForm.isMarkedPrivate());
		distributor.setType(addDistributorForm.getType());

		Address address1 = new Address();
		address1.setStreetAddress(addDistributorForm.getAddress1Line1());
		address1.setStreetAddress2(addDistributorForm.getAddress1Line2());
		address1.setCity(addDistributorForm.getCity1());
		address1.setCountry(addDistributorForm.getCountry1());
		address1.setState(addDistributorForm.getState1());
		address1.setZipcode(addDistributorForm.getZip1());
		distributor.setDistributorAddress(address1);

		Address address2 = new Address();
		address2.setStreetAddress(addDistributorForm.getAddress2Line1());
		address2.setStreetAddress2(addDistributorForm.getAddress2Line2());
		address2.setCity(addDistributorForm.getCity2());
		address2.setCountry(addDistributorForm.getCountry2());
		address2.setState(addDistributorForm.getState2());
		address2.setZipcode(addDistributorForm.getZip2());
		distributor.setDistributorAddress2(address2);

		DistributorPreferences distributorPreferences = new DistributorPreferences();
		distributorPreferences.setAudioEnabled(addDistributorForm.isAudio());
		distributorPreferences.setAudioLocked(addDistributorForm.isAudioLocked());
		distributorPreferences.setVolume(addDistributorForm.getVolume());
		distributorPreferences.setVolumeLocked(addDistributorForm.isVolumeLocked());
		distributorPreferences.setCaptioningEnabled(addDistributorForm.isCaptioning());
		distributorPreferences.setCaptioningLocked(addDistributorForm.isCaptioningLocked());
		if (addDistributorForm.isBandwidth()){
			distributorPreferences.setBandwidth(DistributorPreferences.BANDWIDTH_HIGH);
		}else{
			distributorPreferences.setBandwidth(DistributorPreferences.BANDWIDTH_LOW);
		}
		distributorPreferences.setBandwidthLocked(addDistributorForm.isBandwidthLocked());
		distributorPreferences.setVedioEnabled(addDistributorForm.isVideo());
		distributorPreferences.setVideoLocked(addDistributorForm.isVideoLocked());
		distributorPreferences.setEnableRegistrationEmailsForNewCustomers(addDistributorForm.isRegistrationEmails());
		distributorPreferences.setRegistrationEmailLocked(addDistributorForm.isRegistrationEmailsLocked());
		distributorPreferences.setEnableEnrollmentEmailsForNewCustomers(addDistributorForm.isEnrollmentEmails());
		distributorPreferences.setEnrollmentEmailLocked(addDistributorForm.isEnrollmentEmailsLocked());
		distributorPreferences.setCourseCompletionCertificateEmailEnabled(addDistributorForm.isCourseCompCertificateEmails());
		distributorPreferences.setCourseCompletionCertificateEmailLocked(addDistributorForm.isCourseCompCertificateEmailsLocked());
		distributor.setDistributorPreferences(distributorPreferences);
		distributorPreferences.setDistributor(distributor);

		distributor = distributorRepository.save(distributor);

		// JIRA ID: LMS-3899. Now, we need to enable all the features for this
		// distributor
		// FMK Jan 23 2010
		enableFeaturesForDistributorForSelfService(distributor);

		for (AddDistributorGroups addDistributorGroups : addDistributorForm.getDistributors()) {
			if (addDistributorGroups.isSelected()) {
				DistributorGroup dg = addDistributorGroups.getDistributorGroup();
				DistributorGroup dbDistGroup = distributorGroupRepository.findById(dg.getId());
				dbDistGroup.addDistributor(distributor);
				distributorGroupRepository.save(dbDistGroup);
			}
		}

		// ---- code added for creating reseller/distributor representative customer ---------
		AddCustomerForm customerVO = createCustomerVO(addDistributorForm);
		customerVO.setDefault(true);

		Customer resellerRepresentative = customerService.addCustomer(userId, distributor, customerVO);
		distributor.setMyCustomer(resellerRepresentative);

		// ------ Self Authoring Code starts here --------
		if (addDistributorForm.isSelfAuthor()) {
			ContentOwner co = authorService.addContentOwnerIfNotExist(resellerRepresentative, userId);
			distributor.setContentOwner(co);
		}
		// ------ Self Authoring Code ends here --------

		distributor = distributorRepository.save(distributor);
		// --- code ended for creating reseller/distributor representative customer ---------

		return distributor;
	}

	private AddCustomerForm createCustomerVO(AddDistributorForm distributorForm) {
		AddCustomerForm customerVO = new AddCustomerForm();
		customerVO.setAddress1(distributorForm.getAddress1Line1());
		customerVO.setAddress1a(distributorForm.getAddress1Line2());
		customerVO.setAddress2(distributorForm.getAddress2Line1());
		customerVO.setAddress2a(distributorForm.getAddress2Line2());
		customerVO.setAudio(distributorForm.isAudio());
		customerVO.setAudioLocked(distributorForm.isAudioLocked());
		customerVO.setBandwidth(distributorForm.isBandwidth());
		customerVO.setBandwidthLocked(distributorForm.isBandwidthLocked());
		customerVO.setBrander(distributorForm.getBrander());
		customerVO.setBrandName(distributorForm.getBrandName());
		customerVO.setCaptioning(distributorForm.isCaptioning());
		customerVO.setCaptioningLocked(distributorForm.isCaptioningLocked());
		customerVO.setCity1(distributorForm.getCity1());
		customerVO.setCity2(distributorForm.getCity2());
		// customerVO.setConfirmPassword(distributorForm.getConfirmPassword());//need
		// to be added
		customerVO.setCountry1(distributorForm.getCountry1());
		customerVO.setCountry2(distributorForm.getCountry2());
		customerVO.setCustomerName(distributorForm.getDistributorName());
		customerVO.setCustomerType(Customer.B2B);
		customerVO.setDistName(distributorForm.getDistName());
		customerVO.setEmailAdd(distributorForm.getEmailAdd());
		customerVO.setEnrollmentEmails(distributorForm.isEnrollmentEmails());
		customerVO.setEnrollmentEmailsLocked(distributorForm.isEnrollmentEmailsLocked());
		customerVO.setEventSource(distributorForm.getEventSource());
		customerVO.setExt(distributorForm.getExt());
		customerVO.setFirstName(distributorForm.getFirstName());
		customerVO.setLastName(distributorForm.getLastName());
		customerVO.setLoginEmailID(distributorForm.getLoginEmailID());
		customerVO.setPassword(distributorForm.getPassword());
		customerVO.setConfirmPassword(distributorForm.getConfirmPassword());
		customerVO.setPhone(distributorForm.getPhone());
		customerVO.setRegistrationEmails(distributorForm.isRegistrationEmails());
		customerVO.setRegistrationEmailsLocked(distributorForm.isRegistrationEmailsLocked());
		customerVO.setState1(distributorForm.getState1());
		customerVO.setState2(distributorForm.getState2());
		customerVO.setVideo(distributorForm.isVideo());
		customerVO.setVideoLocked(distributorForm.isVideoLocked());
		customerVO.setVolume(distributorForm.getVolume());
		customerVO.setVolumeLocked(distributorForm.isVolumeLocked());
		customerVO.setWesiteURL(distributorForm.getWesiteURL());
		customerVO.setZip1(distributorForm.getZip1());
		customerVO.setZip2(distributorForm.getZip2());
		customerVO.setLocked(distributorForm.isLocked());
		customerVO.setExpired(distributorForm.isExpired());
		customerVO.setExpirationDate(distributorForm.getExpirationDate());
		customerVO.setDisabled(distributorForm.isDisabled());
		customerVO.setReport(distributorForm.isReport());
		customerVO.setChangePassword(distributorForm.isChangePassword());
		return customerVO;
	}

	/**
	 * Enabling features for a newly added distributor We need to insert in the
	 * following order: insert into DISTRIBUTOR_PERMISSION (ID ,
	 * DISTRIBUTOR_ID,FEATURE_ID,ENABLETF) (select ISNULL(MAX(ID)+1,1) , 5503 ,
	 * 51, 1 FROM DISTRIBUTOR_PERMISSION)
	 * 
	 * @param id
	 *            Distributor id
	 */
	@Transactional
	private boolean enableFeaturesForDistributor(Distributor distributor) {
		// early return if id is not valid
		if (distributor == null) {
			return false;
		}

		List<String> lstRestrictFeatures = getRestrictedFeatures();
		boolean isEnable = true;
		DistributorLMSFeature defalutPermisson;
		// Get all the Features
		List<LMSFeature> defaultFeaturesList = (List<LMSFeature>) lMSFeatureRepository
				.findAll();
		// Loop thru all the features
		for (LMSFeature defaultFeature : defaultFeaturesList) {
			defalutPermisson = new DistributorLMSFeature();
			isEnable = true;

			for (String strFeature : lstRestrictFeatures) {
				if (strFeature
						.equalsIgnoreCase(defaultFeature.getFeatureCode()))
					isEnable = false;
			}
			
			if(defaultFeature.getFeatureCode().equals("LMS-MGR-0033")){
				 isEnable = false;
			}

			defalutPermisson.setEnabled(isEnable);
			defalutPermisson.setDistributor(distributor);
			defalutPermisson.setLmsFeature(defaultFeature);
			// Now insert
			distributorLMSFeatureRepository.save(defalutPermisson);
		}
		return true;
	}

	/**
	 * LMS-15215 - there are Continuous changes in CrowdSourcing distributor
	 * creation. So, I am creating separating function for CrowdSourcing
	 * webservice
	 * ----------------------------------------------------------------
	 * --------------------------------- Enabling features for a newly added
	 * distributor We need to insert in the following order: insert into
	 * DISTRIBUTOR_PERMISSION (ID , DISTRIBUTOR_ID,FEATURE_ID,ENABLETF) (select
	 * ISNULL(MAX(ID)+1,1) , 5503 , 51, 1 FROM DISTRIBUTOR_PERMISSION)
	 * 
	 * @param distributor
	 */
	private boolean enableFeaturesForDistributorForSelfService(
			Distributor distributor) {
		// early return if id is not valid
		if (distributor == null) {
			return false;
		}

		List<String> lstRestrictFeatures = getRestrictedFeaturesForSelfservice();
		boolean isEnable = true;
		DistributorLMSFeature defalutPermisson;
		// Get all the Features
		List<LMSFeature> defaultFeaturesList = (List<LMSFeature>) lMSFeatureRepository
				.findAll();
		// Loop thru all the features
		for (LMSFeature defaultFeature : defaultFeaturesList) {
			defalutPermisson = new DistributorLMSFeature();
			isEnable = true;

			for (String strFeature : lstRestrictFeatures) {
				if (strFeature
						.equalsIgnoreCase(defaultFeature.getFeatureCode()))
					isEnable = false;
			}

			defalutPermisson.setEnabled(isEnable);
			defalutPermisson.setDistributor(distributor);
			defalutPermisson.setLmsFeature(defaultFeature);
			// Now insert
			distributorLMSFeatureRepository.save(defalutPermisson);
		}
		return true;
	}

	// LMS-15296
	// Because of business need, I have disabled the predict permission from
	// Learner, Manager and Admin mode.
	// I have added permissions code into .properties file. [LMS-LRN-0010,
	// LMS-MGR-0030, LMS-ADM-0032]
	// Permissions added in properties file with underscore(_) sign not minus(-)
	// because I was facing parsing error. So after getting from .properties,
	// replace _ with - sign.

	public List<String> getRestrictedFeatures() {

		List<String> objRestrictedFeatureName = null;

		try {
			objRestrictedFeatureName = new ArrayList<String>();
			com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
			lang.setLanguage(Language.DEFAULT_LANG);
			Brander brand = VU360Branding.getInstance().getBrander(
					VU360Branding.DEFAULT_BRAND, lang);
			List<LabelBean> objRestrictedFeatures = null;
			objRestrictedFeatures = brand
					.getBrandMapElements("lms.features.RestrictedFeatures");

			if (objRestrictedFeatures.size() > 0)
				for (LabelBean bean : objRestrictedFeatures) {
					objRestrictedFeatureName.add(bean.getLabel().replace('_',
							'-'));
				}
			else
				throw new Exception();

		} catch (Exception exc) {
			objRestrictedFeatureName = new ArrayList<String>();
			objRestrictedFeatureName.add("LMS-LRN-0010");
			objRestrictedFeatureName.add("LMS-MGR-0030");
			objRestrictedFeatureName.add("LMS-ADM-0032");
			
		}
		return objRestrictedFeatureName;

	}

	// LMS-15215 - there are Continuous changes in CrowdSourcing distributor
	// creation. So, I am creating separating function for CrowdSourcing
	// webservice
	// LMS-15296
	// Because of business need, I have disabled the predict permission from
	// Learner, Manager and Admin mode.
	// I have added permissions code into .properties file. [LMS-LRN-0010,
	// LMS-MGR-0030, LMS-ADM-0032]
	// Permissions added in properties file with underscore(_) sign not minus(-)
	// because I was facing parsing error. So after getting from .properties,
	// replace _ with - sign.
	public List<String> getRestrictedFeaturesForSelfservice() {

		List<String> objRestrictedFeatureName = null;

		try {
			objRestrictedFeatureName = new ArrayList<String>();
			com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
			lang.setLanguage(Language.DEFAULT_LANG);
			Brander brand = VU360Branding.getInstance().getBrander(
					VU360Branding.DEFAULT_BRAND, lang);
			List<LabelBean> objRestrictedFeatures = null;
			objRestrictedFeatures = brand
					.getBrandMapElements("lms.features.RestrictedFeatures.SelfService");

			if (objRestrictedFeatures.size() > 0)
				for (LabelBean bean : objRestrictedFeatures) {
					objRestrictedFeatureName.add(bean.getLabel().replace('_',
							'-'));
				}
			else
				throw new Exception();
		} catch (Exception exc) {
			objRestrictedFeatureName = new ArrayList<String>();
			objRestrictedFeatureName.add("LMS-LRN-0010");
			objRestrictedFeatureName.add("LMS-MGR-0030");
			objRestrictedFeatureName.add("LMS-ADM-0032");
			objRestrictedFeatureName.add("LMS-LRN-0011");
			// LMS-15215
			objRestrictedFeatureName.add("LMS-MGR-0009");
			objRestrictedFeatureName.add("LMS-MGR-0010");
			objRestrictedFeatureName.add("LMS-MGR-0020");
			objRestrictedFeatureName.add("LMS-ADM-0008");
			objRestrictedFeatureName.add("LMS-ADM-0005");
			objRestrictedFeatureName.add("LMS-MGR-0017");
		}
		return objRestrictedFeatureName;

	}

	public boolean updateFeaturesForDistributor(Distributor distributor,
			List<LMSRoleLMSFeature> lmsPermissions) {
		// early return if id is not valid
		if (distributor == null) {
			return false;
		}
		DistributorLMSFeature defalutPermisson;

		// Loop thru all the features
		for (LMSRoleLMSFeature lmsPermission : lmsPermissions) {
			defalutPermisson = this
					.loadForUpdateDistributorPermission(lmsPermission.getId());
			if (defalutPermisson != null) {

				defalutPermisson.setEnabled(lmsPermission.getEnabled());

			} else {
				defalutPermisson = new DistributorLMSFeature();
				defalutPermisson.setEnabled(lmsPermission.getEnabled());
				defalutPermisson.setDistributor(distributor);
				defalutPermisson.setLmsFeature(lmsPermission.getLmsFeature());
			}

			// Now update
			distributorLMSFeatureRepository.save(defalutPermisson);
		}
		return true;
	}

	@Transactional
	public Distributor updateDistributor(
			EditDistributorForm editDistributorForm,
			Distributor authDistributor, com.softech.vu360.lms.vo.VU360User loggedInUser) {
		// make no assumptions where this object came from.
		Distributor origDist = this.loadForUpdateDistributor(authDistributor.getId());

		origDist.setActive(editDistributorForm.isActive());
		origDist.setSelfReporting(editDistributorForm.isSelfReporting());

		origDist.setCallLoggingEnabled(editDistributorForm.getCallLoggingEnabled());

		origDist.setMarkedPrivate(editDistributorForm.isMarkedPrivate());
		origDist.setBrand(authDistributor.getBrand());
		Brand brand = brandService.getBrandById(editDistributorForm.getBrandId());
		origDist.setBrand(brand);
		origDist.setBrandName(brand == null ? "" : brand.getBrandKey());
		origDist.setDistributorEmail(editDistributorForm.getEmailAddress());
		origDist.setFirstName(editDistributorForm.getFirstName());
		origDist.setLastName(editDistributorForm.getLastName());
		origDist.setName(editDistributorForm.getName());
		origDist.setDistributorCode(editDistributorForm.getDistributorCode());
		origDist.setOfficePhone(editDistributorForm.getOfficePhone());
		origDist.setOfficePhoneExtn(editDistributorForm.getOfficePhoneExt());
		origDist.setWebsiteUrl(editDistributorForm.getWebsiteURL());
		origDist.setType(editDistributorForm.getType());
		
		if(origDist.getDistributorAddress()==null)origDist.setDistributorAddress(new Address());
		
		origDist.getDistributorAddress().setCity(editDistributorForm.getDistributorAddress().getCity());
		origDist.getDistributorAddress().setCountry(editDistributorForm.getDistributorAddress().getCountry());
		origDist.getDistributorAddress().setProvince(editDistributorForm.getDistributorAddress().getProvince());
		origDist.getDistributorAddress().setState(editDistributorForm.getDistributorAddress().getState());
		origDist.getDistributorAddress().setStreetAddress(editDistributorForm.getDistributorAddress().getStreetAddress());
		origDist.getDistributorAddress().setStreetAddress2(editDistributorForm.getDistributorAddress().getStreetAddress2());
		origDist.getDistributorAddress().setStreetAddress3(editDistributorForm.getDistributorAddress().getStreetAddress3());
		origDist.getDistributorAddress().setZipcode(editDistributorForm.getDistributorAddress().getZipcode());

		if(origDist.getDistributorAddress()==null)origDist.setDistributorAddress2(new Address());
		
		origDist.getDistributorAddress2().setCity(editDistributorForm.getDistributorAddress2().getCity());
		origDist.getDistributorAddress2().setCountry(editDistributorForm.getDistributorAddress2().getCountry());
		origDist.getDistributorAddress2().setProvince(editDistributorForm.getDistributorAddress2().getProvince());
		origDist.getDistributorAddress2().setState(editDistributorForm.getDistributorAddress2().getState());
		origDist.getDistributorAddress2().setStreetAddress(editDistributorForm.getDistributorAddress2().getStreetAddress());
		origDist.getDistributorAddress2().setStreetAddress2(editDistributorForm.getDistributorAddress2().getStreetAddress2());
		origDist.getDistributorAddress2().setStreetAddress3(editDistributorForm.getDistributorAddress2().getStreetAddress3());
		origDist.getDistributorAddress2().setZipcode(editDistributorForm.getDistributorAddress2().getZipcode());

		// Need to write code for Self-Authoring.
		// editDistributorForm.isSelfAuthor()
		if (editDistributorForm.isSelfAuthor()) {
			if (origDist.getMyCustomer() != null) {
				ContentOwner co = authorService.addContentOwnerIfNotExist(
						origDist.getMyCustomer(), loggedInUser.getId());
				origDist.setContentOwner(co);
			}
		} else if (origDist.isSelfAuthor()) { // [12/9/2010] LMS-7512 :: Update
												// Content Owner for Self-Author
												// Customer/Reseller
			ContentOwner contentOwner = this.authorService
					.updateContentOwner(origDist.getMyCustomer());
			if (contentOwner != null)
				origDist.setContentOwner(contentOwner);
		}

		origDist.setCorporateAuthorVar(editDistributorForm
				.isCorporateAuthorVar());
		Distributor dist = distributorRepository.save(origDist);
		customerService.updateDefaultCustomer(origDist);

		return dist;
	}

	public Distributor saveDistributor(Distributor distributor) {
		// make no assumptions where this object came from.
		Distributor origDist = getDistributorById(distributor.getId());

		origDist.setActive(distributor.getActive());
		origDist.setSelfReporting(distributor.isSelfReporting());

		/**
		 * Call Logging | LMS-8108 | S M Humayun | 27 April 2011
		 */
		origDist.setCallLoggingEnabled(distributor.getCallLoggingEnabled());

		origDist.setMarkedPrivate(distributor.isMarkedPrivate());
		origDist.setBrand(distributor.getBrand());
		origDist.setBrandName(distributor.getBrandName());
		origDist.setDistributorEmail(distributor.getDistributorEmail());
		origDist.setFirstName(distributor.getFirstName());
		origDist.setLastName(distributor.getLastName());
		origDist.setName(distributor.getName());
		origDist.setOfficePhone(distributor.getOfficePhone());
		origDist.setOfficePhoneExtn(distributor.getOfficePhoneExtn());
		origDist.setMobilePhone(distributor.getMobilePhone());
		origDist.setWebsiteUrl(distributor.getWebsiteUrl());
		origDist.setType(distributor.getType());
		if(origDist.getDistributorAddress() != null) {
		origDist.getDistributorAddress().setCity(
				distributor.getDistributorAddress().getCity());
		origDist.getDistributorAddress().setCountry(
				distributor.getDistributorAddress().getCountry());
		origDist.getDistributorAddress().setProvince(
				distributor.getDistributorAddress().getProvince());
		origDist.getDistributorAddress().setState(
				distributor.getDistributorAddress().getState());
		origDist.getDistributorAddress().setStreetAddress(
				distributor.getDistributorAddress().getStreetAddress());
		origDist.getDistributorAddress().setStreetAddress2(
				distributor.getDistributorAddress().getStreetAddress2());
		origDist.getDistributorAddress().setStreetAddress3(
				distributor.getDistributorAddress().getStreetAddress3());
		origDist.getDistributorAddress().setZipcode(
				distributor.getDistributorAddress().getZipcode());
		}
		
		if(origDist.getDistributorAddress2() != null) {
			origDist.getDistributorAddress2().setCity(
					distributor.getDistributorAddress2().getCity());
			origDist.getDistributorAddress2().setCountry(
					distributor.getDistributorAddress2().getCountry());
			origDist.getDistributorAddress2().setProvince(
					distributor.getDistributorAddress2().getProvince());
			origDist.getDistributorAddress2().setState(
					distributor.getDistributorAddress2().getState());
			origDist.getDistributorAddress2().setStreetAddress(
					distributor.getDistributorAddress2().getStreetAddress());
			origDist.getDistributorAddress2().setStreetAddress2(
					distributor.getDistributorAddress2().getStreetAddress2());
			origDist.getDistributorAddress2().setStreetAddress3(
					distributor.getDistributorAddress2().getStreetAddress3());
			origDist.getDistributorAddress2().setZipcode(
					distributor.getDistributorAddress2().getZipcode());
		}
		
		if(origDist.getDistributorPreferences() != null) {
			origDist.getDistributorPreferences().setAudioEnabled(
					distributor.getDistributorPreferences().isAudioEnabled());
			origDist.getDistributorPreferences().setAudioLocked(
					distributor.getDistributorPreferences().isAudioLocked());
			origDist.getDistributorPreferences().setBandwidth(
					distributor.getDistributorPreferences().getBandwidth());
			origDist.getDistributorPreferences().setBandwidthLocked(
					distributor.getDistributorPreferences().isBandwidthLocked());
			origDist.getDistributorPreferences().setCaptioningEnabled(
					distributor.getDistributorPreferences().isCaptioningEnabled());
			origDist.getDistributorPreferences().setCaptioningLocked(
					distributor.getDistributorPreferences().isCaptioningLocked());
			origDist.getDistributorPreferences()
					.setEnableEnrollmentEmailsForNewCustomers(
							distributor.getDistributorPreferences()
									.isEnableEnrollmentEmailsForNewCustomers());
			origDist.getDistributorPreferences()
					.setEnableRegistrationEmailsForNewCustomers(
							distributor.getDistributorPreferences()
									.isEnableRegistrationEmailsForNewCustomers());
			origDist.getDistributorPreferences()
					.setEnableSelfEnrollmentEmailsForNewCustomers(
							distributor.getDistributorPreferences()
									.isEnableSelfEnrollmentEmailsForNewCustomers());
			origDist.getDistributorPreferences().setVedioEnabled(
					distributor.getDistributorPreferences().isVedioEnabled());
			origDist.getDistributorPreferences().setVideoLocked(
					distributor.getDistributorPreferences().isVideoLocked());
			origDist.getDistributorPreferences().setVolume(
					distributor.getDistributorPreferences().getVolume());
			origDist.getDistributorPreferences().setVolumeLocked(
					distributor.getDistributorPreferences().isVolumeLocked());
			origDist.setCustomFields(distributor.getCustomFields());
		}
		
		return distributorRepository.save(origDist);
	}

	public Distributor updateDistributor(Distributor distributor) {
		return distributorRepository.save(distributor);
	}

	public List<DistributorGroup> getDistributorGroupsBydistributorId(long Id) {
		return distributorGroupRepository.findByDistributorsId(Id);
	}

	public List<Distributor> getDistributorByDistributorCode(
			String distributorCode) {
		Distributor d = distributorRepository
				.findByDistributorCode(distributorCode);
		if(d!=null){
			List<Distributor> l = new ArrayList<Distributor>();
			l.add(d);
			return l;
		}
		
		return null;

	}

	public Boolean isDistributCodeMappedToMoreThenOneDistributor(
			String distributorCode, Long exceptSelectedDistributor) {
		List<Distributor> ls = this
				.getDistributorByDistributorCode(distributorCode);

		if (ls != null && ls.size() > 0) {
			for (int i = 0; i < ls.size(); i++)
				if (ls.get(i).getId() == exceptSelectedDistributor)
					ls.remove(i);
		}

		if (ls!=null && ls.size() > 0)
			return new Boolean(true);
		return new Boolean(false);

	}

	public DistributorPreferences saveDistributorPreferences(
			DistributorPreferences distributorPreferences) {
		return distributorPreferencesRepository.save(distributorPreferences);
	}

	public DistributorPreferences getDistributorPreferencesById(
			long distributorPreferencesId) {
		return distributorPreferencesRepository
				.findOne(distributorPreferencesId);
	}

	public List<VU360User> getLearnersByCustomer(String firstName,
			String lastName, String email, String searchCriteria,
			Long customerId, int pageIndex, int retrieveRowCount,
			ResultSet resultSet, String sortBy, int sortDirection) {

		List<VU360User> userList = new ArrayList<VU360User>();
		RepositorySpecificationsBuilder<VU360User> sb_VU360User = new RepositorySpecificationsBuilder<VU360User>();
		sb_VU360User.with("learner_customer_id", sb_VU360User.JOIN_EQUALS,
				customerId, "AND");

		if (!StringUtils.isBlank(searchCriteria)) {
			sb_VU360User.with("firstName", sb_VU360User.LIKE_IGNORE_CASE,
					searchCriteria, "OR");
			sb_VU360User.with("lastName", sb_VU360User.LIKE_IGNORE_CASE,
					searchCriteria, "OR");
			sb_VU360User.with("emailAddress", sb_VU360User.LIKE_IGNORE_CASE,
					searchCriteria, "OR");
		} else {
			sb_VU360User.with("firstName", sb_VU360User.LIKE_IGNORE_CASE,
					firstName, "AND");
			sb_VU360User.with("lastName", sb_VU360User.LIKE_IGNORE_CASE,
					lastName, "AND");
			sb_VU360User.with("emailAddress", sb_VU360User.LIKE_IGNORE_CASE,
					email, "AND");
		}
		Sort sortSpec = orderBy(sortDirection, sortBy);

		PageRequest pageRequest = null;
		if (retrieveRowCount != -1 && pageIndex >= 0) {
			pageRequest = new PageRequest(pageIndex / retrieveRowCount,
					retrieveRowCount, sortSpec);
			Page<VU360User> page = vU360UserRepository.findAll(
					sb_VU360User.build(), pageRequest);
			resultSet.total = ((int) (long) page.getTotalElements());
			userList = page.getContent();
		} else {
			userList = vU360UserRepository.findAll(sb_VU360User.build(),
					sortSpec);
			resultSet.total = userList.size();
		}
		return userList;

	}

	public List<VU360User> getLearnersByDistributor(String firstName,
			String lastName, String email, String searchCriteria,
			Long distributorId, int pageIndex, int retrieveRowCount,
			ResultSet resultSet, String sortBy, int sortDirection) {

		List<VU360User> userList = new ArrayList<VU360User>();
		RepositorySpecificationsBuilder<VU360User> sb_VU360User = new RepositorySpecificationsBuilder<VU360User>();
		sb_VU360User.with("learner_customer_distributor_id",
				sb_VU360User.JOIN_EQUALS, distributorId, "AND");

		if (!StringUtils.isBlank(searchCriteria)) {
			sb_VU360User.with("firstName", sb_VU360User.LIKE_IGNORE_CASE,
					searchCriteria, "OR");
			sb_VU360User.with("lastName", sb_VU360User.LIKE_IGNORE_CASE,
					searchCriteria, "OR");
			sb_VU360User.with("emailAddress", sb_VU360User.LIKE_IGNORE_CASE,
					searchCriteria, "OR");
		} else {
			sb_VU360User.with("firstName", sb_VU360User.LIKE_IGNORE_CASE,
					firstName, "AND");
			sb_VU360User.with("lastName", sb_VU360User.LIKE_IGNORE_CASE,
					lastName, "AND");
			sb_VU360User.with("emailAddress", sb_VU360User.LIKE_IGNORE_CASE,
					email, "AND");
		}
		Sort sortSpec = null;
		if (StringUtils.isBlank(sortBy)) {
			sortSpec = orderBy(0, "firstName");
		} else {
			sortSpec = orderBy(sortDirection, sortBy);
		}

		PageRequest pageRequest = null;
		if (retrieveRowCount != -1 && pageIndex >= 0 ) {
			pageRequest = new PageRequest(pageIndex / retrieveRowCount,
					retrieveRowCount, sortSpec);
			Page<VU360User> page = vU360UserRepository.findAll(
					sb_VU360User.build(), pageRequest);
			resultSet.total = ((int) (long) page.getTotalElements());
			userList = page.getContent();
		} else {
			userList = vU360UserRepository.findAll(sb_VU360User.build(),
					sortSpec);
			resultSet.total = userList.size();
		}
		return userList;

	}

	public List<VU360User> getAllLearners(String firstName, String lastName,
			String email, String searchCriteria, VU360User loggedInUser,
			int pageIndex, int retrieveRowCount, ResultSet resultSet,
			String sortBy, int sortDirection) {

		List<VU360User> userList = new ArrayList<VU360User>();
		RepositorySpecificationsBuilder<VU360User> sb_VU360User = new RepositorySpecificationsBuilder<VU360User>();
		
		firstName = firstName.replaceAll("%", "");
		lastName = lastName.replaceAll("%", "");
		email = email.replaceAll("%", "");
		
		if( (lastName.length()+firstName.length()+email.length()) >= 2  ) {
			if(loggedInUser.getLmsAdministrator()!=null && !loggedInUser.getLmsAdministrator().isGlobalAdministrator()){ 	// apply administrator filtering
				
				Collection<String> distributorIds = getAdminRestrictionExpression(loggedInUser);

				sb_VU360User.with("learner_customer_distributor_id",
						sb_VU360User.JOIN_IN, distributorIds.toArray(), "AND");
			}

			if (!StringUtils.isBlank(searchCriteria)) {
				sb_VU360User.with("firstName", sb_VU360User.LIKE_IGNORE_CASE,
						searchCriteria, "OR");
				sb_VU360User.with("lastName", sb_VU360User.LIKE_IGNORE_CASE,
						searchCriteria, "OR");
				sb_VU360User.with("emailAddress", sb_VU360User.LIKE_IGNORE_CASE,
						searchCriteria, "OR");
			} else {
				sb_VU360User.with("firstName", sb_VU360User.LIKE_IGNORE_CASE,
						firstName, "AND");
				sb_VU360User.with("lastName", sb_VU360User.LIKE_IGNORE_CASE,
						lastName, "AND");
				sb_VU360User.with("emailAddress", sb_VU360User.LIKE_IGNORE_CASE,
						email, "AND");
			}

			sb_VU360User.with("learner_id",
					sb_VU360User.JOIN_GREATER_THAN_EQUALS_TO, -1, "AND");

			Sort sortSpec = orderBy(sortDirection, sortBy);

			PageRequest pageRequest = null;
			if (retrieveRowCount != -1 && pageIndex >= 0) {
				pageRequest = new PageRequest(pageIndex / retrieveRowCount,
						retrieveRowCount, sortSpec);
				Page<VU360User> page = vU360UserRepository.findAll(
						sb_VU360User.build(), pageRequest);
				resultSet.total = ((int) (long) page.getTotalElements());
				userList = page.getContent();
			} else {
				userList = vU360UserRepository.findAll(sb_VU360User.build(),
						sortSpec);
				resultSet.total = userList.size();
			}
			return userList;
		}
		else{
			log.info("Max 2 char to search learner.");
			return new ArrayList<VU360User>();
		}
	}

	public List<VU360User> getAllLearners(String firstName, String lastName,
			String email, String searchCriteria) {

		List<VU360User> ls = vU360UserRepository.getAllLearners(firstName,
				lastName, email, null, null);
		return ls;

	}

	public List<DistributorLMSFeature> getDefaultEnablePermissions(
			Distributor distributor, String roleType) {
		List<DistributorLMSFeature> dlfList = distributorLMSFeatureRepository
				.getAllByDistributorAndLmsFeatureRoleTypeAndEnabledTrue(
						distributor, roleType);
		return dlfList;

	}

	/**
	 * Return list of distributor lms features (enable or disable) against give
	 * <code>distributor</code> and <code>roleType</code>
	 * 
	 * @param distributor
	 *            distributor
	 * @param roleType
	 *            role type
	 * @return list of distributor lms features
	 * @author sm.humayun
	 * @since 4.16.3 {LMS-11041}
	 */
	@Override
	public List<DistributorLMSFeature> getPermissions(Distributor distributor,
			String roleType) {

		List<DistributorLMSFeature> list = null;
		if (StringUtils.isNotEmpty(roleType))
			list = distributorLMSFeatureRepository
					.getAllByDistributorAndLmsFeatureRoleType(distributor,
							roleType);
		else
			list = distributorLMSFeatureRepository
					.getAllByDistributor(distributor);

		return list;
	}

	@Deprecated
	public DistributorGroup loadForUpdateDistributorGroup(
			long distributorGroupId) {
		return distributorGroupRepository.findById(distributorGroupId);
	}

	public Distributor loadForUpdateDistributor(long distributorId) {
		return distributorRepository.findOne(distributorId);
	}

	public DistributorPreferences loadForUpdateDistributorPreferences(
			long distributorPreferencesId) {
		return distributorPreferencesRepository
				.findOne(distributorPreferencesId);
	}

	public DistributorLMSFeature loadForUpdateDistributorPermission(
			long distributorPermissionId) {
		return distributorLMSFeatureRepository.findOne(distributorPermissionId);
	}

	public List<DistributorGroup> findDistributorGroupsByLMSAdministratorId(Long lmsAdministratorId) {
		return distributorGroupRepository.findDistributorGroupsByLMSAdministratorId(lmsAdministratorId);
	}
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * @return the authorService
	 */
	public AuthorService getAuthorService() {
		return authorService;
	}

	/**
	 * @param authorService
	 *            the authorService to set
	 */
	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	public BrandService getBrandService() {
		return brandService;
	}

	public void setBrandService(BrandService brandService) {
		this.brandService = brandService;
	}

	@Override
	public Address findAddressById(Long id) {
		return addressRepository.findOne(id);
	}
	
}