package com.genie.social.core;

import com.genie.social.beans.User;

/**
 * @author dhasarathy
 **/

public class AuthenticationStatus {

	private Integer authenticationStatusCode;
	private User authenticatedUser;
	
	public int getAuthenticationStatusCode() {
		return authenticationStatusCode;
	}
	
	public void setAuthenticationStatusCode(int authenticationStatusCode) {
		this.authenticationStatusCode = authenticationStatusCode;
	}
	
	public User getAuthenticatedUser() {
		return authenticatedUser;
	}
	
	public void setAuthenticatedUser(User authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}
}