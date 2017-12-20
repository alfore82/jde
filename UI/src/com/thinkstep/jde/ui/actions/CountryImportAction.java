/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.actions;

import com.thinkstep.jde.imports.filereaders.TabStopFileReader;
import com.thinkstep.jde.imports.filereaders.XlsFileReader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "com.thinkstep.jde.ui.actions.CountryImportAction"
)
@ActionRegistration(
        displayName = "#CTL_CountryImportAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1000),
    @ActionReference(path = "Shortcuts", name = "DS-I")
})
@Messages("CTL_CountryImportAction=Import country file")
public final class CountryImportAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                try {
                    Stage stage = new Stage();
                    URL location = getClass().getResource("ImportCountryView.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                    Parent root = (Parent) fxmlLoader.load(location.openStream());
                    ImportCountryViewController documentDialogController = (ImportCountryViewController) fxmlLoader.getController();
                    documentDialogController.setTilteForFileDialog("Open Lsp Data-File");
                    documentDialogController.addExtension(new ExtensionFilter("Excel-File", "*.xls", "*.xlsx", "*.xlsm"), new XlsFileReader());
                    documentDialogController.addExtension(new ExtensionFilter("Tab-Stop separated text file", "*.txt"), new TabStopFileReader());
                    Scene scene = new Scene(root);
                    stage.setTitle("Import country data");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        });
    }
}
