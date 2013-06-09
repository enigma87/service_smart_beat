/**
 * 
 */
package com.genie.heartrate.mgmt.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;

/**
 * @author manojkumar
 *
 */
public class ShapeIndexAlgorithm 
{

	public static Map<String, Object> parseHeartRates(String json)
	{
		return new HashMap<String, Object>();
	}
	
	public static UserHeartRateZone calculateHeartRateZones(UserHeartRateTest userheartRateTest)
	{
		UserHeartRateZone userHeartRateZone = new UserHeartRateZone();
		final String decFormat = "#.###";
		
		final Integer restingHr   = userheartRateTest.getRestingHeartRate();
		final Integer thresholdHr = userheartRateTest.getThresholdHeartRate();
		final Integer maximalHr   = userheartRateTest.getMaximalHeartRate();
		final Integer hrReserve   = maximalHr-restingHr;
		
		
		DecimalFormat df = new DecimalFormat(decFormat);
		
		final Double Hrz1Start = Double.parseDouble(df.format((double)restingHr));
		final Double Hrz1End = Double.parseDouble(df.format((double)(thresholdHr-restingHr)/2+restingHr));
		final Double Hrz2Start = Double.parseDouble(df.format(Hrz1End+0.001));
		final Double Hrz2End = Double.parseDouble(df.format((thresholdHr-Hrz1End)/2+Hrz1End));
		final Double Hrz4Start = Double.parseDouble(df.format(thresholdHr-(0.04*hrReserve)));
		final Double Hrz4End = Double.parseDouble(df.format(thresholdHr+(0.02*hrReserve)));
		final Double Hrz3Start = Double.parseDouble(df.format(Hrz2End+0.001));
		final Double Hrz3End = Double.parseDouble(df.format(Hrz4Start-0.001));
		final Double Hrz6Start = Double.parseDouble(df.format(maximalHr-(0.06*hrReserve)));
		final Double Hrz6End = Double.parseDouble(df.format((double)maximalHr));
		final Double Hrz5Start = Double.parseDouble(df.format(Hrz4End+0.001));
		final Double Hrz5End = Double.parseDouble(df.format(Hrz6Start-0.001));
		
		
		userHeartRateZone.setUserid(userheartRateTest.getUserid());
		userHeartRateZone.setHrz1Start(Hrz1Start);
		userHeartRateZone.setHrz1End(Hrz1End);
		userHeartRateZone.setHrz2Start(Hrz2Start);
		userHeartRateZone.setHrz2End(Hrz2End);
		userHeartRateZone.setHrz4Start(Hrz4Start);
		userHeartRateZone.setHrz4End(Hrz4End);
		userHeartRateZone.setHrz3Start(Hrz3Start);
		userHeartRateZone.setHrz3End(Hrz3End);
		userHeartRateZone.setHrz6Start(Hrz6Start);
		userHeartRateZone.setHrz6End(Hrz6End);
		userHeartRateZone.setHrz5Start(Hrz5Start);
		userHeartRateZone.setHrz5End(Hrz5End);
		

		
		return userHeartRateZone;
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
	private static final double TTR_CONSTANT_A_BY_TRAINEE_CLASSIFICATION[] = {-0.0347, -0.0434, -0.0521, -0.0608, -0.0694};
	private static final double TTR_CONSTANT_B_BY_TRAINEE_CLASSIFICATION[] = {4.1667, 5.2083, 6.25, 7.2917, 8.3333};
	public static double getRegressedHomeostasisIndex(TraineeClassification traineeClassification, Timestamp previousTrainingSessionEndTime, double previousTrainingSessionTLE){
		double regressedHomeostasisIndex = 0.0;
		Timestamp timeAtFullRecovery = calculateTimeAtFullRecovery(traineeClassification, previousTrainingSessionEndTime, previousTrainingSessionTLE);
		Timestamp currentTime = new Timestamp(new Date().getTime());
		if(currentTime.getTime() < timeAtFullRecovery.getTime()){
			double hoursElapsed = (new Timestamp(new Date().getTime()).getTime() - previousTrainingSessionEndTime.getTime())/(1000*60*60);
			double TTR_CONSTANT_A = TTR_CONSTANT_A_BY_TRAINEE_CLASSIFICATION[traineeClassification.ordinal()];
			double TTR_CONSTANT_B = TTR_CONSTANT_B_BY_TRAINEE_CLASSIFICATION[traineeClassification.ordinal()];
			double TTR_CONSTANT_C = previousTrainingSessionTLE;
			regressedHomeostasisIndex = TTR_CONSTANT_A*Math.pow(hoursElapsed, 2.0) + TTR_CONSTANT_B*hoursElapsed + TTR_CONSTANT_C;
		}
		return regressedHomeostasisIndex;
	}
	
	public static final double updateHomeostasisIndex(double regressedHomeostasisIndex, double totalLoadOfExercise){
		return regressedHomeostasisIndex - totalLoadOfExercise;
	}
			
	public static Timestamp calculateTimeAtFullRecovery(TraineeClassification traineeClassification, Timestamp trainingSessionEndTime, double totalLoadOfExercise){
		
		Timestamp timeAtFullRecovery = null;
		
		double TTR_CONSTANT_A = TTR_CONSTANT_A_BY_TRAINEE_CLASSIFICATION[traineeClassification.ordinal()];
		double TTR_CONSTANT_B = TTR_CONSTANT_B_BY_TRAINEE_CLASSIFICATION[traineeClassification.ordinal()];
		double TTR_CONSTANT_C = totalLoadOfExercise;
		
		double discriminant = Math.pow(TTR_CONSTANT_B, 2) - 4*TTR_CONSTANT_A*TTR_CONSTANT_C;
		if(discriminant > 0.0){
			double d = Math.sqrt(discriminant);
			double root1 = (-TTR_CONSTANT_B + d)/(2.0*TTR_CONSTANT_A);			
			double root2 = (-TTR_CONSTANT_B - d)/(2.0*TTR_CONSTANT_A);
			double minRoot = Math.min(root1, root2);
			long timeToRecoverInSeconds = new Double(minRoot*60*60).longValue();
			timeAtFullRecovery = new Timestamp(trainingSessionEndTime.getTime()+timeToRecoverInSeconds);
		}		
		return timeAtFullRecovery;
	}
	
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_A = {{-1.0,-4.9},{-25.0,-49.9},{-60.0,-79.9},{-100.0,-149.9},{-180.0,-199.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_B = {{-5.0,-24.9},{-50.0,-99.9},{-80.0,-134.9},{-150.0,-189.9},{-200.0,-219.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_C = {{-25.0,-49.9},{-100.0,-149.9},{-135.0,-174.9},{-190.0,-219.9},{-220.0,-239.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_D = {{-50.0,-99.9},{-150.0,-174.9},{-175.0,-199.9},{-220.0,-239.9},{-240.0,-259.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_E = {{-100.0,-149.9},{-175.0,-199.9},{-200.0,-229.9},{-240.0,-264.9},{-260.0,-279.9}};
	public static final double[][] SUPERCOMENSATION_FROM_HI_MAP_RANGE_F = {{-150.0,-300.0},{-200.0,-300.0},{-230.0,-300.0},{-265.0,-300.0},{-280.0,-300.0}};
	
	public static final double[] SUPERCOMPENSATION_FROM_HI_BY_RANGE = {0.2,0.4,0.6,0.8,1.2,1.6};
	
	public static double calculateSupercompensationPoints(TraineeClassification traineeClassification, double regressionMinimumOfHomeostasisIndex){
		double supercompensationPoints = 0.0;
		int index = traineeClassification.ordinal();
		if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_A[index][0]<= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_A[index][1]>= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[0];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_B[index][0]<= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_B[index][1]>= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[1];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_C[index][0]<= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_C[index][1]>= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[2];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_D[index][0]<= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_D[index][1]>= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[3];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_E[index][0]<= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_E[index][1]>= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[4];
		}else if((SUPERCOMENSATION_FROM_HI_MAP_RANGE_F[index][0]<= regressionMinimumOfHomeostasisIndex ) && (SUPERCOMENSATION_FROM_HI_MAP_RANGE_F[index][1]>= regressionMinimumOfHomeostasisIndex )){
			supercompensationPoints = SUPERCOMPENSATION_FROM_HI_BY_RANGE[5];
		}
		return supercompensationPoints;
	}
}
