/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.purchase;

import app.retail.controller.general.MapKaryawan;
import app.retail.model.purchase.PurchaseModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Purchase;
import app.retail.utility.table.V_All_Purchase;
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
import java.time.LocalDate;
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
public class PurchaseController extends AbstractPurchase implements Initializable {

    private final ObservableList<String> paramList = FXCollections.observableArrayList("All","PO","LANGSUNG");
    private final ObservableList<String> limitlist = FXCollections.observableArrayList("100","200","300","400","500");
    private final ObservableList<String> statusList = FXCollections.observableArrayList("All","ORDER","DI TERIMA");
    private final ObservableList<String> supplierList = FXCollections.observableArrayList();
    private final ObservableList<PurchaseModel> purchaseList = FXCollections.observableArrayList();
    private JFXButton btnedit,btndelete,btnview;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<PurchaseModel> tbl_purchase;
    @FXML
    private TableColumn<PurchaseModel, String> clm_no;
    @FXML
    private TableColumn<PurchaseModel, JFXButton> clm_delete;    
    @FXML
    private TableColumn<PurchaseModel, JFXButton> clm_view;
    @FXML
    private TableColumn<PurchaseModel, JFXButton> clm_edit;
    @FXML
    private JFXTextField txt_cari1;
    @FXML
    private JFXButton btn_po;
    @FXML
    private JFXButton btn_purchase;
    @FXML
    private JFXComboBox<String> cmb_param;
    @FXML
    private JFXComboBox<String> cmb_limit;
    @FXML
    private JFXDatePicker dtPick_1;
    @FXML
    private JFXDatePicker dtPick_2;
    @FXML
    private TableColumn<PurchaseModel, String> clm_tanggl;
    @FXML
    private TableColumn<PurchaseModel, String> clm_refnum;
    @FXML
    private TableColumn<PurchaseModel, String> clm_faktur;
    @FXML
    private TableColumn<PurchaseModel, String> clm_supplier;
    @FXML
    private TableColumn<PurchaseModel, String> clm_type;
    @FXML
    private TableColumn<PurchaseModel, String> clm_status;
    @FXML
    private TableColumn<PurchaseModel, String> clm_po;
    @FXML
    private JFXComboBox<String> cmb_supplier;
    @FXML
    private Button btn_print;
    @FXML
    private JFXComboBox<String> cmb_status;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if (!isAllowed(Akses_List.INPUT_PO)) {
                btn_po.setDisable(true);
            }
            
            if (!isAllowed(Akses_List.INPUT_PURCHASE)) {
                btn_purchase.setDisable(true);
            }
            
            if (!isAllowed(Akses_List.PRINT_DATA_PURCHASE)) {
                btn_print.setDisable(true);
            }
            
