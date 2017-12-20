/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Export.SofiExport;

import com.thinkstep.jde.imports.filereaders.TabStopFileReader;
import com.thinkstep.jde.imports.filereaders.XlsFileReader;
import com.thinkstep.jde.ui.actions.ImportCountryViewController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Help",
        id = "com.thinkstep.jde.ui.Export.SofiExport.SoFiExportSettingsAction"
)
@ActionRegistration(
        displayName = "#CTL_SoFiExportSettingsAction"
)
@ActionReference(path = "Menu/Help", position = 100)
@Messages("CTL_SoFiExportSettingsAction=Edit SoFi connection settings")
public final class SoFiExportSettingsAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                try {
                    Stage stage = new Stage();
                    URL location = getClass().getResource("SoFiExportSetting.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                    Parent root = (Parent) fxmlLoader.load(location.openStream());
                    Scene scene = new Scene(root);
                    stage.setTitle("SoFi connection Settings");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        });
    }
}
