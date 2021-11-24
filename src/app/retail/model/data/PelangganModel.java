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
public class PelangganModel {
    private String no,id_pelanggan,nama_pelanggan,almt_pelanggan,telp,email,pekerjaan;
    private JFXButton btnedit,btndelete;

    public PelangganModel(String no, String id_pelanggan, String nama_pelanggan, String almt_pelanggan, String telp, String email, String pekerjaan, JFXButton btnedit, JFXButton btndelete) {
        this.no = no;
        this.id_pelanggan = id_pelanggan;
        this.nama_pelanggan = nama_pelanggan;
        this.almt_pelanggan = almt_pelanggan;
        this.telp = telp;
        this.email = email;
        this.pekerjaan = pekerjaan;
        this.btnedit = btnedit;
        this.btndelete = btndelete;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getAlmt_pelanggan() {
        return almt_pelanggan;
    }

    public void setAlmt_pelanggan(String almt_pelanggan) {
        this.almt_pelanggan = almt_pelanggan;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
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
