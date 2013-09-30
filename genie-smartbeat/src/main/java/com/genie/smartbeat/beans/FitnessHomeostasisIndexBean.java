package com.genie.smartbeat.beans;

import java.sql.Timestamp;

import com.genie.smartbeat.domain.ShapeIndexAlgorithm;

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
	
	public FitnessHomeostasisIndexBean() {
		traineeClassification = ShapeIndexAlgorithm.TRAINEE_CLASSIFICATION_UNTRAINED;
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
	
	
	public boolean isValidForTableInsert() {
		if ((null != this.getUserid() && !this.getUserid().isEmpty())) {
			return true;
		}
		return false;
	}
}

