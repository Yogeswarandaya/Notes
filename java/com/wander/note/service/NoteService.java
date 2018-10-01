/**
 * 
 */
package com.wander.note.service;

import java.util.Date;

import com.wander.note.requestbean.NoteBean;
import com.wander.note.responsebean.UserNote;

/**
 * @author home
 *
 */
public interface NoteService {

	String USER_ID = "userId";
	String USER_NAME = "userName";
	String SECTION_ID = "sectionId";
	String SECTION_TITLE = "sectionTitle";
	String SECTION_MODIFIED_DATE = "sectionModifiedDate";
	String NOTE_ID = "noteId";
	String NOTE_TITLE = "noteTitle";
	String NOTE_DESCRIPTION = "description";
	String NOTE_CREATED_DATE = "noteCreatedDate";
	String NOTE_MODIFIED_DATE = "noteModifiedDate";

	UserNote fetchNote();
	String createSection(String sectionName, Date createdDate);
	String deleteSection(String sectionName);
	UserNote fetchSectionNote(String sectionName);
	String createNote(NoteBean noteBean);
	String updateNote(NoteBean noteBean);
	String deleteNote(String sectionName, String noteTitle);
}
