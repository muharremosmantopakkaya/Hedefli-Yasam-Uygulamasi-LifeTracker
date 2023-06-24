package com.example.goalguru;

import android.os.Parcel;
import android.os.Parcelable;

public class Egzersiz implements Parcelable {
    private String ad;
    private int resimId;

    public Egzersiz(String egzersizAdi, int resimId) {
        this.ad = egzersizAdi;
        this.resimId = resimId;
    }

    public String getAd() {
        return ad;
    }

    public int getResimId() {
        return resimId;
    }

    protected Egzersiz(Parcel in) {
        ad = in.readString();
        resimId = in.readInt();
    }

    public static final Parcelable.Creator<Egzersiz> CREATOR = new Creator<Egzersiz>() {
        @Override
        public Egzersiz createFromParcel(Parcel in) {
            return new Egzersiz(in);
        }

        @Override
        public Egzersiz[] newArray(int size) {
            return new Egzersiz[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ad);
        dest.writeInt(resimId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
