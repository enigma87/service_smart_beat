/**
 * 
 */
package com.genie.smartbeat.domain;

import java.sql.Timestamp;

import org.apache.commons.math.stat.regression.SimpleRegression;
import org.joda.time.DateTimeUtils;

import com.genie.smartbeat.util.DoubleValueFormatter;

/**
 * @author dhasarathy
 *
 */
public class ShapeIndexAlgorithm 
{
	public static final double SHAPE_INDEX_INITIAL_VALUE 					= 100.0;
	
	/*training session limits*/
	public static final double  MINIMUM_SESSION_DURATION					= 5.0;
	public static final double  MINIMUM_ZONE_TIME 							= 0.5;
	public static final double  MINIMUM_ZONE_SPEED 							= 3.0;
	public static final double  MAXIMUM_ZONE_SPEED 							= 30.0;
	
	/*heartrate test limits*/
	public static final double MINIMUM_RESTING_HEARTRATE 					= 20.0;
	
	
	public static final Integer TRAINEE_CLASSIFICATION_UNTRAINED 			= 1;
	public static final Integer TRAINEE_CLASSIFICATION_LIGHTLY_TRAINED 		= 2;
	public static final Integer TRAINEE_CLASSIFICATION_MODERATELY_TRAINED 	= 3;
	public static final Integer TRAINEE_CLASSIFICATION_WELL_TRAINED 		= 4;
	public static final Integer TRAINEE_CLASSIFICATION_ELITE 				= 5;
	
	public static final Integer HEARTRATE_TYPE_RESTING 						= 0;
	public static final Integer HEARTRATE_TYPE_THRESHOLD 					= 1;
	public static final Integer HEARTRATE_TYPE_MAXIMAL 						= 2;
	public static final Integer HEARTRATE_TYPE_STANDING_ORTHOSTATIC 		= 3;
	
	public static final int GENDER_FEMALE 									= 0;
	public static final int GENDER_MALE 									= 1;
		
	public static final double[] DEFAULT_FEMALE_RESTING_HEARTRATE_BY_TRAINEE_CLASSIFICATION 				= {0.0,78.0,69.0,60.0,53.0,42.0};
	public static final double[] DEFAULT_MALE_RESTING_HEARTRATE_BY_TRAINEE_CLASSIFICATION 					= {0.0,72.0,65.0,55.0,48.0,40.0};
	public static final double[] DEFAULT_FEMALE_THRESHOLD_HEARTRATE_COEFFICIENT_BY_TRAINEE_CLASSIFICATION 	= {0.0,0.63,0.7,0.78,0.85,0.92};
	public static final double[] DEFAULT_MALE_THRESHOLD_HEARTRATE_COEFFICIENT_BY_TRAINEE_CLASSIFICATION 	= {0.0,0.6,0.67,0.75,0.82,0.9};
	public static final double DEFAULT_FEMALE_THRESHOLD_HEARTRATE_COEFFICIENT = 0.9;
	public static final double DEFAULT_MALE_THRESHOLD_HEARTRATE_COEFFICIENT = 0.85;
	
	
	public static double getDefaultRestingHeartrate(int traineeClassification, int gender){
		double r = 0.0;
		if(GENDER_FEMALE == gender){
			r = DEFAULT_FEMALE_RESTING_HEARTRATE_BY_TRAINEE_CLASSIFICATION[traineeClassification];
		}else{
			r = DEFAULT_MALE_RESTING_HEARTRATE_BY_TRAINEE_CLASSIFICATION[traineeClassification];
		}
		return r;
	}
	
	public static double getDefaultMaximalHeartrate(int gender, int age){
		double m = 0.0;
		if(GENDER_FEMALE == gender){
			m = 216.0 - (1.09*age);
		}else{
			m = 202.0 - (0.55*age);
		}
		return m;
	}
	
