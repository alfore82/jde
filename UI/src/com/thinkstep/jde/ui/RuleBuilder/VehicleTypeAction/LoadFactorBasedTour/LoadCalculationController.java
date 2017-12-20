/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.VehicleTypeAction.LoadFactorBasedTour;

import com.thinkstep.jde.persistence.entities.Rules.CalculateLoadAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.ui.Converters.DataKeyStringConverter;
import com.thinkstep.jde.ui.Converters.FieldNameStringConverter;
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
public class LoadCalculationController implements Initializable {

    @FXML
    private ComboBox<DataKey> cbTour;

    private CalculateLoadAction action;
    private LoadCalculatonAction rb;
    @FXML
    private ComboBox<FieldName> cbField;
    
    
    
    private ObservableList<FieldName> fields = FXCollections.observableArrayList();
    private FieldName field;
    private ObservableList<DataKey> keys = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbTour.setItems(keys);
        cbTour.setConverter(new DataKeyStringConverter());
        fields.addAll(FieldName.LOADFACTORPICKUP, FieldName.LOADFACTORMAIN, FieldName.LOADFACTORDELIVERY);
        cbField.setItems(fields);
        cbField.setConverter(new FieldNameStringConverter());
    }    

    @FXML
    private void btnClose(ActionEvent event) {
        if (createInstance()){
            rb.setAction(action);
            Stage stage = (Stage) this.cbTour.getScene().getWindow();
            stage.close();
        }
        
    }
    
    public void setAction(CalculateLoadAction action){
        this.action = action;
        createFromAction();
    }
    
    public  void setRuleBulderFuelTypeAction(LoadCalculatonAction rb){
        this.rb = rb;
    }

    private void createFromAction() {
        if (action.getTourIdentifier() != null){
            this.cbTour.setValue(action.getTourIdentifier());
            this.cbField.setValue(action.getName());
        }
        
        
        
    }

    private boolean createInstance() {
        field = cbField.getSelectionModel().getSelectedItem();
        if (cbField == null){
            return false;
        }
        if (this.action == null){
            this.action = new  CalculateLoadAction();
        }
        if (this.cbTour.getValue()!= null){
            this.action.setTourIdentifier(cbTour.getValue());
            this.action.setName(field);
            return true;
        } else {
            this.action.setTourIdentifier(null);
            return false;
        }
    }
    
    public void setKeys(List<DataKey> keys){
        this.keys.clear();
        this.keys.addAll(keys);
    }
    
}
