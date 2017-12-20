/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.DistanceCalculationAction;

import com.thinkstep.jde.persistence.entities.Rules.CalculateDistanceAction;
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
public class DistanceCalcualtionActionController implements Initializable {

    @FXML
    private ComboBox<FieldName> cbField;
    @FXML
    private ComboBox<DataKey> cbTourIdentifier;
    @FXML
    private ComboBox<DataKey> cbCountryStart;
    @FXML
    private ComboBox<DataKey> cbZipStart;
    @FXML
    private ComboBox<DataKey> cbCityStart;
    @FXML
    private ComboBox<DataKey> cbCountryDestination;
    @FXML
    private ComboBox<DataKey> cbZipDestination;
    @FXML
    private ComboBox<DataKey> cbCityDestination;
    
    private ObservableList<DataKey> keys = FXCollections.observableArrayList();
    private ObservableList<FieldName> fields = FXCollections.observableArrayList();
    
    
    private DistanceCalculationCallback cb;
    private CalculateDistanceAction action;
    
    private FieldName field;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        keys.clear();
        this.cbTourIdentifier.setItems(keys);
        this.cbCountryStart.setItems(keys);
        this.cbZipStart.setItems(keys);
        this.cbCityStart.setItems(keys);
        this.cbCountryDestination.setItems(keys);
        this.cbZipDestination.setItems(keys);
        this.cbCityDestination.setItems(keys);
        this.cbTourIdentifier.setConverter(new DataKeyStringConverter());
        this.cbCountryStart.setConverter(new DataKeyStringConverter());
        this.cbZipStart.setConverter(new DataKeyStringConverter());
        this.cbCityStart.setConverter(new DataKeyStringConverter());
        this.cbCountryDestination.setConverter(new DataKeyStringConverter());
        this.cbZipDestination.setConverter(new DataKeyStringConverter());
        this.cbCityDestination.setConverter(new DataKeyStringConverter());
        
        this.cbField.setItems(fields);
        this.cbField.setConverter(new FieldNameStringConverter());
        
    }    

    @FXML
    private void close(ActionEvent event) {
        if (createActionInstance()){
            cb.setCalculateDistanceAction(action);
            Stage stage = (Stage) this.cbField.getScene().getWindow();
            stage.close();
        }
    }
    
    public void setDistanceCalculationCallback(DistanceCalculationCallback cb){
        this.cb = cb;
    }

    public void setAction(CalculateDistanceAction action){
        this.action = action;
        this.field = action.getName();
        cbField.getSelectionModel().select(field);
        this.cbTourIdentifier.setValue(action.getTourIdentifier());
        this.cbCountryStart.setValue(action.getStartCountry());
        this.cbZipStart.setValue(action.getStartZipCode());
        this.cbCityStart.setValue(action.getStartCity());
        this.cbCountryDestination.setValue(action.getDestinationCountry());
        this.cbZipDestination.setValue(action.getDestinationZipCode());
        this.cbCityDestination.setValue(action.getDestinationCity());
    }
    
    public void setFieldNames(FieldName... fieldNames){
        fields.clear();
        fields.addAll(fieldNames);
        if (fields.size() > 0) cbField.getSelectionModel().select(0);
    }
    

    private boolean createActionInstance() {
        DataKey tourId = this.cbTourIdentifier.getValue();
        DataKey countryStart = this.cbCountryStart.getValue();
        DataKey zipStart = this.cbZipStart.getValue();
        DataKey cityStart = this.cbCityStart.getValue();
        DataKey countryDestination = this.cbCountryDestination.getValue();
        DataKey zipDestination = this.cbZipDestination.getValue();
        DataKey cityDestination = this.cbCityDestination.getValue();
        this.field = this.cbField.getSelectionModel().getSelectedItem();
        
        
        if (tourId != null && 
            countryStart != null && 
            zipStart != null && 
            cityStart != null && 
            countryDestination != null && 
            zipDestination != null && 
            cityDestination != null && 
            field != null){
            if (action == null){
                action = new CalculateDistanceAction();
            }
            
            action.setName(field);
            action.setTourIdentifier(tourId);
            action.setStartCountry(countryStart);
            action.setStartZipCode(zipStart);
            action.setStartCity(cityStart);
            action.setDestinationCountry(countryDestination);
            action.setDestinationZipCode(zipDestination);
            action.setDestinationCity(cityDestination);
            return true;
        } else {
            return false;
        }
    }

    public void setKeys(List<DataKey> keys) {
        this.keys.clear();
        this.keys.addAll(keys);
    }
    
    
    
}
