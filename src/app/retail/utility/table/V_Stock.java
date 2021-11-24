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
public enum V_Stock {
    TABLENAME("v_stock"),
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
    STOK("STOK"),;

    private final String mValue;

    private V_Stock(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
