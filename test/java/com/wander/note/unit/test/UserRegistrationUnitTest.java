package com.wander.note.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.wander.note.config.Constant;
import com.wander.note.repository.UserBeanPersistence;
import com.wander.note.requestbean.UserBean;
import com.wander.note.service.UserAuthenticationService;
import com.wander.note.service.UserRegistration;
import com.wander.note.web.SessionWrapper;

/**
 * @author home
 *
 */
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application.properties")
public class UserRegistrationUnitTest {
	
	@TestConfiguration
	static class UserRegistrationTestContextConfiguration {

		@Bean
		public UserRegistration employeeService() {
			return new UserAuthenticationService();
		}

	}

	@Autowired
	private UserRegistration userRegistrationService;

	@MockBean
	private UserBeanPersistence userBeanPersistence;

	@MockBean
	private SessionWrapper sessionWrapper;
	
	@Before
	public void setUp() {
		Mockito.when(userBeanPersistence.getUserName("sdfsdf"))
		.thenReturn(null);
		Mockito.when(userBeanPersistence.getUserName("nouser"))
		.thenReturn("nouser");
	}
	
	@Value("${username.not.unique}")
	private String nonUniqueMessage;
	
	@Test
	public void testWhenUsernameNotPresentUserRegistration(){
		UserBean userBean=new UserBean();
    	userBean.setUserName("sdfsdf");
    	userBean.setPassword("asdAAD123@#");
		String registerUser = userRegistrationService.registerUser(userBean);
		assertNotNull(registerUser);
    	assertEquals(registerUser, Constant.NAVIGATE);
	}
	
	@Test
	public void testWhenUsernameAlreadyPresentPresentUserRegistration(){
		UserBean userBean=new UserBean();
    	userBean.setUserName("nouser");
    	userBean.setPassword("asdAAD123@#");
		String registerUser = userRegistrationService.registerUser(userBean);
		assertNotNull(registerUser);
    	assertEquals(registerUser, nonUniqueMessage);
	}

}
