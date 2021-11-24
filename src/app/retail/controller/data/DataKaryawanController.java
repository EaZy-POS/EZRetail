/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.data;

import static app.retail.controller.general.General.isAllowed;
import app.retail.model.data.KaryawanModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Karyawan;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DataKaryawanController extends AbstractData implements Initializable {

    private final ObservableList<KaryawanModel> karyawwanList = FXCollections.observableArrayList();
    private JFXButton btnedit, btndelete;
    private static String mQuery;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<KaryawanModel> tbl_data;
    @FXML
    private Button btn_print;
    @FXML
    private JFXTextField txt_search;
    @FXML
    private TableColumn<KaryawanModel, String> clm_no;
    @FXML
    private TableColumn<KaryawanModel, String> clm_id;
    @FXML
    private TableColumn<KaryawanModel, String> clm_nama;
    @FXML
    private TableColumn<KaryawanModel, String> clm_alamat;
    @FXML
    private TableColumn<KaryawanModel, String> clm_telp;
    @FXML
    private TableColumn<KaryawanModel, String> clm_email;
    @FXML
    private TableColumn<KaryawanModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<KaryawanModel, JFXButton> clm_delete;
    @FXML
    private TableColumn<KaryawanModel, String> clm_jab;
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
        if (!isAllowed(Akses_List.INPUT_DATA_KARYAWAN.get(), false)) {
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
            String tSQL = "SELECT * FROM " + Karyawan.TABLENAME.get() + " ORDER BY " + Karyawan.NAMAKARYAWAN.get() + " ASC";
            mQuery = tSQL;
            ResultSet tResult = selectFromDatabase(tSQL);
            int no = 0;
            while (tResult.next()) {
                no++;
                if (isAllowed(Akses_List.EDIT_DATA_KARYAWAN.get())) {
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId("" + no);
                    setButtonOnTableView(btnedit, 0);
                } else {
                    btnedit = null;
                }
                if (isAllowed(Akses_List.DELETE_DATA_KARYAWAN.get())) {
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId("" + no);
                    setButtonOnTableView(btndelete, 1);
                } else {
                    btndelete = null;
                }
                karyawwanList.add(new KaryawanModel(
                        "" + no,
                        tResult.getString(Karyawan.KODEKARYAWAN.get()),
                        tResult.getString(Karyawan.IDKARYAWAN.get()),
                        tResult.getString(Karyawan.NAMAKARYAWAN.get()),
                        tResult.getString(Karyawan.ALMTKARYAWAN.get()),
                        tResult.getString(Karyawan.TELP.get()),
                        tResult.getString(Karyawan.JAB.get()),
                        tResult.getString(Karyawan.EMAIL.get()),
                        btnedit, btndelete));
                setValueColum();
                tbl_data.setItems(karyawwanList);
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {

        txt_search.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (txt_search.getText().length() >= 3) {
                    searchDataByParam(txt_search.getText());
                } else {
                    loadData();
                }
            }
        });

        Platform.runLater(() -> {
            txt_search.requestFocus();
        });

        btn_print.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.PRINT_DATA_KARYAWAN.get())) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Cetak data ?", EZIcon.ICON_APPS);
                if (option.get() == ButtonType.OK) {
                    if (tbl_data.getItems().size() > 0) {
                        showReport("report/master/karyawan/lap_karyawan.jrxml", mQuery);
                    } else {
                        EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Data tidak ada!", EZIcon.ICON_APPS);
                    }
                }
            }
        });

        btn_tambah.setOnAction((evt) -> {
            MasterKaryawanController.setmDataController(this);
            final String tUrl = "/app/retail/fxml/data/MasterKaryawan.fxml";
            loadForm(tUrl, "Master Karyawan");

        });

    }

    @Override
    public void clear() {
        txt_search.setText("");
    }

    public void delete(String param) {
        try {
            String tSQL = "DELETE FROM " + Karyawan.TABLENAME.get() + " WHERE " + Karyawan.KODEKARYAWAN.get() + "='" + param + "'";
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
                    KaryawanModel tModel = tbl_data.getItems().get(index);
                    MasterKaryawanController.setmModel(tModel);
                    btn_tambah.fire();
                }
            }

            if (type == 1) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Yakin hapus item ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    int index = Integer.parseInt(button.getId()) - 1;
                    KaryawanModel tModel = tbl_data.getItems().get(index);
                    delete(tModel.getKode_karyawan());
                }
            }
        });
    }

    public void searchDataByParam(String param) {
        tbl_data.getItems().clear();
        try {
            String tSQL = "SELECT * FROM " + Karyawan.TABLENAME.get() + " WHERE "
                    + Karyawan.NAMAKARYAWAN.get() + " like'%" + param + "%' OREDER BY " + Karyawan.NAMAKARYAWAN.get() + " ASC";
            mQuery = tSQL;
            ResultSet tResult = selectFromDatabase(tSQL);
            int no = 0;
            while (tResult.next()) {
                no++;
                if (isAllowed("Edit Data Karyawan", false)) {
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId("" + no);
                    setButtonOnTableView(btnedit, 0);
                } else {
                    btnedit = null;
                }

                if (isAllowed("Delete Data Karyawan", false)) {
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId("" + no);
                    setButtonOnTableView(btndelete, 1);
                } else {
                    btndelete = null;
                }

                karyawwanList.add(new KaryawanModel(
                        "" + no,
                        tResult.getString(Karyawan.KODEKARYAWAN.get()),
                        tResult.getString(Karyawan.IDKARYAWAN.get()),
                        tResult.getString(Karyawan.NAMAKARYAWAN.get()),
                        tResult.getString(Karyawan.ALMTKARYAWAN.get()),
                        tResult.getString(Karyawan.TELP.get()),
                        tResult.getString(Karyawan.JAB.get()),
                        tResult.getString(Karyawan.EMAIL.get()),
                        btnedit, btndelete));
                setValueColum();
                tbl_data.setItems(karyawwanList);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataKaryawanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 250);
        AnchorPane.setPrefHeight(getHeight() - 355);
        btn_tambah.setCursor(Cursor.HAND);
        btn_print.setCursor(Cursor.HAND);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_id, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama, Pos.CENTER_LEFT);
        setAligmentColoum(clm_alamat, Pos.CENTER_LEFT);
        setAligmentColoum(clm_email, Pos.CENTER_LEFT);
        setAligmentColoum(clm_telp, Pos.CENTER_LEFT);
        double lebar = getWidth() - 250;
        clm_no.setPrefWidth((lebar * 3) / 100);
        clm_id.setPrefWidth((lebar * 9) / 100);
        clm_nama.setPrefWidth((lebar * 15) / 100);
        clm_alamat.setPrefWidth((lebar * 25) / 100);
        clm_telp.setPrefWidth((lebar * 10) / 100);
        clm_email.setPrefWidth((lebar * 12) / 100);
        clm_jab.setPrefWidth((lebar * 12) / 100);
        clm_edit.setPrefWidth((lebar * 7) / 100);
        clm_delete.setPrefWidth((lebar * 7) / 100);
    }

    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_id.setCellValueFactory(new PropertyValueFactory<>(Karyawan.IDKARYAWAN.get()));
        clm_nama.setCellValueFactory(new PropertyValueFactory<>(Karyawan.NAMAKARYAWAN.get()));
        clm_alamat.setCellValueFactory(new PropertyValueFactory<>(Karyawan.ALMTKARYAWAN.get()));
        clm_telp.setCellValueFactory(new PropertyValueFactory<>(Karyawan.TELP.get()));
        clm_email.setCellValueFactory(new PropertyValueFactory<>(Karyawan.EMAIL.get()));
        clm_jab.setCellValueFactory(new PropertyValueFactory<>(Karyawan.JAB.get()));
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btnedit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
    }

    @Override
    public void initializeState() {
        clear();
        loadData();
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
