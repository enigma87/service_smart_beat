package com.genie.heartrate.mgmt.beans;


/**
 * @author dhasarathy
 **/

public class FitnessShapeIndexBean {
	
	private String userid;
	private Integer traineeClassification;
	private Double shapeIndex;
	
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
    
	public Double getShapeIndex() {
		return shapeIndex;
	}
	
	public void setShapeIndex(Double shapeIndex) {
		this.shapeIndex = shapeIndex;
	}
}

