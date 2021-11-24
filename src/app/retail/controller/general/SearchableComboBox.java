/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.controller.general;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;

/**
 *
 * @author RCS
 */
public class SearchableComboBox implements EventHandler<KeyEvent>{
    private static ComboBox comboBox;
    private static ObservableList data;
    private static Integer sid;

    public SearchableComboBox(ComboBox comboBox) {
        SearchableComboBox.comboBox = comboBox;
        SearchableComboBox.data = comboBox.getItems();
    }

    public SearchableComboBox(final ComboBox comboBox, Integer sid) {
        SearchableComboBox.comboBox = comboBox;
        SearchableComboBox.data = comboBox.getItems();
        SearchableComboBox.sid = sid;
    }

    public static void doAutoCompleteBox() {
        comboBox.setEditable(true);
        SearchableComboBox.comboBox.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){//mean onfocus
                SearchableComboBox.comboBox.show();
            }
        });

        SearchableComboBox.comboBox.getEditor().setOnMouseClicked(event ->{
            if(event.getButton().equals(MouseButton.PRIMARY)){
                if(event.getClickCount() == 2){
                    return;
                }
            }
            SearchableComboBox.comboBox.show();
        });

        SearchableComboBox.comboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                moveCaret(SearchableComboBox.comboBox.getEditor().getText().length());
            }
        });

        SearchableComboBox.comboBox.setOnKeyPressed(t -> comboBox.hide());

//        SearchableComboBox.comboBox.setOnKeyReleased(SearchableComboBox.this);

//        if(SearchableComboBox.sid!=null)
//            SearchableComboBox.comboBox.getSelectionModel().select(this.sid);
    }

    @Override
    public void handle(KeyEvent event) {
        if ( event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
                || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB
        ) {
            return;
        }

        if(event.getCode() == KeyCode.BACK_SPACE){
            String str = SearchableComboBox.comboBox.getEditor().getText();
            if (str != null && str.length() > 0) {
                str = str.substring(0, str.length() - 1);
            }
            if(str != null){
                SearchableComboBox.comboBox.getEditor().setText(str);
                moveCaret(str.length());
            }
            SearchableComboBox.comboBox.getSelectionModel().clearSelection();
        }

        if(event.getCode() == KeyCode.ENTER && comboBox.getSelectionModel().getSelectedIndex()>-1)
            return;

        setItems();
    }

    private static void setItems() {
        ObservableList list = FXCollections.observableArrayList();

        SearchableComboBox.data.forEach((datum) -> {
            String s = SearchableComboBox.comboBox.getEditor().getText().toLowerCase();
            if (datum.toString().toLowerCase().contains(s.toLowerCase())) {
                list.add(datum.toString());
            }
        });

        if(list.isEmpty()) SearchableComboBox.comboBox.hide();

        SearchableComboBox.comboBox.setItems(list);
        SearchableComboBox.comboBox.show();
    }

    private static void moveCaret(int textLength) {
        SearchableComboBox.comboBox.getEditor().positionCaret(textLength);
    }
}
