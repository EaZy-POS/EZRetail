/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.utility;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Lutfi
 */
public class AddUserAksesModel {
    private CheckBox modul;
    private String id;
    private String no, idAkses,typeAkses, akses;
    private JFXButton btnedit,btndelete;

    public AddUserAksesModel(CheckBox modul, String id) {
        this.modul = modul;
        this.id = id;
    }

    public AddUserAksesModel(String no, String idAkses, String typeAkses, String akses, JFXButton btnedit, JFXButton btndelete) {
        this.no = no;
        this.idAkses = idAkses;
        this.akses = akses;
        this.typeAkses = typeAkses;
        this.btnedit = btnedit;
        this.btndelete = btndelete;
    }

    public String getAkses() {
        return akses;
    }

    public void setAkses(String akses) {
        this.akses = akses;
    }
    
    public CheckBox getModul() {
        return modul;
    }

    public void setModul(CheckBox modul) {
        this.modul = modul;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getIdAkses() {
        return idAkses;
    }

    public void setIdAkses(String idAkses) {
        this.idAkses = idAkses;
    }

    public String getTypeAkses() {
        return typeAkses;
    }

    public void setTypeAkses(String typeAkses) {
        this.typeAkses = typeAkses;
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
