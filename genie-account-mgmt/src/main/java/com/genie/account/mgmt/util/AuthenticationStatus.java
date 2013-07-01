package com.genie.account.mgmt.util;

import com.genie.account.mgmt.beans.User;

/**
 * @author dhasarathy
 **/

public class AuthenticationStatus {

	public enum Status {
		APPROVED(0),
		DENIED(1),
		EMAIL_REQUIRED(2);
		
		private int value;
		
		private Status (int value) {
			this.value = value;
		}
	
		public int getValue () {
			return value;
		}
	
		public String getDescription () {
			switch (this) {
			default:
				return "Access Denied.";
			case EMAIL_REQUIRED:
				return "Email Permission Required.";
			case APPROVED:
				return "Access Granted.";
			}
		}
	}

	private int authenticationStatus;
	private User authenticatedUser;
	
	public int getAuthenticationStatus() {
		return authenticationStatus;
	}
	
	public void setAuthenticationStatus(int authenticationStatus) {
		this.authenticationStatus = authenticationStatus;
	}
	
	public User getAuthenticatedUser() {
		return authenticatedUser;
	}
	
	public void setAuthenticatedUser(User authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}
}