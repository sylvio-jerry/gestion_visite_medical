package com.hitsabo.dao;

import com.hitsabo.entity.Visiter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.hitsabo.util.HibernateUtil;

import java.util.List;

public class VisiterDAO {
    public boolean create(Visiter visiter) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = (Session) HibernateUtil.openSession();
            transaction = session.beginTransaction();
            session.save(visiter);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        return false;
    }

    public boolean update(Visiter visiter) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = (Session) HibernateUtil.openSession();
            transaction = session.beginTransaction();
            session.update(visiter);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        return false;
    }

    public void delete(Visiter visiter) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = (Session) HibernateUtil.openSession();
            transaction = session.beginTransaction();
            Visiter attachedVisiter = session.get(Visiter.class, visiter.getId());
            session.delete(attachedVisiter);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
    }

    public Visiter getById(int id) {
        Session session = null;
        Visiter visiter = null;
        try {
            session = (Session) HibernateUtil.openSession();
            visiter = session.get(Visiter.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        return visiter;
    }

    public List<Visiter> getAllVisits() {
        Session session = null;
        List<Visiter> visits = null;
        try {
            session = (Session) HibernateUtil.openSession();
            Query<Visiter> query = session.createQuery("SELECT v FROM Visiter v", Visiter.class);
            visits = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        return visits;
    }

    public int getNewIdVisiter() {
        Session session = null;
        Integer lastIdVisiter = null;
        try {
            session = (Session) HibernateUtil.openSession();
            Query<Integer> query = session.createQuery("SELECT MAX(v.id) FROM Visiter v", Integer.class);
            lastIdVisiter = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        if (lastIdVisiter == null){
            return lastIdVisiter = 1;
        }
        else {
            return lastIdVisiter+1;
        }
    }
}
