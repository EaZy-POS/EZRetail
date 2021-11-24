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
public enum V_Pelanggan {
    TABLENAME("v_pelanggan"),
    IDPELANGGAN("IDPELANGGAN"),
    PELANGGAN("PELANGGAN"),
    TELP("TELP"),
    EMAIL("EMAIL"),
    PEKERJAAN("PEKERJAAN"),
    TOTAL("TOTAL"),
    TERBAYAR("TERBAYAR"),
    SISA("SISA"),;

    private final String mValue;

    private V_Pelanggan(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
