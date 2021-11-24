/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import static app.retail.controller.general.General.getStackTraceString;
import app.retail.model.pos.POSBayarModel;
import app.retail.model.pos.POSModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Sale;
import app.retail.utility.table.Sale_Detail;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormBayarController extends AbstractPOS implements Initializable {

    private static POSController mPOSControler;
    private static String tagihan, total_tagihan;
    private static Map<String, Double> mapDiscount;
    private static ObservableList<POSModel> itemList;
    private static final ObservableList<POSBayarModel> litsTypeBayar = FXCollections.observableArrayList();
    private static final List<String> listBayar = new ArrayList<>();
    private static double sisa = 0;
   
    @FXML
    private JFXButton btn_lunas;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private Text lbl_kode_trans;
    @FXML
    private Text lbl_sisa_bayar;
    @FXML
    private Text lbl_total_tagihan;
    @FXML
    private ComboBox<String> cmb_type_bayar;
    @FXML
    private TextField txt_no_kartu;
    @FXML
    private TableView<POSBayarModel> tbl_list_bayar;
    @FXML
    private TableColumn<POSBayarModel, String> clm_type_bayar;
    @FXML
    private TableColumn<POSBayarModel, String> clm_total;
    @FXML
    private TableColumn<POSBayarModel, JFXButton> clm_delete;
    @FXML
    private TableColumn<POSBayarModel, String> clm_ket;
    @FXML
    private Text lbl_kode_customer;
    @FXML
    private Text lbl_nama_customer;
    @FXML
    private Text lbl_piutang;
    @FXML
    private Text lbl_kode_trans1111;
    @FXML
    private Text lbl_poin;
    @FXML
    private TextField txt_bayar;
    @FXML
    private Text status_byr;
    @FXML
    private Text lbl_jdl_piutang;
    @FXML
    private Text lbl_noKartu;
    @FXML
    private JFXButton btn_pas;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeState();
        setButtonListener();
        setToolTip();
    }
    
    private String generateRefnum() throws SQLException{
        String tDate = EZDate.FAKTUR.today();
        String tInitial = mMapKaryawan.get("initial").toUpperCase();
        String tKdAwal = tInitial+tDate;
        String tFaktur;
        
        String tSQL = "SELECT kode_trans FROM `sale` WHERE kode_trans like '"+tKdAwal+"%' ORDER BY kode_trans DESC LIMIT 1";
        
        ResultSet tRes = selectFromDatabase(tSQL);
        if(tRes.next()){
            String tLastFaktur = tRes.getString("kode_trans");
            int number = Integer.parseInt(tLastFaktur.substring(tKdAwal.length(), tLastFaktur.length())) + 1;
            String newNumber = String.valueOf(number);
            if(newNumber.length() > 5){
                EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), "Faktur Overload", EZIcon.ICON_APPS);
                tFaktur = tKdAwal+"00000";
            }else{
                while (newNumber.length() < 5) {
                    newNumber = "0"+newNumber;
                }
                
                tFaktur = tKdAwal+newNumber;
            }
        }else{
            tFaktur = tKdAwal+"00001";
        }
        
        return tFaktur;
    }
    
    @Override
    public void clear() {
        Stage stage = (Stage) btn_simpan.getScene().getWindow();
        mPOSControler.initializeState();
        stage.close();
    }

    public void simpan() {
        try {
            RecordEntry MAPSALE = new RecordEntry();
            MAPSALE.createEntry(Sale.KODETRANS.get(), lbl_kode_trans.getText());
            MAPSALE.createEntry(Sale.TRANDATE.get(), EZDate.SQLDATETIME.today());
            
            String tType = "rp";
            double disc = 0;
            for (String key : mapDiscount.keySet()) {
                tType = key;
                disc = mapDiscount.get(key);
            }
            MAPSALE.createEntry(Sale.DISC.get(), tType.equalsIgnoreCase("%") ? String.valueOf(disc) : "0");
            MAPSALE.createEntry(Sale.DISCAMMOUNT.get(), tType.equalsIgnoreCase("rp") ? String.valueOf(disc) : "0");
            MAPSALE.createEntry(Sale.TOTAL.get(), getTagihan().replace("Rp. ", "").replace(",", ""));
            MAPSALE.createEntry(Sale.GRANDTOTAL.get(), getTotal_tagihan().replace("Rp. ", "").replace(",", ""));
            MAPSALE.createEntry(Sale.SID.get(), getSession());
            MAPSALE.createEntry(Sale.KET.get(), constructDescription());
            MAPSALE.createEntry(Sale.IDPELANGGAN.get(), mMapPelanggan.get("kd_pelanggan"));
            MAPSALE.createEntry(Sale.IDKARYAWAN.get(), mMapKaryawan.get("kode"));
            MAPSALE.createEntry(Sale.SHIFT.get(), mMapKaryawan.get("kode_shift"));
            
            insertToDatabase(MAPSALE, Sale.TABLENAME.get());
            
            for (POSModel model : getItemList()) {
                RecordEntry tMap = new RecordEntry();
                tMap.createEntry(Sale_Detail.AUTOID.get(), genereateID(20));
                tMap.createEntry(Sale_Detail.KODETRANS.get(), lbl_kode_trans.getText());
                tMap.createEntry(Sale_Detail.KODEITEM.get(), model.getIDITEM());
                tMap.createEntry(Sale_Detail.HARGA.get(), model.getHARGAJUAL().replace(",", ""));
                tMap.createEntry(Sale_Detail.QTY.get(), model.getQTY());
                tMap.createEntry(Sale_Detail.SISA.get(), model.getQTY());
                tMap.createEntry(Sale_Detail.DISC.get(), model.getDISC());
                tMap.createEntry(Sale_Detail.TOTAL.get(), model.getTOTAL().replace(",", ""));
                insertToDatabase(tMap, Sale_Detail.TABLENAME.get());
            }
            
            for (POSBayarModel model : tbl_list_bayar.getItems()) {
                if(model.getType_bayar().equalsIgnoreCase("PIUTANG")){
                    String tSQL = "CALL insertPiutang ('"
                            +lbl_kode_trans.getText()+"', '"
                            +mMapPelanggan.get("kd_pelanggan")+"', '"
                            + model.getTotal_bayar().replace(",", "")
                            +"', '"+getSession()+"')";
                    updateToDatabase(tSQL);
                }
            }
            
            Optional<ButtonType> opt = EZSystem.showAllert(EZAlertType.CONFIRM, "Transaksi Telah Disimpan\nCetak Struk ?", EZIcon.ICON_APPS);
            
            if(opt.get() == ButtonType.OK){
                clear();
            }else{
                clear();
            }

        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    private String constructDescription(){
        String tDescription = "";
        ObservableList<POSBayarModel> list = tbl_list_bayar.getItems();
        if(list.size()>0){
            tDescription = list.stream().map(pOSBayarModel -> 
                    "["+pOSBayarModel.getType_bayar()+":"
                            +pOSBayarModel.getTotal_bayar().replace(",", "")+":"
                            +pOSBayarModel.getKet().replace(",", "")+"];")
                    .reduce(tDescription, String::concat);
        }else{
            tDescription = "[CASH:"+getTotal_tagihan().replace(",", "")+"];";
        }
        return tDescription;
    }
    
    @Override
    public void loadForm(String pUrl, String pName, javafx.event.ActionEvent event) {        
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pUrl));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            root.setOnMousePressed((MouseEvent e) -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent e) -> {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {
        setNumericTextfield(txt_bayar);
        
        Platform.runLater(()->{
            txt_bayar.requestFocus();
        });
        
        btn_batal.setOnMouseClicked((MouseEvent event) -> {
            Stage stage = (Stage) btn_batal.getScene().getWindow();
            stage.close();
        });
        
        btn_lunas.setOnMouseClicked((MouseEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> opt = EZSystem.showAllert(alert, "Pembayaran cash lunas?", EZIcon.ICON_APPS);
            if(opt.get() == ButtonType.OK){
                txt_bayar.setText(lbl_total_tagihan.getText().replace("Rp." , ""));
                tbl_list_bayar.getItems().clear();
                lbl_sisa_bayar.setText("Rp. 0");
                status_byr.setText("Kembali");
                simpan();
            }
        });
        
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> opt = EZSystem.showAllert(alert, "Simpan Pembayaran ?", EZIcon.ICON_STAGE);
            if(opt.get() == ButtonType.OK){
                if (sisa>0) {
                    alert = new Alert(Alert.AlertType.WARNING);
                    EZSystem.showAllert(alert, "Pembayaran belum lunas!", EZIcon.ICON_STAGE);
                }else{
                    simpan();
                }
            }
        });
        
        cmb_type_bayar.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                lbl_noKartu.setVisible(cmb_type_bayar.getSelectionModel().getSelectedItem().equalsIgnoreCase("CREDIT") ||
                    cmb_type_bayar.getSelectionModel().getSelectedItem().equalsIgnoreCase("DEBET"));
                txt_no_kartu.setVisible(cmb_type_bayar.getSelectionModel().getSelectedItem().equalsIgnoreCase("CREDIT") ||
                    cmb_type_bayar.getSelectionModel().getSelectedItem().equalsIgnoreCase("DEBET"));
                txt_bayar.requestFocus();
        });
        
        btn_pas.setOnAction(((event) -> {
            txt_bayar.setText(formatRupiah(sisa));
            txt_bayar.fireEvent(event);
        }));
        
        txt_bayar.setOnAction((event) -> {
            if (!txt_bayar.getText().equals("0")) {
                String tTypeBayar = cmb_type_bayar.getSelectionModel().getSelectedItem();
                if (listBayar.contains(tTypeBayar)) {
                    EZSystem.showAllert(EZAlertType.WARNING, "Type Pembayaran Sudah digunakan!", EZIcon.ICON_STAGE);
                } else {
                    if (tTypeBayar.equalsIgnoreCase("CASH")) {
                        executePembayaranCash();
                    } else {
                        if (tTypeBayar.equalsIgnoreCase("CREDIT") || tTypeBayar.equalsIgnoreCase("CREDIT")) {
                            if (!txt_no_kartu.getText().equals("")) {
                                if (validatePembayaran()) {
                                    loadtoTablePembayaran(tTypeBayar);
                                } else {
                                    EZSystem.showAllert(EZAlertType.WARNING, "Pembayaran type melebihi sisa tagihan!", EZIcon.ICON_STAGE);
                                }
                            } else {
                                EZSystem.showAllert(EZAlertType.WARNING, "Masukan Nomor Kartu!", EZIcon.ICON_STAGE);
                                txt_no_kartu.requestFocus();
                            }
                        }else if(tTypeBayar.equalsIgnoreCase("PIUTANG")){
                            if (validatePembayaran()) {
                                loadtoTablePembayaran(tTypeBayar);
                            } else {
                                EZSystem.showAllert(EZAlertType.WARNING, "Pembayaran type melebihi sisa tagihan!", EZIcon.ICON_STAGE);
                            }
                        }
                    }
                }
            }else{
                EZSystem.showAllert(EZAlertType.WARNING, "Masukan Pembayaran!", EZIcon.ICON_STAGE);
            }
        });
        
        txt_no_kartu.setOnAction((event)->{
            txt_bayar.requestFocus();
        });
    }
    
    private void executePembayaranCash(){
        loadtoTablePembayaran("CASH");
    }
    
    private boolean validatePembayaran(){
        double bayar = Double.parseDouble(txt_bayar.getText().replace(",", ""));
        return bayar <= sisa;
    }
    
    private void loadtoTablePembayaran(){
        setValueColum();
        tbl_list_bayar.setItems(litsTypeBayar);
        txt_bayar.setText("0");
        txt_bayar.requestFocus();
        hitungSisaBayar();
    }
    
    private void loadtoTablePembayaran(String typeBayar){
        String totalBayar = txt_bayar.getText();
        String tKet = txt_no_kartu.getText();
        JFXButton btn = getButton(EZButtonType.BTN_DELETE, "Hapus");
        btn.setId(""+tbl_list_bayar.getItems().size());
        btn.setOnAction((event) -> {
           Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Hapus Data Pembayaran ?", EZIcon.ICON_APPS);
           if(opt.get() == ButtonType.OK){
               int index = Integer.parseInt(btn.getId());
               POSBayarModel model = litsTypeBayar.get(index);
               listBayar.remove(model.getType_bayar());
               litsTypeBayar.remove(index);
               loadtoTablePembayaran();
           }
        });
        POSBayarModel model = new POSBayarModel(typeBayar, totalBayar, tKet, btn);
        litsTypeBayar.add(model);
        setValueColum();
        tbl_list_bayar.setItems(litsTypeBayar);
        txt_bayar.setText("0");
        txt_bayar.requestFocus();
        listBayar.add(typeBayar);
        hitungSisaBayar();
    }
    
    private void setValueColum() {
        clm_type_bayar.setCellValueFactory(new PropertyValueFactory<>("type_bayar"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("total_bayar"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btn_delete"));
        clm_ket.setCellValueFactory(new PropertyValueFactory<>("ket"));
    }
    
    private void hitungSisaBayar(){
        double totalBayar = Double.parseDouble(hitungGrandTotal(tbl_list_bayar, 1).replace(",", ""));
        sisa = Double.parseDouble(getTotal_tagihan().replace(",", "")) - totalBayar;
        lbl_sisa_bayar.setText("Rp. "+formatRupiah(sisa));
        txt_no_kartu.setText("");
        
        if(sisa <= 0){
            double tkembali = Math.abs(sisa);
            lbl_sisa_bayar.setText("Rp. "+formatRupiah(tkembali));
            status_byr.setText("Kembali");
            
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Pembayaran Lunas, Simpan transaksi ?", EZIcon.ICON_STAGE);
            if(opt.get() == ButtonType.OK){
                simpan();
            }
        }
    }
    
    @Override
    public void setDate(JFXDatePicker pDatePicker) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDate(JFXDatePicker pDatePicker, Date pDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static POSController getmPOSControler() {
        return mPOSControler;
    }

    public static void setmPOSControler(POSController mPOSControler) {
        FormBayarController.mPOSControler = mPOSControler;
    }
    
    private void setTypeBayar(){
        lbl_kode_customer.setText(mMapPelanggan.get("kd_pelanggan"));
        lbl_nama_customer.setText(mMapPelanggan.get("nm_pelanggan"));
        lbl_piutang.setText(mMapPelanggan.get("piutang"));
        
        ObservableList<String> listTypeBayar = FXCollections.observableArrayList();
        listTypeBayar.add("CASH");
        listTypeBayar.add("CREDIT");
        listTypeBayar.add("DEBET");
        if(!mMapPelanggan.get("kd_pelanggan").equals("0000")){
            listTypeBayar.add("PIUTANG");
        }
        
        cmb_type_bayar.setItems(listTypeBayar);
        cmb_type_bayar.getSelectionModel().select(0);
        lbl_noKartu.setVisible(false);
        txt_no_kartu.setVisible(false);
    }

    @Override
    public void initializeState() {
        try {
            lbl_kode_trans.setText(generateRefnum());
            lbl_total_tagihan.setText("Rp. "+getTotal_tagihan());
            txt_bayar.setText("0");
            status_byr.setText("Sisa Bayar");
            lbl_sisa_bayar.setText("Rp. "+getTotal_tagihan());
            status_byr.setText("Sisa Bayar");
            sisa = Double.parseDouble(getTotal_tagihan().replace(",", ""));
            listBayar.clear();
            litsTypeBayar.clear();
            setTypeBayar();
            setToolTip();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setToolTip() {
        btn_batal.setCursor(Cursor.HAND);
        btn_lunas.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
        setCurencyTextfield(txt_bayar);
        setAligmentColoum(clm_ket, Pos.CENTER_LEFT);
        setAligmentColoum(clm_type_bayar, Pos.CENTER_LEFT);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
    }
    
    @Override
    public void loggingerror(Exception ex){
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }

    public static String getTagihan() {
        return tagihan;
    }

    public static void setTagihan(String tagihan) {
        FormBayarController.tagihan = tagihan;
    }

    public static ObservableList<POSModel> getItemList() {
        return itemList;
    }

    public static void setItemList(ObservableList<POSModel> itemList) {
        FormBayarController.itemList = itemList;
    }

    public static Map<String, Double> getMapDiscount() {
        return mapDiscount;
    }

    public static void setMapDiscount(Map<String, Double> mapDiscount) {
        FormBayarController.mapDiscount = mapDiscount;
    }

    public static String getTotal_tagihan() {
        return total_tagihan;
    }

    public static void setTotal_tagihan(String total_tagihan) {
        FormBayarController.total_tagihan = total_tagihan;
    }
}
