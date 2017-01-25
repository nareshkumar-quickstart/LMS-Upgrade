package com.softech.vu360.lms.web.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softech.vu360.lms.model.Commission;
import com.softech.vu360.lms.web.controller.model.CommissionParticipationForm;

public class CommissionParticipationValidator implements Validator 
{

	@Override
	public boolean supports(Class clazz) {
		return clazz.getName().equals(Commission.class.getName());
	}

	@Override
	public void validate(Object obj, Errors errors) 
	{
		CommissionParticipationForm form = (CommissionParticipationForm)obj;
		if(StringUtils.isEmpty(form.getCommissionablePartyId()) )
			errors.rejectValue("commissionablePartyName", "lms.administrator.resellerCommission.addCommissionParticipationForm.name.error.required");
		if(StringUtils.isEmpty(form.getFlat()))
			errors.rejectValue("flat", "lms.administrator.resellerCommission.addCommissionForm.payout.error.required");
		else
		{
			if(Boolean.parseBoolean(form.getFlat()))
			{
				if(StringUtils.isEmpty(form.getFlatFeeAmount()))
					errors.rejectValue("flatFeeAmount", "lms.administrator.resellerCommission.addCommissionForm.flatRate.error.required");
				else
				{
					Double d;
					try
					{
						d = Double.parseDouble(form.getFlatFeeAmount());
						if(d <= 0 || d > 999999.99)
							errors.rejectValue("flatFeeAmount", "lms.administrator.resellerCommission.addCommissionForm.flatRate.error.invalid");
					}
					catch(Exception e)
					{
						errors.rejectValue("flatFeeAmount", "lms.administrator.resellerCommission.addCommissionForm.flatRate.error.invalid");
					}
				}
			}
			else
			{
				if(StringUtils.isEmpty(form.getPercentage()))
					errors.rejectValue("percentage", "lms.administrator.resellerCommission.addCommissionForm.percentage.error.required");
				else
				{
					Double d;
					try
					{
						d = Double.parseDouble(form.getPercentage());
						if(d <= 0 || d > 100)
							errors.rejectValue("percentage", "lms.administrator.resellerCommission.addCommissionForm.percentage.error.invalid");
					}
					catch(Exception e)
					{
						errors.rejectValue("percentage", "lms.administrator.resellerCommission.addCommissionForm.percentage.error.invalid");
					}
				}
			}
		}
		
	}
}
