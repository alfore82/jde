/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.imports.filereaders;

import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.rawdata.DataItem;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.DataKeyService;
import com.thinkstep.jde.persistence.services.DataRowService;
import com.thinkstep.jde.persistence.services.LogisticServiceProviderService;
import com.thinkstep.jde.ui.actions.LspSelectorController;
import com.thinkstep.jde.ui.views.progress.ProgressBarView;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *
 * @author forell
 */
public class TabStopFileReader implements FileReader{

    @Override
    public void readFile(File file, int headerLine, int firstDataLine, String worksheetName, LogisticServiceProvider lsp) {
        ProgressBarView pbv = new ProgressBarView("Importing File: "+ file.getAbsolutePath(), "Importing Lsp data");
        pbv.show();
            
        new Thread(){
            public void run(){
                try {
                    clearData(lsp);
                    int numRows = countLines(file);
                    String headerRow = getHeaderLine(file, headerLine);
                    String headersString[] = headerRow.split("\t");
                    
                    
                    int arrayLength = headersString.length;
                    List<DataKey> headers = new ArrayList<>();
                    DataKeyService dkservice = DataKeyService.getINSTANCE();
                    for (int i = 0; i<arrayLength; i++){
                        
                        DataKey dataKey = dkservice.findDataKeysByLspAndName(lsp, headersString[i]);
                        if (dataKey == null){
                            dataKey = new DataKey();
                            dataKey.setKey(headersString[i]);
                            dataKey.setLsp(lsp);
                        }
                        
                        headers.add(dataKey);
                    }
                    
                    dkservice.addOrEditLspDataKey(headers);
                    DataRowService drService = DataRowService.getINSTANCE();

                    // new Reader and skip ahead to content
                    BufferedReader br = null;
                    br = new BufferedReader(new java.io.FileReader(file));
                    for (int i = 0; i < firstDataLine-1; i++) {
                        br.readLine();
                    }
                    
                    AtomicInteger row = new AtomicInteger();
                    row.set(0);
                    List<DataRow> drs = new ArrayList<>();
                    String line;
                    while ((line = br.readLine())!= null){
                        String currentLine[] = line.split("\t");
                        Platform.runLater(()->pbv.setProgress((double)row.get()/numRows));
                        row.set(row.get()+1);
                        DataRow dr = new DataRow();
                        dr.setLsp(lsp);
                        drs.add(dr);
                        List<DataItem> ds = new ArrayList<>();
                        for (int i = 0; i<arrayLength; i++){
                            String cell;
                            try {
                                cell = currentLine[i].trim();
                            }catch (ArrayIndexOutOfBoundsException ex){
                                cell = "";
                            }
                            double value;
                            DataItem di;
                            
                            try {
                                Date d = DateExtractor.extractDate(cell);
                                if (d!=null){
                                    di = new DataItem(headers.get(i),""+d);
                                } else {
                                    NumberFormat nf = NumberFormat.getInstance();
                                    Number n;
                                    n = nf.parse(cell);
                                    value = n.doubleValue();
                                    di = new DataItem(headers.get(i),value);
                                }
                            } catch (ParseException ex) {
                                di = new DataItem(headers.get(i),cell);
                            }
                            ds.add(di);
                        }
                        dr.setDataItems(ds);
                        if (drs.size() > 100000){
                            drService.addOrEditDataRows(drs);
                            drs.clear();
                        }
                            
                    }
                drService.addOrEditDataRows(drs);
                drs.clear();
                br.close();
                Platform.runLater(()->pbv.close());
                } catch (FileNotFoundException ex) {
                    Alert al = new Alert(AlertType.ERROR);
                    al.setTitle("File not found");
                    al.setContentText("The file " + file.getAbsolutePath() + " could not be found.");
                    al.showAndWait();
                    pbv.close();
                    return;
                } catch (IOException ex) {
                    Alert al = new Alert(AlertType.ERROR);
                    al.setTitle("Error Reading File");
                    al.setContentText("And Error occured while reading the file.");
                    al.showAndWait();
                    pbv.close();
                    return;
                }
            }
        }.start();
        
        
    }
    
    
    
    
    @Override
    public void readFile(File file, int headerLine, int firstDataLine, String worksheetName) {
        
        
        String headerRow;
        String headersString[];
        try {
            headerRow = getHeaderLine(file, headerLine);
            headersString = headerRow.split("\t");
        } catch (IOException ex) {
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("File not found");
            al.setContentText("The file " + file.getAbsolutePath() + " could not be found.");
            al.showAndWait();
            return;
        }
        int lspCol;
        try {
            Stage stage = new Stage();
            URL location = getClass().getResource("/com/thinkstep/jde/ui/actions/LspSelector.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            LspSelectorController ctrl = (LspSelectorController) fxmlLoader.getController();
            ctrl.setColumns(Arrays.asList(headersString));
            Scene scene = new Scene(root);
            stage.setTitle("Select Lsp column");
            stage.setScene(scene);
            stage.showAndWait();
            lspCol = ctrl.getLspColumn();
        } catch (Exception ex){
            ex.printStackTrace();
            return;
        }
        ProgressBarView pbv = new ProgressBarView("Importing File: "+ file.getAbsolutePath(), "Importing Lsp data");
        pbv.show();
        
        new Thread(){
            public void run(){
                try {
                    int numRows = countLines(file);
                    int arrayLength = headersString.length;
                    List<LogisticServiceProvider> lsps = getLsps(file, firstDataLine, lspCol);
                    clearData(lsps);
                    DataKeyService dkservice = DataKeyService.getINSTANCE();
                    DataRowService drService = DataRowService.getINSTANCE();

                    // new Reader and skip ahead to content
                    BufferedReader br = null;
                    br = new BufferedReader(new java.io.FileReader(file));
                    for (int i = 0; i < firstDataLine-1; i++) {
                        br.readLine();
                    }
                    
                    
                    
                    AtomicInteger row = new AtomicInteger();
                    row.set(0);
                    List<DataRow> drs = new ArrayList<>();
                    String line;
                    String nameLspOld = "";
                    LogisticServiceProvider lsp = null;
                    List<DataKey> headers = new ArrayList<>();
                    while ((line = br.readLine())!= null){
                        String currentLine[] = line.split("\t");
                        LogisticServiceProviderService lspService = LogisticServiceProviderService.getINSTANCE();
                        
                        if (!nameLspOld.equals(currentLine[lspCol])){
                            lsp = lspService.findLspByName(currentLine[lspCol]);
                            if (lsp == null){
                                lsp = new LogisticServiceProvider();
                                lsp.setName(currentLine[lspCol]);
                                lspService.addOrEditLsp(lsp);  
                            }
                            headers.clear();
                            for (int i = 0; i<arrayLength; i++){
                                DataKey dataKey = dkservice.findDataKeysByLspAndName(lsp, headersString[i]);
                                if (dataKey == null){
                                    dataKey = new DataKey();
                                    dataKey.setKey(headersString[i]);
                                    dataKey.setLsp(lsp);
                                }
                                headers.add(dataKey);
                            }
                            dkservice.addOrEditLspDataKey(headers);
                            nameLspOld = lsp.getName();
                        }
                        Platform.runLater(()->pbv.setProgress((double)row.get()/numRows));
                        row.set(row.get()+1);
                        DataRow dr = new DataRow();
                        dr.setLsp(lsp);
                        drs.add(dr);

                        List<DataItem> ds = new ArrayList<>();
                        for (int i = 0; i<arrayLength; i++){
                            String cell;
                            try {
                                cell = currentLine[i].trim();
                            }catch (ArrayIndexOutOfBoundsException ex){
                                cell = "";
                            }
                            double value;
                            DataItem di;
                            try {
                                Date d = DateExtractor.extractDate(cell);
                                if (d!=null){
                                    di = new DataItem(headers.get(i),""+d);
                                } else {
                                    NumberFormat nf = NumberFormat.getInstance();
                                    Number n;
                                    n = nf.parse(cell);
                                    value = n.doubleValue();
                                    di = new DataItem(headers.get(i),value);
                                }
                            } catch (ParseException ex) {
                                di = new DataItem(headers.get(i),cell);
                            }
                            ds.add(di);
                        }
                        dr.setDataItems(ds);
                        if (drs.size() > 100000){
                            drService.addOrEditDataRows(drs);
                            drs.clear();
                        }
                            
                    }
                drService.addOrEditDataRows(drs);
                drs.clear();
                br.close();
                Platform.runLater(()->pbv.close());
                } catch (FileNotFoundException ex) {
                    Alert al = new Alert(AlertType.ERROR);
                    al.setTitle("File not found");
                    al.setContentText("The file " + file.getAbsolutePath() + " could not be found.");
                    pbv.close();
                    al.showAndWait();
                    return;
                } catch (IOException ex) {
                    Alert al = new Alert(AlertType.ERROR);
                    al.setTitle("Error Reading File");
                    al.setContentText("And Error occured while reading the file.");
                    al.showAndWait();
                    pbv.close();
                    return;
                }
            }

            
        }.start();
    }

    @Override
    public String getReaderType() {
        return "txt";
    }
    
    public List<String> getWorksheets(File file){
        return null;
        
    }

    private int countLines(File file) throws FileNotFoundException, IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
    
    private String getHeaderLine(File file, int headerLine) throws FileNotFoundException, IOException {
        BufferedReader br = null;
        br = new BufferedReader(new java.io.FileReader(file));
        for (int i = 0; i < headerLine-1; i++) {
            br.readLine();
        }
        String result = br.readLine();
        br.close();
        return result;
        
    }
    
    private void clearData(List<LogisticServiceProvider> lsps){
        DataRowService drService = DataRowService.getINSTANCE();
        drService.deleteDataRowByLsps(lsps);
    }
    
    private void clearData(LogisticServiceProvider lsp){
        DataRowService drService = DataRowService.getINSTANCE();
        drService.deleteDataRowByLsp(lsp);
    }
    
    private List<LogisticServiceProvider> getLsps(File file, int firstDataLine, int lspCol) throws FileNotFoundException, IOException {
        BufferedReader br = null;
        br = new BufferedReader(new java.io.FileReader(file));
        for (int i = 0; i < firstDataLine-1; i++) {
            br.readLine();
        }
        Set<LogisticServiceProvider> lsps = new HashSet<>();
        String line;
        LogisticServiceProviderService lspService = LogisticServiceProviderService.getINSTANCE();
        String lspOld= "";
        while ((line = br.readLine())!= null){
            String currentLine[] = line.split("\t");
            if (!currentLine[lspCol].equals(lspOld)){
                LogisticServiceProvider lsp = lspService.findLspByName(currentLine[lspCol]);
                if (lsp != null){
                    lsps.add(lsp);
                }
            }
            lspOld = currentLine[lspCol];
        }
        List<LogisticServiceProvider> result = new ArrayList<>();
        result.addAll(lsps);
        br.close();
        return result;
    }
    
}
