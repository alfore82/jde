/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.RawDataAction;

import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.Rules.AssignValueAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.ValueActionService;
import com.thinkstep.jde.ui.Converters.FieldNameStringConverter;
import com.thinkstep.jde.ui.RuleBuilder.RuleBuilderActionType;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.openide.util.Exceptions;

/**
 *
 * @author forell
 */
public class RuleBuilderShipmentWeightAction implements RuleBuilderActionType, RuilBuilderRawDataValueActionCallback{

    
    private RawDataActionController ctrl;
    private AssignValueAction valueAction;
    private Action action;
    
    
    
    @Override
    public Scene openMenu() {
        try {
            URL location = getClass().getResource("RawDataAction.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            ctrl = (RawDataActionController) fxmlLoader.getController();
            ctrl.setRuleBulderConstantAction(this);
            ctrl.setFieldNames(FieldName.SHIPMENTWEIGHT, FieldName.NUMBEROFPALLETS);
            if (this.valueAction != null){
                ctrl.setAction(valueAction);
            }
            return new Scene(root);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
    
    public void setAction(Action action){
        this.action = action;
    }

    @Override
    public long persist() {
        ValueActionService vaService = ValueActionService.getINSTANCE();
        vaService.addOrEditValueAction(valueAction);
        return valueAction.getId();
    }

    @Override
    public void load(long id) {
        ValueActionService vaService = ValueActionService.getINSTANCE();
        valueAction = vaService.findValueActionById(id);
    }

    public static String getDisplayName() {
        return "Map shipment weight / pallets based on raw data";
    }
    
    public void setValueAction(AssignValueAction valueAction){
        this.valueAction = valueAction;
    }
    
    public String displayedValue(){
        FieldNameStringConverter sc = new FieldNameStringConverter();
        return sc.toString(valueAction.getName())+ ": " + valueAction.getKey().getKey();
    }

    public static ActionType getActionType() {
        return ActionType.ASSIGNRAWDATAVALUE;
    }

    @Override
    public void setDataKeys(List<DataKey> keys) {
        ctrl.setKeys(keys);
    }

    
    
   
}
