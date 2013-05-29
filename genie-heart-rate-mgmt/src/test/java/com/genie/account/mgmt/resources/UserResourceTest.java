package com.genie.account.mgmt.resources;

import java.net.URLEncoder;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

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
	
    /*public testUserResource()throws Exception {
        super("com.genie.account.mgmt.resources.UserResource");
    	//super(new WebAppDescriptor.Builder("com.genie.account.mgmt.resources").servletClass(SpringServlet.class).contextParam("contextConfigLocation", "/META-INF/spring/testApplicationContext.xml").contextListenerClass(ContextLoaderListener.class).contextPath("context").build());


    }*/
	
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
	public void testGetUserInformationByEmail() throws Exception{
		
		String appID = "333643156765163";
		/*Get Access Token for the App from Facebook*/
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
		
		
		String getFacebookTestUserUrl = "https://graph.facebook.com/"+appID+"/accounts/test-users?installed=true&permissions=read_stream&method=post&access_token="+URLEncoder.encode(appAccessTokenValue,"ISO-8859-1");
		//String getFacebookTestUserUrl = "https://graph.facebook.com/"+appID+"/accounts/test-users?installed=true&permissions=read_stream&method=post&access_token="+appAccessTokenValue;
		ClientConfig clientConfigGetFacebookTestUser = new DefaultClientConfig();
		clientConfigGetFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientGetFacebookTestUser = Client.create(clientConfigGetFacebookTestUser);
		WebResource getFacebookTestUser = clientGetFacebookTestUser.resource(getFacebookTestUserUrl);
		//JSONObject FacebookTestUser = getFacebookTestUser.type(MediaType.APPLICATION_FORM_URLENCODED).post(JSONObject.class);
		//System.out.println(FacebookTestUser);
		
		String userAccessToken = "333643156765163|QN_P9lIvtcdVT9AsmOLIliq47bs";
		
		/*Parsing User Details*/
		/*String userID = FacebookTestUser.getString("id");
		String userAccessToken = FacebookTestUser.getString("access_token");
	    String userLoginUrl = FacebookTestUser.getString("login_url");
		String userEmail = FacebookTestUser.getString("email");
		String userPassword = FacebookTestUser.getString("password");*/
		
		
		/*Trying to Register the User*/
	    String registerUserUrl = "http://localhost:9998/Users/register";
		//URI uriRegister = UriBuilder.fromUri("http://localhost:9998/Users/register").build();
		JSONObject inputJsonObj = new JSONObject();
		inputJsonObj.put("accessToken", userAccessToken );
		inputJsonObj.put("accessTokenType", "facebook" );
		ClientConfig clientConfigRegisterUser = new DefaultClientConfig();
		clientConfigRegisterUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientRegisterUser = Client.create(clientConfigRegisterUser);
		WebResource registerUser = clientRegisterUser.resource(registerUserUrl);
		String registerResponse = registerUser.type(MediaType.APPLICATION_JSON_TYPE).post(String.class,inputJsonObj.toString());
		System.out.println(registerResponse);
		
	
		//UserInfoJSON userInfo = new ObjectMapper().readValue(response.getString("obj"),UserInfoJSON.class);
		
		
		/*String getUserInfoByEmailUrl = "http://localhost:9998/Users/vidhunkps@yahoo.co.in?accessToken=CAACEdEose0cBALHxgNmVA6tIEwQ5fIJmlYNaOOi8K2hvu7SWuC5AZCnhDwo4aFfY74iuVfCQQPndEffi5pm8qAIQPhXfa4WiXNxGi8ZBd2oizAz6HIsiYLM6Joh5OjyYCcNAe5RR5kp6riekSi9bxXIX65aIfrDVoDrrzAkgZDZD&accessTokenType=facebook";		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client client = Client.create(clientConfig);
		WebResource webresource = client.resource(getUserInfoByEmailUrl);
		JSONObject response = webresource.get(JSONObject.class);
		UserInfoJSON userInfo = new ObjectMapper().readValue(response.getString("obj"),UserInfoJSON.class);*/
		
	
       
	}

}
