/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.master;

import app.retail.controller.home.HomeController;
import app.retail.model.master.kategoriModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Kategori;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class MasterKategoriController extends AbstractMaster implements Initializable {

    ObservableList<kategoriModel> kategoriList = FXCollections.observableArrayList();
    private static boolean sttsSimpan;
    private JFXButton btnedit, btndelete;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableColumn<kategoriModel, String> clm_no_urut;
    @FXML
    private TableColumn<kategoriModel, String> clm_id_kategori;
    @FXML
    private TableColumn<kategoriModel, String> clm_kategori;
    @FXML
    private TableColumn<kategoriModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<kategoriModel, JFXButton> clm_delete;
    @FXML
    private TextField txt_id_kategori;
    @FXML
    private TextField txt_kategori;
    @FXML
    private Button btn_simpan;
    @FXML
    private Button btn_cancel;
    @FXML
    private TableView<kategoriModel> tbl_kategori;
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
        if (!HomeController.isAllowed("Input Data Kategori")) {
            btn_simpan.setDisable(true);
        }
        setToolTip();
        setButtonListener();
        initializeState();
    }

    public MasterKategoriController() {
        setmKategoriController(this);
    }

    @Override
    public void setToolTip() {
        txt_id_kategori.setEditable(false);
        setLimitTexfield(txt_kategori, 30);
        setAligmentColoum(clm_no_urut, Pos.CENTER_LEFT);
        setAligmentColoum(clm_id_kategori, Pos.CENTER_LEFT);
        setAligmentColoum(clm_kategori, Pos.CENTER_LEFT);
        btn_close.setCursor(Cursor.HAND);
        btn_cancel.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
    }

    public void setButtonOnTableView(JFXButton button, int type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == 0) {
                Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Edit data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    sttsSimpan = false;
                    loadData(Integer.parseInt(button.getId()));
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
        String tQuery = "DELETE FROM "+Kategori.TABLENAME.get()+" WHERE  id= '"+param+"'";
        try {
            int x = updateToDatabase(tQuery);
            if (x == 1) {
                EZSystem.showAllert(new Alert(Alert.AlertType.INFORMATION), "Data berhasil dihapus", EZIcon.ICON_STAGE);
                initializeState();
            }
        } catch (SQLException  e) {
            if (e.toString().contains("Cannot delete or update a parent row")) {
                EZSystem.showAllert(EZAlertType.ERROR, "Data tidak bisa dihapus, data sudah dipakai!", EZIcon.ICON_STAGE);
            }else{
                EZSystem.showAllert(EZAlertType.ERROR, "Error Delete data With id "+param+", "+e.getMessage(), EZIcon.ICON_STAGE);
            }
        }
    }
    
    @Override
    public void clear() {
        txt_kategori.setText("");
    }

    public void simpan() {
        RecordEntry tMap = new RecordEntry();
        tMap.createEntry(Kategori.ID.get(), txt_id_kategori.getText());
        tMap.createEntry(Kategori.KATEGORI.get(), txt_kategori.getText());
        tMap.createEntry(Kategori.SID.get(), HomeController.getSession());
        try {
            if (sttsSimpan) {
                tMap.createEntry(Kategori.CDT.get(), EZDate.SQLDATETIME.today());
                int x = insertToDatabase(tMap, Kategori.TABLENAME.get());
                if (x == 1) {
                    EZSystem.showAllert(EZAlertType.INFO, "Data berhasil disimpan", EZIcon.ICON_STAGE);
                    initializeState();
                }
            }else{
                tMap.createEntry(Kategori.UDT.get(), EZDate.SQLDATETIME.today());
                int x = updateToDatabase(tMap, Kategori.TABLENAME.get(), Kategori.ID.get()+"='"+txt_id_kategori.getText()+"'");
                if (x == 1) {
                    EZSystem.showAllert(new Alert(Alert.AlertType.INFORMATION), "Data berhasil di Edit", EZIcon.ICON_STAGE);
                    initializeState();
                }
            }
        } catch (SQLException e) {
            EZSystem.showAllert(EZAlertType.ERROR, "Error Get data\n"+e.getMessage(), EZIcon.ICON_STAGE);
            
        }
    }
    
    @Override
    public void setButtonListener() {
        btn_simpan.setOnAction((ActionEvent event) -> {
            if (!txt_kategori.getText().equals("")) {
                Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION),"Simpan Data?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    simpan();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                EZSystem.showAllert(alert, "Isi data dengan benar!", EZIcon.ICON_STAGE);
                txt_kategori.requestFocus();
            }
        });
        
        tbl_kategori.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                tbl_kategori.getSelectionModel().select(0);
            }
        });
        
        txt_kategori.setOnKeyPressed((KeyEvent evt)->{
            if (evt.getCode() == KeyCode.ENTER) {
                btn_simpan.fire();
            }
            
            if (evt.getCode() == KeyCode.DOWN) {
                tbl_kategori.requestFocus();
            }
        });
        
        tbl_kategori.setOnKeyPressed((KeyEvent evt)->{
            if (evt.getCode() == KeyCode.ENTER) {
                if (HomeController.isAllowed("Edit Data Kategori")) {
                    int index = tbl_kategori.getSelectionModel().getSelectedIndex();
                    Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Edit data ?", EZIcon.ICON_STAGE);
                    if (option.get() == ButtonType.OK) {
                        sttsSimpan = false;
                        loadData(Integer.parseInt(tbl_kategori.getItems().get(index).getBtnedit().getId()));
                        btn_cancel.setVisible(true);
                    }
                }
            }
        });

        btn_close.setOnAction((ActionEvent event) -> {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });

        btn_cancel.setOnAction((ActionEvent event) -> {
            initializeState();
        });
        
        Platform.runLater(()->{
            txt_kategori.requestFocus();
        });
        
        tbl_kategori.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                if(tbl_kategori.getItems().size()>0){
                    tbl_kategori.getSelectionModel().select(0);
                }else{
                    tbl_kategori.requestFocus();
                }
            }
        });
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
    }

    @Override
    public void loadData() {
        tbl_kategori.getItems().clear();
        String tQuery = "SELECT * FROM " + Kategori.TABLENAME.get() + " ORDER BY "+Kategori.KATEGORI.get()+" ASC";
        try {
            ResultSet Result = selectFromDatabase(tQuery);
            int no = 0;
            while (Result.next()) {
                no++;
                if (HomeController.isAllowed("Edit Data Kategori")) {
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId(""+(no-1));
                    setButtonOnTableView(btnedit, 0);
                } else {
                    btnedit = null;
                }

                if (HomeController.isAllowed("Delete Data Kategori")) {
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId(Result.getString(Kategori.ID.get()));
                    setButtonOnTableView(btndelete, 1);
                } else {
                    btndelete = null;
                }
                kategoriList.add(new kategoriModel(Result.getString(Kategori.ID.get()), Result.getString(Kategori.KATEGORI.get()), "" + no, btnedit, btndelete));
            }
            setValueColum();
            tbl_kategori.setItems(kategoriList);
        } catch (SQLException e) {
            EZSystem.showAllert(EZAlertType.ERROR,"Error Get data\n" + e.getMessage(), EZIcon.ICON_STAGE);
            
        } catch (Exception ex) {
            EZSystem.showAllert(EZAlertType.ERROR,"Error Get data\n" + ex.getMessage(), EZIcon.ICON_STAGE);
        }
    }

    private void loadData(int index) {
        kategoriModel model = tbl_kategori.getItems().get(index);
        txt_id_kategori.setText(model.getId());
        txt_kategori.setText(model.getKategori());
        txt_kategori.requestFocus();
    }

    private void setValueColum() {
        clm_no_urut.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_id_kategori.setCellValueFactory(new PropertyValueFactory<>(Kategori.ID.get()));
        clm_kategori.setCellValueFactory(new PropertyValueFactory<>(Kategori.KATEGORI.get()));
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
    public void initializeState() {
        txt_id_kategori.setText(generateRefnum(Kategori.TABLENAME.get(), Kategori.ID.get(), "SUB"));
        txt_kategori.requestFocus();
        loadData();
        clear();
        sttsSimpan = true;
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
