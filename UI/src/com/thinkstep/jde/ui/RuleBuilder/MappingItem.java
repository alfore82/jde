/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;

import com.thinkstep.jde.persistence.entities.graphics.MappingItemGraphics;
import javafx.scene.Group;

/**
 *
 * @author forell
 */
public interface MappingItem {
    public void setMappingItemGraphics(MappingItemGraphics item);
    public MappingItemGraphics getMappingItemGraphics();

    public Group getGroup();
    public MappingItem getParentItem();
    public Dock getDockStart(Dock dockStart);
    public void shift(Dock dockEnd);
    
}
