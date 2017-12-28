/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;

import com.thinkstep.jde.persistence.entities.Rules.Action;

/**
 *
 * @author forell
 */
public interface ActionInterface extends MappingItem{
    public Action getAction();
    public void setAction(Action r);
    public boolean checkSave();
}
