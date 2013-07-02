package com.genie.social.facebook;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.genie.social.beans.User;
import com.genie.social.core.AuthenticationStatus;
import com.genie.social.core.AuthenticationStatusCode;
import com.genie.social.json.facebook.GraphAPIErrorJSON;
import com.genie.social.json.facebook.GraphAPIResponseJSON;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.TestUser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * @author dhasarathy
 **/

public class GraphAPI {
	
	private static final String ENCODING_ISO_8859_1 = "ISO-8859-1";
	/*section - common*/
	private static final String URL_ROOT 			= "https://graph.facebook.com/";	
	
	/*keys*/
	private static final String KEY_METHOD			= "method";
	private static final String KEY_ACCESS_TOKEN 	= "access_token";
	private static final String KEY_FIELDS			= "fields";
	private static final String KEY_CLIENT_ID 		= "client_id";
	private static final String KEY_CLIENT_SECRET 	= "client_secret";
	private static final String KEY_GRANT_TYPE 		= "grant_type";
	private static final String KEY_INSTALLED		= "installed";
	private static final String KEY_PERMISSIONS		= "permissions";
	
	/*vals*/
	private static final String VAL_POST				= "post";	
	private static final String VAL_TRUE 				= "true";
	private static final String VAL_ID 					= "id";
	private static final String VAL_NAME 				= "name";
	private static final String VAL_EMAIL 				= "email";
	private static final String VAL_CLIENT_CREDENTIALS 	= "client_credentials";
	
	/*section - me*/
	private static final String URL_USER 			= URL_ROOT + "me";

	public static AuthenticationStatus getAuthenticatedFacebookUser(String accessToken) {
		FacebookClient facebookClient = null;
		User user = null;
		AuthenticationStatus authenticationStatus = new AuthenticationStatus();
		try{
			facebookClient = new DefaultFacebookClient(accessToken);
			com.restfb.types.User facebookUser = facebookClient.fetchObject("me", com.restfb.types.User.class);
			if(null == facebookUser.getEmail()){
				authenticationStatus.setAuthenticationStatusCode(AuthenticationStatusCode.DENIED_EMAIL_REQUIRED);
				authenticationStatus.setAuthenticatedUser(null);
			}else{
				user = new User();
				String names[] = facebookUser.getName().split(" ");
				user.setFirstName(names[0]);
				user.setLastName(names[1]);
				user.setEmail(facebookUser.getEmail());				
				user.setDob(new Date(facebookUser.getBirthdayAsDate().getTime()));
				user.setAccessToken(accessToken);
				user.setAccessTokenType(User.ACCESS_TOKEN_TYPE_FACEBOOK);
				authenticationStatus.setAuthenticationStatusCode(AuthenticationStatusCode.APPROVED);
				authenticationStatus.setAuthenticatedUser(user);
			}
		}catch(FacebookOAuthException e){
			authenticationStatus.setAuthenticationStatusCode(AuthenticationStatusCode.DENIED);
			authenticationStatus.setAuthenticatedUser(null);
		}
		return authenticationStatus;
	}
	
	/*section - oauth*/
	private static final String APP_ID_GENIE 			= "333643156765163";
	private static final String CLIENT_SECRET_GENIE 	= "bd8fa4961cb1c2a284cbe8486707b73a";
	private static final String URL_OAUTH 				= URL_ROOT + "oauth/";
	private static final String URL_OAUTH_ACC_TOKEN 	= URL_OAUTH + "access_token/";
	
	
	
	public static String getAppGenieAccessToken(){
		FacebookClient facebookClient = new DefaultFacebookClient();
		AccessToken appAccessToken = facebookClient.obtainAppAccessToken(APP_ID_GENIE, CLIENT_SECRET_GENIE);
		return appAccessToken.getAccessToken();		
	}
	
	/*section - app genie*/
	private static final String URL_APP_GENIE 				= URL_ROOT + APP_ID_GENIE + "/";
	private static final String URL_APP_GENIE_ACC 			= URL_APP_GENIE + "accounts/";
	private static final String URL_APP_GENIE_TEST_USERS 	= URL_APP_GENIE_ACC + "test-users";
	
	
	/*private static final String PATH_APP_GENIE			= APP_ID_GENIE + "/";
	private static final String PATH_ACCOUNTS				= "accounts/";
	private static final String PATH_TEST_USERS				= "test-users/";
	
	public static User createTestUser(){
		User user = null;
		FacebookClient facebookClient = new DefaultFacebookClient();
		String getFacebookTestUserPath = "/" + PATH_APP_GENIE + PATH_ACCOUNTS + PATH_TEST_USERS;
		TestUser testUser = facebookClient.publish(getFacebookTestUserPath, TestUser.class, 
														Parameter.with(KEY_INSTALLED, VAL_TRUE),
														Parameter.with(KEY_PERMISSIONS,VAL_EMAIL)
														Parameter.with(KEY_METHOD,VAL_POST),
														Parameter.with(KEY_ACCESS_TOKEN,getAppGenieAccessToken())
														);
		if(null != testUser){
			user = new User();			
			user.setAccessToken(testUser.getAccessToken());
		}
		return user;
	}*/	
	
	public static User getTestUser(){
		String getFacebookTestUserUrl = null;
		try{
			getFacebookTestUserUrl = URL_APP_GENIE_TEST_USERS + "?"
					+ KEY_INSTALLED + "=" + VAL_TRUE + "&"
					+ KEY_PERMISSIONS + "=" + VAL_EMAIL + "&"
					+ KEY_METHOD + "=" + VAL_POST + "&"
					+ KEY_ACCESS_TOKEN + "=" + URLEncoder.encode(getAppGenieAccessToken(), ENCODING_ISO_8859_1);		
			
		}catch(UnsupportedEncodingException e){
			return null;
		}		
		ClientConfig clientConfigGetFacebookTestUser = new DefaultClientConfig();
		clientConfigGetFacebookTestUser.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientGetFacebookTestUser = Client.create(clientConfigGetFacebookTestUser);
		WebResource getFacebookTestUser = clientGetFacebookTestUser.resource(getFacebookTestUserUrl);		
		JSONObject FacebookTestUser = getFacebookTestUser.type(MediaType.APPLICATION_FORM_URLENCODED).post(JSONObject.class);
		
		String userID = null;
		String userAccessToken = null;
		String userEmail = null;
		String firstName = null;
		String lastName = null;
		
		try{
			userID = FacebookTestUser.getString("id");
			userAccessToken = FacebookTestUser.getString("access_token");
			userEmail = FacebookTestUser.getString("email");			
		}catch(JSONException e){
			return null;
		}		
		User user = new User();
		user.setUserid(userID);
		user.setAccessToken(userAccessToken);
		user.setAccessTokenType("facebook");
		user.setEmail(userEmail);
		user.setFirstName(firstName);		
		user.setLastName(lastName);
		
		return user;
	}
}

