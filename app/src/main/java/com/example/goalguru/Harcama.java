package com.example.goalguru;

import com.google.firebase.Timestamp;

public class Harcama {
    private String id;
    private double harcamaTutar;
    private String harcamaAciklama;
    private Timestamp date;

    public Harcama() {
        // Boş yapıcı metod
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Harcama(double harcamaTutar, String harcamaAciklama, Timestamp date) {
        this.harcamaTutar = harcamaTutar;
        this.harcamaAciklama = harcamaAciklama;
        this.date = date;
    }

    // Getter ve Setter metotları

    public double getHarcamaTutar() {
        return harcamaTutar;
    }

    public void setHarcamaTutar(double harcamaTutar) {
        this.harcamaTutar = harcamaTutar;
    }

    public String getHarcamaAciklama() {
        return harcamaAciklama;
    }

    public void setHarcamaAciklama(String harcamaAciklama) {
        this.harcamaAciklama = harcamaAciklama;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


}
