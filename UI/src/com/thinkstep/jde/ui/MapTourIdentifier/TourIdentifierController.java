/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.MapTourIdentifier;

import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.preferences.TourIdentifier;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.DataKeyService;
import com.thinkstep.jde.persistence.services.TourIdentifierService;
import com.thinkstep.jde.ui.Converters.DataKeyStringConverter;
import java.net.URL;
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
public class TourIdentifierController implements Initializable {

    @FXML
    private ComboBox<DataKey> cbTourIdentifier;

    private ObservableList<DataKey> keys = FXCollections.observableArrayList();
    private DataKey dataKey;
    private TourIdentifier t;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbTourIdentifier.setItems(keys);
        cbTourIdentifier.setConverter(new DataKeyStringConverter());

    }    

    @FXML
    private void save(ActionEvent event) {
        dataKey =  cbTourIdentifier.getSelectionModel().getSelectedItem();
        if (dataKey != null){
            t.setDataKey(dataKey);
            TourIdentifierService tid = TourIdentifierService.getINSTANCE();
            tid.addOrEditTourIdentifier(t);
            Stage stage = (Stage) this.cbTourIdentifier.getScene().getWindow();
            stage.close();
        }
        
        
        
        
        
    }
    
    public void setLsp(LogisticServiceProvider lsp){
        keys.clear();
        DataKeyService dkService = DataKeyService.getINSTANCE();
        keys.addAll(dkService.findDataKeysByLsp(lsp));
        TourIdentifierService tid = TourIdentifierService.getINSTANCE();
        t = tid.findTourIdentifier(lsp);
        if (t == null){
            t = new TourIdentifier();
            t.setLsp(lsp);
        }
        this.dataKey = t.getDataKey();
        cbTourIdentifier.getSelectionModel().select(dataKey);
        
    }
    
}
