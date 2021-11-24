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
public enum Item {
    TABLENAME("item"),
    ITEMCODE("item_code"),
    ITEMNAME("item_name"),
    IDKATEGORI("id_kategori"),
    IDSUBKATEGORI("id_sub_kategori"),
    IDSUPPLIER("id_supplier"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),
    FLAG("flag"),
    KET("ket");

    private final String mValue;

    private Item(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
