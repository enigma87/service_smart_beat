/**
 * 
 */
package com.genie.heartrate.mgmt.core;

import com.genie.heartrate.mgmt.beans.HeartRate;

/**
 * @author manojkumar
 *
 */
public interface HeartRateMgmt 
{
	public HeartRate getHeartRateForUser(Long userid);
	public void createHeartRateForUser(HeartRate heartRate);
	public void updateHeartRateForUser(HeartRate heartRate);
}
