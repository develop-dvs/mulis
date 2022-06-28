package dvsdk.client;

import dvsdk.GlobalConfig;
import dvsdk.db.BeanUser;
import dvsdk.util.UtilSec;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author develop-dvs
 */
public class MulisComboBoxModelUsers extends DefaultComboBoxModel {
    private List<BeanUser> elements = new ArrayList<BeanUser>();

    public MulisComboBoxModelUsers(List<BeanUser> elements) {
        this.elements=elements;
        removeAllElements();
        boolean isName=false;
        for (BeanUser binUser : elements) {
            addElement(binUser);
            if (GlobalConfig.AGENT_NAME.equals(binUser.getName())) isName=true;
        }
        if (!GlobalConfig.AGENT_NAME.isEmpty() && isName) 
            setSelectedItem(GlobalConfig.AGENT_NAME);
    }
    
    public String getCurrentElement(int index) {
        if (index==-1) return "";
        return elements.get(index).getIdr();
    }
    
    public BeanUser getCurrentUserElement(int index) {
        if (index==-1) return null;
        return elements.get(index);
    }
    
    /**
     * Проверка пароля
     * @param index
     * @param pwd
     * @return 
     */
    public boolean test(int index, String pwd) {
        if (pwd==null) return false;
        return elements.get(index).getPwdMD5().equals(UtilSec.getMD5(pwd));
    }

    @Override
    public final void addElement(Object anObject) {
        if (anObject instanceof BeanUser)
            super.addElement(((BeanUser)anObject).getName()); //+" | "+((BinUser)anObject).getContact()
        else super.addElement(anObject);
    }

}