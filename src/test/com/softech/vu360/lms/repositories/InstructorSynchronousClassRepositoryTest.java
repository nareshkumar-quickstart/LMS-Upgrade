package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.model.SynchronousClass;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LmsTestConfig.class)
public class InstructorSynchronousClassRepositoryTest {

	@Inject
	private InstructorSynchronousClassRepository instructorSynchronousClassRepository;

	// @Test
	public void findBySynchronousClassIdAndInstructorTypeAndfindBySynchronousClassId() {
		String instType = null;
		Long synchId = new Long(2);
		List<InstructorSynchronousClass> objs = new ArrayList();
		List<Long> instrIds = new ArrayList();
		if (StringUtils.isNotBlank(instType)) {
			objs = instructorSynchronousClassRepository
					.findBySynchronousClassIdAndInstructorType(synchId,
							instType);
		} else {
			objs = instructorSynchronousClassRepository
					.findBySynchronousClassId(synchId);
		}

		for (InstructorSynchronousClass obj : objs) {
			instrIds.add(obj.getInstructor().getId());
			System.out.print("*********************** "
					+ obj.getInstructorType());
		}

		System.out.print("*********************** " + instrIds.size());

	}

	// @Test
	public void findBySynchronousClassIdAndInstructorId() {
		InstructorSynchronousClass iSC = instructorSynchronousClassRepository
				.findBySynchronousClassIdAndInstructorId(2l, 653l);
		if (iSC != null) {
			System.out.println(iSC.getInstructorType());
		}

	}

	// @Test
	public void findOne() {
		InstructorSynchronousClass iSC = instructorSynchronousClassRepository
				.findOne(2l);
		if (iSC != null) {
			System.out.println(iSC.getInstructorType());
		}
	}

	// @Test
	// @Transactional
	public void save() {
		/*
		 * @Kaunain - Warning! please use the following method with caution
		 * failure to do so will result in anomally
		 */
		InstructorSynchronousClass iSC = instructorSynchronousClassRepository
				.findOne(2l);
		InstructorSynchronousClass clone = new InstructorSynchronousClass();
		clone.setInstructor(iSC.getInstructor());
		clone.setSynchronousClass(iSC.getSynchronousClass());
		clone.setInstructorType("Lead");
		instructorSynchronousClassRepository.save(clone);

	}

	// @Test
	public void findLeadInstructors() {
		List<InstructorSynchronousClass> iscList = instructorSynchronousClassRepository
				.getLeadInstructorsOfSynchClass(17072l);
		if (iscList != null && iscList.size() > 0) {
			for (InstructorSynchronousClass isc : iscList) {
				System.out.println(isc.getInstructorType());
			}
		}
	}

	// @Test
	public void findSynchronousClassInstructors() {
		List<InstructorSynchronousClass> iscList = instructorSynchronousClassRepository
				.findSynchronousClassInstructors(13454l, "Ramiz", "Uddin",
						"Lead");
		if (iscList != null && iscList.size() > 0) {
			for (InstructorSynchronousClass isc : iscList) {
				System.out.println(isc.getInstructorType());
			}
		}
	}

	// @Test
	public void findByInstructorIdAndSynchronousClassCourseRetired() {
		long instructorId = 707;
		List<InstructorSynchronousClass> instructorSynchronousClasses = instructorSynchronousClassRepository
				.findByInstructorIdAndSynchronousClassCourseRetired(
						instructorId, false);
		List<SynchronousClass> synchronousClasses = new ArrayList<SynchronousClass>();
		if (instructorSynchronousClasses != null
				&& instructorSynchronousClasses.size() > 0) {
			for (InstructorSynchronousClass insSynClass : instructorSynchronousClasses) {
				synchronousClasses.add(insSynClass.getSynchronousClass());
				System.out.println("*********** "
						+ insSynClass.getSynchronousClass().getId());
			}
		}

	}

	@Test
	public void deleteSynchronousClassInstructors() {

		/*
		 * @Kaunain - Warning! please use this method with caution inappropriate
		 * use may result in anomaly
		 */

		long l[] = { 6053l, 6052l };
		List<long[]> list = Arrays.asList(l);

		instructorSynchronousClassRepository.deleteByIdIn(list);
	}
}