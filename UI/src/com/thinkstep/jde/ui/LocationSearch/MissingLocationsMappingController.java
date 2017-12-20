/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.LocationSearch;

import com.thinkstep.jde.persistence.entities.Location;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.openide.util.Exceptions;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class MissingLocationsMappingController implements Initializable {

    @FXML
    private TableView<Location> tableLocations;
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
    private AnchorPane apLocationWindow;
    ObservableList<Location> data = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            tableLocations.setItems(data);
            tableLocations.setEditable(false);
            tableLocations.setRowFactory(tv -> {
            TableRow<Location> row = new TableRow<>();
            row.setOnDragOver(event ->{
                if(!row.isEmpty()){
                    Dragboard db = event.getDragboard();
                    
                    if (db.getString().contains("EUROINDEX:")){
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                }
                event.consume();
            });
            row.setOnDragDropped(event ->{
                if(!row.isEmpty()){
                    
                    Dragboard db = event.getDragboard();
                    if (db.getString().contains("EUROINDEX:")){
                        Location l2 = row.getItem();
                        int index = Integer.parseInt(db.getString().replace("EUROINDEX:", ""));
                        l2.setIndexEurope(index);
                    }
                    tableLocations.refresh();
                    event.setDropCompleted(true);
                    event.consume();
                }
            
            });
            return row;
        
        });
            
            
            
            colCountry.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Location, String>,ObservableValue<String>>(){
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Location, String> p) {
                    SimpleStringProperty s = new SimpleStringProperty();
                    s.set(p.getValue().getCountry().getCountryCode());
                    return s;
                }
                
            });
            colCountry.setCellFactory(TextFieldTableCell.forTableColumn());
            colZip.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Location, String>,ObservableValue<String>>(){
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Location, String> p) {
                    SimpleStringProperty s = new SimpleStringProperty();
                    s.set(p.getValue().getZipCode());
                    return s;
                }
                
            });
            colZip.setCellFactory(TextFieldTableCell.forTableColumn());
            colCity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Location, String>,ObservableValue<String>>(){
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Location, String> p) {
                    SimpleStringProperty s = new SimpleStringProperty();
                    s.set(p.getValue().getCity1());
                    return s;
                }
                
            });
            colCity.setCellFactory(TextFieldTableCell.forTableColumn());
            colCity2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Location, String>,ObservableValue<String>>(){
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Location, String> p) {
                    SimpleStringProperty s = new SimpleStringProperty();
                    s.set(p.getValue().getCity2());
                    return s;
                }
                
            });
            colCity2.setCellFactory(TextFieldTableCell.forTableColumn());
            colIndexEurope.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Location, String>,ObservableValue<String>>(){
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Location, String> p) {
                    SimpleStringProperty s = new SimpleStringProperty();
                    s.set(""+p.getValue().getIndexEurope());
                    return s;
                }
                
            });
            colIndexEurope.setCellFactory(TextFieldTableCell.forTableColumn());
            
            URL location = getClass().getResource("LocationSearch.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            apLocationWindow.getChildren().add(root);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }    

    @FXML
    private void actionClose(ActionEvent event) {
        Stage stage = (Stage) this.tableLocations.getScene().getWindow();
        stage.close();
        
    }

    public void setMissingLocations(Set<Location> locationsNotFound) {
        this.data.clear();
        this.data.addAll(locationsNotFound);
        tableLocations.refresh();
    }
    
}
