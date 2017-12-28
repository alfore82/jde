/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.actions;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.openide.LifecycleManager;
import org.openide.util.NbPreferences;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class ChangeDatabaseController implements Initializable {

    @FXML
    private TextField tfDatabase;
    @FXML
    private Label lblWaring;
    
    Preferences pref = NbPreferences.forModule(NbPreferences.class);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblWaring.setWrapText(true);
        lblWaring.setPrefWidth(500);
        String connectionUrl = pref.get("connectionUrl", System.getProperty("user.home")+"\\jde.db");
        tfDatabase.setText(connectionUrl);
        
    }    

    @FXML
    private void open(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open database file");
        File file;
        fileChooser.getExtensionFilters().add(new ExtensionFilter("DatabaseFile","*.db"));
        file = fileChooser.showOpenDialog(new Stage());
        
        if (file != null){
            tfDatabase.setText(file.getAbsolutePath());
        }
        
    }

    @FXML
    private void save(ActionEvent event) {
        String connectionUrl = tfDatabase.getText();
        pref.put("connectionUrl", connectionUrl);
        
        LifecycleManager.getDefault().exit(); 
        Stage stage = (Stage) this.tfDatabase.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) this.tfDatabase.getScene().getWindow();
        stage.close();
        
    }
    
}
