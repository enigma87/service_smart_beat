package com.genie.account.mgmt.impl;

import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.core.UserManager;
import com.genie.account.mgmt.dao.UserDao;
import com.genie.account.mgmt.util.AuthenticationStatus;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.research.ws.wadl.Request;
/**
 * @author vidhun
 *
 */

public class UserManagerMySQLImplTest {

	private static ApplicationContext applicationContext;
	private static UserDao userDao;
	private static UserManager userManagerMySQLImpl;
	private static User user;
	private static Date Dob = null;
	private static Timestamp timestamp ;
	private static String appID = "333643156765163";
	
	@BeforeClass
	public static void setupUserDaoTestCases(){
		applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		userDao = (UserDao)applicationContext.getBean("userDao");
		userManagerMySQLImpl = (UserManager)applicationContext.getBean("userManagerMySQLImpl");
		String Dateformat = "MM/dd/yyyy";
		timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
		SimpleDateFormat sdf =  new SimpleDateFormat(Dateformat);
		try{
			Dob = new Date(sdf.parse("01/01/1984").getTime());
			}
			catch (ParseException e){
				
			}

		user = new User();
		user.setUserid("123456789");
		user.setAccessToken("access_token_123456789");
		user.setAccessTokenType(User.ACCESS_TOKEN_TYPE_CUSTOM);
		user.setFirstName("Alice");
		user.setMiddleName("Bob");
		user.setLastName("CampBell");
		user.setDob(Dob);
		user.setEmail("abc@xyz.com");		
		user.setImageUrl("www.picasa.com/1002");
		user.setCreatedTs(timestamp);
		user.setLastUpdatedTs(timestamp);
		user.setLastLoginTs(timestamp);
		user.setActive(true);
	}
	
