package com.genie.util.facebook;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.json.facebook.GraphAPIErrorJSON;
import com.genie.account.mgmt.json.facebook.GraphAPIResponseJSON;
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

	public static GraphAPIResponseJSON authenticateUser(String accessToken) {
		String url = URL_USER + "?" 
					+ KEY_FIELDS + "=" + VAL_ID + "," + VAL_NAME + "," + VAL_EMAIL + "&" 
					+ KEY_ACCESS_TOKEN + "=" 
					+ accessToken;						
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client client = Client.create(clientConfig);
		WebResource webresource = client.resource(url);
		GraphAPIResponseJSON responseJSON = null;
		try{
			responseJSON =  webresource.get(GraphAPIResponseJSON.class);
			if(null != responseJSON.getName()){
				String[] names = responseJSON.getName().split(" ");
				responseJSON.setFirstName(names[0]);
				responseJSON.setLastName(names[1]);
			}
		}
		catch(UniformInterfaceException e){
			GraphAPIErrorJSON errorJSON = new GraphAPIErrorJSON();
			errorJSON.setType(GraphAPIErrorJSON.TYPE_OAUTH_EXCEPTION);
			responseJSON = new GraphAPIResponseJSON();
			responseJSON.setError(errorJSON);
		}
		return responseJSON;
	}
	
	/*section - oauth*/
	private static final String APP_ID_GENIE 			= "333643156765163";
	private static final String CLIENT_SECRET_GENIE 	= "bd8fa4961cb1c2a284cbe8486707b73a";
	private static final String URL_OAUTH 				= URL_ROOT + "oauth/";
	private static final String URL_OAUTH_ACC_TOKEN 	= URL_OAUTH + "access_token/";
	
	
	
	public static String getAppGenieAccessToken(){
		String getAppAccessTokenUrl = URL_OAUTH_ACC_TOKEN + "?"
									+ KEY_CLIENT_ID + "=" + APP_ID_GENIE + "&"
									+ KEY_CLIENT_SECRET + "=" + CLIENT_SECRET_GENIE + "&"
									+ KEY_GRANT_TYPE + "=" + VAL_CLIENT_CREDENTIALS;
		ClientConfig clientConfigGetAppAccessToken = new DefaultClientConfig();
		clientConfigGetAppAccessToken.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client clientGetAppToken = Client.create(clientConfigGetAppAccessToken);
		WebResource getAppAccessToken = clientGetAppToken.resource(getAppAccessTokenUrl);
		String appAccessTokenResponse = getAppAccessToken.get(String.class);
		String[] appAccessToken = appAccessTokenResponse.split("=");
		return appAccessToken[1];
	}
	
	/*section - app genie*/
	private static final String URL_APP_GENIE 				= URL_ROOT + APP_ID_GENIE + "/";
	private static final String URL_APP_GENIE_ACC 			= URL_APP_GENIE + "accounts/";
	private static final String URL_APP_GENIE_TEST_USERS 	= URL_APP_GENIE_ACC + "test-users/";
	
	
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
			String name = FacebookTestUser.getString("name");
			String[] names = name.split(" ");
			firstName = names[0];
			lastName = names[1];
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

