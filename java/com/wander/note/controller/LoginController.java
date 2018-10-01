package com.wander.note.controller;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.wander.note.config.Constant;
import com.wander.note.config.annotation.RedirectPage;
import com.wander.note.exception.BadRequestException;
import com.wander.note.requestbean.UserBean;
import com.wander.note.service.LoginService;
import com.wander.note.service.UserRegistration;

@Controller
public class LoginController {

	@Autowired
	private Validator userBeanValidator;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UserRegistration userRegistration;
	
	@InitBinder
	private void initBinder(WebDataBinder binder){
		binder.setValidator(userBeanValidator);
	}
	
	@ModelAttribute("userBean")
	public UserBean userBeanModel(){
		return new UserBean();
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	@RedirectPage
	public ModelAndView login(@ModelAttribute("userBean") @Validated UserBean userBean, BindingResult bindingResult){
		ModelAndView modelAndView=new ModelAndView();
		if(bindingResult.hasErrors()){
			modelAndView.setViewName(Constant.LOGINPAGE);
			throw new BadRequestException(Constant.VALIDATION_ERROR);
		}
		String response = loginService.authenticateLogin(userBean);
		if(response.equals(Constant.NAVIGATE)){
			modelAndView.setViewName("redirect:note");
			return modelAndView;
		}
		modelAndView.setViewName(Constant.LOGINPAGE);
		modelAndView.addObject(Constant.VALIDATION_ERROR, response);
		return modelAndView;
	}
	
	@RequestMapping(value="register", method=RequestMethod.POST)
	@ResponseBody
	public String registerNewUser(@RequestBody @Validated UserBean userBean, BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			throw new BadRequestException(Constant.VALIDATION_ERROR);
		}
		return userRegistration.registerUser(userBean);
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	public ModelAndView logoutUser(){
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("redirect:logout/success");
		loginService.logout();
		return modelAndView;
	}
	
	@RequestMapping("session/expire")
	public ModelAndView sessionExpired(Model model) {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		model.addAttribute(Constant.EXCEPTIONATTRIBUTE, httpServletRequest.getAttribute(Constant.EXCEPTIONATTRIBUTE));
		return new ModelAndView(Constant.LOGINPAGE);
	}
	
	@RequestMapping(value="ajax/session/expire", params={Constant.EXCEPTIONATTRIBUTE}, method=RequestMethod.GET)
	public RedirectView ajaxSessionExpiration(String exceptionMessage, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute(Constant.EXCEPTIONATTRIBUTE, Base64.getUrlEncoder().encode(exceptionMessage.getBytes()));
		RedirectView redirectView=new RedirectView();
		redirectView.setContextRelative(true);
		redirectView.setUrl("/login/redirect");
		return redirectView; 
	}
	
	@RequestMapping(value="login/redirect")
	public ModelAndView redirectToLogin(Model model, @RequestParam(value=Constant.EXCEPTIONATTRIBUTE, required=false) byte[] exceptionMessage){
		model.addAttribute(Constant.EXCEPTIONATTRIBUTE,  new String(Base64.getUrlDecoder().decode(exceptionMessage)));
		return new ModelAndView(Constant.LOGINPAGE);
	}
	
	@RequestMapping(value="logout/success", method=RequestMethod.GET)
	@RedirectPage(pageName=Constant.LOGINPAGE)
	public ModelAndView loggedOut(Model model){
		return new ModelAndView(Constant.LOGINPAGE);
	}
	
}
