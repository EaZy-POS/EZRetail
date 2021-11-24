/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import static app.retail.controller.general.General.mMapKaryawan;
import static app.retail.controller.general.General.selectFromDatabase;
import app.retail.model.pos.POSModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Sale;
import app.retail.utility.table.Sale_Refund;
import app.retail.utility.table.Sale_Refund_Detail;
import app.retail.utility.table.V_Sale_Detail;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormRefunfController extends AbstractPOS implements Initializable {

    private final ObservableList<String> fakturList = FXCollections.observableArrayList();
    private final ObservableList<String> fakturList2 = FXCollections.observableArrayList();
    private final ObservableList<POSModel> itemList = FXCollections.observableArrayList();
    private static String mKodeTrans;

    @FXML
    private AnchorPane anchorePane;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private Text lbl_kode_item;
    @FXML
    private Text lbl_kode_item1;
    @FXML
    private JFXComboBox<String> cmb_faktur;
    @FXML
    private Text lbl_kode_item11;
    @FXML
    private JFXTextField txt_search;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private TableView<POSModel> tbl_trans;
    @FXML
    private JFXListView<String> list_faktur;
    @FXML
    private JFXDatePicker dt_trans;
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
    private TableColumn<POSModel, TextField> clm_qty_refund;
    @FXML
    private TableColumn<POSModel, String> clm_total;
    @FXML
    private TableColumn<POSModel, String> clm_disc;
    @FXML
    private TableColumn<POSModel, String> clm_sisa;
    @FXML
    private Text lbl_gran_total;
    @FXML
    private JFXCheckBox check_all;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        clm_total.setVisible(false);
        setButtonListener();
        setToolTip();
        initializeState();
    }

    @Override
    public void clear() {
        Stage stage = (Stage) btn_batal.getScene().getWindow();
        stage.close();
    }

    private String generateRefnum() throws SQLException {
        String tDate = EZDate.FAKTUR.today();
        String tInitial = "RF";
        String tKdAwal = tInitial + tDate;
        String tFaktur;

        String tSQL = "SELECT kode_trans FROM " + Sale_Refund.TABLENAME.get()
                + " WHERE " + Sale_Refund.KODETRANS.get() + " like '" + tKdAwal + "%' "
                + "ORDER BY " + Sale_Refund.KODETRANS.get() + " DESC LIMIT 1";

        ResultSet tRes = selectFromDatabase(tSQL);
        if (tRes.next()) {
            String tLastFaktur = tRes.getString(Sale_Refund.KODETRANS.get());
            int number = Integer.parseInt(tLastFaktur.substring(tKdAwal.length(), tLastFaktur.length())) + 1;
            String newNumber = String.valueOf(number);
            if (newNumber.length() > 5) {
                EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), "Faktur Overload", EZIcon.ICON_APPS);
                tFaktur = tKdAwal + "00000";
            } else {
                while (newNumber.length() < 5) {
                    newNumber = "0" + newNumber;
                }

                tFaktur = tKdAwal + newNumber;
            }
        } else {
            tFaktur = tKdAwal + "00001";
        }

        return tFaktur;
    }

    public void simpan() throws SQLException {
        RecordEntry tMapRefund = new RecordEntry();
        String kodetrans = generateRefnum();
        tMapRefund.createEntry(Sale_Refund.KODETRANS.get(), kodetrans);
        tMapRefund.createEntry(Sale_Refund.TRANDATE.get(), EZDate.SQLDATETIME.today());
        tMapRefund.createEntry(Sale_Refund.FAKTUR.get(), mKodeTrans);
        tMapRefund.createEntry(Sale_Refund.TOTAL.get(), lbl_gran_total.getText().replace("Rp. ", "").replace(",", ""));
        tMapRefund.createEntry(Sale_Refund.IDKARYAWAN.get(), mMapKaryawan.get("kode"));
        tMapRefund.createEntry(Sale_Refund.SHIFT.get(), mMapKaryawan.get("kode_shift"));
        tMapRefund.createEntry(Sale_Refund.SID.get(), getSession());
        insertToDatabase(tMapRefund, Sale_Refund.TABLENAME.get());
        simpanDetail(kodetrans);
    }

    private void simpanDetail(String kodeTrans) throws SQLException {
        for (POSModel model : tbl_trans.getItems()) {
            RecordEntry tMapRefundDetail = new RecordEntry();
            if (!model.getTxtQtyRefund().getText().equals("0")) {
                tMapRefundDetail.createEntry(Sale_Refund_Detail.KODETRANS.get(), kodeTrans);
                tMapRefundDetail.createEntry(Sale_Refund_Detail.KODEITEM.get(), model.getIDITEM());
                tMapRefundDetail.createEntry(Sale_Refund_Detail.HARGA.get(), model.getHARGAJUAL().replace(",", ""));
                tMapRefundDetail.createEntry(Sale_Refund_Detail.DISC.get(), model.getDISC());
                tMapRefundDetail.createEntry(Sale_Refund_Detail.TOTAL.get(), model.getTOTAL().replace(",", ""));
                tMapRefundDetail.createEntry(Sale_Refund_Detail.QTY.get(), model.getTxtQtyRefund().getText());
                tMapRefundDetail.createEntry(Sale_Refund_Detail.AUTOID.get(), model.getAUTOID());
                insertToDatabase(tMapRefundDetail, Sale_Refund_Detail.TABLENAME.get());
            }
        }

        EZSystem.showAllert(EZAlertType.INFO, "Data Berhasil Disimpan", EZIcon.ICON_APPS);
        initializeState();
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
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            loggingerror(ex);
        }
    }

    public void loadData() {
        try {
            cmb_faktur.getItems().clear();
            String tSQL = "SELECT " + Sale.KODETRANS.get()
                    + " FROM " + Sale.TABLENAME.get()
                    + " WHERE " + Sale.TRANDATE.get() + " BETWEEN '"
                    + getDate(dt_trans, EZDate.SQLDATE) +" 00:00:00' AND '"
                    + getDate(dt_trans, EZDate.SQLDATE) +" 23:59:59'";
            ResultSet res = selectFromDatabase(tSQL);
            fakturList.add("-- Pilih Faktur --");
            while (res.next()) {
                fakturList.add(res.getString("kode_trans"));
            }
            cmb_faktur.setItems(fakturList);
            cmb_faktur.getSelectionModel().select(0);
        } catch (SQLException ex) {
            loggingerror(ex);
        }

    }

    private void loadDataToTable(String pfaktur) {
        tbl_trans.getItems().clear();
        itemList.clear();
        try {
            String tSQL = "SELECT * FROM " + V_Sale_Detail.TABLENAME.get()
                    + " WHERE " + V_Sale_Detail.KODETRANS.get() + "='" + pfaktur + "'";
            ResultSet res = searchBy(tSQL);
            int no = 0;
            while (res.next()) {
                mKodeTrans = res.getString(V_Sale_Detail.KODETRANS.get());
                String kode = res.getString(V_Sale_Detail.IDITEM.get());
                String nama = res.getString(V_Sale_Detail.ITEMNAME.get());
                String sat = res.getString(V_Sale_Detail.SATUAN.get());
                String qty = res.getString(V_Sale_Detail.QTY.get());
                String harga = formatRupiah(res.getDouble(V_Sale_Detail.HARGA.get()));
                String disc = String.valueOf(res.getInt(V_Sale_Detail.DISC.get()));
                String total = formatRupiah(res.getDouble(V_Sale_Detail.TOTAL.get()));
                String sisa = res.getString(V_Sale_Detail.SISA.get());
                String autoID = res.getString(V_Sale_Detail.AUTOID.get());
                TextField txtQty = new TextField("0");
                txtQty.setAlignment(Pos.CENTER);
                txtQty.setId("" + no);
                setTxtQtyListener(txtQty);
                POSModel model = new POSModel(kode, nama, sat, harga, qty, disc, total, sisa, autoID,txtQty);
                itemList.add(model);
                no++;
            }
            if (list_faktur.isVisible()) {
                list_faktur.setVisible(false);
                list_faktur.getItems().clear();
            }
            setValueColum();
            tbl_trans.setItems(itemList);
            hitungTotal();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    private void setValueColum() {
        clm_kode_item.setCellValueFactory(new PropertyValueFactory<>("IDITEM"));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>("ITEMNAME"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("SATUAN"));
        clm_harga.setCellValueFactory(new PropertyValueFactory<>("HARGAJUAL"));
        clm_qty.setCellValueFactory(new PropertyValueFactory<>("QTY"));
        clm_sisa.setCellValueFactory(new PropertyValueFactory<>("SISA"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("TOTAL"));
        clm_qty_refund.setCellValueFactory(new PropertyValueFactory<>("txtQtyRefund"));
        clm_disc.setCellValueFactory(new PropertyValueFactory<>("DISC"));
    }
    
    private void hitungTotal(){
        double total=0;
        total = tbl_trans.getItems().stream().map((model) -> {
            double harga = Double.parseDouble(model.getHARGAJUAL().replace(",", ""));
            int qty = Integer.parseInt(model.getTxtQtyRefund().getText());
            int disc = Integer.parseInt(model.getDISC());
            double totalharga = (harga - (harga*disc/100)) * qty;
            model.setTOTAL(formatRupiah(totalharga));
            return totalharga;
        }).map((totalharga) -> totalharga).reduce(total, (accumulator, _item) -> accumulator + _item);
        lbl_gran_total.setText("Rp. "+formatRupiah(total));
    }

    private ResultSet searchBy(String tSQL) throws SQLException {
        ResultSet res = selectFromDatabase(tSQL);
        return res;
    }

    @Override
    public void setDate(JFXDatePicker pDatePicker) {
        pDatePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(EZDate.FORMAT_2.get());

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        }
        );
        pDatePicker.setValue(LocalDate.now());
    }

    private void setTxtQtyListener(TextField field) {
        setNumericTextfield(field);
        field.setOnKeyReleased((KeyEvent event) -> {
            if (field.getText().length() > 0) {
                if (event.getCode() != KeyCode.ENTER) {
                    int index = Integer.parseInt(field.getId());
                    POSModel model = tbl_trans.getItems().get(index);
                    int sisa = Integer.parseInt(model.getSISA());
                    int qty = Integer.parseInt(field.getText());
                    if (qty > sisa) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        EZSystem.showAllert(alert, "QTY Refund melebihi sisa penjualan!", EZIcon.ICON_APPS);
                        model.getTxtQtyRefund().setText("0");
                    } else {
                        hitungTotal();
                    }
                }
            }
        });
    }

    private void checkAll() {
        itemList.forEach(model -> {
            String sisa = model.getSISA();
            model.getTxtQtyRefund().setText(sisa);
        });
        hitungTotal();
    }

    @Override
    public void initializeState() {
        mKodeTrans = null;
        setDate(dt_trans);
        list_faktur.setVisible(false);
        lbl_gran_total.setText("Rp. 0");
        loadData();
        tbl_trans.getItems().clear();
    }

    @Override
    public void setButtonListener() {
        dt_trans.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            loadData();
        });

        cmb_faktur.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (cmb_faktur.getSelectionModel().getSelectedIndex() > 0) {
                String tFaktur = cmb_faktur.getSelectionModel().getSelectedItem();
                loadDataToTable(tFaktur);
            }
        });

        Platform.runLater(() -> {
            txt_search.requestFocus();
        });

        txt_search.setOnKeyReleased((KeyEvent event) -> {
            if (txt_search.getText().length() > 6) {
                list_faktur.getItems().clear();
                list_faktur.setVisible(true);
                try {
                    String faktur = txt_search.getText();
                    String tSQL = "SELECT " + Sale.KODETRANS.get()
                            + " FROM " + Sale.TABLENAME.get()
                            + " WHERE " + Sale.KODETRANS.get() + " LIKE '"
                            + faktur + "%' ORDER BY "
                            + Sale.KODETRANS.get() + " ASC";
                    ResultSet res = searchBy(tSQL);
                    while (res.next()) {
                        fakturList2.add(res.getString("kode_trans"));
                    }
                    list_faktur.setItems(fakturList2);
                } catch (SQLException ex) {
                    loggingerror(ex);
                }
            } else {
                list_faktur.getItems().clear();
                list_faktur.setVisible(false);
            }

        });

        txt_search.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN && list_faktur.isVisible()) {
                list_faktur.requestFocus();
            }
        });

        list_faktur.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                list_faktur.getSelectionModel().select(0);
            } else {
                list_faktur.setVisible(false);
            }
        });

        list_faktur.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                String tFaktur = list_faktur.getSelectionModel().getSelectedItem();
                loadDataToTable(tFaktur);
            }
        });

        list_faktur.setOnKeyPressed((KeyEvent) -> {
            if (KeyEvent.getCode() == KeyCode.ENTER) {
                String tFaktur = list_faktur.getSelectionModel().getSelectedItem();
                loadDataToTable(tFaktur);
            }
        });

        btn_batal.setOnMouseClicked((MouseEvent event) -> {
            Stage stage = (Stage) btn_batal.getScene().getWindow();
            stage.close();
        });

        btn_simpan.setOnAction((ActionEvent event) -> {
            if (mKodeTrans != null && tbl_trans.getItems().size() > 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Simpan data refund ?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    try {
                        simpan();
                    } catch (SQLException ex) {
                        loggingerror(ex);
                    }
                }
            }
        });

        check_all.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (check_all.isSelected()) {
                checkAll();
            }
        });
    }

    @Override
    public void setToolTip() {
        setAligmentColoum(clm_kode_item, Pos.CENTER);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_harga, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_qty, Pos.CENTER);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_disc, Pos.CENTER);
        btn_batal.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
    }
}
