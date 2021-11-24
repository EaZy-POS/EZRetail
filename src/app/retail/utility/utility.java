/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Lutfi
 */
public class utility {
    
    private static final String C_FORMAT_DATE_1 = "dd/MM/yyyy";
    private SimpleDateFormat M_SIMPLE_DATE_FORMAT;
    private Date M_DATE;
    
    public String getDateToday(){
        M_DATE = new Date();
        M_SIMPLE_DATE_FORMAT = new SimpleDateFormat(C_FORMAT_DATE_1);
        return M_SIMPLE_DATE_FORMAT.format(M_DATE);
    }
    
    public String getDateToday(Date pDate){
        M_SIMPLE_DATE_FORMAT = new SimpleDateFormat(C_FORMAT_DATE_1);
        return M_SIMPLE_DATE_FORMAT.format(pDate);
    }
    
    public String getDate(Date pDate,String patern){
        M_SIMPLE_DATE_FORMAT = new SimpleDateFormat(patern);
        return M_SIMPLE_DATE_FORMAT.format(pDate);
    }
    
    public String getKodeTengah(String pFormat){
        M_DATE = new Date();
        M_SIMPLE_DATE_FORMAT = new SimpleDateFormat(pFormat);
        return M_SIMPLE_DATE_FORMAT.format(M_DATE);
    }
    
    public String getDateToday(String Patern){
        M_DATE = new Date();
        M_SIMPLE_DATE_FORMAT = new SimpleDateFormat(Patern);
        return M_SIMPLE_DATE_FORMAT.format(M_DATE);
    }

}
