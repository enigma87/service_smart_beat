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
		  case INVALID_TIMESTAMP		: return "user_" + getErrorCode() + ":unknown user"; 
		  case INVALID_DURATION			: return "user_" + getErrorCode() + ":user already exists";
		  case INVALID_IN_CHRONOLOGY	: return "user_" + getErrorCode() + ":email required";		  
		}
		return null;
	}
}

