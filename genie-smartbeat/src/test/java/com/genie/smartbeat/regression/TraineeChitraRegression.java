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

public class TraineeChitraRegression extends JerseyTest{
	
	UserBean chithra = new UserBean();
	String chitraFbId = "100006485698211";
	String userId = null;
	String accessToken = null;

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
		
		/*Get Default HeartrateZones for the User*/
		String getHeartRateZoneUrl = "http://localhost:9998/trainee/id/"+userId+"/heartrateZones?accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientGetUserHrz =  getClient();
		WebResource getUserHrz = clientGetUserHrz.resource(getHeartRateZoneUrl);
		JSONObject getHrzResJsonObj = getUserHrz.get(JSONObject.class);
		JSONObject getHrzResJson = getHrzResJsonObj.getJSONObject("obj");
		assertEquals(userId, getHrzResJson.getString("userid"));
		/*Assert.assertEquals(78.0, getHrzResJson.getDouble("heartrateZone1Start"));
		Assert.assertEquals(98.46, getHrzResJson.getDouble("heartrateZone1End"));
		Assert.assertEquals(98.46, getHrzResJson.getDouble("heartrateZone2Start"));
		Assert.assertEquals(108.69, getHrzResJson.getDouble("heartrateZone2End"));
		Assert.assertEquals(108.69, getHrzResJson.getDouble("heartrateZone3Start"));
		Assert.assertEquals(114.48, getHrzResJson.getDouble("heartrateZone3End"));
		Assert.assertEquals(114.48, getHrzResJson.getDouble("heartrateZone4Start"));
		Assert.assertEquals(121.13, getHrzResJson.getDouble("heartrateZone4End"));
		Assert.assertEquals(121.13, getHrzResJson.getDouble("heartrateZone5Start"));
		Assert.assertEquals(182.11, getHrzResJson.getDouble("heartrateZone5End"));
		Assert.assertEquals(182.11, getHrzResJson.getDouble("heartrateZone6Start"));
		Assert.assertEquals(188.75, getHrzResJson.getDouble("heartrateZone6End"));*/
		
