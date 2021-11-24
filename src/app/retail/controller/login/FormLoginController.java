/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.login;

import app.retail.controller.general.General;
import app.retail.controller.general.MapKaryawan;
import app.retail.controller.home.HomeController;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Session;
import app.retail.utility.table.V_Shift;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.config.SystemConfig;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormLoginController extends General implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_cancel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO   
        enterPressed();
        resetFields();
    }    

    @FXML
    private void minusAction(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void closeAction(ActionEvent event) {
        Platform.exit();
    }
    
    private void resetFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }

    @FXML
    private void loginAction(ActionEvent event) throws Exception {
        authenticate(event);
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        resetFields();
    }
    
    private void enterPressed() {

        usernameField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    authenticate(ke);
                } catch (Exception ex) {
                    loggingerror(ex);
                }
            }
        });
        
        Platform.runLater(() -> {
            usernameField.requestFocus();
        });
    }
    
    private void authenticate(Event event) throws Exception {        
        if (validateInput()) {
            String path = "/app/retail/fxml/home/Home.fxml";
            ((Node) (event.getSource())).getScene().getWindow().hide();
            windows(path);
        } else {
            resetFields();
            errorLabel.setText("User Tidak ditemukan!");
        }
    }
    
    private void windows(String path) throws Exception {
        SystemLog.getLogger().writeLog(LogType.TRACE, "Load: "+ path);
        HomeController.setmUrlForm("/app/retail/fxml/inventory/MasterItem.fxml");
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Stage stage = new Stage();
        Scene scene = new Scene(root);        
        stage.setMaximized(true);
        stage.setTitle("EZPOS Retail:: Version 1.0.0");
        stage.getIcons().add(EZIcon.ICON_APPS.get());
        stage.setScene(scene);
        stage.show();
        Platform.setImplicitExit(true);
        stage.setOnCloseRequest((WindowEvent we) -> {
            Alert alesrt = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> opt = EZSystem.showAllert(alesrt, "Anda ingin menutup aplikasi?", EZIcon.ICON_STAGE);
            if (opt.get() == ButtonType.OK) {
                try {
                    RecordEntry tRecordEntry = new RecordEntry();
                    tRecordEntry.createEntry(Session.SESSIONEND.get(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    String tWhere = Session.SESSIONID.get()+"='"+HomeController.getSession()+"'";
                    updateToDatabase(tRecordEntry, Session.TABLENAME.get(), tWhere);
                } catch (SQLException ex) {
                }
                Platform.exit();
            }else{
                we.consume();
            }
        });
        SystemLog.getLogger().writeLog(LogType.TRACE, "After load: "+path);
    }
    
    private boolean validateInput() throws SQLException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchPaddingException {
        String errorMessage = "";

        if (usernameField.getText() == null || usernameField.getText().length() == 0) {
            errorMessage += "Please enter credentials!\n";
            errorLabel.setText(errorMessage);
            usernameField.requestFocus();
            return false;
        }else if(passwordField.getText().length() == 0){
            errorMessage += "Please enter password!\n";
            errorLabel.setText(errorMessage);
            passwordField.requestFocus();
            return false;
        }
        
        ResultSet res = validateLogin();
        if (res == null || !res.next()) {
            errorMessage += "User tidak ditemukan!\nMohon isi user dan password dengan benar.";
            EZSystem.showAllert(EZAlertType.WARNING, errorMessage, EZIcon.ICON_STAGE);
            usernameField.requestFocus();
            resetFields();
            return false;
        }else{
            if(res.getInt("flag") == 2){
                errorMessage += "User tidak aktif.";
                EZSystem.showAllert(EZAlertType.WARNING, errorMessage, EZIcon.ICON_STAGE);
                usernameField.requestFocus();
                resetFields();
                return false;
            }else{
                errorMessage += "Login berhasil\n";
                errorLabel.setText(errorMessage);
                String ses = generateSession();
                String tAkses = res.getString("akses");
                LinkedList<String> listAkses = new LinkedList<>();
                if(tAkses.equals("*****")){
                    listAkses.add(tAkses);
                }else{
                    String[] akses = tAkses.split(";");
                    listAkses.addAll(Arrays.asList(akses));
                }
                HomeController.setHakAksesList(listAkses);
                mMapKaryawan = new HashMap<>();
                mMapKaryawan.put(MapKaryawan.KODEKARYAWAN.get(), res.getString("kd_karyawan"));
                mMapKaryawan.put(MapKaryawan.NAMAKARYAWAN.get(), res.getString("nama_karyawan"));
                mMapKaryawan.put(MapKaryawan.INITIAL.get(), res.getString("initial"));
                String[] shift = getShift();
                mMapKaryawan.put(MapKaryawan.SHIFT.get(), shift != null ? shift[0] : null);
                mMapKaryawan.put(MapKaryawan.KODESHIFT.get(), shift != null ? shift[1] : null);
                setSession(ses,usernameField.getText() ,res.getString("kd_karyawan"));
                return true;
            }
        }
    }
    
    private String[] getShift() throws SQLException{
        String tSQL = "SELECT * FROM "+V_Shift.TABLENAME.get()
                +" WHERE NOW() BETWEEN "+V_Shift.AWAL.get()+" AND "+V_Shift.AKHIR.get();
        String[] result = null;
        ResultSet res = selectFromDatabase(tSQL);
        if (res.next()) {
            result = new String[]{res.getString(V_Shift.SHIFT.get()).toUpperCase(),res.getString(V_Shift.KODE.get())};
            return result;
        }
        
        return result;
    }
    
    private ResultSet validateLogin() throws SQLException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchPaddingException{
        String txtUserID = usernameField.getText();
        String txtPass = String.valueOf(passwordField.getText());
        String key = getSkey(txtUserID);
        if (key==null || key.equals("")) {
            return null;
        }
        String pass = SystemConfig.getEncryptor().encrypt(txtPass, key);
        String tSQL = getQuery(txtUserID, pass);
        return selectFromDatabase(tSQL);
    }
    
    private String getQuery(String uID, String pass){
        String tSQL = "SELECT u.kd_karyawan,u.flag, a.akses, k.nama_karyawan, u.initial "
                + "FROM users u INNER JOIN type_akses a ON a.id=u.tipe_akses "
                + "INNER JOIN karyawan k ON u.kd_karyawan = k.kode_karyawan "
                + "WHERE u.User_id='"+uID+"' AND u.password='"+pass+"'";
        return tSQL;
    }
    
    private String getSkey(String uID){
        String sKey = null;
        try {
            String tSQL = "SELECT `key` FROM skey WHERE id_user='"+uID+"'";
            ResultSet res = selectFromDatabase(tSQL);
            if (res.next()) {
                sKey = res.getString("key");
            }
        } catch (SQLException ex) {
            SystemLog.getLogger().writeLog(LogType.ERROR, "ERROR getKey: "+ex);
            EZSystem.showAllert(EZAlertType.ERROR, ex.getMessage(), EZIcon.ICON_STAGE);
        }        
        return sKey;
    }
    
    private void setSession(String session,String uid ,String kdKar) throws SQLException{
        RecordEntry tMap = new RecordEntry();
        tMap.createEntry(Session.SESSIONID.get(), session);
        tMap.createEntry(Session.KDKARYAWAN.get(), kdKar);
        tMap.createEntry(Session.USERID.get(), uid);
        tMap.createEntry(Session.SESSIONSTART.get(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        tMap.createEntry(Session.SESSIONEND.get(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        HomeController.setSession(session);
        insertToDatabase(tMap, Session.TABLENAME.get());
    }
    
    private String generateSession(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
    }

    @Override
    public void initializeState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setButtonListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setToolTip() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
