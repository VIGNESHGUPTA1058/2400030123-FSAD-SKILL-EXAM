package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.time.LocalDate;

public class ClientDemo {

    private static SessionFactory sessionFactory;

    // Build the Hibernate SessionFactory
    private static SessionFactory buildSessionFactory() {
        return new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Library.class)
                .buildSessionFactory();
    }

    // I. Insert a new record into the database
    public static void insertLibrary(Library library) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(library);
            transaction.commit();
            System.out.println("Record inserted successfully: " + library);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error inserting record: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    // II. Delete the record based on the ID
    public static void deleteLibrary(int id) {
        Session session = sessionFactory.openSession();
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
            System.err.println("Error deleting record: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    public static void main(String[] args) {
        sessionFactory = buildSessionFactory();

        try {
            // I. Insert a new Library record
            Library library = new Library(
                    "Java Programming",
                    "A comprehensive guide to Java programming language",
                    LocalDate.of(2023, 6, 15),
                    "Available",
                    "James Gosling",
                    "Sun Microsystems",
                    "978-0-13-468599-1"
            );
            insertLibrary(library);

            // Retrieve and print the generated ID
            int generatedId = library.getId();
            System.out.println("Generated ID: " + generatedId);

            // II. Delete the record by ID
            deleteLibrary(generatedId);
        } finally {
            sessionFactory.close();
        }
    }
}
