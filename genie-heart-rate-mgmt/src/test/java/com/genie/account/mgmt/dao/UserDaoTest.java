package com.genie.account.mgmt.dao;

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
	private static User kattoorDada;
	
	@BeforeClass
	public static void setupUserDaoTestCases(){
		applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		userDao = (UserDao)applicationContext.getBean("userDao");
		kattoorDada = new User();
		kattoorDada.setUserid(1000L);
		kattoorDada.setFirstName("Kattoor");		
		kattoorDada.setLastName("Dada");
		kattoorDada.setEmail("kattoor.dada@email.com");
	}
	
	@Test
	public void testCreateUser() {
		userDao.createUser(kattoorDada);
	}
	
	@Test
	public void testUpdateUser() {			    	    
		kattoorDada.setMiddleName("Motham");
		userDao.updateUser(kattoorDada);
	}
	
	@Test
	public void testGetUserInfoString() {
		User user = userDao.getUserInfo("kattoor.dada@email.com");
		System.out.println(user.getFirstName());
		System.out.println(user.getMiddleName());
	}

	@Test
	public void testGetUserInfoLong() {
		User user = userDao.getUserInfo(1000L);
		System.out.println(user.getLastName());
	}
	
	@AfterClass
	public static void teardownUserDaoTestCases(){
		userDao.deleteUser(kattoorDada.getUserid());
	}
}

