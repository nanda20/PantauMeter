package com.dev.muslim.pantaumeter;

/**
 * Created by owner on 7/19/2017.
 */

public class DataPojo {
    private int id1;
    private int id_pel;
    private String no_tiang;
    private String nama;
    private String alamat;
    private String lat;
    private String lng;
    private String kode_baca;
    private String status;
    private String standKini;
    private String foto;
    private String keterangan;

    public  DataPojo(){

    }
    public DataPojo(int id_pel,String standKini,String foto,String keterangan){
        this.id_pel=id_pel;
        this.standKini=standKini;
        this.foto=foto;
        this.keterangan=keterangan;
    }

    public DataPojo(int id1,int id_pel,String nama, String alamat ,String  no_tiang, String lat,String lng,String kode_baca,String status) {
        this.id1=id1;
        this.id_pel= id_pel;
        this.no_tiang= no_tiang;
        this.nama= nama;
        this.alamat = alamat;
        this.lat = lat;
        this.lng = lng;
        this.kode_baca = kode_baca;
        this.status = status;
    }

    public int getNo_Pel() {
        return id_pel;
    }

    public void setId_pel(int id_pel) {
        this.id_pel = id_pel;
    }

    public String getNo_tiang() {
        return no_tiang;
    }

    public void setNo_tiang(String no_tiang) {
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

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStandKini() {
        return standKini;
    }

    public void setStandKini(String standKini) {
        this.standKini = standKini;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
