/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.preferences.SoFiConnectionSetting;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import org.openide.util.NbPreferences;

/**
 *
 * @author forell
 */
public class SoFiConnectionSettingService {
    private static final SoFiConnectionSettingService INSTANCE = new SoFiConnectionSettingService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private SoFiConnectionSettingService(){
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
        listeners.forEach(listener->listener.refresh());
    }
    
    public static SoFiConnectionSettingService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteSoFiConnectionSetting(SoFiConnectionSetting c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        SoFiConnectionSetting cdel = em.find(SoFiConnectionSetting.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditSoFiConnectionSetting(SoFiConnectionSetting c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        if (c.getId()== 0){
            em.persist(c);
        } else {
            em.merge(c);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized SoFiConnectionSetting findSoFiConnectionSetting(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<SoFiConnectionSetting> results = em.createQuery("SELECT c FROM SoFiConnectionSetting c ").getResultList();
        SoFiConnectionSetting result = null;
        if (results.size() > 0){
            result = results.get(0);
        }
        em.close();
        return result;
    }
    
}
