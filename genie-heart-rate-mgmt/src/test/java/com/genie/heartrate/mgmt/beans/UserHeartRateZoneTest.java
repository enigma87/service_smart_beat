package com.genie.heartrate.mgmt.beans;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author vidhun
 *
 */
public class UserHeartRateZoneTest {

	@Test
	public void testUserHeartRateZone() {
		
		UserHeartRateZone userHeartRateZone = new UserHeartRateZone();
			
		userHeartRateZone.setUserid("123456789");
		userHeartRateZone.setHrz1Start(55.0);
		userHeartRateZone.setHrz1End(61.3);
		userHeartRateZone.setHrz2Start(61.4);
		userHeartRateZone.setHrz2End(72.3);
		userHeartRateZone.setHrz3Start(72.4);
		userHeartRateZone.setHrz3End(80.5);
		userHeartRateZone.setHrz4Start(80.6);
		userHeartRateZone.setHrz4End(101.3);
		userHeartRateZone.setHrz5Start(101.4);
		userHeartRateZone.setHrz5End(120.5);
		userHeartRateZone.setHrz6Start(120.6);
		userHeartRateZone.setHrz6End(134.5);

		
		Assert.assertEquals("123456789",userHeartRateZone.getUserid());
		Assert.assertEquals(new Double(55.0),userHeartRateZone.getHrz1Start());
		Assert.assertEquals(new Double(61.3),userHeartRateZone.getHrz1End());
		Assert.assertEquals(new Double(61.4),userHeartRateZone.getHrz2Start());
		Assert.assertEquals(new Double(72.3),userHeartRateZone.getHrz2End());
		Assert.assertEquals(new Double(72.4),userHeartRateZone.getHrz3Start());
		Assert.assertEquals(new Double(80.5),userHeartRateZone.getHrz3End());
		Assert.assertEquals(new Double(80.6),userHeartRateZone.getHrz4Start());
		Assert.assertEquals(new Double(101.3),userHeartRateZone.getHrz4End());
		Assert.assertEquals(new Double(101.4),userHeartRateZone.getHrz5Start());
		Assert.assertEquals(new Double(120.5),userHeartRateZone.getHrz5End());
		Assert.assertEquals(new Double(120.6),userHeartRateZone.getHrz6Start());
		Assert.assertEquals(new Double(134.5),userHeartRateZone.getHrz6End());

		
	}

}
