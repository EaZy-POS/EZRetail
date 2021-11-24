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
public class ITEM {
    private String ITEMCODE,IDITEM,BARCODE,KODE,ITEMNAME,SUPID,SUPNAME,KATID,KAT,SUBKATID,SUBKAT,IDSAT,SATUAN,STOKMIN,KONVERSI,SATUANKE,HRGBELI,HARGAJUAL,FLAG;

    public ITEM(String ITEMCODE, String IDITEM, String BARCODE, String KODE, String ITEMNAME, String SATUAN, String HRGBELI) {
        this.ITEMCODE = ITEMCODE;
        this.IDITEM = IDITEM;
        this.BARCODE = BARCODE;
        this.KODE = KODE;
        this.ITEMNAME = ITEMNAME;
        this.SATUAN = SATUAN;
        this.HRGBELI = HRGBELI;
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
    
}
