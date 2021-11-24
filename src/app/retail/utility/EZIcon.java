/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility;

import javafx.scene.image.Image;

/**
 *
 * @author RCS
 */
public enum EZIcon {
    ICON_STAGE("/app/retail/images/icons8_notification_24px_2.png"),
    ICON_APPS("/app/retail/images/EaZy POS IC.png");
    
    private final String resource;
    private final Image iconImage;
    
    private EZIcon(String resouce){
        this.resource = resouce;
        this.iconImage = new Image(resouce);
    }
    
    public String getResource(){
        return resource;
    }
    
    public Image get(){
        return iconImage;
    }
}
