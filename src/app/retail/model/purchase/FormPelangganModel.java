/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.purchase;

import com.jfoenix.controls.JFXButton;

/**
 *
 * @author Lutfi
 */
public class FormPelangganModel {
    private String no,id_pelanggan,nama_pelanggan,almt,telp;
    private JFXButton btnpilih;

    public FormPelangganModel(String no, String id_pelanggan, String nama_pelanggan, String almt, String telp, JFXButton btnpilih) {
        this.no = no;
        this.id_pelanggan = id_pelanggan;
        this.nama_pelanggan = nama_pelanggan;
        this.almt = almt;
        this.telp = telp;
        this.btnpilih = btnpilih;
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

    public String getAlmt() {
        return almt;
    }

    public void setAlmt(String almt) {
        this.almt = almt;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public JFXButton getBtnpilih() {
        return btnpilih;
    }

    public void setBtnpilih(JFXButton btnpilih) {
        this.btnpilih = btnpilih;
    }
    
}
