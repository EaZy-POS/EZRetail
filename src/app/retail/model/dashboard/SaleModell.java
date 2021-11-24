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
public class SaleModell {
    private int bln,tgl,qty;
    private double sales,salesmonth;
    private String kodeitem,namaitem;
    

    public SaleModell(int bln, double sales) {
        this.bln = bln;
        this.sales = sales;
    }

    public SaleModell(int tgl, double sales, double salesmonth) {
        this.tgl = tgl;
        this.salesmonth = salesmonth;
    }

    public SaleModell(int qty, String kodeitem, String namaitem) {
        this.qty = qty;
        this.kodeitem = kodeitem;
        this.namaitem = namaitem;
    }

    public int getTgl() {
        return tgl;
    }

    public void setTgl(int tgl) {
        this.tgl = tgl;
    }

    public double getSalesmonth() {
        return salesmonth;
    }

    public void setSalesmonth(double salesmonth) {
        this.salesmonth = salesmonth;
    }

    
    public int getBln() {
        return bln;
    }

    public void setBln(int bln) {
        this.bln = bln;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getKodeitem() {
        return kodeitem;
    }

    public void setKodeitem(String kodeitem) {
        this.kodeitem = kodeitem;
    }

    public String getNamaitem() {
        return namaitem;
    }

    public void setNamaitem(String namaitem) {
        this.namaitem = namaitem;
    }
    
    
    
}
