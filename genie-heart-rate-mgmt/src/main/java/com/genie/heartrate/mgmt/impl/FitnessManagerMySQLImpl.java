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
	
	private FitnessShapeIndexDAO fitnessShapeIndexDAO;
	private FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO;
	private FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
	
	
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
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		
		String userid = fitnessTrainingSessionBean.getUserid();
		
		FitnessShapeIndexBean fitnessShapeIndexBean = fitnessShapeIndexDAO.getShapeIndexModelByUserId(userid);
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);		
		fitnessHomeostasisIndexBean.setTotalLoadOfExercise(ShapeIndexAlgorithm.calculateTotalLoadofExercise(fitnessTrainingSessionBean.getTimeDistributionOfHRZ()));
		fitnessHomeostasisIndexBean.setTimeAtFullRecovery(ShapeIndexAlgorithm.calculateTimeAtFullRecovery(fitnessShapeIndexBean.getTraineeClassification(), fitnessTrainingSessionBean.getEndTime(), fitnessHomeostasisIndexBean.getTotalLoadOfExercise()));
		
	}
	
	public void deleteFitnessTrainingSessionbyTrainingSessionId(Integer fitnessTrainingSessionId){
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(fitnessTrainingSessionId);
	}

}