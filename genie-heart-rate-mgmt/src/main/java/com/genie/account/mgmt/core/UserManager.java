package com.genie.account.mgmt.core;

import com.genie.account.mgmt.beans.User;

/**
 * @author dhasarathy
 **/

public interface UserManager {

	public void createUser(User user);
	public User getUserInformation(Long userid);
	public User getUserInformation(String email);
	public void saveUserInformation(User user);
}

