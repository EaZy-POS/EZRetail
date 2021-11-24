/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.finance;

import app.retail.model.finance.PiutangModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_Piutang;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class DataPiutangController extends AbstractFinance implements Initializable {

    private final ObservableList<PiutangModel> piutangList = FXCollections.observableArrayList();
    private final ObservableList<String> statusList = FXCollections.observableArrayList("All","Belum Lunas","Lunas");
    private final ObservableList<String> limitlist = FXCollections.observableArrayList("100","200","300","400","500");
    private final ObservableList<String> pelangganList = FXCollections.observableArrayList();
    private JFXButton btnpay,btnview;
    private static String mQuery;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<PiutangModel> tbl_purchase;
    @FXML
    private TableColumn<PiutangModel,String> clm_no;
    @FXML
    private TableColumn<PiutangModel,String> clm_tanggl;
    @FXML
    private TableColumn<PiutangModel,String> clm_faktur;
    @FXML
    private TableColumn<PiutangModel,String> clm_pelanggan;
    @FXML
    private TableColumn<PiutangModel,String> clm_total;
    @FXML
    private TableColumn<PiutangModel,String> clm_status;
    @FXML
    private TableColumn<PiutangModel,JFXButton> clm_view;
    @FXML
    private TableColumn<PiutangModel,JFXButton> clm_pay;
    @FXML
    private JFXDatePicker dtPick_1;
    @FXML
    private JFXDatePicker dtPick_2;
    @FXML
    private JFXComboBox<String> cmb_status;
    @FXML
    private JFXComboBox<String> cmb_limit;
    @FXML
    private JFXComboBox<String> cmb_pelanggan;
    @FXML
    private Button btn_print;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(isAllowed(Akses_List.PRINT_DATA_PIUTANG)){
            btn_print.setDisable(true);
        }
        
        setToolTip();
        loadPelanggan();
        setButtonListener();
        initializeState();
        if(DataPiutangController.mListParameter != null){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(EZDate.SQLDATE.get());
                Date dateAwal = sdf.parse(mListParameter.get(mTglAwal));
                Date dateAkhir = sdf.parse(mListParameter.get(mTglAkhir));
                String tLimit = (String) mListParameter.get(mLimit);
                String tPelanggan = (String) mListParameter.get(mPelanggan);
                String tStatus = (String) mListParameter.get(mStatus);
                cmb_limit.getSelectionModel().select(tLimit);
                cmb_pelanggan.getSelectionModel().select(tPelanggan);
                cmb_status.getSelectionModel().select(tStatus);
                setDate(dtPick_1, dateAwal);
                setDate(dtPick_2, dateAkhir);
                loadData();
            } catch (ParseException ex) {
                loggingerror(ex);
            }
        }else{
            loadData();
        }
    }

    @Override
    public void clear() {
        cmb_status.setItems(statusList);
        cmb_limit.setItems(limitlist);
        cmb_status.getSelectionModel().select(0);
        cmb_limit.getSelectionModel().select(0);
        cmb_pelanggan.getSelectionModel().select(0);
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth()-250);
        AnchorPane.setPrefHeight(getHeight()-355);
        setAligmentColoum(clm_tanggl, Pos.CENTER_LEFT);
        setAligmentColoum(clm_faktur, Pos.CENTER_LEFT);
        setAligmentColoum(clm_pelanggan, Pos.CENTER_LEFT);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_status, Pos.CENTER);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        double lebar = getWidth() - 200;
        clm_no.setPrefWidth((lebar * 3)/100);
        clm_tanggl.setPrefWidth((lebar * 8)/100);
        clm_faktur.setPrefWidth((lebar * 12)/100);
        clm_pelanggan.setPrefWidth((lebar * 35)/100);
        clm_total.setPrefWidth((lebar * 10)/100);
        clm_faktur.setPrefWidth((lebar * 10)/100);
        clm_status.setPrefWidth((lebar * 8)/100);
        clm_view.setPrefWidth((lebar * 10)/100);
        clm_pay.setPrefWidth((lebar * 10)/100);
    }

    public void loadData() {
        if(isAllowed(Akses_List.DATA_PIUTANG)){
        tbl_purchase.getItems().clear();
        try {
            String tSQL = getQuery();
            ResultSet tResultSet = selectFromDatabase(tSQL);
            int no = 0;
            while (tResultSet.next()) {
                no++;
                String status; 
                
                if(tResultSet.getInt(V_Piutang.FLAG.get())==0){
                    status = "Belum Lunas";
                    if(isAllowed(Akses_List.VIEW_DATA_PIUTANG)){
                        btnview =  getButton(EZButtonType.BTN_VIEW, "View");
                        btnview.setId(tResultSet.getString(V_Piutang.IDTRANS.get()));
                        setButtonOnTableView(btnview, EZButtonType.BTN_VIEW);
                    }else{
                        btnview = null;
                    }
                    if(isAllowed(Akses_List.INPUT_PEMBAYARAN_PIUTANG)){
                        btnpay = getButton(EZButtonType.BTN_PAY, "Bayar");
                        btnpay.setId(tResultSet.getString(V_Piutang.IDTRANS.get()));
                        setButtonOnTableView(btnpay, EZButtonType.BTN_PAY);
                    }else{
                        btnpay=null;
                    }
                }else{
                    status = "Lunas";
                    if(isAllowed(Akses_List.VIEW_DATA_PIUTANG)){
                        btnview =  getButton(EZButtonType.BTN_VIEW, "View");
                        btnview.setId(tResultSet.getString(V_Piutang.IDTRANS.get()));
                        setButtonOnTableView(btnview, EZButtonType.BTN_VIEW);
                    }else{
                        btnview = null;
                    }
                }
                double total = tResultSet.getDouble(V_Piutang.TOTAL.get());
                Date trnDt = tResultSet.getDate(V_Piutang.TRANDATE.get());
                PiutangModel tModel = new PiutangModel(
                        ""+no, 
                        EZDate.FORMAT_2.get(trnDt), 
                        tResultSet.getString(V_Piutang.FAKTUR.get()), 
                        tResultSet.getString(V_Piutang.IDPELANGGAN.get()), 
                        tResultSet.getString(V_Piutang.PELANGGAN.get()),
                        formatRupiah(total),
                        tResultSet.getString(V_Piutang.IDTRANS.get()),
                        status,
                        btnpay, btnview);
                piutangList.add(tModel);
            }
            setValueColum();
            tbl_purchase.setItems(piutangList);
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        }
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

    private String getQuery(){
        String tSQL = "SELECT * FROM "+V_Piutang.TABLENAME.get()
                +" WHERE "+V_Piutang.TRANDATE.get()+" BETWEEN '"+getDate(dtPick_1)+" 00:00:00' AND '"+getDate(dtPick_2)+" 23:59:59' ";
        
        mQuery = "SELECT v_piutang.*, v_pelanggan.TELP, v_pelanggan.EMAIL, v_pelanggan.PEKERJAAN ,ID, TGL, v_bayar_piutang.TOTAL as JMLBAYAR " +
            "FROM v_piutang " +
            "INNER JOIN v_bayar_piutang ON v_bayar_piutang.IDPIUTANG=v_piutang.IDTRANS " +
            "INNER JOIN v_pelanggan ON v_pelanggan.IDPELANGGAN= v_piutang.IDPELANGGAN " +
            "WHERE TRANDATE BETWEEN '"+getDate(dtPick_1)+" 00:00:00' AND '"+getDate(dtPick_2)+" 23:59:59' ";
        
        if (cmb_status.getSelectionModel().getSelectedIndex()>0) {
            tSQL = tSQL.concat(" AND "+V_Piutang.FLAG.get()+"="+(cmb_status.getSelectionModel().getSelectedIndex()-1));
            mQuery = mQuery.concat(" AND "+V_Piutang.FLAG.get()+"="+(cmb_status.getSelectionModel().getSelectedIndex()-1));
            if (cmb_pelanggan.getSelectionModel().getSelectedIndex()>0) {
                String id = MAP_PELANGGAN.get(cmb_pelanggan.getSelectionModel().getSelectedItem());
                tSQL = tSQL.concat(" AND "+V_Piutang.IDPELANGGAN.get()+"='"+id+"'");
                mQuery = mQuery.concat(" AND "+V_Piutang.IDPELANGGAN.get()+"='"+id+"' ");
            }
        }else{
            if (cmb_pelanggan.getSelectionModel().getSelectedIndex()>0) {
                String id = MAP_PELANGGAN.get(cmb_pelanggan.getSelectionModel().getSelectedItem());
                tSQL = tSQL.concat(" AND "+V_Piutang.IDPELANGGAN.get()+"='"+id+"'");
                mQuery = mQuery.concat(" AND "+V_Piutang.TABLENAME.get()+"."+V_Piutang.IDPELANGGAN.get()+"='"+id+"' ");
            }
        }
        
        tSQL = tSQL.concat(" LIMIT "+ (cmb_limit.getSelectionModel().getSelectedItem() == null ? "100" : cmb_limit.getSelectionModel().getSelectedItem()));
        mQuery = mQuery.concat("ORDER BY TRANDATE, TGL ASC");
        return tSQL;
    }

    public void loadPelanggan() {
        List<String> tMap = getPelanggan();
        pelangganList.add("-- Pelanggan --");
        for (int i = 0; i < tMap.size(); i++) {
            String valString = tMap.get(i);
            pelangganList.add(valString);
        }
        cmb_pelanggan.getItems().clear();
        cmb_pelanggan.setItems(pelangganList);
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            if(type==EZButtonType.BTN_PAY || type==EZButtonType.BTN_VIEW){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                
                String tMessage;
                if (type==EZButtonType.BTN_PAY) {
                    tMessage = "Bayar Piutang ?";
                }else {
                    tMessage = "View Data Piutang ?";
                }
                
                Optional<ButtonType> option = EZSystem.showAllert(alert, tMessage, EZIcon.ICON_APPS);
                if (option.get()==ButtonType.OK) {
                    try {
                        setListParam();
                        FormBayarPiutangController.setmKodeHutang(button.getId());
                        final String tUrl = "/app/retail/fxml/finance/FormBayarPiutang.fxml";
                        if (type == EZButtonType.BTN_PAY) {
                            FormBayarPiutangController.setIsView(false);
                        } else {
                            FormBayarPiutangController.setIsView(true);
                        }
                        getmHomeController().loadForm(tUrl, tUrl, event);
                    } catch (IOException ex) {
                        loggingerror(ex);
                    }
                }
            }
        });
    }
    
    private void setListParam(){
        DataPiutangController.mListParameter = new HashMap<>();
        DataPiutangController.mListParameter.put(mTglAwal, getDate(dtPick_1));
        DataPiutangController.mListParameter.put(mTglAkhir, getDate(dtPick_2));
        DataPiutangController.mListParameter.put(mPelanggan, cmb_pelanggan.getSelectionModel().getSelectedItem());
        DataPiutangController.mListParameter.put(mLimit, cmb_limit.getSelectionModel().getSelectedItem());
        DataPiutangController.mListParameter.put(mStatus, cmb_status.getSelectionModel().getSelectedItem());
    }
    
    @Override
    public void loggingerror(Exception ex){
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_tanggl.setCellValueFactory(new PropertyValueFactory<>("tran_date"));
        clm_faktur.setCellValueFactory(new PropertyValueFactory<>("faktur"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        clm_pelanggan.setCellValueFactory(new PropertyValueFactory<>("pelanggan"));
        clm_status.setCellValueFactory(new PropertyValueFactory<>("flag"));
        clm_view.setCellValueFactory(new PropertyValueFactory<>("btnview"));
        clm_pay.setCellValueFactory(new PropertyValueFactory<>("btnpay"));
    }

    @Override
    public void initializeState() {
        clear();
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            tbl_purchase.requestFocus();
        });
        
        setDate(dtPick_1);
        setDate(dtPick_2);
        
        dtPick_1.valueProperty().addListener(
            (ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            loadData();
        });
        
        dtPick_2.valueProperty().addListener(
            (ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            loadData();
        });
        
        cmb_limit.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData();
        });
        
        cmb_status.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData();
        });
        
        cmb_pelanggan.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData();
        });
        
        btn_print.setOnAction((ActionEvent event) -> {
           Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak Data ?", EZIcon.ICON_APPS);
            if (opt.get() == ButtonType.OK) {
                if (tbl_purchase.getItems().size() > 0) {
                    showReport("report/finance/piutang/lap_piutang.jrxml", mQuery, getMapReport());
                } else {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Data tidak ada !", EZIcon.ICON_APPS);
                }
            }
        });
    }
    
    private Map<String, Object> getMapReport(){
        Map<String, Object> tMap = mMapProfile;
        try {
            String tSQL = "SELECT SUM(total) as TOTALPIUTANG, SUM(TERBAYAR) as TERBAYAR, SUM(SISA) as SISA "
                    + "from v_piutang WHERE TRANDATE BETWEEN '"+getDate(dtPick_1)+" 00:00:00' AND '"+getDate(dtPick_2)+" 23:59:59'";
            ResultSet tRes = selectFromDatabase(tSQL);
            if (tRes.next()) {
                tMap.put("totalpiutang", formatRupiah(tRes.getDouble("TOTALPIUTANG")));
                tMap.put("totalbayar", formatRupiah(tRes.getDouble("TERBAYAR")));
                tMap.put("totalsisa", formatRupiah(tRes.getDouble("SISA")));
            }else{
                tMap.put("totalhutang", 0);
                tMap.put("totalbayar", 0);
                tMap.put("totalsisa", 0);
            }
            
            tMap.put("periode", getDate(dtPick_1, EZDate.FORMAT_5)+" s/d "+getDate(dtPick_2, EZDate.FORMAT_5));
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        return tMap;
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
