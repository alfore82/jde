/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.LogisticServiceProvider;

import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.services.LogisticServiceProviderService;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.openide.util.Exceptions;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class LogisticServiceProviderEditorController implements Initializable {

    @FXML
    private AnchorPane ap;
    @FXML
    private TableView<LogisticServiceProvider> tblLsps;
    @FXML
    private TableColumn<LogisticServiceProvider, String> colLspName;
    @FXML
    private TableColumn<LogisticServiceProvider, String> colLSoFiSiteId;
    
    private ObservableList<LogisticServiceProvider> lsps = FXCollections.observableArrayList();
    private LogisticServiceProviderService lspservice = LogisticServiceProviderService.getINSTANCE();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lsps.addAll(lspservice.findAllLsps());
        initializeTable();
        
    }    
    
    private void initializeTable(){
        tblLsps.setItems(lsps);
        tblLsps.setEditable(true);
        colLspName.setCellValueFactory(new Callback<CellDataFeatures<LogisticServiceProvider, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<LogisticServiceProvider, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getName());
                return s;
            }
        
        });
        colLspName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLspName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LogisticServiceProvider, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LogisticServiceProvider, String> t){
                LogisticServiceProvider c = ((LogisticServiceProvider) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                c.setName(t.getNewValue());
                lspservice.addOrEditLsp(c);
            }
        });
        colLSoFiSiteId.setCellValueFactory(new Callback<CellDataFeatures<LogisticServiceProvider, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<LogisticServiceProvider, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getSofiSiteId()));
                return s;
            }
        
        });
        colLSoFiSiteId.setCellFactory(TextFieldTableCell.forTableColumn());
        colLSoFiSiteId.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LogisticServiceProvider, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LogisticServiceProvider, String> t){
                LogisticServiceProvider c = ((LogisticServiceProvider) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                NumberFormat nf = NumberFormat.getInstance();
                try {
                    c.setSofiSiteId(nf.parse(t.getNewValue()).intValue());
                } catch (ParseException ex) {
                    c.setSofiSiteId(0);
                }
                lspservice.addOrEditLsp(c);
            }
        });
        
        
        
    }

    @FXML
    private void addLsp(ActionEvent event) {
        LogisticServiceProvider l = new LogisticServiceProvider();
        l.setName("new");
        lspservice.addOrEditLsp(l);
        lsps.add(l);
    }

    @FXML
    private void deleteLsp(ActionEvent event) {
        LogisticServiceProvider l = tblLsps.getSelectionModel().getSelectedItem();
        lspservice.deleteLsp(l);
        lsps.remove(l);
    }
    
}
