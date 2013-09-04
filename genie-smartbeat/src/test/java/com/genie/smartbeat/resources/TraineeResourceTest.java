package com.genie.smartbeat.resources;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessSpeedHeartRateDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.impl.FitnessManagerMySQLImpl;
import com.genie.social.beans.UserBean;
import com.genie.social.core.AuthenticationStatus;
import com.genie.social.dao.UserDao;
import com.genie.social.facebook.GraphAPI;
import com.genie.social.impl.UserManagerMySQLImpl;
import com.genie.social.json.RegisterRequestJSON;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TraineeResourceTest {

		private static ApplicationContext applicationContext;
		private static UserDao userDao;
		private static FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO;
		private static FitnessShapeIndexDAO fitnessShapeIndexDAO;
		private static FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO;
		private static FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
		private static TraineeResource traineeResource;
		private UserManagerMySQLImpl userManager;
		private static FitnessManagerMySQLImpl fitnessManager;
		private static String appID = "333643156765163";
		private long now;
		
		@Before
		public void setupUserDao(){
			applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");

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
			fitnessShapeIndexDAO = (FitnessShapeIndexDAO)applicationContext.getBean("fitnessShapeIndexDAO");
			fitnessSpeedHeartRateDAO = (FitnessSpeedHeartRateDAO)applicationContext.getBean("fitnessSpeedHeartRateDAO");
			fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO)applicationContext.getBean("fitnessTrainingSessionDAO");
			traineeResource = new TraineeResource();
			traineeResource.setFitnessManager((FitnessManagerMySQLImpl) applicationContext.getBean("fitnessManagerMySQLImpl"));
			traineeResource.setUserManager((UserManagerMySQLImpl) applicationContext.getBean("userManagerMySQLImpl"));
			userManager = (UserManagerMySQLImpl) traineeResource.getUserManager();
			fitnessManager = (FitnessManagerMySQLImpl) traineeResource.getFitnessManager();
			
		}
		
		@Test
		public void testRegisterUser() throws JSONException {			
			UserBean testUser = GraphAPI.getTestUser();
			testUser.setFirstName("jack");
			testUser.setLastName("sparrow");
			testUser.setPrivilegeLevel((byte) 1);
			testUser.setGender((byte) 1);
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
			System.out.println(responseString);
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
		
		//@Test
		public void testSaveFitnessTrainingSession() throws Exception{
			
			long now = new Date().getTime();
			long nowBeforeOneHour = now - 3600000;
			long nowBeforeTwentyMinutes = now - 1200000;
			long nowBeforeFortyMinutes = now - 2*1200000;
			
			/*Get App Access Token from facebook*/
		    String getAppAccessTokenUrl = "https://graph.facebook.com/oauth/access_token?client_id="+appID+"&client_secret=bd8fa4961cb1c2a284cbe8486707b73a&grant_type=client_credentials";
			ClientConfig clientConfigGetAppAccessToken = new DefaultClientConfig();
			clientConfigGetAppAccessToken.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientGetAppToken = Client.create(clientConfigGetAppAccessToken);
			WebResource getAppAccessToken = clientGetAppToken.resource(getAppAccessTokenUrl);
			String appAccessTokenResponse = getAppAccessToken.get(String.class);
			String[] appAccessToken = appAccessTokenResponse.split("=");
			String appAccessTokenValue = appAccessToken[1];
				
			/*Get test user from facebook*/
			String getFacebookTestUserUrl = "https://graph.facebook.com/"+appID+"/accounts/test-users?installed=true&permissions=email&method=post&access_token="+URLEncoder.encode(appAccessTokenValue,"ISO-8859-1");
			ClientConfig clientConfigGetFacebookTestUser = new DefaultClientConfig();
			clientConfigGetFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientGetFacebookTestUser = Client.create(clientConfigGetFacebookTestUser);
			WebResource getFacebookTestUser = clientGetFacebookTestUser.resource(getFacebookTestUserUrl);
			JSONObject FacebookTestUser = getFacebookTestUser.type(MediaType.APPLICATION_FORM_URLENCODED).post(JSONObject.class);
					
			/*Parsing User Details*/
			String userID = FacebookTestUser.getString("id");
			String userAccessToken = FacebookTestUser.getString("access_token");
				
			/*Register the User*/
		    String registerUserUrl = "http://localhost:9998/trainee/register";
			JSONObject inputJsonObj = new JSONObject();
			inputJsonObj.put("accessToken", userAccessToken );
			inputJsonObj.put("accessTokenType", "facebook" );
			ClientConfig clientConfigRegisterUser = new DefaultClientConfig();
			clientConfigRegisterUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientRegisterUser = Client.create(clientConfigRegisterUser);
			WebResource registerUser = clientRegisterUser.resource(registerUserUrl);      
			JSONObject registerResJson = registerUser.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,inputJsonObj);
			JSONObject objRegister = registerResJson.getJSONObject("obj");
			Assert.assertEquals("200", registerResJson.getString("status"));
			Assert.assertEquals("OK", registerResJson.getString("message"));
			String genieUserID = objRegister.getString("userid");
			
			/*Save fitness training Session for the user*/
			String saveFitnessTrainingSessionUrl = "http://localhost:9998/trainee/id/"+genieUserID+"/trainingSession/save?accessToken="+userAccessToken+"&accessTokenType=facebook";;
			JSONObject trainingSessionDataJsonObj = new JSONObject();
			trainingSessionDataJsonObj.put("startTime", new Timestamp (nowBeforeOneHour));
			trainingSessionDataJsonObj.put("endTime", new Timestamp (nowBeforeFortyMinutes) );
			trainingSessionDataJsonObj.put("hrz1Time","12.0");
			trainingSessionDataJsonObj.put("hrz2Time","14.0");
			trainingSessionDataJsonObj.put("hrz3Time","8.0");
			trainingSessionDataJsonObj.put("hrz4Time","6.0");
			trainingSessionDataJsonObj.put("hrz5Time","0.0");
			trainingSessionDataJsonObj.put("hrz6Time","0.0");
			trainingSessionDataJsonObj.put("hrz1Distance","1260.3");
			trainingSessionDataJsonObj.put("hrz2Distance","1680.4");
			trainingSessionDataJsonObj.put("hrz3Distance","1120.2");
			trainingSessionDataJsonObj.put("hrz4Distance","990.18");
			trainingSessionDataJsonObj.put("hrz5Distance","0.0");
			trainingSessionDataJsonObj.put("hrz6Distance","0.0");
			ClientConfig clientConfigSaveFitnessTrainingSession = new DefaultClientConfig();
			clientConfigSaveFitnessTrainingSession.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientSaveFitnessTrainingSession = Client.create(clientConfigSaveFitnessTrainingSession);
			WebResource saveFitnessTrainingSession = clientSaveFitnessTrainingSession.resource(saveFitnessTrainingSessionUrl);
			JSONObject saveFitnessTrainingSessionResponse = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj);
			System.out.println(saveFitnessTrainingSessionResponse);
			Assert.assertEquals("200", saveFitnessTrainingSessionResponse.getString("status"));
			Assert.assertEquals("OK", saveFitnessTrainingSessionResponse.getString("message"));
			JSONObject objSaveFitnessTrainingSessionReponse = saveFitnessTrainingSessionResponse.getJSONObject("obj");
			Assert.assertEquals(genieUserID, objSaveFitnessTrainingSessionReponse.getString("userid"));
			Assert.assertEquals(100.0, objSaveFitnessTrainingSessionReponse.getDouble("shapeIndex"));
			String trainingSessionId =  objSaveFitnessTrainingSessionReponse.getString("trainingSessionId");
			
			/*Delete Facebook TestUser*/
			String DeleteFbTestUserUrl = "https://graph.facebook.com/"+userID+"?method=delete&access_token="+userAccessToken;
			ClientConfig clientConfigDeleteFacebookTestUser = new DefaultClientConfig();
			clientConfigDeleteFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientDeleteFacebookTestUser = Client.create(clientConfigDeleteFacebookTestUser);
			WebResource deleteFacebookTestUser = clientDeleteFacebookTestUser.resource(DeleteFbTestUserUrl);
			deleteFacebookTestUser.post();
				
			/*Delete TestUser from Genie*/
			userDao.deleteUser(genieUserID);
			fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(genieUserID);
			fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(genieUserID);
			fitnessSpeedHeartRateDAO.deleteSpeedHeartRateModelByUserid(genieUserID);
			fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
			       
		}
		
		@Test
		public void testGetShapeIndex() throws Exception{
			
			long now = new Date().getTime();
			long nowBeforeOneHour = now - 3600000;
			long nowBeforeTwentyMinutes = now - 1200000;
			
			/*Get App Access Token from facebook*/
		    String getAppAccessTokenUrl = "https://graph.facebook.com/oauth/access_token?client_id="+appID+"&client_secret=bd8fa4961cb1c2a284cbe8486707b73a&grant_type=client_credentials";
			ClientConfig clientConfigGetAppAccessToken = new DefaultClientConfig();
			clientConfigGetAppAccessToken.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientGetAppToken = Client.create(clientConfigGetAppAccessToken);
			WebResource getAppAccessToken = clientGetAppToken.resource(getAppAccessTokenUrl);
			String appAccessTokenResponse = getAppAccessToken.get(String.class);
			String[] appAccessToken = appAccessTokenResponse.split("=");
			String appAccessTokenValue = appAccessToken[1];
				
			/*Get test user from facebook*/
			String getFacebookTestUserUrl = "https://graph.facebook.com/"+appID+"/accounts/test-users?installed=true&permissions=email&method=post&access_token="+URLEncoder.encode(appAccessTokenValue,"ISO-8859-1");
			ClientConfig clientConfigGetFacebookTestUser = new DefaultClientConfig();
			clientConfigGetFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientGetFacebookTestUser = Client.create(clientConfigGetFacebookTestUser);
			WebResource getFacebookTestUser = clientGetFacebookTestUser.resource(getFacebookTestUserUrl);
			JSONObject FacebookTestUser = getFacebookTestUser.type(MediaType.APPLICATION_FORM_URLENCODED).post(JSONObject.class);
					
			/*Parsing User Details*/
			String userID = FacebookTestUser.getString("id");
			String userAccessToken = FacebookTestUser.getString("access_token");
				
			/*Register the User*/
		    String registerUserUrl = "http://localhost:9998/trainee/register";
			JSONObject inputJsonObj = new JSONObject();
			inputJsonObj.put("accessToken", userAccessToken );
			inputJsonObj.put("accessTokenType", "facebook" );
			ClientConfig clientConfigRegisterUser = new DefaultClientConfig();
			clientConfigRegisterUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientRegisterUser = Client.create(clientConfigRegisterUser);
			WebResource registerUser = clientRegisterUser.resource(registerUserUrl);      
			JSONObject registerResJson = registerUser.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,inputJsonObj);
			JSONObject objRegister = registerResJson.getJSONObject("obj");
			Assert.assertEquals("200", registerResJson.getString("status"));
			Assert.assertEquals("OK", registerResJson.getString("message"));
			String genieUserID = objRegister.getString("userid");
			
			/*Save fitness training Session for the user*/
			String saveFitnessTrainingSessionUrl = "http://localhost:9998/trainee/id/"+genieUserID+"/trainingSession/save?accessToken="+userAccessToken+"&accessTokenType=facebook";
			JSONObject trainingSessionDataJsonObj = new JSONObject();
			trainingSessionDataJsonObj.put("startTime", new Timestamp (nowBeforeOneHour));
			trainingSessionDataJsonObj.put("endTime", new Timestamp (nowBeforeTwentyMinutes) );
			trainingSessionDataJsonObj.put("hrz1Time","4.0");
			trainingSessionDataJsonObj.put("hrz2Time","8.0");
			trainingSessionDataJsonObj.put("hrz3Time","11.0");
			trainingSessionDataJsonObj.put("hrz4Time","3.0");
			trainingSessionDataJsonObj.put("hrz5Time","6.0");
			trainingSessionDataJsonObj.put("hrz6Time","8.0");
			trainingSessionDataJsonObj.put("hrz1Distance","421.7304");
			trainingSessionDataJsonObj.put("hrz2Distance","895.1108");
			trainingSessionDataJsonObj.put("hrz3Distance","1342.9544");
			trainingSessionDataJsonObj.put("hrz4Distance","402.9899");
			trainingSessionDataJsonObj.put("hrz5Distance","1408.0273");
			trainingSessionDataJsonObj.put("hrz6Distance","2070.7614");
			ClientConfig clientConfigSaveFitnessTrainingSession = new DefaultClientConfig();
			clientConfigSaveFitnessTrainingSession.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientSaveFitnessTrainingSession = Client.create(clientConfigSaveFitnessTrainingSession);
			WebResource saveFitnessTrainingSession = clientSaveFitnessTrainingSession.resource(saveFitnessTrainingSessionUrl);
			JSONObject saveFitnessTrainingSessionResponse = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj);			
			Assert.assertEquals("200", saveFitnessTrainingSessionResponse.getString("status"));
			Assert.assertEquals("OK", saveFitnessTrainingSessionResponse.getString("message"));
			JSONObject objSaveFitnessTrainingSessionReponse = saveFitnessTrainingSessionResponse.getJSONObject("obj");
			Assert.assertEquals(genieUserID, objSaveFitnessTrainingSessionReponse.getString("userid"));
			Assert.assertEquals(100.0, objSaveFitnessTrainingSessionReponse.getDouble("shapeIndex"));
			String trainingSessionId =  objSaveFitnessTrainingSessionReponse.getString("trainingSessionId");
			
			/*Get Shape Index*/
			String getShapeIndexUrl = "http://localhost:9998/trainee/id/"+genieUserID+"/shapeIndex?accessToken="+userAccessToken+"&accessTokenType=facebook";
			ClientConfig clientConfigGetShapeIndex = new DefaultClientConfig();
			clientConfigGetShapeIndex.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientGetShapeIndex = Client.create(clientConfigGetShapeIndex);
			WebResource getShapeIndex = clientGetShapeIndex.resource(getShapeIndexUrl);
			JSONObject getShapeIndexResponse = getShapeIndex.get(JSONObject.class);
			Assert.assertEquals("200", getShapeIndexResponse.getString("status"));
			Assert.assertEquals("OK", getShapeIndexResponse.getString("message"));
			JSONObject objGetShapeIndexResponse = getShapeIndexResponse.getJSONObject("obj");
			Assert.assertEquals(genieUserID, objGetShapeIndexResponse.getString("userid"));
			Assert.assertEquals(100.0, objGetShapeIndexResponse.getDouble("shapeIndex"));
			
			
			/*Delete Facebook TestUser*/
			String DeleteFbTestUserUrl = "https://graph.facebook.com/"+userID+"?method=delete&access_token="+userAccessToken;
			ClientConfig clientConfigDeleteFacebookTestUser = new DefaultClientConfig();
			clientConfigDeleteFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientDeleteFacebookTestUser = Client.create(clientConfigDeleteFacebookTestUser);
			WebResource deleteFacebookTestUser = clientDeleteFacebookTestUser.resource(DeleteFbTestUserUrl);
			deleteFacebookTestUser.post();
				
			/*Delete TestUser from Genie*/
			/*userDao.deleteUser(genieUserID);
			fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(genieUserID);
			fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(genieUserID);
			fitnessSpeedHeartRateDAO.deleteSpeedHeartRateModelByUserid(genieUserID);
			fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);*/
			
			String DeleteGenieUser = "http://localhost:9998/trainee/id/"+genieUserID+"/data/clear";
			ClientConfig clientConfigDeleteGenieUser = new DefaultClientConfig();
			//clientConfigDeleteGenieUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientDeleteGenieUser = Client.create(clientConfigDeleteGenieUser);
			WebResource deleteGenieUser = clientDeleteGenieUser.resource(DeleteGenieUser);
			deleteGenieUser.delete();
			       
		}
		
		//@Test
		public void testGetHeartrateZones() {
			String userid = "073a9e7d-9cf2-49a0-8926-f27362fd547e";
			String accessToken = "CAACEdEose0cBAPlsgswFasGRgCIKYexcP5NKCKApuumRV0O2NsO1o1AJrcGFZCD08E74dNsyz6q2mYPRGmjHj0By2gSPQTvmvsH9uiwtBoIueRCpL3cwhBm40PsYCEPVZC9rRWmHZCOor3WMZBgrb59jU9qSRBgZD";
			String getHeartrateZonesUrl = "http://localhost:9998/trainee/id/"+userid+"/heartrateZones?accessToken="+accessToken+"&accessTokenType=facebook";
			ClientConfig clientConfigGetShapeIndex = new DefaultClientConfig();
			clientConfigGetShapeIndex.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client clientGetShapeIndex = Client.create(clientConfigGetShapeIndex);
			WebResource getShapeIndex = clientGetShapeIndex.resource(getHeartrateZonesUrl);
			JSONObject getShapeIndexResponse = getShapeIndex.get(JSONObject.class);
			
		}
		
		//@Test
		public void tempTest() throws JSONException{
			String userAccessToken = "CAAEvclnXOesBAH2q1LYuZCcbUsfui60bWyeiZB4KAh38aUqBP0D0M5LmHyC03TiMP7BbNEZA7gtkkaVlxWZC8twZAg9fwUB54zZATTdcczkIuRCpSMGyt5CoioOtN3q5E8z0UzZB24JwZCzaF6bMorEYj5AwuD1DVluppPs5s55WygZDZD";
			String getUserInfoByEmailUrl = "http://localhost:9998/trainee/email/"+"dhasarathy@gmail.com"+"?accessToken="+userAccessToken+"&accessTokenType=facebook";		
			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
			Client client = Client.create(clientConfig);
			WebResource webresource = client.resource(getUserInfoByEmailUrl);
			JSONObject getUserInfoResponse = webresource.get(JSONObject.class);
			System.out.println(getUserInfoResponse);			
		}
		

}
