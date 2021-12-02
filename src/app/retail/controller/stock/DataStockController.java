/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import app.retail.model.stock.StockModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_Stock;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class DataStockController extends AbstractStock implements Initializable {

    ObservableList<String> limitList = FXCollections.observableArrayList("100","200","300","400","500","All");
    ObservableList<String> paramList = FXCollections.observableArrayList("Search By","Nama","Barcode","Sku");
    ObservableList<String> supplierList = FXCollections.observableArrayList();
    ObservableList<String> kategoriList = FXCollections.observableArrayList();
    ObservableList<StockModel> stockList = FXCollections.observableArrayList();
    private final String[] cListParam = new String[]{"nama_item","barcode","sku"};
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<StockModel> tbl_purchase;
    @FXML
    private JFXComboBox<String> cmb_limit;
    @FXML
    private JFXComboBox<String> cmb_supplier;
    @FXML
    private JFXComboBox<String> cmb_categori;
    @FXML
    private JFXComboBox<String> cmb_param;
    @FXML
    private JFXTextField txt_search;
    @FXML
    private TableColumn<StockModel, String> clm_sku;
    @FXML
    private TableColumn<StockModel, String> clm_barcode;
    @FXML
    private TableColumn<StockModel, String> clm_harga;
    @FXML
    private TableColumn<StockModel, String> clm_no;
    @FXML
    private Button btn_search;
    @FXML
    private Button btn_print;
    @FXML
    private TableColumn<StockModel, String> clm_sup;
    @FXML
    private TableColumn<StockModel, String> clm_kat;
    @FXML
    private TableColumn<StockModel, String> clm_stok;
    @FXML
    private TableColumn<StockModel, String> clm_nam_item;
    @FXML
    private TableColumn<StockModel, String> clm_sat;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!isAllowed(Akses_List.PRINT_DATA_STOCK.get())) {
            btn_print.setDisable(true);
        }
        setToolTip();
        loadSupplier();
        loadKategori();
        setButtonListener();
        initializeState();
    }    

    @Override
    public void loadData() throws SQLException{
        if(isAllowed(Akses_List.DATA_STOCK)){
            String tSQL = getQuery();
            ResultSet tResultSet = searchBy(tSQL);
            if (tResultSet!=null) {
                try {
                    int no = 0;
                    tbl_purchase.getItems().clear();
                    while (tResultSet.next()) {
                        no++;
                        String tKode = tResultSet.getString(V_Stock.ITEMCODE.get());
                        String tNama = tResultSet.getString(V_Stock.ITEMNAME.get());
                        String tSku = tResultSet.getString(V_Stock.KODE.get());
                        String tBarcode = tResultSet.getString(V_Stock.BARCODE.get());
                        String tHarga = formatRupiah(tResultSet.getDouble(V_Stock.HRGBELI.get()));
                        String tSat = tResultSet.getString(V_Stock.SATUAN.get());
                        String tSup = tResultSet.getString(V_Stock.SUPNAME.get());
                        String tKat = tResultSet.getString(V_Stock.KAT.get());
                        String tStock ;
                        if (tResultSet.getString(V_Stock.STOK.get()) == null) {
                            tStock = "0";
                        }else{
                            tStock = tResultSet.getString(V_Stock.STOK.get());
                        }
                        StockModel tModel = new StockModel(""+no, tBarcode, tSku, tNama, tSup, tKat, tSat, tHarga, tStock);
                        stockList.add(tModel);
                        setValueColum();
                        tbl_purchase.setItems(stockList);
                    }

                } catch (SQLException ex) {
                    loggingerror(ex);
                }
            }
        }
    }
    
    @Override
    public void loggingerror(Exception ex){
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }
    
    private String getQuery(){
        String tSQL="SELECT * FROM "+V_Stock.TABLENAME.get();
        String tLimit ,tIdSup,tIdCat;
        
        if (cmb_supplier.getSelectionModel().getSelectedIndex()>0) {
            tIdSup = mMapSupplier.get(cmb_supplier.getSelectionModel().getSelectedItem());
            tSQL+=" WHERE "+V_Stock.SUPID.get()+"='"+tIdSup+"'";
            
            if (cmb_categori.getSelectionModel().getSelectedIndex()>0) {
                tIdCat=mMapKategori.get(cmb_categori.getSelectionModel().getSelectedItem());
                tSQL+=" AND "+V_Stock.KATID.get()+"='"+tIdCat+"'";
            }
            
            if(cmb_param.getSelectionModel().getSelectedIndex()>0){
                String tParam = cListParam[cmb_param.getSelectionModel().getSelectedIndex()-1];
                if (tParam.equalsIgnoreCase("nama_item")) {
                    tSQL+=" AND "+V_Stock.ITEMNAME.get()+" like'%"+txt_search.getText()+"%'";
                }else if(tParam.equalsIgnoreCase("barcode")){
                    tSQL+=" AND "+V_Stock.BARCODE.get()+" ='"+txt_search.getText()+"'";
                }else if(tParam.equalsIgnoreCase("sku")){
                    tSQL+=" AND "+V_Stock.KODE.get()+" ='"+txt_search.getText()+"'";
                }
            }
            
            tSQL+=" ORDER BY "+V_Stock.ITEMNAME.get()+" ASC ";
            
            if (!cmb_limit.getSelectionModel().getSelectedItem().equalsIgnoreCase("All")) {
                tLimit = cmb_limit.getSelectionModel().getSelectedItem();
                tSQL+=" LIMIT "+tLimit;
            }
            
            return tSQL; 
        }
        
        if (cmb_categori.getSelectionModel().getSelectedIndex()>0) {
            tIdCat=mMapKategori.get(cmb_categori.getSelectionModel().getSelectedItem());
            tSQL+=" WHERE "+V_Stock.KATID.get()+"='"+tIdCat+"'";
            
            if(cmb_param.getSelectionModel().getSelectedIndex()>0){
                String tParam = cListParam[cmb_param.getSelectionModel().getSelectedIndex()-1];
                if (tParam.equalsIgnoreCase("nama_item")) {
                    tSQL+=" AND "+V_Stock.ITEMNAME.get()+" like'%"+txt_search.getText()+"%'";
                }else if(tParam.equalsIgnoreCase("barcode")){
                    tSQL+=" AND "+V_Stock.BARCODE.get()+" ='"+txt_search.getText()+"'";
                }else if(tParam.equalsIgnoreCase("sku")){
                    tSQL+=" AND "+V_Stock.KODE.get()+" ='"+txt_search.getText()+"'";
                }
            }
            
            tSQL+=" ORDER BY "+V_Stock.ITEMNAME.get()+" ASC ";
            
            if (!cmb_limit.getSelectionModel().getSelectedItem().equalsIgnoreCase("All")) {
                tLimit = cmb_limit.getSelectionModel().getSelectedItem();
                tSQL+=" LIMIT "+tLimit;
            }
            
            return tSQL; 
        }
        
        if(cmb_param.getSelectionModel().getSelectedIndex()>0){
            String tParam = cListParam[cmb_param.getSelectionModel().getSelectedIndex()-1];
            
            if (tParam.contains("WHERE")) {
                tSQL += "AND";
            }else{
                tSQL += " WHERE ";
            }
            
            if (tParam.equalsIgnoreCase("nama_item")) {
                tSQL +=  V_Stock.ITEMNAME.get() + " like'%" + txt_search.getText() + "%'";
            } else if (tParam.equalsIgnoreCase("barcode")) {
                tSQL +=  V_Stock.BARCODE.get() + " ='" + txt_search.getText() + "'";
            } else if (tParam.equalsIgnoreCase("sku")) {
                tSQL +=  V_Stock.KODE.get() + " ='" + txt_search.getText() + "'";
            }
            
            return tSQL; 
        }
        
        tSQL+=" ORDER BY "+V_Stock.ITEMNAME.get()+" ASC ";
            
        if (!cmb_limit.getSelectionModel().getSelectedItem().equalsIgnoreCase("All")) {
            tLimit = cmb_limit.getSelectionModel().getSelectedItem();
            tSQL+=" LIMIT "+tLimit;
        }
        
        return tSQL;
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("NO"));
        clm_sku.setCellValueFactory(new PropertyValueFactory<>(V_Stock.KODE.get()));
        clm_nam_item.setCellValueFactory(new PropertyValueFactory<>(V_Stock.ITEMNAME.get()));
        clm_barcode.setCellValueFactory(new PropertyValueFactory<>(V_Stock.BARCODE.get()));
        clm_harga.setCellValueFactory(new PropertyValueFactory<>(V_Stock.HRGBELI.get()));
        clm_kat.setCellValueFactory(new PropertyValueFactory<>(V_Stock.KAT.get()));
        clm_sup.setCellValueFactory(new PropertyValueFactory<>(V_Stock.SUPNAME.get()));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>(V_Stock.SATUAN.get()));
        clm_stok.setCellValueFactory(new PropertyValueFactory<>(V_Stock.STOK.get()));
    }

    public ResultSet searchBy(String tSQL) throws SQLException {
        ResultSet tResult = selectFromDatabase(tSQL);
        return tResult;
    }

    public void loadSupplier() {
        List<String> tMap = getSupplier();
        supplierList.add("-- Supplier --");
        for (int i = 0; i < tMap.size(); i++) {
            String valString = tMap.get(i);
            supplierList.add(valString);
        }
        cmb_supplier.getItems().clear();
        cmb_supplier.setItems(supplierList);
    }
    
    public void loadKategori() {
        List<String> tMap  = getKategori();
        kategoriList.add("Semua");
        for (int i = 0; i < tMap.size(); i++) {
            String valString = tMap.get(i);
            kategoriList.add(valString);
        }
        cmb_categori.getItems().clear();
        cmb_categori.setItems(kategoriList);
    }

    @Override
    public void initializeState() {
        try {
            cmb_limit.setItems(limitList);
            cmb_param.setItems(paramList);
            cmb_limit.getSelectionModel().select(0);
            cmb_param.getSelectionModel().select(0);
            cmb_supplier.getSelectionModel().select(0);
            cmb_categori.getSelectionModel().select(0);
            loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {
        btn_search.setOnAction((ActionEvent event) -> {
            try {
                loadData();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });

        txt_search.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                btn_search.fire();
            }

        });

        cmb_categori.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
                txt_search.setText("");
                txt_search.requestFocus();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });

        cmb_param.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txt_search.setText("");
            txt_search.requestFocus();
        });

        cmb_supplier.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
                txt_search.setText("");
                txt_search.requestFocus();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });

        cmb_limit.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
                txt_search.setText("");
                txt_search.requestFocus();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });
        
        btn_print.setOnAction((ActionEvent event) -> {
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak data tabel ?", EZIcon.ICON_APPS);
            if(opt.get() == ButtonType.OK){
                if(tbl_purchase.getItems().size() > 0){
                    showReport("report/stock/lap_stock.jrxml", getQuery());
                }else{
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Data tidak ada!", EZIcon.ICON_APPS);
                }
            }
        });
        
        Platform.runLater(()->{
            txt_search.requestFocus();
        });
        
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth()-250);
        AnchorPane.setPrefHeight(getHeight()- 355);
        btn_print.setCursor(Cursor.HAND);
        btn_search.setCursor(Cursor.HAND);
        btn_print.setTooltip(new Tooltip("print"));
        btn_search.setTooltip(new Tooltip("cari"));;
        setColumnWidth();
    }
    
    private void setColumnWidth(){
        tbl_purchase.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setColumnWithProperty(tbl_purchase, clm_no, 0.05, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_sku, 0.15, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_barcode, 0.15, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_nam_item, 0.25, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_sat, 0.05, true, Pos.CENTER);
        setColumnWithProperty(tbl_purchase, clm_sup, 0.10, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_kat, 0.10, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_harga, 0.09, true, Pos.CENTER_RIGHT);
        setColumnWithProperty(tbl_purchase, clm_stok, 0.05, true, Pos.CENTER_RIGHT);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
