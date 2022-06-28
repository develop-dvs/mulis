package dvsdk.form;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 *
 * @author develop-dvs
 */
public class MouseAdapterDivider extends MouseAdapter {

    private JSplitPane splitPane;
    private JScrollPane scrollPane;
    private int photoSize;

    public MouseAdapterDivider(JSplitPane splitPane, JScrollPane scrollPane, int photoSize) {
        this.splitPane = splitPane;
        this.scrollPane=scrollPane;
        this.photoSize = photoSize;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {   // reset divider location on double-click
            //double prop = photoSize/(Math.abs(photoSize-scrollPane.getWidth()));
            splitPane.setDividerLocation(0);
        }
    }
}
