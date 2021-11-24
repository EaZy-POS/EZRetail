/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.utility;

import com.jfoenix.controls.JFXButton;

/**
 *
 * @author Lutfi
 */
public class UserManagementModel {
    private String no,id,userId,kdKaryawan,nama,kdAkses,tipeAkses,password, initial;
    private JFXButton btnedit,btndelete,btnchange;

    public UserManagementModel(String no, String id, String userId, String kdKaryawan, String nama, String kdAkses, String tipeAkses, String password, String initial, JFXButton btnedit, JFXButton btndelete, JFXButton btnchange) {
        this.no = no;
        this.id = id;
        this.userId = userId;
        this.kdKaryawan = kdKaryawan;
        this.nama = nama;
        this.tipeAkses = tipeAkses;
        this.btnedit = btnedit;
        this.btndelete = btndelete;
        this.password = password;
        this.kdAkses = kdAkses;
        this.btnchange= btnchange;
        this.initial= initial;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public JFXButton getBtnchange() {
        return btnchange;
    }

    public void setBtnchange(JFXButton btnchange) {
        this.btnchange = btnchange;
    }

    public String getKdAkses() {
        return kdAkses;
    }

    public void setKdAkses(String kdAkses) {
        this.kdAkses = kdAkses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKdKaryawan() {
        return kdKaryawan;
    }

    public void setKdKaryawan(String kdKaryawan) {
        this.kdKaryawan = kdKaryawan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTipeAkses() {
        return tipeAkses;
    }

    public void setTipeAkses(String tipeAkses) {
        this.tipeAkses = tipeAkses;
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
