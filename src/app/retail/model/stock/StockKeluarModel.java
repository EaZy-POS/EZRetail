/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.stock;

import com.jfoenix.controls.JFXButton;

/**
 *
 * @author Lutfi
 */
public class StockKeluarModel {
    private String TRANSID,TGL,TOTALITEM,TTLQTY,TOTALHARGA,KET,INPUTBY, NAMA;
    private String no,id,kode_item,nama_item,sat,harga,qty,total_harga;
    private String barcode,sku;
    private JFXButton btnedit,btndelete,btnview;

    public StockKeluarModel(String no, String barcode, String sku, String nama_item, String sat, String harga, String qty, String total_harga) {
        this.no = no;
        this.nama_item = nama_item;
        this.sat = sat;
        this.harga = harga;
        this.qty = qty;
        this.total_harga = total_harga;
        this.barcode = barcode;
        this.sku = sku;
    }

    public StockKeluarModel(String no, String TRANSID, String TGL, String TOTALITEM, String TTLQTY, String TOTALHARGA, String KET, String INPUTBY, JFXButton btnview) {
        this.TRANSID = TRANSID;
        this.TGL = TGL;
        this.TOTALITEM = TOTALITEM;
        this.TTLQTY = TTLQTY;
        this.TOTALHARGA = TOTALHARGA;
        this.KET = KET;
        this.NAMA = INPUTBY;
        this.no = no;
        this.btnview = btnview;
    }

    public StockKeluarModel(String no, String id, String kode_item, String nama_item, String sat, String harga, String qty, String total_harga, JFXButton btnedit, JFXButton btndelete) {
        this.no = no;
        this.id = id;
        this.kode_item = kode_item;
        this.nama_item = nama_item;
        this.sat = sat;
        this.harga = harga;
        this.qty = qty;
        this.total_harga = total_harga;
        this.btnedit = btnedit;
        this.btndelete = btndelete;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTRANSID() {
        return TRANSID;
    }

    public void setTRANSID(String TRANSID) {
        this.TRANSID = TRANSID;
    }

    public String getTGL() {
        return TGL;
    }

    public void setTGL(String TGL) {
        this.TGL = TGL;
    }

    public String getTOTALITEM() {
        return TOTALITEM;
    }

    public void setTOTALITEM(String TOTALITEM) {
        this.TOTALITEM = TOTALITEM;
    }

    public String getTTLQTY() {
        return TTLQTY;
    }

    public void setTTLQTY(String TTLQTY) {
        this.TTLQTY = TTLQTY;
    }

    public String getTOTALHARGA() {
        return TOTALHARGA;
    }

    public void setTOTALHARGA(String TOTALHARGA) {
        this.TOTALHARGA = TOTALHARGA;
    }

    public String getKET() {
        return KET;
    }

    public void setKET(String KET) {
        this.KET = KET;
    }

    public String getINPUTBY() {
        return INPUTBY;
    }

    public void setINPUTBY(String INPUTBY) {
        this.INPUTBY = INPUTBY;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKode_item() {
        return kode_item;
    }

    public void setKode_item(String kode_item) {
        this.kode_item = kode_item;
    }

    public String getNama_item() {
        return nama_item;
    }

    public void setNama_item(String nama_item) {
        this.nama_item = nama_item;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
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

    public JFXButton getBtnview() {
        return btnview;
    }

    public void setBtnview(JFXButton btnview) {
        this.btnview = btnview;
    }

    public String getNAMA() {
        return NAMA;
    }

    public void setNAMA(String NAMA) {
        this.NAMA = NAMA;
    }

}
