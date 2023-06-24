package com.example.goalguru;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/*
 * Aidat sınıfının Parcelable arabirimini uygulaması, sınıfın nesnelerinin başka bileşenler arasında kolayca geçirilebilmesini sağlar.
 * Parcelable, Android platformunda kullanılan bir arabirimdir ve bir nesneyi byte dizileri olarak seri hale getirerek (marshalling) ve geri dönüştürerek (unmarshalling) veri alışverişini kolaylaştırır.
 *
 * Bu durumda, Aidat sınıfının Parcelable arabirimini uygulaması, nesnelerin bir Parcel üzerinden yazılabilmesini ve okunabilmesini sağlar.
 * Parcel, bir veri akışıdır ve writeToParcel() yöntemi kullanılarak nesneler Parcel'e yazılırken, createFromParcel() yöntemi kullanılarak nesneler Parcel'den geri okunabilir.
 *
 * Bu sayede, Aidat nesneleri başka bileşenlere (örneğin, bir Intent ile başka bir aktiviteye) aktarılabilir ve bu nesnelerin durumu korunarak iletişim sağlanabilir.
 *
 * Kısacası, Aidat sınıfının Parcelable arabirimini uygulaması, nesnelerin kolayca geçirilebilir hale getirilmesini sağlar.
 */

public class Aidat implements Parcelable {
    private String aidatId;
    private String aidatAciklama;
    private Timestamp aidatTarihi;
    private String aidatTutari;

    public Aidat() {
        // Boş yapıcı metot Firebase için gereklidir
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(aidatTarihi.toDate());

        return "Aidat{" +
                "aidatId='" + aidatId + '\'' +
                ", aidatAciklama='" + aidatAciklama + '\'' +
                ", aidatTarihi=" + formattedDate +
                ", aidatTutari='" + aidatTutari + '\'' +
                '}';
    }

    public Aidat(String aidatId, String aidatAciklama, Timestamp aidatTarihi, String aidatTutari) {
        // Aidat nesnesi için yapıcı metot
        this.aidatId = aidatId;
        this.aidatAciklama = aidatAciklama;
        this.aidatTarihi = aidatTarihi;
        this.aidatTutari = aidatTutari;
    }

    protected Aidat(Parcel in) {
        // Parcelable desteği için yapıcı metot

        aidatId = in.readString();
        aidatAciklama = in.readString();
        aidatTarihi = new Timestamp(new Date(in.readLong()));
        aidatTutari = in.readString();
    }

    public static final Creator<Aidat> CREATOR = new Creator<Aidat>() {
        @Override
        public Aidat createFromParcel(Parcel in) {
            return new Aidat(in);
        }

        @Override
        public Aidat[] newArray(int size) {
            return new Aidat[size];
        }
    };
    // Getter ve setter metotları

    public String getAidatId() {
        return aidatId;
    }

    public void setAidatId(String aidatId) {
        this.aidatId = aidatId;
    }

    public String getAidatAciklama() {
        return aidatAciklama;
    }

    public void setAidatAciklama(String aidatAciklama) {
        this.aidatAciklama = aidatAciklama;
    }

    public Timestamp getAidatTarihi() {
        return aidatTarihi;
    }

    public void setAidatTarihi(Timestamp aidatTarihi) {
        this.aidatTarihi = aidatTarihi;
    }

    public String getAidatTutari() {
        return aidatTutari;
    }

    public void setAidatTutari(String aidatTutari) {
        this.aidatTutari = aidatTutari;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Parcelable desteği için metot
        dest.writeString(aidatId);
        dest.writeString(aidatAciklama);
        dest.writeLong(aidatTarihi.toDate().getTime());
        dest.writeString(aidatTutari);
    }
}
