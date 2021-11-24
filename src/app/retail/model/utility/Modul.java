/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.model.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Lutfi
 */
public class Modul {
    StringProperty description = new SimpleStringProperty();
    ObservableList<Modul> submodul = FXCollections.observableArrayList();
}
