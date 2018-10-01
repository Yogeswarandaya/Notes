package com.wander.note.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wander.note.config.Constant;
import com.wander.note.config.annotation.RedirectPage;
import com.wander.note.exception.SessionException;

@Aspect
@Component
public class ControllerAspect{
	
	@Value("${session.expired}")
	public String sessionExpiredMessage;
	
	@Before("controllerPackage() &&  annotatedRequestMapping() && argsHttpRequest(httpServletRequest)")
	public void sessionHandler(HttpServletRequest httpServletRequest) throws SessionException{
		HttpSession httpSession=httpServletRequest.getSession(false);
		validateSession(httpSession);
	}
	
	@AfterThrowing(pointcut="controllerPackage() && @annotation(redirectpage) "
			+ "&& argsHttpRequest(httpServletRequest)", throwing="exception")
	public void handleException(RedirectPage redirectpage, HttpServletRequest httpServletRequest, Exception exception){
		httpServletRequest.setAttribute(Constant.REDIRECTPAGE, redirectpage.pageName());
	}
	
	@Pointcut(value="within(com.wander.note.controller.*)")
	public void controllerPackage(){}
	
	@Pointcut(value="@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void annotatedRequestMapping(){}
	
	@Pointcut("args(..,httpServletRequest)")
	public void argsHttpRequest(HttpServletRequest httpServletRequest){}
	
	private void validateSession(HttpSession httpSession) throws SessionException{
		if(httpSession == null){
			throw new SessionException(sessionExpiredMessage);
		}
	}
	
}
