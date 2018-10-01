package com.wander.note.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wander.note.config.Constant;
import com.wander.note.model.Note;
import com.wander.note.model.Section;
import com.wander.note.model.User;
import com.wander.note.repository.NotePersistence;
import com.wander.note.requestbean.NoteBean;
import com.wander.note.responsebean.NoteData;
import com.wander.note.responsebean.SectionData;
import com.wander.note.responsebean.UserNote;
import com.wander.note.util.Utils;
import com.wander.note.web.SessionWrapper;

@Service
@Transactional(readOnly=true)
public class NoteServiceImpl implements NoteService{
	
	@Autowired
	private NotePersistence notePersistence;
	
	@Autowired
	private SessionWrapper sessionWrapper;
	
	@Value("${note.not.unique}")
	private String noteNonUniqueMessage;

	@Override
	public UserNote fetchNote() {
		User currentSessionUser = sessionWrapper.getCurrentSessionUser();
		List<Map<String, Object>> list = notePersistence.fetchUserRelatedSection(currentSessionUser.getUserId());
		UserNote userNote=UserNote.getInstance(currentSessionUser.getUserName());
		list.stream().forEach(map -> {
			SectionData sectionData = createSectionDataInstance(map);
			userNote.getSectionList().add(sectionData);
		});
		Collections.sort(userNote.getSectionList());
		return userNote;
	}
	
	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String createSection(String sectionName, Date createdDate) {
		User currentSessionUser = sessionWrapper.getCurrentSessionUser();
		Long sectionCount = notePersistence.fetchSectionCount(sectionName, currentSessionUser.getUserId());
		if(sectionCount == 0){
			Section section=new Section();
			section.setTitle(sectionName);
			section.setCreatedDate(createdDate);
			section.setModifiedDate(createdDate);
			section.setUser(sessionWrapper.getCurrentSessionUser());
			notePersistence.saveSection(section);
			return Constant.NAVIGATE;
		}
		return Constant.BAD_REQUEST;
	}
	
	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String deleteSection(String sectionName) {
		User currentSessionUser = sessionWrapper.getCurrentSessionUser();
		Integer count = notePersistence.deleteSection(sectionName, currentSessionUser.getUserId());
		if(count != 0){
			return Constant.NAVIGATE;
		}
		return Constant.BAD_REQUEST;
	}
	
	@Override
	public UserNote fetchSectionNote(String sectionName) {
		User currentSessionUser = sessionWrapper.getCurrentSessionUser();
		List<Map<String, Object>> list = notePersistence.fetchUserRelatedNote(sectionName, currentSessionUser.getUserId());
		UserNote userNote=UserNote.getInstance(currentSessionUser.getUserName());
		if(list.size() > 0){
			SectionData sectionData = createSectionDataInstance(list.get(0));
			userNote.getSectionList().add(sectionData);
			list.stream().forEach(map -> {
				NoteData noteData = createNoteData(map) ;
				sectionData.getNoteDataList().add(noteData);
			});
		}
		return userNote;
	}
	
	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String createNote(NoteBean noteBean) {
		String sectionName=noteBean.getSectionName();
		String noteTitle=noteBean.getTitle();
		Long userId=sessionWrapper.getCurrentSessionUser().getUserId();
		Long count = notePersistence.getNoteTitle(sectionName, noteTitle, userId);
		if(count == 0){
			Note note=new Note();
			note.setTitle(noteBean.getTitle());
			note.setDescription(noteBean.getDescription());
			note.setCreatedDate(noteBean.getCreatedDate());
			note.setModifiedDate(noteBean.getCreatedDate());
			Long sectionId = notePersistence.getSectionId(sectionName, userId);
			note.setSection(new Section(sectionId));
			notePersistence.saveNote(note);
			return Constant.NAVIGATE;
		}
		return noteNonUniqueMessage;
	}
	
	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String updateNote(NoteBean noteBean) {
		String sectionName=noteBean.getSectionName();
		String noteTitle=noteBean.getTitle();
		Long userId=sessionWrapper.getCurrentSessionUser().getUserId();
		Long count = notePersistence.getNoteTitle(sectionName, noteTitle, userId, noteBean.getOldTitle());
		if(count <= 1){
			notePersistence.updateNote(sectionName, noteTitle, noteBean.getDescription(), noteBean.getModifiedDate(), userId, noteBean.getOldTitle());
			return Constant.NAVIGATE;
		}
		return noteNonUniqueMessage;
	}
	
	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String deleteNote(String sectionName, String noteTitle) {
		User currentSessionUser = sessionWrapper.getCurrentSessionUser();
		Integer count = notePersistence.deleteNote(sectionName, noteTitle, currentSessionUser.getUserId());
		if(count != 0){
			return Constant.NAVIGATE;
		}
		return Constant.BAD_REQUEST;
	}

	private SectionData createSectionDataInstance(Map<String, Object> map) {
		Long sectionId=(Long) map.get(SECTION_ID);
		String title=(String) map.get(SECTION_TITLE);
		SectionData sectionData=SectionData.getInstance(sectionId, title);
		return sectionData;
	}

	private NoteData createNoteData(Map<String, Object> map) {
		NoteData noteData = createNoteDataInstance(map);
		return noteData;
	}

	private NoteData createNoteDataInstance(Map<String, Object> map) {
		if(!Utils.checkNull(map.get(NOTE_ID))){
			Long noteId=(Long) map.get(NOTE_ID);
			String title=(String) map.get(NOTE_TITLE);
			NoteData noteData=NoteData.getInstance(noteId, title);
			noteData.setDescription((String) map.get(NOTE_DESCRIPTION));
			String createdDate = parseDate((Date) map.get(NOTE_CREATED_DATE));
			String modifiedDate = parseDate((Date) map.get(NOTE_MODIFIED_DATE));
			noteData.setDateVariables(createdDate, modifiedDate);
			return noteData; 
		}
		return null;
	}
	
	private String parseDate(Date date){
		String pattern = Constant.DATE_PATTERN;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}

}
