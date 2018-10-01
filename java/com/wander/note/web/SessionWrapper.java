package com.wander.note.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wander.note.config.Constant;
import com.wander.note.model.User;
import com.wander.note.util.Utils;

@Component
public class SessionWrapper {

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	public void putValue(Object object){
		HttpSession httpSession = httpServletRequest.getSession(false);
		httpSession.setAttribute(Constant.USER, object);
	}

	public void initiateUserSession() {
		httpServletRequest.getSession();
	}
	
	public User getCurrentSessionUser(){
		HttpSession httpSession = httpServletRequest.getSession(false);
		User attribute = (User) httpSession.getAttribute(Constant.USER);
		return attribute; 
	}

	public void invalidateSession() {
		HttpSession httpSession = httpServletRequest.getSession(false);
		if(!Utils.checkNull(httpSession)){
			httpSession.setAttribute(Constant.USER, null);
			httpSession.invalidate();
		}
	}
	
}
