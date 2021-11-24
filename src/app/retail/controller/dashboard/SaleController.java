/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard;

import app.retail.controller.general.General;
import app.retail.model.dashboard.SaleModell;
import app.retail.utility.EZDate;
import app.retail.utility.table.V_Item_terlaris;
import app.retail.utility.table.V_Sale_By_Month;
import app.retail.utility.table.V_Sale_By_Year;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import javafx.event.ActionEvent;

/**
 *
 * @author Lutfi
 */
public class SaleController extends General{
    private static HashMap<String,SaleModell> MAPSALEMODE = new HashMap<>();
    private static HashMap<String,SaleModell> MAPSALEMOUNTHMODE = new HashMap<>();
    private static HashMap<String,SaleModell> MAPITEMTERLARIS = new HashMap<>();

    public SaleController() throws SQLException {
        getDataToMap();
        getDataMountToMap();
        getDataTerlarisToMap();
    }

    public static HashMap<String, SaleModell> getMAPITEMTERLARIS() {
        return MAPITEMTERLARIS;
    }

    public static void setMAPITEMTERLARIS(HashMap<String, SaleModell> MAPITEMTERLARIS) {
        SaleController.MAPITEMTERLARIS = MAPITEMTERLARIS;
    }

    public static HashMap<String, SaleModell> getMAPSALEMOUNTHMODE() {
        return MAPSALEMOUNTHMODE;
    }

    public static void setMAPSALEMOUNTHMODE(HashMap<String, SaleModell> MAPSALEMOUNTHMODE) {
        SaleController.MAPSALEMOUNTHMODE = MAPSALEMOUNTHMODE;
    }
    

    public static HashMap<String, SaleModell> getMAPSALEMODE() {
        return MAPSALEMODE;
    }

    public static void setMAPSALEMODE(HashMap<String, SaleModell> MAPSALEMODE) {
        SaleController.MAPSALEMODE = MAPSALEMODE;
    }
    
    public final void getDataToMap() throws SQLException{
        String tSQL = "SELECT * FROM "+V_Sale_By_Year.TABLENAME.get()+" WHERE "+V_Sale_By_Year.TAHUN.get()+"='"+EZDate.YEAR.today()+"'";
        ResultSet tResult = selectFromDatabase(tSQL);
        MAPSALEMODE.clear();
        while (tResult.next()) {
            MAPSALEMODE.put(tResult.getString(V_Sale_By_Year.BLN.get()), new 
                SaleModell(tResult.getInt(V_Sale_By_Year.BLN.get()), tResult.getDouble(V_Sale_By_Year.TOTAL.get()))
            );
        }
    }
    
    public final void getDataMountToMap() throws SQLException{
        MAPSALEMOUNTHMODE.clear();
        String tSQL = "SELECT " + V_Sale_By_Month.TGL.get() + ", " + V_Sale_By_Month.TOTAL.get()
                + " FROM " + V_Sale_By_Month.TABLENAME.get()
                + " WHERE " + V_Sale_By_Month.TAHUN.get() + " ='" + EZDate.YEAR.today() + "'"
                + " AND " + V_Sale_By_Month.BULAN.get() + " ='" + EZDate.MONTH.today() + "'";
        ResultSet tResult = selectFromDatabase(tSQL);
        while (tResult.next()) {
            MAPSALEMOUNTHMODE.put(tResult.getString(V_Sale_By_Month.TGL.get()),
                    new SaleModell(tResult.getInt(V_Sale_By_Month.TGL.get()), 0, tResult.getDouble(V_Sale_By_Month.TOTAL.get())));
        }

    }
  
    private void getDataTerlarisToMap() throws SQLException{
        String tSQL = "SELECT * FROM "+V_Item_terlaris.TABLENAME.get();
        ResultSet tResultSet = selectFromDatabase(tSQL);
        MAPITEMTERLARIS.clear();
        while (tResultSet.next()) {            
            MAPITEMTERLARIS.put(
                tResultSet.getString(V_Item_terlaris.ITEMCODE.get()), 
                new SaleModell(tResultSet.getInt(V_Item_terlaris.QTY.get()),
                    tResultSet.getString(V_Item_terlaris.ITEMCODE.get()),
                    tResultSet.getString(V_Item_terlaris.ITEMNAME.get())
                )
            );
        }
    }

    @Override
    public void initializeState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setButtonListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setToolTip() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
