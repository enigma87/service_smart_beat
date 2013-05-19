package com.genie.account.mgmt.util;

import com.genie.account.mgmt.beans.User;

/**
 * @author dhasarathy
 **/

public class AuthenticationStatus {

	public static final String AUTHENTICATION_STATUS_APPROVED = "authentication_approved";
	public static final String AUTHENTICATION_STATUS_DENIED = "authentication_denied";
	
	private String authenticationStatus;
	private User authenticatedUser;
	
	public String getAuthenticationStatus() {
		return authenticationStatus;
	}
	
	public void setAuthenticationStatus(String authenticationStatus) {
		this.authenticationStatus = authenticationStatus;
	}
	
	public User getAuthenticatedUser() {
		return authenticatedUser;
	}
	
	public void setAuthenticatedUser(User authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}
	
}