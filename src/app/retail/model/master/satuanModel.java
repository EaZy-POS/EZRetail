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
public class satuanModel {
    private String no,id,descrip;
    private JFXButton btnedit,btndelete;

    public satuanModel(String no, String id, String descrip, JFXButton btnedit, JFXButton btndelete) {
        this.no = no;
        this.id = id;
        this.descrip = descrip;
        this.btnedit = btnedit;
        this.btndelete = btndelete;
    }
    
    public satuanModel(){
        
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

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
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
