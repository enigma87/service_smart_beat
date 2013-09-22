package com.genie.smartbeat.core;

/**
 * @author dhasarathy
 **/

public enum HeartrateTestValidityStatus {
	VALID(0),
	INVALID(1),
	INVALID_TIMESTAMP(2),		
	INVALID_IN_CHRONOLOGY(3),
	INVALID_HEARTRATE(4),;
	private int statusval;
	
	private HeartrateTestValidityStatus(int val) {
		this.statusval = val;
	}
	
	public int getValue() {
		return statusval;
	}
	
	@Override
	public String toString() {
		switch (this) {
		  case VALID: return "OK"; 
		  case INVALID: return "heartrate test invalid";
		  case INVALID_TIMESTAMP: return "invalid timestamp";			  
		  case INVALID_IN_CHRONOLOGY: return "heartrate test invalid in chronology";
		  case INVALID_HEARTRATE: return "invalid heartrate";
		}
		return null;
	}
}

