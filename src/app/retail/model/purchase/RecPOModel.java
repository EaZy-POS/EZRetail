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
public class RecPOModel {
    private String no, item_code,item_name,descrip,buy_price,qty,total_harga,terima;
    private JFXButton btn_edit,btn_delete;

    public RecPOModel(String no, String item_code, String item_name, String descrip, String buy_price, String qty, String total_harga, String terima, JFXButton btn_edit, JFXButton btn_delete) {
        this.no = no;
        this.item_code = item_code;
        this.item_name = item_name;
        this.descrip = descrip;
        this.buy_price = buy_price;
        this.qty = qty;
        this.total_harga = total_harga;
        this.terima = terima;
        this.btn_edit = btn_edit;
        this.btn_delete = btn_delete;
    }

    public RecPOModel() {
    }

    public JFXButton getBtn_edit() {
        return btn_edit;
    }

    public void setBtn_edit(JFXButton btn_edit) {
        this.btn_edit = btn_edit;
    }

    public String getTerima() {
        return terima;
    }

    public void setTerima(String terima) {
        this.terima = terima;
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

    public JFXButton getBtn_delete() {
        return btn_delete;
    }

    public void setBtn_delete(JFXButton btn_delete) {
        this.btn_delete = btn_delete;
    }
    
}
