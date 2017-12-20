/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.Location;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.entities.rawdata.DataType;
import com.thinkstep.jde.persistence.services.CountryService;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *
 * @author forell
 */
public class TourCollector implements Collector<DataRow, Map<String,Set<Location>>, Map<String,Set<Location>>>{
    CountryService cService = CountryService.getINSTANCE();
    DataKey tourIdKey;
    DataKey countryStart;
    DataKey zipStart;
    DataKey cityStart;
    DataKey countryEnd;
    DataKey zipEnd;
    DataKey cityEnd;

    public TourCollector(DataKey tourId, DataKey countryStart, DataKey zipStart, DataKey cityStart, DataKey countryEnd, DataKey zipEnd, DataKey cityend) {
        this.tourIdKey = tourId;
        this.countryStart = countryStart;
        this.zipStart = zipStart;
        this.cityStart = cityStart;
        this.countryEnd = countryEnd;
        this.zipEnd = zipEnd;
        this.cityEnd = cityend;
    }
    
    
    
    
    
    @Override
    public Supplier<Map<String, Set<Location>>> supplier() {
        return new Supplier<Map<String, Set<Location>>>(){
            @Override
            public Map<String, Set<Location>> get() {
                Map<String, Set<Location>> map = new HashMap<>();
                return map;
            }
            
        };
    }

    @Override
    public BiConsumer<Map<String, Set<Location>>, DataRow> accumulator() {
        return new BiConsumer<Map<String, Set<Location>>, DataRow>(){
            @Override
            public void accept(Map<String, Set<Location>> t, DataRow u) {
                
                
                Country cStart = cService.findCountryByCode(u.getDataItem(countryStart).getValueString());
                String city1Start  = u.getDataItem(cityStart).getValueString();
                String zipStartString;
                if (u.getDataItem(zipStart).getDataType().equals(DataType.TEXT)){
                    
                    zipStartString = u.getDataItem(zipStart).getValueString();
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    zipStartString = ""+nf.format(u.getDataItem(zipStart).getValueNumeric());
                }
                boolean createStartLocation = true;
                
                if (cStart == null){
                    createStartLocation = false;
                    u.setError("Adress Extraction Error: Origin country not found; ");
                }
                if ((city1Start == null || city1Start.isEmpty()) && (zipStartString == null || zipStartString.isEmpty())){
                    createStartLocation = false;
                    u.setError("Adress Extraction Error: Neither origin post code nor city given; ");
                }
                Location lStart = null;
                if (createStartLocation ) {
                    lStart = new Location();
                    lStart.setCountry(cStart);
                    lStart.setCity1(city1Start);
                    lStart.setZipCode(zipStartString);
                }
                
                Country cEnd = cService.findCountryByCode(u.getDataItem(countryEnd).getValueString());
                String city1End  = u.getDataItem(cityEnd).getValueString();
                String zipEndString;
                if (u.getDataItem(zipEnd).getDataType().equals(DataType.TEXT)){
                    zipEndString = u.getDataItem(zipEnd).getValueString();
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    zipEndString = ""+nf.format(u.getDataItem(zipEnd).getValueNumeric());
                }
                
                boolean createEndLocation = true;
                if (cEnd == null){
                    createEndLocation = false;
                    u.setError("Adress Extraction Error: Destination country not found; ");
                }
                if ((city1End == null || city1End.isEmpty()) && (zipEndString == null || zipEndString.isEmpty())){
                    createEndLocation = false;
                    u.setError("Adress Extraction Error: Neither destination post code nor city given; ");
                }
                Location lEnd = null;
                if (createEndLocation ) {
                    lEnd = new Location();
                    lEnd.setCountry(cEnd);
                    lEnd.setCity1(city1End);
                    lEnd.setZipCode(zipEndString);
                }
                
                String tourId;
                if (u.getDataItem(tourIdKey).getDataType().equals(DataType.TEXT)){
                    tourId = u.getDataItem(tourIdKey).getValueString();
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    tourId = ""+nf.format(u.getDataItem(tourIdKey).getValueNumeric());
                    
                }
                boolean createTour = true;
                if ((tourId == null || tourId.isEmpty()) ){
                    createTour = false;
                    u.setError("Adress Extraction Error: Tour Id is not valid; ");
                }
                
                
                if (createTour && createEndLocation && createStartLocation){
                    Set<Location> locations = t.get(tourId);
                
                    if (locations == null) {
                        locations = new HashSet<>();
                    }

                    locations.add(lStart);
                    locations.add(lEnd);

                    t.put(tourId, locations);
                }
            }
            
        };
    }

    @Override
    public BinaryOperator<Map<String, Set<Location>>> combiner() {
        return new BinaryOperator<Map<String, Set<Location>>>(){
            @Override
            public Map<String, Set<Location>> apply(Map<String, Set<Location>> t, Map<String, Set<Location>> u) {
                Set<String> keys = new HashSet<>();
                keys.addAll(t.keySet());
                keys.addAll(u.keySet());
                
                for (String key:keys){
                    Set<Location> locations = t.get(key);
                    if (locations == null){
                        locations = new HashSet();
                    }
                    
                    Set<Location> locations2 = u.get(key);
                    if (locations2 != null) {
                        locations.addAll(locations2);
                    }
                    
                    t.put(key, locations);
                }
                return t;
            }
        
        };
    }

    @Override
    public Function<Map<String, Set<Location>>, Map<String, Set<Location>>> finisher() {
        return new Function<Map<String, Set<Location>>, Map<String, Set<Location>>>(){
            @Override
            public Map<String, Set<Location>> apply(Map<String, Set<Location>> t) {
                return t;
            }
            
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        Set<Characteristics> characteristics = new HashSet<>();
        characteristics.add(Characteristics.UNORDERED);
        characteristics.add(Characteristics.IDENTITY_FINISH);
        
        return characteristics;
    }

    
    
}
