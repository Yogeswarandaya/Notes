package com.wander.note.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wander.note.config.Constant;
import com.wander.note.model.User;
import com.wander.note.repository.NotePersistence;
import com.wander.note.requestbean.NoteBean;
import com.wander.note.responsebean.SectionData;
import com.wander.note.responsebean.UserNote;
import com.wander.note.service.NoteService;
import com.wander.note.service.NoteServiceImpl;
import com.wander.note.web.SessionWrapper;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application.properties")
public class NoteServiceUnitTest {

	@TestConfiguration
	static class NoteServiceTestContextConfiguration {

		@Bean
		public NoteService employeeService() {
			return new NoteServiceImpl();
		}

	}

	@Autowired
	private NoteService noteService;

	@MockBean
	private NotePersistence notePersistence;

	@MockBean
	private SessionWrapper sessionWrapper;
	
	@Value("${note.not.unique}")
	private String noteNonUniqueMessage;
	
	private List<String> sectionNameList=new ArrayList<>();
	
	@Before
	public void setUpWhileSetupFetchNotes(){
		List<Map<String, Object>> list = buildList();
		User user=new User();
		user.setUserId(new Long(7));
		user.setUserName("asdadasd");;
		Mockito.when(notePersistence.fetchUserRelatedSection(new Long(7))).thenReturn(list);
		Mockito.when(sessionWrapper.getCurrentSessionUser()).thenReturn(user);
		Mockito.when(notePersistence.fetchSectionCount("data2", new Long(7))).thenReturn(0L);
		Mockito.when(notePersistence.fetchSectionCount("data3", new Long(7))).thenReturn(1L);
		Mockito.when(notePersistence.deleteSection("data2", 13L)).thenReturn(1);
		Mockito.when(notePersistence.deleteSection("data3", 12L)).thenReturn(0);
		Mockito.when(notePersistence.getNoteTitle("data2", "newNote", 7L)).thenReturn(0L);
		Mockito.when(notePersistence.getNoteTitle("data2", "session", 7L)).thenReturn(1L);
		Mockito.when(notePersistence.getSectionId("data2", 7L)).thenReturn(4L);
		Mockito.when(notePersistence.deleteNote("data2", "newNote", 7L)).thenReturn(1);
		Mockito.when(notePersistence.deleteNote("data2", "session", 7L)).thenReturn(0);
	}
	
	@Test
	public void whenFetchedUserRelatedSection(){
		UserNote userNote = noteService.fetchNote();
		assertNotNull(userNote);
    	assertEquals(userNote.getSectionList().size(), 13);
    	assertEquals(userNote.getSectionList().stream().map(SectionData::getTitle).collect(Collectors.toList()), sectionNameList);
	}
	
	@Test
	public void whenCreateSection(){
		String response = noteService.createSection("data2", new Date());
		assertNotNull(response);
    	assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void whenProxyingCreateSection(){
		String response = noteService.createSection("data3", new Date());
		assertNotNull(response);
    	assertEquals(response, Constant.BAD_REQUEST);
	}
	
	@Test
	public void whenDeleteSection(){
		String response = noteService.createSection("data2", new Date());
		assertNotNull(response);
    	assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void whenProxyingDeleteSection(){
		String response = noteService.createSection("data3", new Date());
		assertNotNull(response);
    	assertEquals(response, Constant.BAD_REQUEST);
	}
	
	@Test
	public void whenCreatNote(){
		NoteBean noteBean=getNoteBean("newNote");
		String response = noteService.createNote(noteBean);
		assertNotNull(response);
    	assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void whenNonUniqueCreatNote(){
		NoteBean noteBean=getNoteBean("session");
		String response = noteService.createNote(noteBean);
		assertNotNull(response);
    	assertEquals(response, noteNonUniqueMessage);
	}
	
	@Test
	public void whenUpdateNote(){
		NoteBean noteBean=getNoteBean("newNote");
		String response = noteService.createNote(noteBean);
		assertNotNull(response);
    	assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void whenNonUniqueUpdateNote(){
		NoteBean noteBean=getNoteBean("session");
		String response = noteService.createNote(noteBean);
		assertNotNull(response);
    	assertEquals(response, noteNonUniqueMessage);
	}
	
	@Test
	public void whenDeleteNote(){
		String response = noteService.deleteNote("data2", "newNote");
		assertNotNull(response);
    	assertEquals(response, Constant.NAVIGATE);
	}
	
	@Test
	public void whenNonUniqueDeleteNote(){
		String response = noteService.deleteNote("data2", "session");
		assertNotNull(response);
    	assertEquals(response, Constant.BAD_REQUEST);
	}
	
	private NoteBean getNoteBean(String noteTitle){
		NoteBean noteBean=new NoteBean();
		noteBean.setSectionName("data2");
		noteBean.setTitle(noteTitle);
		noteBean.setDescription("newNote");
		noteBean.setCreatedDate(new Date());
		noteBean.setModifiedDate(new Date());
		return noteBean;
	}
	
	private List<Map<String, Object>> buildList(){
		List<Map<String, Object>> list=new ArrayList<>();
		String resultSectionList="[{sectionTitle=data2, sectionId=4}, "
				+ "{sectionTitle=data3, sectionId=5}, "
				+ "{sectionTitle=yess, sectionId=7}, "
				+ "{sectionTitle=werqqw, sectionId=8}, "
				+ "{sectionTitle=sdfsdfdsf, sectionId=9}, "
				+ "{sectionTitle=sdfsdfdsfsdfsdfds, sectionId=10}, "
				+ "{sectionTitle=wrwerw, sectionId=12}, "
				+ "{sectionTitle=werwerwe, sectionId=13}, "
				+ "{sectionTitle=qweqewqrwrwer, sectionId=14}, "
				+ "{sectionTitle=qwrrwretretre, sectionId=15}, "
				+ "{sectionTitle=ertretr, sectionId=16}, "
				+ "{sectionTitle=wrwerwetetret, sectionId=18}, "
				+ "{sectionTitle=aaqwe, sectionId=19}]";
		Gson gson=new Gson();
		JsonArray jsonArray = gson.fromJson(resultSectionList, JsonArray.class);
		Iterator<JsonElement> iterator = jsonArray.iterator();
		while(iterator.hasNext()){
			JsonElement jsonElement = iterator.next();
			Map<String, Object> map=new HashMap<>();
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			map.put("sectionTitle", jsonObject.get("sectionTitle").getAsString());
			map.put("sectionId", jsonObject.get("sectionId").getAsLong());
			sectionNameList.add((String) map.get("sectionTitle"));
			Collections.sort(sectionNameList);
			list.add(map);
		}
		return list;
	}
	
}
