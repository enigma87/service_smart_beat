package com.genie.social.beans;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.Assert;

import org.junit.Test;

import com.genie.social.beans.User;

/**
 * @author vidhun
 *
 */

public class UserTest {

	@Test
	public void testUserTest() {
		
		User user = new User();
		Timestamp timestamp = new Timestamp (Calendar.getInstance().getTime().getTime());
		
		String Dateformat = "MM/dd/yyyy";
		SimpleDateFormat sdf =  new SimpleDateFormat(Dateformat);
		Date Dob = null;
		try{
		     Dob = new Date(sdf.parse("01/01/1984").getTime());
		}
		catch (ParseException e){
			
		}
		
		user.setUserid("123456789");		
		user.setAccessToken("access_token_123456789");
		user.setAccessTokenType(User.ACCESS_TOKEN_TYPE_CUSTOM);
		user.setFirstName("Antony");
		user.setMiddleName("Bob");
		user.setLastName("CampBell");
		user.setDob(Dob);
		user.setEmail("abc@xyz.com");		
		user.setImageUrl("www.picasa.com/1002");
		user.setCreatedTs(timestamp);
		user.setLastUpdatedTs(timestamp);
		user.setLastLoginTs(timestamp);
		user.setActive(true);
		
		Assert.assertEquals("123456789", user.getUserid());
		Assert.assertEquals("access_token_123456789", user.getAccessToken());
		Assert.assertEquals(User.ACCESS_TOKEN_TYPE_CUSTOM, user.getAccessTokenType());
		Assert.assertEquals("Antony", user.getFirstName());
		Assert.assertEquals("Bob", user.getMiddleName());
		Assert.assertEquals("CampBell", user.getLastName());
		Assert.assertEquals(Dob, user.getDob());
		Assert.assertEquals("abc@xyz.com", user.getEmail());
		Assert.assertEquals("www.picasa.com/1002", user.getImageUrl());
		Assert.assertTrue(timestamp.equals(user.getCreatedTs()));
		Assert.assertTrue(timestamp.equals(user.getLastUpdatedTs()));
		Assert.assertTrue(timestamp.equals(user.getLastLoginTs()));
		Assert.assertEquals(new Boolean(true), user.getActive());
		Assert.assertEquals(user.toString(), user.getFirstName() + " " + user.getLastName());
		
	}

}