	public static double getDefaultThresholdHeartrate(int gender, double maximalHeartrate){
		double t = 0.0;
		if(GENDER_FEMALE == gender){
			t = maximalHeartrate*DEFAULT_FEMALE_THRESHOLD_HEARTRATE_COEFFICIENT;
		}else{
			t = maximalHeartrate*DEFAULT_MALE_THRESHOLD_HEARTRATE_COEFFICIENT;
		}
		return t;
	}
	
	private static final int ZONE_START_IDX = 0;
	private static final int ZONE_END_IDX 	= 1;
	public static double[][] calculateHeartrateZones(double restingHeartrate, double thresholdHeartrate, double maximalHeartrate){
		
		double[] heartrateZones[] = new double[7][2];
		double heartrateReserve = maximalHeartrate - restingHeartrate;
		
		heartrateZones[1][ZONE_START_IDX] 	= DoubleValueFormatter.format3Dot2(restingHeartrate);
		heartrateZones[1][ZONE_END_IDX] 	= DoubleValueFormatter.format3Dot2(restingHeartrate + ((thresholdHeartrate - restingHeartrate)/2));
		
		heartrateZones[2][ZONE_START_IDX] 	= DoubleValueFormatter.format3Dot2(heartrateZones[1][ZONE_END_IDX]);
		heartrateZones[2][ZONE_END_IDX] 	= DoubleValueFormatter.format3Dot2(heartrateZones[1][ZONE_END_IDX] + ((thresholdHeartrate - heartrateZones[1][ZONE_END_IDX])/2));
		
		heartrateZones[4][ZONE_START_IDX] 	= DoubleValueFormatter.format3Dot2(thresholdHeartrate - 0.04*heartrateReserve);
		heartrateZones[4][ZONE_END_IDX] 	= DoubleValueFormatter.format3Dot2(thresholdHeartrate + 0.02*heartrateReserve);
		
		heartrateZones[3][ZONE_START_IDX] 	= heartrateZones[2][ZONE_END_IDX];
		heartrateZones[3][ZONE_END_IDX] 	= heartrateZones[4][ZONE_START_IDX];
		
		heartrateZones[6][ZONE_START_IDX] 	= DoubleValueFormatter.format3Dot2(maximalHeartrate - 0.06*heartrateReserve);
		heartrateZones[6][ZONE_END_IDX] 	= DoubleValueFormatter.format3Dot2(maximalHeartrate);
		
		heartrateZones[5][ZONE_START_IDX] 	= heartrateZones[4][ZONE_END_IDX];
		heartrateZones[5][ZONE_END_IDX] 	= heartrateZones[6][ZONE_START_IDX];
		
		return heartrateZones;
	}
	
	private static final double TRAINING_IMPACT_BY_ZONE[] = {0,0,1,1.75,3,5,9};	
	public static double calculateTotalLoadofExercise(double[] timeDistributionOfHeartRateZones){
		double totalLoadOfExercise = TRAINING_IMPACT_BY_ZONE[1]*timeDistributionOfHeartRateZones[1] +
				TRAINING_IMPACT_BY_ZONE[2]*timeDistributionOfHeartRateZones[2] +
				TRAINING_IMPACT_BY_ZONE[3]*timeDistributionOfHeartRateZones[3] +
				TRAINING_IMPACT_BY_ZONE[4]*timeDistributionOfHeartRateZones[4] +
				TRAINING_IMPACT_BY_ZONE[5]*timeDistributionOfHeartRateZones[5] +
				TRAINING_IMPACT_BY_ZONE[6]*timeDistributionOfHeartRateZones[6];
		return DoubleValueFormatter.format3Dot2(totalLoadOfExercise);
	}
	
