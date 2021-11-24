/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail;

import app.retail.controller.general.General;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author RCS
 */
public class AppRetail extends Application {
    private static double xOffset = 0;
    private static double yOffset = 0;
    
    @Override
    public void start(Stage primaryStage) {
        try{
            SystemLog.getLogger().writeLog(LogType.TRACE, "Starting aplication ..");
            final String tUrl = "/app/retail/fxml/login/FormLogin.fxml";
            openForm(tUrl, getClass().getName(), null);
        }catch(IOException e){
            String tErroMessage = "ERROR Starting Aplication."+ e.getMessage();
            EZSystem.showAllert(EZAlertType.ERROR, tErroMessage, EZIcon.ICON_STAGE);
            SystemLog.getLogger().writeLog(LogType.ERROR, tErroMessage + General.getStackTraceString(e));
        }
    }
    
    private void openForm(String pUrl, String pName, javafx.event.ActionEvent event) throws IOException {        
            Parent root = FXMLLoader.load(getClass().getResource(pUrl));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("EZPOS Retail:: Version 1.0.0");
            stage.getIcons().add(new Image("/app/retail/images/EaZy POS IC.png"));
            root.setOnMousePressed((MouseEvent e) -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent e) -> {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
