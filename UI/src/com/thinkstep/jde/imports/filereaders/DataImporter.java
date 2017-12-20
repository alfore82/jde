/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.imports.filereaders;

import java.io.File;

/**
 *
 * @author forell
 */
public interface DataImporter {
    
    public void importData(File file, FileReader reader, String worksheetname);
    
}
