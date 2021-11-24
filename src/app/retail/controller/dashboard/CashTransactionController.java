/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard;

import app.retail.controller.general.General;
import app.retail.model.finance.CashTransactionModel;
import app.retail.utility.EZDate;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.event.ActionEvent;

/**
 *
 * @author Lutfi
 */
public final class CashTransactionController extends General{
    
    private static HashMap<String,CashTransactionModel> MAPCASHTRANSOUT;
    private static HashMap<String,CashTransactionModel> MAPCASHTRANIN;

    public CashTransactionController() {
        try {
            getMapCashTransIn();
            getMapCashTransOut();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }    
    
    public HashMap<String,CashTransactionModel> getMapCashTransOut() throws SQLException{
        String tSQL = "SELECT * FROM v_cash_transaction_out WHERE tahun='"+EZDate.YEAR.today()+"'";
        ResultSet res = selectFromDatabase(tSQL);
        MAPCASHTRANSOUT = new HashMap<>();
        while (res.next()) {            
            MAPCASHTRANSOUT.put(res.getString("bln"), new CashTransactionModel(res.getString("bln"), res.getDouble("total")));
        }
        return MAPCASHTRANSOUT;
    }
    
    public final HashMap<String,CashTransactionModel> getMapCashTransIn() throws SQLException{
        String tSQL = "SELECT * FROM v_cash_transaction_in WHERE tahun='"+EZDate.YEAR.today()+"'";
        ResultSet res = selectFromDatabase(tSQL);
        MAPCASHTRANIN = new HashMap<>();
        while (res.next()) {            
            MAPCASHTRANIN.put(res.getString("bln"), new CashTransactionModel(res.getString("bln"), res.getDouble("total")));
        }
        return MAPCASHTRANIN;
    }

    public static HashMap<String, CashTransactionModel> getMAPCASHTRANSOUT() {
        return MAPCASHTRANSOUT;
    }

    public static HashMap<String, CashTransactionModel> getMAPCASHTRANIN() {
        return MAPCASHTRANIN;
    }

    @Override
    public void initializeState() {
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
