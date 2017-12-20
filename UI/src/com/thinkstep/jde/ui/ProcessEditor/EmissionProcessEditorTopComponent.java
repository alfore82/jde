/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.ProcessEditor;

import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionProcess;
import com.thinkstep.jde.ui.Services.EmissionProcessEditorService;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.thinkstep.jde.ui.imports.genericimporter//EmissionProcessEditor//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "EmissionProcessEditorTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@Messages({
    "CTL_EmissionProcessEditorTopComponent=EmissionProcessEditor Window",
    "HINT_EmissionProcessEditorTopComponent=This is a EmissionProcessEditor window"
})
public final class EmissionProcessEditorTopComponent extends TopComponent {

    private EmissionProcess emissionProcess;
    private static JFXPanel fxPanel;
    private ProcessEditorController processController;
    
    public EmissionProcessEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_EmissionProcessEditorTopComponent());
        setToolTipText(Bundle.HINT_EmissionProcessEditorTopComponent());
        setLayout(new BorderLayout());
        init(null);
        this.emissionProcess = null;
    }
    
    public EmissionProcessEditorTopComponent(EmissionProcess e) {
        this.emissionProcess = e;
        initComponents();
        setName(Bundle.CTL_EmissionProcessEditorTopComponent());
        setToolTipText(Bundle.HINT_EmissionProcessEditorTopComponent());
        setLayout(new BorderLayout());
        init(e);
    }
    
    private void init(EmissionProcess e){
        fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                createScene(e);
            }
        });
    }
    
    private void createScene(EmissionProcess e){
        
        try {
            
            URL location = getClass().getResource("ProcessEditor.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            fxPanel.setScene(new Scene(root));
            processController = (ProcessEditorController) fxmlLoader.getController();
            processController.setProcess(e);
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
    public EmissionProcess getEmissionProcess(){
        return emissionProcess;
    }
    
    public boolean canClose(){
        EmissionProcessEditorService epes = EmissionProcessEditorService.getInstance();
        epes.removeComponent(this);
        return true;
        
    }
}