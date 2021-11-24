/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.finance;

import app.retail.utility.Akses_List;
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
public class FinanceController extends AbstractFinance implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXButton menu_hutang;
    @FXML
    private JFXButton menu_piutang;
    @FXML
    private JFXButton menu_cash_transaction;
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
        try {
            setToolTip();
            setButtonListener();
            loadForm(getmUrlChild(), new ActionEvent());
        } catch (IOException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadForm(String pUrl, ActionEvent event) throws IOException {
        pane_load.getChildren().clear();
        pane_load.getChildren().add(FXMLLoader.load(getClass().getResource(pUrl)));
    }

    @Override
    public void initializeState() {

    }

    @Override
    public void setButtonListener() {
        menu_hutang.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DATA_HUTANG, true)) {
                String tUrl = "/app/retail/fxml/finance/DataHutang.fxml";
                try {
                    loadForm(tUrl, event);
                } catch (IOException ex) {
                    loggingerror(ex);
                }
            }
        });

        menu_piutang.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DATA_PIUTANG, true)) {
                String tUrl = "/app/retail/fxml/finance/DataPiutang.fxml";
                try {
                    loadForm(tUrl, event);
                } catch (IOException ex) {
                    loggingerror(ex);
                }
            }
        });

        menu_cash_transaction.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DATA_CASH_IN_CASH_OUT, true)) {
                try {
                    String tUrl = "/app/retail/fxml/finance/CashTransaction.fxml";
                    loadForm(tUrl, event);
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
        menu_cash_transaction.setCursor(Cursor.HAND);
        menu_hutang.setCursor(Cursor.HAND);
        menu_piutang.setCursor(Cursor.HAND);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
