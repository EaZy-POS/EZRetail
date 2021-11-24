/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.data;

import com.jfoenix.controls.JFXButton;

/**
 *
 * @author Lutfi
 */
public class KaryawanModel {
    private String no,kode_karyawan,id_karyawan,nama_karyawan,almt_karyawan,telp,jab,email;
    private JFXButton btnedit,btndelete;

    public KaryawanModel(String no, String kode_karyawan, String id_karyawan, String nama_karyawan, String almt_karyawan, String telp, String jab, String email, JFXButton btnedit, JFXButton btndelete) {
        this.no = no;
        this.kode_karyawan = kode_karyawan;
        this.id_karyawan = id_karyawan;
        this.nama_karyawan = nama_karyawan;
        this.almt_karyawan = almt_karyawan;
        this.telp = telp;
        this.jab = jab;
        this.email = email;
        this.btnedit = btnedit;
        this.btndelete = btndelete;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getKode_karyawan() {
        return kode_karyawan;
    }

    public void setKode_karyawan(String kode_karyawan) {
        this.kode_karyawan = kode_karyawan;
    }

    public String getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(String id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public String getNama_karyawan() {
        return nama_karyawan;
    }

    public void setNama_karyawan(String nama_karyawan) {
        this.nama_karyawan = nama_karyawan;
    }

    public String getAlmt_karyawan() {
        return almt_karyawan;
    }

    public void setAlmt_karyawan(String almt_karyawan) {
        this.almt_karyawan = almt_karyawan;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getJab() {
        return jab;
    }

    public void setJab(String jab) {
        this.jab = jab;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public JFXButton getBtnedit() {
        return btnedit;
    }

    public void setBtnedit(JFXButton btnedit) {
        this.btnedit = btnedit;
    }

    public JFXButton getBtndelete() {
        return btndelete;
    }

    public void setBtndelete(JFXButton btndelete) {
        this.btndelete = btndelete;
    }
    
}
