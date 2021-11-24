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
public class kategori_model {
    private String id,kategori,no;
    private JFXButton btnedit,btndelete;

    public kategori_model(String id, String kategori, String no, JFXButton btnedit, JFXButton btndelete) {
        this.id = id;
        this.kategori = kategori;
        this.no = no;
        this.btnedit = btnedit;
        this.btndelete = btndelete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
