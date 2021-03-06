package com.genie.smartbeat.resources;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import com.genie.smartbeat.TestSetup;
import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.core.errors.HeartrateTestErrors;
import com.genie.smartbeat.core.errors.TimeErrors;
import com.genie.smartbeat.core.errors.TrainingSessionErrors;
import com.genie.smartbeat.core.exceptions.homeostasis.HomeostasisIndexModelException;
import com.genie.smartbeat.core.exceptions.session.TrainingSessionException;
import com.genie.smartbeat.dao.FitnessHeartrateTestDAO;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;
import com.genie.smartbeat.impl.FitnessManagerMySQLImpl;
import com.genie.smartbeat.json.SaveFitnessTrainingSessionRequestJson;
import com.genie.smartbeat.json.SaveHeartrateTestRequestJson;
import com.genie.smartbeat.json.debug.FitnessTrainingSessionTimePoint;
import com.genie.smartbeat.json.debug.SaveFitnessTrainingSessionDebugJson;
import com.genie.social.beans.UserBean;
import com.genie.social.core.AuthenticationStatus;
import com.genie.social.core.UserManager;
import com.genie.social.dao.UserDao;
import com.genie.social.facebook.GraphAPI;
import com.genie.social.impl.UserManagerMySQLImpl;
import com.genie.social.json.RegisterRequestJSON;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

public class TraineeResourceTest {

		private static AbstractApplicationContext applicationContext;
		private static UserDao userDao;
		private static FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO;
		private static FitnessHeartrateTestDAO fitnessHeartrateTestDAO;
		private static FitnessShapeIndexDAO fitnessShapeIndexDAO;		
		private static FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
		private static TraineeResource traineeResource;
		private static UserManagerMySQLImpl userManager;
		private static FitnessManagerMySQLImpl fitnessManager;
		private static String appID = "333643156765163";
		private static long now;
		private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
		private static UserBean user;
		
		@BeforeClass
		public static void setupUserDao(){
			applicationContext = TestSetup.getInstance();

			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			cal.set(Calendar.HOUR_OF_DAY, 12);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);		

			now = cal.getTime().getTime();
			
			userDao = (UserDao)applicationContext.getBean("userDao");
			fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO)applicationContext.getBean("fitnessHomeostasisIndexDAO");
			fitnessHeartrateTestDAO = (FitnessHeartrateTestDAO) applicationContext.getBean("fitnessHeartrateTestDAO");
			fitnessShapeIndexDAO = (FitnessShapeIndexDAO)applicationContext.getBean("fitnessShapeIndexDAO");			
			fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO)applicationContext.getBean("fitnessTrainingSessionDAO");
			traineeResource = new TraineeResource();
			traineeResource.setFitnessManager((FitnessManagerMySQLImpl) applicationContext.getBean("fitnessManagerMySQLImpl"));
			traineeResource.setUserManager((UserManagerMySQLImpl) applicationContext.getBean("userManagerMySQLImpl"));
			userManager = (UserManagerMySQLImpl) traineeResource.getUserManager();
			fitnessManager = (FitnessManagerMySQLImpl) traineeResource.getFitnessManager();
			
		}
		
		@BeforeClass
		public static void SetUpObjects() {
			user = new UserBean();
			
			Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
			java.sql.Date Dob = new java.sql.Date(now - 12000);
			
			user.setUserid("123456789");		
			user.setAccessToken("access_token_123456789");
			user.setAccessTokenType(UserBean.ACCESS_TOKEN_TYPE_FACEBOOK);
			user.setFirstName("Antony");
			user.setMiddleName("Bob");
			user.setLastName("CampBell");
			user.setDob( Dob);
			user.setGender((byte) 1);
			user.setEmail("abc@xyz.com");
			user.setPrivilegeLevel((byte) 1);
			user.setImageUrl("www.picasa.com/1002");
			user.setCreatedTs(timestamp);
			user.setLastUpdatedTs(timestamp);
			user.setLastLoginTs(timestamp);
		}
		
