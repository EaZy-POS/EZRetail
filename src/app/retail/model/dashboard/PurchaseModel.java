/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.dashboard;

/**
 *
 * @author Lutfi
 */
public class PurchaseModel {
    int bln,tgl;
    double total,totalmonth;

    public PurchaseModel(int bln, double total) {
        this.bln = bln;
        this.total = total;
    }

    public PurchaseModel(int bln, int tgl, double totalmonth) {
        this.bln = bln;
        this.tgl = tgl;
        this.totalmonth = totalmonth;
    }

    public int getBln() {
        return bln;
    }

    public void setBln(int bln) {
        this.bln = bln;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getTgl() {
        return tgl;
    }

    public void setTgl(int tgl) {
        this.tgl = tgl;
    }

    public double getTotalmonth() {
        return totalmonth;
    }

    public void setTotalmonth(double totalmonth) {
        this.totalmonth = totalmonth;
    }
    
}
