package com.wander.note.responsebean;

import java.util.ArrayList;
import java.util.List;

public class UserNote {

	private String userName;
	private List<SectionData> sectionList;

	private UserNote(String userName) {
		this.userName = userName;
		this.sectionList = new ArrayList<>();
	}

	public String getUserName() {
		return userName;
	}

	public List<SectionData> getSectionList() {
		return sectionList;
	}

	public static UserNote getInstance(String userName) {
		return new UserNote(userName);
	}

}
