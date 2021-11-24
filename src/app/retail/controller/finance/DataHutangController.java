/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.finance;

import app.retail.model.finance.HutangModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_All_Hutang;
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
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class DataHutangController extends AbstractFinance implements Initializable {

    private final ObservableList<HutangModel> hutangList = FXCollections.observableArrayList();
    private final ObservableList<String> statusList = FXCollections.observableArrayList("All","Belum Lunas","Lunas");
    private final ObservableList<String> limitlist = FXCollections.observableArrayList("100","200","300","400","500");
    private final ObservableList<String> supplierList = FXCollections.observableArrayList();
    private JFXButton btnpay,btnview;
    private static String mQuery;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<HutangModel> tbl_trans;
    @FXML
    private TableColumn<HutangModel, String> clm_no;
    @FXML
    private TableColumn<HutangModel, String> clm_tanggl;
    @FXML
    private TableColumn<HutangModel, String> clm_refnum;
    @FXML
    private TableColumn<HutangModel, String> clm_faktur;
    @FXML
    private TableColumn<HutangModel, String> clm_supplier;
    @FXML
    private TableColumn<HutangModel, String> clm_type;
    @FXML
    private TableColumn<HutangModel, String> clm_status;
    @FXML
    private TableColumn<HutangModel, JFXButton> clm_view;
    @FXML
    private JFXDatePicker dtPick_1;
    @FXML
    private JFXDatePicker dtPick_2;
    @FXML
    private JFXComboBox<String> cmb_status;
    @FXML
    private JFXComboBox<String> cmb_limit;
    @FXML
    private JFXComboBox<String> cmb_supplier;
    @FXML
    private TableColumn<HutangModel, JFXButton> clm_pay;
    @FXML
    private TableColumn<HutangModel, String> clm_total;
    @FXML
    private Button btn_print;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            if(!isAllowed(Akses_List.PRINT_DATA_HUTANG)){
                btn_print.setDisable(true);
            }
            setToolTip();
            loadSupplier();
            setButtonListener();
            initializeState();
            if (mListParameter != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(EZDate.SQLDATE.get());
                Date dateAwal = sdf.parse(mListParameter.get(mTglAwal));
                Date dateAkhir = sdf.parse(mListParameter.get(mTglAkhir));
                String tLimit = (String) mListParameter.get(mLimit);
                String tSupplier = (String) mListParameter.get(mSupplier);
                String tStatus = (String) mListParameter.get(mStatus);
                cmb_limit.getSelectionModel().select(tLimit);
                cmb_supplier.getSelectionModel().select(tSupplier);
                cmb_status.getSelectionModel().select(tStatus);
                setDate(dtPick_1, dateAwal);
                setDate(dtPick_2, dateAkhir);
                loadData();
            } else {
                loadData();
            }
        } catch (SQLException | ParseException ex) {
            loggingerror(ex);
        }
    }    

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private String getQuery(){
        String tSQL = "SELECT * FROM "+V_All_Hutang.TABLENAME.get()+" WHERE "
                +V_All_Hutang.TGL.get()+" BETWEEN '"+getDate(dtPick_1)
                +" 00:00:00' AND '"+getDate(dtPick_2)+" 23:59:59' ";
        
        mQuery = "SELECT H.* , B.ID as TRANSID, B.BUKTI, B.TGL as TGLBAYAR, B.TOTAL as BAYAR  FROM v_all_hutang H INNER JOIN v_bayar_hutang B ON B.IDHUTANG = H.ID "
                + "WHERE H.TGL BETWEEN '"+getDate(dtPick_1)+" 00:00:00' AND '"+getDate(dtPick_2)+" 23:59:59'";
        
        if (cmb_status.getSelectionModel().getSelectedIndex()>0) {
            tSQL = tSQL.concat(" AND "+V_All_Hutang.FLAG.get()+"="+(cmb_status.getSelectionModel().getSelectedIndex()-1));
            mQuery =  mQuery.concat(" AND "+V_All_Hutang.FLAG.get()+"="+(cmb_status.getSelectionModel().getSelectedIndex()-1));
            if (cmb_supplier.getSelectionModel().getSelectedIndex()>0) {
                String id = MAP_SUPPLIER.get(cmb_supplier.getSelectionModel().getSelectedItem());
                tSQL = tSQL.concat(" AND "+V_All_Hutang.SUPID.get()+"='"+id+"'");
                mQuery = mQuery.concat(" AND "+V_All_Hutang.SUPID.get()+"='"+id+"'");
            }
        }else{
            if (cmb_supplier.getSelectionModel().getSelectedIndex()>0) {
                String id = MAP_SUPPLIER.get(cmb_supplier.getSelectionModel().getSelectedItem());
                tSQL = tSQL.concat(" AND "+V_All_Hutang.SUPID.get()+"='"+id+"'");
                mQuery = mQuery.concat(" AND "+V_All_Hutang.SUPID.get()+"='"+id+"'");
            }
        }
        
        if(cmb_limit.getSelectionModel().getSelectedItem() != null){
            tSQL = tSQL.concat(" LIMIT "+cmb_limit.getSelectionModel().getSelectedItem());
        }
        return tSQL;
    }

    public void loadData() throws SQLException{
        if(isAllowed(Akses_List.DATA_HUTANG)){
        tbl_trans.getItems().clear();
        String tSQL = getQuery();
        ResultSet tResultSet = selectFromDatabase(tSQL);
        int no = 0;
        while (tResultSet.next()) {
            no++;
            String status;
            if (tResultSet.getInt(V_All_Hutang.FLAG.get()) == 0) {
                status = "Belum Lunas";
                if (isAllowed(Akses_List.VIEW_DATA_HUTANG)) {
                    btnview = getButton(EZButtonType.BTN_VIEW, "View");
                    btnview.setId(tResultSet.getString(V_All_Hutang.ID.get()));
                    setButtonOnTableView(btnview, EZButtonType.BTN_VIEW);
                } else {
                    btnview = null;
                }

                if (isAllowed(Akses_List.INPUT_PEMBAYARAN_HUTANG)) {
                    btnpay = getButton(EZButtonType.BTN_PAY, "Bayar");
                    btnpay.setId(tResultSet.getString(V_All_Hutang.ID.get()));
                    setButtonOnTableView(btnpay, EZButtonType.BTN_PAY);
                } else {
                    btnpay = null;
                }
            } else {
                status = "Lunas";
                if (isAllowed(Akses_List.VIEW_DATA_HUTANG)) {
                    btnview = getButton(EZButtonType.BTN_VIEW, "View");
                    btnview.setId(tResultSet.getString(V_All_Hutang.ID.get()));
                    setButtonOnTableView(btnview, EZButtonType.BTN_VIEW);
                } else {
                    btnview = null;
                }
            }
            double total = tResultSet.getDouble(V_All_Hutang.TOTAL.get());
            Date trnDt = tResultSet.getDate(V_All_Hutang.TGL.get());
            HutangModel tModel = new HutangModel(
                    "" + no,
                    EZDate.FORMAT_2.get(trnDt),
                    tResultSet.getString(V_All_Hutang.REFNUM.get()),
                    tResultSet.getString(V_All_Hutang.FAKTUR.get()),
                    tResultSet.getString(V_All_Hutang.SUPID.get()),
                    tResultSet.getString(V_All_Hutang.SUPPLIER.get()),
                    formatRupiah(total),
                    tResultSet.getString(V_All_Hutang.ID.get()),
                    status,
                    tResultSet.getString(V_All_Hutang.JNS.get()),
                     btnpay, btnview);
            hutangList.add(tModel);
        }
        setValueColum();
        tbl_trans.setItems(hutangList);
        }
    }

    public void loadSupplier() {
        List<String> tMap = getSupplier();
        cmb_supplier.getItems().clear();
        supplierList.clear();
        supplierList.add("-- Supplier --");
        for (int i = 0; i < tMap.size(); i++) {
            String valString = tMap.get(i);
            supplierList.add(valString);
        }
        cmb_supplier.setItems(supplierList);
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            if(type==EZButtonType.BTN_PAY || type==EZButtonType.BTN_VIEW){
                setListParam();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                if (type==EZButtonType.BTN_PAY) {
                    Optional<ButtonType> option = EZSystem.showAllert(alert, "Bayar Hutang ?", EZIcon.ICON_STAGE);
                    if (option.get() == ButtonType.OK) {
                        try {
                            FormBayarHutangController.setmKodeHutang(button.getId());
                            final String tUrl = "/app/retail/fxml/finance/FormBayarHutang.fxml";
                            FormBayarHutangController.setIsView(false);
                            getmHomeController().loadForm(tUrl, getClass().getName(), event);
                        } catch (IOException ex) {
                            loggingerror(ex);
                        }
                    }
                }
                
                if (type==EZButtonType.BTN_VIEW) {
                    Optional<ButtonType> option = EZSystem.showAllert(alert, "View Data Hutang ?", EZIcon.ICON_STAGE);
                    if (option.get() == ButtonType.OK) {
                        try {
                            FormBayarHutangController.setmKodeHutang(button.getId());
                            final String tUrl = "/app/retail/fxml/finance/FormBayarHutang.fxml";
                            FormBayarHutangController.setIsView(true);
                            getmHomeController().loadForm(tUrl, getClass().getName(), event);
                        } catch (IOException ex) {
                            loggingerror(ex);
                        }
                    }
                }
                
            }
        });
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_tanggl.setCellValueFactory(new PropertyValueFactory<>("tran_date"));
        clm_refnum.setCellValueFactory(new PropertyValueFactory<>("kode_trans"));
        clm_faktur.setCellValueFactory(new PropertyValueFactory<>("faktur"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        clm_supplier.setCellValueFactory(new PropertyValueFactory<>("nama"));
        clm_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        clm_status.setCellValueFactory(new PropertyValueFactory<>("flag"));
        clm_view.setCellValueFactory(new PropertyValueFactory<>("btnview"));
        clm_pay.setCellValueFactory(new PropertyValueFactory<>("btnpay"));
    }

    @Override
    public void initializeState() {
        cmb_status.setItems(statusList);
        cmb_limit.setItems(limitlist);
        cmb_status.getSelectionModel().select(0);
        cmb_limit.getSelectionModel().select(0);
        cmb_supplier.getSelectionModel().select(0);
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            tbl_trans.requestFocus();
        });
        
        setDate(dtPick_1);
        setDate(dtPick_2);
        
        dtPick_1.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            try {
                loadData();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });
        
        dtPick_2.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            try {
                loadData();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });
        
        cmb_limit.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });
        
        cmb_status.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });
        
        cmb_supplier.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });
        
        btn_print.setOnAction((ActionEvent event) -> {
           Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak Data ?", EZIcon.ICON_APPS);
            if (opt.get() == ButtonType.OK) {
                if (tbl_trans.getItems().size() > 0) {
                    showReport("report/finance/hutang/lap_hutang.jrxml", mQuery, getMapReport());
                } else {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Data tidak ada !", EZIcon.ICON_APPS);
                }
            }
        });
    }
    
    private void setListParam() {
        DataHutangController.mListParameter = new HashMap<>();
        DataHutangController.mListParameter.put(mTglAwal, getDate(dtPick_1));
        DataHutangController.mListParameter.put(mTglAkhir, getDate(dtPick_2));
        DataHutangController.mListParameter.put(mSupplier, cmb_supplier.getSelectionModel().getSelectedItem());
        DataHutangController.mListParameter.put(mLimit, cmb_limit.getSelectionModel().getSelectedItem());
        DataHutangController.mListParameter.put(mStatus, cmb_status.getSelectionModel().getSelectedItem());
    }
    
    private Map<String, Object> getMapReport(){
        Map<String, Object> tMap = mMapProfile;
        try {
            String tSQL = "SELECT SUM(total) as TOTALHUTANG, SUM(TERBAYAR) as TERBAYAR, SUM(SISA) as SISA "
                    + "from v_all_hutang WHERE TGL BETWEEN '"+getDate(dtPick_1)+" 00:00:00' AND '"+getDate(dtPick_2)+" 23:59:59'";
            ResultSet tRes = selectFromDatabase(tSQL);
            if (tRes.next()) {
                tMap.put("totalhutang", formatRupiah(tRes.getDouble("TOTALHUTANG")));
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
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth()-250);
        AnchorPane.setPrefHeight(getHeight()-355);
        btn_print.setCursor(Cursor.HAND);
        btn_print.setTooltip(new Tooltip("print"));
        setAligmentColoum(clm_tanggl, Pos.CENTER_LEFT);
        setAligmentColoum(clm_refnum, Pos.CENTER_LEFT);
        setAligmentColoum(clm_faktur, Pos.CENTER_LEFT);
        setAligmentColoum(clm_supplier, Pos.CENTER_LEFT);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_type, Pos.CENTER);
        setAligmentColoum(clm_status, Pos.CENTER);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        double lebar = getWidth() - 200;
        clm_no.setPrefWidth((lebar * 3)/100);
        clm_tanggl.setPrefWidth((lebar * 8)/100);
        clm_refnum.setPrefWidth((lebar * 10)/100);
        clm_faktur.setPrefWidth((lebar * 10)/100);
        clm_supplier.setPrefWidth((lebar * 21)/100);
        clm_total.setPrefWidth((lebar * 10)/100);
        clm_faktur.setPrefWidth((lebar * 10)/100);
        clm_type.setPrefWidth((lebar * 8)/100);
        clm_status.setPrefWidth((lebar * 8)/100);
        clm_view.setPrefWidth((lebar * 8)/100);
        clm_pay.setPrefWidth((lebar * 8)/100);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
