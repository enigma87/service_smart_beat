package com.genie.account.mgmt.dao;
import static org.junit.Assert.*;
import junit.framework.Assert;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.dao.UserDao;

/**
 * 
 */

/**
 * @author manojkumar
 *
 */
public class UserDaoTest 
{

	private static BasicDataSource dataSource; 
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/genie");
		dataSource.setUsername("genie");
		dataSource.setPassword("genie");
	}

	/**
	 * Test method for {@link com.genie.account.mgmt.dao.UserDao#getUserInfo(java.lang.String)}.
	 */
	@Test
	public void testGetUserInfo() 
	{
		UserDao userDao = new UserDao();
		userDao.setDataDource(dataSource);
		User user = userDao.getUserInfo("manoj1987@gmail.com");
		Assert.assertNotNull(user);
	}

}
