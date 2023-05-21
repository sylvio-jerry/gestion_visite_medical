package com.hitsabo.dao;

import com.hitsabo.entity.Patient;
import com.hitsabo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PatientDAO {
    public boolean create(Patient patient) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = (Session) HibernateUtil.openSession();
            transaction = session.beginTransaction();
            session.save(patient);
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

    public boolean update(Patient patient) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = (Session) HibernateUtil.openSession();
            transaction = session.beginTransaction();
            session.update(patient);
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

    public boolean delete(Patient patient) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = (Session) HibernateUtil.openSession();
            transaction = session.beginTransaction();

            // Attacher l'instance du médecin à la session
            Patient attachedPatient = session.get(Patient.class, patient.getCodepat());

            // Supprimer le médecin attaché
            session.delete(attachedPatient);

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

    public static Patient getByCodePat(int codepat) {
        Session session = null;
        Patient patient = null;
        try {
            session = (Session) HibernateUtil.openSession();
            patient = session.get(Patient.class, codepat);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        return patient;
    }

    public List<Patient> getAllPatient() {
        Session session = null;
        List<Patient> patients = null;
        try {
            session = (Session) HibernateUtil.openSession();
            Query<Patient> query = session.createQuery("SELECT p FROM Patient p", Patient.class);
            patients = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        return patients;
    }

    public int getLastCodePat() {
        Session session = null;
        Integer lastCodePat = null;
        try {
            session = (Session) HibernateUtil.openSession();
            Query<Integer> query = session.createQuery("SELECT MAX(p.codepat) FROM Patient p", Integer.class);
            lastCodePat = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        if (lastCodePat == null){
            return lastCodePat = 1;
        }
        else {
            return lastCodePat;
        }
    }

    public int getNewCodePat() {
        Session session = null;
        Integer lastCodePat = null;
        try {
            session = (Session) HibernateUtil.openSession();
            Query<Integer> query = session.createQuery("SELECT MAX(p.codepat) FROM Patient p", Integer.class);
            lastCodePat = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                HibernateUtil.closeSession(session);
            }
        }
        if (lastCodePat == null){
            return lastCodePat = 1;
        }
        else {
            return lastCodePat+1;
        }
    }


}
