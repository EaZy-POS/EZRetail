/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard;

import app.retail.controller.general.General;
import app.retail.model.dashboard.PurchaseModel;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_Purchase_Month;
import app.retail.utility.table.V_Purchase_Year;
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
public class PurchControler extends General{
    private static HashMap<String, PurchaseModel> MAPPURCHASE = new HashMap<>();
    private static HashMap<String, PurchaseModel> MAPPURCHASEMONTH = new HashMap<>();

    public PurchControler(){
        try {
            setDataToMapPurchase();
            setDataToMapPurchaseMoth();
        } catch (SQLException e) {
            loggingerror(e);
        }
    }
    
    @Override
    public final void loggingerror(Exception ex){
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }

    public static HashMap<String, PurchaseModel> getMAPPURCHASEMONTH() {
        return MAPPURCHASEMONTH;
    }

    public static void setMAPPURCHASEMONTH(HashMap<String, PurchaseModel> MAPPURCHASEMONTH) {
        PurchControler.MAPPURCHASEMONTH = MAPPURCHASEMONTH;
    }
    
    public static HashMap<String, PurchaseModel> getMAPPURCHASE() {
        return MAPPURCHASE;
    }

    public static void setMAPPURCHASE(HashMap<String, PurchaseModel> MAPPURCHASE) {
        PurchControler.MAPPURCHASE = MAPPURCHASE;
    }
    
    private void setDataToMapPurchase() throws SQLException{
        String[] tSQL = new String[]{
            "SELECT * FROM "+V_Purchase_Year.TABLENAME.get()+" WHERE YEAR("+V_Purchase_Year.TRANDATE.get()+")='"+EZDate.YEAR.today()+"'",
        };
        MAPPURCHASE.clear();
        for (String string : tSQL) {
            ResultSet tResultSet = selectFromDatabase(string);
            while (tResultSet.next()) {                
                String tKey = tResultSet.getString(V_Purchase_Year.BLN.get());
                int bln = tResultSet.getInt(V_Purchase_Year.BLN.get());
                double total = tResultSet.getDouble(V_Purchase_Year.TOTAL.get());
                if (MAPPURCHASE.containsKey(tKey)) {
                    PurchaseModel model= MAPPURCHASE.get(tKey);
                    double tot = model.getTotal();
                    model.setTotal(total+tot);
                    MAPPURCHASE.put(tKey, model);
                }else{
                    PurchaseModel model = new PurchaseModel(bln, total);
                    MAPPURCHASE.put(tKey, model);
                }
            }
        }
    }
    
    private void setDataToMapPurchaseMoth() throws SQLException{
        String[] tSQL = new String[]{
            "SELECT * FROM "+V_Purchase_Month.TABLENAME.get()+" WHERE YEAR("+V_Purchase_Month.TRANDATE.get()+")='"+EZDate.YEAR.today()+"' AND MONTH(tran_date)='"+EZDate.MONTH.today()+"';",
        };
        MAPPURCHASEMONTH.clear();
        for (String string : tSQL) {
            ResultSet tResultSet = selectFromDatabase(string);
            while (tResultSet.next()) {                
                String tKey = tResultSet.getString(V_Purchase_Month.TGL.get());
                int tgl = tResultSet.getInt(V_Purchase_Month.TGL.get());
                double total = tResultSet.getDouble(V_Purchase_Month.TOTAL.get());
                if (MAPPURCHASEMONTH.containsKey(tKey)) {
                    PurchaseModel model= MAPPURCHASEMONTH.get(tKey);
                    double tot = model.getTotalmonth();
                    model.setTotalmonth(total+tot);
                    MAPPURCHASEMONTH.put(tKey, model);
                }else{
                    PurchaseModel model = new PurchaseModel(0, tgl, total);
                    MAPPURCHASEMONTH.put(tKey, model);
                }
            }
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
