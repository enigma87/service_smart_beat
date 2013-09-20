package com.genie.smartbeat.core;

import com.genie.smartbeat.beans.FitnessTrainingSessionBean;

public class TrainingSessionValidityStatus {
	public enum Status {
		APPROVED_VALID(0),
		DENIED(1),
		DENIED_INVALID_IN_CHRONOLOGY(2);
		
		private int statusval;
		
		private  Status(int val) {
			this.statusval = val;
		}
		
		public int getValue() {
			return this.statusval;
		}
		
		@Override
		public String toString() {
			switch (this) {
			  case APPROVED_VALID: return "Training session valid!"; 
			  case DENIED: return "Training session invalid.";
			  case DENIED_INVALID_IN_CHRONOLOGY: return "Training session invalid: Chronology";
			}
			return null;
		}	
	}
	
	private Status validityStatus;
	private FitnessTrainingSessionBean validTrainingSessionBean;
	public Status getValidityStatus() {
		return validityStatus;
	}
	public void setValidityStatus(Status validityStatus) {
		this.validityStatus = validityStatus;
	}
	public FitnessTrainingSessionBean getValidTrainingSessionBean() {
		return validTrainingSessionBean;
	}
	public void setValidTrainingSessionBean(FitnessTrainingSessionBean validTrainingSessionBean) {
		this.validTrainingSessionBean = validTrainingSessionBean;
	}
}
