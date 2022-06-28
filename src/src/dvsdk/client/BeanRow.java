package dvsdk.client;

import dvsdk.db.BeanParam;

/**
 *
 * @author develop-dvs
 */
public class BeanRow extends BeanParam implements Comparable<BeanRow> {
    private String alias;
    private String params;
    private int position;
    private boolean test;
    private int typeClass=0;

    public BeanRow(String name, String value) {
        super(name, value);
    }

    public BeanRow(String name, String value, boolean test) {
        super(name, value);
        this.test = test;
    }
    
    public BeanRow(String name, String value, String alias) {
        super(name, value);
        this.alias = alias;
    }
    
    public BeanRow(String name, String value, String alias, boolean test) {
        super(name, value);
        this.test = test;
        this.alias = alias;
    }

    public BeanRow(String name, String value, String alias, String params, boolean test) {
        super(name, value);
        this.alias = alias;
        this.params = params;
        this.test = test;
    }
    
    public BeanRow(String name, String value, String alias, String params, boolean test, int typeClass) {
        super(name, value);
        this.alias = alias;
        this.params = params;
        this.test = test;
        this.typeClass = typeClass;
    }
    
    public BeanRow(String name, String value, int position ) {
        super(name, value);
        this.position = position;
    }
    
    public BeanRow(String name, String value, String alias, String params, int position, boolean test) {
        super(name, value);
        this.alias = alias;
        this.params = params;
        this.position = position;
        this.test = test;
    }
    
    
    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(int typeClass) {
        this.typeClass = typeClass;
    }
    
    @Override
    public int compareTo(BeanRow o) {
        return this.position-o.getPosition();
    }
}