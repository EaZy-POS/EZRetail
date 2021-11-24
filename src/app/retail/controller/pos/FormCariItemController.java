/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import static app.retail.controller.general.General.formatRupiah;
import static app.retail.controller.general.General.selectFromDatabase;
import app.retail.model.pos.POSModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_All_Item;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class FormCariItemController extends AbstractPOS implements Initializable {

    private final ObservableList<POSModel> itemList = FXCollections.observableArrayList();
    private static POSController mPOSController;
    private static POSModel modelItem;
    
    @FXML
    private AnchorPane anchorePane;
    @FXML
    private TextField txt_nama_item;
    @FXML
    private TextField txt_qty;
    @FXML
    private JFXButton btn_ok;
    @FXML
    private TableView<POSModel> tbl_trans;
    @FXML
    private TableColumn<POSModel, JFXButton> clm_action;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private TableColumn<POSModel, String> clm_sku;
    @FXML
    private TableColumn<POSModel, String> clm_barcode;
    @FXML
    private TableColumn<POSModel, String> clm_nama_item;
    @FXML
    private TableColumn<POSModel, String> clm_sat;
    @FXML
    private TableColumn<POSModel, String> clm_harga;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeState();
        setToolTip();
        setButtonListener();
    }    

    @Override
    public void initializeState() {
        txt_qty.setText("0");
        txt_nama_item.setText("");
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            txt_nama_item.requestFocus();
        });
        
        txt_nama_item.setOnAction((event) -> {
            if(txt_nama_item.getText().length()>0){
               loadData();
            }
        });
        
        tbl_trans.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                if(tbl_trans.getItems().size()>0){
                    tbl_trans.getSelectionModel().select(0);
                }else{
                    txt_nama_item.requestFocus();
                }
            }
        });
        
        btn_batal.setOnAction((event) -> {
           clear();
        });
        
        tbl_trans.setOnKeyPressed(((event) -> {
            if(event.getCode() == KeyCode.ENTER){
                setDataItem(tbl_trans.getSelectionModel().getSelectedItem());
            }
        }));
        
        tbl_trans.setOnMouseClicked(((event) -> {
            if(event.getClickCount() == 2){
                setDataItem(tbl_trans.getSelectionModel().getSelectedItem());
            }
        }));
        
        btn_ok.setOnAction((event) -> {
            if(txt_qty.getText().length() == 0 || txt_qty.getText().equals("0")){
                EZSystem.showAllert(EZAlertType.WARNING, "Mohon isi qty dengan benar!", EZIcon.ICON_APPS);
            }else{
                modelItem.setQTY(txt_qty.getText());
                mPOSController.addItem(modelItem);
                clear();
            }
        });
        
        txt_qty.setOnAction((event) -> {
           btn_ok.fire();
        });
    }

    @Override
    public void clear() {
        Stage stage = (Stage) btn_batal.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setToolTip() {
        setNumericTextfield(txt_qty);
        setAligmentColoum(clm_sku, Pos.CENTER_LEFT);
        setAligmentColoum(clm_barcode, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_harga, Pos.CENTER_RIGHT);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static POSController getmPOSController() {
        return mPOSController;
    }

    public static void setmPOSController(POSController mPOSController) {
        FormCariItemController.mPOSController = mPOSController;
    }
    
    private void loadData() {
        tbl_trans.getItems().clear();
        itemList.clear();
        try {
            String tSQL = "SELECT * FROM " + V_All_Item.TABLENAME.get() + " WHERE "
                    + V_All_Item.ITEMNAME.get() + " LIKE '%" + txt_nama_item.getText() + "%' AND "
                    + V_All_Item.FLAG.get() + "=1";
            ResultSet tResult = selectFromDatabase(tSQL);
            int index =0;
            while (tResult.next()) {
                String tKodeItem = tResult.getString(V_All_Item.IDITEM.get());
                String tBarcode = tResult.getString(V_All_Item.BARCODE.get());
                String tKode = tResult.getString(V_All_Item.KODE.get());
                String tSat = tResult.getString(V_All_Item.SATUAN.get());
                String tNamaItem = tResult.getString(V_All_Item.ITEMNAME.get());
                String tKet = tResult.getString(V_All_Item.KET.get());
                int tQty = 1;
                JFXTextField txtQty = new JFXTextField();
                txtQty.setText(String.valueOf(tQty));
                double harga = tResult.getDouble(V_All_Item.HARGAJUAL.get());
                double total = harga;
                JFXButton btnpilih = getButton(EZButtonType.BTN_CHECK, "Pilih");
                btnpilih.setId(""+index);
                setButtonOnTableView(btnpilih, EZButtonType.BTN_CHECK);
                JFXButton btnMinus = null;
                JFXButton btnDelete = null;
                POSModel model = new POSModel(tKodeItem, tBarcode, tKode, tNamaItem, tSat, formatRupiah(harga), String.valueOf(tQty), "0", formatRupiah(total), tKet, btnpilih, btnMinus, btnDelete, null);
                itemList.add(model);
            }
            loadDataToTable();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            int tKey = Integer.parseInt(button.getId());
            POSModel tModel = itemList.get(tKey);
            setDataItem(tModel);
        });

    }
    
    private void setDataItem(POSModel model){
        modelItem = model;
        txt_qty.setText("1");
        txt_qty.requestFocus();
        tbl_trans.getItems().clear();
        modelItem.setBtnplus(null);
        tbl_trans.getItems().add(modelItem);
    }
    
    private void loadDataToTable() {
        setValueColum();
        tbl_trans.setItems(itemList);
        tbl_trans.requestFocus();
        txt_nama_item.setText("");
    }

    private void setValueColum() {
        clm_sku.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.KODE.get()));
        clm_barcode.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.BARCODE.get()));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.ITEMNAME.get()));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.SATUAN.get()));
        clm_harga.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.HARGAJUAL.get()));
        clm_action.setCellValueFactory(new PropertyValueFactory<>("btnplus"));
    }
}
