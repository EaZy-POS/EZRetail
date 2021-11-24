/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.pos;

import app.retail.model.pos.LisFakturModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import app.retail.utility.table.V_Sale;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author RCS
 */
public class PosMaster extends AbstractPOS implements Initializable {

    private static Scene scence;
    ObservableList<LisFakturModel> itemList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXButton menu_pos;
    @FXML
    private JFXButton menu_kalkulasi;
    @FXML
    private TableView<LisFakturModel> tbl_trans;
    @FXML
    private TableColumn<LisFakturModel, String> clm_no;
    @FXML
    private TableColumn<LisFakturModel, String> clm_faktur;
    @FXML
    private TableColumn<LisFakturModel, String> clm_total;
    @FXML
    private TableColumn<LisFakturModel, String> clm_x;
    @FXML
    private TextField txt_cari;
    @FXML
    private TableColumn<LisFakturModel, String> clm_repriint;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setLayout();
        initializeState();
        setToolTip();
        setButtonListener();
    }

    public PosMaster() {
        setmPosMaster(this);
    }

    private void setLayout() {
        AnchorPane.setPrefWidth(getWidth() - 200);
        AnchorPane.setPrefHeight(getHeight() - 225);
    }

    @Override
    public void initializeState() {
        loadData("*");
        txt_cari.requestFocus();
    }

    @Override
    public void setButtonListener() {

        Platform.runLater(() -> {
            txt_cari.requestFocus();
        });

        menu_pos.setOnAction(((event) -> {
            try {
                if (isAllowed(Akses_List.POS)) {
                    loadPOS(event);
                }
            } catch (IOException ex) {
                loggingerror(ex);
            }
        }));

        menu_kalkulasi.setOnAction((ActionEvent event) -> {
            if (isAllowed(Akses_List.KALKULASI_KASIR)) {
                String tUrl = "/app/retail/fxml/pos/KalkulasiKasir.fxml";
                loadForm(tUrl, "Kalkulasi Kasir");
            }
        });

        txt_cari.setOnAction((event) -> {
            loadData("AND " + V_Sale.KODETRANS.get() + " = '" + txt_cari.getText() + "'");
        });
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setToolTip() {
        menu_kalkulasi.setCursor(Cursor.HAND);
        menu_kalkulasi.setTooltip(new Tooltip("Kalkulasi Kasir"));
        menu_pos.setCursor(Cursor.HAND);
        menu_pos.setTooltip(new Tooltip("Point Of Sale"));
        setAligmentColoum(clm_no, Pos.CENTER_LEFT);
        setAligmentColoum(clm_faktur, Pos.CENTER_LEFT);
        setAligmentColoum(clm_total, Pos.CENTER_RIGHT);
        double lebar = getWidth() - 200;
        clm_no.setPrefWidth((lebar * 5) / 100);
        clm_faktur.setPrefWidth((lebar * 15) / 100);
        clm_total.setPrefWidth((lebar * 10) / 100);
        clm_repriint.setPrefWidth((lebar * 8) / 100);
        clm_x.setPrefWidth((lebar * 52) / 100);
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadPOS(javafx.event.ActionEvent event) throws IOException {
        if (mMapKaryawan.get("kode_shift") == null) {
            EZSystem.showAllert(EZAlertType.WARNING, "Data shift belum dibuat !\nMohon isi data shift terlebih dahulu", EZIcon.ICON_APPS);
        } else {
            double widthh = ((Node) event.getSource()).getScene().getWidth();
            double heightt = ((Node) event.getSource()).getScene().getHeight();
            Parent root = FXMLLoader.load(getClass().getResource("/app/retail/fxml/pos/POS.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            scence = new Scene(root, widthh, heightt);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("POS");
            stage.setMaximized(true);
            stage.getIcons().add(new Image("/app/retail/images/icons8_cash_register_48px.png"));
            stage.setScene(scence);
            stage.show();

            scence.setOnKeyPressed((ev) -> {
                getmPosController().setShortcut(ev.getCode());
            });
        }
    }

    private String getQuery(String pParam) {
        String tSQL = "SELECT KODETRANS, GRANDTOTAL FROM " + V_Sale.TABLENAME.get()
                + " WHERE IDKARYAWAN='" + mMapKaryawan.get("kode") + "' "
                + "AND " + V_Sale.TRANDATE.get() + " BETWEEN '" + EZDate.SQLDATE.today() + " 00:00:00' AND '" + EZDate.SQLDATE.today() + " 23:59:59' "
                + (pParam.equals("*") ? "" : pParam)
                + "ORDER BY KODETRANS ASC";
        return tSQL;
    }

    private void loadData(String pParam) {
        String tSQL = getQuery(pParam);
        itemList.clear();
        try {
            ResultSet res = selectFromDatabase(tSQL);
            int no = 0;
            while (res.next()) {
                no++;
                String tFaktur = res.getString(V_Sale.KODETRANS.get());
                String tTotal = formatRupiah(res.getDouble(V_Sale.GRANDTOTAL.get()));
                JFXButton btn = getButton(EZButtonType.BTN_PRINT, "Reprint");
                btn.setId(tFaktur);
                setButtonOnTableView(btn);
                LisFakturModel model = new LisFakturModel("" + no, tFaktur, tTotal, btn);
                itemList.add(model);
            }
            loadDataToTable();

            if (!pParam.equals("*") && no == 0) {
                loadData("*");
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    public void setButtonOnTableView(JFXButton button) {
        button.setOnAction((ActionEvent event) -> {
            String faktur = button.getId();
            ReprintController.setData(faktur, this);
            if (isAllowed(Akses_List.REPRINT)) {
                String tUrl = "/app/retail/fxml/pos/Reprint.fxml";
                loadForm(tUrl, "Reprint");
            }
        });

    }

    private void loadDataToTable() {
        setValueColum();
        tbl_trans.setItems(itemList);
    }

    private void setValueColum() {
        clm_no.setCellValueFactory(new PropertyValueFactory<>("no"));
        clm_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        clm_faktur.setCellValueFactory(new PropertyValueFactory<>("faktur"));
        clm_repriint.setCellValueFactory(new PropertyValueFactory<>("btnreprint"));
    }

}
