/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.master;

import app.retail.controller.general.General;
import app.retail.controller.home.HomeController;
import app.retail.model.master.ItemModel;
import app.retail.utility.EZButtonType;
import app.retail.utility.table.Item_Detail;
import app.retail.utility.table.V_All_Item;
import com.jfoenix.controls.JFXButton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

/**
 *
 * @author Lutfi
 */
public class getItem extends General{
    JFXButton btnedit,btndelete , btnview;
    private final MasterItemController mMasterController;
    private final String mLimit;

    public getItem(MasterItemController mMasterController) {
        this.mMasterController = mMasterController;
        mLimit = "100";
    }

    public getItem(MasterItemController mMasterController, String mLimit) {
        this.mMasterController = mMasterController;
        this.mLimit = mLimit;
    }
    
    @Override
    public void run() {
        loadData();
    }
    
    private String getQuery(int type, String where){
        String tQuery = "SELECT * "
                + "FROM "+V_All_Item.TABLENAME.get();
        tQuery += " WHERE "+V_All_Item.TABLENAME.get()+"."+V_All_Item.FLAG.get()+"!=2 " ;
        if (type > 0) {
            tQuery+= " AND " + where;
        }
        
        if (!mLimit.equalsIgnoreCase("All")) {
            tQuery+=" ORDER BY "+V_All_Item.ITEMNAME.get()+" ASC LIMIT "+mLimit;
        }
        
        return tQuery;
    }
    
