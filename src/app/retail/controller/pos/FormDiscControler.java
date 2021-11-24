/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import app.retail.utility.EZAlertType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class FormDiscControler extends AbstractPOS implements Initializable {

    private static POSController mPosController;
    private static boolean isDiscItem = false;
    private static int index = 0;
    private static double totalTagihan = 0;
    
    @FXML
    private AnchorPane anchorePane;
    @FXML
    private ToggleGroup type;
    @FXML
    private Text lbl_rp;
    @FXML
    private TextField txt_disc;
    @FXML
    private Text lbl_persen;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private RadioButton persen;
    @FXML
    private RadioButton amount;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setButtonListener();
        setToolTip();
        initializeState();
    }

    @Override
    public void initializeState() {
    }

    @Override
    public void setButtonListener() {
        btn_batal.setOnAction((event) -> {
            clear();
        });

        persen.setOnAction((event) -> {
            lbl_rp.setVisible(!persen.isSelected());
            lbl_persen.setVisible(persen.isSelected());
            txt_disc.setText("0");
            txt_disc.requestFocus();
        });
        
        amount.setOnAction((event) -> {
            lbl_rp.setVisible(amount.isSelected());
            lbl_persen.setVisible(!amount.isSelected());
            txt_disc.setText("0");
            txt_disc.requestFocus();
        });
        
        txt_disc.setOnAction((event) -> {
           if(txt_disc.getText().equals("") || txt_disc.getText().equals("0")){
               EZSystem.showAllert(EZAlertType.WARNING, "Mohon Isi Nilai Dicount!", EZIcon.ICON_APPS);
           }else{
               double disc = Double.parseDouble(txt_disc.getText().replace(",", ""));
               if(persen.isSelected()){
                   if (disc >100) {
                       EZSystem.showAllert(EZAlertType.WARNING, "Nilai discount melebihi besar dari total belanja!", EZIcon.ICON_APPS);
                   }else{
                       if (isDiscItem) {
                           mPosController.setDiscItem(index, disc);
                       } else {
                           mPosController.setDiscount("%", disc);
                       }
                       clear();
                   }
               }else{
                   if (disc > totalTagihan) {
                       EZSystem.showAllert(EZAlertType.WARNING, "Nilai discount melebihi besar dari total belanja!", EZIcon.ICON_APPS);
                   } else {
                       mPosController.setDiscount("rp", disc);
                       clear();
                   }
               }
           }
        });
        
        Platform.runLater(()->{
            persen.setSelected(true);
            lbl_rp.setVisible(!persen.isSelected());
            lbl_persen.setVisible(persen.isSelected());
            if(isDiscItem){
                amount.setVisible(false);
            }
            txt_disc.setText("0");
            txt_disc.requestFocus();
        });
    }

    @Override
    public void clear() {
        Stage stage = (Stage) btn_batal.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setToolTip() {
        btn_batal.setCursor(Cursor.HAND);
        setCurencyTextfield(txt_disc);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static boolean isIsDiscItem() {
        return isDiscItem;
    }

    public static void setIsDiscItem(boolean isDiscItem) {
        FormDiscControler.isDiscItem = isDiscItem;
    }

    public static POSController getmPosController() {
        return mPosController;
    }

    public static void setmPosController(POSController mPosController) {
        FormDiscControler.mPosController = mPosController;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        FormDiscControler.index = index;
    }

    public static double getTotalTagihan() {
        return totalTagihan;
    }

    public static void setTotalTagihan(double totalTagihan) {
        FormDiscControler.totalTagihan = totalTagihan;
    }

}
