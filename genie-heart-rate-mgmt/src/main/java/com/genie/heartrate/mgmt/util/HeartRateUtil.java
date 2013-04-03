/**
 * 
 */
package com.genie.heartrate.mgmt.util;

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
		//TODO Calculate the HRZ's
		return new UserHeartRateZone();
	}
}
