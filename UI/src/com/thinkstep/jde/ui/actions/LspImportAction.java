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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "ImportLsp",
        id = "com.thinkstep.jde.ui.actions.LspImportAction"
)
@ActionRegistration(
        displayName = "#CTL_LspImportAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1100),
    @ActionReference(path = "Shortcuts", name = "D-I")
})
@Messages("CTL_LspImportAction=Import Lsp data")
public final class LspImportAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                try {
                    Stage stage = new Stage();
                    URL location = getClass().getResource("ImportLspView.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                    Parent root = (Parent) fxmlLoader.load(location.openStream());
                    ImportLspViewController documentDialogController = (ImportLspViewController) fxmlLoader.getController();
                    documentDialogController.setTilteForFileDialog("Open Lsp Data-File");
                    documentDialogController.addExtension(new ExtensionFilter("Excel-File", "*.xls", "*.xlsx", "*.xlsm"), new XlsFileReader());
                    documentDialogController.addExtension(new ExtensionFilter("Tab-Stop separated text file", "*.txt"), new TabStopFileReader());
                    Scene scene = new Scene(root);
                    stage.setTitle("Import Lsp Data");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        });
    }
}
