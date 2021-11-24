/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard;

import app.retail.controller.general.General;
import static app.retail.controller.general.General.getStackTraceString;
import app.retail.model.finance.HutangModel;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_Hutang_By_Supplier;
import app.retail.utility.table.V_Hutang_By_Year;
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
public class HutangController extends General{
    private static HashMap<String,HutangModel> MAPHUTANGYEAR;
    private static HashMap<String,HutangModel> MAPHUTANGSUPPLIER;

    public HutangController() {
        try {
            setMapHutangYear();
            setMapHutangSupplier();
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
    
    public final HashMap<String,HutangModel> setMapHutangYear() throws SQLException{
        String tSQL = "SELECT bln,total FROM "+V_Hutang_By_Year.TABLENAME.get()+" WHERE YEAR("+V_Hutang_By_Year.TRANDATE.get()+")='"+EZDate.YEAR.today()+"'";
        ResultSet res = selectFromDatabase(tSQL);
        MAPHUTANGYEAR = new HashMap<>();
        while (res.next()) {            
            MAPHUTANGYEAR.put(res.getString(V_Hutang_By_Year.BLN.get()), 
                    new HutangModel(res.getString(V_Hutang_By_Year.BLN.get()),res.getDouble(V_Hutang_By_Year.TOTAL.get())));
        }
        return MAPHUTANGYEAR;
    }
    
    public final HashMap<String,HutangModel> setMapHutangSupplier() throws SQLException{
        String tSQL = "SELECT * FROM "+V_Hutang_By_Supplier.TABLENAME.get();
        ResultSet res = selectFromDatabase(tSQL);
        MAPHUTANGSUPPLIER = new HashMap<>();
        while (res.next()) {            
            MAPHUTANGSUPPLIER.put(res.getString(V_Hutang_By_Supplier.NAMA.get()), 
                    new HutangModel(res.getString(V_Hutang_By_Supplier.NAMA.get()), 
                            res.getDouble(V_Hutang_By_Supplier.HUTANG.get()), 
                            res.getDouble(V_Hutang_By_Supplier.TERBAYAR.get()), 
                            res.getDouble(V_Hutang_By_Supplier.SISA.get())));
        }
        return MAPHUTANGSUPPLIER;
    }

    public static HashMap<String, HutangModel> getMAPHUTANGYEAR() {
        return MAPHUTANGYEAR;
    }

    public static void setMAPHUTANGYEAR(HashMap<String, HutangModel> MAPHUTANGYEAR) {
        HutangController.MAPHUTANGYEAR = MAPHUTANGYEAR;
    }

    public static HashMap<String, HutangModel> getMAPHUTANGSUPPLIER() {
        return MAPHUTANGSUPPLIER;
    }

    public static void setMAPHUTANGSUPPLIER(HashMap<String, HutangModel> MAPHUTANGSUPPLIER) {
        HutangController.MAPHUTANGSUPPLIER = MAPHUTANGSUPPLIER;
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
