/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.imports.filereaders;

import com.thinkstep.jde.persistence.entities.Location;
import com.thinkstep.jde.persistence.services.CountryService;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author forell
 */
public class OdsProvider{
    CountryService cService = CountryService.getINSTANCE();
    public List<Location> readOds() {
        List<Location> locs = new ArrayList<>();
        try {
            URL url = getClass().getResource("eu2018.ods");
            InputStreamReader reader = new InputStreamReader(url.openStream(), "Cp850");
            BufferedReader br = null;
            br = new BufferedReader(reader);
            String line;
            while (((line = br.readLine()) != null)){
                Location l = new Location();
                l.setCity1(line.substring(12, 35).trim());
                l.setCity2(line.substring(36, 59).trim());
                l.setCountry(cService.findCountryByCode(line.substring(0, 2).trim()));
                l.setIndexEurope(Integer.parseInt(line.substring(129, 138).trim()));
                l.setZipCode(line.substring(3, 11).trim());
                locs.add(l);
            }
            return locs;
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
    
}
