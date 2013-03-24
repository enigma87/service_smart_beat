/**
 * 
 */
package com.genie.account.mgmt.beans;

import java.util.Calendar;

/**
 * @author manojkumar
 *
 */
public class User 
{
	private long userid;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String password;
	private boolean facebookLogin = false;
	private boolean googleLogin = false;
	private boolean twitterLogin = false;
	private String imageUrl;
	private Integer age=0;
	private Integer height=0;
	private Double weight=0.0;
	private boolean active = true;
	private Calendar createdTs;
	private Calendar lastUpdatedTs;
	private Calendar lastloginTs;
	
	public long getUserid()
	{
		return this.userid;
	}
	
	public void setUserid(long userid)
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
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public boolean getFacebookLogin()
	{
		return this.facebookLogin;
	}
	
	public void setFacebookLogin(boolean facebookLogin)
	{
		this.facebookLogin = facebookLogin;
	}
	
	public boolean getGoogleLogin()
	{
		return this.googleLogin;
	}
	
	public void setGoogleLogin(boolean googleLogin)
	{
		this.googleLogin = googleLogin;
	}
	
	public boolean getTwitterLogin()
	{
		return this.twitterLogin;
	}
	
	public void setTwitterLogin(boolean twitterLogin)
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
	
	public Integer getAge()
	{
		return this.age;
	}
	
	public void setAge(Integer age)
	{
		this.age = age;
	}
	
	public Integer getHeight()
	{
		return this.height;
	}
	
	public void setHeight(Integer height)
	{
		this.height = height;
	}
	
	public Double getWeight()
	{
		return this.weight;
	}
	
	public void setWeight(Double weight)
	{
		this.weight = weight;
	}
	
	public boolean getActive()
	{
		return this.active;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public Calendar getCreatedTs()
	{
		return this.createdTs;
	}
	
	public void setCreatedTs(Calendar createdTs)
	{
		this.createdTs = createdTs;
	}
	
	public Calendar getLastUpdatedTs()
	{
		return this.lastUpdatedTs;
	}
	
	public void setLastUpdatedTs(Calendar lastUpdatedTs)
	{
		this.lastUpdatedTs = lastUpdatedTs;
	}
	
	public Calendar getLastLoginTs()
	{
		return this.lastloginTs;
	}
	
	public void setLastLoginTs(Calendar lastLoginTs)
	{
		this.lastloginTs = lastLoginTs;
	}
}
