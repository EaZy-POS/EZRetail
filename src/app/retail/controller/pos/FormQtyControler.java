/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import app.retail.utility.EZAlertType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class FormQtyControler extends AbstractPOS implements Initializable {

    private static int index;
    private static String qty;
    private static POSController mPosController;
    
    @FXML
    private AnchorPane anchorePane;
    @FXML
    private TextField txt_qty;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setButtonListener();
        initializeState();
    }    

    @Override
    public void initializeState() {
        txt_qty.setText(qty);
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            txt_qty.requestFocus();
        });
        
        txt_qty.setOnAction((event) -> {
            if (!txt_qty.getText().equals("") || !txt_qty.getText().equals("0")) {
                qty = txt_qty.getText();
                mPosController.setQtyItem(index, qty);
                clear();
            }else{
                EZSystem.showAllert(EZAlertType.WARNING, "Isi Qty dengan benar!", EZIcon.ICON_APPS);
            }
        });
    }

    @Override
    public void clear() {
        Stage stage = (Stage) txt_qty.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setToolTip() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        FormQtyControler.index = index;
    }

    public static String getQty() {
        return qty;
    }

    public static void setQty(String qty) {
        FormQtyControler.qty = qty;
    }

    public static POSController getmPosController() {
        return mPosController;
    }

    public static void setmPosController(POSController mPosController) {
        FormQtyControler.mPosController = mPosController;
    }
    
}
