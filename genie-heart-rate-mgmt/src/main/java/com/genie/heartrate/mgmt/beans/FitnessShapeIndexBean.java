package com.genie.heartrate.mgmt.beans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


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
	
	private static final String DELIMITER_TRAINING_SESSION_ID = "_";
	public String getFirstTrainingSessiontId(String userid){
		SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
		Date currentDate = new Date();
		return userid + DELIMITER_TRAINING_SESSION_ID + formatYear.format(currentDate) + DELIMITER_TRAINING_SESSION_ID + "0";		
	}
	
	
	/*private static final int INDEX_USERID = 0;*/
	private static final int INDEX_YEAR = 1;
	private static final int INDEX_TRAINING_SESSION_COUNT = 2;
	
	public String getNextTrainingSessionId(String currentTrainingSessionId){
		String nextTrainingSessionId = null;
		String[] parts = currentTrainingSessionId.split(DELIMITER_TRAINING_SESSION_ID);
		SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
		Date currentDate = new Date();
		String currentYear = formatYear.format(currentDate);
		Integer nextCount;
		if(currentYear.equals(parts[INDEX_YEAR])){
			nextCount = Integer.parseInt(parts[INDEX_TRAINING_SESSION_COUNT]) + 1;			
		}else{
			nextCount = 1;
		}
		nextTrainingSessionId = userid + DELIMITER_TRAINING_SESSION_ID + currentYear + DELIMITER_TRAINING_SESSION_ID + nextCount;
		return nextTrainingSessionId;
	}
		
}

