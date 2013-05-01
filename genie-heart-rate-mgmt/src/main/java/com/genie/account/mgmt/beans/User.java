/**
 * 
 */
package com.genie.account.mgmt.beans;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author manojkumar
 *
 */
public class User 
{
	private Long userid;
	private String firstName;
	private String middleName;
	private String lastName;
	private Date dob;
	private String email;
	private Boolean facebookLogin = false;
	private Boolean googleLogin = false;
	private Boolean twitterLogin = false;
	private String imageUrl;		
	private Timestamp createdTs;
	private Timestamp lastUpdatedTs;
	private Timestamp lastLoginTs;
	private Boolean active = true;
	
	public Long getUserid()
	{
		return this.userid;
	}
	
	public void setUserid(Long userid)
	{
		this.userid = userid;
	}
	
	public String getFirstName()
	{
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
		
	public Boolean getFacebookLogin()
	{
		return this.facebookLogin;
	}
	
	public void setFacebookLogin(Boolean facebookLogin)
	{
		this.facebookLogin = facebookLogin;
	}
	
	public Boolean getGoogleLogin()
	{
		return this.googleLogin;
	}
	
	public void setGoogleLogin(Boolean googleLogin)
	{
		this.googleLogin = googleLogin;
	}
	
	public Boolean getTwitterLogin()
	{
		return this.twitterLogin;
	}
	
	public void setTwitterLogin(Boolean twitterLogin)
	{
		this.twitterLogin = twitterLogin;
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
}
