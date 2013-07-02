package com.genie.social.json.facebook;

/**
 * @author dhasarathy
 **/

public class GraphAPIErrorJSON {

	private String message;
	private String type;
	private String code;

	public static final String TYPE_OAUTH_EXCEPTION = "OAuthException";
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
}

