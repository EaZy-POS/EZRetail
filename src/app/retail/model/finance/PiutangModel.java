/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.finance;

import com.jfoenix.controls.JFXButton;

/**
 *
 * @author Lutfi
 */
public class PiutangModel {
    private String no,tran_date,faktur,id_pelanggan,pelanggan,total,id,flag;
    private JFXButton btnpay,btnview;
    private int bln;
    private double totalPiutang,terbayar,sisa;

    public PiutangModel(String no, String tran_date, String faktur, String id_pelanggan, String pelanggan, String total, String id, String flag, JFXButton btnpay, JFXButton btnview) {
        this.no = no;
        this.tran_date = tran_date;
        this.faktur = faktur;
        this.id_pelanggan = id_pelanggan;
        this.pelanggan = pelanggan;
        this.total = total;
        this.id = id;
        this.flag = flag;
        this.btnpay = btnpay;
        this.btnview = btnview;
    }

    public PiutangModel(int bln, double totalPiutang) {
        this.bln = bln;
        this.totalPiutang = totalPiutang;
    }

    public PiutangModel(String pelanggan, double totalPiutang, double terbayar, double sisa) {
        this.pelanggan = pelanggan;
        this.totalPiutang = totalPiutang;
        this.terbayar = terbayar;
        this.sisa = sisa;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTran_date() {
        return tran_date;
    }

    public void setTran_date(String tran_date) {
        this.tran_date = tran_date;
    }

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public JFXButton getBtnpay() {
        return btnpay;
    }

    public void setBtnpay(JFXButton btnpay) {
        this.btnpay = btnpay;
    }

    public JFXButton getBtnview() {
        return btnview;
    }

    public void setBtnview(JFXButton btnview) {
        this.btnview = btnview;
    }

    public int getBln() {
        return bln;
    }

    public void setBln(int bln) {
        this.bln = bln;
    }

    public double getTotalPiutang() {
        return totalPiutang;
    }

    public void setTotalPiutang(double totalPiutang) {
        this.totalPiutang = totalPiutang;
    }

    public double getTerbayar() {
        return terbayar;
    }

    public void setTerbayar(double terbayar) {
        this.terbayar = terbayar;
    }

    public double getSisa() {
        return sisa;
    }

    public void setSisa(double sisa) {
        this.sisa = sisa;
    }

    
}
