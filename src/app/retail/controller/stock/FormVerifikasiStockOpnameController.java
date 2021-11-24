/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import static app.retail.controller.general.General.getStackTraceString;
import app.retail.controller.general.MapKaryawan;
import app.retail.model.stock.StockOpnameModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Stock_Opname;
import app.retail.utility.table.Stock_Opname_Detail;
import app.retail.utility.table.V_Stock_Opname;
import app.retail.utility.table.V_Tmp_Stock_Opname;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormVerifikasiStockOpnameController extends AbstractStock implements Initializable {

    private static String mKodeTrans;
    private TextField txtQty;
    private JFXButton btndelete;
    ObservableList<StockOpnameModel> itemList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Text txt_kode_trans;
    @FXML
    private Text txt_input_by;
    @FXML
    private Text txt_input_date;
    @FXML
    private TableView<StockOpnameModel> tbl_trans;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_no;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_kode_item;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_nama_item;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_sat;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_stock_akhir;
    @FXML
    private TableColumn<StockOpnameModel, TextField> clm_opname;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_selisih;
    @FXML
    private TableColumn<StockOpnameModel, JFXButton> clm_delete;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_kode_barcode;
    @FXML
    private JFXButton btn_batal;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_kembali;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setToolTip();
        setButtonListener();
        initializeState();
    }

    public void simpan() throws SQLException {
        String tSQL = getQueryUpdate(3);
        updateToDatabase(tSQL);
    }

    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_kode_item.setCellValueFactory(new PropertyValueFactory<>("kodeItem"));
        clm_kode_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>("namaItem"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("sat"));
        clm_stock_akhir.setCellValueFactory(new PropertyValueFactory<>("stockakhir"));
        clm_opname.setCellValueFactory(new PropertyValueFactory<>("txtQty"));
        clm_selisih.setCellValueFactory(new PropertyValueFactory<>("selisih"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("btndelete"));
    }

    private void performUpdateStock() {
        tbl_trans.getItems().stream().map((model) -> {
            txtQty = model.getTxtQty();
            return model;
        }).map((model) -> {
            model.setQtyOpname(txtQty.getText());
            return model;
        }).forEach((model) -> {
            String kode = model.getKodeItem();
            String qty = model.getQtyOpname();
            String tSQL = "UPDATE " + Stock_Opname_Detail.TABLENAME.get() + " SET "
                    + Stock_Opname_Detail.OPNAME.get() + "='" + qty + "'," + Stock_Opname_Detail.SELISIH.get() + "="
                    + Stock_Opname_Detail.OPNAME.get() + "-" + Stock_Opname_Detail.STOCK.get() + " , " + Stock_Opname_Detail.FLAG.get() + "=2"
                    + " WHERE " + Stock_Opname_Detail.KODEITEM.get() + "='" + kode + "' AND "
                    + Stock_Opname_Detail.TRANSID.get() + "='" + mKodeTrans + "'";

            try {
                updateToDatabase(tSQL);
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });
    }

    public void setButtonOnTableView(JFXButton button, EZButtonType type) {
        button.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> opt = EZSystem.showAllert(alert, "Hapus item dari list opname ?", EZIcon.ICON_STAGE);
            if (opt.get() == ButtonType.OK) {
                try {
                    delete(button.getId());
                } catch (SQLException ex) {
                    loggingerror(ex);
                }
            }
        });
    }

    private void delete(String kode) throws SQLException {
        String tSQL = "UPDATE " + Stock_Opname_Detail.TABLENAME.get() + " SET flag=3 WHERE " + Stock_Opname_Detail.KODEITEM.get() + "='" + kode + "' AND "
                + Stock_Opname_Detail.TRANSID.get() + "='" + txt_kode_trans.getText() + "'";
        int x = updateToDatabase(tSQL);
        if (x == 1) {
            EZSystem.showAllert(EZAlertType.INFO, "Data berhasil dihapus !", EZIcon.ICON_STAGE);
            loadData();
        }
    }

    private String getQuery() {
        String tSQL = "SELECT * FROM " + V_Tmp_Stock_Opname.TABLENAME.get() + " WHERE " + V_Tmp_Stock_Opname.TRANID.get() + "='"
                + mKodeTrans + "' AND " + V_Tmp_Stock_Opname.OPNAME.get() + " >=0 "
                + "AND " + V_Tmp_Stock_Opname.FLAG.get() + "!=3 ORDER BY " + V_Tmp_Stock_Opname.ITEMNAME.get() + " ASC";
        return tSQL;
    }

    private String getQuery1() {
        String tSQL = "SELECT * FROM " + V_Stock_Opname.TABLENAME.get() + " WHERE " + V_Stock_Opname.ID.get() + "='" + mKodeTrans + "'";
        return tSQL;
    }

    private String getQueryUpdate(int flag) {
        String tSQL = "UPDATE " + Stock_Opname.TABLENAME.get() + " SET flag='" + flag + "',"
                + " u_dt='" + EZDate.SQLDATE.today() + "', sid='" + getSession() + "' "
                + ", ver_by='" + mMapKaryawan.get(MapKaryawan.KODEKARYAWAN.get()) + "' "
                + " WHERE " + Stock_Opname.TRANSID.get() + "='" + mKodeTrans + "'";
        return tSQL;
    }

    public static String getmKodeTrans() {
        return mKodeTrans;
    }

    public static void setmKodeTrans(String mKodeTrans) {
        FormVerifikasiStockOpnameController.mKodeTrans = mKodeTrans;
    }

    @Override
    public void initializeState() {
        try {
            loadData();
        } catch (SQLDataException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {
        btn_simpan.setOnAction((ActionEvent event) -> {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> opt = EZSystem.showAllert(alert, "Simpan data stock opname?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    performUpdateStock();
                    String tSQL = getQueryUpdate(2);
                    int x = updateToDatabase(tSQL);
                    if (x > 0) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
                        showReport("report/stock/lap_hasil_stock_opname.jrxml", getQueryReport());
                        setmUrlChild("/app/retail/fxml/stock/DataStockOpname.fxml");
                        getmHomeController().performLoadStock(event);
                    }
                }
            } catch (SQLException | IOException ex) {
                loggingerror(ex);
            }
        });

        btn_kembali.setOnAction((ActionEvent event) -> {
            try {
                setmUrlChild("/app/retail/fxml/stock/DataStockOpname.fxml");
                getmHomeController().performLoadStock(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });

        btn_batal.setOnAction((ActionEvent event) -> {
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Batalkan Transaksi " + mKodeTrans + " ? ", EZIcon.ICON_APPS);
            if (opt.get() == ButtonType.OK) {
                btn_kembali.fire();
                mKodeTrans = null;
            }
        });

        Platform.runLater(() -> {
            tbl_trans.requestFocus();
        });
    }

    private String getQueryReport() {
        String tSQL = "SELECT OD.*, OP.TGL, OP.NAMA, OP.FLAG as STTS, OP.CANCEL_BY, OP.VERBY "
                + "FROM v_stock_opname_detail OD "
                + "INNER JOIN v_stock_opname OP ON OP.ID=OD.TRANID WHERE OD.TRANID='" + mKodeTrans + "'";
        return tSQL;
    }

    @Override
    public void setToolTip() {
        btn_simpan.setCursor(Cursor.HAND);
        btn_batal.setCursor(Cursor.HAND);
        btn_kembali.setCursor(Cursor.HAND);
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 220);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_no, Pos.CENTER);
        setAligmentColoum(clm_kode_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_stock_akhir, Pos.CENTER);
        setAligmentColoum(clm_selisih, Pos.CENTER);
        double lebar = getWidth() - 200;
        clm_no.setPrefWidth((lebar * 3)/100);
        clm_kode_item.setPrefWidth((lebar * 10)/100);
        clm_kode_barcode.setPrefWidth((lebar * 10)/100);
        clm_nama_item.setPrefWidth((lebar * 35)/100);
        clm_sat.setPrefWidth((lebar * 5)/100);
        clm_stock_akhir.setPrefWidth((lebar * 8)/100);
        clm_opname.setPrefWidth((lebar * 8)/100);
        clm_selisih.setPrefWidth((lebar * 8)/100);
        clm_delete.setPrefWidth((lebar * 10)/100);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadData() throws SQLDataException {
        tbl_trans.getItems().clear();
        try {
            String query = getQuery1();
            ResultSet tResult = selectFromDatabase(query);
            int no = 0;
            if (tResult.next()) {
                txt_kode_trans.setText(tResult.getString(V_Stock_Opname.ID.get()));
                txt_input_date.setText(EZDate.FORMAT_2.get(tResult.getDate(V_Stock_Opname.TGL.get())));
                txt_input_by.setText(tResult.getString(V_Stock_Opname.NAMA.get()));
                String tSQL = getQuery();
                tResult = selectFromDatabase(tSQL);
                while (tResult.next()) {
                    no++;
                    String kode = tResult.getString(V_Tmp_Stock_Opname.IDITEM.get());
                    String sku = tResult.getString(V_Tmp_Stock_Opname.SKU.get());
                    String barcode = tResult.getString(V_Tmp_Stock_Opname.BARCODE.get());
                    String nama = tResult.getString(V_Tmp_Stock_Opname.ITEMNAME.get());
                    String sat = tResult.getString(V_Tmp_Stock_Opname.SATUAN.get());
                    int stock = tResult.getInt(V_Tmp_Stock_Opname.STOK.get());
                    int opname = tResult.getInt(V_Tmp_Stock_Opname.OPNAME.get());
                    btndelete = getButton(EZButtonType.BTN_DELETE, "Delete");
                    btndelete.setId(kode);
                    setButtonOnTableView(btndelete, EZButtonType.BTN_EDIT);
                    txtQty = new TextField("" + opname);
                    txtQty.setAlignment(Pos.CENTER);
                    int selisih = tResult.getInt(V_Tmp_Stock_Opname.SELISIH.get());
                    StockOpnameModel tModel = new StockOpnameModel("" + no, kode, sku, barcode, nama, sat, "" + stock, "" + stock, "" + selisih, txtQty, null);
                    itemList.add(tModel);
                }
                setValueColum();
                tbl_trans.setItems(itemList);
            }

            if (no == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                EZSystem.showAllert(alert, "List item stock opname kosong, Transaksi akan di batalkan!", EZIcon.ICON_STAGE);
                simpan();
                setmUrlChild("/app/retail/fxml/stock/DataStockOpname.fxml");
                getmHomeController().performLoadStock(null);
            }
        } catch (SQLException | IOException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void loggingerror(Exception ex) {
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }
}
