/**
 * 
 */
package com.genie.social.beans;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author vidhun
 * 
 */
public class UserBean implements Cloneable
{
	private String userid;
	private String accessToken;
	private String accessTokenType;
	private String firstName;
	private String middleName;
	private String lastName;
	private Date dob;
	private String email;
	private String imageUrl;		
	private Timestamp createdTs;
	private Timestamp lastUpdatedTs;
	private Timestamp lastLoginTs;
	private Boolean active = true;
	private byte privilegeLevel = 1;

	public static final String ACCESS_TOKEN_TYPE_CUSTOM = "custom";
	public static final String ACCESS_TOKEN_TYPE_FACEBOOK = "facebook";

	public String getUserid()
	{
		return this.userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

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

	public String getFirstName(){
		return this.firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getMiddleName()
	{
		return this.middleName;
	}

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getImageUrl()
	{
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}		

	public Boolean getActive()
	{
		return this.active;
	}

	public void setActive(Boolean active)
	{
		this.active = active;
	}

	public byte getPrivilegeLevel() {
		return this.privilegeLevel;
	}
	
	public void setPrivilegeLevel(byte privilegeLevel) {
		this.privilegeLevel = privilegeLevel;
	}
	public Timestamp getCreatedTs()
	{
		return this.createdTs;
	}

	public void setCreatedTs(Timestamp createdTs)
	{
		this.createdTs = createdTs;
	}

	public Timestamp getLastUpdatedTs()
	{
		return this.lastUpdatedTs;
	}

	public void setLastUpdatedTs(Timestamp lastUpdatedTs)
	{
		this.lastUpdatedTs = lastUpdatedTs;
	}

	public Timestamp getLastLoginTs()
	{
		return this.lastLoginTs;
	}

	public void setLastLoginTs(Timestamp lastLoginTs)
	{
		this.lastLoginTs = lastLoginTs;
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
	
	public UserBean clone() {
		
		try {
			return (UserBean) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}