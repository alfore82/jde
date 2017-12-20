/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.graphics.MappingItemGraphics;
import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.openide.util.NbPreferences;

/**
 *
 * @author forell
 */
public class MappingItemGraphicsService {
    private static final MappingItemGraphicsService INSTANCE = new MappingItemGraphicsService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private MappingItemGraphicsService(){
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
    
    public static MappingItemGraphicsService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteMappingItemGraphics(MappingItemGraphics c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        MappingItemGraphics cdel = em.find(MappingItemGraphics.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditMappingItemGraphics(MappingItemGraphics c){
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
    
    public synchronized void addOrEditLspMappingItemGraphicses(List<MappingItemGraphics> cs){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        for (MappingItemGraphics c:cs){
            if (c.getId()== 0){
                em.persist(c);
            } else {
                em.merge(c);
            }
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized List<MappingItemGraphics> findAllLspMappings(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<MappingItemGraphics> result = em.createQuery("SELECT c FROM MappingItemGraphics c").getResultList();
        em.close();
        return result;
    }
    
    public synchronized List<MappingItemGraphics> findAllLMappingItemGraphicsByLspByMappingType(LogisticServiceProvider lsp, MappingType mappingType){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM MappingItemGraphics c where c.lsp = :lsp and c.mappingType = :mappingType");
        query.setParameter("lsp", lsp);
        query.setParameter("mappingType", mappingType);
        List<MappingItemGraphics> result = query.getResultList();
        em.close();
        return result;
    }
    
    public synchronized void deleteMappingItemGraphicsByLspByMappingType(LogisticServiceProvider lsp, MappingType mappingType){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("DELETE FROM MappingItemGraphics c where c.lsp = :lsp and c.mappingType = :mappingType");
        query.setParameter("lsp", lsp);
        query.setParameter("mappingType", mappingType);
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    
}
