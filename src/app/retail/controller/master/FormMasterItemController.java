/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.master;

import app.retail.controller.general.General;
import static app.retail.controller.general.General.getmHomeController;
import static app.retail.controller.master.AbstractMaster.MAP_KATEGORI;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Item;
import app.retail.utility.table.Item_Detail;
import app.retail.utility.table.V_All_Item;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class FormMasterItemController extends AbstractMaster implements Initializable {

    ObservableList<String> listSatuan = FXCollections.observableArrayList();
    ObservableList<String> listSupplier = FXCollections.observableArrayList();
    ObservableList<String> listKategori = FXCollections.observableArrayList();
    ObservableList<String> listSubKategori = FXCollections.observableArrayList();
    private static boolean isEdit;
    private static String mKodeEdit;
    private static List<String> mListidItem;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXButton btn_kembali;
    @FXML
    private TextField txt_kode_item;
    @FXML
    private TextField txt_nama_item;
    @FXML
    private ComboBox<String> cmb_kategori;
    @FXML
    private ComboBox<String> cmb_subkategori;
    @FXML
    private ComboBox<String> cmb_supplier;
    @FXML
    private TitledPane pane_satuan_1;
    @FXML
    private TextField txt_sku_1;
    @FXML
    private TextField txt_barcode_1;
    @FXML
    private ComboBox<String> cmb_satuan_1;
    @FXML
    private TextField txt_hpp_1;
    @FXML
    private TextField txt_ppn_1;
    @FXML
    private TextField txt_disc_1;
    @FXML
    private TextField txt_harga_beli_1;
    @FXML
    private TextField txt_margin_1;
    @FXML
    private TextField txt_markup_1;
    @FXML
    private TextField txt_harga_jual_1;
    @FXML
    private TitledPane pane_satuan_2;
    @FXML
    private TextField txt_sku_2;
    @FXML
    private TextField txt_barcode_2;
    @FXML
    private ComboBox<String> cmb_satuan_2;
    @FXML
    private TextField txt_konversi_2;
    @FXML
    private TextField txt_hpp_2;
    @FXML
    private TextField txt_ppn_2;
    @FXML
    private TextField txt_disc_2;
    @FXML
    private TextField txt_harga_beli_2;
    @FXML
    private TextField txt_margin_2;
    @FXML
    private TextField txt_markup_2;
    @FXML
    private TextField txt_harga_jual_2;
    @FXML
    private TitledPane pane_satuan_3;
    @FXML
    private TextField txt_sku_3;
    @FXML
    private ComboBox<String> cmb_satuan_3;
    @FXML
    private TextField txt_konversi_3;
    @FXML
    private TextField txt_hpp_3;
    @FXML
    private TextField txt_ppn_3;
    @FXML
    private TextField txt_disc_3;
    @FXML
    private TextField txt_harga_beli_3;
    @FXML
    private TextField txt_margin_3;
    @FXML
    private TextField txt_markup_3;
    @FXML
    private TextField txt_harga_jual_3;
    @FXML
    private TitledPane pane_satuan_4;
    @FXML
    private TextField txt_sku_4;
    @FXML
    private TextField txt_barcode_4;
    @FXML
    private ComboBox<String> cmb_satuan_4;
    @FXML
    private TextField txt_konversi_4;
    @FXML
    private TextField txt_hpp_4;
    @FXML
    private TextField txt_ppn_4;
    @FXML
    private TextField txt_disc_4;
    @FXML
    private TextField txt_harga_beli_4;
    @FXML
    private TextField txt_margin_4;
    @FXML
    private TextField txt_markup_4;
    @FXML
    private TextField txt_harga_jual_4;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private TextField txt_barcode_3;
    @FXML
    private TextField txt_stok_minimal_1;
    @FXML
    private TextField txt_stok_minimal_2;
    @FXML
    private TextField txt_stok_minimal_3;
    @FXML
    private TextField txt_stok_minimal_4;
    @FXML
    private CheckBox check_active_1;
    @FXML
    private CheckBox check_active_2;
    @FXML
    private CheckBox check_active_3;
    @FXML
    private CheckBox check_active_4;
    @FXML
    private Label lbl_tiitle;
    @FXML
    private TextField txt_nama_lain;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setActive(false);
        initializeState();
        setToolTip();
        setButtonListener();
    }
    
    private void setActive(boolean aktif){
        check_active_1.setVisible(aktif);
        check_active_3.setVisible(aktif);
        check_active_4.setVisible(aktif);
        check_active_2.setVisible(aktif);
    }
    
    private void setActive(CheckBox cek, boolean flag){
        cek.setSelected(flag);
    }

    public static void setIsEdit(boolean isEdit) {
        FormMasterItemController.isEdit = isEdit;
    }

    public static void setmKodeEdit(String mKodeEdit) {
        FormMasterItemController.mKodeEdit = mKodeEdit;
    }

    @Override
    public void loadData() {
        try {
            String tSQL = "SELECT * FROM "+V_All_Item.TABLENAME.get()+" WHERE "+V_All_Item.ITEMCODE.get()+"='"+mKodeEdit+"'"+" GROUP BY "+V_All_Item.ITEMCODE.get() +" ";
            ResultSet tResult = selectFromDatabase(tSQL);
            if (tResult.next()) {
                txt_kode_item.setText(tResult.getString(V_All_Item.ITEMCODE.get()));
                txt_nama_item.setText(tResult.getString(V_All_Item.ITEMNAME.get()));
                cmb_kategori.getSelectionModel().select(tResult.getString(V_All_Item.KAT.get()));
                String idKat = tResult.getString(V_All_Item.KATID.get());
                loadSubKategori(idKat);
                cmb_subkategori.getSelectionModel().select(tResult.getString(V_All_Item.SUBKAT.get()));
                cmb_supplier.getSelectionModel().select(tResult.getString(V_All_Item.SUPNAME.get()));
                txt_nama_lain.setText(tResult.getString(V_All_Item.KET.get()));
            }
            
            tSQL = "SELECT * FROM "+V_All_Item.TABLENAME.get()+" WHERE "+V_All_Item.ITEMCODE.get()+"='"+mKodeEdit+"'";
            List<JSONObject> tItemList = queryToList(tSQL);
            load(tItemList);
        } catch (SQLException ex) {
            loggingerror(ex);
        }catch (Exception ex) {
            loggingerror(ex);
        }
        
    }
    
    private void load(List<JSONObject> pListItem) throws Exception{
        mListidItem = new ArrayList<>();
        for (int i = 0; i < pListItem.size(); i++) {
            JSONObject tItem = pListItem.get(i);
            String tid = tItem.getString(V_All_Item.IDITEM.get());
            String tSKu = tItem.getString(V_All_Item.KODE.get());
            String tBarcode = tItem.getString(V_All_Item.BARCODE.get());
            String tSatuan = tItem.getString(V_All_Item.SATUAN.get());
            String tStokMin = tItem.getString(V_All_Item.STOKMIN.get());
            String tKonversi = tItem.getString(V_All_Item.KONVERSI.get());
            double tHargaBeli = tItem.getDouble(V_All_Item.HRGBELI.get());
            double tHargaJual = tItem.getDouble(V_All_Item.HARGAJUAL.get());
            double tPpn = tItem.getDouble(V_All_Item.PPN.get());
            double tdisc = tItem.getDouble(V_All_Item.DISC.get());
            double tHpp = tItem.getDouble(V_All_Item.HPP.get());
            double tMargin = tItem.getDouble(V_All_Item.MARGIN.get());
            double tMarkup = tItem.getDouble(V_All_Item.MARKUP.get());
            boolean isActive = tItem.getString(V_All_Item.ACTIVE.get()).equals("1");
            
            if(i == 0){
                pane_satuan_1.setExpanded(true);
                check_active_1.setVisible(true);
                setActive(check_active_1, isActive);
                mListidItem.add(tid);
                txt_sku_1.setText(tSKu);
                txt_barcode_1.setText(tBarcode);
                cmb_satuan_1.getSelectionModel().select(tSatuan);
                txt_stok_minimal_1.setText(tStokMin);
                txt_hpp_1.setText(formatRupiah(tHpp));
                txt_ppn_1.setText(formatRupiah(tPpn));
                txt_disc_1.setText(formatRupiah(tdisc));
                txt_harga_beli_1.setText(formatRupiah(tHargaBeli));
                txt_markup_1.setText(formatRupiah(tMarkup));
                txt_margin_1.setText(formatRupiah(tMargin));
                txt_harga_jual_1.setText(formatRupiah(tHargaJual));
            }
            
            if(i == 1){
                pane_satuan_2.setExpanded(true);
                check_active_2.setVisible(true);
                setActive(check_active_2, isActive);
                mListidItem.add(tid);
                txt_sku_2.setText(tSKu);
                txt_barcode_2.setText(tBarcode);
                cmb_satuan_2.getSelectionModel().select(tSatuan);
                txt_stok_minimal_2.setText(tStokMin);
                txt_hpp_2.setText(formatRupiah(tHpp));
                txt_ppn_2.setText(formatRupiah(tPpn));
                txt_disc_2.setText(formatRupiah(tdisc));
                txt_harga_beli_2.setText(formatRupiah(tHargaBeli));
                txt_markup_2.setText(formatRupiah(tMarkup));
                txt_margin_2.setText(formatRupiah(tMargin));
                txt_harga_jual_2.setText(formatRupiah(tHargaJual));
                txt_konversi_2.setText(tKonversi);
            }
            
            if(i == 2){
                pane_satuan_3.setExpanded(true);
                check_active_3.setVisible(true);
                setActive(check_active_3, isActive);
                mListidItem.add(tid);
                txt_sku_3.setText(tSKu);
                txt_barcode_3.setText(tBarcode);
                cmb_satuan_3.getSelectionModel().select(tSatuan);
                txt_stok_minimal_3.setText(tStokMin);
                txt_hpp_3.setText(formatRupiah(tHpp));
                txt_ppn_3.setText(formatRupiah(tPpn));
                txt_disc_3.setText(formatRupiah(tdisc));
                txt_harga_beli_3.setText(formatRupiah(tHargaBeli));
                txt_markup_3.setText(formatRupiah(tMarkup));
                txt_margin_3.setText(formatRupiah(tMargin));
                txt_harga_jual_3.setText(formatRupiah(tHargaJual));
                txt_konversi_3.setText(tKonversi);
            }
            
            if(i == 3){
                pane_satuan_4.setExpanded(true);
                check_active_4.setVisible(true);
                setActive(check_active_4, isActive);
                mListidItem.add(tid);
                txt_sku_4.setText(tSKu);
                txt_barcode_4.setText(tBarcode);
                cmb_satuan_4.getSelectionModel().select(tSatuan);
                txt_stok_minimal_4.setText(tStokMin);
                txt_hpp_4.setText(formatRupiah(tHpp));
                txt_ppn_4.setText(formatRupiah(tPpn));
                txt_disc_4.setText(formatRupiah(tdisc));
                txt_harga_beli_4.setText(formatRupiah(tHargaBeli));
                txt_markup_4.setText(formatRupiah(tMarkup));
                txt_margin_4.setText(formatRupiah(tMargin));
                txt_harga_jual_4.setText(formatRupiah(tHargaJual));
                txt_konversi_4.setText(tKonversi);
            }
        }
    }

    @Override
    public void loadKategori() {
        List<String> mList = getKategori();
        cmb_kategori.getItems().clear();
        cmb_subkategori.getItems().clear();
        listKategori.clear();
        listKategori.add("--Pilih Kategori --");
        listSubKategori.clear();
        listSubKategori.add("-- Pilih Sub Kategori --");

        mList.forEach((kat) -> {
            listKategori.add(kat);
        });

        cmb_kategori.setItems(listKategori);
        cmb_subkategori.setItems(listSubKategori);
        cmb_kategori.getSelectionModel().select(0);
        cmb_subkategori.getSelectionModel().select(0);
    }

    @Override
    public void loadSubKategori(String subid) {
        List<String> mList = getSubKategori(subid);
        cmb_subkategori.getItems().clear();
        listSubKategori.clear();
        listSubKategori.add("-- Pilih Sub Kategori --");

        mList.forEach((kat) -> {
            listSubKategori.add(kat);
        });

        cmb_subkategori.setItems(listSubKategori);
        cmb_subkategori.getSelectionModel().select(0);
    }

    @Override
    public void loadSatuan() {
        List<String> mList = getSat();
        cmb_satuan_1.getItems().clear();
        cmb_satuan_2.getItems().clear();
        cmb_satuan_3.getItems().clear();
        cmb_satuan_4.getItems().clear();
        listSatuan.clear();
        listSatuan.add("--Pilih Satuan --");
        mList.forEach((kat) -> {
            listSatuan.add(kat);
        });

        cmb_satuan_1.setItems(listSatuan);
        cmb_satuan_2.setItems(listSatuan);
        cmb_satuan_3.setItems(listSatuan);
        cmb_satuan_4.setItems(listSatuan);
        cmb_satuan_1.getSelectionModel().select(0);
        cmb_satuan_2.getSelectionModel().select(0);
        cmb_satuan_3.getSelectionModel().select(0);
        cmb_satuan_4.getSelectionModel().select(0);
    }

    @Override
    public void loadSupplier() {
        List<String> mList = getSupplier();
        cmb_supplier.getItems().clear();
        listSupplier.clear();
        listSupplier.add("--Pilih Supplier --");

        mList.forEach((kat) -> {
            listSupplier.add(kat);
        });

        cmb_supplier.setItems(listSupplier);
        cmb_supplier.getSelectionModel().select(0);
    }

    @Override
    public void initializeState() {
        AnchorPane.setPrefSize(getWidth() - 210, getHeight());
        btn_batal.setCursor(Cursor.HAND);
        btn_kembali.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
        pane_satuan_4.setExpanded(false);
        pane_satuan_3.setExpanded(false);
        clear();
        if (isEdit) {
            lbl_tiitle.setText("Form Edit Master Item");
            pane_satuan_1.setExpanded(false);
            pane_satuan_2.setExpanded(false);
            loadData();
        }
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(() -> {
            txt_nama_item.requestFocus();
        });

        btn_kembali.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    final String tUrl = "/app/retail/fxml/inventory/MasterItem.fxml";
                    getmHomeController().loadForm(tUrl, this.getClass().getName(), event);
                } catch (IOException ex) {
                    String tErroMessage = "ERROR: " + ex.getMessage();
                    EZSystem.showAllert(EZAlertType.ERROR, tErroMessage, EZIcon.ICON_STAGE);
                    SystemLog.getLogger().writeLog(LogType.ERROR, tErroMessage + General.getStackTraceString(ex));
                }
            }
        });

        cmb_kategori.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String val = cmb_kategori.getValue();
            if (MAP_KATEGORI.containsKey(val)) {
                String id = MAP_KATEGORI.get(val);
                loadSubKategori(id);
            } else {
                cmb_subkategori.getItems().clear();
                listSubKategori.add("-- Pilih Subkategori --");
                cmb_subkategori.setItems(listSubKategori);
                cmb_subkategori.getSelectionModel().select(0);
            }
            cmb_subkategori.getSelectionModel().select(0);
        });
        
        btn_batal.setOnAction((evt)->{
            initializeState();
        });

        btn_simpan.setOnAction((ActionEvent event) -> {
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Simpan data item ?", EZIcon.ICON_STAGE);
            if (opt.get() == ButtonType.OK) {
                try {
                    if (validateForm()) {
                        simpan();
                    }
                } catch (SQLException ex) {
                    loggingerror(ex);
                }
            }
        });
        
        txt_nama_item.setOnKeyReleased((KeyEvent svent) -> {
            txt_nama_lain.setText(generateNickName());
        });

        txt_nama_lain.setOnKeyPressed((KeyEvent svent) -> {
            if (svent.getCode() == KeyCode.ENTER) {
                cmb_kategori.requestFocus();
            }
        });

        txt_sku_2.setOnKeyReleased((KeyEvent event) -> {
            if (txt_sku_2.getText().length() > 0) {
                if (txt_sku_1.getText().length() == 0 || txt_barcode_1.getText().length() == 0) {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Mohon isi data satuan 1 terlebih dahulu!", EZIcon.ICON_STAGE);
                    txt_sku_2.setText("");
                    txt_sku_1.requestFocus();
                }
            }
        });

        txt_barcode_2.setOnKeyReleased((KeyEvent event) -> {
            if (txt_barcode_2.getText().length() > 0) {
                if (txt_sku_1.getText().length() == 0 || txt_barcode_1.getText().length() == 0) {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Mohon isi data satuan 1 terlebih dahulu!", EZIcon.ICON_STAGE);
                    txt_barcode_2.setText("");
                    txt_sku_1.requestFocus();
                }
            }
        });

        txt_sku_3.setOnKeyReleased((KeyEvent event) -> {
            if (txt_sku_3.getText().length() > 0) {
                if (txt_sku_2.getText().length() == 0 || txt_barcode_2.getText().length() == 0) {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Mohon isi data satuan 2 terlebih dahulu!", EZIcon.ICON_STAGE);
                    txt_sku_3.setText("");
                    txt_sku_2.requestFocus();
                }
            }
        });

        txt_barcode_3.setOnKeyReleased((KeyEvent event) -> {
            if (txt_barcode_3.getText().length() > 0) {
                if (txt_sku_2.getText().length() == 0 || txt_barcode_2.getText().length() == 0) {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Mohon isi data satuan 2 terlebih dahulu!", EZIcon.ICON_STAGE);
                    txt_barcode_3.setText("");
                    txt_sku_2.requestFocus();
                }
            }
        });

        txt_sku_4.setOnKeyReleased((KeyEvent event) -> {
            if (txt_sku_4.getText().length() > 0) {
                if (txt_sku_3.getText().length() == 0 || txt_barcode_3.getText().length() == 0) {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Mohon isi data satuan 3 terlebih dahulu!", EZIcon.ICON_STAGE);
                    txt_sku_4.setText("");
                    txt_sku_3.requestFocus();
                }
            }
        });

        txt_barcode_4.setOnKeyReleased((KeyEvent event) -> {
            if (txt_barcode_4.getText().length() > 0) {
                if (txt_sku_3.getText().length() == 0 || txt_barcode_3.getText().length() == 0) {
                    EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Mohon isi data satuan 3 terlebih dahulu!", EZIcon.ICON_STAGE);
                    txt_barcode_4.setText("");
                    txt_sku_3.requestFocus();
                }
            }
        });

        txt_hpp_1.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_1();
        });

        txt_ppn_1.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_1();
        });

        txt_disc_1.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_1();
        });

        txt_hpp_2.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_2();
        });

        txt_ppn_2.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_2();
        });

        txt_disc_2.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_2();
        });

        txt_hpp_3.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_3();
        });

        txt_ppn_3.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_3();
        });

        txt_disc_3.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_3();
        });

        txt_hpp_4.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_4();
        });

        txt_ppn_4.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_4();
        });

        txt_disc_4.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaBeli_4();
        });

        txt_margin_1.setOnKeyReleased((KeyEvent key) -> {
            hitungMargin_1();
        });

        txt_margin_2.setOnKeyReleased((KeyEvent key) -> {
            hitungMargin_2();
        });

        txt_margin_3.setOnKeyReleased((KeyEvent key) -> {
            hitungMargin_3();
        });

        txt_margin_4.setOnKeyReleased((KeyEvent key) -> {
            hitungMargin_4();
        });

        txt_markup_1.setOnKeyReleased((KeyEvent key) -> {
            hitungMarkup_1();
        });

        txt_markup_2.setOnKeyReleased((KeyEvent key) -> {
            hitungMarkup_2();
        });

        txt_markup_3.setOnKeyReleased((KeyEvent key) -> {
            hitungMarkup_3();
        });

        txt_markup_4.setOnKeyReleased((KeyEvent key) -> {
            hitungMarkup_4();
        });

        txt_harga_jual_1.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaJual_1();
        });

        txt_harga_jual_2.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaJual_2();
        });

        txt_harga_jual_3.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaJual_3();
        });

        txt_harga_jual_4.setOnKeyReleased((KeyEvent key) -> {
            hitungHargaJual_4();
        });
    }

    private void hitungHargaBeli_1() {
        double tHpp = Double.parseDouble(txt_hpp_1.getText().replace(",", "").length() > 0 ? txt_hpp_1.getText().replace(",", "") : "0");
        double tPpn = Double.parseDouble(txt_ppn_1.getText().replace(",", "").length() > 0 ? txt_ppn_1.getText().replace(",", "") : "0");
        double tDisc = Double.parseDouble(txt_disc_1.getText().replace(",", "").length() > 0 ? txt_disc_1.getText().replace(",", "") : "0");
        tPpn = (tHpp * tPpn) / 100;
        tDisc = (tHpp * tDisc) / 100;

        double tTotal = (tHpp + tPpn) - tDisc;
        txt_harga_beli_1.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_harga_jual_1.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_margin_1.setText("0");
        txt_markup_1.setText("0");
    }

    private void hitungHargaBeli_2() {
        double tHpp = Double.parseDouble(txt_hpp_2.getText().replace(",", "").length() > 0 ? txt_hpp_2.getText().replace(",", "") : "0");
        double tPpn = Double.parseDouble(txt_ppn_2.getText().replace(",", "").length() > 0 ? txt_ppn_2.getText().replace(",", "") : "0");
        double tDisc = Double.parseDouble(txt_disc_2.getText().replace(",", "").length() > 0 ? txt_disc_2.getText().replace(",", "") : "0");
        tPpn = (tHpp * tPpn) / 100;
        tDisc = (tHpp * tDisc) / 100;

        double tTotal = (tHpp + tPpn) - tDisc;
        txt_harga_beli_2.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_harga_jual_2.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_margin_2.setText("0");
        txt_markup_2.setText("0");
    }

    private void hitungHargaBeli_3() {
        double tHpp = Double.parseDouble(txt_hpp_3.getText().replace(",", "").length() > 0 ? txt_hpp_3.getText().replace(",", "") : "0");
        double tPpn = Double.parseDouble(txt_ppn_3.getText().replace(",", "").length() > 0 ? txt_ppn_3.getText().replace(",", "") : "0");
        double tDisc = Double.parseDouble(txt_disc_3.getText().replace(",", "").length() > 0 ? txt_disc_3.getText().replace(",", "") : "0");
        tPpn = (tHpp * tPpn) / 100;
        tDisc = (tHpp * tDisc) / 100;

        double tTotal = (tHpp + tPpn) - tDisc;
        txt_harga_beli_3.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_harga_jual_3.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_margin_3.setText("0");
        txt_markup_3.setText("0");
    }

    private void hitungHargaBeli_4() {
        double tHpp = Double.parseDouble(txt_hpp_4.getText().replace(",", "").length() > 0 ? txt_hpp_4.getText().replace(",", "") : "0");
        double tPpn = Double.parseDouble(txt_ppn_4.getText().replace(",", "").length() > 0 ? txt_ppn_4.getText().replace(",", "") : "0");
        double tDisc = Double.parseDouble(txt_disc_4.getText().replace(",", "").length() > 0 ? txt_disc_4.getText().replace(",", "") : "0");
        tPpn = (tHpp * tPpn) / 100;
        tDisc = (tHpp * tDisc) / 100;

        double tTotal = (tHpp + tPpn) - tDisc;
        txt_harga_beli_4.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_harga_jual_4.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_margin_4.setText("0");
        txt_markup_4.setText("0");
    }

    private void hitungMargin_1() {
        double tMarkup = Double.parseDouble(txt_margin_1.getText().replace(",", "").length() > 0 ? txt_margin_1.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_beli_1.getText().replace(",", "").length() > 0 ? txt_harga_beli_1.getText().replace(",", "") : "0");

        double tMargin = (tMarkup / tHargaJual) * 100;

        double tTotal = tHargaJual + tMarkup;
        txt_harga_jual_1.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_markup_1.setText(new DecimalFormat("#,##0").format(tMargin));
    }

    private void hitungMargin_2() {
        double tMarkup = Double.parseDouble(txt_margin_2.getText().replace(",", "").length() > 0 ? txt_margin_2.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_beli_2.getText().replace(",", "").length() > 0 ? txt_harga_beli_2.getText().replace(",", "") : "0");

        double tMargin = (tMarkup / tHargaJual) * 100;

        double tTotal = tHargaJual + tMarkup;
        txt_harga_jual_2.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_markup_2.setText(new DecimalFormat("#,##0").format(tMargin));
    }

    private void hitungMargin_3() {
        double tMarkup = Double.parseDouble(txt_margin_3.getText().replace(",", "").length() > 0 ? txt_margin_3.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_beli_3.getText().replace(",", "").length() > 0 ? txt_harga_beli_3.getText().replace(",", "") : "0");

        double tMargin = (tMarkup / tHargaJual) * 100;

        double tTotal = tHargaJual + tMarkup;
        txt_harga_jual_3.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_markup_3.setText(new DecimalFormat("#,##0").format(tMargin));
    }

    private void hitungMargin_4() {
        double tMarkup = Double.parseDouble(txt_margin_4.getText().replace(",", "").length() > 0 ? txt_margin_4.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_beli_4.getText().replace(",", "").length() > 0 ? txt_harga_beli_4.getText().replace(",", "") : "0");

        double tMargin = (tMarkup / tHargaJual) * 100;

        double tTotal = tHargaJual + tMarkup;
        txt_harga_jual_4.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_markup_4.setText(new DecimalFormat("#,##0").format(tMargin));
    }

    private void hitungMarkup_1() {
        double tMarkup = Double.parseDouble(txt_markup_1.getText().replace(",", "").length() > 0 ? txt_markup_1.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_beli_1.getText().replace(",", "").length() > 0 ? txt_harga_beli_1.getText().replace(",", "") : "0");

        double tMargin = (tMarkup * tHargaJual) / 100;

        double tTotal = tHargaJual + tMargin;
        txt_harga_jual_1.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_margin_1.setText(new DecimalFormat("#,##0").format(tMargin));
    }

    private void hitungMarkup_2() {
        double tMarkup = Double.parseDouble(txt_markup_2.getText().replace(",", "").length() > 0 ? txt_markup_2.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_beli_2.getText().replace(",", "").length() > 0 ? txt_harga_beli_2.getText().replace(",", "") : "0");

        double tMargin = (tMarkup * tHargaJual) / 100;

        double tTotal = tHargaJual + tMargin;
        txt_harga_jual_2.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_margin_2.setText(new DecimalFormat("#,##0").format(tMargin));
    }

    private void hitungMarkup_3() {
        double tMarkup = Double.parseDouble(txt_markup_3.getText().replace(",", "").length() > 0 ? txt_markup_3.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_beli_3.getText().replace(",", "").length() > 0 ? txt_harga_beli_3.getText().replace(",", "") : "0");

        double tMargin = (tMarkup * tHargaJual) / 100;

        double tTotal = tHargaJual + tMargin;
        txt_harga_jual_3.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_margin_3.setText(new DecimalFormat("#,##0").format(tMargin));
    }

    private void hitungMarkup_4() {
        double tMarkup = Double.parseDouble(txt_markup_4.getText().replace(",", "").length() > 0 ? txt_markup_4.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_beli_4.getText().replace(",", "").length() > 0 ? txt_harga_beli_4.getText().replace(",", "") : "0");

        double tMargin = (tMarkup * tHargaJual) / 100;

        double tTotal = tHargaJual + tMargin;
        txt_harga_jual_4.setText(new DecimalFormat("#,##0").format(tTotal));
        txt_margin_4.setText(new DecimalFormat("#,##0").format(tMargin));
    }

    private void hitungHargaJual_1() {
        double tHargaBeli = Double.parseDouble(txt_harga_beli_1.getText().replace(",", "").length() > 0 ? txt_harga_beli_1.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_jual_1.getText().replace(",", "").length() > 0 ? txt_harga_jual_1.getText().replace(",", "") : "0");
        double tMargin = tHargaJual - tHargaBeli;
        double tMarkup = (tMargin / tHargaBeli) * 100;

        txt_margin_1.setText(new DecimalFormat("#,##0").format(tMargin));
        txt_markup_1.setText(new DecimalFormat("#,##0").format(tMarkup));
    }

    private void hitungHargaJual_2() {
        double tHargaBeli = Double.parseDouble(txt_harga_beli_2.getText().replace(",", "").length() > 0 ? txt_harga_beli_2.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_jual_2.getText().replace(",", "").length() > 0 ? txt_harga_jual_2.getText().replace(",", "") : "0");
        double tMargin = tHargaJual - tHargaBeli;
        double tMarkup = (tMargin / tHargaBeli) * 100;

        txt_margin_2.setText(new DecimalFormat("#,##0").format(tMargin));
        txt_markup_2.setText(new DecimalFormat("#,##0").format(tMarkup));
    }

    private void hitungHargaJual_3() {
        double tHargaBeli = Double.parseDouble(txt_harga_beli_3.getText().replace(",", "").length() > 0 ? txt_harga_beli_3.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_jual_3.getText().replace(",", "").length() > 0 ? txt_harga_jual_3.getText().replace(",", "") : "0");
        double tMargin = tHargaJual - tHargaBeli;
        double tMarkup = (tMargin / tHargaBeli) * 100;

        txt_margin_3.setText(new DecimalFormat("#,##0").format(tMargin));
        txt_markup_3.setText(new DecimalFormat("#,##0").format(tMarkup));
    }

    private void hitungHargaJual_4() {
        double tHargaBeli = Double.parseDouble(txt_harga_beli_4.getText().replace(",", "").length() > 0 ? txt_harga_beli_4.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_jual_4.getText().replace(",", "").length() > 0 ? txt_harga_jual_4.getText().replace(",", "") : "0");
        double tMargin = tHargaJual - tHargaBeli;
        double tMarkup = (tMargin / tHargaBeli) * 100;

        txt_margin_4.setText(new DecimalFormat("#,##0").format(tMargin));
        txt_markup_4.setText(new DecimalFormat("#,##0").format(tMarkup));
    }

    @Override
    public void clear() {
        txt_kode_item.setText(generateRefnum(Item.TABLENAME.get(), Item.ITEMCODE.get(), "ITM"+EZDate.FAKTUR.today()));
        txt_margin_1.setEditable(false);
        txt_margin_2.setEditable(false);
        txt_margin_3.setEditable(false);
        txt_margin_4.setEditable(false);
        txt_markup_1.setEditable(false);
        txt_markup_2.setEditable(false);
        txt_markup_3.setEditable(false);
        txt_markup_4.setEditable(false);
        txt_nama_item.setText("");
        txt_nama_lain.setText("");
        txt_sku_1.setText("");
        txt_barcode_1.setText("");
        txt_sku_2.setText("");
        txt_barcode_2.setText("");
        txt_sku_3.setText("");
        txt_barcode_3.setText("");
        txt_sku_4.setText("");
        txt_barcode_4.setText("");
        txt_konversi_2.setText("0");
        txt_konversi_3.setText("0");
        txt_konversi_4.setText("0");
        txt_hpp_1.setText("0");
        txt_hpp_2.setText("0");
        txt_hpp_3.setText("0");
        txt_hpp_4.setText("0");
        txt_ppn_1.setText("0");
        txt_ppn_2.setText("0");
        txt_ppn_3.setText("0");
        txt_ppn_4.setText("0");
        txt_disc_1.setText("0");
        txt_disc_2.setText("0");
        txt_disc_3.setText("0");
        txt_disc_4.setText("0");
        txt_harga_beli_1.setText("0");
        txt_harga_beli_2.setText("0");
        txt_harga_beli_3.setText("0");
        txt_harga_beli_4.setText("0");
        txt_margin_1.setText("0");
        txt_margin_2.setText("0");
        txt_margin_3.setText("0");
        txt_margin_4.setText("0");
        txt_markup_1.setText("0");
        txt_markup_2.setText("0");
        txt_markup_3.setText("0");
        txt_markup_4.setText("0");
        txt_harga_jual_1.setText("0");
        txt_harga_jual_2.setText("0");
        txt_harga_jual_3.setText("0");
        txt_harga_jual_4.setText("0");
        txt_stok_minimal_1.setText("0");
        txt_stok_minimal_2.setText("0");
        txt_stok_minimal_3.setText("0");
        txt_stok_minimal_4.setText("0");
        loadKategori();
        loadSatuan();
        loadSupplier();
        txt_nama_item.requestFocus();
    }

    @Override
    public void setToolTip() {
        setCurencyTextfield(txt_hpp_1);
        setCurencyTextfield(txt_hpp_2);
        setCurencyTextfield(txt_hpp_3);
        setCurencyTextfield(txt_hpp_1);
        setCurencyTextfield(txt_harga_jual_1);
        setCurencyTextfield(txt_harga_jual_2);
        setCurencyTextfield(txt_harga_jual_3);
        setCurencyTextfield(txt_harga_jual_4);
        setCurencyTextfield(txt_margin_1);
        setCurencyTextfield(txt_margin_2);
        setCurencyTextfield(txt_margin_3);
        setCurencyTextfield(txt_margin_4);
        setNumericTextfield(txt_disc_1);
        setNumericTextfield(txt_disc_2);
        setNumericTextfield(txt_disc_3);
        setNumericTextfield(txt_disc_4);
        setNumericTextfield(txt_ppn_1);
        setNumericTextfield(txt_ppn_2);
        setNumericTextfield(txt_ppn_3);
        setNumericTextfield(txt_ppn_4);
        setNumericTextfield(txt_markup_1);
        setNumericTextfield(txt_markup_2);
        setNumericTextfield(txt_markup_3);
        setNumericTextfield(txt_markup_4);
        setLimitTexfield(txt_nama_item, 60);
        setLimitTexfield(txt_nama_lain, 15);
        setLimitTexfield(txt_sku_1, 40);
        setLimitTexfield(txt_sku_2, 40);
        setLimitTexfield(txt_sku_3, 40);
        setLimitTexfield(txt_sku_4, 40);
        setLimitTexfield(txt_barcode_1, 40);
        setLimitTexfield(txt_barcode_2, 40);
        setLimitTexfield(txt_barcode_3, 40);
        setLimitTexfield(txt_barcode_4, 40);
        setLimitTexfield(txt_ppn_1, 4);
        setLimitTexfield(txt_ppn_2, 4);
        setLimitTexfield(txt_ppn_3, 4);
        setLimitTexfield(txt_ppn_4, 4);
        setLimitTexfield(txt_disc_1, 4);
        setLimitTexfield(txt_disc_2, 4);
        setLimitTexfield(txt_disc_3, 4);
        setLimitTexfield(txt_disc_4, 4);
        setLimitTexfield(txt_markup_1, 4);
        setLimitTexfield(txt_markup_2, 4);
        setLimitTexfield(txt_markup_3, 4);
        setLimitTexfield(txt_markup_4, 4);
        setNumericTextfield(txt_stok_minimal_1);
        setNumericTextfield(txt_stok_minimal_2);
        setNumericTextfield(txt_stok_minimal_3);
        setNumericTextfield(txt_stok_minimal_4);
        setLimitTexfield(txt_stok_minimal_1, 4);
        setLimitTexfield(txt_stok_minimal_2, 4);
        setLimitTexfield(txt_stok_minimal_3, 4);
        setLimitTexfield(txt_stok_minimal_4, 4);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean validateForm() throws SQLException {
        boolean isValidForm = true;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (txt_nama_item.getText().length() == 0) {
            EZSystem.showAllert(alert, "Mohon isi nama item dengan benar!", EZIcon.ICON_STAGE);
            txt_nama_item.requestFocus();
            return false;
        }

        if (cmb_kategori.getSelectionModel().getSelectedIndex() == 0) {
            EZSystem.showAllert(alert, "Mohon pilih kategori!", EZIcon.ICON_STAGE);
            return false;
        }

        if (cmb_subkategori.getSelectionModel().getSelectedIndex() == 0) {
            EZSystem.showAllert(alert, "Mohon pilih subkategori!", EZIcon.ICON_STAGE);
            return false;
        }

        if (cmb_supplier.getSelectionModel().getSelectedIndex() == 0) {
            EZSystem.showAllert(alert, "Mohon pilih supplier!", EZIcon.ICON_STAGE);
            return false;
        }

        if (txt_sku_1.getText().length() == 0 || txt_barcode_1.getText().length() == 0) {
            EZSystem.showAllert(alert, "Mohon isi Sku/Barcode satuan 1!", EZIcon.ICON_STAGE);
            return false;
        } else {
            if (cmb_satuan_1.getSelectionModel().getSelectedIndex() == 0) {
                EZSystem.showAllert(alert, "Mohon pilih satuan 1!", EZIcon.ICON_STAGE);
                return false;
            }

            if (txt_hpp_1.getText().equals("0")) {
                EZSystem.showAllert(alert, "Mohon isi hpp satuan 1!", EZIcon.ICON_STAGE);
                txt_hpp_1.requestFocus();
                return false;
            }

            if (txt_harga_jual_1.getText().equals("0")) {
                EZSystem.showAllert(alert, "Mohon isi harga satuan 1!", EZIcon.ICON_STAGE);
                txt_harga_jual_1.requestFocus();
                return false;
            }

            if (validateSkuBarcode(Item_Detail.BAARCODE, txt_barcode_1.getText())) {
                EZSystem.showAllert(alert, "Kode Barcode " + txt_barcode_1.getText() + " Sudah digunakan", EZIcon.ICON_STAGE);
                return false;
            }

            if (validateSkuBarcode(Item_Detail.KODE, txt_sku_1.getText())) {
                EZSystem.showAllert(alert, "SKU " + txt_sku_1.getText() + " Sudah digunakan", EZIcon.ICON_STAGE);
                return false;
            }

            if (validateHargaJual_1()) {
                EZSystem.showAllert(alert, "Harga Jual 1 tidak boleh lebih kecil dari harga beli 1", EZIcon.ICON_STAGE);
                return false;
            }
        }

        if (!txt_hpp_2.getText().equals("0")) {
            if ((txt_sku_2.getText().length() == 0 || txt_barcode_2.getText().length() == 0)) {
                EZSystem.showAllert(alert, "Mohon isi Sku/Barcode satuan 2!", EZIcon.ICON_STAGE);
                txt_sku_2.requestFocus();
                return false;
            }
        } else {
            if ((txt_sku_2.getText().length() > 0 || txt_barcode_2.getText().length() > 0)) {
                if (cmb_satuan_2.getSelectionModel().getSelectedIndex() == 0) {
                    EZSystem.showAllert(alert, "Mohon pilih satuan 2!", EZIcon.ICON_STAGE);
                    return false;
                }

                if (txt_konversi_2.getText().equals("0")) {
                    EZSystem.showAllert(alert, "Mohon isi konversi satuan 2!", EZIcon.ICON_STAGE);
                    txt_konversi_2.requestFocus();
                    return false;
                }

                if (txt_hpp_2.getText().equals("0")) {
                    EZSystem.showAllert(alert, "Mohon isi hpp satuan 2!", EZIcon.ICON_STAGE);
                    txt_hpp_2.requestFocus();
                    return false;
                }

                if (txt_harga_jual_2.getText().equals("0")) {
                    EZSystem.showAllert(alert, "Mohon isi harga satuan 2!", EZIcon.ICON_STAGE);
                    txt_harga_jual_2.requestFocus();
                    return false;
                }

                if (validateSkuBarcode(Item_Detail.BAARCODE, txt_barcode_2.getText())) {
                    EZSystem.showAllert(alert, "Kode Barcode " + txt_barcode_2.getText() + " Sudah digunakan", EZIcon.ICON_STAGE);
                    return false;
                }

                if (validateSkuBarcode(Item_Detail.KODE, txt_sku_2.getText())) {
                    EZSystem.showAllert(alert, "SKU " + txt_sku_2.getText() + " Sudah digunakan", EZIcon.ICON_STAGE);
                    return false;
                }

                if (validateHargaJual_2()) {
                    EZSystem.showAllert(alert, "Harga Jual 2 tidak boleh lebih kecil dari harga beli 2", EZIcon.ICON_STAGE);
                    return false;
                }
            }
        }

        if (!txt_hpp_3.getText().equals("0")) {
            if ((txt_sku_3.getText().length() == 0 || txt_barcode_3.getText().length() == 0)) {
                EZSystem.showAllert(alert, "Mohon isi Sku/Barcode satuan 3!", EZIcon.ICON_STAGE);
                txt_sku_2.requestFocus();
                return false;
            }
        } else {
            if ((txt_sku_3.getText().length() > 0 || txt_barcode_3.getText().length() > 0)) {
                if (cmb_satuan_3.getSelectionModel().getSelectedIndex() == 0) {
                    EZSystem.showAllert(alert, "Mohon pilih satuan 3!", EZIcon.ICON_STAGE);
                    return false;
                }

                if (txt_konversi_3.getText().equals("0")) {
                    EZSystem.showAllert(alert, "Mohon isi konversi satuan 2!", EZIcon.ICON_STAGE);
                    txt_konversi_3.requestFocus();
                    return false;
                }

                if (txt_hpp_3.getText().equals("0")) {
                    EZSystem.showAllert(alert, "Mohon isi hpp satuan 2!", EZIcon.ICON_STAGE);
                    txt_hpp_3.requestFocus();
                    return false;
                }

                if (txt_harga_jual_3.getText().equals("0")) {
                    EZSystem.showAllert(alert, "Mohon isi harga satuan 2!", EZIcon.ICON_STAGE);
                    txt_harga_jual_3.requestFocus();
                    return false;
                }

                if (validateSkuBarcode(Item_Detail.BAARCODE, txt_barcode_3.getText())) {
                    EZSystem.showAllert(alert, "Kode Barcode " + txt_barcode_3.getText() + " Sudah digunakan", EZIcon.ICON_STAGE);
                    return false;
                }

                if (validateSkuBarcode(Item_Detail.KODE, txt_sku_3.getText())) {
                    EZSystem.showAllert(alert, "SKU " + txt_sku_3.getText() + " Sudah digunakan", EZIcon.ICON_STAGE);
                    return false;
                }

                if (validateHargaJual_3()) {
                    EZSystem.showAllert(alert, "Harga Jual 3 tidak boleh lebih kecil dari harga beli 3", EZIcon.ICON_STAGE);
                    return false;
                }
            }
        }

        if (!txt_hpp_3.getText().equals("0")) {
            if ((txt_sku_3.getText().length() == 0 || txt_barcode_3.getText().length() == 0)) {
                EZSystem.showAllert(alert, "Mohon isi Sku/Barcode satuan 3!", EZIcon.ICON_STAGE);
                txt_sku_2.requestFocus();
                return false;
            }
        } else {
            if ((txt_sku_4.getText().length() > 0 || txt_barcode_4.getText().length() > 0)) {
                if (cmb_satuan_4.getSelectionModel().getSelectedIndex() == 0) {
                    EZSystem.showAllert(alert, "Mohon pilih satuan 4!", EZIcon.ICON_STAGE);
                    return false;
                }

                if (txt_konversi_4.getText().equals("0")) {
                    EZSystem.showAllert(alert, "Mohon isi konversi satuan 4!", EZIcon.ICON_STAGE);
                    txt_konversi_4.requestFocus();
                    return false;
                }

                if (txt_hpp_4.getText().equals("0")) {
                    EZSystem.showAllert(alert, "Mohon isi hpp satuan 4!", EZIcon.ICON_STAGE);
                    txt_hpp_4.requestFocus();
                    return false;
                }

                if (txt_harga_jual_4.getText().equals("0")) {
                    EZSystem.showAllert(alert, "Mohon isi harga satuan 4!", EZIcon.ICON_STAGE);
                    txt_harga_jual_4.requestFocus();
                    return false;
                }

                if (validateSkuBarcode(Item_Detail.BAARCODE, txt_barcode_4.getText())) {
                    EZSystem.showAllert(alert, "Kode Barcode " + txt_barcode_4.getText() + " Sudah digunakan", EZIcon.ICON_STAGE);
                    return false;
                }

                if (validateSkuBarcode(Item_Detail.KODE, txt_sku_4.getText())) {
                    EZSystem.showAllert(alert, "SKU " + txt_sku_4.getText() + " Sudah digunakan", EZIcon.ICON_STAGE);
                    return false;
                }

                if (validateHargaJual_4()) {
                    EZSystem.showAllert(alert, "Harga Jual 4 tidak boleh lebih kecil dari harga beli 4", EZIcon.ICON_STAGE);
                    return false;
                }
            }
        }

        return isValidForm;
    }

    private void simpan() throws SQLException {
        RecordEntry tItemEntry = new RecordEntry();
        tItemEntry.createEntry(Item.ITEMCODE.get(), txt_kode_item.getText());
        tItemEntry.createEntry(Item.ITEMNAME.get(), txt_nama_item.getText());
        tItemEntry.createEntry(Item.IDKATEGORI.get(), MAP_KATEGORI.get(cmb_kategori.getSelectionModel().getSelectedItem()));
        tItemEntry.createEntry(Item.IDSUBKATEGORI.get(), MAP_SUB_KATEGORI.get(cmb_subkategori.getSelectionModel().getSelectedItem()));
        tItemEntry.createEntry(Item.IDSUPPLIER.get(), MAP_SUPPLIER.get(cmb_supplier.getSelectionModel().getSelectedItem()));
        tItemEntry.createEntry(Item.KET.get(), txt_nama_lain.getText().length() > 0 ? txt_nama_lain.getText() : "-");
        int val;
        if(isEdit){
            tItemEntry.createEntry(Item.UDT.get(), EZDate.SQLDATETIME.today());
            val = updateToDatabase(tItemEntry, Item.TABLENAME.get(), Item.ITEMCODE.get()+"='"+mKodeEdit+"'");
        }else{
            tItemEntry.createEntry(Item.CDT.get(), EZDate.SQLDATETIME.today());
            val = insertToDatabase(tItemEntry, Item.TABLENAME.get());
        }

        boolean isSuccess = false;
        if (val == 1) {
            RecordEntry tItemDetail_1 = new RecordEntry();
            if (!isEdit) {
                tItemDetail_1.createEntry(Item_Detail.ID.get(), genereateID(10));
            }
            tItemDetail_1.createEntry(Item_Detail.BAARCODE.get(), txt_barcode_1.getText());
            tItemDetail_1.createEntry(Item_Detail.KODE.get(), txt_sku_1.getText());
            if (!isEdit) {
                tItemDetail_1.createEntry(Item_Detail.KODEITEM.get(), txt_kode_item.getText());
            }
            tItemDetail_1.createEntry(Item_Detail.HPP.get(), txt_hpp_1.getText().replace(",", ""));
            tItemDetail_1.createEntry(Item_Detail.PPN.get(), txt_ppn_1.getText());
            tItemDetail_1.createEntry(Item_Detail.DISC.get(), txt_disc_1.getText());
            tItemDetail_1.createEntry(Item_Detail.HARGABELI.get(), txt_harga_beli_1.getText().replace(",", ""));
            tItemDetail_1.createEntry(Item_Detail.MARGIN.get(), txt_margin_1.getText().replace(",", ""));
            tItemDetail_1.createEntry(Item_Detail.MARKUP.get(), txt_markup_1.getText().replace(",", ""));
            tItemDetail_1.createEntry(Item_Detail.HARGAJUAL.get(), txt_harga_jual_1.getText().replace(",", ""));
            tItemDetail_1.createEntry(Item_Detail.IDSATUAN.get(), MAP_SAT.get(cmb_satuan_1.getSelectionModel().getSelectedItem()));
            tItemDetail_1.createEntry(Item_Detail.KE.get(), "1");
            tItemDetail_1.createEntry(Item_Detail.MINIMAL.get(), txt_stok_minimal_1.getText().equalsIgnoreCase("") ? "0" : txt_stok_minimal_1.getText());
            int x;
            if(isEdit){
                tItemDetail_1.createEntry(Item_Detail.FLAG.get(), check_active_1.isSelected() ? "1" : "0");
                x = updateToDatabase(tItemDetail_1, Item_Detail.TABLENAME.get(), Item_Detail.ID.get()+"='"+mListidItem.get(0)+"'");
            }else{
                x = insertToDatabase(tItemDetail_1, Item_Detail.TABLENAME.get());
            }
            isSuccess = (x == 1);

            if (isSuccess && txt_barcode_2.getText().length() > 0) {
                tItemDetail_1 = new RecordEntry();
                if(!isEdit || mListidItem.size()<2){
                    tItemDetail_1.createEntry(Item_Detail.ID.get(), genereateID(10));
                }
                tItemDetail_1.createEntry(Item_Detail.BAARCODE.get(), txt_barcode_2.getText());
                tItemDetail_1.createEntry(Item_Detail.KODE.get(), txt_sku_2.getText());
                if(!isEdit || mListidItem.size()<2){
                    tItemDetail_1.createEntry(Item_Detail.KODEITEM.get(), txt_kode_item.getText());
                }
                tItemDetail_1.createEntry(Item_Detail.HPP.get(), txt_hpp_2.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.PPN.get(), txt_ppn_2.getText());
                tItemDetail_1.createEntry(Item_Detail.DISC.get(), txt_disc_2.getText());
                tItemDetail_1.createEntry(Item_Detail.HARGABELI.get(), txt_harga_beli_2.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.MARGIN.get(), txt_margin_2.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.MARKUP.get(), txt_markup_2.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.HARGAJUAL.get(), txt_harga_jual_2.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.IDSATUAN.get(), MAP_SAT.get(cmb_satuan_2.getSelectionModel().getSelectedItem()));
                tItemDetail_1.createEntry(Item_Detail.KE.get(), "2");
                tItemDetail_1.createEntry(Item_Detail.MINIMAL.get(), txt_stok_minimal_2.getText().equalsIgnoreCase("") ? "0" : txt_stok_minimal_2.getText());
                if(isEdit  && mListidItem.size()>=2){
                    tItemDetail_1.createEntry(Item_Detail.FLAG.get(), check_active_2.isSelected() ? "1" : "0");
                    x = updateToDatabase(tItemDetail_1, Item_Detail.TABLENAME.get(), Item_Detail.ID.get()+"='"+mListidItem.get(1)+"'");
                }else{
                    x = insertToDatabase(tItemDetail_1, Item_Detail.TABLENAME.get());
                }
                isSuccess = (x == 1);
            }

            if (isSuccess && txt_barcode_3.getText().length() > 0) {
                tItemDetail_1 = new RecordEntry();
                if(!isEdit  || mListidItem.size()<3){
                    tItemDetail_1.createEntry(Item_Detail.ID.get(), genereateID(10));
                }
                tItemDetail_1.createEntry(Item_Detail.BAARCODE.get(), txt_barcode_3.getText());
                tItemDetail_1.createEntry(Item_Detail.KODE.get(), txt_sku_3.getText());
                if(!isEdit  || mListidItem.size()<3){
                    tItemDetail_1.createEntry(Item_Detail.KODEITEM.get(), txt_kode_item.getText());
                }
                tItemDetail_1.createEntry(Item_Detail.HPP.get(), txt_hpp_3.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.PPN.get(), txt_ppn_3.getText());
                tItemDetail_1.createEntry(Item_Detail.DISC.get(), txt_disc_3.getText());
                tItemDetail_1.createEntry(Item_Detail.HARGABELI.get(), txt_harga_beli_3.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.MARGIN.get(), txt_margin_3.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.MARKUP.get(), txt_markup_3.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.HARGAJUAL.get(), txt_harga_jual_3.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.IDSATUAN.get(), MAP_SAT.get(cmb_satuan_3.getSelectionModel().getSelectedItem()));
                tItemDetail_1.createEntry(Item_Detail.KE.get(), "3");
                tItemDetail_1.createEntry(Item_Detail.MINIMAL.get(), txt_stok_minimal_3.getText().equalsIgnoreCase("") ? "0" : txt_stok_minimal_3.getText());
                if(isEdit  && mListidItem.size()>=3){
                    tItemDetail_1.createEntry(Item_Detail.FLAG.get(), check_active_1.isSelected() ? "1" : "0");
                    x = updateToDatabase(tItemDetail_1, Item_Detail.TABLENAME.get(), Item_Detail.ID.get()+"='"+mListidItem.get(2)+"'");
                }else{
                    x = insertToDatabase(tItemDetail_1, Item_Detail.TABLENAME.get());
                }
                isSuccess = (x == 1);
            }
            
            if (isSuccess && txt_barcode_4.getText().length() > 0) {
                tItemDetail_1 = new RecordEntry();
                if(!isEdit  || mListidItem.size()<4){
                    tItemDetail_1.createEntry(Item_Detail.ID.get(), genereateID(10));
                }
                tItemDetail_1.createEntry(Item_Detail.BAARCODE.get(), txt_barcode_4.getText());
                tItemDetail_1.createEntry(Item_Detail.KODE.get(), txt_sku_4.getText());
                if(!isEdit  || mListidItem.size()<4){
                    tItemDetail_1.createEntry(Item_Detail.KODEITEM.get(), txt_kode_item.getText());
                }
                tItemDetail_1.createEntry(Item_Detail.HPP.get(), txt_hpp_4.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.PPN.get(), txt_ppn_4.getText());
                tItemDetail_1.createEntry(Item_Detail.DISC.get(), txt_disc_4.getText());
                tItemDetail_1.createEntry(Item_Detail.HARGABELI.get(), txt_harga_beli_4.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.MARGIN.get(), txt_margin_4.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.MARKUP.get(), txt_markup_4.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.HARGAJUAL.get(), txt_harga_jual_4.getText().replace(",", ""));
                tItemDetail_1.createEntry(Item_Detail.IDSATUAN.get(), MAP_SAT.get(cmb_satuan_4.getSelectionModel().getSelectedItem()));
                tItemDetail_1.createEntry(Item_Detail.KE.get(), "4");
                tItemDetail_1.createEntry(Item_Detail.MINIMAL.get(), txt_stok_minimal_4.getText().equalsIgnoreCase("") ? "0" : txt_stok_minimal_4.getText());
                if(isEdit  && mListidItem.size()==4){
                    tItemDetail_1.createEntry(Item_Detail.FLAG.get(), check_active_1.isSelected() ? "1" : "0");
                    x = updateToDatabase(tItemDetail_1, Item_Detail.TABLENAME.get(), Item_Detail.ID.get()+"='"+mListidItem.get(3)+"'");
                }else{
                    x = insertToDatabase(tItemDetail_1, Item_Detail.TABLENAME.get());
                }
                isSuccess = (x == 1);
            }
        }

        if (val == 1 && isSuccess) {
            EZSystem.showAllert(EZAlertType.INFO, "Data item berhasil disimpan", EZIcon.ICON_STAGE);
            if(isEdit){
                mKodeEdit=null;
                setIsEdit(false);
                btn_kembali.fire();
            }
            initializeState();
        }else{
            EZSystem.showAllert(EZAlertType.ERROR, "Gagal menyimpan data item", EZIcon.ICON_STAGE);
            if(!isEdit){
                rollbackdatainsert(txt_kode_item.getText());
                initializeState();
            }else{
                btn_kembali.fire();
            }
            
        }
    }
    
    private void rollbackdatainsert(String kodeItem) throws SQLException{
        String tSQL = "DELETE FROM "+Item_Detail.TABLENAME.get()+" WHERE "+Item_Detail.KODEITEM.get()+"='"+kodeItem+"'";
        updateToDatabase(tSQL);
        tSQL = "DELETE FROM "+Item.TABLENAME.get()+" WHERE "+Item.ITEMCODE.get()+"='"+kodeItem+"'";
        updateToDatabase(tSQL);
    }

    private boolean validateSkuBarcode(Item_Detail item, String val) throws SQLException {
        String tSQL;
        if(isEdit){
            tSQL = "SELECT " + item.get() + " FROM " + Item_Detail.TABLENAME.get() + " WHERE " + item.get() + " ='" + val + "' AND "+Item_Detail.KODEITEM.get()+" !='"+mKodeEdit+"'";
        }else{
            tSQL = "SELECT " + item.get() + " FROM " + Item_Detail.TABLENAME.get() + " WHERE " + item.get() + " ='" + val + "'";
        }
        return selectFromDatabase(tSQL).next();
    }

    private boolean validateHargaJual_1() {
        double tHargaBeli = Double.parseDouble(txt_harga_beli_1.getText().replace(",", "").length() > 0 ? txt_harga_beli_1.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_jual_1.getText().replace(",", "").length() > 0 ? txt_harga_jual_1.getText().replace(",", "") : "0");
        return (tHargaJual < tHargaBeli);
    }

    private boolean validateHargaJual_2() {
        double tHargaBeli = Double.parseDouble(txt_harga_beli_2.getText().replace(",", "").length() > 0 ? txt_harga_beli_2.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_jual_2.getText().replace(",", "").length() > 0 ? txt_harga_jual_2.getText().replace(",", "") : "0");
        return (tHargaJual < tHargaBeli);
    }

    private boolean validateHargaJual_3() {
        double tHargaBeli = Double.parseDouble(txt_harga_beli_3.getText().replace(",", "").length() > 0 ? txt_harga_beli_3.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_jual_3.getText().replace(",", "").length() > 0 ? txt_harga_jual_3.getText().replace(",", "") : "0");
        return (tHargaJual < tHargaBeli);
    }

    private boolean validateHargaJual_4() {
        double tHargaBeli = Double.parseDouble(txt_harga_beli_4.getText().replace(",", "").length() > 0 ? txt_harga_beli_4.getText().replace(",", "") : "0");
        double tHargaJual = Double.parseDouble(txt_harga_jual_4.getText().replace(",", "").length() > 0 ? txt_harga_jual_4.getText().replace(",", "") : "0");
        return (tHargaJual < tHargaBeli);
    }

    @Override
    public void loggingerror(Exception ex) {
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }
    
    private String generateNickName(){
        String tTemp = txt_nama_item.getText();
        String[] tList = tTemp.split(" ");
        String val = "";
        if(tList.length > 0){
            if(tList.length > 1){
                for (int i = 0; i < tList.length; i++) {
                    if (i > 0) {
                        String name = tList[i];
                        val = val.concat(name.length() > 3 ? name.substring(0, 3)+" " : name+" " );
                    }else{
                        val = val.concat(tList[i]+" ");
                    }
                    
                    if(val.length() > 15){
                        val = val.substring(0, 15)+" ";
                    }
                }
            }else{
                val = tList[0]+" ";
            }
            
            if (val.length() > 15) {
                val = val.substring(0, 15);
            }
        }
        return val;
    }
}
