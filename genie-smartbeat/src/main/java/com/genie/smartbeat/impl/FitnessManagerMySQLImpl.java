/**
 * 
 */
package com.genie.smartbeat.impl;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeUtils;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionIdBean;
import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.core.exceptions.homeostasis.AbsenceOfHomeostasisIndexModelException;
import com.genie.smartbeat.core.exceptions.homeostasis.HomeostasisIndexModelException;
import com.genie.smartbeat.core.exceptions.session.AbsenceOfTrainingSessionException;
import com.genie.smartbeat.core.exceptions.session.InvalidSpeedDistributionException;
import com.genie.smartbeat.core.exceptions.session.InvalidTimeDistributionException;
import com.genie.smartbeat.core.exceptions.session.TrainingSessionException;
import com.genie.smartbeat.core.exceptions.test.HeartrateTestException;
import com.genie.smartbeat.core.exceptions.test.InvalidHeartrateException;
import com.genie.smartbeat.core.exceptions.time.InvalidDurationException;
import com.genie.smartbeat.core.exceptions.time.InvalidEndTimestampException;
import com.genie.smartbeat.core.exceptions.time.InvalidStartTimestampException;
import com.genie.smartbeat.core.exceptions.time.InvalidTimestampException;
import com.genie.smartbeat.core.exceptions.time.InvalidTimestampInChronologyException;
import com.genie.smartbeat.core.exceptions.time.TimeException;
import com.genie.smartbeat.dao.FitnessHeartrateTestDAO;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;
import com.genie.smartbeat.util.DoubleValueFormatter;
import com.genie.smartbeat.util.SmartbeatIDGenerator;
import com.genie.social.beans.UserBean;
import com.genie.social.core.UserManager;

/**
 * @author dhasarathy
 *
 */
public class FitnessManagerMySQLImpl implements FitnessManager 
{

	private static final Logger log = Logger.getLogger(FitnessManagerMySQLImpl.class);
	private UserManager userManager;
	private FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
	private FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO;
	private FitnessShapeIndexDAO fitnessShapeIndexDAO;
	private FitnessHeartrateTestDAO fitnessHeartrateTestDAO;	
	
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
	
