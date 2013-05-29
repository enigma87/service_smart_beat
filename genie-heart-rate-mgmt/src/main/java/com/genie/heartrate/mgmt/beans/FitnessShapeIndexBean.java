package com.genie.heartrate.mgmt.beans;

/**
 * @author dhasarathy
 **/

public class FitnessShapeIndexBean {

	public static final String TRAINEE_CLASSIFICATION_UNTRAINED 			= "untrained";
	public static final String TRAINEE_CLASSIFICATION_LIGHTLY_TRAINED 		= "lightly_trained";
	public static final String TRAINEE_CLASSIFICATION_MODERATELY_TRAINED 	= "moderately_trained";
	public static final String TRAINEE_CLASSIFICATION_WELL_TRAINED 			= "well_trained";
	public static final String TRAINEE_CLASSIFICATION_ELITE 				= "elite";
	
	private String userid;
	private String traineeClassification;
	private Double shapeIndex;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getTraineeClassification() {
		return traineeClassification;
	}
	
	public void setTraineeClassification(String traineeClassification) {
		this.traineeClassification = traineeClassification;
	}
	
	public Double getShapeIndex() {
		return shapeIndex;
	}
	
	public void setShapeIndex(Double shapeIndex) {
		this.shapeIndex = shapeIndex;
	}
}

