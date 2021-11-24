/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.purchase;

import static app.retail.controller.general.General.getStackTraceString;
import app.retail.model.purchase.RecPOModel;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Purchase;
import app.retail.utility.table.Purchase_Detail;
import app.retail.utility.table.V_All_Purchase;
import app.retail.utility.table.V_All_Purchase_detail;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormTerimaPOController extends AbstractPurchase implements Initializable {

    private static RecPOModel mModel = new RecPOModel();
    private static final ObservableList<RecPOModel> itemList = FXCollections.observableArrayList();
    private static final ObservableList<String> typeBayarList = FXCollections.observableArrayList("-- Type Bayar --","HUTANG","CASH");
    private static String mKodeTransaksi;
    private static int mIndexEdit;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXTextField txt_po;
    @FXML
    private JFXDatePicker dt_trans;
    @FXML
    private JFXTextField txt_no_rec;
    @FXML
    private JFXComboBox<String> cmb_tipe_bayar;
    @FXML
    private JFXDatePicker dt_trans_rec;
    @FXML
    private TableView<RecPOModel> tbl_trans;
    @FXML
    private TableColumn<RecPOModel, String> clm_kode_item;
    @FXML
    private TableColumn<RecPOModel, String> clm_nama_item;
    @FXML
    private TableColumn<RecPOModel, String> clm_sat;
    @FXML
    private TableColumn<RecPOModel, String> clm_harga;
    @FXML
    private TableColumn<RecPOModel, String> clm_qty;
    @FXML
    private TableColumn<RecPOModel, String> clm_total;
    @FXML
    private TableColumn<RecPOModel, JFXButton> clm_delete;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private Text lbl_total_harga;
    @FXML
    private JFXTextField txt_supplier;
    @FXML
    private JFXTextField txt_penerim;
    @FXML
    private TableColumn<RecPOModel, String> clm_qty_terima;
    @FXML
    private JFXTextField txt_faktur;
    @FXML
    private TableColumn<?, ?> clm_edit;
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
    private JFXTextField txt_qty_terima;
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
        initializeState();
        setButtonListener();
        clearForm();
    }    

    @Override
    public void clear() {
        txt_no_rec.setText("");
        txt_po.setText("");
        txt_penerim.setText("");
        txt_faktur.setText("");
        txt_supplier.setText("");
        setmKodeEdit("");
        lbl_total_harga.setText("Rp. 0");
    }

    @Override
    public void simpan() {
        if (validateForm()) {
            try {
                RecordEntry tMapRec = new RecordEntry();
                tMapRec.createEntry(Purchase.RECDATE.get(), getDate(dt_trans_rec));
                tMapRec.createEntry(Purchase.TYPEBAYAR.get(), cmb_tipe_bayar.getSelectionModel().getSelectedItem());
                tMapRec.createEntry(Purchase.RECBY.get(), txt_penerim.getText());
                tMapRec.createEntry(Purchase.UDT.get(), EZDate.SQLDATE.today());
                tMapRec.createEntry(Purchase.FAKTUR.get(), txt_faktur.getText());
                tMapRec.createEntry(Purchase.FLAG.get(), "1");
                String tTotal = lbl_total_harga.getText().replace("Rp. ", "").replace(",", "");
                tMapRec.createEntry(Purchase.TOTAL.get(), tTotal);
                updateToDatabase(tMapRec, Purchase.TABLENAME.get(), Purchase.REFNUM.get()+"='"+txt_no_rec.getText()+"'");
                simpandetail();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        }
    }
    
    private void simpandetail() throws SQLException{
        for(RecPOModel recPOModel:tbl_trans.getItems()){
            RecordEntry tMap = new RecordEntry();
            tMap.createEntry(Purchase_Detail.QTY.get(), recPOModel.getQty());
            tMap.createEntry(Purchase_Detail.QTYTERIMA.get(), recPOModel.getTerima());
            tMap.createEntry(Purchase_Detail.BUYPRICE.get(), recPOModel.getBuy_price().replace(",", ""));
            tMap.createEntry(Purchase_Detail.TOTAL.get(), recPOModel.getTotal_harga().replace(",", ""));
            tMap.createEntry(Purchase_Detail.FLAG.get(), "1");
            updateToDatabase(tMap, Purchase_Detail.TABLENAME.get(), Purchase_Detail.KODEITEM.get() + "='" + recPOModel.getItem_code() + "' AND " + Purchase_Detail.REFNUM.get() + "='" + txt_no_rec.getText() + "'");
        }
        removeItemFromDB();
        EZSystem.showAllert(new Alert(Alert.AlertType.INFORMATION), "Data berhasil disimpan", EZIcon.ICON_STAGE);
        showReport("report/transaksi/purchase/faktur_receiving.jrxml", getQuery(txt_no_rec.getText()));
        clear();
        btn_kembali.fire();
    }
    
    private void removeItemFromDB() throws SQLException{
        String tSQL = "DELETE FROM purchase_detail WHERE qty=0";
        updateToDatabase(tSQL);
    }
    
    private boolean validateForm(){
        Alert alert = new Alert(Alert.AlertType.WARNING);    
        
        if(txt_faktur.getText().equals("")){   
            EZSystem.showAllert(alert, "Mohon isi faktur!", EZIcon.ICON_STAGE);
            return false;
        }
        
        if (cmb_tipe_bayar.getSelectionModel().getSelectedIndex()==0) {
            EZSystem.showAllert(alert, "Mohon pilih tipe bayar!", EZIcon.ICON_STAGE);
            return false;
        }
        
        if (cmb_tipe_bayar.getSelectionModel().getSelectedIndex()==0) {
            EZSystem.showAllert(alert, "Mohon pilih tipe bayar!", EZIcon.ICON_STAGE);
            return false;
        }
        
        if (txt_penerim.getText().equals("")) {
            EZSystem.showAllert(alert, "Mohon isi penerima!", EZIcon.ICON_STAGE);
            return false;
        }
        return true;
    }
    
    private String getQuery(String refnum){
        String tsql = "SELECT P.*, S.NMKARYAWA, PD.IDITEM, PD.ITEMNAME, PD.SATUAN, PD.BUYPRICE, PD.QTY, PD.QTYTERIMA, PD.TOTAL as SUBTOTAL " 
                +"FROM `v_all_purchase` P " 
                +"INNER JOIN v_all_purchase_detail PD on PD.REFNUM= P.REFNUM " 
                +"INNER JOIN v_session S ON P.SID = S.SESSIONID  WHERE P.REFNUM='"+refnum+"'";
        return tsql;
    }

    @Override
    public void loadData() {
        String tSQL = "SELECT * FROM "+V_All_Purchase.TABLENAME.get()+" WHERE "
                +V_All_Purchase.REFNUM.get()+"='"+getmKodeEdit()+"'";
        try {
            ResultSet tResult = selectFromDatabase(tSQL);
            if (tResult.next()) {
                txt_po.setText(tResult.getString(V_All_Purchase.PO.get()));
                txt_supplier.setText(tResult.getString(V_All_Purchase.SUPPLIER.get()));
                Date tDt = tResult.getDate(V_All_Purchase.TGL.get());
                setDate(dt_trans,tDt);
                lbl_total_harga.setText("Rp. "+formatRupiah(tResult.getDouble(V_All_Purchase.TOTAL.get())));
                txt_no_rec.setText(tResult.getString(V_All_Purchase.REFNUM.get()));
                loadDetail(getmKodeEdit());
            }
        } catch (SQLException e) {
            loggingerror(e);
        }
    }

    private void clearForm(){
        txt_kode_item.setText("");
        txt_nama_item.setText("");
        txt_harga.setText("");
        txt_qty.setText("");
        txt_qty_terima.setText("");
        lbl_satuan.setText("");
        txt_qty_terima.setEditable(false);
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == EZButtonType.BTN_EDIT) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Edit data transaksi ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    loadDataEdit(Integer.parseInt(button.getId()));
                }
            }
            
            if (type == EZButtonType.BTN_DELETE) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                int index = Integer.parseInt(button.getId());
                Optional<ButtonType> option = EZSystem.showAllert(alert, 
                        "Yakin hapus item \n"+itemList.get(index).getItem_name()+" ["
                                +itemList.get(index).getDescrip()
                                +"] ?", 
                        EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    RecPOModel model = itemList.get(index);
                    model.setQty("0");
                    model.setTerima("0");
                    model.setTotal_harga("0");
                    itemList.set(index, model);
                    loadItemToTable();
                }
            }
        });
    }
    
    private void loadItemToTable(){
        setValueColum();
        tbl_trans.setItems(itemList);
        lbl_total_harga.setText("Rp. " + hitungGrandTotal(tbl_trans, 6));
        if(hitungGrandTotal(tbl_trans, 6).equalsIgnoreCase("0")){
            cancelTransaction();
        }
    }
    
    private void cancelTransaction() {
        Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION),
                "Nilai Transaksi 0, Batalkan Transaksi " + getmKodeEdit() + " ?", EZIcon.ICON_STAGE);
        if (opt.get() == ButtonType.OK) {
            try {
                deleteTransaction();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        }
    }
    
    private void deleteTransaction() throws SQLException{
        String tSQL = "DELETE FROM "+Purchase_Detail.TABLENAME.get()+" WHERE "
                +Purchase_Detail.REFNUM.get()+"='"+getmKodeEdit()+"'";
        updateToDatabase(tSQL);
        tSQL = "DELETE FROM "+Purchase.TABLENAME.get()+" WHERE "
                +Purchase.REFNUM.get()+"='"+getmKodeEdit()+"'";
        updateToDatabase(tSQL);
        clear();
        btn_kembali.fire();
    }
    
    @Override
    public void loggingerror(Exception ex){
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }
    
    public String getmKodeEdit() {
        return mKodeTransaksi;
    }

    public void setmKodeEdit(String mKodeEdit) {
        FormTerimaPOController.mKodeTransaksi = mKodeEdit;
    }
    
    private void setValueColum(){
        clm_kode_item.setCellValueFactory(new PropertyValueFactory<>("item_code"));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("descrip"));
        clm_harga.setCellValueFactory(new PropertyValueFactory<>("buy_price"));
        clm_qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("total_harga"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btn_delete"));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btn_edit"));
        clm_qty_terima.setCellValueFactory(new PropertyValueFactory<>("terima"));
    }
    
    private void loadDetail(String pKodeEdit) throws SQLException{
        String tSQL = "SELECT * FROM " + V_All_Purchase_detail.TABLENAME.get() + " WHERE " + V_All_Purchase_detail.REFNUM.get() + "='" + pKodeEdit + "'";
        itemList.clear();
        ResultSet tResult = selectFromDatabase(tSQL);
        int no = 0;
        while (tResult.next()) {
            JFXButton btndelete = null;
            JFXButton btnedit = null;
            if (tResult.getInt(V_All_Purchase_detail.FLAG.get()) == 0 & tResult.getInt(V_All_Purchase_detail.QTYTERIMA.get()) == 0) {
                btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                setButtonOnTableView(btndelete, EZButtonType.BTN_DELETE);
                btndelete.setId(""+no);
                btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                setButtonOnTableView(btnedit, EZButtonType.BTN_EDIT);
                btnedit.setId("" + no);
            }
            RecPOModel model = new RecPOModel("" + no, tResult.getString(V_All_Purchase_detail.IDITEM.get()),
                    tResult.getString(V_All_Purchase_detail.ITEMNAME.get()), tResult.getString(V_All_Purchase_detail.SATUAN.get()),
                    formatRupiah(tResult.getDouble(V_All_Purchase_detail.BUYPRICE.get())),
                    tResult.getString(V_All_Purchase_detail.QTY.get()), formatRupiah(tResult.getDouble(V_All_Purchase_detail.TOTAL.get())),
                    tResult.getString(V_All_Purchase_detail.QTY.get()), btnedit, btndelete);
            itemList.add(model);
            no++;
        }
        loadItemToTable();
    }
    
    private void loadDataEdit(int index){
        mIndexEdit = index;
        RecPOModel tModel = itemList.get(index);
        txt_kode_item.setText(tModel.getItem_code());
        txt_nama_item.setText(tModel.getItem_name());
        txt_harga.setText(formatRupiah(Double.parseDouble(tModel.getBuy_price().replace(",", ""))));
        lbl_satuan.setText("/ "+tModel.getDescrip());
        txt_qty.setText(tModel.getQty());
        txt_qty_terima.setText(tModel.getTerima());
        txt_qty_terima.requestFocus();
        txt_qty_terima.setEditable(true);
        txt_harga.setEditable(true);
        mModel = tModel; 
    }

    @Override
    public void initializeState() {
        setDate(dt_trans);
        setDate(dt_trans_rec);
        cmb_tipe_bayar.setItems(typeBayarList);
        cmb_tipe_bayar.getSelectionModel().select(0);
        txt_kode_item.setEditable(false);
        txt_nama_item.setEditable(false);
        txt_qty.setEditable(false);
        txt_harga.setEditable(false);
        dt_trans.setEditable(false);
        loadData();
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            tbl_trans.requestFocus();
        });
        
        btn_batal.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> opt = EZSystem.showAllert(alert,"Batalkan Transaksi?", EZIcon.ICON_STAGE);
                if (opt.get() ==  ButtonType.OK) {
                    clear();
                    btn_kembali.fire();
                }
        });
        
        txt_qty_terima.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!(txt_harga.getText().equals("")) & !(txt_harga.getText().equals("0")) & !(txt_qty_terima.getText().equals("0")) & !(txt_qty_terima.getText().equals("")) & !(txt_qty_terima.getText().length()==0)) {
                    if(!(Integer.parseInt(txt_qty_terima.getText())>Integer.parseInt(txt_qty.getText()))){
                        int tQty = Integer.parseInt(txt_qty_terima.getText());
                        double tHarga = Double.parseDouble(txt_harga.getText().replace(",", ""));
                        double tTotal = tQty*tHarga;
                        RecPOModel model = mModel;
                        model.setBuy_price(txt_harga.getText());
                        model.setTerima(txt_qty_terima.getText());
                        model.setTotal_harga(formatRupiah(tTotal));
                        itemList.set(mIndexEdit, model);
                        loadItemToTable();
                        clearForm();
                        mModel = null;
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        EZSystem.showAllert(alert, "Qty Terima lebih besar dari Qty order!", EZIcon.ICON_STAGE);
                        txt_qty_terima.requestFocus();
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    EZSystem.showAllert(alert, "Isi data dengan benar!", EZIcon.ICON_STAGE);
                    txt_qty_terima.requestFocus();
                }
            }
        });
        
        txt_faktur.setOnAction((event) -> {
            txt_penerim.requestFocus();
        });
        
        txt_penerim.setOnAction((event) -> {
           btn_simpan.fire();
        });
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> option = EZSystem.showAllert(alert, "Simpan data receiving ?", EZIcon.ICON_STAGE);
            if (option.get() == ButtonType.OK) {
                simpan();
            }
        });
        
        btn_kembali.setOnAction((ActionEvent event)->{
            try {
                getmHomeController().performLoadpurchase(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
    }

    @Override
    public void setToolTip() {
        btn_simpan.setCursor(Cursor.HAND);
        btn_batal.setCursor(Cursor.HAND);
        btn_kembali.setCursor(Cursor.HAND);
        txt_po.setEditable(false);
        txt_no_rec.setEditable(false);
        txt_supplier.setEditable(false);
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 220);
        setNumericTextfield(txt_harga);
        setNumericTextfield(txt_qty);
        setNumericTextfield(txt_qty_terima);
        setCurencyTextfield(txt_harga);
        setAligmentColoum(clm_harga, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_qty, Pos.CENTER);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_qty_terima, Pos.CENTER);
        setAligmentColoum(clm_kode_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        setNumericTextfield(txt_qty);
        setNumericTextfield(txt_harga);
        setCurencyTextfield(txt_harga);
        double lebar = getWidth() - 200;
        clm_nama_item.setPrefWidth((lebar * 30)/100);
        clm_kode_item.setPrefWidth((lebar * 10)/100);
        clm_sat.setPrefWidth((lebar * 5)/100);
        clm_harga.setPrefWidth((lebar * 10)/100);
        clm_qty.setPrefWidth((lebar * 5)/100);
        clm_qty_terima.setPrefWidth((lebar * 5)/100);
        clm_total.setPrefWidth((lebar * 10)/100);
        clm_delete.setPrefWidth((lebar * 10)/100);
        clm_edit.setPrefWidth((lebar * 8)/100);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
