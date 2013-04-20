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

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
import com.genie.heartrate.mgmt.core.HeartRateMgmt;
import com.genie.heartrate.mgmt.impl.HeartRateMgmtMySQLImpl;


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
	//@Test
	public void testGetHeartRateTestResults() 
	{
		HeartRateMgmt heartRateMgmt = (HeartRateMgmtMySQLImpl) appContext.getBean("heartRateMgmtMySQLImpl");
		Assert.assertNotNull(heartRateMgmt);
		
		UserHeartRateTest uhrt = heartRateMgmt.getHeartRateTestResultsForUser(Long.parseLong("1000"));
		Assert.assertNotNull(uhrt);
		System.out.println(uhrt.getRestingHeartRate());
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateTestDao#createHeartRateTestResults(com.genie.heartrate.mgmt.beans.UserHeartRateTest)}.
	 */
	//@Test
	public void testCreateHeartRateTestResults() {
		HeartRateMgmt heartRateMgmt = (HeartRateMgmtMySQLImpl) appContext.getBean("heartRateMgmtMySQLImpl");
		Assert.assertNotNull(heartRateMgmt);
		
		UserHeartRateTest uhrt = heartRateMgmt.getHeartRateTestResultsForUser(Long.parseLong("1001"));
		Assert.assertNull(uhrt);
		uhrt = new UserHeartRateTest();
		uhrt.setUserid(1001L);
		uhrt.setRestingHeartRate(50);
		heartRateMgmt.saveHeartRateTestResultsForUser(uhrt);
		uhrt = heartRateMgmt.getHeartRateTestResultsForUser(Long.parseLong("1001"));
		Assert.assertNotNull(uhrt);
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateTestDao#updateHeartRateTestResults(com.genie.heartrate.mgmt.beans.UserHeartRateTest)}.
	 */
	@Test
	public void testUpdateHeartRateTestResults() {
		HeartRateMgmt heartRateMgmt = (HeartRateMgmtMySQLImpl) appContext.getBean("heartRateMgmtMySQLImpl");
		Assert.assertNotNull(heartRateMgmt);
		
		UserHeartRateTest uhrt = heartRateMgmt.getHeartRateTestResultsForUser(Long.parseLong("1000"));
		Assert.assertNotNull(uhrt);
		uhrt.setRestingHeartRate(51);
		heartRateMgmt.saveHeartRateTestResultsForUser(uhrt);
	}

}
