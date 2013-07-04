/**
 * 
 */
package com.genie.smartbeat.core;

import com.genie.smartbeat.beans.FitnessTrainingSessionBean;

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
