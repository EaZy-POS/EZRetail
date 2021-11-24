/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import app.retail.model.purchase.LoadModel;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.table.Sale_Hold;
import app.retail.utility.table.Sale_Hold_Detail;
import app.retail.utility.table.V_Sale_Hold;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormLoadPOSController extends AbstractPOS implements Initializable {
    
    private JFXButton btnload;
    private final ObservableList<LoadModel> loadList = FXCollections.observableArrayList();
    private static POSController mPOSController;
    
    @FXML
    private TableView<LoadModel> tbl_trans;
    @FXML
    private TableColumn<LoadModel, String> clm_no;
    @FXML
    private TableColumn<LoadModel, String> clm_tanggal;
    @FXML
    private TableColumn<LoadModel, String> clm_idtrans;
    @FXML
    private TableColumn<LoadModel, String> clm_nama_pelanggan;
    @FXML
    private TableColumn<LoadModel, String> clm_total;
    @FXML
    private TableColumn<LoadModel, JFXButton> clm_action;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private AnchorPane anchorePane;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeState();
    }    
    
    public void loadData() {
        try {
            String tSQL = "SELECT * FROM " + V_Sale_Hold.TABLENAME.get()
                    + " WHERE " + V_Sale_Hold.TRANDATE.get() + " BETWEEN '"
                    + EZDate.SQLDATE.today() + " 00:00:00' AND '"
                    + EZDate.SQLDATE.today() + " 23:59:59'";
            int no = 0;
            ResultSet tResult = selectFromDatabase(tSQL);
            while (tResult.next()) {
                no++;
                btnload = getButton(EZButtonType.BTN_LOAD, "Load");
                btnload.setId(tResult.getString(V_Sale_Hold.KODETRANS.get()));
                setButtonOnTableView(btnload, EZButtonType.BTN_LOAD);
                Date tDate = tResult.getDate(V_Sale_Hold.TRANDATE.get());
                double tTotal = tResult.getDouble(V_Sale_Hold.TOTAL.get());
                double tGrandTotal = tResult.getDouble(V_Sale_Hold.GRANDTOTAL.get());
                LoadModel tModel = new LoadModel(
                        "" + no,
                        tResult.getString(V_Sale_Hold.KODETRANS.get()),
                        EZDate.FORMAT_2.get(tDate),
                        tResult.getString(V_Sale_Hold.IDPELANGGAN.get()),
                        tResult.getString(V_Sale_Hold.PELANGGAN.get()),
                        formatRupiah(tTotal),
                        tResult.getString(V_Sale_Hold.DISC.get()),
                        tResult.getString(V_Sale_Hold.DISCAMOUNT.get()),
                        formatRupiah(tGrandTotal), btnload);
                loadList.add(tModel);
                setValueColum();
                tbl_trans.setItems(loadList);
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    private void close() {
        Stage stage = (Stage) btn_batal.getScene().getWindow();
        stage.close();
    }
    
    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            try {
                String tKodeTrans = button.getId();
                mPOSController.loadDataHold(tKodeTrans);
                delete(tKodeTrans);
                close();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });
    }
    
    private void delete(String pKodeTrans) throws SQLException {
        String tQuery = "DELETE FROM " + Sale_Hold_Detail.TABLENAME.get()
                + " WHERE " + Sale_Hold_Detail.KODETRANS.get()
                + " ='" + pKodeTrans + "'";
        updateToDatabase(tQuery);

        tQuery = "DELETE FROM " + Sale_Hold.TABLENAME.get() + " WHERE " + Sale_Hold.KODETRANS.get() + " ='" + pKodeTrans + "'";
        updateToDatabase(tQuery);
    }
    
    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_tanggal.setCellValueFactory(new PropertyValueFactory<>("tran_date"));
        clm_idtrans.setCellValueFactory(new PropertyValueFactory<>("kode_trans"));
        clm_nama_pelanggan.setCellValueFactory(new PropertyValueFactory<>("pelanggan"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("grand_total"));        
        clm_action.setCellValueFactory(new PropertyValueFactory<>("btnload"));        
    }
    
    public static POSController getmPOSController() {
        return mPOSController;
    }
    
    public static void setmPOSController(POSController mPOSController) {
        FormLoadPOSController.mPOSController = mPOSController;
    }
    
    @Override
    public void initializeState() {
        setButtonListener();
        setToolTip();
        loadData();
    }
    
    @Override
    public void setButtonListener() {
        
        Platform.runLater(()->{
            tbl_trans.requestFocus();
        });
        
        btn_batal.setOnAction((ActionEvent event) -> {
            close();
        });
        
        tbl_trans.setOnKeyPressed(((event) -> {
            if(event.getCode() == KeyCode.ENTER){
                LoadModel model = tbl_trans.getSelectionModel().getSelectedItem();
                try {
                    String tKodeTrans = model.getBtnload().getId();
                    mPOSController.loadDataHold(tKodeTrans);
                    delete(tKodeTrans);
                    close();
                } catch (SQLException ex) {
                    loggingerror(ex);
                }
            }
        }));
        
        tbl_trans.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                tbl_trans.getSelectionModel().select(0);
            }
        });
        
    }
    
    @Override
    public void setToolTip() {
        btn_batal.setCursor(Cursor.HAND);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_tanggal, Pos.CENTER_LEFT);
        setAligmentColoum(clm_idtrans, Pos.CENTER);
        setAligmentColoum(clm_nama_pelanggan, Pos.CENTER_LEFT);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
    }
    
    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
