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
public class Edge {
    private Location l1;
    private Location l2;
    private double distance;
    private double pheromoneload;

    public Location getL1() {
        return l1;
    }

    public void setL1(Location l1) {
        this.l1 = l1;
    }

    public Location getL2() {
        return l2;
    }

    public void setL2(Location l2) {
        this.l2 = l2;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPheromoneload() {
        return pheromoneload;
    }

    public void setPheromoneload(double pheromoneload) {
        this.pheromoneload = pheromoneload;
    }
    
    public double getAttractiveness(){
        double visibility = Math.pow(1/distance, 0.8);
        double pheromone = Math.pow(pheromoneload,0.2);
        double result = visibility * pheromone;
        return  result;
    }
    
    public boolean edgeRelevant(Location loc1, List<Location> locationsVisited){
        boolean result = false;
        
        if (this.l1.equals(loc1) || this.l2.equals(loc1)) result = true;
        
        if (result){
            Location l = getNextLocation(loc1);
            if (locationsVisited.contains(l)) result = false;
        }
        
        return result;
    }
    
    public boolean sameEdge(Location loc1, Location loc2){
       
        
        if (this.l1.equals(loc1) && this.l2.equals(loc2)) return true;
        if (this.l1.equals(loc2) && this.l2.equals(loc1)) return true;
        
        return false;
        
    }
    
    public Location getNextLocation(Location loc1){
         if (this.l1.equals(loc1)){
             return l2;
         } else {
             return l1;
         }
    }

    @Override
    public String toString() {
        return "Edge{" + "l1=" + l1 + ", l2=" + l2 + ", distance=" + distance + ", pheromoneload=" + pheromoneload + ", attractiveness" + getAttractiveness() + '}';
    }
    
    
}
