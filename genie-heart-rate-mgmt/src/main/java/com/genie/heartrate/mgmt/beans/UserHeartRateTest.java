package com.genie.heartrate.mgmt.beans;

import java.sql.Timestamp;

/**
 * @author manojkumar
 *
 */
public class UserHeartRateTest 
{
	private String userid;
	private Integer restingHeartRate;
	private Timestamp restingHeartRateTimestamp;
	private Integer maximalHeartRate;
	private Timestamp maximalHeartRateTimestamp;
	private Integer thresholdHeartRate;		
	private Timestamp thresholdHeartRateTimestamp;
	
	public String getUserid()
	{
		return this.userid;
	}
	
	public void setUserid(String userid)
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
	
	public Timestamp getRestingHeartRateTimestamp()
	{
		return this.restingHeartRateTimestamp;
	}
	
	public void setRestingHeartRateTimestamp(Timestamp restingHeartRateTimestamp)
	{
		this.restingHeartRateTimestamp = restingHeartRateTimestamp;
	}
	
    public Timestamp getMaximalHeartRateTimestamp() {
		return maximalHeartRateTimestamp;
	}
	
    public void setMaximalHeartRateTimestamp(Timestamp maximalHeartRateTimestamp) {
		this.maximalHeartRateTimestamp = maximalHeartRateTimestamp;
	}
    
    
    public Timestamp getThresholdHeartRateTimestamp() {
		return thresholdHeartRateTimestamp;
	}
    
    public void setThresholdHeartRateTimestamp(Timestamp thresholdHeartRateTimestamp) {
		this.thresholdHeartRateTimestamp = thresholdHeartRateTimestamp;
	}
    
    public boolean hasNewUpdates(){
    	return (null != getRestingHeartRate()) && (null != getMaximalHeartRate()) && (null != getThresholdHeartRate());
    }
    
    public void fillInTheBlanks(UserHeartRateTest uhrtToFillFrom){
    	
    	if(null == getRestingHeartRate()){
    		setRestingHeartRate(uhrtToFillFrom.getRestingHeartRate());
    		setRestingHeartRateTimestamp(uhrtToFillFrom.getMaximalHeartRateTimestamp());
    	}
    	
    	if(null == getMaximalHeartRate()){
    		setMaximalHeartRate(uhrtToFillFrom.getMaximalHeartRate());
    		setMaximalHeartRateTimestamp(uhrtToFillFrom.getMaximalHeartRateTimestamp());
    	}
    	
    	if(null == getThresholdHeartRate()){
    		setThresholdHeartRate(uhrtToFillFrom.getThresholdHeartRate());
    		setThresholdHeartRateTimestamp(thresholdHeartRateTimestamp = uhrtToFillFrom.getMaximalHeartRateTimestamp());
    	}    	        	
    }
}
