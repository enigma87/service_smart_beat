/**
 * 
 */
package com.genie.smartbeat.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessHeartrateZoneBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessSpeedHeartRateBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.dao.FitnessHeartrateTestDAO;
import com.genie.smartbeat.dao.FitnessHeartrateZoneDAO;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessSpeedHeartRateDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;
import com.genie.smartbeat.util.SmartbeatIDGenerator;
import com.genie.social.beans.UserBean;
import com.genie.social.core.UserManager;

/**
 * @author dhasarathy
 *
 */
public class FitnessManagerMySQLImpl implements FitnessManager 
{

	//@Autowired
	//@Qualifier("userManagerMySQLImpl")
	private UserManager userManager;
	private FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
	private FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO;
	private FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO;
	private FitnessShapeIndexDAO fitnessShapeIndexDAO;
	private FitnessHeartrateTestDAO fitnessHeartrateTestDAO;
	private FitnessHeartrateZoneDAO fitnessHeartrateZoneDAO;
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public UserManager getUserManager() {
		return userManager;
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
	
	public FitnessHeartrateTestDAO getFitnessHeartrateTestDAO() {
		return fitnessHeartrateTestDAO;
	}
	
	public void setFitnessHeartrateTestDAO(
			FitnessHeartrateTestDAO fitnessHeartrateTestDAO) {
		this.fitnessHeartrateTestDAO = fitnessHeartrateTestDAO;
	}
	
	public FitnessHeartrateZoneDAO getFitnessHeartrateZoneDAO() {
		return fitnessHeartrateZoneDAO;
	}
	
	public void setFitnessHeartrateZoneDAO(
			FitnessHeartrateZoneDAO fitnessHeartrateZoneDAO) {
		this.fitnessHeartrateZoneDAO = fitnessHeartrateZoneDAO;
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
	
	public FitnessTrainingSessionBean getTrainingSessionById(String fitnessTrainingSessionId) {
		return fitnessTrainingSessionDAO.getFitnessTrainingSessionById(fitnessTrainingSessionId);
	}

	public void updateShapeIndexModel(String userid, FitnessTrainingSessionBean fitnessTrainingSessionBean, String previousTrainingSessionId){
		double shapeIndex;
		FitnessShapeIndexBean shapeIndexBean = new FitnessShapeIndexBean();
		if(null != previousTrainingSessionId){
			/*update shape index*/
			shapeIndex = getShapeIndex(previousTrainingSessionId);
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
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
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
		fitnessHomeostasisIndexBean.setTraineeClassification(ShapeIndexAlgorithm.getTraineeClassificationUsingVdot(fitnessSpeedHeartRateBean.getCurrentVdot()));
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
		
		/*set default surfaceIndex if not present*/
		if(null == fitnessTrainingSessionBean.getSurfaceIndex()){
			fitnessTrainingSessionBean.setSurfaceIndex(ShapeIndexAlgorithm.RUNNING_SURFACE_TRACK_PAVED);
		}
		
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
		fitnessSpeedHeartRateBean.setCurrentVdot(ShapeIndexAlgorithm.calculateVdot(fitnessTrainingSessionBean.getSpeedDistributionOfHRZ(), fitnessTrainingSessionBean.getSurfaceIndex()));
		fitnessSpeedHeartRateDAO.updateSpeedHeartrateModel(fitnessSpeedHeartRateBean);
	}
	
	public void deleteFitnessTrainingSessionbyTrainingSessionId(String fitnessTrainingSessionId){
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(fitnessTrainingSessionId);
	}

	public double getShapeIndex(String recentTrainingSessionId) {
		double newShapeIndex = 0;
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat shapeIndexFormat = new DecimalFormat("###.##",symbols);
		
		if(null != recentTrainingSessionId){
		FitnessShapeIndexBean fitnessShapeIndexBean = fitnessShapeIndexDAO.getShapeIndexModelByTrainingSessionId(recentTrainingSessionId);
		newShapeIndex = fitnessShapeIndexBean.getShapeIndex()
				+ getFitnessSupercompensationPoints(fitnessShapeIndexBean.getUserid())
				+ getSpeedHeartrateFactor(fitnessShapeIndexBean.getUserid())
				- getFitnessDetrainingPenalty(fitnessShapeIndexBean.getUserid());
		}else{
			newShapeIndex = ShapeIndexAlgorithm.SHAPE_INDEX_INITIAL_VALUE;
		}
		
         return Double.valueOf(shapeIndexFormat.format(newShapeIndex));
	}

	public double getFitnessSupercompensationPoints(String userid){
		double supercompensationPoints = 0.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		if (null != fitnessHomeostasisIndexBean) {
				double regressedHomeostasisIndex = ShapeIndexAlgorithm.getRegressedHomeostasisIndex(
						fitnessHomeostasisIndexBean.getTraineeClassification(),
						fitnessHomeostasisIndexBean.getRecentEndTime(),
						fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		
				/*Check condition for supercompensation*/ 
		
				if(0 == regressedHomeostasisIndex){			
					supercompensationPoints = ShapeIndexAlgorithm.calculateSupercompensationPoints(fitnessHomeostasisIndexBean.getTraineeClassification(), 
							fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());			
				}
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
	
	public double getOrthostaticHeartrateFactor(String userid){
		double orthostaticHeartrateFactor = 0.0;
		int numberOfSOHRTests = fitnessHeartrateTestDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		if(ShapeIndexAlgorithm.SOHR_STABILIZATION_LIMIT < numberOfSOHRTests){
			List<FitnessHeartrateTestBean> sohrTimeSeries = fitnessHeartrateTestDAO.getNRecentHeartRateTestsForUserByType(userid, 
					ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC, 
					numberOfSOHRTests - ShapeIndexAlgorithm.SOHR_DAY_OF_RECORD_SERIES_LIMIT);
			double[][] dayOfRecordSOHRSeries = new double[sohrTimeSeries.size()][2];
			int index = 0;
			for(Iterator<FitnessHeartrateTestBean> i = sohrTimeSeries.iterator(); i.hasNext();index++){
				FitnessHeartrateTestBean bean = i.next();				
				dayOfRecordSOHRSeries[index][0] = bean.getDayOfRecord();
				dayOfRecordSOHRSeries[index][1] = bean.getHeartrate();
			}
			orthostaticHeartrateFactor = ShapeIndexAlgorithm.calculateSlopeOfTimeRegressionOfStandingOrthostaticHeartRate(dayOfRecordSOHRSeries);
		}		
		return orthostaticHeartrateFactor;
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
		
		String userid = fitnessHeartrateTestBean.getUserid();
	    FitnessHeartrateTestBean previousHeartrateTestBean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, fitnessHeartrateTestBean.getHeartrateType());
		if(null != previousHeartrateTestBean){
			fitnessHeartrateTestBean.setHeartrateTestId(SmartbeatIDGenerator.getNextId(previousHeartrateTestBean.getHeartrateTestId()));
			Long differenceInDays = (fitnessHeartrateTestBean.getTimeOfRecord().getTime() - previousHeartrateTestBean.getTimeOfRecord().getTime())/(24*60*60*1000);
			Integer latestDayOfRecord = previousHeartrateTestBean.getDayOfRecord() + differenceInDays.intValue();
			fitnessHeartrateTestBean.setDayOfRecord(latestDayOfRecord);
			if(fitnessHeartrateTestBean.getHeartrateType() != ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC){
				fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(previousHeartrateTestBean.getHeartrateTestId());
				/*update heartrate zone model*/
				updateHeartrateZoneModel(fitnessHeartrateTestBean.getUserid());
			}
		}else{
			fitnessHeartrateTestBean.setHeartrateTestId(SmartbeatIDGenerator.getFirstId(userid, SmartbeatIDGenerator.MARKER_HEARTRATE_TEST_ID));
			fitnessHeartrateTestBean.setDayOfRecord(1);
		}
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean);
	}
	
	public void updateHeartrateZoneModel(String userid){
		FitnessHeartrateTestBean restingHeartrateTestBean 	= fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		FitnessHeartrateTestBean thresholdHeartrateTestBean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD);
		FitnessHeartrateTestBean maximalHeartrateTestBean 	= fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
		
		if(null != restingHeartrateTestBean && null != thresholdHeartrateTestBean && null != maximalHeartrateTestBean){
			double[][] heartrateZones = ShapeIndexAlgorithm.calculateHeartrateZones(restingHeartrateTestBean.getHeartrate(), thresholdHeartrateTestBean.getHeartrate(), maximalHeartrateTestBean.getHeartrate());
			FitnessHeartrateZoneBean fitnessHeartrateZoneBean = fitnessHeartrateZoneDAO.getHeartrateZoneModelByUserid(userid);
			if(null == fitnessHeartrateZoneBean){
				fitnessHeartrateZoneBean = new FitnessHeartrateZoneBean();
				fitnessHeartrateZoneBean.setUserid(userid);
				fitnessHeartrateZoneBean.setHeartrateZones(heartrateZones);
				fitnessHeartrateZoneDAO.createHeartrateZoneModel(fitnessHeartrateZoneBean);
			}else{
				fitnessHeartrateZoneBean.setHeartrateZones(heartrateZones);
				fitnessHeartrateZoneDAO.updateHeartrateZoneModel(fitnessHeartrateZoneBean);
			}
		}
	}

	public double[][] getHeartrateZones(String userid) {
		double[][] heartrateZones = null;
		double restingHeartrate = 0.0, thresholdHeartrate = 0.0, maximalHeartrate = 0.0;
		FitnessHeartrateTestBean restingHeartrateTestBean 	= fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		FitnessHeartrateTestBean thresholdHeartrateTestBean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD);
		FitnessHeartrateTestBean maximalHeartrateTestBean 	= fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
		if(null != restingHeartrateTestBean && null != thresholdHeartrateTestBean && null != maximalHeartrateTestBean){
			restingHeartrate 	= restingHeartrateTestBean.getHeartrate();
			thresholdHeartrate 	= thresholdHeartrateTestBean.getHeartrate();
			maximalHeartrate 	= maximalHeartrateTestBean.getHeartrate();			
		}else{
			Integer traineeClassification = fitnessHomeostasisIndexDAO.getTraineeClassificationByUserid(userid);
			if(null == traineeClassification){
				traineeClassification = new Integer(ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_UNTRAINED);
			}
			UserBean user = userManager.getUserInformation(userid);
			double[] rmtHeartrates = ShapeIndexAlgorithm.getDefaultRMTHeartrates(traineeClassification, user.getAge(), user.getGender());
			restingHeartrate 	= rmtHeartrates[0];
			thresholdHeartrate 	= rmtHeartrates[1];
			maximalHeartrate 	= rmtHeartrates[2];
		}
		heartrateZones = ShapeIndexAlgorithm.calculateHeartrateZones(restingHeartrate, thresholdHeartrate, maximalHeartrate);
		return heartrateZones;
	}
	
	public List<String> getTrainingSessionIdsInTimeInterval(String userID, Timestamp startTimestamp, Timestamp endTimestamp) {
		return fitnessTrainingSessionDAO.getFitnessTrainingSessionByTimeRange(userID, startTimestamp, endTimestamp);
	}

	public List<FitnessShapeIndexBean> getShapeIndexHistoryInTimeInterval(String userid, Timestamp startTime, Timestamp endTime) {
		return fitnessShapeIndexDAO.getShapeIndexHistoryDuringInterval(userid, startTime, endTime);
	}
}