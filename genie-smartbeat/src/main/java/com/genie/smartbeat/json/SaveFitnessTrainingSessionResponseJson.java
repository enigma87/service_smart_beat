package com.genie.smartbeat.json;

public class SaveFitnessTrainingSessionResponseJson {

	private String userid;
	private String trainingSessionId;
	private Double shapeIndex;
	private Double recentTotalLoadOfExercise;
	private Double vDot;
	private Double recentMinimumOfHomeostasisIndex;
	private Integer traineeClassification;

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

	public Double getShapeIndex() {
		return shapeIndex;
	}

	public void setShapeIndex(Double shapeIndex) {
		this.shapeIndex = shapeIndex;
	}

	public Double getRecentTotalLoadOfExercise() {
		return recentTotalLoadOfExercise;
	}

	public void setRecentTotalLoadOfExercise(Double recentTotalLoadOfExercise) {
		this.recentTotalLoadOfExercise = recentTotalLoadOfExercise;
	}

	public Double getvDot() {
		return vDot;
	}

	public void setvDot(Double vDot) {
		this.vDot = vDot;
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