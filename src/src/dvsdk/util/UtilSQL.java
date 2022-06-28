package dvsdk.util;

import dvsdk.client.BeanResult;
import dvsdk.client.MulisComboBoxModelUsers;
import dvsdk.client.MulisFrame;
import dvsdk.tmpl.MulisPrinter;
import dvsdk.client.MulisTable;
import dvsdk.db.Base;
import dvsdk.db.BeanBase;
import dvsdk.db.BeanParam;
import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.client.CollectModel;
import dvsdk.client.MulisViewportChangeListener;
import dvsdk.client.ScrollMouseWheelListner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author develop-dvs
 */
public class UtilSQL {
    
    public static void initReTable(Base base, String name, String fields) {
        //Base base = new Base();
        base.reinit(name, fields);
        //base.disconnect();
    }

    public synchronized static BeanBase fillFromTable(Base base, String table_name, String fields, String id) {
        List<BeanBase> binBases = new ArrayList<BeanBase>();
        binBases.addAll(UtilSQL.select(base, table_name, fields, "id=" + id));
        return binBases.get(0);
    }

    public synchronized static List<BeanBase> select(Base base, String base_name, String fields, String where) {
        return base.getAllElementsSync(base_name, fields, where);
    }

    public synchronized static List<BeanBase> selectSE(Base base, String base_name, String fields, String where) {
        return base.getSelElementsSync(base_name, fields, where);
    }

    public synchronized static void fillTabelData(Base base, MulisTable table, String name, String fields, boolean full) {
        fillTabelData(base, table, name, fields, "", full);
    }

    public synchronized static void fake_delete(Base base, String table_name, List<String> ids) {
        List<BeanBase> binBases = new ArrayList<BeanBase>();
        for (Iterator<String> it = ids.iterator(); it.hasNext();) {
            String string = it.next();
            BeanBase binBase = new BeanBase();
            binBase.setId(string);
            binBase.setIda(GlobalConfig.MASTER_INN);
            binBase.setIdr(GlobalConfig.AGENT_INN);
            binBase.setState("0");
            binBases.add(binBase);
        }
        //updateSE(base, table_name, binBases);
        base.syncUpdElements(table_name, binBases, ((GlobalConfig.IS_MASTER)?true:false) );
        
    }
    
    public synchronized static void fake_delete(Base base, String table_name, String id) {
        BeanBase binBase = new BeanBase();
        binBase.setId(id);
        binBase.setIda(GlobalConfig.MASTER_INN);
        binBase.setIdr(GlobalConfig.AGENT_INN);
        binBase.setState("0");
        //update(base, table_name, binBase);
        List<BeanBase> bbs = new ArrayList<BeanBase>();
        bbs.add(binBase);
        base.syncUpdElements(table_name, bbs, ((GlobalConfig.IS_MASTER)?true:false));
    }
    
    public synchronized static void UpdIDRElements(Base base, String baseName, String from_id, String to_id) {
        base.syncUpdIDRElements(baseName, from_id, to_id);
    }

    public synchronized static boolean grab(Base base, MulisFrame frame) {
        boolean r = false;
        BeanResult binResult = Util.getBinResult(frame);
        binResult.setIdr(((MulisComboBoxModelUsers) (frame.getAgentInfo()).getModel()).getCurrentElement(frame.getAgentInfo().getSelectedIndex()).toString());
        if (r = binResult.test(frame.getRootPane(), frame)) {
            if (!frame.isMod()) {
                boolean rx = insert(base, frame.getName(), binResult);
                if (!rx) {
                    // если id при вставке != null
                    String newId = Integer.toString(UtilSQL.count(base, frame.getName(), null) + 1);
                    Logger.put("\n[Set new ID: " + newId + "]");
                    
                    frame.setID(newId);
                    binResult.setId(newId);
                    insert(base, frame.getName(), binResult);
                }
            } else {
                update(base, frame.getName(), binResult);
            }
        }
        return r;
    }
    
