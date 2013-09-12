package com.genie.smartbeat.json;

import java.sql.Timestamp;

import com.genie.smartbeat.beans.FitnessTrainingSessionBean;

public class TrainingSessionByIdResponseJson {
	private String userid;
	private String trainingSessionId;
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
	private Integer surfaceIndex;
	private Integer muscleIndex;
	private Integer sessionIndex;
	private Integer healthIndex;
	private Double averageAltitude;
	private Double extraLoad;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getTrainingSessionId() {
		return trainingSessionId;
	}
	
	public void setTrainingSessionId(String trainingSessionId) {
		this.trainingSessionId = trainingSessionId;
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
	
	public Integer getSurfaceIndex() {
		return surfaceIndex;
	}
	
	public void setSurfaceIndex(Integer surfaceIndex) {
		this.surfaceIndex = surfaceIndex;
	}
	
	public void noJSONSetTrainingSession(FitnessTrainingSessionBean trainingSessionBean) {
		this.setHrz1Time(trainingSessionBean.getHrz1Time());
		this.setHrz2Time(trainingSessionBean.getHrz2Time());
		this.setHrz3Time(trainingSessionBean.getHrz3Time());
		this.setHrz4Time(trainingSessionBean.getHrz4Time());
		this.setHrz5Time(trainingSessionBean.getHrz5Time());
		this.setHrz6Time(trainingSessionBean.getHrz6Time());
		this.setHrz1Distance(trainingSessionBean.getHrz1Distance());
		this.setHrz2Distance(trainingSessionBean.getHrz2Distance());
		this.setHrz3Distance(trainingSessionBean.getHrz3Distance());
		this.setHrz4Distance(trainingSessionBean.getHrz4Distance());
		this.setHrz4Distance(trainingSessionBean.getHrz5Distance());
		this.setHrz5Distance(trainingSessionBean.getHrz6Distance());
		this.setHrz6Distance(trainingSessionBean.getHrz6Distance());
		this.setStartTime(trainingSessionBean.getStartTime());
		this.setEndTime(trainingSessionBean.getEndTime());
		this.setUserid(trainingSessionBean.getUserid());
		this.setTrainingSessionId(trainingSessionBean.getTrainingSessionId());
		this.setSurfaceIndex(trainingSessionBean.getSurfaceIndex());
		this.setMuscleIndex(trainingSessionBean.getMuscleStatePerceptionIndex());
		this.setSessionIndex(trainingSessionBean.getSessionStressPerceptionIndex());
		this.setHealthIndex(trainingSessionBean.getHealthPerceptionIndex());
		this.setAverageAltitude(trainingSessionBean.getAverageAltitude());
		this.setExtraLoad(trainingSessionBean.getExtraLoad());
	}

	public Integer getMuscleIndex() {
		return muscleIndex;
	}

	public void setMuscleIndex(Integer muscleIndex) {
		this.muscleIndex = muscleIndex;
	}

	public Integer getSessionIndex() {
		return sessionIndex;
	}

	public void setSessionIndex(Integer sessionIndex) {
		this.sessionIndex = sessionIndex;
	}

	public Integer getHealthIndex() {
		return healthIndex;
	}

	public void setHealthIndex(Integer healthIndex) {
		this.healthIndex = healthIndex;
	}

	public Double getAverageAltitude() {
		return averageAltitude;
	}

	public void setAverageAltitude(Double averageAltitude) {
		this.averageAltitude = averageAltitude;
	}

	public Double getExtraLoad() {
		return extraLoad;
	}

	public void setExtraLoad(Double extraLoad) {
		this.extraLoad = extraLoad;
	}
}