	/*Quadratic equation form Ax^2 + Bx + C = 0*/	
	/*private static final double TTR_CONSTANT_A_BY_TRAINEE_CLASSIFICATION[] = {0,-0.0347, -0.0434, -0.0521, -0.0608, -0.0694};
	private static final double TTR_CONSTANT_B_BY_TRAINEE_CLASSIFICATION[] = {0,4.1667, 5.2083, 6.25, 7.2917, 8.3333};	
	public static double getRegressedHomeostasisIndex(Integer traineeClassification, Timestamp previousTrainingSessionEndTime, Timestamp timeAtConsideration, double recentMinimumOfHomeostasisIndex){
		double regressedHomeostasisIndex = 0.0;
		Timestamp timeAtFullRecovery = calculateTimeAtFullRecovery(traineeClassification, previousTrainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		if(timeAtConsideration.getTime() < timeAtFullRecovery.getTime()){
			double hoursElapsed = (timeAtConsideration.getTime() - previousTrainingSessionEndTime.getTime())/(1000*60*60);
			double TTR_CONSTANT_A = TTR_CONSTANT_A_BY_TRAINEE_CLASSIFICATION[traineeClassification];
			double TTR_CONSTANT_B = TTR_CONSTANT_B_BY_TRAINEE_CLASSIFICATION[traineeClassification];
			double TTR_CONSTANT_C = recentMinimumOfHomeostasisIndex;
			regressedHomeostasisIndex = TTR_CONSTANT_A*Math.pow(hoursElapsed, 2.0) + TTR_CONSTANT_B*hoursElapsed + TTR_CONSTANT_C;
		}
		return DoubleValueFormatter.format3dot4(regressedHomeostasisIndex);
	}*/
	
