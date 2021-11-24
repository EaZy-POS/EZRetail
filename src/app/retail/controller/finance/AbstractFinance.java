/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.finance;

import app.retail.controller.general.General;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Supplier;
import app.retail.utility.table.V_Piutang_By_Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author RCS
 */
public abstract class AbstractFinance extends General{
    public static HashMap<String, String> MAP_SUPPLIER;
    public static HashMap<String, String> MAP_PELANGGAN;
    public final String mTglAwal = "tgl_awal";
    public final String mTglAkhir = "tgl_akhir";
    public final String mLimit = "limit";
    public final String mPelanggan = "pelanggan";
    public final String mSupplier = "supplier";
    public final String mStatus = "status";
    
    public List<String> getSupplier() {
        List<String> tMap = new ArrayList<>();
        try {
            String tQuery = "SELECT "+Supplier.ID.get()+", "+Supplier.NAMA.get()
                    +" FROM "+Supplier.TABLENAME.get()+" ORDER BY "+Supplier.NAMA.get()+" ASC";
            ResultSet tRest = selectFromDatabase(tQuery);
            MAP_SUPPLIER = new HashMap<>();
            while (tRest.next()) {
                MAP_SUPPLIER.put(tRest.getString(Supplier.NAMA.get()), tRest.getString(Supplier.ID.get()));
                tMap.add(tRest.getString(Supplier.NAMA.get()));
            }
        } catch (SQLException ex) {
            EZSystem.showAllert(EZAlertType.ERROR, "ERROR: "+ex.getMessage(), EZIcon.ICON_STAGE);
        }
        return tMap;
    }
    
    public List<String> getPelanggan() {
        List<String> tMap = new ArrayList<>();
        try {
            String tQuery = "SELECT "+V_Piutang_By_Customer.IDPELANGGAN.get()+", "+V_Piutang_By_Customer.PELANGGAN.get()+" "
                    + "FROM "+V_Piutang_By_Customer.TABLENAME.get()+" ORDER BY "+V_Piutang_By_Customer.PELANGGAN.get()+" ASC";
            ResultSet tRest = selectFromDatabase(tQuery);
            MAP_PELANGGAN = new HashMap<>();
            while (tRest.next()) {
                MAP_PELANGGAN.put(tRest.getString(V_Piutang_By_Customer.PELANGGAN.get()), tRest.getString(V_Piutang_By_Customer.IDPELANGGAN.get()));
                tMap.add(tRest.getString(V_Piutang_By_Customer.PELANGGAN.get()));
            }
        } catch (SQLException ex) {
            EZSystem.showAllert(EZAlertType.ERROR, "ERROR: "+ex.getMessage(), EZIcon.ICON_STAGE);
        }
        return tMap;
    }

}
