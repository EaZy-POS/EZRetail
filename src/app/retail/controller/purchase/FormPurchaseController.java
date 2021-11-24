/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.purchase;

import static app.retail.controller.general.General.genereateID;
import static app.retail.controller.general.General.selectFromDatabase;
import static app.retail.controller.purchase.AbstractPurchase.MAP_SUPPLIER;
import app.retail.model.purchase.PurcasheFormModel;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.ITEM;
import app.retail.utility.table.Purchase;
import app.retail.utility.table.Purchase_Detail;
import app.retail.utility.table.V_All_Item;
import app.retail.utility.table.V_All_Purchase;
import app.retail.utility.table.V_All_Purchase_detail;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
public class FormPurchaseController extends AbstractPurchase implements Initializable {

    ObservableList<String> supplierList = FXCollections.observableArrayList();
    ObservableList<PurcasheFormModel> itemList = FXCollections.observableArrayList();
    public static ObservableList listviewitem = FXCollections.observableArrayList();
    private final ObservableList<String> tipebayarlist = FXCollections.observableArrayList("-- Type Bayar --","CASH","HUTANG");
    JFXButton btnedit,btndelete ;
    private static List<PurcasheFormModel> listPurchaseItem = new ArrayList<>();
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Text lbl_total_harga;
    @FXML
    private JFXComboBox<String> cmb_supplier;
    @FXML
    private JFXTextField txt_no_transaksi;
    @FXML
    private JFXDatePicker dt_trans;
    @FXML
    private JFXTextField txt_cari;
    @FXML
    private JFXRadioButton radio_nama;
    @FXML
    private JFXRadioButton radio_kode;
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
    private TableView<PurcasheFormModel> tbl_trans;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private ToggleGroup rc;
    @FXML
    private TableColumn<PurcasheFormModel, String> clm_kode_item;
    @FXML
    private TableColumn<PurcasheFormModel, String> clm_nama_item;
    @FXML
    private TableColumn<PurcasheFormModel, String> clm_sat;
    @FXML
    private TableColumn<PurcasheFormModel, String> clm_harga;
    @FXML
    private TableColumn<PurcasheFormModel, String> clm_qty;
    @FXML
    private TableColumn<PurcasheFormModel, String> clm_total;
    @FXML
    private TableColumn<PurcasheFormModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<PurcasheFormModel, JFXButton> clm_delete;
    @FXML
    private JFXListView<String> list_view_item;
    @FXML
    private JFXTextField txt_no_faktur;
    @FXML
    private JFXTextField txt_rec_by;
    @FXML
    private JFXComboBox<String> cmb_type_bayar;
    @FXML
    private JFXButton btn_kembali;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setToolTip();
            setDate(dt_trans);
            loadSupplier();
            cmb_type_bayar.setItems(tipebayarlist);
            initializeState();
            setButtonListener();
            list_view_item.setVisible(false);
        } catch (SQLException ex) {
            loggingerror(ex);
        }
                
    }    

    @Override
    public void simpan() {
        RecordEntry tMapPO = new RecordEntry();
        if (cmb_supplier.getSelectionModel().getSelectedIndex()>0) {
            if(cmb_type_bayar.getSelectionModel().getSelectedIndex()>0){
                String refnum = txt_no_transaksi.getText();
                String idSup = MAP_SUPPLIER.get(cmb_supplier.getSelectionModel().getSelectedItem());
                String tranDT = getDate(dt_trans);
                String tFaktur = txt_no_faktur.getText();
                String tTypeBayar = cmb_type_bayar.getSelectionModel().getSelectedItem();
                String tRecBy = txt_rec_by.getText();
                String tTotal = lbl_total_harga.getText().replace("Rp. ", "").replace(",", "");
                tMapPO.createEntry(Purchase.REFNUM.get(), refnum);
                tMapPO.createEntry(Purchase.TRANDATE.get(), tranDT);
                tMapPO.createEntry(Purchase.IDSUPPLIER.get(), idSup);
                tMapPO.createEntry(Purchase.TOTAL.get(), tTotal);
                tMapPO.createEntry(Purchase.CDT.get(), EZDate.SQLDATE.today());
                tMapPO.createEntry(Purchase.SID.get(), getSession());
                tMapPO.createEntry(Purchase.TYPEBAYAR.get(), tTypeBayar);
                tMapPO.createEntry(Purchase.RECBY.get(), tRecBy);
                tMapPO.createEntry(Purchase.FAKTUR.get(), tFaktur);
                tMapPO.createEntry(Purchase.FLAG.get(), "1");
                tMapPO.createEntry(Purchase.JNS.get(), "LANGSUNG");
                tMapPO.createEntry(Purchase.INPUTDATE.get(), EZDate.SQLDATETIME.today());
                try {
                    int x = insertToDatabase(tMapPO, Purchase.TABLENAME.get());
                    if(x == 1){                    
                        simpan(Purchase_Detail.TABLENAME.get(), refnum);
                    }
                } catch (SQLException e) {
                    loggingerror(e);
                    rollback(refnum);
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                EZSystem.showAllert(alert, "Mohon pilih Type Bayar!", EZIcon.ICON_STAGE);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            EZSystem.showAllert(alert, "Mohon pilih supplier!", EZIcon.ICON_STAGE);
        }
    }
    
    protected void simpan(String pTableName,String pRefnum) throws SQLException{
        for (PurcasheFormModel model : tbl_trans.getItems()) {
            RecordEntry tMapDetail = new RecordEntry();
            tMapDetail.createEntry(Purchase_Detail.KODEITEM.get(), model.getItem_code());
            tMapDetail.createEntry(Purchase_Detail.BUYPRICE.get(), model.getBuy_price().replace(",", ""));
            tMapDetail.createEntry(Purchase_Detail.QTY.get(), model.getQty());
            tMapDetail.createEntry(Purchase_Detail.QTYTERIMA.get(), model.getQty());
            tMapDetail.createEntry(Purchase_Detail.FLAG.get(), "1");
            tMapDetail.createEntry(Purchase_Detail.TOTAL.get(), model.getTotal_harga().replace(",", ""));
            tMapDetail.createEntry(Purchase_Detail.REFNUM.get(), pRefnum);

            insertToDatabase(tMapDetail, pTableName);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
        showReport("report/transaksi/purchase/faktur_purchase.jrxml", getQuery(pRefnum));
        initializeState();
    }

    private String getQuery(String refnum){
        String tsql = "SELECT P.*, S.NMKARYAWA, PD.IDITEM, PD.ITEMNAME, PD.SATUAN, PD.BUYPRICE, PD.QTY, PD.QTYTERIMA, PD.TOTAL as SUBTOTAL " 
                +"FROM `v_all_purchase` P " 
                +"INNER JOIN v_all_purchase_detail PD on PD.REFNUM= P.REFNUM " 
                +"INNER JOIN v_session S ON P.SID = S.SESSIONID  WHERE P.REFNUM='"+refnum+"'";
        return tsql;
    }
    
    private void rollback(String refnum){
        try {
            String tSql = "DELETE "+Purchase.TABLENAME.get()+","+Purchase_Detail.TABLENAME.get()
                    + " FROM "+Purchase_Detail.TABLENAME.get()+" RIGHT JOIN "
                    + Purchase.TABLENAME.get()+" ON "+Purchase.TABLENAME.get()+"."+Purchase.REFNUM.get()
                    + " = " +Purchase_Detail.TABLENAME.get()+"."+Purchase_Detail.REFNUM.get()
                    + " WHERE "+Purchase.TABLENAME.get()+"."+Purchase.REFNUM.get()+" = '"+refnum+"'";
            updateToDatabase(tSql);
        } catch (SQLException ex) {
        }
    }
    
    @Override
    public void loadData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setValueColum(){
        clm_kode_item.setCellValueFactory(new PropertyValueFactory<>("item_code"));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("descrip"));
        clm_harga.setCellValueFactory(new PropertyValueFactory<>("buy_price"));
        clm_qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("total_harga"));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btn_edit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btn_delete"));
    }

    public ResultSet searchBy(String tSQL) throws SQLException {
        return selectFromDatabase(tSQL);
    }

    private void loaddatafromlistview() {
        if (list_view_item.getSelectionModel().getSelectedIndex() >= 0) {
            String valueKey = list_view_item.getSelectionModel().getSelectedItem();
            if (mMapItem.containsKey(valueKey)) {
                ITEM tList = mMapItem.get(valueKey);
                txt_kode_item.setText(tList.getIDITEM());
                txt_nama_item.setText(tList.getITEMNAME());
                txt_harga.setText(tList.getHRGBELI());
                double tValue = Double.parseDouble(tList.getHRGBELI());
                txt_harga.setText(formatRupiah(tValue));
                lbl_satuan.setText("/ " + tList.getSATUAN());
                txt_qty.requestFocus();
                txt_cari.setText("");
            }
        }
        list_view_item.setVisible(false);
    }

    public void searchBy(int type, String pParam) throws SQLException {
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

    private void searchByItem(String pParam) {
        String tSQL = "SELECT * FROM " + V_All_Item.TABLENAME.get() + " WHERE " + V_All_Item.ITEMNAME.get() + " like'" + pParam + "%';";
        try {
            List<String> tObject = getListItem(tSQL);
            if (tObject != null) {
                listviewitem.addAll(tObject);
                list_view_item.setItems(listviewitem);
                list_view_item.setVisible(true);
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    public void loadSupplier() throws SQLException {
        List<String> tMap = getSupplier();
        supplierList.add("-- Supplier --");
        for (int i = 0; i < tMap.size(); i++) {
            String valString = tMap.get(i);
            supplierList.add(valString);
        }
        cmb_supplier.getItems().clear();
        cmb_supplier.setItems(supplierList);
    }
    
    @Override
    public List<String> getSupplier() throws SQLException {
        List<String> tMap = new ArrayList<>();
        String tQuery = "SELECT id,nama FROM supplier ORDER BY nama ASC";
        ResultSet tRest = selectFromDatabase(tQuery);
        MAP_SUPPLIER = new HashMap<>();
        while (tRest.next()) {
            MAP_SUPPLIER.put(tRest.getString("nama"), tRest.getString("id"));
            tMap.add(tRest.getString("nama"));
        }
        return tMap;
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == EZButtonType.BTN_EDIT) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Edit data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    int index = Integer.parseInt(button.getId());
                    PurcasheFormModel pModel = itemList.get(index);
                    txt_kode_item.setText(pModel.getItem_code());
                    txt_nama_item.setText(pModel.getItem_name());
                    txt_harga.setText(pModel.getBuy_price());
                    lbl_satuan.setText("/ " + pModel.getDescrip());
                    txt_qty.setText(pModel.getQty());
                    itemList.remove(index);
                    for (int i = 0; i < itemList.size(); i++) {
                        itemList.get(i).getBtn_delete().setId(""+i);
                        itemList.get(i).getBtn_edit().setId(""+i);
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
                        itemList.get(i).getBtn_delete().setId(""+i);
                        itemList.get(i).getBtn_edit().setId(""+i);
                    }
                    loadItemToTable();
                }
            }
        });
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
        txt_no_faktur.setText("");
        txt_rec_by.setText("");
        listPurchaseItem.clear();
    }    

    private void loadDataEdit(String pKdEdit) throws SQLException{
        String tSQL = "SELECT * FROM " + V_All_Purchase.TABLENAME.get() + " WHERE " + V_All_Purchase.REFNUM.get() + "='" + pKdEdit + "'";
        ResultSet tResult = selectFromDatabase(tSQL);
        if (tResult.next()) {
            txt_no_transaksi.setText(tResult.getString(V_All_Purchase.REFNUM.get()));
            cmb_supplier.getSelectionModel().select(tResult.getString(V_All_Purchase.SUPPLIER.get()));
            Date tDt = tResult.getDate(V_All_Purchase.TGL.get());
            setDate(dt_trans, tDt);
            lbl_total_harga.setText("Rp. " + formatRupiah(tResult.getDouble(V_All_Purchase.TOTAL.get())));
            loadDetail(pKdEdit);
        }
    }
    
    private void loadDetail(String pKodeEdit) throws SQLException{
        String tSQL = "SELECT * FROM " + V_All_Purchase_detail.TABLENAME.get() + " WHERE " + V_All_Purchase_detail.REFNUM.get() + "='" + pKodeEdit + "'";
        ResultSet tResult = selectFromDatabase(tSQL);
        while (tResult.next()) {
            int no = tbl_trans.getItems().size();
            if (tResult.getInt(V_All_Purchase_detail.FLAG.get()) == 0 & tResult.getInt(V_All_Purchase_detail.QTYTERIMA.get()) == 0) {
                btnedit = getButton(EZButtonType.BTN_EDIT, "Edit  ");
                btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                setButtonOnTableView(btnedit, EZButtonType.BTN_EDIT);
                setButtonOnTableView(btndelete, EZButtonType.BTN_DELETE);
                btnedit.setId("" + no);
                btndelete.setId("" + no);
            }
            itemList.add(new PurcasheFormModel("" + no, tResult.getString(V_All_Purchase_detail.IDITEM.get()),
                    tResult.getString(V_All_Purchase_detail.ITEMNAME.get()),
                    tResult.getString(V_All_Purchase_detail.SATUAN.get()),
                    formatRupiah(tResult.getDouble(V_All_Purchase_detail.BUYPRICE.get())),
                    tResult.getString(V_All_Purchase_detail.QTY.get()),
                    formatRupiah(tResult.getDouble(V_All_Purchase_detail.TOTAL.get())),
                    btnedit, btndelete));
            setValueColum();
            tbl_trans.setItems(itemList);
            lbl_total_harga.setText("Rp. " + hitungGrandTotal(tbl_trans, 6));
        }
    }

    @Override
    public void initializeState() {
        String tFaktur = generateRefnum(Purchase.TABLENAME.get(), Purchase.REFNUM.get(), "REC-"+EZDate.FAKTUR.today());
        txt_no_transaksi.setText(tFaktur);
        cmb_supplier.getSelectionModel().select(0);
        tbl_trans.getItems().clear();
        cmb_type_bayar.getSelectionModel().select(0);
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
                        int no = tbl_trans.getItems().size();
                        btnedit.setId(""+no);
                        btndelete.setId(""+no);
                        setButtonOnTableView(btnedit, EZButtonType.BTN_EDIT);
                        setButtonOnTableView(btndelete, EZButtonType.BTN_DELETE);
                        int tQ = Integer.parseInt(txt_qty.getText());
                        double tP = Double.valueOf(txt_harga.getText().replace(",", ""));
                        double tT = tP*tQ;
                        itemList.add(new PurcasheFormModel(""+no,
                                txt_kode_item.getText(), txt_nama_item.getText(),
                                lbl_satuan.getText().replace("/ ",""), txt_harga.getText(),
                                txt_qty.getText(), formatRupiah(tT), btnedit, btndelete));
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

        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> option = EZSystem.showAllert(alert, "Simpan Data pembelian ?", EZIcon.ICON_STAGE);
            if (option.get() == ButtonType.OK) {
                alert = new Alert(Alert.AlertType.WARNING);
                if (txt_no_faktur.getText().length()==0) {
                    EZSystem.showAllert(alert, "Mohon lengkapi data!", EZIcon.ICON_STAGE);
                    txt_no_faktur.requestFocus();
                }else if(txt_rec_by.getText().length()==0){
                    EZSystem.showAllert(alert, "Mohon lengkapi data!", EZIcon.ICON_STAGE);
                    txt_rec_by.requestFocus();
                }else{
                    simpan();
                }
            }
        });
        
        btn_batal.setOnAction((ActionEvent event) -> {
            if (tbl_trans.getItems().size()>0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Simpan Data pembelian ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    initializeState();
                }
            }else{
                initializeState();
            }
        });
        
        Platform.runLater(()->{
            txt_cari.requestFocus();
        });
        
        btn_kembali.setOnAction((ActionEvent event)->{
            try {
                getmHomeController().performLoadpurchase(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
    }
    
    private void loadItemToTable(){
        setValueColum();
        tbl_trans.setItems(itemList);
        lbl_total_harga.setText("Rp. " + hitungGrandTotal(tbl_trans, 5));
    }

    @Override
    public void setToolTip() {
        btn_simpan.setCursor(Cursor.HAND);
        btn_batal.setCursor(Cursor.HAND);
        txt_no_transaksi.setEditable(false);
        txt_kode_item.setEditable(false);
        txt_nama_item.setEditable(false);
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 220);
        setAligmentColoum(clm_harga, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_qty, Pos.CENTER);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_kode_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        btn_kembali.setCursor(Cursor.HAND);
        setNumericTextfield(txt_qty);
        setNumericTextfield(txt_harga);
        setCurencyTextfield(txt_harga);
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
}
