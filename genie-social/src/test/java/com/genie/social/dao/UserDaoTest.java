package com.genie.social.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.social.beans.UserBean;
import com.genie.social.beans.UserIdBean;
import com.genie.social.core.UserManager;

/**
 * @author dhasarathy
 **/

public class UserDaoTest {

	private static AbstractApplicationContext applicationContext;
	private static UserDao userDao;
	private static UserBean user;
	private static Date Dob = null;
	private static Timestamp timestamp ;


	
	@BeforeClass
	public static void setupUserDaoTestCases(){
		applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		applicationContext.registerShutdownHook();
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
		user.setGender(UserManager.GENDER_FEMALE);
		user.setDob(Dob);
		user.setEmail("abc@xyz.com");		
		user.setImageUrl("www.picasa.com/1002");
		user.setCreatedTs(timestamp);
		user.setLastUpdatedTs(timestamp);
		user.setLastLoginTs(timestamp);
		user.setActive(true);
		user.setPrivilegeLevel((byte) 1);
	}
	
	@Test
	public void testCreateUser() {
		
		userDao.createUser(user);	
	
		UserBean user1 = userDao.getUserInfo(user.getUserid());
		Assert.assertEquals("123456789", user1.getUserid());		
		Assert.assertEquals("Katoor", user1.getFirstName());
		Assert.assertEquals("Dada", user1.getLastName());
		Assert.assertEquals(Dob, user1.getDob());
		Assert.assertEquals(UserManager.GENDER_FEMALE, user.getGender());
		Assert.assertEquals("abc@xyz.com", user1.getEmail());		
		Assert.assertEquals("www.picasa.com/1002", user1.getImageUrl());
		Assert.assertNotNull(user1.getCreatedTs());
		Assert.assertNotNull(user1.getLastUpdatedTs());
		Assert.assertNotNull(user1.getLastLoginTs());
		Assert.assertEquals(new Boolean(true), user1.getActive());
		Assert.assertEquals(1, user1.getPrivilegeLevel().byteValue());
		
		userDao.deleteUser(user.getUserid());
		
		Assert.assertEquals(0, userDao.createUser(new UserBean()));
	}
	
	@Test
	public void testUpdateUser() {
		UserBean user1 = user.clone();
		userDao.createUser(user1);
		user1.setMiddleName("John");		
		user1.setPrivilegeLevel((byte) 2);
		userDao.updateUser(user1);
		
		UserBean user2 = userDao.getUserInfo(user1.getUserid());
		Assert.assertEquals("John", user2.getMiddleName());
		Assert.assertEquals(2, user2.getPrivilegeLevel().byteValue());
		userDao.deleteUser(user1.getUserid());
		
		Assert.assertEquals(0, userDao.updateUser(new UserBean()));
		user1.setAccessToken("");
		Assert.assertEquals(0, userDao.updateUser(user1));
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

	@Test
	public void testDeleteUser() {
		userDao.createUser(user);
		userDao.deleteUser("12345");
		Assert.assertEquals(user.getUserid(), userDao.getUserInfo(user.getUserid()).getUserid());
		userDao.deleteUser(user.getUserid());
		Assert.assertEquals(null, userDao.getUserInfo(user.getUserid()));
	}
	
	@Test
	public void testIsExistingUser(){
		boolean isExistingUser = false;
		isExistingUser = userDao.isExistingUser("abc@xyz.com");
		Assert.assertFalse(isExistingUser);
		userDao.createUser(user);
		isExistingUser = userDao.isExistingUser("abc@xyz.com");
		Assert.assertTrue(isExistingUser);
		userDao.deleteUser(user.getUserid());
	}	
	
	@Test
	public void testGetUserIds(){
		
		userDao.createUser(user);
		
		
		
		UserBean user1 = new UserBean();
		user1.setUserid("abc123");
		user1.setFirstName("Chithra");
		user1.setEmail("chithra@xyz.com");
		user1.setAccessToken("2222222222222222222");
		user1.setAccessTokenType("facebook");
		userDao.createUser(user1);
		
		user1.setUserid("xyz789");
		user1.setFirstName("Suresh");
		user1.setEmail("suresh@xyz.com");
		user1.setAccessToken("3333333333333333333");
		user1.setAccessTokenType("facebook");
		userDao.createUser(user1);
		
		List<UserIdBean> userIds = userDao.getUserIds();
		Assert.assertEquals(3,userIds.size());
		Assert.assertEquals("123456789", userIds.get(0).getUserid());
		Assert.assertEquals("abc123", userIds.get(1).getUserid());
		Assert.assertEquals("xyz789", userIds.get(2).getUserid());
		
		userDao.deleteUser(user.getUserid());
		userDao.deleteUser("abc123");
		userDao.deleteUser("xyz789");
	}
}

