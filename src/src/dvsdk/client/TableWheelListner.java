package dvsdk.client;

import dvsdk.GlobalConfig;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author develop-dvs
 */
public class TableWheelListner extends MouseAdapter implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        // TODO: REFACTORING
        int inc = e.getWheelRotation() < 0 ? 1 : -1;
        if (e.isAltDown() || e.isAltGraphDown()) {
            Object incom = e.getComponent();
            if (incom instanceof MulisTable) {
                MulisTable table = GlobalConfig.getCore().getCurrentTable();
                table.setRowHeight(table.getRowHeight() + inc);
            }
        }
    }
    
}