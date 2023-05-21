package com.hitsabo.dao;

import com.hitsabo.entity.Medecin;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.hitsabo.util.HibernateUtil;

import java.util.List;

public class MedecinDAO {
    public boolean create(Medecin medecin) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = (Session) HibernateUtil.openSession();
            transaction = session.beginTransaction();
            session.save(medecin);
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

    public boolean update(Medecin medecin) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = (Session) HibernateUtil.openSession();
            transaction = session.beginTransaction();
            session.update(medecin);
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

    public boolean delete(Medecin medecin) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = (Session) HibernateUtil.openSession();
            transaction = session.beginTransaction();

            // Attacher l'instance du médecin à la session
            Medecin attachedMedecin = session.get(Medecin.class, medecin.getCodemed());

            // Supprimer le médecin attaché
            session.delete(attachedMedecin);

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

    public static Medecin getByCodeMed(int codemed) {
        Session session = null;
        Medecin medecin = null;
        try {
            session = (Session) HibernateUtil.openSession();
            medecin = session.get(Medecin.class, codemed);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        return medecin;
    }

    public List<Medecin> getAllMedecin() {
        Session session = null;
        List<Medecin> medecins = null;
        try {
            session = (Session) HibernateUtil.openSession();
            Query<Medecin> query = session.createQuery("SELECT m FROM Medecin m", Medecin.class);
            medecins = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        return medecins;
    }

    public int getLastCodeMed() {
        Session session = null;
        Integer lastCodeMed = null;
        try {
            session = (Session) HibernateUtil.openSession();
            Query<Integer> query = session.createQuery("SELECT MAX(m.codemed) FROM medecin m", Integer.class);
            lastCodeMed = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        if (lastCodeMed == null){
            return lastCodeMed = 1;
        }
        else {
            return lastCodeMed;
        }
    }

    public int getNewCodeMed() {
        Session session = null;
        Integer lastCodeMed = null;
        try {
            session = (Session) HibernateUtil.openSession();
            Query<Integer> query = session.createQuery("SELECT MAX(m.codemed) FROM Medecin m", Integer.class);
            lastCodeMed = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        if (lastCodeMed == null){
            return lastCodeMed = 1;
        }
        else {
            return lastCodeMed+1;
        }
    }


}
