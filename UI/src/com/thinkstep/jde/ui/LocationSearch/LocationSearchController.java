/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.LocationSearch;

import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.Location;
import com.thinkstep.jde.persistence.services.CountryService;
import com.thinkstep.jde.persistence.services.DistanceService;
import com.thinkstep.jde.ui.Converters.CountryStringConverter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class LocationSearchController implements Initializable {

    @FXML
    private TextField tfSearch;
    @FXML
    private TableColumn<Location, String> colCountry;
    @FXML
    private TableColumn<Location, String> colZip;
    @FXML
    private TableColumn<Location, String> colCity;
    @FXML
    private TableColumn<Location, String> colCity2;
    @FXML
    private TableColumn<Location, String> colIndexEurope;
    @FXML
    private ComboBox<Country> cbCountry;

    private ObservableList<Country> countries = FXCollections.observableArrayList();
    FilteredList<Location> filteredData;
    @FXML
    private TableView<Location> tableLocations;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CountryService cService = CountryService.getINSTANCE();
        countries.addAll(cService.findAllCountries());
        cbCountry.setItems(countries);
        cbCountry.setConverter(new CountryStringConverter());
        initializeTable();
        tfSearch.setOnKeyPressed(ev -> {
            filteredData.setPredicate(location -> {
                String text = tfSearch.getText().toLowerCase();
                Country c = cbCountry.getSelectionModel().getSelectedItem();
                if (c == null || text.length() < 3) return false;

                if (c.equals(location.getCountry()) && ( location.getZipCode().toLowerCase().contains(text) || location.getCity1().toLowerCase().contains(text) || location.getCity2().toLowerCase().contains(text) )){
                    return true;
                }
                return false;
            });
        
        });
    }    

    @FXML
    private void search(ActionEvent event) {
        filteredData.setPredicate(location -> {
            String text = tfSearch.getText().toLowerCase();
            Country c = cbCountry.getSelectionModel().getSelectedItem();
            if (c == null || text.length() < 3) return false;
            
            if (c.equals(location.getCountry()) && ( location.getZipCode().toLowerCase().contains(text) || location.getCity1().toLowerCase().contains(text) || location.getCity2().toLowerCase().contains(text) )){
                return true;
            }
            return false;
        });
    }

    private void initializeTable() {
        DistanceService ds = DistanceService.getINSTANCE();
        ObservableList<Location> data = FXCollections.observableArrayList();
        data.addAll(ds.getLocations());
        filteredData = new FilteredList<>(data, p -> false);
        SortedList<Location> sortedData = new SortedList<>(filteredData);
        tableLocations.setItems(sortedData);
        tableLocations.setEditable(false);
        tableLocations.setRowFactory(tv -> {
            TableRow<Location> row = new TableRow<>();
            row.setOnDragDetected(event ->{
                if(!row.isEmpty()){
                    Location ei = row.getItem();
                    Dragboard db = row.startDragAndDrop(TransferMode.COPY);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString("EUROINDEX:"+ei.getIndexEurope());
                    db.setContent(cc);
                    event.consume();
                }
            
            });
            
            
            return row;
        
        });
        colCountry.setCellValueFactory(new Callback<CellDataFeatures<Location, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<Location, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getCountry().getCountryCode());
                return s;
            }
        
        });
        colCountry.setCellFactory(TextFieldTableCell.forTableColumn());
        colZip.setCellValueFactory(new Callback<CellDataFeatures<Location, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<Location, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getZipCode());
                return s;
            }
        
        });
        colZip.setCellFactory(TextFieldTableCell.forTableColumn());
        colCity.setCellValueFactory(new Callback<CellDataFeatures<Location, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<Location, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getCity1());
                return s;
            }
        
        });
        colCity.setCellFactory(TextFieldTableCell.forTableColumn());
        colCity2.setCellValueFactory(new Callback<CellDataFeatures<Location, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<Location, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getCity2());
                return s;
            }
        
        });
        colCity2.setCellFactory(TextFieldTableCell.forTableColumn());
        colIndexEurope.setCellValueFactory(new Callback<CellDataFeatures<Location, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<Location, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(""+p.getValue().getIndexEurope());
                return s;
            }
        
        });
        colIndexEurope.setCellFactory(TextFieldTableCell.forTableColumn());
        
        
    }
    
}
