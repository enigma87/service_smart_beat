package com.genie.smartbeat.core.errors;

/**
 * @author dhasarathy
 **/

public enum HeartrateTestErrors {	
	INVALID_HEARTRATE(1);
	private int statusval;
	
	private HeartrateTestErrors(int val) {
		this.statusval = val;
	}
	
	public int getValue() {
		return statusval;
	}
	
	@Override
	public String toString() {
		switch (this) {		  
		  case INVALID_HEARTRATE: return "heartrate test invalid";		  
		}
		return null;
	}
}

