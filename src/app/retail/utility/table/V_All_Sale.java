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
public enum V_All_Sale {
    TABLENAME("v_all_sale"),
    KODETRANS("KODETRANS"),
    TRANDATE("TRANDATE"),
    IDPELANGGAN("IDPELANGGAN"),
    TOTAL("TOTAL"),
    DISC("DISC"),
    DISCAMOUNT("DISCAMOUNT"),
    GRANDTOTAL("GRANDTOTAL"),
    KET("KET"),
    IDKARYAWAN("IDKARYAWAN"),
    NAMAPELANGGAN("NAMAPELANGGAN"),
    KARYAWAN("KARYAWAN"),
    SHIFT("SHIFT"),
    AUTOID("AUTOID"),
    ITEMCODE("ITEMCODE"),
    IDITEM("IDITEM"),
    BARCODE("BARCODE"),
    KODE("KODE"),
    ITEMNAME("ITEMNAME"),
    QTY("QTY"),
    HARGA("HARGA"),
    DISCITEM("DISC_ITEM"),
    TOTALHARGA("TOTALHARGA"),
    SATUAN("SATUAN"),
    NNAME("NNAME"),;

    private final String mValue;

    private V_All_Sale(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
