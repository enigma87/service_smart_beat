/**
 * 
 */
package com.genie.heartrate.mgmt.core;

import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;

/**
 * @author manojkumar
 *
 */
public interface HeartRateMgmt 
{
	public UserHeartRateTest getHeartRateTestResultsForUser(String userid);
	public void saveHeartRateTestResultsForUser(UserHeartRateTest uhrt);
	
	public UserHeartRateZone getHeartRateZonesForUser(String userid);
	public void saveHeartRateZonesForUser(UserHeartRateZone userHeartRateZone);
}
