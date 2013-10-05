package com.genie.social.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.social.beans.UserBean;
import com.genie.social.core.AuthenticationStatus;
import com.genie.social.core.UserManager;
import com.genie.social.dao.UserDao;
import com.genie.social.facebook.GraphAPI;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
/**
 * @author vidhun
 *
 */

public class UserManagerMySQLImplTest {

	private static AbstractApplicationContext applicationContext;
	private static UserDao userDao;
	private static UserManager userManagerMySQLImpl;
	private static UserBean user;
	private static Date Dob = null;
	private static Timestamp timestamp ;	
	
	@BeforeClass
	public static void setupUserDaoTestCases(){
		applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		applicationContext.registerShutdownHook();
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

		user = new UserBean();
		user.setUserid("123456789");
		user.setAccessToken("access_token_123456789");
		user.setAccessTokenType(UserBean.ACCESS_TOKEN_TYPE_CUSTOM);
		user.setFirstName("Alice");
		user.setMiddleName("Bob");
		user.setLastName("CampBell");
		user.setDob(Dob);
		user.setGender(UserManager.GENDER_FEMALE);
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
		
		UserBean user1 = usMgr.getUserInformation(user.getUserid());
		Assert.assertEquals("123456789", user1.getUserid());
		user.setAccessToken("access_token_123456789");
		user.setAccessTokenType(UserBean.ACCESS_TOKEN_TYPE_CUSTOM);
		Assert.assertEquals("Alice", user1.getFirstName());
		Assert.assertEquals("Bob", user1.getMiddleName());
		Assert.assertEquals("CampBell", user1.getLastName());
		Assert.assertEquals(Dob, user1.getDob());
		Assert.assertEquals("abc@xyz.com", user1.getEmail());		
		Assert.assertNotNull(user1.getCreatedTs());
		Assert.assertNotNull(user1.getLastUpdatedTs());
		Assert.assertNotNull(user1.getLastLoginTs());
		Assert.assertEquals(new Boolean(true), user1.getActive());
		usMgr.deleteUserById(user1.getUserid());
	}

	@Test
	public void testGetUserInformationByUserid() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		UserBean user1 = usMgr.getUserInformation(user.getUserid());
		Assert.assertNotNull(user1);
		usMgr.deleteUserById(user1.getUserid());
	}
	
	@Test
	public void testGetUserInformationByEmail() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		UserBean user1 = usMgr.getUserInformationByEmail(user.getEmail());
		Assert.assertNotNull(user1);
		usMgr.deleteUserById(user1.getUserid());
	}
	
	@Test
	public void testSaveUserInformation() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		
		user.setMiddleName("John");		
		usMgr.saveUserInformation(user);
		
		UserBean user1 = usMgr.getUserInformation(user.getUserid());
		Assert.assertEquals("John",user1.getMiddleName());		
		usMgr.deleteUserById(user1.getUserid());
		user.setMiddleName("Bob");
	}
	
	//@Test
	public void testAuthenticateRequest() throws Exception{
		/*
		 * 1. Get test user from facebook and verify if valid/invalid accessToken grants appropriate access permissions
		 */
		UserBean userFb = GraphAPI.getTestUser();
		userFb.setAccessTokenType("facebook");
		userFb.setFirstName("Achilles");
				
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		
		AuthenticationStatus authStatus;
		
		//case 1.a : new user
		authStatus = userManagerMySQLImpl.authenticateRequest(userFb.getAccessToken(), UserBean.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatus.Status.DENIED.equals(authStatus.getAuthenticationStatus()), true);
		Assert.assertNotNull(authStatus.getAuthenticatedUser());
		
		// case 1.b user exists
		usMgr.registerUser(userFb);
		userDao.updateUser(userFb);
		AuthenticationStatus fbAuthStatus = new AuthenticationStatus();
		fbAuthStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
		GraphAPI.determineUserAuthenticationStatus(fbAuthStatus,userFb.getAccessToken());
		authStatus = userManagerMySQLImpl.authenticateRequest(userFb.getAccessToken(), UserBean.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(fbAuthStatus.getAuthenticationStatus(), authStatus.getAuthenticationStatus());
		Assert.assertNotNull(authStatus.getAuthenticatedUser());

		// case 1.c
		String fakeAccessToken = "CAACEdEose0cBAErbkQ3pVP8p9AZCSMrR6JeuaTlSZADrgeyf9jHnWUUhKOezuC5Jh04VFUCvqGEOFZCohorOZAjFK7608GZAziXv1l3z4utpX9eSyjeP0PMtv10sbZCstKhlCDnilhllZC92d3S16eS2UtwbGHu9eoZD"; 
		authStatus = userManagerMySQLImpl.authenticateRequest(fakeAccessToken, UserBean.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatus.Status.DENIED.equals(authStatus.getAuthenticationStatus()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());
		GraphAPI.deleteTestUser(userFb);
		userDao.deleteUser(userFb.getUserid());
		
		/*
		 *  2. Get test user from facebook: now user the test access_token and id to revoke the email permissions and test the authenticateRequest method
		 *  a request without a valid email permission is a not successfully authenticated
		*/
		userFb = GraphAPI.getTestUser();
		userFb.setAccessTokenType("facebook");
		userFb.setFirstName("Hector");
		
		FacebookClient fbClient = new DefaultFacebookClient(userFb.getAccessToken());
		fbClient.deleteObject(userFb.getUserid() + "/permissions/email");
		// case 2.a
		authStatus = userManagerMySQLImpl.authenticateRequest(userFb.getAccessToken(), UserBean.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatus.Status.DENIED_EMAIL_REQUIRED.equals(authStatus.getAuthenticationStatus()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
		GraphAPI.deleteTestUser(userFb);
		
	}
	
	@Test
	public void testDeleteUserById(){
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		
		UserBean userBean = usMgr.getUserInformation(user.getUserid());
		Assert.assertNotNull(userBean);
		usMgr.deleteUserById(user.getUserid());
		userBean = usMgr.getUserInformation(user.getUserid());
		Assert.assertNull(userBean);
	}
}
