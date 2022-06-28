package dvsdk.client;

import dvsdk.GlobalConfig;
import dvsdk.util.Util;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;

/**
 *
 * @author develop-dvs
 */
public class TableMouseListner extends MouseAdapter {

    /**
     * Добавить/удалить из хэш-таблицы избранных записей
     * @param e MouseEvent
     * @param colnum № колонки
     */
    private void checkFavorite(MouseEvent e, int colnum) {
        Object incom = e.getComponent();
        if (incom instanceof MulisTable) {
            Point p = e.getPoint();
            int colNumber = (colnum >= 0) ? ((MulisTable) incom).columnAtPoint(p) : -1;

            if (colNumber == colnum || colNumber == -1) {
                int rowNumber = ((MulisTable) incom).rowAtPoint(p);

                Object value = ((MulisTable) incom).getValueAt(rowNumber, 0);
                //String key = ((MulisTable) incom).getName() +"|"+ value;
                Util.addRemoveFavoriteKey(((MulisTable) incom).getName(),value.toString());
                ListSelectionModel model = ((MulisTable) incom).getSelectionModel();
                model.setSelectionInterval(rowNumber, rowNumber);

                GlobalConfig.getCore().refreshCurrentRow();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
            checkFavorite(e, -1);
        } else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
            checkFavorite(e, 0);
        }
        super.mousePressed(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Открываем редактор объявления по двойному щелчку
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
            Object incom = e.getComponent();
            if (incom instanceof MulisTable) {
                Object value = ((MulisTable) incom).getValueAt(((MulisTable) incom).getSelectedRow(), 0);
                GlobalConfig.getCore().openCurrentForm(((MulisTable) incom).getName(), ((MulisTable) incom).getSqlParams(), (String) value);
            }
        }
        super.mouseClicked(e);
    }
}