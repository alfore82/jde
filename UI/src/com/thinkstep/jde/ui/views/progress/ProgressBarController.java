/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.views.progress;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author forell
 */
public class ProgressBarController implements Initializable{

    @FXML private ProgressBar progressBar;
    @FXML private Label importedItem;
    @FXML private Label action;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void setProgress(double progress){
        if ((progress >= 0.0) && (progress <= 1.0)){
            progressBar.setProgress(progress);
        }
    }
    
    public void setImportedItemText(String text){
        importedItem.setText(text);
    }
    
    public void setAction(String action){
        this.action.setText(action);
    }
}
