package com.genie.account.mgmt.json;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author vidhun
 *
 */

@XmlRootElement(name = "RegisterRequestJSON")
public class RegisterRequestJSON {
	
	private String accessToken;
	private String accessTokenType;
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getAccessTokenType() {
		return accessTokenType;
	}
	
	public void setAccessTokenType(String accessTokenType) {
		this.accessTokenType = accessTokenType;
	}
	
	

}
