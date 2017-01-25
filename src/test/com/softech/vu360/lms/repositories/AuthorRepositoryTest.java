package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Author;
import com.softech.vu360.lms.model.AuthorGroup;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.ManageUserStatus;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.AuthorGroupRepository;
import com.softech.vu360.lms.repositories.AuthorRepository;
import com.softech.vu360.lms.repositories.VU360UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class AuthorRepositoryTest {

	@Inject
	private AuthorRepository authorRepository;
	@Inject
	private VU360UserRepository userRepository;
	@Inject
	private AuthorGroupRepository authorGroupRepository;
	
	//@Test
	public void findById() {
		Author author = authorRepository.findOne(7335L);
		System.out.println(author);
	}
	
	//@Test(expected=Exception.class)
	public void findByVu360UserIds() {
		
		List<Long> vu360UserIds = new ArrayList<>();
		vu360UserIds.add(11L);
		vu360UserIds.add(1164L);
		vu360UserIds.add(1215L);
		vu360UserIds.add(1216L);
		
		List<Author> authors = (List<Author>)authorRepository.findByVu360UserIdIn(vu360UserIds);
		if (!CollectionUtils.isEmpty(authors)) {
			System.out.println(authors.size());
			for (Author author : authors) {
				if (author == null) {
					System.out.println("author is null");
				} else {
					System.out.println(author.getId());
				}
			}
		}	
	}
	
	//@Test
	public void author_should_save() {
		Author author = new Author();
		VU360User user = userRepository.findOne(1L);
		ContentOwner contentOwner = user.getLearner().getCustomer().getContentOwner();
		if(contentOwner == null){
			contentOwner = user.getLearner().getCustomer().getDistributor().getContentOwner();
		}
		author.getContentOwners().add(contentOwner);
		author.setCreatedDate(new Date());
		author.setCreatedUserId(1L);
		List<AuthorGroup> authorGroups = authorGroupRepository.findByContentOwner(contentOwner);
		if(!CollectionUtils.isEmpty(authorGroups)){
			author.getGroups().add(authorGroups.get(0));
		}
		author.setVu360User(user);
		authorRepository.save(author);
	}

	//@Test
	public void isAuthor() {
		boolean b = authorRepository.isAuthor(1L);
		System.out.println(b);
	}

	//@Test
	public void isThisAuthor() {
		boolean b = authorRepository.isThisAuthor("admin123");
		System.out.println(b);
	}

	@Test
	public void getUserStatus() {
		ManageUserStatus criteria =new ManageUserStatus();
		List<Object[]> aListOfObjArr= authorRepository.getUserStatus(criteria);
		for (Object[] objects : aListOfObjArr) {
			System.out.println("firstName    "+objects[3]);
			System.out.println("emailAddress "+objects[5]);
			System.out.println("courseName   "+objects[7]);
		}
	}
}
