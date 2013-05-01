package com.genie.account.mgmt.impl;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.core.UserManager;
import com.genie.account.mgmt.dao.UserDao;

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
	
	public void createUser(User user) {
		userDao.createUser(user);		
	}

	public User getUserInformation(Long userid) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveUserInformation(User user) {
		// TODO Auto-generated method stub
		
	}

	public User getUserInformation(String email) {
		return userDao.getUserInfo(email);
	}	
}

