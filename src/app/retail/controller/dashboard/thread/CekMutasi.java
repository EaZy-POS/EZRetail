///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package app.retail.controller.dashboard.thread;
//
//import systemlog.SystemLog;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.UUID;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import qs.QS;
//
///**
// *
// * @author Lutfi
// */
//public class CekMutasi extends Thread{
//    private static HashMap<Integer,String> MapItem = new HashMap<>();
//    private static final String[] cTipeMutasi = new String[]{"STOCK AWAL","PURCHASE","SALE","STOCK OUT","STOCK OPNAME","STOCK AKHIR"};
//    
//    @Override
//    public void run() {
//        try {
//            SystemLog.LOG.writeLogThread(getClass().getName(), ""+getId());
//            MapItem = getMapItem();
//            String tDateAwal = getDateAwal();
//            LocalDate tDtFrom = LocalDate.parse(tDateAwal);
//            String tDateAkhir = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//            LocalDate tDtTo = LocalDate.parse(tDateAkhir);
//            boolean next = true;
//            while (next) {
//                for (String kode : MapItem.values()) {
//                    cekInTableMutation(kode, tDtFrom.toString());
//                }
//                tDtFrom=tDtFrom.plusDays(1);
//                if (tDtFrom.isAfter(tDtTo)) {
//                    next=false;
//                }
//            }
//            SystemLog.LOG.writeLogAfterThread(getClass().getName(), ""+getId());
//        } catch (SQLException ex) {
//            Logger.getLogger(CekMutasi.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    private HashMap<Integer,String> getMapItem() throws SQLException{
//        HashMap<Integer,String> tMap = new HashMap<>();
//        int x =0;
//        String tSQL = getQueryItem();
//        ResultSet tResult = new QS().sendQuery(tSQL);
//        while (tResult.next()) {            
//            tMap.put(x, tResult.getString("item_code"));
//            x++;
//        }
//        return tMap;
//    }
//    
//    private String getQueryItem(){
//        String tSQL = "SELECT item_code FROM tbl_item";
//        return tSQL;
//    }
//    
//    private String getQueryMutation(String kode, String date, String tipe){
//        String tSQL = "SELECT * FROM tbl_mutasi_stock WHERE tran_date='"+date+"' AND kode_item='"+kode+"' AND tipe_mutasi='"+tipe+"'";
//        return tSQL;
//    }
//    
//    private String getQueryStock(String kode){
//        String tSQL = "SELECT stock FROM tbl_item_stock WHERE kode_item='"+kode+"'";
//        return tSQL;
//    }
//    
//    private String getQueryDate(){
//        String tSQL = "SELECT tran_date FROM tbl_mutasi_stock GROUP BY tran_date ORDER BY tran_date ASC LIMIT 1";
//        return tSQL;
//    }
//    
//    private void cekInTableMutation(String kode, String date) throws SQLException{
//        String tSQL;
//        for (String tipe : cTipeMutasi) {
//            tSQL = getQueryMutation(kode, date, tipe);
//            ResultSet tResult = new QS().sendQuery(tSQL);
//            if (!tResult.next()) {
//                insertToTableMutasi(kode, date, tipe);
//            }
//        }
//    }
//    
//    private void insertToTableMutasi(String kode,String date,String tipe) throws SQLException{
//        HashMap<String,String> tMapInsert = new HashMap<>();
//        String id = UUID.randomUUID().toString();
//        tMapInsert.put("id", id);
//        tMapInsert.put("tran_date", date);
//        tMapInsert.put("kode_item", kode);
//        tMapInsert.put("tipe_mutasi", tipe);
//        int qty = 0;
//        String tDate = new SimpleDateFormat("yyy-MM-dd").format(new Date());
//        if ((tipe.equalsIgnoreCase("STOCK AWAL") || tipe.equalsIgnoreCase("STOCK AKHIR")) & date.equalsIgnoreCase(tDate)) {
//            qty = getStock(kode);
//        }
//        tMapInsert.put("qty", ""+qty);
//        new QS().sendQueryInsert(tMapInsert, "tbl_mutasi_stock");
//        
//    }
//    
//    private int getStock(String kode) throws SQLException{
//        String tSQL = getQueryStock(kode);
//        ResultSet tResult = new QS().sendQuery(tSQL);
//        if (tResult.next()) {
//            return tResult.getInt("stock");
//        }
//        return 0;
//    }
//    
//    private String getDateAwal() throws SQLException{
//        String tSQL = getQueryDate();
//        SimpleDateFormat tFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date tDate;
//        ResultSet tResult = new QS().sendQuery(tSQL);
//        if (tResult.next()) {
//            tDate = tResult.getDate("tran_date");
//        }else{
//            tDate = new Date();
//        }
//        return tFormat.format(tDate);
//    }
//}
