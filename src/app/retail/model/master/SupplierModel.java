/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.master;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;

/**
 *
 * @author Lutfi
 */
public class SupplierModel {
    private String id,nama,alamat,telp,fax,email,no;
    private JFXButton btnedit,btndelete;

    public SupplierModel(String no,String id, String nama, String alamat, String telp, String fax, String email, JFXButton btnEdit, JFXButton btnDelete) {
        this.no = no;
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.telp = telp;
        this.fax = fax;
        this.email = email;
        this.btnedit = btnEdit;
        this.btndelete = btnDelete;
        this.no = no;
    }
    
    public SupplierModel(){
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Button getBtnEdit() {
        return btnedit;
    }

    public void setBtnEdit(JFXButton btnEdit) {
        this.btnedit = btnEdit;
    }

    public Button getBtnDelete() {
        return btndelete;
    }

    public void setBtnDelete(JFXButton btnDelete) {
        this.btndelete = btnDelete;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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
