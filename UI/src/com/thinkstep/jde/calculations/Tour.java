/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.Location;
import com.thinkstep.jde.persistence.services.DistanceService;
import com.thinkstep.jde.persistence.services.LocationNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author forell
 */
public class Tour {
    
   
    
    private String tourId = "";
    private List<Location> locations = new ArrayList<>();
    private List<Double> distanceToNextLocation = new ArrayList<>();
    private boolean tourCalculatedSuccessFully = false;
    
    
    
    
    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
        distanceToNextLocation.clear();
        DistanceService ds = DistanceService.getINSTANCE();
        for (int i = 0; i < locations.size()-1;i++){
             distanceToNextLocation.add(ds.getDistance2(locations.get(i), locations.get(i+1)));
        }
        distanceToNextLocation.add(ds.getDistance2(locations.get(locations.size()-1), locations.get(0)));
    }
   
    public double getDistance(Location from, Location to) throws LocationNotFoundException{
        
        int startIndex = locations.indexOf(from);
        int endIndex  = locations.indexOf(to);
        
        DistanceService ds = DistanceService.getINSTANCE();
        double distance = 0;
        if (startIndex < endIndex && startIndex>=0 && endIndex>0) {
            for (int i = startIndex; i<=endIndex-1; i++) {
                distance = distance + ds.getDistance2(locations.get(i), locations.get(i+1));
            }
        } else {
            // Bis zum letzten element
            for (int i = startIndex; i<locations.size() - 1; i++) {
                distance = distance + ds.getDistance2(locations.get(i), locations.get(i + 1));
            }
            //der Sprung nach vorn
            distance = distance + ds.getDistance2(locations.get(locations.size()-1), locations.get(0));
            //ab i = 1
            for (int i = 1; i<=endIndex; i++) {
                distance = distance + ds.getDistance2(locations.get(i-1), locations.get(i));
            }
        }
        return distance;
        
    }
    
    public double getDistanceFromTour(Location from, Location to){
        
        int startIndex = locations.indexOf(from);
        int endIndex  = locations.indexOf(to);
        if (startIndex == -1 || endIndex == -1){
            System.out.println("Bastard");
        }
        DistanceService ds = DistanceService.getINSTANCE();
        double distance = 0;
        if (startIndex < endIndex && startIndex>=0 && endIndex>0) {
            for (int i = startIndex; i<=endIndex-1; i++) {
                distance = distance + distanceToNextLocation.get(i);
            }
        } else {
            // Bis zum letzten element
            for (int i = startIndex; i<locations.size(); i++) {
                distance = distance + distanceToNextLocation.get(i);
            }
            //ab i = 0
            for (int i = 0; i<=endIndex-1; i++) {
                distance = distance + distanceToNextLocation.get(i);
            }
        }
        return distance;
        
    }

    public boolean isTourCalculatedSuccessFully() {
        return tourCalculatedSuccessFully;
    }

    public void setTourCalculatedSuccessFully(boolean tourCalculatedSuccessFully) {
        this.tourCalculatedSuccessFully = tourCalculatedSuccessFully;
    }
    
    public List<Location> getMissingLocations(){
        List<Location> missingLocs = new ArrayList<>();
        for (Location l:locations){
            if (l.getIndexEurope() == 0) {
                missingLocs.add(l);
            }
        }
        return missingLocs;
    }
    
}
