package com.example.goalguru;

import java.util.List;

public class PricePackedModel {
    String packedName;
    String packedPrice;
    List<String> id;

    public String getPackedName() {
        return packedName;
    }

    public PricePackedModel(String packedName, String packedPrice, List<String> id) {
        this.packedName = packedName;
        this.packedPrice = packedPrice;
        this.id = id;
    }

    public void setPackedName(String packedName) {
        this.packedName = packedName;
    }

    public String getPackedPrice() {
        return packedPrice;
    }

    public void setPackedPrice(String packedPrice) {
        this.packedPrice = packedPrice;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }
}