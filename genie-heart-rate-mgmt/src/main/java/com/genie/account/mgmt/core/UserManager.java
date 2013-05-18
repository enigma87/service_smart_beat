package com.genie.account.mgmt.core;


import com.genie.account.mgmt.beans.User;
import com.genie.account.mgmt.util.RegisterRequestJSON;
import com.genie.account.mgmt.util.FacebookGraphAPIResponseJSON;

/**
 * @author dhasarathy
 **/

public interface UserManager {

	public void registerUser(User user);
	public User getUserInformation(String userid);
	public User getUserInformationByEmail(String email);
	public void saveUserInformation(User user);
	public FacebookGraphAPIResponseJSON authenticateUser(RegisterRequestJSON requestJson);
}

