/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.Location;
import java.util.List;

/**
 *
 * @author forell
 */
class MissingLocationsException extends Exception {

    private List<Location> ls;
    public MissingLocationsException(List<Location> ls) {
        this.ls = ls;
    }

    public List<Location> getLs() {
        return ls;
    }
    
    
    
}
