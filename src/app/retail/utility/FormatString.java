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
public class FormatString {
    
    public String left(String target, String padding, int length){
        if(target.length() > length){
            return target.substring(0, length);
        }
        
        while (target.length() < length) {            
            target = padding + target;
        }
        
        return target;
    }
    
    public String right(String target, String padding, int length){
        if(target.length() > length){
            return target.substring(0, length);
        }
        
        while (target.length() < length) {            
            target = target+ padding;
        }
        
        return target;
    }
    
    public String center(String target, String padding, int length){
        if(target.length() > length){
            return target.substring(0, length);
        }
        
        while (target.length() < length) {            
            target = padding + target + padding;
        }
        
        if(target.length() > length){
            return target.substring(0, length);
        }
        System.out.println("["+target+"]");
        return target;
        
    }
}
