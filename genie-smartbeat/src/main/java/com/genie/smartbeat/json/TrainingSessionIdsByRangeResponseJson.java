package com.genie.smartbeat.json;

import java.util.List;

public class TrainingSessionIdsByRangeResponseJson {
	private String userID;
	private List<String> trainingSessionIDs;
	
	public String getUserID() {
		return this.userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public List<String> getTrainingSessionIDs() {
		return this.trainingSessionIDs;
	}
	
	public void setTrainingSessionIDs(List<String> trainingSessionIDs) {
		this.trainingSessionIDs = trainingSessionIDs;
	}
}

