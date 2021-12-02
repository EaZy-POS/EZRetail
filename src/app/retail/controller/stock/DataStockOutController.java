/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import app.retail.model.stock.StockKeluarModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_All_Stock_Out;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
public class DataStockOutController extends AbstractStock implements Initializable {

    ObservableList<String> limitList = FXCollections.observableArrayList("100","200","300","400","500","All");
    ObservableList<StockKeluarModel> stockoutlist = FXCollections.observableArrayList();
    private JFXButton btnview;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<StockKeluarModel> tbl_purchase;
    @FXML
    private TableColumn<StockKeluarModel,String> clm_no;
    @FXML
    private TableColumn<StockKeluarModel,String> clm_tanggl;
    @FXML
    private JFXDatePicker dtPick_1;
    @FXML
    private JFXDatePicker dtPick_2;
    @FXML
    private JFXComboBox<String> cmb_limit;
    @FXML
    private JFXButton btn_tambah;
    @FXML
    private TableColumn<StockKeluarModel,String> clm_ket;
    @FXML
    private TableColumn<StockKeluarModel,String> clm_kode_trans;
    @FXML
    private TableColumn<StockKeluarModel,String> clm_input_by;
    @FXML
    private TableColumn<StockKeluarModel,String> clm_total_item;
    @FXML
    private TableColumn<StockKeluarModel,String> clm_total_qty;
    @FXML
    private TableColumn<StockKeluarModel,JFXButton> clm_view;
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
            if (!isAllowed(Akses_List.INPUT_DATA_STOCK_OUT)) {
                btn_tambah.setDisable(true);
            }
            
            if (!isAllowed(Akses_List.PRINT_DATA_STOCK_OUT)) {
                btn_print.setDisable(true);
            }
            
