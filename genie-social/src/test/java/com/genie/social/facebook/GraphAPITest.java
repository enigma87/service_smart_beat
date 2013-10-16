package com.genie.social.facebook;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.genie.social.beans.UserBean;
import com.genie.social.core.AuthenticationStatus;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

/**
 * @author dhasarathy
 **/

public class GraphAPITest {

	@Test
	public void testGetUserAuthenticationStatus() throws IOException {
		UserBean userFb = GraphAPI.getTestUser();
		userFb.setAccessTokenType("facebook");
		userFb.setFirstName("Achilles");
		/*
		 * When user is valid
		 */
		AuthenticationStatus authStatus = new AuthenticationStatus();
		authStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
		GraphAPI.determineUserAuthenticationStatus(authStatus,userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.APPROVED.equals(authStatus.getAuthenticationStatus()), true);
		Assert.assertNotNull(authStatus.getAuthenticatedUser());
		
		/*
		 * When user's token is invalid
		 */
		authStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
		String fakeAccessToken = "CAACEdEose0cBAErbkQ3pVP8p9AZCSMrR6JeuaTlSZADrgeyf9jHnWUUhKOezuC5Jh04VFUCvqGEOFZCohorOZAjFK7608GZAziXv1l3z4utpX9eSyjeP0PMtv10sbZCstKhlCDnilhllZC92d3S16eS2UtwbGHu9eoZD"; 
		GraphAPI.determineUserAuthenticationStatus(authStatus, fakeAccessToken);
		Assert.assertEquals(AuthenticationStatus.Status.DENIED.equals(authStatus.getAuthenticationStatus()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
		/*
		 * When user hasn't allowed email permission
		 */
		authStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
		FacebookClient fbClient = new DefaultFacebookClient(userFb.getAccessToken());
		fbClient.deleteObject(userFb.getUserid() + "/permissions/email");
		GraphAPI.determineUserAuthenticationStatus(authStatus, userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.DENIED_EMAIL_REQUIRED.equals(authStatus.getAuthenticationStatus()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
		/*
		 * When user doesn't have a valid access token (deleted/non-existent/logged out ) 
		 */
		authStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
		GraphAPI.deleteTestUser(userFb);
		GraphAPI.determineUserAuthenticationStatus(authStatus,userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.DENIED.equals(authStatus.getAuthenticationStatus()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
	}
	
	@Test
	public void testGetAppGenieAppToken(){
		String appAccessToken = GraphAPI.getAppGenieAccessToken();
		assertNotNull(appAccessToken);		
	}
	
	@Test
	public void testGetTestUser() throws IOException{
		UserBean userFb = GraphAPI.getTestUser();
		assertNotNull(userFb);		
		GraphAPI.deleteTestUser(userFb);
	}
	
	public void testDeleteTestUser() throws IOException {
		/*
		 * User has fb access
		 */
		AuthenticationStatus authStatus = new AuthenticationStatus();
		authStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
		UserBean userFb = GraphAPI.getTestUser();
		GraphAPI.determineUserAuthenticationStatus(authStatus,userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.APPROVED.equals(authStatus.getAuthenticationStatus()), true);
		Assert.assertNotNull(authStatus.getAuthenticatedUser());

		/*
		 * User doesn't have fb access
		 */
		authStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
		GraphAPI.deleteTestUser(userFb);
		GraphAPI.determineUserAuthenticationStatus(authStatus,userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.DENIED.equals(authStatus.getAuthenticationStatus()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());

	}
}

