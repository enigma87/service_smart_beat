package com.genie.account.mgmt.core;


import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.util.AuthorizationStatus;

/**
 * @author dhasarathy
 **/

public interface UserManager {

	public AuthenticationStatus authenticateRequest(String accessToken, String accessTokenType);
	public AuthorizationStatus authorizeRequest(User subjectOfRequest, User requestingUser);
	public void registerUser(User user);
	public User getUserInformation(String userid);
	public User getUserInformationByEmail(String email);
	public void saveUserInformation(User user);
}

