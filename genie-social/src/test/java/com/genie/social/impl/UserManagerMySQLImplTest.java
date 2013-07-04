package com.genie.social.impl;

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

import com.genie.social.beans.User;
import com.genie.social.core.AuthenticationStatus;
import com.genie.social.core.AuthenticationStatusCode;
import com.genie.social.core.UserManager;
import com.genie.social.dao.UserDao;
import com.genie.social.facebook.GraphAPI;
import com.genie.social.impl.UserManagerMySQLImpl;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.WebRequestor;
import com.restfb.WebRequestor.Response;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
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
		/*
		 * 1. Get test user from facebook and verify if valid/invalid accessToken grants appropriate access permissions
		 */
		GraphAPI graphAPI = new GraphAPI();
		
		User userFb = graphAPI.getTestUser();
		userFb.setAccessTokenType("facebook");
		userFb.setFirstName("Achilles");
				
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(userFb);
		
		AuthenticationStatus authStatus;
		
		//case 1.a
		authStatus = userManagerMySQLImpl.authenticateRequest(userFb.getAccessToken(), User.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatusCode.APPROVED.intValue(), authStatus.getAuthenticationStatusCode());
		Assert.assertNotNull(authStatus.getAuthenticatedUser());
		
		// case 1.b user exists and not cached
		String copyAccessToken = userFb.getAccessToken();
		userFb.setAccessToken("");
		userDao.updateUser(userFb);
		authStatus = userManagerMySQLImpl.authenticateRequest(copyAccessToken, User.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatusCode.APPROVED.intValue(), authStatus.getAuthenticationStatusCode());
		Assert.assertNotNull(authStatus.getAuthenticatedUser());

		// case 1.c
		String fakeAccessToken = "CAACEdEose0cBAErbkQ3pVP8p9AZCSMrR6JeuaTlSZADrgeyf9jHnWUUhKOezuC5Jh04VFUCvqGEOFZCohorOZAjFK7608GZAziXv1l3z4utpX9eSyjeP0PMtv10sbZCstKhlCDnilhllZC92d3S16eS2UtwbGHu9eoZD"; 
		authStatus = userManagerMySQLImpl.authenticateRequest(fakeAccessToken, User.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatusCode.DENIED.intValue(), authStatus.getAuthenticationStatusCode());
		Assert.assertNull(authStatus.getAuthenticatedUser());

		System.out.println(userFb.getUserid());
		graphAPI.deleteTestUser(userFb);
		userDao.deleteUser(userFb.getUserid());
		
		/*
		 *  2. Get test user from facebook: now user the test access_token and id to revoke the email permissions and test the authenticateRequest method
		 *  a request without a valid email permission is a not successfully authenticated
		*/
		userFb = graphAPI.getTestUser();
		userFb.setAccessTokenType("facebook");
		userFb.setFirstName("Hector");
		
		WebRequestor webRequestor = new DefaultWebRequestor();
		
		FacebookClient fbClient = new DefaultFacebookClient(userFb.getAccessToken());
		boolean deleted = fbClient.deleteObject(userFb.getUserid() + "/permissions/email");
		com.restfb.types.User facebookUser = fbClient.fetchObject("me", com.restfb.types.User.class);
		
		// case 2.a
		authStatus = userManagerMySQLImpl.authenticateRequest(userFb.getAccessToken(), User.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatusCode.DENIED_EMAIL_REQUIRED.intValue(), authStatus.getAuthenticationStatusCode());
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
		graphAPI.deleteTestUser(userFb);
		
	}	
}
