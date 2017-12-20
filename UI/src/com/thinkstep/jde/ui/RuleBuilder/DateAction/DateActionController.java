/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.DateAction;

import com.thinkstep.jde.persistence.entities.Rules.AssignDateAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.ui.Converters.DataKeyStringConverter;
import com.thinkstep.jde.ui.Converters.FieldNameStringConverter;
import java.net.URL;
import java.text.NumberFormat;
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
public class DateActionController implements Initializable {

    
    
    private RuleBuilderDateActionCallback rb;
    private AssignDateAction action;
    @FXML
    private ComboBox<DataKey> cbDataKey;
    private ObservableList<DataKey> keys = FXCollections.observableArrayList();
    @FXML
    private ComboBox<FieldName> cbField;
    
    private ObservableList<FieldName> fields = FXCollections.observableArrayList();
    private FieldName field;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        keys.clear();
        cbDataKey.setItems(keys);
        cbDataKey.setConverter(new DataKeyStringConverter());
        cbField.setItems(fields);
        cbField.setConverter(new FieldNameStringConverter());
    }    

    @FXML
    private void btnClose(ActionEvent event) {
        if (createActionInstance()){
            rb.setDateAction(action);
            Stage stage = (Stage) this.cbDataKey.getScene().getWindow();
            stage.close();
        }
    }
    
    public void setRuleBulderConstantAction(RuleBuilderDateActionCallback rb){
        this.rb = rb;
    }
    
    public void setAction(AssignDateAction action){
        this.action = action;
        NumberFormat nf = NumberFormat.getInstance();
        this.cbDataKey.setValue(action.getDataKey());
        this.field = action.getName();
        cbField.getSelectionModel().select(field);
    }
    
    public void setFieldNames(FieldName... fieldNames){
        fields.clear();
        fields.addAll(fieldNames);
        if (fields.size() > 0) cbField.getSelectionModel().select(0);
    }

    private boolean createActionInstance() {
        DataKey key = this.cbDataKey.getValue();
        field = cbField.getSelectionModel().getSelectedItem();
        if (key != null && field != null){
            if (action == null){
                action = new AssignDateAction();
            }
            action.setName(field);
            action.setDataKey(key);
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
