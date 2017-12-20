/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.Location;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author forell
 */
public class Ant implements Runnable{
    private List<Location> locationsToBeVisited;
    private List<Location> locationsVisited;
    private Location currentLocation;
    private Graph graph;
    private double totalDistance;
    
    Ant(Graph graph, Location startLocation){
        this.graph = graph;
        this.locationsToBeVisited = new ArrayList<>();
        this.locationsToBeVisited.addAll(graph.getLocations());
        this.locationsToBeVisited.remove(startLocation);
        this.locationsVisited = new ArrayList<>();
        this.locationsVisited.add(startLocation);
        this.currentLocation = startLocation;
        this.totalDistance = 0;
    }

    @Override
    public void run() {
        int nodes = locationsToBeVisited.size();
        
        for (int i = 0; i< nodes; i++) {
            Edge e = graph.getMostAttractiveEdge(currentLocation, locationsVisited);
            totalDistance = totalDistance + e.getDistance();
            locationsToBeVisited.remove(e.getNextLocation(currentLocation));
            locationsVisited.add(e.getNextLocation(currentLocation));
            currentLocation = e.getNextLocation(currentLocation);
        }
        Edge e = graph.findEdge(locationsVisited.get(locationsVisited.size()-1), locationsVisited.get(0));
        totalDistance = totalDistance + e.getDistance();
    }

    public List<Location> getLocationsVisited() {
        return locationsVisited;
    }

    public void setLocationsVisited(List<Location> locationsVisited) {
        this.locationsVisited = locationsVisited;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    @Override
    public String toString() {
        return "Ant{" + "locationsVisited=" + locationsVisited + ", totalDistance=" + totalDistance + '}';
    }
    
    
    
    
    
}
