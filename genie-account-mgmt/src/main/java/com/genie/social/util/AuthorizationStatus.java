package com.genie.social.util;

/**
 * @author dhasarathy
 **/

public class AuthorizationStatus {

	public static final String AUTORIZATION_STATUS_APPROVED = "authorization_approved";
	public static final String AUTORIZATION_STATUS_DENIED = "authorization_denied";
	
	private String authorizationStatus;
	
	public String getAuthorizationStatus() {
		return authorizationStatus;
	}
	
	public void setAuthorizationStatus(String authorizationStatus) {
		this.authorizationStatus = authorizationStatus;
	}	
}

