package com.wander.note.responsebean;

import com.wander.note.util.Utils;

public class NoteData {

	private Long noteId;
	private String title;
	private String description;
	private String createdDate;
	private String modifiedDate;

	private NoteData(Long noteId, String title) {
		this.noteId = noteId;
		this.title = "";
		if (!Utils.checkNull(title)) {
			this.title = title;
		}
	}

	public Long getNoteId() {
		return noteId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = "";
		if (!Utils.checkNull(description)) {
			this.description = description;
		}
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setDateVariables(String createdDate, String modifiedDate) {
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public static NoteData getInstance(Long noteId, String title) {
		return new NoteData(noteId, title);
	}

}
