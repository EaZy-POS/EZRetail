/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard.thread;

import app.retail.controller.dashboard.CashTransactionController;
import app.retail.controller.dashboard.DashboardController;

/**
 *
 * @author Lutfi
 */
public class getDataCashTransaction extends Thread{

    public getDataCashTransaction(){
        CashTransactionController m = new CashTransactionController();
    }
    
    @Override
    public void run() {
        DashboardController.setmCashTransModel(CashTransactionController.getMAPCASHTRANSOUT());
        DashboardController.setmCashTransInModel(CashTransactionController.getMAPCASHTRANIN());
    }

}
