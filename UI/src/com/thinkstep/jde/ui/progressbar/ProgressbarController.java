/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.progressbar;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class ProgressbarController implements Initializable, ProgressListener {

    @FXML
    private Label lblTitle;
    @FXML
    private Label tfDoing;
    @FXML
    private ProgressBar pbProgress;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void setTitle(String title) {
        Platform.runLater(()->lblTitle.setText(title));
    }

    @Override
    public void setAction(String action) {
        Platform.runLater(()->tfDoing.setText(action));
    }

    @Override
    public void setProgress(double progress) {
        Platform.runLater(()->pbProgress.setProgress(progress));
    }
    
}
