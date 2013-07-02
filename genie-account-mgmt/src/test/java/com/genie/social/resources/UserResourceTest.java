package com.genie.social.resources;

import java.net.URLEncoder;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import com.genie.social.dao.UserDao;
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

public class UserResourceTest extends JerseyTest  {
	
	
	private static ApplicationContext applicationContext;
	private static UserDao userDao;
	private static String appID = "333643156765163";
 	
	@BeforeClass
	public static void setupUserDao(){
		applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		userDao = (UserDao)applicationContext.getBean("userDao");
	}
	
	@Override
	protected AppDescriptor configure() {
		return new WebAppDescriptor.Builder("com.genie.account.mgmt.resources").contextParam("contextConfigLocation", "classpath:META-INF/spring/testApplicationContext.xml").servletClass(SpringServlet.class).contextListenerClass(ContextLoaderListener.class).requestListenerClass(RequestContextListener.class).build();
		//return new WebAppDescriptor.Builder().build();
	}

	@Override
    public TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }
    
	
	@Test
	public void testRegisterUser() throws Exception{
					
		/*Get App Access Token from facebook*/
	    String getAppAccessTokenUrl = "https://graph.facebook.com/oauth/access_token?client_id="+appID+"&client_secret=bd8fa4961cb1c2a284cbe8486707b73a&grant_type=client_credentials";
		ClientConfig clientConfigGetAppAccessToken = new DefaultClientConfig();
		clientConfigGetAppAccessToken.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientGetAppToken = Client.create(clientConfigGetAppAccessToken);
		WebResource getAppAccessToken = clientGetAppToken.resource(getAppAccessTokenUrl);
		String appAccessTokenResponse = getAppAccessToken.get(String.class);
		String[] appAccessToken = appAccessTokenResponse.split("=");
		String appAccessTokenValue = appAccessToken[1];
		System.out.println(appAccessTokenValue);
		
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
	    String registerUserUrl = "http://localhost:9998/Users/register";
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
						
		/*Delete Facebook TestUser*/
		String DeleteFbTestUserUrl = "https://graph.facebook.com/"+userID+"?method=delete&access_token="+userAccessToken;
		ClientConfig clientConfigDeleteFacebookTestUser = new DefaultClientConfig();
		clientConfigDeleteFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientDeleteFacebookTestUser = Client.create(clientConfigDeleteFacebookTestUser);
		WebResource deleteFacebookTestUser = clientDeleteFacebookTestUser.resource(DeleteFbTestUserUrl);
		deleteFacebookTestUser.post();
			
		/*Delete TestUser from Genie*/
		userDao.deleteUser(genieUserID);
		       
	}

	
	@Test
	public void testGetUserInfo() throws Exception{
				
		/*Get App Access Token from facebook*/
	    String getAppAccessTokenUrl = "https://graph.facebook.com/oauth/access_token?client_id="+appID+"&client_secret=bd8fa4961cb1c2a284cbe8486707b73a&grant_type=client_credentials";
		ClientConfig clientConfigGetAppAccessToken = new DefaultClientConfig();
		clientConfigGetAppAccessToken.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientGetAppToken = Client.create(clientConfigGetAppAccessToken);
		WebResource getAppAccessToken = clientGetAppToken.resource(getAppAccessTokenUrl);
		String appAccessTokenResponse = getAppAccessToken.get(String.class);
		String[] appAccessToken = appAccessTokenResponse.split("=");
		String appAccessTokenValue = appAccessToken[1];
		System.out.println(appAccessTokenValue);
		
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
		String userEmail = FacebookTestUser.getString("email");
		
		/*Register the User*/
	    String registerUserUrl = "http://localhost:9998/Users/register";
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
		
		/*Get User Information by email*/
		String getUserInfoByEmailUrl = "http://localhost:9998/Users/"+userEmail+"?accessToken="+userAccessToken+"&accessTokenType=facebook";		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client client = Client.create(clientConfig);
		WebResource webresource = client.resource(getUserInfoByEmailUrl);
		JSONObject getUserInfoResponse = webresource.get(JSONObject.class);
		JSONObject objGetUserInfo = getUserInfoResponse.getJSONObject("obj");
		Assert.assertEquals("200", getUserInfoResponse.getString("status"));
		Assert.assertEquals("OK", getUserInfoResponse.getString("message"));
		Assert.assertEquals(genieUserID, objGetUserInfo.getString("userid"));
		Assert.assertNotNull(objGetUserInfo.getString("firstName"));
		Assert.assertNotNull(objGetUserInfo.getString("lastName"));
				
		/*Delete Facebook TestUser*/
		String DeleteFbTestUserUrl = "https://graph.facebook.com/"+userID+"?method=delete&access_token="+userAccessToken;
		ClientConfig clientConfigDeleteFacebookTestUser = new DefaultClientConfig();
		clientConfigDeleteFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientDeleteFacebookTestUser = Client.create(clientConfigDeleteFacebookTestUser);
		WebResource deleteFacebookTestUser = clientDeleteFacebookTestUser.resource(DeleteFbTestUserUrl);
		deleteFacebookTestUser.post();
			
		/*Delete TestUser from Genie*/
		userDao.deleteUser(genieUserID);
		       
	}
	
	

}
