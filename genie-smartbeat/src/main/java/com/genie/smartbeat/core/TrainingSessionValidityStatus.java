package com.genie.smartbeat.core;

public enum TrainingSessionValidityStatus {
	
		VALID(0),
		INVALID(1),
		INVALID_TIMESTAMP(2),		
		INVALID_IN_CHRONOLOGY(3),
		INVALID_SPEED_DISTRIBUTION(4),
		INVALID_TIME_DISTRIBUTION(5);
		
		private int statusval;
		
		private  TrainingSessionValidityStatus(int val) {
			this.statusval = val;
		}
		
		public int getValue() {
			return this.statusval;
		}
		
		@Override
		public String toString() {
			switch (this) {
			  case VALID: return "OK"; 
			  case INVALID: return "training session invalid";
			  case INVALID_TIMESTAMP: return "invalid timestamp";			  
			  case INVALID_IN_CHRONOLOGY: return "session invalid in chronology";
			  case INVALID_SPEED_DISTRIBUTION: return "invalid speed distribution";
			  case INVALID_TIME_DISTRIBUTION: return "invalid time distribution";
			}
			return null;
		}	
}