    public static void buildPrintTMPL(Base base, String name, String fields, String where_add, List<String> ids, int tmpl, boolean map, boolean addInfo, boolean photo, boolean contact, boolean id, boolean date, boolean priv) {
        String where="id IN(";
        for (Iterator<String> it = ids.iterator(); it.hasNext();) {
            String string = it.next();
            where+=string+(it.hasNext()?",":")");
        }
        if (ids.isEmpty()) where="";
        MulisPrinter printer = new MulisPrinter(selectSE(base, name, fields, where+((where_add.trim().isEmpty())?"":((where.isEmpty())?" ":" AND ")+where_add+" ")), name);
        printer.build(map,addInfo,photo,contact,id,date,priv,tmpl);
    }

    /**
     * Запрос из базы всех данных и вставка их в таблицу
     * @param base
     * @param table
     * @param name
     * @param fields 
     */
    public synchronized static void fillTabelData(Base base, MulisTable table, String name, String fields, String where, boolean full) {
        // TODO: ДОДЕЛАТЬ!!!
        if (base==null) return;
        long ts = System.currentTimeMillis();
        boolean cur=false;
        if (table.getRowCount()==0) cur=true;
        int maxRow=0;
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        if (!(name+where).equals(name+table.getCurWhere())) {
            maxRow = countSE(base, name, where);
            if (maxRow<GlobalConfig.FIRST_LOAD) {table.setLimit(0,GlobalConfig.FIRST_LOAD);}
            table.fixLimit(maxRow);
            tableModel.setNumRows(0);
            tableModel.getDataVector().removeAllElements();
            table.setCurWhere(where);
            cur=true;
        }
        if (full) table.setLimit(0,countSE(base, name, where));

        int i = table.getCurMin();
        if (cur) tableModel.setNumRows(maxRow);
        int maxI = tableModel.getRowCount();
        if (table.getCurMax()-GlobalConfig.ADD_LIMIT<maxI  || maxI<GlobalConfig.FIRST_LOAD || cur) 
            if (Util.pepareSelect(tableModel, i, table.getCurMax()-GlobalConfig.ADD_LIMIT))
            for (Iterator<BeanBase> it = selectSE(base, name, fields, where+table.getRowSort()+table.getLimit()).iterator(); it.hasNext();) {
                BeanBase binBase = it.next();
                int j=0;
                if (tableModel.getValueAt(i, 0)==null) {
                    tableModel.setValueAt(binBase.getId(), i, j++);
                    tableModel.setValueAt(binBase.getIda(), i, j++);
                    tableModel.setValueAt(Lang.findAgentNameByIDA(binBase.getIda(), binBase.getIdr()) , i, j++);
                    tableModel.setValueAt(binBase.getState(), i, j++);
                    tableModel.setValueAt(binBase.getDate_add(), i, j++);
                    tableModel.setValueAt(binBase.getDate_mod(), i, j++);
                    for (Iterator<BeanParam> it1 = binBase.getParams().iterator(); it1.hasNext();) {
                        BeanParam binParam1 = it1.next();
                        if (binParam1.getName().equals(GlobalConfig.COLORIZED_NAME)) tableModel.setValueAt(binParam1.getValue(), i, j++);
                        else tableModel.setValueAt(Lang.findByName(binParam1.getName(), binParam1.getValue()), i, j++);
                    }
                }
                i++;
                //tableModel.insertRow(table.getRowCount(), v);
            }
        if (cur) tableModel.setRowCount(maxRow);
        Logger.put("FillTable " + name + " - [OK:" + Util.getDiffTime(ts, System.currentTimeMillis()) + "]");
        
        
            /*table.getRowSorter().addRowSorterListener(new RowSorterListener() {
                @Override
                public void sorterChanged(RowSorterEvent e) {
                    System.out.println("state changed");
                }
            });*/
    }

