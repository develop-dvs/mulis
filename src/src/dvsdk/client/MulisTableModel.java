package dvsdk.client;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author develop-dvs
 */
public class MulisTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int rowIndex, int mColIndex) {
        return false;
    }
    
}
