package dvsdk.client;

import dvsdk.db.Base;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author develop-dvs
 */
public interface IFrameCore {
    public int openCurrentForm();
    public int openCurrentForm(String t_name, String t_param, String id);
    public MulisFrame getCurrentForm();
    public MulisTable openCurrentTable(boolean reinsert);
    public JFrame createCurrentFormSale();
    public JFrame createCurrentFormRent();
    public JFrame createCurrentFormOutland();
    public JFrame createCurrentFormBuy();
    public MulisTable getCurrentTable();
    public String getCurrentFilter();
    public void generateListModelCurrentTable();
    public MulisTable initCurrentTable();
    public void refreshCurrentTableFull();
    public void refreshCurrentTable();
    public void refreshCurrentTable(boolean force);
    public void refreshCurrentRow();
    public void testCore();
    public int getCurrentIntFrame();
    public void editCurrentTable();
    public void fakeDeleteCurrentItem();
    public String getSqlStatus();
    public void setDisable();
    public void setEnable();
    public IFrameCore getCore();
    public Base getBase(String name);
    public void fillFastFields(String id, String table);
    public boolean isReady();
    public void changeFontOnAllTable();
    public void changeFontViewPanel();
    public void printSelected();
    public void exit();
    public void setFullEnable(boolean en);
    public boolean checkUpdate(Component cmp);
    public void setCurrentTab(int i, int j);
    public void setTitleMainFrame(String str);
    public boolean isGlobal();
    public void setGlobal(boolean set);
    public boolean inArchive();
    public JPanel getCurrentPanel();
    public void setIconMulis(JFrame frame);
}