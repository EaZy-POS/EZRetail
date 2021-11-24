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
public class PurchaseModel {
    private String NO,REFNUM,TGL,FAKTUR,SUPPLIER,TOTAL,TYPEBAYAR,DITERIMA,FLAG,JNS,PO;
    private JFXButton btn_edit,btn_delete,btn_view;

    public PurchaseModel(String NO, String REFNUM, String TGL, String FAKTUR, String SUPPLIER, String FLAG, String JNS, String PO,JFXButton btn_edit, JFXButton btn_delete, JFXButton btn_view) {
        this.NO = NO;
        this.REFNUM = REFNUM;
        this.TGL = TGL;
        this.FAKTUR = FAKTUR;
        this.SUPPLIER = SUPPLIER;
        this.FLAG = FLAG;
        this.JNS = JNS;
        this.PO = PO;
        this.btn_edit = btn_edit;
        this.btn_delete = btn_delete;
        this.btn_view = btn_view;
    }

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getREFNUM() {
        return REFNUM;
    }

    public void setREFNUM(String REFNUM) {
        this.REFNUM = REFNUM;
    }

    public String getTGL() {
        return TGL;
    }

    public void setTGL(String TGL) {
        this.TGL = TGL;
    }

    public String getFAKTUR() {
        return FAKTUR;
    }

    public void setFAKTUR(String FAKTUR) {
        this.FAKTUR = FAKTUR;
    }

    public String getSUPPLIER() {
        return SUPPLIER;
    }

    public void setSUPPLIER(String SUPPLIER) {
        this.SUPPLIER = SUPPLIER;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(String TOTAL) {
        this.TOTAL = TOTAL;
    }

    public String getTYPEBAYAR() {
        return TYPEBAYAR;
    }

    public void setTYPEBAYAR(String TYPEBAYAR) {
        this.TYPEBAYAR = TYPEBAYAR;
    }

    public String getDITERIMA() {
        return DITERIMA;
    }

    public void setDITERIMA(String DITERIMA) {
        this.DITERIMA = DITERIMA;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getJNS() {
        return JNS;
    }

    public void setJNS(String JNS) {
        this.JNS = JNS;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
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

    public JFXButton getBtn_view() {
        return btn_view;
    }

    public void setBtn_view(JFXButton btn_view) {
        this.btn_view = btn_view;
    }

}
