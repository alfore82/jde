/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionProcess;
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
public class EmissionProcessService {
    private static final EmissionProcessService INSTANCE = new EmissionProcessService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private EmissionProcessService(){
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
    
    public synchronized void refresh(){
        listeners.forEach(listener->listener.refresh());
    }
    
    public static EmissionProcessService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteEmissionProcess(EmissionProcess c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        EmissionProcess cdel = em.find(EmissionProcess.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditEmissionProcess(EmissionProcess c){
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
    
    public synchronized List<EmissionProcess> findAllEmissionProcesses(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<EmissionProcess> result = em.createQuery("SELECT c FROM EmissionProcess c order by c.name asc").getResultList();
        em.close();
        return result;
    }
    
    public synchronized EmissionProcess findAllEmissionProcess(long id){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        EmissionProcess result = em.find(EmissionProcess.class, id);
        em.close();
        return result;
    }
    
}
