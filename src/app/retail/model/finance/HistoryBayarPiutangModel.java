/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.finance;

/**
 *
 * @author Lutfi
 */
public class HistoryBayarPiutangModel {
    private String no,tanggal,bukti,total;

    public HistoryBayarPiutangModel(String no, String tanggal, String bukti, String total) {
        this.no = no;
        this.tanggal = tanggal;
        this.bukti = bukti;
        this.total = total;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
}
