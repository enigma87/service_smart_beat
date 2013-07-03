package com.genie.heartrate.mgmt.impl;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.FitnessHomeostasisIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessShapeIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessSpeedHeartRateBean;
import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
import com.genie.heartrate.mgmt.core.FitnessManager;
import com.genie.heartrate.mgmt.dao.FitnessHomeostasisIndexDAO;
import com.genie.heartrate.mgmt.dao.FitnessShapeIndexDAO;
import com.genie.heartrate.mgmt.dao.FitnessSpeedHeartRateDAO;
import com.genie.heartrate.mgmt.dao.FitnessTrainingSessionDAO;
/**
 * @author vidhun
 *
 */

public class FitnessManagerMySQLImplTest {

	ApplicationContext smartbeatContext;	
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	
	@Before
	public void setUpBeforeClass() throws Exception 
	{
		smartbeatContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");	
	}
	
	//@Test
	public void testSaveFitnessTrainingSession10(){
		
		long now = new Date().getTime();
		long nowBeforeTwoDays = now - (2*24*3600000);
		long nowBeforeTwoDaysFourtyMinutes = nowBeforeTwoDays - (2400000);
		long nowPastFiveHours = now - (5*3600000);
		long nowPastFourHours = now - (4*3600000);
		long oneDayAfterNow = now + (24*3600000);
		long oneDayAfterNowBeforeOneHour = oneDayAfterNow - (3600000);
		long twoDaysAfterNow = now + (2*24*3600000);
		long twoDaysAfterNowBeforeFourtyMinutes = twoDaysAfterNow - (2400000);
		String fitnessTrainingSessionId = "20131";
	   
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
		fitnessTrainingSessionBean.setStartTime(new Timestamp(nowBeforeTwoDaysFourtyMinutes));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(nowBeforeTwoDays));
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz2Time(8.0);
		fitnessTrainingSessionBean.setHrz3Time(11.0);
		fitnessTrainingSessionBean.setHrz4Time(3.0);
		fitnessTrainingSessionBean.setHrz5Time(6.0);
		fitnessTrainingSessionBean.setHrz6Time(8.0);
		fitnessTrainingSessionBean.setHrz1Distance(421.7304);
		fitnessTrainingSessionBean.setHrz2Distance(895.1108);
		fitnessTrainingSessionBean.setHrz3Distance(1342.9544);
		fitnessTrainingSessionBean.setHrz4Distance(402.9899);
		fitnessTrainingSessionBean.setHrz5Distance(1408.0273);
		fitnessTrainingSessionBean.setHrz6Distance(2070.7614);
			
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
		Assert.assertEquals(33.865384604692366, fitnessSpeedHeartRateBean.getCurrentVdot());
		Assert.assertNull(null, fitnessSpeedHeartRateBean.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean.getUserid());
		Assert.assertEquals(100.0, fitnessShapeIndexBean.getShapeIndex());
		Assert.assertEquals(trainingSessionId, fitnessShapeIndexBean.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(138.25, fitnessHomeostasisIndexBean.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-138.25, fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-138.25, fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean.getRecentEndTime());
	

		/*Creating the Bean for the second Training Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean1 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean1.setUserid(userid);
	    fitnessTrainingSessionBean1.setStartTime(new Timestamp(nowPastFiveHours));
		fitnessTrainingSessionBean1.setEndTime(new Timestamp(nowPastFourHours));
		fitnessTrainingSessionBean1.setHrz1Time(10.0);
		fitnessTrainingSessionBean1.setHrz2Time(15.0);
		fitnessTrainingSessionBean1.setHrz3Time(10.0);
		fitnessTrainingSessionBean1.setHrz4Time(15.0);
		fitnessTrainingSessionBean1.setHrz5Time(5.0);
		fitnessTrainingSessionBean1.setHrz6Time(5.0);
		fitnessTrainingSessionBean1.setHrz1Distance(500.7304);
		fitnessTrainingSessionBean1.setHrz2Distance(1968.8635);
		fitnessTrainingSessionBean1.setHrz3Distance(1432.3931);
		fitnessTrainingSessionBean1.setHrz4Distance(2364.424);
		fitnessTrainingSessionBean1.setHrz5Distance(823.7615);
		fitnessTrainingSessionBean1.setHrz6Distance(862.7104);
		
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
		Assert.assertEquals(29.69994179705032, fitnessSpeedHeartRateBean1.getCurrentVdot());
		Assert.assertEquals(33.865384604692366, fitnessSpeedHeartRateBean1.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean1.getUserid());
		Assert.assertEquals(99.8, fitnessShapeIndexBean1.getShapeIndex());
		Assert.assertEquals(trainingSessionId1, fitnessShapeIndexBean1.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean1.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean1.getUserid());
		Assert.assertEquals(147.5, fitnessHomeostasisIndexBean1.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-147.5, fitnessHomeostasisIndexBean1.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-147.5, fitnessHomeostasisIndexBean1.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean1.getRecentEndTime());
		
		/*Creating the bean for the third Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean3 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean3.setUserid(userid);
		//fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastFourtyMinutes));
		//fitnessTrainingSessionBean.setEndTime(new Timestamp(now));
		fitnessTrainingSessionBean3.setStartTime(new Timestamp(twoDaysAfterNowBeforeFourtyMinutes));
		fitnessTrainingSessionBean3.setEndTime(new Timestamp(twoDaysAfterNow));
		fitnessTrainingSessionBean3.setHrz1Time(4.0);
		fitnessTrainingSessionBean3.setHrz2Time(8.0);
		fitnessTrainingSessionBean3.setHrz3Time(11.0);
		fitnessTrainingSessionBean3.setHrz4Time(3.0);
		fitnessTrainingSessionBean3.setHrz5Time(6.0);
		fitnessTrainingSessionBean3.setHrz6Time(8.0);
		fitnessTrainingSessionBean3.setHrz1Distance(421.7304);
		fitnessTrainingSessionBean3.setHrz2Distance(895.1108);
		fitnessTrainingSessionBean3.setHrz3Distance(1342.9544);
		fitnessTrainingSessionBean3.setHrz4Distance(402.9899);
		fitnessTrainingSessionBean3.setHrz5Distance(1408.0273);
		fitnessTrainingSessionBean3.setHrz6Distance(2070.7614);
			
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
		Assert.assertEquals(33.865384604692366, fitnessSpeedHeartRateBean2.getCurrentVdot());
		Assert.assertEquals(29.69994179705032, fitnessSpeedHeartRateBean2.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean2.getUserid());
		Assert.assertEquals(87.49999851983112, fitnessShapeIndexBean2.getShapeIndex());
		Assert.assertEquals(trainingSessionId2, fitnessShapeIndexBean2.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean2.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean2.getUserid());
		Assert.assertEquals(138.25, fitnessHomeostasisIndexBean2.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-265.6112, fitnessHomeostasisIndexBean2.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-265.6112, fitnessHomeostasisIndexBean2.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean2.getRecentEndTime());
		System.out.println(fitnessManager.getFitnessShapeIndex(trainingSessionId2));
	
		
		
		
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		fitnessSpeedHeartRateDAO1.deleteSpeedHeartRateModelByUserid(userid);
		fitnessShapeIndexDAO1.deleteShapeIndexModel(userid);
		fitnessHomeostasisIndexDAO1.deleteHomeostasisIndexModelByUserid(userid);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId1);
		
	}


	
	@Test
	public void testSaveFitnessTrainingSession00(){
		
		long now = new Date().getTime();
		long nowBeforeTwoDays = now - (2*24*3600000);
		long nowBeforeTwoDaysFourtyMinutes = nowBeforeTwoDays - (2400000);
		long nowBeforeOneDay = now - (24*3600000);
		long nowBeforeOneDayFourtyMinutes = nowBeforeTwoDays - (2400000);
		long nowPastFiveHours = now - (5*3600000);
		long nowPastFourHours = now - (4*3600000);
		long oneDayAfterNow = now + (24*3600000);
		long oneDayAfterNowBeforeOneHour = oneDayAfterNow - (3600000);
		long twoDaysAfterNow = now + (2*24*3600000);
		long twoDaysAfterNowBeforeFourtyMinutes = twoDaysAfterNow - (2400000);
		String fitnessTrainingSessionId = "20131";
	   
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
		fitnessTrainingSessionBean.setHrz5Time(6.0);
		fitnessTrainingSessionBean.setHrz6Time(8.0);
		fitnessTrainingSessionBean.setHrz1Distance(421.7304);
		fitnessTrainingSessionBean.setHrz2Distance(895.1108);
		fitnessTrainingSessionBean.setHrz3Distance(1342.9544);
		fitnessTrainingSessionBean.setHrz4Distance(402.9899);
		fitnessTrainingSessionBean.setHrz5Distance(1408.0273);
		fitnessTrainingSessionBean.setHrz6Distance(2070.7614);
			
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
		Assert.assertEquals(33.865384604692366, fitnessSpeedHeartRateBean.getCurrentVdot());
		Assert.assertNull(null, fitnessSpeedHeartRateBean.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean.getUserid());
		Assert.assertEquals(100.0, fitnessShapeIndexBean.getShapeIndex());
		Assert.assertEquals(trainingSessionId, fitnessShapeIndexBean.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(138.25, fitnessHomeostasisIndexBean.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-138.25, fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-138.25, fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean.getRecentEndTime());
	

		/*Creating the Bean for the second Training Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean1 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean1.setUserid(userid);
	    fitnessTrainingSessionBean1.setStartTime(new Timestamp(nowPastFiveHours));
		fitnessTrainingSessionBean1.setEndTime(new Timestamp(nowPastFourHours));
		fitnessTrainingSessionBean1.setHrz1Time(10.0);
		fitnessTrainingSessionBean1.setHrz2Time(15.0);
		fitnessTrainingSessionBean1.setHrz3Time(10.0);
		fitnessTrainingSessionBean1.setHrz4Time(15.0);
		fitnessTrainingSessionBean1.setHrz5Time(5.0);
		fitnessTrainingSessionBean1.setHrz6Time(5.0);
		fitnessTrainingSessionBean1.setHrz1Distance(500.7304);
		fitnessTrainingSessionBean1.setHrz2Distance(1968.8635);
		fitnessTrainingSessionBean1.setHrz3Distance(1432.3931);
		fitnessTrainingSessionBean1.setHrz4Distance(2364.424);
		fitnessTrainingSessionBean1.setHrz5Distance(823.7615);
		fitnessTrainingSessionBean1.setHrz6Distance(862.7104);
		
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
		Assert.assertEquals(29.69994179705032, fitnessSpeedHeartRateBean1.getCurrentVdot());
		Assert.assertEquals(33.865384604692366, fitnessSpeedHeartRateBean1.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean1.getUserid());
		Assert.assertEquals(100.0, fitnessShapeIndexBean1.getShapeIndex());
		Assert.assertEquals(trainingSessionId1, fitnessShapeIndexBean1.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean1.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean1.getUserid());
		Assert.assertEquals(147.5, fitnessHomeostasisIndexBean1.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-185.7492, fitnessHomeostasisIndexBean1.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-185.7492, fitnessHomeostasisIndexBean1.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean1.getRecentEndTime());
		
		/*Creating the bean for the third Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean3 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean3.setUserid(userid);
		//fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastFourtyMinutes));
		//fitnessTrainingSessionBean.setEndTime(new Timestamp(now));
		fitnessTrainingSessionBean3.setStartTime(new Timestamp(twoDaysAfterNowBeforeFourtyMinutes));
		fitnessTrainingSessionBean3.setEndTime(new Timestamp(twoDaysAfterNow));
		fitnessTrainingSessionBean3.setHrz1Time(4.0);
		fitnessTrainingSessionBean3.setHrz2Time(8.0);
		fitnessTrainingSessionBean3.setHrz3Time(11.0);
		fitnessTrainingSessionBean3.setHrz4Time(3.0);
		fitnessTrainingSessionBean3.setHrz5Time(6.0);
		fitnessTrainingSessionBean3.setHrz6Time(8.0);
		fitnessTrainingSessionBean3.setHrz1Distance(421.7304);
		fitnessTrainingSessionBean3.setHrz2Distance(895.1108);
		fitnessTrainingSessionBean3.setHrz3Distance(1342.9544);
		fitnessTrainingSessionBean3.setHrz4Distance(402.9899);
		fitnessTrainingSessionBean3.setHrz5Distance(1408.0273);
		fitnessTrainingSessionBean3.setHrz6Distance(2070.7614);
			
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
		Assert.assertEquals(33.865384604692366, fitnessSpeedHeartRateBean2.getCurrentVdot());
		Assert.assertEquals(29.69994179705032, fitnessSpeedHeartRateBean2.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean2.getUserid());
		Assert.assertEquals(87.49999851983112, fitnessShapeIndexBean2.getShapeIndex());
		Assert.assertEquals(trainingSessionId2, fitnessShapeIndexBean2.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean2.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean2.getUserid());
		Assert.assertEquals(138.25, fitnessHomeostasisIndexBean2.getRecentTotalLoadOfExercise());
		Assert.assertEquals(-265.6112, fitnessHomeostasisIndexBean2.getRecentMinimumOfHomeostasisIndex());
		Assert.assertEquals(-265.6112, fitnessHomeostasisIndexBean2.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNotNull(fitnessHomeostasisIndexBean2.getRecentEndTime());
		System.out.println(fitnessManager.getFitnessShapeIndex(trainingSessionId2));
	
		
		
		
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		fitnessSpeedHeartRateDAO1.deleteSpeedHeartRateModelByUserid(userid);
		fitnessShapeIndexDAO1.deleteShapeIndexModel(userid);
		fitnessHomeostasisIndexDAO1.deleteHomeostasisIndexModelByUserid(userid);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId1);
		
	}	
	
	
	//@Test
	public void testGetFitnessSupercompensationPoints(){
		
		long now = new Date().getTime();
		long nowPastOneHour = now - 3600000;
		long nowPastTwoDays = now - (3600000*48);
		String fitnessTrainingSessionId = "20131";
	    
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
		
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
		fitnessHomeostasisIndexBean.setUserid(userid);
		fitnessHomeostasisIndexBean.setTraineeClassification(2);
		fitnessHomeostasisIndexBean.setRecentEndTime(new Timestamp(nowPastOneHour));
		fitnessHomeostasisIndexBean.setRecentTotalLoadOfExercise(120.0);
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(180.0);
		fitnessHomeostasisIndexBean.setPreviousEndTime(new Timestamp(nowPastTwoDays));
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(140.0);		
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		Assert.assertNotNull(fitnessHomeostasisIndexDAO);
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		
		
		
		FitnessManager fitnessManager = new FitnessManagerMySQLImpl();
		fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");
		Double points = ((FitnessManagerMySQLImpl)fitnessManager).getFitnessSupercompensationPoints(userid);
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
	}
}
