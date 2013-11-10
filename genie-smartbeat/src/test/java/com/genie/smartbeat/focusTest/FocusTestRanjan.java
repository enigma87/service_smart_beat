package com.genie.smartbeat.focusTest;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
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

public class FocusTestRanjan extends JerseyTest {

	UserBean ranjan = new UserBean();
	String ranjanFbId = "100006876185961";
	String ranjanId = null;
	String accessToken = null;
	String robertUserId = "537cfed4-1365-48b8-b173-75b5687506df";

	public static final String HOST_GENIE_LIVE_VIDHUN = "ec2-54-229-146-226.eu-west-1.compute.amazonaws.com";
	public static final String HOST_LOCALHOST = "localhost";
	public static final String HOST = HOST_GENIE_LIVE_VIDHUN;
	public static String PORT = "8080";
	public static String BASE_URL;
	
	@Before
	public void setUpBeforeClass() throws Exception 
	{
		accessToken = GraphAPI.getTestUserAccessToken(ranjanFbId);
		ranjan.setAccessToken(accessToken);
		ranjan.setAccessTokenType("facebook");
		
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
	public void traineeRanjanFocusTest() throws Exception {
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(DateTimeUtils.currentTimeMillis());
        Timestamp endTimestamp = new Timestamp(cal.getTime().getTime());
        cal.add(Calendar.YEAR, -1);
        Timestamp startTimestamp = new Timestamp(cal.getTime().getTime());
		
		/*Register the User*/
	    String registerUserUrl = "http://localhost:9998/trainee/register";
	    String registerUserUrlLiveServer = "http://"+HOST+":"+PORT+"/smartbeat/v1.0/trainee/register";
		JSONObject inputJsonObj = new JSONObject();
		inputJsonObj.put("accessToken", ranjan.getAccessToken());
		inputJsonObj.put("accessTokenType", ranjan.getAccessTokenType() );
		
    	Client clientRegisterUser = getClient();
		WebResource registerUser = clientRegisterUser.resource(registerUserUrl);      
		JSONObject registerResJson = registerUser.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,inputJsonObj);
		JSONObject objRegister = registerResJson.getJSONObject("obj");
		ranjanId = objRegister.getString("userid");
		
		//WebResource registerUserLiveServer = clientRegisterUser.resource(registerUserUrlLiveServer);      
		//objRegister = registerUserLiveServer.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,inputJsonObj);
		
		
		String saveFitnessTrainingSessionUrl = "http://localhost:9998/trainee/id/"+ranjanId+"/trainingSession/save?accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientSaveFitnessTrainingSession =  getClient();
		WebResource saveFitnessTrainingSession = clientSaveFitnessTrainingSession.resource(saveFitnessTrainingSessionUrl);
		
		/*Get Training Session for Robert*/
		//String getTrainingSessionsUrl = "http://"+HOST+":"+PORT+"/smartbeat/v1.0/trainee/register";
		String getTrainingSessionsUrl = "http://"+HOST+":"+PORT+"/smartbeat/v1.0/trainee/id/"+robertUserId+"/trainingSession/inTimeInterval?startTimeStamp="+URLEncoder.encode(String.valueOf(startTimestamp), "UTF-8")+"&endTimeStamp="+URLEncoder.encode(String.valueOf(endTimestamp), "UTF-8")+"&accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientGetTrainingSessions = getClient();
		WebResource getTrainingSession = clientGetTrainingSessions.resource(getTrainingSessionsUrl);
		JSONObject getTrainingSessionsRes = getTrainingSession.get(JSONObject.class);
		JSONObject trainingSessionsObj = getTrainingSessionsRes.getJSONObject("obj");
		JSONArray trainingSessionsJson =  trainingSessionsObj.getJSONArray("trainingSessionBeans");
		

	
		for (int i=trainingSessionsJson.length()-1; i>=0 ;i--){
	      
	      JSONObject trainingSessionDataJsonObj = new JSONObject();
		  trainingSessionDataJsonObj.put("startTime", Timestamp.valueOf(trainingSessionsJson.getJSONObject(i).getString("startTime")));
		  Timestamp endTime = Timestamp.valueOf(trainingSessionsJson.getJSONObject(i).getString("endTime"));
		  DateTimeUtils.setCurrentMillisFixed(endTime.getTime());
		  trainingSessionDataJsonObj.put("endTime", endTime);
		  trainingSessionDataJsonObj.put("hrz1Time",trainingSessionsJson.getJSONObject(i).getDouble("hrz1Time"));
		  trainingSessionDataJsonObj.put("hrz2Time",trainingSessionsJson.getJSONObject(i).getDouble("hrz2Time"));
		  trainingSessionDataJsonObj.put("hrz3Time",trainingSessionsJson.getJSONObject(i).getDouble("hrz3Time"));
		  trainingSessionDataJsonObj.put("hrz4Time",trainingSessionsJson.getJSONObject(i).getDouble("hrz4Time"));
		  trainingSessionDataJsonObj.put("hrz5Time",trainingSessionsJson.getJSONObject(i).getDouble("hrz5Time"));
		  trainingSessionDataJsonObj.put("hrz6Time",trainingSessionsJson.getJSONObject(i).getDouble("hrz6Time"));
		  trainingSessionDataJsonObj.put("hrz1Distance",trainingSessionsJson.getJSONObject(i).getDouble("hrz1Distance"));
		  trainingSessionDataJsonObj.put("hrz2Distance",trainingSessionsJson.getJSONObject(i).getDouble("hrz2Distance"));
		  trainingSessionDataJsonObj.put("hrz3Distance",trainingSessionsJson.getJSONObject(i).getDouble("hrz3Distance"));
		  trainingSessionDataJsonObj.put("hrz4Distance",trainingSessionsJson.getJSONObject(i).getDouble("hrz4Distance"));
		  trainingSessionDataJsonObj.put("hrz5Distance",trainingSessionsJson.getJSONObject(i).getDouble("hrz5Distance"));
	      trainingSessionDataJsonObj.put("hrz6Distance",trainingSessionsJson.getJSONObject(i).getDouble("hrz6Distance"));
	      trainingSessionDataJsonObj.put("surfaceIndex",trainingSessionsJson.getJSONObject(i).getInt("surfaceIndex"));
	      trainingSessionDataJsonObj.put("vdot",trainingSessionsJson.getJSONObject(i).getInt("vdot"));
	      trainingSessionDataJsonObj.put("healthPerceptionIndex",trainingSessionsJson.getJSONObject(i).getInt("healthPerceptionIndex"));
	      trainingSessionDataJsonObj.put("muscleStatePerceptionIndex",trainingSessionsJson.getJSONObject(i).getInt("muscleStatePerceptionIndex"));
	      trainingSessionDataJsonObj.put("sessionStressPerceptionIndex",trainingSessionsJson.getJSONObject(i).getInt("sessionStressPerceptionIndex"));
	      saveFitnessTrainingSession.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,trainingSessionDataJsonObj);
	     
		}
		
		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.MONTH, -6);
        Timestamp startTime = new Timestamp(cal1.getTime().getTime());
        Calendar cal2 = Calendar.getInstance();
		Timestamp endTime = new Timestamp(cal2.getTime().getTime());
		DateTimeUtils.setCurrentMillisFixed(cal2.getTime().getTime());
		String getShapeIndexUrl = "http://localhost:9998/trainee/id/"+ranjanId+"/shapeIndex?accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientGetShapeIndex =  getClient();
		WebResource getShapeIndex = clientGetShapeIndex.resource(getShapeIndexUrl);	
		JSONObject getShapeIndexObj0 = getShapeIndex.get(JSONObject.class);
		System.out.println(getShapeIndexObj0);
		
		String getShapeIndexUrlInterval = "http://localhost:9998/trainee/id/"+ranjanId+"/shapeIndex/inTimeInterval?startTimeStamp="+URLEncoder.encode(String.valueOf(startTime),"UTF-8")+"&endTimeStamp="+URLEncoder.encode(String.valueOf(endTime),"UTF-8")+"&accessToken="+accessToken+"&accessTokenType=facebook";
		Client clientGetShapeIndexInteval =  getClient();
		WebResource getShapeIndexInterval = clientGetShapeIndexInteval.resource(getShapeIndexUrlInterval);	
		JSONObject getShapeIndexIntervalObj0 = getShapeIndexInterval.get(JSONObject.class);
		System.out.println(getShapeIndexIntervalObj0);
		
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
		String clearUserDataUrl =  "http://localhost:9998/trainee/id/"+ranjanId+"/data/clear"; 
		ClientConfig clientConfig = new DefaultClientConfig();
		Client clientClearUserData = Client.create(clientConfig);
		WebResource clearUserData = clientClearUserData.resource(clearUserDataUrl);
		clearUserData.delete();
	}
	
}
