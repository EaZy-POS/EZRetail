/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.master;

import app.retail.controller.home.HomeController;
import app.retail.model.master.SubKategoriModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Kategori;
import app.retail.utility.table.Sub_Kategori;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
public class MasterSubKategoriController extends AbstractMaster implements Initializable {

    ObservableList<SubKategoriModel> subkategoriList = FXCollections.observableArrayList();
    private static boolean sttsSimpan;    
    private JFXButton btnedit,btndelete;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btn_simpan;
    @FXML
    private Button btn_cancel;
    @FXML
    private TableView<SubKategoriModel> tbl_subkategori;
    @FXML
    private TableColumn<SubKategoriModel, String> clm_no_urut;
    @FXML
    private TableColumn<SubKategoriModel, String> clm_id_subkategori;
    @FXML
    private TableColumn<SubKategoriModel, String> clm_subkategori;
    @FXML
    private TableColumn<SubKategoriModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<SubKategoriModel, JFXButton> clm_delete;
    @FXML
    private TextField txt_id_subkategori;
    @FXML
    private TextField txt_sub_kategori;
    @FXML
    private ComboBox<String> cmb_kategori;
    @FXML
    private Button btn_close;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!HomeController.isAllowed("Input Data Sub Kategori")) {
            btn_simpan.setDisable(true);
        }
        txt_id_subkategori.setEditable(false);
        setButtonListener();
        initializeState();
        setToolTip();
    }

    public void setButtonOnTableView(JFXButton button, int type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == 0) {
                Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Edit data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    sttsSimpan = false;
                    loadData(Integer.parseInt(button.getId()));
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
    
    private void loadData(int  index) {
        SubKategoriModel model = tbl_subkategori.getItems().get(index);
        txt_id_subkategori.setText(model.getId());
        txt_sub_kategori.setText(model.getSub_kategori());
        cmb_kategori.getSelectionModel().select(model.getKategori());
    }

    public void delete(String param) {
        String tQuery = "DELETE FROM "+Sub_Kategori.TABLENAME.get()+" WHERE "+Sub_Kategori.ID.get()+"='"+param+"'";
        try {
            int x = updateToDatabase(tQuery);
            if (x == 1) {
                EZSystem.showAllert(EZAlertType.INFO, "Data berhasil dihapus", EZIcon.ICON_STAGE);
                String tKat = cmb_kategori.getValue();
                sttsSimpan = true;
                loadKategori();
                cmb_kategori.getSelectionModel().select(tKat);
                txt_id_subkategori.setText(genereateID(10));
                txt_sub_kategori.setText("");
                txt_sub_kategori.requestFocus();
            }
        } catch (SQLException  e) {
            if (e.toString().contains("Cannot delete or update a parent row")) {
                EZSystem.showAllert(EZAlertType.ERROR, "Data tidak bisa dihapus, data sudah dipakai!",EZIcon.ICON_STAGE);
            }else{
                EZSystem.showAllert(EZAlertType.ERROR, "Error Delete data With id "+param+"\n"+e, EZIcon.ICON_STAGE);
            }
            initializeState();
        }
    }

    @Override
    public void clear() {
        txt_sub_kategori.setText("");
        txt_sub_kategori.requestFocus();
        tbl_subkategori.getItems().clear();
    }

    public void simpan() {
        RecordEntry tMap = new RecordEntry();
        tMap.createEntry(Sub_Kategori.ID.get(), txt_id_subkategori.getText());
        String tKat = cmb_kategori.getSelectionModel().getSelectedItem();
        tMap.createEntry(Sub_Kategori.SUBKATEGORI.get(), txt_sub_kategori.getText());
        tMap.createEntry(Sub_Kategori.IDKATEGORI.get(), MAP_KATEGORI.get(cmb_kategori.getSelectionModel().getSelectedItem()));
        tMap.createEntry(Sub_Kategori.SID.get(), HomeController.getSession());
        try {
            if (sttsSimpan) {
                tMap.createEntry(Sub_Kategori.CDT.get(), EZDate.SQLDATETIME.today());
                int x = insertToDatabase(tMap, Sub_Kategori.TABLENAME.get());
                if (x == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
                }
            }else{
                tMap.createEntry(Sub_Kategori.UDT.get(), EZDate.SQLDATETIME.today());
                int x = updateToDatabase(tMap, Sub_Kategori.TABLENAME.get(), Sub_Kategori.ID.get()+"='"+txt_id_subkategori.getText()+"'");
                if (x == 1) {
                    EZSystem.showAllert(EZAlertType.INFO, "Data berhasil di Edit", EZIcon.ICON_STAGE);
                }
            }
            
            sttsSimpan = true;
            loadKategori();
            cmb_kategori.getSelectionModel().select(tKat);
            txt_id_subkategori.setText(genereateID(10));
            txt_sub_kategori.setText("");
            txt_sub_kategori.requestFocus();
        } catch (SQLException e) {
            loggingerror(e);
        }
    }

    @Override
    public void setButtonListener() {
        btn_simpan.setOnAction((ActionEvent event) -> {
            if(!txt_sub_kategori.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType>  option = EZSystem.showAllert(alert, "Simpan Data?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    simpan();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                EZSystem.showAllert(alert, "Isi data dengan benar!", EZIcon.ICON_STAGE);
                txt_sub_kategori.requestFocus();
            }
        });
        
        btn_close.setOnAction((ActionEvent event) -> {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });
        
        btn_cancel.setOnAction((ActionEvent event) -> {
            initializeState();
        });
        
        cmb_kategori.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadSub();
        });
        
        txt_sub_kategori.setOnKeyPressed((KeyEvent evt)->{
            if (evt.getCode() == KeyCode.ENTER) {
                btn_simpan.fire();
            }
            
            if (evt.getCode() == KeyCode.DOWN) {
                tbl_subkategori.requestFocus();
            }
        });
        
        tbl_subkategori.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                if(tbl_subkategori.getItems().size()>0){
                    tbl_subkategori.getSelectionModel().select(0);
                }else{
                    tbl_subkategori.requestFocus();
                }
            }
        });
    }
    
    private void loadSub(){
        String val = cmb_kategori.getValue();
        if (MAP_KATEGORI.containsKey(val)) {
            String id = MAP_KATEGORI.get(val);
            loadSubKategori(id);
        }
    }

    @Override
    public void loadSubKategori(String pId) {
        tbl_subkategori.getItems().clear();
        String tQuery = "SELECT * FROM "+Sub_Kategori.TABLENAME.get()+" JOIN kategori on sub_kategori.id_kategori = kategori.id  WHERE "+Sub_Kategori.IDKATEGORI.get()+"='"+pId+"'  ORDER BY "+Sub_Kategori.SUBKATEGORI.get()+" ASC";
        try {
            ResultSet Result = selectFromDatabase(tQuery);
            int no = 0;
            while (Result.next()) {
                no++;
                if(HomeController.isAllowed("Edit Data Sub Kategori")){
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId(""+(no-1));
                    setButtonOnTableView(btnedit, 0);
                }else{
                    btnedit = null;
                }
                
                if(HomeController.isAllowed("Delete Data Sub Kategori")){
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Hapus");
                    btndelete.setId(Result.getString(Sub_Kategori.ID.get()));
                    setButtonOnTableView(btndelete, 1);
                }else{
                    btndelete = null;
                }
                subkategoriList.add(new SubKategoriModel(Result.getString(Sub_Kategori.ID.get()),  Result.getString(Sub_Kategori.SUBKATEGORI.get()), "", Result.getString(Kategori.KATEGORI.get()), ""+no, btnedit, btndelete));
            }
            setValueColum();
            tbl_subkategori.setItems(subkategoriList);
        } catch (SQLException  e) {
            loggingerror(e);
        }catch(Exception ex){
            loggingerror(ex);
        }
    }

    @Override
    public void loadData() {
        
    }    

    private void setValueColum(){
        clm_no_urut.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_id_subkategori.setCellValueFactory(new PropertyValueFactory<>(Sub_Kategori.ID.get()));
        clm_subkategori.setCellValueFactory(new PropertyValueFactory<>(Sub_Kategori.SUBKATEGORI.get()));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btnedit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
    }
    
    @Override
    public void loadKategori() {
        List<String> tMapKategori = getKategori();
        cmb_kategori.getItems().clear();
        ObservableList<String> kategoriList = FXCollections.observableArrayList();
        kategoriList.add("-- Pilih Kategori --");
        for (int i = 0; i < tMapKategori.size(); i++) {
            String valString = tMapKategori.get(i);
            kategoriList.add(valString);
        }
        cmb_kategori.setItems(kategoriList);
        cmb_kategori.getSelectionModel().select(0);
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
        sttsSimpan=true;
        loadKategori();
        txt_id_subkategori.setText(generateRefnum(Sub_Kategori.TABLENAME.get(), Sub_Kategori.ID.get(), "SUBKAT"));
        clear();
    }

    @Override
    public void setToolTip() {
        btn_cancel.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
