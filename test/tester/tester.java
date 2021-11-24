/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import co.id.eazy.query.service.QueryService;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RCS
 */
public class tester {
    
    public static List<String> getColoumnName(ResultSetMetaData pResultMetaData) throws SQLException{
        List<String> tLst = new ArrayList<>();
        int columnCount = pResultMetaData.getColumnCount();
        for (int i = 0; i < columnCount ; i++) {
            tLst.add(pResultMetaData.getColumnName(i+1));
        }
        return tLst;
    }
    
    public static void generateEnum(String table) throws SQLException{
        String tQuery ="SELECT * FROM "+ table;
        ResultSet tRest = new QueryService().executeQuery(tQuery);
        ResultSetMetaData tMetaData = tRest.getMetaData();
        List<String> tList = getColoumnName(tMetaData);
        
        System.out.println("TABLENAME(\""+table+"\"),");
        tList.stream().map((field) -> field.toUpperCase().replace("_", "").concat("(\"").concat(field).concat("\"),")).forEachOrdered((val) -> {
            System.out.println(val);
        });
        System.out.print(";");
        System.out.println("\n");
        System.out.println("private final String mValue;\n");
        System.out.println(
                        "    private "+getTable(table)+"(String val){\n" +
                        "        this.mValue = val;\n" +
                        "    }\n\n"
        );
        
        System.out.println("    public String get(){\n" +
                        "            return mValue;\n" +
                        "        }");
        
        System.out.println("\n============================================================================");
    }
    
    public static void generateEnumQ(String table) throws SQLException{
        String tQuery = table;
        ResultSet tRest = new QueryService().executeQuery(tQuery);
        List<String> tList = new ArrayList<>();
        
        while (tRest.next()) {            
            String mod = tRest.getString("modul");
            String submod = tRest.getString("sub_modul");
            if(!tList.contains(mod)){
                tList.add(mod);
            }
            
            if(!tList.contains(submod)){
                tList.add(submod);
            }
        }
        
        String name = "Akses_List";
        tList.stream().map((field) -> field.toUpperCase().replace(" ", "_").concat("(\"").concat(field).concat("\"),")).forEachOrdered((val) -> {
            System.out.println(val.trim());
        });
        System.out.print(";");
        System.out.println("\n");
        System.out.println("private final String mValue;\n");
        System.out.println(
                        "    private "+getTable(name)+"(String val){\n" +
                        "        this.mValue = val;\n" +
                        "    }\n\n"
        );
        
        System.out.println("    public String get(){\n" +
                        "            return mValue;\n" +
                        "        }");
        
        System.out.println("\n============================================================================");
    }
    
    public static String getTable(String table){
        String[]name = table.split("_");
        String tName = "";
        for (String val : name) {
            String s1 = String.valueOf(val.charAt(0)).toUpperCase();
            tName += s1 + val.substring(1)+"_";
        }
        
        tName = tName.substring(0, tName.length()-1);
        return tName;
    }
    
    public static void main(String[] args) {
        String[] target = new String[]{
          "v_all_sale",
        };
        try {
            String tSql = "SHOW TABLES";
            ResultSet tRest = new QueryService().executeQuery(tSql);
            while (tRest.next()) {
                String tTable = tRest.getString(1);
                for (String string : target) {
                    if(tTable.equalsIgnoreCase(string)){
                        generateEnum(tTable);
                    }
                }
            }
//            String tSQL = "SELECT modul, sub_modul FROM modul INNER JOIN sub_modul ON sub_modul.id_modul=modul.id";
//            generateEnumQ(tSQL);
        } catch (SQLException ex) {
            Logger.getLogger(tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
