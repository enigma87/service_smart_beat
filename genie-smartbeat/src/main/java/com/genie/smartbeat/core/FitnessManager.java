/**
 * 
 */
package com.genie.smartbeat.core;

import java.sql.Timestamp;
import java.util.List;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;

/**
 * @author dhasarathy
 *
 */
public interface FitnessManager{	
	/*training session*/
	public void saveFitnessTrainingSession(FitnessTrainingSessionBean fitnessTrainingSessionBean);
	public String getRecentTrainingSessionId(String userid);
	public FitnessTrainingSessionBean getTrainingSessionById(String fitnessTrainingSessionId);
	public List<String> getTrainingSessionIdsInTimeInterval(String userID, Timestamp startTime, Timestamp endTime);
	public void deleteFitnessTrainingSessionbyTrainingSessionId(String fitnessTrainingSessionId);

	/*heart rate test*/
	public void saveHeartrateTest(FitnessHeartrateTestBean fitnessHeartrateTestBean);

	/*heart rate zones*/
	public double[][] getHeartrateZones(String userid);

	/*shape index*/
	public double getShapeIndex(String recentTrainingSessionId);
	public List<FitnessShapeIndexBean> getShapeIndexHistoryInTimeInterval(String userid, Timestamp startTime, Timestamp endTime);

	/*clear user*/
	public void clearTraineeData(String userid);
}
