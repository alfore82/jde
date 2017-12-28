/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.imports.filereaders.DistanceMatrixProvider;
import com.thinkstep.jde.imports.filereaders.OdsProvider;
import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.Location;
import com.thinkstep.jde.ui.LocationSearch.MissingLocationsMappingController;
import com.thinkstep.jde.ui.helpers.FXUitlities;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.openide.util.Exceptions;

/**
 *
 * @author forell
 */
public class DistanceService {
    private static final DistanceService INSTANCE = new DistanceService();
    private List<Short> distances;
    private List<Location> locations;
    private boolean initialized;
    DistanceMatrixProvider dmp;
    OdsProvider odsProvider;
    
    private DistanceService(){
        initialized = false;
        distances = new ArrayList<>();
        locations = new ArrayList<>();
        
        dmp = new DistanceMatrixProvider();
        distances = dmp.readDistanceMatrix();
        odsProvider = new OdsProvider();
        locations = odsProvider.readOds();
        initialized = true;
        System.out.println("DistanceService initialized");

    }
    
    public static DistanceService getINSTANCE(){
       return INSTANCE;
    }

    public boolean isInitialized() {
        return initialized;
    }
    
    public void updateLocations(Map<String, Set<Location>> tours){
        List<Location> searcedLocations = convertTours(tours);
        Set<Country> countries = getCountries(searcedLocations);
        
        
        List <Location> filteredList = locations.stream().filter(location -> {
                for (Country c:countries){
                    if (c != null){
                        if (c.equals(location.getCountry())) return true;
                    }
                }
                return false;
        }).collect(Collectors.toList());
        int i = 0;
        
        
        Set<Location> locationsToBeFound = new HashSet<>();
        
        Set<Location> locationsToBeSearched = new HashSet<>();
        locationsToBeFound.addAll(searcedLocations);
        locationsToBeSearched.addAll(locationsToBeFound);
        
        for (Location l:filteredList){
            i++;
            
            
            List<Location> pastIndex = new ArrayList<>();
            for (Location searchedLocation:locationsToBeSearched){
                if (l.getCountry()!= null && l.getCountry().equals(searchedLocation.getCountry()) && 
                        l.getZipCode().compareToIgnoreCase(searchedLocation.getOptimizedZip())>1){
                    pastIndex.add(searchedLocation);
                    
                }
            }
            locationsToBeSearched.removeAll(pastIndex);
            List<Location> perfectMatches = new ArrayList<>();
            for (Location searchedLocation:locationsToBeSearched){
                if (l.getCountry()!= null && l.getCountry().equals(searchedLocation.getCountry()) &&
                    l.getCity1().toLowerCase().contains(searchedLocation.getCity1().toLowerCase()) &&
                    l.getOptimizedZip().toLowerCase().equals(searchedLocation.getOptimizedZip().toLowerCase())){
                searchedLocation.setIndexEurope(l.getIndexEurope());
                
                perfectMatches.add(searchedLocation);
                    
                continue;
                }
                if (searchedLocation.getIndexEurope() == 0 && l.getCountry()!= null && l.getCountry().equals(searchedLocation.getCountry()) &&
                    l.getOptimizedZip().toLowerCase().equals(searchedLocation.getOptimizedZip().toLowerCase())){
                searchedLocation.setIndexEurope(l.getIndexEurope());
                    
                continue;
                }
                if (searchedLocation.getIndexEurope() == 0 && l.getCountry()!= null && l.getCountry().equals(searchedLocation.getCountry()) &&
                    l.getCity1().toLowerCase().contains(searchedLocation.getCity1())){
                    searchedLocation.setIndexEurope(l.getIndexEurope());
                continue;
                }
            }
            locationsToBeSearched.removeAll(perfectMatches); 
        }
        Set<Location> locationsNotFound = new HashSet<>();
         for (Location locFound:locationsToBeFound){
                if (locFound.getIndexEurope()==0){
                    locationsNotFound.add(locFound);
                }
            }
        if (locationsNotFound.size() > 0){
            try {

                FXUitlities.runAndWait(()->{
                    try {
                        URL location = getClass().getResource("/com/thinkstep/jde/ui/LocationSearch/MissingLocationsMapping.fxml");
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(location);
                        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                        Parent root = (Parent) fxmlLoader.load(location.openStream());
                        MissingLocationsMappingController ctrl = (MissingLocationsMappingController) fxmlLoader.getController();
                        ctrl.setMissingLocations(locationsNotFound);
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.initStyle(StageStyle.DECORATED);
                        stage.initModality(Modality.NONE);
                        stage.setAlwaysOnTop(true);
                        stage.showAndWait();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }


                    });

            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        for (Location l:searcedLocations){
            for (Location locFound:locationsToBeFound){
                if (l.equals(locFound)){
                    l.setIndexEurope(locFound.getIndexEurope());
                }
            }
        }
        
    }
    
    public double getDistance2(Location start, Location end){
        if (start != null && start != null){
            double dist = getDistance(start.getIndexEurope(), end.getIndexEurope());
            return dist;
        } else {
            return 0;
        }
    }
    
    private Set<Country> getCountries(List<Location> locations){
        Set<Country> cs = locations.stream().collect(new CountryCollector());
        return cs;
    }
    
    private double getDistance(int indexStart, int indexEnd){
        int a = Math.max(indexStart, indexEnd);
        int b = Math.min(indexStart, indexEnd);
        int pos;
        if (a == b){
            return 10;
        } else {
            pos = (a-1) * (a-2) / 2 + b;
            return (double) distances.get(pos-1);
        }
    }

    private List<Location> convertTours(Map<String, Set<Location>> tours) {
        List<Location> searchedLocations = new ArrayList<>();
        
        Set<String> keys = tours.keySet();
        for (String key:keys){
            searchedLocations.addAll(tours.get(key));
        }
        return searchedLocations;
    }

    public List<Location> getLocations() {
        return locations;
    }
    
    
    
}
