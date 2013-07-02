package com.genie.account.mgmt.core;

import com.genie.account.mgmt.beans.User;

/**
 * @author dhasarathy
 **/

public class AuthenticationStatus {

	private AuthenticationStatusCode authenticationStatus;
	private User authenticatedUser;
	
	public AuthenticationStatusCode getAuthenticationStatus() {
		return authenticationStatus;
	}
	
	public void setAuthenticationStatus(
			AuthenticationStatusCode authenticationStatus) {
		this.authenticationStatus = authenticationStatus;
	}
	
	public User getAuthenticatedUser() {
		return authenticatedUser;
	}
	
	public void setAuthenticatedUser(User authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}
}