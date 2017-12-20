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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.openide.util.Exceptions;

/**
 *
 * @author forell
 */
public class AntColony {
    private final int generations = 25;
    private ExecutorService exService;
    private Set<Location> locations; 
    private Graph g;
    private List<Location> bestRoute;
    private double bestDistance;
    private Tour t;
    
    public AntColony(Tour t, Set<Location> locations) {
        this.locations = locations;
        this.t = t;
        g = new Graph(locations);
        bestDistance = Double.MAX_VALUE;
    }
    
    public void startOptimization(){
        boolean converged = false;
        int i = 0;
        while (!converged){
            List<Ant> ants = generateAnts();
            runAnts(ants);
            updateBestSolution(ants);
            g.updatePheromoneLevels(findBestAnt(ants));
            converged = checkConverged(ants);
            //printAnts(ants, i);
            i++;
        }
        t.setLocations(bestRoute);
        t.setTourCalculatedSuccessFully(true);
    }
    
    private List<Ant> generateAnts(){
        List<Ant> ants = new ArrayList<>();
        for (Location l:locations){
            Ant a = new Ant(g,l);
            ants.add(a);
        }
        return ants;
    }
    
    private void runAnts(List<Ant> ants){
        //exService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        exService = Executors.newFixedThreadPool(1);
        for (Ant a:ants){
            exService.execute(a);
        }
        exService.shutdown();
        try {
            exService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }
    
    private void updateBestSolution(List<Ant> ants){
        for (Ant a:ants){
            if (a.getTotalDistance() < bestDistance){
                bestDistance = a.getTotalDistance();
                bestRoute = a.getLocationsVisited();
            }
        }
    }
    
    private Ant findBestAnt(List<Ant> ants){
        Ant bestAnt = null;
        
        double distance = Double.MAX_VALUE;
        for (Ant a:ants){
            if (a.getTotalDistance()< distance){
                bestAnt = a;
                distance = a.getTotalDistance();
            }
        }
        System.out.println("Best Ant: "+ bestAnt.toString());
        return bestAnt;
    }
    
    private void printAnts(List<Ant> ants, int generation){
        System.out.println("Generation: " + (generation + 1));
        System.out.println("====================");
        int i = 1;
        for (Ant a:ants){
            System.out.println("Ant " + i + " " + a.toString());
            i++;
        }
        System.out.println("--------------------");
        System.out.println("Best Route:  " + bestRoute.toString() + "Distance: "+ bestDistance);
        System.out.println("====================");
        System.out.println("");
    }
    
    private void printEdges(int generation){
        System.out.println("Generation: " + (generation + 1));
        System.out.println("====================");
        g.printEdges();
        
        
    }
    

    private boolean checkConverged(List<Ant> ants) {
        boolean converged = true;
        for (Ant a:ants){
            if (a.getTotalDistance() != bestDistance){
                converged = false;
            }
        }
        return converged;
    }
    
    
    
    
}
