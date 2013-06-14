/**
 * 
 */
package com.genie.heartrate.mgmt.impl;

import com.genie.heartrate.mgmt.beans.FitnessHomeostasisIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessShapeIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
import com.genie.heartrate.mgmt.core.FitnessManager;
import com.genie.heartrate.mgmt.dao.FitnessHomeostasisIndexDAO;
import com.genie.heartrate.mgmt.dao.FitnessShapeIndexDAO;
import com.genie.heartrate.mgmt.dao.FitnessTrainingSessionDAO;
import com.genie.heartrate.mgmt.dao.UserHeartRateTestDao;
import com.genie.heartrate.mgmt.dao.UserHeartRateZoneDao;
import com.genie.heartrate.mgmt.util.ShapeIndexAlgorithm;

/**
 * @author manojkumar
 *
 */
public class FitnessManagerMySQLImpl implements FitnessManager 
{

	private UserHeartRateTestDao userHeartRateTestDao;
	private UserHeartRateZoneDao userHeartRateZoneDao;
	
	private FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO;
	private FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
	private FitnessShapeIndexDAO fitnessShapeIndexDAO;
	
	
	public UserHeartRateTestDao getUserHeartRateTestDao()
	{
		return this.userHeartRateTestDao;
	}
	
	public void setUserHeartRateTestDao(UserHeartRateTestDao userHeartRateTestDao)
	{
		this.userHeartRateTestDao = userHeartRateTestDao;
	}
	
	public UserHeartRateZoneDao getUserHeartRateZoneDao()
	{
		return this.userHeartRateZoneDao;
	}
	
	public void setUserHeartRateZoneDao(UserHeartRateZoneDao userHeartRateZoneDao)
	{
		this.userHeartRateZoneDao = userHeartRateZoneDao;
	}
	
	public FitnessHomeostasisIndexDAO getFitnessHomeostasisIndexDAO() {
		return fitnessHomeostasisIndexDAO;
	}
	
	public void setFitnessHomeostasisIndexDAO(
			FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO) {
		this.fitnessHomeostasisIndexDAO = fitnessHomeostasisIndexDAO;
	}
	
	
	public FitnessTrainingSessionDAO getFitnessTrainingSessionDAO() {
		return fitnessTrainingSessionDAO;
	}
	
	public void setFitnessTrainingSessionDAO(
			FitnessTrainingSessionDAO fitnessTrainingSessionDAO) {
		this.fitnessTrainingSessionDAO = fitnessTrainingSessionDAO;
	}
	
	public UserHeartRateTest getHeartRateTestResultsForUser(String userid) 
	{
		return userHeartRateTestDao.getHeartRateTestResults(userid);
	}

	public void saveHeartRateTestResultsForUser(UserHeartRateTest uhrt) 
	{
		UserHeartRateTest heartRateTest = getHeartRateTestResultsForUser(uhrt.getUserid());			
		if(null == heartRateTest){
			userHeartRateTestDao.createHeartRateTestResults(uhrt);
		}else{			
			uhrt.fillInTheBlanks(heartRateTest);			
			userHeartRateTestDao.updateHeartRateTestResults(uhrt);
		}
  
		//TODO Trigger HRZ calculation
					
	}

	public UserHeartRateZone getHeartRateZonesForUser(String userid)
	{
		UserHeartRateTest userHeartRateTest = userHeartRateTestDao.getHeartRateTestResults(userid);
		return ShapeIndexAlgorithm.calculateHeartRateZones(userHeartRateTest);
	}

	public void saveHeartRateZonesForUser(UserHeartRateZone userHeartRateZone) 
	{
		UserHeartRateZone fromDb = userHeartRateZoneDao.getHeartRateZone(userHeartRateZone.getUserid());
		if (fromDb == null)
			userHeartRateZoneDao.createHeartRateZone(userHeartRateZone);
		else
		{
			userHeartRateZoneDao.updateHeartRateZone(userHeartRateZone);
		}
	}

	public void saveFitnessTrainingSession(FitnessTrainingSessionBean fitnessTrainingSessionBean) {
		
		/*Save current training session*/
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		
		String userid = fitnessTrainingSessionBean.getUserid();
		/*Update Homeostasis index model*/
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		/*Backup last session's data*/
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(fitnessHomeostasisIndexBean.getCurrentTotalLoadOfExercise());
		fitnessHomeostasisIndexBean.setPreviousEndTime(fitnessHomeostasisIndexBean.getCurrentEndTime());
		/*Set current session's data*/
		Double currentTotalLoadOfExercise = ShapeIndexAlgorithm.calculateTotalLoadofExercise(fitnessTrainingSessionBean.getTimeDistributionOfHRZ());
		fitnessHomeostasisIndexBean.setCurrentTotalLoadOfExercise(currentTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setCurrentEndTime(fitnessTrainingSessionBean.getEndTime());
		Double regressedHomeostasisIndex = ShapeIndexAlgorithm.getRegressedHomeostasisIndex(fitnessHomeostasisIndexBean.getTraineeClassification(),fitnessHomeostasisIndexBean.getPreviousEndTime() ,fitnessHomeostasisIndexBean.getPreviousTotalLoadOfExercise());
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(ShapeIndexAlgorithm.getRegressionMinimumOfHomeostasisIndex(regressedHomeostasisIndex, currentTotalLoadOfExercise));
		fitnessHomeostasisIndexDAO.updateHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		
	}
	
	public void deleteFitnessTrainingSessionbyTrainingSessionId(Integer fitnessTrainingSessionId){
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(fitnessTrainingSessionId);
	}

	public FitnessShapeIndexBean getFitnessShapeIndex(String userid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public double getFitnessSupercompensationPoints(String userid){
		double supercompensationPoints = 0.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		double regressedHomeostasisIndex = ShapeIndexAlgorithm.getRegressedHomeostasisIndex(fitnessHomeostasisIndexBean.getTraineeClassification(), fitnessHomeostasisIndexBean.getPreviousEndTime(), fitnessHomeostasisIndexBean.getPreviousTotalLoadOfExercise());
		if(0 == regressedHomeostasisIndex && FitnessHomeostasisIndexBean.UNCOMPENSATED == fitnessHomeostasisIndexBean.getSupercompensationStatus() ){			
			supercompensationPoints = ShapeIndexAlgorithm.calculateSupercompensationPoints(fitnessHomeostasisIndexBean.getTraineeClassification(), fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
		} 
		return supercompensationPoints;
	}

}