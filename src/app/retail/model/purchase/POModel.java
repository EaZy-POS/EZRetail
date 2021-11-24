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
public class POModel {
    private String kode_transaksi,trans_date,refnum,id_supplier,total,type,flag;
    private String no, item_code,item_name,descrip,buy_price,qty,total_harga;
    JFXButton btn_edit,btn_delete;

    public POModel(String kode_transaksi, String trans_date, String refnum, String id_supplier, String total, String type, String flag) {
        this.kode_transaksi = kode_transaksi;
        this.trans_date = trans_date;
        this.refnum = refnum;
        this.id_supplier = id_supplier;
        this.total = total;
        this.type = type;
        this.flag = flag;
    }

    public POModel(String no,String item_code, String item_name, String descrip, String buy_price, String qty, String total_harga, JFXButton btn_edit, JFXButton btn_delete) {
        this.item_code = item_code;
        this.item_name = item_name;
        this.descrip = descrip;
        this.buy_price = buy_price;
        this.qty = qty;
        this.total_harga = total_harga;
        this.btn_edit = btn_edit;
        this.btn_delete = btn_delete;
        this.no = no;
    }        

    public POModel() {
    }    

    public String getKode_transaksi() {
        return kode_transaksi;
    }

    public void setKode_transaksi(String kode_transaksi) {
        this.kode_transaksi = kode_transaksi;
    }

    public String getTrans_date() {
        return trans_date;
    }

    public void setTrans_date(String trans_date) {
        this.trans_date = trans_date;
    }

    public String getRefnum() {
        return refnum;
    }

    public void setRefnum(String refnum) {
        this.refnum = refnum;
    }

    public String getId_supplier() {
        return id_supplier;
    }

    public void setId_supplier(String id_supplier) {
        this.id_supplier = id_supplier;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }

    public JFXButton getBtn_edit() {
        return btn_edit;
    }

    public void setBtn_edit(JFXButton btn_edit) {
        this.btn_edit = btn_edit;
    }

    public JFXButton getBtn_delete() {
        return btn_delete;
    }

    public void setBtn_delete(JFXButton btn_delete) {
        this.btn_delete = btn_delete;
    }
    
}
