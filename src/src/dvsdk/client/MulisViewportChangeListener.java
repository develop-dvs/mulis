package dvsdk.client;

import dvsdk.GlobalConfig;
import dvsdk.Logger;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author develop-dvs
 */
public class MulisViewportChangeListener implements ChangeListener {

    MulisTable table;
    JViewport port;
    //IFrameCore core;
    private int l_first=-1;
    private int l_last=-1;

    public MulisViewportChangeListener(MulisTable table, JViewport port) {
        this.table = table;
        this.port = port;
    }
    

    @Override
    public void stateChanged(ChangeEvent e) {
        Rectangle viewRect = port.getViewRect();
        int first = table.rowAtPoint(new Point(0, viewRect.y));
        if (first == -1) {
            return; // Table is empty
        }
        int last = table.rowAtPoint(new Point(0, viewRect.y + viewRect.height - 1));
        if (last == -1) {
            last = table.getModel().getRowCount() - 1; // Handle empty space below last row
        }
        /*
        for (int i = first; i <= last; i++)
        {
        int row = sorter.convertRowIndexToModel(i); // or: row = i
        //... Do stuff with each visible row
        }*/

        if (last == table.getModel().getRowCount() - 1) {
        } //... Last row is visible
        
        first=( (first-GlobalConfig.ADD_LIMIT)<0 )?0:first-GlobalConfig.ADD_LIMIT;
        last+=GlobalConfig.ADD_LIMIT;
        //if (first>0 && last>0)
        if (this.l_first==first && this.l_last==last) return;
        this.l_first=first;
        this.l_last=last;
        table.setLimit(first,last);
        
        Logger.put("F:" + first + "L:" + last);
        if (GlobalConfig.getCore().isReady()) GlobalConfig.getCore().refreshCurrentTable();
    }
}
