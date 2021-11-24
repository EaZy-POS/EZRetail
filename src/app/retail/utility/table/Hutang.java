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
public enum Hutang {
    TABLENAME("hutang"),
    ID("id"),
    TRANDATE("tran_date"),
    KODETRANS("kode_trans"),
    FAKTUR("faktur"),
    IDSUPPLIER("id_supplier"),
    TOTAL("total"),
    TERBAYAR("terbayar"),
    SISA("sisa"),
    FLAG("flag"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Hutang(String val) {
        this.mValue = val;
    }

}
