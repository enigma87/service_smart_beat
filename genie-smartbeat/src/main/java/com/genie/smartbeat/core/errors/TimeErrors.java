package com.genie.smartbeat.core.errors;

/**
 * @author dhasarathy
 **/

public enum TimeErrors {

	INVALID_TIME(0),
	INVALID_TIMESTAMP(1),
	INVALID_DURATION(2),
	INVALID_IN_CHRONOLOGY(3);
	
	private int errorCode;	
	private TimeErrors(int errorCode){
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	@Override
	public String toString() {
		switch (this) {
		  case INVALID_TIME				: return "time_error_" + getErrorCode() + ":invalid time";
		  case INVALID_TIMESTAMP		: return "time_error_" + getErrorCode() + ":invalid timestamp"; 
		  case INVALID_DURATION			: return "time_error_" + getErrorCode() + ":invalid duration";
		  case INVALID_IN_CHRONOLOGY	: return "time_error_" + getErrorCode() + ":invalid in chronology";		  
		}
		return null;
	}
}

