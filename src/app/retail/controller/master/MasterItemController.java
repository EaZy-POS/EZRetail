/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.master;

import app.retail.controller.home.HomeController;
import app.retail.model.master.ItemModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Item;
import app.retail.utility.table.V_All_Item;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class MasterItemController extends AbstractMaster implements Initializable {

    JFXButton btnedit, btndelete;
    ObservableList<ItemModel> itemList = FXCollections.observableArrayList();
    ObservableList<String> kategoriList = FXCollections.observableArrayList();
    ObservableList<String> subkategoriList = FXCollections.observableArrayList();
    ObservableList<String> supplierList = FXCollections.observableArrayList();
    ObservableList<String> satList = FXCollections.observableArrayList();
    ObservableList<String> limitList = FXCollections.observableArrayList("100", "200", "300", "400", "500", "All");
    ObservableList<String> paramList = FXCollections.observableArrayList( "Nama", "SKU", "Barcode", "Kategori", "Supplier");
    private getItem mGetItem = new getItem(this);
    private static String mQuery;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXTextField txt_cari;
    @FXML
    private Button btn_print;
    @FXML
    private JFXButton menu_supplier;
    @FXML
    private JFXButton menu_kategori;
    @FXML
    private JFXButton menu_subcategori;
    @FXML
    private JFXButton menu_satuan;
    @FXML
    private JFXButton btn_add;
    @FXML
    private ComboBox<String> cmb_batas;
    @FXML
    private ComboBox<String> cmb_param;
    @FXML
    private TableView<ItemModel> tbl_item;
    @FXML
    private TableColumn<ItemModel, String> clm_no;
    @FXML
    private TableColumn<ItemModel, String> clm_nama;
    @FXML
    private TableColumn<ItemModel, String> clm_kategori;
    @FXML
    private TableColumn<ItemModel, String> clm_subkategori;
    @FXML
    private TableColumn<ItemModel, String> clm_supplier;
    @FXML
    private TableColumn<ItemModel, JFXButton> clm_detail;
    @FXML
    private TableColumn<ItemModel, JFXButton> clm_delete;
    @FXML
    private TableColumn<ItemModel, String> clm_sku;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if (!HomeController.isAllowed("Input Data Item")) {
                btn_add.setDisable(true);
            }

            if (!HomeController.isAllowed("Master Supplier")) {
                menu_supplier.setDisable(true);
            }
            if (!HomeController.isAllowed("Master Kategori")) {
                menu_kategori.setDisable(true);
            }
            if (!HomeController.isAllowed("Master Sub Kategori")) {
                menu_subcategori.setDisable(true);
            }
            if (!HomeController.isAllowed("Master Satuan")) {
                menu_satuan.setDisable(true);
            }
            btn_print.setVisible(true);
            cmb_batas.getItems().addAll(limitList);
            cmb_batas.getSelectionModel().select(0);
            cmb_param.getItems().addAll(paramList);
            cmb_param.getSelectionModel().select(0);
            mGetItem.start();
            setLayout();
            setButtonListener();
            setToolTip();
            initializeState();
        } catch (Exception e) {
            loggingerror(e);
        }
    }

    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("NO"));
        clm_sku.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.ITEMCODE.get()));
        clm_nama.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.ITEMNAME.get()));
        clm_kategori.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.KAT.get()));
        clm_subkategori.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.SUBKAT.get()));
        clm_supplier.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.SUPNAME.get()));
        clm_detail.setCellValueFactory(new PropertyValueFactory<>("btnview"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
    }

    private void setLayout() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 225);
    }
    
    private String getWhere(){
        String tCond = cmb_param.getSelectionModel().getSelectedItem();
        String tWhere = "";
        if (tCond.equalsIgnoreCase("nama")) {
            tWhere = V_All_Item.ITEMNAME.get()+" LIKE '%"+txt_cari.getText()+"%'";
        }
        
        if (tCond.equalsIgnoreCase("sku")) {
            tWhere = V_All_Item.KODE.get()+" = '"+txt_cari.getText()+"'";
        }
        
        if (tCond.equalsIgnoreCase("barcode")) {
            tWhere = V_All_Item.BARCODE.get()+" = '"+txt_cari.getText()+"'";
        }
        
        if (tCond.equalsIgnoreCase("kategori")) {
            tWhere = V_All_Item.KAT.get()+" LIKE '%"+txt_cari.getText()+"%'";
        }
        
        if (tCond.equalsIgnoreCase("supplier")) {
            tWhere = V_All_Item.SUPNAME.get()+" LIKE '%"+txt_cari.getText()+"%'";
        }
        
        return tWhere;
    }

    @Override
    public void loadData() {
        tbl_item.getItems().clear();
        setValueColum();
        tbl_item.setItems(getItemList());
    }

    @Override
    public void setButtonListener() {
        txt_cari.setOnKeyTyped((KeyEvent event) -> {
            if (txt_cari.getText().length() >= 3) {
                mGetItem.loadData(getWhere());
            } else {
                mGetItem.loadData();
            }
        });

        menu_supplier.setOnAction((ActionEvent event) -> {
            final String tUrl = "/app/retail/fxml/inventory/MasterSupplier.fxml";
            loadForm(tUrl, "Master Supplier", event);
        });

        menu_kategori.setOnAction((ActionEvent event) -> {
            final String tUrl = "/app/retail/fxml/inventory/MasterKategori.fxml";
            loadForm(tUrl, "Master Kategori", event);
        });

        menu_subcategori.setOnAction((ActionEvent event) -> {
            final String tUrl = "/app/retail/fxml/inventory/MasterSubKategori.fxml";
            loadForm(tUrl, "Master Sub Kategori", event);
        });

        menu_satuan.setOnAction((ActionEvent event) -> {
            final String tUrl = "/app/retail/fxml/inventory/MasterSatuan.fxml";
            loadForm(tUrl, "Master Satuan", event);
        });

        cmb_batas.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            mGetItem = new getItem(MasterItemController.this, cmb_batas.getSelectionModel().getSelectedItem());
            mGetItem.start();
        });
        
        btn_add.setOnAction((ActionEvent event) -> {
            try {
                FormMasterItemController.setIsEdit(false);
                final String tUrl = "/app/retail/fxml/inventory/FormMasterItem.fxml";
                getmHomeController().loadForm(tUrl, "Master Item", event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        btn_print.setOnAction((ActionEvent event) -> {
            if(tbl_item.getItems().size() > 0){
                Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak Data Item ?", EZIcon.ICON_APPS);
                if (opt.get() == ButtonType.OK) {
                    showReport("report/master/item/lap_item.jrxml", mQuery);
                }
            }else{
                EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Data Tidak ada !", EZIcon.ICON_APPS);
            }
        });
    }

    @Override
    public void loadForm(String pUrl, String pName, javafx.event.ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pUrl));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            root.setOnMousePressed((MouseEvent e) -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent e) -> {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(pName);
            stage.getIcons().add(EZIcon.ICON_APPS.get());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
            
            scene.setOnKeyPressed((evt) -> {
                if(pName.equalsIgnoreCase("Master Supplier")){
                    getmSupplierControler().setShortcut(evt);
                }
                
                if(pName.equalsIgnoreCase("Master Kategori")){
                    getmKategoriController().setShortcut(evt);
                }
            });
            
        } catch (IOException ex) {
            loggingerror(ex);
        }
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        Platform.runLater(()->{
            txt_cari.requestFocus();
        });
        
        button.setOnAction((ActionEvent event) -> {
            if (type == EZButtonType.BTN_VIEW) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "View detail data item ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    try {
                        FormMasterItemController.setIsEdit(true);
                        FormMasterItemController.setmKodeEdit(button.getId());
                        final String tUrl = "/app/retail/fxml/inventory/FormMasterItem.fxml";
                        getmHomeController().loadForm(tUrl, this.getClass().getName(), event);
                    } catch (IOException ex) {
                        loggingerror(ex);
                    }
                }
            }

            if (type == EZButtonType.BTN_DELETE) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> option = EZSystem.showAllert(alert, "Yakin hapus data item ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    delete(button.getId());
                }
            }
        });
    }

    public void delete(String param) {
        try {
            String tQuery = "UPDATE "+Item.TABLENAME.get()+" SET "+Item.FLAG.get()+"=2 WHERE "+Item.ITEMCODE.get()+"='" + param + "';";
            updateToDatabase(tQuery);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            EZSystem.showAllert(alert, "Data berhasil di hapus", EZIcon.ICON_STAGE);
            initializeState();
            mGetItem.loadData();
        } catch (SQLException e) {
            if (e.toString().contains("Cannot delete or update a parent row")) {
                EZSystem.showAllert(EZAlertType.ERROR, "Data tidak bisa dihapus, data sudah dipakai!", EZIcon.ICON_STAGE);
            } else {
                loggingerror(e);
            }
        }
    }

    @Override
    public void setToolTip() {
        menu_kategori.setTooltip(new Tooltip("Master Kategori"));
        menu_kategori.setCursor(Cursor.HAND);
        menu_subcategori.setTooltip(new Tooltip("Master Sub Kategori"));
        menu_subcategori.setCursor(Cursor.HAND);
        menu_supplier.setTooltip(new Tooltip("Master Supplier"));
        menu_supplier.setCursor(Cursor.HAND);
        menu_satuan.setTooltip(new Tooltip("Master Satuan"));
        menu_satuan.setCursor(Cursor.HAND);
        btn_print.setTooltip(new Tooltip("Print"));
        btn_print.setCursor(Cursor.HAND);
        btn_add.setTooltip(new Tooltip("Tambah Item"));
        btn_add.setCursor(Cursor.HAND);
    }

    public ObservableList<ItemModel> getItemList() {
        return itemList;
    }

    public void setItemList(ObservableList<ItemModel> itemList) {
        this.itemList = itemList;
    }

    @Override
    public void initializeState() {
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_sku, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama, Pos.CENTER_LEFT);
        setAligmentColoum(clm_kategori, Pos.CENTER_LEFT);
        setAligmentColoum(clm_subkategori, Pos.CENTER_LEFT);
        setAligmentColoum(clm_supplier, Pos.CENTER_LEFT);
        double lebar = getWidth() - 200;
        clm_no.setPrefWidth((lebar*3)/100);
        clm_sku.setPrefWidth((lebar*8)/100);
        clm_nama.setPrefWidth((lebar*29)/100);
        clm_kategori.setPrefWidth((lebar*15)/100);
        clm_subkategori.setPrefWidth((lebar*15)/100);
        clm_supplier.setPrefWidth((lebar*25)/100);
        clm_detail.setPrefWidth((lebar*8)/100);
        clm_delete.setPrefWidth((lebar*8)/100);
    }

    @Override
    public void loadKategori() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadSubKategori(String subid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadSatuan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadSupplier() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String getmQuery() {
        return mQuery;
    }

    public static void setmQuery(String mQuery) {
        MasterItemController.mQuery = mQuery;
    }
}
