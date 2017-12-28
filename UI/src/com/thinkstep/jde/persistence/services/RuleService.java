/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.Rules.ParentConnector;
import com.thinkstep.jde.persistence.entities.Rules.Rule;
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
public class RuleService {
    private static final RuleService INSTANCE = new RuleService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private RuleService(){
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
    
    public static RuleService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteRule(Rule c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        Rule cdel = em.find(Rule.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditRule(Rule c){
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
    
    public synchronized void addOrEditRules(List<Rule> cs){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        for (Rule c:cs){
            if (c.getId()== 0){
                em.persist(c);
            } else {
                em.merge(c);
            }
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized List<Rule> findAllRules(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<Rule> result = em.createQuery("SELECT c FROM Rule c order").getResultList();
        em.close();
        return result;
    }
    
    public synchronized List<Rule> findRulesByLsp(LogisticServiceProvider lsp){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM Rule c where c.lsp = :lsp");
        query.setParameter("lsp", lsp);
        List<Rule> result = query.getResultList();
        em.close();
        return result;
    }
    
    public synchronized List<Rule> findRulesByLspAndMappingTypeAndParent(LogisticServiceProvider lsp, MappingType mappingType, Rule parent){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<Rule> result;
        if (parent != null){
            Query query = em.createQuery("SELECT c FROM Rule c where c.lsp = :lsp and c.mappingType = :mappingType and c.parent = :parent");
            query.setParameter("lsp", lsp);
            query.setParameter("mappingType", mappingType);
            query.setParameter("parent", parent);
            result = query.getResultList();
        } else {
            Query query = em.createQuery("SELECT c FROM Rule c where c.lsp = :lsp and c.mappingType = :mappingType and c.parent IS NULL");
            query.setParameter("lsp", lsp);
            query.setParameter("mappingType", mappingType);
            result = query.getResultList();
        }
        em.close();
        return result;
    }
    
    public synchronized List<Rule> findRulesByLspAndMappingTypeAndParent(LogisticServiceProvider lsp, MappingType mappingType, Rule parent, ParentConnector pc){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<Rule> result;
        if (parent != null){
            Query query = em.createQuery("SELECT c FROM Rule c where c.lsp = :lsp and c.mappingType = :mappingType and c.parent = :parent and c.parentConnector = :pc");
            query.setParameter("lsp", lsp);
            query.setParameter("mappingType", mappingType);
            query.setParameter("parent", parent);
            query.setParameter("pc", pc);
            result = query.getResultList();
        } else {
            Query query = em.createQuery("SELECT c FROM Rule c where c.lsp = :lsp and c.mappingType = :mappingType and c.parent IS NULL and c.parentConnector = :pc");
            query.setParameter("lsp", lsp);
            query.setParameter("mappingType", mappingType);
            query.setParameter("pc", pc);
            result = query.getResultList();
        }
        em.close();
        return result;
    }
    
    public synchronized void deleteRulesByLspAndMappingTypeAndParent(LogisticServiceProvider lsp, MappingType mappingType){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("DELETE FROM Rule c where c.lsp = :lsp and c.mappingType = :mappingType");
        query.setParameter("lsp", lsp);
        query.setParameter("mappingType", mappingType);
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized Rule findRulesMappingItemId(long id){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM Rule c where c.mappingItemId = :id");
        query.setParameter("id", id);
        List<Rule> results = query.getResultList();
        em.close();
        if (results.size()>0){
            return results.get(0);
        }
        return null;
    }
    
}
