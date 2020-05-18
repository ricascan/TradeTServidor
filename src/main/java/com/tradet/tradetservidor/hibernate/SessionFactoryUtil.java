/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tradet.tradetservidor.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;


import org.hibernate.cfg.Configuration;

/**
 *
 * @author usuario
 */
public class SessionFactoryUtil {

    private static final SessionFactory SF;

    static {
        try {
            // Se inicializa el entorno Hibernate y se carga el fichero hibernate.cfg.xml
            // Se crea la fábrica de sesiones

            SF = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException ex) {
            System.out.println("Error: " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SF;
    }

}
