package dvsdk.rest;

import javax.swing.JDialog;

/**
 *
 * @author develop-dvs
 */
public interface IWorker extends IWorkerMsg {
    public void print(String str);
    public void println(String str);
    public void cls();
    public void reset();
    public void complite(boolean close);
    public void error(String err);
    
    public void inc_work();    
    public void max_work(int max);
    public void reset_work();
    
    public void inc_group();    
    public void max_group(int max);
    public void reset_group();
    
    public void inc_global();    
    public void max_global(int max);
    public void reset_global();
    
    public boolean isRepair();
    public boolean isReplace();
    public boolean isUpdate();
    public boolean isDownload();
    public JDialog getRootPaneDialog();
}