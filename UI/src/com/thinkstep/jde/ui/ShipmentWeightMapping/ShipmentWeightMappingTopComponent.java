/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.ShipmentWeightMapping;


import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.Services.DistanceMapperService;
import com.thinkstep.jde.ui.Services.ShipmentWeightMapperService;
import java.awt.BorderLayout;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.thinkstep.jde.ui.ShimpmentWeightMapping//ShimpmentWeightMapping//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ShimpmentWeightTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@Messages({
    "CTL_ShimpmentWeightTopComponent=Rule Builder window",
    "HINT_ShimpmentWeightComponent=This is a Rule Builder window"
})
public final class ShipmentWeightMappingTopComponent extends TopComponent {
    
    private static JFXPanel fxPanel;
    private LogisticServiceProvider lsp;

    public ShipmentWeightMappingTopComponent() {
        initComponents();
        setName("Shipment weight mapping");
        setToolTipText("Shipment weight mapping");
        setLayout(new BorderLayout());
        init();
        this.lsp = null;
    }
    
    public ShipmentWeightMappingTopComponent(LogisticServiceProvider lsp) {
        initComponents();
        setName("Shipment weight mapping: " + lsp.getName());
        setToolTipText("Shipment weight mapping: " + lsp.getName());
        setLayout(new BorderLayout());
        init();
        this.lsp = lsp;
    }
    
    private void init(){
        fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                createScene();
            }
        });
    }
    
    private void createScene(){
            ShipmentWeightMapper root = new ShipmentWeightMapper(lsp);

            
            fxPanel.setScene(new Scene(root));
            
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
    public LogisticServiceProvider getLsp(){
        return this.lsp;
    }
    

    public boolean canClose(){
        ShipmentWeightMapperService vtService = ShipmentWeightMapperService.getInstance();
        vtService.removeComponent(this);
        return true;
        
    }
    
}
