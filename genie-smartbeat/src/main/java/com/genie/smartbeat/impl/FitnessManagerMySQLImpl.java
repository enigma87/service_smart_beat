/**
 * 
 */
package com.genie.smartbeat.impl;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessSpeedHeartRateBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessSpeedHeartRateDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;
import com.genie.smartbeat.util.SmartbeatIDGenerator;

/**
 * @author dhasarathy
 *
 */
public class FitnessManagerMySQLImpl implements FitnessManager 
{

	private FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
	private FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO;
	private FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO;
	private FitnessShapeIndexDAO fitnessShapeIndexDAO;
	
	
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
	
	public void saveFitnessTrainingSession(FitnessTrainingSessionBean fitnessTrainingSessionBean) {
		
		String userid = fitnessTrainingSessionBean.getUserid();		
		String trainingSessionId = null, previousTrainingSessionId = null;		
		FitnessTrainingSessionBean previousTrainingSession = fitnessTrainingSessionDAO.getRecentFitnessTrainingSessionForUser(userid);
		if(null != previousTrainingSession){
			/*generate first training session id*/
			trainingSessionId = SmartbeatIDGenerator.getNextId(previousTrainingSession.getTrainingSessionId());
			/*save previous training session id for updating shape index*/
			previousTrainingSessionId = previousTrainingSession.getTrainingSessionId();
		}else{
			/*generate training session id from previous session id*/
			trainingSessionId = SmartbeatIDGenerator.getFirstId(userid, SmartbeatIDGenerator.MARKER_TRAINING_SESSION_ID);			
		}
		fitnessTrainingSessionBean.setTrainingSessionId(trainingSessionId);
		
		/*update shape index model*/
		updateShapeIndexModel(userid, fitnessTrainingSessionBean, previousTrainingSessionId);
		
		/*update speed-heartrate model*/
		updateSpeedHeartRateModel(userid, fitnessTrainingSessionBean);
		
		/*update homeostasis index model*/
		updateHomeostasisIndexModel(userid, fitnessTrainingSessionBean);
		
		/*save training session*/		
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);

	}
	
	public void updateShapeIndexModel(String userid, FitnessTrainingSessionBean fitnessTrainingSessionBean, String previousTrainingSessionId){
		double shapeIndex;
		FitnessShapeIndexBean shapeIndexBean = new FitnessShapeIndexBean();
		if(null != previousTrainingSessionId){
			/*update shape index*/
			shapeIndex = getFitnessShapeIndex(previousTrainingSessionId);
		}else{
			/*get initial shape index*/
			shapeIndex = ShapeIndexAlgorithm.SHAPE_INDEX_INITIAL_VALUE;
		}
		shapeIndexBean.setUserid(userid);
		shapeIndexBean.setShapeIndex(shapeIndex);
		shapeIndexBean.setTimeOfRecord(fitnessTrainingSessionBean.getEndTime());
		shapeIndexBean.setSessionOfRecord(fitnessTrainingSessionBean.getTrainingSessionId());
		/*save shape index model*/
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(shapeIndexBean);
	}
	
	public void updateHomeostasisIndexModel(String userid, FitnessTrainingSessionBean fitnessTrainingSessionBean){
		
		double regressedHomeostasisIndex = 0.0;
		Double recentMinimumOfHomeostasisIndex = 0.0;
		Double localRegressionMinimumOfHomeostasisIndex = 0.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		if (null != fitnessHomeostasisIndexBean){
			/*backup last session's data*/
			localRegressionMinimumOfHomeostasisIndex = fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex();
			regressedHomeostasisIndex = ShapeIndexAlgorithm.getRegressedHomeostasisIndex(fitnessHomeostasisIndexBean.getTraineeClassification(),fitnessHomeostasisIndexBean.getRecentEndTime() ,fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		}else{
			/*creating Homeostasis Index Model for the user*/
			fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
			/*Setting the userid while the previousTotalLoadOfExercise and PreviousEndTime by default is set to null*/
			fitnessHomeostasisIndexBean.setUserid(userid);
			fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		}
		/*set current session's data*/
		
		Double recentTotalLoadOfExercise = ShapeIndexAlgorithm.calculateTotalLoadofExercise(fitnessTrainingSessionBean.getTimeDistributionOfHRZ());
		recentMinimumOfHomeostasisIndex = regressedHomeostasisIndex - recentTotalLoadOfExercise;
		if (recentMinimumOfHomeostasisIndex < localRegressionMinimumOfHomeostasisIndex || regressedHomeostasisIndex == 0.0){
			localRegressionMinimumOfHomeostasisIndex = recentMinimumOfHomeostasisIndex;
		}
		fitnessHomeostasisIndexBean.setRecentTotalLoadOfExercise(recentTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setRecentEndTime(fitnessTrainingSessionBean.getEndTime());	
		fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(recentMinimumOfHomeostasisIndex);
		/*set HI local regression minimum*/
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(localRegressionMinimumOfHomeostasisIndex);
        fitnessHomeostasisIndexDAO.updateHomeostasisIndexModel(fitnessHomeostasisIndexBean);
	}
	
	
	public void updateSpeedHeartRateModel(String userid, FitnessTrainingSessionBean fitnessTrainingSessionBean){
		
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		/*backup last session's data*/
		if(null != fitnessSpeedHeartRateBean ){
			fitnessSpeedHeartRateBean.setPreviousVdot(fitnessSpeedHeartRateBean.getCurrentVdot());
		}else{
			/*Create Speed Heart Rate Model for the user*/
			fitnessSpeedHeartRateBean = new FitnessSpeedHeartRateBean();
			/*Setting the userid while the PreviousVdot value by default is set to null*/
			fitnessSpeedHeartRateBean.setUserid(userid);
			fitnessSpeedHeartRateDAO.createSpeedHeartRateModel(fitnessSpeedHeartRateBean);
		}
		
		/*set last session's data*/
		fitnessSpeedHeartRateBean.setCurrentVdot(ShapeIndexAlgorithm.calculateVdot(fitnessTrainingSessionBean.getSpeedDistributionOfHRZ()));
		fitnessSpeedHeartRateDAO.updateSpeedHeartrateModel(fitnessSpeedHeartRateBean);
	}
	
	public void deleteFitnessTrainingSessionbyTrainingSessionId(String fitnessTrainingSessionId){
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(fitnessTrainingSessionId);
	}

	public double getFitnessShapeIndex(String recentTrainingSessionId) {
		FitnessShapeIndexBean fitnessShapeIndexBean = fitnessShapeIndexDAO.getShapeIndexModelByTrainingSessionId(recentTrainingSessionId);
		double newShapeIndex = fitnessShapeIndexBean.getShapeIndex()
				+ getFitnessSupercompensationPoints(fitnessShapeIndexBean.getUserid())
				+ getSpeedHeartrateFactor(fitnessShapeIndexBean.getUserid())
				- getFitnessDetrainingPenalty(fitnessShapeIndexBean.getUserid());
		
		return newShapeIndex;
	}
	
	public double getFitnessSupercompensationPoints(String userid){
		double supercompensationPoints = 0.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		double regressedHomeostasisIndex = ShapeIndexAlgorithm.getRegressedHomeostasisIndex(fitnessHomeostasisIndexBean.getTraineeClassification(), 
				fitnessHomeostasisIndexBean.getRecentEndTime(), 
				fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		/*Check condition for supercompensation*/ 
		
		if(0 == regressedHomeostasisIndex){			
			supercompensationPoints = ShapeIndexAlgorithm.calculateSupercompensationPoints(fitnessHomeostasisIndexBean.getTraineeClassification(), 
					fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());			
		}
		return supercompensationPoints;
	}
		
	public double getFitnessDetrainingPenalty(String userid){
		double detrainingPenalty = 0.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		detrainingPenalty = ShapeIndexAlgorithm.calculateDetrainingPenalty(fitnessHomeostasisIndexBean.getTraineeClassification(), 
				fitnessHomeostasisIndexBean.getRecentEndTime(), 
				fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		return detrainingPenalty;
	}
	
	public double getSpeedHeartrateFactor(String userid){
		double speedHeartrateFactor = 0.0;
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		if(null != fitnessSpeedHeartRateBean.getCurrentVdot() && null != fitnessSpeedHeartRateBean.getPreviousVdot()){
			speedHeartrateFactor = ShapeIndexAlgorithm.calculateCompoundedVdot(fitnessSpeedHeartRateBean.getCurrentVdot(), 
					fitnessSpeedHeartRateBean.getPreviousVdot());
		}
		return speedHeartrateFactor;
	}
	
	public String getRecentTrainingSessionId(String userid){
		
		String recentTrainingSessionId = null;
		
		FitnessTrainingSessionBean fitnessTrainingSessionBean = fitnessTrainingSessionDAO.getRecentFitnessTrainingSessionForUser(userid);
		if(null != fitnessTrainingSessionBean){
			recentTrainingSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
		}
		return recentTrainingSessionId;
	}

	public void saveHeartrateTest(FitnessHeartrateTestBean fitnessHeartrateTestBean) {
		// TODO Auto-generated method stub
		
	}

}