package com.hitsabo.entity;

import javax.persistence.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "visiter")
public class Visiter {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "codemed", referencedColumnName = "codemed")
    private Medecin medecinByCodemed;
    @ManyToOne
    @JoinColumn(name = "codepat", referencedColumnName = "codepat")
    private Patient patientByCodepat;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "date")
    /*public Date getDate() {
        return date;
    }*/

    public String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }



    @ManyToOne
    @JoinColumn(name = "codemed", referencedColumnName = "codemed")
    public Medecin getMedecinByCodemed() {
        return medecinByCodemed;
    }

    public void setMedecinByCodemed(Medecin medecinByCodemed) {
        this.medecinByCodemed = medecinByCodemed;
    }

    @ManyToOne
    @JoinColumn(name = "codepat", referencedColumnName = "codepat")
    public Patient getPatientByCodepat() {
        return patientByCodepat;
    }

    public void setPatientByCodepat(Patient patientByCodepat) {
        this.patientByCodepat = patientByCodepat;
    }

    @Override
    public String toString() {
        return "Visiter{" +
                "id=" + id +
                ", date=" + date +
                ", medecinByCodemed=" + medecinByCodemed +
                ", patientByCodepat=" + patientByCodepat +
                '}';
    }

    public String getNomMedecin(){
        return this.getMedecinByCodemed().getNom()+" "+this.getMedecinByCodemed().getPrenom();
    }
    public String getNomPatient(){
        return this.getPatientByCodepat().getNom()+" "+this.getPatientByCodepat().getPrenom();
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visiter visiter = (Visiter) o;

        if (id != visiter.id) return false;
        if (date != null ? !date.equals(visiter.date) : visiter.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
*/

}
