/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.utility;

import app.retail.controller.home.HomeController;
import app.retail.model.utility.UserManagementModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import co.id.eazy.system.config.SystemConfig;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormRubahPasswordController extends AbstractUtility implements Initializable {

    private static UserManagementModel model;
    private static Stage mStage = new Stage();
    private static Scene mScene;
    
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private Text lbl_kembali;
    @FXML
    private PasswordField txt_pass_lama;
    @FXML
    private Text lbl_kembali1;
    @FXML
    private PasswordField txt_pass_baru;
    @FXML
    private Text lbl_kembali11;
    @FXML
    private PasswordField txt_retype;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setButtonListener();
        setToolTip();
    }

    public static UserManagementModel getModel() {
        return model;
    }

    public static void setModel(UserManagementModel model) {
        FormRubahPasswordController.model = model;
    }
    

    public void simpan() throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchPaddingException, SQLException {
        String id = model.getId();
        String key = getSkey(model.getUserId());
        String pass = SystemConfig.getEncryptor().encrypt(txt_pass_baru.getText(), key);
        String tSQL = "UPDATE users SET password='" + pass + "', u_dt='" + EZDate.SQLDATE.today() + "', sid='" + HomeController.getSession() + "' WHERE id='" + id + "'";
        int x = updateToDatabase(tSQL);
        if (x > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            EZSystem.showAllert(alert, "Password berhasil disimpan", EZIcon.ICON_STAGE);
            btn_batal.fire();
        }
    }
    
    private String getSkey(String uID) throws SQLException{
        String sKey = null;
        String tSQL = "SELECT `key` FROM skey WHERE id_user='" + uID + "'";
        ResultSet res = selectFromDatabase(tSQL);
        if (res.next()) {
            sKey = res.getString("key");
        }
        return sKey;
    }
    
    private boolean validateData() throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SQLException{
        Alert alert = new Alert(Alert.AlertType.WARNING);
        String key = getSkey(model.getUserId());
        String passLama = SystemConfig.getEncryptor().decrypt(model.getPassword(),key);
        boolean isSame = passLama.hashCode() == txt_pass_lama.getText().hashCode();
        if (!isSame) {
            EZSystem.showAllert(alert, "Mohon isi password lama dengan benar!", EZIcon.ICON_STAGE);
            txt_pass_lama.requestFocus();
            txt_pass_lama.setText("");
            return false;
        }
        boolean idRetype = txt_pass_baru.getText().hashCode() == txt_retype.getText().hashCode();
        if (!idRetype) {
            EZSystem.showAllert(alert, "Password tidak sama!\nMohon isi kembali password baru!", EZIcon.ICON_STAGE);
            txt_pass_baru.setText("");
            txt_retype.setText("");
            txt_pass_baru.requestFocus();
            return false;
        }
        
        return true;
    }
    
    @Override
    public void initializeState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setButtonListener() {
        
        Platform.runLater(()->{
            txt_pass_lama.requestFocus();
        });
        
        btn_batal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btn_batal.getScene().getWindow();
                FormRubahPasswordController.mStage = stage;
                stage.close();
                getmUtilityController().getMenu_user_management().fire();
            }
        });
        
        btn_simpan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (validateData()) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        Optional<ButtonType> opt = EZSystem.showAllert(alert, "Simpan password?", EZIcon.ICON_STAGE);
                        if (opt.get() == ButtonType.OK) {
                            simpan();
                        }
                    }
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | SQLException ex) {
                    String tMessage = "ERROR: "+ex.getMessage();
                    EZSystem.showAllert(EZAlertType.ERROR, tMessage, EZIcon.ICON_STAGE);
                    SystemLog.getLogger().writeLog(LogType.ERROR, tMessage+ getStackTraceString(ex));
                }
            }
        });
        
        txt_pass_lama.setOnKeyPressed((KeyEvent evnt)->{
            if (evnt.getCode() == KeyCode.ENTER) {
                txt_pass_baru.requestFocus();
            }
        });
        
        txt_pass_baru.setOnKeyPressed((KeyEvent evnt)->{
            if (evnt.getCode() == KeyCode.ENTER) {
                txt_retype.requestFocus();
            }
        });
        
        txt_retype.setOnKeyPressed((KeyEvent evnt)->{
            if (evnt.getCode() == KeyCode.ENTER) {
                btn_simpan.fire();
            }
        });
        
        mScene= mStage.getScene();
        mScene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.out.println("you press esc");
            }
        });
    }

    @Override
    public void setToolTip() {
        btn_batal.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
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
