/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.actions;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class LspSelectorController implements Initializable {

    @FXML
    private ComboBox<String> cbLsp;

    private ObservableList<String> lsps = FXCollections.observableArrayList();
    private int lspColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbLsp.setItems(lsps);
    }

    @FXML
    private void close(ActionEvent event) {
        lspColumn = cbLsp.getSelectionModel().getSelectedIndex();
        Stage stage = (Stage) cbLsp.getScene().getWindow();
        stage.close();
    }

    public int getLspColumn() {
        return lspColumn;
    }
    
    public void setColumns(List<String> cols){
        lsps.clear();
        lsps.addAll(cols);
    }    
    
    
}
