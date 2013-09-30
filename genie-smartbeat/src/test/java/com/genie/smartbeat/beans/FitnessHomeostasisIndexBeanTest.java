package com.genie.smartbeat.beans;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class FitnessHomeostasisIndexBeanTest {

	@Test
	public void testFitnessHomeostasisIndexBean() {
		String userid = "123456_abcd_789";
		Integer traineeClassification = 3;
		Double localRegressionMinimumOfHomeostasisIndex = -150.0;
		Double recentMinimumOfHomeostasisIndex = -75.0;
		Double recentTotalLoadOfExercise = 50.0;
		long now = new Date().getTime();
		Timestamp recentEndTime = new Timestamp(now);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
		fitnessHomeostasisIndexBean.setUserid(userid);
		fitnessHomeostasisIndexBean.setTraineeClassification(traineeClassification);
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(localRegressionMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(recentMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentTotalLoadOfExercise(recentTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setRecentEndTime(recentEndTime);
		
		Assert.assertEquals(userid,fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(traineeClassification,fitnessHomeostasisIndexBean.getTraineeClassification());
		Assert.assertEquals(localRegressionMinimumOfHomeostasisIndex,fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertEquals(recentMinimumOfHomeostasisIndex,fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(recentTotalLoadOfExercise,fitnessHomeostasisIndexBean.getRecentTotalLoadOfExercise());
		Assert.assertEquals(recentEndTime,fitnessHomeostasisIndexBean.getRecentEndTime());
		
	}

}
