package com.genie.smartbeat.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessSpeedHeartRateBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessSpeedHeartRateDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;
import com.genie.smartbeat.impl.FitnessManagerMySQLImpl;
import com.genie.social.beans.UserBean;
import com.genie.social.core.UserManager;
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
		
	@Test
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
		fitnessHomeostasisIndexBean.setUserid("testUser_001");
		fitnessHomeostasisIndexBean.setTraineeClassification(2);
		fitnessHomeostasisIndexBean.setRecentEndTime(new Timestamp(nowPastOneHour));
		fitnessHomeostasisIndexBean.setRecentTotalLoadOfExercise(120.0);
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(180.0);
		fitnessHomeostasisIndexBean.setPreviousEndTime(new Timestamp(nowPastTwoDays));
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(140.0);
		fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(160.0);
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		Assert.assertNotNull(fitnessHomeostasisIndexDAO);
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);		
		
		FitnessManager fitnessManager = new FitnessManagerMySQLImpl();
		fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");
		Double points = ((FitnessManagerMySQLImpl)fitnessManager).getFitnessSupercompensationPoints(fitnessHomeostasisIndexBean.getUserid());
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
	}
	
	@Test
	public void testSaveHeartRateTest(){
		
		long now = new Date().getTime();
		long oneDayBefore = now - (24*3600000);
		long twoDaysBefore = now - (48*3600000);
		FitnessManager fitnessManager = new FitnessManagerMySQLImpl();
		fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");

		FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean1.setUserid("user1");
		fitnessHeartrateTestBean1.setHeartrateTestId("user1Test1");
		fitnessHeartrateTestBean1.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean1.setHeartrate(124.0);
		fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(twoDaysBefore));
		fitnessManager.saveHeartrateTest(fitnessHeartrateTestBean1);
		
		FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean2.setUserid("user1");
		fitnessHeartrateTestBean2.setHeartrateTestId("user1Test2");
		fitnessHeartrateTestBean2.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean2.setHeartrate(114.0);
		fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(now));
		fitnessManager.saveHeartrateTest(fitnessHeartrateTestBean2);
		
	}
	
	@Test
	public void testGetTraineeClassificationUsingVdot(){
		
		long now = new Date().getTime();
		long nowBeforeTwoDays = now - (2*24*3600000);
		long nowBeforeOneDay = now - (24*3600000);
		long nowBeforeOneDayFiftyMinutes = nowBeforeOneDay - (3000000);

	   
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
		fitnessTrainingSessionBean.setStartTime(new Timestamp(nowBeforeOneDayFiftyMinutes));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(nowBeforeOneDay));
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz2Time(8.0);
		fitnessTrainingSessionBean.setHrz3Time(11.0);
		fitnessTrainingSessionBean.setHrz4Time(3.0);
		fitnessTrainingSessionBean.setHrz5Time(10.0);
		fitnessTrainingSessionBean.setHrz6Time(14.0);
		fitnessTrainingSessionBean.setHrz1Distance(1660.0);
		fitnessTrainingSessionBean.setHrz2Distance(1426.67);
		fitnessTrainingSessionBean.setHrz3Distance(2090.0);
		fitnessTrainingSessionBean.setHrz4Distance(2605.0);
		fitnessTrainingSessionBean.setHrz5Distance(2133.33);
		fitnessTrainingSessionBean.setHrz6Distance(2126.67);
			
        /*Saving the first Fitness training session for the user*/
		FitnessManager fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		String trainingSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
		
		
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO = (FitnessSpeedHeartRateDAO) smartbeatContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		/*Asserting the Model data for Vdor and Trainee Classification*/

		Assert.assertEquals(53.72886508310602, fitnessSpeedHeartRateBean.getCurrentVdot());
    	Assert.assertEquals(ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_WELL_TRAINED, fitnessHomeostasisIndexBean.getTraineeClassification());
	
	}
	
	@Test
	public void testGetHeartrateZones(){
		UserManager userManager = (UserManager)smartbeatContext.getBean("userManagerMySQLImpl");
		UserBean user = new UserBean();
		user.setUserid("12345");
		user.setAccessToken("accessToken1");
		user.setAccessTokenType("facebook");
		user.setFirstName("Jane");
		user.setEmail("jane@acme.com");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -25);
		user.setDob(new java.sql.Date(cal.getTimeInMillis()));
		user.setGender(UserManager.GENDER_FEMALE);
		userManager.registerUser(user);
		FitnessManager fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");
		double[][] heartrateZones = fitnessManager.getHeartrateZones("12345");
		System.out.println(Arrays.deepToString(heartrateZones));
	}
	
	@Test
	public void testGetFitnessTrainingSessionIdsByTimeRange() {
		FitnessManager fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");
		List<String> sessions =  fitnessManager.getTrainingSessionIdsInTimeInterval("TEST073a9e7d-9cf2-49a0-8926-f27362fd547e" ,Timestamp.valueOf("2013-07-04 00:00:00"), Timestamp.valueOf("2013-07-05 23:59:59"));
		for (Iterator<String> i = sessions.iterator(); i.hasNext();) {
			String id = i.next();
			System.out.println(id);
		}
	}
	
	@Test 
	public void testgetShapeIndexHistoryInTimeInterval() {
		FitnessManager fitnessManager = (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		List<FitnessShapeIndexBean> shapeIndexBeans = fitnessManager.getShapeIndexHistoryInTimeInterval("TEST073a9e7d-9cf2-49a0-8926-f27362fd547e", Timestamp.valueOf("2013-07-03 18:23:11"), Timestamp.valueOf("2013-08-05 23:59:59"));
		for (Iterator<FitnessShapeIndexBean> i = shapeIndexBeans.iterator(); i.hasNext();) {
			FitnessShapeIndexBean shapeIndexBean = i.next();
			System.out.println(shapeIndexBean.getShapeIndex().toString() + " -> "+ shapeIndexBean.getTimeOfRecord());
		}
	}
}
