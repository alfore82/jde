/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.countries;

import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.services.CountryService;
import com.thinkstep.jde.persistence.services.DataRowService;
import java.net.URL;
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

/**
 * FXML Controller class
 *
 * @author forell
 */
public class CountryEditorController implements Initializable {

    @FXML
    private AnchorPane ap;
    @FXML
    private TableView<Country> tblCountries;
    @FXML
    private TableColumn<Country, String> colCountryCode;
    @FXML
    private TableColumn<Country, String> colCountryCodeISO;
    @FXML
    private TableColumn<Country, String> colName;
    
    private ObservableList<Country> countries = FXCollections.observableArrayList();
    private CountryService cs = CountryService.getINSTANCE();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countries.addAll(cs.findAllCountries());
        initializeTable();
        
    }
    
    private void initializeTable(){
        tblCountries.setItems(countries);
        tblCountries.setEditable(true);
        colCountryCode.setCellValueFactory(new Callback<CellDataFeatures<Country, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<Country, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getCountryCode());
                return s;
            }
        
        });
        colCountryCode.setCellFactory(TextFieldTableCell.forTableColumn());
        colCountryCode.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Country, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Country, String> t){
                Country c = ((Country) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                c.setCountryCode(t.getNewValue());
                cs.addOrEditCountry(c);
            }
        });
        
        colCountryCodeISO.setCellValueFactory(new Callback<CellDataFeatures<Country, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<Country, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getCountryCodeISO());
                return s;
            }
        
        });
        colCountryCodeISO.setCellFactory(TextFieldTableCell.forTableColumn());
        colCountryCodeISO.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Country, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Country, String> t){
                Country c = ((Country) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                c.setCountryCodeISO(t.getNewValue());
                cs.addOrEditCountry(c);
            }
        });
        
        colName.setCellValueFactory(new Callback<CellDataFeatures<Country, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<Country, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getName());
                return s;
            }
        
        });
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Country, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Country, String> t){
                Country c = ((Country) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                c.setName(t.getNewValue());
                cs.addOrEditCountry(c);
            }
        });
    }

    @FXML
    private void addCountry(ActionEvent event) {
        Country c = new Country();
        c.setCountryCode("new");
        cs.addOrEditCountry(c);
        countries.add(c);
    }

    @FXML
    private void deleteCountry(ActionEvent event) {
        Country c = tblCountries.getSelectionModel().getSelectedItem();
        cs.deleteCountry(c);
        countries.remove(c);
    }
    
    
    
}
