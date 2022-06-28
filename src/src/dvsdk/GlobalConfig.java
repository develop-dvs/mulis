package dvsdk;

import dvsdk.client.CollectAgents;
import dvsdk.client.CollectModel;
import dvsdk.client.IFrameCore;
import dvsdk.db.BeanUser;
import dvsdk.util.Util;
import dvsdk.util.UtilXML;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class GlobalConfig {

    public static IFrameCore core = null;
    public static boolean INI = false;
    public static final String VERSION = "B.0.1.6 [04.12.2013]";
    public static String STD_FILEDS = "id,ida,idr,state,date_add,date_mod";
    public static String STD_USR_FILEDS = "inn,name,state,date_add,date_mod";
    public static String STD_FILEDS_CREATE = "id INTEGER PRIMARY KEY AUTOINCREMENT,ida,idr,state INTEGER,date_add date default CURRENT_DATE,date_mod date default CURRENT_DATE";
    public static String STD_FILEDS_IN = "?,?,?,?,?,?";
    public static int STD_FILEDS_C = 6;
    
    public static String[] STR_FIELDS_ARR = {"id", "ida", "idr", "state", "date_add", "date_mod"};
    public static String[] STR_FIELDS_ARR_TYPE = {"INTEGER", "INTEGER", "INTEGER", "INTEGER", "timestamp without time zone", "timestamp without time zone"};
    //public static String[] STR_TABLES = {"s_kvart", "s_komnat", "s_kommers", "s_garazg", "s_dom"};
    
    //public static HashMap<String, Integer> hashMapTypeFields = new HashMap<String, Integer>();
    
    public static String[] STR_FIELDS_ARR_ADD = {"comment","loctab"};
    public static String[] STR_FIELDS_ARR_ADD_TYPE = {"",""};
    public static int STD_FILEDS_ADD_C = 2;
    
    public static boolean SHOW_STD_FIELD_ID = true;
    public static boolean SHOW_STD_FIELD_IDR = true;
    public static boolean SHOW_STD_FIELDS = false;
    
    /*public static int GLOBAL_MULIS_SMTP_PORT = 25000;
    public static String GLOBAL_MULIS_SMTP_HOST = "localhost";*/
    
    public static String GLOBAL_MULIS_HOST = "mulis.ru";
    public static String GLOBAL_MULIS_HOST_UPDATE = "/version/mulis";
    public static String GLOBAL_MULIS_FEEDBACK_EMAIL = "mulis@divasoft.ru";
    public static String GLOBAL_MULIS_FEEDBACK_EMAIL_NAME = "Mulis (Robot)";
    public static String GLOBAL_MULIS_SERVER = "s.divasoft.ru";
    public static String GLOBAL_MULIS_SERVER_PORT = "80";
    public static String GLOBAL_MULIS_SERVER_URI = "mulisrs/resources";
    public static String GLOBAL_MULIS_SERVER_PATH_GLOBAL_BASE = "global";
    public static String GLOBAL_MULIS_SERVER_PATH_AGENT = "agent";
    public static String GLOBAL_MULIS_SERVER_PATH_BASE = "base";
    public static String GLOBAL_MULIS_SERVER_PATH_FULL_BASE = "fbase";
    public static String GLOBAL_MULIS_SERVER_PATH_SETTINGS = "settings";
    public static String GLOBAL_MULIS_SERVER_PATH_FILE_UPLOAD = "file";
    public static final String USER_HOME_LOCATION = System.getProperty("user.home");
    public static String USER_DIR = ".mulis";
    public static final String CONFILE = "global.xml";
    public static final String FAVORITE_HASH_FILE = "favorite.dat";
    public static final String PFIELDS_HASH_FILE = "pfields.dat";
    public static final String PRINT_FILE = "print.html";
    public static final String PRINT_FILE_TABLE = "print_table.html";
    public static boolean PRINT_EXTERNAL = false;
    public static String PRINT_LOCATION = "tmpl/";
    private static String JAR_LOCATION="./";
    public static String LOCATION = "./";
    public static String DB_DIR = "db/";
    public static String BK_PATH = GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/backup/";
    public static String XML_DIR = "db_xml/";
    public static String IMG_DIR = "db_img/";
    public static String AGT_DIR = "db_agt/";
    public static String CFG_DIR = "db_cfg/";
    public static String TMP_DIR = "temp/";
    public static String IMG_EXT = "jpg";
    public static String DB_NAME = ".db";
    public static String DB_PREFIX_GLOBAL = "gl_";
    public static String FLD_;
    public static int DEBUG = 0;
    public static boolean IS_BACKUP_BASE = true;
    public static boolean F_DEBUG = false;
    public static boolean F_CONSOLE = false;
    public static boolean GENERATOR = true;
    public static boolean GENERATOR_XML = false;
    public static boolean REDIRECT_OUT_ERR = true;
    public static int ADD_LIMIT = 4;
    public static int FIRST_LOAD = 50;
    public static double PRINT_LONG_COEF = 8.7;
    public static String AGENT_NAME = "";
    public static String AGENT_INN = "0";
    
    public static String MASTER_INN_GLOBAL = "";
    public static String MASTER_PWD_MD5 = "";
    
    public static String MASTER_NAME = "";
    public static String MASTER_INFO = "";
    public static String MASTER_EMAIL = "";
    public static boolean MASTER_EMAIL_HIDE = true;
    public static String MASTER_SITE = "";
    
    public static String CLIENT_SERVER_GATE = "";
    public static String CLIENT_SERVER_GATE_FILE = "gate.php";
    public static String CLIENT_SERVER_GATE_PATH = "db/base/";
    public static String CLIENT_SERVER_PWD = ""; // TODO -> MD5
    
    public final static String MASTER_INN = ""; //internal
    
    public static String SMTP_SERVER="gmail.com";
    public static String SMTP_LOGIN="";
    public static String SMTP_PWD="";
    public static String SMTP_FROM="";
    
    public static int THUMBINAL_WIDTH = 750;
    public static int THUMBINAL_HEIGHT = 550;
    public static int DEF_COL_SIZE = 40;
    public static int DEF_MIN_COL_SIZE = 30;
    public static int DEF_MAX_COL_SIZE = 100;
    public static int DEF_INC_COL = 3;
    public static String COLORIZED_ALIAS = "Статус сделки";
    public static String COLORIZED_NAME = "status_sd";
    public static String IDA_ALIAS = "ida";
    public static Color st1_TableColor = new Color(255, 255, 255);
    public static Color st2_TableColor = new Color(255, 255, 255);
    public static Color st3_TableColor = new Color(255, 255, 255);
    public static boolean STATUS_COLORIZED = false;
    public static boolean CHECK_UPDATE = true;
    public static Color oddTableColor = new Color(255, 255, 255);
    public static Color evenTableColor = new Color(245, 245, 245);
    public static Color selectedTableColor = new Color(51, 153, 255);
    public static Color fontTableColor = new Color(0, 0, 0);
    public static boolean IS_HALF_COLOR = true;
    public static boolean IS_DARK_ARCH = false;
    public static float coef_color_s = 0.09f;
    public static Font tableFont = new Font("Arial", Font.PLAIN, 11);
    public static Color fontViewColor = new Color(0, 0, 0);
    public static Font viewFont = new Font("Arial", Font.PLAIN, 11);
    public static String[] locationList = {"Остановка", "Ст. метро", "Автобус", "Тролллейбус", "Маршрутное такси"};
    public static List<CollectModel> LIST_MODEL = new ArrayList<CollectModel>();
    public static List<BeanUser> LIST_USERS = new ArrayList<BeanUser>();
    public static List<CollectAgents> LIST_AGENTS = new ArrayList<CollectAgents>();
    public static boolean IS_MASTER=false;
    public static int photoMiniSize=100;
    
    public static HashMap selectedRows = new HashMap();
    public static HashMap printedRows = new HashMap();
    
    public static String warring = "";
    
    public static List<String> version_update_sql = new ArrayList<String>();
    
    public static boolean isExcludeField(String field) {
        return field.equals("comment_private") || field.equals("idr");
    }
    
    public static int getUserIdListPosition(int userId) {
        try {
            int i=0;
            for (BeanUser user : LIST_USERS) {
                int tInt = Integer.parseInt(user.getIdr());
                i++;
                if (tInt==userId) { 
                    return i;
                }
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return 0;
    }
    
    public static int getMaxIdUsers() {
        int max=0;
        try {
            for (BeanUser user : LIST_USERS) {
                //if ())
                int tInt = Integer.parseInt(user.getIdr());
                if (tInt>max) { max=tInt;}
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return max;       
    }
    
    //public static SMTPServer smtpServer = null;
    //public static boolean LAUNCH_OK=false;
    /*public static String[] STR_FIELDS_ARR (){
    return STD_FILEDS.split(",");
    }*/

    
    public static Color getStatusColor(int status) {
        switch (status) {
            case 0:
                return st1_TableColor;
            case 1:
                return st2_TableColor;
            case 2:
                return st3_TableColor;
            default:
                return Color.WHITE;
        }
    }

    //public static String[] STR_FIELDS_ARR_ID_MOD = { "id", "ida", "idr", "state", "date_add", "date_mod" };
    public static void init() {
        new File(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR).mkdirs();
        if (!new File(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.CONFILE).exists()) {
            saveConfigToFile();
            Logger.put("User config generated!");
        }
        Util.loadHashMap(GlobalConfig.FAVORITE_HASH_FILE, GlobalConfig.selectedRows);
        Util.loadHashMap(GlobalConfig.PFIELDS_HASH_FILE, GlobalConfig.printedRows);
        loadConfigFromFile();
        new File(GlobalConfig.BK_PATH).mkdirs();
        new File(GlobalConfig.LOCATION + "/" + GlobalConfig.DB_DIR).mkdirs();
        new File(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR).mkdirs();
        new File(GlobalConfig.LOCATION + "/" + GlobalConfig.XML_DIR).mkdirs();
        new File(GlobalConfig.LOCATION + "/" + GlobalConfig.AGT_DIR).mkdirs();
        new File(GlobalConfig.LOCATION + "/" + GlobalConfig.CFG_DIR).mkdirs();
        new File(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.XML_DIR).mkdirs();
        new File(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.TMP_DIR).mkdirs();

        Logger.put("User home dir: " + USER_HOME_LOCATION + "\nCurrent dir: " + LOCATION + "\nDB:" + DB_DIR + "\nIMG:" + IMG_DIR + "\nXML:" + XML_DIR + "\nTMP:" + TMP_DIR);
       
        //UtilMail.startSMPTServer();
    }

    public static boolean regenerateUserAgentLists() {
        try {
            LIST_USERS.clear();
            LIST_USERS.addAll(UtilXML.XMLToUsers(GlobalConfig.LOCATION + "/" + GlobalConfig.CFG_DIR));

            LIST_AGENTS.clear();
            LIST_AGENTS.add(new CollectAgents(GlobalConfig.MASTER_INN, GlobalConfig.MASTER_NAME, GlobalConfig.MASTER_EMAIL, GlobalConfig.MASTER_SITE, GlobalConfig.MASTER_INFO, LIST_USERS));
            LIST_AGENTS.addAll(UtilXML.XMLToAgents(GlobalConfig.LOCATION + "/" + GlobalConfig.AGT_DIR));
            return true;
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }
    
    public static boolean regenerateListModelLists() {
        try {
            LIST_MODEL.clear();
            //hashMapTypeFields.clear(); // TODO: Выбрать.
            LIST_MODEL.addAll(UtilXML.XMLToCollect(GlobalConfig.LOCATION + "/" + GlobalConfig.XML_DIR));
            return true;
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }

    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }
    
    public static String getJarLocation(){
        return JAR_LOCATION;
    }

    public GlobalConfig(CodeSource codeSource) {
        try {
            System.out.println("Debug level:" + DEBUG);
            Locale.setDefault(new Locale("ru", "RU"));
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("client.encoding.override", "UTF-8");
            System.setProperty("DSQLite.encoding", "UTF-8");


            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            LOCATION = jarFile.getParentFile().getPath();
            JAR_LOCATION = jarFile.getParentFile().getPath();
            init();

            //if (DEBUG > 0) {
                System.out.println("file.encoding=" + System.getProperty("file.encoding"));
                System.out.println("Default Charset=" + Charset.defaultCharset());
                System.out.println("Default Charset in Use=" + getDefaultCharSet());
            //}
                
            if (!Charset.defaultCharset().toString().equals("UTF-8")) {
                warring="<html><body><p><font color='red'>Возможны проблемы при печати объявлений!</font></p><p>Необходим запуск с ключем -Dfile.encoding=\"UTF-8\"</p></body></html>";
            }

            if (new File(GlobalConfig.LOCATION + "/" + GlobalConfig.PRINT_LOCATION).exists()) {
                GlobalConfig.PRINT_LOCATION = GlobalConfig.LOCATION + "/" + GlobalConfig.PRINT_LOCATION;
                PRINT_EXTERNAL = true;
            } else {
                GlobalConfig.PRINT_LOCATION = "";
            }
            
            Logger.put("Print location - " + GlobalConfig.PRINT_LOCATION);
            checkLocPatch();

            regenerateListModelLists();

            //UtilXML.usersToXML(LIST_USERS,false);
            //UtilXML.usersToXML(LIST_USERS,true);
            regenerateUserAgentLists();

            UtilXML.XMLToPostCollect(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.XML_DIR, GlobalConfig.LIST_MODEL);

            // TODO: 10101010101010
            Logger.put("Load XML - " + ((LIST_MODEL.size() < 10) ? "NO" : "OK"));

        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    /**
     * Load/AutoCreate config
     */
    public static synchronized void loadConfigFromFile() {
        loadConfigFromFile(USER_HOME_LOCATION + "/" + USER_DIR + "/" + CONFILE);
    }

    /**
     * Load/AutoCreate config
     * @param confile - Config file
     */
    public static synchronized void loadConfigFromFile(String confile) {
        try {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(confile);
            properties.loadFromXML(fis);

            STD_FILEDS = properties.getProperty("STD_FILEDS", STD_FILEDS);
            STD_USR_FILEDS = properties.getProperty("STD_USR_FILEDS", STD_USR_FILEDS);
            STD_FILEDS_CREATE = properties.getProperty("STD_FILEDS_CREATE", STD_FILEDS_CREATE);
            STD_FILEDS_IN = properties.getProperty("STD_FILEDS_IN", STD_FILEDS_IN);
            STD_FILEDS_C = Integer.parseInt(properties.getProperty("STD_FILEDS_C", Integer.toString(STD_FILEDS_C)));
            SHOW_STD_FIELD_ID = Boolean.parseBoolean(properties.getProperty("SHOW_STD_FIELD_ID", Boolean.toString(SHOW_STD_FIELD_ID)));
            SHOW_STD_FIELD_IDR = Boolean.parseBoolean(properties.getProperty("SHOW_STD_FIELD_IDR", Boolean.toString(SHOW_STD_FIELD_IDR)));
            SHOW_STD_FIELDS = Boolean.parseBoolean(properties.getProperty("SHOW_STD_FIELDS", Boolean.toString(SHOW_STD_FIELDS)));
            IS_BACKUP_BASE = Boolean.parseBoolean(properties.getProperty("IS_BACKUP_BASE", Boolean.toString(IS_BACKUP_BASE)));

            USER_DIR = properties.getProperty("USER_DIR", USER_DIR);
            LOCATION = properties.getProperty("LOCATION", LOCATION);
            DB_DIR = properties.getProperty("DB_DIR", DB_DIR);
            BK_PATH = properties.getProperty("BK_PATH", BK_PATH);
            IMG_DIR = properties.getProperty("IMG_DIR", IMG_DIR);
            XML_DIR = properties.getProperty("XML_DIR", XML_DIR);
            TMP_DIR = properties.getProperty("TMP_DIR", TMP_DIR);
            AGT_DIR = properties.getProperty("AGT_DIR", AGT_DIR);
            CFG_DIR = properties.getProperty("CFG_DIR", CFG_DIR);

            IMG_EXT = properties.getProperty("IMG_EXT", IMG_EXT);
            DB_NAME = properties.getProperty("DB_NAME", DB_NAME);

            if (!F_DEBUG) { DEBUG = Integer.parseInt(properties.getProperty("DEBUG", Integer.toString(DEBUG))); }
            GENERATOR = Boolean.parseBoolean(properties.getProperty("GENERATOR", Boolean.toString(GENERATOR)));
            REDIRECT_OUT_ERR = Boolean.parseBoolean(properties.getProperty("REDIRECT_OUT_ERR", Boolean.toString(REDIRECT_OUT_ERR)));
            ADD_LIMIT = Integer.parseInt(properties.getProperty("ADD_LIMIT", Integer.toString(ADD_LIMIT)));
            FIRST_LOAD = Integer.parseInt(properties.getProperty("FIRST_LOAD", Integer.toString(FIRST_LOAD)));
            PRINT_LONG_COEF = Double.parseDouble(properties.getProperty("PRINT_LONG_COEF", Double.toString(PRINT_LONG_COEF)));

            AGENT_NAME = properties.getProperty("AGENT_NAME", AGENT_NAME);
            AGENT_INN = properties.getProperty("AGENT_INN", AGENT_INN);
            MASTER_NAME = properties.getProperty("MASTER_NAME", MASTER_NAME);
            MASTER_INFO = properties.getProperty("MASTER_INFO", MASTER_INFO);
            MASTER_EMAIL = properties.getProperty("MASTER_EMAIL", MASTER_EMAIL);
            MASTER_EMAIL_HIDE = Boolean.parseBoolean(properties.getProperty("MASTER_EMAIL_HIDE", Boolean.toString(MASTER_EMAIL_HIDE)));
            MASTER_SITE = properties.getProperty("MASTER_SITE", MASTER_SITE);
            
            GLOBAL_MULIS_SERVER = properties.getProperty("GLOBAL_MULIS_SERVER", GLOBAL_MULIS_SERVER);
            GLOBAL_MULIS_SERVER_PORT = properties.getProperty("GLOBAL_MULIS_SERVER_PORT", GLOBAL_MULIS_SERVER_PORT);

            // TODO: не безопасный параметр
            //MASTER_INN = properties.getProperty("MASTER_INN", MASTER_INN);
            MASTER_INN_GLOBAL = properties.getProperty("MASTER_INN_GLOBAL", MASTER_INN_GLOBAL);
            
            MASTER_PWD_MD5 = properties.getProperty("MASTER_PWD_MD5", MASTER_PWD_MD5);
            
            CLIENT_SERVER_GATE = properties.getProperty("CLIENT_SERVER_GATE", CLIENT_SERVER_GATE);
            CLIENT_SERVER_PWD = properties.getProperty("CLIENT_SERVER_PWD", CLIENT_SERVER_PWD);

            THUMBINAL_WIDTH = Integer.parseInt(properties.getProperty("THUMBINAL_WIDTH", Integer.toString(THUMBINAL_WIDTH)));
            THUMBINAL_HEIGHT = Integer.parseInt(properties.getProperty("THUMBINAL_HEIGHT", Integer.toString(THUMBINAL_HEIGHT)));
            DEF_COL_SIZE = Integer.parseInt(properties.getProperty("DEF_COL_SIZE", Integer.toString(DEF_COL_SIZE)));
            DEF_MIN_COL_SIZE = Integer.parseInt(properties.getProperty("DEF_MIN_COL_SIZE", Integer.toString(DEF_MIN_COL_SIZE)));
            DEF_MAX_COL_SIZE = Integer.parseInt(properties.getProperty("DEF_MAX_COL_SIZE", Integer.toString(DEF_MAX_COL_SIZE)));
            DEF_INC_COL = Integer.parseInt(properties.getProperty("DEF_INC_COL", Integer.toString(DEF_INC_COL)));

            COLORIZED_ALIAS = properties.getProperty("COLORIZED_ALIAS", COLORIZED_ALIAS);
            COLORIZED_NAME = properties.getProperty("COLORIZED_NAME", COLORIZED_NAME);

            st1_TableColor = new Color(Integer.parseInt(properties.getProperty("st1_TableColor", "" + st1_TableColor.getRGB())));
            st2_TableColor = new Color(Integer.parseInt(properties.getProperty("st2_TableColor", "" + st2_TableColor.getRGB())));
            st3_TableColor = new Color(Integer.parseInt(properties.getProperty("st3_TableColor", "" + st3_TableColor.getRGB())));
            STATUS_COLORIZED = Boolean.parseBoolean(properties.getProperty("STATUS_COLORIZED", Boolean.toString(STATUS_COLORIZED)));
            IS_HALF_COLOR = Boolean.parseBoolean(properties.getProperty("IS_HALF_COLOR", Boolean.toString(IS_HALF_COLOR)));
            IS_DARK_ARCH = Boolean.parseBoolean(properties.getProperty("IS_DARK_ARCH", Boolean.toString(IS_DARK_ARCH)));
            CHECK_UPDATE = Boolean.parseBoolean(properties.getProperty("CHECK_UPDATE", Boolean.toString(CHECK_UPDATE)));

            oddTableColor = new Color(Integer.parseInt(properties.getProperty("oddTableColor", "" + oddTableColor.getRGB())));
            evenTableColor = new Color(Integer.parseInt(properties.getProperty("evenTableColor", "" + evenTableColor.getRGB())));
            selectedTableColor = new Color(Integer.parseInt(properties.getProperty("selectedTableColor", "" + selectedTableColor.getRGB())));
            fontTableColor = new Color(Integer.parseInt(properties.getProperty("fontTableColor", "" + fontTableColor.getRGB())));
            fontViewColor = new Color(Integer.parseInt(properties.getProperty("fontViewColor", "" + fontViewColor.getRGB())));

            tableFont = new Font(properties.getProperty("tableFontName", tableFont.getFontName()), Integer.parseInt(properties.getProperty("tableFontType", tableFont.getStyle() + "")), Integer.parseInt(properties.getProperty("tableFontSize", tableFont.getSize() + "")));
            viewFont = new Font(properties.getProperty("viewFontName", viewFont.getFontName()), Integer.parseInt(properties.getProperty("viewFontType", viewFont.getStyle() + "")), Integer.parseInt(properties.getProperty("viewFontSize", viewFont.getSize() + "")));

            fis.close();
            Logger.put("User config load - OK");
        } catch (Exception ex) {
            Logger.put("User config load - NO");
            Logger.put(ex);
        }
    }

    /**
     * Save config
     */
    public static synchronized void saveConfigToFile() {
        saveConfigToFile(USER_HOME_LOCATION + "/" + USER_DIR + "/" + CONFILE);
    }

    /**
     * Save config
     * @param confile - Config file
     */
    public static synchronized void saveConfigToFile(String confile) {
        try {
            Properties properties = new Properties();

            properties.setProperty("STD_FILEDS", STD_FILEDS);
            properties.setProperty("STD_USR_FILEDS", STD_USR_FILEDS);
            properties.setProperty("STD_FILEDS_CREATE", STD_FILEDS_CREATE);
            properties.setProperty("STD_FILEDS_IN", STD_FILEDS_IN);
            properties.setProperty("STD_FILEDS_C", Integer.toString(STD_FILEDS_C));
            properties.setProperty("SHOW_STD_FIELD_ID", Boolean.toString(SHOW_STD_FIELD_ID));
            properties.setProperty("SHOW_STD_FIELD_IDR", Boolean.toString(SHOW_STD_FIELD_IDR));
            properties.setProperty("SHOW_STD_FIELDS", Boolean.toString(SHOW_STD_FIELDS));
            properties.setProperty("IS_BACKUP_BASE", Boolean.toString(IS_BACKUP_BASE));

            properties.setProperty("USER_DIR", USER_DIR);
            properties.setProperty("LOCATION", LOCATION);
            properties.setProperty("DB_DIR", DB_DIR);
            properties.setProperty("BK_PATH", BK_PATH);
            properties.setProperty("XML_DIR", XML_DIR);
            properties.setProperty("IMG_DIR", IMG_DIR);
            properties.setProperty("TMP_DIR", TMP_DIR);
            properties.setProperty("AGT_DIR", AGT_DIR);
            properties.setProperty("CFG_DIR", CFG_DIR);

            properties.setProperty("IMG_EXT", IMG_EXT);
            properties.setProperty("DB_NAME", DB_NAME);

            properties.setProperty("DEBUG", Integer.toString(DEBUG));
            properties.setProperty("GENERATOR", Boolean.toString(GENERATOR));
            properties.setProperty("REDIRECT_OUT_ERR", Boolean.toString(REDIRECT_OUT_ERR));
            properties.setProperty("ADD_LIMIT", Integer.toString(ADD_LIMIT));
            properties.setProperty("FIRST_LOAD", Integer.toString(FIRST_LOAD));
            properties.setProperty("PRINT_LONG_COEF", Double.toString(PRINT_LONG_COEF));

            properties.setProperty("AGENT_NAME", AGENT_NAME);
            properties.setProperty("AGENT_INN", AGENT_INN);
            properties.setProperty("MASTER_NAME", MASTER_NAME);
            properties.setProperty("MASTER_SITE", MASTER_SITE);
            properties.setProperty("MASTER_INFO", MASTER_INFO);
            properties.setProperty("MASTER_EMAIL", MASTER_EMAIL);
            properties.setProperty("MASTER_EMAIL_HIDE", Boolean.toString(MASTER_EMAIL_HIDE));
            
            properties.setProperty("GLOBAL_MULIS_SERVER", GLOBAL_MULIS_SERVER);
            properties.setProperty("GLOBAL_MULIS_SERVER_PORT", GLOBAL_MULIS_SERVER_PORT);
            
            //properties.setProperty("MASTER_INN", MASTER_INN);
            properties.setProperty("MASTER_INN_GLOBAL", MASTER_INN_GLOBAL);
            
            properties.setProperty("MASTER_PWD_MD5", MASTER_PWD_MD5);
            
            properties.setProperty("CLIENT_SERVER_GATE", CLIENT_SERVER_GATE);
            properties.setProperty("CLIENT_SERVER_PWD", CLIENT_SERVER_PWD);

            properties.setProperty("THUMBINAL_WIDTH", Integer.toString(THUMBINAL_WIDTH));
            properties.setProperty("THUMBINAL_HEIGHT", Integer.toString(THUMBINAL_HEIGHT));
            properties.setProperty("DEF_COL_SIZE", Integer.toString(DEF_COL_SIZE));
            properties.setProperty("DEF_MIN_COL_SIZE", Integer.toString(DEF_MIN_COL_SIZE));
            properties.setProperty("DEF_MAX_COL_SIZE", Integer.toString(DEF_MAX_COL_SIZE));
            properties.setProperty("DEF_INC_COL", Integer.toString(DEF_INC_COL));

            properties.setProperty("COLORIZED_ALIAS", COLORIZED_ALIAS);
            properties.setProperty("COLORIZED_NAME", COLORIZED_NAME);

            properties.setProperty("STATUS_COLORIZED", Boolean.toString(STATUS_COLORIZED));
            properties.setProperty("IS_HALF_COLOR", Boolean.toString(IS_HALF_COLOR));
            properties.setProperty("IS_DARK_ARCH", Boolean.toString(IS_DARK_ARCH));
            properties.setProperty("CHECK_UPDATE", Boolean.toString(CHECK_UPDATE));

            properties.setProperty("st1_TableColor", st1_TableColor.getRGB() + "");
            properties.setProperty("st2_TableColor", st2_TableColor.getRGB() + "");
            properties.setProperty("st3_TableColor", st3_TableColor.getRGB() + "");

            properties.setProperty("oddTableColor", oddTableColor.getRGB() + "");
            properties.setProperty("evenTableColor", evenTableColor.getRGB() + "");
            properties.setProperty("selectedTableColor", selectedTableColor.getRGB() + "");
            properties.setProperty("fontTableColor", fontTableColor.getRGB() + "");
            properties.setProperty("fontViewColor", fontViewColor.getRGB() + "");

            //properties.setProperty("tableFont", tableFont.toString());
            properties.setProperty("tableFontName", tableFont.getFontName());
            properties.setProperty("tableFontType", tableFont.getStyle() + "");
            properties.setProperty("tableFontSize", tableFont.getSize() + "");

            //properties.setProperty("viewFont", viewFont.toString());
            properties.setProperty("viewFontName", viewFont.getFontName());
            properties.setProperty("viewFontType", viewFont.getStyle() + "");
            properties.setProperty("viewFontSize", viewFont.getSize() + "");

            FileOutputStream fos = new FileOutputStream(confile);
            properties.storeToXML(fos, "ConfigFile");
            fos.close();
           
            Logger.put("User config saved - OK");
            
        } catch (Exception ex) {
            Logger.put("User config saved - NO!");
            Logger.put(ex);
        }
    }

    public static String checkUpdate() {
        try {
            if (!CHECK_UPDATE) {
                return "";
            }
            URL url = new URL("http://"+GLOBAL_MULIS_HOST+"/"+GLOBAL_MULIS_HOST_UPDATE);
            URLConnection urlc = url.openConnection();
            BufferedInputStream buffer = new BufferedInputStream(urlc.getInputStream());
            StringBuilder builder = new StringBuilder();
            int byteRead;
            while ((byteRead = buffer.read()) != -1) {
                builder.append((char) byteRead);
            }

            buffer.close();
            String find = builder.toString().trim();
            boolean test = find.equals(VERSION);
            //find=(test)?"No update {"+VERSION+"}":"Need update! {"+VERSION+"->"+find+"}";
            Logger.put("Проверка обновлений ["+VERSION+"->"+find+"]");
            return (test) ? "" : find;
        } catch (Exception ex) {
            Logger.put("Ошибка при проверке обновлений");
            return "";
        }
    }
    
    public static void generateListModelCurrentTable() {
        core.generateListModelCurrentTable();
    }

    public static IFrameCore getCore() {
        return core;
    }

    public static void setCore(IFrameCore core) {
        GlobalConfig.core = core;
    }
    
    public static void checkLocPatch() {
        try {
            new File(GlobalConfig.LOCATION).mkdirs();
            if (!new File(GlobalConfig.LOCATION).exists() && !new File(GlobalConfig.LOCATION).canRead() && !new File(GlobalConfig.LOCATION).canWrite()) {
                INI=false;
            } else {
                INI=true;
            }
        } catch (Exception ex) {
            INI=false;
        }
    }
}