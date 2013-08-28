package com.genie.social.core;


import com.genie.social.beans.UserBean;
import com.genie.social.util.AuthorizationStatus;

/**
 * @author dhasarathy
 **/

public interface UserManager {

	public static final byte GENDER_FEMALE 	= 0;
	public static final byte GENDER_MALE 	= 1;
	public AuthenticationStatus authenticateRequest(String accessToken, String accessTokenType);
	public AuthorizationStatus authorizeRequest(UserBean subjectOfRequest, UserBean requestingUser);
	public void registerUser(UserBean user);
	public UserBean getUserInformation(String userid);
	public UserBean getUserInformationByEmail(String email);
	public void saveUserInformation(UserBean user);
	public void deleteUserById(String userid);
}