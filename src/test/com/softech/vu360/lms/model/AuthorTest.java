package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

import com.softech.vu360.util.GUIDGeneratorUtil;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class AuthorTest extends
		TestBaseDAO<Author> {

	@Before
	public void setRequiredService() {

	}

//	@Test
	public void AuthorGroup_should_save_with_AuthorPermission() throws Exception {

		AuthorGroup authorGroup = new AuthorGroup();
		List<AuthorPermission> permissions = new ArrayList<AuthorPermission>();
    	
    	ContentOwner contentOwner=(ContentOwner)crudFindById(ContentOwner.class, new Long(1));
    	
    	authorGroup.setName(contentOwner.getName()+" Test_Admin");
    	authorGroup.setCreatedDate(new Date());
    	authorGroup.setCreatedUserId(new Long(1));
    	authorGroup.setGuid(GUIDGeneratorUtil.generateGUID());
    	authorGroup.setDescription("Test_Description_Author");
    	//=============Setting permission
    	AuthorPermission permission=new AuthorPermission();
    	AuthorFeature feature=(AuthorFeature)crudFindById(AuthorFeature.class, new Long(1));
    	permission.setAuthorFeature(feature);
    	permission.setCreatedDate(new Date());
    	permission.setFeatureEnabled(1);
    	permissions.add(permission);
    	permission.setAuthorGroup(authorGroup);
    	

    	authorGroup.setPermissions(permissions);
    	authorGroup.setContentOwner(contentOwner);
    	
    	try{
    		
    		authorGroup=(AuthorGroup)crudSave(AuthorGroup.class, authorGroup);
    	}
    	catch(Exception ex){
    		System.out.println(ex);
    	}

	}

//	@Test
	public void AuthorGroup_should_update() {

		AuthorGroup updateRecord = (AuthorGroup)crudFindById(AuthorGroup.class, new Long(17050));
		AuthorFeature feature=(AuthorFeature)crudFindById(AuthorFeature.class, new Long(2));
		updateRecord.getPermissions().get(0).setAuthorFeature(feature);
		updateRecord=(AuthorGroup)crudSave(AuthorGroup.class, updateRecord);
		
	}

//	@Test
	public void Author_should_save(){
		
		Author author=new Author();
		
		//==============Setting ContentOwner
		List<ContentOwner> ownerList=new ArrayList<ContentOwner>();
		ContentOwner contentOwner=(ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		ownerList.add(contentOwner);
		author.setContentOwners(ownerList);
		//==============Setting AuthorGroup
		List<AuthorGroup> authorGroup=new ArrayList<AuthorGroup>();
		AuthorGroup updateRecord = (AuthorGroup)crudFindById(AuthorGroup.class, new Long(17050));
		authorGroup.add(updateRecord);
		author.setGroups(authorGroup);
		//================Setting user
		VU360User user=(VU360User)crudFindById(VU360User.class, new Long(1));
		author.setVu360User(user);
		author.setCreatedDate(new Date());
		 try{
			 author=save(author);
			 }
		 catch(Exception ex){
			 System.out.println(author.getId());
		 }
	}
	
	@Test
	public void Author_should_update(){
		
		Author author=getById(new Long(7284), Author.class);
		author.getContentOwners().remove(0);
		update(author);
	}
}
