/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.master;

import app.retail.controller.home.HomeController;
import app.retail.model.master.satuanModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Satuan;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class MasterSatuanController extends AbstractMaster implements Initializable {
    ObservableList<satuanModel> satuanlist = FXCollections.observableArrayList();
    private static boolean sttsSimpan;
    private JFXButton btnedit, btndelete;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<satuanModel> tbl_satuan;
    @FXML
    private TableColumn<satuanModel, String> clm_no_urut;
    @FXML
    private TableColumn<satuanModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<satuanModel, JFXButton> clm_delete;
    @FXML
    private TextField txt_id_satuan;
    @FXML
    private TextField txt_satuan;
    @FXML
    private Button btn_simpan;
    @FXML
    private Button btn_cancel;
    @FXML
    private TableColumn<satuanModel, String> clm_id_satuan;
    @FXML
    private TableColumn<satuanModel, String> clm_satuan;
    @FXML
    private Button btn_close;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            if (!HomeController.isAllowed("Input Data Satuan")) {
                btn_simpan.setDisable(true);
            }
            txt_id_satuan.setEditable(false);
            setToolTip();
            setButtonListener();
            initializeState();
        } catch (Exception e) {
            Logger.getLogger(MasterKategoriController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void setButtonOnTableView(JFXButton button, int type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == 0) {
                Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Edit data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    sttsSimpan = false;
                    loadData(Integer.parseInt(button.getId()));
                    txt_satuan.requestFocus();
                    btn_cancel.setVisible(true);
                }
            }

            if (type == 1) {
                Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Yakin hapus data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    delete(button.getId());
                }
            }
        });
    }

    public void delete(String param) {
        String tQuery = "DELETE FROM "+Satuan.TABLENAME.get()+" WHERE  id= '" + param + "'";
        try {
            int x = updateToDatabase(tQuery);
            if (x == 1) {
                EZSystem.showAllert(EZAlertType.INFO,"Data berhasil dihapus", EZIcon.ICON_STAGE);
                initializeState();
            }
        } catch (SQLException e) {
            if (e.toString().contains("Cannot delete or update a parent row")) {
                EZSystem.showAllert(EZAlertType.ERROR,"Data tidak bisa dihapus, data sudah dipakai!", EZIcon.ICON_STAGE);
            } else {
                EZSystem.showAllert(EZAlertType.ERROR, "Error Delete data With id " + param + "\n" + e.getMessage(), EZIcon.ICON_STAGE);
                Logger.getLogger(MasterSatuanController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    @Override
    public void clear() {
        txt_satuan.setText("");
    }

    public void simpan() {
        RecordEntry tMap = new RecordEntry();
        tMap.createEntry(Satuan.ID.get(), txt_id_satuan.getText());
        tMap.createEntry(Satuan.DESCRIP.get(), txt_satuan.getText());
        tMap.createEntry(Satuan.SID.get(), HomeController.getSession());
        try {
            if (sttsSimpan) {
                tMap.createEntry(Satuan.CDT.get(), EZDate.SQLDATETIME.today());
                int x = insertToDatabase(tMap, Satuan.TABLENAME.get());
                if (x == 1) {
                    EZSystem.showAllert(new Alert(Alert.AlertType.INFORMATION), "Data berhasil disimpan", EZIcon.ICON_STAGE);
                    initializeState();
                }
            } else {
                tMap.createEntry(Satuan.UDT.get(), EZDate.SQLDATETIME.today());
                int x = updateToDatabase(tMap, Satuan.TABLENAME.get(), Satuan.ID.get() + "='" + txt_id_satuan.getText() + "'");
                if (x == 1) {
                    EZSystem.showAllert(new Alert(Alert.AlertType.INFORMATION), "Data berhasil di Edit", EZIcon.ICON_STAGE);
                    initializeState();
                }
            }
        } catch (SQLException e) {
            EZSystem.showAllert(EZAlertType.ERROR, "Error Get data\n" + e.getMessage(), EZIcon.ICON_STAGE);
            initializeState();
            Logger.getLogger(MasterKategoriController.class.getName()).log(Level.SEVERE, null, e);
        }
    }


    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            txt_satuan.requestFocus();
        });
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            if (!txt_satuan.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Simpan Data?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    simpan();
                }
            } else {
                EZSystem.showAllert(EZAlertType.WARNING, "Isi data dengan benar!", EZIcon.ICON_STAGE);
                txt_satuan.requestFocus();
            }
        });
        
        txt_satuan.setOnKeyPressed((KeyEvent evnt)->{
            if(evnt.getCode() == KeyCode.ENTER){
                btn_simpan.fire();
            }
            
            if(evnt.getCode() == KeyCode.DOWN){
                tbl_satuan.requestFocus();
            }
        });

        btn_close.setOnAction((ActionEvent event) -> {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });

        btn_cancel.setOnAction((ActionEvent event) -> {
            initializeState();
        });
        
        tbl_satuan.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                if(tbl_satuan.getItems().size()>0){
                    tbl_satuan.getSelectionModel().select(0);
                }else{
                    tbl_satuan.requestFocus();
                }
            }
        });
    }

    @Override
    public void loadData() {
        tbl_satuan.getItems().clear();
        String tQuery = "SELECT * FROM " + Satuan.TABLENAME.get() + " ORDER BY "+Satuan.DESCRIP.get()+" ASC";
        try {
            ResultSet Result = selectFromDatabase(tQuery);
            int no = 0;
            while (Result.next()) {
                no++;
                if (HomeController.isAllowed("Edit Data Satuan")) {
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId(""+(no-1));
                    setButtonOnTableView(btnedit, 0);
                } else {
                    btnedit = null;
                }

                if (HomeController.isAllowed("Delete Data Satuan")) {
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId(Result.getString(Satuan.ID.get()));
                    setButtonOnTableView(btndelete, 1);
                } else {
                    btndelete = null;
                }
                satuanlist.add(new satuanModel("" + no, Result.getString(Satuan.ID.get()), Result.getString(Satuan.DESCRIP.get()), btnedit, btndelete));
            }
            setValueColum();
            tbl_satuan.setItems(satuanlist);
        } catch (SQLException e) {
            EZSystem.showAllert(EZAlertType.ERROR, "Error Get data\n" + e,EZIcon.ICON_STAGE);
            Logger.getLogger(MasterSatuanController.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception ex) {
            EZSystem.showAllert(EZAlertType.ERROR, "Error Get data\n" + ex,EZIcon.ICON_STAGE);
            Logger.getLogger(MasterSatuanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData(int index) {
        satuanModel model = tbl_satuan.getItems().get(index);
        txt_id_satuan.setText(model.getId());
        txt_satuan.setText(model.getDescrip());
    }

    private void setValueColum() {
        clm_no_urut.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_id_satuan.setCellValueFactory(new PropertyValueFactory<>(Satuan.ID.get()));
        clm_satuan.setCellValueFactory(new PropertyValueFactory<>(Satuan.DESCRIP.get()));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btnedit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
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
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initializeState() {
        txt_id_satuan.setText(generateRefnum(Satuan.TABLENAME.get(), Satuan.ID.get(), "SAT"));
        txt_satuan.requestFocus();
        loadData();
        clear();
        sttsSimpan = true;
    }

    @Override
    public void setToolTip() {
        setLimitTexfield(txt_satuan, 3);
        setAligmentColoum(clm_no_urut, Pos.CENTER_LEFT);
        setAligmentColoum(clm_id_satuan, Pos.CENTER_LEFT);
        setAligmentColoum(clm_satuan, Pos.CENTER_LEFT);
        btn_cancel.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
    }
}
