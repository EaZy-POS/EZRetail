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
public enum Sub_Kategori {
    TABLENAME("sub_kategori"),
    ID("id"),
    IDKATEGORI("id_kategori"),
    SUBKATEGORI("sub_kategori"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Sub_Kategori(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
