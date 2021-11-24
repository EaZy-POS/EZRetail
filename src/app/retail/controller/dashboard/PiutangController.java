/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard;

import app.retail.controller.general.General;
import static app.retail.controller.general.General.getStackTraceString;
import app.retail.model.finance.PiutangModel;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_Piutang_By_Customer;
import app.retail.utility.table.V_Piutang_By_Year;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

/**
 *
 * @author Lutfi
 */
public final class PiutangController extends General{
    private static HashMap<String, PiutangModel> MAPPIUTANGYEAR;
    private static HashMap<String, PiutangModel> MAPPIUTANGCUST;

    public PiutangController() {
        try {
            getMapPiutangPelanggan();
            getMapPiutangYear();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    public HashMap<String, PiutangModel> getMapPiutangYear() throws SQLException{
        String tSQL = "SELECT * "
                + " FROM "+V_Piutang_By_Year.TABLENAME.get()
                + " WHERE "+V_Piutang_By_Year.TAHUN.get()+"='"+EZDate.YEAR.today()+"'";
        ResultSet res = selectFromDatabase(tSQL);
        MAPPIUTANGYEAR = new HashMap<>();
        while (res.next()) {            
            MAPPIUTANGYEAR.put(res.getString(V_Piutang_By_Year.BLN.get()), 
                    new PiutangModel(res.getInt(V_Piutang_By_Year.BLN.get()), 
                            res.getDouble(V_Piutang_By_Year.TOTAL.get()))
            );
        }
        return MAPPIUTANGYEAR;
    }
    
    public HashMap<String, PiutangModel> getMapPiutangPelanggan() throws SQLException{
        String tSQL = "SELECT * FROM "+V_Piutang_By_Customer.TABLENAME.get();
        ResultSet res = selectFromDatabase(tSQL);
        MAPPIUTANGCUST = new HashMap<>();
        while (res.next()) {            
            MAPPIUTANGCUST.put(res.getString(V_Piutang_By_Customer.PELANGGAN.get()), 
                    new PiutangModel(res.getString(V_Piutang_By_Customer.PELANGGAN.get()), 
                            res.getDouble(V_Piutang_By_Customer.TOTAL.get()), 
                            res.getDouble(V_Piutang_By_Customer.TERBAYAR.get()), 
                            res.getDouble(V_Piutang_By_Customer.SISA.get())));
        }
        return MAPPIUTANGCUST;
    }
    
    @Override
    public final void loggingerror(Exception ex){
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }

    public static HashMap<String, PiutangModel> getMAPPIUTANGYEAR() {
        return MAPPIUTANGYEAR;
    }

    public static HashMap<String, PiutangModel> getMAPPIUTANGCUST() {
        return MAPPIUTANGCUST;
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