	public void saveFitnessTrainingSession(FitnessTrainingSessionBean fitnessTrainingSessionBean) throws TrainingSessionException, TimeException{		
		String userid = fitnessTrainingSessionBean.getUserid();		
		String trainingSessionId = null, previousTrainingSessionId = null;		
		
		log.info("Trying to save training session " + fitnessTrainingSessionBean.toString());
		if(ShapeIndexAlgorithm.MINIMUM_SESSION_DURATION > fitnessTrainingSessionBean.getSessionDuration()){
			log.info("Invalid duration");
			throw new InvalidTimestampException();
		}
		FitnessTrainingSessionBean previousTrainingSession = fitnessTrainingSessionDAO.getRecentFitnessTrainingSessionForUser(userid);
		if(null != previousTrainingSession){
			/*check validity of new session in chronology */
			if(previousTrainingSession.getEndTime().getTime() >= fitnessTrainingSessionBean.getStartTime().getTime()){								
				throw new InvalidTimestampInChronologyException();
			}
			/*generate first training session id*/
			trainingSessionId = SmartbeatIDGenerator.getNextId(previousTrainingSession.getTrainingSessionId());
			/*save previous training session id for updating shape index*/
			previousTrainingSessionId = previousTrainingSession.getTrainingSessionId();
		}else{
			/*generate training session id from previous session id*/
			trainingSessionId = SmartbeatIDGenerator.getFirstId(userid, SmartbeatIDGenerator.MARKER_TRAINING_SESSION_ID);			
		}						
		// set the generated id to bean 
		fitnessTrainingSessionBean.setTrainingSessionId(trainingSessionId);			
		/*update shape index model*/
		updateShapeIndexModel(userid, fitnessTrainingSessionBean, previousTrainingSessionId);
	
		/*update speed-heartrate model*/				
		updateSpeedHeartRateModel(userid, fitnessTrainingSessionBean, previousTrainingSession);
	
		/*always update SH model before HI model as HI model needs incoming session's vdot*/
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
		if(null != previousTrainingSessionId && !previousTrainingSessionId.isEmpty()){
			/*update shape index*/
			shapeIndex = getShapeIndex(previousTrainingSessionId,fitnessTrainingSessionBean);
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
	
	public void updateHomeostasisIndexModel(String userid, FitnessTrainingSessionBean fitnessTrainingSessionBean) throws InvalidTimeDistributionException{
		
		/* check time distribution validity*/
		double[] timeDistributionOfHRZ = fitnessTrainingSessionBean.getTimeDistributionOfHRZ();
		boolean distributionValid = false;
		if(null != timeDistributionOfHRZ){
			int nullZoneCount = 0;						
			for(int i = 0; i< (FitnessTrainingSessionBean.NUMBER_OF_ZONES + 1); i++){
				if(timeDistributionOfHRZ[i] < ShapeIndexAlgorithm.MINIMUM_ZONE_TIME){
					timeDistributionOfHRZ[i] = 0.0;
				}						
				if(0 >= timeDistributionOfHRZ[i]){
					nullZoneCount++;
				}		
			}
			if((FitnessTrainingSessionBean.NUMBER_OF_ZONES + 1)  > nullZoneCount){
				distributionValid = true;
			}
		}		
		if(false == distributionValid){
			log.info("Invalid time distribution");
			throw new InvalidTimeDistributionException();			
		}
		
		double regressedHomeostasisIndex = 0.0;
		Double recentMinimumOfHomeostasisIndex = 0.0;
		Double localRegressionMinimumOfHomeostasisIndex = 0.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		if (null != fitnessHomeostasisIndexBean){
			/*backup last session's data*/
			localRegressionMinimumOfHomeostasisIndex = fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex();
			regressedHomeostasisIndex = ShapeIndexAlgorithm.getRegressedHomeostasisIndex(fitnessHomeostasisIndexBean.getTraineeClassification(),
										fitnessHomeostasisIndexBean.getRecentEndTime() ,
										fitnessTrainingSessionBean.getStartTime(),
										fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		}else{
			/*creating Homeostasis Index Model for the user*/
			fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
			/*Setting the userid while the recentTotalLoadOfExercise and recentEndTime by default is set to null*/
			fitnessHomeostasisIndexBean.setUserid(userid);
			fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		}
		/*set current session's data*/
		UserBean user = userManager.getUserInformation(userid);
		fitnessHomeostasisIndexBean.setTraineeClassification(ShapeIndexAlgorithm.getTraineeClassificationUsingVdot(user.getGender(),fitnessTrainingSessionBean.getVdot()));
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
	
	
	public void updateSpeedHeartRateModel(String userid, FitnessTrainingSessionBean fitnessTrainingSessionBean, FitnessTrainingSessionBean previousTrainingSessionBean) throws InvalidSpeedDistributionException{
				
		/*check validity of speed distribution*/
		double[] speedDistributionOfHRZ = fitnessTrainingSessionBean.getSpeedDistributionOfHRZ();		
		boolean distributionValid = false;
		if(null != speedDistributionOfHRZ){			
			int nullZoneCount = 0;			
			for(int i = 0; i< (FitnessTrainingSessionBean.NUMBER_OF_ZONES + 1); i++){
				if(speedDistributionOfHRZ[i] < ShapeIndexAlgorithm.MINIMUM_ZONE_SPEED || 
						speedDistributionOfHRZ[i] > ShapeIndexAlgorithm.MAXIMUM_ZONE_SPEED){
					speedDistributionOfHRZ[i] = 0.0;
				}
				if(0.0 >= speedDistributionOfHRZ[i]){
					nullZoneCount++;
				}
			}
			if((FitnessTrainingSessionBean.NUMBER_OF_ZONES + 1) > nullZoneCount){
				distributionValid = true;
			}
		}		
		if(false == distributionValid){
			log.info("Invalid speed distribution");
			throw new InvalidSpeedDistributionException();			
		}
		
		/*get surface index*/
		int surfaceIndex = ShapeIndexAlgorithm.RUNNING_SURFACE_TRACK_PAVED;				
		if(null != fitnessTrainingSessionBean.getSurfaceIndex()){
			surfaceIndex = fitnessTrainingSessionBean.getSurfaceIndex();
		}
		
		/*get previous session's vdot for altitude speed correction factor calculation*/
		double previousVdot = 0.0;
		if(null != previousTrainingSessionBean){
			previousVdot = previousTrainingSessionBean.getVdot();
		}
		
		/*get and set this session's vdot*/
		double vdot = ShapeIndexAlgorithm.calculateVdot(fitnessTrainingSessionBean.getSpeedDistributionOfHRZ(),
												surfaceIndex,
												fitnessTrainingSessionBean.getAsDoubleValueAverageAltitude(),
												fitnessTrainingSessionBean.getAsDoubleValueExtraLoad(),
												previousVdot);
		fitnessTrainingSessionBean.setVdot(vdot);
	}
	
	public void deleteFitnessTrainingSessionbyTrainingSessionId(String fitnessTrainingSessionId){
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(fitnessTrainingSessionId);
	}
	
	public double getShapeIndex(String recentTrainingSessionId) {
		Timestamp timeAtConsideration = new Timestamp(DateTimeUtils.currentTimeMillis());
		return getShapeIndexAtTime(recentTrainingSessionId, timeAtConsideration);
	}
	
	public double getShapeIndex(String recentTrainingSessionId, FitnessTrainingSessionBean newlyArrivedTrainingSession) {
		Timestamp timeAtConsideration = null;
		if(null != newlyArrivedTrainingSession){
			timeAtConsideration = newlyArrivedTrainingSession.getStartTime();
		}else{
			timeAtConsideration = new Timestamp(DateTimeUtils.currentTimeMillis());
		}
		return getShapeIndexAtTime(recentTrainingSessionId, timeAtConsideration);		
	}

	private double getShapeIndexAtTime(String recentTrainingSessionId, Timestamp timeAtConsideration){
		double newShapeIndex = 0;
		FitnessShapeIndexBean fitnessShapeIndexBean = null;
		
		if(null != recentTrainingSessionId && !recentTrainingSessionId.isEmpty()){
			fitnessShapeIndexBean = fitnessShapeIndexDAO.getShapeIndexModelByTrainingSessionId(recentTrainingSessionId);			
		}
		
		if (null != fitnessShapeIndexBean) {
			String userid = fitnessShapeIndexBean.getUserid();
			newShapeIndex = 
				(fitnessShapeIndexBean.getShapeIndex()*getSpeedHeartrateFactor(userid))
				+ getFitnessSupercompensationPoints(userid, timeAtConsideration)
				+ getOrthostaticHeartrateFactor(userid)
				- getFitnessDetrainingPenalty(userid, timeAtConsideration);
		}else{
			newShapeIndex = ShapeIndexAlgorithm.SHAPE_INDEX_INITIAL_VALUE;
		}					
		return DoubleValueFormatter.format3Dot2(newShapeIndex);

	}
	public double getFitnessSupercompensationPoints(String userid, Timestamp timeAtConsideration){
		double supercompensationPoints = 0.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		if (null != fitnessHomeostasisIndexBean) {
				double regressedHomeostasisIndex = ShapeIndexAlgorithm.getRegressedHomeostasisIndex(
						fitnessHomeostasisIndexBean.getTraineeClassification(),
						fitnessHomeostasisIndexBean.getRecentEndTime(),
						timeAtConsideration,
						fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		
				/*Check condition for supercompensation*/
				if(0 == regressedHomeostasisIndex){			
					supercompensationPoints = ShapeIndexAlgorithm.calculateSupercompensationPoints(fitnessHomeostasisIndexBean.getTraineeClassification(), 
							fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());			
				}
			}
		return supercompensationPoints;
	}
		
	public double getFitnessDetrainingPenalty(String userid, Timestamp timeAtConsideration){
		double detrainingPenalty = 0.0;
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		if (null != fitnessHomeostasisIndexBean) {
			detrainingPenalty = ShapeIndexAlgorithm.calculateDetrainingPenalty(fitnessHomeostasisIndexBean.getTraineeClassification(), 
				fitnessHomeostasisIndexBean.getRecentEndTime(),
				timeAtConsideration,
				fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		}
		return detrainingPenalty;
	}
	
	public double getSpeedHeartrateFactor(String userid){
		double speedHeartrateFactor = 1.0;
		double[] vdotHistory = fitnessTrainingSessionDAO.getVdotHistory(userid, ShapeIndexAlgorithm.VDOT_HISTORY_LIMIT);		
		if(null != vdotHistory){			
			speedHeartrateFactor = ShapeIndexAlgorithm.calculateSpeedHeartrateFactor(vdotHistory);
		}
		return speedHeartrateFactor;
	}
	
	public double getOrthostaticHeartrateFactor(String userid){
		double orthostaticHeartrateFactor = 0.0;
		int numberOfSOHRTests = fitnessHeartrateTestDAO.getNumberOfHeartRateTestsForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		if(ShapeIndexAlgorithm.SOHR_STABILIZATION_LIMIT < numberOfSOHRTests){
			int recentTestsToQuery;
			int cutOffLimit = ShapeIndexAlgorithm.SOHR_DAY_OF_RECORD_SERIES_LIMIT + ShapeIndexAlgorithm.SOHR_STABILIZATION_LIMIT;
			if(cutOffLimit < numberOfSOHRTests){
				recentTestsToQuery = ShapeIndexAlgorithm.SOHR_DAY_OF_RECORD_SERIES_LIMIT;
			}else{
				recentTestsToQuery = numberOfSOHRTests - ShapeIndexAlgorithm.SOHR_STABILIZATION_LIMIT;
			}
			List<FitnessHeartrateTestBean> sohrTimeSeries = fitnessHeartrateTestDAO.getNRecentHeartRateTestsForUserByTypeWithOffset(userid, 
					ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC, 
					ShapeIndexAlgorithm.SOHR_STABILIZATION_LIMIT,
					recentTestsToQuery);
			double[][] dayOfRecordSOHRSeries = new double[sohrTimeSeries.size()][2];
			int index = 0;
			for(Iterator<FitnessHeartrateTestBean> i = sohrTimeSeries.iterator(); i.hasNext();index++){
				FitnessHeartrateTestBean bean = i.next();				
				dayOfRecordSOHRSeries[index][0] = bean.getDayOfRecord();
				dayOfRecordSOHRSeries[index][1] = bean.getHeartrate();
			}
			orthostaticHeartrateFactor = ShapeIndexAlgorithm.calculateOrthostaticHeartrateFactor(dayOfRecordSOHRSeries);
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

	public void saveHeartrateTest(FitnessHeartrateTestBean fitnessHeartrateTestBean) throws HeartrateTestException, TimeException{
		
		String userid = fitnessHeartrateTestBean.getUserid();		
		/*timestamp validation*/
		if(null == fitnessHeartrateTestBean.getTimeOfRecord()){
			throw new InvalidTimestampException();
		}
		/*null heart rate validation*/
		if((null == fitnessHeartrateTestBean.getHeartrate()) || (0.0 >= fitnessHeartrateTestBean.getHeartrate())){
			throw new InvalidHeartrateException();
		}
		/*resting heartrate validation*/
		if(ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING.equals(fitnessHeartrateTestBean.getHeartrateType())){
			if(ShapeIndexAlgorithm.MINIMUM_RESTING_HEARTRATE > fitnessHeartrateTestBean.getHeartrate()){
				throw new InvalidHeartrateException();
			}
			
			FitnessHeartrateTestBean restingHeartrateTestBean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
			if(null != restingHeartrateTestBean && restingHeartrateTestBean.getHeartrate() < fitnessHeartrateTestBean.getHeartrate()){
				/*hold this logic for now*/
				log.info("resting heart rate for user " + fitnessHeartrateTestBean.getUserid() + "greater than recorded minimum");
				return;
			}
		}			
		/*threshold heart rate validation*/
		if(ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD.equals(fitnessHeartrateTestBean.getHeartrateType())){
			FitnessHeartrateTestBean maximalHeartrateTestBean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, 
																						ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
			if(null != maximalHeartrateTestBean && maximalHeartrateTestBean.getHeartrate() < fitnessHeartrateTestBean.getHeartrate()){
				throw new InvalidHeartrateException();
			}
		}			
	    FitnessHeartrateTestBean previousHeartrateTestBean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUser(userid);
		if(null != previousHeartrateTestBean){
			/*chronology validation*/
			if(previousHeartrateTestBean.getTimeOfRecord().getTime() > fitnessHeartrateTestBean.getTimeOfRecord().getTime()){
				throw new InvalidTimestampInChronologyException();
			}				
			/*valid so process it*/
			fitnessHeartrateTestBean.setHeartrateTestId(SmartbeatIDGenerator.getNextId(previousHeartrateTestBean.getHeartrateTestId()));
			Long differenceInDays = (fitnessHeartrateTestBean.getTimeOfRecord().getTime() - previousHeartrateTestBean.getTimeOfRecord().getTime())/(24*60*60*1000);
			Integer latestDayOfRecord = previousHeartrateTestBean.getDayOfRecord() + differenceInDays.intValue();
			fitnessHeartrateTestBean.setDayOfRecord(latestDayOfRecord);
			if(fitnessHeartrateTestBean.getHeartrateType() != ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC){
				FitnessHeartrateTestBean recentfitnessHeartrateTestBeanByType = fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(fitnessHeartrateTestBean.getUserid(), fitnessHeartrateTestBean.getHeartrateType());
				if (null != recentfitnessHeartrateTestBeanByType){
					fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(recentfitnessHeartrateTestBeanByType.getHeartrateTestId());
				}					
			}
		}else{
			fitnessHeartrateTestBean.setHeartrateTestId(SmartbeatIDGenerator.getFirstId(userid, SmartbeatIDGenerator.MARKER_HEARTRATE_TEST_ID));
			fitnessHeartrateTestBean.setDayOfRecord(1);
		}
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean);		
	}
	
	public double[][] getHeartrateZones(String userid) {
		double[][] heartrateZones = null;		
		double restingHeartrate = 0.0, thresholdHeartrate = 0.0, maximalHeartrate = 0.0;
		FitnessHeartrateTestBean restingHeartrateTestBean 	= fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_RESTING);
		FitnessHeartrateTestBean thresholdHeartrateTestBean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_THRESHOLD);
		FitnessHeartrateTestBean maximalHeartrateTestBean 	= fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(userid, ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
		
		if(null == restingHeartrateTestBean){
			Integer traineeClassification = fitnessHomeostasisIndexDAO.getTraineeClassificationByUserid(userid);
			if(null == traineeClassification){
				traineeClassification = new Integer(ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_UNTRAINED);
			}
			UserBean user = userManager.getUserInformation(userid);		
			restingHeartrate = ShapeIndexAlgorithm.getDefaultRestingHeartrate(traineeClassification, user.getGender());
		}else{
			restingHeartrate 	= restingHeartrateTestBean.getHeartrate();
		}
		
		/*maximal needs to be calculated before threshold as default threshold depends on maximal*/
		if(null == maximalHeartrateTestBean){
			UserBean user = userManager.getUserInformation(userid);
			maximalHeartrate 	= ShapeIndexAlgorithm.getDefaultMaximalHeartrate(user.getGender(), user.getAge());
		}
		else{
			maximalHeartrate 	= maximalHeartrateTestBean.getHeartrate();
		}
		
		if(null == thresholdHeartrateTestBean){
			Integer traineeClassification = fitnessHomeostasisIndexDAO.getTraineeClassificationByUserid(userid);
			if(null == traineeClassification){
				traineeClassification = new Integer(ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_UNTRAINED);
			}
			UserBean user = userManager.getUserInformation(userid);
			thresholdHeartrate 	= ShapeIndexAlgorithm.getDefaultThresholdHeartrate(traineeClassification, user.getGender(), maximalHeartrate);
		}else{
			thresholdHeartrate 	= thresholdHeartrateTestBean.getHeartrate();
		}
		
		heartrateZones = ShapeIndexAlgorithm.calculateHeartrateZones(restingHeartrate, thresholdHeartrate, maximalHeartrate);
		return heartrateZones;
	}
	
	public List<FitnessTrainingSessionIdBean> getTrainingSessionIdsInTimeInterval(String userID, Timestamp startTimestamp, Timestamp endTimestamp) throws TimeException {
		List<FitnessTrainingSessionIdBean> trainingSessionIds = null;
		if (null != startTimestamp && null != endTimestamp ){
			if (endTimestamp.getTime() > startTimestamp.getTime()){
		       trainingSessionIds = fitnessTrainingSessionDAO.getFitnessTrainingSessionIdsByTimeRange(userID, startTimestamp, endTimestamp);		       
			}else{
				throw new InvalidDurationException();
			}
		}else{
			if(null == startTimestamp){
				throw new InvalidStartTimestampException();
			}else{
				throw new InvalidEndTimestampException();
			}
		}
		return trainingSessionIds;
	}

	public List<FitnessTrainingSessionBean> getTrainingSessionsInTimeInterval(String userID, Timestamp startTimestamp, Timestamp endTimestamp) throws TimeException{	
		List<FitnessTrainingSessionBean> fitnessTrainingSessionBeanList = null;
		if (null != startTimestamp && null != endTimestamp ){
			if (endTimestamp.getTime() > startTimestamp.getTime()){
		        fitnessTrainingSessionBeanList = fitnessTrainingSessionDAO.getFitnessTrainingSessionsByTimeRange(userID, startTimestamp, endTimestamp);
			}else{
				throw new InvalidDurationException();
			}
		}else{
			if(null == startTimestamp){
				throw new InvalidStartTimestampException();
			}else{
				throw new InvalidEndTimestampException();
			}
		}
		return fitnessTrainingSessionBeanList;
	}
	
	public List<FitnessShapeIndexBean> getShapeIndexHistoryInTimeInterval(String userid, Timestamp startTime, Timestamp endTime) throws TimeException {
		List<FitnessShapeIndexBean> fitnessShapeIndexBeanList = null;
		if (null != startTime && null != endTime ){
		    if (endTime.getTime() > startTime.getTime()){
			   fitnessShapeIndexBeanList =  fitnessShapeIndexDAO.getShapeIndexHistoryDuringInterval(userid, startTime, endTime);
			   FitnessShapeIndexBean recentBean = fitnessShapeIndexBeanList.get(0);
			   if(endTime.getTime() > recentBean.getTimeOfRecord().getTime()){
				   double shapeIndex = getShapeIndexAtTime(recentBean.getSessionOfRecord(), endTime);
				   FitnessShapeIndexBean currentBean = new FitnessShapeIndexBean();
				   currentBean.setShapeIndex(shapeIndex);
				   currentBean.setSessionOfRecord(recentBean.getSessionOfRecord());
				   currentBean.setTimeOfRecord(endTime);
				   currentBean.setUserid(userid);
				   fitnessShapeIndexBeanList.add(0, currentBean);
			   }
		    }else{
		    	 throw new InvalidDurationException();
		    }
		}else{
			if(null == startTime){
				throw new InvalidStartTimestampException();
			}else{
				throw new InvalidEndTimestampException();
			}
		}
		return fitnessShapeIndexBeanList;
	}

	public List<FitnessHeartrateTestBean> getFitnessHeartrateTestsByTypeInTimeInterval(String userid, Integer heartrateType, Timestamp startTimestamp, Timestamp endTimestamp) throws TimeException {
		List<FitnessHeartrateTestBean> FitnessHeartrateTestBeanList = null;
		if (null!= heartrateType && null != startTimestamp && null != endTimestamp ){
			if (endTimestamp.getTime() > startTimestamp.getTime()){
			  FitnessHeartrateTestBeanList = fitnessHeartrateTestDAO.getFitnessHeartrateTestsByTypeInTimeInterval(userid, heartrateType, startTimestamp, endTimestamp);
			}else{
			  throw new InvalidDurationException();
			}
		}else{
			if(null == startTimestamp){
				throw new InvalidStartTimestampException();
			}else{
				throw new InvalidEndTimestampException();
			}
		}
		return FitnessHeartrateTestBeanList;
	}
	
	public FitnessHomeostasisIndexBean getHomeostasisIndexModelForUser(String userid) {
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexModel = null;

		fitnessHomeostasisIndexModel = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		return fitnessHomeostasisIndexModel;
	}
	
	public Timestamp getRecoveryTime(String userid) throws TrainingSessionException,HomeostasisIndexModelException {
		
		Timestamp recoveryTime = null;
		FitnessTrainingSessionBean fitnessTrainingSessionBean = fitnessTrainingSessionDAO.getRecentFitnessTrainingSessionForUser(userid);
		if (null == fitnessTrainingSessionBean){
			throw new AbsenceOfTrainingSessionException();
		}
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		if (null == fitnessHomeostasisIndexBean){
			throw new AbsenceOfHomeostasisIndexModelException();
		}
		
		recoveryTime = ShapeIndexAlgorithm.calculateTimeAtFullRecovery(fitnessHomeostasisIndexBean.getTraineeClassification(),fitnessTrainingSessionBean.getEndTime() ,fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		return recoveryTime;
	}
	
	public double getHomeostasisIndex(String userid) throws HomeostasisIndexModelException{
		double homeostasisIndex = 0.0;
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		if(null != fitnessHomeostasisIndexBean){
			Timestamp currentTime = new Timestamp(DateTimeUtils.currentTimeMillis());
			homeostasisIndex = ShapeIndexAlgorithm.getRegressedHomeostasisIndex(fitnessHomeostasisIndexBean.getTraineeClassification(), fitnessHomeostasisIndexBean.getRecentEndTime(), currentTime, fitnessHomeostasisIndexBean.getRecentMinimumOfHomeostasisIndex());
		}else{
			throw new AbsenceOfHomeostasisIndexModelException();
		}
	
		return  homeostasisIndex;
	}
	
	public void clearTraineeData(String userid) {
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser(userid);		
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
		fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(userid);
		fitnessTrainingSessionDAO.deleteAllTrainingSessionsForUser(userid);
	}
}
