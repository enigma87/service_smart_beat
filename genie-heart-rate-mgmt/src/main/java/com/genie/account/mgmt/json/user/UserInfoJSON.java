package com.genie.account.mgmt.json.user;

import java.sql.Date;

import com.genie.account.mgmt.beans.User;

/**
 * @author dhasarathy
 **/

public class UserInfoJSON {

	private String userid;
	private String firstName;	
	private String lastName;
	private Date dob;	
	private String imageUrl;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	
	
	public Date getDob() {
		return dob;
	}
	
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public void copyFromUserBean(User user){
		this.setUserid(user.getUserid());
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setDob(user.getDob());
		this.setImageUrl(user.getImageUrl());
	}
}

