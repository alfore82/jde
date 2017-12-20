/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.VehicleTypeAction.LoadFactorBasedTour;

import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.Rules.CalculateLoadAction;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.CalculateLoadActionService;
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
public class LoadCalculatonAction implements RuleBuilderActionType{

    
    private LoadCalculationController loadCalculationAction;
    private CalculateLoadAction calculateLoadAction;
    private Action action;
    
    
    
    @Override
    public Scene openMenu() {
        try {
            URL location = getClass().getResource("LoadCalculation.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            loadCalculationAction = (LoadCalculationController) fxmlLoader.getController();
            loadCalculationAction.setRuleBulderFuelTypeAction(this);
            if (this.calculateLoadAction != null){
                loadCalculationAction.setAction(calculateLoadAction);
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
        CalculateLoadActionService clService = CalculateLoadActionService.getINSTANCE();
        clService.addOrEditCalculateLoadAction(calculateLoadAction);
        return calculateLoadAction.getId();
    }

    @Override
    public void load(long id) {
        CalculateLoadActionService clService = CalculateLoadActionService.getINSTANCE();
        calculateLoadAction = clService.findCalculateLoadActionById(id);
    }

    public static String getDisplayName() {
        return "Calculate load factor based on tour data";
    }
    
    public void setAction(CalculateLoadAction calculateLoadAction){
        this.calculateLoadAction = calculateLoadAction;
    }
    
    public String displayedValue(){
        FieldNameStringConverter sc = new FieldNameStringConverter();
        return  sc.toString(calculateLoadAction.getName()) + ": " + calculateLoadAction.getTourIdentifier().getKey();
    }

    public static ActionType getActionType() {
        return ActionType.CALCULATELOAD;
    }

    @Override
    public void setDataKeys(List<DataKey> keys) {
        loadCalculationAction.setKeys(keys);
    }

    
    
   
}
