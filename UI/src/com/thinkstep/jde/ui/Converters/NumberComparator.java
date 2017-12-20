/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;

/**
 *
 * @author forell
 */
public class NumberComparator implements Comparator<String>{

    @Override
    public int compare(String o1, String o2) {
        try {
            NumberFormat df = NumberFormat.getInstance();
            Number d1 = df.parse(o1);
            Number d2 = df.parse(o2);
            return Double.compare(d1.doubleValue(), d2.doubleValue());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
    
}
