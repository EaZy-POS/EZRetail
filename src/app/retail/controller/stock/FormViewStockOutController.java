/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.stock;

import static app.retail.controller.general.General.getHeight;
import static app.retail.controller.general.General.getStackTraceString;
import static app.retail.controller.general.General.getWidth;
import static app.retail.controller.general.General.getmHomeController;
import static app.retail.controller.general.General.setmUrlChild;
import app.retail.model.stock.StockKeluarModel;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_All_Stock_Out;
import app.retail.utility.table.V_Stock_Out_Detail;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class FormViewStockOutController extends AbstractStock implements Initializable {

    ObservableList<StockKeluarModel> itemList = FXCollections.observableArrayList();
    private static String mKodeTrans;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Text lbl_total_harga;
    @FXML
    private TableView<StockKeluarModel> tbl_trans;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_no;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_nama_item;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_sat;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_harga;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_qty;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_total;
    @FXML
    private JFXButton btn_cetak;
    @FXML
    private JFXButton btn_kembali;
    @FXML
    private Text lbl_notrans;
    @FXML
    private Text lbl_tangal;
    @FXML
    private Text lbl_ket;
    @FXML
    private Text lbl_inputby;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_barcode;
    @FXML
    private TableColumn<StockKeluarModel, String> clm_sku;
    @FXML
    private TableColumn<?, ?> clm_x;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeState();
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>("nama_item"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("sat"));
        clm_harga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        clm_qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("total_harga"));
        clm_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        clm_sku.setCellValueFactory(new PropertyValueFactory<>("sku"));
    }
    
    private String getQuery(){
        String tSQL = "SELECT * FROM v_all_stock_out SO "
                + "INNER JOIN v_stock_out_detail SD ON SD.TRAN_ID=SO.TRANSID "
                + "WHERE TRANSID='"+mKodeTrans+"' "
                + "ORDER BY TGL,TRANSID ASC;";
        return tSQL;
    }

    @Override
    public void loadData() throws SQLException {
        String tSQL = getQuery();
        ResultSet tResult = selectFromDatabase(tSQL);
        int index = 0;
        itemList.clear();
        while (tResult.next()) {            
            if (index == 0) {
                String tTranID = tResult.getString(V_All_Stock_Out.TRANSID.get());
                String tTgl = EZDate.FORMAT_5.get(tResult.getDate(V_All_Stock_Out.TGL.get()));
                String tNama = tResult.getString(V_All_Stock_Out.NAMA.get());
                String tKet = tResult.getString(V_All_Stock_Out.KET.get());
                lbl_notrans.setText(tTranID);
                lbl_tangal.setText(tTgl);
                lbl_inputby.setText(tNama);
                lbl_ket.setText(tKet);
            }
            String tBarcode = tResult.getString(V_Stock_Out_Detail.BARCODE.get());
            String tSKU = tResult.getString(V_Stock_Out_Detail.SKU.get());
            String tNamaItem = tResult.getString(V_Stock_Out_Detail.ITEMNAME.get());
            String tSat = tResult.getString(V_Stock_Out_Detail.SATUAN.get());
            String tHarga = formatRupiah(tResult.getDouble(V_Stock_Out_Detail.HARGA.get()));
            String tQty = tResult.getString(V_Stock_Out_Detail.QTY.get());
            String tTotal = formatRupiah(tResult.getDouble(V_Stock_Out_Detail.TOTALHARGA.get()));
            index++;
            StockKeluarModel model = new StockKeluarModel(""+index, tBarcode, tSKU, tNamaItem, tSat, tHarga, tQty, tTotal);
            itemList.add(model);
        }
        setValueColum();
        tbl_trans.setItems(itemList);
        lbl_total_harga.setText("Rp. "+hitungGrandTotal(tbl_trans, 7));
    }

    public static String getmKodeTrans() {
        return mKodeTrans;
    }

    public static void setmKodeTrans(String mKodeTrans) {
        FormViewStockOutController.mKodeTrans = mKodeTrans;
    }

    @Override
    public void initializeState() {
        try {
            setToolTip();
            setButtonListener();
            loadData();
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
    public void setButtonListener() {
        Platform.runLater(() -> {
            tbl_trans.requestFocus();
        });
        
        btn_kembali.setOnAction((ActionEvent event)->{
            try {
                setmUrlChild("/app/retail/fxml/stock/DataStockOut.fxml");
                getmHomeController().performLoadStock(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        btn_cetak.setOnAction((ActionEvent event)->{
            Optional<ButtonType> option = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak Data ?", EZIcon.ICON_STAGE);
            if (option.get() == ButtonType.OK) {
                showReport("report/stock/faktur_stock_out.jrxml", getQuery());
            }
        });
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 220);
        btn_kembali.setCursor(Cursor.HAND);
        btn_cetak.setCursor(Cursor.HAND);
        setAligmentColoum(clm_harga, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_sat, Pos.CENTER);
        setAligmentColoum(clm_qty, Pos.CENTER);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_barcode, Pos.CENTER_LEFT);
        setAligmentColoum(clm_sku, Pos.CENTER_LEFT);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        double lebar = getWidth() - 200;
        clm_nama_item.setPrefWidth((lebar * 25)/100);
        clm_sku.setPrefWidth((lebar * 10)/100);
        clm_barcode.setPrefWidth((lebar * 10)/100);
        clm_sat.setPrefWidth((lebar * 5)/100);
        clm_harga.setPrefWidth((lebar * 10)/100);
        clm_qty.setPrefWidth((lebar * 5)/100);
        clm_total.setPrefWidth((lebar * 10)/100);
        clm_x.setPrefWidth((lebar * 18)/100);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
