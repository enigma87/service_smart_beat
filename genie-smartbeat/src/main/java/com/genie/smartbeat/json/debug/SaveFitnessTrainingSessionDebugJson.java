package com.genie.smartbeat.json.debug;

import java.util.List;

/**
 * @author dhasarathy
 **/

public class SaveFitnessTrainingSessionDebugJson {

	private List<FitnessTrainingSessionTimePoint> trainingTimePoints;
	
	public List<FitnessTrainingSessionTimePoint> getTrainingTimePoints() {
		return trainingTimePoints;
	}
	
	public void setTrainingTimePoints(
			List<FitnessTrainingSessionTimePoint> trainingTimePoints) {
		this.trainingTimePoints = trainingTimePoints;
	}
}

