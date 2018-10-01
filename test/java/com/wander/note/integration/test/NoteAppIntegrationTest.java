package com.wander.note.integration.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.wander.note.config.Constant;
import com.wander.note.config.DBConfiguration;
import com.wander.note.config.NoteConfiguration;
import com.wander.note.exception.BadRequestException;
import com.wander.note.model.User;
import com.wander.note.responsebean.UserNote;
import com.wander.note.web.AppExceptionHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = { NoteConfiguration.class, DBConfiguration.class })
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class NoteAppIntegrationTest {
	
	@Resource
    private WebApplicationContext webApplicationContext;
	
	@Value("${viewresolver.prefix}")
	private String prefix;

	@Value("${viewresolver.suffix}")
	private String suffix;
	
	@Value("${note.not.unique}")
	private String noteNonUniqueMessage;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		Map<String, Object> beansWithAnnotation = webApplicationContext.getBeansWithAnnotation(Controller.class);
		List<Object> list=new ArrayList<>();
		beansWithAnnotation.values().stream().forEach(object -> {
			list.add(object);
		});
		Object[] objectArr=list.toArray();
		this.mockMvc = MockMvcBuilders.standaloneSetup(objectArr)
				.setControllerAdvice(new AppExceptionHandler())
				.setViewResolvers(internalViewResolver())
				.build();
	}
	
	private ViewResolver internalViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix(prefix);
		viewResolver.setSuffix(suffix);
		return viewResolver;
	}
	
	@Test(expected=BadRequestException.class)
	public void givenLogin_499() throws Exception {
		mockMvc.perform(post("/login").param("userName", "admin").param("password", "admin@123"))
        .andDo(print()).andExpect(status().is(529)).andReturn();
	}
	
	@Test
	public void givenLogin_200() throws Exception {
		mockMvc.perform(post("/login").param("userName", "admin").param("password", "adminADMIN@123"))
        .andDo(print()).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	public void givenLogin_302() throws Exception {
		mockMvc.perform(post("/login").param("userName", "admin").param("password", "admin"))
        .andDo(print()).andExpect(status().is3xxRedirection()).andReturn();
	}
	
	@Test(expected=BadRequestException.class)
	public void givenRegister_526() throws Exception {
		String json="{\"userName\": \"admin\", \"password\": \"admin@123\"}";
		mockMvc.perform(post("/register").content(json).contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andReturn();
	}
	
	@Test
	public void givenRegister_200() throws Exception {
		String json="{\"userName\": \"admin\", \"password\": \"adminADMIN@123\"}";
		MvcResult andReturn = mockMvc.perform(post("/register").content(json).contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk()).andReturn();
		assertEquals(andReturn.getResponse().getContentAsString(), "Username already exists.");
	}
	
	@Test
	public void givenGetNote_200() throws Exception {
		MvcResult andReturn = mockMvc.perform(get("/note").sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		UserNote userNote = (UserNote) andReturn.getModelAndView().getModel().get(Constant.RESPONSE_MODEL);
		assertEquals(userNote.getSectionList().size(), 0);
	}
	
	@Test
	public void givenCreateSection_200() throws Exception {
		MvcResult andReturn = mockMvc.perform(post("/create/section/").param("sectionName", "admin")
				.param("createdDate", "Mon Oct 01 2018 07:37:00 GMT+0530 (India Standard Time)")
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void givenCreateSection_200_NonUnique() throws Exception {
		MvcResult andReturn = mockMvc.perform(post("/create/section/").param("sectionName", "admin")
				.param("createdDate", "Mon Oct 01 2018 07:37:00 GMT+0530 (India Standard Time)")
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, Constant.BAD_REQUEST);
	}
	
	@Test
	public void givenDeleteSection_200() throws Exception {
		MvcResult andReturn = mockMvc.perform(delete("/section/admin")
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void givenDeleteSection_200_BadRequest() throws Exception {
		MvcResult andReturn = mockMvc.perform(delete("/section/admin")
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, Constant.BAD_REQUEST);
	}
	
	@Test
	public void givenCreateNote_200() throws Exception {
		String json="{\"sectionName\": \"admin\", \"title\": \"adminNote_Fees\", \"description\": \"adminNote\", "
				+ "\"createdDate\": \"2018-10-01T02:18:32.621Z\"}";
		MvcResult andReturn = mockMvc.perform(post("/note").content(json).contentType(MediaType.APPLICATION_JSON)
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void givenCreateNote_200_NonUnique() throws Exception {
		String json="{\"sectionName\": \"admin\", \"title\": \"adminNote_Fees\", \"description\": \"adminNote\", "
				+ "\"createdDate\": \"2018-10-01T02:18:32.621Z\"}";
		MvcResult andReturn = mockMvc.perform(post("/note").content(json).contentType(MediaType.APPLICATION_JSON)
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, noteNonUniqueMessage);
	}

	@Test
	public void givenUpdateNote_200() throws Exception {
		String json="{\"sectionName\": \"admin\", \"title\": \"adminNote_updated\", \"description\": \"adminNote\", "
				+ "\"modifiedDate\": \"2018-10-01T02:18:32.621Z\", \"oldTitle\": \"adminNote\"}";
		MvcResult andReturn = mockMvc.perform(put("/note").content(json).contentType(MediaType.APPLICATION_JSON)
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void givenUpdateNote_200_NonUnique() throws Exception {
		String json="{\"sectionName\": \"admin\", \"title\": \"adminNote_updated\", \"description\": \"adminNote\", "
				+ "\"modifiedDate\": \"2018-10-01T02:18:32.621Z\", \"oldTitle\": \"adminNote\"}";
		MvcResult andReturn = mockMvc.perform(post("/note").content(json).contentType(MediaType.APPLICATION_JSON)
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, noteNonUniqueMessage);
	}
	
	@Test
	public void givenDeleteNote_200() throws Exception {
		MvcResult andReturn = mockMvc.perform(delete("/note/admin/adminNote_Fees")
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void givenDeleteNote_200_BadRequest() throws Exception {
		MvcResult andReturn = mockMvc.perform(delete("/note/admin/adminNote_Fees_Error")
				.sessionAttrs(setSessionAttribute())).andDo(print()).andExpect(status().isOk()).andReturn();
		String response = andReturn.getResponse().getContentAsString();
		assertEquals(response, Constant.BAD_REQUEST);
	}
	
	private Map<String, Object> setSessionAttribute(){
		User user=new User();
		user.setUserName("admin");
		user.setUserId(10L);
		user.setPassword("adminADMIN@123");
		Map<String, Object> sessionAttribute = new HashMap<>();
		sessionAttribute.put(Constant.USER, user);
		return sessionAttribute;
	}
	
}
