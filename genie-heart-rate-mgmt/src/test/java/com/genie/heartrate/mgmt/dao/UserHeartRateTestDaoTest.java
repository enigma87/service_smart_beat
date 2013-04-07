/**
 * 
 */
package com.genie.heartrate.mgmt.dao;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author manojkumar
 *
 */
public class UserHeartRateTestDaoTest 
{

	private ApplicationContext appContext;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUpBeforeClass() throws Exception 
	{
		appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateTestDao#getHeartRateTestResults(java.lang.Long)}.
	 */
	@Test
	public void testGetHeartRateTestResults() 
	{
		
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateTestDao#createHeartRateTestResults(com.genie.heartrate.mgmt.beans.UserHeartRateTest)}.
	 */
	@Test
	public void testCreateHeartRateTestResults() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateTestDao#updateHeartRateTestResults(com.genie.heartrate.mgmt.beans.UserHeartRateTest)}.
	 */
	@Test
	public void testUpdateHeartRateTestResults() {
		fail("Not yet implemented");
	}

}
