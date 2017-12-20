/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.emissioncalculation;

import com.thinkstep.jde.persistence.entities.Country;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author forell
 */
@Entity
public class EmissionProcess {
    
    @Id @GeneratedValue
    private long id;
    private String name = "";
    @OneToOne
    private Country country;
    @Transient
    private List<Parameter> params = new ArrayList<>();
    private String paramsSerial = "";
    @Transient
    private List<EmissionFactorInstance> emissionFactorInstances = new ArrayList<>();
    private String emissionFactorInstancesSerial = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> getParams() {
        return params;
    }

    public void setParams(List<Parameter> params) {
        this.params = params;
    }

    public String getParamsSerial() {
        return paramsSerial;
    }

    public void setParamsSerial(String paramsSerial) {
        this.paramsSerial = paramsSerial;
    }

    public List<EmissionFactorInstance> getEmissionFactorInstances() {
        return emissionFactorInstances;
    }

    public void setEmissionFactorInstances(List<EmissionFactorInstance> emissionFactorInstances) {
        this.emissionFactorInstances = emissionFactorInstances;
    }

    public String getEmissionFactorInstancesSerial() {
        return emissionFactorInstancesSerial;
    }

    public void setEmissionFactorInstancesSerial(String emissionFactorInstancesSerial) {
        this.emissionFactorInstancesSerial = emissionFactorInstancesSerial;
    }

    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    
    
    
    public void serialize(){
        paramsSerial = "";
        for (Parameter p:params){
            paramsSerial = paramsSerial + p.toString() + "|";
        }
        paramsSerial = paramsSerial.substring(0, paramsSerial.length()-1);
        
        emissionFactorInstancesSerial = "";
        for (EmissionFactorInstance efi:emissionFactorInstances){
            emissionFactorInstancesSerial = emissionFactorInstancesSerial + efi.toString() + "|";
        }
        
        emissionFactorInstancesSerial = emissionFactorInstancesSerial.substring(0, emissionFactorInstancesSerial.length()-1);
    }
    
    public void unserialize(){
        params.clear();
        if (!"".equals(paramsSerial)){
            String[] serials = paramsSerial.split("\\|");
            for (int i=0; i<serials.length; i++){
                params.add(Parameter.fromString(serials[i]));
            }
        }
        emissionFactorInstances.clear();
        if (!"".equals(emissionFactorInstancesSerial)){
            String[] efiserials = emissionFactorInstancesSerial.split("\\|");
            for (int i=0; i<efiserials.length; i++){
                emissionFactorInstances.add(EmissionFactorInstance.fromString(efiserials[i], params));
            }
        }
    }
    
}
