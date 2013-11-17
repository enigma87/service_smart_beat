package com.genie.smartbeat.json.debug;

import java.sql.Timestamp;

/**
 * @author dhasarathy
 **/

public class FitnessTrainingSessionTimePoint {

	private Timestamp time;
	private Double heartrate;
	private Double latitude;
	private Double longitude;
	private Double altitude;
	private Double speed;
	
	public Timestamp getTime() {
		return time;
	}
	
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public Double getHeartrate() {
		return heartrate;
	}
	
	public void setHeartrate(Double heartrate) {
		this.heartrate = heartrate;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getAltitude() {
		return altitude;
	}
	
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}
	
	public Double getSpeed() {
		return speed;
	}
	
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	
}

