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
		
		private int statusval;
		
		private  Status(int val) {
			this.statusval = val;
		}
		
		public int getValue() {
			return this.statusval;
		}
		
		@Override
		public String toString() {
			switch (this) {
			  case APPROVED: return "Access Granted!"; 
			  case DENIED_EMAIL_REQUIRED: return "Valid EmailID required for Access."; 
			  case DENIED: return "Access Denied.";
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