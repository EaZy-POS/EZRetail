/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import app.retail.controller.general.General;
import static app.retail.controller.general.General.selectFromDatabase;
import app.retail.utility.ITEM;
import app.retail.utility.table.Kategori;
import app.retail.utility.table.V_All_Item;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author RCS
 */
public abstract class AbstractStock extends General{
    public static HashMap<String, String> mMapKategori;
    public static HashMap<String, String> mMapSupplier;
    public static HashMap<String, String> mMapSubKategori;
    public static HashMap<String, ITEM> mMapItem;
    
    public abstract void loadData() throws SQLException;
    
    public List<String> getSupplier() {
        List<String> tMap = new ArrayList<>();
        try {
            String tQuery = "SELECT * FROM supplier ORDER BY nama ASC";
            ResultSet tRest = selectFromDatabase(tQuery);
            mMapSupplier = new HashMap<>();
            while (tRest.next()) {
                mMapSupplier.put(tRest.getString("nama"), tRest.getString("id"));
                tMap.add(tRest.getString("nama"));
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        return tMap;
    }
    
    public List<String> getKategori() {
        List<String> tMap = new ArrayList<>();
        try {
            String tQuery = "SELECT * FROM "+Kategori.TABLENAME.get()+" ORDER BY "+Kategori.KATEGORI.get()+" ASC";
            ResultSet tRest = selectFromDatabase(tQuery);
            mMapKategori = new HashMap<>();
            while (tRest.next()) {
                mMapKategori.put(tRest.getString(Kategori.KATEGORI.get()), tRest.getString(Kategori.ID.get()));
                tMap.add(tRest.getString(Kategori.KATEGORI.get()));
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        return tMap;
    }
    
    public List<String> getSubKategori(String pIdKat) {
        List<String> tMap = new ArrayList<>();
        try {
            String tQuery = "SELECT id,sub_kategori FROM sub_kategori WHERE id_kategori='" + pIdKat + "' ORDER BY sub_kategori ASC";
            ResultSet tRest = selectFromDatabase(tQuery);
            int no = 0;
            while (tRest.next()) {
                mMapSubKategori.put(tRest.getString("sub_kategori"), tRest.getString("id"));
                tMap.add(tRest.getString("sub_kategori"));
                no++;
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        return tMap;
    }
    
    public List<String> getListItem(String tQuery) throws SQLException{
        ResultSet tResult = selectFromDatabase(tQuery);
        List<String> tList = new ArrayList<>();
        mMapItem = new HashMap<>();
        while (tResult.next()) {
            String tKodeItem = tResult.getString(V_All_Item.ITEMCODE.get());
            String tSku = tResult.getString(V_All_Item.KODE.get());
            String tBarcode = tResult.getString(V_All_Item.BARCODE.get());
            String tIdItem = tResult.getString(V_All_Item.IDITEM.get());
            String tItemName = tResult.getString(V_All_Item.ITEMNAME.get());
            if(tItemName.length() > 30){
                tItemName = tItemName.substring(0, 30);
            }
            String tHargaBeli = tResult.getString(V_All_Item.HRGBELI.get());
            String tSatuan = tResult.getString(V_All_Item.SATUAN.get());
            tList.add(tItemName+" ("+tSatuan+")");
            ITEM item = new ITEM(tKodeItem, tIdItem, tBarcode, tSku, tItemName, tSatuan, tHargaBeli);
            mMapItem.put(tItemName+" ("+tSatuan+")", item);
        }
        return tList;
    }
}


