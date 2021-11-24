/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.utility;

import app.retail.controller.general.General;
import app.retail.controller.general.MapKaryawan;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RCS
 */
public abstract class AbstractUtility extends General{
    public static boolean isEdit;
    public static List<String> mListUserID = new ArrayList<>();
    
    public boolean isCurentUser(String id) throws SQLException{
        return mMapKaryawan.get(MapKaryawan.KODEKARYAWAN.get()).equals(id);
    }
}
