package com.genie.smartbeat.regression;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.impl.FitnessManagerMySQLImpl;
import com.genie.social.beans.UserBean;
import com.genie.social.core.UserManager;
import com.genie.social.facebook.GraphAPI;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly2.web.GrizzlyWebTestContainerFactory;

public class TraineeChitraRegression extends JerseyTest{
	
	UserBean chithra = new UserBean();
	String chitraFbId = "100006485698211";
	String userId = null;
	String accessToken = null;

	public static final String HOST_LOCALHOST = "localhost";
	public static final String HOST_GENIE_LIVE_VIDHUN = "localhost";
	public static final String HOST = HOST_LOCALHOST;
	public static String PORT;
	public static String BASE_URL;
	@Before
	public void setUpBeforeClass() throws Exception 
	{
		accessToken = GraphAPI.getTestUserAccessToken(chitraFbId);
		chithra.setAccessToken(accessToken);
		chithra.setAccessTokenType("facebook");
		
	}
	
	@Override
	protected AppDescriptor configure() {
		
			return new WebAppDescriptor.Builder("com.genie.smartbeat.mgmt.resources").contextParam("contextConfigLocation", "classpath:META-INF/spring/testApplicationContext.xml").servletClass(SpringServlet.class).contextListenerClass(ContextLoaderListener.class).requestListenerClass(RequestContextListener.class).build();

	}

