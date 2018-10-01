package com.wander.note.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wander.note.config.Constant;
import com.wander.note.requestbean.NoteBean;
import com.wander.note.responsebean.UserNote;
import com.wander.note.service.NoteService;

@Controller
public class NoteController {

	@Autowired
	private NoteService noteService;

	@RequestMapping(value="note", method=RequestMethod.GET)
	public ModelAndView getNote(HttpServletRequest httpServletRequest){
		UserNote userNote = noteService.fetchNote();
		ModelAndView modelAndView=new ModelAndView("note", Constant.RESPONSE_MODEL, userNote);
		return modelAndView;
	}

	@RequestMapping(value="create/section/", method=RequestMethod.POST)
	@ResponseBody
	public String createSection(@RequestParam("sectionName") String sectionName, @RequestParam("createdDate") Date createdDate, 
			HttpServletRequest httpServletRequest){
		return noteService.createSection(sectionName, createdDate);
	}

	@RequestMapping(value="section/{sectionName}", method=RequestMethod.DELETE)
	@ResponseBody
	public String deleteSection(@PathVariable("sectionName")String sectionName, HttpServletRequest httpServletRequest){
		return noteService.deleteSection(sectionName);
	}

	@RequestMapping(value="section/note", method=RequestMethod.GET)
	@ResponseBody
	public UserNote getSectionNote(@RequestParam("sectionName") String sectionName, HttpServletRequest httpServletRequest){
		return noteService.fetchSectionNote(sectionName);
	}
	
	@RequestMapping(value="note", method=RequestMethod.POST)
	@ResponseBody
	public String createNote(@RequestBody NoteBean noteBean, HttpServletRequest httpServletRequest){
		return noteService.createNote(noteBean);
	}
	
	@RequestMapping(value="note", method=RequestMethod.PUT)
	@ResponseBody
	public String updateNote(@RequestBody NoteBean noteBean, HttpServletRequest httpServletRequest){
		return noteService.updateNote(noteBean);
	}
	
	@RequestMapping(value="note/{sectionName}/{noteTitle}", method=RequestMethod.DELETE)
	@ResponseBody
	public String deleteNote(@PathVariable("sectionName") String sectionName, @PathVariable("noteTitle") String noteTitle, 
			HttpServletRequest httpServletRequest){
		return noteService.deleteNote(sectionName, noteTitle);
	}
	

}
