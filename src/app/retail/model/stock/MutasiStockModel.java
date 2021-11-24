/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.stock;

/**
 *
 * @author Lutfi
 */
public class MutasiStockModel {
    private String no,tanggal,sku,barcode,namaItem,sat,supplier,kategori,stokAwal,pembelian,stockOpname,stockOut,penjualan,stockAkhir,refund,konv, inversi;

    public MutasiStockModel(String no, String tanggal, String sku, String barcode, String namaItem, String sat, String supplier, 
            String kategori, String stokAwal, String pembelian, String stockOpname, String stockOut, String penjualan, String stockAkhir,
            String refund, String konv, String inversi) {
        this.no = no;
        this.tanggal = tanggal;
        this.sku = sku;
        this.barcode = barcode;
        this.namaItem = namaItem;
        this.sat = sat;
        this.supplier = supplier;
        this.kategori = kategori;
        this.stokAwal = stokAwal;
        this.pembelian = pembelian;
        this.stockOpname = stockOpname;
        this.stockOut = stockOut;
        this.penjualan = penjualan;
        this.stockAkhir = stockAkhir;
        this.refund = refund;
        this.konv=konv;
        this.inversi = inversi;
    }

    public String getInversi() {
        return inversi;
    }

    public void setInversi(String inversi) {
        this.inversi = inversi;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getKonv() {
        return konv;
    }

    public void setKonv(String konv) {
        this.konv = konv;
    }

    public String getStockOpname() {
        return stockOpname;
    }

    public void setStockOpname(String stockOpname) {
        this.stockOpname = stockOpname;
    }

    public String getStockOut() {
        return stockOut;
    }

    public void setStockOut(String stockOut) {
        this.stockOut = stockOut;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getStokAwal() {
        return stokAwal;
    }

    public void setStokAwal(String stokAwal) {
        this.stokAwal = stokAwal;
    }

    public String getPembelian() {
        return pembelian;
    }

    public void setPembelian(String pembelian) {
        this.pembelian = pembelian;
    }

    public String getPenjualan() {
        return penjualan;
    }

    public void setPenjualan(String penjualan) {
        this.penjualan = penjualan;
    }

    public String getStockAkhir() {
        return stockAkhir;
    }

    public void setStockAkhir(String stockAkhir) {
        this.stockAkhir = stockAkhir;
    }
    
}
