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
public enum V_Sale_Detail {
    TABLENAME("v_sale_detail"),
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
    KODETRANS("KODETRANS"),
    QTY("QTY"),
    SISA("SISA"),
    HARGA("HARGA"),
    DISC("DISC"),
    TOTAL("TOTAL"),
    AUTOID("AUTOID");

    private final String mValue;

    private V_Sale_Detail(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
