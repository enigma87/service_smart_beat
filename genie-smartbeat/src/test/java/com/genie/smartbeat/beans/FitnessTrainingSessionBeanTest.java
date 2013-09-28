package com.genie.smartbeat.beans;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.genie.smartbeat.core.errors.TrainingSessionErrors;

@SuppressWarnings("unused")
public class FitnessTrainingSessionBeanTest {

	@Test
	public void testFitnessTrainingSessionBean() {
		String userid =  "123456_abcd_789";
		String trainingSessionId =  "2013_05_abcd";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		Timestamp endTime = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE, -40);
		Timestamp startTime = new Timestamp(cal.getTimeInMillis());					
		
		Integer surfaceIndex = 2;
	    Double vdot = 23.0;
	    Integer sessionStressPerceptionIndex = 3;
	    Integer muscleStatePerceptionIndex = 3;
	    Integer healthPerceptionIndex = 3;

	    
	    FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
	    Assert.assertFalse(fitnessTrainingSessionBean.isValidForTableInsert());

	    fitnessTrainingSessionBean.setUserid("");
	    fitnessTrainingSessionBean.setTrainingSessionId("");
	    Assert.assertFalse(fitnessTrainingSessionBean.isValidForTableInsert());
	    
	    fitnessTrainingSessionBean.setUserid("test");
	    fitnessTrainingSessionBean.setTrainingSessionId("");
	    Assert.assertFalse(fitnessTrainingSessionBean.isValidForTableInsert());
	    
	    fitnessTrainingSessionBean.setUserid("");
	    fitnessTrainingSessionBean.setTrainingSessionId("test");
	    Assert.assertFalse(fitnessTrainingSessionBean.isValidForTableInsert());
	    
	    fitnessTrainingSessionBean.setUserid(userid);
	    fitnessTrainingSessionBean.setTrainingSessionId(trainingSessionId);
	    fitnessTrainingSessionBean.setSurfaceIndex(surfaceIndex);
	    fitnessTrainingSessionBean.setStartTime(startTime);
	    fitnessTrainingSessionBean.setEndTime(endTime);

	    fitnessTrainingSessionBean.setVdot(vdot);
	    fitnessTrainingSessionBean.setHealthPerceptionIndex(healthPerceptionIndex);
	    fitnessTrainingSessionBean.setMuscleStatePerceptionIndex(muscleStatePerceptionIndex);
	    fitnessTrainingSessionBean.setSessionStressPerceptionIndex(sessionStressPerceptionIndex);
	    
	    Assert.assertEquals(userid,fitnessTrainingSessionBean.getUserid());
	    Assert.assertEquals(trainingSessionId,fitnessTrainingSessionBean.getTrainingSessionId());
	    Assert.assertEquals(surfaceIndex,fitnessTrainingSessionBean.getSurfaceIndex());
	    Assert.assertEquals(startTime,fitnessTrainingSessionBean.getStartTime());
	    Assert.assertEquals(endTime,fitnessTrainingSessionBean.getEndTime());
	    Assert.assertEquals(vdot, fitnessTrainingSessionBean.getVdot());
	    Assert.assertEquals(sessionStressPerceptionIndex, fitnessTrainingSessionBean.getSessionStressPerceptionIndex());
	    Assert.assertEquals(muscleStatePerceptionIndex, fitnessTrainingSessionBean.getMuscleStatePerceptionIndex());
	    Assert.assertEquals(healthPerceptionIndex, fitnessTrainingSessionBean.getHealthPerceptionIndex());