    /**
     * Запрос из базы только нужной строки
     * @param base
     * @param table
     * @param name
     * @param fields 
     * @param id 
     */
    public synchronized static void fillTabelData(Base base, MulisTable table, String name, String fields, String where, String id) {
        // TODO: ДОДЕЛАТЬ!!!
        long ts = System.currentTimeMillis();

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        
        for (Iterator<BeanBase> it = selectSE(base, name, fields, where+" AND id="+id+" "+table.getRowSort()).iterator(); it.hasNext();) {
            BeanBase binBase = it.next();
            int j=0;
            int i=table.getSelectedRow();
            if (tableModel.getValueAt(i, 0)!=null && tableModel.getValueAt(i, 0).equals(binBase.getId())) {
                tableModel.setValueAt(binBase.getId(), i, j++);
                tableModel.setValueAt(binBase.getIda(), i, j++);
                tableModel.setValueAt(Lang.findAgentNameByIDA(binBase.getIda(), binBase.getIdr()) , i, j++);
                tableModel.setValueAt(binBase.getState(), i, j++);
                tableModel.setValueAt(binBase.getDate_add(), i, j++);
                tableModel.setValueAt(binBase.getDate_mod(), i, j++);
                for (Iterator<BeanParam> it1 = binBase.getParams().iterator(); it1.hasNext();) {
                    BeanParam binParam1 = it1.next();
                    if (binParam1.getName().equals(GlobalConfig.COLORIZED_NAME)) tableModel.setValueAt(binParam1.getValue(), i, j++);
                    else tableModel.setValueAt(Lang.findByName(binParam1.getName(), binParam1.getValue()), i, j++);
                }
            }
        }
        Logger.put("FillTable["+id+"] " + name + " - [OK:" + Util.getDiffTime(ts, System.currentTimeMillis()) + "]");
    }
    
    public synchronized static void initTable(Base base, String name, String fields) {
        base.init(name, fields);
    }

    public synchronized static boolean insert(Base base, String base_name, BeanResult beanResult) {
        List<BeanBase> bbs = new ArrayList<BeanBase>();
        //for (int i = 0; i < 10000; i++) {
            bbs.add(beanResult);
        //}
        return base.syncAddElements(base_name, bbs);
    }

    public synchronized static boolean insert(Base base, String base_name, List<BeanResult> beanResults) {
        List<BeanBase> bbs = new ArrayList<BeanBase>();
        bbs.addAll(beanResults);
        return base.syncAddElements(base_name, bbs);
    }
    
    public synchronized static boolean insertSE(Base base, String base_name, List<BeanBase> beanBases) {
        return base.syncAddElements(base_name, beanBases);
    }
    
    public synchronized static boolean insertSE(Base base, String base_name, BeanBase beanBase) {
        List<BeanBase> bbs = new ArrayList<BeanBase>();
        bbs.add(beanBase);
        return base.syncAddElements(base_name, bbs);
    }
    
    public synchronized static boolean delete(Base base, String base_name, String where) {
        return base.syncDelElements(base_name, where);
    }
    
    public synchronized static boolean delete(Base base, String base_name, BeanResult beanResult) {
        List<BeanBase> bbs = new ArrayList<BeanBase>();
        bbs.add(beanResult);
        return base.syncDelElements(base_name, bbs);
    }

    public synchronized static boolean delete(Base base, String base_name, List<BeanResult> beanResults) {
        List<BeanBase> bbs = new ArrayList<BeanBase>();
        bbs.addAll(beanResults);
        return base.syncDelElements(base_name, bbs);
    }
    
    public synchronized static boolean deleteSE(Base base, String base_name, List<BeanBase> beanBases) {
        return base.syncDelElements(base_name, beanBases);
    }
    
    public synchronized static boolean updateID(Base base, String base_name, List<BeanBase> binBases, int maxId) {
        return base.syncUpdIdElements(base_name, binBases, maxId, true);
    }
    
    public synchronized static boolean update(Base base, String base_name, BeanBase beanBase) {
        List<BeanBase> bbs = new ArrayList<BeanBase>();
        bbs.add(beanBase);
        return base.syncUpdElements(base_name, bbs);
    }
    
    // TODO: Refactoring
    public synchronized static boolean updateSE(Base base, String base_name, List<BeanBase> binBases, boolean noIDA) {
        return base.syncUpdElements(base_name, binBases, true, noIDA);
    }
    
    public synchronized static boolean updateSE(Base base, String base_name, List<BeanBase> binBases) {
        return base.syncUpdElements(base_name, binBases);
    }
    
    public synchronized static boolean update(Base base, String base_name, BeanResult beanResult) {
        List<BeanBase> bbs = new ArrayList<BeanBase>();
        bbs.add(beanResult);
        return base.syncUpdElements(base_name, bbs);
    }

    public synchronized static boolean update(Base base, String base_name, List<BeanResult> beanResults) {
        List<BeanBase> bbs = new ArrayList<BeanBase>();
        bbs.addAll(beanResults);
        return base.syncUpdElements(base_name, bbs);
    }

