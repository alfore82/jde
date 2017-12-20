/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.imports.filereaders;

import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import java.io.File;

/**
 *
 * @author forell
 */
public interface FileReader {
    
    public void readFile(File file, int headerLine, int firstDataLine, String worksheetName);
    public void readFile(File file, int headerLine, int firstDataLine, String worksheetName, LogisticServiceProvider lsp);
    
    public String getReaderType();
    
}
