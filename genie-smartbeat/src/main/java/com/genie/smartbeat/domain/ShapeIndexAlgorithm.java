/**
 * 
 */
package com.genie.smartbeat.domain;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

import org.apache.commons.math.stat.regression.SimpleRegression;

/**
 * @author dhasarathy
 *
 */
public class ShapeIndexAlgorithm 
{
	public static final double SHAPE_INDEX_INITIAL_VALUE 					= 100.0;
	
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
		public static double[] getDefaultRMTHeartrates(int traineeClassification, int age, int gender){
		double[] rmtHeartrates = new double[3];
		double r = 0.0, m = 0.0, t = 0.0;
		if(GENDER_FEMALE == gender){
			r = DEFAULT_FEMALE_RESTING_HEARTRATE_BY_TRAINEE_CLASSIFICATION[traineeClassification];
			m = 216.0 - (1.09*age);
			t = m*DEFAULT_FEMALE_THRESHOLD_HEARTRATE_COEFFICIENT_BY_TRAINEE_CLASSIFICATION[traineeClassification];
		}else{
			r = DEFAULT_MALE_RESTING_HEARTRATE_BY_TRAINEE_CLASSIFICATION[traineeClassification];
			m = 202.0 - (0.55*age);
			t = m*DEFAULT_MALE_THRESHOLD_HEARTRATE_COEFFICIENT_BY_TRAINEE_CLASSIFICATION[traineeClassification];
		}
		rmtHeartrates[0] = r;
		rmtHeartrates[1] = t;
		rmtHeartrates[2] = m;
		return rmtHeartrates;
	}
	
	private static final int ZONE_START_IDX = 0;
	private static final int ZONE_END_IDX 	= 1;
	public static double[][] calculateHeartrateZones(double restingHeartrate, double thresholdHeartrate, double maximalHeartrate){
		
		double[] heartrateZones[] = new double[7][2];
		double heartrateReserve = maximalHeartrate - restingHeartrate;
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat heartrateZoneFormat = new DecimalFormat("###.##",symbols);
		
		
		heartrateZones[1][ZONE_START_IDX] 	= Double.valueOf(heartrateZoneFormat.format(restingHeartrate));
		heartrateZones[1][ZONE_END_IDX] 	= Double.valueOf(heartrateZoneFormat.format(restingHeartrate + ((thresholdHeartrate - restingHeartrate)/2)));
		
		heartrateZones[2][ZONE_START_IDX] 	= Double.valueOf(heartrateZoneFormat.format(heartrateZones[1][ZONE_END_IDX]));
		heartrateZones[2][ZONE_END_IDX] 	= Double.valueOf(heartrateZoneFormat.format(heartrateZones[1][ZONE_END_IDX] + ((thresholdHeartrate - heartrateZones[1][ZONE_END_IDX])/2)));
		
		heartrateZones[4][ZONE_START_IDX] 	= Double.valueOf(heartrateZoneFormat.format(thresholdHeartrate - 0.04*heartrateReserve));
		heartrateZones[4][ZONE_END_IDX] 	= Double.valueOf(heartrateZoneFormat.format(thresholdHeartrate + 0.02*heartrateReserve));
		
		heartrateZones[3][ZONE_START_IDX] 	= heartrateZones[2][ZONE_END_IDX];
		heartrateZones[3][ZONE_END_IDX] 	= heartrateZones[4][ZONE_START_IDX];
		
		heartrateZones[6][ZONE_START_IDX] 	= Double.valueOf(heartrateZoneFormat.format(maximalHeartrate - 0.06*heartrateReserve));
		heartrateZones[6][ZONE_END_IDX] 	= Double.valueOf(heartrateZoneFormat.format(maximalHeartrate));
		
		heartrateZones[5][ZONE_START_IDX] 	= heartrateZones[4][ZONE_END_IDX];
		heartrateZones[5][ZONE_END_IDX] 	= heartrateZones[6][ZONE_START_IDX];
		
		return heartrateZones;
	}
	
