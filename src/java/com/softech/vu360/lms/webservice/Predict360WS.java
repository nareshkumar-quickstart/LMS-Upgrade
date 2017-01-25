package com.softech.vu360.lms.webservice;

import com.softech.vu360.lms.webservice.message.predict360.SurveyImportPredictRequest;
import com.softech.vu360.lms.webservice.message.predict360.SurveyImportPredictResponse;

public interface Predict360WS extends AbstractWS {
	SurveyImportPredictResponse lmsImportEvent(SurveyImportPredictRequest surveyImportRequest);
}
