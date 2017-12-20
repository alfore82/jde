/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.rawdata;

import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author forell
 */
@Entity
public class DataKey {
    @Id @GeneratedValue
    private long id;
    @OneToOne
    private LogisticServiceProvider lsp;
    private String key;
    
    public DataKey(){
        this.lsp = new LogisticServiceProvider();
        this.key = "";
    }
    
    public DataKey(LogisticServiceProvider lsp){
        this.lsp = lsp;
        this.key = "";
    }

    public DataKey(LogisticServiceProvider lsp, String key) {
        this.lsp = lsp;
        this.key = key;
    }

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }




    
}

