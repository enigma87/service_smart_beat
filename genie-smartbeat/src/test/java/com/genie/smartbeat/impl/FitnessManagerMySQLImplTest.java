package com.genie.smartbeat.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTimeUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.TestSetup;
import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessHeartrateZoneBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.core.HeartrateTestValidityStatus;
import com.genie.smartbeat.core.TrainingSessionValidityStatus;
import com.genie.smartbeat.core.exceptions.session.InvalidSpeedDistributionException;
import com.genie.smartbeat.core.exceptions.session.InvalidTimeDistributionException;
import com.genie.smartbeat.dao.FitnessHeartrateTestDAO;
import com.genie.smartbeat.dao.FitnessHeartrateZoneDAO;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;
import com.genie.smartbeat.util.SmartbeatIDGenerator;
import com.genie.social.beans.UserBean;
import com.genie.social.core.UserManager;
import com.genie.social.dao.UserDao;
/**
 * @author vidhun
 *
 */

public class FitnessManagerMySQLImplTest {

	private static AbstractApplicationContext smartbeatContext;	
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	private static long now;
	private static FitnessManagerMySQLImpl fitnessManagerMySQLImpl;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		smartbeatContext = TestSetup.getInstance();
		fitnessManagerMySQLImpl = (FitnessManagerMySQLImpl) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);		

		now = cal.getTime().getTime();
		fitnessManagerMySQLImpl.clearTraineeData(userid);
	}
		
	@Test 
	public void testSaveFitnessTrainingSession() {
		
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid(userid);
		
		/*covering exceptions*/
		/*invalid duration of 5 min*/
		Calendar cal = Calendar.getInstance();
		Timestamp startTime = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE, 5);
		Timestamp endTime = new Timestamp(cal.getTimeInMillis());
		fitnessTrainingSessionBean.setStartTime(startTime);
		fitnessTrainingSessionBean.setEndTime(endTime);
		fitnessManagerMySQLImpl.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertEquals(TrainingSessionValidityStatus.INVALID_TIMESTAMP, fitnessTrainingSessionBean.getValidityStatus());
		
		/*setting a valid duration*/
		cal.set(Calendar.HOUR, 10);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		startTime = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE,60);
		endTime = new Timestamp(cal.getTimeInMillis());
		fitnessTrainingSessionBean.setStartTime(startTime);
		fitnessTrainingSessionBean.setEndTime(endTime);
				
		/*invalid speed distribution */
		/*zone 2 under min speed limit*/
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz1Distance(1000.0);
		fitnessTrainingSessionBean.setHrz2Time(200.0);
		fitnessTrainingSessionBean.setHrz2Distance(5920.0);
		fitnessTrainingSessionBean.setHrz3Time(14.0);
		fitnessTrainingSessionBean.setHrz3Distance(2753.33);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2200.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);
		fitnessManagerMySQLImpl.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertEquals(TrainingSessionValidityStatus.INVALID_SPEED_DISTRIBUTION, fitnessTrainingSessionBean.getValidityStatus());
		
		/*setting a first session*/
		/*creating a user for context*/
		/*Valid user bean needed for age and gender*/
		UserBean user = new UserBean();
		user.setUserid(userid);
		user.setEmail("abc@xyz.com");
		user.setFirstName("Jane");
		user.setGender(UserManager.GENDER_FEMALE);
		user.setAccessToken("atoken");
		user.setAccessTokenType("facebook");		
		UserDao userDao = (UserDao)smartbeatContext.getBean("userDao");
		userDao.createUser(user);
		/*Sandra scenario 2*/
		fitnessTrainingSessionBean.setHrz1Time(5.0);
		fitnessTrainingSessionBean.setHrz1Distance(1000.0);
		fitnessTrainingSessionBean.setHrz2Time(32.0);
		fitnessTrainingSessionBean.setHrz2Distance(5920.0);
		fitnessTrainingSessionBean.setHrz3Time(14.0);
		fitnessTrainingSessionBean.setHrz3Distance(2753.33);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2200.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);
		fitnessManagerMySQLImpl.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertEquals(TrainingSessionValidityStatus.VALID, fitnessTrainingSessionBean.getValidityStatus());
		FitnessTrainingSessionDAO ftsDAO = (FitnessTrainingSessionDAO)smartbeatContext.getBean("fitnessTrainingSessionDAO");
		Assert.assertNotNull(ftsDAO.getFitnessTrainingSessionById(fitnessTrainingSessionBean.getTrainingSessionId()));
		Assert.assertEquals(SmartbeatIDGenerator.getFirstId(userid, SmartbeatIDGenerator.MARKER_TRAINING_SESSION_ID), fitnessTrainingSessionBean.getTrainingSessionId());
		
		
		/*invalid timestamp - overlaps with previous training session*/
		cal.add(Calendar.MINUTE,-1);
		startTime = new Timestamp(cal.getTimeInMillis());
		startTime.setNanos(0);
		cal.add(Calendar.MINUTE,40);
		endTime = new Timestamp(cal.getTimeInMillis());
		endTime.setNanos(0);
		fitnessTrainingSessionBean.setStartTime(startTime);
		fitnessTrainingSessionBean.setEndTime(endTime);
		fitnessManagerMySQLImpl.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertEquals(TrainingSessionValidityStatus.INVALID_IN_CHRONOLOGY, fitnessTrainingSessionBean.getValidityStatus());
		
		/*creating a valid second session*/
		/*Sandra scenario 7*/
		cal.set(Calendar.HOUR, 19);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		startTime = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE,110);
		endTime = new Timestamp(cal.getTimeInMillis());
		fitnessTrainingSessionBean.setStartTime(startTime);
		fitnessTrainingSessionBean.setEndTime(endTime);		
		fitnessTrainingSessionBean.setHrz1Time(8.0);
		fitnessTrainingSessionBean.setHrz1Distance(1000.0);
		fitnessTrainingSessionBean.setHrz2Time(42.0);
		fitnessTrainingSessionBean.setHrz2Distance(7420.0);
		fitnessTrainingSessionBean.setHrz3Time(34.0);
		fitnessTrainingSessionBean.setHrz3Distance(6460.0);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2133.33);
		fitnessTrainingSessionBean.setHrz5Time(10.0);
		fitnessTrainingSessionBean.setHrz5Distance(2166.67);
		fitnessTrainingSessionBean.setHrz6Time(6.0);
		fitnessTrainingSessionBean.setHrz6Distance(1410.0);
		String firstSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
		fitnessManagerMySQLImpl.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertEquals(TrainingSessionValidityStatus.VALID, fitnessTrainingSessionBean.getValidityStatus());
		Assert.assertEquals(SmartbeatIDGenerator.getNextId(firstSessionId), fitnessTrainingSessionBean.getTrainingSessionId());
		
		fitnessManagerMySQLImpl.clearTraineeData(userid);
		userDao.deleteUser(userid);
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
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(-80.0);
		fitnessHomeostasisIndexBean.setPreviousEndTime(new Timestamp(nowPastTwoDays));
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(140.0);
		fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(-10.0);
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);		
		
		Timestamp timeAtConsideration = new Timestamp(DateTimeUtils.currentTimeMillis());
		Double points = fitnessManagerMySQLImpl.getFitnessSupercompensationPoints(fitnessHomeostasisIndexBean.getUserid(), timeAtConsideration);
		Assert.assertEquals(0.0, points);
		
		fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(-1.0);
		fitnessHomeostasisIndexDAO.updateHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		
		points = fitnessManagerMySQLImpl.getFitnessSupercompensationPoints(fitnessHomeostasisIndexBean.getUserid(), timeAtConsideration);
		Assert.assertTrue(points > 0.0);
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
	}
	
	@Test
	public void testSaveHeartRateTest(){
		FitnessHeartrateTestDAO hrtDAO = (FitnessHeartrateTestDAO)smartbeatContext.getBean("fitnessHeartrateTestDAO");
		FitnessHeartrateTestBean fitnessHeartrateTestBean = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean.setUserid(userid);		
		
		/*invalid timestamp*/
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.INVALID_TIMESTAMP, fitnessHeartrateTestBean.getValidityStatus());
		
		/*valid timestamp but invalid heartrate*/
		Calendar cal = Calendar.getInstance();
		Timestamp timeOfRecord = new Timestamp(cal.getTimeInMillis());
		fitnessHeartrateTestBean.setTimeOfRecord(timeOfRecord);
		
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.INVALID_HEARTRATE, fitnessHeartrateTestBean.getValidityStatus());
		
		
		/*invalid resting heartrate*/
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		fitnessHeartrateTestBean.setHeartrate(15.0);
		
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.INVALID_HEARTRATE, fitnessHeartrateTestBean.getValidityStatus());
		
		/*valid first resting heartrate*/
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		fitnessHeartrateTestBean.setHeartrate(60.0);
		
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.VALID, fitnessHeartrateTestBean.getValidityStatus());
		Assert.assertEquals(SmartbeatIDGenerator.getFirstId(userid, SmartbeatIDGenerator.MARKER_HEARTRATE_TEST_ID), fitnessHeartrateTestBean.getHeartrateTestId());
		Assert.assertNotNull(hrtDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean.getHeartrateTestId()));
		Assert.assertEquals(1, fitnessHeartrateTestBean.getDayOfRecord().intValue());
		
		/*second resting heartrate invalid in chronology*/
		cal.add(Calendar.HOUR, -1);
		timeOfRecord = new Timestamp(cal.getTimeInMillis());
		fitnessHeartrateTestBean.setTimeOfRecord(timeOfRecord);
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		fitnessHeartrateTestBean.setHeartrate(60.0);
		
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.INVALID_IN_CHRONOLOGY, fitnessHeartrateTestBean.getValidityStatus());
		
		/*valid second resting heartrate*/
		cal.add(Calendar.HOUR, 1);
		timeOfRecord = new Timestamp(cal.getTimeInMillis());
		fitnessHeartrateTestBean.setTimeOfRecord(timeOfRecord);
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		fitnessHeartrateTestBean.setHeartrate(55.0);
		
		String previousTestId = fitnessHeartrateTestBean.getHeartrateTestId();
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.VALID, fitnessHeartrateTestBean.getValidityStatus());
		Assert.assertEquals(SmartbeatIDGenerator.getNextId(previousTestId), fitnessHeartrateTestBean.getHeartrateTestId());
		Assert.assertNotNull(hrtDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean.getHeartrateTestId()));
		Assert.assertEquals(1, fitnessHeartrateTestBean.getDayOfRecord().intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING).intValue());
		
		/*valid third resting heartrate*/
		/*one day ahead to check day of record*/
		cal.add(Calendar.DATE, 1);
		timeOfRecord = new Timestamp(cal.getTimeInMillis());
		fitnessHeartrateTestBean.setTimeOfRecord(timeOfRecord);
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		fitnessHeartrateTestBean.setHeartrate(52.0);
		
		previousTestId = fitnessHeartrateTestBean.getHeartrateTestId();
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.VALID, fitnessHeartrateTestBean.getValidityStatus());
		Assert.assertEquals(SmartbeatIDGenerator.getNextId(previousTestId), fitnessHeartrateTestBean.getHeartrateTestId());
		Assert.assertNotNull(hrtDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean.getHeartrateTestId()));
		Assert.assertEquals(2, fitnessHeartrateTestBean.getDayOfRecord().intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING).intValue());
		
		/*valid first maximal heartrate*/
		/*one day ahead to check day of record*/		
		timeOfRecord = new Timestamp(cal.getTimeInMillis());
		fitnessHeartrateTestBean.setTimeOfRecord(timeOfRecord);
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
		fitnessHeartrateTestBean.setHeartrate(189.0);
		
		previousTestId = fitnessHeartrateTestBean.getHeartrateTestId();
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.VALID, fitnessHeartrateTestBean.getValidityStatus());
		Assert.assertEquals(SmartbeatIDGenerator.getNextId(previousTestId), fitnessHeartrateTestBean.getHeartrateTestId());
		Assert.assertNotNull(hrtDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean.getHeartrateTestId()));
		Assert.assertEquals(2, fitnessHeartrateTestBean.getDayOfRecord().intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING).intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL).intValue());
		
		/*invalid first threshold heartrate*/
		/*greater than the maximal heartrate*/		
		timeOfRecord = new Timestamp(cal.getTimeInMillis());
		fitnessHeartrateTestBean.setTimeOfRecord(timeOfRecord);
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD);
		fitnessHeartrateTestBean.setHeartrate(195.0);
		
		previousTestId = fitnessHeartrateTestBean.getHeartrateTestId();
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.INVALID_HEARTRATE, fitnessHeartrateTestBean.getValidityStatus());
		
		/*valid first threshold heartrate*/
		/*one day ahead to check day of record*/
		cal.add(Calendar.DATE, 1);
		timeOfRecord = new Timestamp(cal.getTimeInMillis());
		fitnessHeartrateTestBean.setTimeOfRecord(timeOfRecord);
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD);
		fitnessHeartrateTestBean.setHeartrate(147.0);
		
		previousTestId = fitnessHeartrateTestBean.getHeartrateTestId();
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.VALID, fitnessHeartrateTestBean.getValidityStatus());
		Assert.assertEquals(SmartbeatIDGenerator.getNextId(previousTestId), fitnessHeartrateTestBean.getHeartrateTestId());
		Assert.assertNotNull(hrtDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean.getHeartrateTestId()));
		Assert.assertEquals(3, fitnessHeartrateTestBean.getDayOfRecord().intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING).intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL).intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD).intValue());
		
		
		/*valid first orthostatic heartrate*/
		/*one day ahead to check day of record*/
		cal.add(Calendar.DATE, 1);
		timeOfRecord = new Timestamp(cal.getTimeInMillis());
		fitnessHeartrateTestBean.setTimeOfRecord(timeOfRecord);
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean.setHeartrate(78.0);
		
		previousTestId = fitnessHeartrateTestBean.getHeartrateTestId();
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.VALID, fitnessHeartrateTestBean.getValidityStatus());
		Assert.assertEquals(SmartbeatIDGenerator.getNextId(previousTestId), fitnessHeartrateTestBean.getHeartrateTestId());
		Assert.assertNotNull(hrtDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean.getHeartrateTestId()));
		Assert.assertEquals(4, fitnessHeartrateTestBean.getDayOfRecord().intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING).intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL).intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD).intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC).intValue());
		
		/*valid second orthostatic heartrate to check non-deletion of SOHR tests*/
		/*one day ahead to check day of record*/
		cal.add(Calendar.DATE, 1);
		timeOfRecord = new Timestamp(cal.getTimeInMillis());
		fitnessHeartrateTestBean.setTimeOfRecord(timeOfRecord);
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean.setHeartrate(78.0);
		
		previousTestId = fitnessHeartrateTestBean.getHeartrateTestId();
		fitnessManagerMySQLImpl.saveHeartrateTest(fitnessHeartrateTestBean);
		Assert.assertEquals(HeartrateTestValidityStatus.VALID, fitnessHeartrateTestBean.getValidityStatus());
		Assert.assertEquals(SmartbeatIDGenerator.getNextId(previousTestId), fitnessHeartrateTestBean.getHeartrateTestId());
		Assert.assertNotNull(hrtDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean.getHeartrateTestId()));
		Assert.assertEquals(5, fitnessHeartrateTestBean.getDayOfRecord().intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING).intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL).intValue());
		Assert.assertEquals(1, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD).intValue());
		Assert.assertEquals(2, hrtDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC).intValue());
		
		hrtDAO.deleteAllHeartrateTestsForUser(userid);
	}
		
	@Test
	public void testGetHeartrateZones(){
		/*No test history*/
		/*creating user for context*/
		UserDao userDAO = (UserDao)smartbeatContext.getBean("userDao");
		Calendar cal = Calendar.getInstance();
		UserBean user = new UserBean();
		user.setUserid(userid);
		user.setAccessToken("accessToken1");
		user.setAccessTokenType("facebook");
		user.setFirstName("Suresh");
		user.setEmail("suresh@acme.com");		
		cal.add(Calendar.YEAR, -55);
		user.setDob(new java.sql.Date(cal.getTimeInMillis()));
		user.setGender(UserManager.GENDER_MALE);
		userDAO.createUser(user);
		
		double[][] heartrateZones = fitnessManagerMySQLImpl.getHeartrateZones(userid);
		Assert.assertEquals(72.0,  	heartrateZones[1][0]);
		Assert.assertEquals(87.52,	heartrateZones[1][1]);
		Assert.assertEquals(87.52, 	heartrateZones[2][0]);
		Assert.assertEquals(95.28, heartrateZones[2][1]);
		Assert.assertEquals(95.28, heartrateZones[3][0]);
		Assert.assertEquals(99.06, heartrateZones[3][1]);
		Assert.assertEquals(99.06, heartrateZones[4][0]);
		Assert.assertEquals(105.04, heartrateZones[4][1]);
		Assert.assertEquals(105.04, heartrateZones[5][0]);
		Assert.assertEquals(165.76, heartrateZones[5][1]);
		Assert.assertEquals(165.76, heartrateZones[6][0]);
		Assert.assertEquals(171.75, heartrateZones[6][1]);
		
		/*creating HI bean for trainee classification*/
		FitnessHomeostasisIndexDAO hiDAO = (FitnessHomeostasisIndexDAO)smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		FitnessHomeostasisIndexBean hiBean = new FitnessHomeostasisIndexBean();
		hiBean.setUserid(userid);
		hiBean.setTraineeClassification(ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_UNTRAINED);
		hiDAO.createHomeostasisIndexModel(hiBean);
		
		heartrateZones = fitnessManagerMySQLImpl.getHeartrateZones(userid);
		Assert.assertEquals(72.0,  	heartrateZones[1][0]);
		Assert.assertEquals(87.52,	heartrateZones[1][1]);
		Assert.assertEquals(87.52, 	heartrateZones[2][0]);
		Assert.assertEquals(95.28, heartrateZones[2][1]);
		Assert.assertEquals(95.28, heartrateZones[3][0]);
		Assert.assertEquals(99.06, heartrateZones[3][1]);
		Assert.assertEquals(99.06, heartrateZones[4][0]);
		Assert.assertEquals(105.04, heartrateZones[4][1]);
		Assert.assertEquals(105.04, heartrateZones[5][0]);
		Assert.assertEquals(165.76, heartrateZones[5][1]);
		Assert.assertEquals(165.76, heartrateZones[6][0]);
		Assert.assertEquals(171.75, heartrateZones[6][1]);
		
		FitnessHeartrateTestDAO hrtDAO = (FitnessHeartrateTestDAO)smartbeatContext.getBean("fitnessHeartrateTestDAO");
		FitnessHeartrateTestBean hrtBean = new FitnessHeartrateTestBean();
		hrtBean.setUserid(userid);
		hrtBean.setDayOfRecord(1);		
		cal = Calendar.getInstance();
		Timestamp timeOfRecord = new Timestamp(cal.getTimeInMillis());
		hrtBean.setTimeOfRecord(timeOfRecord);
		
		/*resting heart rate*/
		hrtBean.setHeartrateTestId(SmartbeatIDGenerator.getFirstId(userid, SmartbeatIDGenerator.MARKER_HEARTRATE_TEST_ID));
		hrtBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		hrtBean.setHeartrate(60.0);
		hrtDAO.createHeartrateTest(hrtBean);
		
		/*threshold heart rate*/
		String previousTestId = hrtBean.getHeartrateTestId();
		hrtBean.setHeartrateTestId(SmartbeatIDGenerator.getNextId(previousTestId));
		hrtBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD);
		hrtBean.setHeartrate(147.0);
		hrtDAO.createHeartrateTest(hrtBean);
		
		/*maximal heart rate*/
		previousTestId = hrtBean.getHeartrateTestId();
		hrtBean.setHeartrateTestId(SmartbeatIDGenerator.getNextId(previousTestId));
		hrtBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
		hrtBean.setHeartrate(189.0);
		hrtDAO.createHeartrateTest(hrtBean);
		
		heartrateZones = fitnessManagerMySQLImpl.getHeartrateZones(userid);		
		Assert.assertEquals(60.0,  	heartrateZones[1][0]);
		Assert.assertEquals(103.5,	heartrateZones[1][1]);
		Assert.assertEquals(103.5, 	heartrateZones[2][0]);
		Assert.assertEquals(125.25, heartrateZones[2][1]);
		Assert.assertEquals(125.25, heartrateZones[3][0]);
		Assert.assertEquals(141.84, heartrateZones[3][1]);
		Assert.assertEquals(141.84, heartrateZones[4][0]);
		Assert.assertEquals(149.58, heartrateZones[4][1]);
		Assert.assertEquals(149.58, heartrateZones[5][0]);
		Assert.assertEquals(181.26, heartrateZones[5][1]);
		Assert.assertEquals(181.26, heartrateZones[6][0]);
		Assert.assertEquals(189.0, 	heartrateZones[6][1]);
		
		
		hrtDAO.deleteAllHeartrateTestsForUser(userid);
		hiDAO.deleteHomeostasisIndexModelByUserid(userid);
		userDAO.deleteUser(userid);
	}
	
	@Test 
	public void testGetTrainingSessionIdsInTimeInterval() {
		
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		
		Assert.assertEquals(0, fitnessManagerMySQLImpl.getTrainingSessionIdsInTimeInterval("TEST073a9e7d-9cf2-49a0-8926-f27362fd547e", Timestamp.valueOf("2013-07-03 18:23:10"), Timestamp.valueOf("2013-08-30 18:23:10")).size());
		
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
			
			Assert.assertEquals(9, fitnessManagerMySQLImpl.getTrainingSessionIdsInTimeInterval("TEST073a9e7d-9cf2-49a0-8926-f27362fd547e" , Timestamp.valueOf("2013-07-03 18:23:10"), Timestamp.valueOf("2013-08-30 18:23:10")).size());
		
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

		shapeIndexBeans = fitnessManagerMySQLImpl.getShapeIndexHistoryInTimeInterval(userid, startInterval, endInterval);
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
		
		Assert.assertNull(fitnessManagerMySQLImpl.getRecentTrainingSessionId("jack_sparrow"));
		fitnessTrainingSessionDAO.createFitnessTrainingSession(newTrainingSession);
		Assert.assertNotNull(fitnessManagerMySQLImpl.getRecentTrainingSessionId("jack_sparrow"));
		
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(newTrainingSession.getTrainingSessionId());
	}

	@Test
	public void testUpdateHeartrateZoneModel() {
		
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
		
		fitnessManagerMySQLImpl.updateHeartrateZoneModel("user1");
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
		
		fitnessTrainingSessionDAO.deleteAllTrainingSessionsForUser("user1");
		
		speedHeartrateFactor = fitnessManagerMySQLImpl.getSpeedHeartrateFactor("user1");
		Assert.assertTrue(speedHeartrateFactor == 1.0);
		
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
		
		speedHeartrateFactor = fitnessManagerMySQLImpl.getSpeedHeartrateFactor("user1");
		Assert.assertTrue(speedHeartrateFactor > 0.0);
		
		fitnessTrainingSessionDAO.deleteAllTrainingSessionsForUser("user1");
		
	}

	
