/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

/**
 *
 * @author RCS
 */
public class Generator {
    public void generate(){
        String x = "/app/retail/report/stock/lap_stock.jrxml";
        String[] y = x.split("/");
        System.out.println(y[y.length - 1].replace(".jrxml", ""));
    }
    
    public static void main(String[] args) {
        new Generator().generate();
    }
}
