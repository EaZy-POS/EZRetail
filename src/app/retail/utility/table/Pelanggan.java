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
public enum Pelanggan {
    TABLENAME("pelanggan"),
    IDPELANGGAN("id_pelanggan"),
    NAMAPELANGGAN("nama_pelanggan"),
    ALMTPELANGGAN("almt_pelanggan"),
    TELP("telp"),
    PEKERJAAN("pekerjaan"),
    EMAIL("email"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Pelanggan(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
