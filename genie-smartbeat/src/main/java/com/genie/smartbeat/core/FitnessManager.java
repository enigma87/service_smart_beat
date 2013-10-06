/**
 * 
 */
package com.genie.smartbeat.core;

import java.sql.Timestamp;
import java.util.List;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionIdBean;
import com.genie.smartbeat.core.exceptions.session.TrainingSessionException;
import com.genie.smartbeat.core.exceptions.test.HeartrateTestException;
import com.genie.smartbeat.core.exceptions.time.TimeException;

/**
 * @author dhasarathy
 *
 */
public interface FitnessManager{	
	/*training session*/
	public void saveFitnessTrainingSession(FitnessTrainingSessionBean fitnessTrainingSessionBean) throws TrainingSessionException, TimeException;
	public String getRecentTrainingSessionId(String userid);
	public FitnessTrainingSessionBean getTrainingSessionById(String fitnessTrainingSessionId);
	public List<FitnessTrainingSessionIdBean> getTrainingSessionIdsInTimeInterval(String userID, Timestamp startTime, Timestamp endTime) throws TimeException;
	public List<FitnessTrainingSessionBean> getTrainingSessionsInTimeInterval(String userID, Timestamp startTime, Timestamp endTime) throws TimeException;
	public void deleteFitnessTrainingSessionbyTrainingSessionId(String fitnessTrainingSessionId);
	
	/*heart rate test*/
	public void saveHeartrateTest(FitnessHeartrateTestBean fitnessHeartrateTestBean) throws HeartrateTestException, TimeException;
	public List<FitnessHeartrateTestBean> getFitnessHeartrateTestsByTypeInTimeInterval(String userid, Integer heartrateType, Timestamp startTimestamp, Timestamp endTimestamp) throws TimeException;

	/*heart rate zones*/
	public double[][] getHeartrateZones(String userid);

	/*homeostasis index model*/
	public FitnessHomeostasisIndexBean getHomeostasisIndexModelForUser(String userid);
	
	/*shape index*/
	public double getShapeIndex(String recentTrainingSessionId);
	public double getShapeIndex(String recentTrainingSessionId, FitnessTrainingSessionBean newlyArrivedTrainingSession);
	public List<FitnessShapeIndexBean> getShapeIndexHistoryInTimeInterval(String userid, Timestamp startTime, Timestamp endTime) throws TimeException;

	/*Recovery Time*/
	public Timestamp getRecoveryTime(String userid);
	
	/*clear user*/
	public void clearTraineeData(String userid);
	
}
