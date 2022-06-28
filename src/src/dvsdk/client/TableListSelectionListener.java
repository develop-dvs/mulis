package dvsdk.client;

import dvsdk.GlobalConfig;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author develop-dvs
 */
public class TableListSelectionListener implements ListSelectionListener {
    //private IFrameCore frameCore = null;
    private MulisTable mulisTable = null;

    public TableListSelectionListener(MulisTable table) {
        this.mulisTable=table;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        GlobalConfig.getCore().fillFastFields(mulisTable.getCurrentId(),mulisTable.getName());
    }

}