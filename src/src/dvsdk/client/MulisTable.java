package dvsdk.client;

import dvsdk.GlobalConfig;
import dvsdk.util.Util;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author develop-dvs
 */
public class MulisTable extends JTable {
    //private IFrameCore frameCore;
    private int type;
    private String sqlVisible="";
    private String sqlExport="";
    private String limit="";
    private int curMin=0;
    private int curMax=GlobalConfig.FIRST_LOAD;
    private String curWhere="";
    private String sortColumn="";
    private String sortOrder="";
    private JScrollPane panel = null;

    public void addMouseSortListnerForHeader() {
        this.getTableHeader().addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                /*MulisTable.this.setRowSorter(null);
                java.awt.Point p = e.getPoint();
                int index = columnModel.getColumnIndexAtX(p.x);
                int realIndex = columnModel.getColumn(index).getModelIndex();
                String tmp_sortColumn=getRawNameColumn(realIndex);
                if (sortColumn.equals(tmp_sortColumn)) {
                    sortOrder=("".equals(sortOrder) || "DESC".equals(sortOrder))?"ASC":"DESC";
                } else {
                    sortColumn=tmp_sortColumn;
                    sortOrder="ASC";
                }*/
                GlobalConfig.getCore().refreshCurrentTableFull();
            }
        });
    }
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        int status = getCurrentStatus(row);
        Util.colorizedSell(c, row, status, isCellSelected(row, column));
        return super.prepareRenderer(renderer, row, column);
    }
    
    @Override
    protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel) {
            @Override
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int index = columnModel.getColumnIndexAtX(p.x);
                int realIndex = columnModel.getColumn(index).getModelIndex();
                
                return columnModel.getColumn(realIndex).getHeaderValue().toString();
            }
        };
    }
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    /**
     * ???????????????? ?????? ???????? ?? ???????? ??????????????
     * @return "area,city,street,nhome,comment,a_phone"
     */
    public String getSqlParams() {
        if (this.getAccessibleContext().getAccessibleDescription()!=null)
            return this.getAccessibleContext().getAccessibleDescription();
        return "";
    }
    
    /**
     * ???????????????? ???????????????? ???? ?????????????? "????????????"
     * @return 
     */
    public int getCurrentStatus() {
        return getCurrentStatus(this.getSelectedRow());
    }
    
    /**
     * ???????????????? ???????????????? ???? ?????????????? "????????????"
     * @return 
     */
    public int getCurrentStatus(int row) {
        if (row==-1) return 0;
        try {
            return Integer.parseInt(getValueAt(row, this.getColumn(GlobalConfig.COLORIZED_ALIAS).getModelIndex()).toString());
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * ???????????????? ???????????????? ???? ?????????????? "ida"
     * @return 
     */
    public String getCurrentIDA() {
        return getCurrentIDA(this.getSelectedRow());
    }
    
    /**
     * ???????????????? ???????????????? ???? ?????????????? "ida"
     * @return 
     */
    public String getCurrentIDA(int row) {
        if (row==-1) return "";
        try {
            return getValueAt(row, this.getColumn(GlobalConfig.IDA_ALIAS).getModelIndex()).toString();
        } catch (Exception ex) {
            return "";
        }
    }
    
    /**
     * ???????????????? ?????????????????? ID (0-?? ??????????????)
     * @return 0/1/2/3/4 ...
     */
    public String getCurrentId() {
        int row=this.getSelectedRow();
        if (row==-1) return null;
        return (String) getValueAt(row, 0);
    }
    
    /**
     * ???????????????? ?????????????????? ID (0-?? ??????????????)
     * @return 0/1/2/3/4 ...
     */
    public String getCurrentLastId() {
        int rows[] = this.getSelectedRows();
        int row=rows[this.getSelectedRowCount()-1];
        if (row==-1) return null;
        return (String) getValueAt(row, 0);
    }
    /**
     * ???????????????? ?????????????????? ID (0-?? ??????????????)
     * @return 0,1,2,3,4 ...
     */
    public List<String> getCurrentIds() {
        int[] ids = this.getSelectedRows();
        List<String> ret = new ArrayList<String>();
        for (int i : ids) {
            if (i==-1) return null;
            ret.add((String) getValueAt(i, 0));
        }
        return ret;
    }
    
    public String getRawNameColumn(int id) {
        String[] arr = (GlobalConfig.STD_FILEDS+","+getAccessibleContext().getAccessibleDescription().toString()).split(",");
        return (arr.length>id)?arr[id]:arr[0];
    }
    
    @Deprecated
    private String getAscDesc(SortOrder order) {
        if (order.equals(SortOrder.ASCENDING)) return "ASC";
        else if (order.equals(SortOrder.DESCENDING)) return "DESC";
        else return "";
    } 
    
    // TODO: ????????????????????...
    public String getRowSort() {
        //    ?????????????? 3: ???????????????? ?????? ???????????? ???? ?????????????? ?? ?????????????????? ?????????????????????? ??????????????????????????
        if ("".equals(sortColumn)) return "";
        //    ?????????????? 2: ?????? ?????????? ???? ??????????????, ?????????????????? ???????????? ????????????
        return " ORDER BY "+sortColumn+" "+sortOrder+" ";
        /* // ?????????????? 1: ???????????????? 1?? ?????????????????????? ?? ?????????????????? ???? ????????????
        if (getRowSorter()==null) return "";
        List<? extends SortKey> sortKeys = getRowSorter().getSortKeys();
        String orderBy = "";
        for (Iterator<? extends SortKey> it = sortKeys.iterator(); it.hasNext();) {
            SortKey sortKey = it.next();
            orderBy=" ORDER BY "+getRawNameColumn(sortKey.getColumn())+" "+getAscDesc(sortKey.getSortOrder());
            break;
        }
        //System.out.println(orderBy);
        return orderBy;*/
    }

    public String getSqlVisible() {
        return sqlVisible;
    }

    public void setSqlVisible(String sqlVisible) {
        this.sqlVisible = sqlVisible;
    }

    public String getSqlExport() {
        return sqlExport;
    }

    public void setSqlExport(String sqlExport) {
        this.sqlExport = sqlExport;
    }
    
    public String getLimit() {
        return limit;
    }

    public void setLimit(int start, int end) {
        this.curMin=start;
        this.curMax=end;
        this.limit = " LIMIT "+(end-start)+" OFFSET "+start;
    }
    public void fixLimit(int max) {
        int lim=(curMax-curMin);
        if (max<curMin) curMin=curMax-lim;
        this.limit = " LIMIT "+lim+" OFFSET "+curMin;
    }

    public int getCurMax() {
        return curMax;
    }

    public void setCurMax(int curMax) {
        this.curMax = curMax;
    }

    public int getCurMin() {
        return curMin;
    }

    public void setCurMin(int curMin) {
        this.curMin = curMin;
    }

    public String getCurWhere() {
        return curWhere;
    }

    public void setCurWhere(String curWhere) {
        this.curWhere = curWhere;
    }

    public JScrollPane getPanel() {
        return panel;
    }

    public void setPanel(JScrollPane panel) {
        this.panel = panel;
    }
}