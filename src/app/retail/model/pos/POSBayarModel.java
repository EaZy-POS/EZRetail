/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.pos;

import com.jfoenix.controls.JFXButton;

/**
 *
 * @author RCS
 */
public class POSBayarModel {
    private String type_bayar, total_bayar, ket;
    private JFXButton btn_delete;

    public POSBayarModel(String type_bayar, String total_bayar, String ket, JFXButton btn_delete) {
        this.type_bayar = type_bayar;
        this.total_bayar = total_bayar;
        this.btn_delete = btn_delete;
        this.ket = ket;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public String getType_bayar() {
        return type_bayar;
    }

    public void setType_bayar(String type_bayar) {
        this.type_bayar = type_bayar;
    }

    public String getTotal_bayar() {
        return total_bayar;
    }

    public void setTotal_bayar(String total_bayar) {
        this.total_bayar = total_bayar;
    }

    public JFXButton getBtn_delete() {
        return btn_delete;
    }

    public void setBtn_delete(JFXButton btn_delete) {
        this.btn_delete = btn_delete;
    }
    
}
