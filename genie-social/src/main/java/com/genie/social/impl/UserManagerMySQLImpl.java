package com.genie.social.impl;

import com.genie.social.beans.User;
import com.genie.social.core.AuthenticationStatus;
import com.genie.social.core.UserManager;
import com.genie.social.dao.UserDao;
import com.genie.social.facebook.GraphAPI;
import com.genie.social.util.AuthorizationStatus;


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
				

	public AuthenticationStatus authenticateRequest(String accessToken, String accessTokenType) {
		AuthenticationStatus authStatus = new AuthenticationStatus();
		User user = userDao.getUserInfoByAccessToken(accessToken);		
		
		if(null == user){
			/*Token not cached*/			
			
			if(accessTokenType.equals(User.ACCESS_TOKEN_TYPE_FACEBOOK)){
				authStatus = GraphAPI.getUserAuthenticationStatus(accessToken);
				/* get existing user if there, by email of authenticated FB user */
				User authUser = authStatus.getAuthenticatedUser();
				user =  null == authUser ? null : userDao.getUserInfoByEmail(authUser.getEmail());
			
				if (user != null) {
					/*Uncached token matches an existing user*/
					user.setAccessToken(accessToken);
					userDao.updateUser(user);
					authStatus.setAuthenticationStatusCode(AuthenticationStatus.Status.APPROVED.getValue());						
					authStatus.setAuthenticatedUser(user);
				}
			}
		}
		else{/*Token cached*/
			authStatus.setAuthenticationStatusCode(AuthenticationStatus.Status.APPROVED.getValue());			
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