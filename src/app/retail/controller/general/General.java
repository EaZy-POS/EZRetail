/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.general;

import app.retail.controller.home.HomeController;
import app.retail.controller.master.MasterKategoriController;
import app.retail.controller.master.MasterSupplierController;
import app.retail.controller.pos.POSController;
import app.retail.controller.pos.PosMaster;
import app.retail.controller.utility.UtilityController;
import app.retail.model.master.ItemModel;
import app.retail.model.purchase.POModel;
import app.retail.model.stock.StockModel;
import app.retail.utility.EZAlertType;
import app.retail.utility.EZButtonType;
import app.retail.utility.EZDate;
import app.retail.utility.EZIcon;
import app.retail.utility.EZReport;
import app.retail.utility.EZSystem;
import co.id.eazy.query.service.QueryService;
import co.id.eazy.query.service.RecordEntry;
import co.id.eazy.system.log.LogType;
import co.id.eazy.system.log.SystemLog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRException;
import org.json.JSONObject;

/**
 *
 * @author RCS
 */
public abstract class General extends Thread{
    public static final QueryService QS = new QueryService();
    private static HomeController mHomeController;
    private static POSController mPosController;
    private static PosMaster mPosMaster;
    private static MasterSupplierController mSupplierControler;
    private static MasterKategoriController mKategoriController;
    private static UtilityController mUtilityController;
    public static boolean isForceLogout;
    public static HashMap<String, String> mListParameter;
    
    private static double height,width ;
    public static String mUrlForm, session, mUrlChild;
    public double xOffset = 0;
    public double yOffset = 0;
    public static boolean isRunThread = true;
    public static boolean isStartRun = true;
    public static String mProfile;
    public static LinkedList<String> hakAksesList ;
    public static Map<String, Object> mMapProfile;
    public static Map<String, String> mMapKaryawan;
    public static Map<String, String> mMapPelanggan;
    
    public abstract void initializeState();
    public abstract void setButtonListener();
    public abstract void clear();
    public abstract void setToolTip();
    public abstract void loadForm(String pUrl, String pName, ActionEvent event) throws IOException;
    
    public General(){
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        setHeight(screenBounds.getHeight());
        setWidth(screenBounds.getWidth());
        runEventMySQL();
    }
    
    private void runEventMySQL(){
        try {
            String tSQL = "SET GLOBAL event_scheduler = ON";
            updateToDatabase(tSQL);
        } catch (SQLException e) {
        }
    }
    
    public static boolean isAllowed(String akses, boolean show){
        if(hakAksesList.contains("["+akses+"]") || hakAksesList.contains("*****")){
            return true;
        }else{
            if(show){
                notAllowedNotif();
            }
            return false;
        }
    }
    
    public static boolean isAllowed(Enum akses, boolean show){
        if(hakAksesList.contains("["+akses.toString()+"]") || hakAksesList.contains("*****")){
            return true;
        }else{
            if(show){
                notAllowedNotif();
            }
            return false;
        }
    }
    
    public static boolean isAllowed(String akses){
        return hakAksesList.contains("["+akses+"]") || hakAksesList.contains("*****");
    }
    
    public static boolean isAllowed(Enum akses){
        return hakAksesList.contains("["+akses.toString()+"]") || hakAksesList.contains("*****");
    }
    
    public static String genereateID(int length){
        return UUID.randomUUID().toString().replace("-", "").substring(0, length).toUpperCase();
    }

    public static HomeController getmHomeController() {
        General.isForceLogout = false;
        return mHomeController;
    }
    
    public static HomeController getmHomeController(boolean force) {
        General.isForceLogout = force;
        return mHomeController;
    }

    public static void setmHomeController(HomeController mHomeController) {
        General.mHomeController = mHomeController;
    }

    public static UtilityController getmUtilityController() {
        return mUtilityController;
    }

    public static void setmUtilityController(UtilityController mUtilityController) {
        General.mUtilityController = mUtilityController;
    }

    public double getxOffset() {
        return xOffset;
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }
    
    public static String getmUrlForm() {
        return mUrlForm;
    }

    public static void setmUrlForm(String mUrlForm) {
        General.mUrlForm = mUrlForm;
    }

    public static String getmUrlChild() {
        return mUrlChild;
    }

