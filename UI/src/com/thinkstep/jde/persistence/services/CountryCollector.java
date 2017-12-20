/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.Location;
import java.util.HashSet;
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
public class CountryCollector implements Collector<Location, Set<Country>, Set<Country>>{

    @Override
    public Supplier<Set<Country>> supplier() {
        return new Supplier<Set<Country>>(){
            @Override
            public Set<Country> get() {
                Set<Country> countries = new HashSet<>();
                return countries;
            }
        };
    }

    @Override
    public BiConsumer<Set<Country>, Location> accumulator() {
        return new BiConsumer<Set<Country>, Location>(){
            @Override
            public void accept(Set<Country> t, Location u) {
                t.add(u.getCountry());
            }
            
        };
    }

    @Override
    public BinaryOperator<Set<Country>> combiner() {
        return new BinaryOperator<Set<Country>>(){
            @Override
            public Set<Country> apply(Set<Country> t, Set<Country> u) {
                t.addAll(u);
                return t;
            }
            
        };
    }

    @Override
    public Function<Set<Country>, Set<Country>> finisher() {
        return new Function<Set<Country>, Set<Country>>(){
            @Override
            public Set<Country> apply(Set<Country> t) {
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