    public void loadData() {
        ObservableList<ItemModel> itemList = FXCollections.observableArrayList();
        String tQuery = "SELECT * FROM "+V_All_Item.TABLENAME.get()+" WHERE "+V_All_Item.FLAG.get()+"!=2" +" GROUP BY "+V_All_Item.ITEMCODE.get() +" ";
        
        if (!mLimit.equalsIgnoreCase("All")) {
            tQuery+=" ORDER BY "+V_All_Item.ITEMNAME.get()+" ASC LIMIT "+mLimit;
        }
        
        MasterItemController.setmQuery(getQuery(0, ""));
        try {
            ResultSet Reult = selectFromDatabase(tQuery);
            int no = 0;
            while (Reult.next()) {
                no++;
                String tItemCode = Reult.getString(V_All_Item.ITEMCODE.get());
                String tIDItem = Reult.getString(V_All_Item.IDITEM.get());
                String tBarcode = Reult.getString(V_All_Item.BARCODE.get());
                String tKode = Reult.getString(V_All_Item.KODE.get());
                String tItemName = Reult.getString(V_All_Item.ITEMNAME.get());
                String tSupID = Reult.getString(V_All_Item.SUPID.get());
                String ttSupName = Reult.getString(V_All_Item.SUPNAME.get());
                String tKatId = Reult.getString(V_All_Item.KATID.get());
                String tKat = Reult.getString(V_All_Item.KAT.get());
                String tSubKatid = Reult.getString(V_All_Item.SUBKATID.get());
                String tSubKat = Reult.getString(V_All_Item.SUBKAT.get());
                String tIdSat = Reult.getString(V_All_Item.IDSAT.get());
                String tSatuan = Reult.getString(V_All_Item.SATUAN.get());
                String tStockMin = Reult.getString(V_All_Item.STOKMIN.get());
                String tKonversi = Reult.getString(V_All_Item.KONVERSI.get());
                String tSatuanKe = Reult.getString(V_All_Item.SATUANKE.get());
                String tHargaBeli = formatRupiah(Reult.getDouble(V_All_Item.HRGBELI.get()));
                String tHargaJual = formatRupiah(Reult.getDouble(V_All_Item.HARGAJUAL.get()));
                String tFlag = Reult.getString(V_All_Item.FLAG.get());
                
                if(HomeController.isAllowed("Delete Data Item")){
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId(Reult.getString(V_All_Item.ITEMCODE.get()));
                    mMasterController.setButtonOnTableView(btndelete,EZButtonType.BTN_DELETE);
                }else{
                    btndelete = null;
                }
                
                btnview = getButton(EZButtonType.BTN_VIEW, "Detail");
                btnview.setId(Reult.getString(V_All_Item.ITEMCODE.get()));
                mMasterController.setButtonOnTableView(btnview,EZButtonType.BTN_VIEW);
                
                itemList.add(
                    new ItemModel(""+no,tItemCode, tIDItem, tBarcode, tKode, tItemName, tSupID, ttSupName, tKatId, tKat, tSubKatid, tSubKat, tIdSat, tSatuan, tStockMin, tKonversi, tSatuanKe, tHargaBeli, tHargaJual, tFlag, btnedit, btndelete, btnview)
                );
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        
        mMasterController.setItemList(itemList);
        mMasterController.loadData();
    }
    
    public void loadData(String where) {
        ObservableList<ItemModel> itemList = FXCollections.observableArrayList();
        String tQuery = "SELECT * FROM "+V_All_Item.TABLENAME.get()+" WHERE "+where +" AND "+V_All_Item.FLAG.get()+"!=2 GROUP BY "+V_All_Item.ITEMCODE.get() +" ";
        if (!mLimit.equalsIgnoreCase("All")) {
            tQuery+=" ORDER BY "+V_All_Item.ITEMNAME.get()+" ASC LIMIT "+mLimit;
        }
        MasterItemController.setmQuery(getQuery(1, where));
        try {
            ResultSet Reult = selectFromDatabase(tQuery);
            int no = 0;
            while (Reult.next()) {
                no++;
                String tItemCode = Reult.getString(V_All_Item.ITEMCODE.get());
                String tIDItem = Reult.getString(V_All_Item.IDITEM.get());
                String tBarcode = Reult.getString(V_All_Item.BARCODE.get());
                String tKode = Reult.getString(V_All_Item.KODE.get());
                String tItemName = Reult.getString(V_All_Item.ITEMNAME.get());
                String tSupID = Reult.getString(V_All_Item.SUPID.get());
                String ttSupName = Reult.getString(V_All_Item.SUPNAME.get());
                String tKatId = Reult.getString(V_All_Item.KATID.get());
                String tKat = Reult.getString(V_All_Item.KAT.get());
                String tSubKatid = Reult.getString(V_All_Item.SUBKATID.get());
                String tSubKat = Reult.getString(V_All_Item.SUBKAT.get());
                String tIdSat = Reult.getString(V_All_Item.IDSAT.get());
                String tSatuan = Reult.getString(V_All_Item.SATUAN.get());
                String tStockMin = Reult.getString(V_All_Item.STOKMIN.get());
                String tKonversi = Reult.getString(V_All_Item.KONVERSI.get());
                String tSatuanKe = Reult.getString(V_All_Item.SATUANKE.get());
                String tHargaBeli = formatRupiah(Reult.getDouble(V_All_Item.HRGBELI.get()));
                String tHargaJual = formatRupiah(Reult.getDouble(V_All_Item.HARGAJUAL.get()));
                String tFlag = Reult.getString(V_All_Item.FLAG.get());
                
                if(HomeController.isAllowed("Delete Data Item")){
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId(Reult.getString(V_All_Item.ITEMCODE.get()));
                    mMasterController.setButtonOnTableView(btndelete,EZButtonType.BTN_DELETE);
                }else{
                    btndelete = null;
                }
                
                btnview = getButton(EZButtonType.BTN_VIEW, "Detail");
                btnview.setId(Reult.getString(V_All_Item.ITEMCODE.get()));
                mMasterController.setButtonOnTableView(btnview,EZButtonType.BTN_VIEW);
                
                itemList.add(
                    new ItemModel(""+no,tItemCode, tIDItem, tBarcode, tKode, tItemName, tSupID, ttSupName, tKatId, tKat, tSubKatid, tSubKat, tIdSat, tSatuan, tStockMin, tKonversi, tSatuanKe, tHargaBeli, tHargaJual, tFlag, btnedit, btndelete, btnview)
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(MasterItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        mMasterController.setItemList(itemList);
        mMasterController.loadData();
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
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
