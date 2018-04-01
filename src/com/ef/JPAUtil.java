package com.ef;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

	private static EntityManagerFactory entityManagerFactory;

	static {
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		entityManagerFactory = Persistence.createEntityManagerFactory("webserveraccesslog");
	}

	public static EntityManager getEntityManager() {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		return entityManager;
	}

	public static void closeEntityManagerFactory() {
		entityManagerFactory.close();
	}
}
