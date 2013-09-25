package com.genie.smartbeat.core.errors;

/**
 * @author dhasarathy
 **/

public enum UserErrors {
	USER_UNKNOWN(1),
	USER_ALREADY_EXISTS(1),
	USER_EMAIL_REQUIRED(2),
	USER_DOB_AND_GENDER_REQUIRED(3);
	
	private int errorCode;
	private UserErrors(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	@Override
	public String toString() {
		switch (this) {
		  case USER_UNKNOWN					: return "user_" + getErrorCode() + ":unknown user"; 
		  case USER_ALREADY_EXISTS			: return "user_" + getErrorCode() + ":user already exists";
		  case USER_EMAIL_REQUIRED			: return "user_" + getErrorCode() + ":email required";
		  case USER_DOB_AND_GENDER_REQUIRED	: return "user_" + getErrorCode() + ":dob and gender required";
		}
		return null;
	}

}

