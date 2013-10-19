package com.genie.smartbeat.core.errors;

public enum TrainingSessionErrors {
	
		INVALID_TRAINING_SESSION(0),
		INVALID_TIMESTAMP(1),		
		INVALID_IN_CHRONOLOGY(2),
		INVALID_SPEED_DISTRIBUTION(3),
		INVALID_TIME_DISTRIBUTION(4),
		ABSENCE_OF_TRAINING_SESSION(5);
		
		private int errorCode;
		
		private  TrainingSessionErrors(int errorCode) {
			this.errorCode = errorCode;
		}
		
		public int getErrorCode() {
			return this.errorCode;
		}
		
		@Override
		public String toString() {
			switch (this) {			  
			  case INVALID_TRAINING_SESSION: return "training_session_error_" + getErrorCode() + ":invalid training session";
			  case INVALID_TIMESTAMP: return "training_session_error_" + getErrorCode() + "invalid timestamp";			  
			  case INVALID_IN_CHRONOLOGY: return "training_session_error_" + getErrorCode() + "session invalid in chronology";
			  case INVALID_SPEED_DISTRIBUTION: return "training_session_error_" + getErrorCode() + "invalid speed distribution";
			  case INVALID_TIME_DISTRIBUTION: return "training_session_error_" + getErrorCode() + "invalid time distribution";
			  case ABSENCE_OF_TRAINING_SESSION: return "training_session_error_" + getErrorCode() + "absence of training session";
			}
			return null;
		}	
}
