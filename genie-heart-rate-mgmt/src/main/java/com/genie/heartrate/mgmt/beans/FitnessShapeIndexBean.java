package com.genie.heartrate.mgmt.beans;

import com.genie.heartrate.mgmt.util.TraineeClassification;

/**
 * @author dhasarathy
 **/

public class FitnessShapeIndexBean {
	
	private String userid;
	private TraineeClassification traineeClassification;
	private Double shapeIndex;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public TraineeClassification getTraineeClassification() {
		return traineeClassification;
	}
	
	public void setTraineeClassification(TraineeClassification traineeClassification) {
		this.traineeClassification = traineeClassification;
	}
	
	public Double getShapeIndex() {
		return shapeIndex;
	}
	
	public void setShapeIndex(Double shapeIndex) {
		this.shapeIndex = shapeIndex;
	}
}

