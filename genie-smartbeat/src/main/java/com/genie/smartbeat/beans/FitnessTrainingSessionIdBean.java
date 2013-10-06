package com.genie.smartbeat.beans;
import java.sql.Timestamp;

public class FitnessTrainingSessionIdBean {
	
	private String trainingSessionId;
	private Timestamp startTime;
	private Timestamp endTime;
	
	public String getTrainingSessionId() {
		return trainingSessionId;
	}
	public void setTrainingSessionId(String trainingSessionId) {
		this.trainingSessionId = trainingSessionId;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	
}
