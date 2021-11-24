/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import app.retail.controller.home.HomeController;
import app.retail.model.stock.MutasiStockModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZDate;
import app.retail.utility.table.V_Mutasi_Stock;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
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
public class DataMutasiStockController extends AbstractStock implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<MutasiStockModel> tbl_purchase;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_no;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_sku;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_barcode;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_nama;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_sat;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_supplier;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_kategori;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_tanggal;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_stock_awal;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_pembelian;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_penjualan;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_stock_akhir;
    @FXML
    private JFXComboBox<String> cmb_categori;
    @FXML
    private JFXComboBox<String> cmb_param;
    @FXML
    private JFXTextField txt_search;
    @FXML
    private Button btn_search;
    @FXML
    private JFXDatePicker dt_pick_1;
    @FXML
    private JFXDatePicker dt_pick_2;
    ObservableList<String> limitList = FXCollections.observableArrayList("100","200","300","400","500","All");
    ObservableList<String> paramList = FXCollections.observableArrayList("Semua","Kode item","Nama","Barcode","Sku");
    ObservableList<String> kategoriList = FXCollections.observableArrayList();
    ObservableList<MutasiStockModel> mutasistockList = FXCollections.observableArrayList();
    private static final List<String> mMapItemList = new ArrayList<>();
    @FXML
    private Button btn_print;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_stock_opname;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_stock_out;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_komversi;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_refund;
    @FXML
    private TableColumn<MutasiStockModel, String> clm_inversi;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!isAllowed(Akses_List.PRINT_DATA_MUTASI_STOCK)) {
            btn_print.setDisable(true);
        }
        setToolTip();
        loadSupplier();
        initializeState();
        setButtonListener();
    }    

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadData() {
        if(isAllowed(Akses_List.DATA_MUTASI_STOCK)){
            mMapItemList.clear();
            try {
                String tSQL = getQuery();
                ResultSet tResult = selectFromDatabase(tSQL);
                int no = 0;
                tbl_purchase.getItems().clear();
                while (tResult.next()) {
                    String nomor="";
                    String sku= "";
                    String barcode = "";
                    String nama = "";
                    String sat = "";
                    String suplier = "";
                    String kategori = "";
                    String tgl ;
                    String stockAwal ;
                    String pembelian ;
                    String so ;
                    String stockOut ;
                    String penjualan ;
                    String stockAkhir ;
                    String refund ;
                    String konv , inv;
                    String kodeitem = tResult.getString(V_Mutasi_Stock.IDITEM.get());
                    if(!mMapItemList.contains(kodeitem)){
                        no++;
                        nomor=""+no;
                        sku= tResult.getString(V_Mutasi_Stock.KODE.get());
                        barcode = tResult.getString(V_Mutasi_Stock.BARCODE.get());
                        nama = tResult.getString(V_Mutasi_Stock.ITEMNAME.get());
                        sat = tResult.getString(V_Mutasi_Stock.SATUAN.get());
                        suplier = tResult.getString(V_Mutasi_Stock.SUPNAME.get());
                        kategori = tResult.getString(V_Mutasi_Stock.KAT.get());
                        tgl = EZDate.FORMAT_2.get(tResult.getDate(V_Mutasi_Stock.TGL.get()));
                        stockAwal = tResult.getString(V_Mutasi_Stock.STOCKAWAL.get());
                        pembelian = tResult.getString(V_Mutasi_Stock.PURCHASE.get());
                        so = tResult.getString(V_Mutasi_Stock.STOCKOPNAME.get());
                        stockOut = tResult.getString(V_Mutasi_Stock.STOCKOUT.get());
                        penjualan = tResult.getString(V_Mutasi_Stock.SALE.get());
                        stockAkhir = tResult.getString(V_Mutasi_Stock.STOCKAKIR.get());
                        refund = tResult.getString(V_Mutasi_Stock.REFUND.get());
                        konv = tResult.getString(V_Mutasi_Stock.KONV.get());
                        inv = tResult.getString(V_Mutasi_Stock.INVERSI.get());
                        mMapItemList.add(kodeitem);
                    }else{
                        tgl = EZDate.FORMAT_2.get(tResult.getDate(V_Mutasi_Stock.TGL.get()));
                        stockAwal = tResult.getString(V_Mutasi_Stock.STOCKAWAL.get());
                        pembelian = tResult.getString(V_Mutasi_Stock.PURCHASE.get());
                        penjualan = tResult.getString(V_Mutasi_Stock.SALE.get());
                        stockAkhir = tResult.getString(V_Mutasi_Stock.STOCKAKIR.get());
                        so = tResult.getString(V_Mutasi_Stock.STOCKOPNAME.get());
                        stockOut = tResult.getString(V_Mutasi_Stock.STOCKOUT.get());
                        refund = tResult.getString(V_Mutasi_Stock.REFUND.get());
                        konv = tResult.getString(V_Mutasi_Stock.KONV.get());
                        inv = tResult.getString(V_Mutasi_Stock.INVERSI.get());
                    }                
                    MutasiStockModel tModel = new MutasiStockModel(nomor, tgl, sku, barcode, nama, sat, suplier, kategori, 
                            stockAwal, pembelian,so,stockOut,penjualan, stockAkhir, refund, konv, inv);
                    mutasistockList.add(tModel);
                    setValueColum();
                    tbl_purchase.setItems(mutasistockList);
                }
                mMapItemList.clear();
            } catch (SQLException ex) {
                loggingerror(ex);
            }        
        }
    }
    
    private String getQuery(){
        String tDate1 = getDate(dt_pick_1);
        String tDate2 = getDate(dt_pick_2);
        String[] pParam = new String[]{
            V_Mutasi_Stock.ITEMNAME.get(),V_Mutasi_Stock.BARCODE.get(),V_Mutasi_Stock.KODE.get()
        };
        
        String tSQL = "SELECT * FROM " +V_Mutasi_Stock.TABLENAME.get()+" WHERE "+V_Mutasi_Stock.TGL.get()+" BETWEEN '"+tDate1+"' AND '"+tDate2+"'";
        
        if (cmb_param.getSelectionModel().getSelectedIndex()>0){
            String tParam = pParam[cmb_param.getSelectionModel().getSelectedIndex()-1];
            if (tParam.equalsIgnoreCase(V_Mutasi_Stock.ITEMNAME.get())) {
                tSQL += " AND "+tParam+" like'%"+txt_search.getText()+"%'";
            }else{
                tSQL += " AND "+tParam+" ='"+txt_search.getText()+"'";
            }
        }
        
        if (cmb_categori.getSelectionModel().getSelectedIndex()>0) {
            String idKategori = mMapKategori.get(cmb_categori.getSelectionModel().getSelectedItem());
            tSQL+= " AND "+V_Mutasi_Stock.KATID.get()+"='"+idKategori+"'";
        }
        
        tSQL+= " ORDER BY "+V_Mutasi_Stock.ITEMNAME.get()+","+V_Mutasi_Stock.SATUANKE.get()+","+V_Mutasi_Stock.TGL.get()+" ASC";
        
        return tSQL;
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_sku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        clm_nama.setCellValueFactory(new PropertyValueFactory<>("namaItem"));
        clm_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        clm_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        clm_kategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        clm_supplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("sat"));
        clm_stock_awal.setCellValueFactory(new PropertyValueFactory<>("stokAwal"));
        clm_pembelian.setCellValueFactory(new PropertyValueFactory<>("pembelian"));
        clm_stock_opname.setCellValueFactory(new PropertyValueFactory<>("stockOpname"));
        clm_stock_out.setCellValueFactory(new PropertyValueFactory<>("stockOut"));
        clm_penjualan.setCellValueFactory(new PropertyValueFactory<>("penjualan"));
        clm_stock_akhir.setCellValueFactory(new PropertyValueFactory<>("stockAkhir"));
        clm_refund.setCellValueFactory(new PropertyValueFactory<>("refund"));
        clm_komversi.setCellValueFactory(new PropertyValueFactory<>("konv"));
        clm_inversi.setCellValueFactory(new PropertyValueFactory<>("inversi"));
    }

    public void loadSupplier() {
        List<String> tMap = getKategori();
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
        tbl_purchase.getItems().clear();
        cmb_param.setItems(paramList);
        cmb_param.getSelectionModel().select(0);
        cmb_categori.getSelectionModel().select(0);
        if (!mMapItemList.isEmpty()) {
            mMapItemList.clear();
        }
    }

    @Override
    public void setButtonListener() {
        setDate(dt_pick_1);
        setDate(dt_pick_2);
        
        btn_search.setOnAction((ActionEvent event) -> {
            loadData();
            txt_search.requestFocus();
        });
        
        txt_search.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                loadData();
                txt_search.requestFocus();
            }
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth()-250);
        AnchorPane.setPrefHeight(getHeight()- 355);
        setAligmentColoum(clm_sku, Pos.CENTER_LEFT);
        setAligmentColoum(clm_barcode, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama, Pos.CENTER_LEFT);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_tanggal, Pos.CENTER);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_supplier, Pos.CENTER_LEFT);
        setAligmentColoum(clm_kategori, Pos.CENTER_LEFT);
        setAligmentColoum(clm_stock_awal, Pos.CENTER);
        setAligmentColoum(clm_stock_akhir, Pos.CENTER);
        setAligmentColoum(clm_pembelian, Pos.CENTER);
        setAligmentColoum(clm_penjualan, Pos.CENTER);
        setAligmentColoum(clm_stock_opname, Pos.CENTER);
        setAligmentColoum(clm_stock_out, Pos.CENTER);
        setAligmentColoum(clm_refund, Pos.CENTER);
        setAligmentColoum(clm_komversi, Pos.CENTER);
        setAligmentColoum(clm_inversi, Pos.CENTER);
        btn_search.setCursor(Cursor.HAND);
        btn_print.setCursor(Cursor.HAND);
        btn_search.setTooltip(new Tooltip("Cari"));
        btn_print.setTooltip(new Tooltip("Print"));
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
