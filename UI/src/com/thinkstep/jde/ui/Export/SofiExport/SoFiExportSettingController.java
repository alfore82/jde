/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Export.SofiExport;

import com.thinkstep.jde.persistence.entities.preferences.SoFiConnectionSetting;
import com.thinkstep.jde.persistence.services.SoFiConnectionSettingService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class SoFiExportSettingController implements Initializable {

    @FXML
    private TextField tfUrl;
    @FXML
    private TextField tfConnectionKey;
    @FXML
    private TextField tfConnectionSecret;
    
    private SoFiConnectionSettingService conService = SoFiConnectionSettingService.getINSTANCE();
    private SoFiConnectionSetting setting;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setting = conService.findSoFiConnectionSetting();
        if (setting == null){
            setting = new SoFiConnectionSetting();
        }
        
        tfUrl.setText(setting.getUrl());
        tfConnectionKey.setText(setting.getConnectionKey());
        tfConnectionSecret.setText(setting.getConnectionSecret());
    }    

    @FXML
    private void save(ActionEvent event) {
        setting.setUrl(tfUrl.getText());
        setting.setConnectionKey(tfConnectionKey.getText());
        setting.setConnectionSecret(tfConnectionSecret.getText());
        conService.addOrEditSoFiConnectionSetting(setting);
        Stage stage = (Stage) this.tfUrl.getScene().getWindow();
        stage.close();
        
    }
    
}
