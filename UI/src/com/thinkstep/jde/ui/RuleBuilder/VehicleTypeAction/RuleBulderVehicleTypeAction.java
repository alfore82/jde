/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.VehicleTypeAction;

import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.Rules.AssignVehicleTypeAction;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.VehicleTypeActionService;
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
public class RuleBulderVehicleTypeAction implements RuleBuilderActionType{

    
    private VehicleTypeActionController vehicleTypeActionController;
    private AssignVehicleTypeAction vehicleTypeAction;
    private Action action;
    
    
    
    @Override
    public Scene openMenu() {
        try {
            URL location = getClass().getResource("VehicleTypeAction.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            vehicleTypeActionController = (VehicleTypeActionController) fxmlLoader.getController();
            vehicleTypeActionController.setRuleBulderFuelTypeAction(this);
            if (this.vehicleTypeAction != null){
                vehicleTypeActionController.setAction(vehicleTypeAction);
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
        VehicleTypeActionService vtService = VehicleTypeActionService.getINSTANCE();
        vtService.addOrEditVehicleTypeAction(vehicleTypeAction);
        return vehicleTypeAction.getId();
    }

    @Override
    public void load(long id) {
        VehicleTypeActionService vtService = VehicleTypeActionService.getINSTANCE();
        vehicleTypeAction = vtService.findVehicleTypeActionById(id);
    }

    public static String getDisplayName() {
        return "Map vehicle type";
    }
    
    public void setVehicleTypeAction(AssignVehicleTypeAction vehicleTypeAction){
        this.vehicleTypeAction = vehicleTypeAction;
    }
    
    public String displayedValue(){
        return "Vehicle Type: " + vehicleTypeAction.getVehicleType().getName();
    }

    public static ActionType getActionType() {
        return ActionType.VEHICLETYPE;
    }

    @Override
    public void setDataKeys(List<DataKey> keys) {
        // do nothing;
    }

    
    
   
}
