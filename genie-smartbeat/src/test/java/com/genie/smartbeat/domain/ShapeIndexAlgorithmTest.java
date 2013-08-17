package com.genie.smartbeat.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * @author dhasarathy
 **/

public class ShapeIndexAlgorithmTest {
	
	@Test
	public void testGetDefaultRMTHeartrates() {
		int traineeClassification = ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_MODERATELY_TRAINED, age = 25, gender = ShapeIndexAlgorithm.GENDER_FEMALE;
		double[] defaultHRZs = ShapeIndexAlgorithm.getDefaultRMTHeartrates(traineeClassification, age, gender);
		assertEquals(60, Math.round(defaultHRZs[0]));
		assertEquals(147, Math.round(defaultHRZs[1]));
		assertEquals(189, Math.round(defaultHRZs[2]));		
	}

	@Test
	public void testCalculateHeartrateZones() {
		double restingHeartrate = 60, thresholdHeartrate = 147, maximalHeartrate = 189;		
		double[][] hrz = ShapeIndexAlgorithm.calculateHeartrateZones(restingHeartrate, thresholdHeartrate, maximalHeartrate);		
		assertEquals(60, Math.round(hrz[1][0]));
		assertEquals(104, Math.round(hrz[1][1]));
		assertEquals(104, Math.round(hrz[2][0]));
		assertEquals(125, Math.round(hrz[2][1]));
		assertEquals(125, Math.round(hrz[3][0]));
		/*assertEquals(143, Math.round(hrz[3][1]));
		assertEquals(143, Math.round(hrz[4][0]));*/
		/*assertEquals(149, Math.round(hrz[4][1]));
		assertEquals(149, Math.round(hrz[5][0]));*/
		assertEquals(181, Math.round(hrz[5][1]));
		assertEquals(181, Math.round(hrz[6][0]));
		assertEquals(189, Math.round(hrz[6][1]));
		
		restingHeartrate = 54;
		hrz = ShapeIndexAlgorithm.calculateHeartrateZones(restingHeartrate, thresholdHeartrate, maximalHeartrate);
		
		assertEquals(54, Math.round(hrz[1][0]));
		assertEquals(101, Math.round(hrz[1][1]));
		assertEquals(101, Math.round(hrz[2][0]));
		assertEquals(124, Math.round(hrz[2][1]));
		assertEquals(124, Math.round(hrz[3][0]));
		/*assertEquals(143, Math.round(hrz[3][1]));
		assertEquals(143, Math.round(hrz[4][0]));*/
		/*assertEquals(149, Math.round(hrz[4][1]));
		assertEquals(149, Math.round(hrz[5][0]));*/
		assertEquals(181, Math.round(hrz[5][1]));
		assertEquals(181, Math.round(hrz[6][0]));
		assertEquals(189, Math.round(hrz[6][1]));
	}

	@Test
	public void testCalculateTotalLoadofExercise() {
		double[] timeDistributionOfHeartRateZones = {4,32,14,10,0,0};
		double totalLoadOfExercise = ShapeIndexAlgorithm.calculateTotalLoadofExercise(timeDistributionOfHeartRateZones);
		assertEquals(Math.round(86.5), Math.round(totalLoadOfExercise));
	}

	@Test
	public void testGetRegressedHomeostasisIndex() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRegressionMinimumOfHomeostasisIndex() {
		double regressedHomeostasisIndex = 0, totalLoadOfExercise = 86.5;
		double regressionMinimumOfHomeostasisIndex = ShapeIndexAlgorithm.getRegressionMinimumOfHomeostasisIndex(regressedHomeostasisIndex, totalLoadOfExercise);
		assertEquals(Math.round(-86.5), Math.round(regressionMinimumOfHomeostasisIndex));
	}

	@Test
	public void testCalculateTimeAtFullRecovery() {
		int traineeClassification = ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_MODERATELY_TRAINED;
		Timestamp trainingSessionEndTime = new Timestamp(new Date().getTime());
		double recentMinimumOfHomeostasisIndex = -86.5;
		Timestamp timeAtFullRecovery = ShapeIndexAlgorithm.calculateTimeAtFullRecovery(traineeClassification, trainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		assertEquals(timeAtFullRecovery.getTime(), trainingSessionEndTime.getTime()+(83040*1000));		
	}

	@Test
	public void testCalculateTimeToRecover() {
		int traineeClassification = ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_MODERATELY_TRAINED;
		Timestamp trainingSessionEndTime = new Timestamp(new Date().getTime());
		double recentMinimumOfHomeostasisIndex = -86.5;
		double timeToRecover = ShapeIndexAlgorithm.calculateTimeToRecover(traineeClassification, trainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		assertEquals(Math.round(23), Math.round(timeToRecover));
	}

	@Test
	public void testCalculateTimeAfterRecovery() {
		int traineeClassification = ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_MODERATELY_TRAINED;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		calendar.add(Calendar.HOUR, -1);
		Timestamp trainingSessionEndTime = new Timestamp(calendar.getTimeInMillis());
		double recentMinimumOfHomeostasisIndex = -86.5;
		double timeAfterRecovery = ShapeIndexAlgorithm.calculateTimeAfterRecovery(traineeClassification, trainingSessionEndTime, recentMinimumOfHomeostasisIndex);
		assertEquals(Math.round(1.93),Math.round(timeAfterRecovery));
		System.out.println(timeAfterRecovery);
	}

	@Test
	public void testCalculateSupercompensationPoints() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalculateDetrainingPenalty() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalculateVdot() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalculateCompoundedVdot() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalculateSlopeOfTimeRegressionOfStandingOrthostaticHeartRate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTraineeClassificationUsingVdot() {
		fail("Not yet implemented");
	}

}

