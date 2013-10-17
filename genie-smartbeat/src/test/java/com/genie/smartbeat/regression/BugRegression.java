package com.genie.smartbeat.regression;

import java.util.Calendar;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTimeUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

/**
 * @author dhasarathy
 **/

public class BugRegression extends JerseyTest{
	
	UserBean chithra = new UserBean();
	String chitraFbId = "100006781293572";
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
		System.out.println(registerResJson);
		JSONObject objRegister = registerResJson.getJSONObject("obj");
		Assert.assertEquals("200", registerResJson.getString("status"));
		Assert.assertEquals("OK", registerResJson.getString("message"));
		userId = objRegister.getString("userid");
		chithra.setUserid(userId);
		
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
	public void testIssue7() throws Exception{
	
		
		
		/*Save Training Session for the user in Day 0*/
		String saveFitnessTrainingSessionUrl = "http://localhost:9998/trainee/id/"+userId+"/trainingSession/save?accessToken="+accessToken+"&accessTokenType=facebook";
		JSONObject trainingSessionDataJsonObj = new JSONObject();		
		
		trainingSessionDataJsonObj.put("healthPerceptionIndex","5");
		trainingSessionDataJsonObj.put("hrz6Time","0.0");
		trainingSessionDataJsonObj.put("hrz3Distance","0");
		trainingSessionDataJsonObj.put("hrz1Time","0.2992607653141022");
		trainingSessionDataJsonObj.put("hrz2Distance","0");
		trainingSessionDataJsonObj.put("sessionStressPerceptionIndex","5");
		trainingSessionDataJsonObj.put("hrz4Time","0");
		trainingSessionDataJsonObj.put("hrz1Distance","0");
		trainingSessionDataJsonObj.put("hrz2Time","0.06650201976299286");
		trainingSessionDataJsonObj.put("hrz5Time","0");
		trainingSessionDataJsonObj.put("startTime", "2013-09-17 08:36:57.938");
		trainingSessionDataJsonObj.put("hrz6Distance","0");
		trainingSessionDataJsonObj.put("surfaceIndex","3");
		trainingSessionDataJsonObj.put("hrz5Distance","0");
		trainingSessionDataJsonObj.put("endTime", "2013-09-17 08:37:39.489");
		trainingSessionDataJsonObj.put("hrz3Time","0");
		trainingSessionDataJsonObj.put("hrz4Distance","0");
		
		/*trainingSessionDataJsonObj.put("startTime", "2013-09-17 08:36:57.938");
		trainingSessionDataJsonObj.put("endTime", "2013-09-17 08:37:39.489");
		trainingSessionDataJsonObj.put("hrz1Time","0.2992607653141022");
		trainingSessionDataJsonObj.put("hrz2Time","0.06650201976299286");
		trainingSessionDataJsonObj.put("hrz3Time","0");
		trainingSessionDataJsonObj.put("hrz4Time","0");
		trainingSessionDataJsonObj.put("hrz5Time","0");
		trainingSessionDataJsonObj.put("hrz6Time","0.0");
		trainingSessionDataJsonObj.put("hrz1Distance","0");
		trainingSessionDataJsonObj.put("hrz2Distance","0");
		trainingSessionDataJsonObj.put("hrz3Distance","0");
		trainingSessionDataJsonObj.put("hrz4Distance","0");
		trainingSessionDataJsonObj.put("hrz5Distance","0.0");
		trainingSessionDataJsonObj.put("hrz6Distance","0.0");
		trainingSessionDataJsonObj.put("surfaceIndex","0");
		trainingSessionDataJsonObj.put("healthPerceptionIndex","5");
		trainingSessionDataJsonObj.put("sessionStressPerceptionIndex","5");
		trainingSessionDataJsonObj.put("muscleStatePerceptionIndex","5");*/
		
		Client clientSaveFitnessTrainingSession =  getClient();
		WebResource saveFitnessTrainingSession = clientSaveFitnessTrainingSession.resource(saveFitnessTrainingSessionUrl);
		JSONObject saveFitnessTrainingSessionResJson = saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj);
		System.out.println(saveFitnessTrainingSessionResJson);
		
	}
	
	@Test
	public void testIssue17() throws Exception{
		String saveHeartrateTestUrl = "http://localhost:9998/trainee/id/"+userId+"/heartrateTest/save?accessToken="+accessToken+"&accessTokenType=facebook";
		JSONObject heartrateTestJson = new JSONObject();
		heartrateTestJson.put("timeOfRecord","2013-10-10 21:21:04.087");
		heartrateTestJson.put("heartrateType","0");
		heartrateTestJson.put("heartrate","60");
		Client clientSaveHeartrateTest =  getClient();
		WebResource saveHeartrateTest = clientSaveHeartrateTest.resource(saveHeartrateTestUrl);
		JSONObject saveHeartrateTestResJson = saveHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,heartrateTestJson);
		System.out.println(saveHeartrateTestResJson);
		
		heartrateTestJson = new JSONObject();
		heartrateTestJson.put("timeOfRecord","2013-10-10 21:21:54.573");
		heartrateTestJson.put("heartrateType","1");
		heartrateTestJson.put("heartrate","160");
		clientSaveHeartrateTest =  getClient();
		saveHeartrateTest = clientSaveHeartrateTest.resource(saveHeartrateTestUrl);
		saveHeartrateTestResJson = saveHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,heartrateTestJson);
		System.out.println(saveHeartrateTestResJson);
		
		heartrateTestJson = new JSONObject();
		heartrateTestJson.put("timeOfRecord","2013-10-10 21:21:55.420");
		heartrateTestJson.put("heartrateType","2");
		heartrateTestJson.put("heartrate","180");
		clientSaveHeartrateTest =  getClient();
		saveHeartrateTest = clientSaveHeartrateTest.resource(saveHeartrateTestUrl);
		saveHeartrateTestResJson = saveHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,heartrateTestJson);
		System.out.println(saveHeartrateTestResJson);
		
		heartrateTestJson = new JSONObject();
		heartrateTestJson.put("timeOfRecord","2013-10-10 21:21:56.894");
		heartrateTestJson.put("heartrateType","3");
		heartrateTestJson.put("heartrate","80");
		clientSaveHeartrateTest =  getClient();
		saveHeartrateTest = clientSaveHeartrateTest.resource(saveHeartrateTestUrl);
		saveHeartrateTestResJson = saveHeartrateTest.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,heartrateTestJson);
		System.out.println(saveHeartrateTestResJson);
	}
	private Client getClient(){
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client client = Client.create(clientConfig);	
		return client;
	}	
}

