package com.wander.note.responsebean;

import java.util.ArrayList;
import java.util.List;

public class SectionData implements Comparable<SectionData>{

	private Long sectionId;
	private String title;
	private List<NoteData> noteDataList;

	public SectionData(Long sectionId, String title) {
		this.sectionId=sectionId;
		this.title=title;
		this.noteDataList=new ArrayList<>();
	}

	public Long getSectionId() {
		return sectionId;
	}

	public String getTitle() {
		return title;
	}

	public List<NoteData> getNoteDataList() {
		return noteDataList;
	}

	public static SectionData getInstance(Long sectionId, String title){
		return new SectionData(sectionId, title);
	}

	@Override
	public int compareTo(SectionData o) {
		return this.title.compareTo(o.getTitle());
	}

}