    public static void setmUrlChild(String mUrlChild) {
        General.mUrlChild = mUrlChild;
    }
    
    public static double getHeight() {
        return height;
    }

    public static void setHeight(double height) {
        General.height = height;
    }

    public static double getWidth() {
        return width;
    }

    public static void setWidth(double width) {
        General.width = width;
    }

    public static boolean isIsRunThread() {
        return isRunThread;
    }

    public static void setIsRunThread(boolean isRunThread) {
        General.isRunThread = isRunThread;
    }

    public static boolean isIsStartRun() {
        return isStartRun;
    }

    public static void setIsStartRun(boolean isStartRun) {
        General.isStartRun = isStartRun;
    }

    public static String getmProfile() {
        return mProfile;
    }

    public static void setmProfile(String mProfile) {
        General.mProfile = mProfile;
    }

    public static String getSession() {
        return session;
    }

    public static void setSession(String session) {
        General.session = session;
    }

    public static LinkedList<String> getHakAksesList() {
        return hakAksesList;
    }

    public static void setHakAksesList(LinkedList<String> hakAksesList) {
        General.hakAksesList = hakAksesList;
    }
    
    public static int insertToDatabase(RecordEntry recordEntry, String table) throws SQLException{
        return QS.sendQueryInsert(recordEntry, table);
    }
    
    public static ResultSet selectFromDatabase(String tQuery) throws SQLException{
        return QS.executeQuery(tQuery);
    }
    
    public static List<JSONObject> queryToList(String tQuery) throws SQLException{
        return QS.executeQueryToList(tQuery);
    }
    
    public static int updateToDatabase(String tQuery) throws SQLException{
        return QS.executeQueryUpdate(tQuery);
    }
    
    public static int updateToDatabase(RecordEntry recordEndtry, String table, String where) throws SQLException{
        return QS.sendQueryUpdate(recordEndtry, table, where);
    }
    
    public JFXButton getButton(EZButtonType type, String lable){
        JFXButton button = new JFXButton();
        Image image = new Image(getClass().getResourceAsStream(type.getButtonIcon()));
        button.setCursor(Cursor.HAND);
        button.setText(lable);
        button.setGraphic(new ImageView(image));
        return button;
    }
    
    public static void setLimitTexfield(TextField textField, int max){
        textField.setOnKeyTyped((KeyEvent event) -> {
            if (textField.getText().length()>=max) {
                event.consume();
            }
        });
    }
    
    public static void setNumericTextfield(TextField pTextField) {
        pTextField.setOnKeyTyped((KeyEvent evt) -> {
            String karakter = evt.getCharacter();
            String tRegex = "([0-9])";
            boolean isNumerik = karakter.matches(tRegex);
            if (!((isNumerik) || karakter.equals(KeyCode.BACK_SPACE) || karakter.equals(KeyCode.DELETE))) {
                evt.consume();
            }
        });
        
    }
    
