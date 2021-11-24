/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.finance;

import app.retail.model.finance.CashTransactionModel;
import app.retail.utility.EZDate;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
public class ViewCashTransactionController extends AbstractFinance implements Initializable {

    private final ObservableList<CashTransactionModel> cashTransList = javafx.collections.FXCollections.observableArrayList();
    private static String mKodeEdit;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Text lbl_total_trans;
    @FXML
    private Text lbl_kode_transaksi;
    @FXML
    private Text lbl_tanggal;
    @FXML
    private Text lbl_tipe;
    @FXML
    private Text lbl_keterangan;
    @FXML
    private Text lbl_inpu;
    @FXML
    private TableView<CashTransactionModel> tbl_trans;
    @FXML
    private TableColumn<CashTransactionModel,String> clm_no;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_desc;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_faktur;
    @FXML
    private TableColumn<CashTransactionModel, String> clm_jumlah;
    @FXML
    private TableColumn<CashTransactionModel, JFXButton> clm_delete;
    @FXML
    private JFXButton btn_kembali;
    @FXML
    private JFXButton btn_cetak;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            setToolTip();
            setButtonListener();
            loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }    

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadData() throws SQLException {
        String tSQL = "SELECT * FROM v_cash_transaction WHERE id='" + mKodeEdit + "'";
        ResultSet res = selectFromDatabase(tSQL);
        if (res.next()) {
            lbl_kode_transaksi.setText(res.getString("id"));
            lbl_tanggal.setText(EZDate.SHORTEMDATE.get(res.getDate("tran_date")));
            lbl_tipe.setText(res.getString("type"));
            lbl_keterangan.setText(res.getString("ket"));
            lbl_inpu.setText(res.getString("input_by"));
            lbl_total_trans.setText("Rp. " + formatRupiah(res.getDouble("total")));
            loadDetail(mKodeEdit);
        }
    }
    
    private void loadDetail(String kodeEdit) throws SQLException{
        String tSQL = "SELECT * FROM cash_transaction_detail WHERE id_trans='" + kodeEdit + "'";
        ResultSet res = selectFromDatabase(tSQL);
        int no = 0;
        while (res.next()) {
            no++;
            String faktur = res.getString("faktur");
            String total = formatRupiah(res.getDouble("total"));
            String ket = res.getString("ket");
            CashTransactionModel tModel = new CashTransactionModel("" + no, total, faktur, ket, null);
            cashTransList.add(tModel);
        }
        setValueColum();
        tbl_trans.setItems(cashTransList);
    }

    private void setValueColum(){
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_desc.setCellValueFactory(new PropertyValueFactory<>("deskrip"));
        clm_faktur.setCellValueFactory(new PropertyValueFactory<>("faktur"));
        clm_jumlah.setCellValueFactory(new PropertyValueFactory<>("total_detail"));
        clm_delete.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    public static String getmKodeEdit() {
        return mKodeEdit;
    }

    public static void setmKodeEdit(String mKodeEdit) {
        ViewCashTransactionController.mKodeEdit = mKodeEdit;
    }

    @Override
    public void initializeState() {
    }

    @Override
    public void setButtonListener() {
        Platform.runLater(()->{
            tbl_trans.requestFocus();
        });
        
        btn_kembali.setOnAction((ActionEvent event)->{
            try {
                setmUrlChild("/app/retail/fxml/finance/CashTransaction.fxml");
                getmHomeController().performLoadFinance(event);
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        btn_cetak.setOnAction((ActionEvent event)->{
            showReport("report/finance/cash/faktur_cash_transaction.jrxml", getQuery());
        });
    }
    
    private String getQuery(){
        String tSQL = "SELECT V.id , V.type , v.total , V.ket,V.tran_date, V.nama ,D.faktur, D.ket as desk, D.total as nilai "
                + "FROM v_cash_transaction V INNER JOIN cash_transaction_detail D on D.id_trans=V.id "
                + "WHERE V.id='"+mKodeEdit+"'";
        return tSQL;
    }

    @Override
    public void setToolTip() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 220);
        setAligmentColoum(clm_desc, Pos.CENTER_LEFT);
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_faktur, Pos.CENTER);
        setAligmentColoum(clm_jumlah, Pos.CENTER_RIGHT);
        btn_kembali.setCursor(Cursor.HAND);
        btn_cetak.setCursor(Cursor.HAND);
        double lebar = getWidth() - 200;
        clm_no.setPrefWidth((lebar * 3)/100);
        clm_desc.setPrefWidth((lebar * 20)/100);
        clm_faktur.setPrefWidth((lebar * 10)/100);
        clm_jumlah.setPrefWidth((lebar * 10)/100);
        clm_jumlah.setPrefWidth((lebar * 57)/100);
        
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
