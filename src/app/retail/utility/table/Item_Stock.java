/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility.table;

/**
 *
 * @author RCS
 */
public enum Item_Stock {
    TABLENAME("item_stock"),
    IDITEM("id_item"),
    STOCK("stock"),;

    private final String mValue;

    private Item_Stock(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
