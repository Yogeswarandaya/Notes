package com.wander.note.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wander.note.config.Constant;
import com.wander.note.model.User;
import com.wander.note.repository.UserBeanPersistence;
import com.wander.note.requestbean.UserBean;
import com.wander.note.util.Utils;
import com.wander.note.web.SessionWrapper;

@Service
@Transactional(readOnly=true)
public class UserAuthenticationService implements LoginService, UserRegistration{
	
	@Autowired
	private UserBeanPersistence userBeanPersistence;
	
	@Autowired
	private SessionWrapper sessionWrapper;
	
	@Value("${username.not.unique}")
	private String nonUniqueMessage;
	
	@Value("${username.password.not.correct}")
	private String credenTialsWrongMessage;

	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String registerUser(UserBean userBean) {
		String userName = userBeanPersistence.getUserName(userBean.getUserName().trim());
		if(Utils.checkNull(userName)){
			User user=new User();
			user.setUserName(userBean.getUserName());
			user.setPassword(userBean.getPassword());
			user.setStatus(true);
			user.setCreatedDate(new Date());
			user.setModifiedDate(new Date());
			userBeanPersistence.saveUserDetail(user);
			return Constant.NAVIGATE;
		}
		return nonUniqueMessage;
	}

	@Override
	public String authenticateLogin(UserBean userBean) {
		User userDetail = userBeanPersistence.getUserDetail(userBean.getUserName(), userBean.getPassword());
		if(!Utils.checkNull(userDetail)){
			sessionWrapper.initiateUserSession();
			sessionWrapper.putValue(userDetail);
			return Constant.NAVIGATE;
		}
		return credenTialsWrongMessage;
	}
	
	@Override
	public void logout() {
		sessionWrapper.invalidateSession();
	}

}
