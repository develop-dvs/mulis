package dvsdk.client;

import dvsdk.GlobalConfig;

/**
 *
 * @author develop-dvs
 */
public class BeanColumn {
    private String name;
    private int size=GlobalConfig.DEF_COL_SIZE;
    private int min=GlobalConfig.DEF_MIN_COL_SIZE;
    private int max=GlobalConfig.DEF_MAX_COL_SIZE;
    private boolean show = true;
    private int type=0;
    private int export=0;

    public BeanColumn(String name, boolean show) {
        this.name = name;
        this.show = show;
    }
    
    public BeanColumn(String name, boolean show, int type) {
        this(name, show);
        this.type = type;
    }
    
    public BeanColumn(String name, int size) {
        this.name = name;
        this.size = size;
        this.max = size*GlobalConfig.DEF_INC_COL;
    }
        
    public BeanColumn(String name, int size, boolean show) {
        this(name, size);
        this.show = show;
    }
    
    public BeanColumn(String name, int size, boolean show, int type) {
        this(name, size, show);
        this.type = type;
    }
    
    public BeanColumn(String name, int size, int max) {
        this.name = name;
        this.size = size;
        this.max = max;
    }
    
    public BeanColumn(String name, int size, int max, boolean show) {
        this(name, size, max);
        this.show = show;
    }
    
    public BeanColumn(String name, int size, int max, boolean show, int type) {
        this(name, size, max, show);
        this.type=type;
    }
    
    public BeanColumn(String name, int size, int min, int max) {
        this.name = name;
        this.size = size;
        this.min = min;
        this.max = max;
    }
    
    public BeanColumn(String name, int size, int min, int max, boolean show) {
        this(name, size, min, max);
        this.show = show;
    }
    
    public BeanColumn(String name, int size, int min, int max, boolean show, int type) {
        this(name, size, min, max, show);
        this.type = type;
    }
    
    public BeanColumn(String name, int size, int min, int max, boolean show, int type, int export) {
        this(name, size, min, max, show, type);
        this.export = export;
    }
    
    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getExport() {
        return export;
    }

    public void setExport(int export) {
        this.export = export;
    }
    
}