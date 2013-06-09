/**
 * 
 */
package com.genie.heartrate.mgmt.core;

import com.genie.heartrate.mgmt.beans.FitnessShapeIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;

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
	public void deleteFitnessTrainingSessionbyTrainingSessionId(Integer fitnessTrainingSessionId);
	
	public FitnessShapeIndexBean getFitnessShapeIndex(String userid);
}
