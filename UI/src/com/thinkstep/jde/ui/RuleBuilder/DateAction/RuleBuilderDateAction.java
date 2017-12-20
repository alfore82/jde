/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.DateAction;

import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.Rules.AssignDateAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.DateActionService;
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
public class RuleBuilderDateAction implements RuleBuilderActionType, RuleBuilderDateActionCallback{

    
    private DateActionController dateController;
    private AssignDateAction valueAction;
    private Action action;
    
    
    
    @Override
    public Scene openMenu() {
        try {
            URL location = getClass().getResource("DateAction.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            dateController = (DateActionController) fxmlLoader.getController();
            dateController.setFieldNames(FieldName.DATE);
            dateController.setRuleBulderConstantAction(this);
            if (this.valueAction != null){
                dateController.setAction(valueAction);
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
        DateActionService daService = DateActionService.getINSTANCE();
        daService.addOrEditAssignDateActionAction(valueAction);
        return valueAction.getId();
    }

    @Override
    public void load(long id) {
        DateActionService daService = DateActionService.getINSTANCE();
        valueAction = daService.findAssignDateActionById(id);
    }

    public static String getDisplayName() {
        return "Map date based on raw data";
    }
    
    public void setDateAction(AssignDateAction valueAction){
        this.valueAction = valueAction;
    }
    
    public String displayedValue(){
        FieldNameStringConverter sc = new FieldNameStringConverter();
        return sc.toString(valueAction.getName())+ ": " + valueAction.getDataKey().getKey();
    }

    public static ActionType getActionType() {
        return ActionType.DATE;
    }

    @Override
    public void setDataKeys(List<DataKey> keys) {
        dateController.setKeys(keys);
    }

    

    
    
   
}
