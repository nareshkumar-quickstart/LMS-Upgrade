package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.model.survey.Survey;

public class AdminSurveyApprovalForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;

		private Survey survey;
		private long surveyId = 0;
		private long learnerId = 0;
		private VU360User user;

		public Survey getSurvey() {
			return survey;
		}
		public void setSurvey(Survey survey) {
			this.survey = survey;
		}
		/**
		 * @return the learnerId
		 */
		public long getLearnerId() {
			return learnerId;
		}
		/**
		 * @param learnerId the learnerId to set
		 */
		public void setLearnerId(long learnerId) {
			this.learnerId = learnerId;
		}
		/**
		 * @return the user
		 */
		public VU360User getUser() {
			return user;
		}
		/**
		 * @param user the user to set
		 */
		public void setUser(VU360User user) {
			this.user = user;
		}
		/**
		 * @return the surveyId
		 */
		public long getSurveyId() {
			return surveyId;
		}
		/**
		 * @param surveyId the surveyId to set
		 */
		public void setSurveyId(long surveyId) {
			this.surveyId = surveyId;
		}

}
