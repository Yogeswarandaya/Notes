package com.wander.note.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wander.note.config.Constant;
import com.wander.note.exception.SessionException;

@ControllerAdvice
public class AppExceptionHandler {
	
	@ExceptionHandler(SessionException.class)
	public String handleSessionException(Exception exception, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException{
		System.err.println("SessionException handled");
		formHttpResponse(httpServletResponse, exception, 589);
		if(httpServletRequest.getHeader(Constant.AJAXHEADER)!=null){
			return null;
		}
		setRequestAttribute(httpServletRequest, exception.getMessage());
		return "forward:session/expire";
	}
	
	@ExceptionHandler(Exception.class)
	public String handleRunTimeException(Exception exception, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException{
		System.err.println("exception handled");
		formHttpResponse(httpServletResponse, exception, 529);
		if(httpServletRequest.getHeader(Constant.AJAXHEADER)!=null){
			return null;
		}
		String redirectPage=(String) httpServletRequest.getAttribute(Constant.REDIRECTPAGE);
		httpServletRequest.setAttribute(Constant.EXCEPTIONATTRIBUTE, Constant.EXCEPTIONMESSAGE);
		return redirectPage;
	}
	
	private HttpServletRequest setRequestAttribute(HttpServletRequest httpServletRequest, String message){
		httpServletRequest.setAttribute(Constant.EXCEPTIONATTRIBUTE, message);
		return httpServletRequest;
	}
	
	private HttpServletResponse formHttpResponse(HttpServletResponse httpServletResponse, Exception exception, Integer statusCode) throws IOException{
		httpServletResponse.reset();
		httpServletResponse.setStatus(statusCode);
		exception.printStackTrace();
		httpServletResponse.setContentType("text/plain; charset=UTF-8");
		if(exception.getMessage()!=null)
			httpServletResponse.getWriter().println(exception.getMessage());
		return httpServletResponse; 
	}
}
