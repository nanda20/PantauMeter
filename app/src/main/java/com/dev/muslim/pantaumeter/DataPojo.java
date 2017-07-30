package com.dev.muslim.pantaumeter;

/**
 * Created by owner on 7/19/2017.
 */

public class DataPojo {
    private int id_pel;
    private int no_tiang;
    private String nama;
    private String alamat;
    private String lat;
    private String lng;
    private String kode_baca;

    public DataPojo(int id_pel, int no_tiang,String nama, String alamat, String lat,String lng,String kode_baca) {

        this.id_pel= id_pel;
        this.no_tiang= no_tiang;
        this.nama= nama;
        this.alamat = alamat;
        this.lat = lat;
        this.lng = lng;
        this.kode_baca = kode_baca;
    }

    public int getNo_Pel() {
        return id_pel;
    }

    public void setId_pel(int id_pel) {
        this.id_pel = id_pel;
    }

    public int getNo_tiang() {
        return no_tiang;
    }

    public void setNo_tiang(int no_tiang) {
        this.no_tiang = no_tiang;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getKode_baca() {
        return kode_baca;
    }

    public void setKode_baca(String kode_baca) {
        this.kode_baca = kode_baca;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
