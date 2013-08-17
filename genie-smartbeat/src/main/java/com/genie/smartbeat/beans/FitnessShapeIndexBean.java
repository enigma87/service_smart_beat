package com.genie.smartbeat.beans;

import java.sql.Timestamp;


/**
 * @author dhasarathy
 **/

public class FitnessShapeIndexBean {
	
	private String userid;	
	private Double shapeIndex;
	private Timestamp timeOfRecord;	
	private String sessionOfRecord;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Double getShapeIndex() {
		return shapeIndex;
	}
	
	public void setShapeIndex(Double shapeIndex) {
		this.shapeIndex = shapeIndex;
	}
	
	public Timestamp getTimeOfRecord() {
		return timeOfRecord;
	}
	
	public void setTimeOfRecord(Timestamp timeOfRecord) {
		this.timeOfRecord = timeOfRecord;
	}
			
	public String getSessionOfRecord() {
		return sessionOfRecord;
	}
	
	public void setSessionOfRecord(String sessionOfRecord) {
		this.sessionOfRecord = sessionOfRecord;
	}				
	
	public boolean isValidForTableInsert() {
		if (this.getUserid().isEmpty()
				|| this.getSessionOfRecord().isEmpty()) {

			return false;
		}
		return true;
	}
}