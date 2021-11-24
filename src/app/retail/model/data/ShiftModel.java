/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.data;

import com.jfoenix.controls.JFXButton;

/**
 *
 * @author Lutfi
 */
public class ShiftModel {
    private String no,kode_shift,shift,jam_awal,jam_akhir;
    private JFXButton btnedit,btndelete;

    public ShiftModel(String no, String kode_shift, String shift, String jam_awal, String jam_akhir, JFXButton btnedit, JFXButton btndelete) {
        this.no = no;
        this.kode_shift = kode_shift;
        this.shift = shift;
        this.jam_awal = jam_awal;
        this.jam_akhir = jam_akhir;
        this.btnedit = btnedit;
        this.btndelete = btndelete;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getKode_shift() {
        return kode_shift;
    }

    public void setKode_shift(String kode_shift) {
        this.kode_shift = kode_shift;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getJam_awal() {
        return jam_awal;
    }

    public void setJam_awal(String jam_awal) {
        this.jam_awal = jam_awal;
    }

    public String getJam_akhir() {
        return jam_akhir;
    }

    public void setJam_akhir(String jam_akhir) {
        this.jam_akhir = jam_akhir;
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
    
}
