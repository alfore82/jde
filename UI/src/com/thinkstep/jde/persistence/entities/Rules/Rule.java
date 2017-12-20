/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.Rules;

import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
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
public class Rule {
    @Id @GeneratedValue
    private long id;
    @OneToOne
    private LogisticServiceProvider lsp;
    @Enumerated(EnumType.STRING)
    private MappingType mappingType;
    @OneToOne
    private DataKey key;
    @Enumerated(EnumType.STRING)
    private RuleType ruleType;
    private String valueString;
    private double valueDouble;
    @OneToOne
    private Rule parent;
    @Enumerated(EnumType.STRING)
    private ParentConnector parentConnector;
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

    public MappingType getMappingType() {
        return mappingType;
    }

    public void setMappingType(MappingType mappingType) {
        this.mappingType = mappingType;
    }

    public DataKey getKey() {
        return key;
    }

    public void setKey(DataKey key) {
        this.key = key;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public double getValueDouble() {
        return valueDouble;
    }

    public void setValueDouble(double valueDouble) {
        this.valueDouble = valueDouble;
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

    public ParentConnector getParentConnector() {
        return parentConnector;
    }

    public void setParentConnector(ParentConnector parentConnector) {
        this.parentConnector = parentConnector;
    }
    
    
    
    
    
    
}
