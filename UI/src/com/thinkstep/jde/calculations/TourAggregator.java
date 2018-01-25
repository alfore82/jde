/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *
 * @author aforell
 */
public class TourAggregator implements Collector<TourData, List<TourData>, List<TourData>> {

    @Override
    public Supplier<List<TourData>> supplier() {
        return new Supplier<List<TourData>>(){
            @Override
            public List<TourData> get() {
                return new ArrayList<TourData>();
            }
            
        };
    }

    @Override
    public BiConsumer<List<TourData>, TourData> accumulator() {
        return new BiConsumer<List<TourData>, TourData>(){
            @Override
            public void accept(List<TourData> t, TourData u) {
                
                TourData t2 = new TourData();
                t2.setDistance(u.getDistance());
                t2.setMass(u.getMass());
                t2.setNumVehicle(u.getNumVehicle());
                t2.setNumpallets(u.getNumpallets());
                t2.setVehicleType(u.getVehicleType());
                for (TourData t1:t){
                    if (t1.getDistance() <= u.getDistance()){
                        t1.setMass(t1.getMass()+u.getMass());
                        t1.setNumpallets(t1.getNumpallets()+u.getNumpallets());
                    } else {
                        t2.setMass(t1.getMass()+t2.getMass());
                        t2.setNumpallets(t1.getNumpallets()+t2.getNumpallets());
                    }
                } 
                boolean found = false;
                for (TourData t1:t){
                    if (t2.getDistance() == t1.getDistance()) found = true;
                }
                if (!found){
                    t.add(t2);
                }
                
                
            }
            
        };
    }

    @Override
    public BinaryOperator<List<TourData>> combiner() {
        return new BinaryOperator<List<TourData>>(){
            @Override
            public List<TourData> apply(List<TourData> t, List<TourData> u) {
                List<TourData> finalData = new ArrayList<>();
                for (TourData t1:t){
                    accumulate(finalData, t1);
                }
                for (TourData u1:u){
                    accumulate(finalData,u1);
                }
                return finalData;
            }
            
            private void accumulate(List<TourData> dataList, TourData dataPoint){
                TourData t2 = new TourData();
                t2.setDistance(dataPoint.getDistance());
                t2.setMass(dataPoint.getMass());
                t2.setNumVehicle(dataPoint.getNumVehicle());
                t2.setNumpallets(dataPoint.getNumpallets());
                t2.setVehicleType(dataPoint.getVehicleType());
                for (TourData t1:dataList){
                    if (t1.getDistance() <= dataPoint.getDistance()){
                        t1.setMass(t1.getMass()+dataPoint.getMass());
                        t1.setNumpallets(t1.getNumpallets()+dataPoint.getNumpallets());
                    } else {
                        t2.setMass(t1.getMass()+t2.getMass());
                        t2.setNumpallets(t1.getNumpallets()+t2.getNumpallets());
                    }
                } 
                boolean found = false;
                for (TourData t1:dataList){
                    if (t2.getDistance() == t1.getDistance()) found = true;
                }
                if (!found){
                    dataList.add(t2);
                }
            }
        
        };
    }

    @Override
    public Function<List<TourData>, List<TourData>> finisher() {
        return new Function<List<TourData>, List<TourData>>(){
            @Override
            public List<TourData> apply(List<TourData> t) {
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
