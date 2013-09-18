package com.genie.smartbeat.regression;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessSpeedHeartRateBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessSpeedHeartRateDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;

public class TrainingSessionRegression {
  
	
	@Autowired AbstractApplicationContext smartbeatContext;	
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	
	
	@Before
	public void setUpBeforeClass() throws Exception 
	{
		smartbeatContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		smartbeatContext.registerShutdownHook();
	}
	
	@Test
	public void testSaveFitnessTrainingSession10(){
		
		long now = new Date().getTime();
		long nowBeforeTwentyEightHours = now - (28*3600000);
		long nowBeforeTwentyEightHoursAfterFourtyMinutes = nowBeforeTwentyEightHours+(2400000);
		long nowPastFourHours = now - (4*3600000);
		long nowPastFourHoursTwentyFiveMinutes  = nowPastFourHours - 1500000;
		long twoDaysAfterNow = now + (2*24*3600000);
		long twoDaysAfterNowBeforeThirtyTwoMinutes = twoDaysAfterNow - (1920000);
	
	   
		/*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = dateFormat.parse("10/06/2012");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		long time = date.getTime();
		currentnew Timestamp(time);*/
		
		/*Creating the Bean for the first Training Session*/	
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid(userid);
		//fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastFourtyMinutes));
		//fitnessTrainingSessionBean.setEndTime(new Timestamp(now));
		fitnessTrainingSessionBean.setStartTime(new Timestamp(nowBeforeTwentyEightHours));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(nowBeforeTwentyEightHoursAfterFourtyMinutes));
		fitnessTrainingSessionBean.setHrz1Time(12.0);
		fitnessTrainingSessionBean.setHrz2Time(14.0);
		fitnessTrainingSessionBean.setHrz3Time(8.0);
		fitnessTrainingSessionBean.setHrz4Time(6.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz1Distance(1260.0);
		fitnessTrainingSessionBean.setHrz2Distance(1680.0);
		fitnessTrainingSessionBean.setHrz3Distance(1120.0);
		fitnessTrainingSessionBean.setHrz4Distance(990.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);
			
        /*Saving the first Fitness training session for the user*/
		FitnessManager fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		String trainingSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
		
		
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean = fitnessShapeIndexDAO.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean.getUserid());
		Assert.assertEquals(28.81, fitnessSpeedHeartRateBean.getCurrentVdot());
		Assert.assertNull(null, fitnessSpeedHeartRateBean.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean.getUserid());
		Assert.assertEquals(100.0, fitnessShapeIndexBean.getShapeIndex());
		Assert.assertEquals(trainingSessionId, fitnessShapeIndexBean.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(46.0, fitnessHomeostasisIndexBean.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-46.0, fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-46.0, fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean.getRecentEndTime());
	

		/*Creating the Bean for the second Training Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean1 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean1.setUserid(userid);
	    fitnessTrainingSessionBean1.setStartTime(new Timestamp(nowPastFourHoursTwentyFiveMinutes));
		fitnessTrainingSessionBean1.setEndTime(new Timestamp(nowPastFourHours));
		fitnessTrainingSessionBean1.setHrz1Time(10.0);
		fitnessTrainingSessionBean1.setHrz2Time(11.0);
		fitnessTrainingSessionBean1.setHrz3Time(2.0);
		fitnessTrainingSessionBean1.setHrz4Time(2.0);
		fitnessTrainingSessionBean1.setHrz5Time(0.0);
		fitnessTrainingSessionBean1.setHrz6Time(0.0);
		fitnessTrainingSessionBean1.setHrz1Distance(1016.67);
		fitnessTrainingSessionBean1.setHrz2Distance(1338.33);
		fitnessTrainingSessionBean1.setHrz3Distance(286.67);
		fitnessTrainingSessionBean1.setHrz4Distance(313.33);
		fitnessTrainingSessionBean1.setHrz5Distance(0.0);
		fitnessTrainingSessionBean1.setHrz6Distance(0.0);
		
		/*Saving the second Fitness training session for the user*/
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean1);
		String trainingSessionId1 = fitnessTrainingSessionBean1.getTrainingSessionId();
	
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO1 = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO1 = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO1 = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean1 = fitnessSpeedHeartRateDAO1.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean1 = fitnessShapeIndexDAO1.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean1 = fitnessHomeostasisIndexDAO1.getHomeostasisIndexModelByUserid(userid);
		
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean1.getUserid());
		Assert.assertEquals(28.58, fitnessSpeedHeartRateBean1.getCurrentVdot());
		Assert.assertEquals(28.81, fitnessSpeedHeartRateBean1.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean1.getUserid());
		Assert.assertEquals(100.51, fitnessShapeIndexBean1.getShapeIndex());
		Assert.assertEquals(trainingSessionId1, fitnessShapeIndexBean1.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean1.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean1.getUserid());
		Assert.assertEquals(20.5, fitnessHomeostasisIndexBean1.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-20.5, fitnessHomeostasisIndexBean1.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-20.5, fitnessHomeostasisIndexBean1.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean1.getRecentEndTime());
		
		/*Creating the bean for the third Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean3 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean3.setUserid(userid);
		//fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastFourtyMinutes));
		//fitnessTrainingSessionBean.setEndTime(new Timestamp(now));
		fitnessTrainingSessionBean3.setStartTime(new Timestamp(twoDaysAfterNowBeforeThirtyTwoMinutes));
		fitnessTrainingSessionBean3.setEndTime(new Timestamp(twoDaysAfterNow));
		fitnessTrainingSessionBean3.setHrz1Time(10.0);
		fitnessTrainingSessionBean3.setHrz2Time(22.0);
		fitnessTrainingSessionBean3.setHrz3Time(0.0);
		fitnessTrainingSessionBean3.setHrz4Time(0.0);
		fitnessTrainingSessionBean3.setHrz5Time(0.0);
		fitnessTrainingSessionBean3.setHrz6Time(0.0);
		fitnessTrainingSessionBean3.setHrz1Distance(1033.33);
		fitnessTrainingSessionBean3.setHrz2Distance(2640.0);
		fitnessTrainingSessionBean3.setHrz3Distance(0.0);
		fitnessTrainingSessionBean3.setHrz4Distance(0.0);
		fitnessTrainingSessionBean3.setHrz5Distance(0.0);
		fitnessTrainingSessionBean3.setHrz6Distance(0.0);
			
        /*Saving the first Fitness training session for the user*/
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean3);
		String trainingSessionId2 = fitnessTrainingSessionBean3.getTrainingSessionId();
		
		
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO2 = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO2 = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO2 = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean2 = fitnessSpeedHeartRateDAO2.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean2 = fitnessShapeIndexDAO2.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean2 = fitnessHomeostasisIndexDAO2.getHomeostasisIndexModelByUserid(userid);
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean2.getUserid());
		Assert.assertEquals(26.06, fitnessSpeedHeartRateBean2.getCurrentVdot());
		Assert.assertEquals(28.58, fitnessSpeedHeartRateBean2.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean2.getUserid());
		Assert.assertEquals(99.71, fitnessShapeIndexBean2.getShapeIndex());
		Assert.assertEquals(trainingSessionId2, fitnessShapeIndexBean2.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean2.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean2.getUserid());
		Assert.assertEquals(22.0, fitnessHomeostasisIndexBean2.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-26.3884, fitnessHomeostasisIndexBean2.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-26.3884, fitnessHomeostasisIndexBean2.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean2.getRecentEndTime());
				
		
		
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		fitnessSpeedHeartRateDAO1.deleteSpeedHeartRateModelByUserid(userid);
		fitnessShapeIndexDAO1.deleteShapeIndexHistoryForUser(userid);
		fitnessHomeostasisIndexDAO1.deleteHomeostasisIndexModelByUserid(userid);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId1);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId2);
		
	}


	
	@Test
	public void testSaveFitnessTrainingSession00(){
		
		long now = new Date().getTime();
		long nowBeforeTwoDays = now - (2*24*3600000);
		long nowBeforeOneDay = now - (24*3600000);
		long nowBeforeOneDayFourtyMinutes = nowBeforeTwoDays - (2400000);
		long nowPastFourHours = now - (4*3600000);
		long nowPastFourHoursThirtyMinutes = nowPastFourHours - 1800000;
		long twoDaysAfterNow = now + (2*24*3600000);
		long twoDaysMinusOneHourThirtyOneMinutesAfterNow = twoDaysAfterNow - (5460000);
		
	   
		/*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = dateFormat.parse("10/06/2012");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = date.getTime();
		currentnew Timestamp(time);*/
		
		/*Creating the Bean for the first Training Session*/	
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid(userid);
		//fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastFourtyMinutes));
		//fitnessTrainingSessionBean.setEndTime(new Timestamp(now));
		fitnessTrainingSessionBean.setStartTime(new Timestamp(nowBeforeOneDayFourtyMinutes));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(nowBeforeOneDay));
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz2Time(8.0);
		fitnessTrainingSessionBean.setHrz3Time(11.0);
		fitnessTrainingSessionBean.setHrz4Time(3.0);
		fitnessTrainingSessionBean.setHrz5Time(10.0);
		fitnessTrainingSessionBean.setHrz6Time(14.0);
		fitnessTrainingSessionBean.setHrz1Distance(660.0);
		fitnessTrainingSessionBean.setHrz2Distance(1426.67);
		fitnessTrainingSessionBean.setHrz3Distance(2090.0);
		fitnessTrainingSessionBean.setHrz4Distance(605.0);
		fitnessTrainingSessionBean.setHrz5Distance(2133.33);
		fitnessTrainingSessionBean.setHrz6Distance(3126.67);
			
        /*Saving the first Fitness training session for the user*/
		FitnessManager fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		String trainingSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
		
		
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean = fitnessShapeIndexDAO.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean.getUserid());
		Assert.assertEquals(42.94, fitnessSpeedHeartRateBean.getCurrentVdot());
		Assert.assertNull(null, fitnessSpeedHeartRateBean.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean.getUserid());
		Assert.assertEquals(100.0, fitnessShapeIndexBean.getShapeIndex());
		Assert.assertEquals(trainingSessionId, fitnessShapeIndexBean.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(212.25, fitnessHomeostasisIndexBean.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-212.25, fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-212.25, fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean.getRecentEndTime());
	

		/*Creating the Bean for the second Training Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean1 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean1.setUserid(userid);
	    fitnessTrainingSessionBean1.setStartTime(new Timestamp(nowPastFourHoursThirtyMinutes));
		fitnessTrainingSessionBean1.setEndTime(new Timestamp(nowPastFourHours));
		fitnessTrainingSessionBean1.setHrz1Time(4.0);
		fitnessTrainingSessionBean1.setHrz2Time(6.0);
		fitnessTrainingSessionBean1.setHrz3Time(5.0);
		fitnessTrainingSessionBean1.setHrz4Time(12.0);
		fitnessTrainingSessionBean1.setHrz5Time(3.0);
		fitnessTrainingSessionBean1.setHrz6Time(0.0);
		fitnessTrainingSessionBean1.setHrz1Distance(626.67);
		fitnessTrainingSessionBean1.setHrz2Distance(1090.0);
		fitnessTrainingSessionBean1.setHrz3Distance(966.67);
		fitnessTrainingSessionBean1.setHrz4Distance(2500.0);
		fitnessTrainingSessionBean1.setHrz5Distance(630.0);
		fitnessTrainingSessionBean1.setHrz6Distance(0.0);
		
		/*Saving the second Fitness training session for the user*/
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean1);
		String trainingSessionId1 = fitnessTrainingSessionBean1.getTrainingSessionId();
	
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO1 = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO1 = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO1 = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean1 = fitnessSpeedHeartRateDAO1.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean1 = fitnessShapeIndexDAO1.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean1 = fitnessHomeostasisIndexDAO1.getHomeostasisIndexModelByUserid(userid);
		
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean1.getUserid());
		Assert.assertEquals(43.82, fitnessSpeedHeartRateBean1.getCurrentVdot());
		Assert.assertEquals(42.94, fitnessSpeedHeartRateBean1.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean1.getUserid());
		Assert.assertEquals(100.0, fitnessShapeIndexBean1.getShapeIndex());
		Assert.assertEquals(trainingSessionId1, fitnessShapeIndexBean1.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean1.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean1.getUserid());
		Assert.assertEquals(65.75, fitnessHomeostasisIndexBean1.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-177.9992, fitnessHomeostasisIndexBean1.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-212.25, fitnessHomeostasisIndexBean1.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean1.getRecentEndTime());
		
		/*Creating the bean for the third Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean3 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean3.setUserid(userid);
		//fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastFourtyMinutes));
		//fitnessTrainingSessionBean.setEndTime(new Timestamp(now));
		fitnessTrainingSessionBean3.setStartTime(new Timestamp(twoDaysMinusOneHourThirtyOneMinutesAfterNow));
		fitnessTrainingSessionBean3.setEndTime(new Timestamp(twoDaysAfterNow));
		fitnessTrainingSessionBean3.setHrz1Time(32.0);
		fitnessTrainingSessionBean3.setHrz2Time(43.0);
		fitnessTrainingSessionBean3.setHrz3Time(1.0);
		fitnessTrainingSessionBean3.setHrz4Time(3.0);
		fitnessTrainingSessionBean3.setHrz5Time(12.0);
		fitnessTrainingSessionBean3.setHrz6Time(0.0);
		fitnessTrainingSessionBean3.setHrz1Distance(5360.0);
		fitnessTrainingSessionBean3.setHrz2Distance(7811.67);
		fitnessTrainingSessionBean3.setHrz3Distance(191.67);
		fitnessTrainingSessionBean3.setHrz4Distance(627.5);
		fitnessTrainingSessionBean3.setHrz5Distance(2550.0);
		fitnessTrainingSessionBean3.setHrz6Distance(0.0);
			
        /*Saving the first Fitness training session for the user*/
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean3);
		String trainingSessionId2 = fitnessTrainingSessionBean3.getTrainingSessionId();
		
		
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO2 = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO2 = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO2 = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean2 = fitnessSpeedHeartRateDAO2.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean2 = fitnessShapeIndexDAO2.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean2 = fitnessHomeostasisIndexDAO2.getHomeostasisIndexModelByUserid(userid);
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean2.getUserid());
		Assert.assertEquals(43.92, fitnessSpeedHeartRateBean2.getCurrentVdot());
		Assert.assertEquals(43.82, fitnessSpeedHeartRateBean2.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean2.getUserid());
		Assert.assertEquals(102.05, fitnessShapeIndexBean2.getShapeIndex());
		Assert.assertEquals(trainingSessionId2, fitnessShapeIndexBean2.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean2.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean2.getUserid());
		Assert.assertEquals(113.75, fitnessHomeostasisIndexBean2.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-271.6104, fitnessHomeostasisIndexBean2.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-271.6104, fitnessHomeostasisIndexBean2.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean2.getRecentEndTime());
		
	
		
		
		
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		fitnessSpeedHeartRateDAO1.deleteSpeedHeartRateModelByUserid(userid);
		fitnessShapeIndexDAO1.deleteShapeIndexHistoryForUser(userid);
		fitnessHomeostasisIndexDAO1.deleteHomeostasisIndexModelByUserid(userid);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId1);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId2);
	}
	
	@Test
	public void testSaveFitnessTrainingSession11(){
		
		long now = new Date().getTime();
		long nowBeforeSixDays = now - (6*24*3600000);
		long nowBeforeSixDaysFourtyMinutes = nowBeforeSixDays - (2400000);
		long nowBeforeThreeDays = now - (3*24*3600000);
		long nowBeforeThreeDaysAfterFourtyMinutes = nowBeforeThreeDays+(2400000);
		long twoDaysAfterNow = now + (2*24*3600000);
		long twoDaysAfterNowBeforeThirtyTwoMinutes = twoDaysAfterNow - (1920000);
	
	   
		/*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = dateFormat.parse("10/06/2012");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		long time = date.getTime();
		currentnew Timestamp(time);*/
		
		/*Creating the Bean for the first Training Session*/	
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid(userid);
		//fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastFourtyMinutes));
		//fitnessTrainingSessionBean.setEndTime(new Timestamp(now));
		fitnessTrainingSessionBean.setStartTime(new Timestamp(nowBeforeSixDaysFourtyMinutes));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(nowBeforeSixDays));
		fitnessTrainingSessionBean.setHrz1Time(12.0);
		fitnessTrainingSessionBean.setHrz2Time(14.0);
		fitnessTrainingSessionBean.setHrz3Time(8.0);
		fitnessTrainingSessionBean.setHrz4Time(6.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz1Distance(1260.0);
		fitnessTrainingSessionBean.setHrz2Distance(1680.0);
		fitnessTrainingSessionBean.setHrz3Distance(1120.0);
		fitnessTrainingSessionBean.setHrz4Distance(990.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);
			
        /*Saving the first Fitness training session for the user*/
		FitnessManager fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		String trainingSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
		
		
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean = fitnessShapeIndexDAO.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean.getUserid());
		Assert.assertEquals(28.81, fitnessSpeedHeartRateBean.getCurrentVdot());
		Assert.assertNull(null, fitnessSpeedHeartRateBean.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean.getUserid());
		Assert.assertEquals(100.0, fitnessShapeIndexBean.getShapeIndex());
		Assert.assertEquals(trainingSessionId, fitnessShapeIndexBean.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(46.0, fitnessHomeostasisIndexBean.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-46.0, fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-46.0, fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean.getRecentEndTime());
	

		/*Creating the Bean for the second Training Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean1 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean1.setUserid(userid);
	    fitnessTrainingSessionBean1.setStartTime(new Timestamp(nowBeforeThreeDays));
		fitnessTrainingSessionBean1.setEndTime(new Timestamp(nowBeforeThreeDaysAfterFourtyMinutes));
		fitnessTrainingSessionBean1.setHrz1Time(10.0);
		fitnessTrainingSessionBean1.setHrz2Time(11.0);
		fitnessTrainingSessionBean1.setHrz3Time(2.0);
		fitnessTrainingSessionBean1.setHrz4Time(2.0);
		fitnessTrainingSessionBean1.setHrz5Time(0.0);
		fitnessTrainingSessionBean1.setHrz6Time(0.0);
		fitnessTrainingSessionBean1.setHrz1Distance(1016.67);
		fitnessTrainingSessionBean1.setHrz2Distance(1338.33);
		fitnessTrainingSessionBean1.setHrz3Distance(286.67);
		fitnessTrainingSessionBean1.setHrz4Distance(313.33);
		fitnessTrainingSessionBean1.setHrz5Distance(0.0);
		fitnessTrainingSessionBean1.setHrz6Distance(0.0);
		
		/*Saving the second Fitness training session for the user*/
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean1);
		String trainingSessionId1 = fitnessTrainingSessionBean1.getTrainingSessionId();
	
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO1 = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO1 = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO1 = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean1 = fitnessSpeedHeartRateDAO1.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean1 = fitnessShapeIndexDAO1.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean1 = fitnessHomeostasisIndexDAO1.getHomeostasisIndexModelByUserid(userid);
		
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean1.getUserid());
		Assert.assertEquals(28.58, fitnessSpeedHeartRateBean1.getCurrentVdot());
		Assert.assertEquals(28.81, fitnessSpeedHeartRateBean1.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean1.getUserid());
		Assert.assertEquals(99.65, fitnessShapeIndexBean1.getShapeIndex());
		Assert.assertEquals(trainingSessionId1, fitnessShapeIndexBean1.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean1.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean1.getUserid());
		Assert.assertEquals(20.5, fitnessHomeostasisIndexBean1.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-20.5, fitnessHomeostasisIndexBean1.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-20.5, fitnessHomeostasisIndexBean1.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean1.getRecentEndTime());
		
		/*Creating the bean for the third Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean3 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean3.setUserid(userid);
		//fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastFourtyMinutes));
		//fitnessTrainingSessionBean.setEndTime(new Timestamp(now));
		fitnessTrainingSessionBean3.setStartTime(new Timestamp(twoDaysAfterNowBeforeThirtyTwoMinutes));
		fitnessTrainingSessionBean3.setEndTime(new Timestamp(twoDaysAfterNow));
		fitnessTrainingSessionBean3.setHrz1Time(10.0);
		fitnessTrainingSessionBean3.setHrz2Time(22.0);
		fitnessTrainingSessionBean3.setHrz3Time(0.0);
		fitnessTrainingSessionBean3.setHrz4Time(0.0);
		fitnessTrainingSessionBean3.setHrz5Time(0.0);
		fitnessTrainingSessionBean3.setHrz6Time(0.0);
		fitnessTrainingSessionBean3.setHrz1Distance(1033.33);
		fitnessTrainingSessionBean3.setHrz2Distance(2640.0);
		fitnessTrainingSessionBean3.setHrz3Distance(0.0);
		fitnessTrainingSessionBean3.setHrz4Distance(0.0);
		fitnessTrainingSessionBean3.setHrz5Distance(0.0);
		fitnessTrainingSessionBean3.setHrz6Distance(0.0);
			
        /*Saving the first Fitness training session for the user*/
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean3);
		String trainingSessionId2 = fitnessTrainingSessionBean3.getTrainingSessionId();
		
		
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO2 = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO2 = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO2 = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean2 = fitnessSpeedHeartRateDAO2.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean2 = fitnessShapeIndexDAO2.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean2 = fitnessHomeostasisIndexDAO2.getHomeostasisIndexModelByUserid(userid);
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean2.getUserid());
		Assert.assertEquals(26.06, fitnessSpeedHeartRateBean2.getCurrentVdot());
		Assert.assertEquals(28.58, fitnessSpeedHeartRateBean2.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean2.getUserid());
		Assert.assertEquals(98.62, fitnessShapeIndexBean2.getShapeIndex());
		Assert.assertEquals(trainingSessionId2, fitnessShapeIndexBean2.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean2.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean2.getUserid());
		Assert.assertEquals(22.0, fitnessHomeostasisIndexBean2.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-22.0, fitnessHomeostasisIndexBean2.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-22.0, fitnessHomeostasisIndexBean2.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean2.getRecentEndTime());
				
		
		
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		fitnessSpeedHeartRateDAO1.deleteSpeedHeartRateModelByUserid(userid);
		fitnessShapeIndexDAO1.deleteShapeIndexHistoryForUser(userid);
		fitnessHomeostasisIndexDAO1.deleteHomeostasisIndexModelByUserid(userid);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId1);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId2);
		
	}

	
}
