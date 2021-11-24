/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.purchase;

/**
 *
 * @author Lutfi
 */
public class ViewPurchaseModel {
    private String no,kode_item,nama_item,sat,harga,qty,total;

    public ViewPurchaseModel(String no, String kode_item, String nama_item, String sat, String harga, String qty, String total) {
        this.no = no;
        this.kode_item = kode_item;
        this.nama_item = nama_item;
        this.sat = sat;
        this.harga = harga;
        this.qty = qty;
        this.total = total;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
}
