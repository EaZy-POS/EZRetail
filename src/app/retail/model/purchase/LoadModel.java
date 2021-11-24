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
public class LoadModel {
    private String no,kode_trans,tran_date,id_pelanggan,pelanggan,Total,disc,disc_ammount,grand_total;
    private JFXButton btnload;

    public LoadModel(String no, String kode_trans, String tran_date, String id_pelanggan, String pelanggan, String Total, String disc, String disc_ammount, String grand_total, JFXButton btnload) {
        this.no = no;
        this.kode_trans = kode_trans;
        this.tran_date = tran_date;
        this.id_pelanggan = id_pelanggan;
        this.pelanggan = pelanggan;
        this.Total = Total;
        this.disc = disc;
        this.disc_ammount = disc_ammount;
        this.grand_total = grand_total;
        this.btnload = btnload;
    }

    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getDisc_ammount() {
        return disc_ammount;
    }

    public void setDisc_ammount(String disc_ammount) {
        this.disc_ammount = disc_ammount;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }
    
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getKode_trans() {
        return kode_trans;
    }

    public void setKode_trans(String kode_trans) {
        this.kode_trans = kode_trans;
    }

    public String getTran_date() {
        return tran_date;
    }

    public void setTran_date(String tran_date) {
        this.tran_date = tran_date;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public JFXButton getBtnload() {
        return btnload;
    }

    public void setBtnload(JFXButton btnload) {
        this.btnload = btnload;
    }
    
}
