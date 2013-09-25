package com.genie.smartbeat.json;
import java.sql.Timestamp;

public class RecoveryTimeResponseJson {

	private String userId;
	private String recentTrainingSessionId;
	private Timestamp recoveryTime;
	
	public void setRecentTrainingSessionId(String recentTrainingSessionId) {
		this.recentTrainingSessionId = recentTrainingSessionId;
	}
	
    public void setRecoveryTime(Timestamp recoveryTime) {
		this.recoveryTime = recoveryTime;
	}
    
    public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRecentTrainingSessionId() {
		return recentTrainingSessionId;
	}
	
	public Timestamp getRecoveryTime() {
		return recoveryTime;
	}
		
    public String getUserId() {
		return userId;
	}	
}