	@Test
	public void testCreateUser() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		
		User user1 = usMgr.getUserInformation(user.getUserid());
		Assert.assertEquals("123456789", user1.getUserid());
		user.setAccessToken("access_token_123456789");
		user.setAccessTokenType(User.ACCESS_TOKEN_TYPE_CUSTOM);
		Assert.assertEquals("Alice", user1.getFirstName());
		Assert.assertEquals("Bob", user1.getMiddleName());
		Assert.assertEquals("CampBell", user1.getLastName());
		Assert.assertEquals(Dob, user1.getDob());
		Assert.assertEquals("abc@xyz.com", user1.getEmail());		
		Assert.assertNotNull(user1.getCreatedTs());
		Assert.assertNotNull(user1.getLastUpdatedTs());
		Assert.assertNotNull(user1.getLastLoginTs());
		Assert.assertEquals(new Boolean(true), user1.getActive());
		userDao.deleteUser(user1.getUserid());
	}

	@Test
	public void testGetUserInformationByUserid() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		User user1 = usMgr.getUserInformation(user.getUserid());
		Assert.assertNotNull(user1);
		userDao.deleteUser(user1.getUserid());
	}
	
	@Test
	public void testGetUserInformationByEmail() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		User user1 = usMgr.getUserInformationByEmail(user.getEmail());
		Assert.assertNotNull(user1);
		userDao.deleteUser(user1.getUserid());
	}
	
	@Test
	public void testSaveUserInformation() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		
		user.setMiddleName("John");		
		usMgr.saveUserInformation(user);
		
		User user1 = usMgr.getUserInformation(user.getUserid());
		Assert.assertEquals("John",user1.getMiddleName());		
		userDao.deleteUser(user1.getUserid());
		user.setMiddleName("Bob");
	}
	
	@Test
	public void testAuthenticateRequest() throws Exception{
		
		/*Get App Access Token from facebook*/
	    String getAppAccessTokenUrl = "https://graph.facebook.com/oauth/access_token?client_id="+appID+"&client_secret=bd8fa4961cb1c2a284cbe8486707b73a&grant_type=client_credentials";
		ClientConfig clientConfigGetAppAccessToken = new DefaultClientConfig();
		clientConfigGetAppAccessToken.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientGetAppToken = Client.create(clientConfigGetAppAccessToken);
		WebResource getAppAccessToken = clientGetAppToken.resource(getAppAccessTokenUrl);
		String appAccessTokenResponse = getAppAccessToken.get(String.class);
		String[] appAccessToken = appAccessTokenResponse.split("=");
		String appAccessTokenValue = appAccessToken[1];

	
		/* 1. Get test user from facebook*/
		String getFacebookTestUserUrl = "https://graph.facebook.com/"+appID+"/accounts/test-users?installed=true&permissions=email&method=post&access_token="+URLEncoder.encode(appAccessTokenValue,"ISO-8859-1");
		ClientConfig clientConfigGetFacebookTestUser = new DefaultClientConfig();
		clientConfigGetFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientGetFacebookTestUser = Client.create(clientConfigGetFacebookTestUser);
		WebResource getFacebookTestUser = clientGetFacebookTestUser.resource(getFacebookTestUserUrl);
		JSONObject FacebookTestUser = getFacebookTestUser.type(MediaType.APPLICATION_FORM_URLENCODED).post(JSONObject.class);
	
		String userID = FacebookTestUser.getString("id");
		String userAccessToken = FacebookTestUser.getString("access_token");
		String userEmail = FacebookTestUser.getString("email");
		
		User userFb = new User();
		userFb.setUserid(userID);
		userFb.setAccessToken(userAccessToken);
		userFb.setAccessTokenType("facebook");
		userFb.setEmail(userEmail);
		userFb.setFirstName("Alice");
		userFb.setMiddleName("Bob");
		userFb.setLastName("Charlie");
				
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(userFb);
		
		String fakeAccessToken = "CAACEdEose0cBAErbkQ3pVP8p9AZCSMrR6JeuaTlSZADrgeyf9jHnWUUhKOezuC5Jh04VFUCvqGEOFZCohorOZAjFK7608GZAziXv1l3z4utpX9eSyjeP0PMtv10sbZCstKhlCDnilhllZC92d3S16eS2UtwbGHu9eoZD"; 
		AuthenticationStatus authStatus = userManagerMySQLImpl.authenticateRequest(fakeAccessToken, User.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatus.Status.DENIED.getValue(), authStatus.getAuthenticationStatus());
		Assert.assertNull(authStatus.getAuthenticatedUser());

		authStatus = userManagerMySQLImpl.authenticateRequest(userAccessToken, User.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatus.Status.APPROVED.getValue(), authStatus.getAuthenticationStatus());
		Assert.assertNotNull(authStatus.getAuthenticatedUser());
		
		/*Delete Facebook TestUser*/
		String DeleteFbTestUserUrl = "https://graph.facebook.com/"+userID+"?method=delete&access_token="+userAccessToken;
		ClientConfig clientConfigDeleteFacebookTestUser = new DefaultClientConfig();
		clientConfigDeleteFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientDeleteFacebookTestUser = Client.create(clientConfigDeleteFacebookTestUser);
		WebResource deleteFacebookTestUser = clientDeleteFacebookTestUser.resource(DeleteFbTestUserUrl);
		deleteFacebookTestUser.post();
		userDao.deleteUser(userFb.getUserid());

		
		/*
		 *  now user the test access_token and id to revoke the email permissions and test the authenticateRequest method
		 *  a request without a valid email permission is a not successfully authenticated
		*/

		/* 2. Get test user from facebook */
		getFacebookTestUserUrl = "https://graph.facebook.com/"+appID+"/accounts/test-users?installed=true&permissions=email&method=post&access_token="+URLEncoder.encode(appAccessTokenValue,"ISO-8859-1");
		clientConfigGetFacebookTestUser = new DefaultClientConfig();
		clientConfigGetFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		clientGetFacebookTestUser = Client.create(clientConfigGetFacebookTestUser);
		getFacebookTestUser = clientGetFacebookTestUser.resource(getFacebookTestUserUrl);
		FacebookTestUser = getFacebookTestUser.type(MediaType.APPLICATION_FORM_URLENCODED).post(JSONObject.class);
	
		String accesstoken = FacebookTestUser.getString("access_token"); 
		
		WebResource deleteEmailPermission = clientGetFacebookTestUser.resource(
				"https://graph.facebook.com/"
				+ FacebookTestUser.getString("id")
				+ "/permissions/email?access_token=" 
				+ URLEncoder.encode(accesstoken,"ISO-8859-1")
		);
		
		deleteEmailPermission.type(MediaType.TEXT_PLAIN_TYPE).delete(String.class);
		
		WebResource getFacebookTestUserNoEmail = clientGetFacebookTestUser.resource(
				"https://graph.facebook.com/"
				+ FacebookTestUser.getString("id") 
				+ "?fields=id,name,email&access_token=" 
				+ URLEncoder.encode(accesstoken,"ISO-8859-1")
		);

		JSONObject FacebookTestUserNoEmail = getFacebookTestUserNoEmail.type(MediaType.APPLICATION_FORM_URLENCODED).get(JSONObject.class);
				
		userID = FacebookTestUser.getString("id");
		userAccessToken = FacebookTestUser.getString("access_token");

		if (FacebookTestUserNoEmail.has("email") == false ) {
			userEmail = "";
		}
		
		userFb = new User();
		userFb.setUserid(userID);
		userFb.setAccessToken(userAccessToken);
		userFb.setAccessTokenType("facebook");
		userFb.setEmail(userEmail);
		userFb.setFirstName("Alice");
		userFb.setMiddleName("Bob");
		userFb.setLastName("Charlie");
		
		authStatus = userManagerMySQLImpl.authenticateRequest(userAccessToken, User.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatus.Status.EMAIL_REQUIRED.getValue(), authStatus.getAuthenticationStatus());
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
		/*Delete Facebook TestUser*/
		DeleteFbTestUserUrl = "https://graph.facebook.com/"+userID+"?method=delete&access_token="+userAccessToken;
		clientConfigDeleteFacebookTestUser = new DefaultClientConfig();
		clientConfigDeleteFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		clientDeleteFacebookTestUser = Client.create(clientConfigDeleteFacebookTestUser);
		deleteFacebookTestUser = clientDeleteFacebookTestUser.resource(DeleteFbTestUserUrl);
		deleteFacebookTestUser.post();
		userDao.deleteUser(userFb.getUserid());

	}	
}
