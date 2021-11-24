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
public enum Profile {
    TABLENAME("profile"),
    ID("id"),
    NAMA("nama"),
    ALAMAT("alamat"),
    KOTA("kota"),
    TELP("telp"),
    FAX("fax"),
    LOGO("logo"),;

    private final String mValue;

    private Profile(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
