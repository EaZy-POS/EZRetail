/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility;

/**
 *
 * @author RCS
 */
public enum Akses_List {
    INPUT_DATA_USER_AKSES("Input Data User Akses"),
    INPUT_DATA_USER("Input Data User"),
    PRINT_DATA_STOCK_OUT("Print Data Stock Out"),
    VIEW_DETAIL_DATA_STOCK_OPNAME("View Data Stock Opname"),
    PRINT_DATA_STOCK_OPNAME("Print Data Stock Opname"),
    INPUT_DATA_PELANGGAN("Input Data Pelanggan"),
    INPUT_DATA_KARYAWAN("Input Data Karyawan"),
    PROFILE("Profile"),
    EDIT_PROFILE("Edit Profile"),
    DASHBOARD("Dashboard"),
    DATA_DASHBOARD("Data Dashboard"),
    DATA("Data"),
    EDIT_DATA_SATUAN("Edit Data Satuan"),
    DATA_SUPPLIER("Data Supplier"),
    DATA_KATEGORI("Data Kategori"),
    DATA_SUB_KATEGORI("Data Sub Kategori"),
    DATA_SATUAN("Data Satuan"),
    PRINT_DATA_ITEM("Print Data Item"),
    VIEW_DATA_ITEM("View Data Item"),
    EDIT_DATA_ITEM("Edit Data Item"),
    DELETE_DATA_ITEM("Delete Data Item"),
    EDIT_DATA_SUPPLIER("Edit Data Supplier"),
    DELETE_DATA_SUPPLIER("Delete Data Supplier"),
    EDIT_DATA_KATEGORI("Edit Data Kategori"),
    DELETE_DATA_KATEGORI("Delete Data Kategori"),
    PRINT_DATA_SUPPLIER("Print Data Supplier"),
    DELETE_DATA_SATUAN("Delete Data Satuan"),
    EDIT_DATA_SUB_KATEGORI("Edit Data Sub Kategori"),
    DELETE_DATA_SUB_KATEGORI("Delete Data Sub Kategori"),
    PURCHASE("Purchase"),
    DATA_PURCHASE("Data Purchase"),
    PRINT_DATA_PURCHASE("Print Data Purchase"),
    INPUT_PO("Input PO"),
    TERIMA_PO("Terima PO"),
    INPUT_PURCHASE("Input Purchase"),
    EDIT_PO("Edit PO"),
    DELETE_PO("Delete PO"),
    VIEW_DETAIL_PURCHASE("View Detail Purchase"),
    POS("POS"),
    KALKULASI_KASIR("Kalkulasi Kasir"),
    REPRINT("Reprint"),
    HOLD_TRANSAKSI("Hold Transaksi"),
    LOAD_TRANSAKSI("Load Transaksi"),
    DISC_ITEM("Disc Item"),
    REFUND("Refund"),
    DISC_ALL("Disc All"),
    BATAL("Batal"),
    STOCK("Stock"),
    DATA_STOCK("Data Stock"),
    PRINT_DATA_STOCK("Print Data Stock"),
    DATA_MUTASI_STOCK("Data Mutasi Stock"),
    PRINT_DATA_MUTASI_STOCK("Print Data Mutasi Stock"),
    DATA_STOCK_OPNAME("Data Stock Opname"),
    EDIT_DATA_STOCK_OPNAME("Edit Data Stock Opname"),
    DELETE_DATA_STOCK_OPNAME("Delete Data Stock Opname"),
    VERIFIKASI_DATA_STOCK_OPNAME("Verifikasi Data Stock Opname"),
    INPUT_DATA_STOCK_OPNAME("Input Data Stock Opname"),
    DATA_STOCK_OUT("Data Stock Out"),
    INPUT_DATA_STOCK_OUT("Input Data Stock Out"),
    VIEW_DETAIL_STOCK_OUT("View Detail Stock Out"),
    MASTER("Master"),
    DATA_PELANGGAN("Data Pelanggan"),
    EDIT_DATA_PELANGGAN("Edit Data Pelanggan"),
    DELETE_DATA_PELANGGAN("Delete Data Pelanggan"),
    DATA_KARYAWAN("Data Karyawan"),
    EDIT_DATA_KARYAWAN("Edit Data Karyawan"),
    DELETE_DATA_KARYAWAN("Delete Data Karyawan"),
    DATA_SHIFT("Data Shift"),
    EDIT_DATA_SHIFT("Edit Data Shift"),
    DELETE_DATA_SHIFT("Delete Data Shift"),
    PRINT_DATA_PELANGGAN("Print Data Pelanggan"),
    PRINT_DATA_KARYAWAN("Print Data Karyawan"),
    INPUT_DATA_SHIFT("Input Data Shift"),
    FINANCE("Finance"),
    DATA_HUTANG("Data Hutang"),
    VIEW_DATA_HUTANG("View Data Hutang"),
    INPUT_PEMBAYARAN_HUTANG("Input Pembayaran Hutang"),
    VIEW_DATA_PIUTANG("View Data Piutang"),
    INPUT_PEMBAYARAN_PIUTANG("Input Pembayaran Piutang"),
    DATA_PIUTANG("Data Piutang"),
    DATA_CASH_IN_CASH_OUT("Data Cash In Cash Out"),
    VIEW_CASH_IN_CASH_OUT("View Cash In Cash Out"),
    INPUT_CASH_IN_CASH_OUT("Input Cash In Cash Out"),
    PRINT_DATA_HUTANG("Print Data Hutang"),
    PRINT_DATA_PIUTANG("Print Data Piutang"),
    PRINT_DATA_CASH_IN_CASH_OUT("Print Data Cash In Cash Out"),
    TOOLS("Tools"),
    DATA_USER("Data User"),
    EDIT_DATA_USER("Edit Data User"),
    DELETE_DATA_USER("Delete Data User"),
    RUBAH_PASSWORD("Rubah Password"),
    DATA_USER_AKSES("Data User Akses"),
    EDIT_USER_AKSES("Edit User Akses"),
    DELETE_USER_AKSES("Delete User Akses"),
    SETTING("Setting"),
    LAPORAN("Laporan"),;

    private final String mValue;

    private Akses_List(String val) {
        this.mValue = val;
    }

    @Override
    public String toString() {
        return mValue;
    }

    public String get() {
        return mValue;
    }
}
