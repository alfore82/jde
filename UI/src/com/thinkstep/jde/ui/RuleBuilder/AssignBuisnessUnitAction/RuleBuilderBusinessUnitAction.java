/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.AssignBuisnessUnitAction;

import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.Rules.AssignBusinessUnitAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.BusinessUnitActionService;
import com.thinkstep.jde.ui.Converters.BusinessUnitStringConverter;
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
public class RuleBuilderBusinessUnitAction implements RuleBuilderActionType, AssignBusinessUnitActionCallback{

    
    private BusinessUnitActionController businessUnitActionController;
    private AssignBusinessUnitAction businessUnitAction;
    private Action action;
    
    
    
    @Override
    public Scene openMenu() {
        try {
            URL location = getClass().getResource("AssignBusinessUnitAction.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            businessUnitActionController = (BusinessUnitActionController) fxmlLoader.getController();
            businessUnitActionController.setRuleBulderConstantAction(this);
            businessUnitActionController.setFieldNames(FieldName.BUSINESSUNIT);
            if (this.businessUnitAction != null){
                businessUnitActionController.setAction(businessUnitAction);
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
        BusinessUnitActionService buService = BusinessUnitActionService.getINSTANCE();
        buService.addOrEditAssignBusinessUnitAction(businessUnitAction);
        return businessUnitAction.getId();
    }

    @Override
    public void load(long id) {
        BusinessUnitActionService buService = BusinessUnitActionService.getINSTANCE();
        businessUnitAction = buService.findAssignBusinessUnitActionById(id);
    }

    public static String getDisplayName() {
        return "Map business unit";
    }
    
    @Override
    public void setBusinessUnitAction(AssignBusinessUnitAction businessUnitAction){
        this.businessUnitAction = businessUnitAction;
    }
    
    public String displayedValue(){
        NumberFormat nf = NumberFormat.getInstance();
        FieldNameStringConverter sc = new FieldNameStringConverter();
        BusinessUnitStringConverter busc = new BusinessUnitStringConverter();
        return sc.toString(businessUnitAction.getName())+ ": " + busc.toString(businessUnitAction.getBusinessUnit());
    }

    public static ActionType getActionType() {
        return ActionType.ASSIGNBUSINESSUNIT;
    }

    @Override
    public void setDataKeys(List<DataKey> keys) {
        //Nothing to Do
    }

    
   
}
