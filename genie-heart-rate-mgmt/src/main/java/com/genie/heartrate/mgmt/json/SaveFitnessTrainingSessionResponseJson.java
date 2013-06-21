package com.genie.heartrate.mgmt.json;

public class SaveFitnessTrainingSessionResponseJson {
	
	private String userid;
	private String trainingSessionId;
	private Double shapeIndex;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getTrainingSessionId() {
		return trainingSessionId;
	}
	
	public void setTrainingSessionId(String trainingSessionId) {
		this.trainingSessionId = trainingSessionId;
	}

	public Double getShapeIndex() {
		return shapeIndex;
	}
	
	public void setShapeIndex(Double shapeIndex) {
		this.shapeIndex = shapeIndex;
	}
}
