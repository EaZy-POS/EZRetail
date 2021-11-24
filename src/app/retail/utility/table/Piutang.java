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
public enum Piutang {
    TABLENAME("piutang"),
    ID("id"),
    FAKTUR("faktur"),
    TRANDATE("tran_date"),
    IDPELANGGAN("id_pelanggan"),
    TOTAL("total"),
    FLAG("flag"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Piutang(String val) {
        this.mValue = val;
    }

}
