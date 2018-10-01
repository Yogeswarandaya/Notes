package com.wander.note.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wander.note.model.Note;
import com.wander.note.model.Section;

@Repository
public class NotePersistenceImpl implements NotePersistence{
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> fetchUserRelatedSection(Long userId) {
		Session session = sessionFactory.getCurrentSession();
		Query query=session.createQuery("select section.sectionId as sectionId, section.title as sectionTitle, "
				+ "section.modifiedDate as sectionModifiedDate "
				+ "from User user inner join user.sections section "
				+ "where user.userId=(:userId)");
		query.setParameter("userId", userId);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	@Override
	public Long fetchSectionCount(String sectionName, Long userId) {
		Session session = sessionFactory.getCurrentSession();
		Query query=session.createQuery("select count(*) from Section section inner join section.user user "
				+ "where title=(:title) and user.userId=(:userId)");
		query.setParameter("title", sectionName);
		query.setParameter("userId", userId);
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long saveSection(Section section) {
		Session session = sessionFactory.getCurrentSession();
		Long id = (Long) session.save(section);
		return id;
	}
	
	@Override
	public Integer deleteSection(String sectionName, Long userId) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("Delete from Section where title = (:sectionName) and user.userId = (:userId)");
		query.setParameter("sectionName", sectionName);
		query.setParameter("userId", userId);
		deleteNote(sectionName, userId);
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> fetchUserRelatedNote(String sectionName, Long userId) {
		Session session = sessionFactory.getCurrentSession();
		Query query=session.createQuery("select section.sectionId as sectionId, section.title as sectionTitle, "
				+ "note.noteId as noteId, note.title as noteTitle, note.description as description, "
				+ "note.createdDate as noteCreatedDate, note.modifiedDate as noteModifiedDate "
				+ "from User user inner join user.sections section inner join section.notes note "
				+ "where user.userId=(:userId) and section.title=(:sectionName) order by noteModifiedDate desc");
		query.setParameter("sectionName", sectionName);
		query.setParameter("userId", userId);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	@Override
	public Long getNoteTitle(String sectionName, String noteTitle, Long userId){
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("select count(*) from Note note inner join note.section section "
				+ "where note.title=(:noteTitle) and section.title=(:sectionName) and section.user.userId=(:userId)");
		query.setParameter("noteTitle", noteTitle);
		query.setParameter("sectionName", sectionName);
		query.setParameter("userId", userId);
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long getNoteTitle(String sectionName, String noteTitle, Long userId, String oldTitle) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("select count(*) from Note note inner join note.section section "
				+ "where note.title=(:noteTitle) and section.title=(:sectionName) "
				+ "and section.user.userId=(:userId) and note.title != (:oldTitle)");
		query.setParameter("noteTitle", noteTitle);
		query.setParameter("sectionName", sectionName);
		query.setParameter("userId", userId);
		query.setParameter("oldTitle", oldTitle);
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long saveNote(Note note){
		Session session=sessionFactory.getCurrentSession();
		Long id = (Long) session.save(note);
		return id;
	}
	
	@Override
	public Integer updateNote(String sectionName, String noteTitle, String noteContent, Date modifiedDate, Long userId, String oldTitle){
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("update Note set title=(:noteTitle), description=(:noteContent), modifiedDate=(:modifiedDate) "
				+ "where title=(:oldTitle) and section.sectionId=(select sectionId from Section where title=(:sectionName) "
				+ "and user.userId=(:userId))");
		query.setParameter("noteTitle", noteTitle);
		query.setParameter("noteContent", noteContent);
		query.setParameter("modifiedDate", modifiedDate);
		query.setParameter("sectionName", sectionName);
		query.setParameter("userId", userId);
		query.setParameter("oldTitle", oldTitle);
		return query.executeUpdate();
	}
	
	@Override
	public Integer deleteNote(String sectionName, String noteTitle, Long userId) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("Delete from Note where title=(:noteTitle) "
				+ "and section.sectionId=(select sectionId from Section where title=(:sectionName) "
				+ "and user.userId=(:userId))");
		query.setParameter("noteTitle", noteTitle);
		query.setParameter("sectionName", sectionName);
		query.setParameter("userId", userId);
		return query.executeUpdate();
	}
	
	private void deleteNote(String sectionName, Long userId) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("Delete from Note "
				+ "where section.sectionId=(select sectionId from Section where title=(:sectionName) "
				+ "and user.userId=(:userId))");
		query.setParameter("sectionName", sectionName);
		query.setParameter("userId", userId);
		query.executeUpdate();
	}
	
	@Override
	public Long getSectionId(String sectionName, Long userId) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("select sectionId from Section where title=(:sectionName) and user.userId=(:userId)");
		query.setParameter("sectionName", sectionName);
		query.setParameter("userId", userId);
		return (Long) query.setMaxResults(1).uniqueResult();
	}

}
