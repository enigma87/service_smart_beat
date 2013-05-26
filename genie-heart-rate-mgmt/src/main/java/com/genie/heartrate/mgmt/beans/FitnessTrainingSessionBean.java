package com.genie.heartrate.mgmt.beans;

import java.sql.Timestamp;

/**
 * @author dhasarathy
 **/

public class FitnessTrainingSessionBean {
	private String userid;
	private Integer trainingSessionId;
	private Timestamp startTime;
	private Timestamp endTime;	
	private Double hrz1Time;
	private Double hrz2Time;
	private Double hrz3Time;
	private Double hrz4Time;
	private Double hrz5Time;
	private Double hrz6Time;
	private Double hrz1Distance;
	private Double hrz2Distance;
	private Double hrz3Distance;
	private Double hrz4Distance;
	private Double hrz5Distance;
	private Double hrz6Distance;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public void setTrainingSessionId(Integer trainingSessionId) {
		this.trainingSessionId = trainingSessionId;
	}
	
	public Integer getTrainingSessionId() {
		return trainingSessionId;
	}
	
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
	public Timestamp getStartTime() {
		return startTime;
	}
	
	public Timestamp getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}	
	
	public Double getHrz1Time() {
		return hrz1Time;
	}
	
	public void setHrz1Time(Double hrz1Time) {
		this.hrz1Time = hrz1Time;
	}
	
	public Double getHrz2Time() {
		return hrz2Time;
	}
	
	public void setHrz2Time(Double hrz2Time) {
		this.hrz2Time = hrz2Time;
	}
	
	public Double getHrz3Time() {
		return hrz3Time;
	}
	
	public void setHrz3Time(Double hrz3Time) {
		this.hrz3Time = hrz3Time;
	}
	
	public Double getHrz4Time() {
		return hrz4Time;
	}
	
	public void setHrz4Time(Double hrz4Time) {
		this.hrz4Time = hrz4Time;
	}
	
	public Double getHrz5Time() {
		return hrz5Time;
	}
	
	public void setHrz5Time(Double hrz5Time) {
		this.hrz5Time = hrz5Time;
	}
	
	public Double getHrz6Time() {
		return hrz6Time;
	}
	
	public void setHrz6Time(Double hrz6Time) {
		this.hrz6Time = hrz6Time;
	}
	
	public Double getHrz1Distance() {
		return hrz1Distance;
	}
	
	public void setHrz1Distance(Double hrz1Distance) {
		this.hrz1Distance = hrz1Distance;
	}
	
	public Double getHrz2Distance() {
		return hrz2Distance;
	}
	
	public void setHrz2Distance(Double hrz2Distance) {
		this.hrz2Distance = hrz2Distance;
	}
	
	public Double getHrz3Distance() {
		return hrz3Distance;
	}
	
	public void setHrz3Distance(Double hrz3Distance) {
		this.hrz3Distance = hrz3Distance;
	}
	
	public Double getHrz4Distance() {
		return hrz4Distance;
	}
	
	public void setHrz4Distance(Double hrz4Distance) {
		this.hrz4Distance = hrz4Distance;
	}
	
	public Double getHrz5Distance() {
		return hrz5Distance;
	}
	
	public void setHrz5Distance(Double hrz5Distance) {
		this.hrz5Distance = hrz5Distance;
	}
	
	public Double getHrz6Distance() {
		return hrz6Distance;
	}
	
	public void setHrz6Distance(Double hrz6Distance) {
		this.hrz6Distance = hrz6Distance;
	}

}

