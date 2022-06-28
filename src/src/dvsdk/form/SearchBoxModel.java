package dvsdk.form;

/**
 *
 * @author develop-dvs
 */
import dvsdk.client.MulisComboBoxModel;
import dvsdk.util.Util;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class SearchBoxModel extends AbstractListModel
        implements ComboBoxModel, KeyListener, ItemListener {

    ArrayList<String> db = new ArrayList<String>();
    ArrayList<String> data = new ArrayList<String>();
    String selection;
    JComboBox cb;
    MulisComboBoxModel mcbm;
    ComboBoxEditor cbe;
    int currPos = 0;

    public SearchBoxModel(JComboBox jcb) {
        jcb.setEditable(true);
        String tmp_str=jcb.getSelectedItem().toString().trim();
        this.cb = jcb;
        this.mcbm = (MulisComboBoxModel) jcb.getModel();
        this.cbe = jcb.getEditor();
//here we add the key listener to the text field that the combobox is wrapped around
        this.cbe.getEditorComponent().addKeyListener(this);
        this.db.addAll(Util.toList(cb));
        //this.currPos = jcb.getSelectedIndex();
        
        //cb.setSelectedItem(tmp_str);
        updateModel(tmp_str);
        postInit(tmp_str);
//set up our "database" of items - in practice you will usuallu have a proper db.
    }

    private void postInit(String str) {
        setSelectedItem(str); 
    }

    private void updateModel(String in) {
        data.clear();
        in=in.toLowerCase();
//lets find any items which start with the string the user typed, and add it to the popup list
//here you would usually get your items from a database, or some other storage...
        for (String s : db) {
            if (s.toLowerCase().startsWith(in)) {
                data.add(s);
            }
        }

        super.fireContentsChanged(this, 0, data.size());

//this is a hack to get around redraw problems when changing the list length of the displayed popups
        cb.hidePopup();
        cb.showPopup();
        if (data.size() != 0) {
            cb.setSelectedIndex(0);
        }
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public Object getElementAt(int index) {
        try {
            return data.get( (index)==-1?0:index ); 
        } catch (Exception ex) {
            // TODO: Feedback
            return null;
        }
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selection = (String) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String str = cbe.getItem().toString();
        JTextField jtf = (JTextField) cbe.getEditorComponent();
        currPos = jtf.getCaretPosition();

        if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED) {
            if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                cbe.setItem(str);
                jtf.setCaretPosition(currPos);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            cb.setSelectedIndex(cb.getSelectedIndex());
        } else {
            updateModel(cb.getEditor().getItem().toString());
            cbe.setItem(str);
            jtf.setCaretPosition(currPos);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        cbe.setItem(e.getItem().toString());
        cb.setSelectedItem(e.getItem());
        mcbm.setSelectedItem(e.getItem().toString());
    }

    public String getCurrentElement(int index) {
        return mcbm.getCurrentElement(mcbm.getIndexOf(getElementAt(index).toString()));
        //return mcbm.getCurrentElement();
    }
}