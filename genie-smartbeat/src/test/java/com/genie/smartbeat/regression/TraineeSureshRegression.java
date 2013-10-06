package com.genie.smartbeat.regression;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import com.genie.social.beans.UserBean;
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

public class TraineeSureshRegression extends JerseyTest{

	UserBean suresh = new UserBean();
	String sureshFbId = "100006828780611";
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
		accessToken = GraphAPI.getTestUserAccessToken(sureshFbId);
		System.out.println(accessToken);
		suresh.setAccessToken(accessToken);
		suresh.setAccessTokenType("facebook");
		
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
	public void traineeSureshRegressionTest() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(DateTimeUtils.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, 1);
		
		/*Register the User*/
	    String registerUserUrl = "http://localhost:9998/trainee/register";
		JSONObject inputJsonObj = new JSONObject();
		inputJsonObj.put("accessToken", suresh.getAccessToken());
		inputJsonObj.put("accessTokenType", suresh.getAccessTokenType() );
		Client clientRegisterUser = getClient();
		WebResource registerUser = clientRegisterUser.resource(registerUserUrl);      
		JSONObject registerResJson = registerUser.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,inputJsonObj);
		System.out.println(registerResJson);
		JSONObject objRegister = registerResJson.getJSONObject("obj");
		Assert.assertEquals("200", registerResJson.getString("status"));
		Assert.assertEquals("OK", registerResJson.getString("message"));
		userId = objRegister.getString("userid");
		suresh.setUserid(userId);
		
		
		/*Get Heart Rate Zones for the user*/
		String getHeartRateZoneUrl = "http://localhost:9998/trainee/id/"+userId+"/heartrateZones?accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientGetUserHrz =  getClient();
		WebResource getUserHrz = clientGetUserHrz.resource(getHeartRateZoneUrl);
		JSONObject getHrzResJson = getUserHrz.get(JSONObject.class);
		JSONObject heartrateZones0 = getHrzResJson.getJSONObject("obj");
		assertEquals(userId, heartrateZones0.getString("userid"));
		Assert.assertEquals(72.0,  heartrateZones0.getDouble("heartrateZone1Start"));
		Assert.assertEquals(87.52, heartrateZones0.getDouble("heartrateZone1End"));
		Assert.assertEquals(87.52, heartrateZones0.getDouble("heartrateZone2Start"));
		Assert.assertEquals(95.28, heartrateZones0.getDouble("heartrateZone2End"));
		Assert.assertEquals(95.28, heartrateZones0.getDouble("heartrateZone3Start"));
		Assert.assertEquals(99.06, heartrateZones0.getDouble("heartrateZone3End"));
		Assert.assertEquals(99.06, heartrateZones0.getDouble("heartrateZone4Start"));
		Assert.assertEquals(105.04, heartrateZones0.getDouble("heartrateZone4End"));
		Assert.assertEquals(105.04, heartrateZones0.getDouble("heartrateZone5Start"));
		Assert.assertEquals(165.76, heartrateZones0.getDouble("heartrateZone5End"));
		Assert.assertEquals(165.76, heartrateZones0.getDouble("heartrateZone6Start"));
		Assert.assertEquals(171.75, heartrateZones0.getDouble("heartrateZone6End"));
		
		/*Save Resting and Ortho Static Heart Rate test day 0*/
		String saveHeartRateTestUrl = "http://localhost:9998/trainee/id/"+userId+"/heartrateTest/save?accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientRestingHeartrateTest =  getClient();
		WebResource saveRestingHeartrateTest =  clientRestingHeartrateTest.resource(saveHeartRateTestUrl);
		cal.set(Calendar.HOUR_OF_DAY,11);
		cal.set(Calendar.MINUTE, 0);
		JSONObject restingHeartRateTestReqJson0 = new JSONObject();
		restingHeartRateTestReqJson0.put("heartrateType", "0");
		restingHeartRateTestReqJson0.put("heartrate", "66.0");
		restingHeartRateTestReqJson0.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson0);
		
		JSONObject orthoHeartRateTestReqJson0 = new JSONObject();
		orthoHeartRateTestReqJson0.put("heartrateType", "3");
		orthoHeartRateTestReqJson0.put("heartrate", "72.0");
		orthoHeartRateTestReqJson0.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		Client clientOrthoHeartrateTest =  getClient();
		WebResource saveOrthoHeartRateTest =  clientOrthoHeartrateTest.resource(saveHeartRateTestUrl);
    	JSONObject saveHeartrateTestRes0 = saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson0);
    	JSONObject saveHeartrateTestResObj0 = saveHeartrateTestRes0.getJSONObject("obj");
    	JSONObject heartRateZones1 = saveHeartrateTestResObj0.getJSONObject("heartrateZones");
    	Assert.assertEquals(66.0, heartRateZones1.getDouble("heartrateZone1Start"));
		Assert.assertEquals(84.52, heartRateZones1.getDouble("heartrateZone1End"));
		Assert.assertEquals(84.52, heartRateZones1.getDouble("heartrateZone2Start"));
		Assert.assertEquals(93.78, heartRateZones1.getDouble("heartrateZone2End"));
		Assert.assertEquals(93.78, heartRateZones1.getDouble("heartrateZone3Start"));
		Assert.assertEquals(98.82, heartRateZones1.getDouble("heartrateZone3End"));
		Assert.assertEquals(98.82, heartRateZones1.getDouble("heartrateZone4Start"));
		Assert.assertEquals(105.16, heartRateZones1.getDouble("heartrateZone4End"));
		Assert.assertEquals(105.16, heartRateZones1.getDouble("heartrateZone5Start"));
		Assert.assertEquals(165.4, heartRateZones1.getDouble("heartrateZone5End"));
		Assert.assertEquals(165.4, heartRateZones1.getDouble("heartrateZone6Start"));
		Assert.assertEquals(171.75, heartRateZones1.getDouble("heartrateZone6End"));
		
				
		/*Save Training Session for the user in Day 0*/
		String saveFitnessTrainingSessionUrl = "http://localhost:9998/trainee/id/"+userId+"/trainingSession/save?accessToken="+accessToken+"&accessTokenType=facebook";
		JSONObject trainingSessionDataJsonObj0 = new JSONObject();
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		Long sessionStartTime = cal.getTime().getTime();
		cal.add(Calendar.MINUTE, 29);
		Long sessionEndTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		
		trainingSessionDataJsonObj0.put("startTime", new Timestamp(sessionStartTime));
		trainingSessionDataJsonObj0.put("endTime", new Timestamp(sessionEndTime) );
		trainingSessionDataJsonObj0.put("hrz1Time","0.0");
		trainingSessionDataJsonObj0.put("hrz2Time","3.0");
		trainingSessionDataJsonObj0.put("hrz3Time","14.0");
		trainingSessionDataJsonObj0.put("hrz4Time","10.0");
		trainingSessionDataJsonObj0.put("hrz5Time","2.0");
		trainingSessionDataJsonObj0.put("hrz6Time","0.0");
		trainingSessionDataJsonObj0.put("hrz1Distance","0.0");
		trainingSessionDataJsonObj0.put("hrz2Distance","365.0");
		trainingSessionDataJsonObj0.put("hrz3Distance","1960.0");
		trainingSessionDataJsonObj0.put("hrz4Distance","1500.0");
		trainingSessionDataJsonObj0.put("hrz5Distance","316.67");
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
		Assert.assertEquals(67.5, objSaveFitnessTrainingSessionReponse.getDouble("recentTotalLoadOfExercise"));
		Assert.assertEquals(27.75, objSaveFitnessTrainingSessionReponse.getDouble("vDot"));

		
		
		
		/*Get Shape Index three hours after the training session is over*/
		cal.set(Calendar.HOUR_OF_DAY, 15);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		String getShapeIndexUrl = "http://localhost:9998/trainee/id/"+userId+"/shapeIndex?accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientGetShapeIndex =  getClient();
		WebResource getShapeIndex = clientGetShapeIndex.resource(getShapeIndexUrl);	
		JSONObject getShapeIndexObj0 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex0 = getShapeIndexObj0.getJSONObject("obj");
		System.out.println(getShapeIndex0);
		Assert.assertEquals(userId, getShapeIndex0.getString("userid"));
		Assert.assertEquals(100.0, getShapeIndex0.getDouble("shapeIndex"));
		
		/*Save Second Resting and Ortho Static Heart Rate test day 0*/
		cal.set(Calendar.HOUR_OF_DAY,19);
		cal.set(Calendar.MINUTE, 0);
		JSONObject restingHeartRateTestReqJson01 = new JSONObject();
		restingHeartRateTestReqJson01.put("heartrateType", "0");
		restingHeartRateTestReqJson01.put("heartrate", "66.0");
		restingHeartRateTestReqJson01.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson01);
		
		JSONObject orthoHeartRateTestReqJson01 = new JSONObject();
		orthoHeartRateTestReqJson01.put("heartrateType", "3");
		orthoHeartRateTestReqJson01.put("heartrate", "72.0");
		orthoHeartRateTestReqJson01.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
    	saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson01);
		
		/*Get Shape Index after supercompensation on day 1*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 28);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject getShapeIndexObj1 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex1 = getShapeIndexObj1.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex1.getString("userid"));
		Assert.assertEquals(100.69, getShapeIndex1.getDouble("shapeIndex"));
		
		/*Get Shape Index on Day 2*/
		cal.add(Calendar.DATE,1);
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject getShapeIndexObj2 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex2 = getShapeIndexObj2.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex2.getString("userid"));
		Assert.assertEquals(100.43, getShapeIndex2.getDouble("shapeIndex"));
		
		/*Save Training Session Data on Day 2*/
		cal.set(Calendar.HOUR_OF_DAY, 17);
		sessionStartTime = cal.getTime().getTime();
		cal.add(Calendar.MINUTE, 42);
		sessionEndTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		JSONObject trainingSessionDataJsonObj1 = new JSONObject();
		trainingSessionDataJsonObj1.put("startTime", new Timestamp(sessionStartTime));
		trainingSessionDataJsonObj1.put("endTime", new Timestamp(sessionEndTime) );
		trainingSessionDataJsonObj1.put("hrz1Time","2.0");
		trainingSessionDataJsonObj1.put("hrz2Time","12.0");
		trainingSessionDataJsonObj1.put("hrz3Time","14.0");
		trainingSessionDataJsonObj1.put("hrz4Time","8.0");
		trainingSessionDataJsonObj1.put("hrz5Time","4.0");
		trainingSessionDataJsonObj1.put("hrz6Time","2.0");
		trainingSessionDataJsonObj1.put("hrz1Distance","100.0");
		trainingSessionDataJsonObj1.put("hrz2Distance","1500.0");
		trainingSessionDataJsonObj1.put("hrz3Distance","2006.67");
		trainingSessionDataJsonObj1.put("hrz4Distance","1213.33");
		trainingSessionDataJsonObj1.put("hrz5Distance","646.67");
		trainingSessionDataJsonObj1.put("hrz6Distance","343.33");
		trainingSessionDataJsonObj1.put("surfaceIndex","2");
		JSONObject saveFitnessTrainingSessionRes1 = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj1);
		Assert.assertEquals("200", saveFitnessTrainingSessionRes1.getString("status"));
		Assert.assertEquals("OK",  saveFitnessTrainingSessionRes1.getString("message"));
		JSONObject objSaveFitnessTrainingSessionRes1 = saveFitnessTrainingSessionRes1.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSessionRes1.getString("userid"));
		Assert.assertEquals(100.42, objSaveFitnessTrainingSessionRes1.getDouble("shapeIndex"));
		Assert.assertEquals(98.5, objSaveFitnessTrainingSessionRes1.getDouble("recentTotalLoadOfExercise"));
		Assert.assertEquals(30.86, objSaveFitnessTrainingSessionRes1.getDouble("vDot"));
		
		/*Save Resting and Orthostatic Heart Rate test day 2*/
		cal.set(Calendar.HOUR_OF_DAY, 21);
		cal.set(Calendar.MINUTE, 50);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson2 = new JSONObject();
		restingHeartRateTestReqJson2.put("heartrateType", "0");
		restingHeartRateTestReqJson2.put("heartrate", "68.0");
		restingHeartRateTestReqJson2.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson2);
		
		JSONObject orthoHeartRateTestReqJson2 = new JSONObject();
		orthoHeartRateTestReqJson2.put("heartrateType", "3");
		orthoHeartRateTestReqJson2.put("heartrate", "100.0");
		orthoHeartRateTestReqJson2.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson2);
	
		/*Save Resting and Orthostatic Heart Rate test day 3*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 50);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson3 = new JSONObject();
		restingHeartRateTestReqJson3.put("heartrateType", "0");
		restingHeartRateTestReqJson3.put("heartrate", "62.0");
		restingHeartRateTestReqJson3.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson3);
		
		JSONObject orthoHeartRateTestReqJson3 = new JSONObject();
		orthoHeartRateTestReqJson3.put("heartrateType", "3");
		orthoHeartRateTestReqJson3.put("heartrate", "97.0");
		orthoHeartRateTestReqJson3.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		JSONObject saveHeartrateTestRes3 = saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson3);
		JSONObject saveHeartrateTestResObj3 = saveHeartrateTestRes3.getJSONObject("obj");
    	JSONObject heartRateZones3 = saveHeartrateTestResObj3.getJSONObject("heartrateZones");
    	Assert.assertEquals(62.0, heartRateZones3.getDouble("heartrateZone1Start"));
		Assert.assertEquals(82.52, heartRateZones3.getDouble("heartrateZone1End"));
		Assert.assertEquals(82.52, heartRateZones3.getDouble("heartrateZone2Start"));
		Assert.assertEquals(92.78, heartRateZones3.getDouble("heartrateZone2End"));
		Assert.assertEquals(92.78, heartRateZones3.getDouble("heartrateZone3Start"));
		Assert.assertEquals(98.66, heartRateZones3.getDouble("heartrateZone3End"));
		Assert.assertEquals(98.66, heartRateZones3.getDouble("heartrateZone4Start"));
		Assert.assertEquals(105.24, heartRateZones3.getDouble("heartrateZone4End"));
		Assert.assertEquals(105.24, heartRateZones3.getDouble("heartrateZone5Start"));
		Assert.assertEquals(165.16, heartRateZones3.getDouble("heartrateZone5End"));
		Assert.assertEquals(165.16, heartRateZones3.getDouble("heartrateZone6Start"));
		Assert.assertEquals(171.75, heartRateZones3.getDouble("heartrateZone6End"));
		
		/*Get Shape Index on day 3*/	
		cal.set(Calendar.HOUR_OF_DAY, 17);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject getShapeIndexObj3 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex3 = getShapeIndexObj3.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex3.getString("userid"));
		Assert.assertEquals(100.42, getShapeIndex3.getDouble("shapeIndex"));
		
		
		/*Get Shape Index on day 4*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 13);
		cal.set(Calendar.MINUTE, 06);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject getShapeIndexObj4 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex4 = getShapeIndexObj4.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex4.getString("userid"));
		Assert.assertEquals(101.18, getShapeIndex4.getDouble("shapeIndex"));
		
		/*Save Resting and Orthostatic Heart Rate test day 4*/
		cal.set(Calendar.HOUR, 19);
		cal.set(Calendar.MINUTE, 50);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson4 = new JSONObject();
		restingHeartRateTestReqJson4.put("heartrateType", "0");
		restingHeartRateTestReqJson4.put("heartrate", "64.0");
		restingHeartRateTestReqJson4.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson4);
		
		JSONObject orthoHeartRateTestReqJson4 = new JSONObject();
		orthoHeartRateTestReqJson4.put("heartrateType", "3");
		orthoHeartRateTestReqJson4.put("heartrate", "102.0");
		orthoHeartRateTestReqJson4.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson4);
		
	    /*Save Resting and Orthostatic Heart Rate test day 5*/
	    cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 21);
		cal.set(Calendar.MINUTE, 00);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson5 = new JSONObject();
		restingHeartRateTestReqJson5.put("heartrateType", "0");
		restingHeartRateTestReqJson5.put("heartrate", "67.0");
		restingHeartRateTestReqJson5.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson5);
		
		JSONObject orthoHeartRateTestReqJson5 = new JSONObject();
		orthoHeartRateTestReqJson5.put("heartrateType", "3");
		orthoHeartRateTestReqJson5.put("heartrate", "96.0");
		orthoHeartRateTestReqJson5.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson5);
		
		/*Save Training Session Data Day 6*/
	    //cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 19);
		cal.set(Calendar.MINUTE, 0);
		sessionStartTime = cal.getTime().getTime();
		cal.add(Calendar.MINUTE, 60);
		sessionEndTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		JSONObject trainingSessionDataJsonObj6 = new JSONObject();
		trainingSessionDataJsonObj6.put("startTime", new Timestamp(sessionStartTime));
		trainingSessionDataJsonObj6.put("endTime", new Timestamp(sessionEndTime) );
		trainingSessionDataJsonObj6.put("hrz1Time","8.0");
		trainingSessionDataJsonObj6.put("hrz2Time","12.0");
		trainingSessionDataJsonObj6.put("hrz3Time","34.0");
		trainingSessionDataJsonObj6.put("hrz4Time","6.0");
		trainingSessionDataJsonObj6.put("hrz5Time","0.0");
		trainingSessionDataJsonObj6.put("hrz6Time","0.0");
		trainingSessionDataJsonObj6.put("hrz1Distance","1000.0");
		trainingSessionDataJsonObj6.put("hrz2Distance","1400.0");
		trainingSessionDataJsonObj6.put("hrz3Distance","4590.0");
		trainingSessionDataJsonObj6.put("hrz4Distance","900.0");
		trainingSessionDataJsonObj6.put("hrz5Distance","0.0");
		trainingSessionDataJsonObj6.put("hrz6Distance","0.0");
		trainingSessionDataJsonObj6.put("surfaceIndex","0");
		JSONObject saveFitnessTrainingSessionRes6 = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj6);
		Assert.assertEquals("200", saveFitnessTrainingSessionRes6.getString("status"));
		Assert.assertEquals("OK", saveFitnessTrainingSessionRes6.getString("message"));
		JSONObject objSaveFitnessTrainingSessionRes6 = saveFitnessTrainingSessionRes6.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSessionRes6.getString("userid"));
		Assert.assertEquals(100.64, objSaveFitnessTrainingSessionRes6.getDouble("shapeIndex"));
		Assert.assertEquals(89.5, objSaveFitnessTrainingSessionRes6.getDouble("recentTotalLoadOfExercise"));
		Assert.assertEquals(26.61, objSaveFitnessTrainingSessionRes6.getDouble("vDot"));
		
		/*Save Resting and Orthostatic Heart Rate test day 6*/
		cal.set(Calendar.HOUR_OF_DAY, 21);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson6 = new JSONObject();
		restingHeartRateTestReqJson6.put("heartrateType", "0");
		restingHeartRateTestReqJson6.put("heartrate", "68.0");
		restingHeartRateTestReqJson6.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson6);
		
		JSONObject orthoHeartRateTestReqJson6 = new JSONObject();
		orthoHeartRateTestReqJson6.put("heartrateType", "3");
		orthoHeartRateTestReqJson6.put("heartrate", "98.0");
		orthoHeartRateTestReqJson6.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson6);
		
	    /*Save Threshold Test and Training Session  on day 7*/	    
	    cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 0);
		sessionStartTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionStartTime);
		
		Client clientThresholdHeartrateTest =  getClient();
		WebResource saveThresholdHeartrateTest =  clientThresholdHeartrateTest.resource(saveHeartRateTestUrl);
		JSONObject thresholdtHeartRateTestReqJson7 = new JSONObject();
		thresholdtHeartRateTestReqJson7.put("heartrateType", "1");
		thresholdtHeartRateTestReqJson7.put("heartrate", "132.0");
		thresholdtHeartRateTestReqJson7.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		JSONObject saveHeartrateTestRes7 = saveThresholdHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class, thresholdtHeartRateTestReqJson7);
		JSONObject saveHeartrateTestResObj7 = saveHeartrateTestRes7.getJSONObject("obj");
    	JSONObject heartRateZones7 = saveHeartrateTestResObj7.getJSONObject("heartrateZones");
    	Assert.assertEquals(62.0, heartRateZones7.getDouble("heartrateZone1Start"));
		Assert.assertEquals(97.0, heartRateZones7.getDouble("heartrateZone1End"));
		Assert.assertEquals(97.0, heartRateZones7.getDouble("heartrateZone2Start"));
		Assert.assertEquals(114.5, heartRateZones7.getDouble("heartrateZone2End"));
		Assert.assertEquals(114.5, heartRateZones7.getDouble("heartrateZone3Start"));
		Assert.assertEquals(127.61, heartRateZones7.getDouble("heartrateZone3End"));
		Assert.assertEquals(127.61, heartRateZones7.getDouble("heartrateZone4Start"));
		Assert.assertEquals(134.2, heartRateZones7.getDouble("heartrateZone4End"));
		Assert.assertEquals(134.2, heartRateZones7.getDouble("heartrateZone5Start"));
		Assert.assertEquals(165.16, heartRateZones7.getDouble("heartrateZone5End"));
		Assert.assertEquals(165.16, heartRateZones7.getDouble("heartrateZone6Start"));
		Assert.assertEquals(171.75, heartRateZones7.getDouble("heartrateZone6End"));
		
		cal.add(Calendar.MINUTE, 53);
		sessionEndTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		JSONObject trainingSessionDataJsonObj7 = new JSONObject();
		trainingSessionDataJsonObj7.put("startTime", new Timestamp(sessionStartTime));
		trainingSessionDataJsonObj7.put("endTime", new Timestamp(sessionEndTime) );
		trainingSessionDataJsonObj7.put("hrz1Time","5.0");
		trainingSessionDataJsonObj7.put("hrz2Time","5.0");
		trainingSessionDataJsonObj7.put("hrz3Time","8.0");
		trainingSessionDataJsonObj7.put("hrz4Time","30.0");
		trainingSessionDataJsonObj7.put("hrz5Time","5.0");
		trainingSessionDataJsonObj7.put("hrz6Time","0.0");
		trainingSessionDataJsonObj7.put("hrz1Distance","1000.0");
		trainingSessionDataJsonObj7.put("hrz2Distance","625.0");
		trainingSessionDataJsonObj7.put("hrz3Distance","1146.67");
		trainingSessionDataJsonObj7.put("hrz4Distance","4950.0");
		trainingSessionDataJsonObj7.put("hrz5Distance","925.0");
		trainingSessionDataJsonObj7.put("hrz6Distance","0.0");
		trainingSessionDataJsonObj7.put("surfaceIndex","0");
		JSONObject saveFitnessTrainingSessionRes7 = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj7);
		Assert.assertEquals("200", saveFitnessTrainingSessionRes7.getString("status"));
		Assert.assertEquals("OK", saveFitnessTrainingSessionRes7.getString("message"));
		JSONObject objSaveFitnessTrainingSessionRes7 = saveFitnessTrainingSessionRes7.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSessionRes7.getString("userid"));
		Assert.assertEquals(96.54, objSaveFitnessTrainingSessionRes7.getDouble("shapeIndex"));
		Assert.assertEquals(134.0, objSaveFitnessTrainingSessionRes7.getDouble("recentTotalLoadOfExercise"));
		Assert.assertEquals(31.0, objSaveFitnessTrainingSessionRes7.getDouble("vDot"));
		
		/*Save Resting and Orthostatic Heart Rate test day 8*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 21);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson8 = new JSONObject();
		restingHeartRateTestReqJson8.put("heartrateType", "0");
		restingHeartRateTestReqJson8.put("heartrate", "66.0");
		restingHeartRateTestReqJson8.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson8);
		
		JSONObject orthoHeartRateTestReqJson8 = new JSONObject();
		orthoHeartRateTestReqJson8.put("heartrateType", "3");
		orthoHeartRateTestReqJson8.put("heartrate", "92.0");
		orthoHeartRateTestReqJson8.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson8);
		
	    /*Save Resting and Orthostatic Heart Rate test day 9*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 21);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson9 = new JSONObject();
		restingHeartRateTestReqJson9.put("heartrateType", "0");
		restingHeartRateTestReqJson9.put("heartrate", "67.0");
		restingHeartRateTestReqJson9.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson9);
		
		JSONObject orthoHeartRateTestReqJson9 = new JSONObject();
		orthoHeartRateTestReqJson9.put("heartrateType", "3");
		orthoHeartRateTestReqJson9.put("heartrate", "95.0");
		orthoHeartRateTestReqJson9.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson9);
	    
	    /*Get Shape Index on day 10*/
	    cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject getShapeIndexObj10 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex10 = getShapeIndexObj10.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex10.getString("userid"));
		//Assert.assertEquals(104.48, getShapeIndex10.getDouble("shapeIndex"));
	    
		/*Save Resting and Orthostatic Heart Rate test day 10*/
		cal.set(Calendar.HOUR_OF_DAY, 21);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson10 = new JSONObject();
		restingHeartRateTestReqJson10.put("heartrateType", "0");
		restingHeartRateTestReqJson10.put("heartrate", "68.0");
		restingHeartRateTestReqJson10.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson10);
		
		JSONObject orthoHeartRateTestReqJson10 = new JSONObject();
		orthoHeartRateTestReqJson10.put("heartrateType", "3");
		orthoHeartRateTestReqJson10.put("heartrate", "91.0");
		orthoHeartRateTestReqJson10.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
	    saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson10);
	    
	    
	    /*Get Second Shape Index on day 10*/
	    JSONObject getShapeIndexObj101 = getShapeIndex.get(JSONObject.class);
		JSONObject getShapeIndex101 = getShapeIndexObj101.getJSONObject("obj");
		Assert.assertEquals(userId, getShapeIndex101.getString("userid"));
		//Assert.assertEquals(104.78, getShapeIndex101.getDouble("shapeIndex"));
	    
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
	
	@After
	public void cleanUpAfterClass() throws Exception 
	{
		/*Delete Trainee Data*/	
		String clearUserDataUrl =  "http://localhost:9998/trainee/id/"+userId+"/data/clear"; 
		ClientConfig clientConfig = new DefaultClientConfig();
		Client clientClearUserData = Client.create(clientConfig);
		WebResource clearUserData = clientClearUserData.resource(clearUserDataUrl);
		clearUserData.delete();
	}


}
