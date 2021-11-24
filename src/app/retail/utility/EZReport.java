/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author RCS
 */
public class EZReport extends JFrame{
    private final String mFileName;
    private final Connection mCon;

    public EZReport(String mFileName, Connection con) {
        this.mFileName = mFileName;
        this.mCon = con;
    }
    
    public void showReport() throws JRException, ClassNotFoundException {
        JasperReport jasperReport = JasperCompileManager.compileReport(mFileName);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, null, mCon);
        JRViewer viewer = new JRViewer(print);
        print.setName(getReportName());
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setTitle("Laporan");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/app/retail/images/icons8_report_card_48px.png")));
        this.setMinimumSize(new Dimension(900, 700));
        this.setLocationRelativeTo(this);
        this.setVisible(true);
    }
    
    public void showReport(String Query) throws JRException, ClassNotFoundException {
        JasperDesign jasperDesign = JRXmlLoader.load(mFileName);
        JRDesignQuery jasperQuery = new JRDesignQuery();
        jasperQuery.setText(Query);
        jasperDesign.setQuery(jasperQuery);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, null, mCon);
        print.setName(getReportName());
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setTitle("Laporan");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/app/retail/images/icons8_report_card_48px.png")));
        this.setMinimumSize(new Dimension(900, 700));
        this.setLocationRelativeTo(this);
        this.setVisible(true);
    }
    
    public void showReport(String Query , Map<String, Object> Param) throws JRException, ClassNotFoundException {
        JasperDesign jasperDesign = JRXmlLoader.load(mFileName);
        JRDesignQuery jasperQuery = new JRDesignQuery();
        jasperQuery.setText(Query);
        jasperDesign.setQuery(jasperQuery);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, Param, mCon);
        print.setName(getReportName());
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setTitle("Laporan");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/app/retail/images/icons8_report_card_48px.png")));
        this.setMinimumSize(new Dimension(900, 700));
        this.setLocationRelativeTo(this);
        this.setVisible(true);
    }
    
    private String getReportName(){
        String[] y = mFileName.split("/");
        return y[y.length - 1].replace(".jrxml", "");
    }
}
