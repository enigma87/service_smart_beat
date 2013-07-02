package com.genie.account.mgmt.core;

/**
 * @author dhasarathy
 **/

public enum AuthenticationStatusCode {

	APPROVED(0),
	DENIED(1),
	DENIED_EMAIL_REQUIRED(2);
	
	private int value;
	
	private AuthenticationStatusCode(int value) {
		this.value = value;
	}

	public int getValue () {
		return value;
	}

	public String getDescription () {
		switch (this) {
		default:
			return "Access denied";
		case DENIED_EMAIL_REQUIRED:
			return "Access denied - permissions to use email required";
		case APPROVED:
			return "Access granted";
		}
	}
}

