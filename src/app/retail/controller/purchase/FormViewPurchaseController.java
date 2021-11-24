/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.purchase;

import app.retail.controller.home.HomeController;
import app.retail.model.purchase.ViewPurchaseModel;
import app.retail.utility.EZDate;
import app.retail.utility.table.V_All_Purchase;
import app.retail.utility.table.V_All_Purchase_detail;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class FormViewPurchaseController extends AbstractPurchase implements Initializable {

    private static String mKodeTrans;
    ObservableList<ViewPurchaseModel> itemList = FXCollections.observableArrayList();
    private static String mJns;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TableView<ViewPurchaseModel> tbl_trans;
    @FXML
    private TableColumn<ViewPurchaseModel, String> clm_no;
    @FXML
    private TableColumn<ViewPurchaseModel, String> clm_kode_item;
    @FXML
    private TableColumn<ViewPurchaseModel, String> clm_nama_item;
    @FXML
    private TableColumn<ViewPurchaseModel, String> clm_sat;
    @FXML
    private TableColumn<ViewPurchaseModel, String> clm_harga;
    @FXML
    private TableColumn<ViewPurchaseModel, String> clm_qty;
    @FXML
    private TableColumn<ViewPurchaseModel, String> clm_total;
    @FXML
    private Text lbl_total_harga;
    @FXML
    private Label lbl_profile;
    @FXML
    private Label lbl_trans_id;
    @FXML
    private Label lbl_nm_supplier;
    @FXML
    private Label lbl_alamat;
    @FXML
    private Label lbl_kontak;
    @FXML
    private Label lbl_email;
    @FXML
    private Label lbl_tgl_trans;
    @FXML
    private Label lbl_faktur;
    @FXML
    private Label lbl_tipe_bayar;
    @FXML
    private Label lbl_jatuh_tempo;
    @FXML
    private Label lbl_diterima_oleh;
    @FXML
    private Label lbl_po_number;
    @FXML
    private JFXButton btn_kembali;
    @FXML
    private JFXButton btn_print;
    @FXML
    private TableColumn<ViewPurchaseModel, String> clm_x;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setToolTip();
        loadData();
        setButtonListener();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void simpan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadData() {
        try {
            loadHeader(mKodeTrans);
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    private void loadHeader(String kodeTrans) throws SQLException{
        lbl_po_number.setVisible(false);
        loadHeaderPurchase(kodeTrans);
    }
    
    private void loadHeaderPurchase(String kodeTrans) throws SQLException{
        String tSQL = "SELECT * FROM "+V_All_Purchase.TABLENAME.get()+" WHERE "+V_All_Purchase.REFNUM.get()+"='"+kodeTrans+"'";
        ResultSet tResult = selectFromDatabase(tSQL);
        if (tResult.next()) {
            String kode = tResult.getString(V_All_Purchase.REFNUM.get());
            String sup = tResult.getString(V_All_Purchase.SUPPLIER.get());
            String almt = tResult.getString(V_All_Purchase.ALAMAT.get());
            if (almt == null || almt.equals("")) {
                almt = "-";
            }
            String tlp = tResult.getString(V_All_Purchase.TELP.get());
            if (tlp == null || tlp.equals("") ) {
                tlp = "-";
            }
            String fax = tResult.getString(V_All_Purchase.FAX.get());
            if (fax == null || fax.equals("")) {
                fax = "-";
            }
            String kontak = "Telp. "+tlp+"Fax. "+fax;
            String email = tResult.getString(V_All_Purchase.EMAIL.get());
            Date dtTrans = tResult.getDate(V_All_Purchase.TGL.get());
            String faktur = tResult.getString(V_All_Purchase.FAKTUR.get());
            String po = tResult.getString(V_All_Purchase.PO.get());
            String jns = tResult.getString(V_All_Purchase.JNS.get());
            mJns = jns;
            String typeBayar = tResult.getString(V_All_Purchase.TYPEBAYAR.get());
            String recBy = tResult.getString(V_All_Purchase.DITERIMA.get());
            double total = tResult.getDouble(V_All_Purchase.TOTAL.get());
            
            lbl_po_number.setText(po);
            if (jns.equalsIgnoreCase("PO")) {
                lbl_po_number.setVisible(true);
            }
            lbl_alamat.setText(almt);
            lbl_diterima_oleh.setText(recBy);
            lbl_email.setText(email);
            lbl_faktur.setText(faktur);
            lbl_jatuh_tempo.setText("-");
            lbl_kontak.setText(kontak);
            lbl_nm_supplier.setText(sup);
            lbl_tgl_trans.setText(EZDate.FORMAT_2.get(dtTrans));
            lbl_tipe_bayar.setText(typeBayar);
            lbl_trans_id.setText(kode);
            lbl_total_harga.setText("Total: "+formatRupiah(total));
        }
        loadDetailPurchase(kodeTrans);
    }
   
    
    private void loadDetailPurchase(String pKodeEdit) throws SQLException{
        String tSQL = "SELECT * FROM " + V_All_Purchase_detail.TABLENAME.get() + " WHERE " + V_All_Purchase_detail.REFNUM.get() + "='" + pKodeEdit + "'";
        ResultSet tResult = selectFromDatabase(tSQL);
        int no = 0;
        while (tResult.next()) {
            no++;
            itemList.add(new ViewPurchaseModel("" + no, tResult.getString(V_All_Purchase_detail.IDITEM.get()),
                    tResult.getString(V_All_Purchase_detail.ITEMNAME.get()),
                    tResult.getString(V_All_Purchase_detail.SATUAN.get()),
                    formatRupiah(tResult.getDouble(V_All_Purchase_detail.BUYPRICE.get())),
                    tResult.getString(V_All_Purchase_detail.QTYTERIMA.get()),
                    formatRupiah(tResult.getDouble(V_All_Purchase_detail.TOTAL.get()))));
            setValueColum();
            tbl_trans.setItems(itemList);
        }
    }
    
    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_kode_item.setCellValueFactory(new PropertyValueFactory<>("kode_item"));
        clm_nama_item.setCellValueFactory(new PropertyValueFactory<>("nama_item"));
        clm_sat.setCellValueFactory(new PropertyValueFactory<>("sat"));
        clm_harga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        clm_qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    public static String getmKodeTrans() {
        return mKodeTrans;
    }

    public static void setmKodeTrans(String mKodeTrans) {
        FormViewPurchaseController.mKodeTrans = mKodeTrans;
    }

    @Override
    public void initializeState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setButtonListener() {
        btn_kembali.setOnAction((ActionEvent event)->{
            try {
                getmHomeController().performLoadpurchase(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        Platform.runLater(()->{
            tbl_trans.requestFocus();
        });
        
        btn_print.setOnAction((ActionEvent event)->{
            if(mJns.equalsIgnoreCase("LANGSUNG")){
                showReport("report/transaksi/purchase/faktur_purchase.jrxml", getQuery(mKodeTrans));
            }else{
                showReport("report/transaksi/purchase/faktur_receiving.jrxml", getQuery(mKodeTrans));
            }
        });
    }
    
    private String getQuery(String refnum){
        String tsql = "SELECT P.*, S.NMKARYAWA, PD.IDITEM, PD.ITEMNAME, PD.SATUAN, PD.BUYPRICE, PD.QTY, PD.QTYTERIMA, PD.TOTAL as SUBTOTAL " 
                +"FROM `v_all_purchase` P " 
                +"INNER JOIN v_all_purchase_detail PD on PD.REFNUM= P.REFNUM " 
                +"INNER JOIN v_session S ON P.SID = S.SESSIONID  WHERE P.REFNUM='"+refnum+"'";
        return tsql;
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 220);
        setAligmentColoum(clm_kode_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_qty, Pos.CENTER);
        setAligmentColoum(clm_nama_item, Pos.CENTER_LEFT);
        setAligmentColoum(clm_harga, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        setAligmentColoum(clm_sat, Pos.CENTER);
        lbl_profile.setText(HomeController.getmProfile());
        btn_kembali.setCursor(Cursor.HAND);
        btn_print.setCursor(Cursor.HAND);
        double lebar = getWidth() - 200;
        clm_nama_item.setPrefWidth((lebar * 35)/100);
        clm_kode_item.setPrefWidth((lebar * 10)/100);
        clm_sat.setPrefWidth((lebar * 5)/100);
        clm_harga.setPrefWidth((lebar * 10)/100);
        clm_qty.setPrefWidth((lebar * 5)/100);
        clm_total.setPrefWidth((lebar * 10)/100);
        clm_x.setPrefWidth((lebar * 21)/100);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
