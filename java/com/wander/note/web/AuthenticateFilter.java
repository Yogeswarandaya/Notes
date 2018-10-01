package com.wander.note.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wander.note.util.Utils;

public class AuthenticateFilter implements Filter{
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest=(HttpServletRequest)request;
		System.out.println("filter in: "+httpServletRequest.getRequestURI());
		HttpSession httpSession = httpServletRequest.getSession(false);
		filterChain.doFilter(request, response);
//		if(!Utils.checkNull(httpSession)){
//		}else{
//			HttpServletResponse httpServletResponse=(HttpServletResponse)response;
//			request.getRequestDispatcher("sessionexpired").forward(httpServletRequest, httpServletResponse);
//		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
}
