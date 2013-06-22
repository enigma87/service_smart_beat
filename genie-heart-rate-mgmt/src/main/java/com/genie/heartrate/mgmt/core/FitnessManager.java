/**
 * 
 */
package com.genie.heartrate.mgmt.core;

import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;

/**
 * @author dhasarathy
 *
 */
public interface FitnessManager 
{	
	public void saveFitnessTrainingSession(FitnessTrainingSessionBean fitnessTrainingSessionBean);
	public void deleteFitnessTrainingSessionbyTrainingSessionId(String fitnessTrainingSessionId);
	
	public double getFitnessShapeIndex(String recentTrainingSessionId);
	
	public String getRecentTrainingSessionId(String userid);
}
