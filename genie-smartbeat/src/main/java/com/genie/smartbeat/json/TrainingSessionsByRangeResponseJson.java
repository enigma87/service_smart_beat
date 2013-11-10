package com.genie.smartbeat.json;

import java.util.List;

import com.genie.smartbeat.beans.FitnessTrainingSessionBean;

public class TrainingSessionsByRangeResponseJson {

	private String userID;
	private List<TrainingSessionByIdResponseJson> trainingSessions;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public List<TrainingSessionByIdResponseJson> getTrainingSessions() {
		return trainingSessions;
	}
	public void setTrainingSessions(List<TrainingSessionByIdResponseJson> trainingSessionBeans) {
		this.trainingSessions = trainingSessionBeans;
	}
}
