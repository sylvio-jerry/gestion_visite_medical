package com.hitsabo.entity;

import javax.persistence.*;

@Entity
@Table(name = "patient")
public class Patient {
    private int codepat;
    private String nom;
    private String prenom;
    private String sexe;
    private String adresse;

    @Id
    @Column(name = "codepat")
    public int getCodepat() {
        return codepat;
    }

    public void setCodepat(int codepat) {
        this.codepat = codepat;
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
    @Column(name = "sexe")
    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    @Basic
    @Column(name = "adresse")
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        if (codepat != patient.codepat) return false;
        if (nom != null ? !nom.equals(patient.nom) : patient.nom != null) return false;
        if (prenom != null ? !prenom.equals(patient.prenom) : patient.prenom != null) return false;
        if (sexe != null ? !sexe.equals(patient.sexe) : patient.sexe != null) return false;
        if (adresse != null ? !adresse.equals(patient.adresse) : patient.adresse != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codepat;
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (prenom != null ? prenom.hashCode() : 0);
        result = 31 * result + (sexe != null ? sexe.hashCode() : 0);
        result = 31 * result + (adresse != null ? adresse.hashCode() : 0);
        return result;
    }*/

    @Override
    public String toString() {
        return codepat +" "+ nom;
    }
}
