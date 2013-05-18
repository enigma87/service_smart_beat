package com.genie.account.mgmt.beans;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

import junit.framework.Assert;

import com.genie.account.mgmt.beans.User;
import org.junit.Test;

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
		user.setFirstName("Antony");
		user.setMiddleName("Bob");
		user.setLastName("CampBell");
		user.setDob(Dob);
		user.setEmail("abc@xyz.com");
		user.setFacebookLogin(true);
		user.setGoogleLogin(false);
		user.setTwitterLogin(true);
		user.setImageUrl("www.picasa.com/1002");
		user.setCreatedTs(timestamp);
		user.setLastUpdatedTs(timestamp);
		user.setLastLoginTs(timestamp);
		user.setActive(true);
		
		Assert.assertEquals("123456789", user.getUserid());
		Assert.assertEquals("Antony", user.getFirstName());
		Assert.assertEquals("Bob", user.getMiddleName());
		Assert.assertEquals("CampBell", user.getLastName());
		Assert.assertEquals(Dob, user.getDob());
		Assert.assertEquals("abc@xyz.com", user.getEmail());
		Assert.assertEquals(new Boolean(true), user.getFacebookLogin());
		Assert.assertEquals(new Boolean(false), user.getGoogleLogin());
		Assert.assertEquals(new Boolean(true), user.getTwitterLogin());
		Assert.assertEquals("www.picasa.com/1002", user.getImageUrl());
		Assert.assertTrue(timestamp.equals(user.getCreatedTs()));
		Assert.assertTrue(timestamp.equals(user.getLastUpdatedTs()));
		Assert.assertTrue(timestamp.equals(user.getLastLoginTs()));
		Assert.assertEquals(new Boolean(true), user.getActive());
		
		
	}

}
