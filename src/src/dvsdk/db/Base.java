package dvsdk.db;

import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.util.Util;
import dvsdk.util.UtilImg;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Base {
    private Connection connection;
    private Statement statement;
    private String name;
    //private ResultSet resultSet;

    public Base(String db_patch, String name) {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:"+db_patch+name+GlobalConfig.DB_NAME);
            this.statement = connection.createStatement();
            this.name = name;
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }
    
    public Base(String name) {
        this(GlobalConfig.LOCATION + "/" + GlobalConfig.DB_DIR,name);
    }
    
    @Deprecated
    public Base() {
        this(GlobalConfig.LOCATION + "/" + GlobalConfig.DB_DIR,"global");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Deprecated
    public synchronized void init(String base) {
        init(base, "");
    }

    public synchronized void init(String base, String prop) {
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS "+base+" ("+GlobalConfig.STD_FILEDS_CREATE+","+prop+");");
            Logger.put("CREATE TABLE IF NOT EXISTS "+base+" ("+GlobalConfig.STD_FILEDS_CREATE+","+prop+");");
        } catch (Exception ex) {
            Logger.put(base,ex);
        }
    } 
   
    public synchronized void reinit(String base, String prop) {
        try {
            statement.execute("DROP TABLE IF EXISTS "+base+";");
            statement.execute("CREATE TABLE "+base+" ("+GlobalConfig.STD_FILEDS_CREATE+","+prop+");");
            Logger.put("CREATE TABLE "+base+" ("+GlobalConfig.STD_FILEDS_CREATE+","+prop+");");
        } catch (Exception ex) {
            Logger.put(base,ex);
        }
    }
    
    public synchronized ResultSet getAllElementsSync(String baseName){
        try {
            //ResultSet returnSet;
                ResultSet rs = statement.executeQuery("SELECT * FROM "+baseName+";");
                //ResultSetMetaData rsmd = rs.getMetaData();
                //int cols = rsmd.getColumnCount();
                //returnSet = rs;
                //rs.close();
               //ResultSetMetaData rsmd = rs.getMetaData();
                //int cols = rsmd.getColumnCount();
                return rs;
            //return returnSet;
        } catch (Exception ex) {
            Logger.put(baseName,ex);
            return null;
        }
    }
    public synchronized List<BeanBase> getAllElementsSync(String base_name, String fields) {
        return getAllElementsSync(base_name, fields, "");
    }
    
    public synchronized List<BeanBase> getAllElementsSync(String base_name, String fields, String where) {
        try {
            long ts = System.currentTimeMillis();
            String sql="SELECT * FROM "+base_name+" "+((where.equals(""))?"":"WHERE "+where)+";";
            Logger.pu(sql);
            ResultSet rs = statement.executeQuery(sql);
            
            List<BeanBase> ret = new ArrayList<BeanBase>();
            //int cols = rsmd.getColumnCount();
            String[] str_f=GlobalConfig.STR_FIELDS_ARR;
            String[] str_a=fields.split(",");

            while (rs.next()) {
                BeanBase binBase = new BeanBase();
                List<BeanParam> binParams = new ArrayList<BeanParam>();
                    for (String str : str_f) {
                        binBase.setStdValue(str, rs.getString(str));
                    }

                    for (String str : str_a) {
                        binParams.add(new BeanParam(str, rs.getString(str)));
                    }
                    binBase.setParams(binParams);
                    ret.add(binBase);
            }
            Logger.put("[C:"+ret.size()+"] [OK:"+Util.getDiffTime(ts, System.currentTimeMillis())+"]");
            rs.close();
            return ret;
        } catch (Exception ex) {
            Logger.put(base_name,ex);
            return null;
        }
    }
    
    public synchronized List<BeanBase> getSelElementsSync(String base_name, String fields, String where) {
        try {
            long ts = System.currentTimeMillis();
            // TODO: SELECT *
            String sql="SELECT "+((!fields.equals("*"))?GlobalConfig.STD_FILEDS+((!fields.isEmpty())?","+fields:""):"*")+" FROM "+base_name+" "+((where.equals(""))?"":"WHERE "+where)+";";
            Logger.pu(sql);
            ResultSet rs = statement.executeQuery(sql);
                       
            List<BeanBase> ret = new ArrayList<BeanBase>();
            //int cols = rsmd.getColumnCount();
            String[] str_f=GlobalConfig.STR_FIELDS_ARR;
            String[] str_a=fields.split(",");

            while (rs.next()) {
                BeanBase binBase = new BeanBase();
                List<BeanParam> binParams = new ArrayList<BeanParam>();
                    for (String str : str_f) {
                        binBase.setStdValue(str, rs.getString(str));
                    }

                    if (!fields.isEmpty())
                        for (String str : str_a) {
                            binParams.add(new BeanParam(str, rs.getString(str)));
                        }
                    binBase.setParams(binParams);
                    ret.add(binBase);
            }
            Logger.put("[C:"+ret.size()+"] [OK:"+Util.getDiffTime(ts, System.currentTimeMillis())+"]");
            rs.close();
            return ret;
        } catch (Exception ex) {
            Logger.put(base_name,ex);
            
            // TODO: Optimized Repair table if error "no such column"
            if (ex.getMessage().contains("no such column")) {
                String[] errRepair = ex.getMessage().split(": ");
                GlobalConfig.generateListModelCurrentTable();
                GlobalConfig.regenerateListModelLists();
                if (errRepair.length==2) {
                    String col_r_name=errRepair[1].replace(")", "").trim();
                    syncAlterTable(base_name, "ADD "+col_r_name+" "+Util.getTypeColumnByName(col_r_name));
                    return getSelElementsSync(base_name, fields, where);
                }
            }
            
            return null;
        }
    }
    
    public synchronized ResultSet getNewElementsSync(String baseName, String id, String ida, String idr){
        try {
            //ResultSet returnSet;
            ResultSet rs = statement.executeQuery("SELECT * FROM "+baseName+" WHERE ida LIKE '"+ida+"' AND idr LIKE '"+idr+"' AND id NOT IN ( "+id+" )");
            //returnSet = rs;
            //rs.close();
            
            return rs;
        } catch (Exception ex) {
            Logger.put(baseName,ex);
            return null;
        }
    }
    
    public synchronized ResultSet getExistsElementsSync(String baseName, String id, String ida, String idr){
        try {
            //ResultSet returnSet;
            ResultSet rs = statement.executeQuery("SELECT * FROM "+baseName+" WHERE ida LIKE '"+ida+"' AND idr LIKE '"+idr+"' AND id IN ( "+id+" )");
            //returnSet = rs;
            //rs.close();
            
            return rs;
        } catch (Exception ex) {
            Logger.put(baseName,ex);
            return null;
        }
    }
    
    public synchronized ResultSet getModElementsSync(ResultSet irs, List<BeanBase> bases) {
        try {
            //List<BinBase> list = new ArrayList();
            //Date dt;
            for (Iterator<BeanBase> it = bases.iterator(); it.hasNext();) {
                BeanBase binBase = it.next();
                Integer fid = Util.findElementRS(irs, "id", binBase.getId());
                if (fid>-1) {
                    irs.relative(fid);
                    if (irs.getString("date_mod").equals(binBase.getDate_mod())) {
                        //list.add(binBase);
                        irs.deleteRow();
                    }
                }
            }
            return irs;
        } catch (Exception ex) {
            Logger.put(ex);
            return null;
        }
    }
    
    
    public synchronized boolean syncAddElements(String baseName, List<BeanBase> bases) {
        try {
            if (bases.isEmpty()) return true;
            long ts = System.currentTimeMillis();
            String sql="INSERT INTO "+baseName+" ( "+GlobalConfig.STD_FILEDS+","+bases.get(0).getParamsSQL()+" ) VALUES ( "+GlobalConfig.STD_FILEDS_IN+","+bases.get(0).getParamsInsertSQL()+" );";
            Logger.pu(sql);
            
            PreparedStatement pStatement = connection.prepareStatement(sql);
            for (Iterator<BeanBase> it = bases.iterator(); it.hasNext();) {
                BeanBase binBase = it.next();
                int pc=1;
                // TODO: test new add. cur or null
                //pStatement.setString(pc++, binBase.getId());
                pStatement.setString(pc++, null);
                pStatement.setString(pc++, binBase.getIda());
                pStatement.setString(pc++, binBase.getIdr()); 
                pStatement.setString(pc++, binBase.getState());
                pStatement.setString(pc++, Util.getCurDate());
                pStatement.setString(pc++, Util.getCurDate());
                Logger.pu(binBase.getId()+","+binBase.getIda()+","+binBase.getIdr()+","+binBase.getState()+","+binBase.getDate_add()+","+binBase.getDate_mod()+", "+binBase.getSQLInsertVal());
                List<BeanParam> tParams=binBase.getParams();
                for (Iterator<BeanParam> itx = tParams.iterator(); itx.hasNext();) {
                    BeanParam binParam = itx.next();
                    pStatement.setString(pc++, binParam.getValue());
                }
                pStatement.addBatch(); //if (it.hasNext()) 
            }
            connection.setAutoCommit(false);
            pStatement.executeBatch();
            //connection.commit();
            connection.setAutoCommit(true);
            Logger.put(" [OK:"+Util.getDiffTime(ts, System.currentTimeMillis())+"]");
            return true;
        } catch (Exception ex) {
            Logger.put(baseName,ex);
            return false;
        }
    }
    
    public synchronized boolean syncTransElements(String baseName, List<BeanBase> bases) {
        try {
            if (bases.isEmpty()) return true;
            long ts = System.currentTimeMillis();
            String sql="INSERT OR REPLACE INTO "+baseName+" ( "+GlobalConfig.STD_FILEDS+","+bases.get(0).getParamsSQL()+" ) VALUES ( "+GlobalConfig.STD_FILEDS_IN+","+bases.get(0).getParamsInsertSQL()+" );";
            Logger.put(sql);
            PreparedStatement pStatement = connection.prepareStatement(sql);
            for (Iterator<BeanBase> it = bases.iterator(); it.hasNext();) {
                BeanBase binBase = it.next();
                int pc=1;
                // TODO: test new add. cur or null
                pStatement.setString(pc++, binBase.getId());
                pStatement.setString(pc++, binBase.getIda());
                pStatement.setString(pc++, binBase.getIdr()); 
                pStatement.setString(pc++, binBase.getState());
                pStatement.setString(pc++, binBase.getDate_add());
                pStatement.setString(pc++, binBase.getDate_mod());
                Logger.put(binBase.getId()+","+binBase.getIda()+","+binBase.getIdr()+","+binBase.getState()+","+binBase.getDate_add()+","+binBase.getDate_mod()+", "+binBase.getSQLInsertVal());
                List<BeanParam> tParams=binBase.getParams();
                for (Iterator<BeanParam> itx = tParams.iterator(); itx.hasNext();) {
                    BeanParam binParam = itx.next();
                    pStatement.setString(pc++, binParam.getValue());
                }
                pStatement.addBatch();
            }
            connection.setAutoCommit(false);
            pStatement.executeBatch();
            connection.setAutoCommit(true);
            Logger.put(" [OK:"+Util.getDiffTime(ts, System.currentTimeMillis())+"]");
            return true;
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }
    
    public synchronized boolean syncUpdElements(String baseName, List<BeanBase> bases) { 
        return syncUpdElements(baseName, bases, false);
    }
    
    public synchronized boolean syncUpdIDRElements(String baseName, String from_id, String to_id) {
        long ts = System.currentTimeMillis();
        String sql="UPDATE "+baseName+" SET idr=\""+to_id+"\", date_mod=\""+Util.getCurDate()+"\" WHERE idr=\""+from_id+"\" AND ida=\""+GlobalConfig.MASTER_INN+"\"";
        Logger.pu(sql);
        try {
            statement.execute(sql);
            Logger.put(" [OK:"+Util.getDiffTime(ts, System.currentTimeMillis())+"]");
            return true;
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }
    
    public synchronized boolean syncUpdIdElements(String baseName, List<BeanBase> bases, int idMaxStart, boolean repositionPhotos) {
        try {
            long ts = System.currentTimeMillis();
            if (bases.isEmpty()) {
                return false;
            }
            String sql="UPDATE "+baseName+" SET id=? WHERE id=?";
            Logger.pu(sql);
            PreparedStatement pStatement = connection.prepareStatement(sql);
            for (Iterator<BeanBase> it = bases.iterator(); it.hasNext();) {
                BeanBase binBase = it.next();
                int pc=1;
                if (repositionPhotos) {
                    UtilImg.repositionPhotos(binBase.getId(),""+idMaxStart,baseName,GlobalConfig.MASTER_INN);
                }
                pStatement.setString(pc++, Integer.toString(idMaxStart++));
                pStatement.setString(pc++, binBase.getId());
                pStatement.addBatch(); //if (it.hasNext()) 
            }
            connection.setAutoCommit(false);
            pStatement.executeBatch();
            connection.setAutoCommit(true);
            Logger.put(" [OK:"+Util.getDiffTime(ts, System.currentTimeMillis())+"]");
            return true;
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }
    
    public synchronized boolean syncUpdElements(String baseName, List<BeanBase> bases, boolean noIDR) {
        return syncUpdElements(baseName, bases, noIDR, false);
    }
    
    public synchronized boolean syncUpdElements(String baseName, List<BeanBase> bases, boolean noIDR, boolean noIDA) {
        try {
            long ts = System.currentTimeMillis();
            if (bases.isEmpty()) {
                return false;
            }
            String param=bases.get(0).getSQLUpdate().trim()+((GlobalConfig.IS_MASTER && !noIDR)?",idr=?":"");
            String sql="UPDATE "+baseName+" SET "+(param.isEmpty()?"":param+",")+" date_mod=\""+Util.getCurDate()+"\", state=? WHERE id=?"+((!noIDA)?" AND ida=?":"")+((GlobalConfig.IS_MASTER || noIDR)?"":" AND idr=?");
            Logger.pu(sql);
            PreparedStatement pStatement = connection.prepareStatement(sql);
            for (Iterator<BeanBase> it = bases.iterator(); it.hasNext();) {
                BeanBase binBase = it.next();
                int pc=1;
                List<BeanParam> tParams=binBase.getParams();
                for (Iterator<BeanParam> itx = tParams.iterator(); itx.hasNext();) {
                    BeanParam binParam = itx.next();
                    pStatement.setString(pc++, binParam.getValue());
                }
                // TODO: Разобраться с idr
                if (GlobalConfig.IS_MASTER && !noIDR) {pStatement.setString(pc++, binBase.getIdr());}
                pStatement.setString(pc++, binBase.getState());
                pStatement.setString(pc++, binBase.getId());
                pStatement.setString(pc++, binBase.getIda());
                if (!GlobalConfig.IS_MASTER && !noIDR) pStatement.setString(pc++, binBase.getIdr());
                pStatement.addBatch(); //if (it.hasNext()) 
            }
            connection.setAutoCommit(false);
            pStatement.executeBatch();
            connection.setAutoCommit(true);
            Logger.put(" [OK:"+Util.getDiffTime(ts, System.currentTimeMillis())+"]");
            return true;
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }
    
    public synchronized boolean syncDelElements(String baseName, List<BeanBase> bases) {
        try {
            if (bases.isEmpty()) return true;
            long ts = System.currentTimeMillis();
            String sql="DELETE FROM "+baseName+" WHERE id = ?";
            Logger.pu(sql);
            PreparedStatement pStatement = connection.prepareStatement(sql);
            for (Iterator<BeanBase> it = bases.iterator(); it.hasNext();) {
                BeanBase binBase = it.next();
                pStatement.setString(1, binBase.getId());
                pStatement.addBatch();
            }
            connection.setAutoCommit(false);
            pStatement.executeBatch();
            connection.setAutoCommit(true);
            Logger.put(" [OK:"+Util.getDiffTime(ts, System.currentTimeMillis())+"]");
            return true;
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }
    
    public synchronized boolean syncDelElements(String baseName, String where) {
        try {
            long ts = System.currentTimeMillis();
            String sql="DELETE FROM "+baseName+((where!=null)?(" WHERE "+where):"");
            Logger.pu(sql);
            statement.execute(sql);
            Logger.put(" [DEL OK:"+Util.getDiffTime(ts, System.currentTimeMillis())+"]");
            return true;
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }
    
    public synchronized int syncMaxIdElements(String baseName) {
        try {
            //long ts = System.currentTimeMillis();
            int ret;
            String sql="SELECT max(id) AS mx FROM "+baseName;
            Logger.pu(sql);
            ResultSet rs = statement.executeQuery(sql);
            ret = rs.getInt("mx");
            rs.close();
            Logger.put("MAX:"+baseName+":"+ret);
            return ret;
        } catch (Exception ex) {
            Logger.put(ex);
            return 0;
        }
    }
    
    public synchronized int syncCountElements(String baseName,String state) {
        try {
            //long ts = System.currentTimeMillis();
            int ret;
            String sql="SELECT COUNT(1) AS cnt FROM "+baseName+((state!=null)?(" WHERE state ="+state):"");
            Logger.pu(sql);
            ResultSet rs = statement.executeQuery(sql);
            ret = rs.getInt("cnt");
            rs.close();
            Logger.put("CNT:"+baseName+":"+ret);
            return ret;
        } catch (Exception ex) {
            Logger.put(ex);
            return 0;
        }
    }
    
    public synchronized int syncCountElementsSE(String baseName, String where) {
        try {
            int ret;
            String sql="SELECT COUNT(1) AS cnt FROM "+baseName+((where!=null)?(" WHERE "+where):"");
            Logger.pu(sql);
            ResultSet rs = statement.executeQuery(sql);
            ret = rs.getInt("cnt");
            rs.close();
            Logger.put("CNT:"+baseName+":"+ret);
            return ret;
            // TODO ERROR: call() called in inappropriate state
        } catch (Exception ex) {
            Logger.put(ex);
            return 0;
        }
    }
    
    public synchronized void syncAlterTable(String baseName, String alter) {
        try {
            String sql="ALTER TABLE "+baseName+" "+alter;
            statement.execute(sql);
            Logger.put("\nALT:"+sql);
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }
    
    public void disconnect() {
        try {
            statement.close();
            connection.close();
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }
}