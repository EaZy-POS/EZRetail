/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard.thread;

import app.retail.controller.dashboard.DashboardController;
import app.retail.controller.dashboard.SaleController;
import app.retail.model.dashboard.SaleModell;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author Lutfi
 */
public class getDataSale extends Thread{
    
    public getDataSale() throws SQLException{
        new SaleController();
    }
    
    @Override
    public void run() {
        HashMap<String,SaleModell> mSaleModel = SaleController.getMAPSALEMODE();
        HashMap<String,SaleModell> mSaleMonthModel = SaleController.getMAPSALEMOUNTHMODE();
        HashMap<String,SaleModell> mSaleItemTerlarisModel = SaleController.getMAPITEMTERLARIS();
        DashboardController.setmSaleModel(mSaleModel);
        DashboardController.setmSaleMonthModel(mSaleMonthModel);
        DashboardController.setmSaleItemTerlarisModel(mSaleItemTerlarisModel);
    }
    
}
