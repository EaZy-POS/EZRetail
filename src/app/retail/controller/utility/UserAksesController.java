/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.utility;

import app.retail.controller.home.HomeController;
import app.retail.model.utility.AddUserAksesModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Type_Akses;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class UserAksesController extends AbstractUtility implements Initializable {

    ObservableList<AddUserAksesModel> aksesList = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<AddUserAksesModel> tbl_data;
    @FXML
    private TableColumn<AddUserAksesModel, String> clm_no;
    @FXML
    private TableColumn<AddUserAksesModel, String> clm_user_id;
    @FXML
    private TableColumn<AddUserAksesModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<AddUserAksesModel, JFXButton> clm_delete;
    @FXML
    private JFXButton btn_add;
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(!isAllowed(Akses_List.INPUT_DATA_USER_AKSES)){
            btn_add.setDisable(true);
        }        
        setToolTip();
        setButtonListener();
        initializeState();
    }

    public void loadData() {
        if(isAllowed(Akses_List.DATA_USER_AKSES)){
        tbl_data.getItems().clear();
        aksesList.clear();
        try {
            String tSQL = "SELECT * FROM "+Type_Akses.TABLENAME.get()
                    +" WHERE "+Type_Akses.ID.get()+" !='*0000#' ORDER BY "
                    +Type_Akses.TIPEAKSES.get()+" ASC";
            ResultSet res = selectFromDatabase(tSQL);
            int no = 0;
            while (res.next()) {                
                no++;
                String id = res.getString(Type_Akses.ID.get());
                JFXButton btnedit = null;
                String type = res.getString(Type_Akses.TIPEAKSES.get());
                String akses = res.getString(Type_Akses.AKSES.get());
                if (isAllowed(Akses_List.EDIT_USER_AKSES)) {
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId("" + no);
                    setButtonOnTableView(btnedit, EZButtonType.BTN_EDIT);
                }
                
                JFXButton btndelete = null;
                if(isAllowed(Akses_List.DELETE_USER_AKSES)){
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId(""+no);
                    setButtonOnTableView(btndelete, EZButtonType.BTN_DELETE);
                }
                
                AddUserAksesModel model = new AddUserAksesModel(""+no, id, type, akses,btnedit, btndelete);
                aksesList.add(model);
            }
            setValueColumModul();
            tbl_data.setItems(aksesList);
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        }
    }
    
    private void setValueColumModul(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_user_id.setCellValueFactory(new PropertyValueFactory<>("typeAkses"));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btnedit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent)->{
            int index = Integer.parseInt(button.getId())-1;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            if(type == EZButtonType.BTN_EDIT){
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Edit data ?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    AddUserAksesModel model = tbl_data.getItems().get(index);
                    AddUserAksesController.setmModel(model);
                    btn_add.fire();
                }
            }
            
            if(type == EZButtonType.BTN_DELETE){
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Delete data ?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    String id = tbl_data.getItems().get(index).getIdAkses();
                    try {
                        delete(id);
                    } catch (SQLException ex) {
                        String msg = ex.getMessage();
                        if (msg.contains("Cannot delete or update a parent row")) {
                            msg = "Data tidak bisa dihapus!\nData sudah digunakan";
                        }
                        EZSystem.showAllert(EZAlertType.ERROR, msg, EZIcon.ICON_STAGE);
                        SystemLog.getLogger().writeLog(LogType.ERROR, msg + getStackTraceString(ex));
                    }
                }
            }
        });
    }
    
    private void delete(String id) throws SQLException{
        String tSQL = "DELETE FROM type_akses WHERE id='"+id+"'";
        int x = updateToDatabase(tSQL);
        if (x>0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            EZSystem.showAllert(alert, "Data berhasil dihapus", EZIcon.ICON_STAGE);
            initializeState();
        }
    }

    @Override
    public void initializeState() {
        loadData();
    }

    @Override
    public void setButtonListener() {
        btn_add.setOnAction((ActionEvent event) -> {
            String tUrl = "/app/retail/fxml/utility/AddUserAkses.fxml";
            AddUserAksesController.setmController(this);
            loadForm(tUrl, "User Akses");
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth()-250);
        AnchorPane.setPrefHeight(getHeight()-355);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_user_id, Pos.CENTER_LEFT);
        btn_add.setCursor(Cursor.HAND);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
