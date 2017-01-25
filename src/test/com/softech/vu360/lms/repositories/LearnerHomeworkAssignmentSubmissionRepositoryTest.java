package com.softech.vu360.lms.repositories;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LearnerHomeworkAssignmentSubmission;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LearnerHomeworkAssignmentSubmissionRepositoryTest {
	
	@Inject
	private LearnerHomeworkAssignmentSubmissionRepository lhw;
	
	//@Test
	public void OrganizationalGroup_should_findByNameIgnoreCaseAndCustomerId() {
		LearnerHomeworkAssignmentSubmission lh = lhw.findOne(2L);
		System.out.println(lh.getStatus());
	}

	//@Test
	public void findBylearnerEnrollmentIdOrderByIdDesc() {
		List<LearnerHomeworkAssignmentSubmission> lh = lhw.findBylearnerEnrollmentIdOrderByIdDesc(245935L);
		System.out.println(lh.get(0).getStatus());
	}
	
	
	//@Test
	public void getLearner_Count_ByOrg() {
		
		List<Object[]> learnerGroup = lhw.getHomeWorkAssignmentAssetByEnrollId(245935L);
		HashMap<Long,Long> learnerGroupMap = new HashMap<Long, Long>();
		
		for (int i=0; i< learnerGroup.size(); i++){
			learnerGroupMap.put(Long.valueOf(learnerGroup.get(i)[1].toString()), Long.valueOf(learnerGroup.get(i)[0].toString()));
		}
		
		//LMS-6764 : Set learner count to zero, if not fetched from database
		/*if (learnerGroupMap.size() < lg.length) {
			Long tempCount = null;
			for (int i=0; i< lg.length; i++) {
				tempCount = learnerGroupMap.get( lg[i] );
				if (tempCount == null) {
					learnerGroupMap.put( Long.valueOf(lg[i].toString()) , 0L);
				}
			}
		}*/
	}
}
