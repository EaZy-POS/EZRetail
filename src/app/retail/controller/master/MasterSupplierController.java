/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.master;

import app.retail.controller.home.HomeController;
import app.retail.model.master.SupplierModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Supplier;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class MasterSupplierController extends AbstractMaster implements Initializable {

    private final ObservableList<SupplierModel> supplierList = FXCollections.observableArrayList();
    private final ObservableList<String> limitList = FXCollections.observableArrayList("100", "200", "300", "400", "500", "Semua");
    private JFXButton btnDelete;
    private JFXButton btnEdit;
    private String kodeEdit;
    private static boolean sttsSimpan;
    private static String mQuery;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btn_print;
    @FXML
    private ComboBox<String> cmb_batas;
    @FXML
    private JFXTextField txt_cari;
    @FXML
    private TableColumn<SupplierModel, JFXButton> clm_delete;
    @FXML
    private TextField txt_id_supplier;
    @FXML
    private TextField txt_nama_supplier;
    @FXML
    private TextField txt_telp;
    @FXML
    private Button btn_simpan;
    @FXML
    private Button btn_cancel;
    @FXML
    private TextField txt_fax;
    @FXML
    private TextField txt_email;
    @FXML
    private TableView<SupplierModel> tbl_supplier;
    @FXML
    private TableColumn<SupplierModel, String> clm_no_urut;
    @FXML
    private TableColumn<SupplierModel, String> clm_nama_supplier;
    @FXML
    private TableColumn<SupplierModel, JFXButton> clm_edit;
    @FXML
    private TextArea txt_almt;
    @FXML
    private Button btn_close;
    @FXML
    private TableColumn<SupplierModel, String> clm_alamat;
    @FXML
    private TableColumn<SupplierModel, String> clm_telp;
    @FXML
    private TableColumn<SupplierModel, String> clm_fax;
    @FXML
    private TableColumn<SupplierModel, String> clm_email;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!HomeController.isAllowed("Input Data Supplier")) {
            btn_simpan.setDisable(true);
        }
        setToolTip();
        setButtonListener();
        setLayout();
        initializeState();
    }

    public MasterSupplierController() {
        setmSupplierControler(this);
    }

    @Override
    public void setToolTip() {
        btn_print.setTooltip(new Tooltip("Print"));
        btn_print.setCursor(Cursor.HAND);
        btn_cancel.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
        setLimitTexfield(txt_nama_supplier, 60);
        setAligmentColoum(clm_no_urut, Pos.CENTER_LEFT);
        setAligmentColoum(clm_alamat, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_supplier, Pos.CENTER_LEFT);
        setAligmentColoum(clm_telp, Pos.CENTER_LEFT);
        setAligmentColoum(clm_fax, Pos.CENTER_LEFT);
        setAligmentColoum(clm_email, Pos.CENTER_LEFT);
    }
    
    private String getQuery(String param){
        String tQuery = "SELECT * FROM " + Supplier.TABLENAME.get() + " WHERE "
                + Supplier.NAMA.get() + " LIKE'%" + param + "%' ORDER BY "
                + Supplier.NAMA.get() + " ASC";
        if (!cmb_batas.getSelectionModel().getSelectedItem().equalsIgnoreCase("Semua")) {
            tQuery += " LIMIT " + cmb_batas.getSelectionModel().getSelectedItem();
        }
        mQuery = tQuery;
        return tQuery;
    }

    public void searchDataByParam(String param){
        try {
            tbl_supplier.getItems().clear();
            String tQuery = getQuery(param);
            
            ResultSet Reult = selectFromDatabase(tQuery);
            int no = 0;
            while (Reult.next()) {
                no++;
                btnEdit = getButton(EZButtonType.BTN_EDIT, "Edit");
                btnEdit.setId(Reult.getString(Supplier.ID.get()));
                btnDelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                btnDelete.setId(Reult.getString(Supplier.ID.get()));
                supplierList.add(new SupplierModel("" + no,
                        Reult.getString(Supplier.ID.get()),
                        Reult.getString(Supplier.NAMA.get()),
                        Reult.getString(Supplier.ALAMAT.get()),
                        Reult.getString(Supplier.TELPON.get()),
                        Reult.getString(Supplier.FAX.get()),
                        Reult.getString(Supplier.EMAIL.get()),
                        btnEdit,
                        btnDelete));
                setButtonOnTableView(btnEdit, 0);
                setButtonOnTableView(btnDelete, 1);
            }
            setValueColum();
            tbl_supplier.setItems(supplierList);
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    public void setButtonOnTableView(JFXButton button, int type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == 0) {
                Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Edit data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    kodeEdit = button.getId();
                    sttsSimpan = false;
                    loadData(kodeEdit);
                    txt_nama_supplier.requestFocus();
                }
            }

            if (type == 1) {
                Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Yakin hapus data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    kodeEdit = button.getId();
                    delete(kodeEdit);
                }
            }
        });
    }

    public void delete(String param) {
        String tQuery = "DELETE FROM " + Supplier.TABLENAME.get() + " WHERE " + Supplier.ID.get() + "='" + param + "'";
        try {
            updateToDatabase(tQuery);
            EZSystem.showAllert(EZAlertType.INFO, "Data berhasil di hapus", EZIcon.ICON_STAGE);
            initializeState();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (e.toString().contains("Cannot delete or update a parent row")) {
                EZSystem.showAllert(alert, "Data tidak bisa dihapus, data sudah dipakai!", EZIcon.ICON_STAGE);
            } else {
                EZSystem.showAllert(alert, "Error Delete data With id " + param + "\n" + e.getMessage(), EZIcon.ICON_STAGE);
            }
            
        }
    }

    @Override
    public void clear() {
        txt_nama_supplier.setText("");
        txt_telp.setText("");
        txt_fax.setText("");
        txt_email.setText("");
        txt_almt.setText("");
        kodeEdit = "";
    }

    public void simpan() {
        RecordEntry tSupMap = new RecordEntry();
        tSupMap.createEntry(Supplier.ID.get(), txt_id_supplier.getText());
        tSupMap.createEntry(Supplier.NAMA.get(), txt_nama_supplier.getText());
        tSupMap.createEntry(Supplier.ALAMAT.get(), txt_almt.getText());
        tSupMap.createEntry(Supplier.TELPON.get(), txt_telp.getText());
        tSupMap.createEntry(Supplier.FAX.get(), txt_fax.getText());
        tSupMap.createEntry(Supplier.EMAIL.get(), txt_email.getText());
        tSupMap.createEntry(Supplier.SID.get(), HomeController.getSession());
        if (sttsSimpan) {
            try {
                tSupMap.createEntry(Supplier.CDT.get(), EZDate.SQLDATE.today());
                int x = insertToDatabase(tSupMap, Supplier.TABLENAME.get());
                if (x == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
                    initializeState();
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                EZSystem.showAllert(alert, "Error Insert To DB \n" + ex.getMessage(), EZIcon.ICON_STAGE);
            }
        } else {
            try {
                tSupMap.createEntry(Supplier.UDT.get(), EZDate.SQLDATE.today());
                String param = Supplier.ID.get() + "='" + txt_id_supplier.getText() + "'";
                int x = updateToDatabase(tSupMap, Supplier.TABLENAME.get(), param);
                if (x == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    EZSystem.showAllert(alert, "Data berhasil diedit", EZIcon.ICON_STAGE);
                    initializeState();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                EZSystem.showAllert(alert, "Error update data DB \n" + e.getMessage(), EZIcon.ICON_STAGE);
            }
        }
        sttsSimpan = true;
    }

    private boolean validasiForm() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (txt_nama_supplier.getText().equals("")) {
            EZSystem.showAllert(alert, "Nama Supplier Belum diisi", EZIcon.ICON_STAGE);
            return false;
        }

        return true;
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            txt_nama_supplier.requestFocus();
        });
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            if (validasiForm()) {
                Optional<ButtonType> btn = EZSystem.showAllert(alert, "Simpan data?", EZIcon.ICON_STAGE);
                if (btn.get() == ButtonType.OK) {
                    simpan();
                    initializeState();
                }
            }
        });

        btn_close.setOnAction((ActionEvent event) -> {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });

        btn_cancel.setOnAction((ActionEvent event) -> {
            initializeState();
        });

        txt_cari.setOnKeyTyped((KeyEvent event) -> {
            if (txt_cari.getText().length()> 2) {
                searchDataByParam(txt_cari.getText());
            }else{
                loadData();
            }
        });
        
        txt_cari.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.ENTER){
                tbl_supplier.requestFocus();
            }
        });
        
        txt_almt.setOnKeyPressed((KeyEvent evt)->{
            if (evt.getCode() == KeyCode.TAB) {
                txt_telp.requestFocus();
            }
        });
        
        txt_email.setOnKeyPressed((KeyEvent evt)->{
            if (evt.getCode() == KeyCode.ENTER) {
                btn_simpan.fire();
            }
        });
        
        btn_print.setOnAction((ActionEvent ent)->{
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak Data ?", EZIcon.ICON_STAGE);
            if(opt.get() == ButtonType.OK){
                if(tbl_supplier.getItems().size() > 0){
                    showReport("report/master/supplier/lap_supplier.jrxml", mQuery);
                }else{
                    EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Data tidak ada", EZIcon.ICON_STAGE);
                }
            }
        });
        
        tbl_supplier.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                if(tbl_supplier.getItems().size()>0){
                    tbl_supplier.getSelectionModel().select(0);
                }else{
                    txt_cari.requestFocus();
                }
            }
        });
        
