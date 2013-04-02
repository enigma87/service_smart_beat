package com.genie.heartrate.mgmt.beans;

import java.util.Calendar;

/**
 * @author manojkumar
 *
 */
public class UserHeartRateTest 
{
	private Long userid;
	private Integer restingHr;
	private Integer maximalHr;
	private Integer thresholdHr;
	private Calendar restingHrTs;
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
	
	public Integer getRestingHr()
	{
		return this.restingHr;
	}
	
	public void setRestingHr(Integer restingHr)
	{
		this.restingHr = restingHr;
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
	
	public Calendar getRestingHrTs()
	{
		return this.restingHrTs;
	}
	
	public void setRestingHrTs(Calendar restingHrTs)
	{
		this.restingHrTs = restingHrTs;
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
