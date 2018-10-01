package com.wander.note.requestbean;

public class UserBean {
	
	public static final String USER_NAME="userName";
	public static final String PASSWORD="password";
	
	private String userName;
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
