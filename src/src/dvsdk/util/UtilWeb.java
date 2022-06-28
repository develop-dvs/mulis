package dvsdk.util;

import dvsdkweb.db.BeanTransBase;
import dvsdkweb.db.BeanTransFullTable;
import dvsdkweb.db.BeanTransParam;
import dvsdkweb.db.BeanTransRecord;
import dvsdkweb.db.BeanTransTable;
import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.client.CollectAgents;
import dvsdk.db.Base;
import dvsdk.db.BeanBase;
import dvsdk.db.BeanParam;
import dvsdk.db.BeanUser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dvsdkweb.usr.BeanTransAgent;
import dvsdkweb.usr.BeanTransSettings;
import dvsdkweb.usr.BeanTransUser;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author develop-dvs
 */
public class UtilWeb {

    public static BeanTransAgent getTransportAgent() {
        BeanTransAgent bta = new BeanTransAgent();
        if (!GlobalConfig.MASTER_INN_GLOBAL.trim().isEmpty()) {
            bta.setIda(Integer.parseInt(GlobalConfig.MASTER_INN_GLOBAL));
        } else {
            bta.setIda(0);
        }
        bta.setInfo(GlobalConfig.MASTER_INFO);
        bta.setEmail(GlobalConfig.MASTER_EMAIL);
        bta.setTitle(GlobalConfig.MASTER_NAME);
        bta.setSite(GlobalConfig.MASTER_SITE);
        bta.setHide(GlobalConfig.MASTER_EMAIL_HIDE);

        List<BeanTransUser> users = new ArrayList<BeanTransUser>();
        for (Iterator<BeanUser> it = GlobalConfig.LIST_USERS.iterator(); it.hasNext();) {
            BeanUser beanUser = it.next();
            users.add(new BeanTransUser(Integer.parseInt(beanUser.getIdr()), beanUser.getName(), beanUser.getPwdMD5(), beanUser.getContact()));
        }
        bta.setAgents(users);
        return bta;
    }

    public static BeanTransTable getTransportTable(Base base, String table, String where, int ida, int idr, String ssid) {
        BeanTransTable btt = new BeanTransTable(ida, idr, table, ssid);
        List<BeanBase> beanBase = new ArrayList<BeanBase>();
        List<BeanTransRecord> records = new ArrayList<BeanTransRecord>();
        beanBase = UtilSQL.selectSE(base, table, "", where);
        for (Iterator<BeanBase> it = beanBase.iterator(); it.hasNext();) {
            BeanBase beanBase1 = it.next();
            records.add(new BeanTransRecord(Integer.parseInt(beanBase1.getId()), beanBase1.getDate_add(), beanBase1.getDate_mod()));
        }
        btt.setRecords(records);
        return btt;
    }

    public static BeanTransFullTable getTransportTableFull(Base base, String table, String where, String fields, int ida, int idr, String ssid) {
        BeanTransFullTable btt = new BeanTransFullTable(ida, idr, table, ssid);
        List<BeanBase> beanBase = new ArrayList<BeanBase>();
        List<BeanTransBase> records = new ArrayList<BeanTransBase>();
        fields = fields.replace(",comment_private", "");
        //fields=fields.replace(",,", ",");
        //fields=fields.replace(",)", ")");
        //fields=fields.replace(",,", ",");
        beanBase = UtilSQL.selectSE(base, table, fields, where);
        for (Iterator<BeanBase> it = beanBase.iterator(); it.hasNext();) {
            BeanBase beanBase1 = it.next();
            BeanTransBase btr = new BeanTransBase(Integer.parseInt(beanBase1.getId()), ida, Integer.parseInt(beanBase1.getIdr()), beanBase1.getDate_add(), beanBase1.getDate_mod(), Integer.parseInt(beanBase1.getState()));
            List<BeanTransParam> params = new ArrayList<BeanTransParam>();
            for (Iterator<BeanParam> it1 = beanBase1.getParams().iterator(); it1.hasNext();) {
                BeanParam beanParam = it1.next();
                params.add(new BeanTransParam(beanParam.getName(), beanParam.getValue()));
            }
            btr.setParams(params);
            records.add(btr);
        }
        btt.setRecordsFull(records);
        return btt;
    }