	    Assert.assertTrue(fitnessTrainingSessionBean.isValidForTableInsert());
	    
	}
	
	@Test
	public void testGetDuration(){
		Calendar cal = Calendar.getInstance();
		
		/*null timestamps*/
		FitnessTrainingSessionBean bean = new FitnessTrainingSessionBean();
		Assert.assertEquals(Math.round(0*100), Math.round(bean.getSessionDuration()));
		
		/*null start timestamp*/		
		Timestamp endTime = new Timestamp(cal.getTimeInMillis());
		bean.setEndTime(endTime);
		bean.setStartTime(null);
		Assert.assertEquals(Math.round(0*100), Math.round(bean.getSessionDuration()));
		
		/*null end timestamp*/		
		Timestamp startTime = new Timestamp(cal.getTimeInMillis());
		bean.setStartTime(startTime);
		bean.setEndTime(null);		
		Assert.assertEquals(Math.round(0*100), Math.round(bean.getSessionDuration()));
		
		/*correct 40 min duration*/
		cal.add(Calendar.DATE, -2);
		endTime = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE, -40);
		startTime = new Timestamp(cal.getTimeInMillis());
		bean.setStartTime(startTime);
		bean.setEndTime(endTime);
		Assert.assertEquals(Math.round(40*100), Math.round(bean.getSessionDuration()*100));
		
		/*incorrect 0 min duration*/
		cal.add(Calendar.MINUTE, 40);
		endTime = new Timestamp(cal.getTimeInMillis());
		bean.setEndTime(startTime);
		Assert.assertEquals(Math.round(0*100), Math.round(bean.getSessionDuration()));
		
		/*incorrect -40 min duration*/
		cal.add(Calendar.MINUTE, 40);
		endTime = new Timestamp(cal.getTimeInMillis());
		bean.setEndTime(startTime);
		Assert.assertEquals(Math.round(0*100), Math.round(bean.getSessionDuration()));
	}
	
	@Test
	public void testGetAsDoubleValueExtraLoad(){
		FitnessTrainingSessionBean bean = new FitnessTrainingSessionBean();
		Assert.assertEquals(Math.round(0*100), Math.round(bean.getAsDoubleValueExtraLoad()*100));
		bean.setExtraLoad(new Double(3));
		Assert.assertEquals(Math.round(3*100), Math.round(bean.getAsDoubleValueExtraLoad()*100));
	}
	
	@Test
	public void testGetAsDoubleValueAverageAltitude(){
		FitnessTrainingSessionBean bean = new FitnessTrainingSessionBean();
		Assert.assertEquals(Math.round(0*100), Math.round(bean.getAsDoubleValueAverageAltitude()*100));
		bean.setAverageAltitude(new Double(3));
		Assert.assertEquals(Math.round(3*100), Math.round(bean.getAsDoubleValueAverageAltitude()*100));
	}
	
	@Test
	public void testGetTimeDistributionOfHRZ(){
		FitnessTrainingSessionBean bean = new FitnessTrainingSessionBean();
		Assert.assertNull(bean.getTimeDistributionOfHRZ());
		
		bean.setHrz1Time(0.0);
	    bean.setHrz2Time(0.0);
	    bean.setHrz3Time(0.0);
	    bean.setHrz4Time(0.0);
	    bean.setHrz5Time(0.0);
	    bean.setHrz6Time(0.0);
	    Assert.assertNull(bean.getTimeDistributionOfHRZ());
	    
		
		Double hrz1Time = 4.0;
		Double hrz2Time = 8.0;
		Double hrz3Time = 11.0;
		Double hrz4Time = 3.0;
		Double hrz5Time = 10.0;
		Double hrz6Time = 14.0;		
	    bean.setHrz1Time(hrz1Time);
	    bean.setHrz2Time(hrz2Time);
	    bean.setHrz3Time(hrz3Time);
	    bean.setHrz4Time(hrz4Time);
	    bean.setHrz5Time(hrz5Time);
	    bean.setHrz6Time(hrz6Time);	    
	    Assert.assertEquals(hrz1Time,bean.getHrz1Time());
	    Assert.assertEquals(hrz2Time,bean.getHrz2Time());
	    Assert.assertEquals(hrz3Time,bean.getHrz3Time());
	    Assert.assertEquals(hrz4Time,bean.getHrz4Time());
	    Assert.assertEquals(hrz5Time,bean.getHrz5Time());
	    Assert.assertEquals(hrz6Time,bean.getHrz6Time());
	    
	    double[] getTimeDistributionOfHRZ = bean.getTimeDistributionOfHRZ();
	    Assert.assertEquals(hrz1Time,getTimeDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz2Time,getTimeDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz3Time,getTimeDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz4Time,getTimeDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz5Time,getTimeDistributionOfHRZ[5]);
	    Assert.assertEquals(hrz6Time,getTimeDistributionOfHRZ[6]);
	}
	
	@Test
	public void testGetSpeedDistributionOfHRZ(){
		FitnessTrainingSessionBean bean = new FitnessTrainingSessionBean();
		Assert.assertNull(bean.getSpeedDistributionOfHRZ());
		
		bean.setHrz1Time(0.0);
	    bean.setHrz2Time(0.0);
	    bean.setHrz3Time(0.0);
	    bean.setHrz4Time(0.0);
	    bean.setHrz5Time(0.0);
	    bean.setHrz6Time(0.0);
	    Assert.assertNull(bean.getSpeedDistributionOfHRZ());
		
		
	    Double hrz1Distance = 660.0;
		Double hrz2Distance = 1426.67;
		Double hrz3Distance = 2090.0;
		Double hrz4Distance = 605.0;
		Double hrz5Distance = 2133.33;
		Double hrz6Distance = 3126.67;
		bean.setHrz1Distance(hrz1Distance);
		bean.setHrz2Distance(hrz2Distance);
		bean.setHrz3Distance(hrz3Distance);
		bean.setHrz4Distance(hrz4Distance);
		bean.setHrz5Distance(hrz5Distance);
		bean.setHrz6Distance(hrz6Distance);
		Assert.assertEquals(hrz1Distance,bean.getHrz1Distance());
		Assert.assertEquals(hrz2Distance,bean.getHrz2Distance());
		Assert.assertEquals(hrz3Distance,bean.getHrz3Distance());
		Assert.assertEquals(hrz4Distance,bean.getHrz4Distance());
		Assert.assertEquals(hrz5Distance,bean.getHrz5Distance());
		Assert.assertEquals(hrz6Distance,bean.getHrz6Distance());
		
		Double hrz1Time = 4.0;
		Double hrz2Time = 8.0;
		Double hrz3Time = 11.0;
		Double hrz4Time = 3.0;
		Double hrz5Time = 10.0;
		Double hrz6Time = 14.0;
		bean.setHrz1Time(hrz1Time);
	    bean.setHrz2Time(hrz2Time);
	    bean.setHrz3Time(hrz3Time);
	    bean.setHrz4Time(hrz4Time);
	    bean.setHrz5Time(hrz5Time);
	    bean.setHrz6Time(hrz6Time);
	    	    
		Double hrz1Speed = (hrz1Distance/hrz1Time)*0.06;
	    Double hrz2Speed = (hrz2Distance/hrz2Time)*0.06;
	    Double hrz3Speed = (hrz3Distance/hrz3Time)*0.06;
	    Double hrz4Speed = (hrz4Distance/hrz4Time)*0.06;
	    Double hrz5Speed = (hrz5Distance/hrz5Time)*0.06;
	    Double hrz6Speed = (hrz6Distance/hrz6Time)*0.06;
	    
	    double[] getSpeedDistributionOfHRZ = bean.getSpeedDistributionOfHRZ();
	    Assert.assertEquals(hrz1Speed,getSpeedDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz2Speed,getSpeedDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz3Speed,getSpeedDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz4Speed,getSpeedDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz5Speed,getSpeedDistributionOfHRZ[5]);
	    Assert.assertEquals(hrz6Speed,getSpeedDistributionOfHRZ[6]);
	    
	    hrz1Time = 0.0;
		hrz2Time = 8.0;
		hrz3Time = 11.0;
		hrz4Time = 3.0;
		hrz5Time = 10.0;
		hrz6Time = 14.0;
		bean.setHrz1Time(hrz1Time);
	    bean.setHrz2Time(hrz2Time);
	    bean.setHrz3Time(hrz3Time);
	    bean.setHrz4Time(hrz4Time);
	    bean.setHrz5Time(hrz5Time);
	    bean.setHrz6Time(hrz6Time);
		
		hrz1Speed = 0.0;
	    hrz2Speed = (hrz2Distance/hrz2Time)*0.06;
	    hrz3Speed = (hrz3Distance/hrz3Time)*0.06;
	    hrz4Speed = (hrz4Distance/hrz4Time)*0.06;
	    hrz5Speed = (hrz5Distance/hrz5Time)*0.06;
	    hrz6Speed = (hrz6Distance/hrz6Time)*0.06;
	    
	    getSpeedDistributionOfHRZ = bean.getSpeedDistributionOfHRZ();
	    Assert.assertEquals(hrz1Speed,getSpeedDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz2Speed,getSpeedDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz3Speed,getSpeedDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz4Speed,getSpeedDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz5Speed,getSpeedDistributionOfHRZ[5]);
	    Assert.assertEquals(hrz6Speed,getSpeedDistributionOfHRZ[6]);
	    
	    hrz1Time = 4.0;
		hrz2Time = 0.0;
		hrz3Time = 11.0;
		hrz4Time = 3.0;
		hrz5Time = 10.0;
		hrz6Time = 14.0;
		bean.setHrz1Time(hrz1Time);
	    bean.setHrz2Time(hrz2Time);
	    bean.setHrz3Time(hrz3Time);
	    bean.setHrz4Time(hrz4Time);
	    bean.setHrz5Time(hrz5Time);
	    bean.setHrz6Time(hrz6Time);
		
		hrz1Speed = (hrz1Distance/hrz1Time)*0.06;
	    hrz2Speed = 0.0;
	    hrz3Speed = (hrz3Distance/hrz3Time)*0.06;
	    hrz4Speed = (hrz4Distance/hrz4Time)*0.06;
	    hrz5Speed = (hrz5Distance/hrz5Time)*0.06;
	    hrz6Speed = (hrz6Distance/hrz6Time)*0.06;
	    
	    getSpeedDistributionOfHRZ = bean.getSpeedDistributionOfHRZ();
	    Assert.assertEquals(hrz1Speed,getSpeedDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz2Speed,getSpeedDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz3Speed,getSpeedDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz4Speed,getSpeedDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz5Speed,getSpeedDistributionOfHRZ[5]);
	    Assert.assertEquals(hrz6Speed,getSpeedDistributionOfHRZ[6]);

	    hrz1Time = 4.0;
		hrz2Time = 8.0;
		hrz3Time = 0.0;
		hrz4Time = 3.0;
		hrz5Time = 10.0;
		hrz6Time = 14.0;
		bean.setHrz1Time(hrz1Time);
	    bean.setHrz2Time(hrz2Time);
	    bean.setHrz3Time(hrz3Time);
	    bean.setHrz4Time(hrz4Time);
	    bean.setHrz5Time(hrz5Time);
	    bean.setHrz6Time(hrz6Time);
		
		hrz1Speed = (hrz1Distance/hrz1Time)*0.06;
	    hrz2Speed = (hrz2Distance/hrz2Time)*0.06;
	    hrz3Speed = 0.0;
	    hrz4Speed = (hrz4Distance/hrz4Time)*0.06;
	    hrz5Speed = (hrz5Distance/hrz5Time)*0.06;
	    hrz6Speed = (hrz6Distance/hrz6Time)*0.06;
	    
	    getSpeedDistributionOfHRZ = bean.getSpeedDistributionOfHRZ();
	    Assert.assertEquals(hrz1Speed,getSpeedDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz2Speed,getSpeedDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz3Speed,getSpeedDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz4Speed,getSpeedDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz5Speed,getSpeedDistributionOfHRZ[5]);
	    Assert.assertEquals(hrz6Speed,getSpeedDistributionOfHRZ[6]);

	    hrz1Time = 4.0;
		hrz2Time = 8.0;
		hrz3Time = 11.0;
		hrz4Time = 0.0;
		hrz5Time = 10.0;
		hrz6Time = 14.0;
		bean.setHrz1Time(hrz1Time);
	    bean.setHrz2Time(hrz2Time);
	    bean.setHrz3Time(hrz3Time);
	    bean.setHrz4Time(hrz4Time);
	    bean.setHrz5Time(hrz5Time);
	    bean.setHrz6Time(hrz6Time);
		
		hrz1Speed = (hrz1Distance/hrz1Time)*0.06;
	    hrz2Speed = (hrz2Distance/hrz2Time)*0.06;
	    hrz3Speed = (hrz3Distance/hrz3Time)*0.06;
	    hrz4Speed = 0.0;
	    hrz5Speed = (hrz5Distance/hrz5Time)*0.06;
	    hrz6Speed = (hrz6Distance/hrz6Time)*0.06;
	    
	    getSpeedDistributionOfHRZ = bean.getSpeedDistributionOfHRZ();
	    Assert.assertEquals(hrz1Speed,getSpeedDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz2Speed,getSpeedDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz3Speed,getSpeedDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz4Speed,getSpeedDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz5Speed,getSpeedDistributionOfHRZ[5]);
	    Assert.assertEquals(hrz6Speed,getSpeedDistributionOfHRZ[6]);
	    
	    hrz1Time = 4.0;
		hrz2Time = 8.0;
		hrz3Time = 11.0;
		hrz4Time = 3.0;
		hrz5Time = 0.0;
		hrz6Time = 14.0;
		bean.setHrz1Time(hrz1Time);
	    bean.setHrz2Time(hrz2Time);
	    bean.setHrz3Time(hrz3Time);
	    bean.setHrz4Time(hrz4Time);
	    bean.setHrz5Time(hrz5Time);
	    bean.setHrz6Time(hrz6Time);
		
		hrz1Speed = (hrz1Distance/hrz1Time)*0.06;
	    hrz2Speed = (hrz2Distance/hrz2Time)*0.06;
	    hrz3Speed = (hrz3Distance/hrz3Time)*0.06;
	    hrz4Speed = (hrz4Distance/hrz4Time)*0.06;
	    hrz5Speed = 0.0;
	    hrz6Speed = (hrz6Distance/hrz6Time)*0.06;
	    
	    getSpeedDistributionOfHRZ = bean.getSpeedDistributionOfHRZ();
	    Assert.assertEquals(hrz1Speed,getSpeedDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz2Speed,getSpeedDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz3Speed,getSpeedDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz4Speed,getSpeedDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz5Speed,getSpeedDistributionOfHRZ[5]);
	    Assert.assertEquals(hrz6Speed,getSpeedDistributionOfHRZ[6]);

	    hrz1Time = 4.0;
		hrz2Time = 8.0;
		hrz3Time = 11.0;
		hrz4Time = 3.0;
		hrz5Time = 10.0;
		hrz6Time = 0.0;
		bean.setHrz1Time(hrz1Time);
	    bean.setHrz2Time(hrz2Time);
	    bean.setHrz3Time(hrz3Time);
	    bean.setHrz4Time(hrz4Time);
	    bean.setHrz5Time(hrz5Time);
	    bean.setHrz6Time(hrz6Time);
		
		hrz1Speed = (hrz1Distance/hrz1Time)*0.06;
	    hrz2Speed = (hrz2Distance/hrz2Time)*0.06;
	    hrz3Speed = (hrz3Distance/hrz3Time)*0.06;
	    hrz4Speed = (hrz4Distance/hrz4Time)*0.06;
	    hrz5Speed = (hrz5Distance/hrz5Time)*0.06;
	    hrz6Speed = 0.0;
	    
	    getSpeedDistributionOfHRZ = bean.getSpeedDistributionOfHRZ();
	    Assert.assertEquals(hrz1Speed,getSpeedDistributionOfHRZ[1]);
	    Assert.assertEquals(hrz2Speed,getSpeedDistributionOfHRZ[2]);
	    Assert.assertEquals(hrz3Speed,getSpeedDistributionOfHRZ[3]);
	    Assert.assertEquals(hrz4Speed,getSpeedDistributionOfHRZ[4]);
	    Assert.assertEquals(hrz5Speed,getSpeedDistributionOfHRZ[5]);
	    Assert.assertEquals(hrz6Speed,getSpeedDistributionOfHRZ[6]);


	}
}
