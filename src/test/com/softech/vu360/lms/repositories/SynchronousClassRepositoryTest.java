package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.SynchronousClass;

public class SynchronousClassRepositoryTest extends
		SpringJUnitConfigAbstractTest {

	@Inject
	private SynchronousClassRepository synchronousClassRepository;

	// @Test
	public void getById() {

		SynchronousClass synchClass = synchronousClassRepository.findOne(7l);
		if (synchClass != null) {
			System.out.println(synchClass.getKey());
		}
	}

	// @Test
	// @Transactional
	public void saveSynchronousClass() {
		/*
		 * @Kaunain - Warning! -please use or test this method carefully as
		 * uncautioned use may result in anomaly
		 */
		SynchronousClass synchClass = synchronousClassRepository.findOne(5l);
		SynchronousClass synchClass2 = new SynchronousClass();
		//synchClass2.setLocation(synchClass.getLocation());
		synchClass2.setCourse(synchClass.getCourse());
		synchronousClassRepository.save(synchClass2);

	}

	// @Test
	public void findByCourseId() {

		List<SynchronousClass> synchList = synchronousClassRepository
				.findByCourse_Id(6956l);
		if (synchList != null && synchList.size() > 0) {
			for (SynchronousClass synchClass : synchList) {
				System.out.println(synchClass.getKey());
			}
		}

	}

	// @Test
	public void findByCourseIdIN() {
		Vector<Long> ids = new Vector<Long>();
		ids.add(6956l);
		ids.add(7077l);
		List<SynchronousClass> synchList = synchronousClassRepository
				.findByCourse_IdIn(ids);
		if (synchList != null && synchList.size() > 0) {
			for (SynchronousClass synchClass : synchList) {
				System.out.println(synchClass.getKey());
			}
		}
	}

	// @Test
	public void findByGuid() {
		SynchronousClass synchClass = synchronousClassRepository
				.findByGuid("7BB05D2D-8F63-413A-A7D1-F473199FDD5D");
		if (synchClass != null) {
			System.out.println(synchClass.getKey());
		}
	}

	//@Test
	public void findSynchClassesByCourseId() {
		Map<Object, Object> map = synchronousClassRepository
				.findSynchClassesByCourseId(117759l, "i", "", "", 0, 10, "id",
						0, 100);
		if (map != null && map.size() > 0) {
			int count = (Integer) map.get("recordSize");
			@SuppressWarnings("unchecked")
			List<SynchronousClass> classList = (List<SynchronousClass>) map
					.get("list");
			if (classList != null && classList.size() > 0) {
				for (SynchronousClass sC : classList) {
					System.out.println(sC.getKey());
				}
			}
		}
	}

	//@Test
	public void findSynchClassesByCourseIdO() {
		Map<Object, Object> map = synchronousClassRepository
				.findAllSynchClassesByCourseId(117759l, "i", "", "", "id", 0,
						100);
		if (map != null && map.size() > 0) {
			int count = (Integer) map.get("recordSize");
			@SuppressWarnings("unchecked")
			List<SynchronousClass> classList = (List<SynchronousClass>) map
					.get("list");
			if (classList != null && classList.size() > 0) {
				for (SynchronousClass sC : classList) {
					System.out.println(sC.getKey());
				}
			}
		}
	}
	
	//@Test
	public void deleteSynchronousClass(){
		/*
		 * @Kaunain Warning! - Use with caution - inappropriate use may result in anomally
		 */
		List <Long> ids=new ArrayList<Long>(1);
		ids.add(new Long(941850l));
		synchronousClassRepository.deleteByIdIn(ids);
	}
	@Test
	public void findResourceAssociatedSynchronousClass(){
		List<SynchronousClass> lst = synchronousClassRepository.findResourceAssociatedSynchronousClass(1001L);
		for(SynchronousClass sc : lst){
			System.out.println("***********************"+sc.getId());
		}
	}

}
