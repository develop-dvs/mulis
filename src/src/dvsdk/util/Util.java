package dvsdk.util;

import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.client.BeanColumn;
import dvsdk.client.BeanModel;
import dvsdk.client.BeanResult;
import dvsdk.client.BeanRow;
import dvsdk.client.CollectModel;
import dvsdk.client.MulisComboBoxModel;
import dvsdk.client.MulisComboBoxModelUsers;
import dvsdk.client.MulisFrame;
import dvsdk.client.MulisKeyListener;
import dvsdk.client.MulisTable;
import dvsdk.client.MulisTableCellCheckBoxRenderer;
import dvsdk.client.MulisTableModel;
import dvsdk.client.TableListSelectionListener;
import dvsdk.client.TableMouseListner;
import dvsdk.db.BeanBase;
import dvsdk.db.BeanParam;
import dvsdk.db.BeanUser;
import dvsdk.form.SearchBoxModel;
import dvsdkweb.db.BeanTransParam;
import dvsdkweb.usr.BeanTransImages;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author develop-dvs
 */
public class Util {

    public static final int F_SALE_KVARTIR = 0;
    public static final int F_SALE_KOMNTAT = 1;
    public static final int F_SALE_KOMMERS = 2;
    public static final int F_SALE_DOM = 3;
    public static final int F_SALE_UCH = 4;
    public static final int F_SALE_GARAZG = 5;
    
    public static final int F_RENT_KVARTIR = 0;
    public static final int F_RENT_KOMNTAT = 1;
    public static final int F_RENT_KOMMERS = 2;
    public static final int F_RENT_DOM = 3;
    public static final int F_RENT_UCH = 4;
    public static final int F_RENT_GARAZG = 5;
        
    public static final int F_OUTLAND_OBJECTS = 0;
    public static final int F_BUY_OBJECTS = 0;
    
    public static final int F_SALE = 10;
    public static final int F_RENT = 20;
    public static final int F_OUTLAND = 30;
    public static final int F_BUY = 40;
    public static final int[] F_SALE_ARR = {F_SALE_KVARTIR, F_SALE_KOMNTAT, F_SALE_KOMMERS, F_SALE_DOM, F_SALE_UCH, F_SALE_GARAZG};
    public static final int[] F_RENT_ARR = {F_RENT_KVARTIR, F_RENT_KOMNTAT, F_RENT_KOMMERS, F_RENT_DOM, F_RENT_UCH, F_RENT_GARAZG};
    public static final int[] F_OUTLAND_ARR = {F_OUTLAND_OBJECTS};
    public static final int[] F_BUY_ARR = {F_BUY_OBJECTS};
    
    public static final int[] F_SALE_RENT_ARR = {F_SALE, F_RENT, F_OUTLAND};

