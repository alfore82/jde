/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.ConstantAction;

import com.thinkstep.jde.persistence.entities.Rules.AssignConstantAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.ui.Converters.FieldNameStringConverter;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class ConstantActionController implements Initializable {

    @FXML
    private TextField tfValue;
    
    private ConstantActionCallback rb;
    private AssignConstantAction action;
    @FXML
    private ChoiceBox<FieldName> cbField;
    
    private ObservableList<FieldName> fields = FXCollections.observableArrayList();
    private FieldName field;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbField.setItems(fields);
        cbField.setConverter(new FieldNameStringConverter());
        
    }    

    @FXML
    private void btnClose(ActionEvent event) {
        if (createActionInstance()){
            rb.setConstantAction(action);
            Stage stage = (Stage) this.tfValue.getScene().getWindow();
            stage.close();
        }
    }
    
    public void setRuleBulderConstantAction(ConstantActionCallback rb){
        this.rb = rb;
    }
    
    public void setAction(AssignConstantAction action){
        this.action = action;
        NumberFormat nf = NumberFormat.getInstance();
        this.tfValue.setText(nf.format(action.getValue()));
        this.cbField.getSelectionModel().select(action.getName());
    }
    
    public void setFieldNames(FieldName... fieldNames){
        fields.clear();
        fields.addAll(fieldNames);
         if (fields.size() > 0) cbField.getSelectionModel().select(0);
    }

    private boolean createActionInstance() {
        boolean success = false;
        NumberFormat nf = NumberFormat.getInstance();
        field = cbField.getSelectionModel().getSelectedItem();
        if (field != null){
            success = true;
        } else {
            success = false;
        }
        try {
            String text = this.tfValue.getText();
            if (action == null){
                action = new AssignConstantAction();
            }
            if (success){
                action.setValue(nf.parse(text).doubleValue());
                action.setName(field);
                success = true;
            }
        } catch (ParseException ex) {
            this.tfValue.setText("");
            success = false;
        }
        
        return success;
    }
    
}
