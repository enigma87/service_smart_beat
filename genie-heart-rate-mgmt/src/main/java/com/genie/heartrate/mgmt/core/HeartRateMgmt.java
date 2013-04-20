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
	public UserHeartRateTest getHeartRateTestResultsForUser(Long userid);
	public void saveHeartRateTestResultsForUser(UserHeartRateTest uhrt);
	
	public UserHeartRateZone getHeartRateZonesForUser(Long userid);
	public void saveHeartRateZonesForUser(UserHeartRateZone userHeartRateZone);
}
