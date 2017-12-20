/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.ConstantAction;

import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.Rules.AssignConstantAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.ConstantActionService;
import com.thinkstep.jde.ui.Converters.FieldNameStringConverter;
import com.thinkstep.jde.ui.RuleBuilder.RuleBuilderActionType;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
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
public class RuleBuilderConstantDistanceAction implements RuleBuilderActionType, ConstantActionCallback{

    
    private ConstantActionController constantActionController;
    private AssignConstantAction constantAction;
    private Action action;
    
    
    
    @Override
    public Scene openMenu() {
        try {
            URL location = getClass().getResource("ConstantAction.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            constantActionController = (ConstantActionController) fxmlLoader.getController();
            constantActionController.setRuleBulderConstantAction(this);
            constantActionController.setFieldNames(FieldName.DISTANCEPICKUP, FieldName.DISTANCEMAIN, FieldName.DISTANCEDELIVERY);
            if (this.constantAction != null){
                constantActionController.setAction(constantAction);
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
        ConstantActionService ftaService = ConstantActionService.getINSTANCE();
        ftaService.addOrEditConstantAction(constantAction);
        return constantAction.getId();
    }

    @Override
    public void load(long id) {
        ConstantActionService caService = ConstantActionService.getINSTANCE();
        constantAction = caService.findAConstantActionById(id);
    }

    public static String getDisplayName() {
        return "Map constant constant distance value";
    }
    
    public void setConstantAction(AssignConstantAction constantAction){
        this.constantAction = constantAction;
    }
    
    public String displayedValue(){
        NumberFormat nf = NumberFormat.getInstance();
        FieldNameStringConverter sc = new FieldNameStringConverter();
        return sc.toString(constantAction.getName())+ ": " + constantAction.getValue();
    }

    public static ActionType getActionType() {
        return ActionType.ASSIGNCONSTANT;
    }

    @Override
    public void setDataKeys(List<DataKey> keys) {
        //Nothing to Do
    }

    
    
   
}
