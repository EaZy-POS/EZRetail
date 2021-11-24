/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.pos;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TextField;

/**
 *
 * @author Lutfi
 */
public class POSModel {
    private String NO, ITEMCODE, IDITEM, BARCODE, KODE, ITEMNAME, SUPID, SUPNAME, KATID, KAT, SUBKATID, SUBKAT, IDSAT, SATUAN, STOKMIN, KONVERSI, SATUANKE, HRGBELI, HARGAJUAL, FLAG, QTY, TOTAL, KET, DISC, SISA, AUTOID;
    private JFXButton btnplus,btnmin,btn_delete;
    private TextField txtQtyRefund;

    public POSModel(String IDITEM, String BARCODE, String KODE, String ITEMNAME, String SATUAN, String HARGAJUAL, String QTY, String DISC, String TOTAL, String KET, JFXButton btnplus, JFXButton btnmin, JFXButton btn_delete, TextField txtQtyRefund) {
        this.IDITEM = IDITEM;
        this.BARCODE = BARCODE;
        this.KODE = KODE;
        this.ITEMNAME = ITEMNAME;
        this.SATUAN = SATUAN;
        this.HARGAJUAL = HARGAJUAL;
        this.QTY = QTY;
        this.TOTAL = TOTAL;
        this.btnmin = btnmin;
        this.btnplus = btnplus;
        this.btn_delete = btn_delete;
        this.txtQtyRefund = txtQtyRefund;
        this.KET = KET;
        this.DISC = DISC;
    }
    
    public POSModel(String IDITEM, String ITEMNAME, String SATUAN, String HARGAJUAL, String QTY, String DISC, String TOTAL, String SISA, String AUTOID,TextField txtQtyRefund) {
        this.IDITEM = IDITEM;
        this.ITEMNAME = ITEMNAME;
        this.SATUAN = SATUAN;
        this.HARGAJUAL = HARGAJUAL;
        this.QTY = QTY;
        this.TOTAL = TOTAL;
        this.txtQtyRefund = txtQtyRefund;
        this.DISC = DISC;
        this.SISA = SISA;
        this.AUTOID= AUTOID;
    }

    public String getAUTOID() {
        return AUTOID;
    }

    public void setAUTOID(String AUTOID) {
        this.AUTOID = AUTOID;
    }

    public String getSISA() {
        return SISA;
    }

    public void setSISA(String SISA) {
        this.SISA = SISA;
    }

    public String getDISC() {
        return DISC;
    }

    public void setDISC(String DISC) {
        this.DISC = DISC;
    }

    public String getKET() {
        return KET;
    }

    public void setKET(String KET) {
        this.KET = KET;
    }

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getITEMCODE() {
        return ITEMCODE;
    }

    public void setITEMCODE(String ITEMCODE) {
        this.ITEMCODE = ITEMCODE;
    }

    public String getIDITEM() {
        return IDITEM;
    }

    public void setIDITEM(String IDITEM) {
        this.IDITEM = IDITEM;
    }

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getKODE() {
        return KODE;
    }

    public void setKODE(String KODE) {
        this.KODE = KODE;
    }

    public String getITEMNAME() {
        return ITEMNAME;
    }

    public void setITEMNAME(String ITEMNAME) {
        this.ITEMNAME = ITEMNAME;
    }

    public String getSUPID() {
        return SUPID;
    }

    public void setSUPID(String SUPID) {
        this.SUPID = SUPID;
    }

    public String getSUPNAME() {
        return SUPNAME;
    }

    public void setSUPNAME(String SUPNAME) {
        this.SUPNAME = SUPNAME;
    }

    public String getKATID() {
        return KATID;
    }

    public void setKATID(String KATID) {
        this.KATID = KATID;
    }

    public String getKAT() {
        return KAT;
    }

    public void setKAT(String KAT) {
        this.KAT = KAT;
    }

    public String getSUBKATID() {
        return SUBKATID;
    }

    public void setSUBKATID(String SUBKATID) {
        this.SUBKATID = SUBKATID;
    }

    public String getSUBKAT() {
        return SUBKAT;
    }

    public void setSUBKAT(String SUBKAT) {
        this.SUBKAT = SUBKAT;
    }

    public String getIDSAT() {
        return IDSAT;
    }

    public void setIDSAT(String IDSAT) {
        this.IDSAT = IDSAT;
    }

    public String getSATUAN() {
        return SATUAN;
    }

    public void setSATUAN(String SATUAN) {
        this.SATUAN = SATUAN;
    }

    public String getSTOKMIN() {
        return STOKMIN;
    }

    public void setSTOKMIN(String STOKMIN) {
        this.STOKMIN = STOKMIN;
    }

    public String getKONVERSI() {
        return KONVERSI;
    }

    public void setKONVERSI(String KONVERSI) {
        this.KONVERSI = KONVERSI;
    }

    public String getSATUANKE() {
        return SATUANKE;
    }

    public void setSATUANKE(String SATUANKE) {
        this.SATUANKE = SATUANKE;
    }

    public String getHRGBELI() {
        return HRGBELI;
    }

    public void setHRGBELI(String HRGBELI) {
        this.HRGBELI = HRGBELI;
    }

    public String getHARGAJUAL() {
        return HARGAJUAL;
    }

    public void setHARGAJUAL(String HARGAJUAL) {
        this.HARGAJUAL = HARGAJUAL;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(String TOTAL) {
        this.TOTAL = TOTAL;
    }

    public JFXButton getBtnplus() {
        return btnplus;
    }

    public void setBtnplus(JFXButton btnplus) {
        this.btnplus = btnplus;
    }

    public JFXButton getBtnmin() {
        return btnmin;
    }

    public void setBtnmin(JFXButton btnmin) {
        this.btnmin = btnmin;
    }

    public JFXButton getBtn_delete() {
        return btn_delete;
    }

    public void setBtn_delete(JFXButton btn_delete) {
        this.btn_delete = btn_delete;
    }

    public TextField getTxtQtyRefund() {
        return txtQtyRefund;
    }

    public void setTxtQtyRefund(TextField txtQtyRefund) {
        this.txtQtyRefund = txtQtyRefund;
    }
    
}
