/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.actions;

import com.thinkstep.jde.imports.filereaders.DataImporter;
import com.thinkstep.jde.imports.filereaders.FileReader;
import com.thinkstep.jde.imports.filereaders.XlsFileReader;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.services.LogisticServiceProviderService;
import com.thinkstep.jde.presets.PathPresets;
import com.thinkstep.jde.ui.Converters.LogisticServiceProviderStringConverter;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author forell
 */
public class ImportLspViewController implements Initializable{
    
    @FXML TextField displayFileName;
    @FXML ComboBox<String> cbWorksheet;
    
    private ObservableList<String> cbItems =  FXCollections.observableArrayList();
    private final Map<ExtensionFilter, FileReader> extensions = new HashMap<>();
    private ExtensionFilter ef = null;
    private String title;
    
    File file;
    
    
    private ObservableList<LogisticServiceProvider> lsps = FXCollections.observableArrayList();
    @FXML
    private ComboBox<LogisticServiceProvider> cbLsp;
    @FXML
    private TextField tfDataRow;
    @FXML
    private TextField tfFirstRow;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbWorksheet.setItems(cbItems);
        LogisticServiceProviderService lspService = LogisticServiceProviderService.getINSTANCE();
        this.lsps.addAll(lspService.findAllLsps());
        cbLsp.setItems(lsps);
        cbLsp.setConverter(new LogisticServiceProviderStringConverter());
        tfFirstRow.setText("1");
        tfDataRow.setText("2");
    }
    
    @FXML protected void handleImportAction(ActionEvent ae){
        List<String[]> lines = null;
        int headerRow;
        int dataRow;
        try{
            headerRow = Integer.parseInt(tfFirstRow.getText());
            if (!(headerRow > 0)){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex){
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Number for header row invalid");
            al.showAndWait();
            tfFirstRow.setText("");
            return;
        }
        try{
            dataRow = Integer.parseInt(tfDataRow.getText());
            if (!(dataRow > 0)){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex){
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Number for data row invalid");
            al.showAndWait();
            tfFirstRow.setText("");
            return;
        }
        String worksheet = this.cbWorksheet.getSelectionModel().getSelectedItem();
        if (extensions.get(ef).getReaderType().equals("xls")){
            if (worksheet == null){
                Alert al = new Alert(AlertType.ERROR);
                al.setTitle("No worksheet selected");
                al.showAndWait();
                return;
            }
        }
        LogisticServiceProvider lsp = this.cbLsp.getSelectionModel().getSelectedItem();
        if (lsp == null){
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("No Lsp selected");
            al.showAndWait();
            return;
        }
        
        if (file != null){
            FileReader fr = extensions.get(ef);
            fr.readFile(file, headerRow, dataRow, worksheet, lsp);
        }
        closeWindow();
        
    }
    
    @FXML protected void handleCancelAction(ActionEvent ae){
        closeWindow();
    }
    
    @FXML protected void handleOpenFileAction(ActionEvent ae){
        
        FileChooser fileChooser = new FileChooser();
        if (PathPresets.getPath() != null){
            fileChooser.setInitialDirectory(PathPresets.getPath());
        }
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(extensions.keySet());
        file = fileChooser.showOpenDialog(new Stage());
        
        if (file != null){
            PathPresets.setPath(file);
            ef = fileChooser.getSelectedExtensionFilter();
            if (extensions.get(ef).getReaderType().equals("xls")){
                cbWorksheet.setDisable(false);
                cbItems.clear();
                XlsFileReader reader = (XlsFileReader) extensions.get(ef);
                cbItems.addAll(reader.getWorksheets(file));
            } else {
                cbWorksheet.setDisable(true);
                cbItems.clear();
            }
            displayFileName.setText(file.getName());
        }
        
        
    }
    
    
    private void closeWindow(){
        Stage stage = (Stage) this.displayFileName.getScene().getWindow();
        stage.close();
    }
    
    
    public void setTilteForFileDialog(String title){
        this.title=title;
    }
    
    public void addExtension(ExtensionFilter ef, FileReader fr){
        extensions.put(ef, fr);
    }
}
