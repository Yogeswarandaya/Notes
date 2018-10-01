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
import com.wander.note.model.User;
import com.wander.note.repository.UserBeanPersistence;
import com.wander.note.requestbean.UserBean;
import com.wander.note.service.LoginService;
import com.wander.note.service.UserAuthenticationService;
import com.wander.note.web.SessionWrapper;

/**
 * @author home
 *
 */

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application.properties")
public class LoginServiceUnitTest {


    @TestConfiguration
    static class LoginServiceTestContextConfiguration {
  
        @Bean
        public LoginService employeeService() {
            return new UserAuthenticationService();
        }
        
    }
    
    @Autowired
    private LoginService loginService;
    
    @MockBean
    private UserBeanPersistence userBeanPersistence;
    
    @MockBean
    private SessionWrapper sessionWrapper;
    
    @Before
    public void setUp() {
        Mockito.when(userBeanPersistence.getUserDetail("sdfsdf","asdAAD123@#"))
          .thenReturn(new User());
    }
    
    @Value("${username.password.not.correct}")
	private String credenTialsWrongMessage;
    
    @Test
    public void whenValidName_thenUserDetailShouldBeFound() {
    	UserBean userBean=new UserBean();
    	userBean.setUserName("sdfsdf");
    	userBean.setPassword("asdAAD123@#");
    	String found = loginService.authenticateLogin(userBean);
    	assertNotNull(found);
    	assertEquals(found, Constant.NAVIGATE);
     }
    
    @Test
    public void whenNotValidName_thenUserDetailShouldBeFound() {
    	UserBean userBean=new UserBean();
    	userBean.setUserName("sdfsdfa");
    	userBean.setPassword("asdAAD123@#");
    	String found = loginService.authenticateLogin(userBean);
    	assertNotNull(found);
    	assertEquals(found, credenTialsWrongMessage);
    }
    
    @Test
    public void whenLogout() {
    	loginService.logout();
    }
	
}
