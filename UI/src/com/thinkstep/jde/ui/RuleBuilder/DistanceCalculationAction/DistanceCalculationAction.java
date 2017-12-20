/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.DistanceCalculationAction;

import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.Rules.CalculateDistanceAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.CalculateDistanceActionService;
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
public class DistanceCalculationAction implements RuleBuilderActionType, DistanceCalculationCallback{

    private DistanceCalcualtionActionController distanceCalculationActionController;
    private CalculateDistanceAction distAction;
    private Action action;
    
    
    
    @Override
    public Scene openMenu() {
        try{
            URL location = getClass().getResource("DistanceCalcualtionAction.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            distanceCalculationActionController = (DistanceCalcualtionActionController) fxmlLoader.getController();
            distanceCalculationActionController.setFieldNames(FieldName.DISTANCEPICKUP, FieldName.DISTANCEMAIN, FieldName.DISTANCEDELIVERY);
            distanceCalculationActionController.setDistanceCalculationCallback(this);
            if (this.distAction != null){
                distanceCalculationActionController.setAction(distAction);
            }
            return new Scene(root);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }

    @Override
    public long persist() {
        CalculateDistanceActionService caService = CalculateDistanceActionService.getINSTANCE();
        caService.addOrEditCalculateDistanceAction(distAction);
        return distAction.getId();
    }

    @Override
    public void load(long id) {
        CalculateDistanceActionService caService = CalculateDistanceActionService.getINSTANCE();
        distAction = caService.findCalculateDistanceActionById(id);
    }
    
    public static String getDisplayName() {
        return "Calculate Distance based on Raw data";
    }

    @Override
    public String displayedValue() {
        FieldNameStringConverter sc = new FieldNameStringConverter();
        return sc.toString(distAction.getName()) + ": From: " + distAction.getStartCountry().getKey() + 
            " - " + distAction.getStartZipCode().getKey() + 
            " " + distAction.getStartCity().getKey() +
            "\n\tTo: " + distAction.getDestinationCountry().getKey() + 
            " - " + distAction.getDestinationZipCode().getKey() + 
            " " + distAction.getDestinationCity().getKey();
    }

    @Override
    public void setCalculateDistanceAction(CalculateDistanceAction distAction) {
        this.distAction = distAction;
    }
    
    public static ActionType getActionType() {
        return ActionType.CALCULATEDISTANCE;
    }

    @Override
    public void setDataKeys(List<DataKey> keys) {
        distanceCalculationActionController.setKeys(keys);
    }
    
}
