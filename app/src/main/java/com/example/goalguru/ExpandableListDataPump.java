package com.example.goalguru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("Güvenliğin ve gizliliğin sizin için önemli olduğunu biliyoruz ve bizim için de önemliler. Güçlü güvenlik sağlamayı bir öncelik haline getiriyoruz ve bilgilerinizin ihtiyacınız olduğunda güvende ve erişilebilir olduğuna dair size güven veriyoruz.\n" +
                "\n" +
                "Güçlü bir güvenlik sağlamak, gizliliğinizi korumak ve LifeTracker'ı sizin için daha etkili ve verimli hale getirmek için sürekli çalışıyoruz. Her yıl güvenliğe yüz milyonlarca dolar harcıyoruz ve bilgilerinizi güvende tutmak için veri güvenliği konusunda dünyaca ünlü uzmanlarla çalışıyoruz. Ayrıca, LifeTracker Kontrol Paneli, 2 adımlı doğrulama ve Reklam Merkezim'de bulunan kişiselleştirilmiş reklam ayarları gibi kullanımı kolay gizlilik ve güvenlik araçları oluşturduk. Dolayısıyla, LifeTracker ile paylaştığınız bilgiler söz konusu olduğunda, kontrol sizdedir.");



        List<String> football = new ArrayList<String>();
        football.add("LifeTracker Projesi, kullanıcıların hedeflerini yönetmelerine ve kişisel gelişimlerine odaklanmalarına yardımcı olan kapsamlı bir uygulamadır. Hedeflerinizi takip etmek ve yönetmek için çeşitli özellikler sunar. LifeTracker Projesi, kullanıcılara geniş bir yelpazede hedeflerini takip etme ve yönetme imkanı sunar. Özellikler arasında para yönetimi, kilo yönetimi, aidat yönetimi ve günlük kayıt bulunur.");


        List<String> basketball = new ArrayList<String>();
        basketball.add("Mail ile iletişime geçebilirsiniz, muharremtopakkaya@gmail.com veya Telefon ile iletişime geçmek isterseniz +90 536 067 41 06.");


        expandableListDetail.put("LifeTracker gizliliğimi nasıl koruyor ve bilgilerimin güvenliğini nasıl sağlıyor?", cricket);
        expandableListDetail.put("LifeTracker projesi nedir?", football);
        expandableListDetail.put("İletişim bilgileriniz nedir?", basketball);
        return expandableListDetail;
    }
}