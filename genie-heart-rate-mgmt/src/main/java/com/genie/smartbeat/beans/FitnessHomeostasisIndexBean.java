package com.genie.smartbeat.beans;

import java.sql.Timestamp;

/**
 * @author dhasarathy
 **/

public class FitnessHomeostasisIndexBean {


	private String userid;
	private Integer traineeClassification;
	private Double localRegressionMinimumOfHomeostasisIndex;
	private Double recentMinimumOfHomeostasisIndex;
	private Double recentTotalLoadOfExercise;	
	private Timestamp recentEndTime;
	private Double previousTotalLoadOfExercise;	
	private Timestamp previousEndTime;		
	
	public static final Integer UNTRAINED = 1;
	public static final Integer LIGHTLY_TRAINED = 2;
	public static final Integer MODERATELY_TRAINED = 3;
	public static final Integer WELL_TRAINED = 4;
	public static final Integer ELITE = 5;
	
	
	public FitnessHomeostasisIndexBean() {
		traineeClassification = UNTRAINED;
	}

	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}	
	
	  public Integer getTraineeClassification() {
		return traineeClassification;
	    }
	  
	  public void setTraineeClassification(Integer traineeClassification) {
		this.traineeClassification = traineeClassification;
	}
		
	public Double getLocalRegressionMinimumOfHomeostasisIndex() {
		return localRegressionMinimumOfHomeostasisIndex;
	}
	
	public void setLocalRegressionMinimumOfHomeostasisIndex(
			Double localRegressionMinimumOfHomeostasisIndex) {
		this.localRegressionMinimumOfHomeostasisIndex = localRegressionMinimumOfHomeostasisIndex;
	}
	
	public Double getRecentMinimumOfHomeostasisIndex() {
		return recentMinimumOfHomeostasisIndex;
	}
	
	public void setRecentMinimumOfHomeostasisIndex(
			Double recentMinimumOfHomeostasisIndex) {
		this.recentMinimumOfHomeostasisIndex = recentMinimumOfHomeostasisIndex;
	}
	
	public Double getRecentTotalLoadOfExercise() {
		return recentTotalLoadOfExercise;
	}
	
	public void setRecentTotalLoadOfExercise(Double currentTotalLoadOfExercise) {
		this.recentTotalLoadOfExercise = currentTotalLoadOfExercise;
	}	
	
	public Timestamp getRecentEndTime() {
		return recentEndTime;
	}
	
	public void setRecentEndTime(Timestamp currentEndTime) {
		this.recentEndTime = currentEndTime;
	}
	
	public Double getPreviousTotalLoadOfExercise() {
		return previousTotalLoadOfExercise;
	}
	
	public void setPreviousTotalLoadOfExercise(
			Double previousTotalLoadOfExercise) {
		this.previousTotalLoadOfExercise = previousTotalLoadOfExercise;
	}
	
	public Timestamp getPreviousEndTime() {
		return previousEndTime;
	}
		
	public void setPreviousEndTime(Timestamp previousEndTime) {
		this.previousEndTime = previousEndTime;
	}		
	
}

