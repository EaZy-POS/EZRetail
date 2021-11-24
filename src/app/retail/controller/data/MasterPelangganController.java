/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.data;

import static app.retail.controller.general.General.insertToDatabase;
import static app.retail.controller.general.General.updateToDatabase;
import app.retail.controller.home.HomeController;
import app.retail.model.data.PelangganModel;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Pelanggan;
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
public class MasterPelangganController extends AbstractData implements Initializable {

    private static boolean isEdit;
    private static DataPelangganController mDataController;
    private static PelangganModel mModel;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btn_close;
    @FXML
    private JFXTextField txt_kd_pelanggan;
    @FXML
    private JFXTextField txt_nama_pelanggan;
    @FXML
    private JFXTextArea txt_alamat;
    @FXML
    private JFXTextField txt_telp;
    @FXML
    private JFXTextField txt_pekerjaan;
    @FXML
    private JFXTextField txt_email;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_batal;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setButtonListener();
        setToolTip();
        initializeState();
        
        if(isEdit){
            txt_kd_pelanggan.setText(mModel.getId_pelanggan());
            txt_nama_pelanggan.setText(mModel.getNama_pelanggan());
            txt_alamat.setText(mModel.getAlmt_pelanggan());
            txt_email.setText(mModel.getEmail());
            txt_pekerjaan.setText(mModel.getPekerjaan());
            txt_telp.setText(mModel.getTelp());
        }
    }    

    @Override
    public void loadData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initializeState() {
        clear();
        txt_kd_pelanggan.setText(generateRefnum(Pelanggan.TABLENAME.get(), Pelanggan.IDPELANGGAN.get(), "CUST"));
        txt_kd_pelanggan.setEditable(false);
        txt_nama_pelanggan.requestFocus();
        
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

        btn_batal.setOnAction((ActionEvent event) -> {
            if(isEdit){
                btn_close.fire();
            }else{
                initializeState();
            }
        });
        
        txt_nama_pelanggan.setOnKeyPressed((KeyEvent event) -> {
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
                txt_pekerjaan.requestFocus();
            }
        });

        txt_pekerjaan.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                txt_email.requestFocus();
            }
        });
        
        btn_close.setOnAction((ActionEvent event) -> {
            isEdit = false;
            ((Node) (event.getSource())).getScene().getWindow().hide();
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
        
        Platform.runLater(()->{
            txt_nama_pelanggan.requestFocus();
        });
    }
    
    private boolean validateForm() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (txt_nama_pelanggan.getText().equals("")) {
            EZSystem.showAllert(alert, "Mohon isi nama pelanggan", EZIcon.ICON_STAGE);
            txt_nama_pelanggan.requestFocus();
            return false;
        }

        if (txt_alamat.getText().equals("")) {
            EZSystem.showAllert(alert, "Mohon isi alamat pelanggan", EZIcon.ICON_STAGE);
            txt_alamat.requestFocus();
            return false;
        }
        return !txt_nama_pelanggan.getText().equals("");
    }

    public void simpan() {
        try {
            RecordEntry tMapInsert = new RecordEntry();
            tMapInsert.createEntry(Pelanggan.IDPELANGGAN.get(), txt_kd_pelanggan.getText());
            tMapInsert.createEntry(Pelanggan.NAMAPELANGGAN.get(), txt_nama_pelanggan.getText());
            tMapInsert.createEntry(Pelanggan.ALMTPELANGGAN.get(), txt_alamat.getText());
            tMapInsert.createEntry(Pelanggan.TELP.get(), txt_telp.getText());
            tMapInsert.createEntry(Pelanggan.PEKERJAAN.get(), txt_pekerjaan.getText());
            tMapInsert.createEntry(Pelanggan.EMAIL.get(), txt_email.getText());
            tMapInsert.createEntry(Pelanggan.SID.get(), HomeController.getSession());
            if (!isEdit) {
                tMapInsert.createEntry(Pelanggan.CDT.get(), EZDate.SQLDATE.today());
                insertToDatabase(tMapInsert, Pelanggan.TABLENAME.get());
            } else {
                tMapInsert.createEntry(Pelanggan.UDT.get(), EZDate.SQLDATE.today());
                updateToDatabase(tMapInsert, Pelanggan.TABLENAME.get(), Pelanggan.IDPELANGGAN.get() + "='" + txt_kd_pelanggan.getText() + "'");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
            mDataController.loadData();
            if(isEdit){
                btn_close.fire();
                mDataController.getTxt_cari().requestFocus();
            }else{
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
        txt_kd_pelanggan.setText("");
        txt_pekerjaan.setText("");
        txt_nama_pelanggan.setText("");
        txt_telp.setText("");
    }

    @Override
    public void setToolTip() {
        btn_simpan.setCursor(Cursor.HAND);
        btn_batal.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
        setLimitTexfield(txt_nama_pelanggan, 60);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        
    }

    public static void setmDataController(DataPelangganController mDataController) {
        MasterPelangganController.mDataController = mDataController;
    }

    public static void setmModel(PelangganModel mModel) {
        isEdit = true;
        MasterPelangganController.mModel = mModel;
    }
    
}
