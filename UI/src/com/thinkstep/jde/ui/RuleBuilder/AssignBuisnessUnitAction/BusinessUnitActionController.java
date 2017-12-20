/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.AssignBuisnessUnitAction;

import com.thinkstep.jde.persistence.entities.BusinessUnit;
import com.thinkstep.jde.persistence.entities.Rules.AssignBusinessUnitAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.ui.Converters.BusinessUnitStringConverter;
import com.thinkstep.jde.ui.Converters.FieldNameStringConverter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class BusinessUnitActionController implements Initializable {

    private AssignBusinessUnitActionCallback rb;
    private AssignBusinessUnitAction action;
    @FXML
    private ChoiceBox<FieldName> cbField;
    @FXML
    private ChoiceBox<BusinessUnit> cbBuisnessUnit;
    
    private ObservableList<FieldName> fields = FXCollections.observableArrayList();
    private ObservableList<BusinessUnit> bus = FXCollections.observableArrayList();
    private FieldName field;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbField.setItems(fields);
        cbField.setConverter(new FieldNameStringConverter());
        bus.addAll(BusinessUnit.PROFESSIONAL,BusinessUnit.RETAIL);
        cbBuisnessUnit.setItems(bus);
        cbBuisnessUnit.setConverter(new BusinessUnitStringConverter());
    }    

    @FXML
    private void btnClose(ActionEvent event) {
        if (createActionInstance()){
            rb.setBusinessUnitAction(action);
            Stage stage = (Stage) this.cbBuisnessUnit.getScene().getWindow();
            stage.close();
        }
    }
    
    public void setRuleBulderConstantAction(AssignBusinessUnitActionCallback rb){
        this.rb = rb;
    }
    
    public void setAction(AssignBusinessUnitAction action){
        this.action = action;
        BusinessUnitStringConverter busc = new BusinessUnitStringConverter();
        this.cbBuisnessUnit.getSelectionModel().select(action.getBusinessUnit());
        this.cbField.getSelectionModel().select(action.getName());
    }
    
    public void setFieldNames(FieldName... fieldNames){
        fields.clear();
        fields.addAll(fieldNames);
         if (fields.size() > 0) cbField.getSelectionModel().select(0);
    }

    private boolean createActionInstance() {
        boolean success = false;
        BusinessUnitStringConverter busc = new BusinessUnitStringConverter();
        field = cbField.getSelectionModel().getSelectedItem();
        if (field != null){
            success = true;
        } else {
            success = false;
        }
       
        BusinessUnit bu = this.cbBuisnessUnit.getSelectionModel().getSelectedItem();
        if (action == null){
            action = new AssignBusinessUnitAction();
        }
        if (success && bu != null){
            action.setBusinessUnit(bu);
            action.setName(field);
            success = true;
        }
       
        
        return success;
    }
    
}
