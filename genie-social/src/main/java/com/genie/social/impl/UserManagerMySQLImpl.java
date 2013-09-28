package com.genie.social.impl;

import com.genie.social.beans.UserBean;
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
	
	public void registerUser(UserBean user) {
		userDao.createUser(user);
	}
	
	public UserBean getUserInformation(String userid) {	
		
		return userDao.getUserInfo(userid);
	}


	public UserBean getUserInformationByEmail(String email) {
		return userDao.getUserInfoByEmail(email);
	}

	public AuthenticationStatus authenticateRequest(String accessToken, String accessTokenType) {
		AuthenticationStatus authStatus = null;
		UserBean authenticatedUser = userDao.getUserInfoByAccessToken(accessToken);

		if (authenticatedUser != null) {
			authStatus = new AuthenticationStatus();
			authStatus.setAuthenticationStatus(AuthenticationStatus.Status.APPROVED);
			authStatus.setAuthenticatedUser(authenticatedUser);
		}
		else if (accessTokenType.equals(UserBean.ACCESS_TOKEN_TYPE_FACEBOOK)) {
			authStatus = GraphAPI.getUserAuthenticationStatus(accessToken);
			
			if (authStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)
				&& userDao.getUserInfoByEmail(authStatus.getAuthenticatedUser().getEmail()) == null) {
				
				authStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
			}
		}	
		return authStatus;
	}

	public void saveUserInformation(UserBean user) {
		userDao.updateUser(user);		
	}

	public AuthorizationStatus authorizeRequest(UserBean subjectOfRequest, UserBean requestingUser) {
		
		return null;
	}

	public void deleteUserById(String userid) {
		userDao.deleteUser(userid);		
	}
		
}