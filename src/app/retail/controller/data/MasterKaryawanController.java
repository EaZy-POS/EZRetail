/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.data;

import static app.retail.controller.general.General.insertToDatabase;
import static app.retail.controller.general.General.updateToDatabase;
import app.retail.controller.home.HomeController;
import app.retail.model.data.KaryawanModel;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Karyawan;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class MasterKaryawanController extends AbstractData implements Initializable {

    private static boolean isEdit;
    private static DataKaryawanController mDataController;
    private static KaryawanModel mModel;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btn_close;
    @FXML
    private JFXTextField txt_kode_karyawan;
    @FXML
    private JFXTextField txt_id_karyawan;
    @FXML
    private JFXTextField txt_nama_karyawan;
    @FXML
    private JFXTextArea txt_alamat;
    @FXML
    private JFXTextField txt_telp;
    @FXML
    private JFXTextField txt_jabatan;
    @FXML
    private JFXTextField txt_email;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_btl;

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
        initializeState();
    }

    @Override
    public void loadData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initializeState() {
        clear();
        txt_kode_karyawan.setText(generateRefnum(Karyawan.TABLENAME.get(), Karyawan.KODEKARYAWAN.get(), "EMP"));
        txt_kode_karyawan.setEditable(false);
        if (isEdit) {
            KaryawanModel tModel = mModel;
            txt_kode_karyawan.setText(tModel.getKode_karyawan());
            txt_id_karyawan.setText(tModel.getId_karyawan());
            txt_nama_karyawan.setText(tModel.getNama_karyawan());
            txt_alamat.setText(tModel.getAlmt_karyawan());
            txt_email.setText(tModel.getEmail());
            txt_jabatan.setText(tModel.getJab());
            txt_telp.setText(tModel.getTelp());
        }
        txt_id_karyawan.requestFocus();
    }

    @Override
    public void setButtonListener() {
        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> option = EZSystem.showAllert(alert, "Simpan data ?", EZIcon.ICON_STAGE);
            if (option.get() == ButtonType.OK) {
                if (validateForm()) {
                    simpan();
                }
            }
        });

        btn_close.setOnAction((ActionEvent event) -> {
            isEdit = false;
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });

        btn_btl.setOnAction((ActionEvent event) -> {
            if(isEdit){
                btn_close.fire();
            }else{
                initializeState();
            }
        });

        txt_id_karyawan.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                txt_nama_karyawan.requestFocus();
            }
        });

        txt_nama_karyawan.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                txt_alamat.requestFocus();
            }
        });

        txt_alamat.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                txt_telp.requestFocus();
            }
        });

        txt_telp.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                txt_jabatan.requestFocus();
            }
        });

        txt_jabatan.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                txt_email.requestFocus();
            }
        });

        txt_email.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Simpan data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    if (validateForm()) {
                        simpan();
                    }
                }
            }
        });

        Platform.runLater(() -> {
            txt_id_karyawan.requestFocus();
        });
    }

    private boolean validateForm() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (txt_id_karyawan.getText().equals("")) {
            EZSystem.showAllert(alert, "Mohon isi id karyawan", EZIcon.ICON_STAGE);
            txt_id_karyawan.requestFocus();
            return false;
        }

        if (txt_nama_karyawan.getText().equals("")) {
            EZSystem.showAllert(alert, "Mohon isi nama karyawan", EZIcon.ICON_STAGE);
            txt_nama_karyawan.requestFocus();
            return false;
        }

        if (txt_jabatan.getText().equals("")) {
            EZSystem.showAllert(alert, "Mohon isi jabatan karyawan", EZIcon.ICON_STAGE);
            txt_jabatan.requestFocus();
            return false;
        }
        return !txt_nama_karyawan.getText().equals("");
    }

    public void simpan() {
        try {
            RecordEntry tMapInsert = new RecordEntry();
            tMapInsert.createEntry(Karyawan.KODEKARYAWAN.get(), txt_kode_karyawan.getText());
            tMapInsert.createEntry(Karyawan.IDKARYAWAN.get(), txt_id_karyawan.getText());
            tMapInsert.createEntry(Karyawan.NAMAKARYAWAN.get(), txt_nama_karyawan.getText());
            tMapInsert.createEntry(Karyawan.ALMTKARYAWAN.get(), txt_alamat.getText());
            tMapInsert.createEntry(Karyawan.TELP.get(), txt_telp.getText());
            tMapInsert.createEntry(Karyawan.JAB.get(), txt_jabatan.getText());
            tMapInsert.createEntry(Karyawan.EMAIL.get(), txt_email.getText());
            tMapInsert.createEntry(Karyawan.SID.get(), HomeController.getSession());
            if (!isEdit) {
                tMapInsert.createEntry(Karyawan.CDT.get(), EZDate.SQLDATE.today());
                insertToDatabase(tMapInsert, Karyawan.TABLENAME.get());
            } else {
                tMapInsert.createEntry(Karyawan.UDT.get(), EZDate.SQLDATE.today());
                updateToDatabase(tMapInsert, Karyawan.TABLENAME.get(), Karyawan.KODEKARYAWAN.get() + "='" + txt_kode_karyawan.getText() + "'");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
            mDataController.loadData();
            if (isEdit) {
                btn_close.fire();
            } else {
                initializeState();
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void clear() {
        txt_alamat.setText("");
        txt_email.setText("");
        txt_id_karyawan.setText("");
        txt_jabatan.setText("");
        txt_kode_karyawan.setText("");
        txt_nama_karyawan.setText("");
        txt_telp.setText("");        
    }

    @Override
    public void setToolTip() {
        btn_simpan.setCursor(Cursor.HAND);
        btn_btl.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void setmDataController(DataKaryawanController mDataController) {
        MasterKaryawanController.mDataController = mDataController;
    }

    public static void setmModel(KaryawanModel mModel) {
        isEdit = true;
        MasterKaryawanController.mModel = mModel;
    }

}
