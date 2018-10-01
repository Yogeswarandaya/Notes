package com.wander.note.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.wander.note.config.Constant;
import com.wander.note.requestbean.UserBean;

@Component
public class UserBeanValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> classObj) {
		return UserBean.class.equals(classObj);
	}

	@Override
	public void validate(Object obj, Errors error) {
		System.err.println("validation doing: "+obj);
		UserBean userBean = (UserBean) obj;
		System.err.println("validation doing: "+userBean.getUserName());
		String userName = userBean.getUserName();
		if(userName.length() > 25){
			error.rejectValue(UserBean.USER_NAME, Constant.BAD_REQUEST);
		}
		String password = userBean.getPassword();
		if(password.length() < 8 || password.length() > 25){
			error.rejectValue(UserBean.PASSWORD, Constant.BAD_REQUEST);
		}
		if(!isPasswordPatternValid(password)){
			error.rejectValue(UserBean.PASSWORD, Constant.BAD_REQUEST);
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(error, UserBean.USER_NAME, Constant.BAD_REQUEST);
		ValidationUtils.rejectIfEmptyOrWhitespace(error, UserBean.PASSWORD, Constant.BAD_REQUEST);
		System.err.println("data validated");
	}
	
	private Boolean isPasswordPatternValid(String password){
		Boolean result=true;
		String patt1 = "[a-z]";
		String patt2 = "[A-Z]";
		String patt3 = "[0-9]";
		String patt4 = "[^a-zA-Z0-9]";
		Pattern r = Pattern.compile(patt1);
		Matcher m = r.matcher(password);
		Pattern r1 = Pattern.compile(patt2);
		Matcher m1 = r1.matcher(password);
		Pattern r2 = Pattern.compile(patt3);
		Matcher m2 = r2.matcher(password);
		if (!(m.find() && m1.find() && m2.find())) {
			result=false;
		}
		Pattern r3 = Pattern.compile(patt4);
		Matcher m3 = r3.matcher(password);
		if (!m3.find()) {
			result=false;
		}
		return result;
	}
	
	public static void main(String[] args) {
		String patt1 = "[a-z]";
		String patt2 = "[A-Z]";
		String patt3 = "[0-9]";
		String patt4 = "[^a-zA-Z0-9]";
		String password="ab12c!@ASAS!";
		Pattern r = Pattern.compile(patt1);
		Matcher m = r.matcher(password);
		Pattern r1 = Pattern.compile(patt2);
		Matcher m1 = r1.matcher(password);
		Pattern r2 = Pattern.compile(patt3);
		Matcher m2 = r2.matcher(password);
		if (!(m.find() && m1.find() && m2.find())) {
			System.out.println("Should contain at least one alphabet, one number, one special character. 111");
		}
		Pattern r3 = Pattern.compile(patt4);
		Matcher m3 = r3.matcher(password);
		if (!m3.find()) {
			System.out.println("Should contain at least one alphabet, one number, one special character. 222");
		}
//		if(!(patt1.test(password) && patt2.test(password) && patt3.test(password))){
//			System.out.println("Should contain at least one alphabet, one number, one special character.");
//		}
//		if(!patt4.test(password)){
//			System.out.println("Should contain at least one alphabet, one number, one special character.");
//		}
	}

}
