/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import app.retail.utility.Akses_List;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLDataException;
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
public class StockController extends AbstractStock implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXButton menu_stcok_opname;
    @FXML
    private JFXButton menu_stock_out;
    @FXML
    private Pane pane_load;
    @FXML
    private JFXButton menu_stock;
    @FXML
    private JFXButton menu_mutasi_stock;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            initializeState();
            loadForm(mUrlChild, getClass().getName(), null);
        } catch (IOException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void initializeState() {
        setToolTip();
        setButtonListener();
    }

    @Override
    public void setButtonListener() {
        menu_stcok_opname.setOnAction((ActionEvent event) -> {
            try {
                if (isAllowed(Akses_List.DATA_STOCK_OPNAME, true)) {
                    final String tUrl = "/app/retail/fxml/stock/DataStockOpname.fxml";
                    loadForm(tUrl, getClass().getName(), event);
                }
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });

        menu_stock_out.setOnAction((ActionEvent event) -> {
            try {
                if (isAllowed(Akses_List.DATA_STOCK_OUT, true)) {
                    final String tUrl = "/app/retail/fxml/stock/DataStockOut.fxml";
                    loadForm(tUrl, getClass().getName(), event);
                }
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });

        menu_stock.setOnAction((ActionEvent event) -> {
            try {
                if (isAllowed(Akses_List.DATA_STOCK, true)) {
                    final String tUrl = "/app/retail/fxml/stock/DataStock.fxml";
                    loadForm(tUrl, getClass().getName(), event);
                }
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });

        menu_mutasi_stock.setOnAction((ActionEvent event) -> {
            try {
                if (isAllowed(Akses_List.DATA_MUTASI_STOCK, true)) {
                    final String tUrl = "/app/retail/fxml/stock/DataMutasiStock.fxml";
                    loadForm(tUrl, getClass().getName(), event);
                }
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 225);
        menu_stcok_opname.setCursor(Cursor.HAND);
        menu_stock_out.setCursor(Cursor.HAND);
        menu_stock.setCursor(Cursor.HAND);
        menu_mutasi_stock.setCursor(Cursor.HAND);
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

    @Override
    public void loadData() throws SQLDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
