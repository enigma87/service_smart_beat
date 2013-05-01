package com.genie.heartrate.mgmt.impl;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;

import junit.framework.Assert;

import com.genie.heartrate.mgmt.impl.HeartRateMgmtMySQLImpl;
import com.genie.heartrate.mgmt.core.HeartRateMgmt;
import com.genie.heartrate.mgmt.dao.UserHeartRateTestDao;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class HeartRateMgmtMySQLImplTest {

	private ApplicationContext appContext;
	private UserHeartRateTestDao userHeartRateTestDao;
	
	@Before
	public void setUpBeforeClass() throws Exception 
	{
		appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		userHeartRateTestDao = (UserHeartRateTestDao) appContext.getBean("userHeartRateTestDao");
	}
	
	@Test
	public void testgetHeartRateTestResultsForUser() {
		
		HeartRateMgmt hrMgmt = new HeartRateMgmtMySQLImpl();
		UserHeartRateTest uhrt1 = new UserHeartRateTest();
		Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
		
		uhrt1.setUserid(1003L);
		uhrt1.setRestingHeartRate(56);
		uhrt1.setMaximalHeartRate(168);
		uhrt1.setThresholdHeartRate(108);
		uhrt1.setRestingHeartRateTimestamp(timestamp);
		uhrt1.setMaximalHeartRateTimestamp(timestamp);
		uhrt1.setThresholdHeartRateTimestamp(timestamp);
		if(hrMgmt instanceof HeartRateMgmtMySQLImpl){}
		((HeartRateMgmtMySQLImpl)hrMgmt).setUserHeartRateTestDao(userHeartRateTestDao);
		hrMgmt.saveHeartRateTestResultsForUser(uhrt1);
		
		UserHeartRateTest uhrt2 = hrMgmt.getHeartRateTestResultsForUser(1003L);
		Assert.assertNotNull(uhrt2);
		
	    userHeartRateTestDao.deleteHeartRateTestResults(1003L);
			
	}

	
	@Test
	public void testsaveHeartRateTestResultsForUser() {
		
		HeartRateMgmt hrMgmt = new HeartRateMgmtMySQLImpl();
		UserHeartRateTest uhrt1 = new UserHeartRateTest();
		Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
				
		uhrt1.setUserid(1003L);
		uhrt1.setRestingHeartRate(56);
		uhrt1.setMaximalHeartRate(168);
		uhrt1.setThresholdHeartRate(108);
		uhrt1.setRestingHeartRateTimestamp(timestamp);
		uhrt1.setMaximalHeartRateTimestamp(timestamp);
		uhrt1.setThresholdHeartRateTimestamp(timestamp);
		if(hrMgmt instanceof HeartRateMgmtMySQLImpl){}
		((HeartRateMgmtMySQLImpl)hrMgmt).setUserHeartRateTestDao(userHeartRateTestDao);
		hrMgmt.saveHeartRateTestResultsForUser(uhrt1);
		
		UserHeartRateTest uhrt2 = hrMgmt.getHeartRateTestResultsForUser(1003L);
		Assert.assertEquals(new Long(1003L), uhrt2.getUserid());
		Assert.assertEquals(new Integer(56), uhrt2.getRestingHeartRate());
		Assert.assertEquals(new Integer(168), uhrt2.getMaximalHeartRate());
		Assert.assertEquals(new Integer(108), uhrt2.getThresholdHeartRate());
		Assert.assertNotNull(uhrt2.getRestingHeartRateTimestamp());
		Assert.assertNotNull(uhrt2.getMaximalHeartRateTimestamp());
		Assert.assertNotNull(uhrt2.getThresholdHeartRateTimestamp());
		
		userHeartRateTestDao.deleteHeartRateTestResults(1003L);
			
	}
	
	@Test
	public void testgetHeartRateZonesForUser() {
		
		HeartRateMgmt hrMgmt = new HeartRateMgmtMySQLImpl();
		UserHeartRateTest uhrt = new UserHeartRateTest();
		Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
		
		uhrt.setUserid(1003L);
		uhrt.setRestingHeartRate(56);
		uhrt.setMaximalHeartRate(168);
		uhrt.setThresholdHeartRate(108);
		uhrt.setRestingHeartRateTimestamp(timestamp);
		uhrt.setMaximalHeartRateTimestamp(timestamp);
		uhrt.setThresholdHeartRateTimestamp(timestamp);
		if(hrMgmt instanceof HeartRateMgmtMySQLImpl){}
		((HeartRateMgmtMySQLImpl)hrMgmt).setUserHeartRateTestDao(userHeartRateTestDao);
		hrMgmt.saveHeartRateTestResultsForUser(uhrt);
		
		UserHeartRateZone uhrz = hrMgmt.getHeartRateZonesForUser(1003L);
		Assert.assertEquals(new Long(1003L), uhrz.getUserid());
		Assert.assertEquals(new Double(56.0), uhrz.getHrz1Start());
		Assert.assertEquals(new Double(82.0), uhrz.getHrz1End());
		Assert.assertEquals(new Double(82.001), uhrz.getHrz2Start());
		Assert.assertEquals(new Double(95.0), uhrz.getHrz2End());
		Assert.assertEquals(new Double(95.001), uhrz.getHrz3Start());
		Assert.assertEquals(new Double(103.519), uhrz.getHrz3End());
		Assert.assertEquals(new Double(103.52), uhrz.getHrz4Start());
		Assert.assertEquals(new Double(110.24), uhrz.getHrz4End());
		Assert.assertEquals(new Double(110.241), uhrz.getHrz5Start());
		Assert.assertEquals(new Double(161.279), uhrz.getHrz5End());
		Assert.assertEquals(new Double(161.28), uhrz.getHrz6Start());
		Assert.assertEquals(new Double(168.0), uhrz.getHrz6End());
   
	    userHeartRateTestDao.deleteHeartRateTestResults(1003L);
		
			
	}
}
