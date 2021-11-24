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
public enum V_All_Item {
    TABLENAME("v_all_item"),
    ITEMCODE("ITEMCODE"),
    IDITEM("IDITEM"),
    BARCODE("BARCODE"),
    KODE("KODE"),
    ITEMNAME("ITEMNAME"),
    SUPID("SUPID"),
    SUPNAME("SUPNAME"),
    KATID("KATID"),
    KAT("KAT"),
    SUBKATID("SUBKATID"),
    SUBKAT("SUBKAT"),
    IDSAT("IDSAT"),
    SATUAN("SATUAN"),
    STOKMIN("STOKMIN"),
    KONVERSI("KONVERSI"),
    SATUANKE("SATUANKE"),
    HRGBELI("HRGBELI"),
    HARGAJUAL("HARGAJUAL"),
    FLAG("FLAG"),
    KET("KET"),
    ACTIVE("ACTIVE"),
    HPP("HPP"),
    PPN("PPN"),
    DISC("DISC"),
    MARGIN("MARGIN"),
    MARKUP("MARKUP"),;

    private final String mValue;

    private V_All_Item(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
