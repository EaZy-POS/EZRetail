/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author RCS
 */
public class EZSystem {
    public static final FormatString STRINGFORMARTER = new FormatString();
    
    public static Optional<ButtonType> showAllert(EZAlertType alertType, String pesan, EZIcon icon) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        
        if (null != alertType)
        switch (alertType) {
            case CONFIRM:
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                break;
            case ERROR:
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            case INFO:
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            case WARNING:
                alert = new Alert(Alert.AlertType.WARNING);
            default:
                break;
        }else{
            alertType = EZAlertType.CONFIRM;
        }
        
        alert.setTitle(alertType.getTitle());
        alert.setContentText(pesan);
        alert.setHeaderText(null);
        alert.setWidth(100);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon.get());
        return alert.showAndWait();
    }
    
    public static Optional<ButtonType> showAllert(Alert alert, String pesan, EZIcon icon) {
        EZAlertType type = EZAlertType.parseType(alert);
        alert.setTitle(type.getTitle());
        alert.setContentText(pesan);
        alert.setHeaderText(null);
        alert.setWidth(100);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon.get());
        return alert.showAndWait();
    }
    
}
