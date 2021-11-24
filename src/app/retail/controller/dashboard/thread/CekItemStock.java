///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package app.retail.controller.dashboard.thread;
//
//import app.retail.controller.general.General;
//import static app.retail.controller.general.General.getStackTraceString;
//import app.retail.utility.EZIcon;
//import app.retail.utility.EZSystem;
//import co.id.eazy.system.log.LogType;
//import co.id.eazy.system.log.SystemLog;
//import java.io.IOException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javafx.event.ActionEvent;
//import javafx.scene.control.Alert;
//
///**
// *
// * @author Lutfi
// */
//public class CekItemStock extends General{
//    private static HashMap<Integer,String> MapItem = new HashMap<>();
//    private final CekMutasi mCekMutasi = new CekMutasi();
//    @Override
//    public void run() {
//        try {
//            SystemLog.getLogger().writeLog(LogType.THREAD, "Running Thread: "+getId() +" in "+getClass().getName());
//            MapItem = getMapItem();
//            MapItem.values().stream().forEach((kode) -> {
//                new InsertItemStock(kode).start();
//            });
//            SystemLog.getLogger().writeLog(LogType.THREAD, "After Running Thread: "+getId() +" in "+getClass().getName());
//            mCekMutasi.start();
//        } catch (SQLException ex) {
//            loggingerror(ex);
//        }
//    }
//    
//    @Override
//    public void loggingerror(Exception ex){
//        String tMessage = "ERROR: " + ex.getMessage();
//        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
//        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
//    }
//    
//    private HashMap<Integer,String> getMapItem() throws SQLException{
//        HashMap<Integer,String> tMap = new HashMap<>();
//        int x =0;
//        String tSQL = getQueryItemStock();
//        ResultSet tResult = new QS().sendQuery(tSQL);
//        while (tResult.next()) {            
//            tMap.put(x, tResult.getString("item_code"));
//            x++;
//        }
//        return tMap;
//    }
//    
//    private String getQueryItemStock(){
//        String tSQL = "SELECT item_code FROM tbl_item WHERE item_code NOT IN(SELECT kode_item FROM tbl_item_stock)";
//        return tSQL;
//    }
//
//    @Override
//    public void initializeState() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void setButtonListener() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void clear() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void setToolTip() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//}
