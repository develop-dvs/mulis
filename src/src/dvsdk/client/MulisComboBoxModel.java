package dvsdk.client;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author develop-dvs
 */
public class MulisComboBoxModel extends DefaultComboBoxModel {
    private List<BeanModel> elements = new ArrayList<BeanModel>();

    public MulisComboBoxModel(List<BeanModel> elements) {
        this.elements=elements;
        removeAllElements();
        for (BeanModel binModel : elements) {
            addElement(binModel);
        }
    }
    
    public String getCurrentElement(int index) {
        if (index==-1) return "";
        return elements.get(index).getId();
    }

    @Override
    public final void addElement(Object anObject) {
        if (anObject instanceof BeanModel)
            //super.addElement(((BinModel)anObject).getName());
            super.addElement(((BeanModel)anObject).getName()+((!((BeanModel)anObject).getAddStr().isEmpty())?" "+((BeanModel)anObject).getAddStr():""));
        else super.addElement(anObject);
    }
}