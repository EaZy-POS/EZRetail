/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility;

import javafx.scene.control.Alert;

/**
 *
 * @author RCS
 */
public enum EZAlertType {
    INFO(new Alert(Alert.AlertType.INFORMATION), "Informasi"), 
    NONE(new Alert(Alert.AlertType.NONE), "None"), 
    ERROR (new Alert(Alert.AlertType.ERROR), "Error"), 
    CONFIRM(new Alert(Alert.AlertType.CONFIRMATION), "Konfirmasi"),
    WARNING(new Alert(Alert.AlertType.WARNING), "Peringatan");
    
    private final Alert mAlert;
    private final String mJudul;
    
    private EZAlertType(Alert alert, String judul){
        this.mAlert = alert;
        this.mJudul = judul;
    }
    
    public Alert getAlert(){
        return mAlert;
    }
    
    public String getTitle(){
        return mJudul;
    }
    
    public static EZAlertType parseType(Alert alert){
        if (alert.getAlertType() == INFO.getAlert().getAlertType()) {
            return INFO;
        }
        
        if (alert.getAlertType() == ERROR.getAlert().getAlertType()) {
            return ERROR;
        }
        
        if (alert.getAlertType() == CONFIRM.getAlert().getAlertType()) {
            return CONFIRM;
        }
        
        if (alert.getAlertType() == WARNING.getAlert().getAlertType()) {
            return WARNING;
        }
        
        return null;
    }
}
