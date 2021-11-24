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
public class LisFakturModel {
    private String no, faktur, total;
    private JFXButton btnreprint;

    public LisFakturModel(String no, String faktur, String total, JFXButton btn) {
        this.no = no;
        this.faktur = faktur;
        this.total = total;
        this.btnreprint =btn;
    }

    public JFXButton getBtnreprint() {
        return btnreprint;
    }

    public void setBtnreprint(JFXButton btnreprint) {
        this.btnreprint = btnreprint;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
}
