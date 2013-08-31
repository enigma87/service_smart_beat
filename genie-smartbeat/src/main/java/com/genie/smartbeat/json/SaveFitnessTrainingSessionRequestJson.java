package com.genie.smartbeat.json;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.genie.smartbeat.beans.FitnessTrainingSessionBean;


@XmlRootElement(name = "SaveFitnessTrainingSessionRequestJson")
public class SaveFitnessTrainingSessionRequestJson {

	private String userid;
	private String startTime;
	private String endTime;	
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
	private Double percentageInclination;
	private Double percentageDeclination;

	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
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
	
	public Double getPercentageInclination() {
		return percentageInclination;
	}
	
	public void setPercentageInclination(Double percentageInclination) {
		this.percentageInclination = percentageInclination;
	}
	
	public Double getPercentageDeclination() {
		return percentageDeclination;
	}
	
	public void setPercentageDeclination(Double percentageDeclination) {
		this.percentageDeclination = percentageDeclination;
	}
	
	public FitnessTrainingSessionBean getAsTrainingSessionBean(){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date startTimeInDate = null;
        Date endTimeInDate = null;
		try {
			startTimeInDate = dateFormat.parse(getStartTime());
			endTimeInDate = dateFormat.parse(getEndTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid(getUserid());
		fitnessTrainingSessionBean.setHrz1Distance(getHrz1Distance());
		fitnessTrainingSessionBean.setHrz2Distance(getHrz2Distance());
		fitnessTrainingSessionBean.setHrz3Distance(getHrz3Distance());
		fitnessTrainingSessionBean.setHrz4Distance(getHrz4Distance());
		fitnessTrainingSessionBean.setHrz5Distance(getHrz5Distance());
		fitnessTrainingSessionBean.setHrz6Distance(getHrz6Distance());
		fitnessTrainingSessionBean.setHrz1Time(getHrz1Time());
		fitnessTrainingSessionBean.setHrz2Time(getHrz2Time());
		fitnessTrainingSessionBean.setHrz3Time(getHrz3Time());
		fitnessTrainingSessionBean.setHrz4Time(getHrz4Time());
		fitnessTrainingSessionBean.setHrz5Time(getHrz5Time());
		fitnessTrainingSessionBean.setHrz6Time(getHrz6Time());
		fitnessTrainingSessionBean.setStartTime(new Timestamp(startTimeInDate.getTime()));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(endTimeInDate.getTime()));
		fitnessTrainingSessionBean.setSurfaceIndex(getSurfaceIndex());
		fitnessTrainingSessionBean.setMuscleIndex(getMuscleIndex());
		fitnessTrainingSessionBean.setSessionIndex(getSessionIndex());
		fitnessTrainingSessionBean.setHealthIndex(getHealthIndex());
		fitnessTrainingSessionBean.setPercentageInclination(getPercentageInclination());
		fitnessTrainingSessionBean.setPercentageDeclination(getPercentageDeclination());
		
		return fitnessTrainingSessionBean;
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
}
