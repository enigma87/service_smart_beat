package com.genie.account.mgmt.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.account.mgmt.beans.User;

/**
 * @author dhasarathy
 **/

public class UserDaoTest {

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
		
		userDao.createUser(user);
		
		User user1 = userDao.getUserInfo(user.getUserid());
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
	public void testUpdateUser() {
		
		userDao.createUser(user);
		user.setMiddleName("John");
		user.setFacebookLogin(false);
		userDao.updateUser(user);
		
		User user1 = userDao.getUserInfo(user.getUserid());
		Assert.assertEquals("John", user1.getMiddleName());
		Assert.assertEquals(new Boolean(false), user1.getFacebookLogin());
		userDao.deleteUser(user.getUserid());
	}
	
	@Test
	public void testGetUserInfoByEmail() {
		
		userDao.createUser(user);
		User user = userDao.getUserInfoByEmail("abc@xyz.com");
		Assert.assertNotNull(user);
		userDao.deleteUser(user.getUserid());
	
	}

	@Test
	public void testGetUserInfoByUserid() {
		userDao.createUser(user);
		User user = userDao.getUserInfo("123456789");
		Assert.assertNotNull(user);
		userDao.deleteUser(user.getUserid());
	}
	

}

