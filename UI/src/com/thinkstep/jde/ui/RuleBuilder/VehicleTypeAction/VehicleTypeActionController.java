/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.VehicleTypeAction;

import com.thinkstep.jde.persistence.entities.Rules.AssignVehicleTypeAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.fuelconsumptioncalculation.VehicleType;
import com.thinkstep.jde.persistence.services.VehicleTypeService;
import com.thinkstep.jde.ui.Converters.FieldNameStringConverter;
import com.thinkstep.jde.ui.Converters.VehicleTypeStringConverter;
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
public class VehicleTypeActionController implements Initializable {

    @FXML
    private ComboBox<VehicleType> cbVehicleType;

    private AssignVehicleTypeAction action;
    private RuleBulderVehicleTypeAction rb;
    @FXML
    private ComboBox<FieldName> cbField;
    
    private ObservableList<FieldName> fields = FXCollections.observableArrayList();
    private FieldName field;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbVehicleType.getItems().addAll(VehicleTypeService.getINSTANCE().findAllVehicleTypes());
        cbVehicleType.setConverter(new VehicleTypeStringConverter());
        fields.addAll(FieldName.VEHICLETYPEPICKUP, FieldName.VEHICLETYPEMAIN, FieldName.VEHICLETYPEDELIVERY);
        cbField.setItems(fields);
        cbField.setConverter(new FieldNameStringConverter());
    }    

    @FXML
    private void btnClose(ActionEvent event) {
        if (createInstance()){
            rb.setVehicleTypeAction(action);
            Stage stage = (Stage) this.cbVehicleType.getScene().getWindow();
            stage.close();
        }
        
    }
    
    public void setAction(AssignVehicleTypeAction action){
        this.action = action;
        createFromAction();
    }
    
    public  void setRuleBulderFuelTypeAction(RuleBulderVehicleTypeAction rb){
        this.rb = rb;
    }

    private void createFromAction() {
        if (action.getVehicleType() != null){
            this.cbVehicleType.setValue(action.getVehicleType());
            this.cbField.setValue(action.getName());
        }
        
        
        
    }

    private boolean createInstance() {
        field = cbField.getSelectionModel().getSelectedItem();
        if (cbField == null){
            return false;
        }
        if (this.action == null){
            this.action = new  AssignVehicleTypeAction();
        }
        if (this.cbVehicleType.getValue()!= null){
            this.action.setVehicleType(cbVehicleType.getValue());
            this.action.setName(field);
            return true;
        } else {
            this.action.setVehicleType(null);
            return false;
        }
    }
    
}
