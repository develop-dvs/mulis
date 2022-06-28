package dvsdk.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BeanBase {
    protected String id;
    protected String ida;
    protected String idr;
    protected String date_add;
    protected String date_mod;
    protected String state;
    protected List<BeanParam> params = new ArrayList<BeanParam>();
    
    @Deprecated
    public BeanBase(){
    }
    public BeanBase(String id){
        this.id=id;
    }
    
    public BeanBase(String ida, String idr, String date_add, String date_mod, String state) {
        this.ida = ida;
        this.idr = idr;
        this.date_add = date_add;
        this.date_mod = date_mod;
        this.state = state;
    }
    
    public BeanBase(String id, String ida, String idr, String date_add, String date_mod, String state) {
        this.id = id;
        this.ida = ida;
        this.idr = idr;
        this.date_add = date_add;
        this.date_mod = date_mod;
        this.state = state;
    }
    
    public BeanBase(String id, String ida, String idr, String date_add, String date_mod, String state, List<BeanParam> params) {
        this.id = id;
        this.ida = ida;
        this.idr = idr;
        this.date_add = date_add;
        this.date_mod = date_mod;
        this.state = state;
        this.params=params;
    }
   
    public void setStdValue(String name, String val) {
        if (name.equals("id")) this.id=val;
        else if (name.equals("ida")) this.ida=val;
        else if (name.equals("idr")) this.idr=val;
        else if (name.equals("date_add")) this.date_add=val;
        else if (name.equals("date_mod")) this.date_mod=val;
        else if (name.equals("state")) this.state=val;
        // TODO Установка остальных параметров
        // else getParams().add(new BeanParam(name, val));
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }

    public String getDate_mod() {
        return date_mod;
    }

    public void setDate_mod(String date_mod) {
        this.date_mod = date_mod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIda() {
        return ida;
    }

    public void setIda(String ida) {
        this.ida = ida;
    }

    public String getIdr() {
        return idr;
    }

    public void setIdr(String idr) {
        this.idr = idr;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public List<BeanParam> getParams() {
        return params;
    }

    public void setParams(List<BeanParam> params) {
        this.params = params;
    }
    
    /**
     * Сформировать ключи CREATE SQL
     * @return "key1 INTEGER,key2 TEXT,key3 INTEGER,key4 INTEGER"
     */
    public String getParamsCreateSQL() {
        String ret=" ";
        if (params.isEmpty()) return ret;
        for (Iterator<BeanParam> it = params.iterator(); it.hasNext();) {
            BeanParam binParam = it.next();
            ret+=binParam.getName()+((binParam.getType()==null)?"":" "+binParam.getType())+((it.hasNext())?",":" ");
        }
        return ret;
    }
    
    /**
     * Сформировать ключи SQL
     * @return "key1,key2,key3,key4"
     */
    public String getParamsSQL() {
        String ret=" ";
        if (params.isEmpty()) return ret;
        for (Iterator<BeanParam> it = params.iterator(); it.hasNext();) {
            BeanParam binParam = it.next();
            //if (binParam.getName().equals("id")) continue; // TODO: wtf
            ret+=binParam.getName()+((it.hasNext())?",":" ");
        }
        return ret;
    }
    
    /**
     * Сформировать маску для вставки SQL
     * @return "?,?,?,?"
     */
    public String getParamsInsertSQL() {
        String ret=" ";
        if (params.isEmpty()) return ret;
        for (Iterator<BeanParam> it = params.iterator(); it.hasNext();) {
            BeanParam binParam = it.next();
            ret+="?"+((it.hasNext())?",":" ");
        }
        return ret;
    }

    /**
     * Сформировать значения SQL
     * @return "val1,val2,val3,val4"
     */
    public String getSQLInsertVal(){
        String ret=" ";
        for (Iterator<BeanParam> it = params.iterator(); it.hasNext();) {
            BeanParam binParam = it.next();
            ret+=binParam.getValue()+((it.hasNext())?",":" ");
        }
        return ret;
    }
    
    /**
     * Сформировать ключ=значение SQL
     * @return "key1=val1,key2=val2,key3=val3"
     */
    public String getSQLUpdateVal(){
        String ret=" ";
        for (Iterator<BeanParam> it = params.iterator(); it.hasNext();) {
            BeanParam binParam = it.next();
            ret+=binParam.getName()+"="+binParam.getValue()+((it.hasNext())?",":" ");
        }
        return ret;
    }
    /**
     * Сформировать ключ=? SQL
     * @return "key1=?,key2=?,key3=?"
     */
    public String getSQLUpdate(){
        String ret=" ";
        for (Iterator<BeanParam> it = params.iterator(); it.hasNext();) {
            BeanParam binParam = it.next();
            ret+=binParam.getName()+"=?"+((it.hasNext())?",":" ");
        }
        return ret;
    }
}