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
import com.genie.account.mgmt.util.AuthenticationStatus;
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
		usMgr.saveUserInformation(user);
		
		User user1 = usMgr.getUserInformation(user.getUserid());
		Assert.assertEquals("John",user1.getMiddleName());		
		userDao.deleteUser(user.getUserid());
	}
	
	@Test
	public void testAuthenticateRequest(){
		String accessToken = "CAACEdEose0cBAErbkQ3pVP8p9AZCSMrR6JeuaTlSZADrgeyf9jHnWUUhKOezuC5Jh04VFUCvqGEOFZCohorOZAjFK7608GZAziXv1l3z4utpX9eSyjeP0PMtv10sbZCstKhlCDnilhllZC92d3S16eS2UtwbGHu9eoZD"; 
		AuthenticationStatus authStatus = userManagerMySQLImpl.authenticateRequest(accessToken, User.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatus.AUTHENTICATION_STATUS_DENIED, authStatus.getAuthenticationStatus());
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
		accessToken = "CAACEdEose0cBAO62RBR8UwvLx9nFXyLYuZBFsSFlEOrX31YyYWObBx5Y3lDVhVrEPK1iPZB6uIxXqNWBv6NxfZCWKAoqc6dYZCDI6JWxEMtVPvAula0FGkVGRgOQg6DtBNB3SZBGxBcrNPAUa2TIXrZCOktBvg4DYZD"; 
		authStatus = userManagerMySQLImpl.authenticateRequest(accessToken, User.ACCESS_TOKEN_TYPE_FACEBOOK);
		Assert.assertEquals(AuthenticationStatus.AUTHENTICATION_STATUS_APPROVED, authStatus.getAuthenticationStatus());
		Assert.assertNotNull(authStatus.getAuthenticatedUser());
		
		
	}
}