	public static double getRegressedHomeostasisIndex(Integer traineeClassification, Timestamp previousTrainingSessionEndTime, Timestamp timeAtConsideration, double recentMinimumOfHomeostasisIndex){
		double regressedHomeostasisIndex = 0.0;
		Timestamp timeAtFullRecovery = calculateTimeAtFullRecovery(traineeClassification, previousTrainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		if(timeAtConsideration.getTime() < timeAtFullRecovery.getTime()){
			double hoursElapsed = (timeAtConsideration.getTime() - previousTrainingSessionEndTime.getTime())/(1000*60*60);
			double recoveryRate = RECOVERY_RATE_BY_TRAINEE_CLASSIFICATION[traineeClassification];
			regressedHomeostasisIndex = recentMinimumOfHomeostasisIndex + (hoursElapsed*recoveryRate);
		}
		return DoubleValueFormatter.format3dot4(regressedHomeostasisIndex);
	}
	
	public static final double getRegressionMinimumOfHomeostasisIndex(double regressedHomeostasisIndex, double totalLoadOfExercise){
		return regressedHomeostasisIndex - totalLoadOfExercise;
	}
			
	/*public static Timestamp calculateTimeAtFullRecovery(Integer traineeClassification, Timestamp trainingSessionEndTime, double recentMinimumOfHomeostasisIndex){
		
		Timestamp timeAtFullRecovery = null;
		
		double TTR_CONSTANT_A = TTR_CONSTANT_A_BY_TRAINEE_CLASSIFICATION[traineeClassification];
		double TTR_CONSTANT_B = TTR_CONSTANT_B_BY_TRAINEE_CLASSIFICATION[traineeClassification];
		double TTR_CONSTANT_C = recentMinimumOfHomeostasisIndex;
		
		double discriminant = Math.pow(TTR_CONSTANT_B, 2) - 4*TTR_CONSTANT_A*TTR_CONSTANT_C;
		if(discriminant > 0.0){
			double d = Math.sqrt(discriminant);
			double root1 = (-TTR_CONSTANT_B + d)/(2.0*TTR_CONSTANT_A);			
			double root2 = (-TTR_CONSTANT_B - d)/(2.0*TTR_CONSTANT_A);
			double minRoot = Math.min(root1, root2);
			long timeToRecoverInSeconds = new Double(minRoot*60*60).longValue();
			Timestamp timestamp = new Timestamp(trainingSessionEndTime.getTime()+(timeToRecoverInSeconds*1000));
			timeAtFullRecovery = timestamp;
		}		
		return timeAtFullRecovery;
	}*/
	
private static final double RECOVERY_RATE_BY_TRAINEE_CLASSIFICATION[] = {0,2.5,3.125,3.75,4.375,5};		
public static Timestamp calculateTimeAtFullRecovery(Integer traineeClassification, Timestamp trainingSessionEndTime, double recentMinimumOfHomeostasisIndex){
		Timestamp timeAtFullRecovery = null;	
		double recoveryRate = RECOVERY_RATE_BY_TRAINEE_CLASSIFICATION[traineeClassification];      

		double timeToRecoverInHours = (Math.abs(recentMinimumOfHomeostasisIndex))/recoveryRate;
	    long timeToRecoverInSeconds = new Double(timeToRecoverInHours*60*60).longValue();
		Timestamp timestamp = new Timestamp(trainingSessionEndTime.getTime()+(timeToRecoverInSeconds*1000));
		timeAtFullRecovery = timestamp;		


		return timeAtFullRecovery;
	}

	private static double calculateTimeDifferenceInHours(Timestamp startTime, Timestamp endTime){
		double timeDifferenceInHours = (endTime.getTime() - startTime.getTime())/(new Long(1000*60*60).doubleValue());
		return DoubleValueFormatter.format3Dot2(timeDifferenceInHours);
	}
	public static double calculateTimeToRecover(Integer traineeClassification, Timestamp trainingSessionEndTime, double recentMinimumOfHomeostasisIndex){		
		double timeToRecover = 0.0;
		Timestamp currentTime = new Timestamp(DateTimeUtils.currentTimeMillis());
		Timestamp timeAtFullRecovery = calculateTimeAtFullRecovery(traineeClassification, trainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		if(currentTime.getTime() < timeAtFullRecovery.getTime()){
			timeToRecover = calculateTimeDifferenceInHours(currentTime, timeAtFullRecovery);
		}
		return timeToRecover;
	}
	
	public static double calculateTimeAfterRecovery(Integer traineeClassification, Timestamp trainingSessionEndTime, Timestamp timeAtConsideration, double recentMinimumOfHomeostasisIndex){
		double timeAfterRecovery = 0.0;		
		Timestamp timeAtFullRecovery = calculateTimeAtFullRecovery(traineeClassification, trainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		if(timeAtFullRecovery.getTime() < timeAtConsideration.getTime()){
			timeAfterRecovery = calculateTimeDifferenceInHours(timeAtFullRecovery, timeAtConsideration);
		}
		return timeAfterRecovery;
	}
	
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_A = {{0,0},{-1.0,-4.9},{-25.0,-49.9},{-60.0,-79.9},{-100.0,-149.9},{-180.0,-199.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_B = {{0,0},{-5.0,-24.9},{-50.0,-99.9},{-80.0,-134.9},{-150.0,-189.9},{-200.0,-219.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_C = {{0,0},{-25.0,-49.9},{-100.0,-149.9},{-135.0,-174.9},{-190.0,-219.9},{-220.0,-239.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_D = {{0,0},{-50.0,-99.9},{-150.0,-174.9},{-175.0,-199.9},{-220.0,-239.9},{-240.0,-259.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_E = {{0,0},{-100.0,-149.9},{-175.0,-199.9},{-200.0,-229.9},{-240.0,-264.9},{-260.0,-279.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_F = {{0,0},{-150.0,-300.0},{-200.0,-300.0},{-230.0,-300.0},{-265.0,-300.0},{-280.0,-300.0}};
	
	public static final double[] SUPERCOMPENSATION_FROM_HI_BY_RANGE = {0.2,0.4,0.6,0.8,1.2,1.6};
	
	public static double calculateSupercompensationPoints(Integer traineeClassification, double regressionMinimumOfHomeostasisIndex){
		double supercompensationPoints = 0.0;
		int index = traineeClassification;
		if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_A[index][0]>= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_A[index][1]<= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[0];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_B[index][0]>= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_B[index][1]<= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[1];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_C[index][0]>= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_C[index][1]<= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[2];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_D[index][0]>= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_D[index][1]<= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[3];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_E[index][0]>= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_E[index][1]<= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[4];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_F[index][0]>= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_F[index][1]<= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[5];
		}
		return supercompensationPoints;
	}
	
	private static final double DETRAINING_THRESHOLD = 64;
	private static final double[] DETRAINING_BASE_PENALTY_RATE_BY_TRAINEE_CLASSIFICATION = {0,0.01,0.02,0.03,0.05,0.1};
	public static double calculateDetrainingPenalty(Integer traineeClassification, Timestamp trainingSessionEndTime, Timestamp timeAtConsideration, double recentMinimumOfHomeostasisIndex){
		double detrainingPenalty = 0.0;		
		double timeAfterRecovery = ShapeIndexAlgorithm.calculateTimeAfterRecovery(traineeClassification, trainingSessionEndTime, timeAtConsideration, recentMinimumOfHomeostasisIndex);
		if(0 != timeAfterRecovery){
			double basePenaltyRate = DETRAINING_BASE_PENALTY_RATE_BY_TRAINEE_CLASSIFICATION[traineeClassification];
			if(DETRAINING_THRESHOLD < timeAfterRecovery){
				detrainingPenalty += ((basePenaltyRate/2)*(timeAfterRecovery - DETRAINING_THRESHOLD));
				timeAfterRecovery = DETRAINING_THRESHOLD;
			}
			detrainingPenalty += (basePenaltyRate*timeAfterRecovery);
		}
		return DoubleValueFormatter.format3Dot2(detrainingPenalty);
	}
	
	/*Speed-Vdot regression model of the form y = Ax + B with y as speed and x as Vdot*/
	private static final double[] SPEED_VDOT_CONSTANT_A_BY_HRZ 		= {0,0,0.1852,0.2024,0.2232,0.2316,0.2406};
	private static final double[] SPEED_VDOT_CONSTANT_B_BY_HRZ 		= {0,0,2.3743,2.5846,2.8357,3.0071,3.1978};
	private static final double[] SPEED_CORRECTION_FACTOR_BY_SURFACE = {1,1.03,1.05,1.07,1.1,1.2,1.35,1.5};
	public static final int RUNNING_SURFACE_TRACK_PAVED 			= 0;
	public static final int RUNNING_SURFACE_GOOD_DIRT_TRACK_WET		= 1;
	public static final int RUNNING_SURFACE_GOOD_FOREST_PATH		= 2;
	public static final int RUNNING_SURFACE_MEDIOCRE_SHORT_GRASS	= 3;
	public static final int RUNNING_SURFACE_ROUGH_PATH				= 4;
	public static final int RUNNING_SURFACE_SLIPPERY_PATH			= 5;
	public static final int RUNNING_SURFACE_MUD_SNOW_SAND			= 6;
	public static final int RUNNING_SURFACE_WET_MUD_DEEP_SNOW		= 7;
	
	/*averageAltitude in m and extraLoad in kg*/
	public static double calculateVdot(double[] speedDistributionOfHRZ, int runningSurface, double averageAltitude, double extraLoad, double previousVdot){
		double[] VdotByZone = new double[7];
		double Vdot = 0.0;
		int validZoneCount = 0;		
		double surfaceSpeedCorrectionFactor 	= SPEED_CORRECTION_FACTOR_BY_SURFACE[runningSurface];
		double altitudeSpeedCorrectionFactor 	= 1.0;
		if(0 < averageAltitude){
			altitudeSpeedCorrectionFactor = 1 + (((-1*0.000175*previousVdot*averageAltitude) + 0.0067686)/100); 
		}
		double speedCorrectionFactor = surfaceSpeedCorrectionFactor*altitudeSpeedCorrectionFactor;
				
		/*no contribution to vDot from zone 1*/
		for(int i = 2; i<=6;i++){
			if(0 < speedDistributionOfHRZ[i]){
				VdotByZone[i] = ((speedCorrectionFactor*speedDistributionOfHRZ[i]) - SPEED_VDOT_CONSTANT_B_BY_HRZ[i])/SPEED_VDOT_CONSTANT_A_BY_HRZ[i];
				validZoneCount++;
			}
		}		
		double sum = 0;
	    for (int i = 0; i < VdotByZone.length; i++) {
	    		sum += VdotByZone[i];	    		
	    }
	    if(0 < validZoneCount){
	    	Vdot = sum / validZoneCount;
	    }
		return DoubleValueFormatter.format3Dot2(Vdot);
	}
	
	public static final int VDOT_HISTORY_LIMIT = 4;
	private static final int SESSION_1 = 0;
	private static final int SESSION_2 = 1;
	private static final int SESSION_3 = 2;
	private static final int SESSION_4 = 3;
	public static double calculateSpeedHeartrateFactor(double[] vdotHistory){
		double speedHeartrateFactor = 0;
		if(VDOT_HISTORY_LIMIT == vdotHistory.length){
			speedHeartrateFactor = (vdotHistory[SESSION_4]+vdotHistory[SESSION_3])/(vdotHistory[SESSION_2]+vdotHistory[SESSION_1]);
		}
		return DoubleValueFormatter.format3dot4(speedHeartrateFactor);
	}
		
	public static final double[][] VDOT_RANGE_FOR_FEMALE_TRAINEE_BY_CLASSIFICATION 	= {{34.0,41.0},{41.0,48.0},{48.0,58.0}};
	public static final double[][] VDOT_RANGE_FOR_MALE_TRAINEE_BY_CLASSIFICATION 	= {{34.0,44.0},{44.0,52.0},{52.0,65.0}};
		
	public static int getTraineeClassificationUsingVdot(int gender, Double Vdot){
		Integer traineeClassification = TRAINEE_CLASSIFICATION_UNTRAINED;
		
		if(GENDER_FEMALE == gender){
			if((Vdot>= VDOT_RANGE_FOR_FEMALE_TRAINEE_BY_CLASSIFICATION[0][0]) && (Vdot< VDOT_RANGE_FOR_FEMALE_TRAINEE_BY_CLASSIFICATION[0][1])){
				traineeClassification = TRAINEE_CLASSIFICATION_LIGHTLY_TRAINED;
			}else if((Vdot>= VDOT_RANGE_FOR_FEMALE_TRAINEE_BY_CLASSIFICATION[1][0]) && (Vdot< VDOT_RANGE_FOR_FEMALE_TRAINEE_BY_CLASSIFICATION[1][1])){
				traineeClassification = TRAINEE_CLASSIFICATION_MODERATELY_TRAINED;
			}else if((Vdot>= VDOT_RANGE_FOR_FEMALE_TRAINEE_BY_CLASSIFICATION[2][0]) && (Vdot< VDOT_RANGE_FOR_FEMALE_TRAINEE_BY_CLASSIFICATION[2][1])){
				traineeClassification = TRAINEE_CLASSIFICATION_WELL_TRAINED;
			}else if(Vdot>= VDOT_RANGE_FOR_FEMALE_TRAINEE_BY_CLASSIFICATION[2][1]){
				traineeClassification = TRAINEE_CLASSIFICATION_ELITE;
			}
		}else if(GENDER_MALE == gender){
				
			if((Vdot>= VDOT_RANGE_FOR_MALE_TRAINEE_BY_CLASSIFICATION[0][0]) && (Vdot< VDOT_RANGE_FOR_MALE_TRAINEE_BY_CLASSIFICATION[0][1])){
				traineeClassification = TRAINEE_CLASSIFICATION_LIGHTLY_TRAINED;
			}else if((Vdot>= VDOT_RANGE_FOR_MALE_TRAINEE_BY_CLASSIFICATION[1][0]) && (Vdot< VDOT_RANGE_FOR_MALE_TRAINEE_BY_CLASSIFICATION[1][1])){
				traineeClassification = TRAINEE_CLASSIFICATION_MODERATELY_TRAINED;
			}else if((Vdot>= VDOT_RANGE_FOR_MALE_TRAINEE_BY_CLASSIFICATION[2][0]) && (Vdot< VDOT_RANGE_FOR_MALE_TRAINEE_BY_CLASSIFICATION[2][1])){
				traineeClassification = TRAINEE_CLASSIFICATION_WELL_TRAINED;
			}else if(Vdot>= VDOT_RANGE_FOR_MALE_TRAINEE_BY_CLASSIFICATION[2][1]){
				traineeClassification = TRAINEE_CLASSIFICATION_ELITE;
			}
		}
        
		return traineeClassification;
	}
	
	public static final int SOHR_STABILIZATION_LIMIT 		= 4;
	public static final int SOHR_DAY_OF_RECORD_SERIES_LIMIT = 30;
	public static final double[][] SOHR_TIME_REGRESSION_RANGE = { 	{-4,-3},{-3,-2},{-2,-1},{-1,-0.8}, {-0.8,-0.6},{-0.6,-0.4},{-0.4,-0.2},{-0.2,0.2},
																	{0.2,0.4},{0.4,0.6},{0.6,0.8},{0.8,1},{1,2},{2,3},{3,4}};
	public static final double[] SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP = {1.2,1,0.8,0.6,0.5,0.4,0.3,0.2,0,-0.2,-0.3,-0.4,-0.5,-0.6,-0.8,-1,-1.2};
	
	public static double calculateOrthostaticHeartrateFactor(double[][] dayOfRecordSOHRSeries){
		double slopeOfTimeRegressionOfSHR = 0.0;
		double orthostaticHeartrateFactor;
		SimpleRegression regressionModel = new SimpleRegression();
		regressionModel.addData(dayOfRecordSOHRSeries);
		slopeOfTimeRegressionOfSHR = regressionModel.getSlope();
		if(SOHR_TIME_REGRESSION_RANGE[0][0] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[0];
		}else if(SOHR_TIME_REGRESSION_RANGE[0][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[0][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[1];
		}else if(SOHR_TIME_REGRESSION_RANGE[1][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[1][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[2];
		}else if(SOHR_TIME_REGRESSION_RANGE[2][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[2][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[3];
		}else if(SOHR_TIME_REGRESSION_RANGE[3][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[3][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[4];
		}else if(SOHR_TIME_REGRESSION_RANGE[4][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[4][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[5];
		}else if(SOHR_TIME_REGRESSION_RANGE[5][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[5][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[6];
		}else if(SOHR_TIME_REGRESSION_RANGE[6][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[6][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[7];
		}else if(SOHR_TIME_REGRESSION_RANGE[7][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[7][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[8];
		}else if(SOHR_TIME_REGRESSION_RANGE[8][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[8][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[9];
		}else if(SOHR_TIME_REGRESSION_RANGE[9][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[9][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[10];
		}else if(SOHR_TIME_REGRESSION_RANGE[10][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[10][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[11];
		}else if(SOHR_TIME_REGRESSION_RANGE[11][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[11][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[12];
		}else if(SOHR_TIME_REGRESSION_RANGE[12][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[12][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[13];
		}else if(SOHR_TIME_REGRESSION_RANGE[13][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[13][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[14];
		}else if(SOHR_TIME_REGRESSION_RANGE[14][0] <= slopeOfTimeRegressionOfSHR && SOHR_TIME_REGRESSION_RANGE[14][1] > slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[15];
		}
		else if(SOHR_TIME_REGRESSION_RANGE[14][1] < slopeOfTimeRegressionOfSHR){
			orthostaticHeartrateFactor = SOHR_TIME_REGRESSION_TO_OHR_FACTOR_MAP[16];
		}else{
			orthostaticHeartrateFactor = 0.0;
		}
		return orthostaticHeartrateFactor;
	}
}
