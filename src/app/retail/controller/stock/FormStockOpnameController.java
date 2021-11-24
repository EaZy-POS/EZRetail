/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import static app.retail.controller.general.General.getStackTraceString;
import app.retail.controller.general.MapKaryawan;
import app.retail.model.stock.StockOpnameModel;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Stock_Opname;
import app.retail.utility.table.Stock_Opname_Detail;
import app.retail.utility.table.V_Stock;
import app.retail.utility.table.V_Tmp_Stock_Opname;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.JTextField;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormStockOpnameController extends AbstractStock implements Initializable {

    ObservableList<String> kategoriList = FXCollections.observableArrayList();
    ObservableList<String> subkategoriList = FXCollections.observableArrayList();
    ObservableList<StockOpnameModel> itemList = FXCollections.observableArrayList();
    private TextField txtQty;
    private static String mKodeTrans;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXTextField txt_cari;
    @FXML
    private JFXRadioButton radio_nama;
    @FXML
    private ToggleGroup rc;
    @FXML
    private JFXRadioButton radio_sku;
    @FXML
    private ToggleGroup rc1;
    @FXML
    private JFXRadioButton radio_barcode;
    @FXML
    private ToggleGroup rc11;
    @FXML
    private JFXComboBox<String> cmb_kategori;
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
    private JFXButton btn_batal;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXComboBox<String> cmb_sub_kategori;
    @FXML
    private JFXButton btn_cari;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_selisih;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_stock_akhir;
    @FXML
    private TableColumn<StockOpnameModel, JTextField> clm_opname;
    @FXML
    private JFXButton btn_ok;
    @FXML
    private JFXButton btn_reload;
    @FXML
    private JFXButton btn_check_item;
    @FXML
    private JFXButton btn_kembali;
    @FXML
    private TableColumn<StockOpnameModel, String> clm_x;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        clm_selisih.setVisible(false);
        initializeState();
        setToolTip();
        loadKategori();
        subkategoriList.add("-- Semua --");
        cmb_sub_kategori.setItems(subkategoriList);
        cmb_sub_kategori.getSelectionModel().select(0);
        setButtonListener();
    }

    private void performDelete() throws SQLException {
        String tQuery = "DELETE FROM " + Stock_Opname_Detail.TABLENAME.get() + " WHERE " + Stock_Opname_Detail.FLAG.get() + "='0'";
        updateToDatabase(tQuery);
        tQuery = "DELETE FROM " + Stock_Opname.TABLENAME.get() + " WHERE " + Stock_Opname.FLAG.get() + "='0'";
        updateToDatabase(tQuery);
    }

    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_kode_item.setCellValueFactory(new PropertyValueFactory<>("kodeItem"));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>("namaItem"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("sat"));
        clm_stock_akhir.setCellValueFactory(new PropertyValueFactory<>("stockakhir"));
        clm_opname.setCellValueFactory(new PropertyValueFactory<>("txtQty"));
        clm_selisih.setCellValueFactory(new PropertyValueFactory<>("selisih"));
    }

    private String getQuery() {
        String tSQL = "SELECT * FROM "+V_Tmp_Stock_Opname.TABLENAME.get()+ " WHERE "+V_Tmp_Stock_Opname.TRANID.get()+"='" + mKodeTrans + "' ";

        if (!txt_cari.getText().equals("")) {
            tSQL += " AND ";
            if (radio_nama.isSelected()) {
                tSQL += V_Tmp_Stock_Opname.ITEMNAME.get()+" LIKE '%" + txt_cari.getText() + "%' ";
            }

            if (radio_sku.isSelected()) {
                tSQL += V_Tmp_Stock_Opname.SKU.get()+"  ='" + txt_cari.getText() + "'";
            }

            if (radio_barcode.isSelected()) {
                tSQL += V_Tmp_Stock_Opname.BARCODE.get()+" ='" + txt_cari.getText() + "'";
            }

            if (cmb_kategori.getSelectionModel().getSelectedIndex() > 0) {
                tSQL += " AND "+V_Tmp_Stock_Opname.KATID.get()+"='" + mMapKategori.get(cmb_kategori.getSelectionModel().getSelectedItem()) + "'";
                if (cmb_sub_kategori.getSelectionModel().getSelectedIndex() > 0) {
                    tSQL += " AND "+V_Tmp_Stock_Opname.SUBKATID.get()+"='" + mMapSubKategori.get(cmb_sub_kategori.getSelectionModel().getSelectedItem()) + "'";
                }
            }
        } else {
            if (cmb_kategori.getSelectionModel().getSelectedIndex() > 0) {
                tSQL += " AND "+V_Tmp_Stock_Opname.KATID.get()+"='" + mMapKategori.get(cmb_kategori.getSelectionModel().getSelectedItem()) + "'";
                if (cmb_sub_kategori.getSelectionModel().getSelectedIndex() > 0) {
                    tSQL += " AND "+V_Tmp_Stock_Opname.SUBKATID.get()+"='" + mMapSubKategori.get(cmb_sub_kategori.getSelectionModel().getSelectedItem()) + "'";
                }
            }
        }

        return tSQL + " ORDER BY "+V_Tmp_Stock_Opname.ITEMNAME.get()+" ASC";
    }

    private String getQueryReload(String type) {
        String tSQL = "SELECT * FROM "+V_Tmp_Stock_Opname.TABLENAME.get()+ " WHERE "+V_Tmp_Stock_Opname.TRANID.get()+"='" + mKodeTrans + "' ";
        if (type.equalsIgnoreCase("reload")) {
            tSQL += " AND "+V_Tmp_Stock_Opname.OPNAME.get()+" >0 AND FLAG!=3 ORDER BY "+V_Tmp_Stock_Opname.ITEMNAME.get()+" ASC";
        }

        if (type.equalsIgnoreCase("check")) {
            tSQL += " AND "+V_Tmp_Stock_Opname.OPNAME.get()+"=0 AND FLAG!=3 ORDER BY "+V_Tmp_Stock_Opname.ITEMNAME.get()+" ASC";
        }
        return tSQL;
    }

    private String getQueryUpdate() {
        String tSQL = "UPDATE " + Stock_Opname.TABLENAME.get() + " SET "+Stock_Opname.FLAG.get()+"='1' WHERE " + Stock_Opname.TRANSID.get() + "='" + mKodeTrans + "'";
        return tSQL;
    }

    public ResultSet searchBy(String tSQL) throws SQLException {
        return selectFromDatabase(tSQL);
    }
    
    public void setListenerOnTableView(TextField text) {
        setNumericTextfield(text);
        int index = Integer.parseInt(text.getId());
        StockOpnameModel model = tbl_trans.getItems().get(index);
        text.setOnKeyTyped((KeyEvent event) -> {
            int stock = Integer.parseInt(model.getStockakhir());
            int opn = Integer.parseInt(model.getQtyOpname());
            int selisih = stock+opn;
            model.setSelisih(""+selisih);
        });
    }
    
    public void loadKategori() {
        List<String> tMap = getKategori();
        kategoriList.add("Semua");
        for (int i = 0; i < tMap.size(); i++) {
            String valString = tMap.get(i);
            kategoriList.add(valString);
        }
        cmb_kategori.getItems().clear();
        cmb_kategori.setItems(kategoriList);
        cmb_kategori.getSelectionModel().select(0);
    }
    
    public void loadSubKategori(String pId) {
        cmb_sub_kategori.getItems().clear();
        List<String> tMap = getSubKategori(pId);
        subkategoriList.add("-- Semua --");
        for (int i = 0; i < tMap.size(); i++) {
            String valString = tMap.get(i);
            subkategoriList.add(valString);
        }    
        cmb_sub_kategori.setItems(subkategoriList);
        cmb_sub_kategori.getSelectionModel().select(0);
        cmb_sub_kategori.getSelectionModel().select(0);
    }
    
    private void loadDataToTable(ResultSet pResult){
        tbl_trans.getItems().clear();
        try {
            int no = 0;
            while (pResult.next()) {
                no++;
                String kode = pResult.getString(V_Tmp_Stock_Opname.IDITEM.get());
                String nama = pResult.getString(V_Tmp_Stock_Opname.ITEMNAME.get());
                String sat = pResult.getString(V_Tmp_Stock_Opname.SATUAN.get());
                int stock = pResult.getInt(V_Tmp_Stock_Opname.STOK.get());
                int opname = pResult.getInt(V_Tmp_Stock_Opname.OPNAME.get());
                txtQty = new TextField(""+opname);
                txtQty.setAlignment(Pos.CENTER);
                int selisih = pResult.getInt(V_Tmp_Stock_Opname.SELISIH.get());
                StockOpnameModel tModel = new StockOpnameModel(""+no, kode, nama, sat, ""+stock, ""+stock, ""+selisih, txtQty);
                itemList.add(tModel);
                setValueColum();
                tbl_trans.setItems(itemList);
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    @Override
    public void loggingerror(Exception ex){
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
    }
    
    @Override
    public void initializeState() {
        try {
            performDelete();
            mKodeTrans = generateRefnum(Stock_Opname.TABLENAME.get(), 
                    Stock_Opname.TRANSID.get(), "SO-"+EZDate.FAKTUR.today());
            if (mKodeTrans != null) {
                RecordEntry tMapOpname = new RecordEntry();
                tMapOpname.createEntry(Stock_Opname.TRANSID.get(), mKodeTrans);
                tMapOpname.createEntry(Stock_Opname.TRANDATE.get(), EZDate.SQLDATE.today());
                tMapOpname.createEntry(Stock_Opname.INPUTBY.get(), mMapKaryawan.get(MapKaryawan.KODEKARYAWAN.get()));
                tMapOpname.createEntry(Stock_Opname.CDT.get(), EZDate.SQLDATE.today());
                tMapOpname.createEntry(Stock_Opname.SID.get(), getSession());
                int x = insertToDatabase(tMapOpname, Stock_Opname.TABLENAME.get());
                if (x > 0) {
                    String tSQL = "INSERT INTO "+Stock_Opname_Detail.TABLENAME.get()
                            +" (id,trans_id,kode_item,stock) SELECT UUID(),'" + mKodeTrans 
                            + "',"+V_Stock.IDITEM.get()+","+V_Stock.STOK.get()+" FROM v_stock ";
                    updateToDatabase(tSQL);
                }
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            txt_cari.requestFocus();
        });
        
        cmb_kategori.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String kode = mMapKategori.get(cmb_kategori.getSelectionModel().getSelectedItem());
            loadSubKategori(kode);
        });

        btn_cari.setOnAction((ActionEvent event) -> {
            try {
                loadData();
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });

        txt_cari.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    loadData();
                } catch (SQLException ex) {
                    loggingerror(ex);
                }
            }
        });

        btn_ok.setOnAction((ActionEvent event) -> {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> opt = EZSystem.showAllert(alert1, "Simpan data ke list stock opname?", EZIcon.ICON_STAGE);
            if (opt.get() == ButtonType.OK) {
                tbl_trans.getItems().stream().map((model) -> {
                    txtQty = model.getTxtQty();
                    return model;
                }).map((model) -> {
                    model.setQtyOpname(txtQty.getText());
                    return model;
                }).forEach((model) -> {
                    String kode = model.getKodeItem();
                    String qty = model.getQtyOpname();
                    if (!qty.equals("0")) {
                        String tSQL = "UPDATE " + Stock_Opname_Detail.TABLENAME.get() + " SET "
                                + Stock_Opname_Detail.OPNAME.get() + "=" + qty + ", " + Stock_Opname_Detail.SELISIH.get() + "="
                                + qty + "-" + model.getStockakhir()
                                + " WHERE " + Stock_Opname_Detail.KODEITEM.get() + "='" + kode + "' AND "
                                + Stock_Opname_Detail.TRANSID.get() + "='" + mKodeTrans + "'";
                        try {
                            updateToDatabase(tSQL);
                        } catch (SQLException ex) {
                            loggingerror(ex);
                        }
                    }
                });
                
                tbl_trans.getItems().clear();
            }
        });

        btn_reload.setOnAction((ActionEvent event) -> {
            try {
                String tSQL = getQueryReload("reload");
                ResultSet result = searchBy(tSQL);
                if (result != null) {
                    loadDataToTable(result);
                }
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });

        btn_check_item.setOnAction((ActionEvent event) -> {
            try {
                String tSQL = getQueryReload("check");
                ResultSet result = searchBy(tSQL);
                if (result != null) {
                    loadDataToTable(result);
                }
            } catch (SQLException ex) {
                loggingerror(ex);
            }
        });

        btn_simpan.setOnAction((ActionEvent event) -> {
            try {
                String tSQL = getQueryReload("reload");
                ResultSet tRes = selectFromDatabase(tSQL);
                if (tRes.next()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    Optional<ButtonType> opt = EZSystem.showAllert(alert, "Simpan data stock opname?", EZIcon.ICON_STAGE);
                    if (opt.get() == ButtonType.OK) {
                        tSQL = getQueryUpdate();
                        int x = updateToDatabase(tSQL);
                        if (x > 0) {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
                            showReport("report/stock/lap_hasil_stock_opname.jrxml", getQueryReport());
                            btn_kembali.fire();
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    EZSystem.showAllert(alert, "Belum ada data stock opname yang di input!", EZIcon.ICON_STAGE);
                }
            } catch (SQLException ex) {
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

    }
    
    private String getQueryReport(){
        String tSQL = "SELECT OD.*, OP.TGL, OP.NAMA, OP.FLAG as STTS, OP.CANCEL_BY, OP.VERBY "
                + "FROM v_stock_opname_detail OD "
                + "INNER JOIN v_stock_opname OP ON OP.ID=OD.TRANID WHERE OD.TRANID='"+mKodeTrans+"'";
        return tSQL;
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 225);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_no, Pos.CENTER);
        setAligmentColoum(clm_kode_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_stock_akhir, Pos.CENTER);
        setAligmentColoum(clm_selisih, Pos.CENTER);
        btn_batal.setCursor(Cursor.HAND);
        btn_cari.setCursor(Cursor.HAND);
        btn_check_item.setCursor(Cursor.HAND);
        btn_kembali.setCursor(Cursor.HAND);
        btn_ok.setCursor(Cursor.HAND);
        btn_reload.setCursor(Cursor.HAND);
        btn_simpan.setCursor(Cursor.HAND);
        double lebar = getWidth() - 200;
        clm_no.setPrefWidth((lebar * 3)/100);
        clm_kode_item.setPrefWidth((lebar * 15)/100);
        clm_nama_item.setPrefWidth((lebar * 35)/100);
        clm_sat.setPrefWidth((lebar * 5)/100);
        clm_stock_akhir.setPrefWidth((lebar * 8)/100);
        clm_opname.setPrefWidth((lebar * 8)/100);
        clm_selisih.setPrefWidth((lebar * 8)/100);
        clm_x.setPrefWidth((lebar * 22)/100);
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
    public void loadData() throws SQLException {
        tbl_trans.getItems().clear();
        try {
            String tSQL = getQuery();
            ResultSet tResult = selectFromDatabase(tSQL);
            int no = 0;
            while (tResult.next()) {
                no++;
                String kode = tResult.getString(V_Tmp_Stock_Opname.IDITEM.get());
                String nama = tResult.getString(V_Tmp_Stock_Opname.ITEMNAME.get());
                String sat = tResult.getString(V_Tmp_Stock_Opname.SATUAN.get());
                int stock = tResult.getInt(V_Tmp_Stock_Opname.STOK.get());
                int opname = tResult.getInt(V_Tmp_Stock_Opname.OPNAME.get());
                txtQty = new TextField("" + opname);
                txtQty.setAlignment(Pos.CENTER);
                int selisih = tResult.getInt(V_Tmp_Stock_Opname.SELISIH.get());
                StockOpnameModel tModel = new StockOpnameModel("" + no, kode, nama, sat, "" + stock, "" + stock, "" + selisih, txtQty);
                itemList.add(tModel);
                setValueColum();
                tbl_trans.setItems(itemList);
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

}
