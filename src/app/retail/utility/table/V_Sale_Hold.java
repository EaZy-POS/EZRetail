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
public enum V_Sale_Hold {
    TABLENAME("v_sale_hold"),
    KODETRANS("KODETRANS"),
    TRANDATE("TRANDATE"),
    IDPELANGGAN("IDPELANGGAN"),
    PELANGGAN("PELANGGAN"),
    DISC("DISC"),
    DISCAMOUNT("DISCAMOUNT"),
    TOTAL("TOTAL"),
    GRANDTOTAL("GRANDTOTAL"),;

    private final String mValue;

    private V_Sale_Hold(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
