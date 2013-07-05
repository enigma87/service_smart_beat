/**
 * 
 */
package com.genie.smartbeat.domain;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.math.stat.regression.SimpleRegression;

/**
 * @author dhasarathy
 *
 */
public class ShapeIndexAlgorithm 
{
	public static final double SHAPE_INDEX_INITIAL_VALUE = 100.0;
		
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
	public static final double[] SPEED_VDOT_CONSTANT_A_BY_HRZ = {0,0,0.1852,0.2024,0.2232,0.2316,0.2406};
	public static final double[] SPEED_VDOT_CONSTANT_B_BY_HRZ = {0,0,2.3743,2.5846,2.8357,3.0071,3.1978};
	public static double calculateVdot(double[] speedDistributionOfHRZ){
		double[] VdotByZone = new double[7];
		double Vdot = 0.0;
		/*no contribution to vDot from zone 1*/
		VdotByZone[2] = (speedDistributionOfHRZ[2] - SPEED_VDOT_CONSTANT_B_BY_HRZ[2])/SPEED_VDOT_CONSTANT_A_BY_HRZ[2];
		VdotByZone[3] = (speedDistributionOfHRZ[3] - SPEED_VDOT_CONSTANT_B_BY_HRZ[3])/SPEED_VDOT_CONSTANT_A_BY_HRZ[3];
		VdotByZone[4] = (speedDistributionOfHRZ[4] - SPEED_VDOT_CONSTANT_B_BY_HRZ[4])/SPEED_VDOT_CONSTANT_A_BY_HRZ[4];
		VdotByZone[5] = (speedDistributionOfHRZ[5] - SPEED_VDOT_CONSTANT_B_BY_HRZ[5])/SPEED_VDOT_CONSTANT_A_BY_HRZ[5];
		VdotByZone[6] = (speedDistributionOfHRZ[6] - SPEED_VDOT_CONSTANT_B_BY_HRZ[6])/SPEED_VDOT_CONSTANT_A_BY_HRZ[6];
		double sum = 0;
	    for (int i = 0; i < VdotByZone.length; i++) {
	        sum += VdotByZone[i];
	    }
	    Vdot = sum / VdotByZone.length;
		return Vdot;
	}
	
	public static double calculateCompoundedVdot(double currentVdot, double previousVdot){
		return ((100*currentVdot)/previousVdot)-100;		
	}
	
	private static final int LENGTH_LIMIT_OHR_FACTOR = 4;
	public static double calculateSlopeOfTimeRegressionOfStandingHeartRate(double[][] timeSHRPairs){
		double slopeOfTimeRegressionOfSHR = 0.0;
		if(LENGTH_LIMIT_OHR_FACTOR < timeSHRPairs.length){
			SimpleRegression regressionModel = new SimpleRegression();
			regressionModel.addData(timeSHRPairs);
			slopeOfTimeRegressionOfSHR = regressionModel.getSlope();
		}
		return slopeOfTimeRegressionOfSHR;
	}		
}
