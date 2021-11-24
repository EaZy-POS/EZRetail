/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.finance;

import app.retail.controller.general.MapKaryawan;
import app.retail.controller.home.HomeController;
import app.retail.model.finance.CashTransactionModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Cash_Transaction;
import app.retail.utility.table.Cash_Transaction_Detail;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
public class FormInputCashTransactionController extends AbstractFinance implements Initializable {

    private final ObservableList<String> statusList = javafx.collections.FXCollections.observableArrayList("-- Tipe Trans --","IN","OUT");
    private final ObservableList<CashTransactionModel> cashTransList = javafx.collections.FXCollections.observableArrayList();
    private JFXButton btndelet;
    private static final HashMap<String,CashTransactionModel> MAPTRANS = new HashMap<>();
    private static String mID;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Text lbl_total_trans;
    @FXML
    private JFXDatePicker dt_trans;
    @FXML
    private JFXTextArea txt_ket;
    @FXML
    private JFXTextField txt_deskripsi;
    @FXML
    private JFXTextField txt_faktur;
    @FXML
    private JFXTextField txt_jumlah;
    @FXML
    private TableView<CashTransactionModel> tbl_trans;
    @FXML
    private TableColumn<CashTransactionModel,String> clm_no;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_desc;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_faktur;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_jumlah;
    @FXML
    private TableColumn<CashTransactionModel, JFXButton> clm_delete;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXComboBox<String> cmb_tipe;
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
        setButtonListener();
        initializeState();
    }    

    @Override
    public void clear() {
        setDate(dt_trans);
        txt_deskripsi.setText("");
        txt_faktur.setText("");
        txt_jumlah.setText("0");
        txt_ket.setText("");
        MAPTRANS.clear();
        tbl_trans.getItems().clear();
    }

    public void simpan() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> opt = EZSystem.showAllert(alert, "Simpan data transaksi?", EZIcon.ICON_STAGE);
        if (opt.get() == ButtonType.OK) {
            try {
                RecordEntry tMapCash = new RecordEntry();
                tMapCash.createEntry(Cash_Transaction.ID.get(), mID);
                tMapCash.createEntry(Cash_Transaction.TRANDATE.get(), getDate(dt_trans));
                tMapCash.createEntry(Cash_Transaction.TYPE.get(), cmb_tipe.getSelectionModel().getSelectedItem());
                tMapCash.createEntry(Cash_Transaction.TOTAL.get(), lbl_total_trans.getText().replace("Rp. ", "").replace(",", ""));
                String kodeKar = mMapKaryawan.get(MapKaryawan.KODEKARYAWAN.get());
                tMapCash.createEntry(Cash_Transaction.INPUTBY.get(), kodeKar);
                tMapCash.createEntry(Cash_Transaction.KET.get(), txt_ket.getText());
                tMapCash.createEntry(Cash_Transaction.CDT.get(), getDate(dt_trans));
                tMapCash.createEntry(Cash_Transaction.SID.get(), HomeController.getSession());
                int x = insertToDatabase(tMapCash, Cash_Transaction.TABLENAME.get());
                if (x>0) {
                    simpan(Cash_Transaction_Detail.TABLENAME.get());
                }
            } catch (SQLException ex) {
                loggingerror(ex);
            }            
        }
    }
    
    private String getQuery(){
        String tSQL = "SELECT V.id , V.type , v.total , V.ket,V.tran_date, V.nama ,D.faktur, D.ket as desk, D.total as nilai "
                + "FROM v_cash_transaction V INNER JOIN cash_transaction_detail D on D.id_trans=V.id "
                + "WHERE V.id='"+mID+"'";
        return tSQL;
    }
    
    private void simpan(String pTable) throws SQLException{
        int x = 0;
        for (CashTransactionModel model : tbl_trans.getItems()) {
            RecordEntry tMapDetail = new RecordEntry();
            tMapDetail.createEntry(Cash_Transaction_Detail.IDTRANS.get(), mID);
            tMapDetail.createEntry(Cash_Transaction_Detail.TOTAL.get(), model.getTotal_detail().replace(",", ""));
            tMapDetail.createEntry(Cash_Transaction_Detail.FAKTUR.get(), model.getFaktur());
            tMapDetail.createEntry(Cash_Transaction_Detail.KET.get(), model.getDeskrip());
            x = x + insertToDatabase(tMapDetail, pTable);
        }
        if (x > 0) {
            EZSystem.showAllert(EZAlertType.INFO, "Data berhasil disimpan", EZIcon.ICON_STAGE);
            showReport("report/finance/cash/faktur_cash_transaction.jrxml", getQuery());
            initializeState();
        }
    }

    public void loadData() {
        if (validateForm()) {
            String no = ""+(tbl_trans.getItems().size()+1);
            String desc = txt_deskripsi.getText();
            String faktur = txt_faktur.getText();
            String jml = txt_jumlah.getText();
            btndelet = getButton(EZButtonType.BTN_DELETE, "Delete");
            btndelet.setId(no);
            setButtonOnTableView(btndelet, EZButtonType.BTN_DELETE);
            CashTransactionModel tModel = new CashTransactionModel(no, jml, faktur, desc, btndelet);
            MAPTRANS.put(no, tModel);
            loadDataToTable();
            txt_deskripsi.setText("");
            txt_faktur.setText("");
            txt_jumlah.setText("0");
            txt_deskripsi.requestFocus();
        }
    }
    
    private void loadDataToTable(){
        tbl_trans.getItems().clear();
        MAPTRANS.values().stream().map((model) -> {
            cashTransList.add(model);
            return model;
        }).map((_item) -> {
            setValueColum();
            return _item;
        }).forEach((_item) -> {
            tbl_trans.setItems(cashTransList);
        });        
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> option = EZSystem.showAllert(alert, "Yakin hapus item ?", EZIcon.ICON_STAGE);
            if (option.get() == ButtonType.OK) {
                String index = button.getId();
                MAPTRANS.remove(index);
                resetNoModel();
                loadDataToTable();
                lbl_total_trans.setText("Rp. "+hitungGrandTotal(tbl_trans, 3));
            }
        });
    }
    
    private void resetNoModel(){
        int no=0;
        for (CashTransactionModel model : MAPTRANS.values()) {
            no++;
            model.setNo(""+no);
        }
    }
    
    private boolean validateForm(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (cmb_tipe.getSelectionModel().getSelectedIndex()==0) {
            EZSystem.showAllert(alert, "Pilih Tipe Transaksi!", EZIcon.ICON_STAGE);
            return false;
        }
        
        if (txt_deskripsi.getText().equals("")) {
            EZSystem.showAllert(alert, "Deskripsi belum diisi!", EZIcon.ICON_STAGE);
            return false;
        }
        
        if (txt_jumlah.getText().equals("0")) {
            EZSystem.showAllert(alert, "Jumlah belum diisi!", EZIcon.ICON_STAGE);
            return false;
        }
        return true;
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_desc.setCellValueFactory(new PropertyValueFactory<>("deskrip"));
        clm_faktur.setCellValueFactory(new PropertyValueFactory<>("faktur"));
        clm_jumlah.setCellValueFactory(new PropertyValueFactory<>("total_detail"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    @Override
    public void initializeState() {
        cmb_tipe.setItems(statusList);
        cmb_tipe.getSelectionModel().select(0);
        lbl_total_trans.setText("Rp. 0");
        txt_jumlah.setText("0");
        mID = "CF/"+EZDate.SHORTDATE.today()+"-"+genereateID(6);
        clear();
    }

    @Override
    public void setButtonListener() {
        setDate(dt_trans);
        setCurencyTextfield(txt_jumlah);
        
        txt_jumlah.setOnAction((ActionEvent event) -> {
            loadData();
            lbl_total_trans.setText("Rp. "+hitungGrandTotal(tbl_trans, 3));
        });
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            simpan();
        });
        
        btn_batal.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> opt = EZSystem.showAllert(alert, "Batalkan Transaksi?", EZIcon.ICON_STAGE);
            if (opt.get() ==  ButtonType.OK) {
                initializeState();
            }
        });
        
        btn_kembali.setOnAction((ActionEvent event)->{
            try {
                setmUrlChild("/app/retail/fxml/finance/CashTransaction.fxml");
                getmHomeController().performLoadFinance(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        Platform.runLater(()->{
            txt_deskripsi.requestFocus();
        });
        
        txt_ket.setOnKeyPressed((KeyEvent evt)->{
            if (evt.getCode() == KeyCode.TAB) {
                txt_deskripsi.requestFocus();
            }
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 220);
        setAligmentColoum(clm_desc, Pos.CENTER_LEFT);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_faktur, Pos.CENTER);
        setAligmentColoum(clm_jumlah, Pos.CENTER_RIGHT);
        btn_simpan.setCursor(Cursor.HAND);
        btn_batal.setCursor(Cursor.HAND);
        btn_kembali.setCursor(Cursor.HAND);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
