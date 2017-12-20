/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.Rules;

import com.thinkstep.jde.persistence.entities.graphics.MappingType;
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
public class Action {
    @Id @GeneratedValue
    long id;
    @OneToOne
    private LogisticServiceProvider lsp;
    @Enumerated(EnumType.STRING)
    ActionType actionType;
    long actionId;
    @OneToOne
    private Rule parent;
    @Enumerated(EnumType.STRING)
    private ParentConnector parentConnector;
    @Enumerated(EnumType.STRING)
    private MappingType mappingType;
    private long mappingItemId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LogisticServiceProvider getLsp() {
        return lsp;
    }

    public void setLsp(LogisticServiceProvider lsp) {
        this.lsp = lsp;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public Rule getParent() {
        return parent;
    }

    public void setParent(Rule parent) {
        this.parent = parent;
    }

    public long getMappingItemId() {
        return mappingItemId;
    }

    public void setMappingItemId(long mappingItemId) {
        this.mappingItemId = mappingItemId;
    }

    public MappingType getMappingType() {
        return mappingType;
    }

    public void setMappingType(MappingType mappingType) {
        this.mappingType = mappingType;
    }

    public ParentConnector getParentConnector() {
        return parentConnector;
    }

    public void setParentConnector(ParentConnector parentConnector) {
        this.parentConnector = parentConnector;
    }
    
    
    
    
    
}
