package dvsdk.client;

/**
 *
 * @author develop-dvs
 */
public class BeanAddress implements Comparable<BeanAddress> {
    private int pos;
    private String data;

    public BeanAddress(int pos, String data) {
        this.pos = pos;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
    
    @Override
    public int compareTo(BeanAddress o) {
        return this.pos-o.getPos();
    }
    
}
