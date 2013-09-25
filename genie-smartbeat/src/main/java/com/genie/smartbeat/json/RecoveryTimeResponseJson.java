package com.genie.smartbeat.json;
import java.sql.Timestamp;

public class RecoveryTimeResponseJson {

	private String recentTrainingSessionId;
	private Timestamp recoveryTime;
	
	public void setRecentTrainingSessionId(String recentTrainingSessionId) {
		this.recentTrainingSessionId = recentTrainingSessionId;
	}
	
    public void setRecoveryTime(Timestamp recoveryTime) {
		this.recoveryTime = recoveryTime;
	}
	
	public String getRecentTrainingSessionId() {
		return recentTrainingSessionId;
	}
	
	public Timestamp getRecoveryTime() {
		return recoveryTime;
	}
		
	
}