    public synchronized static int count(Base base, String base_name, String status) {
        return base.syncCountElements(base_name, status);
    }
    
    public synchronized static int max(Base base, String base_name) {
        return base.syncMaxIdElements(base_name);
    }
    
    public synchronized static int countSE(Base base, String base_name, String where) {
        return base.syncCountElementsSE(base_name, where);
    }
    
    public synchronized static void clearAndFixBase(MulisFrame frame) {
        try {
            // TODO: REFACTORING!
            List<CollectModel> collectModels = Util.collectJFrame(frame);
            List<String> typed_fields = new ArrayList<String>();
            typed_fields.add("photos");
            String sql_typed_fields = "";
            
            for (Iterator<CollectModel> it = collectModels.iterator(); it.hasNext();) {
                CollectModel collectModel = it.next();

                if (GlobalConfig.isExcludeField(collectModel.getName())) {
                    continue;
                }
                
                if (!Util.convertIntToType(collectModel.getTypeR1()).isEmpty()) {
                    typed_fields.add(collectModel.getName());
                }
            }
            
            for (Iterator<String> it1 = typed_fields.iterator(); it1.hasNext();) {
                String string = it1.next();
                sql_typed_fields+=string+((it1.hasNext())?",":"");
            }
            // TODO: REFACTORING;
            sql_typed_fields=sql_typed_fields.replace("price_a", "#").replace("price", "$").replace("#", "price").replace("$", "price_a");
            UtilImg.repositionImgDirs(frame.getName());
            Base base = GlobalConfig.getCore().getBase(frame.getName());
            if (base==null) return;
            List<BeanBase> binBases = UtilSQL.selectSE(base, frame.getName(), sql_typed_fields, "");
            for (Iterator<BeanBase> it = binBases.iterator(); it.hasNext();) {
                BeanBase beanBase = it.next();
                beanBase.setIda(GlobalConfig.MASTER_INN);
                
                List<BeanParam> params = beanBase.getParams();
                int price = 0;
                for (Iterator<BeanParam> it1 = params.iterator(); it1.hasNext();) {
                    BeanParam beanParam = it1.next();
                    try {
                        if (beanParam.getName().equals("photos")) {
                            beanParam.setValue(""+UtilImg.getImageCountFromDir(beanBase.getId(), beanBase.getIda(), frame.getName()));
                            continue;
                        }
                        // TODO: FIX null
                        //if (beanParam.getValue()==null) continue;
                        
                        String crl = beanParam.getValue();
                        switch (Util.getIntTypeFromHashMap(beanParam.getName())) {
                            case 0: default: break;

                            // INTEGER
                            case 1:
                                if (crl==null || crl.isEmpty() || crl.trim().isEmpty()) {
                                    beanParam.setValue("0");
                                    //crl="0";
                                    //continue;
                                }
                                crl = Util.clear(beanParam.getValue().trim().replace(" ", ""),"[-]?\\d+(\\.\\d+)?");
                                if (beanParam.getName().equals("price") || beanParam.getName().equals("auction") || beanParam.getName().equals("prepaid")) {
                                    if (crl.trim().isEmpty()) crl="0";
                                    price+=Integer.parseInt(crl);
                                    beanParam.setValue(Integer.toString(Integer.parseInt(crl)));
                                } else if (beanParam.getName().equals("price_a")) {
                                    beanParam.setValue(Integer.toString(price));
                                } else beanParam.setValue(Integer.toString(Integer.parseInt(crl)));
                                break;
                            // FLOAT (REAL)
                            case 2:
                                if (crl==null || crl.isEmpty() || crl.trim().isEmpty()) {
                                    beanParam.setValue("0");
                                    continue;
                                }
                                crl = Util.clear(beanParam.getValue().trim().replace(" ", ""),"[-]?\\d+(\\.\\d+)?");
                                if (crl.trim().isEmpty()) crl="0";
                                beanParam.setValue(Float.toString(Float.parseFloat(crl)));
                                break;
                        }
                    } catch (Exception ex) {
                        Logger.put(ex);
                    }
                }
            }
            UtilSQL.updateSE(base, frame.getName(), binBases);
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }
    
    /**
     * Инициализировать таблицу
     * @param base
     * @param frame
     * @param drop
     * @return 
     */
    public synchronized static MulisTable initBaseTable(Base base, JFrame frame, boolean drop) {
        try {
            if (frame == null) {
                return null;
            }
            MulisTable jTable = new MulisTable();
            //jTable.setAutoCreateRowSorter(false);
            //jTable.setRowSorter(null);
            jTable.addMouseSortListnerForHeader();
            JScrollPane jScrollPane = new JScrollPane();
            jScrollPane.add(jTable);
            jScrollPane.setViewportView(jTable);

            // Работает все одновременно
            jScrollPane.addMouseWheelListener(new ScrollMouseWheelListner());

            // Работает только увеличение, скролл не работает над таблицей. Над сколл-баром работает.
            //jTable.addMouseWheelListener(new TableWheelListner());
            //panel.removeAll();
            //panel.revalidate();
           // panel.repaint();
            //JPanel jp = new JPanel();
            
            //jp.add(jScrollPane);
            //panel.revalidate();
           // panel.repaint();
            
            //panel.updateUI();

            BeanResult binResult = Util.getBinResult(frame);
            binResult.resort();

            String tmp_par = binResult.getParamsCreateSQL().trim();
            String ret_params = binResult.getParamsSQL().trim();

            jTable.setSqlExport(binResult.prepareExport());

            binResult.hide();
            String hide_params = binResult.getParamsSQL().trim();
            jTable.setSqlVisible(hide_params);
            jTable.getTableHeader().setReorderingAllowed(false);

            jTable.setName(frame.getName());
            jTable.setType(GlobalConfig.getCore().getCurrentIntFrame());
            jTable.getAccessibleContext().setAccessibleDescription(ret_params);
            
            if (drop) {
                UtilSQL.initReTable(base, frame.getName(), tmp_par);
            } else {
                UtilSQL.initTable(base, frame.getName(), tmp_par);
            }
            Util.fillTableModel(jTable, binResult.fillTableColsFromFrame());
            UtilSQL.fillTabelData(base, jTable, frame.getName(), hide_params, GlobalConfig.getCore().getCurrentFilter(),false);
            frame.dispose();
            JViewport viewport = jScrollPane.getViewport();
            viewport.addChangeListener(new MulisViewportChangeListener(jTable, viewport));
            jTable.setPanel(jScrollPane);
            return jTable;
        } catch (Exception ex) {
            Logger.put(ex);
            return null;
        }
    }
    
    /**
     * Сформировать фильтр из JToggle
     * @param incom JFrame
     * @param add Добавить условие И
     * @return "room>=4 and new=1" //пример
     */
    public static String getSQLWhere(Object incom, String add) {
        List<String> sql = Util.scanJToggle(incom);
        String ret = "";
        if (sql.isEmpty() && add.isEmpty()) {
            return ret;
        }

        for (Iterator<String> it = sql.iterator(); it.hasNext();) {
            String string = it.next();
            ret += string + ((it.hasNext()) ? " and " : " ");
        }
        Logger.put(ret);
        return ret + ((add.isEmpty()) ? "" : (ret.isEmpty()) ? add : " and " + add);
    }

    /**
     * Сформировать фильтр из JToggle
     * @param incom JFrame
     * @return "room>=4 and new=1" //пример
     */
    public static String getSQLWhere(Object incom) {
        return getSQLWhere(incom, "");
    }
    
    public static String buildFavoriteFilter(String name) {
        String ret="";
        for (Object p : GlobalConfig.selectedRows.keySet()) {
            if (p.toString().startsWith(name)) 
                ret+=p.toString().split("\\|")[1]+",";
            //Object id=GlobalConfig.selectedRows.get(p);
        }
        return (ret.isEmpty())?"":" AND id IN ("+ret.substring(0, ret.length()-1) +") ";
    }

    public static String reInsert(Base base, String table, String ids, String fields, int maxId) {
        if (ids.isEmpty()) return "Обновлений нет";
        maxId++;
        List<BeanBase> list = selectSE(base, table, fields, "id IN("+ids+")");
        updateID(base, table, list, maxId);
        
        return "Обновление индексов завершено";
    }
}