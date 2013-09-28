package com.genie.social.json;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.XmlRootElement;

import com.genie.social.beans.UserBean;

/**
 * @author vidhun
 *
 */

@XmlRootElement(name = "RegisterRequestJSON")
public class RegisterRequestJSON {
	
	private String accessToken;
	private String accessTokenType;
	private String dob;
	private String gender;
	
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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public UserBean getAsUserBean() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date userdob = null;
		
		if (null != getDob()) {
			try {
				userdob = dateFormat.parse(getDob());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		UserBean newUser = new UserBean();
		newUser.setAccessToken(getAccessToken());
		newUser.setAccessTokenType(getAccessTokenType());
		
		if (null != userdob
				&& null != getGender()) {
			
			newUser.setDob(new java.sql.Date(userdob.getTime()));
			newUser.setGender((byte) Integer.parseInt(getGender())); 
		}
		
		return newUser;
	}
}