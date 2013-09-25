package com.genie.smartbeat.core.errors;

/**
 * @author dhasarathy
 **/

public enum AccessTokenError {
	ACCESS_TOKEN_INVALID(1);
	
	private int errorCode;
	private AccessTokenError(int errorCode){
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	@Override
	public String toString() {
		switch (this) {
		  case ACCESS_TOKEN_INVALID			: return "access_token_" + getErrorCode() + ":access token invalid"; 		  
		}
		return null;
	}

}

