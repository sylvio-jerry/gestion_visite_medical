package com.hitsabo.entity;

import javax.persistence.*;

@Entity
@Table(name = "medecin")
public class Medecin {
    private int codemed;
    private String nom;
    private String prenom;
    private String grade;

    @Id
    @Column(name = "codemed")
    public int getCodemed() {
        return codemed;
    }

    public void setCodemed(int codemed) {
        this.codemed = codemed;
    }

    @Basic
    @Column(name = "nom")
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Basic
    @Column(name = "prenom")
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Basic
    @Column(name = "grade")
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Medecin medecin = (Medecin) o;

        if (codemed != medecin.codemed) return false;
        if (nom != null ? !nom.equals(medecin.nom) : medecin.nom != null) return false;
        if (prenom != null ? !prenom.equals(medecin.prenom) : medecin.prenom != null) return false;
        if (grade != null ? !grade.equals(medecin.grade) : medecin.grade != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codemed;
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (prenom != null ? prenom.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        return result;
    }*/


    @Override
    public String toString() {
        return codemed +" "+ nom;
    }
}
