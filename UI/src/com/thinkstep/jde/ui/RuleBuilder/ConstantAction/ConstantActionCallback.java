/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.ConstantAction;

import com.thinkstep.jde.persistence.entities.Rules.AssignConstantAction;

/**
 *
 * @author forell
 */
public interface ConstantActionCallback {

    public void setConstantAction(AssignConstantAction action);
    
}
