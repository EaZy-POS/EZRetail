/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.utility;

import app.retail.model.utility.UserManagementModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Skey;
import app.retail.utility.table.Users;
import app.retail.utility.table.V_All_Users;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class UserManagementController extends AbstractUtility implements Initializable {

    private JFXButton btnedit, btndelete, btnchange;
    private static final ObservableList<UserManagementModel> userList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<UserManagementModel> tbl_data;
    @FXML
    private TableColumn<UserManagementModel, String> clm_no;
    @FXML
    private TableColumn<UserManagementModel, String> clm_user_id;
    @FXML
    private TableColumn<UserManagementModel, String> clm_karyawan;
    @FXML
    private TableColumn<UserManagementModel, String> clm_akses;
    @FXML
    private TableColumn<UserManagementModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<UserManagementModel, JFXButton> clm_delete;
    @FXML
    private TableColumn<UserManagementModel, JFXButton> clm_change_password;
    @FXML
    private Button btn_tambah;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!isAllowed(Akses_List.INPUT_DATA_USER)) {
            btn_tambah.setDisable(true);
        }

        setToolTip();
        initializeState();
        setButtonListener();
    }

    public void loadData() {
        if (isAllowed(Akses_List.DATA_USER)) {
            tbl_data.getItems().clear();
            userList.clear();
            mListUserID.clear();
            try {
                String tSQL = "SELECT * FROM " + V_All_Users.TABLENAME.get();
                ResultSet tResult = selectFromDatabase(tSQL);
                int no = 0;
                while (tResult.next()) {
                    no++;
                    String id = tResult.getString(V_All_Users.ID.get());
                    String uId = tResult.getString(V_All_Users.USERID.get());
                    String pwd = tResult.getString(V_All_Users.PASSWORD.get());
                    String kdKar = tResult.getString(V_All_Users.KODEKAR.get());
                    String nmKar = tResult.getString(V_All_Users.NAMA.get());
                    String aksesId = tResult.getString(V_All_Users.IDAKSES.get());
                    String aksestipe = tResult.getString(V_All_Users.TYPEAKSES.get());
                    String initial = tResult.getString(V_All_Users.INITIAL.get());
                    mListUserID.add(uId);
                    if (isAllowed(Akses_List.EDIT_DATA_USER)) {
                        btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                        btnedit.setId("" + no);
                        setButtonOnTableView(btnedit, EZButtonType.BTN_EDIT);
                    } else {
                        btnedit = null;
                    }

                    if (isAllowed(Akses_List.DELETE_DATA_USER)) {
                        btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                        btndelete.setId("" + no);
                        setButtonOnTableView(btndelete, EZButtonType.BTN_DELETE);
                    } else {
                        btndelete = null;
                    }
                    if (isAllowed(Akses_List.RUBAH_PASSWORD)) {
                        btnchange = getButton(EZButtonType.BTN_CHANGE_PASSWORD, "Rubah Password");
                        btnchange.setId("" + no);
                        setButtonOnTableView(btnchange, EZButtonType.BTN_CHANGE_PASSWORD);
                    } else {
                        btnchange = null;
                    }
                    UserManagementModel model = new UserManagementModel("" + no, id, uId, kdKar, nmKar, aksesId, aksestipe, pwd, initial, btnedit, btndelete, btnchange);
                    userList.add(model);
                }
                setValueColum();
                tbl_data.setItems(userList);
                tbl_data.requestFocus();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        }
    }

    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_user_id.setCellValueFactory(new PropertyValueFactory<>("userId"));
        clm_karyawan.setCellValueFactory(new PropertyValueFactory<>("nama"));
        clm_akses.setCellValueFactory(new PropertyValueFactory<>("tipeAkses"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btnedit"));
        clm_change_password.setCellValueFactory(new PropertyValueFactory<>("btnchange"));
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        int index = Integer.parseInt(button.getId()) - 1;
        button.setOnAction((ActionEvent event) -> {
            if (type == EZButtonType.BTN_EDIT) {
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Edit data user?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    isEdit = true;
                    UserManagementModel model = tbl_data.getItems().get(index);
                    MasterUserController.setmModel(model);
                    btn_tambah.fire();
                }
            }

            if (type == EZButtonType.BTN_DELETE) {
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Delete data user?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    UserManagementModel model = tbl_data.getItems().get(index);
                    try {
                        delete(model);
                    } catch (SQLException ex) {
                        loggingerror(ex);
                    }
                }
            }

            if (type == EZButtonType.BTN_CHANGE_PASSWORD) {
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Rubah password user ?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    UserManagementModel model = tbl_data.getItems().get(index);
                    FormRubahPasswordController.setModel(model);
                    String tUrl = "/app/retail/fxml/utility/FormRubahPassword.fxml";
                    loadForm(tUrl, "Rubah ");
                }
            }

        });
    }

    private void delete(UserManagementModel model) throws SQLException {
        String tSQL = "DELETE FROM " + Users.TABLENAME.get()
                + " WHERE " + Users.USERID.get() + "='" + model.getUserId() + "'";
        updateToDatabase(tSQL);

        tSQL = "DELETE FROM " + Skey.TABLENAME.get()
                + " WHERE " + Skey.IDUSER.get() + "='" + model.getUserId() + "'";

        updateToDatabase(tSQL);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        EZSystem.showAllert(alert, "Data berhasil dihapus", EZIcon.ICON_STAGE);
        if (isCurentUser(model.getKdKaryawan())) {
            alert = new Alert(Alert.AlertType.WARNING);
            EZSystem.showAllert(alert, "Anda sedang login dengan user id ini.\nMohon login kembali", EZIcon.ICON_STAGE);
            getmHomeController(true).getBtn_logout().fire();
        } else {
            initializeState();
        }

    }

    @Override
    public void initializeState() {
        loadData();
    }

    @Override
    public void setButtonListener() {

        Platform.runLater(() -> {
            tbl_data.requestFocus();
        });

        btn_tambah.setOnAction((evt) -> {
            MasterUserController.setmUController(this);
            String tUrl = "/app/retail/fxml/utility/MasterUser.fxml";
            loadForm(tUrl, "Master User");
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 250);
        AnchorPane.setPrefHeight(getHeight() - 355);
        btn_tambah.setCursor(Cursor.HAND);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_karyawan, Pos.CENTER_LEFT);
        setAligmentColoum(clm_akses, Pos.CENTER_LEFT);
        setAligmentColoum(clm_user_id, Pos.CENTER_LEFT);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {

    }
}
