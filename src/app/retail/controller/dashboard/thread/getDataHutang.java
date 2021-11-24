/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard.thread;

import app.retail.controller.dashboard.DashboardController;
import app.retail.controller.dashboard.HutangController;

/**
 *
 * @author Lutfi
 */
public class getDataHutang extends Thread{

    public getDataHutang() {
        HutangController hutangController = new HutangController();
    }

    @Override
    public void run() {
        DashboardController.setmHutangYear(HutangController.getMAPHUTANGYEAR());
        DashboardController.setmHutangSupplier(HutangController.getMAPHUTANGSUPPLIER());
    }
}
