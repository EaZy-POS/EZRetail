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
public enum V_Mutasi_Stock {
    TABLENAME("v_mutasi_stock"),
    TGL("TGL"),
    IDITEM("IDITEM"),
    ITEMCODE("ITEMCODE"),
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
    STOCKAWAL("STOCK_AWAL"),
    PURCHASE("PURCHASE"),
    STOCKOPNAME("STOCK_OPNAME"),
    STOCKOUT("STOCK_OUT"),
    SALE("SALE"),
    STOCKAKIR("STOCKAKIR"),
    REFUND("REFUND"),
    KONV("KONV"),
    INVERSI("INVERSI");

    private final String mValue;

    private V_Mutasi_Stock(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
