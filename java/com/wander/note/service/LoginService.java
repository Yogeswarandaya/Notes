package com.wander.note.service;

import com.wander.note.requestbean.UserBean;

public interface LoginService {
	
	String authenticateLogin(UserBean userBean);
	void logout();
	
}
