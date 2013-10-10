package com.genie.smartbeat.json;
import java.sql.Timestamp;

public class RecoveryTimeResponseJson {

	private String userId;
	private String recentTrainingSessionId;
	private Double recentMinimumOfHomeostasisIndex;
	private Double localRegressionMinimumOfHomeostasisIndex;
	private Timestamp recoveryTime;
	private Integer traineeClassification;
	
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
    
	public Double getLocalRegressionMinimumOfHomeostasisIndex() {
		return localRegressionMinimumOfHomeostasisIndex;
	}

	public void setLocalRegressionMinimumOfHomeostasisIndex(
			Double localRegressionMinimumOfHomeostasisIndex) {
		this.localRegressionMinimumOfHomeostasisIndex = localRegressionMinimumOfHomeostasisIndex;
	}

	public Double getRecentMinimumOfHomeostasisIndex() {
		return recentMinimumOfHomeostasisIndex;
	}

	public void setRecentMinimumOfHomeostasisIndex(
			Double recentMinimumOfHomeostasisIndex) {
		this.recentMinimumOfHomeostasisIndex = recentMinimumOfHomeostasisIndex;
	}

	public Integer getTraineeClassification() {
		return traineeClassification;
	}

	public void setTraineeClassification(Integer traineeClassification) {
		this.traineeClassification = traineeClassification;
	}
}
