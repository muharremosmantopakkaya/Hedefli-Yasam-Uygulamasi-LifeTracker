package com.example.goalguru;

public class VKE {
    private String weight;
    private String height;

    public VKE() {
        // Boş kurucu metot gereklidir (Firebase'den verileri çekerken kullanılır)
    }

    public VKE(String weight, String height) {
        this.weight = weight;
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
