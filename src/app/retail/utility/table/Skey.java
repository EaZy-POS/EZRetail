/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility.table;

/**
 *
 * @author RCS
 */
public enum Skey {
    TABLENAME("skey"),
    ID("id"),
    IDUSER("id_user"),
    KEY("`key`"),;

    private final String mValue;

    private Skey(String val) {
        this.mValue = val;
    }

    public String get(){
        return mValue;
    }
}
