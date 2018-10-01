package com.wander.note.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wander.note.config.Constant;

@Entity
@Table(name = "note")
@SequenceGenerator(name = Constant.SEQUENCE_GENERATOR_NAME, sequenceName = Constant.SEQUENCE_NAME)
public class Note implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = Constant.SEQUENCE_GENERATOR_NAME)
	@Column(name = "note_id")
	private Long noteId;
	@Column(name = "title")
	private String title;
	@Column(name = "description")
	private String description;
	@ManyToOne
	@JoinColumn(name = "section_id")
	private Section section;
	@Column(name = "createddate")
	private Date createdDate;
	@Column(name = "modifieddate")
	private Date modifiedDate;

	public Note() {

	}

	public Note(Long noteId) {
		this.noteId = noteId;
	}

	public Long getNoteId() {
		return noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
