/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import static app.retail.controller.general.General.getStackTraceString;
import app.retail.model.pos.POSModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Sale_Hold;
import app.retail.utility.table.Sale_Hold_Detail;
import app.retail.utility.table.V_All_Item;
import app.retail.utility.table.V_Sale_Hold_Detail;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class POSController extends AbstractPOS implements Initializable {

    private static boolean isDiscItem;
    private final ObservableList<POSModel> itemList = FXCollections.observableArrayList();
    private static Timer mTimer;
    private static Map<String, Double> mMapDiscount;

    @FXML
    private Text lbl_kasir;
    @FXML
    private Text lbl_tanggal;
    @FXML
    private TableView<POSModel> tbl_trans;
    @FXML
    private JFXButton btn_close;
    @FXML
    private JFXButton btn_hold;
    @FXML
    private JFXButton btn_load;
    @FXML
    private JFXButton btn_cust;
    @FXML
    private JFXButton btn_refund;
    @FXML
    private Text lbl_total_harga;
    @FXML
    private JFXButton btn_bayar;
    @FXML
    private TableColumn<POSModel, String> clm_kode_item;
    @FXML
    private TableColumn<POSModel, String> clm_nama_item;
    @FXML
    private TableColumn<POSModel, String> clm_sat;
    @FXML
    private TableColumn<POSModel, String> clm_harga;
    @FXML
    private TableColumn<POSModel, String> clm_qty;
    @FXML
    private TableColumn<POSModel, String> clm_disc;
    @FXML
    private TableColumn<POSModel, String> clm_total;
    @FXML
    private Text lbl_total_item;
    @FXML
    private Text lbl_ttl_trans;
    @FXML
    private TextField txt_cari;
    @FXML
    private TableColumn<POSModel, JFXButton> clm_min;
    @FXML
    private TableColumn<POSModel, JFXButton> clm_plus;
    @FXML
    private TableColumn<POSModel, JFXButton> clm_delete;
    @FXML
    private Text lbl_kd_customer;
    @FXML
    private Text lbl_nama_customer;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private JFXButton btn_cari;
    @FXML
    private AnchorPane anchorePane;
    @FXML
    private Text lbl_prfile;
    @FXML
    private Text lbl_shift;
    @FXML
    private JFXButton btn_disc_item;
    @FXML
    private JFXButton btn_disc_all;
    @FXML
    private Text lbl_subtotal;
    @FXML
    private Text lbl_nama_custoer;
    @FXML
    private Text lbl_disc_all;
    @FXML
    private JFXButton btn_change_qty;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDateTimeToday();
        runningText();
        initializeState();
        setToolTip();
        setButtonListener();
    }

    public POSController() {
        setmPosController(this);
    }

    private void loadProfile() {
        Map<String, Object> tMap = mMapProfile;
        String tProfileText = tMap.get("nama").toString() + " ";
        tProfileText += tMap.get("alamat").toString() + ",";
        tProfileText += tMap.get("kota").toString() + " ";
        tProfileText += tMap.get("kontak").toString();
        lbl_prfile.setText(tProfileText);

        lbl_kasir.setText(mMapKaryawan.get("nama"));
        lbl_shift.setText(mMapKaryawan.get("shift"));
    }

    private void runningText() {
        loadProfile();
        double sceneWidth = getWidth();
        double msgWidth = lbl_prfile.getLayoutBounds().getWidth();

        KeyValue initKeyValue = new KeyValue(lbl_prfile.translateXProperty(), sceneWidth);
        KeyFrame initFrame = new KeyFrame(javafx.util.Duration.ONE, initKeyValue);

        KeyValue endKeyValue = new KeyValue(lbl_prfile.translateXProperty(), -1.0 * msgWidth);
        KeyFrame endFrame = new KeyFrame(javafx.util.Duration.seconds(20), endKeyValue);

        Timeline timeline = new Timeline(initFrame, endFrame);

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setDateTimeToday() {
        ActionListener taskPerformer = (java.awt.event.ActionEvent evt) -> {
            String tDate = EZDate.FORMAT_6.today();
            lbl_tanggal.setText(tDate);

        };
        mTimer = new javax.swing.Timer(1000, taskPerformer);
        mTimer.start();
    }

    @Override
    public void clear() {
        initializeState();
    }

    private void btnClosePerfomed() {
        Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Keluar dari transaksi ?", EZIcon.ICON_STAGE);
        if (opt.get() == ButtonType.OK) {
            if (tbl_trans.getItems().isEmpty()) {
                mTimer.stop();
                Stage stage = (Stage) btn_close.getScene().getWindow();
                getmPosMaster().initializeState();
                stage.close();
            } else {
                EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Selesaikan transaksi terlebih dahulu!", EZIcon.ICON_STAGE);
            }
        }

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
            stage.initStyle(StageStyle.UNDECORATED);
            stage.getIcons().add(new Image("/app/retail/images/icons8_cash_register_48px.png"));
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            loggingerror(ex);
        }
    }

    private void hitungTotal() {
        txt_cari.setText("");
        double totalHarga = Double.parseDouble(hitungGrandTotal(tbl_trans, 8).replace(",", ""));
        String tDiscType = "Rp";
        double tDiscValue = 0;

        for (String key : mMapDiscount.keySet()) {
            tDiscType = key;
            tDiscValue = mMapDiscount.get(key);
        }

        double tGrandTotal;

        if (tDiscType.equalsIgnoreCase("Rp")) {
            tGrandTotal = totalHarga - tDiscValue;
        } else {
            tGrandTotal = totalHarga - ((totalHarga * tDiscValue) / 100);
        }

        String tDisc = tDiscType.equalsIgnoreCase("Rp") ? "Rp. " + formatRupiah(tDiscValue) : formatRupiah(tDiscValue) + " %";
        lbl_disc_all.setText(tDisc);
        lbl_total_harga.setText("Rp. " + formatRupiah(tGrandTotal));
        lbl_subtotal.setText("Rp. " + formatRupiah(totalHarga));
        lbl_ttl_trans.setText(hitungGrandTotal(tbl_trans, 5));
        lbl_total_item.setText("" + tbl_trans.getItems().size());
        txt_cari.setText("");
        txt_cari.requestFocus();
    }

    public void setPelanggan(String id, String nama, String piutang) {
        lbl_kd_customer.setText(id);
        lbl_nama_customer.setText(nama);
        mMapPelanggan = new HashMap<>();
        mMapPelanggan.put("kd_pelanggan", id);
        mMapPelanggan.put("nm_pelanggan", nama);
        mMapPelanggan.put("piutang", piutang);
        txt_cari.requestFocus();
    }

    public void setDiscItem(int index, double disc) {
        POSModel model = itemList.get(index);
        model.setDISC(formatRupiah(disc));
        double harga = Double.parseDouble(model.getHARGAJUAL().replace(",", ""));
        int qty = Integer.parseInt(model.getQTY());
        double discPersen = Double.parseDouble(model.getDISC().replace(",", ""));
        double hargatemp = harga - (harga * discPersen / 100);
        double total = qty * hargatemp;
        model.setTOTAL(formatRupiah(total));
        itemList.set(index, model);
        loadDataToTable();
        isDiscItem = false;
    }

    @Override
    public void initializeState() {
        tbl_trans.getItems().clear();
        setPelanggan("0000", "N/A", "0");
        setDiscount("rp", 0);
        isDiscItem = false;
        itemList.clear();

    }

    @Override
    public void setButtonListener() {
        Platform.runLater(() -> {
            txt_cari.requestFocus();
        });

        btn_close.setOnAction((event) -> {
            btnClosePerfomed();
        });

        txt_cari.setOnAction((ActionEvent event) -> {
            if (txt_cari.getText().length() > 0) {
                loadData();
            } else {
                EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Masukan SKU/BARCODE !", EZIcon.ICON_STAGE);
                txt_cari.requestFocus();
                txt_cari.setText("");
            }
        });

        btn_batal.setOnAction((ActionEvent event) -> {
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Anda ingin membatalkan transaksi ?", EZIcon.ICON_STAGE);
            if (opt.get() == ButtonType.OK) {
                initializeState();
            }
        });

        btn_bayar.setOnAction((ActionEvent event) -> {
            if (tbl_trans.getItems().size() > 0) {
                FormBayarController.setTagihan(lbl_subtotal.getText().replace("Rp. ", ""));
                FormBayarController.setTotal_tagihan(lbl_total_harga.getText().replace("Rp. ", ""));
                FormBayarController.setMapDiscount(mMapDiscount);
                FormBayarController.setmPOSControler(this);
                FormBayarController.setItemList(itemList);
                String url = "/app/retail/fxml/pos/FormBayar.fxml";
                loadForm(url, this.getClass().getName(), event);
            }
        });

        btn_load.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.LOAD_TRANSAKSI, true)) {
                if (tbl_trans.getItems().isEmpty()) {
                    FormLoadPOSController.setmPOSController(this);
                    String url = "/app/retail/fxml/pos/FormLoadPOS.fxml";
                    loadForm(url, this.getClass().getName(), event);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    EZSystem.showAllert(alert, "Selesaikan Transaksi terlebih dahulu!", EZIcon.ICON_STAGE);
                }
            }
        });

        btn_cust.setOnAction((ActionEvent event) -> {
            FormPelangganController.setmPOSController(this);
            String url = "/app/retail/fxml/pos/FormPelanggan.fxml";
            loadForm(url, this.getClass().getName(), event);
        });

        btn_disc_all.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DISC_ALL, true)) {
                if (tbl_trans.getItems().size() > 0) {
                    FormDiscControler.setmPosController(this);
                    FormDiscControler.setIsDiscItem(false);
                    FormDiscControler.setTotalTagihan(Double.parseDouble(lbl_total_harga.getText().replace("Rp. ", "").replace(",", "")));
                    String url = "/app/retail/fxml/pos/FormDisc.fxml";
                    loadForm(url, this.getClass().getName(), event);
                } else {
                    txt_cari.requestFocus();
                }
            }
        });

        btn_cari.setOnAction((ActionEvent event) -> {
            FormCariItemController.setmPOSController(this);
            String url = "/app/retail/fxml/pos/FormCariItem.fxml";
            loadForm(url, this.getClass().getName(), event);
        });

        tbl_trans.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                if (!tbl_trans.getItems().isEmpty()) {
                    tbl_trans.getSelectionModel().select(0);
                }
            }
        });

        btn_hold.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.HOLD_TRANSAKSI, true)) {
                if (!tbl_trans.getItems().isEmpty()) {
                    Alert aler = new Alert(Alert.AlertType.CONFIRMATION);
                    Optional<ButtonType> opt = EZSystem.showAllert(aler, "Tunda Transaksi?", EZIcon.ICON_STAGE);
                    if (opt.get() == ButtonType.OK) {
                        try {
                            holdTransaksi();
                        } catch (SQLException ex) {
                            loggingerror(ex);
                        }
                    }
                }
            }
        });

        btn_disc_item.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.DISC_ITEM, true)) {
                isDiscItem = true;
                tbl_trans.requestFocus();
            }
        });

        tbl_trans.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (isDiscItem) {
                    if (tbl_trans.getItems().size() > 0) {
                        int index = tbl_trans.getSelectionModel().getSelectedIndex();
                        FormDiscControler.setmPosController(this);
                        FormDiscControler.setIsDiscItem(isDiscItem);
                        FormDiscControler.setIndex(index);
                        String url = "/app/retail/fxml/pos/FormDisc.fxml";
                        loadForm(url, this.getClass().getName(), null);
                    } else {
                        txt_cari.requestFocus();
                    }
                } else {
                    if (tbl_trans.getItems().size() > 0) {
                        int index = tbl_trans.getSelectionModel().getSelectedIndex();
                        FormQtyControler.setmPosController(this);
                        FormQtyControler.setQty(itemList.get(index).getQTY());
                        FormQtyControler.setIndex(index);
                        String url = "/app/retail/fxml/pos/FormQty.fxml";
                        loadForm(url, this.getClass().getName(), null);
                    } else {
                        txt_cari.requestFocus();
                    }
                }
            }
        });

        tbl_trans.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                if (isDiscItem) {
                    if (tbl_trans.getItems().size() > 0) {
                        int index = tbl_trans.getSelectionModel().getSelectedIndex();
                        FormDiscControler.setmPosController(this);
                        FormDiscControler.setIsDiscItem(isDiscItem);
                        FormDiscControler.setIndex(index);
                        String url = "/app/retail/fxml/pos/FormDisc.fxml";
                        loadForm(url, this.getClass().getName(), null);
                    } else {
                        txt_cari.requestFocus();
                    }
                } else {
                    if (tbl_trans.getItems().size() > 0) {
                        int index = tbl_trans.getSelectionModel().getSelectedIndex();
                        FormQtyControler.setmPosController(this);
                        FormQtyControler.setQty(itemList.get(index).getQTY());
                        FormQtyControler.setIndex(index);
                        String url = "/app/retail/fxml/pos/FormQty.fxml";
                        loadForm(url, this.getClass().getName(), null);
                    } else {
                        txt_cari.requestFocus();
                    }
                }
            }
        });

        btn_refund.setOnAction((ActionEvent) -> {
            if (isAllowed(Akses_List.REFUND, true)) {
                if (tbl_trans.getItems().isEmpty()) {
                    String url = "/app/retail/fxml/pos/FormRefund.fxml";
                    loadForm(url, this.getClass().getName(), null);
                } else {
                    EZSystem.showAllert(EZAlertType.WARNING, "Selesaikan Transaksi Terlebih Dahulu!", EZIcon.ICON_APPS);
                }
            }
        });

        btn_change_qty.setOnAction((ActionEvent event) -> {
            isDiscItem = false;
            tbl_trans.requestFocus();
        });

    }

    private void holdTransaksi() throws SQLException {
        RecordEntry tMap = new RecordEntry();
        String kodetrans = genereateID(10);
        tMap.createEntry(Sale_Hold.KODETRANS.get(), kodetrans);
        tMap.createEntry(Sale_Hold.TRANDATE.get(), EZDate.SQLDATETIME.today());
        tMap.createEntry(Sale_Hold.IDPELANGGAN.get(), mMapPelanggan.get("kd_pelanggan"));
        tMap.createEntry(Sale_Hold.IDKARYAWAN.get(), mMapKaryawan.get("kode"));
        tMap.createEntry(Sale_Hold.TOTAL.get(), hitungGrandTotal(tbl_trans, 8).replace(",", ""));
        tMap.createEntry(Sale_Hold.DISC.get(), "0");
        tMap.createEntry(Sale_Hold.DISCAMMOUNT.get(), "0");
        tMap.createEntry(Sale_Hold.GRANDTOTAL.get(), hitungGrandTotal(tbl_trans, 8).replace(",", ""));
        tMap.createEntry(Sale_Hold.SHIFT.get(), mMapKaryawan.get("kode_shift"));
        tMap.createEntry(Sale_Hold.SID.get(), getSession());
        insertToDatabase(tMap, Sale_Hold.TABLENAME.get());

        for (POSModel model : itemList) {
            tMap = new RecordEntry();
            tMap.createEntry(Sale_Hold_Detail.KODETRANS.get(), kodetrans);
            tMap.createEntry(Sale_Hold_Detail.KODEITEM.get(), model.getIDITEM());
            tMap.createEntry(Sale_Hold_Detail.HARGA.get(), model.getHARGAJUAL().replace(",", ""));
            tMap.createEntry(Sale_Hold_Detail.QTY.get(), model.getQTY());
            tMap.createEntry(Sale_Hold_Detail.DISC.get(), model.getDISC());
            tMap.createEntry(Sale_Hold_Detail.TOTAL.get(), model.getTOTAL().replace(",", ""));
            insertToDatabase(tMap, Sale_Hold_Detail.TABLENAME.get());
        }

        clear();
    }

    @Override
    public void setToolTip() {
        btn_bayar.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
        btn_disc_item.setCursor(Cursor.HAND);
        btn_disc_all.setCursor(Cursor.HAND);
        btn_cust.setCursor(Cursor.HAND);
        btn_hold.setCursor(Cursor.HAND);
        btn_load.setCursor(Cursor.HAND);
        btn_refund.setCursor(Cursor.HAND);
        btn_batal.setCursor(Cursor.HAND);
        btn_cari.setCursor(Cursor.HAND);
        setAligmentColoum(clm_harga, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_kode_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_qty, Pos.CENTER);
        setAligmentColoum(clm_disc, Pos.CENTER);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        clm_kode_item.setPrefWidth((15 * getWidth()) / 100);
        clm_nama_item.setPrefWidth((30 * getWidth()) / 100);
        clm_sat.setPrefWidth((3 * getWidth()) / 100);
        clm_min.setPrefWidth((3 * getWidth()) / 100);
        clm_plus.setPrefWidth((3 * getWidth()) / 100);
        clm_qty.setPrefWidth((5 * getWidth()) / 100);
        clm_disc.setPrefWidth((5 * getWidth()) / 100);
        clm_delete.setPrefWidth((9 * getWidth()) / 100);
        clm_harga.setPrefWidth((13 * getWidth()) / 100);
        clm_total.setPrefWidth((13 * getWidth()) / 100);
    }

    private void loadData() {
        try {
            String tSQL = "SELECT * FROM " + V_All_Item.TABLENAME.get() + " WHERE "
                    + V_All_Item.BARCODE.get() + "='" + txt_cari.getText() + "' OR "
                    + V_All_Item.KODE.get() + "='" + txt_cari.getText() + "' AND "
                    + V_All_Item.ACTIVE.get() + "=1";
            ResultSet tResult = selectFromDatabase(tSQL);
            int index = tbl_trans.getItems().size();
            if (tResult.next()) {
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
                JFXButton btnPlus = getButton(EZButtonType.BTN_PLUS, "");
                btnPlus.setId(String.valueOf(index));
                setButtonOnTableView(btnPlus, EZButtonType.BTN_PLUS);
                JFXButton btnMinus = getButton(EZButtonType.BTN_MINUS, "");
                btnMinus.setId(String.valueOf(index));
                setButtonOnTableView(btnMinus, EZButtonType.BTN_MINUS);
                JFXButton btnDelete = getButton(EZButtonType.BTN_DELETE, "Hapus");
                btnDelete.setId(String.valueOf(index));
                setButtonOnTableView(btnDelete, EZButtonType.BTN_DELETE);
                POSModel model = new POSModel(tKodeItem, tBarcode, tKode, tNamaItem, tSat, formatRupiah(harga), String.valueOf(tQty), "0", formatRupiah(total), tKet, btnPlus, btnMinus, btnDelete, null);
                itemList.add(model);
                loadDataToTable();
            } else {
                EZSystem.showAllert(new Alert(Alert.AlertType.WARNING), "Data Item Tidak Ditemukan!", EZIcon.ICON_APPS);
                txt_cari.requestFocus();
                txt_cari.setText("");
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    public void loadDataHold(String pKodeTrans) {
        try {
            String tSQL = "SELECT * FROM " + V_Sale_Hold_Detail.TABLENAME.get()
                    + " WHERE " + V_Sale_Hold_Detail.KODETRANS.get() + "='" + pKodeTrans + "'";
            ResultSet tResult = selectFromDatabase(tSQL);
            int index = tbl_trans.getItems().size();
            while (tResult.next()) {
                String tKodeItem = tResult.getString(V_Sale_Hold_Detail.IDITEM.get());
                String tBarcode = tResult.getString(V_Sale_Hold_Detail.BARCODE.get());
                String tKode = tResult.getString(V_Sale_Hold_Detail.KODE.get());
                String tSat = tResult.getString(V_Sale_Hold_Detail.SATUAN.get());
                String tNamaItem = tResult.getString(V_Sale_Hold_Detail.ITEMNAME.get());
                String tKet = tResult.getString(V_Sale_Hold_Detail.KET.get());
                int tQty = 1;
                JFXTextField txtQty = new JFXTextField();
                txtQty.setText(String.valueOf(tQty));
                double harga = tResult.getDouble(V_Sale_Hold_Detail.HARGA.get());
                double total = harga;
                JFXButton btnPlus = getButton(EZButtonType.BTN_PLUS, "");
                btnPlus.setId(String.valueOf(index));
                setButtonOnTableView(btnPlus, EZButtonType.BTN_PLUS);
                JFXButton btnMinus = getButton(EZButtonType.BTN_MINUS, "");
                btnMinus.setId(String.valueOf(index));
                setButtonOnTableView(btnMinus, EZButtonType.BTN_MINUS);
                JFXButton btnDelete = getButton(EZButtonType.BTN_DELETE, "Hapus");
                btnDelete.setId(String.valueOf(index));
                setButtonOnTableView(btnDelete, EZButtonType.BTN_DELETE);
                POSModel model = new POSModel(tKodeItem, tBarcode, tKode, tNamaItem, tSat, formatRupiah(harga), String.valueOf(tQty), "0", formatRupiah(total), tKet, btnPlus, btnMinus, btnDelete, null);
                itemList.add(model);
                loadDataToTable();
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    private void loadDataToTable() {
        setValueColum();
        tbl_trans.setItems(itemList);
        hitungTotal();
    }

    private void setValueColum() {
        clm_kode_item.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.IDITEM.get()));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.ITEMNAME.get()));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.SATUAN.get()));
        clm_harga.setCellValueFactory(new PropertyValueFactory<>(V_All_Item.HARGAJUAL.get()));
        clm_qty.setCellValueFactory(new PropertyValueFactory<>("QTY"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("TOTAL"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btn_delete"));
        clm_plus.setCellValueFactory(new PropertyValueFactory<>("btnplus"));
        clm_min.setCellValueFactory(new PropertyValueFactory<>("btnmin"));
        clm_disc.setCellValueFactory(new PropertyValueFactory<>("DISC"));
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            int tKey = Integer.parseInt(button.getId());
            POSModel tModel = itemList.get(tKey);
            if (type == EZButtonType.BTN_DELETE) {
                Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Hapus item ?", EZIcon.ICON_STAGE);
                if (option.get() == ButtonType.OK) {
                    itemList.remove(tKey);

                    int index = 0;
                    for (POSModel model : itemList) {
                        model.getBtn_delete().setId("" + index);
                        model.getBtnplus().setId("" + index);
                        model.getBtnmin().setId("" + index);
                        index++;
                    }

                    loadDataToTable();
                }
            } else {
                int tQty = Integer.parseInt(tModel.getQTY());
                if (type == EZButtonType.BTN_MINUS) {
                    if (tQty > 1) {
                        tQty = tQty - 1;
                    }
                }

                if (type == EZButtonType.BTN_PLUS) {
                    tQty = tQty + 1;
                }

                double tHarga = Double.parseDouble(tModel.getHARGAJUAL().replace(",", ""));
                double discPersen = Double.parseDouble(tModel.getDISC().replace(",", ""));
                double hargatemp = tHarga - (tHarga * discPersen / 100);
                double tTotal = hargatemp * tQty;
                tModel.setQTY(String.valueOf(tQty));
                tModel.setTOTAL(formatRupiah(tTotal));
                itemList.set(tKey, tModel);
                loadDataToTable();
            }
        });

    }

    @Override
    public void loggingerror(Exception ex) {
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }

    public void addItem(POSModel model) {
        int index = tbl_trans.getItems().size();
        JFXButton btnPlus = getButton(EZButtonType.BTN_PLUS, "");
        btnPlus.setId(String.valueOf(index));
        setButtonOnTableView(btnPlus, EZButtonType.BTN_PLUS);
        JFXButton btnMinus = getButton(EZButtonType.BTN_MINUS, "");
        btnMinus.setId(String.valueOf(index));
        setButtonOnTableView(btnMinus, EZButtonType.BTN_MINUS);
        JFXButton btnDelete = getButton(EZButtonType.BTN_DELETE, "Hapus");
        btnDelete.setId(String.valueOf(index));
        setButtonOnTableView(btnDelete, EZButtonType.BTN_DELETE);
        model.setBtnplus(btnPlus);
        model.setBtnmin(btnMinus);
        model.setBtn_delete(btnDelete);
        itemList.add(model);
        loadDataToTable();
        txt_cari.requestFocus();
    }

    public void setDiscount(String type, double value) {
        mMapDiscount = new HashMap<>();
        mMapDiscount.put(type, value);
        hitungTotal();
    }

    public void setQtyItem(int index, String pQty) {
        POSModel model = itemList.get(index);
        model.setQTY(pQty);
        double harga = Double.parseDouble(model.getHARGAJUAL().replace(",", ""));
        int qty = Integer.parseInt(model.getQTY());
        double discPersen = Double.parseDouble(model.getDISC().replace(",", ""));
        double hargatemp = harga - (harga * discPersen / 100);
        double total = qty * hargatemp;
        model.setTOTAL(formatRupiah(total));
        itemList.set(index, model);
        loadDataToTable();
        isDiscItem = false;
    }

    public void setShortcut(KeyCode key) {
        if (key == KeyCode.ESCAPE) {
            btn_close.fire();
        }

        if (key == KeyCode.F1) {
            btn_hold.fire();
        }

        if (key == KeyCode.F2) {
            btn_load.fire();
        }

        if (key == KeyCode.F3) {
            btn_cust.fire();
        }

        if (key == KeyCode.F4) {
            btn_cari.fire();
        }

        if (key == KeyCode.F5) {
            btn_refund.fire();
        }

        if (key == KeyCode.F6) {
            btn_change_qty.fire();
        }

        if (key == KeyCode.F7) {
            btn_disc_item.fire();
        }

        if (key == KeyCode.F8) {
            btn_disc_all.fire();
        }

        if (key == KeyCode.F9) {
            btn_batal.fire();
        }

        if (key == KeyCode.F10) {
            btn_bayar.fire();
        }
    }

}
