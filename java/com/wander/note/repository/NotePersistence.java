package com.wander.note.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wander.note.model.Note;
import com.wander.note.model.Section;

public interface NotePersistence {
	
	List<Map<String, Object>> fetchUserRelatedSection(Long userId);
	Long saveSection(Section section);
	Integer deleteSection(String section, Long userId);
	List<Map<String, Object>> fetchUserRelatedNote(String sectionName, Long userId);
	Integer deleteNote(String sectionName, String noteTitle, Long userId);
	Long getNoteTitle(String sectionName, String noteTitle, Long userId);
	Long getNoteTitle(String sectionName, String noteTitle, Long userId, String oldTitle);
	Long saveNote(Note note);
	Integer updateNote(String sectionName, String noteTitle, String noteContent, 
			Date modifiedDate, Long userId, String oldTitle);
	Long getSectionId(String sectionName, Long userId);
	Long fetchSectionCount(String sectionName, Long userId);
}
