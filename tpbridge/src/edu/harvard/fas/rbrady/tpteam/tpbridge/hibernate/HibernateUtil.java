/********************************************************************
 * 
 * File		:	HibernateUtil.java
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	Provides Hibernate framework utility methods
 * 
 ********************************************************************/
package edu.harvard.fas.rbrady.tpteam.tpbridge.hibernate;

import org.hibernate.*;
import org.hibernate.cfg.*;

/********************************************************************
 * File			:	HibernateUtil.java
 *
 * Description	: 	Provides Hibernate framework utility methods
 *
 * @author Bob Brady, rpbrady@gmail.com
 * @version $Revision$
 * @date $Date$
 * Copyright (c) 2007 Bob Brady
 *********************************************************************/
public class HibernateUtil {

	/** The Hibernate Database Connection Session Factory */
	private static final SessionFactory sessionFactory;

	static {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Getter
	 * @return The Hibernate Database Session Factory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}