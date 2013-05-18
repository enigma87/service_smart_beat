package com.genie.account.mgmt.impl;

import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.core.UserManager;
import com.genie.account.mgmt.dao.UserDao;
import com.genie.account.mgmt.util.FacebookGraphAPIResponseJSON;
import com.genie.account.mgmt.util.RegisterRequestJSON;
import com.sun.jersey.api.client.Client;
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

	public void saveUserInformation(User user) {
		
		userDao.updateUser(user);
		
	}

	public User getUserInformationByEmail(String email) {
		return userDao.getUserInfoByEmail(email);
	}	
	
	public FacebookGraphAPIResponseJSON authenticateUser(RegisterRequestJSON requestJson){
		
		String url = null;
		if (requestJson.getAccessTokenType().equals("facebook")){
		   url = "https://graph.facebook.com/me?fields=id,name,email&access_token="+requestJson.getAccessToken();
		}
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,Boolean.TRUE);
		Client client = Client.create(clientConfig);
		WebResource webresource = client.resource(url);
		return  webresource.get(FacebookGraphAPIResponseJSON.class);
		
	}
}

