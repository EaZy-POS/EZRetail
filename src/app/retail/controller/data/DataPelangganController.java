/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.data;

import app.retail.model.data.PelangganModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Pelanggan;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class DataPelangganController extends AbstractData implements Initializable {

    private final ObservableList<PelangganModel> pelangganList = FXCollections.observableArrayList();
    private JFXButton btnedit, btndelete;
    private static String mQuery;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<PelangganModel> tbl_data;
    @FXML
    private Button btn_print;
    @FXML
    private JFXTextField txt_cari;
    @FXML
    private TableColumn<PelangganModel, String> clm_no;
    @FXML
    private TableColumn<PelangganModel, String> clm_id_pelanggan;
    @FXML
    private TableColumn<PelangganModel, String> clm_nama;
    @FXML
    private TableColumn<PelangganModel, String> clm_alamat;
    @FXML
    private TableColumn<PelangganModel, String> clm_telp;
    @FXML
    private TableColumn<PelangganModel, String> clm_email;
    @FXML
    private TableColumn<PelangganModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<PelangganModel, JFXButton> clm_delete;
    @FXML
    private TableColumn<PelangganModel, String> clm_pekerjaan;
    @FXML
    private Button btn_tambah;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!isAllowed(Akses_List.INPUT_DATA_PELANGGAN.get())) {
            btn_tambah.setDisable(true);
        }

        setToolTip();
        setButtonListener();
        initializeState();
    }

    @Override
    public void loadData() {
        tbl_data.getItems().clear();
        try {
            String tSQL = "SELECT * FROM " + Pelanggan.TABLENAME.get() + " WHERE " + Pelanggan.IDPELANGGAN.get() + "!='0000' ORDER BY "+Pelanggan.NAMAPELANGGAN.get()+" ASC";
            mQuery = tSQL;
            ResultSet tResult = selectFromDatabase(tSQL);
            int no = 0;
            while (tResult.next()) {
                no++;
                if (isAllowed(Akses_List.EDIT_DATA_PELANGGAN.get())) {
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId("" + no);
                    setButtonOnTableView(btnedit, 0);
                } else {
                    btnedit = null;
                }
                if (isAllowed(Akses_List.DELETE_DATA_PELANGGAN.get())) {
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId("" + no);
                    setButtonOnTableView(btndelete, 1);
                } else {
                    btndelete = null;
                }
                pelangganList.add(new PelangganModel(
                        "" + no,
                        tResult.getString(Pelanggan.IDPELANGGAN.get()),
                        tResult.getString(Pelanggan.NAMAPELANGGAN.get()),
                        tResult.getString(Pelanggan.ALMTPELANGGAN.get()),
                        tResult.getString(Pelanggan.TELP.get()),
                        tResult.getString(Pelanggan.EMAIL.get()),
                        tResult.getString(Pelanggan.PEKERJAAN.get()),
                        btnedit, btndelete));
                setValueColum();
                tbl_data.setItems(pelangganList);
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        
    }

    @Override
    public void setButtonListener() {

        btn_print.setOnAction((ActionEvent event) -> {
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak Data ?", EZIcon.ICON_APPS);
            if (opt.get() == ButtonType.OK) {
                if (tbl_data.getItems().size() > 0) {
                    showReport("report/master/pelanggan/lap_pelanggan.jrxml", mQuery);
                } else {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Data tidak ada!", EZIcon.ICON_APPS);
                }
            }
        });

        txt_cari.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (txt_cari.getText().length() >= 3) {
                    searchDataByParam(txt_cari.getText());
                } else {
                    loadData();
                }
            }
        });

        Platform.runLater(() -> {
            txt_cari.requestFocus();
        });

        btn_tambah.setOnAction((evt)->{
            MasterPelangganController.setmDataController(this);
            final String tUrl = "/app/retail/fxml/data/MasterPelanggan.fxml";
            loadForm(tUrl, "Master Pelanggan");
        });
    }

    @Override
    public void clear() {
        
    }

    public void delete(String param) {
        try {
            String tSQL = "DELETE FROM " + Pelanggan.TABLENAME.get() + " WHERE " + Pelanggan.IDPELANGGAN.get() + "='" + param + "'";
            updateToDatabase(tSQL);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            EZSystem.showAllert(alert, "Data berhasil dihapus", EZIcon.ICON_STAGE);
            initializeState();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    public void setButtonOnTableView(JFXButton button, int type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Edit data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    int index = Integer.parseInt(button.getId()) - 1;
                    PelangganModel tModel = tbl_data.getItems().get(index);
                    MasterPelangganController.setmModel(tModel);
                    btn_tambah.fire();
                }
            }

            if (type == 1) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Yakin hapus item ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    int index = Integer.parseInt(button.getId()) - 1;
                    PelangganModel tModel = tbl_data.getItems().get(index);
                    delete(tModel.getId_pelanggan());
                }
            }
        });
    }

    public void searchDataByParam(String param) {
        tbl_data.getItems().clear();
        try {
            String tSQL = "SELECT * FROM " + Pelanggan.TABLENAME.get() + " WHERE " + Pelanggan.IDPELANGGAN.get() + "!='0000'" + " AND "
                    + Pelanggan.NAMAPELANGGAN.get() + " like'%" + param + "%' ORDER BY "+Pelanggan.NAMAPELANGGAN.get()+" ASC";
            mQuery = tSQL;
            ResultSet tResult = selectFromDatabase(tSQL);
            int no = 0;
            while (tResult.next()) {
                no++;
                if (isAllowed("Edit Data Pelanggan", false)) {
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId("" + no);
                    setButtonOnTableView(btnedit, 0);
                } else {
                    btnedit = null;
                }
                if (isAllowed("Delete Data Pelanggan", false)) {
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId("" + no);
                    setButtonOnTableView(btndelete, 1);
                } else {
                    btndelete = null;
                }
                pelangganList.add(new PelangganModel(
                        "" + no,
                        tResult.getString(Pelanggan.IDPELANGGAN.get()),
                        tResult.getString(Pelanggan.NAMAPELANGGAN.get()),
                        tResult.getString(Pelanggan.ALMTPELANGGAN.get()),
                        tResult.getString(Pelanggan.TELP.get()),
                        tResult.getString(Pelanggan.EMAIL.get()),
                        tResult.getString(Pelanggan.PEKERJAAN.get()),
                        btnedit, btndelete));
                setValueColum();
                tbl_data.setItems(pelangganList);
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(DataController.getWidth() - 250);
        AnchorPane.setPrefHeight(DataController.getHeight() - 355);
        btn_print.setCursor(Cursor.HAND);
        btn_tambah.setCursor(Cursor.HAND);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_id_pelanggan, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama, Pos.CENTER_LEFT);
        setAligmentColoum(clm_alamat, Pos.CENTER_LEFT);
        setAligmentColoum(clm_email, Pos.CENTER_LEFT);
        setAligmentColoum(clm_telp, Pos.CENTER_LEFT);
        double lebar = getWidth()-250;
        clm_no.setPrefWidth((lebar * 3)/100);
        clm_id_pelanggan.setPrefWidth((lebar * 7)/100);
        clm_nama.setPrefWidth((lebar * 15)/100);
        clm_alamat.setPrefWidth((lebar * 25)/100);
        clm_telp.setPrefWidth((lebar * 10)/100);
        clm_email.setPrefWidth((lebar * 12)/100);
        clm_pekerjaan.setPrefWidth((lebar * 12)/100);
        clm_edit.setPrefWidth((lebar * 7)/100);
        clm_delete.setPrefWidth((lebar * 7)/100);
    }

    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_id_pelanggan.setCellValueFactory(new PropertyValueFactory<>(Pelanggan.IDPELANGGAN.get()));
        clm_nama.setCellValueFactory(new PropertyValueFactory<>(Pelanggan.NAMAPELANGGAN.get()));
        clm_alamat.setCellValueFactory(new PropertyValueFactory<>(Pelanggan.ALMTPELANGGAN.get()));
        clm_telp.setCellValueFactory(new PropertyValueFactory<>(Pelanggan.TELP.get()));
        clm_pekerjaan.setCellValueFactory(new PropertyValueFactory<>(Pelanggan.PEKERJAAN.get()));
        clm_email.setCellValueFactory(new PropertyValueFactory<>(Pelanggan.EMAIL.get()));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btnedit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
    }

    @Override
    public void initializeState() {
        loadData();
    }

    public JFXTextField getTxt_cari() {
        return txt_cari;
    }
}
