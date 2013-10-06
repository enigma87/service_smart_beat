package com.genie.smartbeat.beans;

import java.sql.Timestamp;
import java.util.Calendar;

import junit.framework.Assert;

import org.junit.Test;

public class FitnessTrainingSessionIdBeanTest {

	@Test
	public void testFitnessTrainingSessionIdBean() {
		String trainingSessionId =  "2013_05_abcd";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		Timestamp endTime = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE, -40);
		Timestamp startTime = new Timestamp(cal.getTimeInMillis());		
		
		FitnessTrainingSessionIdBean fitnessTrainingSessionIdBean = new FitnessTrainingSessionIdBean();
		fitnessTrainingSessionIdBean.setTrainingSessionId(trainingSessionId);
		fitnessTrainingSessionIdBean.setStartTime(startTime);
		fitnessTrainingSessionIdBean.setEndTime(endTime);
		
		Assert.assertEquals(trainingSessionId, fitnessTrainingSessionIdBean.getTrainingSessionId());
		Assert.assertEquals(startTime, fitnessTrainingSessionIdBean.getStartTime());
		Assert.assertEquals(endTime, fitnessTrainingSessionIdBean.getEndTime());		
	}

}
