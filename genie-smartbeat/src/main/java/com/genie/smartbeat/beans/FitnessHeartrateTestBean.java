package com.genie.smartbeat.beans;

import java.sql.Timestamp;

/**
 * @author dhasarathy
 **/

public class FitnessHeartrateTestBean {
	
	private String userid;
	private String heartrateTestId;
	private Integer heartrateType;
	private Double heartrate;
	private Timestamp timeOfRecord;
	private Integer dayOfRecord;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getHeartrateTestId() {
		return heartrateTestId;
	}
	
	public void setHeartrateTestId(String heartrateTestId) {
		this.heartrateTestId = heartrateTestId;
	}
	
	public Integer getHeartrateType() {
		return heartrateType;
	}
	
	public void setHeartrateType(Integer heartrateType) {
		this.heartrateType = heartrateType;
	}
	
	public Double getHeartrate() {
		return heartrate;
	}
	
	public void setHeartrate(Double heartrate) {
		this.heartrate = heartrate;
	}
	
	public Timestamp getTimeOfRecord() {
		return timeOfRecord;
	}
	
	public void setTimeOfRecord(Timestamp timeOfRecord) {
		this.timeOfRecord = timeOfRecord;
	}	
	
	public Integer getDayOfRecord() {
		return dayOfRecord;
	}
	
	public void setDayOfRecord(Integer dayOfRecord) {
		this.dayOfRecord = dayOfRecord;
	}
}

