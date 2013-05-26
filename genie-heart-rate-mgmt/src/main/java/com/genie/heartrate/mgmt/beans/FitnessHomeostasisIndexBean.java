package com.genie.heartrate.mgmt.beans;

/**
 * @author dhasarathy
 **/

public class FitnessHomeostasisIndexBean {

	private String userid;
	private Double homeostasisIndex;
	private Double totalLoadOfExercise;
	private Double timeToRecover;
	private Integer lastTrainingSessionId;
	
	public FitnessHomeostasisIndexBean() {
		this.homeostasisIndex = 0.0;
	}
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public Double getHomeostasisIndex() {
		return homeostasisIndex;
	}
	
	public void setHomeostasisIndex(Double homeostasisIndex) {
		this.homeostasisIndex = homeostasisIndex;
	}
	
	public Double getTotalLoadOfExercise() {
		return totalLoadOfExercise;
	}
	
	public void setTotalLoadOfExercise(Double totalLoadOfExercise) {
		this.totalLoadOfExercise = totalLoadOfExercise;
	}
	
	public Double getTimeToRecover() {
		return timeToRecover;
	}
	
	public void setTimeToRecover(Double timeToRecover) {
		this.timeToRecover = timeToRecover;
	}
	
	public Integer getLastTrainingSessionId() {
		return lastTrainingSessionId;
	}
	
	public void setLastTrainingSessionId(Integer lastTrainingSessionId) {
		this.lastTrainingSessionId = lastTrainingSessionId;
	}
}

