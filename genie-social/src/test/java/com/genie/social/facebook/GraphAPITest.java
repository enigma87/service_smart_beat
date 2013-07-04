package com.genie.social.facebook;

import static org.junit.Assert.*;

import org.junit.Test;

import com.genie.social.beans.User;
import com.genie.social.core.AuthenticationStatus;

/**
 * @author dhasarathy
 **/

public class GraphAPITest {

	@Test
	public void testGetUserAuthenticationStatus() {
		String accessToken = "CAACEdEose0cBABFKK0l8hmNFYYglNUn9vtMZC7j9rTbdY6xKcxFY63KG3r1pYkuedILaUOzIMSZBiw2M03Rl9BXZA9ptEZCMThksL3a0L6yuEmzhUbZAk7e0bT8P1unbvkDQaZAuJdxOctnD4pjvSri0j2m03jMZAkZD";
		AuthenticationStatus authenticationStatus = GraphAPI.getUserAuthenticationStatus(accessToken);
		assertNotNull(authenticationStatus.getAuthenticatedUser());
		System.out.println(authenticationStatus.getAuthenticatedUser());
	}
	
	@Test
	public void testGetAppGenieAppToken(){
		String appAccessToken = GraphAPI.getAppGenieAccessToken();
		assertNotNull(appAccessToken);
		System.out.println(appAccessToken);
	}
	
	@Test
	public void testGetTestUser(){
		User user = GraphAPI.getTestUser();
		assertNotNull(user);		
	}
}

