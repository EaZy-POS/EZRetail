/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard.thread;

import app.retail.controller.dashboard.DashboardController;
import app.retail.controller.dashboard.PurchControler;
import app.retail.model.dashboard.PurchaseModel;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author Lutfi
 */
public class getDataPurchase extends Thread{
    private static HashMap<String,PurchaseModel> mPurchaseModel;
    private static HashMap<String,PurchaseModel> mPurchaseModelMonth;

    public getDataPurchase() throws SQLException {
        PurchControler purchControler = new PurchControler();
    }

    @Override
    public synchronized void start() {
        mPurchaseModel = PurchControler.getMAPPURCHASE();
        mPurchaseModelMonth = PurchControler.getMAPPURCHASEMONTH();
        DashboardController.setmPurchaseModel(mPurchaseModel);
        DashboardController.setmPurchaseModelMonth(mPurchaseModelMonth);
    }
    
    
}
