/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.finance;

import static app.retail.controller.general.General.QS;
import static app.retail.controller.general.General.getStackTraceString;
import static app.retail.controller.general.General.mMapProfile;
import app.retail.model.finance.HistoryBayarHutangModel;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZReport;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Bayar_Hutang;
import app.retail.utility.table.V_All_Hutang;
import app.retail.utility.table.V_Bayar_Hutang;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
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
import net.sf.jasperreports.engine.JRException;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormBayarHutangController extends AbstractFinance implements Initializable {

    private static String mKodeHutang;
    private final ObservableList<HistoryBayarHutangModel> hystoryList = javafx.collections.FXCollections.observableArrayList();
    private static Map<String, Object> mMap;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Label lbl_kode_trans;
    @FXML
    private Label lbl_nm_supplier;
    @FXML
    private Label lbl_tanggal;
    @FXML
    private Label lbl_total;
    @FXML
    private Label lbl_terbayar;
    @FXML
    private Label lbl_sisa_bayar;
    @FXML
    private TableView<HistoryBayarHutangModel> tbl_history;
    @FXML
    private TableColumn<HistoryBayarHutangModel, String> clm_no;
    @FXML
    private TableColumn<HistoryBayarHutangModel, String> clm_tanggal;
    @FXML
    private TableColumn<HistoryBayarHutangModel, String> clm_bukti_bayar;
    @FXML
    private TableColumn<HistoryBayarHutangModel, String> clm_total_bayar;
    @FXML
    private JFXDatePicker dt_bayar;
    @FXML
    private JFXTextField txt_bukti_bayar;
    @FXML
    private JFXTextField txt_bayar;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private Label lbl_faktur;
    private static boolean isView;
    @FXML
    private Label lbl_judul;
    @FXML
    private JFXButton btn_kembali;
    @FXML
    private AnchorPane panel_bayar;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (isView) {
            lbl_judul.setText("DATA HUTANG");
            panel_bayar.setVisible(false);
            lbl_kode_trans.setVisible(false);
            
        }else{
            lbl_judul.setText("PEMBAYARAN HUTANG");
            panel_bayar.setVisible(true);
            lbl_kode_trans.setVisible(true);
        }
        setToolTip();
        setButtonListener();
        initializeState();
    }
    
    private String getQuery(){
        String tSQL = "SELECT H.*, B.ID as TRANSID, B.BUKTI, B.TGL as TGLBAYAR, B.TOTAL as BAYAR  "
                + "FROM v_all_hutang H INNER JOIN v_bayar_hutang B ON B.IDHUTANG = H.ID "
                + "WHERE H.ID='"+mKodeHutang+"' AND B.ID='"+lbl_kode_trans.getText()+"' LIMIT 1";
        return tSQL;
    }

    @Override
    public void showReport(String pReportFile, String Query) {
        try {
            new EZReport(pReportFile, QS.getConnection()).showReport(Query, mMap);
        } catch (JRException | ClassNotFoundException e) {
            loggingerror(e);
        }
    }
    
    public void simpan() {
        if (validatePembayaran()) {
            if (!"".equals(txt_bukti_bayar.getText())) {
                if(!txt_bayar.getText().equals("0")){
                    try {
                        String kdTrans = lbl_kode_trans.getText();
                        String id = mKodeHutang;
                        String tgl = getDate(dt_bayar);
                        String bkt = txt_bukti_bayar.getText();
                        String ttl = txt_bayar.getText().replace(",", "");
                        RecordEntry tMap = new RecordEntry();
                        tMap.createEntry(Bayar_Hutang.IDTRANS.get(), kdTrans);
                        tMap.createEntry(Bayar_Hutang.IDHUTANG.get(), id);
                        tMap.createEntry(Bayar_Hutang.TRANDATE.get(), tgl);
                        tMap.createEntry(Bayar_Hutang.INPUTDATE.get(), EZDate.SQLDATETIME.today());
                        tMap.createEntry(Bayar_Hutang.BUKTIBAYAR.get(), bkt);
                        tMap.createEntry(Bayar_Hutang.TOTAL.get(), ttl);
                        tMap.createEntry(Bayar_Hutang.CDT.get(), EZDate.SQLDATE.today());
                        tMap.createEntry(Bayar_Hutang.SID.get(), getSession());
                        int x = insertToDatabase(tMap, Bayar_Hutang.TABLENAME.get());
                        if (x==1) {
                            cekFlag(id);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
                            showReport("report/finance/hutang/faktur_bayar_hutang.jrxml", getQuery());
                            done();
                        }
                    } catch (SQLException ex) {
                        loggingerror(ex);
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    EZSystem.showAllert(alert, "Isi nominal bayar!", EZIcon.ICON_STAGE);
                    txt_bayar.requestFocus();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                EZSystem.showAllert(alert, "Isi bukti bayar!", EZIcon.ICON_STAGE);
                txt_bukti_bayar.requestFocus();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            EZSystem.showAllert(alert, "Pembayaran melebihi sisa hutang!", EZIcon.ICON_STAGE);
        }
    }
    
    private void cekFlag(String kode) throws SQLException{
        String tSQL = "SELECT sisa FROM hutang WHERE id='"+kode+"'";
        ResultSet tRes = selectFromDatabase(tSQL);
        if (tRes.next()) {
            if (tRes.getInt("sisa")==0) {
                updateFlag(kode);
            }
        }
    }
    
    private void updateFlag(String kode) throws SQLException{
        String tSQL = "UPDATE hutang set flag=1 WHERE id='"+kode+"'";
        updateToDatabase(tSQL);
    }
    
    private boolean validatePembayaran(){
        double sisa = Double.parseDouble(lbl_sisa_bayar.getText().replace(",", ""));
        double bayar = Double.parseDouble(txt_bayar.getText().replace(",", ""));
        return bayar<=sisa;
    }
    
    private void done(){
        try {
            final String mUrl = "/app/retail/fxml/finance/DataHutang.fxml";
            setmUrlChild(mUrl);
            getmHomeController().performLoadFinance(null);
        } catch (IOException ex) {
            loggingerror(ex);
        }
    }

    public void loadData() throws SQLException {
        String tSQL = "SELECT * FROM " + V_All_Hutang.TABLENAME.get()
                + " WHERE " + V_All_Hutang.ID.get() + "='" + mKodeHutang + "'";
        ResultSet tResult = selectFromDatabase(tSQL);
        if (tResult.next()) {
            mMap = mMapProfile;
            lbl_faktur.setText(tResult.getString(V_All_Hutang.REFNUM.get()));
            mMap.put("refnum", tResult.getString(V_All_Hutang.REFNUM.get()));
            mMap.put("faktur", tResult.getString(V_All_Hutang.FAKTUR.get()));
            lbl_nm_supplier.setText(tResult.getString(V_All_Hutang.SUPPLIER.get()));
            lbl_sisa_bayar.setText(formatRupiah(tResult.getDouble(V_All_Hutang.SISA.get())));
            mMap.put("sisahutang", formatRupiah(tResult.getDouble(V_All_Hutang.SISA.get())));
            lbl_tanggal.setText(EZDate.FORMAT_4.get(tResult.getDate(V_All_Hutang.TGL.get())));
            lbl_terbayar.setText(formatRupiah(tResult.getDouble(V_All_Hutang.TERBAYAR.get())));
            mMap.put("terbayar", formatRupiah(tResult.getDouble(V_All_Hutang.TERBAYAR.get())));
            lbl_total.setText(formatRupiah(tResult.getDouble(V_All_Hutang.TOTAL.get())));
            mMap.put("total", formatRupiah(tResult.getDouble(V_All_Hutang.TOTAL.get())));
        }
        loadHistoryBayar(mKodeHutang);
    }
    
    private void loadHistoryBayar(String id) throws SQLException{
        String tSQL = "SELECT * FROM "+V_Bayar_Hutang.TABLENAME.get()+" WHERE "+V_Bayar_Hutang.IDHUTANG.get()+"='"+id+"'";
        ResultSet tResult = selectFromDatabase(tSQL);
        int no = 0;
        while (tResult.next()) {            
            no++;
            HistoryBayarHutangModel tModel = new HistoryBayarHutangModel(""+no, EZDate.FORMAT_4.get(tResult.getDate(V_Bayar_Hutang.TGL.get())),
                    tResult.getString(V_Bayar_Hutang.BUKTI.get()), formatRupiah(tResult.getDouble(V_Bayar_Hutang.TOTAL.get())));
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

    public static String getmKodeHutang() {
        return mKodeHutang;
    }

    public static void setmKodeHutang(String mKodeHutang) {
        FormBayarHutangController.mKodeHutang = mKodeHutang;
    }

    public static boolean isIsView() {
        return isView;
    }

    public static void setIsView(boolean isView) {
        FormBayarHutangController.isView = isView;
    }

    @Override
    public void initializeState() {
        try {
            lbl_kode_trans.setText("PRC/"+EZDate.SHORTDATE.today()+"/"+genereateID(6));
            txt_bayar.setText("0");
            txt_bukti_bayar.setText("");
            setDate(dt_bayar);
            loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {
        setNumericTextfield(txt_bayar);
        setCurencyTextfield(txt_bayar);
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> optional = EZSystem.showAllert(alert, "Simpan data pembayaran?", EZIcon.ICON_STAGE);
            if (optional.get() == ButtonType.OK) {
                simpan();
            }
        });
        
        txt_bukti_bayar.setOnAction((ActionEvent event)->{
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Batalkan Transaksi ?", app.retail.utility.EZIcon.ICON_STAGE);
            if (opt.get() == ButtonType.OK) {
                initializeState();
            }
        });
        
        txt_bayar.setOnAction((ActionEvent event)->{
            btn_simpan.fire();
        });
        
        txt_bukti_bayar.setOnAction((ActionEvent event)->{
                txt_bayar.requestFocus();
        });
        
        btn_kembali.setOnAction((ActionEvent event)->{
            try {
                setmUrlChild("/app/retail/fxml/finance/DataHutang.fxml");
                getmHomeController().performLoadFinance(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        
        
        Platform.runLater(()->{
            if (isView) {
                tbl_history.requestFocus();
            }else{
                txt_bukti_bayar.requestFocus();
            }
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 220);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_tanggal, Pos.CENTER);
        setAligmentColoum(clm_bukti_bayar, Pos.CENTER);
        setAligmentColoum(clm_total_bayar, Pos.CENTER_RIGHT);
        btn_kembali.setCursor(Cursor.HAND);
        txt_bayar.setAlignment(Pos.CENTER_RIGHT);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loggingerror(Exception ex) {
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
        btn_kembali.fire();
    }
    
}