            setToolTip();
            initializeState();
            setButtonListener();
            loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }    

    private String getQuery(String dateawal, String dateAkhir){
        String tSQL = "SELECT * FROM "+V_All_Stock_Out.TABLENAME.get()+" WHERE "+V_All_Stock_Out.TGL.get()+" BETWEEN '"+dateawal+"' AND '"+dateAkhir+"'";
        if (!cmb_limit.getSelectionModel().getSelectedItem().equalsIgnoreCase("All")) {
            tSQL+=" LIMIT "+cmb_limit.getSelectionModel().getSelectedItem();
        }
        return tSQL;
    }
    
    private String getQueryReport(){
        String tSQL = "SELECT * FROM v_all_stock_out SO "
                + "INNER JOIN v_stock_out_detail SD "
                + "ON SD.TRAN_ID=SO.TRANSID "
                +" WHERE "+V_All_Stock_Out.TGL.get()+" BETWEEN '"+getDate(dtPick_1)+"' AND '"+getDate(dtPick_2)+"' "
                + "ORDER BY TGL,TRANSID ASC";
        return tSQL;
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_kode_trans.setCellValueFactory(new PropertyValueFactory<>(V_All_Stock_Out.TRANSID.get()));
        clm_tanggl.setCellValueFactory(new PropertyValueFactory<>(V_All_Stock_Out.TGL.get()));
        clm_input_by.setCellValueFactory(new PropertyValueFactory<>(V_All_Stock_Out.NAMA.get()));
        clm_total_item.setCellValueFactory(new PropertyValueFactory<>(V_All_Stock_Out.TOTALITEM.get()));
        clm_total_qty.setCellValueFactory(new PropertyValueFactory<>(V_All_Stock_Out.TTLQTY.get()));
        clm_ket.setCellValueFactory(new PropertyValueFactory<>(V_All_Stock_Out.KET.get()));
        clm_view.setCellValueFactory(new PropertyValueFactory<>("btnview"));
    }

    @Override
    public void initializeState() {
        cmb_limit.setItems(limitList);
        cmb_limit.getSelectionModel().select(0);
    }

    @Override
    public void setButtonListener() {
        setDate(dtPick_1);
        setDate(dtPick_2);

        btn_tambah.setOnAction((ActionEvent event) -> {
            try {
                final String tUrl = "/app/retail/fxml/stock/FormInputStockOut.fxml";
                getmHomeController().loadForm(tUrl, getClass().getName(), event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        btn_print.setOnAction((ActionEvent event) -> {
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak Data ?", app.retail.utility.EZIcon.ICON_APPS);
            if (opt.get() == ButtonType.OK) {
                if (tbl_purchase.getItems().size() > 0) {
                    Map tMap = mMapProfile;
                    tMap.put("periode", getDate(dtPick_1, EZDate.FORMAT_5) + " s/d " + getDate(dtPick_2, EZDate.FORMAT_5));
                    showReport("report/stock/lap_stock_out.jrxml", getQueryReport(), tMap);
                } else {
                    EZSystem.showAllert(EZAlertType.WARNING, "Data tidak ada !", EZIcon.ICON_APPS);
                }
            }
        });

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
        
        Platform.runLater(()->{
            tbl_purchase.requestFocus();
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth()-250);
        AnchorPane.setPrefHeight(getHeight()- 355);
        btn_tambah.setCursor(Cursor.HAND);
        btn_tambah.setTooltip(new Tooltip("Tambah"));
        btn_print.setCursor(Cursor.HAND);
        btn_print.setTooltip(new Tooltip("Print"));
        setColumnWidth();
    }
    
    private void setColumnWidth(){
        tbl_purchase.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setColumnWithProperty(tbl_purchase, clm_no, 0.05, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_tanggl, 0.1, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_kode_trans, 0.20, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_input_by, 0.20, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_total_item, 0.17, true, Pos.CENTER);
        setColumnWithProperty(tbl_purchase, clm_total_qty, 0.17, true, Pos.CENTER);
        setColumnWithProperty(tbl_purchase, clm_ket, 0.20, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_view, 0.1, false, Pos.CENTER_LEFT);
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
    public void loadData() throws SQLException {
        tbl_purchase.getItems().clear();
        String tDtawal = getDate(dtPick_1);
        String TDtAkhir = getDate(dtPick_2);
        String tSQL = getQuery(tDtawal, TDtAkhir);
        ResultSet tResult = selectFromDatabase(tSQL);
        int no = 0;
        while (tResult.next()) {
            no++;
            String idTrans = tResult.getString(V_All_Stock_Out.TRANSID.get());
            String tDate = EZDate.FORMAT_2.get(tResult.getDate(V_All_Stock_Out.TGL.get()));
            String tInput = tResult.getString(V_All_Stock_Out.NAMA.get());
            String ttlItem = tResult.getString(V_All_Stock_Out.TOTALITEM.get());
            String ttlQty = tResult.getString(V_All_Stock_Out.TTLQTY.get());
            String tKet = tResult.getString(V_All_Stock_Out.KET.get());
            if (isAllowed("View Data Stock Out")) {
                btnview = getButton(EZButtonType.BTN_VIEW, "View");
                btnview.setId(idTrans);
                setButtonOnTableView(btnview, EZButtonType.BTN_VIEW);
            }
            StockKeluarModel tModel = new StockKeluarModel("" + no, idTrans, tDate, ttlItem, ttlQty, "0", tKet, tInput, btnview);
            stockoutlist.add(tModel);
            setValueColum();
            tbl_purchase.setItems(stockoutlist);
        }
    }
    
    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == EZButtonType.BTN_VIEW) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "View data "+button.getId()+" ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    try {
                        FormViewStockOutController.setmKodeTrans(button.getId());
                        final String tUrl ="/app/retail/fxml/stock/FormViewStockOut.fxml";
                        getmHomeController().loadForm(tUrl, getClass().getName(), event);
                    } catch (IOException ex) {
                        loggingerror(ex);
                    }
                }
            }
        });
    }

    @Override
    public void loggingerror(Exception ex) {
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }
    
    
}
