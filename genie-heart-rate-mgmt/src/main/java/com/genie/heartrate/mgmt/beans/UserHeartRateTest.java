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
	private Integer maximalHr;
	private Integer thresholdHr;
	private Calendar restingHeartRateTimestamp;
	private Calendar maximalHrTs;
	private Calendar thresholdHrTs;
	
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
	
	public Integer getMaximalHr()
	{
		return this.maximalHr;
	}
	
	public void setMaximalHr(Integer maximalHr)
	{
		this.maximalHr = maximalHr;
	}
	
	public Integer getThresholdHr()
	{
		return this.thresholdHr;
	}
	
	public void setThresholdHr(Integer thresholdHr)
	{
		this.thresholdHr = thresholdHr;
	}
	
	public Calendar getRestingHeartRateTimestamp()
	{
		return this.restingHeartRateTimestamp;
	}
	
	public void setRestingHeartRateTimestamp(Calendar restingHeartRateTimestamp)
	{
		this.restingHeartRateTimestamp = restingHeartRateTimestamp;
	}
	
	public Calendar getMaximalHrTs()
	{
		return this.maximalHrTs;
	}
	
	public void setMaximalHrTs(Calendar maximalHrTs)
	{
		this.maximalHrTs = maximalHrTs;
	}
	
	public Calendar getThresholdHrTs()
	{
		return this.thresholdHrTs;
	}
	
	public void setThresholdHrTs(Calendar thresholdHrTs)
	{
		this.thresholdHrTs = thresholdHrTs;
	}
}
