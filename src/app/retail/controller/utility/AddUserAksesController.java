/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.utility;

import static app.retail.controller.general.General.getStackTraceString;
import app.retail.model.utility.AddUserAksesModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Modul;
import app.retail.utility.table.Sub_Modul;
import app.retail.utility.table.Type_Akses;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.application.Platform;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class AddUserAksesController extends AbstractUtility implements Initializable {

    ObservableList<AddUserAksesModel> modulList = FXCollections.observableArrayList();
    ObservableList<AddUserAksesModel> submodulList = FXCollections.observableArrayList();
    private static HashMap<String, List<AddUserAksesModel>> mDataModul = new HashMap<>();
    private static UserAksesController mController;
    private static AddUserAksesModel mModel;
    
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private TableView<AddUserAksesModel> tbl_modul;
    @FXML
    private TableView<AddUserAksesModel> tbl_sub_modul;
    @FXML
    private TableColumn<AddUserAksesModel, CheckBox> clm_modul;
    @FXML
    private TableColumn<AddUserAksesModel, CheckBox> clm_sub_modul;
    @FXML
    private JFXTextField txt_type_akses;
    @FXML
    private CheckBox check_all_modul;
    @FXML
    private CheckBox check_all_submodul;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btn_close;
    @FXML
    private JFXButton btn_batal;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setToolTip();
        setButtonListener();
        initializeState();
    }    

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void simpan() {
        String typeAkses = txt_type_akses.getText();
        String stringkses = "";
        if (typeAkses.equals("")) {
            EZSystem.showAllert(EZAlertType.WARNING, "Isi tipe akses dengan benar!", EZIcon.ICON_STAGE);
            txt_type_akses.requestFocus();
        }else{
            try {
                for (AddUserAksesModel addUserAksesModel : tbl_modul.getItems()) {
                    CheckBox box = addUserAksesModel.getModul();
                    if(box.isSelected()){
                        stringkses += "["+box.getText()+"]";
                        String id = addUserAksesModel.getId();
                        List<AddUserAksesModel> mapSubModul = mDataModul.get(id);
                        for (AddUserAksesModel model : mapSubModul) {
                            CheckBox boxSub = model.getModul();
                            if (boxSub.isSelected()) {
                                stringkses+=";";
                                stringkses += "["+boxSub.getText()+"]";
                            }
                        }
                    }
                    if(stringkses.startsWith("[") && stringkses.endsWith("]")){
                        stringkses+=";";
                    }
                }
                if (stringkses.endsWith(";")) {
                    stringkses = stringkses.substring(0, stringkses.length()-1);
                }
                
                RecordEntry tMap = new RecordEntry();
                if(!isEdit){
                    tMap.createEntry("id", UUID.randomUUID().toString().substring(0, 6));
                    tMap.createEntry("tipe_akses", typeAkses);
                    tMap.createEntry("akses", stringkses);
                    insertToDatabase(tMap, "type_akses");
                    EZSystem.showAllert(EZAlertType.INFO, "Data berhasil disimpan", EZIcon.ICON_STAGE);
                    initializeState();
                }else{
                    tMap.createEntry("tipe_akses", typeAkses);
                    tMap.createEntry("akses", stringkses);
                    updateToDatabase(tMap, Type_Akses.TABLENAME.get(), Type_Akses.ID.get()+"='" + mModel.getIdAkses() + "'");
                    EZSystem.showAllert(EZAlertType.INFO, "Data berhasil dirubah", EZIcon.ICON_STAGE);
                    btn_close.fire();
                }
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        }
    }

    public void setButtonOnTableView(CheckBox box) {
        box.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (box.isSelected()) {
                loadSubModul(box.getId());
            }else{
                tbl_sub_modul.getItems().clear();
            }
        });
    }
    
    private void loadModul() throws SQLException{
        String tSQL = "SELECT "+Modul.TABLENAME.get()+".*, "+Sub_Modul.SUBMODUL.get()
                +" FROM "+Modul.TABLENAME.get()
                +" LEFT JOIN "+Sub_Modul.TABLENAME.get()
                +" ON "+Sub_Modul.TABLENAME.get()+"."+Sub_Modul.IDMODUL.get()+"="+Modul.TABLENAME.get()+"."+Modul.ID.get()
                +" ORDER BY "+Modul.MODUL.get()+","+Sub_Modul.GROUPID.get()+","+Sub_Modul.SUBMODUL.get()+" ASC";
        ResultSet result = selectFromDatabase(tSQL);
        tbl_modul.getItems().clear();
        mDataModul = new HashMap<>();
        
        List<String> tListAkses = new ArrayList<>();
        
        if(isEdit){
            String[] akses = mModel.getAkses().split(";");
            tListAkses.addAll(Arrays.asList(akses));
        }
        
        while (result.next()) {            
            String id = result.getString("id");
            String modul = result.getString("modul");
            CheckBox cekBox = new CheckBox(modul);
            cekBox.setId(id);
            setButtonOnTableView(cekBox);
            AddUserAksesModel model = new AddUserAksesModel(cekBox, id);
            cekBox.setSelected(tListAkses.contains("["+modul+"]"));
            
            List<AddUserAksesModel> list;
            
            String idSubModul = "0";
            String subModul = result.getString(Sub_Modul.SUBMODUL.get());
            CheckBox box = new CheckBox(subModul);
            AddUserAksesModel submodel = new AddUserAksesModel(box, idSubModul);
            box.setSelected(tListAkses.contains("["+subModul+"]"));
            
            if(mDataModul.containsKey(id)){
                list = mDataModul.get(id);
                list.add(submodel);
            }else{
                list = new ArrayList<>();
                list.add(submodel);
                modulList.add(model);
            }
            
            mDataModul.put(id, list);
        }
        setValueColumModul();
        tbl_modul.setItems(modulList);
    }
    
    private void loadSubModul(String id){
        if(mDataModul.containsKey(id)){
            tbl_sub_modul.getItems().clear();
            List<AddUserAksesModel> mapSubModul = mDataModul.get(id);
            mapSubModul.forEach(addUserAksesModel -> {
                submodulList.add(addUserAksesModel);
            });
            
            setValueColumSubModul();
            tbl_sub_modul.setItems(submodulList);
        }
    }
    
    private void setValueColumModul(){
        clm_modul.setCellValueFactory(new PropertyValueFactory<>("modul"));
    }
    
    private void setValueColumSubModul(){
        clm_sub_modul.setCellValueFactory(new PropertyValueFactory<>("modul"));
    }

    @Override
    public void initializeState() {
        try {
            if(!isEdit){
                txt_type_akses.setText("");
                txt_type_akses.requestFocus();
                tbl_sub_modul.getItems().clear();
                loadModul();
                mController.loadData();
            }else{
                loadDataEdit();
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    private void loadDataEdit() throws SQLException{
        txt_type_akses.setText(mModel.getTypeAkses());
        loadModul();
    }
    
    @Override
    public void setButtonListener() {
        btn_close.setOnAction((ActionEvent event) -> {
            mController.loadData();
            isEdit = false;
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });
        
        btn_batal.setOnAction((evt)->{
            initializeState();
        });
        
        check_all_modul.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            for (int i = 0; i < tbl_modul.getItems().size(); i++) {
                tbl_modul.getItems().get(i).getModul().setSelected(check_all_modul.isSelected());
            }
 
        });
        
        check_all_submodul.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            for (int i = 0; i < tbl_sub_modul.getItems().size(); i++) {
                tbl_sub_modul.getItems().get(i).getModul().setSelected(check_all_submodul.isSelected());
            }
 
        });
        
        Platform.runLater(()->{
            txt_type_akses.requestFocus();
        });
        
        btn_simpan.setOnAction((ActionEvent ActionEvent) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            String msg;
            if (isEdit) {
                msg = "Simpan perubahan data?";
            }else{
                msg = "Simpan data?";
            }
            Optional<ButtonType> opt = EZSystem.showAllert(alert, msg, EZIcon.ICON_STAGE);
            if (opt.get() == ButtonType.OK) {
                simpan();
            }
        });
        
        
        tbl_modul.setOnMouseClicked((MouseEvent event) -> {
            int index = tbl_modul.getSelectionModel().getSelectedIndex();
            if(tbl_modul.getItems().get(index).getModul().isSelected()){
                String id = tbl_modul.getItems().get(index).getId();
                loadSubModul(id);
            }else{
                tbl_sub_modul.getItems().clear();
            }
        });
    }

    @Override
    public void setToolTip() {
        btn_simpan.setCursor(Cursor.HAND);
        btn_batal.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void setmController(UserAksesController mController) {
        AddUserAksesController.mController = mController;
    }

    public static void setmModel(AddUserAksesModel mModel) {
        isEdit = true;
        AddUserAksesController.mModel = mModel;
    }

}
