///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package app.retail.controller.profile;
//
//import appcore.model.master.ProfileModel;
//import systemlog.SystemLog;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Optional;
//import java.util.ResourceBundle;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javafx.embed.swing.SwingFXUtils;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.ButtonType;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.text.Text;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import javax.imageio.ImageIO;
//import qs.QS;
//
///**
// * FXML Controller class
// *
// * @author Lutfi
// */
//public class ProfileController implements Initializable {
//
//    @FXML
//    private AnchorPane AnchorPane;
//    @FXML
//    private TextArea txt_almt;
//    @FXML
//    private TextField txt_kota;
//    @FXML
//    private TextField txt_telp;
//    @FXML
//    private TextField txt_fax;
//    @FXML
//    private Button btn_simpan;
//    @FXML
//    private TextField txt_nama_usaha;
//    @FXML
//    private Button btn_cancel;
//    @FXML
//    private Text lbl_close;
//    @FXML
//    private ImageView img_logo;
//    @FXML
//    private Button btn_browse;
//    private final FileChooser fileChooser = new FileChooser();
//    private final ProfileModel mProfileModel = new ProfileModel();
//    private final QS QUERY_SERVICES = new QS();
//    private static boolean isEdit;
//    private static HomeController mHomeController;
//
//    /**
//     * Initializes the controller class.
//     * @param url
//     * @param rb
//     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        try {
//            // TODO
//            SystemLog.LOG.writeLogInit(getClass().getName());
//            if(!HomeController.isAllowed("Edit Profile")){
//                btn_browse.setDisable(true);
//                btn_simpan.setDisable(true);
//            }
//            setFilterChoosher();
//            setListener();
//            loadData();
//            SystemLog.LOG.writeLogAfterInit(getClass().getName());
//        } catch (IOException ex) {
//            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }    
//    
//    private void setFilterChoosher(){
//        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
//        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
//        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
//    }
//    
//    private void setListener(){
//        lbl_close.setOnMouseEntered((MouseEvent event) -> {
//            lbl_close.setFill(javafx.scene.paint.Color.BLUE);
//        });
//        
//        lbl_close.setOnMouseExited((MouseEvent event) -> {
//            lbl_close.setFill(javafx.scene.paint.Color.BLACK);
//        });
//        
//        lbl_close.setOnMouseClicked((MouseEvent event) -> {
//            ((Node) (event.getSource())).getScene().getWindow().hide();
//        });
//        
//        btn_browse.setOnAction((final ActionEvent e) -> {
//            Stage stage = (Stage) AnchorPane.getScene().getWindow();
//            File file = fileChooser.showOpenDialog(stage);
//            if (file != null) {
//                Image image1 = new Image(file.toURI().toString());
//                String imageUrl = file.getPath();
//                img_logo.setImage(image1);
//                mProfileModel.setImageurl(imageUrl);
//            }
//        }); 
//        
//        btn_simpan.setOnAction((ActionEvent event) -> {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            Optional<ButtonType> opt = showAllert(alert, "Konfirmasi", "Simpan data?");
//            if (opt.get() ==  ButtonType.OK) {
//                try {
//                    mProfileModel.setNama_usaha(txt_nama_usaha.getText());
//                    mProfileModel.setAlmt(txt_almt.getText());
//                    mProfileModel.setKota(txt_kota.getText());
//                    mProfileModel.setTelp(txt_telp.getText());
//                    mProfileModel.setFax(txt_fax.getText());
//                    String tSQL ="";
//                    PreparedStatement tPStatment = null ;
//                    if(!isEdit){
//                        tSQL = "INSERT INTO tbl_profile (id,nama,alamat,kota,telp,fax,logo) VALUES (UUID(),?,?,?,?,?,?)";
//                        tPStatment = QUERY_SERVICES.sendQueryInsert(tSQL);
//                        tPStatment.setString(1, mProfileModel.getNama_usaha());
//                        tPStatment.setString(2, mProfileModel.getAlmt());
//                        tPStatment.setString(3, mProfileModel.getKota());
//                        tPStatment.setString(4, mProfileModel.getTelp());
//                        tPStatment.setString(5, mProfileModel.getFax());
//                        InputStream tInputStream = new FileInputStream(mProfileModel.getImageurl());
//                        tPStatment.setBlob(6, tInputStream);
//                    }else{
//                        if(mProfileModel.getImageurl()!=null){
//                            tSQL = "UPDATE tbl_profile SET nama=?,alamat=?,kota=?,telp=?,fax=?,logo=?";
//                            tPStatment = QUERY_SERVICES.sendQueryInsert(tSQL);
//                            tPStatment.setString(1, mProfileModel.getNama_usaha());
//                            tPStatment.setString(2, mProfileModel.getAlmt());
//                            tPStatment.setString(3, mProfileModel.getKota());
//                            tPStatment.setString(4, mProfileModel.getTelp());
//                            tPStatment.setString(5, mProfileModel.getFax());
//                            InputStream tInputStream = new FileInputStream(mProfileModel.getImageurl());
//                            tPStatment.setBlob(6, tInputStream);
//                        }else{
//                            tSQL = "UPDATE tbl_profile SET nama=?,alamat=?,kota=?,telp=?,fax=?";
//                            tPStatment = QUERY_SERVICES.sendQueryInsert(tSQL);
//                            tPStatment.setString(1, mProfileModel.getNama_usaha());
//                            tPStatment.setString(2, mProfileModel.getAlmt());
//                            tPStatment.setString(3, mProfileModel.getKota());
//                            tPStatment.setString(4, mProfileModel.getTelp());
//                            tPStatment.setString(5, mProfileModel.getFax());
//                        }
//                    }
//                    
//                    QUERY_SERVICES.executePrepareStatement(tPStatment);
//                    alert = new Alert(Alert.AlertType.INFORMATION);
//                    showAllert(alert, "Informasi", "Data berhasil disimpan");
//                    loadData();
//                    HomeController.isStartRun = true;
//                    mHomeController.awal();
//                } catch (SQLException | FileNotFoundException ex) {
//                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//    }
//    
//    private void loadData() throws IOException{
//        try {
//            String tSQL = "SELECT * FROM tbl_profile";
//            ResultSet tResult = QUERY_SERVICES.sendQuery(tSQL);
//            if (tResult.next()) {
//                isEdit = true;
//                txt_nama_usaha.setText(tResult.getString("nama"));
//                txt_almt.setText(tResult.getString("alamat"));
//                txt_kota.setText(tResult.getString("kota"));
//                txt_telp.setText(tResult.getString("telp"));
//                txt_fax.setText(tResult.getString("fax"));
//                byte[]  imgLoad=tResult.getBytes("logo");
//                ByteArrayInputStream bis = new ByteArrayInputStream(imgLoad);
//                BufferedImage read = ImageIO.read(bis);
//                Image image = SwingFXUtils.toFXImage(read, null);
//                img_logo.setImage(image);
//            }else{
//                isEdit=false;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public static HomeController getmHomeController() {
//        return mHomeController;
//    }
//
//    public static void setmHomeController(HomeController mHomeController) {
//        ProfileController.mHomeController = mHomeController;
//    }
//    
//    public Optional<ButtonType> showAllert(Alert alert, String judul, String pesan){
//        alert.setTitle(judul);
//        alert.setContentText(pesan);
//        alert.setHeaderText(null);
//        alert.setWidth(100);
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("/appcore/images/icons8_notification_24px_2.png")));        
//        return alert.showAndWait();
//    }
//    
//}