    public static void syncTransportTable(Base base, BeanTransFullTable fullTable, boolean forceIda) {
        List<BeanTransBase> recordsFull = fullTable.getRecordsFull();
        List<BeanBase> bases = new ArrayList<BeanBase>();
        for (Iterator<BeanTransBase> it = recordsFull.iterator(); it.hasNext();) {
            BeanTransBase beanTransBase = it.next();
            BeanBase beanBase = new BeanBase(Integer.toString(beanTransBase.getId()), (forceIda) ? GlobalConfig.MASTER_INN : Integer.toString(beanTransBase.getIda()), Integer.toString(beanTransBase.getIdr()), beanTransBase.getDate_add(), beanTransBase.getDate_mod(), Integer.toString(beanTransBase.getState()));
            List<BeanParam> params = new ArrayList<BeanParam>();
            for (Iterator<BeanTransParam> it1 = beanTransBase.getParams().iterator(); it1.hasNext();) {
                BeanTransParam beanTransParam = it1.next();
                // TODO: UID del
                params.add(new BeanParam(beanTransParam.getName(), beanTransParam.getValue()));
            }
            beanBase.setParams(params);
            bases.add(beanBase);
        }
        //base.syncDelElements(fullTable.getTable(), bases);
        base.syncTransElements(fullTable.getTable(), bases);
    }

    public static int getMaxIdFromBeanTransFullTable(Base base, BeanTransFullTable fullTable) {
        int ret = 0, cnt_base_max = 0;
        for (Iterator<BeanTransBase> it = fullTable.getRecordsFull().iterator(); it.hasNext();) {
            BeanTransBase beanTransBase = it.next();
            int cur = beanTransBase.getId();
            if (cur > ret) {
                ret = cur;
            }
        }
        try {
            cnt_base_max = UtilSQL.max(base, fullTable.getTable()) + 1;
        } catch (Exception ex) {
            cnt_base_max = 0;
        }
        return (cnt_base_max > ret) ? cnt_base_max : ret;
    }

    public static void syncGlobalTransportTable(Base base, BeanTransFullTable fullTable, String newID) {
        List<BeanTransBase> recordsFull = fullTable.getRecordsFull();
        if (fullTable.getTable() == null) {
            return;
        }

        List<BeanBase> basesDel = new ArrayList<BeanBase>();
        String ids[] = newID.split(",");
        for (String id : ids) {
            basesDel.add(new BeanBase(id));
        }
        base.syncDelElements(fullTable.getTable(), basesDel);

        if (recordsFull.isEmpty()) {
            return;
        }
        List<BeanBase> bases = new ArrayList<BeanBase>();
        for (Iterator<BeanTransBase> it = recordsFull.iterator(); it.hasNext();) {
            BeanTransBase beanTransBase = it.next();
            BeanBase beanBase = new BeanBase(Integer.toString(beanTransBase.getId()), Integer.toString(beanTransBase.getIda()), Integer.toString(beanTransBase.getIdr()), beanTransBase.getDate_add(), beanTransBase.getDate_mod(), Integer.toString(beanTransBase.getState()));
            List<BeanParam> params = new ArrayList<BeanParam>();
            for (Iterator<BeanTransParam> it1 = beanTransBase.getParams().iterator(); it1.hasNext();) {
                BeanTransParam beanTransParam = it1.next();
                // TODO: UID del
                if (beanTransParam.getName().equals("id")) {
                    continue;
                }
                params.add(new BeanParam(beanTransParam.getName(), beanTransParam.getValue()));
            }
            beanBase.setParams(params);
            bases.add(beanBase);
        }

        base.syncTransElements(fullTable.getTable(), bases);
    }

