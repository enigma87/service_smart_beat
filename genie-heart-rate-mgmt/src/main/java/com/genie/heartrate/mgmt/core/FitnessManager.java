/**
 * 
 */
package com.genie.heartrate.mgmt.core;

import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;

/**
 * @author dhasarathy
 *
 */
public interface FitnessManager 
{
	public UserHeartRateTest getHeartRateTestResultsForUser(String userid);
	public void saveHeartRateTestResultsForUser(UserHeartRateTest uhrt);
	
	public UserHeartRateZone getHeartRateZonesForUser(String userid);
	public void saveHeartRateZonesForUser(UserHeartRateZone userHeartRateZone);
		
	public void saveFitnessTrainingSession(FitnessTrainingSessionBean fitnessTrainingSessionBean);
	public void deleteFitnessTrainingSessionbyTrainingSessionId(String fitnessTrainingSessionId);
	
	public double getFitnessShapeIndex(String recentTrainingSessionId);
	
	public String getRecentTrainingSessionId(String userid);
}
