/**
 * 
 */
package com.softech.vu360.lms.webservice.client;

import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.Learner;

/**
 * @author sana.majeed
 *
 */
public interface VcsDiscussionForumClientWS extends ClientAbstractWS {
	
	// Forum specific
	public boolean createForumEvent ( DiscussionForumCourse dfCourse );
	public boolean updateForumEvent ( DiscussionForumCourse dfCourse );
	public boolean deleteForumEvent ( String courseGUID );
	
	// User specific
	public boolean registerInstructorEvent ( Instructor instructor, String courseGUID );
	public boolean revokeInstructorPermissionsEvent ( String instructorGUID, String courseGUID );
	public boolean registerLearnerEvent ( Learner learner, String courseGUID );
	public String getUserAvatarNameEvent ( String userGUID );	
	public boolean saveUserAvatarEvent(String userGUID, String avatar, int avatarWidth, int avatarHeight);	
}
