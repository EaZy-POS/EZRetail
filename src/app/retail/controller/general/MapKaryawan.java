/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.general;

/**
 *
 * @author RCS
 */
public enum MapKaryawan {
    KODEKARYAWAN("kode"),
    NAMAKARYAWAN("nama"),
    INITIAL("initial"),
    SHIFT("shift"),
    KODESHIFT("kode_shift");
    
    private final String mValue;
    private MapKaryawan(String val){
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
