package com.lenggogeni.appmading.model;

public class Informasi {
    int id;
    String judul, detail, waktu, diunggah_oleh, image;

    public Informasi() {
    }

    public Informasi(int id, String judul, String detail, String waktu, String diunggah_oleh) {
        this.id = id;
        this.judul = judul;
        this.detail = detail;
        this.waktu = waktu;
        this.diunggah_oleh = diunggah_oleh;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDiunggah_oleh() {
        return diunggah_oleh;
    }

    public void setDiunggah_oleh(String diunggah_oleh) {
        this.diunggah_oleh = diunggah_oleh;
    }
}
