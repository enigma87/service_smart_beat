/**
 * 
 */
package com.genie.heartrate.mgmt.dao;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.UserHeartRateZone;

/**
 * @author manojkumar
 *
 */
public class UserHeartRateZoneDaoTest 
{

	private ApplicationContext appContext;
	private UserHeartRateZoneDao userHeartRateZoneDao;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		userHeartRateZoneDao = (UserHeartRateZoneDao) appContext.getBean("userHeartRateZoneDao");
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateZoneDao#getHeartRateZone(java.lang.Long)}.
	 */
	@Test
	public void testGetHeartRateZone() 
	{
		
		
		Assert.assertNotNull(userHeartRateZoneDao);
		
		UserHeartRateZone heartRateZone = userHeartRateZoneDao.getHeartRateZone("123456789");
		Assert.assertNotNull(heartRateZone);
		
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateZoneDao#createHeartRateZone(com.genie.heartrate.mgmt.beans.UserHeartRateZone)}.
	 */
	@Test
	public void testCreateHeartRateZone() {
		
		Assert.assertNotNull(userHeartRateZoneDao);
		
		UserHeartRateZone uhrz = new UserHeartRateZone();
		uhrz.setUserid("123456789");
		uhrz.setHrz1Start(50.0);
		uhrz.setHrz1End(90.0);
		uhrz.setHrz2Start(90.1);
		uhrz.setHrz2End(113.0);
		uhrz.setHrz3Start(113.1);
		uhrz.setHrz3End(124.0);
		uhrz.setHrz4Start(124.1);
		uhrz.setHrz4End(132.0);
		uhrz.setHrz5Start(132.1);
		uhrz.setHrz5End(150.0);
		uhrz.setHrz6Start(150.1);
		uhrz.setHrz6End(190.0);
        
        userHeartRateZoneDao.createHeartRateZone(uhrz);
        
        UserHeartRateZone uhrz1 = new UserHeartRateZone();
        uhrz1 = userHeartRateZoneDao.getHeartRateZone("123456789");
        Assert.assertEquals(new Double(50.0), uhrz1.getHrz1Start());
        Assert.assertEquals(new Double(90.0), uhrz1.getHrz1End());
        Assert.assertEquals(new Double(90.1), uhrz1.getHrz2Start());
        Assert.assertEquals(new Double(113.0), uhrz1.getHrz2End());
        Assert.assertEquals(new Double(113.1), uhrz1.getHrz3Start());
        Assert.assertEquals(new Double(124.0), uhrz1.getHrz3End());
        Assert.assertEquals(new Double(124.1), uhrz1.getHrz4Start());
        Assert.assertEquals(new Double(132.0), uhrz1.getHrz4End());
        Assert.assertEquals(new Double(132.1), uhrz1.getHrz5Start());
        
		
		
		
		
		
	}

	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateZoneDao#updateHeartRateZone(com.genie.heartrate.mgmt.beans.UserHeartRateZone)}.
	 */
	@Test
	public void testUpdateHeartRateZone() {
		fail("Not yet implemented");
	}

}
