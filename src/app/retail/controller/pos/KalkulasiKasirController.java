/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import static app.retail.controller.general.General.insertToDatabase;
import static app.retail.controller.general.General.updateToDatabase;
import app.retail.controller.general.MapKaryawan;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.Kalkulasi_Kasir;
import co.id.eazy.query.service.RecordEntry;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class KalkulasiKasirController extends AbstractPOS implements Initializable {

    private static String mRefnum;
    private static boolean isEdit;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button btn_close;
    @FXML
    private Text lbl_tanggal;
    @FXML
    private Text lbl_kasir;
    @FXML
    private Text lbl_shift;
    @FXML
    private JFXTextField txt_cash;
    @FXML
    private JFXTextField txt_debet;
    @FXML
    private JFXTextField txt_credit;
    @FXML
    private JFXTextField txt_piutang;
    @FXML
    private Text lbl_total;
    @FXML
    private JFXButton btn_simpan;
    @FXML
    private JFXButton btn_batal;

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

    @Override
    public void initializeState() {
        try {
            clear();
            lbl_tanggal.setText(EZDate.FORMAT_2.today());
            lbl_kasir.setText(mMapKaryawan.get(MapKaryawan.NAMAKARYAWAN.get()));
            lbl_shift.setText(mMapKaryawan.get(MapKaryawan.SHIFT.get()));
            loadData();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    private void loadData() throws SQLException{
        String tSQL = "SELECT * FROM "+Kalkulasi_Kasir.TABLENAME.get()
                +" WHERE "+Kalkulasi_Kasir.IDKARYAWAN.get()+" = '"+mMapKaryawan.get(MapKaryawan.KODEKARYAWAN.get())+"' AND "
                + Kalkulasi_Kasir.IDSHIFT.get()+" = '"+mMapKaryawan.get(MapKaryawan.KODESHIFT.get())+"' "
                + "AND DATE_FORMAT("+Kalkulasi_Kasir.TRANDATE.get()+",'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d')";
        
        ResultSet result = selectFromDatabase(tSQL);
        if(result.next()){
            isEdit = true;
            mRefnum = result.getString(Kalkulasi_Kasir.REFNUM.get());
            txt_cash.setText(formatRupiah(result.getDouble(Kalkulasi_Kasir.CASH.get())));
            txt_debet.setText(formatRupiah(result.getDouble(Kalkulasi_Kasir.DEBET.get())));
            txt_credit.setText(formatRupiah(result.getDouble(Kalkulasi_Kasir.CREDIT.get())));
            txt_piutang.setText(formatRupiah(result.getDouble(Kalkulasi_Kasir.PIUTANG.get())));
            lbl_total.setText("Rp. "+formatRupiah(result.getDouble(Kalkulasi_Kasir.TOTAL.get())));
        }else{
            isEdit= false;
            mRefnum = generateRefnum(Kalkulasi_Kasir.TABLENAME.get(), Kalkulasi_Kasir.REFNUM.get(), "CL"+EZDate.FAKTUR.today());
        }
    }
    
    @Override
    public void setButtonListener() {
        btn_simpan.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> option = EZSystem.showAllert(alert, "Simpan data ?", EZIcon.ICON_STAGE);
            if (option.get() == ButtonType.OK) {
                simpan();
            }
        });

        btn_close.setOnAction((ActionEvent event) -> {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });

        btn_batal.setOnAction((ActionEvent event) -> {
            initializeState();
        });

        txt_cash.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                txt_debet.requestFocus();
            }
        });
        
        txt_cash.setOnKeyReleased((KeyEvent event) -> {
            hitungTotal();
        });

        txt_debet.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                txt_credit.requestFocus();
            }
        });
        
        txt_debet.setOnKeyReleased((KeyEvent event) -> {
            hitungTotal();
        });

        txt_credit.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                txt_piutang.requestFocus();
            }
        });
        
        txt_credit.setOnKeyReleased((KeyEvent event) -> {
            hitungTotal();
        });

        txt_piutang.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                btn_simpan.fire();
            }
        });
        
        txt_piutang.setOnKeyReleased((KeyEvent event) -> {
            hitungTotal();
        });

        Platform.runLater(() -> {
            txt_cash.requestFocus();
        });
    }

    public void simpan() {
        try {
            RecordEntry tMapInsert = new RecordEntry();
            tMapInsert.createEntry(Kalkulasi_Kasir.REFNUM.get(), mRefnum);
            tMapInsert.createEntry(Kalkulasi_Kasir.TRANDATE.get(), EZDate.SQLDATETIME.today());
            tMapInsert.createEntry(Kalkulasi_Kasir.IDKARYAWAN.get(), mMapKaryawan.get(MapKaryawan.KODEKARYAWAN.get()));
            tMapInsert.createEntry(Kalkulasi_Kasir.IDSHIFT.get(), mMapKaryawan.get(MapKaryawan.KODESHIFT.get()));
            tMapInsert.createEntry(Kalkulasi_Kasir.CASH.get(), txt_cash.getText().replace(",", ""));
            tMapInsert.createEntry(Kalkulasi_Kasir.DEBET.get(), txt_debet.getText().replace(",", ""));
            tMapInsert.createEntry(Kalkulasi_Kasir.CREDIT.get(), txt_credit.getText().replace(",", ""));
            tMapInsert.createEntry(Kalkulasi_Kasir.PIUTANG.get(), txt_piutang.getText().replace(",", ""));
            tMapInsert.createEntry(Kalkulasi_Kasir.TOTAL.get(), lbl_total.getText().replace(",", "").replace("Rp. ", ""));
            if (!isEdit) {
                insertToDatabase(tMapInsert, Kalkulasi_Kasir.TABLENAME.get());
            } else {
                updateToDatabase(tMapInsert, Kalkulasi_Kasir.TABLENAME.get(), Kalkulasi_Kasir.REFNUM.get() + "='" + mRefnum + "'");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            EZSystem.showAllert(alert, "Data berhasil disimpan", EZIcon.ICON_STAGE);
            initializeState();
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }

    @Override
    public void clear() {
        txt_cash.setText("0");
        txt_debet.setText("0");
        txt_credit.setText("0");
        txt_piutang.setText("0");
        lbl_total.setText("Rp. 0");
        txt_cash.requestFocus();
    }

    @Override
    public void setToolTip() {
        btn_simpan.setCursor(Cursor.HAND);
        btn_batal.setCursor(Cursor.HAND);
        btn_close.setCursor(Cursor.HAND);
        setCurencyTextfield(txt_cash);
        setCurencyTextfield(txt_credit);
        setCurencyTextfield(txt_debet);
        setCurencyTextfield(txt_piutang);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void hitungTotal(){
        double cash = Double.parseDouble(txt_cash.getText().equals("") ? "0" : txt_cash.getText().replace(",", ""));
        double debt = Double.parseDouble(txt_debet.getText().equals("") ? "0" : txt_debet.getText().replace(",", ""));
        double cred = Double.parseDouble(txt_credit.getText().equals("") ? "0" : txt_credit.getText().replace(",", ""));
        double piu = Double.parseDouble(txt_piutang.getText().equals("") ? "0" : txt_piutang.getText().replace(",", ""));
        double total = cash + debt + cred + piu;
        
        lbl_total.setText("Rp. "+ formatRupiah(total));
    }

}
