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
		Double previousTotalLoadOfExercise = 40.0;
		long nowBeforeTwoDays = now - (2*24*3600000);
		Timestamp previousEndTime = new Timestamp(nowBeforeTwoDays);
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
		fitnessHomeostasisIndexBean.setUserid(userid);
		fitnessHomeostasisIndexBean.setTraineeClassification(traineeClassification);
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(localRegressionMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(recentMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentTotalLoadOfExercise(recentTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setRecentEndTime(recentEndTime);
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(previousTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setPreviousEndTime(previousEndTime);
		
		Assert.assertEquals(userid,fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(traineeClassification,fitnessHomeostasisIndexBean.getTraineeClassification());
		Assert.assertEquals(localRegressionMinimumOfHomeostasisIndex,fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertEquals(recentMinimumOfHomeostasisIndex,fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(recentTotalLoadOfExercise,fitnessHomeostasisIndexBean.getRecentTotalLoadOfExercise());
		Assert.assertEquals(recentEndTime,fitnessHomeostasisIndexBean.getRecentEndTime());
		Assert.assertEquals(previousTotalLoadOfExercise,fitnessHomeostasisIndexBean.getPreviousTotalLoadOfExercise());
		Assert.assertEquals(previousEndTime,fitnessHomeostasisIndexBean.getPreviousEndTime());
	}

}
