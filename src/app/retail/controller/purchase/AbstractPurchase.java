/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.purchase;

import app.retail.controller.general.General;
import static app.retail.controller.general.General.selectFromDatabase;
import app.retail.utility.ITEM;
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
public abstract class AbstractPurchase extends General{
    public static HashMap<String, ITEM> mMapItem;
    
    public abstract void loadData()throws SQLException;
    public abstract void simpan() throws SQLException;
    
    public static HashMap<String, String> MAP_SUPPLIER;
    
    public List<String> getSupplier() throws SQLException {
        List<String> tMap = new ArrayList<>();
        String tQuery = "SELECT id,nama FROM supplier WHERE id IN (SELECT id_supplier FROM purchase) ORDER BY nama ASC";
        ResultSet tRest = selectFromDatabase(tQuery);
        MAP_SUPPLIER = new HashMap<>();
        while (tRest.next()) {
            MAP_SUPPLIER.put(tRest.getString("nama"), tRest.getString("id"));
            tMap.add(tRest.getString("nama"));
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
