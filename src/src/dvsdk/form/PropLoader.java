package dvsdk.form;

import java.util.List;

public class PropLoader {
    private List<String> base;
    private String file;
    private String name;

    public PropLoader() {
    }
    

    public PropLoader(String file, String name) {
        this.file = file;
        this.name = name;
    }
    public void init() {
        
    }
    
    private void loadFromXML(){
        
    }
    
    public void saveToXML(){
        
    }

    public List<String> getBase() {
        return base;
    }

    public void setBase(List<String> base) {
        this.base = base;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}