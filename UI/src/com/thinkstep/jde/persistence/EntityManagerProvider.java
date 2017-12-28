/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.openide.util.NbPreferences;

/**
 *
 * @author forell
 */
public class EntityManagerProvider {
    private static EntityManagerProvider emfp = null;
    private EntityManagerFactory  emf;
    private Preferences pref = NbPreferences.forModule(NbPreferences.class);
    
    protected EntityManagerProvider(){
        newEmf();
        pref.addPreferenceChangeListener(new PreferenceChangeListener(){
            @Override
            public void preferenceChange(PreferenceChangeEvent evt) {
                if (evt.getKey().equals("dbpath")){
                    newEmf();
                }
            }
        
        });
        
    }
    
    private void newEmf(){
        String path = pref.get("connectionUrl", System.getProperty("user.home")+"\\jde.db");
        Properties p = new Properties();
        p.put("javax.persistence.jdbc.url", "jdbc:sqlite:"+path);
        p.put("foreign_keys", "true");
        if (emf != null){
            if (emf.isOpen()) emf.close();    
        }
        emf = Persistence.createEntityManagerFactory("PersistenceJDE", p);
        //emf = Persistence.createEntityManagerFactory("PersistenceJDE");
        pref.putBoolean("dbrefresh", false);
        pref.putBoolean("dbrefresh", true);
    }
    
    public static EntityManagerProvider getInstance(){
        if (emfp == null){
            emfp = new EntityManagerProvider();
        }
        return emfp;
    }
    
    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
    
    public void doVaccum(){
        Connection conn = null;
        try {
            // db parameters
            String path = pref.get("connectionUrl", System.getProperty("user.home")+"\\jde.db");
            // create a connection to the database
            conn = DriverManager.getConnection("jdbc:sqlite:"+path);
            
            System.out.println("Connection to SQLite has been established.");
            conn.prepareStatement("VACUUM").execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
}
