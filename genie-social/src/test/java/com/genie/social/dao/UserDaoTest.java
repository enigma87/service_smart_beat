package com.genie.social.dao;

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

import com.genie.social.beans.UserBean;
import com.genie.social.dao.UserDao;

/**
 * @author dhasarathy
 **/

public class UserDaoTest {

	private static ApplicationContext applicationContext;
	private static UserDao userDao;
	private static UserBean user;
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

		user = new UserBean();
		user.setUserid("123456789");
		user.setAccessToken("CAACEdEose0cBANFZBcZAyzn7agEZAOzlEKr75c6hSppL969nNNS9wFwxxFHJwp48vi2G884onYxxqZBuwVNsC4BqVUfwZAIUZCrXvNGv2yvjXdBnxZCbfMMDRtDbw2XVhpQGLOEr1wlIJHBGsBwcKY9M72Q4PO9loMZD");
		user.setAccessTokenType(UserBean.ACCESS_TOKEN_TYPE_FACEBOOK);
		user.setFirstName("Katoor");
		user.setMiddleName("Motham");
		user.setLastName("Dada");
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
		
		userDao.createUser(user);
		
		UserBean user1 = userDao.getUserInfo(user.getUserid());
		Assert.assertEquals("123456789", user1.getUserid());		
		Assert.assertEquals("Katoor", user1.getFirstName());
		Assert.assertEquals("Motham", user1.getMiddleName());
		Assert.assertEquals("Dada", user1.getLastName());
		Assert.assertEquals(Dob, user1.getDob());
		Assert.assertEquals("abc@xyz.com", user1.getEmail());		
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
		userDao.updateUser(user);
		
		UserBean user1 = userDao.getUserInfo(user.getUserid());
		Assert.assertEquals("John", user1.getMiddleName());
		userDao.deleteUser(user.getUserid());
	}
	
	@Test
	public void testGetUserInfoByEmail() {
		
		userDao.createUser(user);
		UserBean user = userDao.getUserInfoByEmail("abc@xyz.com");
		Assert.assertNotNull(user);
		userDao.deleteUser(user.getUserid());
	
	}

	@Test
	public void testGetUserInfoByUserid() {
		userDao.createUser(user);
		UserBean user = userDao.getUserInfo("123456789");
		Assert.assertNotNull(user);
		userDao.deleteUser(user.getUserid());
	}
	
	@Test
	public void testGetUserInfoByAccessToken(){
		String accessToken = "CAACEdEose0cBANFZBcZAyzn7agEZAOzlEKr75c6hSppL969nNNS9wFwxxFHJwp48vi2G884onYxxqZBuwVNsC4BqVUfwZAIUZCrXvNGv2yvjXdBnxZCbfMMDRtDbw2XVhpQGLOEr1wlIJHBGsBwcKY9M72Q4PO9loMZD";
		UserBean testUser = userDao.getUserInfoByAccessToken(accessToken);
		Assert.assertNull(testUser);
		userDao.createUser(user);
		testUser = userDao.getUserInfoByAccessToken(accessToken);
		Assert.assertEquals(user.getUserid(), testUser.getUserid());
		Assert.assertEquals(accessToken, testUser.getAccessToken());
		userDao.deleteUser(testUser.getUserid());
	}
	

}

