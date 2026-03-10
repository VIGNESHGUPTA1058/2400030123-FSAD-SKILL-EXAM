package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class ClientDemo {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void insertRecord(Library library) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(library);
            transaction.commit();
            System.out.println("Record inserted successfully with ID: " + library.getId() + " -> " + library);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error inserting record: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void deleteRecord(int id) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Library library = session.get(Library.class, id);
            if (library != null) {
                session.delete(library);
                transaction.commit();
                System.out.println("Record deleted successfully with ID: " + id);
            } else {
                System.out.println("No record found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error deleting record: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void main(String[] args) {
        Library library = new Library();
        library.setName("Java Programming");
        library.setDescription("A comprehensive guide to Java programming language");
        library.setDate(LocalDate.of(2024, 1, 15));
        library.setStatus("Available");
        library.setAuthor("James Gosling");
        library.setCategory("Programming");

        insertRecord(library);

        deleteRecord(library.getId());

        getSessionFactory().close();
    }
}