    public static void setCurencyTextfield(TextField pTextField) {
        
        pTextField.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                String curText = pTextField.getText();
                pTextField.setText(curText.substring(0, curText.length() - 1));
                String currentText = pTextField.getText().replaceAll("\\.", "").replace(",", "");
                double longVal = 0;
                if (currentText.length() > 0) {
                    longVal = Double.parseDouble(currentText);
                }
                setTxtCurency(pTextField, longVal);
            }
        });

        pTextField.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.DELETE)) {
                String currentText = pTextField.getText().replaceAll("\\.", "").replace(",", "");
                double longVal = 0;
                if (currentText.length() > 0) {
                    longVal = Double.parseDouble(currentText);
                }
                setTxtCurency(pTextField, longVal);
            }
        });

        pTextField.setOnKeyTyped(event -> {
            if (pTextField.getText().replace(",", "").length() >= 12) {
                event.consume();
            } else {
                event.consume();
                if (event.getCharacter().matches("\\d*")) {
                    String currentText = pTextField.getText().replaceAll("\\.", "").replace(",", "");
                    double longVal = 0;
                    if (currentText.length() > 0) {
                        longVal = Double.parseDouble(currentText.concat(event.getCharacter()));
                    }
                    setTxtCurency(pTextField, longVal);
                }
            }
        });

        pTextField.setOnMouseClicked((MouseEvent event) -> {
            String currentText = pTextField.getText().replaceAll("\\.", "").replace(",", "");
            double longVal = 0;
            if (currentText.length() > 0) {
                longVal = Double.parseDouble(currentText);
            }
            setTxtCurency(pTextField, longVal);
        });
    }

    private static void setTxtCurency(TextField pTextField, double val) {
        pTextField.setText(new DecimalFormat("#,##0").format(val));
    }
    
    public static String formatRupiah(double val) {
        return new DecimalFormat("#,##0").format(val);
    }
    
    public void setAligmentColoum(TableColumn pColom, Pos pPosVale){
        pColom.setCellFactory(new Callback<TableColumn<ItemModel, String>, TableCell<ItemModel, String>>() {
            @Override
            public TableCell<ItemModel, String> call(TableColumn<ItemModel, String> param) {
                return new TableCellFormat(pPosVale);
            }
        });
    }
    
    private class TableCellFormat extends TableCell<ItemModel, String>{
        Pos posVal;
        public TableCellFormat(Pos posVale){
            this.posVal=posVale;
        }
        
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty); 
            this.setText(item);
            this.setAlignment(posVal);
        }
    }
    
    public void setDate(JFXDatePicker pDatePicker) {
        pDatePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(EZDate.FORMAT_2.get());
                @Override 
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override 
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            }
        );
        pDatePicker.setValue(LocalDate.now());
    }
    
    public String getDate(JFXDatePicker datePicker){
        DateFormat tFormat = new SimpleDateFormat(EZDate.SQLDATE.get());
        LocalDate localDate = datePicker.getValue();
        Calendar tCal =  Calendar.getInstance();
        tCal.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        Date date = tCal.getTime();
        return tFormat.format(date);
    }
    
    public String getDate(JFXDatePicker datePicker, EZDate Format){
        DateFormat tFormat = new SimpleDateFormat(Format.get());
        LocalDate localDate = datePicker.getValue();
        Calendar tCal =  Calendar.getInstance();
        tCal.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        Date date = tCal.getTime();
        return tFormat.format(date);
    }
    
    public static String getStackTraceString(Exception e){
        String tMessage = "\n[Exception: "+e.getMessage()+"]\n";
        StackTraceElement[] tElement = e.getStackTrace();
        for (StackTraceElement stackTraceElement : tElement) {
            String tClas = stackTraceElement.getClassName();
            String tMethode = stackTraceElement.getMethodName();
            int line = stackTraceElement.getLineNumber();
            tMessage = tMessage.concat(" [Stacktrace:("+tClas+":"+tMethode+":"+line+")]\n");
        }
        return tMessage;
    }
    
    public void setAccelerator(final Button button) {
        Scene scene = button.getScene();
        if (scene == null) {
            throw new IllegalArgumentException("setSaveAccelerator must be called when a button is attached to a scene");
        }

        scene.setOnKeyPressed((KeyEvent event)->{
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("you presss enter");
            }
        });
    }
    
    public void loggingerror(Exception ex){
        String tMessage = "ERROR: " + ex.getMessage();
        EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), tMessage, EZIcon.ICON_STAGE);
        SystemLog.getLogger().writeLog(LogType.ERROR, tMessage + getStackTraceString(ex));
