/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.master;

import app.retail.controller.general.General;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Lutfi
 */
public abstract class AbstractMaster extends General {

    public static HashMap<String, String> MAP_KATEGORI = new HashMap<>();
    public static HashMap<String, String> MAP_SAT = new HashMap<>();
    public static HashMap<String, String> MAP_SUB_KATEGORI = new HashMap<>();
    public static HashMap<String, String> MAP_SUPPLIER = new HashMap<>();

    public abstract void loadData();
    public abstract void loadKategori();
    public abstract void loadSubKategori(String subid);
    public abstract void loadSatuan();
    public abstract void loadSupplier();

    private String getNol(int pParam) {
        if (pParam < 10) {
            return "00";
        }

        if (pParam < 100) {
            return "0";
        }

        if (pParam < 1000) {
            return "";
        }

        return "Overlod";
    }

    public List<String> getKategori() {
        List<String> tMap = new ArrayList<>();
        try {
            String tQuery = "SELECT * FROM kategori Order By kategori ASC";
            ResultSet tRest = selectFromDatabase(tQuery);
            while (tRest.next()) {
                MAP_KATEGORI.put(tRest.getString("kategori"), tRest.getString("id"));
                tMap.add(tRest.getString("kategori"));
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        return tMap;
    }

    public List<String> getSat() {
        List<String> tMap = new ArrayList<>();
        try {
            String tQuery = "SELECT * FROM satuan ORDER BY descrip ASC";
            ResultSet tRest = selectFromDatabase(tQuery);
            int no = 0;
            while (tRest.next()) {
                MAP_SAT.put(tRest.getString("descrip"), tRest.getString("id"));
                tMap.add(tRest.getString("descrip"));
                no++;
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        return tMap;
    }

    public List<String> getSupplier() {
        List<String> tMap = new ArrayList<>();
        try {
            String tQuery = "SELECT * FROM supplier ORDER BY nama ASC";
            ResultSet tRest = selectFromDatabase(tQuery);
            int no = 0;
            while (tRest.next()) {
                MAP_SUPPLIER.put(tRest.getString("nama"), tRest.getString("id"));
                tMap.add(tRest.getString("nama"));
                no++;
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        return tMap;
    }

    public List<String> getSubKategori(String pIdKat) {
        List<String> tMap = new ArrayList<>();
        try {
            String tQuery = "SELECT id,sub_kategori FROM sub_kategori WHERE id_kategori='" + pIdKat + "' ORDER BY sub_kategori ASC";
            ResultSet tRest = selectFromDatabase(tQuery);
            int no = 0;
            while (tRest.next()) {
                MAP_SUB_KATEGORI.put(tRest.getString("sub_kategori"), tRest.getString("id"));
                tMap.add(tRest.getString("sub_kategori"));
                no++;
            }
        } catch (SQLException ex) {
            loggingerror(ex);
        }
        return tMap;
    }

}