		/*Save Resting and Ortho Static Heart Rate test day 0*/
		String saveHeartRateTestUrl = "http://localhost:9998/trainee/id/"+userId+"/heartrateTest/save?accessToken="+accessToken+"&accessTokenType=facebook";
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);
		Client clientRestingHeartrateTest =  getClient();
		WebResource saveRestingHeartrateTest =  clientRestingHeartrateTest.resource(saveHeartRateTestUrl);
		JSONObject restingHeartRateTestReqJson = new JSONObject();
		restingHeartRateTestReqJson.put("heartrateType", "0");
		restingHeartRateTestReqJson.put("heartrate", "54.0");
		restingHeartRateTestReqJson.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		JSONObject heartRateZonesFromHRT = saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson);
		//JSONObject heartRateZonesFromHRT1 = heartRateZonesFromHRT.getJSONObject("heartrateZones");
		
		JSONObject orthoHeartRateTestReqJson = new JSONObject();
		orthoHeartRateTestReqJson.put("heartrateType", "3");
		orthoHeartRateTestReqJson.put("heartrate", "78.0");
		orthoHeartRateTestReqJson.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		Client clientOrthoHeartrateTest =  getClient();
		WebResource saveOrthoHeartRateTest =  clientOrthoHeartrateTest.resource(saveHeartRateTestUrl);
    	JSONObject saveOrthoHRTResJson = saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson);
    	//heartRateZonesFromHRT = saveOrthoHRTResJson.getJSONObject("heartrateZones");
    	/*Assert.assertEquals(78.0, heartRateZonesFromHRT.getDouble("heartrateZone1Start"));
		Assert.assertEquals(98.46, heartRateZonesFromHRT.getDouble("heartrateZone1End"));
		Assert.assertEquals(98.46, heartRateZonesFromHRT.getDouble("heartrateZone2Start"));
		Assert.assertEquals(108.69, heartRateZonesFromHRT.getDouble("heartrateZone2End"));
		Assert.assertEquals(108.69, heartRateZonesFromHRT.getDouble("heartrateZone3Start"));
		Assert.assertEquals(114.48, heartRateZonesFromHRT.getDouble("heartrateZone3End"));
		Assert.assertEquals(114.48, heartRateZonesFromHRT.getDouble("heartrateZone4Start"));
		Assert.assertEquals(121.13, heartRateZonesFromHRT.getDouble("heartrateZone4End"));
		Assert.assertEquals(121.13, heartRateZonesFromHRT.getDouble("heartrateZone5Start"));
		Assert.assertEquals(182.11, heartRateZonesFromHRT.getDouble("heartrateZone5End"));
		Assert.assertEquals(182.11, heartRateZonesFromHRT.getDouble("heartrateZone6Start"));
		Assert.assertEquals(188.75, heartRateZonesFromHRT.getDouble("heartrateZone6End"));*/
		
		
		
		/*Save Training Session for the user in Day 0*/
		String saveFitnessTrainingSessionUrl = "http://localhost:9998/trainee/id/"+userId+"/trainingSession/save?accessToken="+accessToken+"&accessTokenType=facebook";
		JSONObject trainingSessionDataJsonObj = new JSONObject();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 0);
		Long sessionStartTime = cal.getTime().getTime();
		cal.add(Calendar.HOUR, 1);
		Long sessionEndTime = cal.getTime().getTime();
		DateTimeUtils.setCurrentMillisFixed(sessionEndTime);
		
		trainingSessionDataJsonObj.put("startTime", new Timestamp(sessionStartTime));
		trainingSessionDataJsonObj.put("endTime", new Timestamp(sessionEndTime) );
		trainingSessionDataJsonObj.put("hrz1Time","4.0");
		trainingSessionDataJsonObj.put("hrz2Time","32.0");
		trainingSessionDataJsonObj.put("hrz3Time","14.0");
		trainingSessionDataJsonObj.put("hrz4Time","10.0");
		trainingSessionDataJsonObj.put("hrz5Time","0.0");
		trainingSessionDataJsonObj.put("hrz6Time","0.0");
		trainingSessionDataJsonObj.put("hrz1Distance","1000.0");
		trainingSessionDataJsonObj.put("hrz2Distance","5920.0");
		trainingSessionDataJsonObj.put("hrz3Distance","2753.33");
		trainingSessionDataJsonObj.put("hrz4Distance","2200.0");
		trainingSessionDataJsonObj.put("hrz5Distance","0.0");
		trainingSessionDataJsonObj.put("hrz6Distance","0.0");
		trainingSessionDataJsonObj.put("surfaceIndex","0");
		Client clientSaveFitnessTrainingSession =  getClient();
		WebResource saveFitnessTrainingSession = clientSaveFitnessTrainingSession.resource(saveFitnessTrainingSessionUrl);
		JSONObject saveFitnessTrainingSessionResJson = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj);
		Assert.assertEquals("200", saveFitnessTrainingSessionResJson.getString("status"));
		Assert.assertEquals("OK", saveFitnessTrainingSessionResJson.getString("message"));
		JSONObject objSaveFitnessTrainingSessionReponse = saveFitnessTrainingSessionResJson.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSessionReponse.getString("userid"));
		Assert.assertEquals(100.0, objSaveFitnessTrainingSessionReponse.getDouble("shapeIndex"));
		
		
		
		/*Get Shape Index one hour after the training session is over*/
		cal.set(Calendar.HOUR_OF_DAY, 12);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		String getShapeIndexUrl = "http://localhost:9998/trainee/id/"+userId+"/shapeIndex?accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientGetShapeIndex =  getClient();
		WebResource getShapeIndex = clientGetShapeIndex.resource(getShapeIndexUrl);
		JSONObject getShapeIndexResJson0 = getShapeIndex.get(JSONObject.class);
		System.out.println(getShapeIndexResJson0);
		
		/*Get Shape Index after supercompensation on day 1*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 15);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject getShapeIndexResJson11 = getShapeIndex.get(JSONObject.class);
		System.out.println(getShapeIndexResJson11);
		
		/*Get Shape Index just before the next training session*/
		cal.set(Calendar.HOUR_OF_DAY, 19);
		cal.set(Calendar.MINUTE, 0);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject getShapeIndexResJson12 = getShapeIndex.get(JSONObject.class);
		System.out.println(getShapeIndexResJson12);
		
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
		JSONObject saveFitnessTrainingSession1ResJson = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj1);
		Assert.assertEquals("200", saveFitnessTrainingSession1ResJson.getString("status"));
		Assert.assertEquals("OK", saveFitnessTrainingSession1ResJson.getString("message"));
		JSONObject objSaveFitnessTrainingSession1Reponse = saveFitnessTrainingSessionResJson.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSession1Reponse.getString("userid"));
		//Assert.assertEquals(100.0, objSaveFitnessTrainingSession1Reponse.getDouble("shapeIndex"));
		System.out.println(saveFitnessTrainingSession1ResJson);
		
		/*Save Resting and Orthostatic Heart Rate test day 2*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 50);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson1 = new JSONObject();
		restingHeartRateTestReqJson1.put("heartrateType", "0");
		restingHeartRateTestReqJson1.put("heartrate", "55.0");
		restingHeartRateTestReqJson1.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		JSONObject heartRateZonesFromHRT1 = saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson1);
		
		JSONObject orthoHeartRateTestReqJson1 = new JSONObject();
		orthoHeartRateTestReqJson1.put("heartrateType", "3");
		orthoHeartRateTestReqJson1.put("heartrate", "76.0");
		orthoHeartRateTestReqJson1.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		JSONObject saveOrthoHRTResJson1 = saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson1);
	
		/*Get Shape Index on day 2*/
		JSONObject getShapeIndexResJson2 = getShapeIndex.get(JSONObject.class);
		System.out.println(getShapeIndexResJson2);
		
		/*Save Resting and Orthostatic Heart Rate test day 3*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 50);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson2 = new JSONObject();
		restingHeartRateTestReqJson2.put("heartrateType", "0");
		restingHeartRateTestReqJson2.put("heartrate", "58.0");
		restingHeartRateTestReqJson2.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		JSONObject heartRateZonesFromHRT2 = saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson2);
		
		JSONObject orthoHeartRateTestReqJson2 = new JSONObject();
		orthoHeartRateTestReqJson2.put("heartrateType", "3");
		orthoHeartRateTestReqJson2.put("heartrate", "75.0");
		orthoHeartRateTestReqJson2.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		JSONObject saveOrthoHRTResJson2 = saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson2);
		
		/*Get Shape Index on day 4*/
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 15);
		cal.set(Calendar.MINUTE, 00);
		JSONObject getShapeIndexResJson3 = getShapeIndex.get(JSONObject.class);
		System.out.println(getShapeIndexResJson3);
		
		/*Save Resting and Orthostatic Heart Rate test day 4*/
		cal.set(Calendar.MINUTE, 50);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		JSONObject restingHeartRateTestReqJson3 = new JSONObject();
		restingHeartRateTestReqJson3.put("heartrateType", "0");
		restingHeartRateTestReqJson3.put("heartrate", "55.0");
		restingHeartRateTestReqJson3.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		JSONObject heartRateZonesFromHRT3 = saveRestingHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,restingHeartRateTestReqJson3);
		
		JSONObject orthoHeartRateTestReqJson3 = new JSONObject();
		orthoHeartRateTestReqJson3.put("heartrateType", "3");
		orthoHeartRateTestReqJson3.put("heartrate", "77.0");
		orthoHeartRateTestReqJson3.put("timeOfRecord", new Timestamp(cal.getTime().getTime()));
		JSONObject saveOrthoHRTResJson3 = saveOrthoHeartRateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,orthoHeartRateTestReqJson3);
		
		
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
		JSONObject saveFitnessTrainingSessionResJson4 = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj4);
		Assert.assertEquals("200", saveFitnessTrainingSession1ResJson.getString("status"));
		Assert.assertEquals("OK", saveFitnessTrainingSession1ResJson.getString("message"));
		JSONObject objSaveFitnessTrainingSessionReponse4 = saveFitnessTrainingSessionResJson.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSession1Reponse.getString("userid"));
		//Assert.assertEquals(100.0, objSaveFitnessTrainingSession1Reponse.getDouble("shapeIndex"));
		System.out.println(saveFitnessTrainingSessionResJson4);
		
	}
	
	private Client getClient(){
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client client = Client.create(clientConfig);	
		return client;
	}

}
