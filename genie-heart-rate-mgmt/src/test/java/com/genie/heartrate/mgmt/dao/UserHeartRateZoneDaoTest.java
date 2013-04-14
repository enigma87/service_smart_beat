/**
 * 
 */
package com.genie.heartrate.mgmt.dao;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
import com.genie.heartrate.mgmt.core.HeartRateMgmt;
import com.genie.heartrate.mgmt.impl.HeartRateMgmtMySQLImpl;

/**
 * @author manojkumar
 *
 */
public class UserHeartRateZoneDaoTest 
{

	private ApplicationContext appContext;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateZoneDao#getHeartRateZone(java.lang.Long)}.
	 */
	@Test
	public void testGetHeartRateZone() 
	{
		
		HeartRateMgmt heartRateMgmt = (HeartRateMgmtMySQLImpl) appContext.getBean("heartRateMgmtMySQLImpl");
		Assert.assertNotNull(heartRateMgmt);
		
		UserHeartRateZone heartRateZone = heartRateMgmt.getHeartRateZonesForUser(Long.parseLong("1000"));
		Assert.assertNotNull(heartRateZone);
		System.out.println(heartRateZone.getHrz1Start());
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateZoneDao#createHeartRateZone(com.genie.heartrate.mgmt.beans.UserHeartRateZone)}.
	 */
	@Test
	public void testCreateHeartRateZone() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateZoneDao#updateHeartRateZone(com.genie.heartrate.mgmt.beans.UserHeartRateZone)}.
	 */
	@Test
	public void testUpdateHeartRateZone() {
		fail("Not yet implemented");
	}

}
