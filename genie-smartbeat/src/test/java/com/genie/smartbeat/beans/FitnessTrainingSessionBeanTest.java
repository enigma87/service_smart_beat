package com.genie.smartbeat.beans;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class FitnessTrainingSessionBeanTest {

	@Test
	public void testFitnessTrainingSessionBean() {
		String userid =  "123456_abcd_789";
		String trainingSessionId =  "2013_05_abcd";
		long now = new Date().getTime();
		long nowBeforeTwoDays = now - (2*24*3600000);
		long nowBeforeTwoDaysFourtyMinutes = nowBeforeTwoDays - (2400000);
		Timestamp startTime = new Timestamp(nowBeforeTwoDaysFourtyMinutes);
		Timestamp endTime = new Timestamp(nowBeforeTwoDays);	
		Double hrz1Time = 4.0;
		Double hrz2Time = 8.0;
		Double hrz3Time = 11.0;
		Double hrz4Time = 3.0;
		Double hrz5Time = 10.0;
		Double hrz6Time = 14.0;
		Double hrz1Distance = 660.0;
		Double hrz2Distance = 1426.67;
		Double hrz3Distance = 2090.0;
		Double hrz4Distance = 605.0;
		Double hrz5Distance = 2133.33;
		Double hrz6Distance = 3126.67;
	    Integer surfaceIndex = 2;
	    Double hrz1Speed = (hrz1Distance/hrz1Time)*0.06;
	    Double hrz2Speed = (hrz2Distance/hrz2Time)*0.06;
	    Double hrz3Speed = (hrz3Distance/hrz3Time)*0.06;
	    Double hrz4Speed = (hrz4Distance/hrz4Time)*0.06;
	    Double hrz5Speed = (hrz5Distance/hrz5Time)*0.06;
	    Double hrz6Speed = (hrz6Distance/hrz6Time)*0.06;
	    Double percentageInclination = 25.0;
	    Double percentageDeclination = 25.0;
	    Double vdot = 23.0;
	    
	    FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
	    fitnessTrainingSessionBean.setUserid(userid);
	    fitnessTrainingSessionBean.setTrainingSessionId(trainingSessionId);
	    fitnessTrainingSessionBean.setSurfaceIndex(surfaceIndex);
	    fitnessTrainingSessionBean.setStartTime(startTime);
	    fitnessTrainingSessionBean.setEndTime(endTime);
	    fitnessTrainingSessionBean.setHrz1Time(hrz1Time);
	    fitnessTrainingSessionBean.setHrz2Time(hrz2Time);
	    fitnessTrainingSessionBean.setHrz3Time(hrz3Time);
	    fitnessTrainingSessionBean.setHrz4Time(hrz4Time);
	    fitnessTrainingSessionBean.setHrz5Time(hrz5Time);
	    fitnessTrainingSessionBean.setHrz6Time(hrz6Time);
	    fitnessTrainingSessionBean.setHrz1Distance(hrz1Distance);
	    fitnessTrainingSessionBean.setHrz2Distance(hrz2Distance);
	    fitnessTrainingSessionBean.setHrz3Distance(hrz3Distance);
	    fitnessTrainingSessionBean.setHrz4Distance(hrz4Distance);
	    fitnessTrainingSessionBean.setHrz5Distance(hrz5Distance);
	    fitnessTrainingSessionBean.setHrz6Distance(hrz6Distance);
	    fitnessTrainingSessionBean.setPercentageInclination(percentageInclination);
	    fitnessTrainingSessionBean.setPercentageDeclination(percentageDeclination);
	    fitnessTrainingSessionBean.setVdot(vdot);
	    
	    Assert.assertEquals(userid,fitnessTrainingSessionBean.getUserid());
	    Assert.assertEquals(trainingSessionId,fitnessTrainingSessionBean.getTrainingSessionId());
	    Assert.assertEquals(surfaceIndex,fitnessTrainingSessionBean.getSurfaceIndex());
	    Assert.assertEquals(startTime,fitnessTrainingSessionBean.getStartTime());
	    Assert.assertEquals(endTime,fitnessTrainingSessionBean.getEndTime());
	    Assert.assertEquals(hrz1Time,fitnessTrainingSessionBean.getHrz1Time());
	    Assert.assertEquals(hrz2Time,fitnessTrainingSessionBean.getHrz2Time());
	    Assert.assertEquals(hrz3Time,fitnessTrainingSessionBean.getHrz3Time());
	    Assert.assertEquals(hrz4Time,fitnessTrainingSessionBean.getHrz4Time());
	    Assert.assertEquals(hrz5Time,fitnessTrainingSessionBean.getHrz5Time());
	    Assert.assertEquals(hrz6Time,fitnessTrainingSessionBean.getHrz6Time());
	    Assert.assertEquals(hrz1Distance,fitnessTrainingSessionBean.getHrz1Distance());
	    Assert.assertEquals(hrz2Distance,fitnessTrainingSessionBean.getHrz2Distance());
	    Assert.assertEquals(hrz3Distance,fitnessTrainingSessionBean.getHrz3Distance());
	    Assert.assertEquals(hrz4Distance,fitnessTrainingSessionBean.getHrz4Distance());
	    Assert.assertEquals(hrz5Distance,fitnessTrainingSessionBean.getHrz5Distance());
	    Assert.assertEquals(hrz6Distance,fitnessTrainingSessionBean.getHrz6Distance());
	    Assert.assertEquals(percentageInclination, fitnessTrainingSessionBean.getPercentageInclination());
	    Assert.assertEquals(percentageDeclination, fitnessTrainingSessionBean.getPercentageDeclination());
	    Assert.assertEquals(vdot, fitnessTrainingSessionBean.getVdot());
	    
	    double[] getTimeDistributionOfHRZ = fitnessTrainingSessionBean.getTimeDistributionOfHRZ();
	    Assert.assertEquals(hrz1Time,getTimeDistributionOfHRZ[0]);
	    Assert.assertEquals(hrz2Time,getTimeDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz3Time,getTimeDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz4Time,getTimeDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz5Time,getTimeDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz6Time,getTimeDistributionOfHRZ[5]);
	    
	    double[] getSpeedDistributionOfHRZ = fitnessTrainingSessionBean.getSpeedDistributionOfHRZ();
	    Assert.assertEquals(hrz1Speed,getSpeedDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz2Speed,getSpeedDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz3Speed,getSpeedDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz4Speed,getSpeedDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz5Speed,getSpeedDistributionOfHRZ[5]);
	    Assert.assertEquals(hrz6Speed,getSpeedDistributionOfHRZ[6]);
	    
	}

}
