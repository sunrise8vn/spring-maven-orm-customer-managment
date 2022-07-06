package com.cg.service.impl;

import com.cg.model.Customer;
import com.cg.service.CustomerService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

public class HibernateCustomerServiceImpl implements CustomerService {

    private static SessionFactory sessionFactory;

    private static EntityManager entityManager;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.conf.xml")
                    .buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> findAll() {
//        String queryStr = "SELECT c FROM Customer AS c";
        String queryStr = "SELECT * FROM customers AS c";
//        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        Query query = entityManager.createNativeQuery(queryStr, Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer findOne(Long id) {
        String queryStr = "SELECT c FROM Customer AS c WHERE c.id = :id";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Customer save(Customer customer) {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            if (customer.getId() == null) {
                session.saveOrUpdate(customer);
                transaction.commit();
                return customer;
            } else {
                Customer origin = findOne(customer.getId());
                origin.setName(customer.getName());
                origin.setEmail(customer.getEmail());
                origin.setAddress(customer.getAddress());

                session.saveOrUpdate(origin);
                transaction.commit();
                return origin;
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }

    @Override
    public List<Customer> save(List<Customer> customers) {
        return null;
    }

    @Override
    public boolean exists(Long id) {
        return false;
    }

    @Override
    public List<Customer> findAll(List<Long> ids) {
        return Collections.emptyList();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    @Transactional
    public void delete(Long id) {

//        Transaction transaction = null;
//        Session session = sessionFactory.openSession();
//        transaction = session.beginTransaction();

        String queryStr = "DELETE FROM Customer AS c WHERE c.id = :id ";

//        session.createQuery(queryStr, Customer.class);
//        transaction.commit();

//        Query query = entityManager.createQuery(queryStr, Customer.class);
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        query.setParameter("id", id);
        query.executeUpdate();

//        Transaction transaction = null;
//        try {
//            Session session = sessionFactory.openSession();
//            transaction = session.beginTransaction();
//
//            Customer customer = findOne(id);
//
//            if (customer != null) {
//                session.delete(customer);
//                transaction.commit();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (transaction != null) {
//                transaction.rollback();
//            }
//        }
    }

    @Override
    public void delete(Customer customer) {
    }

    @Override
    public void delete(List<Customer> customers) {
    }

    @Override
    public void deleteAll() {
    }
}
