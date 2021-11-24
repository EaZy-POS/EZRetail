/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.finance;

import app.retail.model.finance.HistoryBayarPiutangModel;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Bayar_Piutang;
import app.retail.utility.table.V_Bayar_Piutang;
import app.retail.utility.table.V_Piutang;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormBayarPiutangController extends AbstractFinance implements Initializable {

    private static String mKodeHutang;
    private final ObservableList<HistoryBayarPiutangModel> hystoryList = javafx.collections.FXCollections.observableArrayList();
    private static boolean isView;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Label lbl_judul;
    @FXML
    private Label lbl_kode_trans;
    @FXML
    private Label lbl_faktur;
    @FXML
    private Label lbl_nm_pelanggan;
    @FXML
    private Label lbl_tanggal;
    @FXML
    private Label lbl_total;
    @FXML
    private Label lbl_terbayar;
    @FXML
    private Label lbl_sisa_bayar;
    @FXML
    private TableView<HistoryBayarPiutangModel> tbl_history;
    @FXML
    private TableColumn<HistoryBayarPiutangModel,String> clm_no;
    @FXML
    private TableColumn<HistoryBayarPiutangModel,String> clm_tanggal;
    @FXML
    private TableColumn<HistoryBayarPiutangModel,String> clm_bukti_bayar;
    @FXML
    private TableColumn<HistoryBayarPiutangModel,String> clm_total_bayar;
    @FXML
    private VBox vbox_bayar;
    @FXML
    private JFXDatePicker dt_bayar;
    @FXML
    private JFXTextField txt_bayar;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_batal;
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
        if (isView) {
            lbl_judul.setText("DATA PIUTANG");
            vbox_bayar.setVisible(false);
            lbl_kode_trans.setVisible(false);
        }else{
            lbl_judul.setText("PEMBAYARAN PIUTANG");
            vbox_bayar.setVisible(true);
            lbl_kode_trans.setVisible(true);
        }
        setDate(dt_bayar);
        setToolTip();
        setButtonListener();
        initializeState();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void simpan() {
        if (validatePembayaran()) {
                if(!txt_bayar.getText().equals("0")){
                    try {
                        String kdTrans = lbl_kode_trans.getText();
                        String id = mKodeHutang;
                        String tgl = getDate(dt_bayar);
                        String ttl = txt_bayar.getText().replace(",", "");
                        RecordEntry tMap = new RecordEntry();
                        tMap.createEntry(Bayar_Piutang.IDTRANS.get(), kdTrans);
                        tMap.createEntry(Bayar_Piutang.IDPIUTANG.get(), id);
                        tMap.createEntry(Bayar_Piutang.TRANDATE.get(), tgl);
                        tMap.createEntry(Bayar_Piutang.TOTAL.get(), ttl);
                        tMap.createEntry(Bayar_Piutang.INPUTDATE.get(), EZDate.SQLDATETIME.today());
                        tMap.createEntry(Bayar_Piutang.SID.get(), getSession());
                        insertToDatabase(tMap, Bayar_Piutang.TABLENAME.get());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_APPS);
                        done();
                    } catch (SQLException ex) {
                        loggingerror(ex);
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    EZSystem.showAllert(alert, "Isi nominal bayar!", EZIcon.ICON_APPS);
                    txt_bayar.requestFocus();
                }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            EZSystem.showAllert(alert, "Pembayaran melebihi sisa hutang!", EZIcon.ICON_APPS);
        }
    }
    
    private boolean validatePembayaran(){
        double sisa = Double.parseDouble(lbl_sisa_bayar.getText().replace(",", ""));
        double bayar = Double.parseDouble(txt_bayar.getText().replace(",", ""));
        return bayar<=sisa;
    }
    
    private void done(){
        btn_kembali.fire();
    }

    public void loadData() {
        try {
            String tSQL = "SELECT * FROM "+V_Piutang.TABLENAME.get()+" WHERE "+V_Piutang.IDTRANS.get()+"='"+mKodeHutang+"'";
            ResultSet tResult = selectFromDatabase(tSQL);
            if (tResult.next()) {
                lbl_faktur.setText(tResult.getString(V_Piutang.FAKTUR.get()));
                lbl_nm_pelanggan.setText(tResult.getString(V_Piutang.PELANGGAN.get()));
                lbl_sisa_bayar.setText(formatRupiah(tResult.getDouble(V_Piutang.SISA.get())));
                lbl_tanggal.setText(EZDate.FORMAT_4.get(tResult.getDate(V_Piutang.TRANDATE.get())));
                lbl_terbayar.setText(formatRupiah(tResult.getDouble(V_Piutang.TERBAYAR.get())));
                lbl_total.setText(formatRupiah(tResult.getDouble(V_Piutang.TOTAL.get())));
                loadHistoryBayar(mKodeHutang);
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    private void loadHistoryBayar(String id) throws SQLException{
        String tSQL = "SELECT * FROM "+V_Bayar_Piutang.TABLENAME.get()+" WHERE "+V_Bayar_Piutang.ID.get()+"='"+id+"'";
        ResultSet tResult = selectFromDatabase(tSQL);
        int no = 0;
        while (tResult.next()) {            
            no++;
            HistoryBayarPiutangModel tModel = new HistoryBayarPiutangModel(""+no, 
                    EZDate.FORMAT_4.get(tResult.getDate(V_Bayar_Piutang.TGL.get())),
                    tResult.getString(V_Bayar_Piutang.BUKTI.get()), 
                    formatRupiah(tResult.getDouble(V_Bayar_Piutang.TOTAL.get())));
            hystoryList.add(tModel);
        }
        setValueColum();
        tbl_history.setItems(hystoryList);
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        clm_bukti_bayar.setCellValueFactory(new PropertyValueFactory<>("bukti"));
        clm_total_bayar.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    @Override
    public void setDate(JFXDatePicker pDatePicker) {
        pDatePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(EZDate.FORMAT_2.get());
                @Override 
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override 
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            }
        );
        pDatePicker.setValue(LocalDate.now());
    }

    public static String getmKodeHutang() {
        return mKodeHutang;
    }

    public static void setmKodeHutang(String mKodeHutang) {
        FormBayarPiutangController.mKodeHutang = mKodeHutang;
    }

    public static boolean isIsView() {
        return isView;
    }

    public static void setIsView(boolean isView) {
        FormBayarPiutangController.isView = isView;
    }

    @Override
    public void initializeState() {
        lbl_kode_trans.setText(generateRefnum(Bayar_Piutang.TABLENAME.get(), Bayar_Piutang.IDTRANS.get(), "TRBP"));
        txt_bayar.setText("0");
        loadData();
    }

    @Override
    public void setButtonListener() {
        setNumericTextfield(txt_bayar);
        setCurencyTextfield(txt_bayar);
        
        Platform.runLater(()->{
            if(isView){
                tbl_history.requestFocus();
            }else{
                txt_bayar.requestFocus();
            }
        });
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> optional = EZSystem.showAllert(alert, "Simpan data pembayaran?", EZIcon.ICON_APPS);
            if (optional.get() == ButtonType.OK) {
                simpan();
            }
        });
        
        txt_bayar.setOnAction((ActionEvent event)->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> optional = EZSystem.showAllert(alert, "Simpan data pembayaran?", EZIcon.ICON_APPS);
            if (optional.get() == ButtonType.OK) {
                simpan();
            }
        });
        
        btn_kembali.setOnAction((ActionEvent event)->{
            try {
                setmUrlChild("/app/retail/fxml/finance/DataPiutang.fxml");
                getmHomeController().performLoadFinance(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth()-200);
        AnchorPane.setPrefHeight(getHeight()-155);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_tanggal, Pos.CENTER);
        setAligmentColoum(clm_bukti_bayar, Pos.CENTER);
        setAligmentColoum(clm_total_bayar, Pos.CENTER_RIGHT);
        btn_batal.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
        txt_bayar.setAlignment(Pos.CENTER_RIGHT);
        btn_kembali.setCursor(Cursor.HAND);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
