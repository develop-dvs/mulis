package dvsdk.client;
import dvsdk.util.Util;
import dvsdk.db.BeanBase;
import dvsdk.db.BeanParam;
import dvsdk.GlobalConfig;
import dvsdk.Logger;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author develop-dvs
 */
public class BeanResult extends BeanBase {

    private List<BeanRow> rows;
    private List<BeanColumn> cols;
    private String errMsg="";
    private String sqlExport="";

    public BeanResult(List<BeanRow> rows) {
        super(GlobalConfig.MASTER_INN, GlobalConfig.AGENT_INN, null, null, "1");
        setRowsParam(rows);
    }
    
    public BeanResult(JFrame frame) {
        super(GlobalConfig.MASTER_INN, GlobalConfig.AGENT_INN, null, null, "1");
        setRowsParam(frame);
    }
    
    public boolean test(Component c, JFrame frame) {
        setRowsParam(frame);
        clearRows();
        Logger.put("F: "+getParamsSQL());
        if (!errMsg.isEmpty()) {
            JOptionPane.showMessageDialog(c, errMsg,"Заполните поля!",JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }
        
    public void clearRows() {
        errMsg="";
        for (Iterator<BeanRow> it = rows.iterator(); it.hasNext();) {
            BeanRow string = it.next();
            if (string.getName()==null) continue;
            Util.splitParam(string);
            if (!string.isTest()) {errMsg+=string.getAlias()+"\n";}
        }
    }
    
    public void clearParam() {
        for (Iterator<BeanParam> it = params.iterator(); it.hasNext();) {
            BeanParam string = it.next();
            if (string.getName()==null) continue;
            Util.splitParam(string);
        }
    }
    
    public void resort() {
        String param[] = null;
        for (Iterator<BeanRow> it = rows.iterator(); it.hasNext();) {
            BeanRow binRow = it.next();
            //System.out.println(binRow.getName());
            if (binRow.getParams()==null) {
                Logger.put("ReErr: "+binRow.getName());
            } else {
                param = binRow.getParams().trim().split("\\|"); 
                if (param.length==1) binRow.setPosition(99);
                if (param.length==2) binRow.setPosition(Util.testFillParam(param[0]));
                if (param.length==3) binRow.setPosition(Util.testFillParam(param[0]));
                if (param.length==4) binRow.setPosition(Util.testFillParam(param[0]));
                if (param.length==5) binRow.setPosition(Util.testFillParam(param[0]));
            }
        }
        
        Logger.put(rows.size()+"-A: "+getPositionParamsSQL());
        Collections.sort(rows);
        Logger.put(rows.size()+"-B: "+getPositionParamsSQL());
        setParamRows();
    }
    
    private void setParamRows() {
        List<BeanParam> bps = new ArrayList<BeanParam>();
        bps.addAll(rows);
        this.params=bps;
    }
    
    private void setRowsParam(JFrame frame){
        this.rows = Util.scanJFrame(frame);
        List<BeanParam> bps = new ArrayList<BeanParam>();
        bps.addAll(rows);
        this.params=bps;
        clearParam();
    }
    
    private void setRowsParam(List<BeanRow> rows) {
        this.rows = rows;
        List<BeanParam> bps = new ArrayList<BeanParam>();
        bps.addAll(rows);
        this.params=bps;
        clearParam();
    }
    
    public List<BeanRow> getRows() {
        return rows;
    }
    
    public void setRows(List<BeanRow> rows) {
        setRowsParam(rows);
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    
    public String getPositionParamsSQL() {
        String ret=" ";
        if (rows.isEmpty()) return ret;
        for (Iterator<BeanRow> it = rows.iterator(); it.hasNext();) {
            BeanRow binRow = it.next();
            ret+=binRow.getPosition()+"-"+binRow.getName()+((it.hasNext())?",":" ");
        }
        return ret;
    }
    
    public String prepareExport() {
        sqlExport="";  
        if (rows.isEmpty()) return sqlExport;
        for (Iterator<BeanRow> it = rows.iterator(); it.hasNext();) {
            BeanRow binRow = it.next();
            String param[] = binRow.getParams().trim().split("\\|");
            if (param.length==5) {
                sqlExport+=(!param[4].equals("0"))?binRow.getName()+",":"";
            }
        }
        sqlExport=(!sqlExport.isEmpty())?sqlExport.substring(0, sqlExport.length()-1):"";
        Logger.put("SQL_EXPORT: "+sqlExport);
        return sqlExport;
    }
    
    public String hide() {
        String ret="";  
        if (rows.isEmpty()) return ret;
        for (Iterator<BeanRow> it = rows.iterator(); it.hasNext();) {
            BeanRow binRow = it.next();
            if (binRow.getPosition()!=100) ret+=binRow.getPosition()+"-"+binRow.getName()+((it.hasNext())?",":" "); 
            else it.remove();
        }
        Logger.put("SQL_SHOW: "+ret);
        setParamRows();
        return ret;
    }

    public String getSqlExport() {
        return sqlExport;
    }

    public void setSqlExport(String sqlExport) {
        this.sqlExport = sqlExport;
    }

    /**
     * Создать колонки для таблицы
     * @return 
     */
    public List<BeanColumn> fillTableColsFromFrame() {
        //BeanRow binRowIn=null;
        try {
            cols = new ArrayList<BeanColumn>();
            for (String str : GlobalConfig.STR_FIELDS_ARR) {
                if (str.equals("id")) cols.add(new BeanColumn(str, GlobalConfig.SHOW_STD_FIELD_ID));
               // else if (str.equals("ida")) {cols.add(new BeanColumn(str, false));}
                else if (str.equals("idr")) cols.add(new BeanColumn(str, 50,GlobalConfig.SHOW_STD_FIELD_IDR));
                else cols.add(new BeanColumn(str, GlobalConfig.SHOW_STD_FIELDS));
            }
            for (Iterator<BeanRow> it = rows.iterator(); it.hasNext();) {
                BeanRow binRowIn = it.next();
                String param[] = binRowIn.getParams().trim().split("\\|");
                if (binRowIn.getParams()==null) 
                    throw new NullPointerException(binRowIn.getAlias());
                if (binRowIn.getParams().isEmpty()) 
                    throw new Exception(binRowIn.getAlias());
                if (param.length==1) cols.add(new BeanColumn(binRowIn.getAlias(), Util.testParam(param[0]), binRowIn.getTypeClass()));
                if (param.length==2) cols.add(new BeanColumn(binRowIn.getAlias(), Integer.parseInt(param[1]), Util.testParam(param[0]), binRowIn.getTypeClass()));
                if (param.length==3) cols.add(new BeanColumn(binRowIn.getAlias(), Integer.parseInt(param[1]), Integer.parseInt(param[2]), Util.testParam(param[0]), binRowIn.getTypeClass()));
                if (param.length==4) cols.add(new BeanColumn(binRowIn.getAlias(), Integer.parseInt(param[1]), Integer.parseInt(param[2]), Integer.parseInt(param[3]), Util.testParam(param[0]), binRowIn.getTypeClass()));
                if (param.length==5) {
                    //TODO: REFACTORING TYPE
                    cols.add(new BeanColumn(binRowIn.getAlias(), Integer.parseInt(param[1]), Integer.parseInt(param[2]), Integer.parseInt(param[3]), Util.testParam(param[0]), binRowIn.getTypeClass(), Integer.parseInt(param[4])));
                }
            }
            return cols;
        } catch (Exception ex) {
            Logger.put(ex);
            return null;
        }
    }
}