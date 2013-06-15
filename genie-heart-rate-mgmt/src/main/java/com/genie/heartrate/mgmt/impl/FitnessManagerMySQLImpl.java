/**
 * 
 */
package com.genie.heartrate.mgmt.impl;

import com.genie.heartrate.mgmt.beans.FitnessHomeostasisIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessShapeIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessSpeedHeartRateBean;
import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
import com.genie.heartrate.mgmt.core.FitnessManager;
import com.genie.heartrate.mgmt.dao.FitnessHomeostasisIndexDAO;
import com.genie.heartrate.mgmt.dao.FitnessShapeIndexDAO;
import com.genie.heartrate.mgmt.dao.FitnessSpeedHeartRateDAO;
import com.genie.heartrate.mgmt.dao.FitnessTrainingSessionDAO;
import com.genie.heartrate.mgmt.dao.UserHeartRateTestDao;
import com.genie.heartrate.mgmt.dao.UserHeartRateZoneDao;
import com.genie.heartrate.mgmt.util.ShapeIndexAlgorithm;

/**
 * @author dhasarathy
 *
 */
public class FitnessManagerMySQLImpl implements FitnessManager 
{

	private UserHeartRateTestDao userHeartRateTestDao;
	private UserHeartRateZoneDao userHeartRateZoneDao;
	
	private FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
	private FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO;
	private FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO;
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
	
	public FitnessTrainingSessionDAO getFitnessTrainingSessionDAO() {
		return fitnessTrainingSessionDAO;
	}
	
	public void setFitnessTrainingSessionDAO(
			FitnessTrainingSessionDAO fitnessTrainingSessionDAO) {
		this.fitnessTrainingSessionDAO = fitnessTrainingSessionDAO;
	}
	
	public FitnessHomeostasisIndexDAO getFitnessHomeostasisIndexDAO() {
		return fitnessHomeostasisIndexDAO;
	}
	
	public void setFitnessHomeostasisIndexDAO(
			FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO) {
		this.fitnessHomeostasisIndexDAO = fitnessHomeostasisIndexDAO;
	}
	
	public FitnessSpeedHeartRateDAO getFitnessSpeedHeartRateDAO() {
		return fitnessSpeedHeartRateDAO;
	}
	
	public void setFitnessSpeedHeartRateDAO(
			FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO) {
		this.fitnessSpeedHeartRateDAO = fitnessSpeedHeartRateDAO;
	}
	
	public FitnessShapeIndexDAO getFitnessShapeIndexDAO() {
		return fitnessShapeIndexDAO;
	}
	
	public void setFitnessShapeIndexDAO(
			FitnessShapeIndexDAO fitnessShapeIndexDAO) {
		this.fitnessShapeIndexDAO = fitnessShapeIndexDAO;
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
		/*update Homeostasis index model*/
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		/*backup last session's data*/
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(fitnessHomeostasisIndexBean.getCurrentTotalLoadOfExercise());
		fitnessHomeostasisIndexBean.setPreviousEndTime(fitnessHomeostasisIndexBean.getCurrentEndTime());
		/*set current session's data*/
		Double currentTotalLoadOfExercise = ShapeIndexAlgorithm.calculateTotalLoadofExercise(fitnessTrainingSessionBean.getTimeDistributionOfHRZ());
		fitnessHomeostasisIndexBean.setCurrentTotalLoadOfExercise(currentTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setCurrentEndTime(fitnessTrainingSessionBean.getEndTime());
		/*set supercompensation status*/
		fitnessHomeostasisIndexBean.setSupercompensationStatus(FitnessHomeostasisIndexBean.UNCOMPENSATED);
		/*set HI local regression minimum*/
		Double regressedHomeostasisIndex = ShapeIndexAlgorithm.getRegressedHomeostasisIndex(fitnessHomeostasisIndexBean.getTraineeClassification(),fitnessHomeostasisIndexBean.getPreviousEndTime() ,fitnessHomeostasisIndexBean.getPreviousTotalLoadOfExercise());
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(ShapeIndexAlgorithm.getRegressionMinimumOfHomeostasisIndex(regressedHomeostasisIndex, currentTotalLoadOfExercise));
		fitnessHomeostasisIndexDAO.updateHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		
		/*update speed-heartrate model*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		/*backup last session's data*/
		fitnessSpeedHeartRateBean.setPreviousVdot(fitnessSpeedHeartRateBean.getCurrentVdot());
		/*set last session's data*/
		fitnessSpeedHeartRateBean.setCurrentVdot(ShapeIndexAlgorithm.calculateVdot(fitnessTrainingSessionBean.getSpeedDistributionOfHRZ()));
		fitnessSpeedHeartRateDAO.updateSpeedHeartrateModel(fitnessSpeedHeartRateBean);
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
		/*Check condition for supercompensation*/ 
		if(0 == regressedHomeostasisIndex && FitnessHomeostasisIndexBean.UNCOMPENSATED == fitnessHomeostasisIndexBean.getSupercompensationStatus() ){			
			supercompensationPoints = ShapeIndexAlgorithm.calculateSupercompensationPoints(fitnessHomeostasisIndexBean.getTraineeClassification(), fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
			fitnessHomeostasisIndexBean.setSupercompensationStatus(FitnessHomeostasisIndexBean.COMPENSATED);
			fitnessHomeostasisIndexDAO.updateHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		} 
		return supercompensationPoints;
	}
		
	public double getFitnessDetrainingPenalty(String userid){
		double detrainingPenalty = 0.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		detrainingPenalty = ShapeIndexAlgorithm.calculateDetrainingPenalty(fitnessHomeostasisIndexBean.getTraineeClassification(), fitnessHomeostasisIndexBean.getCurrentEndTime(), fitnessHomeostasisIndexBean.getCurrentTotalLoadOfExercise());
		return detrainingPenalty;
	}
	
	public double getSpeedHeartrateFactor(String userid){
		double speedHeartrateFactor = 0.0;
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		if(null != fitnessSpeedHeartRateBean.getCurrentVdot() && null != fitnessSpeedHeartRateBean.getPreviousVdot()){
			speedHeartrateFactor = ShapeIndexAlgorithm.calculateCompoundedVdot(fitnessSpeedHeartRateBean.getCurrentVdot(), fitnessSpeedHeartRateBean.getPreviousVdot());
		}
		return speedHeartrateFactor;
	}

}