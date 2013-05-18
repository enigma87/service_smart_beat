package com.genie.account.mgmt.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.core.UserManager;
import com.genie.account.mgmt.dao.UserDao;
import com.genie.account.mgmt.util.FacebookGraphAPIResponseJSON;
import com.genie.account.mgmt.util.RegisterRequestJSON;
/**
 * @author vidhun
 *
 */

public class UserManagerMySQLImplTest {

	private static ApplicationContext applicationContext;
	private static UserDao userDao;
	private static User user;
	private static Date Dob = null;
	private static Timestamp timestamp ;
	
	@BeforeClass
	public static void setupUserDaoTestCases(){
		applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		userDao = (UserDao)applicationContext.getBean("userDao");
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
		user.setFirstName("Alice");
		user.setMiddleName("Bob");
		user.setLastName("CampBell");
		user.setDob(Dob);
		user.setEmail("abc@xyz.com");
		user.setFacebookLogin(true);
		user.setGoogleLogin(false);
		user.setTwitterLogin(true);
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
		Assert.assertEquals("Alice", user1.getFirstName());
		Assert.assertEquals("Bob", user1.getMiddleName());
		Assert.assertEquals("CampBell", user1.getLastName());
		Assert.assertEquals(Dob, user1.getDob());
		Assert.assertEquals("abc@xyz.com", user1.getEmail());
		Assert.assertEquals(new Boolean(true), user1.getFacebookLogin());
		Assert.assertEquals(new Boolean(false), user1.getGoogleLogin());
		Assert.assertEquals(new Boolean(true), user1.getTwitterLogin());
		Assert.assertEquals("www.picasa.com/1002", user1.getImageUrl());
		Assert.assertNotNull(user1.getCreatedTs());
		Assert.assertNotNull(user1.getLastUpdatedTs());
		Assert.assertNotNull(user1.getLastLoginTs());
		Assert.assertEquals(new Boolean(true), user1.getActive());
		userDao.deleteUser(user.getUserid());
	}

	@Test
	public void testGetUserInformationByUserid() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		User user1 = usMgr.getUserInformation(user.getUserid());
		Assert.assertNotNull(user1);
		userDao.deleteUser(user.getUserid());
	}
	
	@Test
	public void testGetUserInformationByEmail() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		User user1 = usMgr.getUserInformationByEmail(user.getEmail());
		Assert.assertNotNull(user1);
		userDao.deleteUser(user.getUserid());
	}
	
	@Test
	public void testSaveUserInformation() {
		
		UserManager usMgr = new UserManagerMySQLImpl();
		if(usMgr instanceof UserManagerMySQLImpl){}
		((UserManagerMySQLImpl)usMgr).setUserDao(userDao);
		usMgr.registerUser(user);
		
		user.setMiddleName("John");
		user.setFacebookLogin(false);
		usMgr.saveUserInformation(user);
		
		User user1 = usMgr.getUserInformation(user.getUserid());
		Assert.assertEquals("John",user1.getMiddleName());
		Assert.assertEquals(new Boolean(false),user1.getFacebookLogin());
		userDao.deleteUser(user.getUserid());
	}
	
	@Test
	public void testAuthenticateUser(){
		
		UserManager usMgr = new UserManagerMySQLImpl();
		RegisterRequestJSON requestJson = new RegisterRequestJSON();
		requestJson.setAccessToken("CAACEdEose0cBAEJubAysZCKUmqbjBVVhyyPgRmpUmpxxEZBOtZCa104c5SIFY1o7ItdPB0oZBP9lcsNmAmf1hFZCneFJSs7xF3LX4cKYe5JWeoS2wJJPLpE4ZAWGYeTqlk4uPrYDaew3Umx9ZAgMgBnCKzInHlM8dYhWHZCRuZC6uKAZDZD");
		requestJson.setAccessTokenType("facebook");
		FacebookGraphAPIResponseJSON responseJson = usMgr.authenticateUser(requestJson);
        System.out.print("The ID is "+ responseJson.getId());
        System.out.print("The Name is "+responseJson.getName());
		
	}
}
