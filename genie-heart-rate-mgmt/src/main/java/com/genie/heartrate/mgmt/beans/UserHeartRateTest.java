package com.genie.heartrate.mgmt.beans;

import java.util.Calendar;

/**
 * @author manojkumar
 *
 */
public class UserHeartRateTest 
{
	private Long userid;
	private Integer restingHeartRate;
	private Integer maximalHeartRate;
	private Integer thresholdHeartRate;
	private Calendar restingHeartRateTimestamp;
	private Calendar maximalHeartRateTimestamp;
	private Calendar thresholdHeartRateTimestamp;
	
	public Long getUserid()
	{
		return this.userid;
	}
	
	public void setUserid(Long userid)
	{
		this.userid = userid;
	}
	
	public Integer getRestingHeartRate() {
		return restingHeartRate;
	}
	
	public void setRestingHeartRate(Integer restingHeartRate) {
		this.restingHeartRate = restingHeartRate;
	}
	
	
	public void setMaximalHeartRate(Integer maximalHeartRate) {
		this.maximalHeartRate = maximalHeartRate;
	}
	
	public Integer getMaximalHeartRate() {
		return maximalHeartRate;
	}
	
    public void setThresholdHeartRate(Integer thresholdHeartRate) {
		this.thresholdHeartRate = thresholdHeartRate;
	}
    
    public Integer getThresholdHeartRate() {
		return thresholdHeartRate;
	}
	
	public Calendar getRestingHeartRateTimestamp()
	{
		return this.restingHeartRateTimestamp;
	}
	
	public void setRestingHeartRateTimestamp(Calendar restingHeartRateTimestamp)
	{
		this.restingHeartRateTimestamp = restingHeartRateTimestamp;
	}
	
    public Calendar getMaximalHeartRateTimestamp() {
		return maximalHeartRateTimestamp;
	}
	
    public void setMaximalHeartRateTimestamp(Calendar maximalHeartRateTimestamp) {
		this.maximalHeartRateTimestamp = maximalHeartRateTimestamp;
	}
    
    
    public Calendar getThresholdHeartRateTimestamp() {
		return thresholdHeartRateTimestamp;
	}
    
    public void setThresholdHeartRateTimestamp(
			Calendar thresholdHeartRateTimestamp) {
		this.thresholdHeartRateTimestamp = thresholdHeartRateTimestamp;
	}
}
