/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author forell
 */
public class DistanceCalculationWrapper {
    private List<Tour> tours = new ArrayList<>();
    
    DistanceCalculationWrapper(){
        
    }
    
    public void calculateDistances(Set<Location> locations, String tourId){
        boolean found = false;
        for (Tour tour:tours){
            if (tour.getTourId().equals(tourId)){
                found = true;
            }
        }
        if (!found) {
           
            Tour t = new Tour();
            t.setTourId(tourId);
            boolean testTour = true;
            if (locations == null){
                testTour = false;
            }
            if (testTour){
                if (locations.size()<2){
                    testTour = false;
                }
                
            }
            if (testTour){
                for (Location l:locations){
                    if (l.getIndexEurope() == 0) testTour = false;                
                }
            }
            
            if (testTour){
                AntColony ac = new AntColony(t, locations);
                ac.startOptimization();
            } else {
                t.setTourCalculatedSuccessFully(false);
            }
            tours.add(t);
        }
    }
    
    
    public double getDistance(Location from, Location to, String tourId) throws MissingLocationsException{
        for (Tour t:tours){    
            if (t.getTourId().equals(tourId)){
                if (t.isTourCalculatedSuccessFully()){
                    return t.getDistanceFromTour(from, to);
                }
                else {
                    throw new MissingLocationsException(t.getMissingLocations());
                }
            }
        }
        return 0;
    }
    
    public void clearTours(){
        tours.clear();
    }
}
