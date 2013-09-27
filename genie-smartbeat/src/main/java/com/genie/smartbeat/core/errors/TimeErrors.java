package com.genie.smartbeat.core.errors;

/**
 * @author dhasarathy
 **/

public enum TimeErrors {

	INVALID_TIMESTAMP(0),
	INVALID_DURATION(1),
	INVALID_IN_CHRONOLOGY(2),;
	
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
		  case INVALID_TIMESTAMP		: return "time_error_" + getErrorCode() + ":invalid timestamp"; 
		  case INVALID_DURATION			: return "time_error_" + getErrorCode() + ":invalid duration";
		  case INVALID_IN_CHRONOLOGY	: return "time_error_" + getErrorCode() + ":invalid in chronology";		  
		}
		return null;
	}
}

