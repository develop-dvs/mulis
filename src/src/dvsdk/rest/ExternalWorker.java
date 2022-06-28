package dvsdk.rest;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import dvsdkweb.db.BeanResultList;
import dvsdkweb.db.BeanTransFullTable;
import dvsdkweb.db.BeanTransTable;
import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.client.MulisTable;
import dvsdk.util.UtilSQL;
import dvsdk.util.Lang;
import dvsdk.util.Util;
import dvsdk.util.UtilWeb;
import dvsdk.util.UtilXML;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Providers;
import dvsdkweb.usr.BeanTransAgent;
import dvsdkweb.usr.BeanTransImages;
import dvsdkweb.usr.BeanTransSettings;
import dvsdkweb.usr.BeanTransUser;
import java.io.File;

/**
 *
 * @author develop-dvs
 */
public class ExternalWorker implements Runnable {

    private Thread thread;
    private IWorker worker;
    private int cur_i = 0, cur_j = 0;
    private boolean cur_g = false;
    private String ssid = "";
    //private boolean repair = false;

    public ExternalWorker(IWorker worker) {
        this.worker = worker;
        int cur_t = GlobalConfig.getCore().getCurrentIntFrame();
        this.cur_i = cur_t / 10 % 10 - 1;
        this.cur_j = cur_t / 10 - 1;
        this.cur_g = GlobalConfig.getCore().isGlobal();
        //this.repair=worker.isRepair();
        init();
    }