    public static List<CollectAgents> syncTransSettings(BeanTransSettings bts) {
        List<CollectAgents> agents = new ArrayList<CollectAgents>();
        for (Iterator<BeanTransAgent> it = bts.getBta().iterator(); it.hasNext();) {
            BeanTransAgent bta = it.next();
            List<BeanUser> users = new ArrayList<BeanUser>();

            for (Iterator<BeanTransUser> it1 = bta.getAgents().iterator(); it1.hasNext();) {
                BeanTransUser btu = it1.next();
                users.add(new BeanUser(btu.getTitle(), Integer.toString(btu.getIdr()), btu.getContact(), btu.getPwdMD5(), "true"));
            }
            CollectAgents ca = new CollectAgents(Integer.toString(bta.getIda()), bta.getTitle(), bta.getEmail(), bta.getSite(), bta.getInfo(), users);
            agents.add(ca);
        }
        return agents;
    }

    public static void replaceSettings(BeanTransSettings bts, Component pane) {
        replaceInfo(bts, pane);
        replaceUsers(bts, pane);
    }
    
    public static void replaceInfo(BeanTransSettings bts, Component pane) {
        if (bts.getBta().size() > 0) {
            BeanTransAgent bta = bts.getBta().get(0);
            if (JOptionPane.showConfirmDialog(pane, "ID: [" + bta.getIda() + "]\nНазвание: [" + bta.getTitle() + "]\nEmail: [" + bta.getEmail() + "]\nСайт: [" + bta.getSite() + "]\nИнформация: [" + bta.getInfo()+"]", "Заменить информацию об агентстве?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                GlobalConfig.MASTER_NAME = bta.getTitle();
                GlobalConfig.MASTER_EMAIL = bta.getEmail();
                GlobalConfig.MASTER_SITE = bta.getSite();
                GlobalConfig.MASTER_INFO = bta.getInfo();
            }
        }
    }
    
    public static boolean replaceUsers(BeanTransSettings bts, Component pane) {
        if (bts.getBta().size() > 0) {
            //BeanTransAgent bta = bts.getBta().get(0);
            List<CollectAgents> syncTransSettings = UtilWeb.syncTransSettings(bts);
            String users ="Обнаружены пользователи: "+ syncTransSettings.get(0).getAgents().size()+" шт\n";
            for (Iterator<BeanUser> it = syncTransSettings.get(0).getAgents().iterator(); it.hasNext();) {
                BeanUser beanUser = it.next();
                users+="(id:"+beanUser.getIdr()+") "+beanUser.getName()+" ["+beanUser.getContact()+"]"+((it.hasNext())?"\n":"");
            }
            if (JOptionPane.showConfirmDialog(pane, users, "Заменить пользователей?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                GlobalConfig.LIST_USERS.clear();
                GlobalConfig.LIST_USERS.addAll(syncTransSettings.get(0).getAgents());
                UtilXML.usersToXML(GlobalConfig.LIST_USERS, false);
                UtilXML.usersToXML(GlobalConfig.LIST_USERS, true);
                GlobalConfig.regenerateUserAgentLists();
                return true;
            }
        }
        return false;
    }
    
    public static List<BeanTransParam> generateImageList() {
        List<BeanTransParam> ret = new ArrayList<BeanTransParam>();
        try {
            String[] ext = {"jpg","png"};
            Collection<File> listFiles = FileUtils.listFiles(new File(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR+"/m"+GlobalConfig.MASTER_INN), ext, true);
            for (File file : listFiles) {
                String[] cnPath = file.getAbsolutePath().split(GlobalConfig.IMG_DIR.substring(0,GlobalConfig.IMG_DIR.length()-1));
                if (cnPath.length==2) {
                    ret.add(new BeanTransParam(cnPath[1].replace("\\", "/"), DigestUtils.md5Hex(new FileInputStream(file))));
                    //System.out.println(cnPath[1]+";"+dvsdkweb.sys.Files.calcMD5File(file.getAbsolutePath()));
                }
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return ret;
    }
}