/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.master;

import com.jfoenix.controls.JFXButton;

/**
 *
 * @author Lutfi
 */
public class SubKategoriModel {
    private String id, sub_kategori, idkat,kategori,no;
    private JFXButton btnedit,btndelete;

    public SubKategoriModel(String id, String sub_kategori, String idkat, String kategori, String no, JFXButton btnedit, JFXButton btndelete) {
        this.id = id;
        this.sub_kategori = sub_kategori;
        this.idkat = idkat;
        this.kategori = kategori;
        this.no = no;
        this.btnedit = btnedit;
        this.btndelete = btndelete;
    }

    public SubKategoriModel() {
    }

    public String getSub_kategori() {
        return sub_kategori;
    }

    public void setSub_kategori(String sub_kategori) {
        this.sub_kategori = sub_kategori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdkat() {
        return idkat;
    }

    public void setIdkat(String idkat) {
        this.idkat = idkat;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
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
