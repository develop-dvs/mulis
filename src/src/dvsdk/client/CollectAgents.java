package dvsdk.client;

import dvsdk.db.BeanUser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author develop-dvs
 */
public class CollectAgents {

    private String ida;
    private String name;
    private String email;
    private String site;
    private String info;
    private List<BeanUser> agents = new ArrayList<BeanUser>();

    public CollectAgents() {
    }
/*
    public CollectAgents(String ida, String name, List<BeanUser> agents) {
        this.ida = ida;
        this.name = name;
        this.agents = agents;
    }*/

    public CollectAgents(String ida, String name, String email, String site, String info, List<BeanUser> agents) {
        this.ida = ida;
        this.name = name;
        this.email = email;
        this.site = site;
        this.info = info;
        this.agents = agents;
    }
    

    public List<BeanUser> getAgents() {
        return agents;
    }

    public void setAgents(List<BeanUser> agents) {
        this.agents = agents;
    }

    public String getIda() {
        return ida;
    }

    public void setIda(String ida) {
        this.ida = ida;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
    
}