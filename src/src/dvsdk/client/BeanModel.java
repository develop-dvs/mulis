package dvsdk.client;

/**
 *
 * @author develop-dvs
 */
public class BeanModel {

    private String id;
    private String name;
    private String addStr;

    public BeanModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public BeanModel(String id, String name, String addStr) {
        this.id = id;
        this.name = name;
        this.addStr = addStr;
    }

    public String getAddStr() {
        return addStr;
    }

    public void setAddStr(String addStr) {
        this.addStr = addStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
