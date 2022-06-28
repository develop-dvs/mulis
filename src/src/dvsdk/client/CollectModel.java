package dvsdk.client;

import java.util.List;

/**
 *
 * @author develop-dvs
 */
public class CollectModel {

    private String name;
    private String alias="";
    private String desc="";
    private String id="";
    private String type="";
    private List<BeanModel> collect;

    public CollectModel() {
    }

    public CollectModel(String name, String alias, String desc, List<BeanModel> collect, String type) {
        this.name = name;
        this.alias = alias;
        this.desc = desc;
        this.collect = collect;
        this.type = type;
    }
    
    public CollectModel(String id, String name, String alias, String desc, List<BeanModel> collect, String type) {
        this.id=id;
        this.name = name;
        this.alias = alias;
        this.desc = desc;
        this.collect = collect;
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
    public int getTypeR0() {
        return Integer.parseInt(type)%10;
    }
    
    public int getTypeR1() {
        return Integer.parseInt(type)/10%10;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public List<BeanModel> getCollect() {
        return collect;
    }

    public void setCollect(List<BeanModel> collect) {
        this.collect = collect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
