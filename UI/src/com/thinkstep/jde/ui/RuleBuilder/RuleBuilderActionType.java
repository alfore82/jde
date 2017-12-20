/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;

import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import java.util.List;
import javafx.scene.Scene;

/**
 *
 * @author forell
 */
public interface RuleBuilderActionType {
    
    public void setDataKeys(List<DataKey> keys);
    
    public Scene openMenu();
    
    public long persist();
    
    public void load(long id);
    
    public String displayedValue();
    
    public static String getDisplayName(){
        return "Method not overridden";
    };
    
    public static ActionType getActionType(){
        return ActionType.UNDEFINED;
    }
    
}