//		@Test
		public void testRegisterUser() throws JSONException {			
			UserBean testUser = GraphAPI.getTestUser();
			testUser.setFirstName("jack");
			testUser.setLastName("sparrow");
			testUser.setPrivilegeLevel((byte) 1);
			testUser.setGender(UserManager.GENDER_FEMALE);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -25);
			testUser.setDob(new java.sql.Date(cal.getTimeInMillis()));
			RegisterRequestJSON newUserJSON = new RegisterRequestJSON();
			newUserJSON.setAccessToken(testUser.getAccessToken());
			newUserJSON.setAccessTokenType(testUser.getAccessTokenType());			
			Assert.assertNull(userDao.getUserInfoByEmail(testUser.getEmail()));
			
			String registerResponse = traineeResource.registerUser(newUserJSON);
			Assert.assertNotNull(userDao.getUserInfoByEmail(testUser.getEmail()));
			JSONObject jsonResponse = new JSONObject(registerResponse);
			Assert.assertEquals(Status.OK.getStatusCode(), Integer.parseInt(jsonResponse.get("status").toString()));
	
			registerResponse = traineeResource.registerUser(newUserJSON);
			jsonResponse = new JSONObject(registerResponse);
			Assert.assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), Integer.parseInt(jsonResponse.getString("status").toString()));

			userDao.deleteUser(userDao.getUserInfoByEmail(testUser.getEmail()).getUserid());
			
			FacebookClient fbClient = new DefaultFacebookClient(testUser.getAccessToken());
			fbClient.deleteObject(testUser.getUserid() + "/permissions/email");
			registerResponse = traineeResource.registerUser(newUserJSON);
			jsonResponse = new JSONObject(registerResponse);
			Assert.assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), Integer.parseInt(jsonResponse.getString("status").toString()));

			newUserJSON.setAccessToken("ASDfasDFASDFaSDFAd");
			registerResponse = traineeResource.registerUser(newUserJSON);
			jsonResponse = new JSONObject(registerResponse);
			Assert.assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), Integer.parseInt(jsonResponse.getString("status").toString()));
		}

		
		@Test
		public void testGetUserInfo() throws Exception{
			String responseString = null;
			JSONObject jsonResponse;
			
			responseString = traineeResource.getUserInfo(user.getEmail(), user.getAccessToken(), user.getAccessTokenType());
			jsonResponse = new JSONObject(responseString);
			Assert.assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), Integer.parseInt(jsonResponse.get("status").toString()));

			userDao.createUser(user);
			
			responseString = traineeResource.getUserInfo(user.getEmail(), user.getAccessToken(), user.getAccessTokenType());
			jsonResponse = new JSONObject(responseString);
			System.out.println(responseString);
			Assert.assertEquals(Status.OK.getStatusCode(), Integer.parseInt(jsonResponse.get("status").toString()));
			Assert.assertEquals(user.getUserid(), new JSONObject(jsonResponse.get("obj").toString()).get("userid").toString());

			user.setEmail("rockstar@bollywood.com");
			responseString = traineeResource.getUserInfo(user.getEmail(), user.getAccessToken(), user.getAccessTokenType());
			jsonResponse = new JSONObject(responseString);
			System.out.println(responseString);
			Assert.assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), Integer.parseInt(jsonResponse.get("status").toString()));
			
			userDao.deleteUser(user.getUserid());		
	}
		
		@Test
		public void testSaveFitnessTrainingSession() throws Exception{
		
			SaveFitnessTrainingSessionRequestJson saveTrainingSessionRequestJson = new SaveFitnessTrainingSessionRequestJson();						
			String response = traineeResource.saveFitnessTrainingSession(user.getUserid(), user.getAccessToken(), user.getAccessTokenType(), saveTrainingSessionRequestJson);
			JSONObject responseJSON = new JSONObject(response);
			Assert.assertEquals("406", responseJSON.getString("status"));
			Assert.assertEquals(AuthenticationStatus.Status.DENIED.toString(), responseJSON.getString("message"));
			
			/*need user data for authorization*/
			String accessToken 		= "accessToken1";
			String accessTokenType 	= "facebook";
			Calendar cal = Calendar.getInstance();
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken(accessToken);
			user.setAccessTokenType(accessTokenType);
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);
			
			response = traineeResource.saveFitnessTrainingSession(userid, accessToken, accessTokenType, saveTrainingSessionRequestJson);
			responseJSON = new JSONObject(response);
			Assert.assertEquals("406", responseJSON.getString("status"));
			Assert.assertEquals(TimeErrors.INVALID_TIMESTAMP.toString(), responseJSON.getString("message"));
			
			/*setting a valid duration*/
			cal = Calendar.getInstance();
			cal.set(Calendar.HOUR, 10);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Timestamp startTime = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.MINUTE,60);
			Timestamp endTime = new Timestamp(cal.getTimeInMillis());
			saveTrainingSessionRequestJson.setStartTime(startTime.toString());
			saveTrainingSessionRequestJson.setEndTime(endTime.toString());
					
			/*invalid speed distribution */
			/*zone 1 over max speed limit*/
			saveTrainingSessionRequestJson.setHrz1Time(4.0);
			saveTrainingSessionRequestJson.setHrz1Distance(3000.0);
			/*zone 2 under min speed limit*/
			saveTrainingSessionRequestJson.setHrz2Time(200.0);
			saveTrainingSessionRequestJson.setHrz2Distance(5920.0);
			saveTrainingSessionRequestJson.setHrz3Time(0.0);
			saveTrainingSessionRequestJson.setHrz3Distance(0.0);
			saveTrainingSessionRequestJson.setHrz4Time(0.0);
			saveTrainingSessionRequestJson.setHrz4Distance(0.0);
			saveTrainingSessionRequestJson.setHrz5Time(0.0);
			saveTrainingSessionRequestJson.setHrz5Distance(0.0);
			saveTrainingSessionRequestJson.setHrz6Time(0.0);
			saveTrainingSessionRequestJson.setHrz6Distance(0.0);
			
			response = traineeResource.saveFitnessTrainingSession(user.getUserid(), user.getAccessToken(), user.getAccessTokenType(), saveTrainingSessionRequestJson);
			//System.out.println(response);
			responseJSON = new JSONObject(response);
			Assert.assertEquals("406", responseJSON.getString("status"));
			Assert.assertEquals(TrainingSessionErrors.INVALID_SPEED_DISTRIBUTION.toString(), responseJSON.getString("message"));
			
			cal = Calendar.getInstance();
			cal.set(Calendar.HOUR, 19);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			saveTrainingSessionRequestJson.setStartTime(new Timestamp(cal.getTimeInMillis()).toString());
			cal.add(Calendar.MINUTE, 100);
			saveTrainingSessionRequestJson.setEndTime(new Timestamp(cal.getTimeInMillis()).toString());
			saveTrainingSessionRequestJson.setHrz1Time(8.0);
			saveTrainingSessionRequestJson.setHrz1Distance(1000.0);
			saveTrainingSessionRequestJson.setHrz2Time(42.0);
			saveTrainingSessionRequestJson.setHrz2Distance(7420.0);
			saveTrainingSessionRequestJson.setHrz3Time(34.0);
			saveTrainingSessionRequestJson.setHrz3Distance(6460.0);
			saveTrainingSessionRequestJson.setHrz4Time(10.0);
			saveTrainingSessionRequestJson.setHrz4Distance(2133.33);
			saveTrainingSessionRequestJson.setHrz5Time(10.0);
			saveTrainingSessionRequestJson.setHrz5Distance(2166.67);
			saveTrainingSessionRequestJson.setHrz6Time(6.0);
			saveTrainingSessionRequestJson.setHrz6Distance(1410.0);
			
			response = traineeResource.saveFitnessTrainingSession(user.getUserid(), user.getAccessToken(), user.getAccessTokenType(), saveTrainingSessionRequestJson);
			//System.out.println(response);
			responseJSON = new JSONObject(response);
			Assert.assertEquals("200", responseJSON.getString("status"));
			
			/*invalid timestamp - overlaps with previous training session*/
			cal.add(Calendar.MINUTE,-1);
			startTime = new Timestamp(cal.getTimeInMillis());
			startTime.setNanos(0);
			cal.add(Calendar.MINUTE,40);
			endTime = new Timestamp(cal.getTimeInMillis());
			endTime.setNanos(0);
			saveTrainingSessionRequestJson.setStartTime(startTime.toString());
			saveTrainingSessionRequestJson.setEndTime(endTime.toString());
			
			response = traineeResource.saveFitnessTrainingSession(user.getUserid(), user.getAccessToken(), user.getAccessTokenType(), saveTrainingSessionRequestJson);
			//System.out.println(response);
			responseJSON = new JSONObject(response);
			Assert.assertEquals("406", responseJSON.getString("status"));
			Assert.assertEquals(TimeErrors.INVALID_IN_CHRONOLOGY.toString(), responseJSON.getString("message"));
			
			
			/*clean up*/
			fitnessTrainingSessionDAO.deleteAllTrainingSessionsForUser(userid);
			fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
			userDao.deleteUser(userid);
		}
		
		@Test
		public void testGetShapeIndex() throws Exception{
			       
		}
		
		@Test
		public void testGetHeartrateZones() {
			
		}
		
		@Test
		public void testGetTrainingSessionIdsInTimeInterval() throws JSONException{
			
			String responseString = traineeResource.getFitnessTrainingSessionIds(userid, null, null,null,null);
			JSONObject responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
			
			responseString = traineeResource.getFitnessTrainingSessionIds(null, null, null,null,null);
			responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
		
			Calendar cal = Calendar.getInstance();
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken("accessToken1");
			user.setAccessTokenType("facebook");
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);
	
			cal.setTimeInMillis(DateTimeUtils.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, 10);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Long sessionStartTime = cal.getTime().getTime();
			cal.add(Calendar.HOUR, 1);
			Long sessionEndTime = cal.getTime().getTime();
			DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
					
			FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
			fitnessTrainingSessionBean.setUserid(userid);
			fitnessTrainingSessionBean.setStartTime(new Timestamp(sessionStartTime));
			fitnessTrainingSessionBean.setEndTime(new Timestamp(sessionEndTime));
			fitnessTrainingSessionBean.setHrz1Time(4.0);
			fitnessTrainingSessionBean.setHrz2Time(32.0);
			fitnessTrainingSessionBean.setHrz3Time(14.0);
			fitnessTrainingSessionBean.setHrz4Time(10.0);
			fitnessTrainingSessionBean.setHrz5Time(0.0);
			fitnessTrainingSessionBean.setHrz6Time(0.0);
			fitnessTrainingSessionBean.setHrz1Distance(1000.0);
			fitnessTrainingSessionBean.setHrz2Distance(5920.0);
			fitnessTrainingSessionBean.setHrz3Distance(2753.33);
			fitnessTrainingSessionBean.setHrz4Distance(2200.0);
			fitnessTrainingSessionBean.setHrz5Distance(0.0);
			fitnessTrainingSessionBean.setHrz6Distance(0.0);
			fitnessTrainingSessionBean.setSurfaceIndex(0);
			fitnessTrainingSessionBean.setTrainingSessionId("test1");
			
			fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
			
		    /*Sandra Session 2*/
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 19);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			sessionStartTime = cal.getTime().getTime();
			cal.add(Calendar.MINUTE, 110);
			sessionEndTime = cal.getTime().getTime();
			DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		
			fitnessTrainingSessionBean.setUserid(userid);
			fitnessTrainingSessionBean.setStartTime(new Timestamp(sessionStartTime));
			fitnessTrainingSessionBean.setEndTime(new Timestamp(sessionEndTime));
			fitnessTrainingSessionBean.setHrz1Time(4.0);
			fitnessTrainingSessionBean.setHrz2Time(42.0);
			fitnessTrainingSessionBean.setHrz3Time(34.0);
			fitnessTrainingSessionBean.setHrz4Time(10.0);
			fitnessTrainingSessionBean.setHrz5Time(10.0);
			fitnessTrainingSessionBean.setHrz6Time(6.0);
			fitnessTrainingSessionBean.setHrz1Distance(1000.0);
			fitnessTrainingSessionBean.setHrz2Distance(7420.0);
			fitnessTrainingSessionBean.setHrz3Distance(6460.0);
			fitnessTrainingSessionBean.setHrz4Distance(2133.33);
			fitnessTrainingSessionBean.setHrz5Distance(2166.67);
			fitnessTrainingSessionBean.setHrz6Distance(1410.0);
			fitnessTrainingSessionBean.setSurfaceIndex(2);
			fitnessTrainingSessionBean.setTrainingSessionId("test2");
						
			fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
				
			/*Get Training Session Ids response*/  
			cal.add(Calendar.DATE, -2);
			Long startTime = cal.getTime().getTime();
			cal.add(Calendar.DATE, 5);
			Long endTime = cal.getTime().getTime();
			responseString = traineeResource.getFitnessTrainingSessionIds(userid, new Timestamp(startTime), new Timestamp(endTime),"accessToken1", "facebook");
			responseJSON = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(responseJSON.get("obj").toString());
			Assert.assertEquals(userid, dataJson.getString("userID"));
			JSONArray trainingSessionIds = dataJson.getJSONArray("trainingSessionIDs");
			Assert.assertEquals("test2", trainingSessionIds.getJSONObject(0).get("trainingSessionId"));
			Assert.assertNotNull(trainingSessionIds.getJSONObject(0).get("startTime"));
			Assert.assertNotNull(trainingSessionIds.getJSONObject(0).get("endTime"));
			Assert.assertEquals("test1", trainingSessionIds.getJSONObject(1).get("trainingSessionId"));
			Assert.assertNotNull(trainingSessionIds.getJSONObject(1).get("startTime"));
			Assert.assertNotNull(trainingSessionIds.getJSONObject(1).get("endTime"));
						
			/*Validation*/
			
						
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
			
		}
		
		
		@Test
		public void testGetTrainingSessionsInTimeInterval() throws JSONException{
			
			String responseString = traineeResource.getFitnessTrainingSessionsInInterval(userid, null, null,null,null);
			JSONObject responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
			
			responseString = traineeResource.getFitnessTrainingSessionsInInterval(null, null, null,null,null);
			responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
		
			Calendar cal = Calendar.getInstance();
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken("accessToken1");
			user.setAccessTokenType("facebook");
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);
	
			cal.setTimeInMillis(DateTimeUtils.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, 10);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Long sessionStartTime = cal.getTime().getTime();
			cal.add(Calendar.HOUR, 1);
			Long sessionEndTime = cal.getTime().getTime();
			DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
					
			FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
			fitnessTrainingSessionBean.setUserid(userid);
			fitnessTrainingSessionBean.setStartTime(new Timestamp(sessionStartTime));
			fitnessTrainingSessionBean.setEndTime(new Timestamp(sessionEndTime));
			fitnessTrainingSessionBean.setHrz1Time(4.0);
			fitnessTrainingSessionBean.setHrz2Time(32.0);
			fitnessTrainingSessionBean.setHrz3Time(14.0);
			fitnessTrainingSessionBean.setHrz4Time(10.0);
			fitnessTrainingSessionBean.setHrz5Time(0.0);
			fitnessTrainingSessionBean.setHrz6Time(0.0);
			fitnessTrainingSessionBean.setHrz1Distance(1000.0);
			fitnessTrainingSessionBean.setHrz2Distance(5920.0);
			fitnessTrainingSessionBean.setHrz3Distance(2753.33);
			fitnessTrainingSessionBean.setHrz4Distance(2200.0);
			fitnessTrainingSessionBean.setHrz5Distance(0.0);
			fitnessTrainingSessionBean.setHrz6Distance(0.0);
			fitnessTrainingSessionBean.setSurfaceIndex(0);
			fitnessTrainingSessionBean.setTrainingSessionId("test1");
			
			fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
			
		    /*Sandra Session 2*/
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 19);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			sessionStartTime = cal.getTime().getTime();
			cal.add(Calendar.MINUTE, 110);
			sessionEndTime = cal.getTime().getTime();
			DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		
			fitnessTrainingSessionBean.setUserid(userid);
			fitnessTrainingSessionBean.setStartTime(new Timestamp(sessionStartTime));
			fitnessTrainingSessionBean.setEndTime(new Timestamp(sessionEndTime));
			fitnessTrainingSessionBean.setHrz1Time(4.0);
			fitnessTrainingSessionBean.setHrz2Time(42.0);
			fitnessTrainingSessionBean.setHrz3Time(34.0);
			fitnessTrainingSessionBean.setHrz4Time(10.0);
			fitnessTrainingSessionBean.setHrz5Time(10.0);
			fitnessTrainingSessionBean.setHrz6Time(6.0);
			fitnessTrainingSessionBean.setHrz1Distance(1000.0);
			fitnessTrainingSessionBean.setHrz2Distance(7420.0);
			fitnessTrainingSessionBean.setHrz3Distance(6460.0);
			fitnessTrainingSessionBean.setHrz4Distance(2133.33);
			fitnessTrainingSessionBean.setHrz5Distance(2166.67);
			fitnessTrainingSessionBean.setHrz6Distance(1410.0);
			fitnessTrainingSessionBean.setSurfaceIndex(2);
			fitnessTrainingSessionBean.setTrainingSessionId("test2");
						
			fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
				
			/*Get Training Session Ids response*/  
			cal.add(Calendar.DATE, -2);
			Long startTime = cal.getTime().getTime();
			cal.add(Calendar.DATE, 5);
			Long endTime = cal.getTime().getTime();
			responseString = traineeResource.getFitnessTrainingSessionsInInterval(userid, new Timestamp(startTime), new Timestamp(endTime),"accessToken1", "facebook");
			responseJSON = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(responseJSON.get("obj").toString());
			Assert.assertEquals(userid, dataJson.getString("userID"));
			JSONArray trainingSessionIds = dataJson.getJSONArray("trainingSessions");
			
			Assert.assertEquals(userid, trainingSessionIds.getJSONObject(0).get("userid"));
			Assert.assertEquals("test2", trainingSessionIds.getJSONObject(0).get("trainingSessionId"));
			Assert.assertNotNull(trainingSessionIds.getJSONObject(0).get("startTime"));
			Assert.assertNotNull(trainingSessionIds.getJSONObject(0).get("endTime"));
			Assert.assertEquals(4, trainingSessionIds.getJSONObject(0).get("hrz1Time"));
			Assert.assertEquals(42, trainingSessionIds.getJSONObject(0).get("hrz2Time"));
			Assert.assertEquals(34, trainingSessionIds.getJSONObject(0).get("hrz3Time"));
			Assert.assertEquals(10, trainingSessionIds.getJSONObject(0).get("hrz4Time"));
			Assert.assertEquals(10, trainingSessionIds.getJSONObject(0).get("hrz5Time"));
			Assert.assertEquals(6, trainingSessionIds.getJSONObject(0).get("hrz6Time"));
			Assert.assertEquals(1000, trainingSessionIds.getJSONObject(0).get("hrz1Distance"));
			Assert.assertEquals(7420, trainingSessionIds.getJSONObject(0).get("hrz2Distance"));
			Assert.assertEquals(6460, trainingSessionIds.getJSONObject(0).get("hrz3Distance"));
			Assert.assertEquals(2133.33, trainingSessionIds.getJSONObject(0).get("hrz4Distance"));
			Assert.assertEquals(2166.67, trainingSessionIds.getJSONObject(0).get("hrz5Distance"));
			Assert.assertEquals(1410, trainingSessionIds.getJSONObject(0).get("hrz6Distance"));
			Assert.assertEquals(2, trainingSessionIds.getJSONObject(0).get("surfaceIndex"));
			
			Assert.assertEquals(userid, trainingSessionIds.getJSONObject(1).get("userid"));
			Assert.assertEquals("test1", trainingSessionIds.getJSONObject(1).get("trainingSessionId"));
			Assert.assertNotNull(trainingSessionIds.getJSONObject(1).get("startTime"));
			Assert.assertNotNull(trainingSessionIds.getJSONObject(1).get("endTime"));
			Assert.assertEquals(4, trainingSessionIds.getJSONObject(1).get("hrz1Time"));
			Assert.assertEquals(32, trainingSessionIds.getJSONObject(1).get("hrz2Time"));
			Assert.assertEquals(14, trainingSessionIds.getJSONObject(1).get("hrz3Time"));
			Assert.assertEquals(10, trainingSessionIds.getJSONObject(1).get("hrz4Time"));
			Assert.assertEquals(0, trainingSessionIds.getJSONObject(1).get("hrz5Time"));
			Assert.assertEquals(0, trainingSessionIds.getJSONObject(1).get("hrz6Time"));
			Assert.assertEquals(1000, trainingSessionIds.getJSONObject(1).get("hrz1Distance"));
			Assert.assertEquals(5920, trainingSessionIds.getJSONObject(1).get("hrz2Distance"));
			Assert.assertEquals(2753.33, trainingSessionIds.getJSONObject(1).get("hrz3Distance"));
			Assert.assertEquals(2200, trainingSessionIds.getJSONObject(1).get("hrz4Distance"));
			Assert.assertEquals(0, trainingSessionIds.getJSONObject(1).get("hrz5Distance"));
			Assert.assertEquals(0, trainingSessionIds.getJSONObject(1).get("hrz6Distance"));
			Assert.assertEquals(0, trainingSessionIds.getJSONObject(1).get("surfaceIndex"));
						
			/*Validation*/
			
						
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
			
		}
		
		@Test
		public void testSaveHeartrateTest() throws Exception{
			SaveHeartrateTestRequestJson saveHeartrateTestRequestJson = new SaveHeartrateTestRequestJson();
			String responseString = traineeResource.saveHeartrateTest(user.getUserid(), user.getAccessToken(), user.getAccessTokenType(), saveHeartrateTestRequestJson);
			
			JSONObject responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
			Assert.assertEquals(AuthenticationStatus.Status.DENIED.toString(), responseJSON.getString("message"));
			
			/*need user data for authorization*/
			String accessToken 		= "accessToken1";
			String accessTokenType 	= "facebook";
			Calendar cal = Calendar.getInstance();
			/*need user data for authorization*/					
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken(accessToken);
			user.setAccessTokenType(accessTokenType);
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);

			responseString = traineeResource.saveHeartrateTest(userid, accessToken, accessTokenType, saveHeartrateTestRequestJson);
			responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
			Assert.assertEquals(TimeErrors.INVALID_TIMESTAMP.toString(), responseJSON.getString("message"));
			
			/*valid timestamp but invalid heartrate*/
			cal = Calendar.getInstance();
			Timestamp timeOfRecord = new Timestamp(cal.getTimeInMillis());
			saveHeartrateTestRequestJson.setTimeOfRecord(timeOfRecord.toString());
			
			responseString = traineeResource.saveHeartrateTest(userid, accessToken, accessTokenType, saveHeartrateTestRequestJson);
			responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
			Assert.assertEquals(HeartrateTestErrors.INVALID_HEARTRATE.toString(), responseJSON.getString("message"));
			
			
			
			/*valid first resting heartrate*/
			saveHeartrateTestRequestJson.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
			saveHeartrateTestRequestJson.setHeartrate(60.0);			
			responseString = traineeResource.saveHeartrateTest(user.getUserid(), user.getAccessToken(), user.getAccessTokenType(), saveHeartrateTestRequestJson);
			responseJSON = new JSONObject(responseString);
			Assert.assertEquals("200", responseJSON.getString("status"));
			
			/*second resting heartrate invalid in chronology*/
			cal.add(Calendar.HOUR, -1);
			timeOfRecord = new Timestamp(cal.getTimeInMillis());
			saveHeartrateTestRequestJson.setTimeOfRecord(timeOfRecord.toString());
			saveHeartrateTestRequestJson.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
			saveHeartrateTestRequestJson.setHeartrate(60.0);
			
			responseString = traineeResource.saveHeartrateTest(user.getUserid(), user.getAccessToken(), user.getAccessTokenType(), saveHeartrateTestRequestJson);
			responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
			Assert.assertEquals(TimeErrors.INVALID_IN_CHRONOLOGY.toString(), responseJSON.getString("message"));
			
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
		}
		
		@Test 
		public void testGetRestingHeartrateTestsInInterval() throws JSONException {
			
			/*need user data for authorization*/
			String accessToken 		= "accessToken1";
			String accessTokenType 	= "facebook";
			Calendar cal = Calendar.getInstance();
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken(accessToken);
			user.setAccessTokenType(accessTokenType);
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);

			
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			Timestamp startTimeStamp = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.DATE, +5);
			Timestamp endTimeStamp = new Timestamp(cal.getTimeInMillis());
			//System.out.println(startTimeStamp.toString() + "   " + endTimeStamp.toString());					

			String responseString = traineeResource.getRestingHeartrateTestsInInterval(user.getUserid(), startTimeStamp, endTimeStamp, accessToken, accessTokenType);
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(jsonResponse.get("obj").toString());
			JSONArray heartrateTestsJson = dataJson.getJSONArray("heartrateTests");

			Assert.assertEquals(user.getUserid(), dataJson.get("userId"));
