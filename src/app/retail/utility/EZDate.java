/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author RCS
 */
public enum EZDate {
    SQLDATE("yyyy-MM-dd"),
    SQLDATETIME("yyyy-MM-dd HH:mm:ss"),
    FORMAT_2("dd/MM/yyyy"),
    FORMAT_4("dd/MMM/yyyy"),
    FORMAT_5("dd MMM yyyy"),
    FORMAT_3("yyyyMMdd HH:mm:ss"),
    FORMAT_6("dd/MM/yyyy HH:mm:ss"),
    SHORTDATE("yyMMdd"),
    SHORTTIME("HH:mm"),
    SHORTEMDATE("dd-MM-yyy"),
    YEAR("yyyy"),
    DATE("dd"),
    MONTH("MM"),
    MONTHYEAR("MMM yyyy"),
    FAKTUR("yyyyMMdd"),
    ;
    
    private final String mValue;
    
    private EZDate(String pValue){
        this.mValue = pValue;
    }
    
    public String get(){
        return mValue;
    }
    
    public String today(){
        SimpleDateFormat tFormat = new SimpleDateFormat(mValue);
        return tFormat.format(new Date());
    }
    
    public String tomorow(){
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        SimpleDateFormat tFormat = new SimpleDateFormat(mValue);
        return tFormat.format(dt);
    }
    
    public String get(Date pDate){
        SimpleDateFormat tFormat = new SimpleDateFormat(mValue);
        return tFormat.format(pDate);
    }
}
