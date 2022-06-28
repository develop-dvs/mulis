package dvsdk.client;

import dvsdk.Logger;
import dvsdk.util.Util;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author develop-dvs
 */
public class MulisTableCellCheckBoxRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JCheckBox rendererComponent = new JCheckBox();
        try {
            if (column == 0) {
                rendererComponent.setSelected(Util.checkExistFavoriteKey(table.getName(), table.getValueAt(row, 0)));
                if (rendererComponent.isSelected()) {
                    rendererComponent.setIcon(new ImageIcon(getClass().getResource("/dvsdk/resources/bullet_star_16.png")));
                    //rendererComponent.setSelectedIcon(new ImageIcon(getClass().getResource("/dvsdk/resources/bullet_star.png")));
                }
            } else {
                boolean marked = (value != null) ? (value.toString().equals("0") ? false : true) : false;
                if (marked) {
                    rendererComponent.setSelected(true);
                }
            }

            int status = ((MulisTable) table).getCurrentStatus(row);
            Util.colorizedSell(rendererComponent, row, status, isSelected);
            rendererComponent.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception ex) {
            Logger.put(ex);
        }
        //rendererComponent.add
        return rendererComponent;
    }
}