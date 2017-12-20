/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.openide.util.NbPreferences;

/**
 *
 * @author forell
 */
public class LogisticServiceProviderService {
    private static final LogisticServiceProviderService INSTANCE = new LogisticServiceProviderService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private LogisticServiceProviderService(){
        listeners = new HashSet<>();
        
        pref = NbPreferences.forModule(NbPreferences.class);
        pref.addPreferenceChangeListener((PreferenceChangeEvent evt) -> {
            if ((evt.getKey().equals("dbrefresh") && evt.getNewValue().equals("true")) ||
                    (evt.getKey().equals("importnotification") && evt.getNewValue().equals("true"))){
                refresh();
            }
        } 
        );
    }
    
    private synchronized void refresh(){
        Platform.runLater(()->listeners.forEach(listener->listener.refresh()));
    }
    
    public static LogisticServiceProviderService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteLsp(LogisticServiceProvider c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        LogisticServiceProvider cdel = em.find(LogisticServiceProvider.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditLsp(LogisticServiceProvider c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        if (c.getId()== 0){
            em.persist(c);
        } else {
            em.merge(c);
        }
        em.getTransaction().commit();
        em.close();
        refresh();
    }
    
    public synchronized List<LogisticServiceProvider> findAllLsps(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<LogisticServiceProvider> result = em.createQuery("SELECT c FROM LogisticServiceProvider c order by c.name asc").getResultList();
        em.close();
        
        return result;
    }
    
    public synchronized LogisticServiceProvider findLspByName(String name){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query q = em.createQuery("SELECT c FROM LogisticServiceProvider c WHERE c.name LIKE :name order by c.name asc");
        q.setParameter("name", name);
        List<LogisticServiceProvider> result = q.getResultList();
        em.close();
        if (result.size()>0) return result.get(0);
        else return null;
    }
    
}
