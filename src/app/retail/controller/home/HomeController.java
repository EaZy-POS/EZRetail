/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.home;

import app.retail.controller.general.General;
import app.retail.utility.Akses_List;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZSystem;
import com.jfoenix.controls.JFXButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class HomeController extends General implements Initializable {
    private static Timer mTimer;
    
    @FXML
    private Text lbl_datetime;    
    @FXML
    private AnchorPane pnl_scroll;
    @FXML
    private Button btn_home,btn_data;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private JFXButton btn_purchase;
    @FXML
    private JFXButton btn_pos;
    @FXML
    private JFXButton btn_master;
    @FXML
    private JFXButton btn_finance;
    @FXML
    private JFXButton btn_tools;
    @FXML
    private JFXButton btn_stock;
    @FXML
    private Text txt_nama_tko;
    @FXML
    private Text txt_alamat;
    @FXML
    private Text txt_telp_fax;
    @FXML
    private Pane pane_logo;
    @FXML
    private ImageView img_logo;
    @FXML
    private Text txt_users;
    @FXML
    private JFXButton btn_logout;
    @FXML
    private JFXButton btn_report;
    @FXML
    private Label txt_copyright;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            setDateTimeToday();
            setButtonListener();
            setToolTip();
            initializeState();
            btn_home.fire();
        }catch(Exception e){
            loggingerror(e);
        }
    }    
    
    private void setDateTimeToday(){
        ActionListener taskPerformer = (ActionEvent evt) -> {
            String tDate = EZDate.FORMAT_6.today();
            lbl_datetime.setText(tDate);
        };
        mTimer = new javax.swing.Timer(1000, taskPerformer);
        mTimer.start();
    }


    @Override
    public void setButtonListener() {
        img_logo.setCursor(Cursor.HAND);
        
        Platform.runLater(() -> {
            btn_home.requestFocus();
        });
        
        btn_home.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent ActionEvent) {
                try {
                    final String tUrl = "/app/retail/fxml/dashboard/Dashboard.fxml";
                    loadForm(tUrl, this.getClass().getName(), ActionEvent);
                } catch (IOException ex) {
                    loggingerror(ex);
                }

            }
        });
        
        btn_data.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent ActionEvent) {
                if(isAllowed(Akses_List.DATA.get(), true)){
                    try {
                        final String  tUrl = "/app/retail/fxml/inventory/MasterItem.fxml";
                        loadForm(tUrl, this.getClass().getName(),ActionEvent);
                    } catch (IOException ex) {
                        loggingerror(ex);
                    }
                }
            }
        });
        
        btn_purchase.setOnAction((javafx.event.ActionEvent ActionEvent) -> {
            if (isAllowed(Akses_List.PURCHASE.get(), true)) {
                try {
                    performLoadpurchase(ActionEvent);
                } catch (IOException ex) {
                    loggingerror(ex);
                }
            }
        });
        
        btn_master.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent ActionEvent) {
                if(isAllowed(Akses_List.MASTER.get(), true)){
                    try {
                        final String  tUrl = "/app/retail/fxml/data/Data.fxml";
                        final String mUrl = "/app/retail/fxml/data/DataPelanggan.fxml";
                        setmUrlChild(mUrl);
                        loadForm(tUrl, this.getClass().getName(),ActionEvent);
                    } catch (IOException ex) {
                        loggingerror(ex);
                    }
                }
            }
        });
        
        btn_finance.setOnAction((javafx.event.ActionEvent ActionEvent) -> {
            if(isAllowed(Akses_List.FINANCE.get(), true)){
                try {
                    setmUrlChild("/app/retail/fxml/finance/DataHutang.fxml");
                    performLoadFinance(ActionEvent);
                } catch (IOException ex) {
                    loggingerror(ex);
                }
            }
        });
        
        btn_stock.setOnAction((javafx.event.ActionEvent ActionEvent) -> {
            if(isAllowed(Akses_List.STOCK.get(), true)){
                try {
                    setmUrlChild("/app/retail/fxml/stock/DataStock.fxml");
                    performLoadStock(ActionEvent);
                } catch (IOException ex) {
                    loggingerror(ex);
                }
            }
        });
        
        btn_tools.setOnAction((javafx.event.ActionEvent ActionEvent) -> {
            if(isAllowed(Akses_List.TOOLS.get(), true)){
                try {
                    performLoadUtility(ActionEvent);
                } catch (IOException ex) {
                    loggingerror(ex);
                }
            }
        });
        
        btn_pos.setOnAction((javafx.event.ActionEvent event) -> {
            try {
                if(isAllowed(Akses_List.POS.get(), true)){
                    performLoadPOS(event);
                }
            } catch (IOException ex) {
                loggingerror(ex);
            }
        });
        
        img_logo.setOnMouseClicked((MouseEvent event) -> {
            if(isAllowed(Akses_List.PROFILE.get(), true)){
                String tUrl = "/fxml/main/Profile.fxml";
                loadForm(tUrl, "Profile");
            }
        });
        
        btn_logout.setOnAction((javafx.event.ActionEvent event) -> {
            if (isForceLogout) {
                resetSession();
                isStartRun = true;
                mTimer.stop();
                isForceLogout = false;
                ((Node) (event.getSource())).getScene().getWindow().hide();
                loadForm("/app/retail/fxml/login/FormLogin.fxml", "EZPOS Retail:: Version 1.0.0");
            } else {
                Optional<ButtonType> opt = EZSystem.showAllert(EZAlertType.CONFIRM, "Anda ingin logout?", EZIcon.ICON_STAGE);
                if (opt.get() == ButtonType.OK) {
                    resetSession();
                    isStartRun = true;
                    mTimer.stop();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    loadForm("/app/retail/fxml/login/FormLogin.fxml", "EZPOS Retail:: Version 1.0.0");
                }
            }
        });
        
        btn_report.setOnAction((javafx.event.ActionEvent event) -> {
            if(isAllowed(Akses_List.LAPORAN.get(), true)){
                try {
                    performLoadReport(event);
                } catch (IOException e) {
                    loggingerror(e);
                }
            }
        });
    }
    
    public void performLoadFinance(javafx.event.ActionEvent ActionEvent) throws IOException{
        final String  tUrl = "/app/retail/fxml/finance/Finance.fxml";
        loadForm(tUrl, this.getClass().getName(),ActionEvent);
    }
    
    public void performLoadpurchase(javafx.event.ActionEvent ActionEvent) throws IOException{
        final String  tUrl = "/app/retail/fxml/purchase/Purchase.fxml";
        loadForm(tUrl, this.getClass().getName(),ActionEvent);
    }
    
    public void performLoadStock(javafx.event.ActionEvent ActionEvent) throws IOException{
        final String  tUrl = "/app/retail/fxml/stock/Stock.fxml";
        loadForm(tUrl, this.getClass().getName(),ActionEvent);
    }
    
    public void performLoadUtility(javafx.event.ActionEvent ActionEvent) throws IOException{
        final String  tUrl = "/app/retail/fxml/utility/Utility.fxml";
        loadForm(tUrl, this.getClass().getName(),ActionEvent);
    }
    
    public void performLoadReport(javafx.event.ActionEvent ActionEvent) throws IOException{
        final String  tUrl = "/fxml/main/Report.fxml";
        loadForm(tUrl, this.getClass().getName(),ActionEvent);
    }
    
    public void performLoadPOS(javafx.event.ActionEvent ActionEvent) throws IOException{
        final String  tUrl = "/app/retail/fxml/pos/PosMaster.fxml";
        loadForm(tUrl, this.getClass().getName(),ActionEvent);
    }

    @Override
    public void loadForm(String pUrl, String pName, javafx.event.ActionEvent event) throws IOException {
        pnl_scroll.getChildren().clear();
        pnl_scroll.getChildren().add(FXMLLoader.load(getClass().getResource(pUrl)));
    }
    
    public void loadReport(javafx.event.ActionEvent event) throws IOException{
        double widthh = ((Node) event.getSource()).getScene().getWidth();
        double heightt = ((Node) event.getSource()).getScene().getHeight();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main/Report.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, widthh, heightt);
        stage.setTitle("Laporan");
        stage.setMaximized(true);
        stage.getIcons().add(new Image("/images/icons8_report_card_48px.png"));
        stage.setScene(scene);
        stage.show();
    }
    
    public void resetSession(){
        try {
            String tSQL = " UPDATE session SET session_end='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"' WHERE session_id='"+getSession()+"'";
            updateToDatabase(tSQL);
            setSession("");
        } catch (SQLException ex) {
            loggingerror(ex);
        }
    }
    
    private void setSession() throws SQLException{
        String tSQL = "SELECT b.nama_karyawan as user FROM session a INNER JOIN karyawan b ON a.kd_karyawan=b.kode_karyawan WHERE a.session_id='"+getSession()+"'";
        ResultSet res = selectFromDatabase(tSQL);
        if (res.next()) {
            txt_users.setText(res.getString("user"));
        }else{
            EZSystem.showAllert(EZAlertType.WARNING,"User tidak ditemukan!\nMohon login kembali", EZIcon.ICON_STAGE);            
            resetSession(); 
        }
        
    }

    @Override
    public void setToolTip() {
        btn_logout.setCursor(Cursor.HAND);
    }

    public JFXButton getBtn_logout() {
        return btn_logout;
    }

    @Override
    public void initializeState() {
        txt_copyright.setText("Develop by EZ Team @ 2020-"+EZDate.YEAR.today());
        if(isStartRun){
            try {
                setSession();
                String tSQL = "SELECT * FROM profile";
                ResultSet tResult = selectFromDatabase(tSQL);
                if (tResult.next()) {
                    mMapProfile = new HashMap<>();
                    txt_nama_tko.setText(tResult.getString("nama"));
                    mMapProfile.put("nama", tResult.getString("nama"));
                    txt_alamat.setText(tResult.getString("alamat")+","+tResult.getString("kota"));
                    mMapProfile.put("alamat", tResult.getString("alamat"));
                    mMapProfile.put("kota", tResult.getString("kota"));
                    txt_telp_fax.setText("Telp. "+tResult.getString("telp")+" Fax."+tResult.getString("fax"));
                    mMapProfile.put("kontak", "Telp. "+tResult.getString("telp")+" / Fax."+tResult.getString("fax"));
                    byte[]  imgLoad=tResult.getBytes("logo");
                    ByteArrayInputStream bis = new ByteArrayInputStream(imgLoad);
                    BufferedImage read = ImageIO.read(bis);
                    Image image = SwingFXUtils.toFXImage(read, null);
                    
                    ImageIcon logo;
                    if(imgLoad == null){
                        logo = new ImageIcon();
                    }else{
                        logo = new ImageIcon(imgLoad);
                    }
                    mMapProfile.put("logo", logo.getImage());
                    img_logo.setImage(image);
                    setmProfile(txt_nama_tko.getText());
                }
            } catch (SQLException | IOException ex) {
                loggingerror(ex);
            }
            isStartRun = false;
        }
        setmHomeController(this);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
