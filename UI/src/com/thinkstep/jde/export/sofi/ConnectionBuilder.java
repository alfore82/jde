/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.export.sofi;



import com.thinkstep.jde.persistence.entities.preferences.SoFiConnectionSetting;
import com.thinkstep.jde.persistence.services.SoFiConnectionSettingService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author forell
 */
public class ConnectionBuilder {
    
    public static void prepareImportFiles(List<LineItem> data, File file){
        try {
        
        PrintWriter test = new PrintWriter(file);

        for (LineItem li:data){
            test.println(li.getLine());
        }
        test.close();

        } catch (IOException ex) {
            Logger.getLogger(ConnectionBuilder.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not posted");
        }
    }

    public static void importData(List<LineItem> data){
        try {
            SoFiConnectionSettingService sService = SoFiConnectionSettingService.getINSTANCE();
            SoFiConnectionSetting setting = sService.findSoFiConnectionSetting();
            
            
            File tmp = File.createTempFile("ssr", "csv");
            PrintWriter writer = new PrintWriter(tmp);
            for (LineItem li:data){
                    writer.println(li.getLine());
            }
            writer.close();
            preparePostrequest(tmp, setting.getConnectionKey(), setting.getConnectionSecret(),  setting.getUrl(), data.get(0).getFile()+".csv");
            tmp.delete();

        } catch (IOException ex) {
            Logger.getLogger(ConnectionBuilder.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not posted");
        }
    }    
            
            
        
    private static void preparePostrequest(File file, String username, String secret, String url, String filename) throws IOException{
        String wsse = getWsse(username, secret);
        
        HttpPost postRequest = new HttpPost(url+"?filename=abc");
        postRequest.addHeader("Accept-Charset", "UTF-8");
        postRequest.addHeader("Content-Type", "text/csv");
        postRequest.addHeader("Content-Disposition","filename=\""+filename+"\"");
        postRequest.addHeader("Accept", "application/vnd.pe-sofi.job.v1+xml");
        postRequest.addHeader("Accept-Language", "en");
        postRequest.addHeader("Authorization", "WSSE profile=\"UsernameToken\"");
        postRequest.addHeader("X-WSSE", wsse);
        postRequest.addHeader("csvFirstLineIsData", ""+2);
        
        postRequest.setEntity(new FileEntity(file));
        
        System.out.println("Sending request: " + postRequest + Arrays.deepToString(postRequest.getAllHeaders()));
        HttpClient client = HttpClientBuilder.create().build();

        HttpResponse response = client.execute(postRequest);
        System.out.println("Obtained response: " + response.getStatusLine().toString() + ". Entity: " + response.getEntity());

    }    
    
    
private static String getWsse(String username, String secret){
        byte[] randomNonce = java.security.SecureRandom.getSeed(32); // random nonce
        String randomNonceStrB64 = javax.xml.bind.DatatypeConverter.printBase64Binary(randomNonce);

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String currentDateStr = formatter.format(currentTime).toString();

        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            bao.write(randomNonce);
            bao.write(currentDateStr.getBytes());
            bao.write(secret.getBytes());

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(bao.toByteArray());

            String passwordDigest = javax.xml.bind.DatatypeConverter.printBase64Binary(md.digest());

            return String.format("UsernameToken Username=\"%s\", PasswordDigest=\"%s\", Nonce=\"%s\", Created=\"%s\"", username, passwordDigest, randomNonceStrB64, currentDateStr);
        } catch (Exception e){
            return null;
        }
    }
}