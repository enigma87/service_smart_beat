package com.genie.social.facebook;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.genie.social.beans.UserBean;
import com.genie.social.core.AuthenticationStatus;
import com.genie.social.core.UserManager;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.WebRequestor;
import com.restfb.exception.FacebookOAuthException;

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
	private static final String KEY_INSTALLED		= "installed";
	private static final String KEY_PERMISSIONS		= "permissions";
	
	/*vals*/
	private static final String VAL_POST				= "post";	
	private static final String VAL_TRUE 				= "true";	
	private static final String VAL_EMAIL 				= "email";	
	private static final String VAL_DELETE				= "delete";
	private static final String VAL_GENDER_FEMALE		= "female";
	private static final String VAL_GENDER_MALE			= "male";

	public static void determineUserAuthenticationStatus(AuthenticationStatus authenticationStatus, String accessToken) {
		FacebookClient facebookClient = null;
		UserBean user = null;		
		try{
			facebookClient = new DefaultFacebookClient(accessToken);
			com.restfb.types.User facebookUser = facebookClient.fetchObject("me", com.restfb.types.User.class);
			if(null == facebookUser.getEmail()){
				authenticationStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED_EMAIL_REQUIRED);
				authenticationStatus.setAuthenticatedUser(null);
			}else{
				user = new UserBean();
				String names[] = facebookUser.getName().split(" ");
				user.setFirstName(names[0]);
				user.setLastName(names[1]);
				user.setEmail(facebookUser.getEmail());				
				if (facebookUser.getBirthdayAsDate() != null) user.setDob(new Date(facebookUser.getBirthdayAsDate().getTime()));
				if(facebookUser.getGender()!=null){
					if(VAL_GENDER_FEMALE.equals(facebookUser.getGender())){
						user.setGender(UserManager.GENDER_FEMALE);
					}else if(VAL_GENDER_MALE.equals(facebookUser.getGender())){
						user.setGender(UserManager.GENDER_MALE);
					}
				}
				user.setAccessToken(accessToken);
				user.setAccessTokenType(UserBean.ACCESS_TOKEN_TYPE_FACEBOOK);
				authenticationStatus.setAuthenticationStatus(AuthenticationStatus.Status.APPROVED);
				authenticationStatus.setAuthenticatedUser(user);
			}
		}catch(FacebookOAuthException e){
			authenticationStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
			authenticationStatus.setAuthenticatedUser(null);
		}		
	}
	
	/*section - oauth*/
	private static final String APP_ID_GENIE 			= "201913066636280";
	private static final String CLIENT_SECRET_GENIE 	= "1493598debf77f145ab4c3f00451a531";

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
	
	public static UserBean getTestUser() {
		String getFacebookTestUserUrl = null;

		try{
			getFacebookTestUserUrl = URL_APP_GENIE_TEST_USERS + "?"
					+ KEY_INSTALLED + "=" + VAL_TRUE + "&"
					+ KEY_PERMISSIONS + "=" + VAL_EMAIL +"&"
					+ KEY_METHOD + "=" + VAL_POST + "&"
					+ KEY_ACCESS_TOKEN + "=" + URLEncoder.encode(getAppGenieAccessToken(), ENCODING_ISO_8859_1);		
			
		}catch(UnsupportedEncodingException e){
			return null;
		}
	
		WebRequestor facebookAppWebRequestor = new DefaultWebRequestor();
		String stringResponseJSON;
		JSONObject facebookTestUser;
		
		try {
			stringResponseJSON = facebookAppWebRequestor.executePost(getFacebookTestUserUrl, "").getBody();
		} catch (IOException e1) {
			return null;
		}
		
		try {
			facebookTestUser = new JSONObject(stringResponseJSON);
		} catch (JSONException e1) {
			return null;
		}
		String userID = null;
		String userAccessToken = null;
		String userEmail = null;
		String firstName = null;
		String lastName = null;
		
		try{
			userID = facebookTestUser.getString("id");
			userAccessToken = facebookTestUser.getString("access_token");
			userEmail = facebookTestUser.getString("email");			
		}catch(JSONException e){
			return null;
		}		
		UserBean user = new UserBean();
		user.setUserid(userID);
		user.setAccessToken(userAccessToken);
		user.setAccessTokenType("facebook");
		user.setEmail(userEmail);
		user.setFirstName(firstName);		
		user.setLastName(lastName);
		
		return user;
	}
	
	public static String getTestUserAccessToken(String FacebookId) {
		
		String getFacebookTestUsersUrl = null;
		String accessToken = null;
		 
		try{
			getFacebookTestUsersUrl = URL_APP_GENIE_TEST_USERS + "?"
					+ KEY_ACCESS_TOKEN + "=" + URLEncoder.encode(getAppGenieAccessToken(), ENCODING_ISO_8859_1);		
			
		}catch(UnsupportedEncodingException e){
			return null;
		}
		
		WebRequestor facebookAppWebRequestor = new DefaultWebRequestor();
		String stringResponse;
		JSONArray facebookTestUsers;
		JSONObject facebookTestUser;
		
		try {
			stringResponse = facebookAppWebRequestor.executeGet(getFacebookTestUsersUrl).getBody();
		
		} catch (IOException e1) {
			return null;
		}
		
		try {
			JSONObject stringResponseJson  = new JSONObject(stringResponse);
			facebookTestUsers = stringResponseJson.getJSONArray("data");
			
			
		} catch (JSONException e1) {
			return null;
		}
		
		for (int i=0; i<facebookTestUsers.length(); i++){
			try {
				facebookTestUser = facebookTestUsers.getJSONObject(i);
				if(FacebookId.equals(facebookTestUser.getString("id"))){
					  accessToken = facebookTestUser.getString("access_token");
					  break;
					}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return accessToken;
		
	}
	

	public static void deleteTestUser(UserBean testUser) throws IOException {
		
		String deleteTestUserURL;
		
		deleteTestUserURL = URL_ROOT + testUser.getUserid() + "?"
					+ KEY_METHOD + "=" + VAL_DELETE + "&"
					+ KEY_ACCESS_TOKEN + "=" + URLEncoder.encode(testUser.getAccessToken(), ENCODING_ISO_8859_1);		
		new DefaultWebRequestor().executePost(deleteTestUserURL, "");	
		return;
	}

}