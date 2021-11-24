/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.finance;

import app.retail.model.finance.CashTransactionModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Cash_Transaction;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class CashTransactionController extends AbstractFinance implements Initializable {

    private final ObservableList<CashTransactionModel> cashTransList = javafx.collections.FXCollections.observableArrayList();
    private final ObservableList<String> statusList = javafx.collections.FXCollections.observableArrayList("Tipe", "IN", "OUT");
    private final ObservableList<String> limitlist = javafx.collections.FXCollections.observableArrayList("100", "200", "300", "400", "500");
    private JFXButton btnview;
    private static String mQuery;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<CashTransactionModel> tbl_purchase;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_no;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_tanggl;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_total;
    @FXML
    private TableColumn<CashTransactionModel, JFXButton> clm_view;
    @FXML
    private JFXDatePicker dtPick_1;
    @FXML
    private JFXDatePicker dtPick_2;
    @FXML
    private JFXComboBox<String> cmb_status;
    @FXML
    private JFXComboBox<String> cmb_limit;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_ket;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_type;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_input_by;
    @FXML
    private JFXButton btn_tambah;
    @FXML
    private Button btn_print;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!isAllowed(Akses_List.DATA_CASH_IN_CASH_OUT)) {
            btn_tambah.setDisable(true);
        }

        if (!isAllowed(Akses_List.PRINT_DATA_CASH_IN_CASH_OUT)) {
            btn_print.setDisable(true);
        }

        setToolTip();
        setButtonListener();
        initializeState();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent) -> {
            setListParam();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> opt = EZSystem.showAllert(alert, "View data transaksi?", EZIcon.ICON_STAGE);
            if (opt.get() == ButtonType.OK) {
                try {
                    ViewCashTransactionController.setmKodeEdit(button.getId());
                    getmHomeController().loadForm("/app/retail/fxml/finance/ViewCashTransaction.fxml", getClass().getName(), ActionEvent);
                } catch (IOException ex) {
                    loggingerror(ex);
                }
            }
        });
    }

    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_tanggl.setCellValueFactory(new PropertyValueFactory<>(Cash_Transaction.TRANDATE.get()));
        clm_input_by.setCellValueFactory(new PropertyValueFactory<>(Cash_Transaction.INPUTBY.get()));
        clm_ket.setCellValueFactory(new PropertyValueFactory<>(Cash_Transaction.KET.get()));
        clm_total.setCellValueFactory(new PropertyValueFactory<>(Cash_Transaction.TOTAL.get()));
        clm_type.setCellValueFactory(new PropertyValueFactory<>(Cash_Transaction.TYPE.get()));
        clm_view.setCellValueFactory(new PropertyValueFactory<>("btnview"));
    }

    private String getQuery() {
        String tSQL = "SELECT * FROM v_cash_transaction WHERE tran_date BETWEEN '" + getDate(dtPick_1) + " 00:00:00' AND '" + getDate(dtPick_2) + " 23:59:59' ";
        mQuery = "SELECT V.id , V.type , v.total , V.ket,V.tran_date, V.nama ,D.faktur, D.ket as desk, D.total as nilai "
                + "FROM v_cash_transaction V INNER JOIN cash_transaction_detail D on D.id_trans=V.id "
                + "WHERE V.tran_date BETWEEN '" + getDate(dtPick_1) + " 00:00:00' AND '" + getDate(dtPick_2) + " 23:59:59' ";

        if (cmb_status.getSelectionModel().getSelectedIndex() > 0) {
            tSQL = tSQL.concat(" AND type='" + (cmb_status.getSelectionModel().getSelectedItem()) + "'");
            mQuery = mQuery.concat(" AND V.type='" + (cmb_status.getSelectionModel().getSelectedItem()) + "'");
        }

        if (cmb_limit.getSelectionModel().getSelectedIndex() > 0) {
            tSQL = tSQL.concat(" LIMIT " + cmb_limit.getSelectionModel().getSelectedItem());
        }
        mQuery = mQuery.concat(" ORDER BY V.tran_date,V.id ASC");
        return tSQL;
    }

    @Override
    public void initializeState() {
        cmb_status.setItems(statusList);
        cmb_limit.setItems(limitlist);
        cmb_status.getSelectionModel().select(0);
        cmb_limit.getSelectionModel().select(0);
        if (mListParameter != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(EZDate.SQLDATE.get());
                Date dateAwal = sdf.parse(mListParameter.get(mTglAwal));
                Date dateAkhir = sdf.parse(mListParameter.get(mTglAkhir));
                String tLimit = (String) mListParameter.get(mLimit);
                String tStatus = (String) mListParameter.get(mStatus);
                cmb_limit.getSelectionModel().select(tLimit);
                cmb_status.getSelectionModel().select(tStatus);
                setDate(dtPick_1, dateAwal);
                setDate(dtPick_2, dateAkhir);
                loadData();
            } catch (ParseException ex) {
                loggingerror(ex);
            }
        } else {
            loadData();
        }
    }

    private void setListParam() {
        DataHutangController.mListParameter = new HashMap<>();
        DataHutangController.mListParameter.put(mTglAwal, getDate(dtPick_1));
        DataHutangController.mListParameter.put(mTglAkhir, getDate(dtPick_2));
        DataHutangController.mListParameter.put(mLimit, cmb_limit.getSelectionModel().getSelectedItem());
        DataHutangController.mListParameter.put(mStatus, cmb_status.getSelectionModel().getSelectedItem());
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(() -> {
            tbl_purchase.requestFocus();
        });

        setDate(dtPick_1);
        setDate(dtPick_2);

        dtPick_1.valueProperty().addListener(
                (ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
                    loadData();
                });

        dtPick_2.valueProperty().addListener(
                (ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
                    loadData();
                });

        cmb_limit.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData();
        });

        cmb_status.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData();
        });

        btn_tambah.setOnAction((ActionEvent event) -> {
            try {
                final String tUrl = "/app/retail/fxml/finance/FormInputCashTransaction.fxml";
                getmHomeController().loadForm(tUrl, getClass().getName(), event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });

        btn_print.setOnAction((ActionEvent event) -> {
            Map tMap = mMapProfile;
            tMap.put("periode", getDate(dtPick_1, EZDate.FORMAT_5) + " s/d " + getDate(dtPick_2, EZDate.FORMAT_5));
            showReport("report/finance/cash/lap_cash_transaction.jrxml", mQuery, tMap);
        });
    }

    public void loadData() {
        if (isAllowed(Akses_List.DATA_CASH_IN_CASH_OUT)) {
            tbl_purchase.getItems().clear();
            try {
                String tSQL = getQuery();
                ResultSet tResultSet = selectFromDatabase(tSQL);
                int no = 0;
                while (tResultSet.next()) {
                    no++;
                    if (isAllowed(Akses_List.VIEW_CASH_IN_CASH_OUT)) {
                        btnview = getButton(EZButtonType.BTN_VIEW, "View");
                        btnview.setId(tResultSet.getString("id"));
                        setButtonOnTableView(btnview, EZButtonType.BTN_VIEW);
                    } else {
                        btnview = null;
                    }
                    double total = tResultSet.getDouble(Cash_Transaction.TOTAL.get());
                    Date trnDt = tResultSet.getDate(Cash_Transaction.TRANDATE.get());
                    CashTransactionModel tModel = new CashTransactionModel("" + no, EZDate.FORMAT_2.get(trnDt),
                            tResultSet.getString("type"), tResultSet.getString("input_by"),
                            formatRupiah(total), tResultSet.getString("ket"), tResultSet.getString("id"),
                            btnview
                    );
                    cashTransList.add(tModel);
                }
                setValueColum();
                tbl_purchase.setItems(cashTransList);
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        }
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 250);
        AnchorPane.setPrefHeight(getHeight() - 355);
        setAligmentColoum(clm_tanggl, Pos.CENTER_LEFT);
        setAligmentColoum(clm_input_by, Pos.CENTER_LEFT);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_type, Pos.CENTER);
        setAligmentColoum(clm_ket, Pos.CENTER_LEFT);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        btn_tambah.setCursor(Cursor.HAND);
        btn_print.setCursor(Cursor.HAND);
        double lebar = getWidth() - 200;
        clm_no.setPrefWidth((lebar * 3) / 100);
        clm_tanggl.setPrefWidth((lebar * 7) / 100);
        clm_type.setPrefWidth((lebar * 10) / 100);
        clm_total.setPrefWidth((lebar * 15) / 100);
        clm_input_by.setPrefWidth((lebar * 15) / 100);
        clm_ket.setPrefWidth((lebar * 35) / 100);
        clm_view.setPrefWidth((lebar * 11) / 100);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {

    }

}
