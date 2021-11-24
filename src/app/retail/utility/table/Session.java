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
public enum Session {
    TABLENAME("session"),
    SESSIONID("session_id"),
    USERID("user_id"),
    KDKARYAWAN("kd_karyawan"),
    SESSIONSTART("session_start"),
    SESSIONEND("session_end"),;

    private final String mValue;

    private Session(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
    
}