@Test
	public void testGetFitnessDetrainingPenalty() {
	
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
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
		
		Timestamp timeAtConsideration = new Timestamp(DateTimeUtils.currentTimeMillis());
		Assert.assertEquals(0.0, fitnessManagerMySQLImpl.getFitnessDetrainingPenalty(userid,timeAtConsideration));
		
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
		fitnessHomeostasisIndexBean.setRecentEndTime(new Timestamp(now - 15 * 24 * 3600 * 1000));
		fitnessHomeostasisIndexBean.setPreviousEndTime(new Timestamp(now - 30 * 24* 3600 * 1000));
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		
		Assert.assertTrue(fitnessManagerMySQLImpl.getFitnessDetrainingPenalty(userid,timeAtConsideration) > 0.0);
		
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
	}

	@Test
	public void testDeleteFitnessTrainingSessionbyTrainingSessionId() {
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		
		fitnessManagerMySQLImpl.deleteFitnessTrainingSessionbyTrainingSessionId("test9999");
		
		fitnessTrainingSessionBean.setUserid(userid);
		fitnessTrainingSessionBean.setTrainingSessionId("test9999");
		fitnessTrainingSessionBean.setStartTime(new Timestamp(now - 2 * 3600 * 1000 ));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(now - 1 * 3600 * 1000));
		fitnessTrainingSessionBean.setSurfaceIndex(ShapeIndexAlgorithm.RUNNING_SURFACE_MUD_SNOW_SAND);
		
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertNotNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById("test9999"));
		
		
		fitnessManagerMySQLImpl.deleteFitnessTrainingSessionbyTrainingSessionId("test9999");
		
		Assert.assertNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById("test9999"));
	}
	
	@Test
	public void testGetTrainingSessionById() {
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
	
		Assert.assertNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById(""));
	
		fitnessManagerMySQLImpl.deleteFitnessTrainingSessionbyTrainingSessionId("test9999");
		Assert.assertNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById("test9999"));
	
		fitnessTrainingSessionBean.setUserid(userid);
		fitnessTrainingSessionBean.setTrainingSessionId("test9999");
		fitnessTrainingSessionBean.setStartTime(new Timestamp(now - 2 * 3600 * 1000 ));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(now - 1 * 3600 * 1000));
		fitnessTrainingSessionBean.setSurfaceIndex(ShapeIndexAlgorithm.RUNNING_SURFACE_MUD_SNOW_SAND);
	
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertNotNull(fitnessTrainingSessionDAO.getFitnessTrainingSessionById("test9999"));
		fitnessManagerMySQLImpl.deleteFitnessTrainingSessionbyTrainingSessionId("test9999");
	}
	
	@Test
	public void testUpdateSpeedHeartRateModel() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Timestamp endTimestamp = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE, -50);
		Timestamp startTimestamp = new Timestamp(cal.getTimeInMillis());		
				
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid(userid);
		fitnessTrainingSessionBean.setStartTime(startTimestamp);
		fitnessTrainingSessionBean.setEndTime(endTimestamp);
		
		/*invalid time distribution*/
		fitnessTrainingSessionBean.setHrz1Time(0.0);
		fitnessTrainingSessionBean.setHrz2Time(0.0);
		fitnessTrainingSessionBean.setHrz3Time(0.0);
		fitnessTrainingSessionBean.setHrz4Time(0.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz1Distance(1660.0);
		fitnessTrainingSessionBean.setHrz2Distance(1426.67);
		fitnessTrainingSessionBean.setHrz3Distance(2090.0);
		fitnessTrainingSessionBean.setHrz4Distance(2605.0);
		fitnessTrainingSessionBean.setHrz5Distance(2133.33);
		fitnessTrainingSessionBean.setHrz6Distance(2126.67);
		
		try{
			fitnessManagerMySQLImpl.updateSpeedHeartRateModel(fitnessTrainingSessionBean.getUserid(), fitnessTrainingSessionBean,null);
			Assert.fail("No InvalidSpeedDistributionException");
		}catch(InvalidSpeedDistributionException e){
			Assert.assertTrue(true);
		}
		
		/*invalid speed distribution */
		/*zones exceed max speed limit*/
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz1Distance(3000.0);
		fitnessTrainingSessionBean.setHrz2Time(4.0);
		fitnessTrainingSessionBean.setHrz2Distance(2000.0);
		fitnessTrainingSessionBean.setHrz3Time(4.0);
		fitnessTrainingSessionBean.setHrz3Distance(2000.0);
		fitnessTrainingSessionBean.setHrz4Time(4.0);
		fitnessTrainingSessionBean.setHrz4Distance(2000.0);
		fitnessTrainingSessionBean.setHrz5Time(4.0);
		fitnessTrainingSessionBean.setHrz5Distance(2000.0);
		fitnessTrainingSessionBean.setHrz6Time(4.0);
		fitnessTrainingSessionBean.setHrz6Distance(2000.0);		
		try{
			fitnessManagerMySQLImpl.updateSpeedHeartRateModel(fitnessTrainingSessionBean.getUserid(), fitnessTrainingSessionBean,null);
			Assert.fail("No InvalidSpeedDistributionException");
		}catch(InvalidSpeedDistributionException e){
			Assert.assertTrue(true);
		}
		
		/*invalid speed distribution */
		/*zones under max speed limit*/
		fitnessTrainingSessionBean.setHrz1Time(200.0);
		fitnessTrainingSessionBean.setHrz1Distance(5920.67);
		fitnessTrainingSessionBean.setHrz2Time(200.0);
		fitnessTrainingSessionBean.setHrz2Distance(5920.67);
		fitnessTrainingSessionBean.setHrz3Time(200.0);
		fitnessTrainingSessionBean.setHrz3Distance(5920.67);
		fitnessTrainingSessionBean.setHrz4Time(200.0);
		fitnessTrainingSessionBean.setHrz4Distance(5920.67);
		fitnessTrainingSessionBean.setHrz5Time(200.0);
		fitnessTrainingSessionBean.setHrz5Distance(5920.67);
		fitnessTrainingSessionBean.setHrz6Time(200.0);
		fitnessTrainingSessionBean.setHrz6Distance(5920.67);		
		try{
			fitnessManagerMySQLImpl.updateSpeedHeartRateModel(fitnessTrainingSessionBean.getUserid(), fitnessTrainingSessionBean,null);
			Assert.fail("No InvalidSpeedDistributionException");
		}catch(InvalidSpeedDistributionException e){
			Assert.assertTrue(true);
		}
		
		/*invalid speed distribution */
		/*zone 1 exceeds max speed limit*/
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz1Distance(3000.0);
		fitnessTrainingSessionBean.setHrz2Time(32.0);
		fitnessTrainingSessionBean.setHrz2Distance(5920.0);
		fitnessTrainingSessionBean.setHrz3Time(14.0);
		fitnessTrainingSessionBean.setHrz3Distance(2753.33);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2200.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);		
		try{
			fitnessManagerMySQLImpl.updateSpeedHeartRateModel(fitnessTrainingSessionBean.getUserid(), fitnessTrainingSessionBean,null);
			Assert.fail("No InvalidSpeedDistributionException");
		}catch(InvalidSpeedDistributionException e){
			Assert.assertTrue(true);
		}
		
		/*invalid speed distribution */
		/*zone 2 under min speed limit*/
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz1Distance(1000.0);
		fitnessTrainingSessionBean.setHrz2Time(200.0);
		fitnessTrainingSessionBean.setHrz2Distance(5920.0);
		fitnessTrainingSessionBean.setHrz3Time(14.0);
		fitnessTrainingSessionBean.setHrz3Distance(2753.33);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2200.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);		
		try{
			fitnessManagerMySQLImpl.updateSpeedHeartRateModel(fitnessTrainingSessionBean.getUserid(), fitnessTrainingSessionBean,null);
			Assert.fail("No InvalidSpeedDistributionException");
		}catch(InvalidSpeedDistributionException e){
			Assert.assertTrue(true);
		}
		
		/*valid speed distribution */
		/*no previous vdot and no surface index*/
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz1Distance(1000.0);
		fitnessTrainingSessionBean.setHrz2Time(32.0);
		fitnessTrainingSessionBean.setHrz2Distance(5920.0);
		fitnessTrainingSessionBean.setHrz3Time(14.0);
		fitnessTrainingSessionBean.setHrz3Distance(2753.33);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2200.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);
		
		try{
			fitnessManagerMySQLImpl.updateSpeedHeartRateModel(	fitnessTrainingSessionBean.getUserid(), 
																fitnessTrainingSessionBean,
																null);
			/*expected vdot 46.36*/
			Assert.assertEquals(Math.round(46.36*100), Math.round(fitnessTrainingSessionBean.getVdot()*100));
		}catch(InvalidSpeedDistributionException e){
			Assert.fail("Unexpected InvalidSpeedDistributionException");
		}
		
		/*valid speed distribution */
		/*no altitude and no surface index*/
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz1Distance(1000.0);
		fitnessTrainingSessionBean.setHrz2Time(32.0);
		fitnessTrainingSessionBean.setHrz2Distance(5920.0);
		fitnessTrainingSessionBean.setHrz3Time(14.0);
		fitnessTrainingSessionBean.setHrz3Distance(2753.33);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2200.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);
		
		try{
			fitnessManagerMySQLImpl.updateSpeedHeartRateModel(	fitnessTrainingSessionBean.getUserid(), 
																fitnessTrainingSessionBean,
																null);
			/*expected vdot 46.36*/
			Assert.assertEquals(Math.round(46.36*100), Math.round(fitnessTrainingSessionBean.getVdot()*100));
		}catch(InvalidSpeedDistributionException e){
			Assert.fail("Unexpected InvalidSpeedDistributionException");
		}
		
		/*valid speed distribution */
		/*no altitude and surface index - good forest path*/
		fitnessTrainingSessionBean.setSurfaceIndex(ShapeIndexAlgorithm.RUNNING_SURFACE_GOOD_FOREST_PATH);
		fitnessTrainingSessionBean.setHrz1Time(8.0);
		fitnessTrainingSessionBean.setHrz1Distance(1000.0);
		fitnessTrainingSessionBean.setHrz2Time(42.0);
		fitnessTrainingSessionBean.setHrz2Distance(7420.0);
		fitnessTrainingSessionBean.setHrz3Time(34.0);
		fitnessTrainingSessionBean.setHrz3Distance(6460.0);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2133.33);
		fitnessTrainingSessionBean.setHrz5Time(10.0);
		fitnessTrainingSessionBean.setHrz5Distance(2166.67);
		fitnessTrainingSessionBean.setHrz6Time(6.0);
		fitnessTrainingSessionBean.setHrz6Distance(1410.0);
		
		try{
			fitnessManagerMySQLImpl.updateSpeedHeartRateModel(	fitnessTrainingSessionBean.getUserid(), 
																fitnessTrainingSessionBean,
																null);
			/*expected vdot 47.07*/			
			Assert.assertEquals(Math.round(47.07*100), Math.round(fitnessTrainingSessionBean.getVdot()*100));
		}catch(InvalidSpeedDistributionException e){
			Assert.fail("Unexpected InvalidSpeedDistributionException");
		}
		
		/*valid speed distribution */
		/*altitude 432m and surface index - short grass path*/
		/*needs previous vDot - 46.4*/
		FitnessTrainingSessionBean previousTrainingSessionBean = new FitnessTrainingSessionBean();
		previousTrainingSessionBean.setUserid(userid);
		previousTrainingSessionBean.setVdot(46.4);
		
		fitnessTrainingSessionBean.setSurfaceIndex(ShapeIndexAlgorithm.RUNNING_SURFACE_MEDIOCRE_SHORT_GRASS);
		fitnessTrainingSessionBean.setAverageAltitude(432.0);
		fitnessTrainingSessionBean.setHrz1Time(0.0);
		fitnessTrainingSessionBean.setHrz1Distance(0.0);
		fitnessTrainingSessionBean.setHrz2Time(32.0);
		fitnessTrainingSessionBean.setHrz2Distance(5493.33);
		fitnessTrainingSessionBean.setHrz3Time(8.0);
		fitnessTrainingSessionBean.setHrz3Distance(1466.67);
		fitnessTrainingSessionBean.setHrz4Time(23.0);
		fitnessTrainingSessionBean.setHrz4Distance(4638.33);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);
		
		try{
			fitnessManagerMySQLImpl.updateSpeedHeartRateModel(	fitnessTrainingSessionBean.getUserid(), 
																fitnessTrainingSessionBean,
																previousTrainingSessionBean);
			/*expected vdot 43.74*/			
			Assert.assertEquals(Math.round(43.74*100), Math.round(fitnessTrainingSessionBean.getVdot()*100));
		}catch(InvalidSpeedDistributionException e){
			Assert.fail("Unexpected InvalidSpeedDistributionException");
		}
	}

	@Test
	public void testGetShapeIndex() {
		long nowBeforeOneDay = now - (24*3600000);
		long nowBeforeOneDayFiftyMinutes = nowBeforeOneDay - (3000000);
		
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
		fitnessTrainingSessionBean.setTrainingSessionId("test12345");
		
		
		FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO) smartbeatContext.getBean("fitnessTrainingSessionDAO");
		fitnessTrainingSessionDAO.deleteAllTrainingSessionsForUser(userid);
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		Assert.assertEquals(100.0, fitnessManagerMySQLImpl.getShapeIndex(null));
		Assert.assertEquals(100.0, fitnessManagerMySQLImpl.getShapeIndex(""));
		Assert.assertEquals(100.0, fitnessManagerMySQLImpl.getShapeIndex(fitnessTrainingSessionBean.getTrainingSessionId()));

		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		
		long nowPastOneHour = now - 3600000;
		long nowPastTwoHour = now - 7200000;
		String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
		Integer traineeClassification = 2;
		Double localRegressionMinimumOfHomeostasisIndex = 130.0;
		Double recentMinimumOfHomeostasisIndex = 110.0;
		Double recentTotalLoadOfExercise = 100.0;
		Double previousTotalLoadOfExercise = 125.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
		fitnessHomeostasisIndexBean.setUserid(userid);
		fitnessHomeostasisIndexBean.setTraineeClassification(traineeClassification);
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(localRegressionMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(recentMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentTotalLoadOfExercise(recentTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(previousTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setRecentEndTime(new Timestamp(now - 15 * 24 * 3600 * 1000));
		fitnessHomeostasisIndexBean.setPreviousEndTime(new Timestamp(now - 30 * 24* 3600 * 1000));
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);

		
		FitnessShapeIndexBean fitnessShapeIndexBean = new FitnessShapeIndexBean(); 
		fitnessShapeIndexBean.setUserid(userid);
		fitnessShapeIndexBean.setShapeIndex(120.0);
		fitnessShapeIndexBean.setTimeOfRecord(new Timestamp(nowBeforeOneDayFiftyMinutes));
		fitnessShapeIndexBean.setSessionOfRecord(fitnessTrainingSessionBean.getTrainingSessionId());
		
		FitnessShapeIndexDAO fitnessShapeIndexDAO = (FitnessShapeIndexDAO) smartbeatContext.getBean("fitnessShapeIndexDAO");
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean);
		Assert.assertTrue(fitnessManagerMySQLImpl.getShapeIndex(fitnessTrainingSessionBean.getTrainingSessionId()) > 100.0);
	
		fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(userid);
		fitnessTrainingSessionDAO.deleteAllTrainingSessionsForUser(userid);
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
	
	}

	/*
	 *  THE METHOD ISN'T USABLE YET, LETS KEEP THIS COMMENTED UNTIL ITS DONE
	 * 
	 * */
	@Test
	public void testGetOrthostaticHeartrateFactor() {
		Calendar cal = Calendar.getInstance();
	
		FitnessHeartrateTestDAO fitnessHeartrateTestDAO = (FitnessHeartrateTestDAO)smartbeatContext.getBean("fitnessHeartrateTestDAO");
		FitnessHeartrateZoneDAO fitnessHeartrateZoneDAO = (FitnessHeartrateZoneDAO) smartbeatContext.getBean("fitnessHeartrateZoneDAO");
		fitnessHeartrateZoneDAO.deleteHeartrateZoneModelByUserid("user1");
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		
		double[] ohrtValues = {101.0,99.0,100.0,97.0,102.0,96.0,98.0,92.0,95.0,91.0};
		FitnessHeartrateTestBean bean = null;
		for(int i=10; i>=1; i--){
			cal.add(Calendar.DATE, -1);
			bean = new FitnessHeartrateTestBean();
			bean.setUserid("user1");
			bean.setHeartrateTestId("user1Test"+i);
			bean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
			bean.setHeartrate(ohrtValues[i-1]);
			bean.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
			bean.setDayOfRecord(i);
			fitnessHeartrateTestDAO.createHeartrateTest(bean);
			
		}				
		FitnessManagerMySQLImpl fitnessManager = (FitnessManagerMySQLImpl) smartbeatContext.getBean("fitnessManagerMySQLImpl");
		double ohrFactor = fitnessManager.getOrthostaticHeartrateFactor("user1");
		Assert.assertEquals(Math.round(0.3*10), Math.round(ohrFactor*10));
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
	}


	@Test
	public void testUpdateShapeIndexModel() {
		Calendar cal = Calendar.getInstance();
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		/*Sandra scenario 1*/
		cal.set(Calendar.HOUR, 10);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp startTime = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE,60);
		Timestamp endTime = new Timestamp(cal.getTimeInMillis());
		fitnessTrainingSessionBean.setStartTime(startTime);
		fitnessTrainingSessionBean.setEndTime(endTime);
		fitnessTrainingSessionBean.setTrainingSessionId(SmartbeatIDGenerator.getFirstId(userid, SmartbeatIDGenerator.MARKER_TRAINING_SESSION_ID));
		fitnessTrainingSessionBean.setHrz1Time(5.0);
		fitnessTrainingSessionBean.setHrz1Distance(1000.0);
		fitnessTrainingSessionBean.setHrz2Time(32.0);
		fitnessTrainingSessionBean.setHrz2Distance(5920.0);
		fitnessTrainingSessionBean.setHrz3Time(14.0);
		fitnessTrainingSessionBean.setHrz3Distance(2753.33);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2200.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz5Distance(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		fitnessTrainingSessionBean.setHrz6Distance(0.0);
		
		fitnessManagerMySQLImpl.updateShapeIndexModel(	userid, 
														fitnessTrainingSessionBean, 
														null);
		FitnessShapeIndexDAO fsiDAO = (FitnessShapeIndexDAO)smartbeatContext.getBean("fitnessShapeIndexDAO");
		FitnessShapeIndexBean fitnessShapeIndexBean = fsiDAO.getShapeIndexModelByTrainingSessionId(fitnessTrainingSessionBean.getTrainingSessionId());
		Assert.assertNotNull(fitnessShapeIndexBean);
		Assert.assertEquals(Math.round(ShapeIndexAlgorithm.SHAPE_INDEX_INITIAL_VALUE), Math.round(fitnessShapeIndexBean.getShapeIndex()));
		Assert.assertEquals(fitnessTrainingSessionBean.getEndTime().toString(), fitnessShapeIndexBean.getTimeOfRecord().toString());
		Assert.assertEquals(fitnessTrainingSessionBean.getTrainingSessionId(), fitnessShapeIndexBean.getSessionOfRecord());
		
		
		/*Sandra scenario 7*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR, 19);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		startTime = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.MINUTE,110);
		endTime = new Timestamp(cal.getTimeInMillis());
		fitnessTrainingSessionBean.setStartTime(startTime);
		fitnessTrainingSessionBean.setEndTime(endTime);
		String previousSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
		fitnessTrainingSessionBean.setTrainingSessionId(SmartbeatIDGenerator.getNextId(previousSessionId));
		fitnessTrainingSessionBean.setHrz1Time(8.0);
		fitnessTrainingSessionBean.setHrz1Distance(1000.0);
		fitnessTrainingSessionBean.setHrz2Time(42.0);
		fitnessTrainingSessionBean.setHrz2Distance(7420.0);
		fitnessTrainingSessionBean.setHrz3Time(34.0);
		fitnessTrainingSessionBean.setHrz3Distance(6460.0);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz4Distance(2133.33);
		fitnessTrainingSessionBean.setHrz5Time(10.0);
		fitnessTrainingSessionBean.setHrz5Distance(2166.67);
		fitnessTrainingSessionBean.setHrz6Time(6.0);
		fitnessTrainingSessionBean.setHrz6Distance(1410.0);
		
		fitnessManagerMySQLImpl.updateShapeIndexModel(	userid, 
														fitnessTrainingSessionBean, 
														null);
		fsiDAO = (FitnessShapeIndexDAO)smartbeatContext.getBean("fitnessShapeIndexDAO");
		fitnessShapeIndexBean = fsiDAO.getShapeIndexModelByTrainingSessionId(fitnessTrainingSessionBean.getTrainingSessionId());
		Assert.assertNotNull(fitnessShapeIndexBean);

		fitnessManagerMySQLImpl.clearTraineeData(userid);
	}

	@Test
	public void testUpdateHomeostasisIndexModel() {
		
		
				
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid(userid);

		
		/*invalid time distribution*/
		fitnessTrainingSessionBean.setHrz1Time(0.0);
		fitnessTrainingSessionBean.setHrz2Time(0.0);
		fitnessTrainingSessionBean.setHrz3Time(0.0);
		fitnessTrainingSessionBean.setHrz4Time(0.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);

		try{
			fitnessManagerMySQLImpl.updateHomeostasisIndexModel(userid, fitnessTrainingSessionBean);
			Assert.fail("No InvalidTimeDistributionException");
		}catch(InvalidTimeDistributionException e){
			Assert.assertTrue(true);
		}
		
		/*invalid time distribution*/
		/*zone 1 below min time limit*/
		fitnessTrainingSessionBean.setHrz1Time(1.0);
		fitnessTrainingSessionBean.setHrz2Time(32.0);
		fitnessTrainingSessionBean.setHrz3Time(14.0);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);

		try{
			fitnessManagerMySQLImpl.updateHomeostasisIndexModel(userid, fitnessTrainingSessionBean);
			Assert.fail("No InvalidTimeDistributionException");
		}catch(InvalidTimeDistributionException e){
			Assert.assertTrue(true);
		}

		/*Sandra scenario 7*/
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 19);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp startTimestamp = new Timestamp(cal.getTimeInMillis());
		startTimestamp.setNanos(0);
		cal.add(Calendar.MINUTE, 110);
		Timestamp endTimestamp = new Timestamp(cal.getTimeInMillis());		
		endTimestamp.setNanos(0);
		fitnessTrainingSessionBean.setStartTime(startTimestamp);
		fitnessTrainingSessionBean.setEndTime(endTimestamp);
		
		/*valid time distribution*/		
		fitnessTrainingSessionBean.setHrz1Time(8.0);
		fitnessTrainingSessionBean.setHrz2Time(42.0);
		fitnessTrainingSessionBean.setHrz3Time(34.0);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz5Time(10.0);
		fitnessTrainingSessionBean.setHrz6Time(6.0);		
		/*HI functionality needs vdot*/
		fitnessTrainingSessionBean.setVdot(47.0);
		
		/*No HI model in DB*/
		FitnessHomeostasisIndexDAO hiDAO = (FitnessHomeostasisIndexDAO)smartbeatContext.getBean("fitnessHomeostasisIndexDAO");
		FitnessHomeostasisIndexBean hiBean = hiDAO.getHomeostasisIndexModelByUserid(userid);
		Assert.assertNull(hiBean);
		
		/*Valid user bean needed for age and gender*/
		UserBean user = new UserBean();
		user.setUserid(userid);
		user.setEmail("abc@xyz.com");
		user.setFirstName("Jane");
		user.setGender(UserManager.GENDER_FEMALE);
		user.setAccessToken("atoken");
		user.setAccessTokenType("facebook");		
		UserDao userDao = (UserDao)smartbeatContext.getBean("userDao");
		userDao.createUser(user);		
		try{
			fitnessManagerMySQLImpl.updateHomeostasisIndexModel(userid, fitnessTrainingSessionBean);
			hiBean = hiDAO.getHomeostasisIndexModelByUserid(userid);
			Assert.assertNotNull(hiBean);
			/*expected TLE 235.5*/
			Assert.assertEquals(Math.round(235.5*100), Math.round(hiBean.getRecentTotalLoadOfExercise()*100));
			Assert.assertEquals(ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_MODERATELY_TRAINED, hiBean.getTraineeClassification());
			Assert.assertEquals(fitnessTrainingSessionBean.getEndTime().toString(), hiBean.getRecentEndTime().toString());
			Assert.assertEquals(Math.round(-235.5*100), Math.round(hiBean.getRecentMinimumOfHomeostasisIndex()*100));
			Assert.assertEquals(Math.round(-235.5*100), Math.round(hiBean.getLocalRegressionMinimumOfHomeostasisIndex()*100));			
		}catch(InvalidTimeDistributionException e){
			Assert.fail("Unexpected InvalidTimeDistributionException");
		}
		
		/*Sandra scenario 13*/
		cal.add(Calendar.DATE, 3);
		cal.set(Calendar.HOUR,12);
		cal.set(Calendar.MINUTE,50);
		cal.set(Calendar.SECOND,00);
		cal.set(Calendar.MILLISECOND,00);
		startTimestamp = new Timestamp(cal.getTimeInMillis());
		startTimestamp.setNanos(0);
		cal.add(Calendar.MINUTE,40);
		endTimestamp = new Timestamp(cal.getTimeInMillis());		
		endTimestamp.setNanos(0);		
		fitnessTrainingSessionBean.setStartTime(startTimestamp);
		fitnessTrainingSessionBean.setEndTime(endTimestamp);
		
		fitnessTrainingSessionBean.setHrz1Time(8.0);
		fitnessTrainingSessionBean.setHrz2Time(22.0);
		fitnessTrainingSessionBean.setHrz3Time(5.0);
		fitnessTrainingSessionBean.setHrz4Time(6.0);
		fitnessTrainingSessionBean.setHrz5Time(0.0);
		fitnessTrainingSessionBean.setHrz6Time(0.0);
		/*HI functionality needs vdot*/
		fitnessTrainingSessionBean.setVdot(46.4);
		try{
			fitnessManagerMySQLImpl.updateHomeostasisIndexModel(userid, fitnessTrainingSessionBean);
			hiBean = hiDAO.getHomeostasisIndexModelByUserid(userid);
			Assert.assertEquals(Math.round(48.75*100), Math.round(hiBean.getRecentTotalLoadOfExercise()*100));
			Assert.assertEquals(ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_MODERATELY_TRAINED, hiBean.getTraineeClassification());
			Assert.assertEquals(fitnessTrainingSessionBean.getEndTime().toString(), hiBean.getRecentEndTime().toString());
			Assert.assertEquals(Math.round(-48.75*100), Math.round(hiBean.getRecentMinimumOfHomeostasisIndex()*100));
			Assert.assertEquals(Math.round(-48.75*100), Math.round(hiBean.getLocalRegressionMinimumOfHomeostasisIndex()*100));			
		}catch(InvalidTimeDistributionException e){
			Assert.fail("Unexpected InvalidTimeDistributionException");
		}		
		/*clean up hi model and user*/
		hiDAO.deleteHomeostasisIndexModelByUserid(userid);
		userDao.deleteUser(userid);
	}
}
