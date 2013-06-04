package com.genie.heartrate.mgmt.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
import com.genie.heartrate.mgmt.core.FitnessManager;
import com.genie.heartrate.mgmt.dao.UserHeartRateTestDao;
/**
 * @author vidhun
 *
 */

public class FitnessManagerMySQLImplTest {

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
		
		FitnessManager hrMgmt = new FitnessManagerMySQLImpl();
		UserHeartRateTest uhrt1 = new UserHeartRateTest();
		Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
		
		uhrt1.setUserid("123456789");
		uhrt1.setRestingHeartRate(56);
		uhrt1.setMaximalHeartRate(168);
		uhrt1.setThresholdHeartRate(108);
		uhrt1.setRestingHeartRateTimestamp(timestamp);
		uhrt1.setMaximalHeartRateTimestamp(timestamp);
		uhrt1.setThresholdHeartRateTimestamp(timestamp);
		if(hrMgmt instanceof FitnessManagerMySQLImpl){}
		((FitnessManagerMySQLImpl)hrMgmt).setUserHeartRateTestDao(userHeartRateTestDao);
		hrMgmt.saveHeartRateTestResultsForUser(uhrt1);
		
		UserHeartRateTest uhrt2 = hrMgmt.getHeartRateTestResultsForUser("123456789");
		Assert.assertNotNull(uhrt2);
		
		userHeartRateTestDao.deleteHeartRateTestResults("123456789");
			
	}

	
	@Test
	public void testsaveHeartRateTestResultsForUser() {
		
		FitnessManager hrMgmt = new FitnessManagerMySQLImpl();
		UserHeartRateTest uhrt1 = new UserHeartRateTest();
		Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
		uhrt1.setUserid("123456789");
		uhrt1.setRestingHeartRate(56);
		uhrt1.setMaximalHeartRate(168);
		uhrt1.setThresholdHeartRate(108);
		uhrt1.setRestingHeartRateTimestamp(timestamp);
		uhrt1.setMaximalHeartRateTimestamp(timestamp);
		uhrt1.setThresholdHeartRateTimestamp(timestamp);
		if(hrMgmt instanceof FitnessManagerMySQLImpl){}
		((FitnessManagerMySQLImpl)hrMgmt).setUserHeartRateTestDao(userHeartRateTestDao);
		hrMgmt.saveHeartRateTestResultsForUser(uhrt1);
		
		UserHeartRateTest uhrt2 = hrMgmt.getHeartRateTestResultsForUser("123456789");
		Assert.assertEquals("123456789", uhrt2.getUserid());
		Assert.assertEquals(new Integer(56), uhrt2.getRestingHeartRate());
		Assert.assertEquals(new Integer(168), uhrt2.getMaximalHeartRate());
		Assert.assertEquals(new Integer(108), uhrt2.getThresholdHeartRate());
		Assert.assertNotNull(uhrt2.getRestingHeartRateTimestamp());
		Assert.assertNotNull(uhrt2.getMaximalHeartRateTimestamp());
		Assert.assertNotNull(uhrt2.getThresholdHeartRateTimestamp());
		
		userHeartRateTestDao.deleteHeartRateTestResults("123456789");
			
	}
	
	@Test
	public void testgetHeartRateZonesForUser() {
		
		FitnessManager hrMgmt = new FitnessManagerMySQLImpl();
		UserHeartRateTest uhrt = new UserHeartRateTest();
		Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
		
		uhrt.setUserid("123456789");
		uhrt.setRestingHeartRate(56);
		uhrt.setMaximalHeartRate(168);
		uhrt.setThresholdHeartRate(108);
		uhrt.setRestingHeartRateTimestamp(timestamp);
		uhrt.setMaximalHeartRateTimestamp(timestamp);
		uhrt.setThresholdHeartRateTimestamp(timestamp);
		if(hrMgmt instanceof FitnessManagerMySQLImpl){}
		((FitnessManagerMySQLImpl)hrMgmt).setUserHeartRateTestDao(userHeartRateTestDao);
		hrMgmt.saveHeartRateTestResultsForUser(uhrt);
		
		UserHeartRateZone uhrz = hrMgmt.getHeartRateZonesForUser("123456789");
		Assert.assertEquals("123456789", uhrz.getUserid());
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
   
		userHeartRateTestDao.deleteHeartRateTestResults("123456789");					
	}	
	
	@Test
	public void testSaveFitnessTrainingSession(){
		
		long now = new Date().getTime();
		long nowPastOneHour = now - 3600000;
		Integer fitnessTrainingSessionId = 20131;
	
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid("ff2d44bb-8af8-46e3-b88f-0cd777ac188e");
		fitnessTrainingSessionBean.setTrainingSessionId(fitnessTrainingSessionId);
		fitnessTrainingSessionBean.setStartTime(new Timestamp(now));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(nowPastOneHour));
		fitnessTrainingSessionBean.setHrz1Time(2.0);
		fitnessTrainingSessionBean.setHrz2Time(5.0);
		fitnessTrainingSessionBean.setHrz3Time(5.0);
		fitnessTrainingSessionBean.setHrz4Time(10.0);
		fitnessTrainingSessionBean.setHrz5Time(28.0);
		fitnessTrainingSessionBean.setHrz6Time(10.0);

		FitnessManager fitnessManager = (FitnessManager)appContext.getBean("fitnessManagerMySQLImpl");
		
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		fitnessManager.deleteFitnessTrainingSessionbyTrainingSessionId(fitnessTrainingSessionId);
	}
}