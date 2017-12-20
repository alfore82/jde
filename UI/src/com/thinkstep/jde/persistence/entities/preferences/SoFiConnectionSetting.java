/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.preferences;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author forell
 */
@Entity
public class SoFiConnectionSetting {
    @Id @GeneratedValue
    private long id;
    
    private String url = "";
    private String connectionKey = "";
    private String connectionSecret = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getConnectionKey() {
        return connectionKey;
    }

    public void setConnectionKey(String connectionKey) {
        this.connectionKey = connectionKey;
    }

    public String getConnectionSecret() {
        return connectionSecret;
    }

    public void setConnectionSecret(String connectionSecret) {
        this.connectionSecret = connectionSecret;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 59 * hash + Objects.hashCode(this.url);
        hash = 59 * hash + Objects.hashCode(this.connectionKey);
        hash = 59 * hash + Objects.hashCode(this.connectionSecret);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SoFiConnectionSetting other = (SoFiConnectionSetting) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.connectionKey, other.connectionKey)) {
            return false;
        }
        if (!Objects.equals(this.connectionSecret, other.connectionSecret)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
