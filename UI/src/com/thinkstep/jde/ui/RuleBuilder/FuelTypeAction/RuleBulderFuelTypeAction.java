/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.FuelTypeAction;

import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.Rules.AssignFuelTypeAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.FuelTypeActionService;
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
public class RuleBulderFuelTypeAction implements RuleBuilderActionType{

    
    private FuelTypeActionController fuelTypeActionController;
    private AssignFuelTypeAction fuelTypeAction;
    private Action action;
    
    
    
    @Override
    public Scene openMenu() {
        try {
            URL location = getClass().getResource("FuelTypeAction.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            fuelTypeActionController = (FuelTypeActionController) fxmlLoader.getController();
            fuelTypeActionController.setRuleBulderFuelTypeAction(this);
            fuelTypeActionController.setFieldNames(FieldName.FUELTYPEPICKUP, FieldName.FUELTYPEMAIN, FieldName.FUELTYPEDELIVERY);
            if (this.fuelTypeAction != null){
                fuelTypeActionController.setAction(fuelTypeAction);
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
        FuelTypeActionService ftaService = FuelTypeActionService.getINSTANCE();
        ftaService.addOrEditFuelTypeAction(fuelTypeAction);
        return fuelTypeAction.getId();
    }

    @Override
    public void load(long id) {
        FuelTypeActionService ftaService = FuelTypeActionService.getINSTANCE();
        fuelTypeAction = ftaService.findFuelTypeActionById(id);
        fuelTypeAction.unserialize();
    }

    public static String getDisplayName() {
        return "Map fuel type";
    }
    
    public void setFuelTypeAction(AssignFuelTypeAction fuelTypeAction){
        this.fuelTypeAction = fuelTypeAction;
    }
    
    public String displayedValue(){
        return "Fuel Type: " + fuelTypeAction.getEmissionProcess().getCountry().getCountryCodeISO() 
                + " - " + fuelTypeAction.getEmissionProcess().getName() + " (Parameters: " +
                fuelTypeAction.getParametersDisplay() +")";
    }

    public static ActionType getActionType() {
        return ActionType.FUELTYPE;
    }

    @Override
    public void setDataKeys(List<DataKey> keys) {
        //Do nothing
    }

    
    
   
}
