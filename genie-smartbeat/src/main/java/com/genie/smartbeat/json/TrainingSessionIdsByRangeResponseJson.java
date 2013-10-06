package com.genie.smartbeat.json;

import java.util.List;

import com.genie.smartbeat.beans.FitnessTrainingSessionIdBean;

public class TrainingSessionIdsByRangeResponseJson {
	private String userID;
	private List<FitnessTrainingSessionIdBean> trainingSessionIDs;
	
	public String getUserID() {
		return this.userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public List<FitnessTrainingSessionIdBean> getTrainingSessionIDs() {
		return this.trainingSessionIDs;
	}
	
	public void setTrainingSessionIDs(List<FitnessTrainingSessionIdBean> trainingSessionIDs) {
		this.trainingSessionIDs = trainingSessionIDs;
	}
}

