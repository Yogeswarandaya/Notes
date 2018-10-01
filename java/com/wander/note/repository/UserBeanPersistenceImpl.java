package com.wander.note.repository;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wander.note.model.User;

/**
 * @author home
 *
 */
@Repository
public class UserBeanPersistenceImpl implements UserBeanPersistence{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public String getUserName(String userName) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("select userName from User where userName=(:userName)");
		query.setParameter("userName", userName);
		return (String) query.setMaxResults(1).uniqueResult();
	}
	
	@Override
	public void saveUserDetail(User user) {
		Session session=sessionFactory.getCurrentSession();
		session.save(user);
	}
	
	@Override
	public User getUserDetail(String userName, String password) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from User where userName=(:userName) and password=(:password)");
		query.setParameter("userName", userName);
		query.setParameter("password", password);
		return (User) query.setMaxResults(1).uniqueResult();
	}
}
