package com.genie.smartbeat.resources;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.dao.FitnessHeartrateTestDAO;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessSpeedHeartRateDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.impl.FitnessManagerMySQLImpl;
import com.genie.smartbeat.json.SaveFitnessTrainingSessionRequestJson;
import com.genie.social.beans.UserBean;
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
		private static FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO;
		private static FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
		private static TraineeResource traineeResource;
		private static UserManagerMySQLImpl userManager;
		private static FitnessManagerMySQLImpl fitnessManager;
		private static String appID = "333643156765163";
		private static long now;
		private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
		
		@BeforeClass
		public static void setupUserDao(){
			applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
			applicationContext.registerShutdownHook();
			
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
			fitnessSpeedHeartRateDAO = (FitnessSpeedHeartRateDAO)applicationContext.getBean("fitnessSpeedHeartRateDAO");
			fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO)applicationContext.getBean("fitnessTrainingSessionDAO");
			traineeResource = new TraineeResource();
			traineeResource.setFitnessManager((FitnessManagerMySQLImpl) applicationContext.getBean("fitnessManagerMySQLImpl"));
			traineeResource.setUserManager((UserManagerMySQLImpl) applicationContext.getBean("userManagerMySQLImpl"));
			userManager = (UserManagerMySQLImpl) traineeResource.getUserManager();
			fitnessManager = (FitnessManagerMySQLImpl) traineeResource.getFitnessManager();
			
		}
		
		@AfterClass
		public static void tearDown()
		{
			applicationContext.close();
		}
		
		@Test
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
			
			UserBean user = new UserBean();
			Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
			
			java.sql.Date Dob = null;
				
			Dob = new java.sql.Date(now - 12000);
			
			user.setUserid("123456789");		
			user.setAccessToken("access_token_123456789");
			user.setAccessTokenType(UserBean.ACCESS_TOKEN_TYPE_CUSTOM);
			user.setFirstName("Antony");
			user.setMiddleName("Bob");
			user.setLastName("CampBell");
			user.setDob( Dob);
			user.setEmail("abc@xyz.com");		
			user.setImageUrl("www.picasa.com/1002");
			user.setCreatedTs(timestamp);
			user.setLastUpdatedTs(timestamp);
			user.setLastLoginTs(timestamp);
			
			String responseString = null;
			JSONObject jsonResponse;
			
			responseString = traineeResource.getUserInfo(user.getEmail(), user.getAccessToken(), user.getAccessTokenType());
			jsonResponse = new JSONObject(responseString);
			Assert.assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), Integer.parseInt(jsonResponse.get("status").toString()));

			userDao.createUser(user);
			
			responseString = traineeResource.getUserInfo(user.getEmail(), user.getAccessToken(), user.getAccessTokenType());
			jsonResponse = new JSONObject(responseString);
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
			
			String response = traineeResource.saveFitnessTrainingSession(userid, null, null, saveTrainingSessionRequestJson);
			JSONObject responseJSON = new JSONObject(response);
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
			
			response = traineeResource.saveFitnessTrainingSession(userid, null, null, saveTrainingSessionRequestJson);
			//System.out.println(response);
			responseJSON = new JSONObject(response);
			fitnessManager.clearTraineeData(user.getUserid());
			userDao.deleteUser(user.getUserid());
		}
		
		@Test
		public void testGetShapeIndex() throws Exception{
			       
		}
		
		@Test
		public void testGetHeartrateZones() {
			
		}
		
		@Test 
		public void testGetRestingHeartrateTestsInInterval() throws JSONException {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			Timestamp startTimeStamp = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.DATE, +5);
			Timestamp endTimeStamp = new Timestamp(cal.getTimeInMillis());
			//System.out.println(startTimeStamp.toString() + "   " + endTimeStamp.toString());

			String responseString = traineeResource.getRestingHeartrateTestsInInterval("user1", startTimeStamp, endTimeStamp, "accessToken", "accessTokenType");
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(jsonResponse.get("obj").toString());
			JSONArray heartrateTestsJson = dataJson.getJSONArray("heartrateTests");

			Assert.assertEquals("user1", dataJson.get("userId"));
			Assert.assertTrue(heartrateTestsJson.isNull(0));
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean1.setDayOfRecord(1);
			fitnessHeartrateTestBean1.setHeartrate(100.0);
			fitnessHeartrateTestBean1.setHeartrateTestId("user1test1");
			fitnessHeartrateTestBean1.setHeartrateType(0);
			cal.add(Calendar.DATE, -3);
			fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 5 * 3600 * 1000));
			fitnessHeartrateTestBean1.setUserid("user1");
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean2.setDayOfRecord(2);
			fitnessHeartrateTestBean2.setHeartrate(110.0);
			fitnessHeartrateTestBean2.setHeartrateTestId("user1test2");
			fitnessHeartrateTestBean2.setHeartrateType(0);
			fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 2 * 3600 * 1000));
			fitnessHeartrateTestBean2.setUserid("user1");
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
			
			responseString = traineeResource.getRestingHeartrateTestsInInterval("user1", startTimeStamp, endTimeStamp, "accessToken", "accessTokenType");
			jsonResponse = new JSONObject(responseString);
			dataJson = new JSONObject(jsonResponse.get("obj").toString());
			heartrateTestsJson = dataJson.getJSONArray("heartrateTests");
			
			Assert.assertEquals("user1", dataJson.get("userId"));
			Assert.assertNotNull(heartrateTestsJson.get(0));

			fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		}
		
		@Test 
		public void testGetThresholdHeartrateTestsInInterval() throws JSONException {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			Timestamp startTimeStamp = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.DATE, +5);
			Timestamp endTimeStamp = new Timestamp(cal.getTimeInMillis());
			System.out.println(startTimeStamp.toString() + "   " + endTimeStamp.toString());

			String responseString = traineeResource.getThresholdHeartrateTestsInInterval("user1", startTimeStamp, endTimeStamp, "accessToken", "accessTokenType");
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(jsonResponse.get("obj").toString());
			JSONArray heartrateTestsJson = dataJson.getJSONArray("heartrateTests");

			Assert.assertEquals("user1", dataJson.get("userId"));
			Assert.assertTrue(heartrateTestsJson.isNull(0));
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean1.setDayOfRecord(1);
			fitnessHeartrateTestBean1.setHeartrate(100.0);
			fitnessHeartrateTestBean1.setHeartrateTestId("user1test1");
			fitnessHeartrateTestBean1.setHeartrateType(1);
			cal.add(Calendar.DATE, -3);
			fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 5 * 3600 * 1000));
			fitnessHeartrateTestBean1.setUserid("user1");
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean2.setDayOfRecord(2);
			fitnessHeartrateTestBean2.setHeartrate(110.0);
			fitnessHeartrateTestBean2.setHeartrateTestId("user1test2");
			fitnessHeartrateTestBean2.setHeartrateType(1);
			fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 2 * 3600 * 1000));
			fitnessHeartrateTestBean2.setUserid("user1");
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);

			responseString = traineeResource.getThresholdHeartrateTestsInInterval("user1", startTimeStamp, endTimeStamp, "accessToken", "accessTokenType");
			jsonResponse = new JSONObject(responseString);
			dataJson = new JSONObject(jsonResponse.get("obj").toString());
			heartrateTestsJson = dataJson.getJSONArray("heartrateTests");
			
			Assert.assertEquals("user1", dataJson.get("userId"));
			Assert.assertNotNull(heartrateTestsJson.get(0));

			fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		}

		@Test 
		public void testGetMaximalHeartrateTestsInInterval() throws JSONException {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			Timestamp startTimeStamp = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.DATE, +5);
			Timestamp endTimeStamp = new Timestamp(cal.getTimeInMillis());
			System.out.println(startTimeStamp.toString() + "   " + endTimeStamp.toString());

			String responseString = traineeResource.getMaximalHeartrateTestsInInterval("user1", startTimeStamp, endTimeStamp, "accessToken", "accessTokenType");
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(jsonResponse.get("obj").toString());
			JSONArray heartrateTestsJson = dataJson.getJSONArray("heartrateTests");

			Assert.assertEquals("user1", dataJson.get("userId"));
			Assert.assertTrue(heartrateTestsJson.isNull(0));
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean1.setDayOfRecord(1);
			fitnessHeartrateTestBean1.setHeartrate(100.0);
			fitnessHeartrateTestBean1.setHeartrateTestId("user1test1");
			fitnessHeartrateTestBean1.setHeartrateType(2);
			cal.add(Calendar.DATE, -3);
			fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 5 * 3600 * 1000));
			fitnessHeartrateTestBean1.setUserid("user1");
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean2.setDayOfRecord(2);
			fitnessHeartrateTestBean2.setHeartrate(110.0);
			fitnessHeartrateTestBean2.setHeartrateTestId("user1test2");
			fitnessHeartrateTestBean2.setHeartrateType(2);
			fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 2 * 3600 * 1000));
			fitnessHeartrateTestBean2.setUserid("user1");
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);

			responseString = traineeResource.getMaximalHeartrateTestsInInterval("user1", startTimeStamp, endTimeStamp, "accessToken", "accessTokenType");
			jsonResponse = new JSONObject(responseString);
			dataJson = new JSONObject(jsonResponse.get("obj").toString());
			heartrateTestsJson = dataJson.getJSONArray("heartrateTests");
			
			Assert.assertEquals("user1", dataJson.get("userId"));
			Assert.assertNotNull(heartrateTestsJson.get(0));

			fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		}

		@Test 
		public void testGetOrthostaticHeartrateTestsInInterval() throws JSONException {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			Timestamp startTimeStamp = new Timestamp(cal.getTimeInMillis());
			cal.add(Calendar.DATE, +5);
			Timestamp endTimeStamp = new Timestamp(cal.getTimeInMillis());
			System.out.println(startTimeStamp.toString() + "   " + endTimeStamp.toString());

			String responseString = traineeResource.getOrthostaticHeartrateTestsInInterval("user1", startTimeStamp, endTimeStamp, "accessToken", "accessTokenType");
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONObject dataJson = new JSONObject(jsonResponse.get("obj").toString());
			JSONArray heartrateTestsJson = dataJson.getJSONArray("heartrateTests");

			Assert.assertEquals("user1", dataJson.get("userId"));
			Assert.assertTrue(heartrateTestsJson.isNull(0));
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean1.setDayOfRecord(1);
			fitnessHeartrateTestBean1.setHeartrate(100.0);
			fitnessHeartrateTestBean1.setHeartrateTestId("user1test1");
			fitnessHeartrateTestBean1.setHeartrateType(3);
			cal.add(Calendar.DATE, -3);
			fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 5 * 3600 * 1000));
			fitnessHeartrateTestBean1.setUserid("user1");
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
			
			FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
			fitnessHeartrateTestBean2.setDayOfRecord(2);
			fitnessHeartrateTestBean2.setHeartrate(110.0);
			fitnessHeartrateTestBean2.setHeartrateTestId("user1test2");
			fitnessHeartrateTestBean2.setHeartrateType(3);
			fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(cal.getTimeInMillis() - 2 * 3600 * 1000));
			fitnessHeartrateTestBean2.setUserid("user1");
			fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);

			responseString = traineeResource.getOrthostaticHeartrateTestsInInterval("user1", startTimeStamp, endTimeStamp, "accessToken", "accessTokenType");
			jsonResponse = new JSONObject(responseString);
			dataJson = new JSONObject(jsonResponse.get("obj").toString());
			heartrateTestsJson = dataJson.getJSONArray("heartrateTests");
			
			Assert.assertEquals("user1", dataJson.get("userId"));
			Assert.assertNotNull(heartrateTestsJson.get(0));

			fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		}
}
