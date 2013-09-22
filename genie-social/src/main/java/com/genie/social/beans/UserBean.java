/**
 * 
 */
package com.genie.social.beans;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

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
	private Byte gender;
	private String email;
	private String imageUrl;		
	private Timestamp createdTs;
	private Timestamp lastUpdatedTs;
	private Timestamp lastLoginTs;
	private Boolean active = true;
	private Byte privilegeLevel = 1;

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

	public Byte getGender() {
		return gender;
	}
	
	public void setGender(Byte gender) {
		this.gender = gender;
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

	public Byte getPrivilegeLevel() {
		return this.privilegeLevel;
	}
	
	public void setPrivilegeLevel(Byte privilegeLevel) {
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
	
	public int getAge(){
		int age = -1;		
	    if(null != getDob()){
	    	Calendar today = Calendar.getInstance();
		    Calendar birthDate = Calendar.getInstance();
		    birthDate.setTime(getDob());
		    if (!birthDate.after(today)) {		    	
		    	age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
		    	// If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year   
		        if ( (birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
		                (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH ))){
		            age--;
	
		         // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
		        }else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH )) &&
		                  (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH ))){
		            age--;
		        }
		    }
	    }
	    return age;
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
	 
	public boolean isValidForTableInsert() {
		if ((null != this.getUserid() && !this.getUserid().isEmpty())
				&& (null != this.getAccessToken() && !this.getAccessToken().isEmpty())
				&& (null != this.getAccessTokenType() && !this.getAccessTokenType().isEmpty())
				&& (null != this.getFirstName() && !this.getFirstName().isEmpty())
				&& (null != this.getEmail() && !this.getEmail().isEmpty())
				&& 0 != this.getPrivilegeLevel()) {
			
			return true;
		}
		
		return false;
	}
}