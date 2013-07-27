package com.genie.social.impl;

import javax.annotation.Resource.AuthenticationType;

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
		AuthenticationStatus fbAuthStatus = new AuthenticationStatus();
		fbAuthStatus = GraphAPI.getUserAuthenticationStatus(accessToken);
		
		// fb Authentication is successful
		if (accessTokenType.equals(UserBean.ACCESS_TOKEN_TYPE_FACEBOOK)
			&& fbAuthStatus.getAuthenticationStatus().equals(AuthenticationStatus.Status.APPROVED)
			&& fbAuthStatus.getAuthenticatedUser() != null) { 
		
			// User exists in system: use fbAuth 
			if (userDao.getUserInfoByAccessToken(accessToken) != null) return fbAuthStatus;
			if (userDao.getUserInfoByEmail(fbAuthStatus.getAuthenticatedUser().getEmail()) != null) return fbAuthStatus;
			
			// New fb user: keep auth user, deny authentication 
			fbAuthStatus.setAuthenticationStatus(AuthenticationStatus.Status.DENIED);
			return fbAuthStatus;
		}
		// not a valid user: use fbAuth
		return fbAuthStatus;
	}		

	public void saveUserInformation(UserBean user) {
		userDao.updateUser(user);		
	}

	public AuthorizationStatus authorizeRequest(UserBean subjectOfRequest, UserBean requestingUser) {
		
		return null;
	}
		
}