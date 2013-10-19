package com.genie.social.core;

import com.genie.social.beans.UserBean;

/**
 * @author dhasarathy
 **/

public class AuthenticationStatus {

	public enum Status {
		APPROVED(0),
		DENIED(1),
		DENIED_EMAIL_REQUIRED(2);
		
		private int errorCode;
		
		private  Status(int errorCode) {
			this.errorCode = errorCode;
		}
		
		public int getErrorCode() {
			return this.errorCode;
		}
		
		@Override
		public String toString() {
			switch (this) {
			  case APPROVED					: return "authentication_status_" + getErrorCode() + ": authentication granted";
			  case DENIED					: return "authentication_status_" + getErrorCode() + ": authentication denied";
			  case DENIED_EMAIL_REQUIRED	: return "authentication_status_" + getErrorCode() + ": authentication denied - email required"; 			  
			}
			return null;
		}	
	}
	
	private Status authenticationStatus;
	private UserBean authenticatedUser;
	
	public Status getAuthenticationStatus() {
		return authenticationStatus;
	}
	
	public void setAuthenticationStatus(Status authenticationStatus) {
		this.authenticationStatus = authenticationStatus;
	}
	
	public UserBean getAuthenticatedUser() {
		return authenticatedUser;
	}
	
	public void setAuthenticatedUser(UserBean authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}
}