            setToolTip();
            initializeState();
            loadSupplier();
            setButtonListener();
            loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }    

    @Override
    public void loadData() throws SQLException {
        String tTanggalAwal = getDate(dtPick_1);
        String tTanggalAkhir = getDate(dtPick_2);
        String tLimit = cmb_limit.getSelectionModel().getSelectedItem();
        String type = cmb_param.getSelectionModel().getSelectedItem();
        int status = cmb_status.getSelectionModel().getSelectedIndex();
        loadData(type, tTanggalAwal, tTanggalAkhir, tLimit, status);
    }
    
    protected void loadData(String pType,String pTglAwal, String pTglAkhir, String pLimit, int status) throws SQLException{
        String supplier;
        String tSQL = "SELECT * FROM " + V_All_Purchase.TABLENAME.get();
        tSQL += " WHERE ";
        if (cmb_param.getSelectionModel().getSelectedIndex() > 0) {
            tSQL += " " + V_All_Purchase.JNS.get() + "='" + cmb_param.getSelectionModel().getSelectedItem() + "' AND";
        }
        if (cmb_supplier.getSelectionModel().getSelectedIndex() > 0) {
            supplier = MAP_SUPPLIER.get(cmb_supplier.getSelectionModel().getSelectedItem());
            tSQL += " SUPID='" + supplier + "'  AND";
        }
        
        if (txt_cari1.getText().length() > 0) {
            if (pType.equalsIgnoreCase("PO")) {
                tSQL += " " + V_All_Purchase.PO.get() + "='" + txt_cari1.getText() + "' AND";
            } else if (pType.equalsIgnoreCase("FAKTUR")) {
                tSQL += " ("+ V_All_Purchase.REFNUM.get() + " ='" + txt_cari1.getText() + "' OR " + V_All_Purchase.FAKTUR.get() + "='" + txt_cari1.getText() + "') AND ";
            }else{
                tSQL += " " + V_All_Purchase.PO.get() + "='" + txt_cari1.getText() + "' OR ("+V_All_Purchase.REFNUM.get() + " ='" + txt_cari1.getText() + "' OR " + V_All_Purchase.FAKTUR.get() + "='" + txt_cari1.getText() + "') AND ";
            }
        }
        
        tSQL += " " + V_All_Purchase.TGL.get() + " BETWEEN '" + pTglAwal + "' AND '" + pTglAkhir ;
        
        if (cmb_status.getSelectionModel().getSelectedIndex() > 0) {
            tSQL += "' AND "+V_All_Purchase.FLAG.get()+" = "+(status-1);
        }else{
            tSQL += "' AND "+V_All_Purchase.FLAG.get()+" <2 ";
        }
        
        tSQL += " LIMIT " + pLimit;
        loadData(tSQL);
    }
    
    protected String getQuery(){
        String pTanggalAwal = getDate(dtPick_1);
        String pTanggalAkhir = getDate(dtPick_2);
        String pLimit = cmb_limit.getSelectionModel().getSelectedItem();
        String pType = cmb_param.getSelectionModel().getSelectedItem();
        int status = cmb_status.getSelectionModel().getSelectedIndex();
        String supplier;
        String tSQL = "SELECT P.*, S.NMKARYAWA, PD.IDITEM, PD.ITEMNAME, PD.SATUAN, PD.BUYPRICE, PD.QTY, PD.QTYTERIMA, PD.TOTAL as SUBTOTAL " 
                +"FROM `v_all_purchase` P " 
                +"INNER JOIN v_all_purchase_detail PD on PD.REFNUM= P.REFNUM " 
                +"INNER JOIN v_session S ON P.SID = S.SESSIONID ";
        tSQL += " WHERE ";
        if (cmb_param.getSelectionModel().getSelectedIndex() > 0) {
            tSQL += " " + V_All_Purchase.JNS.get() + "='" + cmb_param.getSelectionModel().getSelectedItem() + "' AND";
        }
        if (cmb_supplier.getSelectionModel().getSelectedIndex() > 0) {
            supplier = MAP_SUPPLIER.get(cmb_supplier.getSelectionModel().getSelectedItem());
            tSQL += " SUPID='" + supplier + "'  AND";
        }
        
        if (txt_cari1.getText().length() > 0) {
            if (pType.equalsIgnoreCase("PO")) {
                tSQL += " " + V_All_Purchase.PO.get() + "='" + txt_cari1.getText() + "' AND";
            } else if (pType.equalsIgnoreCase("FAKTUR")) {
                tSQL += " ("+ V_All_Purchase.REFNUM.get() + " ='" + txt_cari1.getText() + "' OR " + V_All_Purchase.FAKTUR.get() + "='" + txt_cari1.getText() + "') AND ";
            }else{
                tSQL += " " + V_All_Purchase.PO.get() + "='" + txt_cari1.getText() + "' OR ("+V_All_Purchase.REFNUM.get() + " ='" + txt_cari1.getText() + "' OR " + V_All_Purchase.FAKTUR.get() + "='" + txt_cari1.getText() + "') AND ";
            }
        }
        
        tSQL += " " + V_All_Purchase.TGL.get() + " BETWEEN '" + pTanggalAwal + "' AND '" + pTanggalAkhir ;
        
        if (cmb_status.getSelectionModel().getSelectedIndex() > 0) {
            tSQL += "' AND P."+V_All_Purchase.FLAG.get()+" = "+(status-1);
        }else{
            tSQL += "' AND P."+V_All_Purchase.FLAG.get()+" <2 ";
        }
        
        tSQL += "ORDER BY TGL ASC LIMIT " + pLimit;
        
        return tSQL;
    }
    
    protected void loadData(String tSQL) throws SQLException{
        purchaseList.clear();
        ResultSet tResult = selectFromDatabase(tSQL);
        int no = 0;
        tResult.beforeFirst();
        while (tResult.next()) {
            no++;
            Date dt = tResult.getDate(V_All_Purchase.TGL.get());
            String tranDt = EZDate.FORMAT_2.get(dt);
            String refnum = tResult.getString(V_All_Purchase.REFNUM.get());
            String PO = tResult.getString(V_All_Purchase.PO.get());
            String faktur = tResult.getString(V_All_Purchase.FAKTUR.get());
            String sup = tResult.getString(V_All_Purchase.SUPPLIER.get());
            String tipe = tResult.getString(V_All_Purchase.JNS.get());
            String stts = tResult.getString(V_All_Purchase.FLAG.get()).equals("0") ? "ORDER" : (tResult.getString(V_All_Purchase.FLAG.get()).equals("1") ? "DITERIMA" : "");
            if (stts.equals("ORDER")) {
                if (isAllowed(Akses_List.TERIMA_PO)) {
                    btnview = getButton(EZButtonType.BTN_ACCEPT, "Terima");
                    btnview.setId(refnum);
                    setButtonOnTableView(btnview, EZButtonType.BTN_ACCEPT);
                } else {
                    btnview = null;
                }

                if (isAllowed(Akses_List.EDIT_PO)) {
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId(refnum);
                    setButtonOnTableView(btnedit, EZButtonType.BTN_EDIT);
                } else {
                    btnedit = null;
                }

                if (isAllowed(Akses_List.DELETE_PO)) {
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId(refnum);
                    setButtonOnTableView(btndelete, EZButtonType.BTN_DELETE);
                } else {
                    btndelete = null;
                }

            } else {
                if (isAllowed(Akses_List.VIEW_DETAIL_PURCHASE)) {
                    btnview = getButton(EZButtonType.BTN_VIEW, "View");
                    btnview.setId(refnum);
                    btnedit = null;
                    btndelete = null;
                    setButtonOnTableView(btnview, EZButtonType.BTN_VIEW);
                } else {
                    btnview = null;
                    btnedit = null;
                    btndelete = null;
                }
            }
            PurchaseModel model = new PurchaseModel("" + no, refnum, tranDt, faktur, sup, stts, tipe, PO, btnedit, btndelete, btnview);
            purchaseList.add(model);
        }
        setValueColum();
        tbl_purchase.setItems(purchaseList);
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("NO"));
        clm_tanggl.setCellValueFactory(new PropertyValueFactory<>(V_All_Purchase.TGL.get()));
        clm_refnum.setCellValueFactory(new PropertyValueFactory<>(V_All_Purchase.REFNUM.get()));
        clm_faktur.setCellValueFactory(new PropertyValueFactory<>(V_All_Purchase.FAKTUR.get()));
        clm_supplier.setCellValueFactory(new PropertyValueFactory<>(V_All_Purchase.SUPPLIER.get()));
        clm_type.setCellValueFactory(new PropertyValueFactory<>(V_All_Purchase.JNS.get()));
        clm_status.setCellValueFactory(new PropertyValueFactory<>(V_All_Purchase.FLAG.get()));
        clm_po.setCellValueFactory(new PropertyValueFactory<>(V_All_Purchase.PO.get()));
        clm_view.setCellValueFactory(new PropertyValueFactory<>("btn_view"));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btn_edit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btn_delete"));
    }


    public void loadSupplier() throws SQLException {
        List<String> tMap = getSupplier();
        supplierList.clear();
        cmb_supplier.getItems().clear();
        supplierList.add("-- Supplier --");
        for (int i = 0; i < tMap.size(); i++) {
            String valString = tMap.get(i);
            supplierList.add(valString);
        }
        cmb_supplier.setItems(supplierList);
    }

    @Override
    public void clear() {        
    }
    
    private void delete(String pKodeTransaksi) throws SQLException{
        String tSQL = "UPDATE "+Purchase.TABLENAME.get()+" SET "+Purchase.FLAG.get() + "=2, "
                +Purchase.DID.get()+"='"+mMapKaryawan.get(MapKaryawan.KODEKARYAWAN.get())+"', "
                +Purchase.UDT.get()+"='"+EZDate.SQLDATE.today()+"' WHERE "
                +Purchase.REFNUM.get()+"='"+pKodeTransaksi+"'";
        int x = updateToDatabase(tSQL);
        if (x > 0) {
            EZSystem.showAllert(new Alert(Alert.AlertType.INFORMATION), "Data "+pKodeTransaksi+" berhasil dihapus", EZIcon.ICON_STAGE);
            initializeState();
            loadData();
        }else{
            EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), "Gagal dihapus data transaksi "+pKodeTransaksi, EZIcon.ICON_STAGE);
        }
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == EZButtonType.BTN_EDIT) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Edit data transaksi "+button.getId()+"?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    FormPOController.setIsEdit(true);
                    FormPOController.setKodeEdit(button.getId());
                    btn_po.fire();
                }
            }
            
            if (type == EZButtonType.BTN_DELETE) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Yakin hapus data transaksi "+button.getId()+" ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    try {
                        delete(button.getId());
                    } catch (SQLException ex) {
                        if (ex.getMessage().contains("a foreign key constraint")) {
                            EZSystem.showAllert(EZAlertType.INFO, "Gagal hapus data, data sudah digunakan", EZIcon.ICON_STAGE);
                        }
                    }
                }
            }
            
            if (type == EZButtonType.BTN_ACCEPT) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Terima PO "+button.getId()+" ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    try {
                        new FormTerimaPOController().setmKodeEdit(button.getId());
                        getmHomeController().loadForm("/app/retail/fxml/purchase/FormTerimaPO.fxml", getClass().getName(), event);
                    } catch (IOException ex) {
                        loggingerror(ex);
                    }
                }
            }
            
            if (type == EZButtonType.BTN_VIEW) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "View Data "+button.getId(), EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    try {
                        FormViewPurchaseController.setmKodeTrans(button.getId());
                        getmHomeController().loadForm("/app/retail/fxml/purchase/FormViewPurchase.fxml", getClass().getName(), event);
                    } catch (IOException ex) {
                        loggingerror(ex);
                    }
                }
            }
        });
    }

    @Override
    public void initializeState() {
        cmb_param.setItems(paramList);
        cmb_limit.setItems(limitlist);
        cmb_status.setItems(statusList);
        cmb_param.getSelectionModel().select(0);
        cmb_limit.getSelectionModel().select(0);
        cmb_supplier.getSelectionModel().select(0);
        cmb_status.getSelectionModel().select(0);
    }

    @Override
    public void setButtonListener() {
        setDate(dtPick_1);
        setDate(dtPick_2);
        
        btn_po.setOnAction((ActionEvent event) -> {
            try {
                final String tUrl = "/app/retail/fxml/purchase/FormPO.fxml";
                getmHomeController().loadForm(tUrl, this.getClass().getName(), event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        btn_purchase.setOnAction((ActionEvent event) -> {
            try {
                final String tUrl = "/app/retail/fxml/purchase/FormPurchase.fxml";
                getmHomeController().loadForm(tUrl, this.getClass().getName(), event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });

        txt_cari1.setOnAction((ActionEvent event) -> {
            try {
                loadData();
            } catch (SQLException e) {
                loggingerror(e);
            }
        });
        
        dtPick_1.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            try {
                loadData();
            } catch (SQLException e) {
                loggingerror(e);
            }
        });
        
        dtPick_2.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            try {
                loadData();
            } catch (SQLException e) {
                loggingerror(e);
            }
        });
        
        cmb_param.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
            } catch (SQLException e) {
                loggingerror(e);
            }
        });
        
        cmb_limit.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
            } catch (SQLException e) {
                loggingerror(e);
            }
        });
         
        cmb_supplier.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
            } catch (SQLException e) {
                loggingerror(e);
            }
        });
        
        cmb_status.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                loadData();
            } catch (SQLException e) {
                loggingerror(e);
            }
        });
        
        Platform.runLater(()->{
            txt_cari1.requestFocus();
        });
        
        btn_print.setOnAction((ActionEvent event) -> {
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak Data ?", EZIcon.ICON_APPS);
            if (opt.get() == ButtonType.OK) {
                if (tbl_purchase.getItems().size() > 0) {
                    Map tMap = mMapProfile;
                    tMap.put("periode", getDate(dtPick_1, EZDate.FORMAT_5) + " s/d " + getDate(dtPick_2, EZDate.FORMAT_5));
                    showReport("report/transaksi/purchase/lap_purchase.jrxml", getQuery(), tMap);
                } else {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Data tidak ada !", EZIcon.ICON_APPS);
                }
            }
        });
    }

    @Override
    public void setToolTip() {
        btn_po.setCursor(Cursor.HAND);        
        btn_purchase.setCursor(Cursor.HAND);
        btn_print.setCursor(Cursor.HAND);
        btn_print.setTooltip(new Tooltip("Print"));
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 225);
        setAligmentColoum(clm_tanggl, Pos.CENTER_LEFT);
        setAligmentColoum(clm_refnum, Pos.CENTER);
        setAligmentColoum(clm_faktur, Pos.CENTER);
        setAligmentColoum(clm_po, Pos.CENTER);
        setAligmentColoum(clm_supplier, Pos.CENTER_LEFT);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_type, Pos.CENTER);
        setAligmentColoum(clm_status, Pos.CENTER);
        double lebar = getWidth() - 200;
        clm_no.setPrefWidth((lebar * 3)/100);
        clm_tanggl.setPrefWidth((lebar * 8)/100);
        clm_refnum.setPrefWidth((lebar * 10)/100);
        clm_po.setPrefWidth((lebar * 10)/100);
        clm_faktur.setPrefWidth((lebar * 10)/100);
        clm_supplier.setPrefWidth((lebar * 35)/100);
        clm_faktur.setPrefWidth((lebar * 10)/100);
        clm_type.setPrefWidth((lebar * 8)/100);
        clm_view.setPrefWidth((lebar * 8)/100);
        clm_edit.setPrefWidth((lebar * 8)/100);
        clm_delete.setPrefWidth((lebar * 8)/100);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void simpan() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loggingerror(Exception ex) {
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }
    
}
