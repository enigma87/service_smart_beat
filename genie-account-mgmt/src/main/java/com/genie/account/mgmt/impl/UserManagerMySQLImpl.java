package com.genie.account.mgmt.impl;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.core.AuthenticationStatus;
import com.genie.account.mgmt.core.AuthenticationStatusCode;
import com.genie.account.mgmt.core.UserManager;
import com.genie.account.mgmt.dao.UserDao;
import com.genie.account.mgmt.json.facebook.GraphAPIErrorJSON;
import com.genie.account.mgmt.json.facebook.GraphAPIResponseJSON;
import com.genie.account.mgmt.util.AuthorizationStatus;
//import com.genie.account.mgmt.util.RegistrationStatus;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;


/**
 * @author dhasarathy
 **/

public class UserManagerMySQLImpl implements UserManager{
	
	private UserDao userDao;
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
	
	public void registerUser(User user) {
		userDao.createUser(user);
	}
	
	public User getUserInformation(String userid) {	
		
		return userDao.getUserInfo(userid);
	}


	public User getUserInformationByEmail(String email) {
		return userDao.getUserInfoByEmail(email);
	}	
				
	public GraphAPIResponseJSON authenticateFacebookUser(String accessToken) {
		
		String url = "https://graph.facebook.com/me?fields=id,name,email&access_token="+accessToken;		
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

	public AuthenticationStatus authenticateRequest(String accessToken, String accessTokenType) {
		
		AuthenticationStatus authStatus = new AuthenticationStatus();
		
		User user = userDao.getUserInfoByAccessToken(accessToken);		
		if(null == user){/*Token not cached*/			
			if(accessTokenType.equals(User.ACCESS_TOKEN_TYPE_FACEBOOK)){
				GraphAPIResponseJSON responseJson = authenticateFacebookUser(accessToken);
				if(null != responseJson.getError()){/*Token invalid*/
					authStatus.setAuthenticationStatus(AuthenticationStatusCode.DENIED);					
					authStatus.setAuthenticatedUser(null);
				} else if (null == responseJson.getEmail()) {/*Token invalid - no email permissions given*/
					authStatus.setAuthenticationStatus(AuthenticationStatusCode.DENIED_EMAIL_REQUIRED);					
					authStatus.setAuthenticatedUser(null);
				}
				else{/*Token valid*/
					user = userDao.getUserInfoByEmail(responseJson.getEmail());
					if(null == user){/*Uncached token doesn't match an existing user*/
						authStatus.setAuthenticationStatus(AuthenticationStatusCode.DENIED);						
						user = new User();
						user.setAccessToken(accessToken);
						user.setAccessTokenType(accessTokenType);
						user.setEmail(responseJson.getEmail());
						user.setFirstName(responseJson.getFirstName());
						user.setLastName(responseJson.getLastName());
						authStatus.setAuthenticatedUser(user);
					}
					else{/*Uncached token matches an existing user*/
						user.setAccessToken(accessToken);
						userDao.updateUser(user);
						authStatus.setAuthenticationStatus(AuthenticationStatusCode.APPROVED);						
						authStatus.setAuthenticatedUser(user);
					}
				}
			}
		}
		else{/*Token cached*/
			authStatus.setAuthenticationStatus(AuthenticationStatusCode.APPROVED);			
			authStatus.setAuthenticatedUser(user);
		}
		return authStatus;
	}

	public void saveUserInformation(User user) {
		userDao.updateUser(user);		
	}

	public AuthorizationStatus authorizeRequest(User subjectOfRequest, User requestingUser) {
		
		return null;
	}
		
}


