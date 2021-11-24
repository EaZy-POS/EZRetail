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
public class CashTransactionModel {
    private String no,tran_date,type,input_by,total,ket,id;
    private String total_detail,faktur,deskrip;
    private JFXButton btnview,delete;
    private double totalTransaction;

    public CashTransactionModel(String no, String tran_date, String type, String input_by, String total, String ket, String id, JFXButton btnview) {
        this.no = no;
        this.tran_date = tran_date;
        this.type = type;
        this.input_by = input_by;
        this.total = total;
        this.ket = ket;
        this.id = id;
        this.btnview = btnview;
    }

    public CashTransactionModel(String no, String total_detail, String faktur, String deskrip, JFXButton delete) {
        this.no = no;
        this.total_detail = total_detail;
        this.faktur = faktur;
        this.deskrip = deskrip;
        this.delete = delete;
    }

    public CashTransactionModel(String type, double totalTransaction) {
        this.type = type;
        this.totalTransaction = totalTransaction;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInput_by() {
        return input_by;
    }

    public void setInput_by(String input_by) {
        this.input_by = input_by;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public JFXButton getBtnview() {
        return btnview;
    }

    public void setBtnview(JFXButton btnview) {
        this.btnview = btnview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal_detail() {
        return total_detail;
    }

    public void setTotal_detail(String total_detail) {
        this.total_detail = total_detail;
    }

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    public String getDeskrip() {
        return deskrip;
    }

    public void setDeskrip(String deskrip) {
        this.deskrip = deskrip;
    }

    public JFXButton getDelete() {
        return delete;
    }

    public void setDelete(JFXButton delete) {
        this.delete = delete;
    }

    public double getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(double totalTransaction) {
        this.totalTransaction = totalTransaction;
    }
    
}
