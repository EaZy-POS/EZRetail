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
public enum Item_Detail {
    TABLENAME("item_detail"),
    ID("id"),
    BAARCODE("baarcode"),
    KODE("kode"),
    KODEITEM("kode_item"),
    HARGABELI("harga_beli"),
    HARGAJUAL("harga_jual"),
    IDSATUAN("id_satuan"),
    MINIMAL("minimal"),
    KE("ke"),
    KONVERSI("konversi"),
    HPP("hpp"),
    PPN("ppn"),
    DISC("disc"),
    MARGIN("margin"),
    MARKUP("markup"),
    FLAG("flag");

    private final String mValue;

    private Item_Detail(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
