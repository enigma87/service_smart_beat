package com.genie.smartbeat.json;

import java.util.List;

import com.genie.smartbeat.beans.FitnessTrainingSessionBean;

public class TrainingSessionsByRangeResponseJson {

	private String userID;
	private List<FitnessTrainingSessionBean> trainingSessionBeans;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public List<FitnessTrainingSessionBean> getTrainingSessionBeans() {
		return trainingSessionBeans;
	}
	public void setTrainingSessionBeans(List<FitnessTrainingSessionBean> trainingSessionBeans) {
		this.trainingSessionBeans = trainingSessionBeans;
	}
}
