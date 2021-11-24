/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.utility;

import static app.retail.controller.general.General.insertToDatabase;
import static app.retail.controller.general.General.selectFromDatabase;
import app.retail.controller.home.HomeController;
import app.retail.model.utility.UserManagementModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Karyawan;
import app.retail.utility.table.Skey;
import app.retail.utility.table.Users;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.config.SystemConfig;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class MasterUserController extends AbstractUtility implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btn_close;
    @FXML
    private JFXTextField txt_user_id;
    @FXML
    private JFXComboBox<String> cmb_karyawan;
    @FXML
    private JFXComboBox<String> cmb_type_akses;
    @FXML
    private Text lbl_pass;
    @FXML
    private JFXPasswordField txt_password;
    @FXML
    private Text lbl_retype;
    @FXML
    private JFXPasswordField txt_retype_password;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_btl;

    private static UserManagementModel mModel;
    private static final HashMap<String, String> MAPKARYAWAN = new HashMap<>();
    private static final HashMap<String, String> MAPTYPEAKSES = new HashMap<>();
    ObservableList<String> karywanList = FXCollections.observableArrayList();
    ObservableList<String> typeaksesList = FXCollections.observableArrayList();
    private static UserManagementController mUController;
    @FXML
    private JFXTextField txt_initial;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        setToolTip();
        setButtonListener();
        initializeState();
    }

    private void performIsEdit() {
        lbl_pass.setVisible(!isEdit);
        lbl_retype.setVisible(!isEdit);
        txt_password.setVisible(!isEdit);
        txt_retype_password.setVisible(!isEdit);
    }

    private void loadKaryawan() throws SQLException {
        String tSQL = "SELECT " + Karyawan.KODEKARYAWAN.get() + ", " + Karyawan.NAMAKARYAWAN.get() + " FROM " + Karyawan.TABLENAME.get()
                + " WHERE " + Karyawan.KODEKARYAWAN.get()
                + " NOT IN (SELECT " + Users.KDKARYAWAN.get() + " FROM " + Users.TABLENAME.get() 
                + " WHERE " + Users.FLAG.get() + "=1)"+ (isEdit ? " OR "+Karyawan.KODEKARYAWAN.get()+"='"+mModel.getKdKaryawan()+"'" : "");
        ResultSet tResult = selectFromDatabase(tSQL);
        cmb_karyawan.getItems().clear();
        karywanList.add("-- Pilih Karyawan --");
        while (tResult.next()) {
            String kode = tResult.getString(Karyawan.KODEKARYAWAN.get());
            String nama = tResult.getString(Karyawan.NAMAKARYAWAN.get());
            MAPKARYAWAN.put(nama, kode);
            karywanList.add(nama);
        }
        cmb_karyawan.setItems(karywanList);
        cmb_karyawan.getSelectionModel().select(0);
    }

    private void loadTypeAkses() throws SQLException {
        String tSQL = "SELECT id,tipe_akses FROM type_akses ORDER BY tipe_akses ASC";
        ResultSet tResult = selectFromDatabase(tSQL);
        cmb_type_akses.getItems().clear();
        typeaksesList.clear();
        MAPTYPEAKSES.clear();
        typeaksesList.add("-- Pilih Type Akses --");
        while (tResult.next()) {
            String kode = tResult.getString("id");
            String nama = tResult.getString("tipe_akses");
            MAPTYPEAKSES.put(nama, kode);
            typeaksesList.add(nama);
        }
        cmb_type_akses.setItems(typeaksesList);
        cmb_type_akses.getSelectionModel().select(0);
    }

    @Override
    public void initializeState() {
        try {
            loadKaryawan();
            loadTypeAkses();
            clear();
            performIsEdit();
            mUController.loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            txt_user_id.requestFocus();
        });
        
        btn_close.setOnAction((ActionEvent event) -> {
            isEdit = false;
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Simpan data user?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    try {
                        String uId = txt_user_id.getText();
                        String kdKar = MAPKARYAWAN.get(cmb_karyawan.getSelectionModel().getSelectedItem());
                        String akses = MAPTYPEAKSES.get(cmb_type_akses.getSelectionModel().getSelectedItem());
                        String pass = txt_password.getText();
                        String retype = txt_retype_password.getText();
                        String sKey = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
                        String init = txt_initial.getText().toUpperCase();
                        if(validateExsistingUsers(uId)){
                            alert = new Alert(Alert.AlertType.WARNING);
                            EZSystem.showAllert(alert, "User ID "+ uId +" Sudah digunakan!", EZIcon.ICON_STAGE);
                            txt_user_id.requestFocus();
                        } else {
                            if (!retype.equals(pass)) {
                                alert = new Alert(Alert.AlertType.WARNING);
                                EZSystem.showAllert(alert, "Password tidak sama!\nMohon ulangi input password", EZIcon.ICON_STAGE);
                                txt_retype_password.setText("");
                                txt_retype_password.requestFocus();
                            } else {
                                RecordEntry tMap = new RecordEntry();
                                tMap.createEntry(Users.USERID.get(), uId);
                                tMap.createEntry(Users.KDKARYAWAN.get(), kdKar);
                                tMap.createEntry(Users.TIPEAKSES.get(), akses);
                                tMap.createEntry(Users.INITIAL.get(), init);
                                if (!isEdit) {
                                    tMap.createEntry(Users.CDT.get(), EZDate.SQLDATE.today());
                                    tMap.createEntry(Users.SID.get(), HomeController.getSession());
                                    tMap.createEntry(Users.PASSWORD.get(), "" + SystemConfig.getEncryptor().encrypt(pass, sKey));
                                    insertToDatabase(tMap, "users");
                                    insertSkey(uId, sKey);
                                } else {
                                    tMap.createEntry(Users.UDT.get(), EZDate.SQLDATE.today());
                                    updateToDatabase(tMap, Users.TABLENAME.get(), Users.USERID.get() + "='" + mModel.getUserId() + "'");
                                    insertSkey(mModel.getUserId(), sKey);
                                }

                                alert = new Alert(Alert.AlertType.INFORMATION);
                                EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
                                if (isEdit) {
                                    if (isCurentUser(mModel.getKdKaryawan())) {
                                        EZSystem.showAllert(EZAlertType.WARNING, "Anda sedang login dengan user id ini.\nMohon login kembali", EZIcon.ICON_STAGE);
                                        HomeController.setIsStartRun(true);
                                        getmHomeController(true).getBtn_logout().fire();
                                    }
                                    mUController.loadData();
                                    btn_close.fire();
                                }else{
                                    initializeState();
                                }
                            }
                        }
                    } catch (SQLException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | NoSuchPaddingException ex) {
                        if (ex.getMessage().contains("Duplicate")) {
                            EZSystem.showAllert(EZAlertType.WARNING, "User ID " + txt_user_id.getText() + " Sudah digunakan!", EZIcon.ICON_APPS);
                        } else {
                            loggingerror(ex);
                        }
                    }
                }
        });

        btn_btl.setOnAction((ActionEvent event) -> {
            initializeState();
        });

    }

    private boolean validateExsistingUsers(String pUID){
        if(isEdit){
            if(pUID.equals(mModel.getUserId())){
                return false;
            }else{
                return mListUserID.contains(pUID);
            }
        }
        
        return mListUserID.contains(pUID);
    }
    
    @Override
    public void clear() {
        txt_password.setText(isEdit ? mModel.getPassword(): "");
        txt_retype_password.setText(isEdit ? mModel.getPassword():"");
        txt_user_id.setText(isEdit ? mModel.getUserId():"");
        txt_initial.setText(isEdit ? mModel.getInitial():"");
        if(isEdit){ 
            cmb_karyawan.getSelectionModel().select(mModel.getNama());
            cmb_type_akses.getSelectionModel().select(mModel.getTipeAkses());
        }
        txt_user_id.requestFocus();
    }

    @Override
    public void setToolTip() {
        btn_btl.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
    }

    private void insertSkey(String id, String sKey) throws SQLException {
        RecordEntry tMap = new RecordEntry();
        if(isEdit){
            tMap.createEntry(Skey.IDUSER.get(), txt_user_id.getText());
            updateToDatabase(tMap, Skey.TABLENAME.get(), Skey.IDUSER.get()+"='"+id+"'");
        }else{
            tMap.createEntry(Skey.ID.get(), UUID.randomUUID().toString());
            tMap.createEntry(Skey.IDUSER.get(), id);
            tMap.createEntry(Skey.KEY.get(), sKey);
            insertToDatabase(tMap, Skey.TABLENAME.get());
        }
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void setmUController(UserManagementController mUController) {
        MasterUserController.mUController = mUController;
    }

    public static void setmModel(UserManagementModel mModel) {
        MasterUserController.mModel = mModel;
    }

}
