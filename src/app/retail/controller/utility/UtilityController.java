/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.utility;

import app.retail.controller.general.General;
import app.retail.utility.Akses_List;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class UtilityController extends AbstractUtility implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXButton menu_user_management;
    @FXML
    private JFXButton menu_user_akses;
    @FXML
    private JFXButton menu_setting;
    @FXML
    private Pane pane_load;

    /**
     * Initializes the controller class.
     *
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
    public void initializeState() {
        menu_user_management.fire();
        setmUtilityController(this);
    }

    public JFXButton getMenu_user_management() {
        return menu_user_management;
    }

    @Override
    public void setButtonListener() {
        menu_user_management.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DATA_USER, true)) {
                final String tUrl = "/app/retail/fxml/utility/UserManagement.fxml";
                try {
                    loadForm(tUrl, getClass().getName(), event);
                } catch (IOException ex) {
                    loggingerror(ex);
                }
            }
        });

        menu_setting.setOnAction((ActionEvent event) -> {
//            final String tUrl = "/app/retail/fxml/utility/UserManagement.fxml";
//            try {
//                loadForm(tUrl,getClass().getName(), event);
//            } catch (IOException ex) {
//                String tErroMessage = "ERROR: " + ex.getMessage();
//                EZSystem.showAllert(EZAlertType.ERROR, tErroMessage, EZIcon.ICON_STAGE);
//                SystemLog.getLogger().writeLog(LogType.ERROR, tErroMessage + General.getStackTraceString(ex));
//            }
        });

        menu_user_akses.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DATA_USER_AKSES, true)) {
                final String tUrl = "/app/retail/fxml/utility/UserAkses.fxml";
                try {
                    loadForm(tUrl, getClass().getName(), event);
                } catch (IOException ex) {
                    loggingerror(ex);
                }
            }
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 225);
        menu_setting.setCursor(Cursor.HAND);
        menu_user_akses.setCursor(Cursor.HAND);
        menu_user_management.setCursor(Cursor.HAND);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        pane_load.getChildren().clear();
        pane_load.getChildren().add(FXMLLoader.load(getClass().getResource(pUrl)));
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