    /**
     * Проверка на пустое значение строки
     *
     * @param jtc
     * @return true/false
     */
    public static boolean isClean(String jtc) {
        try {
            String s = jtc.trim();
            if (s == null) {
                return false;
            }
            return s.equals("");
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }

    public static boolean notNull(String str0) {
        return notNull(str0, "", "");
    }

    public static boolean notNull(String str0, String str1) {
        return notNull(str0, str1, "");
    }

    public static boolean notNull(String str0, String str1, String str2) {
        return (str0 != null && str1 != null && str2 != null);
    }

    /**
     * Проверка на пустое значение JTextComponent
     *
     * @param jtc
     * @return true/false
     */
    public static boolean isClean(JTextComponent jtc) {
        return isClean(jtc.getText());
    }

    public static void colorizedSell(Component c, int row, int status, boolean selected) {
        boolean isArch = GlobalConfig.getCore().inArchive();
        Color clrB = c.getBackground();

        c.setForeground(GlobalConfig.fontTableColor);

        if (row % 2 == 0 && !selected) {
            clrB = GlobalConfig.evenTableColor;
        } else {
            clrB = GlobalConfig.oddTableColor;
        }

        if (status >= 1 && GlobalConfig.STATUS_COLORIZED) {
            if (row % 2 != 0 || !GlobalConfig.IS_HALF_COLOR) {
                clrB = GlobalConfig.getStatusColor(status);
            } else {
                clrB = Util.getSaturColor(GlobalConfig.getStatusColor(status), GlobalConfig.coef_color_s);
            }
        }

        if (isArch) {
            clrB = clrB.darker();
        }

        if (selected) {
            clrB = GlobalConfig.selectedTableColor;
        }

        c.setBackground(clrB);
    }

    /**
     * Получить тукущую JPanel из общего jTabbedPane
     *
     * @param masterPane - Основной Pane
     * @param pane1 - Pane продажи
     * @param pane2 - Pane аренды
     * @param pane3 - Pane покупки
     * @return JPanel
     */
    public static JPanel getCurrentPanel(JTabbedPane masterPane, JTabbedPane pane1, JTabbedPane pane2, JTabbedPane pane3, JTabbedPane pane4) {
        switch ((masterPane.getSelectedIndex() + 1) * 10) {
            case Util.F_SALE:
                return (JPanel) pane1.getSelectedComponent();
            case Util.F_RENT:
                return (JPanel) pane2.getSelectedComponent();
            case Util.F_OUTLAND:
                return (JPanel) pane3.getSelectedComponent();
            case Util.F_BUY:
                return (JPanel) pane4.getSelectedComponent();
            default:
                break;
        }
        return null;
    }

    /*
     public static MulisTable getCurrentTable(JTabbedPane masterPane, JTabbedPane pane1, JTabbedPane pane2) {
     JPanel panel = getCurrentPanel(masterPane, pane1, pane2);
     return (MulisTable) findTable(panel);
     }*/
    public static Object findTable(Object incom) {
        Component[] components = getComponentsFromObject(incom);
        for (Object object : components) {

            if (object instanceof JPanel) {
                findTable((JPanel) object);
            } else if (object instanceof JDialog) {
                findTable((JDialog) object);
            } else if (object instanceof JFrame) {
                findTable((JFrame) object);
            } else if (object instanceof JScrollPane) {
                findTable((JScrollPane) object);
            } else if (object instanceof JViewport) {
                findTable((JViewport) object);
            } else if (object instanceof JTable) {
                return object;
            }
            /*if (object instanceof JTabbedPane) {
             findTable((JTabbedPane)object);
             }*/
        }
        return null;
    }

    /**
     * Заполнить модель таблицы
     *
     * @param table
     * @param nameCols
     */
    public static void fillTableModel(MulisTable table, List<BeanColumn> nameCols) {
        try {
            MulisTableModel dtm = new MulisTableModel();
            for (Iterator<BeanColumn> it = nameCols.iterator(); it.hasNext();) {
                BeanColumn bc = it.next();
                if (bc.getName().equals("idr")) {
                    dtm.addColumn(Lang.STD_FIELD_IDR);
                } else {
                    dtm.addColumn(bc.getName());
                }
            }
            //table.setFrameCore(frameCore);
            table.setModel(dtm);
            table.setAutoCreateColumnsFromModel(false);
            table.setAutoCreateRowSorter(true);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.addMouseListener(new TableMouseListner());
            table.addKeyListener(new MulisKeyListener());
            table.getSelectionModel().addListSelectionListener(new TableListSelectionListener(table));
            table.setFont(GlobalConfig.tableFont);
            table.getTableHeader().setFont(GlobalConfig.tableFont);
            //table.setDefaultRenderer(String.class, new MulisTableCellRenderer());
            //dtm.setDataVector(null, null);

            for (int i = 0; i < dtm.getColumnCount(); i++) {
                boolean show = nameCols.get(i).isShow();
                if (nameCols.get(i).getName().equals(GlobalConfig.COLORIZED_ALIAS)) {
                    //table.getColumnModel().getColumn(i).setCellRenderer(new MulisTableCellStringRenderer());
                    show = false;
                }
                table.getColumnModel().getColumn(i).setPreferredWidth((nameCols.get(i).isShow()) ? nameCols.get(i).getSize() : 0);
                table.getColumnModel().getColumn(i).setMinWidth((show) ? nameCols.get(i).getMin() : 0);
                table.getColumnModel().getColumn(i).setMaxWidth((show) ? nameCols.get(i).getMax() : 0);
                if (nameCols.get(i).getType() == 1 || i == 0) { // Замена ID на чекбоксы
                    table.getColumnModel().getColumn(i).setCellRenderer(new MulisTableCellCheckBoxRenderer());
                }

            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    /**
     * Получить данные элементов на форме
     *
     * @param frame
     * @return
     */
    public static BeanResult getBinResult(JFrame frame) {
        BeanResult binResult = new BeanResult(Util.scanJFrame(frame));
        // HARD MOD: frame.setId();
        binResult.setId(frame.getAccessibleContext().getAccessibleDescription()); // TODO: JFrame -> MulisFrame
        return binResult;
    }

    /**
     * Добавить таблицу на фрейм
     *
     * @param panel
     * @param nameCols
     * @param tableName
     */
    public static MulisTable addTableToPanel(JPanel panel, List<BeanColumn> nameCols, String tableName) {
        try {
            MulisTable jTable = new MulisTable();
            jTable.setName(tableName);
            //jTable.setFrameCore(frameCore);
            Util.fillTableModel(jTable, nameCols);
            JScrollPane jScrollPane = new JScrollPane();
            jScrollPane.add(jTable);
            jScrollPane.setViewportView(jTable);
            panel.add(jScrollPane);
            return jTable;
        } catch (Exception ex) {
            Logger.put(ex);
            return null;
        }
    }

    public static Object getRoot(Object incom) {
        //if (incom instanceof IFrameCore) return incom;
        Component component = ((Component) incom).getParent();
        if (component.getParent() == null) {
            return component;
        } else {
            return getRoot(component);
        }
    }

    /**
     * Проверить объект, если в конце имени есть '*'
     *
     * @param object
     * @return
     */
    public static boolean testJObject(Object object) {

        if (((Component) object).getName() == null) {
            return true;
        }
        if (((Component) object).getName().isEmpty()) {
            return true;
        }

        if (object instanceof JFormattedTextField) {
            if (((JFormattedTextField) object).getName().endsWith("*")) {
                if (((JFormattedTextField) object).getValue() == null || ((JFormattedTextField) object).getValue().equals("")) {
                    return false;
                }
            }
        } else if (object instanceof JTextField) {
            if (((JTextField) object).getName().endsWith("*")) {
                if (((JTextField) object).getText().trim().isEmpty()) {
                    return false;
                }
            }
        } else if (object instanceof JSpinner) {
            if (((JSpinner) object).getName().endsWith("*")) {
                if (((JSpinner) object).getValue().toString().equals("0")) {
                    return false;
                }
            }
        } else if (object instanceof JComboBox) {
            if (((JComboBox) object).getName().endsWith("*")) {
                if (((JComboBox) object).getSelectedItem().toString().trim().isEmpty()) {
                    return false;
                }
            }
        } else if (object instanceof JTextArea) {
            if (((JTextArea) object).getName().endsWith("*")) {
                if (((JTextArea) object).getText().trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Сделать активным в JComboBox элемент по индексу
     *
     * @param comboBox
     * @param value
     */
    public static void setActiveComboBox(JComboBox comboBox, String value) {
        try {
            //if (value.isEmpty()) return;
            int cur = Integer.parseInt(value);
            if (comboBox.getModel() instanceof MulisComboBoxModel) {
                for (int i = 0; i < comboBox.getModel().getSize(); i++) {
                    if (((MulisComboBoxModel) comboBox.getModel()).getCurrentElement(i).toString().equals(value)) {
                        comboBox.setSelectedIndex(i);
                        return;
                    }
                }
            } else if (comboBox.getModel() instanceof MulisComboBoxModelUsers) {
                // TODO: -MulisComboBoxModelUsers
                for (int i = 0; i < comboBox.getModel().getSize(); i++) {
                    if (((MulisComboBoxModelUsers) comboBox.getModel()).getCurrentElement(i).toString().equals(value)) {
                        comboBox.setSelectedIndex(i);
                        return;
                    }
                }
            } else {
                if (cur > comboBox.getModel().getSize()) {
                    comboBox.setSelectedIndex(0);
                } else {
                    comboBox.setSelectedIndex(cur);
                }
                return;
            }
            comboBox.setSelectedIndex(0);
        } catch (Exception ex) {
            comboBox.setSelectedIndex(0);
        }
    }

    /**
     * Сделать активным/добавить в JComboBox элемент по строке
     *
     * @param comboBox
     * @param value
     */
    @Deprecated
    public static void setActiveComboBoxFromString(JComboBox comboBox, String value) {
        for (int i = 0; i < comboBox.getModel().getSize(); i++) {
            if (comboBox.getModel().getElementAt(i).toString().equals(value)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.addItem(value);
        comboBox.setSelectedIndex(comboBox.getModel().getSize() - 1);
    }

    /**
     * Получить содержимое объекта
     *
     * @param incom
     * @return
     */
    public static Component[] getComponentsFromObject(Object incom) {
        return (incom instanceof JPanel) ? ((JPanel) incom).getComponents()
                : (incom instanceof JTabbedPane) ? ((JTabbedPane) incom).getComponents()
                : (incom instanceof JScrollPane) ? ((JScrollPane) incom).getViewport().getComponents()
                : (incom instanceof JViewport) ? ((JViewport) incom).getComponents()
                : (incom instanceof JFrame) ? ((JFrame) incom).getContentPane().getComponents() : null;
    }

    /**
     * Сканировать и заполнить элементы на фреймах
     *
     * @param incom
     * @param name
     * @param value
     */
    public static void fillJFrameObject(Object object, String name, String value) {
        if (((Component) object).getName() != null) {
            if (((Component) object).getName().isEmpty()) {
                return;
            }
            try {
                if (object instanceof JFormattedTextField) {
                    if ((clearFullStr(((JFormattedTextField) object).getName()).equals(name))) {
                        //((JFormattedTextField) object).setValue((value.equals(""))?"0":value);
                        ((JFormattedTextField) object).setText((value.isEmpty()) ? "0" : value);
                        ((JFormattedTextField) object).commitEdit();
                    }
                } else if (object instanceof JTextField) {
                    if ((clearFullStr(((JTextField) object).getName()).equals(name))) {
                        ((JTextField) object).setText(value);
                    }
                } else if (object instanceof JSpinner) {
                    if ((clearFullStr(((JSpinner) object).getName()).equals(name))) {
                        ((JSpinner) object).setValue(Integer.parseInt(value));
                    }
                } else if (object instanceof JComboBox) {
                    if ((clearFullStr(((JComboBox) object).getName()).equals(name))) {
                        setActiveComboBox((JComboBox) object, value);
                    }
                } else if (object instanceof JCheckBox) {
                    if ((clearFullStr(((JCheckBox) object).getName()).equals(name))) {
                        if (value.equals("0")) {
                            ((JCheckBox) object).setSelected(false);
                        } else {
                            ((JCheckBox) object).setSelected(true);
                        }
                    }
                } else if (object instanceof JTextArea) {
                    if ((clearFullStr(((JTextArea) object).getName()).equals(name))) {
                        ((JTextArea) object).setText(value);
                    }
                } else if (object instanceof JTable) {
                    if ((clearFullStr(((JTable) object).getName()).equals(name))) {
                        stringToTable((JTable) object, value);
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Заполняем фрейм данными
     *
     * @param frame
     * @param binParams
     */
    public static void fillJFrame(MulisFrame frame, List<BeanParam> binParams) {
        List<Component> components = new ArrayList<Component>();
        components = grabJFrame(frame);

        for (Iterator<Component> it = components.iterator(); it.hasNext();) {
            Component component = it.next();
            for (Iterator<BeanParam> it1 = binParams.iterator(); it1.hasNext();) {
                BeanParam binParam = it1.next();
                fillJFrameObject(component, binParam.getName(), binParam.getValue());
            }
        }
    }

    /**
     * Заполняем фрейм данными
     *
     * @param frame
     * @param binBase
     */
    public static JFrame fillJFrame(MulisFrame frame, BeanBase binBase) {
        frame.setID(binBase.getId());
        frame.setTitle(frame.getTitle() + " [" + binBase.getId() + ":" + binBase.getDate_add() + "/" + binBase.getDate_mod() + "]");
        frame.setMod(true);
        frame.setBinBase(binBase);
        fillJFrame(frame, binBase.getParams());
        return frame;
    }

    /**
     * Сканировать фрейм рекурсивно
     *
     * @param incom
     * @return
     */
    public static List<Component> grabJFrame(Object incom) {
        List<Component> ret = new ArrayList<Component>();
        Component[] components = getComponentsFromObject(incom);
        for (Object object : components) {
            /*if (object instanceof JDialog) {
             ret.addAll(grabJFrame((JDialog)object));
             }*/
            if (object instanceof JPanel) {
                ret.addAll(grabJFrame((JPanel) object));
            } else if (object instanceof JFrame) {
                ret.addAll(grabJFrame((JFrame) object));
            } else if (object instanceof JScrollPane) {
                ret.addAll(grabJFrame((JScrollPane) object));
            } else if (object instanceof JTabbedPane) {
                ret.addAll(grabJFrame((JTabbedPane) object));
            }

            if (((Component) object).getName() == null) {
                continue;
            }
            if (((Component) object).getName().isEmpty()) {
                continue;
            }

            if (object instanceof JTextField) {
                ret.add((JTextField) object);
            } else if (object instanceof JSpinner) {
                ret.add((JSpinner) object);
            } else if (object instanceof JComboBox) {
                ret.add((JComboBox) object);
            } else if (object instanceof JCheckBox) {
                ret.add((JCheckBox) object);
            } else if (object instanceof JTextArea) {
                ret.add((JTextArea) object);
            } else if (object instanceof JTable) {
                ret.add((JTable) object);
            }
        }
        return ret;
    }

    /**
     * Сканировать фрейм рекурсивно и заполнить JComboBox
     *
     * @param incom
     * @return
     */
    public static void fillJFrameJComboBox(Object incom) {
        Component[] components = getComponentsFromObject(incom);
        for (Object object : components) {
            /*if (object instanceof JDialog) {
             fillJFrameJComboBox((JDialog)object);
             }*/
            if (object instanceof JPanel) {
                fillJFrameJComboBox((JPanel) object);
            } else if (object instanceof JFrame) {
                fillJFrameJComboBox((JFrame) object);
            } else if (object instanceof JScrollPane) {
                fillJFrameJComboBox((JScrollPane) object);
            } else if (object instanceof JTabbedPane) {
                fillJFrameJComboBox((JTabbedPane) object);
            }

            if (((Component) object).getName() == null) {
                continue;
            }
            if (((Component) object).getName().isEmpty()) {
                continue;
            }

            if (object instanceof JComboBox) {
                Lang.fillComboBoxByName((JComboBox) object);
                Lang.fillComponentByName((JComponent) object);
            } else if (object instanceof JTextField || object instanceof JSpinner || object instanceof JCheckBox) {
                Lang.fillComponentByName((JComponent) object);
            } else if (object instanceof JTextArea) {
                ((JTextArea) object).setFont(GlobalConfig.viewFont);
            }

        }
    }

    /**
     * Сканировать фрейм рекурсивно на наличие JToggle
     *
     * @param incom
     * @return List getAccessibleDescription()
     */
    public static List<String> scanJToggle(Object incom) {
        List<String> ret = new ArrayList<String>();
        Component[] components = getComponentsFromObject(incom);
        for (Object object : components) {
            if (object instanceof JPanel) {
                ret.addAll(scanJToggle((JPanel) object));
            } else if (object instanceof JFrame) {
                ret.addAll(scanJToggle((JFrame) object));
            }

            if (((Component) object).getAccessibleContext().getAccessibleDescription() == null) {
                continue;
            }
            if (((Component) object).getAccessibleContext().getAccessibleDescription().isEmpty()) {
                continue;
            }
            if (object instanceof JToggleButton) {
                if (((JToggleButton) object).isSelected()) {
                    ret.add(((JToggleButton) object).getAccessibleContext().getAccessibleDescription().trim());
                }
            }
        }
        return ret;
    }

    /**
     * Сканировать фрейм рекурсивно
     *
     * @param incom
     * @return
     */
    public static List<BeanRow> scanJFrame(Object incom) {
        List<BeanRow> ret = new ArrayList<BeanRow>();
        Component[] components = getComponentsFromObject(incom);
        for (Object object : components) {
            if (object instanceof JPanel) {
                ret.addAll(scanJFrame((JPanel) object));
            } else if (object instanceof JFrame) {
                ret.addAll(scanJFrame((JFrame) object));
            } else if (object instanceof JScrollPane) {
                ret.addAll(scanJFrame((JScrollPane) object));
            } else if (object instanceof JTabbedPane) {
                ret.addAll(scanJFrame((JTabbedPane) object));
            }

            if (((Component) object).getName() == null) {
                continue;
            }
            if (((Component) object).getName().isEmpty()) {
                continue;
            }
            if (isSTD_FILEDS(((Component) object).getName())) {
                continue;
            }
            if (object instanceof JFormattedTextField) {
                boolean tmpTest = testJObject(object);
                ret.add(new BeanRow(((JFormattedTextField) object).getName(), ((JFormattedTextField) object).getValue() == null ? "" : ((JFormattedTextField) object).getValue().toString(), ((JFormattedTextField) object).getAccessibleContext().getAccessibleName(), ((JFormattedTextField) object).getAccessibleContext().getAccessibleDescription(), tmpTest));
                if (!tmpTest) {
                    ((JTextField) object).requestFocusInWindow();
                }
            } else if (object instanceof JTextField) {
                boolean tmpTest = testJObject(object);
                ret.add(new BeanRow(((JTextField) object).getName(), ((JTextField) object).getText(), ((JTextField) object).getAccessibleContext().getAccessibleName(), ((JTextField) object).getAccessibleContext().getAccessibleDescription(), tmpTest));
                if (!tmpTest) {
                    ((JTextField) object).requestFocusInWindow();
                }
            } /*
             if (object instanceof GraphBtn) {
             System.out.println("Find!");
             }*/ else if (object instanceof JSpinner) {
                boolean tmpTest = testJObject(object);
                ret.add(new BeanRow(((JSpinner) object).getName(), ((JSpinner) object).getValue().toString(), ((JSpinner) object).getAccessibleContext().getAccessibleName(), ((JSpinner) object).getAccessibleContext().getAccessibleDescription(), tmpTest));
                if (!tmpTest) {
                    ((JSpinner) object).requestFocusInWindow();
                }
            } else if (object instanceof JComboBox) {
                boolean tmpTest = testJObject(object);
                // TODO: -MulisComboBoxModelUsers
                String val;
                try {
                    val =
                            (((JComboBox) object).getModel() instanceof MulisComboBoxModel) ? ((MulisComboBoxModel) ((JComboBox) object).getModel()).getCurrentElement(((JComboBox) object).getSelectedIndex()).toString()
                            : (((JComboBox) object).getModel() instanceof SearchBoxModel) ? ((SearchBoxModel) ((JComboBox) object).getModel()).getCurrentElement(((JComboBox) object).getSelectedIndex()).toString()
                            : (((JComboBox) object).getModel() instanceof MulisComboBoxModelUsers) ? ((MulisComboBoxModelUsers) ((JComboBox) object).getModel()).getCurrentElement(((JComboBox) object).getSelectedIndex()).toString()
                            : Integer.toString(((JComboBox) object).getSelectedIndex());
                } catch (Exception ex) {
                    val = null;
                    tmpTest = false;
                }
                ret.add(new BeanRow(((JComboBox) object).getName(), val, ((JComboBox) object).getAccessibleContext().getAccessibleName(), ((JComboBox) object).getAccessibleContext().getAccessibleDescription(), tmpTest));
                if (!tmpTest) {
                    ((JComboBox) object).requestFocusInWindow();
                }
            } else if (object instanceof JCheckBox) {
                boolean tmpTest = testJObject(object);
                ret.add(new BeanRow(((JCheckBox) object).getName(), ((JCheckBox) object).isSelected() ? "1" : "0", ((JCheckBox) object).getAccessibleContext().getAccessibleName(), ((JCheckBox) object).getAccessibleContext().getAccessibleDescription(), tmpTest, 1));
                if (!tmpTest) {
                    ((JCheckBox) object).requestFocusInWindow();
                }
            } else if (object instanceof JTextArea) {
                boolean tmpTest = testJObject(object);
                ret.add(new BeanRow(((JTextArea) object).getName(), ((JTextArea) object).getText(), ((JTextArea) object).getAccessibleContext().getAccessibleName(), ((JTextArea) object).getAccessibleContext().getAccessibleDescription(), tmpTest));
                if (!tmpTest) {
                    ((JTextArea) object).requestFocusInWindow();
                }
            } else if (object instanceof JTable) {
                boolean tmpTest = testJObject(object);
                ret.add(new BeanRow(((JTable) object).getName(), tableToString((JTable) object), ((JTable) object).getAccessibleContext().getAccessibleName(), ((JTable) object).getAccessibleContext().getAccessibleDescription(), tmpTest));
                if (!tmpTest) {
                    ((JTable) object).requestFocusInWindow();
                }
            }
        }
        return ret;
    }

    /**
     * Очистить строку от знака '*'
     *
     * @param str
     * @return
     */
    public static String clearStr(String str) {
        return str.replace('*', ' ').trim();
    }

    public static String clearFullStr(String str) {
        String params[] = clearStr(str).trim().split("\\|");
        if (params.length == 2) {
            return params[1].trim();
        }
        if (params.length == 1) {
            return params[0].trim();
        }
        return "";
    }

    public static int findElementRS(ResultSet irs, String col, String val) {
        try {
            while (irs.next()) {
                if (irs.getString(col).equals(val)) {
                    return irs.getRow();
                }
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return -1;
    }

    /**
     * Текущая дата в формате SQLite
     *
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static String getCurDate() {
        return getCurDate("yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurDateFS() {
        return getCurDate("dd.MM.yyyy_HH.mm.ss");
    }

    public static String getCurDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void fillUserComboBox(List<BeanUser> list, JComboBox box) {
        box.removeAllItems();
        for (BeanUser object : list) {
            box.addItem(object.getName() + " #" + object.getIdr());
        }
    }

    public static void fillComboBox(List list, JComboBox box) {
        box.removeAllItems();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            box.addItem(object.toString());

        }
    }

    /**
     * Растягиваем окно на всю рабочую область
     *
     * @param frame
     */
    public static void maxiSizer(JDialog frame) {
        // Растягиваем окно на всю рабочую область
        GraphicsConfiguration gc = frame.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= insets.left + insets.right;
        bounds.height -= insets.top + insets.bottom;
        frame.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public static String createFileNameFilterDescriptionFromExtensions(
            String description, String[] extensions) {
        String fullDescription = (description == null)
                ? "(" : description + " (";
        // build the description from the extension list
        fullDescription += "." + extensions[0];
        for (int i = 1; i < extensions.length; i++) {
            fullDescription += ", .";
            fullDescription += extensions[i];
        }
        fullDescription += ")";
        return fullDescription;
    }

    public static boolean testParam(String str) {
        return (Integer.parseInt(str) == -1 ? false : true);
    }

    public static int testFillParam(String str) {
        int ret = Integer.parseInt(str);
        return ((ret == -1) ? 100 : ret);
    }

    public static FileFilter createFileFilter(String description,
            boolean showExtensionInDescription, String... extensions) {
        if (showExtensionInDescription) {
            description = createFileNameFilterDescriptionFromExtensions(
                    description, extensions);
        }
        return new javax.swing.filechooser.FileNameExtensionFilter(description, extensions);
    }

    public static void deleteDir(String path) {
        deleteDir(new File(path));
    }

    public static void deleteDir(File path) {
        try {
            if (path.isDirectory()) {
                for (File child : path.listFiles()) {
                    deleteDir(child);
                }
            }
            if (!path.delete()) {
                Logger.put("Ошибка при удалении: " + path);
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    public static void fillYears(JComboBox comboBox, int from, int to) {
        comboBox.removeAllItems();
        for (int i = from; i <= to; i++) {
            comboBox.addItem(i);
        }
    }

    /**
     * Конверт данных таблицы в строку
     *
     * @param table
     * @return "row|row|row~row|row|row~row|row|row"
     */
    public static String tableToString(JTable table) {
        int size = table.getModel().getRowCount();
        int cols = table.getModel().getColumnCount();
        String ret = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < cols; j++) {
                ret += table.getModel().getValueAt(i, j).toString().trim() + "|";
            }
            ret += "~";
        }
        return ret;
    }

    /**
     * Заполняем таблицу из строки
     *
     * @param table
     * @param value "row|row|row~row|row|row~row|row|row"
     */
    public static void stringToTable(JTable table, String value) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);
        String cols[] = value.split("~");
        for (String col : cols) {
            Vector v = new Vector();
            String rows[] = col.split("\\|");
            if (rows.length == 3) {
                v.addAll(Arrays.asList(rows));
                model.insertRow(table.getRowCount(), v);
            } else if (rows.length == 2) {
                v.addAll(Arrays.asList(addElement(rows, "")));
                model.insertRow(table.getRowCount(), v);
            }
        }
    }

    public static String[] addElement(String[] org, String added) {
        String[] result = Arrays.copyOf(org, org.length + 1);
        result[org.length] = added;
        return result;
    }

    /**
     * Сканировать фрейм рекурсивно, и собрать данные списков
     *
     * @param incom
     * @return
     */
    public static List<CollectModel> collectJFrame(Object incom) {
        List<CollectModel> ret = new ArrayList<CollectModel>();
        Component[] components = getComponentsFromObject(incom);
        for (Object object : components) {
            if (object instanceof JPanel) {
                ret.addAll(collectJFrame((JPanel) object));
            } else if (object instanceof JFrame) {
                ret.addAll(collectJFrame((JFrame) object));
            } else if (object instanceof JScrollPane) {
                ret.addAll(collectJFrame((JScrollPane) object));
            } else if (object instanceof JTabbedPane) {
                ret.addAll(collectJFrame((JTabbedPane) object));
            }

            if (((Component) object).getName() == null) {
                continue;
            }
            if (((Component) object).getName().isEmpty()) {
                continue;
            }

            if (object instanceof JComboBox) {
                List<BeanModel> binModels = new ArrayList<BeanModel>();

                for (int i = 0; i < ((JComboBox) object).getModel().getSize(); i++) {
                    if (((JComboBox) object).getModel().getElementAt(i) == null) {
                        continue;
                    }
                    binModels.add(new BeanModel(i + "", ((JComboBox) object).getModel().getElementAt(i).toString()));
                }
                ret.add(new CollectModel(splitParam(((JComboBox) object).getName()), ((JComboBox) object).getAccessibleContext().getAccessibleName(), ((JComboBox) object).getAccessibleContext().getAccessibleDescription(), binModels, Util.splitParamAndGetType(((JComboBox) object).getName()) + "0"));
            } else if (object instanceof JCheckBox) {
                List<BeanModel> binModels = new ArrayList<BeanModel>();
                ret.add(new CollectModel(splitParam(((JCheckBox) object).getName()), ((JCheckBox) object).getAccessibleContext().getAccessibleName(), ((JCheckBox) object).getAccessibleContext().getAccessibleDescription(), binModels, Util.splitParamAndGetType(((JCheckBox) object).getName()) + "1"));
            } else if (object instanceof JTextField) {
                List<BeanModel> binModels = new ArrayList<BeanModel>();
                ret.add(new CollectModel(splitParam(((JTextField) object).getName()), ((JTextField) object).getAccessibleContext().getAccessibleName(), ((JTextField) object).getAccessibleContext().getAccessibleDescription(), binModels, Util.splitParamAndGetType(((JTextField) object).getName()) + "2"));
            } else if (object instanceof JSpinner) {
                List<BeanModel> binModels = new ArrayList<BeanModel>();
                ret.add(new CollectModel(splitParam(((JSpinner) object).getName()), ((JSpinner) object).getAccessibleContext().getAccessibleName(), ((JSpinner) object).getAccessibleContext().getAccessibleDescription(), binModels, Util.splitParamAndGetType(((JSpinner) object).getName()) + "3"));
            }
        }
        return ret;
    }

    /**
     * Сканировать фрейм рекурсивно, и собрать данные пользователя
     *
     * @param incom
     * @param set - true - перезаписать данные
     * @return
     */
    public static void usersJFrame(Object incom, BeanUser user, boolean set) {
        Component[] components = getComponentsFromObject(incom);
        for (Object object : components) {
            if (object instanceof JPanel) {
                usersJFrame((JPanel) object, user, set);
            } else if (object instanceof JFrame) {
                usersJFrame((JFrame) object, user, set);
            } else if (object instanceof JScrollPane) {
                usersJFrame((JScrollPane) object, user, set);
            } else if (object instanceof JTabbedPane) {
                usersJFrame((JTabbedPane) object, user, set);
            }

            if (((Component) object).getName() == null) {
                continue;
            }
            if (((Component) object).getName().isEmpty()) {
                continue;
            }

            if (object instanceof JTextField || object instanceof JPasswordField) {
                String val = user.setStdParam(splitParam(((JTextField) object).getName()), ((JTextField) object).getText().trim(), set);
                if (!val.trim().isEmpty()) {
                    ((JTextField) object).setText(val);
                }
            }
        }
    }

    public static String splitParam(String string) {
        String type_tmp[] = string.split("\\|");
        if (type_tmp.length == 1) {
            return Util.clearStr(type_tmp[0].trim());
        }
        if (type_tmp.length == 2) {
            return Util.clearStr(type_tmp[1].trim());
        }
        return string;
    }

    public static String splitParamAndGetType(String string) {
        String type_tmp[] = string.split("\\|");
        if (type_tmp.length == 1) {
            return "";
        }
        if (type_tmp.length == 2) {
            return Util.convertTypeToInt(type_tmp[0]);
        }
        return string;
    }

    public static String convertTypeToInt(String str) {
        String tmp = str.toLowerCase().trim();
        if (tmp.equals("integer")) {
            return "1";
        } else if (tmp.equals("real")) {
            return "2";
        } else {
            return "";
        }
    }

    public static String convertIntToType(int num) {
        if (num == 1) {
            return "INTEGER";
        } else if (num == 2) {
            return "REAL";
        } else {
            return "";
        }
    }

    public static int getIntTypeFromHashMap(String fieldName) {
        try {

            //TODO: REFACTORING!
            for (Iterator<CollectModel> it = GlobalConfig.LIST_MODEL.iterator(); it.hasNext();) {
                CollectModel collectModel = it.next();
                if (fieldName.equals(collectModel.getName())) {
                    //String ret = collectModel.getTypeR1();
                    return collectModel.getTypeR1();
                }
            }
            return 0;
            //return GlobalConfig.hashMapTypeFields.get(fieldName);
        } catch (Exception ex) {
            Logger.put(ex);
            return 0;
        }
    }

    public static void splitParam(BeanParam string) {
        String type_tmp[] = string.getName().split("\\|");
        if (type_tmp.length == 1) {
            string.setName(Util.clearStr(type_tmp[0].trim()));
        }
        if (type_tmp.length == 2) {
            string.setType(type_tmp[0].trim());
            string.setName(Util.clearStr(type_tmp[1].trim()));
        }
    }

    public static boolean pepareSelect(DefaultTableModel tableModel, int start, int end) {
        if (tableModel.getRowCount() == 0 || tableModel.getRowCount() == end) {
            return true;
        }
        for (int i = start; i <= end; i++) {
            try {
                if (tableModel.getValueAt(i, 0) == null) {
                    return true;
                }
            } catch (Exception ex) {
                return false;
            } // TODO: поймать редкую ошибку
        }
        return false;
    }

    public static boolean isSTD_FILEDS(String str) {
        for (String object : GlobalConfig.STR_FIELDS_ARR) {
            if (object.equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param dt - System.currentTimeMillis() / Date().getTime();
     * @param dtx - System.currentTimeMillis() / Date().getTime();
     * @return Разница во времени
     */
    public static String getDiffTime(long dt, long dtx) {
        String nm[] = {"Мс.", "Сек.", "Мин.", "Час.", "Дн.", "Нед.", "Мес.", "Лет", "Век."};
        double tm[] = {0.001, 1, 60, 3600, 86400, 604800, 2630880, 31570560, 315705600};
        double dif = Math.abs(dt - dtx);
        if (dif < 1) {
            return "0 " + nm[0];
        }
        //double d = dt/1000;
        double x = 0;
        String tmp = "";
        for (int i = tm.length - 1; i >= 0; i--) {
            if (dif >= tm[i] * 1000) {
                x = Math.floor(dif / (tm[i] * 1000));
                dif -= (x * tm[i] * 1000);
                if (Math.floor(x) != 0) {
                    tmp += (int) x + "-" + nm[i] + " ";
                }
            }
        }
        return tmp.trim();
    }

    public static String readTextFile(String patch, boolean external, Class c) {
        try {
            InputStream is = (external) ? new FileInputStream(patch) : c.getResourceAsStream(patch);
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();
            isr.close();
            is.close();
            return result;
        } catch (Exception ex) {
            Logger.put(ex);
            return "";
        }
    }

    public static void saveTempFile(String patch, String text, boolean open) {
        try {
            File file = new File(patch);
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
            BufferedWriter out = new BufferedWriter(writer);
            out.write(text);
            out.close();
            if (open) {
                Desktop.getDesktop().open(file);
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    public static boolean renameFolder(String from, String to) {
        try {
            File fromFile = new File(from);
            if (fromFile.exists() || fromFile.isDirectory()) {
                File toFile = new File(to);
                if (fromFile.renameTo(toFile)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return false;
    }

    /**
     * Runtime.getRuntime().exec(patch);
     *
     * @param patch
     */
    public static void exec(String patch) {
        try {
            Runtime.getRuntime().exec(patch);
            //System.out.println(Runtime.getRuntime().exec(patch).getOutputStream());
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    public static void runProgramAroundJar(String exe) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            exec(GlobalConfig.getJarLocation() + "\\" + exe);
        } else {
            exec(GlobalConfig.getJarLocation() + "/" + exe);
        }
    }

    public static void runTeamViwerDL() {
        runProgramAroundJar("Support.exe");
    }

    public static void restartProgram() {
        runProgramAroundJar("Mulis.exe");
        System.exit(1);
    }

    public static void openUrlInExternalBorwser(String url) {
        try {
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    public static String getCommand(String file) {
        String os = System.getProperty("os.name");
        //String.format("gnome-open %s", fileName)
        //String.format("open %s", fileName)
        if (os.equals("win")) {
            return String.format("cmd /c start %s", file);
        }
        return file;
    }

    public static boolean moveDir(String from, String to) {
        File from_file = new File(from);
        File to_file = new File(to);
        //if (to_file.exists()) to_file.delete();
        return from_file.renameTo(to_file);
    }
    public static String sepFav = "|";

    public static boolean checkExistFavoriteKey(String prefix, Object num) {
        return (num != null) ? checkExistFavoriteKey(prefix, num.toString()) : false;
    }

    public static boolean checkExistFavoriteKey(String prefix, String num) {
        return GlobalConfig.selectedRows.containsKey(prefix + sepFav + num);
    }

    public static void addRemoveFavoriteKey(String prefix, String num) {
        if (!GlobalConfig.selectedRows.containsKey(prefix + sepFav + num)) {
            GlobalConfig.selectedRows.put(prefix + sepFav + num, "1");
        } else {
            GlobalConfig.selectedRows.remove(prefix + sepFav + num);
        }
    }

    public static boolean checkExistPFieldsKey(String prefix, String name) {
        return GlobalConfig.printedRows.containsKey(prefix + sepFav + name);
    }

    public static void removePFieldsKey(String prefix, String name) {
        GlobalConfig.printedRows.remove(prefix + sepFav + name);
    }

    public static void addPFieldsKey(String prefix, String name) {
        if (!GlobalConfig.printedRows.containsKey(prefix + sepFav + name)) {
            GlobalConfig.printedRows.put(prefix + sepFav + name, "1");
        }
    }

    /**
     * Сохранить записи в файл
     */
    public static void saveHashMap(String fName, HashMap map) {
        File f = new File(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + fName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (Object p : map.keySet()) {
                bw.write(p + "," + map.get(p));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            Logger.put("HashMap [" + f.getAbsolutePath() + "] saved");
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    /**
     * Загрузить записи из файла
     */
    public static void loadHashMap(String fName, HashMap map) {
        File f = new File(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + fName);
        if (!f.exists()) {
            Logger.put("HashMap [" + f.getAbsolutePath() + "] not exist;");
            return;
        }
        map.clear();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String l;
            while ((l = br.readLine()) != null) {
                String[] args = l.split("[,]", 2);
                if (args.length != 2) {
                    continue;
                }
                String p = args[0].replaceAll(" ", "");
                String b = args[1].replaceAll(" ", "");
                if (b.equalsIgnoreCase("1")) {
                    map.put(p, 1);
                }
            }
            Logger.put("HashMap [" + f.getAbsolutePath() + "] loaded");
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    public static ArrayList<String> toList(JComboBox comboBox) {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            ret.add(comboBox.getItemAt(i).toString());
        }
        return ret;
    }

    public static String buildHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            Logger.put(ex);
            return "error";
        }
    }

    public static String getTypeColumnByName(String name) {
        for (Iterator<CollectModel> it = GlobalConfig.LIST_MODEL.iterator(); it.hasNext();) {
            CollectModel collectModel = it.next();
            if (name.equals(collectModel.getName())) {
                return Util.convertIntToType(collectModel.getTypeR1());
            }
        }
        return "";
    }

    public static String clear(String str, String regex) {
        if (str == null) {
            return "";
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            return m.group();
        }
        return "";
    }

    /**
     * Склеиваем строки
     *
     * @param glue
     * @param str
     * @return
     */
    public static String glue(String glue, String... str) {
        String ret = "";
        for (String string : str) {
            if (string == null || string.isEmpty()) {
                continue;
            }
            ret += string + glue;
        }
        return (ret.length() >= 2) ? ret.substring(0, ret.length() - 1) : ret;
    }

    public static Color getSaturColor(Color orig, float s) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(orig.getRed(), orig.getGreen(), orig.getBlue(), hsv);
        return Color.getHSBColor(hsv[0], Math.max(0, Math.min(hsv[1] - s, 1)), hsv[2]);
    }

    public static boolean createZipArchive(String zippedDir, String outputZipFile) {
        try {
            ZipFile zipFile = new ZipFile(outputZipFile);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipFile.addFolder(zippedDir, parameters);
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return true;
    }

    public static boolean createMyPhotoZipArchive(String ida, String outputZipFile) {
        try {
            File file = new File(outputZipFile);
            if (file.exists()) {
                file.delete();
            }

            ZipFile zipFile = new ZipFile(outputZipFile);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
            zipFile.addFolder(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/m" + ida + "/", parameters);
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return true;
    }

    public static boolean createMyDbZipArchive(String outputZipFile) {
        try {
            
            File tempZipDir = new File(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.TMP_DIR + "/" + GlobalConfig.DB_DIR);
            if (tempZipDir.exists()) {
                FileUtils.deleteDirectory(tempZipDir);
            }
            tempZipDir.mkdirs();
            
            FileUtils.copyFileToDirectory(new File(GlobalConfig.LOCATION + "/" + GlobalConfig.CFG_DIR+"/users.xml"), tempZipDir);
            FileUtils.copyDirectory(new File(GlobalConfig.LOCATION + "/" + GlobalConfig.DB_DIR), tempZipDir);
            ZipFile zipFile = new ZipFile(outputZipFile);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
            zipFile.addFolder(tempZipDir.getAbsolutePath(), parameters);
            
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
        return true;
    }

    public static boolean createMyPhotoZipArchive(BeanTransImages images, String outputZipFile) {
        try {
            File tempZipDir = new File(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.TMP_DIR + "/" + GlobalConfig.IMG_DIR);
            if (tempZipDir.exists()) {
                FileUtils.deleteDirectory(tempZipDir);
            }
            tempZipDir.mkdirs();

            for (BeanTransParam image : images.getImages()) {
                File img = new File(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/" + image.getName().toString());
                FileUtils.copyFileToDirectory(img, new File(tempZipDir + image.getName().toString().replace(img.getName(), "")));
            }

            File file = new File(outputZipFile);
            if (file.exists()) {
                FileUtils.deleteQuietly(file);
            }

            ZipFile zipFile = new ZipFile(outputZipFile);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
            zipFile.addFolder(tempZipDir.getAbsolutePath(), parameters);

            if (tempZipDir.exists()) {
                FileUtils.deleteDirectory(tempZipDir);
            }

            /*if (file.exists()) {
             FileUtils.deleteQuietly(file);
             }*/

        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
        return true;
    }
    
    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}