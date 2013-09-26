package com.genie.smartbeat.beans;

import java.sql.Timestamp;

import com.genie.smartbeat.core.TrainingSessionValidityStatus;

/**
 * @author dhasarathy
 **/

public class FitnessTrainingSessionBean {
	public static final int NUMBER_OF_ZONES = 6;	
	private String userid;
	private String trainingSessionId;
	private Timestamp startTime;
	private Timestamp endTime;
	/*time in minutes*/
	private Double hrz1Time;
	private Double hrz2Time;
	private Double hrz3Time;
	private Double hrz4Time;
	private Double hrz5Time;
	private Double hrz6Time;
	/*distance in m*/
	private Double hrz1Distance;
	private Double hrz2Distance;
	private Double hrz3Distance;
	private Double hrz4Distance;
	private Double hrz5Distance;
	private Double hrz6Distance;
	private Integer surfaceIndex;
	private Double vdot;
	private Integer healthPerceptionIndex;
	private Integer muscleStatePerceptionIndex;
	private Integer sessionStressPerceptionIndex;
	/*altitude in m*/
	private Double averageAltitude;
	/*load in kg*/
	private Double extraLoad;
	private TrainingSessionValidityStatus validityStatus;
	
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
	
	public Double getSessionDuration(){
		Double sessionDurationInMinutes = 0.0;
		if(null != getStartTime() && null != getEndTime()){
			if(getEndTime().getTime() > getStartTime().getTime()){
				sessionDurationInMinutes = new Double((getEndTime().getTime() - getStartTime().getTime())/(1000*60));
			}
		}
		return sessionDurationInMinutes;
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

	public Double getVdot() {
		return vdot;
	}
	
	public void setVdot(Double vdot) {
		this.vdot = vdot;
	}
	
	public double[] getTimeDistributionOfHRZ(){
		double[] timeDistributionOfHrz = new double[7];
		int nullZoneCount = 0;
		
		if(null != getHrz1Time() && 0.0 < getHrz1Time()){
			timeDistributionOfHrz[1] = getHrz1Time().doubleValue();
		}else{
			nullZoneCount++;
		}
		
		if(null != getHrz2Time() && 0.0 < getHrz2Time()){
			timeDistributionOfHrz[2] = getHrz2Time().doubleValue();
		}else{
			nullZoneCount++;
		}
		
		if(null != getHrz3Time() && 0.0 < getHrz3Time()){
			timeDistributionOfHrz[3] = getHrz3Time().doubleValue();
		}else{
			nullZoneCount++;
		}
		
		if(null != getHrz4Time() && 0.0 < getHrz4Time()){
			timeDistributionOfHrz[4] = getHrz4Time().doubleValue();
		}else{
			nullZoneCount++;
		}
		
		if(null != getHrz5Time() && 0.0 < getHrz5Time()){
			timeDistributionOfHrz[5] = getHrz5Time().doubleValue();
		}else{
			nullZoneCount++;
		}
		
		if(null != getHrz6Time() && 0.0 < getHrz6Time()){
			timeDistributionOfHrz[6] = getHrz6Time().doubleValue();
		}else{
			nullZoneCount++;
		}
		
		if(NUMBER_OF_ZONES == nullZoneCount){
			timeDistributionOfHrz = null;
		}
		return timeDistributionOfHrz;
	}

	public double[] getSpeedDistributionOfHRZ(){
		double[] speedDistributiOnofHrz = null;
		if(null != getTimeDistributionOfHRZ()){
			speedDistributiOnofHrz = new double[7];
			if (0.0 < getHrz1Time() ){
				speedDistributiOnofHrz[1] = (getHrz1Distance()/getHrz1Time())*0.06;
			}else{
				speedDistributiOnofHrz[1] = 0.0;
			}
			
			if (0.0 < getHrz2Time()){
				speedDistributiOnofHrz[2] = (getHrz2Distance()/getHrz2Time())*0.06;
			}else{
				speedDistributiOnofHrz[2] = 0.0;
			}
		
			if(0.0 < getHrz3Time()){
				speedDistributiOnofHrz[3] = (getHrz3Distance()/getHrz3Time())*0.06;
			}else{
				speedDistributiOnofHrz[3] = 0.0;
			}
			
			if(0.0 < getHrz4Time()){
				speedDistributiOnofHrz[4] = (getHrz4Distance()/getHrz4Time())*0.06;
			}else{
				speedDistributiOnofHrz[4] = 0.0;
			}
			
			if(0.0 < getHrz5Time()){
				speedDistributiOnofHrz[5] = (getHrz5Distance()/getHrz5Time())*0.06;
			}else{
				speedDistributiOnofHrz[5] = 0.0;
			}
			
			if (0.0 < getHrz6Time()){
				speedDistributiOnofHrz[6] = (getHrz6Distance()/getHrz6Time())*0.06;
			}else{
				speedDistributiOnofHrz[6] = 0.0;
			}		
		}
			return speedDistributiOnofHrz;
	}

	public boolean isValidForTableInsert() {		
		if ((null != this.getUserid() && !this.getUserid().isEmpty())
				&& (null != this.getTrainingSessionId() && !this.getTrainingSessionId().isEmpty())) {
			
			return true;
		}
		return false;
	}

	public Integer getHealthPerceptionIndex() {
		return healthPerceptionIndex;
	}

	public void setHealthPerceptionIndex(Integer healthPerceptionIndex) {
		this.healthPerceptionIndex = healthPerceptionIndex;
	}

	public Integer getMuscleStatePerceptionIndex() {
		return muscleStatePerceptionIndex;
	}

	public void setMuscleStatePerceptionIndex(Integer muscleStatePerceptionIndex) {
		this.muscleStatePerceptionIndex = muscleStatePerceptionIndex;
	}

	public Integer getSessionStressPerceptionIndex() {
		return sessionStressPerceptionIndex;
	}

	public void setSessionStressPerceptionIndex(
			Integer sessionStressPerceptionIndex) {
		this.sessionStressPerceptionIndex = sessionStressPerceptionIndex;
	}

	public Double getAverageAltitude() {
		return averageAltitude;
	}
	
	public double getAsDoubleValueAverageAltitude(){
		double averageAltitudeAsDoubleValue = 0.0;
		if(null != getAverageAltitude()){
			averageAltitudeAsDoubleValue = getAverageAltitude().doubleValue();
		}
		return averageAltitudeAsDoubleValue;
	}

	public void setAverageAltitude(Double averageAltitude) {
		this.averageAltitude = averageAltitude;
	}

	public Double getExtraLoad() {
		return extraLoad;
	}
	
	public double getAsDoubleValueExtraLoad(){
		double extraLoadAsDoubleValue = 0.0;
		if(null != getExtraLoad()){
			extraLoadAsDoubleValue = getExtraLoad().doubleValue();
		}
		return extraLoadAsDoubleValue;
	}

	public void setExtraLoad(Double extraLoad) {
		this.extraLoad = extraLoad;
	}

	public TrainingSessionValidityStatus getValidityStatus() {
		return validityStatus;
	}

	public void setValidityStatus(TrainingSessionValidityStatus validityStatus) {
		this.validityStatus = validityStatus;
	}
	
	@Override
	public String toString() {
		return "ID:" + getTrainingSessionId() +"-" 
				+ "-" + "startTime:" 		+ getStartTime() 	+ "-" 
				+ "-" + "endTime:"  	 	+ getEndTime() 		+ "-"
				+ "-" + "hrz1Time:" 		+ getHrz1Time() 	+ "-"
				+ "-" + "hrz1Distance" 	+ getHrz1Distance() + "-"
				+ "-" + "hrz2Time:" 		+ getHrz1Time() 	+ "-"
				+ "-" + "hrz2Distance" 	+ getHrz1Distance() + "-"
				+ "-" + "hrz3Time:" 		+ getHrz1Time() 	+ "-"
				+ "-" + "hrz3Distance" 	+ getHrz1Distance() + "-"
				+ "-" + "hrz4Time:" 		+ getHrz1Time() 	+ "-"
				+ "-" + "hrz4Distance" 	+ getHrz1Distance() + "-"
				+ "-" + "hrz5Time:" 		+ getHrz1Time() 	+ "-"
				+ "-" + "hrz5Distance" 	+ getHrz1Distance() + "-"
				+ "-" + "hrz6Time:" 		+ getHrz1Time() 	+ "-"
				+ "-" + "hrz6Distance" 	+ getHrz1Distance() + "-"
				+ "-" + "extraLoad" 		+ getExtraLoad() 	+ "-"
				+ "-" + "averageAltitude" 	+ getAverageAltitude() + "-"
				+ "-" + "surfaceIndex" 	+ getSurfaceIndex() + "-"				
				+ "-" + "sessionStressPerceptionIndex" 	+ getSessionStressPerceptionIndex() 	+ "-"
				+ "-" + "healthPerceptionIndex" 			+ getHealthPerceptionIndex() 			+ "-"
				+ "-" + "muscleStatePerceptionIndex" 		+ getMuscleStatePerceptionIndex();
	}
}
