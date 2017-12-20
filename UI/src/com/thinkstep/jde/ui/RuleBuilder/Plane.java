/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;

import com.thinkstep.jde.persistence.entities.Rules.ParentConnector;

/**
 *
 * @author forell
 */
public interface Plane {
    public MappingItem addCondition(Dock dockStart, MappingItem parent, ParentConnector parentConnector);
    public MappingItem addAction(Dock dockStart, MappingItem parent, ParentConnector parentConnector);
    public void remove(MappingItem parent);
}
