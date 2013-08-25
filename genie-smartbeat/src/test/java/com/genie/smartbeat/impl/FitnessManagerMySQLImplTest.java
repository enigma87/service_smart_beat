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
import com.genie.smartbeat.beans.FitnessHeartrateZoneBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessSpeedHeartRateBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.dao.FitnessHeartrateTestDAO;
import com.genie.smartbeat.dao.FitnessHeartrateTestDAOTest;
import com.genie.smartbeat.dao.FitnessHeartrateZoneDAO;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessSpeedHeartRateDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;
import com.genie.smartbeat.impl.FitnessManagerMySQLImpl;
import com.genie.social.beans.UserBean;
import com.genie.social.core.UserManager;
import com.genie.social.dao.UserDao;
/**
 * @author vidhun
 *
 */

public class FitnessManagerMySQLImplTest {

	ApplicationContext smartbeatContext;	
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	private long now = new Date().getTime();
	
	@Before
	public void setUpBeforeClass() throws Exception 
	{
		smartbeatContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");	
	}
		
	@Test 
	public void testSaveFitnessTrainingSession() {
		
		long nowBeforeTwoDays = now - (2*24*3600000);
		long nowBeforeOneDay = now - (24*3600000);
		long nowBeforeOneDayFiftyMinutes = nowBeforeOneDay - (3000000);
		
		FitnessManager fitnessManager =  (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
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
		
		Assert.assertNull(fitnessManager.getTrainingSessionById(fitnessTrainingSessionBean.getTrainingSessionId()));
		
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		
		Assert.assertNotNull(fitnessManager.getTrainingSessionById(fitnessTrainingSessionBean.getTrainingSessionId()));
		
	}
	
	@Test
	public void testGetFitnessSupercompensationPoints(){
		
		long now = new Date().getTime();
		long nowPastOneHour = now - 3600000;
		long nowPastTwoDays = now - (3600000*48);
		String fitnessTrainingSessionId = "20131";
	    
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
		FitnessHeartrateTestDAO fitnessHeartrateTestDAO = (FitnessHeartrateTestDAO) smartbeatContext.getBean("fitnessHeartrateTestDAO");
		
		Assert.assertTrue(fitnessHeartrateTestDAO.getNumberOfHeartRateTestsForUserByType("user1", ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC).intValue() == 0);
		
		FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean1.setUserid("user1");
		fitnessHeartrateTestBean1.setHeartrateTestId("user1Test1");
		fitnessHeartrateTestBean1.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
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
		
		System.out.println( ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC+ "-" + fitnessHeartrateTestDAO.getNumberOfHeartRateTestsForUserByType("user1", ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC).intValue());
		Assert.assertEquals(1, fitnessHeartrateTestDAO.getNumberOfHeartRateTestsForUserByType("user1", ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC).intValue());
		
		List<FitnessHeartrateTestBean> heartrateTestBeans = fitnessHeartrateTestDAO.getAllHeartrateTestsByUser("user1");
		for (Iterator i = heartrateTestBeans.iterator();i.hasNext();) {
			FitnessHeartrateTestBean testBean = (FitnessHeartrateTestBean) i.next();
			fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(testBean.getHeartrateTestId());
		}
	}
		
	@Test
	public void testGetHeartrateZones(){ 
		FitnessHeartrateZoneDAO fitnessHeartrateZoneDAO = (FitnessHeartrateZoneDAO)smartbeatContext.getBean("fitnessHeartrateZoneDAO");
		FitnessHeartrateZoneBean fitnessHeartrateZoneBean = new FitnessHeartrateZoneBean();
		FitnessManager fitnessManager = (FitnessManager)smartbeatContext.getBean("fitnessManagerMySQLImpl");
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
		
		Double heartrateZone1Start 	= 50.0;
		Double heartrateZone1End 	= 105.0;
		Double heartrateZone2Start 	= 105.0;
		Double heartrateZone2End 	= 133.0;
		Double heartrateZone3Start 	= 133.0;
		Double heartrateZone3End 	= 156.0;
		Double heartrateZone4Start 	= 156.0;
		Double heartrateZone4End 	= 164.0;
		Double heartrateZone5Start 	= 164.0;
		Double heartrateZone5End 	= 182.0;
		Double heartrateZone6Start 	= 182.0;
		Double heartrateZone6End 	= 190.0;
		
		fitnessHeartrateZoneBean.setUserid(user.getUserid());
		fitnessHeartrateZoneBean.setHeartrateZone1Start(heartrateZone1Start);
		fitnessHeartrateZoneBean.setHeartrateZone1End(heartrateZone1End);
		fitnessHeartrateZoneBean.setHeartrateZone2Start(heartrateZone2Start);
		fitnessHeartrateZoneBean.setHeartrateZone2End(heartrateZone2End);
		fitnessHeartrateZoneBean.setHeartrateZone3Start(heartrateZone3Start);
		fitnessHeartrateZoneBean.setHeartrateZone3End(heartrateZone3End);
		fitnessHeartrateZoneBean.setHeartrateZone4Start(heartrateZone4Start);
		fitnessHeartrateZoneBean.setHeartrateZone4End(heartrateZone4End);
		fitnessHeartrateZoneBean.setHeartrateZone5Start(heartrateZone5Start);
		fitnessHeartrateZoneBean.setHeartrateZone5End(heartrateZone5End);
		fitnessHeartrateZoneBean.setHeartrateZone6Start(heartrateZone6Start);
		fitnessHeartrateZoneBean.setHeartrateZone6End(heartrateZone6End);
		
		fitnessHeartrateZoneDAO.createHeartrateZoneModel(fitnessHeartrateZoneBean);
		
		double[][] heartrateZones = fitnessManager.getHeartrateZones(user.getUserid());
		
		Assert.assertNotNull(heartrateZones[1][0]);
		Assert.assertNotNull(heartrateZones[6][1]);
		System.out.println(Arrays.deepToString(heartrateZones));
		
		long now = cal.getTime().getTime();
		long oneHourBefore = now -(1 * 3600000);
		long twoHoursBefore = now -(2 * 3600000);
				
		FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean1.setUserid(user.getUserid());
		fitnessHeartrateTestBean1.setHeartrateTestId("user1Test1");
		fitnessHeartrateTestBean1.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD);
		fitnessHeartrateTestBean1.setHeartrate(124.0);
		fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(now));
		fitnessHeartrateTestBean1.setDayOfRecord(1);
		
		FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean2.setUserid(user.getUserid());
		fitnessHeartrateTestBean2.setHeartrateTestId("user1Test2");
		fitnessHeartrateTestBean2.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
		fitnessHeartrateTestBean2.setHeartrate(130.0);
		fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(twoHoursBefore));
		fitnessHeartrateTestBean2.setDayOfRecord(3);
		
		FitnessHeartrateTestBean fitnessHeartrateTestBean3 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean3.setUserid(user.getUserid());
		fitnessHeartrateTestBean3.setHeartrateTestId("user1Test3");
		fitnessHeartrateTestBean3.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		fitnessHeartrateTestBean3.setHeartrate(146.0);
		fitnessHeartrateTestBean3.setTimeOfRecord(new Timestamp(oneHourBefore));
		fitnessHeartrateTestBean3.setDayOfRecord(8);
		
		FitnessHeartrateTestDAO fitnessHeartrateTestDAO = (FitnessHeartrateTestDAO) smartbeatContext.getBean("fitnessHeartrateTestDAO");
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		
		heartrateZones = fitnessManager.getHeartrateZones(user.getUserid());
		
		Assert.assertNotNull(heartrateZones[1][0]);
		Assert.assertNotNull(heartrateZones[6][1]);
		System.out.println(Arrays.deepToString(heartrateZones));
		
		fitnessHeartrateZoneDAO.deleteHeartrateZoneModelByUserid(user.getUserid());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean2.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean3.getHeartrateTestId());
		UserDao userDao = (UserDao) smartbeatContext.getBean("userDao");
		userDao.deleteUser(user.getUserid());
	}
	
	@Test 
	public void testGetTrainingSessionIdsInTimeInterval() {
		
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		FitnessManager fitnessManager = (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		
		Assert.assertEquals(0, fitnessManager.getTrainingSessionIdsInTimeInterval("TEST073a9e7d-9cf2-49a0-8926-f27362fd547e", Timestamp.valueOf("2013-07-03 18:23:10"), Timestamp.valueOf("2013-08-30 18:23:10")).size());
		
		String [][] dummyValues = {{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","1","2013-07-03 18:23:10","2013-07-03 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
				{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","2","2013-07-04 18:23:10","2013-07-04 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
				{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","3","2013-07-05 18:23:10","2013-07-05 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
				{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","4","2013-08-03 18:23:10","2013-08-03 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
				{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","5","2013-08-04 18:23:10","2013-08-04 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
				{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","6","2013-08-05 18:23:10","2013-08-05 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
				{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","7","2013-08-06 18:23:10","2013-08-06 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
				{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","8","2013-08-07 18:23:10","2013-08-07 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
				{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","9","2013-08-08 18:23:10","2013-08-08 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"}};

			for (int i=0; i < dummyValues.length; i++) {
				FitnessTrainingSessionBean newTrainingSession = new FitnessTrainingSessionBean();
				String [] row = dummyValues[i];
				
				newTrainingSession.setUserid(row[0]);
				newTrainingSession.setTrainingSessionId(row[1]);
				newTrainingSession.setStartTime(Timestamp.valueOf(row[2]));
				newTrainingSession.setEndTime(Timestamp.valueOf(row[3]));
				newTrainingSession.setHrz1Time(Double.valueOf(row[4]));
				newTrainingSession.setHrz2Time(Double.valueOf(row[5]));
				newTrainingSession.setHrz3Time(Double.valueOf(row[6]));
				newTrainingSession.setHrz4Time(Double.valueOf(row[7]));
				newTrainingSession.setHrz5Time(Double.valueOf(row[8]));
				newTrainingSession.setHrz6Time(Double.valueOf(row[9]));
				newTrainingSession.setHrz1Distance(Double.valueOf(row[10]));
				newTrainingSession.setHrz1Distance(Double.valueOf(row[11]));
				newTrainingSession.setHrz1Distance(Double.valueOf(row[12]));
				newTrainingSession.setHrz1Distance(Double.valueOf(row[13]));
				newTrainingSession.setHrz1Distance(Double.valueOf(row[14]));
				newTrainingSession.setHrz1Distance(Double.valueOf(row[15]));
				newTrainingSession.setSurfaceIndex(Integer.valueOf(row[16]));
			
				fitnessTrainingSessionDAO.createFitnessTrainingSession(newTrainingSession);
			}
			
			Assert.assertEquals(9, fitnessManager.getTrainingSessionIdsInTimeInterval("TEST073a9e7d-9cf2-49a0-8926-f27362fd547e" , Timestamp.valueOf("2013-07-03 18:23:10"), Timestamp.valueOf("2013-08-30 18:23:10")).size());
		
			fitnessTrainingSessionDAO.deleteAllTrainingSessionsForUser("TEST073a9e7d-9cf2-49a0-8926-f27362fd547e");;
	}
	
	@Test 
	public void testgetShapeIndexHistoryInTimeInterval() {
		Date today = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		long now = cal.getTime().getTime();
		long nowPastOneHour = now - 3600000;
		long nowPastTwoHour = now - 7200000;
		Timestamp nowTimeStamp = new Timestamp(now);
		Timestamp nowPastOneHourTimeStamp = new Timestamp(nowPastOneHour);
		Timestamp nowPastTwoHourTimeStamp = new Timestamp(nowPastTwoHour);
		long nowPastOneDay 	= now - (24*3600000);
		long nowPastTwoDays 	= now - (2*24*3600000);
		long nowPastThreeDays 	= now - (3*24*3600000);
		long nowPastFourDays 	= now - (4*24*3600000);
		
		FitnessShapeIndexDAO fitnessShapeIndexDAO = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		List<FitnessShapeIndexBean> shapeIndexBeans = null;
		
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		Timestamp endInterval = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.DATE, -3);
		Timestamp startInterval = new Timestamp(cal.getTimeInMillis());
				
		FitnessShapeIndexBean  fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20135");
		fitnessShapeIndexBean1.setShapeIndex(150.0);
		fitnessShapeIndexBean1.setTimeOfRecord(nowPastOneHourTimeStamp);
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20134");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(nowPastOneDay));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20133");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(nowPastTwoDays));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20132");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(nowPastThreeDays));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);

		FitnessManager fitnessManager = (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		shapeIndexBeans = fitnessManager.getShapeIndexHistoryInTimeInterval(userid, startInterval, endInterval);
		Assert.assertEquals(3, shapeIndexBeans.size());
		
		for (Iterator<FitnessShapeIndexBean> i = shapeIndexBeans.iterator(); i.hasNext();) {
			FitnessShapeIndexBean shapeIndexBean = i.next();
			System.out.println(shapeIndexBean.getShapeIndex().toString() + " -> "+ shapeIndexBean.getTimeOfRecord());
		}
		fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(userid);
	}

	@Test
	public void testGetRecentTrainingSessionId() {
		
		FitnessTrainingSessionBean newTrainingSession = new FitnessTrainingSessionBean();

		long now = new Date().getTime();
		
		newTrainingSession.setUserid("jack_sparrow");
		newTrainingSession.setTrainingSessionId("123456");
		newTrainingSession.setStartTime(new Timestamp(now));
		newTrainingSession.setEndTime(new Timestamp(now + 3600000));
		newTrainingSession.setHrz1Time(1.0);
		newTrainingSession.setHrz2Time(2.0);
		newTrainingSession.setHrz3Time(3.0);
		newTrainingSession.setHrz4Time(4.0);
		newTrainingSession.setHrz5Time(5.0);
		newTrainingSession.setHrz6Time(6.0);
		newTrainingSession.setHrz1Distance(1.0);
		newTrainingSession.setHrz1Distance(2.0);
		newTrainingSession.setHrz1Distance(3.0);
		newTrainingSession.setHrz1Distance(4.0);
		newTrainingSession.setHrz1Distance(5.0);
		newTrainingSession.setHrz1Distance(6.0);
		newTrainingSession.setSurfaceIndex(16);
	
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		
		FitnessManager fitnessManager = (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		Assert.assertNull(fitnessManager.getRecentTrainingSessionId("jack_sparrow"));
		fitnessTrainingSessionDAO.createFitnessTrainingSession(newTrainingSession);
		Assert.assertNotNull(fitnessManager.getRecentTrainingSessionId("jack_sparrow"));
		
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(newTrainingSession.getTrainingSessionId());
	}

	@Test
	public void testUpdateHeartrateZoneModel() {
		
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);		

		long now = cal.getTime().getTime();
		long oneHourBefore = now -(1 * 3600000);
		long twoHoursBefore = now -(2 * 3600000);
		long threeHoursBefore = now - (3 * 3600000);
		
		FitnessHeartrateTestDAO fitnessHeartrateTestDAO = (FitnessHeartrateTestDAO)smartbeatContext.getBean("fitnessHeartrateTestDAO");
		FitnessHeartrateZoneDAO fitnessHeartrateZoneDAO = (FitnessHeartrateZoneDAO) smartbeatContext.getBean("fitnessHeartrateZoneDAO");
		fitnessHeartrateZoneDAO.deleteHeartrateZoneModelByUserid("user1");
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		
		FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean1.setUserid("user1");
		fitnessHeartrateTestBean1.setHeartrateTestId("user1Test1");
		fitnessHeartrateTestBean1.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
		fitnessHeartrateTestBean1.setHeartrate(124.0);
		fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(threeHoursBefore));
		fitnessHeartrateTestBean1.setDayOfRecord(1);
		
		FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean2.setUserid("user1");
		fitnessHeartrateTestBean2.setHeartrateTestId("user1Test2");
		fitnessHeartrateTestBean2.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		fitnessHeartrateTestBean2.setHeartrate(130.0);
		fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(twoHoursBefore));
		fitnessHeartrateTestBean2.setDayOfRecord(3);
		
		FitnessHeartrateTestBean fitnessHeartrateTestBean3 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean3.setUserid("user1");
		fitnessHeartrateTestBean3.setHeartrateTestId("user1Test3");
		fitnessHeartrateTestBean3.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD);
		fitnessHeartrateTestBean3.setHeartrate(146.0);
		fitnessHeartrateTestBean3.setTimeOfRecord(new Timestamp(oneHourBefore));
		fitnessHeartrateTestBean3.setDayOfRecord(8);
		
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		
		FitnessManager fitnessManager = (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		fitnessManager.updateHeartrateZoneModel("user1");
		Assert.assertNotNull(fitnessHeartrateZoneDAO.getHeartrateZoneModelByUserid("user1"));
		fitnessHeartrateZoneDAO.deleteHeartrateZoneModelByUserid("user1");
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		
	}

	@Test
	public void testGetSpeedHeartrateFactor() {
		long now = new Date().getTime();
		long nowBeforeTwoDays = now - (2*24*3600000);
		long nowBeforeOneDay = now - (24*3600000);
		long nowBeforeOneDayFiftyMinutes = nowBeforeOneDay - (3000000);
		double speedHeartrateFactor = 0.0;
		
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid("user1");
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
		fitnessTrainingSessionBean.setVdot(23.0);
		fitnessTrainingSessionBean.setTrainingSessionId("1001");
		
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		FitnessManager fitnessManager = (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		
		fitnessTrainingSessionDAO.deleteAllTrainingSessionsForUser("user1");
		
		speedHeartrateFactor = fitnessManager.getSpeedHeartrateFactor("user1");
		Assert.assertTrue(speedHeartrateFactor == 0.0);
		
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		
		fitnessTrainingSessionBean.setStartTime(new Timestamp(now - 24 * 2 * 3600000 - 3600000));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(now - 24 * 2* 3600000));
		fitnessTrainingSessionBean.setVdot(23.5);
		fitnessTrainingSessionBean.setTrainingSessionId("1002");
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		
		fitnessTrainingSessionBean.setStartTime(new Timestamp(now - 24 * 3 * 3600000 - 3600000 ));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(now - 24 * 3 *3600000));
		fitnessTrainingSessionBean.setVdot(22.0);
		fitnessTrainingSessionBean.setTrainingSessionId("1003");
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		
		fitnessTrainingSessionBean.setStartTime(new Timestamp(now - 24 * 4 * 3600000 - 3600000 ));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(now - 24 * 4 *3600000));
		fitnessTrainingSessionBean.setVdot(22.5);
		fitnessTrainingSessionBean.setTrainingSessionId("1004");
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		
		speedHeartrateFactor = fitnessManager.getSpeedHeartrateFactor("user1");
		Assert.assertTrue(speedHeartrateFactor > 0.0);
		
		fitnessTrainingSessionDAO.deleteAllTrainingSessionsForUser("user1");
	}

	
@Test
public void testFitnessDetrainingPenalty() {
	
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		FitnessManager fitnessManager = (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
		
		long nowPastOneHour = now - 3600000;
		long nowPastTwoHour = now - 7200000;
		String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
		Integer traineeClassification = 2;
		Double localRegressionMinimumOfHomeostasisIndex = 130.0;
		Double recentMinimumOfHomeostasisIndex = 110.0;
		Double recentTotalLoadOfExercise = 100.0;
		Double previousTotalLoadOfExercise = 125.0;
	
		fitnessHomeostasisIndexBean.setUserid(userid);
		fitnessHomeostasisIndexBean.setTraineeClassification(traineeClassification);
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(localRegressionMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(recentMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentTotalLoadOfExercise(recentTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(previousTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setRecentEndTime(new Timestamp(now));
		fitnessHomeostasisIndexBean.setPreviousEndTime(new Timestamp(nowPastTwoHour));
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		
		Assert.assertEquals(0.0, fitnessManager.getFitnessDetrainingPenalty(userid));
		
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
		fitnessHomeostasisIndexBean.setRecentEndTime(new Timestamp(now - 15 * 24 * 3600 * 1000));
		fitnessHomeostasisIndexBean.setPreviousEndTime(new Timestamp(now - 30 * 24* 3600 * 1000));
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		
		Assert.assertTrue(fitnessManager.getFitnessDetrainingPenalty(userid) > 0.0);
		
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
	}

	@Test
	public void testDeleteFitnessTrainingSessionbyTrainingSessionId() {
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		FitnessManager fitnessManager = (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		
		fitnessManager.deleteFitnessTrainingSessionbyTrainingSessionId("test9999");
		
		fitnessTrainingSessionBean.setUserid(userid);
		fitnessTrainingSessionBean.setTrainingSessionId("test9999");
		fitnessTrainingSessionBean.setStartTime(new Timestamp(now - 2 * 3600 * 1000 ));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(now - 1 * 3600 * 1000));
		fitnessTrainingSessionBean.setSurfaceIndex(ShapeIndexAlgorithm.RUNNING_SURFACE_MUD_SNOW_SAND);
		
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertNotNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById("test9999"));
		
		
		fitnessManager.deleteFitnessTrainingSessionbyTrainingSessionId("test9999");
		
		Assert.assertNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById("test9999"));
	}
	
	@Test
	public void testGetTrainingSessionById() {
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		FitnessManager fitnessManager = (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
	
		Assert.assertNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById(""));
	
		fitnessManager.deleteFitnessTrainingSessionbyTrainingSessionId("test9999");
		Assert.assertNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById("test9999"));
	
		fitnessTrainingSessionBean.setUserid(userid);
		fitnessTrainingSessionBean.setTrainingSessionId("test9999");
		fitnessTrainingSessionBean.setStartTime(new Timestamp(now - 2 * 3600 * 1000 ));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(now - 1 * 3600 * 1000));
		fitnessTrainingSessionBean.setSurfaceIndex(ShapeIndexAlgorithm.RUNNING_SURFACE_MUD_SNOW_SAND);
	
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertNotNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById("test9999"));
		fitnessManager.deleteFitnessTrainingSessionbyTrainingSessionId("test9999");
	}
	
	@Test
	public void testUpdateSpeedHeartRateModel() {
		long nowBeforeTwoDays = now - (2*24*3600000);
		long nowBeforeOneDay = now - (24*3600000);
		long nowBeforeOneDayFiftyMinutes = nowBeforeOneDay - (3000000);
		
		FitnessManager fitnessManager =  (FitnessManager) smartbeatContext.getBean("fitnessManagerMySQLImpl");
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
		
		fitnessManager.updateSpeedHeartRateModel(fitnessTrainingSessionBean.getUserid(), fitnessTrainingSessionBean);
		Assert.assertNotNull(fitnessTrainingSessionBean.getVdot());
	}

}