	@Override
    public TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }
	
	@Test
	public void traineeChitraRegressionTest() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(DateTimeUtils.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, 1);
		
		/*Register the User*/
	    String registerUserUrl = "http://localhost:9998/trainee/register";
		JSONObject inputJsonObj = new JSONObject();
		inputJsonObj.put("accessToken", chithra.getAccessToken());
		inputJsonObj.put("accessTokenType", chithra.getAccessTokenType() );
		Client clientRegisterUser = getClient();
		WebResource registerUser = clientRegisterUser.resource(registerUserUrl);      
		JSONObject registerResJson = registerUser.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,inputJsonObj);
		JSONObject objRegister = registerResJson.getJSONObject("obj");
		Assert.assertEquals("200", registerResJson.getString("status"));
		Assert.assertEquals("OK", registerResJson.getString("message"));
		userId = objRegister.getString("userid");
		chithra.setUserid(userId);
		
		/*Save Heart rate tests for the user for RMT*/
		String saveHeartRateTestUrl = "http://localhost:9998/trainee/id/"+userId+"/heartrateTest/save?accessToken="+accessToken+"&accessTokenType=facebook";
		
		Client clientRestingHeartrateTest =  getClient();
		WebResource saveRestingHeartrateTest =  clientRestingHeartrateTest.resource(saveHeartRateTestUrl);
		JSONObject restingHeartRateTestReqJson0 = new JSONObject();
		restingHeartRateTestReqJson0.put("heartrateType", "0");
		restingHeartRateTestReqJson0.put("heartrate", "60.0");
		restingHeartRateTestReqJson0.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson0);

	    Client clientThresholdHeartrateTest =  getClient();
		WebResource saveThresholdHeartrateTest =  clientThresholdHeartrateTest.resource(saveHeartRateTestUrl);
		JSONObject thresholdtHeartRateTestReqJson0 = new JSONObject();
		thresholdtHeartRateTestReqJson0.put("heartrateType", "1");
		thresholdtHeartRateTestReqJson0.put("heartrate", "147.0");
		thresholdtHeartRateTestReqJson0.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveThresholdHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class, thresholdtHeartRateTestReqJson0);

		Client clientMaximalHeartrateTest =  getClient();
		WebResource saveMaximalHeartrateTest =  clientMaximalHeartrateTest.resource(saveHeartRateTestUrl);
		JSONObject maximaltHeartRateTestReqJson0 = new JSONObject();
		maximaltHeartRateTestReqJson0.put("heartrateType", "2");
		maximaltHeartRateTestReqJson0.put("heartrate", "189.0");
		maximaltHeartRateTestReqJson0.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveMaximalHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class, maximaltHeartRateTestReqJson0);
		
		/*Get Heart Rate Zones for the user*/
		String getHeartRateZoneUrl = "http://localhost:9998/trainee/id/"+userId+"/heartrateZones?accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientGetUserHrz =  getClient();
		WebResource getUserHrz = clientGetUserHrz.resource(getHeartRateZoneUrl);
		JSONObject getHrzResJson = getUserHrz.get(JSONObject.class);
		JSONObject heartrateZones0 = getHrzResJson.getJSONObject("obj");
		assertEquals(userId, heartrateZones0.getString("userid"));
		Assert.assertEquals(60.0,  heartrateZones0.getDouble("heartrateZone1Start"));
		Assert.assertEquals(103.5, heartrateZones0.getDouble("heartrateZone1End"));
		Assert.assertEquals(103.5, heartrateZones0.getDouble("heartrateZone2Start"));
		Assert.assertEquals(125.25, heartrateZones0.getDouble("heartrateZone2End"));
		Assert.assertEquals(125.25, heartrateZones0.getDouble("heartrateZone3Start"));
		Assert.assertEquals(141.84, heartrateZones0.getDouble("heartrateZone3End"));
		Assert.assertEquals(141.84, heartrateZones0.getDouble("heartrateZone4Start"));
		Assert.assertEquals(149.58, heartrateZones0.getDouble("heartrateZone4End"));
		Assert.assertEquals(149.58, heartrateZones0.getDouble("heartrateZone5Start"));
		Assert.assertEquals(181.26, heartrateZones0.getDouble("heartrateZone5End"));
		Assert.assertEquals(181.26, heartrateZones0.getDouble("heartrateZone6Start"));
		Assert.assertEquals(189.0, heartrateZones0.getDouble("heartrateZone6End"));
		
		/*Save Resting and Ortho Static Heart Rate test day 0*/
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);
		JSONObject restingHeartRateTestReqJson1 = new JSONObject();
		restingHeartRateTestReqJson1.put("heartrateType", "0");
		restingHeartRateTestReqJson1.put("heartrate", "54.0");
		restingHeartRateTestReqJson1.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson1);
		
		JSONObject orthoHeartRateTestReqJson0 = new JSONObject();
		orthoHeartRateTestReqJson0.put("heartrateType", "3");
		orthoHeartRateTestReqJson0.put("heartrate", "78.0");
		orthoHeartRateTestReqJson0.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		Client clientOrthoHeartrateTest =  getClient();
		WebResource saveOrthoHeartRateTest =  clientOrthoHeartrateTest.resource(saveHeartRateTestUrl);
    	JSONObject saveHeartrateTestRes0 = saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson0);
    	JSONObject saveHeartrateTestResObj0 = saveHeartrateTestRes0.getJSONObject("obj");
    	JSONObject heartRateZones1 = saveHeartrateTestResObj0.getJSONObject("heartrateZones");
    	Assert.assertEquals(54.0, heartRateZones1.getDouble("heartrateZone1Start"));
		Assert.assertEquals(100.5, heartRateZones1.getDouble("heartrateZone1End"));
		Assert.assertEquals(100.5, heartRateZones1.getDouble("heartrateZone2Start"));
		Assert.assertEquals(123.75, heartRateZones1.getDouble("heartrateZone2End"));
		Assert.assertEquals(123.75, heartRateZones1.getDouble("heartrateZone3Start"));
		Assert.assertEquals(141.6, heartRateZones1.getDouble("heartrateZone3End"));
		Assert.assertEquals(141.6, heartRateZones1.getDouble("heartrateZone4Start"));
		Assert.assertEquals(149.7, heartRateZones1.getDouble("heartrateZone4End"));
		Assert.assertEquals(149.7, heartRateZones1.getDouble("heartrateZone5Start"));
		Assert.assertEquals(180.9, heartRateZones1.getDouble("heartrateZone5End"));
		Assert.assertEquals(180.9, heartRateZones1.getDouble("heartrateZone6Start"));
		Assert.assertEquals(189.0, heartRateZones1.getDouble("heartrateZone6End"));
		
				
		/*Save Training Session for the user in Day 0*/
		String saveFitnessTrainingSessionUrl = "http://localhost:9998/trainee/id/"+userId+"/trainingSession/save?accessToken="+accessToken+"&accessTokenType=facebook";
		JSONObject trainingSessionDataJsonObj0 = new JSONObject();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 0);
		Long sessionStartTime = cal.getTime().getTime();
		cal.add(Calendar.HOUR, 1);
		Long sessionEndTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		
		trainingSessionDataJsonObj0.put("startTime", new Timestamp(sessionStartTime));
		trainingSessionDataJsonObj0.put("endTime", new Timestamp(sessionEndTime) );
		trainingSessionDataJsonObj0.put("hrz1Time","4.0");
		trainingSessionDataJsonObj0.put("hrz2Time","32.0");
		trainingSessionDataJsonObj0.put("hrz3Time","14.0");
		trainingSessionDataJsonObj0.put("hrz4Time","10.0");
		trainingSessionDataJsonObj0.put("hrz5Time","0.0");
		trainingSessionDataJsonObj0.put("hrz6Time","0.0");
		trainingSessionDataJsonObj0.put("hrz1Distance","1000.0");
		trainingSessionDataJsonObj0.put("hrz2Distance","5920.0");
		trainingSessionDataJsonObj0.put("hrz3Distance","2753.33");
		trainingSessionDataJsonObj0.put("hrz4Distance","2200.0");
		trainingSessionDataJsonObj0.put("hrz5Distance","0.0");
		trainingSessionDataJsonObj0.put("hrz6Distance","0.0");
		trainingSessionDataJsonObj0.put("surfaceIndex","0");
		Client clientSaveFitnessTrainingSession =  getClient();
		WebResource saveFitnessTrainingSession = clientSaveFitnessTrainingSession.resource(saveFitnessTrainingSessionUrl);
		JSONObject saveFitnessTrainingSessionRes0 = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj0);
		Assert.assertEquals("200", saveFitnessTrainingSessionRes0.getString("status"));
		Assert.assertEquals("OK", saveFitnessTrainingSessionRes0.getString("message"));
		JSONObject objSaveFitnessTrainingSessionReponse = saveFitnessTrainingSessionRes0.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSessionReponse.getString("userid"));
		Assert.assertEquals(100.0, objSaveFitnessTrainingSessionReponse.getDouble("shapeIndex"));
		
		
		
		/*Get Shape Index one hour after the training session is over*/
		cal.set(Calendar.HOUR_OF_DAY, 12);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		String getShapeIndexUrl = "http://localhost:9998/trainee/id/"+userId+"/shapeIndex?accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientGetShapeIndex =  getClient();
		WebResource getShapeIndex = clientGetShapeIndex.resource(getShapeIndexUrl);	
		JSONObject getShapeIndexObj0 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex0 = getShapeIndexObj0.getJSONObject("obj");
		System.out.println(getShapeIndex0);
		Assert.assertEquals(userId, getShapeIndex0.getString("userid"));
		Assert.assertEquals(100.0, getShapeIndex0.getDouble("shapeIndex"));
		
		
		/*Get Shape Index after supercompensation on day 1*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 15);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject getShapeIndexObj1 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex1 = getShapeIndexObj1.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex1.getString("userid"));
		Assert.assertEquals(100.39, getShapeIndex1.getDouble("shapeIndex"));
		
		/*Get Shape Index just before the next training session*/
		cal.set(Calendar.HOUR_OF_DAY, 19);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject getShapeIndexObj2 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex2 = getShapeIndexObj2.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex2.getString("userid"));
		Assert.assertEquals(100.13, getShapeIndex2.getDouble("shapeIndex"));
		
		/*Save Training Session Data on Day 1*/
		sessionStartTime = cal.getTime().getTime();
		cal.add(Calendar.MINUTE, 110);
		sessionEndTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		JSONObject trainingSessionDataJsonObj1 = new JSONObject();
		trainingSessionDataJsonObj1.put("startTime", new Timestamp(sessionStartTime));
		trainingSessionDataJsonObj1.put("endTime", new Timestamp(sessionEndTime) );
		trainingSessionDataJsonObj1.put("hrz1Time","8.0");
		trainingSessionDataJsonObj1.put("hrz2Time","42.0");
		trainingSessionDataJsonObj1.put("hrz3Time","34.0");
		trainingSessionDataJsonObj1.put("hrz4Time","10.0");
		trainingSessionDataJsonObj1.put("hrz5Time","10.0");
		trainingSessionDataJsonObj1.put("hrz6Time","6.0");
		trainingSessionDataJsonObj1.put("hrz1Distance","1000.0");
		trainingSessionDataJsonObj1.put("hrz2Distance","7420.0");
		trainingSessionDataJsonObj1.put("hrz3Distance","6460.0");
		trainingSessionDataJsonObj1.put("hrz4Distance","2133.33");
		trainingSessionDataJsonObj1.put("hrz5Distance","2166.67");
		trainingSessionDataJsonObj1.put("hrz6Distance","1410.0");
		trainingSessionDataJsonObj1.put("surfaceIndex","2");
		JSONObject saveFitnessTrainingSessionRes1 = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj1);
		Assert.assertEquals("200", saveFitnessTrainingSessionRes1.getString("status"));
		Assert.assertEquals("OK",  saveFitnessTrainingSessionRes1.getString("message"));
		JSONObject objSaveFitnessTrainingSessionRes1 = saveFitnessTrainingSessionRes1.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSessionRes1.getString("userid"));
		Assert.assertEquals(100.13, objSaveFitnessTrainingSessionRes1.getDouble("shapeIndex"));
				
		/*Save Resting and Orthostatic Heart Rate test day 2*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 50);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson2 = new JSONObject();
		restingHeartRateTestReqJson2.put("heartrateType", "0");
		restingHeartRateTestReqJson2.put("heartrate", "55.0");
		restingHeartRateTestReqJson2.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson2);
		
		JSONObject orthoHeartRateTestReqJson1 = new JSONObject();
		orthoHeartRateTestReqJson1.put("heartrateType", "3");
		orthoHeartRateTestReqJson1.put("heartrate", "76.0");
		orthoHeartRateTestReqJson1.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson1);
	
		/*Get Shape Index on day 2*/
		cal.set(Calendar.MINUTE, 55);
		JSONObject getShapeIndexObj3 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex3 = getShapeIndexObj3.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex3.getString("userid"));
		Assert.assertEquals(100.13, getShapeIndex3.getDouble("shapeIndex"));
		
		/*Save Resting and Orthostatic Heart Rate test day 3*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 50);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson3 = new JSONObject();
		restingHeartRateTestReqJson3.put("heartrateType", "0");
		restingHeartRateTestReqJson3.put("heartrate", "58.0");
		restingHeartRateTestReqJson3.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson3);
		
		JSONObject orthoHeartRateTestReqJson2 = new JSONObject();
		orthoHeartRateTestReqJson2.put("heartrateType", "3");
		orthoHeartRateTestReqJson2.put("heartrate", "75.0");
		orthoHeartRateTestReqJson2.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson2);
		
		/*Get Shape Index on day 4*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 15);
		cal.set(Calendar.MINUTE, 00);
		JSONObject getShapeIndexObj4 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex4 = getShapeIndexObj4.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex4.getString("userid"));
		Assert.assertEquals(100.13, getShapeIndex4.getDouble("shapeIndex"));
		
		/*Save Resting and Orthostatic Heart Rate test day 4*/
		cal.set(Calendar.MINUTE, 50);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson4 = new JSONObject();
		restingHeartRateTestReqJson4.put("heartrateType", "0");
		restingHeartRateTestReqJson4.put("heartrate", "55.0");
		restingHeartRateTestReqJson4.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson4);
		
		JSONObject orthoHeartRateTestReqJson3 = new JSONObject();
		orthoHeartRateTestReqJson3.put("heartrateType", "3");
		orthoHeartRateTestReqJson3.put("heartrate", "77.0");
		orthoHeartRateTestReqJson3.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson3);
		
		
		/*Save Training Session Data Day 4*/
		sessionStartTime = cal.getTime().getTime();
		cal.add(Calendar.MINUTE, 40);
		sessionEndTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		JSONObject trainingSessionDataJsonObj4 = new JSONObject();
		trainingSessionDataJsonObj4.put("startTime", new Timestamp(sessionStartTime));
		trainingSessionDataJsonObj4.put("endTime", new Timestamp(sessionEndTime) );
		trainingSessionDataJsonObj4.put("hrz1Time","8.0");
		trainingSessionDataJsonObj4.put("hrz2Time","22.0");
		trainingSessionDataJsonObj4.put("hrz3Time","4.0");
		trainingSessionDataJsonObj4.put("hrz4Time","6.0");
		trainingSessionDataJsonObj4.put("hrz5Time","0.0");
		trainingSessionDataJsonObj4.put("hrz6Time","0.0");
		trainingSessionDataJsonObj4.put("hrz1Distance","1000.0");
		trainingSessionDataJsonObj4.put("hrz2Distance","4106.67");
		trainingSessionDataJsonObj4.put("hrz3Distance","793.33");
		trainingSessionDataJsonObj4.put("hrz4Distance","1300.0");
		trainingSessionDataJsonObj4.put("hrz5Distance","0.0");
		trainingSessionDataJsonObj4.put("hrz6Distance","0.0");
		trainingSessionDataJsonObj4.put("surfaceIndex","0");
		JSONObject saveFitnessTrainingSessionRes4 = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj4);
		Assert.assertEquals("200", saveFitnessTrainingSessionRes4.getString("status"));
		Assert.assertEquals("OK", saveFitnessTrainingSessionRes4.getString("message"));
		JSONObject objSaveFitnessTrainingSessionRes4 = saveFitnessTrainingSessionRes4.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSessionRes4.getString("userid"));
		Assert.assertEquals(101.6, objSaveFitnessTrainingSessionRes4.getDouble("shapeIndex"));
		
		
		/*Save Resting and Orthostatic Heart Rate test day 5*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson5 = new JSONObject();
		restingHeartRateTestReqJson5.put("heartrateType", "0");
		restingHeartRateTestReqJson5.put("heartrate", "57.0");
		restingHeartRateTestReqJson5.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson5);
		
		JSONObject orthoHeartRateTestReqJson4 = new JSONObject();
		orthoHeartRateTestReqJson4.put("heartrateType", "3");
		orthoHeartRateTestReqJson4.put("heartrate", "73.0");
		orthoHeartRateTestReqJson4.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson4);
		
	    /*Save Training Session Data on day 5*/	    
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 0);
		sessionStartTime = cal.getTime().getTime();
		cal.add(Calendar.MINUTE, 65);
		sessionEndTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		JSONObject trainingSessionDataJsonObj5 = new JSONObject();
		trainingSessionDataJsonObj5.put("startTime", new Timestamp(sessionStartTime));
		trainingSessionDataJsonObj5.put("endTime", new Timestamp(sessionEndTime) );
		trainingSessionDataJsonObj5.put("hrz1Time","2.0");
		trainingSessionDataJsonObj5.put("hrz2Time","32.0");
		trainingSessionDataJsonObj5.put("hrz3Time","8.0");
		trainingSessionDataJsonObj5.put("hrz4Time","23.0");
		trainingSessionDataJsonObj5.put("hrz5Time","0.0");
		trainingSessionDataJsonObj5.put("hrz6Time","0.0");
		trainingSessionDataJsonObj5.put("hrz1Distance","1000.0");
		trainingSessionDataJsonObj5.put("hrz2Distance","5493.33");
		trainingSessionDataJsonObj5.put("hrz3Distance","1466.67");
		trainingSessionDataJsonObj5.put("hrz4Distance","4638.33");
		trainingSessionDataJsonObj5.put("hrz5Distance","0.0");
		trainingSessionDataJsonObj5.put("hrz6Distance","0.0");
		trainingSessionDataJsonObj5.put("surfaceIndex","3");
		JSONObject saveFitnessTrainingSessionRes5 = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj5);
		Assert.assertEquals("200", saveFitnessTrainingSessionRes5.getString("status"));
		Assert.assertEquals("OK", saveFitnessTrainingSessionRes5.getString("message"));
		JSONObject objSaveFitnessTrainingSessionRes5 = saveFitnessTrainingSessionRes5.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSessionRes5.getString("userid"));
		Assert.assertEquals(98.21, objSaveFitnessTrainingSessionRes5.getDouble("shapeIndex"));
		
		/*Save Resting and Orthostatic Heart Rate test day 6*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson6 = new JSONObject();
		restingHeartRateTestReqJson6.put("heartrateType", "0");
		restingHeartRateTestReqJson6.put("heartrate", "57.0");
		restingHeartRateTestReqJson6.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson6);
		
		JSONObject orthoHeartRateTestReqJson5 = new JSONObject();
		orthoHeartRateTestReqJson5.put("heartrateType", "3");
		orthoHeartRateTestReqJson5.put("heartrate", "73.0");
		orthoHeartRateTestReqJson5.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson5);
		
	    /*Get Shape Index on day 6*/
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 45);
		JSONObject getShapeIndexObj5 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex5 = getShapeIndexObj5.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex5.getString("userid"));
		Assert.assertEquals(98.61, getShapeIndex5.getDouble("shapeIndex"));
	    
		/*Save Resting and Orthostatic Heart Rate test day 7*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson7 = new JSONObject();
		restingHeartRateTestReqJson7.put("heartrateType", "0");
		restingHeartRateTestReqJson7.put("heartrate", "57.0");
		restingHeartRateTestReqJson7.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson7);
		
		JSONObject orthoHeartRateTestReqJson6 = new JSONObject();
		orthoHeartRateTestReqJson6.put("heartrateType", "3");
		orthoHeartRateTestReqJson6.put("heartrate", "73.0");
		orthoHeartRateTestReqJson6.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson6);
	    
		/*Save Resting and Orthostatic Heart Rate test day 8*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson8 = new JSONObject();
		restingHeartRateTestReqJson8.put("heartrateType", "0");
		restingHeartRateTestReqJson8.put("heartrate", "57.0");
		restingHeartRateTestReqJson8.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson8);
		
		JSONObject orthoHeartRateTestReqJson7 = new JSONObject();
		orthoHeartRateTestReqJson7.put("heartrateType", "3");
		orthoHeartRateTestReqJson7.put("heartrate", "73.0");
		orthoHeartRateTestReqJson7.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson7);
	    
	    /*Save Resting and Orthostatic Heart Rate test day 9*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson9 = new JSONObject();
		restingHeartRateTestReqJson9.put("heartrateType", "0");
		restingHeartRateTestReqJson9.put("heartrate", "57.0");
		restingHeartRateTestReqJson9.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson9);
		
		JSONObject orthoHeartRateTestReqJson8 = new JSONObject();
		orthoHeartRateTestReqJson8.put("heartrateType", "3");
		orthoHeartRateTestReqJson8.put("heartrate", "73.0");
		orthoHeartRateTestReqJson8.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson8);
	    
	    /*Get Shape Index on day 9*/
	    JSONObject getShapeIndexObj6 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex6 = getShapeIndexObj6.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex6.getString("userid"));
		Assert.assertEquals(99.21, getShapeIndex6.getDouble("shapeIndex"));
	    
		/*Delete Trainee Data*/	
		String clearUserDataUrl =  "http://localhost:9998/trainee/id/"+userId+"/data/clear"; 
		ClientConfig clientConfig = new DefaultClientConfig();
		Client clientClearUserData = Client.create(clientConfig);
		WebResource clearUserData = clientClearUserData.resource(clearUserDataUrl);
		clearUserData.delete();
		
	}
	
	private Client getClient(){
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client client = Client.create(clientConfig);	
		return client;
	}

}
