/**
 * 
 */
package com.genie.heartrate.mgmt.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;

/**
 * @author manojkumar
 *
 */
public class HeartRateUtil 
{

	public static Map<String, Object> parseHeartRates(String json)
	{
		return new HashMap<String, Object>();
	}
	
	public static UserHeartRateZone calculateHeartRateZones(UserHeartRateTest userheartRateTest)
	{
		UserHeartRateZone userHeartRateZone = new UserHeartRateZone();
		
		final Integer restingHr   = userheartRateTest.getRestingHr();
		final Integer thresholdHr = userheartRateTest.getThresholdHr();
		final Integer maximalHr   = userheartRateTest.getMaximalHr();
		final Integer hrReserve   = maximalHr-restingHr;
		
		userHeartRateZone.setUserid(userheartRateTest.getUserid());
		userHeartRateZone.setHrz1Start((double) restingHr);
		userHeartRateZone.setHrz1End((double)((thresholdHr-restingHr)/2+restingHr));
		userHeartRateZone.setHrz2Start(userHeartRateZone.getHrz1End()+0.01);
		userHeartRateZone.setHrz2End((thresholdHr-userHeartRateZone.getHrz1End())/2+userHeartRateZone.getHrz1End());
		userHeartRateZone.setHrz4Start(thresholdHr-(0.04*hrReserve));
		userHeartRateZone.setHrz4End(thresholdHr+(0.02*hrReserve));
		userHeartRateZone.setHrz3Start(userHeartRateZone.getHrz2End()+0.01);
		userHeartRateZone.setHrz3End(userHeartRateZone.getHrz4Start()-0.01);
		userHeartRateZone.setHrz6Start(maximalHr-(0.06*hrReserve));
		userHeartRateZone.setHrz6End((double)maximalHr);
		userHeartRateZone.setHrz5Start(userHeartRateZone.getHrz4End()+0.01);
		userHeartRateZone.setHrz5End(userHeartRateZone.getHrz6Start()-0.01);
		
		Timestamp timestamp = new Timestamp((Calendar.getInstance().getTime().getTime()));
		userHeartRateZone.setCreatedTs(timestamp);
		userHeartRateZone.setUpdatedTs(timestamp);
		
		return userHeartRateZone;
	}
}