//        tbl_supplier.setOnKeyPressed((event) -> {
//           if(event.getCode() == KeyCode.ENTER){
//               int indec = tbl_supplier.getSelectionModel().getSelectedIndex();
//               Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Edit data ?", EZIcon.ICON_STAGE);
//                if (option.get() == ButtonType.OK) {
//                    kodeEdit = tbl_supplier.getItems().get(indec).getId();
//                    sttsSimpan = false;
//                    loadData(kodeEdit);
//                    txt_nama_supplier.requestFocus();
//                }
//           } 
//        });
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
    }

    private void loadData(String kodeSup) {
        String tQuery = "SELECT * FROM "+Supplier.TABLENAME.get()+" WHERE "+Supplier.ID.get()+"='" + kodeSup + "'";
        try {
            ResultSet result = selectFromDatabase(tQuery);
            if (result.next()) {
                txt_id_supplier.setText(result.getString(Supplier.ID.get()));
                txt_nama_supplier.setText(result.getString(Supplier.NAMA.get()));
                txt_almt.setText(result.getString(Supplier.ALAMAT.get()));
                txt_fax.setText(result.getString(Supplier.FAX.get()));
                txt_telp.setText(result.getString(Supplier.TELPON.get()));
                txt_email.setText(result.getString(Supplier.EMAIL.get()));
            }
        } catch (SQLException e) {
            loggingerror(e);
        } catch (Exception ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void loadData() {
        tbl_supplier.getItems().clear();
        String tQuery = "SELECT * FROM "+Supplier.TABLENAME.get()+" ORDER BY "+Supplier.NAMA.get()+" ASC";
        mQuery = tQuery;
        try {
            ResultSet Reult = selectFromDatabase(tQuery);
            int no = 0;
            while (Reult.next()) {
                no++;
                if (HomeController.isAllowed("Edit Data Supplier")) {
                    btnEdit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnEdit.setId(Reult.getString(Supplier.ID.get()));
                    setButtonOnTableView(btnEdit, 0);
                } else {
                    btnEdit = null;
                }

                if (HomeController.isAllowed("Delete Data Supplier")) {
                    btnDelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btnDelete.setId(Reult.getString(Supplier.ID.get()));
                    setButtonOnTableView(btnDelete, 1);
                } else {
                    btnDelete = null;
                }

                supplierList.add(new SupplierModel("" + no,
                        Reult.getString(Supplier.ID.get()),
                        Reult.getString(Supplier.NAMA.get()),
                        Reult.getString(Supplier.ALAMAT.get()),
                        Reult.getString(Supplier.TELPON.get()),
                        Reult.getString(Supplier.FAX.get()),
                        Reult.getString(Supplier.EMAIL.get()),
                        btnEdit,
                        btnDelete));
            }
            setValueColum();
            tbl_supplier.setItems(supplierList);
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    private void setValueColum() {
        clm_no_urut.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_alamat.setCellValueFactory(new PropertyValueFactory<>(Supplier.ALAMAT.get()));
        clm_nama_supplier.setCellValueFactory(new PropertyValueFactory<>(Supplier.NAMA.get()));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btnedit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
        clm_telp.setCellValueFactory(new PropertyValueFactory<>("telp"));
        clm_fax.setCellValueFactory(new PropertyValueFactory<>(Supplier.FAX.get()));
        clm_email.setCellValueFactory(new PropertyValueFactory<>(Supplier.EMAIL.get()));
    }

    private void setLayout() {
        AnchorPane.setPrefWidth(getWidth()-350);
        AnchorPane.setPrefHeight(getHeight() - 200);
        double lebar = getWidth()-390;
        clm_no_urut.setPrefWidth((lebar * 3)/100);
        clm_alamat.setPrefWidth((lebar * 28)/100);
        clm_nama_supplier.setPrefWidth((lebar * 20)/100);
        clm_telp.setPrefWidth((lebar * 10)/100);
        clm_fax.setPrefWidth((lebar * 10)/100);
        clm_email.setPrefWidth((lebar * 10)/100);
        clm_edit.setPrefWidth((lebar * 7)/100);
        clm_delete.setPrefWidth((lebar * 8)/100);
    }
    
    

    @Override
    public void loadKategori() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadSubKategori(String subid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadSatuan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadSupplier() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initializeState() {
        clear();
        loadData();
        txt_id_supplier.setEditable(false);
        txt_nama_supplier.requestFocus();
        txt_id_supplier.setText(generateRefnum(Supplier.TABLENAME.get(), Supplier.ID.get(), "SUP"));
        cmb_batas.setItems(limitList);
        cmb_batas.getSelectionModel().select(0);
        sttsSimpan = true;
        txt_cari.setText("");
    }

    public void setShortcut(KeyEvent evt){
        KeyCodeCombination combo = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY);
        if(combo.match(evt)){
            btn_simpan.fire();
        }
        
        if(evt.getCode() == KeyCode.ESCAPE){
            btn_close.fire();
        }
        
        combo = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
        if(combo.match(evt)){
            btn_cancel.fire();
        }
    }
}
