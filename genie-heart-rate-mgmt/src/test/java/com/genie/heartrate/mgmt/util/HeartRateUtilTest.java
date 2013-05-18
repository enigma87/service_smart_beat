package com.genie.heartrate.mgmt.util;

import org.junit.Test;

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
/**
 * @author vidhun
 *
 */
public class HeartRateUtilTest {

	@Test
	public void testCalculateHeartRateZones() {
		
		UserHeartRateTest userHeartRateTest = new UserHeartRateTest();
		UserHeartRateZone userHeartRateZone = new UserHeartRateZone();
		
		userHeartRateTest.setUserid("123456789");
		userHeartRateTest.setRestingHeartRate(56);
		userHeartRateTest.setThresholdHeartRate(108);
		userHeartRateTest.setMaximalHeartRate(168);
		
		userHeartRateZone = HeartRateUtil.calculateHeartRateZones(userHeartRateTest);
		
		System.out.print("The User ID is "+userHeartRateZone.getUserid()+"\n");
		System.out.print("The Zone 1 Range is "+userHeartRateZone.getHrz1Start()+" - "+userHeartRateZone.getHrz1End()+"\n");
		System.out.print("The Zone 2 Range is "+userHeartRateZone.getHrz2Start()+" - "+userHeartRateZone.getHrz2End()+"\n");
		System.out.print("The Zone 3 Range is "+userHeartRateZone.getHrz3Start()+" - "+userHeartRateZone.getHrz3End()+"\n");
		System.out.print("The Zone 4 Range is "+userHeartRateZone.getHrz4Start()+" - "+userHeartRateZone.getHrz4End()+"\n");
		System.out.print("The Zone 5 Range is "+userHeartRateZone.getHrz5Start()+" - "+userHeartRateZone.getHrz5End()+"\n");
		System.out.print("The Zone 6 Range is "+userHeartRateZone.getHrz6Start()+" - "+userHeartRateZone.getHrz6End()+"\n");

		
	}

}
