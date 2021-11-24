/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.stock;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TextField;

/**
 *
 * @author Lutfi
 */
public class StockOpnameModel {
    private String no, kodeItem, sku, barcode, namaItem, sat, stockakhir, qtyOpname, selisih;
    private TextField txtQty;
    private String transId,tgl,inputBy,flag;
    private JFXButton btnview,btnverifikasi,btndelete;

    public StockOpnameModel(String no, String kodeItem,  String namaItem, String sat, String stockakhir, String qtyOpname, String selisih, TextField txtQty) {
        this.no = no;
        this.kodeItem = kodeItem;
        this.namaItem = namaItem;
        this.sat = sat;
        this.stockakhir = stockakhir;
        this.qtyOpname = qtyOpname;
        this.selisih = selisih;
        this.txtQty = txtQty;
    }
    
    public StockOpnameModel(String no, String kodeItem, String sku, String barcode, String namaItem, String sat, String stockakhir, String qtyOpname, String selisih, TextField txtQty, JFXButton btndelete) {
        this.no = no;
        this.kodeItem = kodeItem;
        this.barcode = barcode;
        this.sku = sku;
        this.namaItem = namaItem;
        this.sat = sat;
        this.stockakhir = stockakhir;
        this.qtyOpname = qtyOpname;
        this.selisih = selisih;
        this.txtQty = txtQty;
        this.btndelete = btndelete;
    }

    public StockOpnameModel(String no, String transId, String tgl, String inputBy, String flag, JFXButton btnview, JFXButton btnverifikasi, JFXButton btndelete) {
        this.no = no;
        this.transId = transId;
        this.tgl = tgl;
        this.inputBy = inputBy;
        this.flag = flag;
        this.btnview = btnview;
        this.btnverifikasi = btnverifikasi;
        this.btndelete = btndelete;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getInputBy() {
        return inputBy;
    }

    public void setInputBy(String inputBy) {
        this.inputBy = inputBy;
    }

    public JFXButton getBtnview() {
        return btnview;
    }

    public void setBtnview(JFXButton btnview) {
        this.btnview = btnview;
    }

    public JFXButton getBtnverifikasi() {
        return btnverifikasi;
    }

    public void setBtnverifikasi(JFXButton btnverifikasi) {
        this.btnverifikasi = btnverifikasi;
    }

    public JFXButton getBtndelete() {
        return btndelete;
    }

    public void setBtndelete(JFXButton btndelete) {
        this.btndelete = btndelete;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getKodeItem() {
        return kodeItem;
    }

    public void setKodeItem(String kodeItem) {
        this.kodeItem = kodeItem;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getStockakhir() {
        return stockakhir;
    }

    public void setStockakhir(String stockakhir) {
        this.stockakhir = stockakhir;
    }

    public String getQtyOpname() {
        return qtyOpname;
    }

    public void setQtyOpname(String qtyOpname) {
        this.qtyOpname = qtyOpname;
    }

    public String getSelisih() {
        return selisih;
    }

    public void setSelisih(String selisih) {
        this.selisih = selisih;
    }

    public TextField getTxtQty() {
        return txtQty;
    }

    public void setTxtQty(TextField txtQty) {
        this.txtQty = txtQty;
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
    
}
