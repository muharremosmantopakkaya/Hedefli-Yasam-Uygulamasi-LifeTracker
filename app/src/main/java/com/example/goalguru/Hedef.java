package com.example.goalguru;
public class Hedef {
    private String ad;
    private boolean durum;
    private String notes;

    public Hedef() {
        // Default constructor required for calls to DataSnapshot.getValue(Hedef.class)
    }

    public Hedef(String ad, boolean durum, String notes) {
        this.ad = ad;
        this.durum = durum;
        this.notes = notes;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }



    public boolean isCompleted() {
        return durum;
    }

}

