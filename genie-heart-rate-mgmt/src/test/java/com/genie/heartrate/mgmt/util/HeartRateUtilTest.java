package com.genie.heartrate.mgmt.util;

import static org.junit.Assert.*;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;


import org.junit.Test;

public class HeartRateUtilTest {

	@Test
	public void testCalculateHeartRateZones() {
		
		UserHeartRateTest userHeartRateTest = new UserHeartRateTest();
		UserHeartRateZone userHeartRateZone = new UserHeartRateZone();
		
		userHeartRateTest.setUserid(1238990008L);
		userHeartRateTest.setRestingHeartRate(50);
		userHeartRateTest.setThresholdHeartRate(130);
		userHeartRateTest.setMaximalHeartRate(200);
		
		userHeartRateZone = HeartRateUtil.calculateHeartRateZones(userHeartRateTest);
		
		System.out.print("The User ID is "+userHeartRateZone.getUserid()+"\n");
		System.out.print("The Zone 1 Range is "+userHeartRateZone.getHrz1Start()+" - "+userHeartRateZone.getHrz1End()+"\n");
		System.out.print("The Zone 2 Range is "+userHeartRateZone.getHrz2Start()+" - "+userHeartRateZone.getHrz2End()+"\n");
		System.out.print("The Zone 3 Range is "+userHeartRateZone.getHrz3Start()+" - "+userHeartRateZone.getHrz3End()+"\n");
		System.out.print("The Zone 4 Range is "+userHeartRateZone.getHrz4Start()+" - "+userHeartRateZone.getHrz4End()+"\n");
		System.out.print("The Zone 5 Range is "+userHeartRateZone.getHrz5Start()+" - "+userHeartRateZone.getHrz5End()+"\n");
		System.out.print("The Zone 6 Range is "+userHeartRateZone.getHrz6Start()+" - "+userHeartRateZone.getHrz6End()+"\n");
		System.out.print("The Zones were created at "+userHeartRateZone.getCreatedTs()+"\n");
		System.out.print("The Zones were updated at "+userHeartRateZone.getUpdatedTs()+"\n");
		
	}

}
