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
import static app.retail.controller.general.General.selectFromDatabase;
import static app.retail.controller.general.General.setmUrlChild;
import app.retail.model.stock.StockOpnameModel;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_Stock_Opname;
import app.retail.utility.table.V_Tmp_Stock_Opname;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class FormViewStockOpnameController extends AbstractStock implements Initializable {
    private static String mKodeTrans;
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
    private Text txt_ver_oleh;
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
    private TableColumn<StockOpnameModel, String> clm_kode_barcode;
    @FXML
    private JFXButton btn_kembali;
    @FXML
    private JFXButton btn_print;
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
        setToolTip();
        setButtonListener();
        initializeState();
    }    

    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_kode_item.setCellValueFactory(new PropertyValueFactory<>("sku"));
        clm_kode_barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>("namaItem"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("sat"));
        clm_stock_akhir.setCellValueFactory(new PropertyValueFactory<>("stockakhir"));
        clm_opname.setCellValueFactory(new PropertyValueFactory<>("qtyOpname"));
        clm_selisih.setCellValueFactory(new PropertyValueFactory<>("selisih"));
    }

    public static String getmKodeTrans() {
        return mKodeTrans;
    }

    public static void setmKodeTrans(String mKodeTrans) {
        FormViewStockOpnameController.mKodeTrans = mKodeTrans;
    }

    @Override
    public void initializeState() {
        try {
            loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void setButtonListener() {
        btn_kembali.setOnAction((ActionEvent event) -> {
            try {
                setmUrlChild("/app/retail/fxml/stock/DataStockOpname.fxml");
                getmHomeController().performLoadStock(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        btn_print.setOnAction((ActionEvent event) -> {
            Optional<ButtonType> opt = EZSystem.showAllert(new Alert(Alert.AlertType.CONFIRMATION), "Cetak Data ?", EZIcon.ICON_STAGE);
            if(opt.get() == ButtonType.OK){
                showReport("report/stock/lap_hasil_stock_opname.jrxml", getQueryReport());
            }
        });
        
        Platform.runLater(()->{
            tbl_trans.requestFocus();
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
        btn_print.setCursor(Cursor.HAND);
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
        clm_x.setPrefWidth((lebar * 10)/100);
    }

    @Override
    public void loadForm(String pUrl, String pName,ActionEvent event) throws IOException {
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
            String query = getQueryReport();
            ResultSet tResult = selectFromDatabase(query);
            int no = 0;
            while (tResult.next()) {
                txt_kode_trans.setText(tResult.getString("TRANID"));
                txt_input_date.setText(EZDate.FORMAT_5.get(tResult.getDate(V_Stock_Opname.TGL.get())));
                txt_input_by.setText(tResult.getString(V_Stock_Opname.NAMA.get()));
                txt_ver_oleh.setText(tResult.getString(V_Stock_Opname.VERBY.get()));
                no++;
                String kode = tResult.getString(V_Tmp_Stock_Opname.IDITEM.get());
                String sku = tResult.getString(V_Tmp_Stock_Opname.SKU.get());
                String barcode = tResult.getString(V_Tmp_Stock_Opname.BARCODE.get());
                String nama = tResult.getString(V_Tmp_Stock_Opname.ITEMNAME.get());
                String sat = tResult.getString(V_Tmp_Stock_Opname.SATUAN.get());
                int stock = tResult.getInt(V_Tmp_Stock_Opname.STOK.get());
                int opname = tResult.getInt(V_Tmp_Stock_Opname.OPNAME.get());
                int selisih = tResult.getInt(V_Tmp_Stock_Opname.SELISIH.get());
                StockOpnameModel tModel = new StockOpnameModel("" + no, kode, sku, barcode, nama, sat, "" + stock, "" + opname, "" + selisih, null, null);
                itemList.add(tModel);
                setValueColum();
                tbl_trans.setItems(itemList);
            }
        } catch (SQLException ex) {
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
