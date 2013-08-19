package com.genie.smartbeat.beans;

/**
 * @author dhasarathy
 **/

public class FitnessSpeedHeartRateBean {

	private String userid;
	private Double currentVdot;
	private Double previousVdot;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public Double getCurrentVdot() {
		return currentVdot;
	}
	
	public void setCurrentVdot(Double currentVdot) {
		this.currentVdot = currentVdot;
	}
	
	public Double getPreviousVdot() {
		return previousVdot;
	}
	
	public void setPreviousVdot(Double previousVdot) {
		this.previousVdot = previousVdot;
	}
	
	public boolean isValidForTableInsert() {
		if ((null != this.getUserid() && !this.getUserid().isEmpty())) {
			return true;
		}
		return false;
	}
}