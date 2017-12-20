/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.Rules;

import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author forell
 */
@Entity
public class CalculateDistanceAction {
    @Id @GeneratedValue
    private long id;
    @Enumerated(EnumType.STRING)
    private FieldName name;
    
    private DataKey tourIdentifier;
    private DataKey startCountry;
    private DataKey startZipCode;
    private DataKey startCity;
    private DataKey destinationCountry;
    private DataKey destinationZipCode;
    private DataKey destinationCity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FieldName getName() {
        return name;
    }

    public void setName(FieldName name) {
        this.name = name;
    }

    public DataKey getTourIdentifier() {
        return tourIdentifier;
    }

    public void setTourIdentifier(DataKey tourIdentifier) {
        this.tourIdentifier = tourIdentifier;
    }

    public DataKey getStartCountry() {
        return startCountry;
    }

    public void setStartCountry(DataKey startCountry) {
        this.startCountry = startCountry;
    }

    public DataKey getStartZipCode() {
        return startZipCode;
    }

    public void setStartZipCode(DataKey startZipCode) {
        this.startZipCode = startZipCode;
    }

    public DataKey getStartCity() {
        return startCity;
    }

    public void setStartCity(DataKey startCity) {
        this.startCity = startCity;
    }

    public DataKey getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(DataKey destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public DataKey getDestinationZipCode() {
        return destinationZipCode;
    }

    public void setDestinationZipCode(DataKey destinationZipCode) {
        this.destinationZipCode = destinationZipCode;
    }

    public DataKey getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(DataKey destinationCity) {
        this.destinationCity = destinationCity;
    }
    

    
    
    
}
