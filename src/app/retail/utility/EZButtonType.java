/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility;

/**
 *
 * @author Lutfi
 */
public enum EZButtonType {
    BTN_EDIT("/app/retail/images/icons8_edit_16px.png"),
    BTN_DELETE("/app/retail/images/icons8_delete_bin_16px.png"),
    BTN_VIEW("/app/retail/images/icons8_search_property_16px_1.png"),
    BTN_ACCEPT("/app/retail/images/icons8_trolley_16px_1.png"),
    BTN_MINUS("/app/retail/images/icons8_minus_16px.png"),
    BTN_PLUS("/app/retail/images/icons8_plus_math_16px.png"),
    BTN_LOAD("/app/retail/images/icons8_download_from_ftp_16px.png"),
    BTN_CHECK("/app/retail/images/icons8_checked_checkbox_16px.png"),
    BTN_PAY("/app/retail/images/icons8_cash_in_hand_16px.png"),
    BTN_VERIFICATION("/app/retail/images/icons8_checked_16px.png"),
    BTN_CHANGE_PASSWORD("/app/retail/images/icons8_password_16px.png"),
    BTN_PRINT("/app/retail/images/icons8_print_24px.png");
    private final String mIcon;

    private EZButtonType(String mType) {
        this.mIcon = mType;
    }
    
    public String getButtonIcon(){
        return mIcon;
    }
    
}
