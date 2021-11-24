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
public class HutangModel {
    private String no,tran_date,kode_trans,faktur,id_supplier,nama,total,id,flag,type,bln;
    private JFXButton btnpay,btnview;
    private double totalHutang,terBayar,sisa;

    public HutangModel(String no, String tran_date, String kode_trans, String faktur, String id_supplier, String nama, String total, String id, String flag, String type, JFXButton btnpay, JFXButton btnview) {
        this.no = no;
        this.tran_date = tran_date;
        this.kode_trans = kode_trans;
        this.faktur = faktur;
        this.id_supplier = id_supplier;
        this.nama = nama;
        this.total = total;
        this.id = id;
        this.flag = flag;
        this.type = type;
        this.btnpay = btnpay;
        this.btnview = btnview;
    }

    public HutangModel(String nama, double totalHutang, double terBayar, double sisa) {
        this.nama = nama;
        this.totalHutang = totalHutang;
        this.terBayar = terBayar;
        this.sisa = sisa;
    }

    public HutangModel(String bln, double totalHutang) {
        this.bln = bln;
        this.totalHutang = totalHutang;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getKode_trans() {
        return kode_trans;
    }

    public void setKode_trans(String kode_trans) {
        this.kode_trans = kode_trans;
    }

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    public String getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(String id_supplier) {
        this.id_supplier = id_supplier;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public String getBln() {
        return bln;
    }

    public void setBln(String bln) {
        this.bln = bln;
    }

    public double getTotalHutang() {
        return totalHutang;
    }

    public void setTotalHutang(double totalHutang) {
        this.totalHutang = totalHutang;
    }

    public double getTerBayar() {
        return terBayar;
    }

    public void setTerBayar(double terBayar) {
        this.terBayar = terBayar;
    }

    public double getSisa() {
        return sisa;
    }

    public void setSisa(double sisa) {
        this.sisa = sisa;
    }
    
}
