package com.genie.smartbeat.json;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;

/**
 * @author dhasarathy
 **/

public class SaveHeartrateTestRequestJson {

	private Integer heartrateType;
	private Double heartrate;
	private String timeOfRecord;
	
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
	
	public String getTimeOfRecord() {
		return timeOfRecord;
	}
	
	public void setTimeOfRecord(String timeOfRecord) {
		this.timeOfRecord = timeOfRecord;
	}
	
	public FitnessHeartrateTestBean getAsHeartrateTestBean(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date timeOfRecord = null;        
		try {
			timeOfRecord = dateFormat.parse(getTimeOfRecord());			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FitnessHeartrateTestBean heartrateTestBean = new FitnessHeartrateTestBean();
		heartrateTestBean.setHeartrateType(getHeartrateType());
		heartrateTestBean.setHeartrate(getHeartrate());
		heartrateTestBean.setTimeOfRecord(new Timestamp(timeOfRecord.getTime()));
		return heartrateTestBean;
	}
}

