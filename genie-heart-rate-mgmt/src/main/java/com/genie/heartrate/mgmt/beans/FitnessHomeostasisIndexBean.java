package com.genie.heartrate.mgmt.beans;

import java.sql.Timestamp;

import com.genie.heartrate.mgmt.util.TraineeClassification;

/**
 * @author dhasarathy
 **/

public class FitnessHomeostasisIndexBean {


	private String userid;
	private Integer traineeClassification;
	private Double localRegressionMinimumOfHomeostasisIndex;
	private Double currentTotalLoadOfExercise;
	private Timestamp currentEndTime;
	private Double previousTotalLoadOfExercise;
	private Timestamp previousEndTime;
	

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
	
	public Double getCurrentTotalLoadOfExercise() {
		return currentTotalLoadOfExercise;
	}
	
	public void setCurrentTotalLoadOfExercise(Double currentTotalLoadOfExercise) {
		this.currentTotalLoadOfExercise = currentTotalLoadOfExercise;
	}
	
	public Timestamp getCurrentEndTime() {
		return currentEndTime;
	}
	
	public void setCurrentEndTime(Timestamp currentEndTime) {
		this.currentEndTime = currentEndTime;
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

