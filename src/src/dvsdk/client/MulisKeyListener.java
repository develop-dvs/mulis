package dvsdk.client;

import dvsdk.GlobalConfig;
import dvsdk.util.Util;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author develop-dvs
 */
public class MulisKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {

            Object incom = e.getComponent();
            if (incom instanceof MulisTable) {
                Object value = ((MulisTable) incom).getValueAt(((MulisTable) incom).getSelectedRow(), 0);
                Util.addRemoveFavoriteKey(((MulisTable) incom).getName(), value.toString());
                //IFrameCore core = ((MulisTable) incom).getFrameCore();
                GlobalConfig.getCore().refreshCurrentRow();
            }
        }
        super.keyPressed(e);
    }
}
