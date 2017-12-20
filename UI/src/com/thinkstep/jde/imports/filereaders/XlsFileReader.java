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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author forell
 */
public class XlsFileReader implements FileReader{

    @Override
    public void readFile(File file, int headerLine, int firstDataLine, String worksheetName, LogisticServiceProvider lsp) {
        ProgressBarView pbv = new ProgressBarView("Importing File: "+ file.getAbsolutePath(), "Importing Lsp data");
        pbv.show();
            
        new Thread(){
            public void run(){
                try {
                    clearData(lsp);
                    Workbook wb = WorkbookFactory.create(file);
                    Sheet importSheet = wb.getSheet(worksheetName);
                    
                    int numRows = importSheet.getPhysicalNumberOfRows();
                    
                    // Read headers
                    Row headerRow = importSheet.getRow(headerLine-1);
                    int arrayLength = headerRow.getLastCellNum();
                    List<DataKey> headers = new ArrayList<>();
                    DataKeyService dkservice = DataKeyService.getINSTANCE();
                    for (int i = 0; i<arrayLength; i++){
                        
                        DataKey dataKey = dkservice.findDataKeysByLspAndName(lsp, headerRow.getCell(i).getRichStringCellValue().getString());
                        if (dataKey == null){
                            dataKey = new DataKey();
                            dataKey.setKey(headerRow.getCell(i).getRichStringCellValue().getString());
                            dataKey.setLsp(lsp);
                        }
                        
                        headers.add(dataKey);
                    }
                    
                    dkservice.addOrEditLspDataKey(headers);

                    DataRowService drService = DataRowService.getINSTANCE();

                    Iterator<Row> rows = importSheet.iterator();
                    
                    AtomicInteger row = new AtomicInteger();
                    row.set(0);
                    List<DataRow> drs = new ArrayList<>();
                    while (rows.hasNext()){
                        Row currentRow = rows.next();
                        Platform.runLater(()->pbv.setProgress((double)row.get()/numRows));
                        row.set(row.get()+1);
                        if (currentRow.getRowNum()>=firstDataLine-1){
                            DataRow dr = new DataRow();
                            dr.setLsp(lsp);
                            drs.add(dr);
                            List<DataItem> ds = new ArrayList<>();
                            for (int i = 0; i<arrayLength; i++){
                                switch (currentRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getCellTypeEnum()){
                                    case BLANK:
                                        DataItem diblank = new DataItem(headers.get(i),"");
                                        ds.add(diblank);
                                        break;
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(currentRow.getCell(i))) {
                                            DataItem didate = new DataItem(headers.get(i),""+currentRow.getCell(i).getDateCellValue());
                                            ds.add(didate);
                                        } else {
                                            DataItem dinum = new DataItem(headers.get(i),currentRow.getCell(i).getNumericCellValue());
                                            ds.add(dinum);
                                        }
                                        break;
                                    case STRING:
                                        DataItem distring = new DataItem(headers.get(i),currentRow.getCell(i).getRichStringCellValue().getString());
                                        ds.add(distring);
                                        break;
                                    case FORMULA:
                                        switch(currentRow.getCell(i).getCachedFormulaResultTypeEnum()) {
                                            case NUMERIC:
                                                if (DateUtil.isCellDateFormatted(currentRow.getCell(i))) {
                                                    DataItem didateform = new DataItem(headers.get(i),""+currentRow.getCell(i).getDateCellValue());
                                                    ds.add(didateform);
                                                } else {
                                                    DataItem dinumform = new DataItem(headers.get(i),currentRow.getCell(i).getNumericCellValue());
                                                    ds.add(dinumform);
                                                }
                                                break;
                                            case STRING:
                                                DataItem distringform = new DataItem(headers.get(i),currentRow.getCell(i).getRichStringCellValue().getString());
                                                ds.add(distringform);
                                                break;
                                        }
                                        break;
                                    default:
                                        DataItem di = new DataItem(headers.get(i),""+currentRow.getCell(i).getStringCellValue());
                                        ds.add(di);
                                }

                            }
                            dr.setDataItems(ds);
                            if (drs.size() > 10000){
                                drService.addOrEditDataRows(drs);
                                drs.clear();
                            }
                            
                        }
                    }
                    drService.addOrEditDataRows(drs);
                    drs.clear();
                    Platform.runLater(()->pbv.close());
                } catch (IOException ex) {
                    pbv.close();
                    Alert al = new Alert(AlertType.ERROR);
                    al.setTitle("File not found");
                    al.setContentText("The file " + file.getAbsolutePath() + " could not be found.");
                    al.showAndWait();
                } catch (InvalidFormatException ex) {
                    pbv.close();
                    Alert al = new Alert(AlertType.ERROR);
                    al.setTitle("Invalid format");
                    al.setContentText("The file " + file.getAbsolutePath() + " does not seem to be properly formatted Excel-file.");
                    al.showAndWait();
                } catch (EncryptedDocumentException ex) {
                    pbv.close();
                    Alert al = new Alert(AlertType.ERROR);
                    al.setTitle("Document encrypted");
                    al.setContentText("Fhe file " + file.getAbsolutePath() + " is encrypted and cannot be read.");
                    al.showAndWait();
                }
            }
        }.start();
        
        
    }
    
    
    
    
    @Override
    public void readFile(File file, int headerLine, int firstDataLine, String worksheetName) {
        Workbook wb;
        try {
            wb = WorkbookFactory.create(file);
        } catch (IOException ex) {
            
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("File not found");
            al.setContentText("The file " + file.getAbsolutePath() + " could not be found.");
            al.showAndWait();
            return;
        } catch (InvalidFormatException ex) {
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Invalid format");
            al.setContentText("The file " + file.getAbsolutePath() + " does not seem to be properly formatted Excel-file.");
            al.showAndWait();
            return;
        } catch (EncryptedDocumentException ex) {
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Document encrypted");
            al.setContentText("Fhe file " + file.getAbsolutePath() + " is encrypted and cannot be read.");
            al.showAndWait();
            return;
        }
        Sheet importSheet = wb.getSheet(worksheetName);
        Row headerRow = importSheet.getRow(headerLine-1);
        int arrayLength = headerRow.getLastCellNum();
        List<String> headerString = new ArrayList<>();
        
        for (int i = 0; i<arrayLength; i++){
            headerString.add(headerRow.getCell(i).getRichStringCellValue().getString());
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
            ctrl.setColumns(headerString);
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
                List<LogisticServiceProvider> lsps = getLsps(file, importSheet, lspCol, firstDataLine);
                clearData(lsps);
                int numRows = importSheet.getPhysicalNumberOfRows();
                DataRowService drService = DataRowService.getINSTANCE();
                Iterator<Row> rows = importSheet.iterator();        
                AtomicInteger row = new AtomicInteger();
                row.set(0);
                List<DataRow> drs = new ArrayList<>();
                while (rows.hasNext()){
                    Row currentRow = rows.next();
                    Platform.runLater(()->pbv.setProgress((double)row.get()/numRows));
                    row.set(row.get()+1);
                    if (currentRow.getRowNum()>=firstDataLine-1){
                        LogisticServiceProviderService lspService = LogisticServiceProviderService.getINSTANCE();
                        LogisticServiceProvider lsp = lspService.findLspByName(currentRow.getCell(lspCol).getRichStringCellValue().getString());
                        if (lsp == null){
                            lsp = new LogisticServiceProvider();
                            lsp.setName(currentRow.getCell(lspCol).getRichStringCellValue().getString());
                            lspService.addOrEditLsp(lsp);
                        }
                        DataRow dr = new DataRow();
                        dr.setLsp(lsp);
                        drs.add(dr);
                        List<DataItem> ds = new ArrayList<>();


                        for (int i = 0; i<arrayLength; i++){
                            DataKeyService dkService = DataKeyService.getINSTANCE();
                            DataKey currentDk = dkService.findDataKeysByLspAndName(lsp, headerString.get(i));
                            if (currentDk == null){
                                currentDk = new DataKey();
                                currentDk.setLsp(lsp);
                                currentDk.setKey(headerString.get(i));
                                dkService.addOrEditDataKey(currentDk);
                            }

                            switch (currentRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getCellTypeEnum()){
                                case BLANK:
                                    DataItem diblank = new DataItem(currentDk,"");
                                    ds.add(diblank);
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(currentRow.getCell(i))) {
                                        DataItem didate = new DataItem(currentDk,""+currentRow.getCell(i).getDateCellValue());
                                        ds.add(didate);
                                    } else {
                                        DataItem dinum = new DataItem(currentDk,currentRow.getCell(i).getNumericCellValue());
                                        ds.add(dinum);
                                    }
                                    break;
                                case STRING:
                                    DataItem distring = new DataItem(currentDk,currentRow.getCell(i).getRichStringCellValue().getString());
                                    ds.add(distring);
                                    break;
                                case FORMULA:
                                    switch(currentRow.getCell(i).getCachedFormulaResultTypeEnum()) {
                                        case NUMERIC:
                                            if (DateUtil.isCellDateFormatted(currentRow.getCell(i))) {
                                                DataItem didateform = new DataItem(currentDk,""+currentRow.getCell(i).getDateCellValue());
                                                ds.add(didateform);
                                            } else {
                                                DataItem dinumform = new DataItem(currentDk,currentRow.getCell(i).getNumericCellValue());
                                                ds.add(dinumform);
                                            }
                                            break;
                                        case STRING:
                                            DataItem distringform = new DataItem(currentDk,currentRow.getCell(i).getRichStringCellValue().getString());
                                            ds.add(distringform);
                                            break;
                                    }
                                    break;
                                default:
                                    DataItem di = new DataItem(currentDk,""+currentRow.getCell(i).getStringCellValue());
                                    ds.add(di);
                            }

                        }
                        dr.setDataItems(ds);
                        if (drs.size() > 10000){
                            drService.addOrEditDataRows(drs);
                            drs.clear();
                        }
                    }
                }
                drService.addOrEditDataRows(drs);
                            drs.clear();
                Platform.runLater(()->pbv.close());
                
            }
        }.start();
    }

    @Override
    public String getReaderType() {
        return "xls";
    }
    
    public List<String> getWorksheets(File file){
        try {
            List<String> worksheets = new ArrayList<>();
            
            Workbook wb = WorkbookFactory.create(file);
            Iterator<Sheet> sheets = wb.iterator();
            while (sheets.hasNext()){
                Sheet currentSheet = sheets.next();
                worksheets.add(currentSheet.getSheetName());
            }
            wb.close();
            return worksheets;
        } catch (IOException ex) {
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("File not found");
            al.setContentText("The file " + file.getAbsolutePath() + " could not be found.");
            al.showAndWait();
            return null;
        } catch (InvalidFormatException ex) {
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Invalid format");
            al.setContentText("The file " + file.getAbsolutePath() + " does not seem to be properly formatted Excel-file.");
            al.showAndWait();
            return null;
        } catch (EncryptedDocumentException ex) {
            Alert al = new Alert(AlertType.ERROR);
            al.setTitle("Document encrypted");
            al.setContentText("Fhe file " + file.getAbsolutePath() + " is encrypted and cannot be read.");
            al.showAndWait();
            return null;
        }
        
    }
    
    private void clearData(List<LogisticServiceProvider> lsps){
        DataRowService drService = DataRowService.getINSTANCE();
        drService.deleteDataRowByLsps(lsps);
    }
    
    private void clearData(LogisticServiceProvider lsp){
        DataRowService drService = DataRowService.getINSTANCE();
        drService.deleteDataRowByLsp(lsp);
    }

    private List<LogisticServiceProvider> getLsps(File file, Sheet importSheet, int lspCol, int firstDataLine) {
        Set<LogisticServiceProvider> lsps = new HashSet<>();
        String line;
        LogisticServiceProviderService lspService = LogisticServiceProviderService.getINSTANCE();
        
        Iterator<Row> rows = importSheet.iterator();        
        while (rows.hasNext()){
            Row currentRow = rows.next();
            if (currentRow.getRowNum()>=firstDataLine-1){
                
                LogisticServiceProvider lsp = lspService.findLspByName(currentRow.getCell(lspCol).getRichStringCellValue().getString());
                if (lsp != null){
                    lsps.add(lsp);
                }
            }
        }
        List<LogisticServiceProvider> result = new ArrayList<>();
        result.addAll(lsps);
        return result;
    }
    
}
