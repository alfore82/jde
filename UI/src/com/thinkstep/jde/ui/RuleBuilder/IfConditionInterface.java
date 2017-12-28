/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;

import com.thinkstep.jde.persistence.entities.Rules.Rule;

/**
 *
 * @author forell
 */
public interface IfConditionInterface extends MappingItem{
    public Rule getRule();
    public void setRule(Rule r);
    public boolean checkSave();
    
}
