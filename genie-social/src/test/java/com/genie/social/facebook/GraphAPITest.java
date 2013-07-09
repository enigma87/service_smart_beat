package com.genie.social.facebook;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.ws.rs.DELETE;

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
		AuthenticationStatus authStatus = GraphAPI.getUserAuthenticationStatus(userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.APPROVED.equals(authStatus.getAuthenticationStatusCode()), true);
		Assert.assertNotNull(authStatus.getAuthenticatedUser());
		
		/*
		 * When user's token is invalid
		 */
		String fakeAccessToken = "CAACEdEose0cBAErbkQ3pVP8p9AZCSMrR6JeuaTlSZADrgeyf9jHnWUUhKOezuC5Jh04VFUCvqGEOFZCohorOZAjFK7608GZAziXv1l3z4utpX9eSyjeP0PMtv10sbZCstKhlCDnilhllZC92d3S16eS2UtwbGHu9eoZD"; 
		authStatus = GraphAPI.getUserAuthenticationStatus(fakeAccessToken);
		Assert.assertEquals(AuthenticationStatus.Status.DENIED.equals(authStatus.getAuthenticationStatusCode()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
		/*
		 * When user hasn't allowed email permission
		 */
		FacebookClient fbClient = new DefaultFacebookClient(userFb.getAccessToken());
		fbClient.deleteObject(userFb.getUserid() + "/permissions/email");
		authStatus = GraphAPI.getUserAuthenticationStatus(userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.DENIED_EMAIL_REQUIRED.equals(authStatus.getAuthenticationStatusCode()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
		/*
		 * When user doesn't have a valid access token (deleted/non-existent/logged out ) 
		 */
		GraphAPI.deleteTestUser(userFb);
		authStatus = GraphAPI.getUserAuthenticationStatus(userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.DENIED.equals(authStatus.getAuthenticationStatusCode()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());
		
	}
	
	@Test
	public void testGetAppGenieAppToken(){
		String appAccessToken = GraphAPI.getAppGenieAccessToken();
		assertNotNull(appAccessToken);
		System.out.println(appAccessToken);
	}
	
	@Test
	public void testGetTestUser(){
		UserBean userFb = GraphAPI.getTestUser();
		assertNotNull(userFb);		
	}
	
	public void testDeleteTestUser() throws IOException {
		/*
		 * User has fb access
		 */
		UserBean userFb = GraphAPI.getTestUser();
		AuthenticationStatus authStatus = GraphAPI.getUserAuthenticationStatus(userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.APPROVED.equals(authStatus.getAuthenticationStatusCode()), true);
		Assert.assertNotNull(authStatus.getAuthenticatedUser());

		/*
		 * User doesn't have fb access
		 */
		GraphAPI.deleteTestUser(userFb);
		authStatus = GraphAPI.getUserAuthenticationStatus(userFb.getAccessToken());
		Assert.assertEquals(AuthenticationStatus.Status.DENIED.equals(authStatus.getAuthenticationStatusCode()), true);
		Assert.assertNull(authStatus.getAuthenticatedUser());

	}
}