    private void init() {
        thread = new Thread(this);
        thread.setName("ExternalWorker");
        thread.start();
        ssid = UUID.randomUUID().toString();
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
        }
    }
    @Context
    Providers ps;

    @Override
    public void run() {
        if (!GlobalConfig.CLIENT_SERVER_GATE.isEmpty()) {
            syncPersonalSite();
        }
        if (!GlobalConfig.MASTER_INN_GLOBAL.isEmpty()) {
            syncGlobalServer();
        }
    }

    private void syncPersonalSite() {
        worker.reset();

        worker.max_global(1);
        worker.max_group(1);
        worker.max_work(2);
        Gson gson = new Gson();
        if (worker.isRepair()) {
            if (GlobalConfig.IS_BACKUP_BASE) {
                worker.println("\n[" + Util.getCurDate() + "] Архивация объявлений");
                Util.createZipArchive(GlobalConfig.LOCATION + "/" + GlobalConfig.DB_DIR, GlobalConfig.BK_PATH + Util.getCurDateFS() + ".zip");
            }
            repairBase();
        }
        
        try {
            worker.println("[" + Util.getCurDate() + "] Инициализация подключения [" + GlobalConfig.CLIENT_SERVER_GATE + "]");
            NewJerseyClient client = new NewJerseyClient(GlobalConfig.CLIENT_SERVER_GATE_FILE, GlobalConfig.CLIENT_SERVER_GATE, "80", GlobalConfig.CLIENT_SERVER_GATE_PATH);

            worker.println("[" + Util.getCurDate() + "] Подготовка архива с базой");
            String file_db_up = GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.TMP_DIR + "/db.zip";
            Util.createMyDbZipArchive(file_db_up);
            worker.println("[" + Util.getCurDate() + "] Отправление архива с базой");
            client.postFileUpload(new File(file_db_up), GlobalConfig.CLIENT_SERVER_PWD);
            

            worker.println("[" + Util.getCurDate() + "] Генерация списка локальных фотографий");
            BeanTransImages localImageList = new BeanTransImages(UtilWeb.generateImageList());
            worker.println("[" + Util.getCurDate() + "] Синхронизация списка фотографий [" + localImageList.getImages().size() + " шт]");
            String request = gson.toJson(localImageList, BeanTransImages.class);
            Logger.put("REQ:" + request);
            String ansver = client.postRequest(request, GlobalConfig.CLIENT_SERVER_PWD);
            Logger.put("ANS:" + ansver);
            BeanTransImages serverImageList = gson.fromJson(ansver, BeanTransImages.class);

            if (serverImageList == null) {
                throw new Exception("Ответ не получен");
            }

            if (!serverImageList.getImages().isEmpty()) { //serverImageList!=null && 
                worker.println("[" + Util.getCurDate() + "] Всего фотографий для обновления [" + serverImageList.getImages().size() + " шт]");
                String file_photos_up = GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.TMP_DIR + "/photos.zip";
                worker.println("[" + Util.getCurDate() + "] Подготовка архива с фотографиями");
                Util.createMyPhotoZipArchive(serverImageList, file_photos_up);
                worker.println("[" + Util.getCurDate() + "] Отправление архива с фотографиями ["+Util.readableFileSize(new File(file_photos_up).length())+"]");
                //client.postFileUpload(new File(file_photos_up), GlobalConfig.CLIENT_SERVER_PWD);
                //worker.println("[" + Util.getCurDate() + "] Отправление архива с фотографиями - завершено");
            } else {
                worker.println("[" + Util.getCurDate() + "] Обновление фотографий не требуется");
            }
            worker.println("[" + Util.getCurDate() + "] Синхронизация завершена");
            worker.complite(true);
        } catch (Exception ex) {
            worker.complite(false);
            worker.println(Lang.decodeErrorServerAnswer(ex));
            Logger.put(ex);
            worker.error(Lang.decodeErrorServerAnswer(ex));
        }
    }

    private void repairBase() {
        BeanTransAgent bta = UtilWeb.getTransportAgent();
        int i = 0;
        int ar[];
        worker.reset();
        worker.max_group(Util.F_SALE_ARR.length + Util.F_RENT_ARR.length + Util.F_OUTLAND_ARR.length + Util.F_BUY_ARR.length);
        worker.max_work(bta.getAgents().size() + ((worker.isRepair()) ? 1 : 0));
        while (i <= 2) { // AUTOFIX: ENABLE BUY
            switch (i) {
                case 0:
                default:
                    ar = Util.F_SALE_ARR;
                    break;
                case 1:
                    ar = Util.F_RENT_ARR;
                    break;
                case 2:
                    ar = Util.F_OUTLAND_ARR;
                    break;
                case 3:
                    ar = Util.F_BUY_ARR;
                    break;
            }
            for (int num : ar) {
                worker.reset_work();
                GlobalConfig.getCore().setCurrentTab(i, num);
                MulisTable table = GlobalConfig.getCore().getCurrentTable();
                //tables.add(table.getName());
                worker.println("\n[" + Util.getCurDate() + "] Таблица: " + table.getName());
                if (worker.isRepair()) {
                    //MulisFrame frame = GlobalConfig.getCore().getCurrentForm();
                    UtilSQL.clearAndFixBase(GlobalConfig.getCore().getCurrentForm());
                    worker.println("\n[" + Util.getCurDate() + "] Ремонт таблицы завершен.");
                    worker.inc_work();
                }
                GlobalConfig.getCore().refreshCurrentTable(true);
            }
            i++;
        }
        worker.complite(true);
    }

    private void syncGlobalServer() {
        worker.reset();
        BeanTransAgent bta = UtilWeb.getTransportAgent();
        worker.max_global(4);
        worker.max_group(1);
        worker.max_work(1);

        worker.println("[" + Util.getCurDate() + "] Инициализация подключения [" + GlobalConfig.GLOBAL_MULIS_SERVER + ":" + GlobalConfig.GLOBAL_MULIS_SERVER_PORT + "]");
        try {
            NewJerseyClient client = new NewJerseyClient(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_SETTINGS);
            client.setUsernamePassword(GlobalConfig.MASTER_INN_GLOBAL, GlobalConfig.MASTER_PWD_MD5);

            ClientResponse response = client.getWebResource().head();
            int statusCode = response.getStatus();
            if (ssid.isEmpty()) {
                ssid = UUID.randomUUID().toString();
            }
            worker.println("[" + Util.getCurDate() + "] Код ответа сервера: " + statusCode + "[" + ssid + "]");
            worker.println("\n[" + Util.getCurDate() + "] Синхронизация пользователей");
            Gson gson = new Gson();
            List<BeanTransAgent> btas = new ArrayList<BeanTransAgent>();
            btas.add(bta);
            BeanTransSettings bts = new BeanTransSettings(btas, "");
            client.putJson(gson.toJson(bts));

            //List<String> tables = new ArrayList<String>();
            worker.inc_work();
            worker.inc_group();
            //worker.println("[" + Util.getCurDate() + "] Ответ сервера: " + client.getState());
            //worker.reset_work();
            //worker.reset_group();
            worker.inc_global();


            worker.reset_work();
            worker.reset_group();
            worker.println("\n[" + Util.getCurDate() + "] Синхронизация агентств");
            client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_AGENT);
            //client.getState();
            worker.inc_work();
            worker.inc_group();
            bts = gson.fromJson(client.getState(), BeanTransSettings.class);
            worker.println("[" + Util.getCurDate() + "] Количество агентств: " + bts.getBta().size());

            // TODO: simple fix generate duplicate self xml 
            UtilXML.agentsToXML(UtilWeb.syncTransSettings(bts));

            //worker.reset_work();
            //worker.reset_group();
            worker.inc_global();

            if (GlobalConfig.IS_BACKUP_BASE) {
                worker.println("\n[" + Util.getCurDate() + "] Архивация объявлений");
                Util.createZipArchive(GlobalConfig.LOCATION + "/" + GlobalConfig.DB_DIR, GlobalConfig.BK_PATH + Util.getCurDateFS() + ".zip");
            }

            worker.println("\n[" + Util.getCurDate() + "] Синхронизация объявлений");
            client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE);
            int i = 0;
            int ar[];
            worker.max_group(Util.F_SALE_ARR.length + Util.F_RENT_ARR.length + Util.F_OUTLAND_ARR.length + Util.F_BUY_ARR.length);
            worker.max_work(bta.getAgents().size() + ((worker.isRepair()) ? 1 : 0));
            while (i <= 1) {
                switch (i) {
                    case 0:
                    default:
                        ar = Util.F_SALE_ARR;
                        break;
                    case 1:
                        ar = Util.F_RENT_ARR;
                        break;
                    case 2:
                        ar = Util.F_OUTLAND_ARR;
                        break;
                    case 3:
                        ar = Util.F_BUY_ARR;
                        break;
                }
                for (int num : ar) {

                    worker.reset_work();
                    GlobalConfig.getCore().setCurrentTab(i, num);
                    MulisTable table = GlobalConfig.getCore().getCurrentTable();
                    //tables.add(table.getName());
                    worker.println("\n[" + Util.getCurDate() + "] Таблица: " + table.getName());
                    if (worker.isRepair()) {
                        //MulisFrame frame = GlobalConfig.getCore().getCurrentForm();
                        UtilSQL.clearAndFixBase(GlobalConfig.getCore().getCurrentForm());
                        worker.println("\n[" + Util.getCurDate() + "] Ремонт таблицы завершен.");
                        worker.inc_work();
                    }
                    for (Iterator<BeanTransUser> it = bta.getAgents().iterator(); it.hasNext();) {
                        BeanTransUser user = it.next();
                        client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE);

                        // Режим замены всех локальных объявлений пользователя
                        if (worker.isDownload()) {
                            BeanTransTable btt = new BeanTransTable(Integer.parseInt(GlobalConfig.MASTER_INN_GLOBAL), user.getIdr(), table.getName(), ssid);
                            btt.setMode(2);
                            worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] Запрос записей с сервера");

                            client.putJson(gson.toJson(btt));
                            client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE + "/" + ssid);
                            BeanResultList resultList = gson.fromJson(client.getState(), BeanResultList.class);
                            client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE);
                            int res_srv_cnt = resultList.getFullTable().getRecordsFull().size();
                            String res_srv = "Записей на сервере: " + res_srv_cnt;
                            int res_cl_cnt = UtilSQL.countSE(GlobalConfig.getCore().getBase(table.getName()), table.getName(), "idr='" + user.getIdr() + "'");
                            String res_cl = "Записей у клиента: " + res_cl_cnt;
                            worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] " + res_srv);
                            worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] " + res_cl);
                            if (res_srv_cnt != 0 || res_cl_cnt != 0) {
                                if (JOptionPane.showConfirmDialog(worker.getRootPaneDialog(), res_srv + "\n" + res_cl + "\n" + "Вы уверены, что хотите заменить все свои локальные объявления?", "Внимание!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                                    if (JOptionPane.showConfirmDialog(worker.getRootPaneDialog(), "Это действие безвозвратно сотрет все ваши локальные объявления", "Вы уверены?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                                        client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_FULL_BASE);
                                        BeanTransFullTable btft = resultList.getFullTable();
                                        worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] Удаление " + ((UtilSQL.delete(GlobalConfig.getCore().getBase(table.getName()), table.getName(), "idr='" + user.getIdr() + "'")) ? "" : "не ") + "завершено");
                                        UtilWeb.syncTransportTable(GlobalConfig.getCore().getBase(table.getName()), btft, true);
                                        worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] Кол-во новых ID: " + btft.getRecordsFull().size());
                                    }
                                }
                            }

                        } // Режим замены всех объявлений на сервере текущего пользователя
                        else if (worker.isReplace()) {
                            BeanTransTable btt = new BeanTransTable(Integer.parseInt(GlobalConfig.MASTER_INN_GLOBAL), user.getIdr(), table.getName(), ssid);
                            btt.setMode(1);
                            client.putJson(gson.toJson(btt));
                            try {
                                client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE + "/" + ssid);
                                int res_srv_cnt = Integer.parseInt(client.getState());
                                String res_srv = "Записей на сервере: " + res_srv_cnt;
                                client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE);
                                int res_cl_cnt = UtilSQL.countSE(GlobalConfig.getCore().getBase(table.getName()), table.getName(), "idr='" + user.getIdr() + "'");
                                String res_cl = "Записей у клиента: " + res_cl_cnt;
                                worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] " + res_srv);
                                worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] " + res_cl);
                                if (res_srv_cnt != 0 || res_cl_cnt != 0) {
                                    if (JOptionPane.showConfirmDialog(worker.getRootPaneDialog(), res_srv + "\n" + res_cl + "\n" + "Вы уверены, что хотите заменить все свои объявления на сервере?", "Внимание!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                                        client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_FULL_BASE);
                                        BeanTransFullTable btft = UtilWeb.getTransportTableFull(GlobalConfig.getCore().getBase(table.getName()), table.getName(), " idr='" + user.getIdr() + "' ", table.getSqlParams(), bta.getIda(), user.getIdr(), ssid);
                                        btft.setMode(1);
                                        client.putJson(gson.toJson(btft));
                                        worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] Кол-во новых ID: " + btft.getRecordsFull().size());


                                    }
                                }
                            } catch (Exception ex) {
                                Logger.put(ex);
                                client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE);
                            }

                            // Режим синхронизации всех объявлений текущего пользователя
                        } else if (worker.isUpdate()) {
                            BeanTransTable btt = UtilWeb.getTransportTable(GlobalConfig.getCore().getBase(table.getName()), table.getName(), " idr='" + user.getIdr() + "'", bta.getIda(), user.getIdr(), ssid);
                            client.putJson(gson.toJson(btt));
                            client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE + "/" + ssid);
                            BeanResultList resultList = gson.fromJson(client.getState(), BeanResultList.class);
                            client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE);
                            String ids = "";

                            if (resultList.getNewID() != null && !resultList.getNewID().isEmpty()) {
                                worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] Новые ID: " + resultList.getNewID());
                                ids = Util.glue(",", ids, resultList.getNewID());
                            }

                            if (resultList.getUpdateID() != null && !resultList.getUpdateID().isEmpty()) {
                                worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] Обновления в ID: " + resultList.getUpdateID());
                                ids = Util.glue(",", ids, resultList.getUpdateID());
                            }

                            if (resultList.getChangedID() != null && !resultList.getChangedID().isEmpty()) {
                                worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] Расхождения в ID: " + resultList.getChangedID());

                                BeanTransFullTable btft = resultList.getFullTable();
                                worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] " + UtilSQL.reInsert(GlobalConfig.getCore().getBase(table.getName()), table.getName(), resultList.getChangedID(), table.getSqlParams(), UtilWeb.getMaxIdFromBeanTransFullTable(GlobalConfig.getCore().getBase(table.getName()), btft)));
                                UtilWeb.syncTransportTable(GlobalConfig.getCore().getBase(table.getName()), btft, true);
                            }

                            if (!ids.isEmpty()) {
                                client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_FULL_BASE);
                                BeanTransFullTable btft = UtilWeb.getTransportTableFull(GlobalConfig.getCore().getBase(table.getName()), table.getName(), " idr='" + user.getIdr() + "' AND id IN(" + ids + ")", table.getSqlParams(), bta.getIda(), user.getIdr(), ssid);
                                client.putJson(gson.toJson(btft));
                                worker.println("[" + Util.getCurDate() + "] [" + user.getIdr() + ":" + user.getTitle() + "] Кол-во новых/измененных ID: " + btft.getRecordsFull().size());
                            }
                        }
                        worker.inc_work();
                    }
                    worker.inc_group();
                }
                //worker.reset_group();
                GlobalConfig.getCore().refreshCurrentTable(true);
                i++;
            }
            //worker.reset_work();
            worker.inc_global();


            // Global SYNC

            worker.println("\n[" + Util.getCurDate() + "] Синхронизация глобальных объявлений");
            GlobalConfig.getCore().setGlobal(true);
            i = 0;
            worker.max_work(Util.F_SALE_ARR.length + Util.F_RENT_ARR.length+ Util.F_OUTLAND_ARR.length + Util.F_BUY_ARR.length);

            worker.max_group(2);
            worker.reset_group();
            while (i <= 1) {
                switch (i) {
                    case 0:
                    default:
                        ar = Util.F_SALE_ARR;
                        break;
                    case 1:
                        ar = Util.F_RENT_ARR;
                        break;
                    case 2:
                        ar = Util.F_OUTLAND_ARR;
                        break;
                    case 3:
                        ar = Util.F_BUY_ARR;
                        break;
                }
                worker.reset_work();
                for (int num : ar) {
                    if (!GlobalConfig.getCore().isGlobal()) {
                        continue;
                    }

                    GlobalConfig.getCore().setCurrentTab(i, num);
                    MulisTable table = GlobalConfig.getCore().getCurrentTable();
                    worker.println("\n[" + Util.getCurDate() + "] Глобальная таблица: " + table.getName());

                    BeanTransTable btt = UtilWeb.getTransportTable(GlobalConfig.getCore().getBase(table.getName()), table.getName(), "", Integer.parseInt(GlobalConfig.MASTER_INN_GLOBAL), 0, ssid);

                    client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_GLOBAL_BASE);
                    client.putJson(gson.toJson(btt));
                    client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_BASE + "/" + ssid);
                    BeanResultList resultList = gson.fromJson(client.getState(), BeanResultList.class);
                    BeanTransFullTable btft = resultList.getFullTable();
                    //worker.println("[" + Util.getCurDate() + "] " + UtilSQL.reInsert(GlobalConfig.getCore().getBase(table.getName()), table.getName(), resultList.getChangedID(), table.getSqlParams(), UtilWeb.getMaxIdFromBeanTransFullTable(GlobalConfig.getCore().getBase(table.getName()), btft)));
                    worker.println("[" + Util.getCurDate() + "] Кол-во новых/измененных ID: " + btft.getRecordsFull().size());
                    UtilWeb.syncGlobalTransportTable(GlobalConfig.getCore().getBase(table.getName()), btft, resultList.getNewID());
                    worker.inc_work();
                }
                worker.inc_group();
                GlobalConfig.getCore().refreshCurrentTable(true);
                i++;
            }
            worker.inc_global();


            //worker.println(); worker.inc();
            //MessageBodyWriter uw = ps.getMessageBodyWriter(BeanUser.class, BeanUser.class, new Annotation[0], MediaType.APPLICATION_JSON_TYPE);
            /*for (Iterator<BeanTransUser> it = bta.getAgents().iterator(); it.hasNext();) {
             BeanTransUser beanUser = it.next();
             worker.print(beanUser.getIdr());
             Gson gson = new Gson();
             client.putJson(gson.toJson(beanUser));
             worker.inc_work();
             worker.println(" - " + client.getState() + "; ");
             }
             worker.print("\n");*/
            worker.println("[" + Util.getCurDate() + "] Подготовка архива с фотографиями");
            String file_photos_up = GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.TMP_DIR + "/photos.zip";
            Util.createMyPhotoZipArchive(GlobalConfig.MASTER_INN, file_photos_up);
            worker.println("[" + Util.getCurDate() + "] Подготовка архива с фотографиями завершена");
            client.changePath(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_FILE_UPLOAD);


            String remoteMD5 = client.getState();
            worker.println("[" + Util.getCurDate() + "] Запрос контрольной суммы архива с фотографиями: [" + remoteMD5 + "]");

            String localMD5 = dvsdkweb.sys.Files.calcMD5File(file_photos_up);
            worker.println("[" + Util.getCurDate() + "] Подсчет локальной контрольной суммы архива с фотографиями: [" + localMD5 + "]");
            if (!remoteMD5.equals(localMD5)) {
                worker.println("[" + Util.getCurDate() + "] Архивы различаются, обновление началось");
                client.putFileUpload(new File(file_photos_up));
            } else {
                worker.println("[" + Util.getCurDate() + "] Архивы идентичны, обновление не требуется");
            }

            worker.println("[" + Util.getCurDate() + "] Синхронизация завершена\n");
            client.close();
            GlobalConfig.getCore().setGlobal(cur_g);
            GlobalConfig.getCore().setCurrentTab(cur_i, cur_j);
            worker.complite(true);
        } catch (Exception ex) {
            worker.println(Lang.decodeErrorServerAnswer(ex));
            Logger.put(ex);
            worker.error(Lang.decodeErrorServerAnswer(ex));
            GlobalConfig.getCore().setGlobal(cur_g);
            GlobalConfig.getCore().setCurrentTab(cur_i, cur_j);
        }
    }
}