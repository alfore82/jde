/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.Location;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author forell
 */
public class CountryPredicate implements Predicate<Location>{

    private List<Country> countries;
    
    CountryPredicate(List<Country> countries){
        this.countries = countries;
    }
    @Override
    public boolean test(Location t) {
        for (Country c:countries){
            if (c.equals(t.getCountry())) return true;
            
        }
        return false;
    }
    
}
