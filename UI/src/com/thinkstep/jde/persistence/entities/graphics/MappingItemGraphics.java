/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.graphics;

import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author forell
 */
@Entity
public class MappingItemGraphics {
    @Id @GeneratedValue
    private long id;
    @Enumerated(EnumType.STRING)
    private MappingType mappingType;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    private LogisticServiceProvider lsp;
    
    @OneToOne
    private MappingItemGraphics mappingItemGraphics;
    private double layoutX;
    private double layoutY;
    
    private double dockLayoutX;
    private double dockLayoutY;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MappingType getMappingType() {
        return mappingType;
    }

    public void setMappingType(MappingType mappingType) {
        this.mappingType = mappingType;
    }

    public LogisticServiceProvider getLsp() {
        return lsp;
    }

    public void setLsp(LogisticServiceProvider lsp) {
        this.lsp = lsp;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    public MappingItemGraphics getMappingItemGraphics() {
        return mappingItemGraphics;
    }

    public void setMappingItemGraphics(MappingItemGraphics mappingItemGraphics) {
        this.mappingItemGraphics = mappingItemGraphics;
    }

    
    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public double getDockLayoutX() {
        return dockLayoutX;
    }

    public void setDockLayoutX(double dockLayoutX) {
        this.dockLayoutX = dockLayoutX;
    }

    public double getDockLayoutY() {
        return dockLayoutY;
    }

    public void setDockLayoutY(double dockLayoutY) {
        this.dockLayoutY = dockLayoutY;
    }
    
    
    
    
    
}
