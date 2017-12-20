/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.Location;
import com.thinkstep.jde.persistence.services.DistanceService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author forell
 */
public class Graph {
    private final double EVAPORATIONRATE = 0.7;
    private List<Location> locations;
    private List<Edge> edges;
    private double totalDistance;
    double totalPheromone = 0;
    
    Graph(Set<Location> locations) {
        this.locations = new ArrayList<>();
        this.locations.addAll(locations);
        buildEdges();
    }
    
    private void buildEdges(){
        totalDistance = 0;
        this.edges = new ArrayList<>();
        DistanceService diService = DistanceService.getINSTANCE();
        for (int i = 0; i < locations.size()-1; i++){
            if (locations.size() > 0){
                
                for (int j = i+1; j< locations.size(); j++){
                    Edge e = new Edge();
                    e.setL1(locations.get(i));
                    e.setL2(locations.get(j));
                    e.setDistance(diService.getDistance2(locations.get(i), locations.get(j)));
                    totalDistance = totalDistance + e.getDistance();
                    edges.add(e);
                } 

            } else {
                System.out.println("no locations in tour");
            }
            
        }
        totalPheromone = 0;
        for (Edge e:edges){
            e.setPheromoneload(1);
            totalPheromone = totalPheromone + 1;
        }
    }
    
   
    
    public Edge getMostAttractiveEdge(Location loc, List<Location> locationsVisited){
        Edge mostAttractiveEdge = null;
        List<Edge> relevantEdges = findRelevantEdeges(loc, locationsVisited);
        double maxAttraction = 0; 
        double mostAttration = 0;
        
        for (Edge e:relevantEdges){
            maxAttraction = maxAttraction + e.getAttractiveness();
        }
        for (Edge e:relevantEdges){
            double a = e.getAttractiveness()/maxAttraction;
            if (a >  mostAttration) {
                mostAttration = a;
                mostAttractiveEdge = e;
            }
        }
        
        return mostAttractiveEdge;
    }
    
    private List<Edge> findRelevantEdeges(Location loc, List<Location> locationsVisited){
        List<Edge> relevantEdges = new ArrayList<>();
        for (Edge e:edges){
            if (e.edgeRelevant(loc, locationsVisited)){
                relevantEdges.add(e);
            }
        }
        return relevantEdges;
    }

    public List<Location> getLocations() {
        return locations;
    }
    
    public void updatePheromoneLevels(Ant bestAnt){
        for (Edge e:edges){
            e.setPheromoneload(e.getPheromoneload()*EVAPORATIONRATE);
        }
        double pheromonePerEdge = 1/bestAnt.getTotalDistance();
        List<Location> locs = bestAnt.getLocationsVisited();
        for (int i= 0; i< locs.size()-1; i++){
                Edge e = findEdge(locs.get(i),locs.get(i+1));
                e.setPheromoneload(e.getPheromoneload()+pheromonePerEdge*e.getDistance());
        }
        Edge e = findEdge(locs.get(locs.size()-1),locs.get(0));
        e.setPheromoneload(e.getPheromoneload()+pheromonePerEdge*e.getDistance());
        
    }
    
    public Edge findEdge(Location l1, Location l2){
        for (Edge e:edges){
            if (e.sameEdge(l1, l2)){
                return e;
            }
        }
        return null;
    }
    
    public void printEdges(){
        for (Edge e: edges){
            System.out.println(e.toString());
        }
    }
}
