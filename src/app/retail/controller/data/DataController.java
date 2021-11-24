/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.data;

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
public class DataController extends AbstractData implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXButton menu_pelanggan;
    @FXML
    private JFXButton menu_karyawan;
    @FXML
    private JFXButton menu_discount;
    @FXML
    private JFXButton menu_shift;
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
        // TODO
        setToolTip();
        setButtonListener();
        menu_discount.setVisible(false);
        loadForm(getmUrlChild(), getClass().getName(), null);
    }

    @Override
    public void loadData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        try {
            pane_load.getChildren().clear();
            pane_load.getChildren().add(FXMLLoader.load(getClass().getResource(pUrl)));
        } catch (IOException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {
        menu_pelanggan.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DATA_PELANGGAN.get(), true)) {
                String tUrl = "/app/retail/fxml/data/DataPelanggan.fxml";
                loadForm(tUrl, getClass().getName(), event);
            }
        });

        menu_karyawan.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DATA_KARYAWAN.get(), true)) {
                String tUrl = "/app/retail/fxml/data/DataKaryawan.fxml";
                loadForm(tUrl, getClass().getName(), event);
            }
        });

        menu_discount.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DATA.get(), true)) {
                String tUrl = "/app/retail/fxml/data/DataDiscount.fxml";
                loadForm(tUrl, getClass().getName(), event);
            }
        });

        menu_shift.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DATA_SHIFT.get(), true)) {
                String tUrl = "/app/retail/fxml/data/DataShift.fxml";
                loadForm(tUrl, getClass().getName());
            }
        });
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 225);
        menu_shift.setCursor(Cursor.HAND);
        menu_pelanggan.setCursor(Cursor.HAND);
        menu_karyawan.setCursor(Cursor.HAND);
        menu_discount.setCursor(Cursor.HAND);
    }

    @Override
    public void initializeState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
