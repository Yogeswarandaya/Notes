package com.wander.note.repository;

import com.wander.note.model.User;

public interface UserBeanPersistence {
	
	String getUserName(String userName);
	void saveUserDetail(User user);
	User getUserDetail(String userName, String password);
}
