///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package app.retail.controller.dashboard.thread;
//
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import qs.QS;
//
///**
// *
// * @author Lutfi
// */
//public class InsertItemStock extends Thread{
//    private final String kodeItem;
//
//    public InsertItemStock(String kodeItem) {
//        this.kodeItem = kodeItem;
//    }
//
//    @Override
//    public synchronized void start() {
//        try {
//            insertItemStock(kodeItem);
//        } catch (SQLException ex) {
//            Logger.getLogger(InsertItemStock.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    private void insertItemStock(String kode) throws SQLException{
//        HashMap<String,String> tMap = new HashMap<>();
//        tMap.put("kode_item", kode);
//        tMap.put("stock", "0");
//        new QS().sendQueryInsert(tMap, "tbl_item_stock");
//    }
//    
//}
