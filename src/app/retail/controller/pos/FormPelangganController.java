/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import app.retail.model.pos.FormPelangganModel;
import app.retail.utility.EZButtonType;
import app.retail.utility.table.Pelanggan;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormPelangganController extends AbstractPOS implements Initializable {

    private JFXButton btnpilih;
    private final ObservableList<FormPelangganModel> pelangganList = FXCollections.observableArrayList();
    private static POSController mPOSController;
    
    @FXML
    private AnchorPane anchorePane;
    @FXML
    private TableView<FormPelangganModel> tbl_trans;
    @FXML
    private TableColumn<FormPelangganModel,String> clm_no;
    @FXML
    private TableColumn<FormPelangganModel,String> clm_id_pelanggan;
    @FXML
    private TableColumn<FormPelangganModel,String> clm_nama_pelanggan;
    @FXML
    private TableColumn<FormPelangganModel,String> clm_alamat;
    @FXML
    private TableColumn<FormPelangganModel,String> clm_telp;
    @FXML
    private TableColumn<FormPelangganModel,JFXButton> clm_action;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private TextField txt_kode_pelanggan;
    @FXML
    private TextField txt_nama_pelanggan;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            initializeState();
            loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    public void loadData() throws SQLException {
        tbl_trans.getItems().clear();
        String tSQL = "SELECT * FROM " + Pelanggan.TABLENAME.get()
                + " WHERE " + Pelanggan.IDPELANGGAN.get() + " !='0000' "
                + "ORDER BY " + Pelanggan.NAMAPELANGGAN.get() + " ASC LIMIT 100";
        int no = 0;
        ResultSet tResult = selectFromDatabase(tSQL);
        while (tResult.next()) {
            no++;
            btnpilih = getButton(EZButtonType.BTN_CHECK, "Pilih");
            btnpilih.setId("" + no);
            setButtonOnTableView(btnpilih, EZButtonType.BTN_EDIT);
            FormPelangganModel tModel = new FormPelangganModel(
                    "" + no,
                    tResult.getString(Pelanggan.IDPELANGGAN.get()),
                    tResult.getString(Pelanggan.NAMAPELANGGAN.get()),
                    tResult.getString(Pelanggan.ALMTPELANGGAN.get()),
                    tResult.getString(Pelanggan.TELP.get()),
                    btnpilih
            );
            pelangganList.add(tModel);
            setValueColum();
            tbl_trans.setItems(pelangganList);
        }
    }

    private void searchBy(String tSQL) throws SQLException {
        tbl_trans.getItems().clear();
        int no = 0;
        ResultSet tResult = selectFromDatabase(tSQL);
        while (tResult.next()) {
            no++;
            btnpilih = getButton(EZButtonType.BTN_CHECK, "Pilih");
            btnpilih.setId("" + no);
            setButtonOnTableView(btnpilih, EZButtonType.BTN_EDIT);
            FormPelangganModel tModel = new FormPelangganModel(
                    "" + no,
                    tResult.getString(Pelanggan.IDPELANGGAN.get()),
                    tResult.getString(Pelanggan.NAMAPELANGGAN.get()),
                    tResult.getString(Pelanggan.ALMTPELANGGAN.get()),
                    tResult.getString(Pelanggan.TELP.get()),
                    btnpilih
            );
            pelangganList.add(tModel);
            setValueColum();
            tbl_trans.setItems(pelangganList);
        }
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            int index = Integer.parseInt(button.getId())-1;
            loadDataToPOS(index);
        });
    }
    
    private void loadDataToPOS(int index){
        FormPelangganModel tModel = tbl_trans.getItems().get(index);
        String id = tModel.getId_pelanggan();
        String nama = tModel.getNama_pelanggan();
        mPOSController.setPelanggan(id, nama, "0");
        clear();
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_id_pelanggan.setCellValueFactory(new PropertyValueFactory<>("id_pelanggan"));
        clm_alamat.setCellValueFactory(new PropertyValueFactory<>("almt"));
        clm_nama_pelanggan.setCellValueFactory(new PropertyValueFactory<>("nama_pelanggan"));
        clm_telp.setCellValueFactory(new PropertyValueFactory<>("telp"));        
        clm_action.setCellValueFactory(new PropertyValueFactory<>("btnpilih"));        
    }

    public static POSController getmPOSController() {
        return mPOSController;
    }

    public static void setmPOSController(POSController mPOSController) {
        FormPelangganController.mPOSController = mPOSController;
    }

    @Override
    public void initializeState() {
        setToolTip();
        setButtonListener();
    }

    @Override
    public void setButtonListener() {
        btn_batal.setOnAction((ActionEvent event) -> {
            clear();
        });
        
        txt_nama_pelanggan.setOnAction((ActionEvent event) -> {
            if (txt_nama_pelanggan.getText().length()>3){
                try {
                    String tSQL = "SELECT * FROM " + Pelanggan.TABLENAME.get()
                            + " WHERE " + Pelanggan.IDPELANGGAN.get() + " !='0000' "
                            + " AND "+Pelanggan.NAMAPELANGGAN.get()+" LIKE '"+txt_nama_pelanggan.getText()+"%' "
                            + "ORDER BY " + Pelanggan.NAMAPELANGGAN.get() + " ASC LIMIT 100";
                    searchBy(tSQL);
                } catch (SQLException ex) {
                    loggingerror(ex);
                }
            }
        });
        
        txt_kode_pelanggan.setOnAction((ActionEvent event) -> {
            try {
                String tSQL = "SELECT * FROM " + Pelanggan.TABLENAME.get()
                            + " WHERE " + Pelanggan.IDPELANGGAN.get() + " !='0000' "
                            + " AND "+Pelanggan.IDPELANGGAN.get()+" = '"+txt_kode_pelanggan.getText()+"' "
                            + "ORDER BY " + Pelanggan.NAMAPELANGGAN.get() + " ASC LIMIT 100";
                searchBy(tSQL);
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });
        
        txt_nama_pelanggan.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode().equals(KeyCode.ENTER)||event.getCode().equals(KeyCode.DOWN)){
                tbl_trans.requestFocus();
            }
        });
        
        tbl_trans.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                if(tbl_trans.getItems().size()>0){
                    tbl_trans.getSelectionModel().select(0);
                }
            }
        });
        
        tbl_trans.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                int index = tbl_trans.getSelectionModel().getSelectedIndex();
                loadDataToPOS(index);
            }
        });
        
        tbl_trans.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount()==2) {
                int index = tbl_trans.getSelectionModel().getSelectedIndex();
                loadDataToPOS(index);
            }
 
        });
        
    }

    @Override
    public void setToolTip() {
        btn_batal.setCursor(Cursor.HAND);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_id_pelanggan, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_pelanggan, Pos.CENTER_LEFT);
        setAligmentColoum(clm_alamat, Pos.CENTER_LEFT);
        setAligmentColoum(clm_telp, Pos.CENTER_RIGHT);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        Stage stage = (Stage) btn_batal.getScene().getWindow();
        stage.close();
    }
    
}
