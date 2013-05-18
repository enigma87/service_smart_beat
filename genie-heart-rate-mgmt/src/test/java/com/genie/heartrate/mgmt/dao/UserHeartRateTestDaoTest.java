/**
 * 
 */
package com.genie.heartrate.mgmt.dao;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;


/**
 * @author manojkumar
 *
 */
public class UserHeartRateTestDaoTest 
{
	private ApplicationContext appContext;
	private UserHeartRateTestDao userHeartRateTestDao;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUpBeforeClass() throws Exception 
	{
		appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		userHeartRateTestDao = (UserHeartRateTestDao) appContext.getBean("userHeartRateTestDao");
	}

	
	
	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateTestDao#getHeartRateTestResults(java.lang.Long)}.
	 */
	
	@Test
	public void testGetHeartRateTestResults() 
	{
		/* Creating User Heart Rate Test Results */
		UserHeartRateTest uhrt1 = new UserHeartRateTest();
		uhrt1.setUserid("123456789");
		uhrt1.setRestingHeartRate(50);
		uhrt1.setThresholdHeartRate(100);
		uhrt1.setMaximalHeartRate(170);
		userHeartRateTestDao.createHeartRateTestResults(uhrt1);
		Assert.assertNotNull(userHeartRateTestDao);
	
		/* The test starts here */ 
		UserHeartRateTest uhrt2 = userHeartRateTestDao.getHeartRateTestResults("123456789");
		Assert.assertNotNull(uhrt2);
		
		/*Cleanup*/
		userHeartRateTestDao.deleteHeartRateTestResults("123456789");
				
	}
	
    
	
	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateTestDao#createHeartRateTestResults(com.genie.heartrate.mgmt.beans.UserHeartRateTest)}.
	 */
	@Test
	public void testCreateHeartRateTestResults() {
		
		Assert.assertNotNull(userHeartRateTestDao);
		
		/*The test starts here*/
		UserHeartRateTest uhrt = new UserHeartRateTest();
		uhrt.setUserid("123456789");
		uhrt.setRestingHeartRate(50);
		uhrt.setThresholdHeartRate(100);
		uhrt.setMaximalHeartRate(170);
		userHeartRateTestDao.createHeartRateTestResults(uhrt);
		
		UserHeartRateTest uhrt1 = new UserHeartRateTest();
		uhrt1 = userHeartRateTestDao.getHeartRateTestResults("123456789");
		Assert.assertEquals( new Integer(50), uhrt1.getRestingHeartRate());
		Assert.assertEquals( new Integer(170), uhrt1.getMaximalHeartRate());
		Assert.assertEquals( new Integer(100), uhrt1.getThresholdHeartRate());
		
		
		/*Cleanup*/
		userHeartRateTestDao.deleteHeartRateTestResults("123456789");
	
	}
    
  
	/**
	 * Test method for {@link com.genie.heartrate.mgmt.dao.UserHeartRateTestDao#updateHeartRateTestResults(com.genie.heartrate.mgmt.beans.UserHeartRateTest)}.
	*/
	@Test
	public void testUpdateHeartRateTestResults() {
		
		Assert.assertNotNull(userHeartRateTestDao);
		
		/* Create User Heart Rate Results*/
		UserHeartRateTest uhrt1 = new UserHeartRateTest();
		uhrt1.setUserid("123456789");
		uhrt1.setRestingHeartRate(50);
		uhrt1.setThresholdHeartRate(100);
		uhrt1.setMaximalHeartRate(170);
		userHeartRateTestDao.createHeartRateTestResults(uhrt1);
		
		/* Update User Heart Rate Results test starts here*/
		UserHeartRateTest uhrt2 = userHeartRateTestDao.getHeartRateTestResults("123456789");
		Assert.assertNotNull(uhrt2);
		uhrt2.setRestingHeartRate(61);
		uhrt2.setMaximalHeartRate(175);
		uhrt2.setThresholdHeartRate(107);
		userHeartRateTestDao.updateHeartRateTestResults(uhrt2);
		
		UserHeartRateTest uhrt3 = new UserHeartRateTest();
		uhrt3 = userHeartRateTestDao.getHeartRateTestResults("123456789");
		Assert.assertEquals( new Integer(61), uhrt3.getRestingHeartRate());
		Assert.assertEquals( new Integer(175), uhrt3.getMaximalHeartRate());
		Assert.assertEquals( new Integer(107), uhrt3.getThresholdHeartRate());
		
		/*Cleanup*/
		userHeartRateTestDao.deleteHeartRateTestResults("123456789");
	
	}
	
    @Test
    public void testDeleteHeartRateTestResults(){
    	
    	Assert.assertNotNull(userHeartRateTestDao);
    	
		/* Create User Heart Rate Results*/
		UserHeartRateTest uhrt1 = new UserHeartRateTest();
		uhrt1.setUserid("123456789");
		uhrt1.setRestingHeartRate(50);
		uhrt1.setThresholdHeartRate(100);
		uhrt1.setMaximalHeartRate(170);
		userHeartRateTestDao.createHeartRateTestResults(uhrt1);
    	
		/* Delete User Heart Rate Results Test starts here*/
    	userHeartRateTestDao.deleteHeartRateTestResults("123456789");
    	
    	UserHeartRateTest uhrt2 = new UserHeartRateTest();
		uhrt2 = userHeartRateTestDao.getHeartRateTestResults("123456789");
		Assert.assertNull(uhrt2);
    	
		    	
    }

}
