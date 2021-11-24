/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard;

import app.retail.controller.dashboard.thread.getDataCashTransaction;
import app.retail.controller.dashboard.thread.getDataHutang;
import app.retail.controller.dashboard.thread.getDataPiutang;
import app.retail.controller.dashboard.thread.getDataPurchase;
import app.retail.controller.dashboard.thread.getDataSale;
import app.retail.controller.general.General;
import app.retail.model.dashboard.PurchaseModel;
import app.retail.model.dashboard.SaleModell;
import app.retail.model.finance.CashTransactionModel;
import app.retail.model.finance.HutangModel;
import app.retail.model.finance.PiutangModel;
import app.retail.utility.Akses_List;
import app.retail.utility.EZDate;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Lutfi
 */
public class DashboardController extends General implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private LineChart<String, Number> saleChart;
    @FXML
    private NumberAxis yAxisSale;
    @FXML
    private CategoryAxis xAxisSale;
    private static SaleController saleControl ;
    private static PurchControler purchaseControl;
    private static HashMap<String,SaleModell> mSaleModel;
    private static HashMap<String,SaleModell> mSaleMonthModel;
    private static HashMap<String,SaleModell> mSaleItemTerlarisModel = new HashMap<>();
    private static HashMap<String,PurchaseModel> mPurchaseModel;
    private static HashMap<String,PurchaseModel> mPurchaseModelMonth;
    private static HashMap<String,HutangModel> mHutangYear ;
    private static HashMap<String,HutangModel> mHutangSupplier;
    private static HashMap<String,PiutangModel> mPiutangYear;
    private static HashMap<String,PiutangModel> mPiutangCust;
    private static HashMap<String,CashTransactionModel> mCashTransModel; 
    private static HashMap<String,CashTransactionModel> mCashTransInModel;
    @FXML
    private LineChart<String, Number> saleChartMonth;
    @FXML
    private NumberAxis yAxisSaleMonth;
    @FXML
    private CategoryAxis xAxisSaleMonth;
    @FXML
    private PieChart itemTerlarisChart;
    private final getDataSale mSale ;
    private final getDataPurchase mPurchase;
    private final getDataHutang mHutang;
    private final getDataPiutang mPiutang;
    private final getDataCashTransaction mCashTransaction;
    @FXML
    private LineChart<String, Number> hutangChart;
    @FXML
    private NumberAxis yAxisHutangPiutang;
    @FXML
    private CategoryAxis xAxishutangPiutang;
    @FXML
    private StackedBarChart<String, Number> hutangSupplierChart;
    @FXML
    private NumberAxis yAxisHutangSupplier;
    @FXML
    private CategoryAxis xAxisHutangsupplier;
    @FXML
    private StackedBarChart<String, Number> piutangCustomerChart;
    @FXML
    private NumberAxis yAxisPiutangCustomer;
    @FXML
    private CategoryAxis xAxisPiutangCustomer;
    @FXML
    private BarChart<String, Number> cashTransactionChart;
    @FXML
    private NumberAxis yAxisCashTransaction;
    @FXML
    private CategoryAxis xAxistCashtransaction;
    @FXML
    private VBox vx;
    @FXML
    private StackPane st;
    
    public DashboardController() throws SQLException{
        mSale = new getDataSale();
        mPurchase = new getDataPurchase();
        mHutang = new getDataHutang();
        mPiutang = new getDataPiutang();
        mCashTransaction = new getDataCashTransaction();
        if (isAllowed("Data Dashboard", false)) {
            mSale.start();
            mPurchase.start();
            mHutang.start();
            mPiutang.start();
            mCashTransaction.start();
        }
    }
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (isAllowed(Akses_List.DATA_DASHBOARD.get(), false)) {
            loadSaleChart();
            loadPurchaseChart();
            loadSaleMonthChart();
            loadTerlarisChart();
            loadHutangPiutangChart();
            loadCashTransactionChart();
            loadHutangSupplierChart();
            loadPiutangCustChart();
            vx.setVisible(false);
            st.setVisible(true);
        }else{
            vx.setVisible(true);
            st.setVisible(false);
            AnchorPane.setPrefHeight(getHeight() - 225);
        }
        initializeState();
    }    
    
    private void loadSaleChart() {
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        ObservableList lists = FXCollections.observableArrayList(months);
        XYChart.Series series = new XYChart.Series();
        saleChart.setTitle("Grapik Transaksi "+ EZDate.YEAR.today());
        
        mSaleModel.values().stream().forEach((model) -> {
            String month = months[model.getBln()-1];
            series.getData().add(new XYChart.Data(month, model.getSales()));
        });

        series.setName("Sales");
        xAxisSale.setCategories(lists);
        saleChart.getData().add(series);
    }
    
    private void loadHutangPiutangChart() {
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        ObservableList lists = FXCollections.observableArrayList(months);
        XYChart.Series series = new XYChart.Series();
        hutangChart.setTitle("Hutang Piutang "+EZDate.YEAR.today());
        
        mHutangYear.values().stream().forEach((model) -> {
            String month = months[Integer.parseInt(model.getBln())-1];
            series.getData().add(new XYChart.Data(month, model.getTotalHutang()));
        });

        series.setName("Total Hutang");
        
        xAxishutangPiutang.setCategories(lists);
        hutangChart.getData().add(series);
        XYChart.Series seriess = new XYChart.Series();
        mPiutangYear.values().stream().forEach((model) -> {
            String month = months[model.getBln()-1];
            seriess.getData().add(new XYChart.Data(month, model.getTotalPiutang()));
        });

        seriess.setName("Total Piutang");
        xAxishutangPiutang.setCategories(lists);
        hutangChart.getData().add(seriess);
    }
    
    private void loadCashTransactionChart() {
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        cashTransactionChart.setTitle("Grapich Cash Transaction Tahun "+ EZDate.YEAR.today());
        xAxistCashtransaction.setLabel("Bulan");       
        yAxisCashTransaction.setLabel("Total");

        XYChart.Series seriesOut= new XYChart.Series();
        seriesOut.setName("Cash Out ");
        if (mCashTransModel != null) {
            for (int i = 0; i < months.length; i++) {
                String bulan = months[i];
                String key = "" + (i + 1);
                if (mCashTransModel.containsKey(key)) {
                    CashTransactionModel model = mCashTransModel.get(key);
                    seriesOut.getData().add(new XYChart.Data(bulan, model.getTotalTransaction()));
                } else {
                    seriesOut.getData().add(new XYChart.Data(bulan, 0));
                }
            }
        }
        cashTransactionChart.getData().add(seriesOut);
        
        
        XYChart.Series seriesin= new XYChart.Series();
        seriesin.setName("Cash In ");
        if (mCashTransInModel != null) {
            for (int i = 0; i < 12; i++) {
                String bulan = months[i];
                String key = "" + (i + 1);
                if (mCashTransInModel.containsKey(key)) {
                    CashTransactionModel model = mCashTransInModel.get(key);
                    seriesin.getData().add(new XYChart.Data(bulan, model.getTotalTransaction()));
                } else {
                    seriesin.getData().add(new XYChart.Data(bulan, 0));
                }
            }
        }
        cashTransactionChart.getData().add(seriesin);
    }
    
    private void loadHutangSupplierChart() {
        hutangSupplierChart.setTitle("Grapik hutang per supplier");
        final ObservableList<String> supList = javafx.collections.FXCollections.observableArrayList();
        mHutangSupplier.keySet().stream().forEach((cust) -> {
            supList.add(cust);
        });
        xAxisHutangsupplier.setCategories(supList);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Hutang");
        
        mHutangSupplier.values().stream().forEach((model) -> {
            series.getData().add(new XYChart.Data<>(model.getNama(), model.getTotalHutang()));
        });
        hutangSupplierChart.getData().addAll(series);
        
        XYChart.Series<String, Number> seriesTerbayar = new XYChart.Series<>();
        seriesTerbayar.setName("Terbayar");
        mHutangSupplier.values().stream().forEach((model) -> {
            seriesTerbayar.getData().add(new XYChart.Data<>(model.getNama(), model.getTerBayar()));
        });
        hutangSupplierChart.getData().addAll(seriesTerbayar);
        
        XYChart.Series<String, Number> seriesSisa = new XYChart.Series<>();
        seriesSisa.setName("Sisa");
        mHutangSupplier.values().stream().forEach((model) -> {
            seriesSisa.getData().add(new XYChart.Data<>(model.getNama(), model.getSisa()));
        });
        hutangSupplierChart.getData().addAll(seriesSisa);
    }
    
    private void loadPiutangCustChart() {
        piutangCustomerChart.setTitle("Grapik piutang per pelanggan");
        final ObservableList<String> supList = javafx.collections.FXCollections.observableArrayList();
        mPiutangCust.keySet().stream().forEach((cust) -> {
            supList.add(cust);
        });
        xAxisPiutangCustomer.setCategories(supList);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Piutang");
        mPiutangCust.values().stream().forEach((model) -> {
            series.getData().add(new XYChart.Data<>(model.getPelanggan(), model.getTotalPiutang()));
        });
        piutangCustomerChart.getData().addAll(series);
        
        XYChart.Series<String, Number> seriesTerbayar = new XYChart.Series<>();
        seriesTerbayar.setName("Terbayar");
        mPiutangCust.values().stream().forEach((model) -> {
            seriesTerbayar.getData().add(new XYChart.Data<>(model.getPelanggan(), model.getTerbayar()));
        });
        piutangCustomerChart.getData().addAll(seriesTerbayar);
        
        XYChart.Series<String, Number> seriesSisa = new XYChart.Series<>();
        seriesSisa.setName("Sisa");
        mPiutangCust.values().stream().forEach((model) -> {
            seriesSisa.getData().add(new XYChart.Data<>(model.getPelanggan(), model.getSisa()));
        });
        piutangCustomerChart.getData().addAll(seriesSisa);
    }
    
    private void loadPurchaseChart() {
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        XYChart.Series series = new XYChart.Series();        
        mPurchaseModel.values().stream().forEach((model) -> {
            if(model.getBln()>0){
                String month = months[model.getBln()-1];
                series.getData().add(new XYChart.Data(month, model.getTotal()));
            }
        });

        series.setName("Purchase");
        saleChart.getData().add(series);
    }
    
    private void loadSaleMonthChart() {
        String[] dates = getDateMounth();
        ObservableList lists = FXCollections.observableArrayList(dates);
        XYChart.Series series = new XYChart.Series();
        saleChartMonth.setTitle("Grapik Transaksi Bulan "+ EZDate.MONTHYEAR.today());
        xAxisSaleMonth.setCategories(lists);
        mSaleMonthModel.values().stream().forEach((model) -> {
            int tgl = model.getTgl();
            if(tgl>0){
                String date = dates[model.getTgl()-1];
                series.getData().add(new XYChart.Data(date, model.getSalesmonth()));
            }
        });
        series.setName("Sales");        
        saleChartMonth.getData().add(series);
        
        XYChart.Series seriespurc = new XYChart.Series();
        mPurchaseModelMonth.values().stream().forEach((model)->{
            int tgl = model.getTgl();
            if(tgl>0){
                String date = dates[model.getTgl()-1];
                seriespurc.getData().add(new XYChart.Data(date, model.getTotalmonth()));
            }
        });
        
        seriespurc.setName("Purchase");
        saleChartMonth.getData().add(seriespurc);
    }
    
    private String[] getDateMounth(){
        Calendar cal = Calendar.getInstance();
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String[] tValue = new String[maxDay];
        for (int i = 0; i < maxDay; i++) {
            cal.set(Calendar.DAY_OF_MONTH, i + 1);
            tValue[i]=df.format(cal.getTime());            
        }
        return tValue;
    }
    
    private void loadTerlarisChart(){
    
        ObservableList<PieChart.Data> lists = FXCollections.observableArrayList();
        
        mSaleItemTerlarisModel.values().stream().forEach((model) -> {
            String nama = model.getNamaitem();
            if (nama.length()>15) {
                nama = nama.substring(0, 15);
            }
            lists.add(new PieChart.Data(nama, model.getQty()));
        });
        itemTerlarisChart.setTitle("10 Item Terlaris Bulan "+ EZDate.YEAR.today());
        itemTerlarisChart.getData().addAll(lists);
    }
    
    public static SaleController getSaleControl() {
        return saleControl;
    }

    public static void setSaleControl(SaleController saleControl) {
        DashboardController.saleControl = saleControl;
    }

    public static PurchControler getPurchaseControl() {
        return purchaseControl;
    }

    public static void setPurchaseControl(PurchControler purchaseControl) {
        DashboardController.purchaseControl = purchaseControl;
    }

    public static HashMap<String, SaleModell> getmSaleModel() {
        return mSaleModel;
    }

    public static void setmSaleModel(HashMap<String, SaleModell> mSaleModel) {
        DashboardController.mSaleModel = mSaleModel;
    }

    public static HashMap<String, SaleModell> getmSaleMonthModel() {
        return mSaleMonthModel;
    }

    public static void setmSaleMonthModel(HashMap<String, SaleModell> mSaleMonthModel) {
        DashboardController.mSaleMonthModel = mSaleMonthModel;
    }

    public static HashMap<String, SaleModell> getmSaleItemTerlarisModel() {
        return mSaleItemTerlarisModel;
    }

    public static void setmSaleItemTerlarisModel(HashMap<String, SaleModell> mSaleItemTerlarisModel) {
        DashboardController.mSaleItemTerlarisModel = mSaleItemTerlarisModel;
    }

    public static HashMap<String, PurchaseModel> getmPurchaseModel() {
        return mPurchaseModel;
    }

    public static void setmPurchaseModel(HashMap<String, PurchaseModel> mPurchaseModel) {
        DashboardController.mPurchaseModel = mPurchaseModel;
    }

    public static HashMap<String, PurchaseModel> getmPurchaseModelMonth() {
        return mPurchaseModelMonth;
    }

    public static void setmPurchaseModelMonth(HashMap<String, PurchaseModel> mPurchaseModelMonth) {
        DashboardController.mPurchaseModelMonth = mPurchaseModelMonth;
    }

    public static HashMap<String, HutangModel> getmHutangYear() {
        return mHutangYear;
    }

    public static void setmHutangYear(HashMap<String, HutangModel> mHutangYear) {
        DashboardController.mHutangYear = mHutangYear;
    }

    public static HashMap<String, PiutangModel> getmPiutangYear() {
        return mPiutangYear;
    }

    public static void setmPiutangYear(HashMap<String, PiutangModel> mPiutangYear) {
        DashboardController.mPiutangYear = mPiutangYear;
    }

    public static HashMap<String, CashTransactionModel> getmCashTransModel() {
        return mCashTransModel;
    }

    public static void setmCashTransModel(HashMap<String, CashTransactionModel> mCashTransModel) {
        DashboardController.mCashTransModel = mCashTransModel;
    }

    public static HashMap<String, CashTransactionModel> getmCashTransInModel() {
        return mCashTransInModel;
    }

    public static void setmCashTransInModel(HashMap<String, CashTransactionModel> mCashTransInModel) {
        DashboardController.mCashTransInModel = mCashTransInModel;
    }

    public static HashMap<String, HutangModel> getmHutangSupplier() {
        return mHutangSupplier;
    }

    public static void setmHutangSupplier(HashMap<String, HutangModel> mHutangSupplier) {
        DashboardController.mHutangSupplier = mHutangSupplier;
    }

    public static HashMap<String, PiutangModel> getmPiutangCust() {
        return mPiutangCust;
    }

    public static void setmPiutangCust(HashMap<String, PiutangModel> mPiutangCust) {
        DashboardController.mPiutangCust = mPiutangCust;
    }

    @Override
    public void initializeState() {
        AnchorPane.setPrefWidth(getWidth() - 200);
    }

    @Override
    public void setButtonListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setToolTip() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadForm(String pUrl, String pName, ActionEvent event) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
