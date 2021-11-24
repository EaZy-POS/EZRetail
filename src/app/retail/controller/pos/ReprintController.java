/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import app.retail.controller.general.General;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_All_Sale;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class ReprintController extends General implements Initializable {

    private static String mFaktur;
    private static PosMaster mMaster;
    private final ObservableList<String> itemList = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btn_close;
    @FXML
    private JFXButton btn_print;
    @FXML
    private ListView<String> list;

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
        loadData();
    }

    @Override
    public void setButtonListener() {
        btn_close.setOnAction((ActionEvent event) -> {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });
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
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void setData(String mFaktur, PosMaster mMaster) {
        ReprintController.mFaktur = mFaktur;
        ReprintController.mMaster = mMaster;
    }
    
    private void loadData(){
        try {
            String tSQL = "SELECT * FROM "+V_All_Sale.TABLENAME.get()+" WHERE "+V_All_Sale.KODETRANS.get()+" ='"+mFaktur+"'";
            ResultSet result = selectFromDatabase(tSQL);
            String tRow = "";
            if (result.next()) {
                tRow = EZSystem.STRINGFORMARTER.center(" Reprint ", "-", 80);
                itemList.add(tRow);
                tRow = EZSystem.STRINGFORMARTER.center("", "=", 80);
                itemList.add(tRow);
                
                for (Object value : mMapProfile.values()) {
                    tRow = EZSystem.STRINGFORMARTER.center(String.valueOf(value), " ", 80);
                    itemList.add(tRow);
                }
                
                result.beforeFirst();
                while (result.next()) {
                    
                }
            }
            
            list.setItems(itemList);
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
}
