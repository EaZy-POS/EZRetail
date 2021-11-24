/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.data;

import static app.retail.controller.general.General.getStackTraceString;
import app.retail.model.data.ShiftModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Shift;
import app.retail.utility.table.V_Shift;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class DataShiftController extends AbstractData implements Initializable {

    private final ObservableList<ShiftModel> shiftList = FXCollections.observableArrayList();
    private JFXButton btnedit,btndelete;
    private static boolean isEdit;
    
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_btl;
    @FXML
    private TableView<ShiftModel> tbl_data;
    @FXML
    private JFXTextField txt_shift;
    @FXML
    private JFXTextField txt_deskrip;
    @FXML
    private JFXTimePicker jam_awal;
    @FXML
    private JFXTimePicker jam_akhir;
    @FXML
    private TableColumn<ShiftModel, String> clm_no;
    @FXML
    private TableColumn<ShiftModel, String> clm_shift;
    @FXML
    private TableColumn<ShiftModel, String> clm_awal;
    @FXML
    private TableColumn<ShiftModel, String> clm_akhir;
    @FXML
    private TableColumn<ShiftModel, JFXButton> clm_edit;
    @FXML
    private TableColumn<ShiftModel, JFXButton> clm_delete;
    @FXML
    private AnchorPane AnchorPane1;
    @FXML
    private Button btn_close;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(!isAllowed("Input Data Shift", false)){
            btn_simpan.setDisable(true);
        }
        setToolTip();
        setButtonListener();
        initializeState();
    }    

    @Override
    public void loadData() {
        tbl_data.getItems().clear();
        try {
            String tSQL = "SELECT "
                    + V_Shift.KODE.get()+", "
                    + V_Shift.SHIFT.get()+", "
                    + "date_format("+V_Shift.AWAL.get()+",'%H:%i') as "+V_Shift.AWAL.get()+", "
                    + "date_format("+V_Shift.AKHIR.get()+",'%H:%i') as "+V_Shift.AKHIR.get()
                    +" FROM "+V_Shift.TABLENAME.get();
            ResultSet tResult = selectFromDatabase(tSQL);
            int no = 0;
            while (tResult.next()) {
                no++;
                if(isAllowed(Akses_List.EDIT_DATA_SHIFT.get())){
                    btnedit = getButton(EZButtonType.BTN_EDIT, "Edit");
                    btnedit.setId(""+no);
                    setButtonOnTableView(btnedit, 0);
                }else{
                    btnedit=null;
                }
                
                if(isAllowed(Akses_List.DELETE_DATA_SHIFT.get())){
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId(tResult.getString(V_Shift.KODE.get()));
                    setButtonOnTableView(btndelete, 1);
                }
                
                shiftList.add(new ShiftModel(""+no, 
                    tResult.getString(V_Shift.KODE.get()), 
                    tResult.getString(V_Shift.SHIFT.get()), 
                    tResult.getString(V_Shift.AWAL.get()), 
                    tResult.getString(V_Shift.AKHIR.get()), 
                    btnedit, 
                    btndelete)
                );
                setValueColum();
                tbl_data.setItems(shiftList);
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setButtonListener() {
        setTime(jam_awal);
        setTime(jam_akhir);
        
        btn_simpan.setOnAction((ActionEvent event) -> {
            if (txt_deskrip.getText().length()>0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Simpan data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                   simpan();
                }
            }else{
                EZSystem.showAllert(EZAlertType.WARNING, "Mohon nama shift!", EZIcon.ICON_STAGE);
                txt_deskrip.requestFocus();
            }
        });
        
        Platform.runLater(()->{
            txt_deskrip.requestFocus();
        });
        
        btn_btl.setOnAction((ActionEvent event) -> {
            initializeState();
            clear();
        });
        
        btn_close.setOnAction((ActionEvent event) -> {
            isEdit = false;
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });
    }

    public void simpan() {
        try {
            if (validateTime()) {
                EZSystem.showAllert(EZAlertType.WARNING, "Jam yang anda pilih sudah digunakan!", EZIcon.ICON_STAGE);
            } else {
                RecordEntry tMapInsert = new RecordEntry();
                tMapInsert.createEntry(Shift.KODESHIFT.get(), txt_shift.getText());
                tMapInsert.createEntry(Shift.SHIFT.get(), txt_deskrip.getText());
                tMapInsert.createEntry(Shift.JAMAWAL.get(), getTime(jam_awal));
                tMapInsert.createEntry(Shift.JAMAKHIR.get(), getTime(jam_akhir));
                tMapInsert.createEntry(Shift.SID.get(), getSession());
                if (!isEdit) {
                    tMapInsert.createEntry(Shift.CDT.get(), EZDate.SQLDATE.today());
                    insertToDatabase(tMapInsert, Shift.TABLENAME.get());
                } else {
                    tMapInsert.createEntry(Shift.UDT.get(), EZDate.SQLDATE.today());
                    String id = txt_shift.getText();
                    updateToDatabase(tMapInsert, Shift.TABLENAME.get(), Shift.KODESHIFT.get() + "='" + id + "'");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
                initializeState();
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            EZSystem.showAllert(alert, "Error when insert data \n"+ex.getMessage(), EZIcon.ICON_STAGE);
            loggingerror(ex);
        }
    }

    @Override
    public void clear() {
        txt_deskrip.setText("");
    }

    public void delete(String param) {
        try {
            String tSQL = "DELETE FROM "+Shift.TABLENAME.get()+" WHERE "+ Shift.KODESHIFT.get() +"='"+param+"'";
            updateToDatabase(tSQL);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            EZSystem.showAllert(alert, "Data berhasil dihapus", EZIcon.ICON_STAGE);
            initializeState();
        } catch (SQLException ex) {
            String tMsg = ex.getMessage();
            if(tMsg.contains("Cannot delete or update a parent row")){
                EZSystem.showAllert(EZAlertType.ERROR, "Data tidak bisa dihapus karena sudah digunakan untuk transaksi!", EZIcon.ICON_STAGE);
            }else{
                EZSystem.showAllert(EZAlertType.ERROR, "ERROR: "+tMsg, EZIcon.ICON_STAGE);
            }
            loggingerror(ex);
        }
    }
    
    @Override
    public void loggingerror(Exception ex){
        String tMessage = "ERROR: " + ex.getMessage();
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
        initializeState();
    }

    public void setButtonOnTableView(JFXButton button, int type) {
        button.setOnAction((ActionEvent event) -> {
            if (type == 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Edit data ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    isEdit = true;
                    int index = Integer.parseInt(button.getId())-1;
                    ShiftModel tModel = tbl_data.getItems().get(index);
                    txt_shift.setText(tModel.getKode_shift());
                    txt_deskrip.setText(tModel.getShift());
                    jam_awal.setValue(LocalTime.parse(tModel.getJam_awal()));
                    jam_akhir.setValue(LocalTime.parse(tModel.getJam_akhir()));
                }
            }
            
            if (type == 1) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Yakin hapus item ?",EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    delete(button.getId());
                }
            }
        });
    }

    @Override
    public void setToolTip() {
        txt_shift.setEditable(false);
        btn_simpan.setCursor(Cursor.HAND);
        btn_btl.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
    }
    
    private void setTime(JFXTimePicker pTimePicker){
        pTimePicker.setConverter(new StringConverter<LocalTime>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(EZDate.SHORTTIME.get());
            @Override
            public String toString(LocalTime object) {
                if (object != null) {
                        return dateFormatter.format(object);
                    } else {
                        return "";
                    }
            }

            @Override
            public LocalTime fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalTime.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        pTimePicker.setValue(LocalTime.now());
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_shift.setCellValueFactory(new PropertyValueFactory<>(Shift.SHIFT.get()));
        clm_awal.setCellValueFactory(new PropertyValueFactory<>(Shift.JAMAWAL.get()));
        clm_akhir.setCellValueFactory(new PropertyValueFactory<>(Shift.JAMAKHIR.get()));        
        clm_edit.setCellValueFactory(new PropertyValueFactory<>("btnedit"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
    }
    
    public String getTime(JFXTimePicker pTimePicker){
        LocalTime localTime = pTimePicker.getValue();
        return localTime.toString();
    }
    
    public boolean isTomorow(){
        LocalTime awal = jam_awal.getValue();
        LocalTime ahir = jam_akhir.getValue();
        
        return awal.isAfter(ahir);
    }
    
    private boolean validateTime() throws SQLException{
        String id = txt_shift.getText();
        String tSQL = "SELECT * FROM "+V_Shift.TABLENAME.get()+" WHERE '"+EZDate.SQLDATE.today()+" "+getTime(jam_awal)+":00' BETWEEN AWAL AND AKHIR";
        
        if(isTomorow()){
            tSQL = "SELECT * FROM "+V_Shift.TABLENAME.get()+" WHERE '"+EZDate.SQLDATE.tomorow()+" "+getTime(jam_awal)+":00' BETWEEN AWAL AND AKHIR";
        }
        
        if(isEdit){
            tSQL += " AND "+V_Shift.KODE.get()+" !='"+id+"'";
        }
        ResultSet res = selectFromDatabase(tSQL);
        if(res.next()){
            return true;
        }else{
            tSQL = "SELECT * FROM " + V_Shift.TABLENAME.get() + " WHERE '" + EZDate.SQLDATE.today() + " " + getTime(jam_akhir) + ":00' BETWEEN AWAL AND AKHIR";

            if (isTomorow()) {
                tSQL = "SELECT * FROM " + V_Shift.TABLENAME.get() + " WHERE '" + EZDate.SQLDATE.tomorow() + " " + getTime(jam_akhir) + ":00' BETWEEN AWAL AND AKHIR";
            }
            
            if (isEdit) {
                tSQL += " AND " + V_Shift.KODE.get() + " !='" + id + "'";
            }
            
            res = selectFromDatabase(tSQL);
            return res.next();
        }
    }

    @Override
    public void initializeState() {
        txt_shift.setText(generateRefnum(V_Shift.TABLENAME.get(), V_Shift.KODE.get(), "SH"));
        loadData();
        isEdit = false;
        txt_deskrip.setText("");
        txt_deskrip.requestFocus();
    }
    
}