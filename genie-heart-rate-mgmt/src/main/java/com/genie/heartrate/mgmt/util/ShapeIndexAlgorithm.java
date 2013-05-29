/**
 * 
 */
package com.genie.heartrate.mgmt.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
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
	public static double calculateTotalLoadofExercise(FitnessTrainingSessionBean fitnessTrainingSessionBean){
		double totalLoadOfExercise = TRAINING_IMPACT_BY_ZONE[1]*fitnessTrainingSessionBean.getHrz1Time() +
				TRAINING_IMPACT_BY_ZONE[2]*fitnessTrainingSessionBean.getHrz2Time() +
				TRAINING_IMPACT_BY_ZONE[3]*fitnessTrainingSessionBean.getHrz3Time() +
				TRAINING_IMPACT_BY_ZONE[4]*fitnessTrainingSessionBean.getHrz4Time() +
				TRAINING_IMPACT_BY_ZONE[5]*fitnessTrainingSessionBean.getHrz5Time() +
				TRAINING_IMPACT_BY_ZONE[6]*fitnessTrainingSessionBean.getHrz6Time();
		return totalLoadOfExercise;
	}
}
