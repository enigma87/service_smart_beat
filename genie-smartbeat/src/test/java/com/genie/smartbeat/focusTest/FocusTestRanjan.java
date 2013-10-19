package com.genie.smartbeat.focusTest;

import java.util.Calendar;

import javax.ws.rs.core.MediaType;

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

public class FocusTestRanjan extends JerseyTest {

	UserBean ranjan = new UserBean();
	String ranjanFbId = "100006876185961";
	String ranjanId = null;
	String accessToken = null;
	String robertUserId = "537cfed4-1365-48b8-b173-75b5687506df";

	public static final String HOST_GENIE_LIVE_VIDHUN = "ec2-54-229-146-226.eu-west-1.compute.amazonaws.com";
	public static final String HOST_LOCALHOST = "localhost";
	public static final String HOST = HOST_LOCALHOST;
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

		
		/*Register the User*/
	    String registerUserUrl = "http://"+HOST+":"+PORT+"/smartbeat/v1.0/trainee/register";
		JSONObject inputJsonObj = new JSONObject();
		inputJsonObj.put("accessToken", ranjan.getAccessToken());
		inputJsonObj.put("accessTokenType", ranjan.getAccessTokenType() );
		
    	Client clientRegisterUser = getClient();
		WebResource registerUser = clientRegisterUser.resource(registerUserUrl);      
		JSONObject registerResJson = registerUser.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(JSONObject.class,inputJsonObj);
		JSONObject objRegister = registerResJson.getJSONObject("obj");
		ranjanId = objRegister.getString("userid");
		
		/*Get Training Session for Robert*/
		
		
		
	}
	
	
	private Client getClient(){
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client client = Client.create(clientConfig);	
		return client;
	}
	
}