	private static final double TRAINING_IMPACT_BY_ZONE[] = {0,0,1,1.75,3,5,9};	
	public static double calculateTotalLoadofExercise(double[] timeDistributionOfHeartRateZones){
		double totalLoadOfExercise = TRAINING_IMPACT_BY_ZONE[1]*timeDistributionOfHeartRateZones[0] +
				TRAINING_IMPACT_BY_ZONE[2]*timeDistributionOfHeartRateZones[1] +
				TRAINING_IMPACT_BY_ZONE[3]*timeDistributionOfHeartRateZones[2] +
				TRAINING_IMPACT_BY_ZONE[4]*timeDistributionOfHeartRateZones[3] +
				TRAINING_IMPACT_BY_ZONE[5]*timeDistributionOfHeartRateZones[4] +
				TRAINING_IMPACT_BY_ZONE[6]*timeDistributionOfHeartRateZones[5];
		return totalLoadOfExercise;
	}
	
	/*Quadratic equation form Ax^2 + Bx + C = 0*/	
	private static final double TTR_CONSTANT_A_BY_TRAINEE_CLASSIFICATION[] = {0,-0.0347, -0.0434, -0.0521, -0.0608, -0.0694};
	private static final double TTR_CONSTANT_B_BY_TRAINEE_CLASSIFICATION[] = {0,4.1667, 5.2083, 6.25, 7.2917, 8.3333};	
	public static double getRegressedHomeostasisIndex(Integer traineeClassification, Timestamp previousTrainingSessionEndTime, double recentMinimumOfHomeostasisIndex){
		double regressedHomeostasisIndex = 0.0;
		Timestamp timeAtFullRecovery = calculateTimeAtFullRecovery(traineeClassification, previousTrainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		Timestamp currentTime = new Timestamp(new Date().getTime());
		if(currentTime.getTime() < timeAtFullRecovery.getTime()){
			double hoursElapsed = ((new Timestamp(new Date().getTime())).getTime() - previousTrainingSessionEndTime.getTime())/(1000*60*60);
			double TTR_CONSTANT_A = TTR_CONSTANT_A_BY_TRAINEE_CLASSIFICATION[traineeClassification];
			double TTR_CONSTANT_B = TTR_CONSTANT_B_BY_TRAINEE_CLASSIFICATION[traineeClassification];
			double TTR_CONSTANT_C = recentMinimumOfHomeostasisIndex;
			regressedHomeostasisIndex = TTR_CONSTANT_A*Math.pow(hoursElapsed, 2.0) + TTR_CONSTANT_B*hoursElapsed + TTR_CONSTANT_C;
		}
		return regressedHomeostasisIndex;
	}
	
	public static final double getRegressionMinimumOfHomeostasisIndex(double regressedHomeostasisIndex, double totalLoadOfExercise){
		return regressedHomeostasisIndex - totalLoadOfExercise;
	}
			
	public static Timestamp calculateTimeAtFullRecovery(Integer traineeClassification, Timestamp trainingSessionEndTime, double recentMinimumOfHomeostasisIndex){
		
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
	}
	
	public static double calculateTimeToRecover(Integer traineeClassification, Timestamp trainingSessionEndTime, double recentMinimumOfHomeostasisIndex){
		double timeToRecover = 0.0;
		Timestamp currentTime = new Timestamp(new Date().getTime());
		Timestamp timeAtFullRecovery = calculateTimeAtFullRecovery(traineeClassification, trainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		if(currentTime.getTime() < timeAtFullRecovery.getTime()){
			timeToRecover = (timeAtFullRecovery.getTime() - currentTime.getTime())/(1000*60*60);
		}
		return timeToRecover;
	}
	
	public static double calculateTimeAfterRecovery(Integer traineeClassification, Timestamp trainingSessionEndTime, double recentMinimumOfHomeostasisIndex){
		double timeAfterRecovery = 0.0;
		Timestamp currentTime = new Timestamp(new Date().getTime());
		Timestamp timeAtFullRecovery = calculateTimeAtFullRecovery(traineeClassification, trainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		if(timeAtFullRecovery.getTime() < currentTime.getTime()){
			timeAfterRecovery = (currentTime.getTime() - timeAtFullRecovery.getTime())/(1000*60*60);
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
	private static final double DETRAINING_PENALTY_RATE[] = {0.1, 0.05};
	public static double calculateDetrainingPenalty(Integer traineeClassification, Timestamp trainingSessionEndTime, double recentMinimumOfHomeostasisIndex){
		double detrainingPenalty = 0.0;
		double timeAfterRecovery = ShapeIndexAlgorithm.calculateTimeAfterRecovery(traineeClassification, trainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		if(0 != timeAfterRecovery){
			if(DETRAINING_THRESHOLD < timeAfterRecovery){
				detrainingPenalty += (DETRAINING_PENALTY_RATE[1]*(DETRAINING_THRESHOLD - timeAfterRecovery));
				timeAfterRecovery -= DETRAINING_THRESHOLD;
			}
			detrainingPenalty += (DETRAINING_PENALTY_RATE[0]*timeAfterRecovery);
		}
		return detrainingPenalty;
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
	
	public static double calculateVdot(double[] speedDistributionOfHRZ, int runningSurface){
		double[] VdotByZone = new double[7];
		double Vdot = 0.0;
		double speedCorrectionFactor = SPEED_CORRECTION_FACTOR_BY_SURFACE[runningSurface];
		/*no contribution to vDot from zone 1*/
		VdotByZone[2] = (speedCorrectionFactor*speedDistributionOfHRZ[2] - SPEED_VDOT_CONSTANT_B_BY_HRZ[2])/SPEED_VDOT_CONSTANT_A_BY_HRZ[2];
		VdotByZone[3] = (speedCorrectionFactor*speedDistributionOfHRZ[3] - SPEED_VDOT_CONSTANT_B_BY_HRZ[3])/SPEED_VDOT_CONSTANT_A_BY_HRZ[3];
		VdotByZone[4] = (speedCorrectionFactor*speedDistributionOfHRZ[4] - SPEED_VDOT_CONSTANT_B_BY_HRZ[4])/SPEED_VDOT_CONSTANT_A_BY_HRZ[4];
		VdotByZone[5] = (speedCorrectionFactor*speedDistributionOfHRZ[5] - SPEED_VDOT_CONSTANT_B_BY_HRZ[5])/SPEED_VDOT_CONSTANT_A_BY_HRZ[5];
		VdotByZone[6] = (speedCorrectionFactor*speedDistributionOfHRZ[6] - SPEED_VDOT_CONSTANT_B_BY_HRZ[6])/SPEED_VDOT_CONSTANT_A_BY_HRZ[6];
		double sum = 0;
	    for (int i = 0; i < VdotByZone.length; i++) {
	        sum += VdotByZone[i];
	    }
	    Vdot = sum / (VdotByZone.length-1);
		return Vdot;
	}
	
	public static double calculateCompoundedVdot(double currentVdot, double previousVdot){
		return ((100*currentVdot)/previousVdot)-100;		
	}
	
	public static final int SOHR_STABILIZATION_LIMIT = 4;
	public static double calculateSlopeOfTimeRegressionOfStandingOrthostaticHeartRate(double[][] dayOfRecordSOHRSeries){
		double slopeOfTimeRegressionOfSHR = 0.0;
		if(SOHR_STABILIZATION_LIMIT < dayOfRecordSOHRSeries.length){
			SimpleRegression regressionModel = new SimpleRegression();
			regressionModel.addData(dayOfRecordSOHRSeries);
			slopeOfTimeRegressionOfSHR = regressionModel.getSlope();
		}
		return slopeOfTimeRegressionOfSHR;
	}

	public static final double[][] VDOT_RANGE_FOR_TRAINEE_CLASSIFICATION = {{35.0,45.0},{45.0,52.0},{52.0,58.0}};
	public static int getTraineeClassificationUsingVdot(Double Vdot){
		Integer traineeClassification = TRAINEE_CLASSIFICATION_UNTRAINED;
        if((Vdot>= VDOT_RANGE_FOR_TRAINEE_CLASSIFICATION[0][0]) && (Vdot< VDOT_RANGE_FOR_TRAINEE_CLASSIFICATION[0][1])){
			traineeClassification = TRAINEE_CLASSIFICATION_LIGHTLY_TRAINED;
		}else if((Vdot>= VDOT_RANGE_FOR_TRAINEE_CLASSIFICATION[1][0]) && (Vdot< VDOT_RANGE_FOR_TRAINEE_CLASSIFICATION[1][1])){
			traineeClassification = TRAINEE_CLASSIFICATION_MODERATELY_TRAINED;
		}else if((Vdot>= VDOT_RANGE_FOR_TRAINEE_CLASSIFICATION[2][0]) && (Vdot< VDOT_RANGE_FOR_TRAINEE_CLASSIFICATION[2][1])){
			traineeClassification = TRAINEE_CLASSIFICATION_WELL_TRAINED;
		}else if(Vdot>= VDOT_RANGE_FOR_TRAINEE_CLASSIFICATION[2][1]){
			traineeClassification = TRAINEE_CLASSIFICATION_ELITE;
		}
		return traineeClassification;
	}
}
