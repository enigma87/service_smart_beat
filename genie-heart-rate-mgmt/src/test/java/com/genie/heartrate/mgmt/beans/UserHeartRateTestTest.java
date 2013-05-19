package com.genie.heartrate.mgmt.beans;

import java.sql.Timestamp;
import java.util.Calendar;

import junit.framework.Assert;

import org.junit.Test;
/**
 * @author vidhun
 *
 */

public class UserHeartRateTestTest {

		
	@Test
	public void testUserHeartRateTest() {
		
		UserHeartRateTest userHeartRateTest = new UserHeartRateTest();
		Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
		
		userHeartRateTest.setUserid("123456789");
		userHeartRateTest.setRestingHeartRate(59);
		userHeartRateTest.setRestingHeartRateTimestamp(timestamp);
		userHeartRateTest.setMaximalHeartRate(164);
		userHeartRateTest.setMaximalHeartRateTimestamp(timestamp);
		userHeartRateTest.setThresholdHeartRate(117);
		userHeartRateTest.setThresholdHeartRateTimestamp(timestamp);
		
		Assert.assertEquals("123456789",userHeartRateTest.getUserid());
		Assert.assertEquals(new Integer(59),userHeartRateTest.getRestingHeartRate());
		Assert.assertEquals(new Integer(164),userHeartRateTest.getMaximalHeartRate());
		Assert.assertEquals(new Integer(117),userHeartRateTest.getThresholdHeartRate());
		Assert.assertTrue(timestamp.equals(userHeartRateTest.getRestingHeartRateTimestamp()));
		Assert.assertTrue(timestamp.equals(userHeartRateTest.getMaximalHeartRateTimestamp()));
		Assert.assertTrue(timestamp.equals(userHeartRateTest.getThresholdHeartRateTimestamp()));
		
	
	}

}
