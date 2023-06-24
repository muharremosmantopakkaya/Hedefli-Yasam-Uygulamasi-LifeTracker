package com.example.goalguru;

public class OrucPlan {
    private String orucPlanAdi;
    private String orucPlanDetay;

    public OrucPlan(String orucPlanAdi, String orucPlanDetay) {
        this.orucPlanAdi = orucPlanAdi;
        this.orucPlanDetay = orucPlanDetay;
    }

    public String getOrucPlanAdi() {
        return orucPlanAdi;
    }

    public String getOrucPlanDetay() {
        return orucPlanDetay;
    }
}
