/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.preferences;

import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author forell
 */
@Entity
public class TourIdentifier {
    
    @Id @GeneratedValue
    private long id;
    
    private LogisticServiceProvider lsp;
    private DataKey dataKey;

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

    public DataKey getDataKey() {
        return dataKey;
    }

    public void setDataKey(DataKey dataKey) {
        this.dataKey = dataKey;
    }
    
    
    
}
