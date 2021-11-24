/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import app.retail.controller.general.MapKaryawan;
import app.retail.model.stock.StockKeluarModel;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.ITEM;
import app.retail.utility.table.Stock_Out;
import app.retail.utility.table.Stock_Out_Detail;
import app.retail.utility.table.V_All_Item;
import app.retail.utility.table.V_All_Stock_Out;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormInputStockOutController extends AbstractStock implements Initializable {

    JFXButton btnedit,btndelete ;
    ObservableList<StockKeluarModel> itemList = FXCollections.observableArrayList();
    ObservableList<String> itemListView = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXListView<String> list_view_item;
    @FXML
    private Text lbl_total_harga;
    @FXML
    private JFXTextField txt_no_transaksi;
    @FXML
    private JFXDatePicker dt_trans;
    @FXML
    private JFXTextField txt_cari;
    @FXML
    private JFXRadioButton radio_kode;
    @FXML
    private ToggleGroup rc;
    @FXML
    private JFXRadioButton radio_nama;
    @FXML
    private JFXTextField txt_kode_item;
    @FXML
    private JFXTextField txt_nama_item;
    @FXML
    private JFXTextField txt_harga;
    @FXML
    private Text lbl_satuan;
    @FXML
    private JFXTextField txt_qty;
    @FXML
    private TableView<StockKeluarModel> tbl_trans;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_kode_item;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_nama_item;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_sat;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_harga;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_qty;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_total;
    @FXML
    private TableColumn<StockKeluarModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<StockKeluarModel, JFXButton> clm_delete;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXTextArea txt_ket;
    @FXML
    private JFXButton btn_kembali;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setToolTip();
        setDate(dt_trans);
        initializeState();
        setButtonListener();
        list_view_item.setVisible(false);
    }    

    @Override
    public void clear() {
        lbl_satuan.setText("");
        lbl_total_harga.setText("Rp. 0");
        txt_cari.setText("");
        txt_kode_item.setText("");
        txt_nama_item.setText("");
        txt_harga.setText("0");
        txt_qty.setText("0");
    }

    public void simpan() throws SQLException {
        RecordEntry tMap = new RecordEntry();
        tMap.createEntry(Stock_Out.IDTRANS.get(), txt_no_transaksi.getText());
        tMap.createEntry(Stock_Out.TRANDT.get(), getDate(dt_trans));
        tMap.createEntry(Stock_Out.TTLITEM.get(), "" + tbl_trans.getItems().size());
        int ttlQty = Integer.parseInt(hitungGrandTotal(tbl_trans, 4));
        tMap.createEntry(Stock_Out.TTLQTY.get(), "" + ttlQty);
        tMap.createEntry(Stock_Out.TTLHARGA.get(), lbl_total_harga.getText().replace("Rp. ", "").replace(",", ""));
        tMap.createEntry(Stock_Out.KET.get(), txt_ket.getText());
        tMap.createEntry(Stock_Out.INPUTBY.get(), mMapKaryawan.get(MapKaryawan.KODEKARYAWAN.get()));
        tMap.createEntry(Stock_Out.CDT.get(), EZDate.SQLDATE.today());
        tMap.createEntry(Stock_Out.UDT.get(), EZDate.SQLDATE.today());
        tMap.createEntry(Stock_Out.SID.get(), getSession());
        insertToDatabase(tMap, Stock_Out.TABLENAME.get());
        simpanDetail(Stock_Out_Detail.TABLENAME.get(), txt_no_transaksi.getText());
    }
    
    private String getQueryReport(String idTrans){
        String tSQL = "SELECT * FROM v_all_stock_out SO "
                + "INNER JOIN v_stock_out_detail SD "
                + "ON SD.TRAN_ID=SO.TRANSID "
                +" WHERE "+V_All_Stock_Out.TRANSID.get()+"='"+idTrans+"' "
                + "ORDER BY TGL,TRANSID ASC";
        return tSQL;
    }
    
    private void simpanDetail(String pTable, String pIdTrans) throws SQLException{
        for (int i = 0; i < tbl_trans.getItems().size(); i++) {
            StockKeluarModel stockKeluarModel = tbl_trans.getItems().get(i);
            RecordEntry tMap = new RecordEntry();
            tMap.createEntry(Stock_Out_Detail.ID.get(), stockKeluarModel.getId());
            tMap.createEntry(Stock_Out_Detail.IDTRANS.get(), pIdTrans);
            tMap.createEntry(Stock_Out_Detail.KODEITEM.get(), stockKeluarModel.getKode_item());
            tMap.createEntry(Stock_Out_Detail.HARGA.get(), stockKeluarModel.getHarga().replace(",", ""));
            tMap.createEntry(Stock_Out_Detail.QTY.get(), stockKeluarModel.getQty());
            tMap.createEntry(Stock_Out_Detail.TOTALHARGA.get(), stockKeluarModel.getTotal_harga().replace(",", ""));
            insertToDatabase(tMap, pTable);
        }
        EZSystem.showAllert(new Alert(Alert.AlertType.INFORMATION), "Data berhasil disimpan", EZIcon.ICON_STAGE);
        showReport("report/stock/lap_stock_out.jrxml", getQueryReport(pIdTrans));
        initializeState();
        clear();
    }
    
    private void loaddatafromlistview(){
        if (list_view_item.getSelectionModel().getSelectedIndex()>=0) {
            String valueKey = list_view_item.getSelectionModel().getSelectedItem();
            if(mMapItem.containsKey(valueKey)){
                ITEM item = mMapItem.get(valueKey);
                txt_kode_item.setText(item.getIDITEM());
                txt_nama_item.setText(item.getITEMNAME());
                double tValue = Double.parseDouble(item.getHRGBELI());
                txt_harga.setText(formatRupiah(tValue));
                lbl_satuan.setText("/ "+item.getSATUAN());
                txt_qty.requestFocus();
                txt_cari.setText("");
            }
        }
        list_view_item.setVisible(false);
    }
    
    public void searchBy(int type, String pParam) throws SQLException{
        String[] tParam = new String[]{"KODE","BARCODE"};
        int t = 0;
        String tSQL;
        if (type==0) {
            boolean next = true;
            while (next && t<tParam.length) {                
                tSQL = "SELECT * FROM "+V_All_Item.TABLENAME.get()+ " WHERE "+tParam[t]+" ='"+pParam+"';";
                
                ResultSet tResult = selectFromDatabase(tSQL);
                if (tResult != null) {
                    if (tResult.next()) {
                        txt_kode_item.setText(tResult.getString(V_All_Item.IDITEM.get()));
                        txt_nama_item.setText(tResult.getString(V_All_Item.ITEMNAME.get()));
                        double tValue = tResult.getDouble(V_All_Item.HRGBELI.get());
                        txt_harga.setText(formatRupiah(tValue));
                        lbl_satuan.setText("/ "+tResult.getString(V_All_Item.SATUAN.get()));
                        txt_qty.requestFocus();
                        txt_cari.setText("");
                        next=false;
                    }
                }
                t++;                
                if (t==tParam.length && next == true) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    EZSystem.showAllert(alert, "Data item tidak ditemukan!", EZIcon.ICON_STAGE);
                    txt_cari.requestFocus();
                    txt_cari.setText("");
                }
            }
        }else if (type==1){
            if(pParam.length()>=3){
                list_view_item.getItems().clear();
                searchByItem(pParam);
            }
        }
    }
    
    private void searchByItem(String pParam) throws SQLException{
        String tSQL = "SELECT * FROM " + V_All_Item.TABLENAME.get() + " WHERE " + V_All_Item.ITEMNAME.get() + " like'" + pParam + "%';";
        List<String> tObject = getListItem(tSQL);
        if (tObject != null) {
            itemListView.addAll(tObject);
            list_view_item.setItems(itemListView);
            list_view_item.setVisible(true);
        }
    }
    
    private void setValueColum(){
        clm_kode_item.setCellValueFactory(new PropertyValueFactory<>("kode_item"));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>("nama_item"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("sat"));
        clm_harga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        clm_qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("total_harga"));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btnedit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == EZButtonType.BTN_EDIT) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Edit data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    int index = Integer.parseInt(button.getId());
                    StockKeluarModel pModel = tbl_trans.getItems().get(index);
                    txt_kode_item.setText(pModel.getKode_item());
                    txt_nama_item.setText(pModel.getNama_item());
                    txt_harga.setText(pModel.getHarga());
                    lbl_satuan.setText("/ "+pModel.getSat());
                    txt_qty.setText(pModel.getQty());
                    tbl_trans.getItems().remove(index);
                    for (int i = 0; i < itemList.size(); i++) {
                        itemList.get(i).getBtndelete().setId(""+i);
                        itemList.get(i).getBtnedit().setId(""+i);
                    }
                    loadItemToTable();
                    txt_qty.requestFocus();
                }
            }
            
            if (type == EZButtonType.BTN_DELETE) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Yakin hapus item ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    int index = Integer.parseInt(button.getId());
                    tbl_trans.getItems().remove(index);
                    for (int i = 0; i < itemList.size(); i++) {
                        itemList.get(i).getBtndelete().setId(""+i);
                        itemList.get(i).getBtnedit().setId(""+i);
                    }
                    loadItemToTable();
                }
            }
        });
    }   


    @Override
    public void initializeState() {
        txt_cari.requestFocus();
        txt_no_transaksi.setText(generateRefnum(Stock_Out.TABLENAME.get(), Stock_Out.IDTRANS.get(), "STO-"+EZDate.FAKTUR.today()));
        tbl_trans.getItems().clear();
        txt_ket.setText("");
        clear();
    }

    @Override
    public void setButtonListener() {
        txt_cari.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                if (radio_kode.isSelected()) {
                    try {
                        searchBy(0, txt_cari.getText());
                    } catch (SQLException ex) {
                        loggingerror(ex);
                    }
                }else if(radio_nama.isSelected()){
                    list_view_item.requestFocus();
                }
            }else if(event.getCode().equals(KeyCode.DOWN)){
                if(list_view_item.isVisible()){
                    list_view_item.requestFocus();
                }
            }
        });
        
        list_view_item.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                list_view_item.getSelectionModel().select(0);
            }else{
                list_view_item.setVisible(false);
            }
        });
        
        txt_cari.setOnKeyTyped((KeyEvent event) -> {
            if(radio_nama.isSelected()){
                if(txt_cari.getText().length()>=3){
                    try {
                        searchBy(1, txt_cari.getText());
                    } catch (SQLException ex) {
                        loggingerror(ex);
                    }
                }else{
                    list_view_item.setVisible(false);
                }
            }
        });
        
        list_view_item.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loaddatafromlistview();
            }
        });
        
        list_view_item.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount()==2) {
                loaddatafromlistview();
            }
        });
        
        radio_nama.setOnAction((ActionEvent event) -> {
            txt_cari.requestFocus();
        });
        
        radio_kode.setOnAction((ActionEvent event) -> {
            txt_cari.requestFocus();
        });
        
        txt_qty.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!txt_kode_item.getText().equals("") || !txt_nama_item.getText().equals("") || !txt_harga.getText().equals("0") ) {
                    if (txt_qty.getText().equals("0")) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        EZSystem.showAllert(alert, "Isi Qty dengan benar!", EZIcon.ICON_STAGE);
                        txt_qty.requestFocus();
                    }else{
                        btnedit = getButton(EZButtonType.BTN_EDIT, "Edit  ");
                        btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                        setButtonOnTableView(btnedit, EZButtonType.BTN_EDIT);
                        setButtonOnTableView(btndelete, EZButtonType.BTN_DELETE);
                        int no = tbl_trans.getItems().size();
                        btnedit.setId(""+no);
                        btndelete.setId(""+no);
                        int tQ = Integer.parseInt(txt_qty.getText());
                        double tP = Double.valueOf(txt_harga.getText().replace(",", ""));
                        double tT = tP*tQ;
                        itemList.add(new StockKeluarModel(
                                ""+no, UUID.randomUUID().toString().replace("-", "").toUpperCase(), txt_kode_item.getText(), 
                                txt_nama_item.getText(),lbl_satuan.getText().replace("/", ""), txt_harga.getText(), 
                                ""+tQ, formatRupiah(tT), btnedit, btndelete));
                        loadItemToTable();
                        txt_kode_item.setText("");
                        txt_nama_item.setText("");
                        txt_harga.setText("0");
                        lbl_satuan.setText("");
                        txt_qty.setText("0");
                        txt_cari.requestFocus();
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    EZSystem.showAllert(alert, "Isi data dengan benar!", EZIcon.ICON_STAGE);
                    txt_cari.requestFocus();
                }
            }
        });
        
        setNumericTextfield(txt_qty);
        setNumericTextfield(txt_harga);
        setCurencyTextfield(txt_harga);
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> option = EZSystem.showAllert(alert, "Simpan data stock keluar ?", EZIcon.ICON_STAGE);
            if (option.get() == ButtonType.OK) {
                try {
                    simpan();
                } catch (SQLException ex) {
                    loggingerror(ex);
                }
            }
        });
        
        btn_batal.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> opt = EZSystem.showAllert(alert, "Batalkan Transaksi?", EZIcon.ICON_STAGE);
            if (opt.get() ==  ButtonType.OK) {
                initializeState();
            }
        });
        
        Platform.runLater(() -> {
            txt_cari.requestFocus();
        });
        
        btn_kembali.setOnAction((ActionEvent event)->{
            try {
                setmUrlChild("/app/retail/fxml/stock/DataStockOut.fxml");
                getmHomeController().performLoadStock(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 220);
        btn_simpan.setCursor(Cursor.HAND);
        btn_batal.setCursor(Cursor.HAND);
        txt_no_transaksi.setEditable(false);
        txt_kode_item.setEditable(false);
        txt_nama_item.setEditable(false);
        txt_harga.setEditable(false);
        setAligmentColoum(clm_harga, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_qty, Pos.CENTER);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_kode_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        btn_kembali.setCursor(Cursor.HAND);
        double lebar = getWidth() - 200;
        clm_nama_item.setPrefWidth((lebar * 35)/100);
        clm_kode_item.setPrefWidth((lebar * 10)/100);
        clm_sat.setPrefWidth((lebar * 5)/100);
        clm_harga.setPrefWidth((lebar * 10)/100);
        clm_qty.setPrefWidth((lebar * 5)/100);
        clm_total.setPrefWidth((lebar * 10)/100);
        clm_delete.setPrefWidth((lebar * 13)/100);
        clm_edit.setPrefWidth((lebar * 8)/100);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadData() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void loadItemToTable(){
        setValueColum();
        tbl_trans.setItems(itemList);
        lbl_total_harga.setText("Rp. " + hitungGrandTotal(tbl_trans, 5));
    }
}
