package dvsdk.mail;

/**
 *
 * @author develop-dvs
 */
public class EMailBin {
    public String NAME;
    public String EMAIL;
    public String GROUP;

    public EMailBin(String name, String email, String group) {
        this.NAME=name;
        this.EMAIL=email;
        this.GROUP=group;
    }
    public EMailBin(String name, String email) {
        this(name, email, "");
    }
    public EMailBin(String email) {this("",email,"");}

}