//        initializeState();
    }
    
    private static void notAllowedNotif(){
        EZSystem.showAllert(EZAlertType.WARNING, "Anda tidak mempunyai akses!", EZIcon.ICON_STAGE);
    }
    
    public String hitungGrandTotal(TableView pTable, int pColom){
        int index = pTable.getItems().size();
        if(index>0){
            double tValue = 0;
            TableColumn<POModel, String> column = (TableColumn<POModel, String>) pTable.getColumns().get(pColom);
            for (int i = 0; i < index; i++) {
                tValue+=Double.valueOf(column.getCellData(i).replace(",", ""));
            }
            return formatRupiah(tValue);
        }
        return "0";
    }
    
    public void setDate(JFXDatePicker pDatePicker, Date pDate) {
        Instant instant = Instant.ofEpochMilli(pDate.getTime()); 
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()); 
        LocalDate localDate = localDateTime.toLocalDate(); 
        pDatePicker.setValue(localDate);
    }
    
    public void showReport(String pReportFile){
        try {
            new EZReport(pReportFile, QS.getConnection()).showReport();
        } catch (JRException | ClassNotFoundException e) {
            loggingerror(e);
        }
    }
    
    public void showReport(String pReportFile, String Query){
        try {
            new EZReport(pReportFile, QS.getConnection()).showReport(Query, mMapProfile);
        } catch (JRException | ClassNotFoundException e) {
            loggingerror(e);
        }
    }
    
    public void showReport(String pReportFile, String Query, Map<String, Object> pMap){
        try {
            new EZReport(pReportFile, QS.getConnection()).showReport(Query, pMap);
        } catch (JRException | ClassNotFoundException e) {
            loggingerror(e);
        }
    }
    
    public void setMNemonic(Button btn, KeyCode key){
        KeyCombination kc = new KeyCodeCombination(key, KeyCombination.CONTROL_ANY);
        Mnemonic mn = new Mnemonic(btn, kc);
    }

    public static POSController getmPosController() {
        return mPosController;
    }

    public static void setmPosController(POSController mPosController) {
        General.mPosController = mPosController;
    }
   
    public String generateRefnum(String table, String field, String patern){
        String tKdAwal = patern;
        String tFaktur = null;

        try {
            String tSQL = "SELECT "+field+" FROM "+table+" WHERE "+field+" like '"+tKdAwal+"%' ORDER BY "+field+" DESC LIMIT 1";
            
            ResultSet tRes = selectFromDatabase(tSQL);
            if(tRes.next()){
                String tLastFaktur = tRes.getString(field);
                int number = Integer.parseInt(tLastFaktur.substring(tKdAwal.length(), tLastFaktur.length())) + 1;
                String newNumber = String.valueOf(number);
                if(newNumber.length() > 5){
                    EZSystem.showAllert(new Alert(Alert.AlertType.ERROR), "Kode Overload", EZIcon.ICON_STAGE);
                    tFaktur = tKdAwal+"00000";
                }else{
                    while (newNumber.length() < 5) {
                        newNumber = "0"+newNumber;
                    }
                    
                    tFaktur = tKdAwal+newNumber;
                }
            }else{
                tFaktur = tKdAwal+"00001";
            }
            
            
        } catch (SQLException | NumberFormatException ex) {
            loggingerror(ex);
        }
        
        return tFaktur;
    }
    
    public void loadForm(String pUrl, String pName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pUrl));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            root.setOnMousePressed((MouseEvent e) -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent e) -> {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(pName);
            stage.getIcons().add(EZIcon.ICON_APPS.get());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            loggingerror(ex);
        }
    }

    public static MasterKategoriController getmKategoriController() {
        return mKategoriController;
    }

    public static void setmKategoriController(MasterKategoriController mKategoriController) {
        General.mKategoriController = mKategoriController;
    }

    public static MasterSupplierController getmSupplierControler() {
        return mSupplierControler;
    }

    public static void setmSupplierControler(MasterSupplierController mSupplierControler) {
        General.mSupplierControler = mSupplierControler;
    }

    public static PosMaster getmPosMaster() {
        return mPosMaster;
    }

    public static void setmPosMaster(PosMaster mPosMaster) {
        General.mPosMaster = mPosMaster;
    }
    
    public void setWrapingColumnTable(TableColumn column, Pos poVal) {
        column.setCellFactory(param -> {
            return new TableCell<StockModel, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        Text text = new Text(item);
                        if (null != poVal) switch (poVal) {
                            case CENTER_LEFT:
                                text.setStyle("-fx-text-alignment:justify;");
                                break;
                            case CENTER:
                                text.setStyle("-fx-text-alignment:center;");
                                break;
                            case CENTER_RIGHT:
                                text.setStyle("-fx-text-alignment:right;");
                                break;
                            default:
                                break;
                        }
                        
                        text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(5));
                        setGraphic(text);
                    }
                }
            };
        });
    }
    
    public void setColumnWithProperty(TableView table, TableColumn column, double width, boolean isWrapping, Pos pos){
        column.minWidthProperty().bind(table.widthProperty().multiply(width));
        column.prefWidthProperty().bind(table.widthProperty().multiply(width));
        if (isWrapping) {
            setWrapingColumnTable(column, pos);
        }
    }
}
