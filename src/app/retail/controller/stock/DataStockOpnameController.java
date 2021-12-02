/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import app.retail.controller.general.MapKaryawan;
import app.retail.model.stock.StockOpnameModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Stock_Opname;
import app.retail.utility.table.V_Stock_Opname;
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
public class DataStockOpnameController extends AbstractStock implements Initializable {

    ObservableList<String> statusList = FXCollections.observableArrayList("Semua","Belum diverifikasi","Sudah diverifikasi");
    ObservableList<String> limitList = FXCollections.observableArrayList("100","200","300","All");
    private final ObservableList<StockOpnameModel> opnameList = FXCollections.observableArrayList();
    JFXButton btnview,btnverifikasi,btndelete;
    private static String mQuery;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<StockOpnameModel> tbl_purchase;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_no;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_status;
    @FXML
    private TableColumn<StockOpnameModel, JFXButton> clm_ac1;
    @FXML
    private TableColumn<StockOpnameModel, JFXButton> clm_ac2;
    @FXML
    private TableColumn<StockOpnameModel, JFXButton> clm_ac3;
    @FXML
    private JFXDatePicker dtPick_1;
    @FXML
    private JFXDatePicker dtPick_2;
    @FXML
    private JFXComboBox<String> cmb_limit;
    @FXML
    private JFXComboBox<String> cmb_status;
    @FXML
    private JFXButton btn_add;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_id_trans;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_input_by;
    @FXML
    private Button btn_print;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_tanggl;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!isAllowed(Akses_List.INPUT_DATA_STOCK_OPNAME)) {
            btn_add.setDisable(true);
        }
        
        if (!isAllowed(Akses_List.PRINT_DATA_STOCK_OPNAME)) {
            btn_print.setDisable(true);
        }
        
        setToolTip();
        setButtonListener();
        initializeState();
    }

    public ResultSet searchBy(String tSQL) {
        try {
            return selectFromDatabase(tSQL);
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        return null;
    }
    
    private String getQuery(){
        String tglAwal = getDate(dtPick_1);
        String tglAkhir = getDate(dtPick_2);
        String tSQL = "SELECT * FROM "+V_Stock_Opname.TABLENAME.get()
                + " WHERE "+V_Stock_Opname.TGL.get()+" BETWEEN '"+tglAwal+"' AND '"+tglAkhir+"' ";
        
        mQuery = "SELECT OD.*, OP.TGL, OP.NAMA, OP.FLAG as STTS, OP.CANCEL_BY, OP.VERBY "
                + "FROM v_stock_opname_detail OD INNER JOIN v_stock_opname OP ON OP.ID=OD.TRANID "
                + " WHERE "+V_Stock_Opname.TGL.get()+" BETWEEN '"+tglAwal+"' AND '"+tglAkhir+"' ";
        
        if (cmb_status.getSelectionModel().getSelectedIndex()==0) {
            tSQL+= "AND "+V_Stock_Opname.FLAG.get()+"=1 OR "+V_Stock_Opname.FLAG.get()+"=2 ";
            mQuery+= "AND OP."+V_Stock_Opname.FLAG.get()+"=1 OR OP."+V_Stock_Opname.FLAG.get()+"=2 ";
        }
        
        if (cmb_status.getSelectionModel().getSelectedIndex()==1) {
            tSQL+= "AND "+V_Stock_Opname.FLAG.get()+"=1 ";
            mQuery+= "AND OP."+V_Stock_Opname.FLAG.get()+"=1 ";
        }
        
        if (cmb_status.getSelectionModel().getSelectedIndex()==2) {
            tSQL+= "AND "+V_Stock_Opname.FLAG.get()+"=2 ";
            mQuery+= "AND OP."+V_Stock_Opname.FLAG.get()+"=2 ";
        }
        
        if (!cmb_limit.getSelectionModel().getSelectedItem().equalsIgnoreCase("All")) {
            tSQL +="ORDER BY "+V_Stock_Opname.TGL.get()+" DESC LIMIT "+cmb_limit.getSelectionModel().getSelectedItem();
        }
        
        mQuery += "ORDER BY OP.TGL, TRANID ASC";
        return tSQL;
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        
        button.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            if (type == EZButtonType.BTN_VIEW) {
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "View data stock opname ?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    try {
                        FormViewStockOpnameController.setmKodeTrans(button.getId());
                        final String tUrl ="/app/retail/fxml/stock/FormViewStockOpname.fxml";
                        getmHomeController().loadForm(tUrl, getClass().getName(), event);
                    } catch (IOException ex) {
                        loggingerror(ex);
                    }
                }
            }
            
            if (type == EZButtonType.BTN_VERIFICATION) {
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Verifikasi data stock opname ?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    try {
                        FormVerifikasiStockOpnameController.setmKodeTrans(button.getId());
                        final String tUrl ="/app/retail/fxml/stock/FormVerifikasiStockOpname.fxml";
                        getmHomeController().loadForm(tUrl, getClass().getName(), event);
                    } catch (IOException ex) {
                        loggingerror(ex);
                    }
                }
            }

            if (type == EZButtonType.BTN_DELETE) {
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Delete data stock opname ?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    String kode = button.getId();
                    try {
                        delete(kode);
                    } catch (SQLException ex) {
                        loggingerror(ex);
                    }
                }
            }
        });
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_tanggl.setCellValueFactory(new PropertyValueFactory<>("tgl"));
        clm_id_trans.setCellValueFactory(new PropertyValueFactory<>("transId"));
        clm_input_by.setCellValueFactory(new PropertyValueFactory<>("inputBy"));
        clm_status.setCellValueFactory(new PropertyValueFactory<>("flag"));
        clm_ac1.setCellValueFactory(new PropertyValueFactory<>("btnview"));
        clm_ac2.setCellValueFactory(new PropertyValueFactory<>("btnverifikasi"));
        clm_ac3.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
    }
    
    private void delete(String kode) throws SQLException{
        String tSQL = 
                "UPDATE "+Stock_Opname.TABLENAME.get()+" SET flag=3, u_dt='"
                +EZDate.SQLDATE.today()+"', sid='"+getSession()
                +"' ,cancel_by='"+mMapKaryawan.get(MapKaryawan.KODEKARYAWAN.get())+"' WHERE "+Stock_Opname.TRANSID.get()+"='"+kode+"'";
        int x = updateToDatabase(tSQL);
        if(x == 1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            EZSystem.showAllert(alert, "Data berhasil dihapus !", EZIcon.ICON_STAGE);
            loadData();
        }
    }

    @Override
    public void initializeState() {
        try {
            cmb_limit.setItems(limitList);
            cmb_limit.getSelectionModel().select(0);
            cmb_status.setItems(statusList);
            cmb_status.getSelectionModel().select(0);
            loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            tbl_purchase.requestFocus();
        });
        
        setDate(dtPick_1);
        setDate(dtPick_2);
        
        btn_add.setOnAction((ActionEvent event) -> {
            try {
                final String tUrl ="/app/retail/fxml/stock/FormStockOpname.fxml";
                getmHomeController().loadForm(tUrl, getClass().getName(), event);
            } catch (IOException ex) {
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
        
        btn_print.setOnAction((ActionEvent event) -> {
            if (tbl_purchase.getItems().size() >0) {
                Map tMap = mMapProfile;
                tMap.put("periode", getDate(dtPick_1, EZDate.FORMAT_5)+" s/d "+getDate(dtPick_2, EZDate.FORMAT_5));
                showReport("report/stock/lap_stock_opname.jrxml", mQuery, tMap);
            }else{
                EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Data tidak ada!", EZIcon.ICON_APPS);
            }
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth()-250);
        AnchorPane.setPrefHeight(getHeight()- 355);
        btn_add.setCursor(Cursor.HAND);
        btn_add.setTooltip(new Tooltip("Tambah"));
        btn_print.setCursor(Cursor.HAND);
        btn_print.setTooltip(new Tooltip("Print"));
        setColumnWidth();
    }
    
    private void setColumnWidth(){
        tbl_purchase.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setColumnWithProperty(tbl_purchase, clm_no, 0.05, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_tanggl, 0.1, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_id_trans, 0.20, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_input_by, 0.20, true, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_status, 0.1, true, Pos.CENTER);
        setColumnWithProperty(tbl_purchase, clm_ac1, 0.10, false, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_ac2, 0.10, false, Pos.CENTER_LEFT);
        setColumnWithProperty(tbl_purchase, clm_ac3, 0.15, false, Pos.CENTER_LEFT);
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
        String tSQL = getQuery();
        ResultSet result = searchBy(tSQL);
        if (result != null) {
            try {
                int no = 0;
                while (result.next()) {
                    no++;
                    String tgl = EZDate.FORMAT_2.get(result.getDate(V_Stock_Opname.TGL.get()));
                    String idTrans = result.getString(V_Stock_Opname.ID.get());
                    String nama = result.getString(V_Stock_Opname.NAMA.get());
                    int flag = result.getInt(V_Stock_Opname.FLAG.get());
                    String status = "";
                    if (flag == 1) {
                        if(isAllowed(Akses_List.VIEW_DETAIL_DATA_STOCK_OPNAME)){
                            btnview = getButton(EZButtonType.BTN_VIEW, "View");
                            btnview.setId(idTrans);
                            setButtonOnTableView(btnview, EZButtonType.BTN_VIEW);
                        }else{
                            btnview = null;
                        }
                        
                        if(isAllowed(Akses_List.VERIFIKASI_DATA_STOCK_OPNAME)){
                            btnverifikasi = getButton(EZButtonType.BTN_VERIFICATION, "Verifikasi");
                            btnverifikasi.setId(idTrans);
                            setButtonOnTableView(btnverifikasi, EZButtonType.BTN_VERIFICATION);
                        }else{
                            btnverifikasi=null;
                        }
                        
                        if(isAllowed(Akses_List.DELETE_DATA_STOCK_OPNAME)){
                            btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                            btndelete.setId(idTrans);
                            setButtonOnTableView(btndelete, EZButtonType.BTN_DELETE);
                        }else{
                            btndelete=null;
                        }
                        status = "Belum diverifikasi";
                    }else if(flag == 2){
                        if(isAllowed("View Detail S.O")){
                            btnview = getButton(EZButtonType.BTN_VIEW, "View");
                            btnview.setId(idTrans);
                            setButtonOnTableView(btnview, EZButtonType.BTN_VIEW);
                        }else{
                            btnview=null;
                        }
                        status = "Terverifikasi";
                    }
                    StockOpnameModel model = new StockOpnameModel(""+no, idTrans, tgl, nama, status,btnview, btnverifikasi, btndelete);
                    opnameList.add(model);
                    btndelete = null;
                    btnverifikasi = null;
                    btnview = null;
                }
                setValueColum();
                tbl_purchase.setItems(opnameList);
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        }
    }

}
