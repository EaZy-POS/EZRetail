/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.master;

/**
 *
 * @author Lutfi
 */
public class ProfileModel {
    private String nama_usaha,almt,kota,telp,fax,imageurl;

    public ProfileModel() {
    }

    public ProfileModel(String nama_usaha, String almt, String kota, String telp, String fax, String imageurl) {
        this.nama_usaha = nama_usaha;
        this.almt = almt;
        this.kota = kota;
        this.telp = telp;
        this.fax = fax;
        this.imageurl = imageurl;
    }

    public String getNama_usaha() {
        return nama_usaha;
    }

    public void setNama_usaha(String nama_usaha) {
        this.nama_usaha = nama_usaha;
    }

    public String getAlmt() {
        return almt;
    }

    public void setAlmt(String almt) {
        this.almt = almt;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
    
}
