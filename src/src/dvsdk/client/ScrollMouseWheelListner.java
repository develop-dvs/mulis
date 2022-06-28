package dvsdk.client;

import dvsdk.GlobalConfig;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 *
 * @author develop-dvs
 */
public class ScrollMouseWheelListner extends MouseAdapter implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //
        // TODO: REFACTORING
        int inc = e.getWheelRotation() < 0 ? 1 : -1;
        if (e.isAltDown() || e.isAltGraphDown()) {
            Object incom = e.getComponent();
            if (incom instanceof JScrollPane) {
                JViewport viewport = ((JScrollPane) incom).getViewport();
                for (int i = 0; i < viewport.getAccessibleContext().getAccessibleChildrenCount(); i++) {
                    Object obj = viewport.getAccessibleContext().getAccessibleChild(i);
                    if (obj instanceof MulisTable) {
                        MulisTable table = GlobalConfig.getCore().getCurrentTable();
                        table.setRowHeight(table.getRowHeight() + inc);
                        return;
                        //break;
                    }
                }
            }

        }
        super.mouseWheelMoved(e);
    }
}