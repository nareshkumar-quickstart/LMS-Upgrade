package com.softech.vu360.lms.repositories;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.SelfServiceProductDetails;

public interface SelfServiceProductDetailsRepository extends
		CrudRepository<SelfServiceProductDetails, Long> {

	@Procedure(procedureName="TEST_CREATE_FREEMIUM_ACCOUNT")
	public Integer createFreemiumAccount(
			@Param("ContentOwnerName") Object contentOwnerName,
			@Param("UserName") Object userName,
			@Param("EmailAddress") Object emailAddress,
			@Param("FirstName") Object firstName,
			@Param("LastName") Object lastName,
			@Param("Password") Object password,
			@Param("EffectiveFrom") Object effectiveFrom,
			@Param("EffectiveTo") Object effectiveTo,
			@Param("MaxAuthorCount") Object maxAuthorCount,
			@Param("MaxCourseCount") Object maxCourseCount);

	@Procedure(procedureName="INSERT_COURSE_LICENSE_BY_FREEMIUM")
	public Integer insertCourseLicenseByFreemium(
			@Param("UserName") Object userName);

	@Procedure("INSERT_AUTHOR_LICENSE_BY_FREEMIUM_INDIVIDUAL")
	public boolean insertAuthorRightsByFreemium(
			@Param("UserName") Object userName,
			@Param("QUANTITY") Object proQuantity);

	@Procedure("INSERT_COURSE_LICENSE_BY_FREEMIUM_INDIVIDUAL")
	public Integer insertCoursePublishRightsByFreemium(
			@Param("UserName") Object userName,
			@Param("QUANTITY") Object proQuantity);

	@Procedure("INSERT_SCENE_TEMPLATE_BY_FREEMIUM")
	public Integer insertSceneTemplateByFreemium(
			@Param("UserName") Object userName,
			@Param("SceneTemplateName") Object SceneTemplateName,
			@Param("EffectiveFrom") Object effectiveFrom,
			@Param("EffectiveTo") Object effectiveTo);

	@Procedure("INSERT_SCENE_TEMPLATE_BY_FREEMIUM2")
	public Integer insertSceneTemplateByFreemiumV2(
			@Param("UserName") Object userName,
			@Param("SceneTemplateName") Object SceneTemplateName,
			@Param("EffectiveFrom") Object effectiveFrom,
			@Param("EffectiveTo") Object effectiveTo);

}
