/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author forell
 */
public class Location implements Serializable{
    
    private Country country = null;
    private String zipCode = "";
    private String city1 = "";
    private String city2 = "";
    private int indexEurope = 0;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }
    
    public String getOptimizedZip(){
        if (country != null && country.getCountryCode().equals("GB")){
            String zip = zipCode;
            String parts[] = zip.split(" ");
            if (parts.length > 1){
                return parts[0] + " " + parts[1].substring(0, 1);
            } else{
                return parts[0];
            }
        }
        
        if (country != null && country.getCountryCode().equals("F")){
            String zip = zipCode;
            if ((zip.length())<5){
                zip = zip + "0"+zip;
            }
            return zip.substring(0, zip.length()-2)+"0";
        }
        
         if (country != null && country.getCountryCode().equals("D")){
            String zip = zipCode;
            if ((zip.length())<5){
                return "0"+zip;
            }
            
        }
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public int getIndexEurope() {
        return indexEurope;
    }

    public void setIndexEurope(int indexEurope) {
        this.indexEurope = indexEurope;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.country);
        hash = 97 * hash + Objects.hashCode(this.zipCode);
        hash = 97 * hash + Objects.hashCode(this.city1);
        hash = 97 * hash + Objects.hashCode(this.city2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        if (!Objects.equals(this.zipCode, other.zipCode)) {
            return false;
        }
        if (!Objects.equals(this.city1, other.city1)) {
            return false;
        }
        if (!Objects.equals(this.city2, other.city2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Location{" + "country=" + country + ", zipCode=" + zipCode + ", city1=" + city1 + ", city2=" + city2 + '}';
    }
    
    
    
    
}
