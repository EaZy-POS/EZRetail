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
public enum Karyawan {
    TABLENAME("karyawan"),
    KODEKARYAWAN("kode_karyawan"),
    IDKARYAWAN("id_karyawan"),
    NAMAKARYAWAN("nama_karyawan"),
    ALMTKARYAWAN("almt_karyawan"),
    TELP("telp"),
    JAB("jab"),
    EMAIL("email"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Karyawan(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
