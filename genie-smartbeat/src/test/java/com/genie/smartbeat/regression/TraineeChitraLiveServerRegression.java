package com.genie.smartbeat.regression;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;

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

public class TraineeChitraLiveServerRegression{

	UserBean chithra = new UserBean();
	String chitraFbId = "100006485698211";
	String userId = null;
	String accessToken = null;

	public static final String HOST_GENIE_LIVE_VIDHUN = "ec2-54-229-146-226.eu-west-1.compute.amazonaws.com";
	public static final String HOST = HOST_GENIE_LIVE_VIDHUN;
	public static String PORT = "8080";
	
	@Before
	public void setUpBeforeClass() throws Exception 
	{
		accessToken = GraphAPI.getTestUserAccessToken(chitraFbId);
		chithra.setAccessToken(accessToken);
		chithra.setAccessTokenType("facebook");
	}
	
	
	@Test
	public void traineeChitraRegressionTest() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(DateTimeUtils.currentTimeMillis());

		
		/*Register the User*/
	    String registerUserUrl = "http://"+HOST+":"+PORT+"/smartbeat/v1.0/trainee/register";
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
		String getHeartRateZoneUrl = "http://"+HOST+":"+PORT+"/smartbeat/v1.0/trainee/id/"+userId+"/heartrateZones?accessToken="+accessToken+"&accessTokenType=facebook";
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
		String saveHeartRateTestUrl = "http://"+HOST+":"+PORT+"/smartbeat/v1.0/trainee/id/"+userId+"/heartrateTest/save?accessToken="+accessToken+"&accessTokenType=facebook";
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
		String saveFitnessTrainingSessionUrl = "http://"+HOST+":"+PORT+"/smartbeat/v1.0/trainee/id/"+userId+"/trainingSession/save?accessToken="+accessToken+"&accessTokenType=facebook";
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
		System.out.println(saveFitnessTrainingSessionResJson);
		Assert.assertEquals("200", saveFitnessTrainingSessionResJson.getString("status"));
		Assert.assertEquals("OK", saveFitnessTrainingSessionResJson.getString("message"));
		JSONObject objSaveFitnessTrainingSessionReponse = saveFitnessTrainingSessionResJson.getJSONObject("obj");
		Assert.assertEquals(userId, objSaveFitnessTrainingSessionReponse.getString("userid"));
		Assert.assertEquals(100.0, objSaveFitnessTrainingSessionReponse.getDouble("shapeIndex"));
		
		
		
		/*Get Shape Index one hour after the training session is over*/
		cal.set(Calendar.HOUR_OF_DAY, 12);
		DateTimeUtils.setCurrentMillisFixed(cal.getTime().getTime());
		String getShapeIndexUrl = "http://"+HOST+":"+PORT+"/smartbeat/v1.0/trainee/id/"+userId+"/shapeIndex?accessToken="+accessToken+"&accessTokenType=facebook";
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
		
	
		
	}
	
	private Client getClient(){
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client client = Client.create(clientConfig);	
		return client;
	}


}
