/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.dashboard.thread;

import app.retail.controller.dashboard.DashboardController;
import app.retail.controller.dashboard.PiutangController;

/**
 *
 * @author Lutfi
 */
public class getDataPiutang extends Thread{

    public getDataPiutang() {
        PiutangController piutangController = new PiutangController();
    }

    @Override
    public void run() {
        DashboardController.setmPiutangYear(PiutangController.getMAPPIUTANGYEAR());
        DashboardController.setmPiutangCust(PiutangController.getMAPPIUTANGCUST());
    }
    
}