//			Assert.assertTrue(heartrateTestsJson.isNull(0));
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean1.setDayOfRecord(1);
			fitnessHeartrateTestBean1.setHeartrate(100.0);
			fitnessHeartrateTestBean1.setHeartrateTestId("user1test1");
			fitnessHeartrateTestBean1.setHeartrateType(0);
			cal.add(Calendar.DATE, -3);
			fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 5 * 3600 * 1000));
			fitnessHeartrateTestBean1.setUserid(user.getUserid());
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean2.setDayOfRecord(2);
			fitnessHeartrateTestBean2.setHeartrate(110.0);
			fitnessHeartrateTestBean2.setHeartrateTestId("user1test2");
			fitnessHeartrateTestBean2.setHeartrateType(0);
			fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 2 * 3600 * 1000));
			fitnessHeartrateTestBean2.setUserid(user.getUserid());
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
			
			responseString = traineeResource.getRestingHeartrateTestsInInterval(user.getUserid(), startTimeStamp, endTimeStamp, accessToken, accessTokenType);
			jsonResponse = new JSONObject(responseString);
			dataJson = new JSONObject(jsonResponse.get("obj").toString());
			heartrateTestsJson = dataJson.getJSONArray("heartrateTests");
			
			Assert.assertEquals(user.getUserid(), dataJson.get("userId"));
			Assert.assertNotNull(heartrateTestsJson.get(0));

			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
		}
		
		@Test 
		public void testGetThresholdHeartrateTestsInInterval() throws JSONException {		
			/*need user data for authorization*/
			String accessToken 		= "accessToken1";
			String accessTokenType 	= "facebook";
			Calendar cal = Calendar.getInstance();
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken(accessToken);
			user.setAccessTokenType(accessTokenType);
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);
			
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			Timestamp startTimeStamp = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.DATE, +5);
			Timestamp endTimeStamp = new Timestamp(cal.getTimeInMillis());
			//System.out.println(startTimeStamp.toString() + "   " + endTimeStamp.toString());

			String responseString = traineeResource.getThresholdHeartrateTestsInInterval(user.getUserid(), startTimeStamp, endTimeStamp, accessToken, accessTokenType);
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(jsonResponse.get("obj").toString());
			JSONArray heartrateTestsJson = dataJson.getJSONArray("heartrateTests");

			Assert.assertEquals(user.getUserid(), dataJson.get("userId"));
			Assert.assertTrue(heartrateTestsJson.isNull(0));
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean1.setDayOfRecord(1);
			fitnessHeartrateTestBean1.setHeartrate(100.0);
			fitnessHeartrateTestBean1.setHeartrateTestId("user1test1");
			fitnessHeartrateTestBean1.setHeartrateType(1);
			cal.add(Calendar.DATE, -3);
			fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 5 * 3600 * 1000));
			fitnessHeartrateTestBean1.setUserid(user.getUserid());
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean2.setDayOfRecord(2);
			fitnessHeartrateTestBean2.setHeartrate(110.0);
			fitnessHeartrateTestBean2.setHeartrateTestId("user1test2");
			fitnessHeartrateTestBean2.setHeartrateType(1);
			fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 2 * 3600 * 1000));
			fitnessHeartrateTestBean2.setUserid(user.getUserid());
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);

			responseString = traineeResource.getThresholdHeartrateTestsInInterval(user.getUserid(), startTimeStamp, endTimeStamp, accessToken, accessTokenType);
			jsonResponse = new JSONObject(responseString);
			dataJson = new JSONObject(jsonResponse.get("obj").toString());
			heartrateTestsJson = dataJson.getJSONArray("heartrateTests");
			
			Assert.assertEquals(user.getUserid(), dataJson.get("userId"));
			Assert.assertNotNull(heartrateTestsJson.get(0));

			fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser(userid);
			
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
		}

		@Test 
		public void testGetMaximalHeartrateTestsInInterval() throws JSONException {
			/*need user data for authorization*/
			String accessToken 		= "accessToken1";
			String accessTokenType 	= "facebook";
			Calendar cal = Calendar.getInstance();
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken(accessToken);
			user.setAccessTokenType(accessTokenType);
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);
			
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			Timestamp startTimeStamp = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.DATE, +5);
			Timestamp endTimeStamp = new Timestamp(cal.getTimeInMillis());
			//System.out.println(startTimeStamp.toString() + "   " + endTimeStamp.toString());

			String responseString = traineeResource.getMaximalHeartrateTestsInInterval(user.getUserid(), startTimeStamp, endTimeStamp, accessToken, accessTokenType);
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(jsonResponse.get("obj").toString());
			JSONArray heartrateTestsJson = dataJson.getJSONArray("heartrateTests");

			Assert.assertEquals(user.getUserid(), dataJson.get("userId"));
			Assert.assertTrue(heartrateTestsJson.isNull(0));
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean1.setDayOfRecord(1);
			fitnessHeartrateTestBean1.setHeartrate(100.0);
			fitnessHeartrateTestBean1.setHeartrateTestId("user1test1");
			fitnessHeartrateTestBean1.setHeartrateType(2);
			cal.add(Calendar.DATE, -3);
			fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 5 * 3600 * 1000));
			fitnessHeartrateTestBean1.setUserid(user.getUserid());
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean2.setDayOfRecord(2);
			fitnessHeartrateTestBean2.setHeartrate(110.0);
			fitnessHeartrateTestBean2.setHeartrateTestId("user1test2");
			fitnessHeartrateTestBean2.setHeartrateType(2);
			fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 2 * 3600 * 1000));
			fitnessHeartrateTestBean2.setUserid(user.getUserid());
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);

			responseString = traineeResource.getMaximalHeartrateTestsInInterval(user.getUserid(), startTimeStamp, endTimeStamp, accessToken, accessTokenType);
			jsonResponse = new JSONObject(responseString);
			dataJson = new JSONObject(jsonResponse.get("obj").toString());
			heartrateTestsJson = dataJson.getJSONArray("heartrateTests");
			
			Assert.assertEquals(user.getUserid(), dataJson.get("userId"));
			Assert.assertNotNull(heartrateTestsJson.get(0));

			fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser(user.getUserid());
			
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
		}

		@Test 
		public void testGetOrthostaticHeartrateTestsInInterval() throws JSONException {
			
			/*need user data for authorization*/
			String accessToken 		= "accessToken1";
			String accessTokenType 	= "facebook";
			Calendar cal = Calendar.getInstance();
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken(accessToken);
			user.setAccessTokenType(accessTokenType);
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);
			
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			Timestamp startTimeStamp = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.DATE, +5);
			Timestamp endTimeStamp = new Timestamp(cal.getTimeInMillis());
			//System.out.println(startTimeStamp.toString() + "   " + endTimeStamp.toString());

			String responseString = traineeResource.getOrthostaticHeartrateTestsInInterval(user.getUserid(), startTimeStamp, endTimeStamp, accessToken, accessTokenType);
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(jsonResponse.get("obj").toString());
			JSONArray heartrateTestsJson = dataJson.getJSONArray("heartrateTests");

			Assert.assertEquals(user.getUserid(), dataJson.get("userId"));
			Assert.assertTrue(heartrateTestsJson.isNull(0));
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean1.setDayOfRecord(1);
			fitnessHeartrateTestBean1.setHeartrate(100.0);
			fitnessHeartrateTestBean1.setHeartrateTestId("user1test1");
			fitnessHeartrateTestBean1.setHeartrateType(3);
			cal.add(Calendar.DATE, -3);
			fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 5 * 3600 * 1000));
			fitnessHeartrateTestBean1.setUserid(user.getUserid());
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean2.setDayOfRecord(2);
			fitnessHeartrateTestBean2.setHeartrate(110.0);
			fitnessHeartrateTestBean2.setHeartrateTestId("user1test2");
			fitnessHeartrateTestBean2.setHeartrateType(3);
			fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 2 * 3600 * 1000));
			fitnessHeartrateTestBean2.setUserid(user.getUserid());
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);

			responseString = traineeResource.getOrthostaticHeartrateTestsInInterval(user.getUserid(), startTimeStamp, endTimeStamp, accessToken, accessTokenType);
			jsonResponse = new JSONObject(responseString);
			dataJson = new JSONObject(jsonResponse.get("obj").toString());
			heartrateTestsJson = dataJson.getJSONArray("heartrateTests");
			
			Assert.assertEquals(user.getUserid(), dataJson.get("userId"));
			Assert.assertNotNull(heartrateTestsJson.get(0));

			fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser(user.getUserid());
			
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
		}
		
		@Test
		public void testGetRecoveryTime() throws JSONException, TrainingSessionException, HomeostasisIndexModelException{
			
			String responseString = traineeResource.getRecoveryTime(userid, null, null);
			JSONObject responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
			
			responseString = traineeResource.getRecoveryTime(null, null, null);
			responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
		
			Calendar cal = Calendar.getInstance();
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken("accessToken1");
			user.setAccessTokenType("facebook");
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);
	
			cal.setTimeInMillis(DateTimeUtils.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, 10);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Long sessionStartTime = cal.getTime().getTime();
			cal.add(Calendar.HOUR, 1);
			Long sessionEndTime = cal.getTime().getTime();
			DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
					
			FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
			fitnessTrainingSessionBean.setUserid(userid);
			fitnessTrainingSessionBean.setStartTime(new Timestamp(sessionStartTime));
			fitnessTrainingSessionBean.setEndTime(new Timestamp(sessionEndTime));
			fitnessTrainingSessionBean.setHrz1Time(4.0);
			fitnessTrainingSessionBean.setHrz2Time(32.0);
			fitnessTrainingSessionBean.setHrz3Time(14.0);
			fitnessTrainingSessionBean.setHrz4Time(10.0);
			fitnessTrainingSessionBean.setHrz5Time(0.0);
			fitnessTrainingSessionBean.setHrz6Time(0.0);
			fitnessTrainingSessionBean.setHrz1Distance(1000.0);
			fitnessTrainingSessionBean.setHrz2Distance(5920.0);
			fitnessTrainingSessionBean.setHrz3Distance(2753.33);
			fitnessTrainingSessionBean.setHrz4Distance(2200.0);
			fitnessTrainingSessionBean.setHrz5Distance(0.0);
			fitnessTrainingSessionBean.setHrz6Distance(0.0);
			fitnessTrainingSessionBean.setSurfaceIndex(0);
			fitnessTrainingSessionBean.setTrainingSessionId("test1");
			
			FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
			fitnessHomeostasisIndexBean.setUserid(userid);
			fitnessHomeostasisIndexBean.setTraineeClassification(3);
			fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(-86.5);
			fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(-86.5);
			
			fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
			fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
			
		    /*Sandra Session 2*/
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 19);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			sessionStartTime = cal.getTime().getTime();
			cal.add(Calendar.MINUTE, 110);
			sessionEndTime = cal.getTime().getTime();
			DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		
			fitnessTrainingSessionBean.setUserid(userid);
			fitnessTrainingSessionBean.setStartTime(new Timestamp(sessionStartTime));
			fitnessTrainingSessionBean.setEndTime(new Timestamp(sessionEndTime));
			fitnessTrainingSessionBean.setHrz1Time(4.0);
			fitnessTrainingSessionBean.setHrz2Time(42.0);
			fitnessTrainingSessionBean.setHrz3Time(34.0);
			fitnessTrainingSessionBean.setHrz4Time(10.0);
			fitnessTrainingSessionBean.setHrz5Time(10.0);
			fitnessTrainingSessionBean.setHrz6Time(6.0);
			fitnessTrainingSessionBean.setHrz1Distance(1000.0);
			fitnessTrainingSessionBean.setHrz2Distance(7420.0);
			fitnessTrainingSessionBean.setHrz3Distance(6460.0);
			fitnessTrainingSessionBean.setHrz4Distance(2133.33);
			fitnessTrainingSessionBean.setHrz5Distance(2166.67);
			fitnessTrainingSessionBean.setHrz6Distance(1410.0);
			fitnessTrainingSessionBean.setSurfaceIndex(2);
			fitnessTrainingSessionBean.setTrainingSessionId("test2");
			
			fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(-235.5);
			fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(-235.5);
			
			fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
			fitnessHomeostasisIndexDAO.updateHomeostasisIndexModel(fitnessHomeostasisIndexBean);
			
			/*Get recovery time response*/
			responseString = traineeResource.getRecoveryTime(userid, "accessToken1", "facebook");
			responseJSON = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(responseJSON.get("obj").toString());
			String responseUserId = dataJson.getString("userId");
			String recentTrainingSessionId = dataJson.getString("recentTrainingSessionId");
			String recoveryTime = dataJson.getString("recoveryTime");
			Double recentMinimumOfHI = dataJson.getDouble("recentMinimumOfHomeostasisIndex");
			Double localRegressionMinimumOfHI = dataJson.getDouble("localRegressionMinimumOfHomeostasisIndex");
			
						
			/*Validation*/
			cal.add(Calendar.DATE, 3);
			cal.set(Calendar.HOUR_OF_DAY, 11);
			cal.set(Calendar.MINUTE, 38);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Assert.assertEquals(userid, responseUserId);
			Assert.assertEquals("test2", recentTrainingSessionId );
			Assert.assertEquals(Timestamp.valueOf(recoveryTime).getTime(), cal.getTime().getTime());
			Assert.assertEquals(-235.5, recentMinimumOfHI);
			Assert.assertEquals(-235.5, localRegressionMinimumOfHI);
						
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
			
		}
		
		@Test
		public void testGetHomeostasisIndex() throws JSONException, HomeostasisIndexModelException, TrainingSessionException{
			String responseString = traineeResource.getHomeostasisIndex(userid, null, null);
			JSONObject responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
			
			responseString = traineeResource.getRecoveryTime(null, null, null);
			responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
		
			Calendar cal = Calendar.getInstance();
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken("accessToken1");
			user.setAccessTokenType("facebook");
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");		
			cal.add(Calendar.YEAR, -25);
			user.setDob(new java.sql.Date(cal.getTimeInMillis()));
			user.setGender(UserManager.GENDER_FEMALE);
			userDao.createUser(user);
	
			cal.setTimeInMillis(DateTimeUtils.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, 10);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		
			FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
			fitnessHomeostasisIndexBean.setUserid(userid);
			fitnessHomeostasisIndexBean.setTraineeClassification(3);
			fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(-86.5);
			fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(-86.5);
			fitnessHomeostasisIndexBean.setRecentTotalLoadOfExercise(86.5);
			fitnessHomeostasisIndexBean.setRecentEndTime(new Timestamp(cal.getTime().getTime()));
			
			
			fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
			
		    /*Sandra Session 2*/
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());

			
			/*Get Homeostasis Index response*/
			responseString = traineeResource.getHomeostasisIndex(userid, "accessToken1", "facebook");
			responseJSON = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(responseJSON.get("obj").toString());
			String responseUserId = dataJson.getString("userid");
			Double homeostasisIndex = dataJson.getDouble("homeostasisIndex");	
						
			/*Validation*/
			Assert.assertEquals(userid, responseUserId);
			Assert.assertEquals(-30.25, homeostasisIndex);

						
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
			DateTimeUtils.setCurrentMillisSystem();
		}
		
		@Test
		public void testGetTraineeIds() throws JSONException, TrainingSessionException, HomeostasisIndexModelException{
			String responseString = traineeResource.getRecoveryTime(userid, null, null);
			JSONObject responseJSON = new JSONObject(responseString);
			Assert.assertEquals("406", responseJSON.getString("status"));
				
			UserBean user = new UserBean();
			user.setUserid(userid);
			user.setAccessToken("accessToken1");
			user.setAccessTokenType("facebook");
			user.setFirstName("Chitra");
			user.setEmail("chitra@acme.com");
			userDao.createUser(user);
			
			user.setUserid("22222bbbbb");
			user.setAccessToken("accessToken2");
			user.setAccessTokenType("facebook");
			user.setFirstName("Suresh");
			user.setEmail("suresh@acme.com");
			userDao.createUser(user);
			
			user.setUserid("33333ccccc");
			user.setAccessToken("accessToken3");
			user.setAccessTokenType("facebook");
			user.setFirstName("Sandra");
			user.setEmail("sandra@acme.com");
			userDao.createUser(user);
			
			responseString = traineeResource.getTraineeIds(userid, "accessToken1", "facebook");
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(jsonResponse.get("obj").toString());
			JSONArray traineeIds = dataJson.getJSONArray("traineeIds");
			
			JSONObject trainee = traineeIds.getJSONObject(0);
			Assert.assertEquals("22222bbbbb", trainee.get("userid"));
			Assert.assertEquals("Suresh", trainee.get("firstName"));
			
			trainee = traineeIds.getJSONObject(1);
			Assert.assertEquals("33333ccccc", trainee.get("userid"));
			Assert.assertEquals("Sandra", trainee.get("firstName"));
			
			trainee = traineeIds.getJSONObject(2);
			Assert.assertEquals(userid, trainee.get("userid"));
			Assert.assertEquals("Chitra", trainee.get("firstName"));
			
			userDao.deleteUser(userid);
			userDao.deleteUser("22222bbbbb");
			userDao.deleteUser("33333ccccc");
			
			
		}
		
		@Test
		public void testSaveFitnessTrainingSessionDebugInfo(){
			List<FitnessTrainingSessionTimePoint> timePoints = new ArrayList<FitnessTrainingSessionTimePoint>();
			FitnessTrainingSessionTimePoint timePoint = new FitnessTrainingSessionTimePoint();			
			Calendar cal = Calendar.getInstance();			
			timePoint.setTime(new Timestamp(cal.getTimeInMillis()));
			timePoint.setHeartrate(new Double(122));
			timePoint.setLongitude(new Double(13.418));
			timePoint.setLatitude(new Double(55.364));
			timePoint.setAltitude(new Double(10.0));
			timePoint.setSpeed(new Double(12.0));
			timePoints.add(timePoint);
			
			timePoint = new FitnessTrainingSessionTimePoint();
			cal.add(Calendar.SECOND, 1);
			timePoint.setTime(new Timestamp(cal.getTimeInMillis()));
			timePoint.setHeartrate(new Double(122));
			timePoint.setLongitude(new Double(13.418));
			timePoint.setLatitude(new Double(55.364));
			timePoint.setAltitude(new Double(10.0));
			timePoint.setSpeed(new Double(12.0));
			timePoints.add(timePoint);
			
			timePoint = new FitnessTrainingSessionTimePoint();
			cal.add(Calendar.SECOND, 1);
			timePoint.setTime(new Timestamp(cal.getTimeInMillis()));
			timePoint.setHeartrate(new Double(122));
			timePoint.setLongitude(new Double(13.418));
			timePoint.setLatitude(new Double(55.364));
			timePoint.setAltitude(new Double(10.0));
			timePoint.setSpeed(new Double(12.0));
			timePoints.add(timePoint);
			SaveFitnessTrainingSessionDebugJson saveFitnessTrainingSessionDebugJson = new SaveFitnessTrainingSessionDebugJson();
			saveFitnessTrainingSessionDebugJson.setTrainingTimePoints(timePoints);
			traineeResource.saveFitnessTrainingSessionDebugInfo("user1", "trainingSession1", null, null, saveFitnessTrainingSessionDebugJson);
		}
		
		@Before
		public void testSetup(){
			
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
		}
		
		@After
		public void testTakeDown(){
			
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
		}
		
		@AfterClass
		public static void suiteTakeDown(){
			
			/*clean up*/
			fitnessManager.clearTraineeData(userid);
			userDao.deleteUser(userid);
		}
}
