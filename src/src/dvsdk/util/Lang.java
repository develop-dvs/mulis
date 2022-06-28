package dvsdk.util;

import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.client.BeanModel;
import dvsdk.client.CollectAgents;
import dvsdk.client.CollectModel;
import dvsdk.client.MulisComboBoxModel;
import dvsdk.client.MulisComboBoxModelUsers;
import dvsdk.db.BeanUser;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.mozilla.universalchardet.UniversalDetector;


/**
 *
 * @author develop-dvs
 */
public class Lang {
    public static final String STD_FIELD_IDR="Агент";
    
    // TODO: Lang...
    public static final String ERROR_BD_PATH_MSG = "База данных не доступна для чтения/записи!\nПроверьте настройки сети и прав доступа\nВозможно отключен сетевой диск\nИли укажите другой путь";
    
    public static JFileChooser setUpdateUI(JFileChooser choose) {
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.lookInLabelText", "Смотреть в");
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файла");

        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");

        UIManager.put("FileChooser.lookInLabelText", "Папка");
        UIManager.put("FileChooser.saveInLabelText", "Папка");
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файлов");

        UIManager.put("FileChooser.upFolderToolTipText", "На один уровень вверх");
        UIManager.put("FileChooser.newFolderToolTipText", "Создание новой папки");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
        UIManager.put("FileChooser.fileNameHeaderText", "Имя");
        UIManager.put("FileChooser.fileSizeHeaderText", "Размер");
        UIManager.put("FileChooser.fileTypeHeaderText", "Тип");
        UIManager.put("FileChooser.fileDateHeaderText", "Изменен");
        UIManager.put("FileChooser.fileAttrHeaderText", "Атрибуты");

        UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
        
        UIManager.put("FileChooser.homeFolderToolTipText", "Домой");
        UIManager.put("FileChooser.viewMenuLabelText", "Настроить вид");
        UIManager.put("FileChooser.listViewActionLabelText", "Список");
        UIManager.put("FileChooser.detailsViewActionLabelText", "Подробно");
        UIManager.put("FileChooser.refreshActionLabelText", "Обновить");
        UIManager.put("FileChooser.newFolderActionLabelText", "Новая папка");
        
        choose.updateUI();

        return choose;
    }
    
     public static void fillComponentByName(JComponent component){
        String name=component.getName().replace('*', ' ').trim();
        
        for (Iterator<CollectModel> it = GlobalConfig.LIST_MODEL.iterator(); it.hasNext();) {
            CollectModel collectModel = it.next();
            if (name.endsWith(collectModel.getName())) {
                component.getAccessibleContext().setAccessibleDescription(collectModel.getDesc());
            }
                //component.getAccessibleContext().setAccessibleName(collectModel.getAlias());
        }
    }
    
    public static void fillComboBoxByName(JComboBox comboBox){
        String name=comboBox.getName().replace('*', ' ').trim();
        if (name.equals("idr")) {
            comboBox.setModel(new MulisComboBoxModelUsers(GlobalConfig.LIST_USERS));
            return;
        }
        for (Iterator<CollectModel> it = GlobalConfig.LIST_MODEL.iterator(); it.hasNext();) {
            CollectModel collectModel = it.next();
            if (name.endsWith(collectModel.getName())) {
                comboBox.setModel(new MulisComboBoxModel(collectModel.getCollect()));
            }
        }
    }
    public static String findByName(String name, String value) {
        return findByName(name, value, true);
    }
    public static String findByName(String name, String value, boolean eq) {
        for (Iterator<CollectModel> it = GlobalConfig.LIST_MODEL.iterator(); it.hasNext();) {
            CollectModel collectModel = it.next();
            if (name.endsWith(collectModel.getName())) {
                if (collectModel.getName().equals("idr")) { 
                    return findUserNameByIDR(value,GlobalConfig.LIST_USERS);
                } 
                //else if (collectModel.getName().equals("a_phone")) return findUserContactByINN(value); 
                else {
                    return findByName(collectModel.getCollect(), value, eq);
                }
            }
        }
        return value;
    }
    
    // TODO: REFACTORING
    public static String findAgentNameByIDA(String ida, String idr) {
        for (Iterator<CollectAgents> it = GlobalConfig.LIST_AGENTS.iterator(); it.hasNext();) {
            CollectAgents collectAgents = it.next();
            if (collectAgents.getIda().equals(ida)) {
                return findUserNameByIDR(idr, collectAgents.getAgents());
            }
        }
        return "";
    }
    // TODO: REFACTORING
    public static String findAgentContactByIDA(String ida, String idr) {
        for (Iterator<CollectAgents> it = GlobalConfig.LIST_AGENTS.iterator(); it.hasNext();) {
            CollectAgents collectAgents = it.next();
            if (collectAgents.getIda().equals(ida)) {
                return findUserContactByIDR(idr, collectAgents.getAgents());
            }
        }
        return "";
    }
    // TODO: REFACTORING
    public static String findWWWByIDA(String ida) {
        for (Iterator<CollectAgents> it = GlobalConfig.LIST_AGENTS.iterator(); it.hasNext();) {
            CollectAgents collectAgents = it.next();
            if (collectAgents.getIda().equals(ida)) {
                return collectAgents.getSite();
            }
        }
        return "";
    }
    
    // TODO: REFACTORING
    public static String findNameAgentstvoByIDA(String ida) {
        for (Iterator<CollectAgents> it = GlobalConfig.LIST_AGENTS.iterator(); it.hasNext();) {
            CollectAgents collectAgents = it.next();
            if (collectAgents.getIda().equals(ida)) {
                return collectAgents.getName();
            }
        }
        return "";
    }
    
    public static BeanUser findUserByIDR(String idr, List<BeanUser> list) {
        for (Iterator<BeanUser> it = list.iterator(); it.hasNext();) {
            BeanUser binUser = it.next();
            if (binUser.getIdr().equals(idr)) {
                return binUser;
            }
        }
        return null;
    }
    
    public static String findUserNameByIDR(String idr, List<BeanUser> list) {
        BeanUser binUser = findUserByIDR(idr, list);
        return (binUser!=null)?binUser.getName():"";
    }
    
    public static String findUserContactByIDR(String idr, List<BeanUser> list) {
        BeanUser binUser = findUserByIDR(idr, list);
        return (binUser!=null)?binUser.getContact():"";
    }
    
    public static String findUserContactByIDA(String idr) {
        for (Iterator<BeanUser> it = GlobalConfig.LIST_USERS.iterator(); it.hasNext();) {
            BeanUser binUser = it.next();
            if (binUser.getIdr().equals(idr)) {
                return binUser.getContact();
            }
        }
        return "";
    }
    
    public static String findAliasByName(String name) {
        for (Iterator<CollectModel> it = GlobalConfig.LIST_MODEL.iterator(); it.hasNext();) {
            CollectModel collectModel = it.next();
            if (name.endsWith(collectModel.getName())) {
                return collectModel.getAlias();
            }
        }
        return name;
    }
    
    public static String findTypeByName(String name) {
        for (Iterator<CollectModel> it = GlobalConfig.LIST_MODEL.iterator(); it.hasNext();) {
            CollectModel collectModel = it.next();
            if (name.endsWith(collectModel.getName())) {
                return collectModel.getType();
            }
        }
        return name;
    }
    
    public static String findByName(List<BeanModel> list, String value) {
        return findByName(list, value, true);
    }
    
    public static String findByName(List<BeanModel> list, String value, boolean eq) {
        String ret="";
        if (list.isEmpty() && eq) {
            return value;
        }
        for (Iterator<BeanModel> it = list.iterator(); it.hasNext();) {
            BeanModel binModel = it.next();
            if (eq) {
                if (binModel.getId().equals(value)) {
                    return binModel.getName();
                }
            } else {
                String[] val=value.split(" ");
                for (String string : val) {
                    if (binModel.getName().toLowerCase().contains(string.toLowerCase())) {
                        ret+="\""+binModel.getId()+"\",";
                    }
                }
            }
        }
        return (ret.isEmpty())?"":ret+"\"99999\"";
    }
    
    public static void setTextForm(JTextField textField, String header, String ext, String text) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(header);
        fileChooser.setFileFilter(new FileNameExtensionFilter(text, ext));
        if (fileChooser.showOpenDialog(fileChooser)==JFileChooser.APPROVE_OPTION)
        {
            textField.setText(fileChooser.getSelectedFile().getAbsolutePath().toString());
        }
    }

    public static String setTextButton(JButton jButton, String prefix, String header, String ext, String text) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(header);
        fileChooser.setFileFilter(new FileNameExtensionFilter(text, ext));
        if (fileChooser.showOpenDialog(fileChooser)==JFileChooser.APPROVE_OPTION)
        {
            jButton.setText(prefix+fileChooser.getSelectedFile().getName().toString());
            return fileChooser.getSelectedFile().getAbsolutePath().toString();
        }
        return "";
    }
    
    public static void setTextForm(JTextField textField, String header){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle(header);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION)
        {
            textField.setText(fileChooser.getSelectedFile().getAbsolutePath().toString());
        }
    }
    
    /**
     *
     * @param inc - RUS(UTF-8) to Latin
     * @return translite str
     */
    public static String toTranslite(String inc)
    {
        String tmp="";
        try {
            char[] str = new String(inc.getBytes("UTF-8"), "UTF-8").toLowerCase().toCharArray();
            for (int i=0;i<str.length;i++)
            {
                switch(str[i])
                {
                    case 'а': tmp+="a"; break;
                    case 'б': tmp+="b"; break;
                    case 'в': tmp+="v"; break;
                    case 'г': tmp+="g"; break;
                    case 'д': tmp+="d"; break;
                    case 'е': tmp+="e"; break;
                    case 'ё': tmp+="jo"; break;
                    case 'ж': tmp+="zh"; break;
                    case 'з': tmp+="z"; break;
                    case 'и': tmp+="i"; break;
                    case 'й': tmp+="jj"; break;
                    case 'к': tmp+="k"; break;
                    case 'л': tmp+="l"; break;
                    case 'м': tmp+="m"; break;
                    case 'н': tmp+="n"; break;
                    case 'о': tmp+="o"; break;
                    case 'п': tmp+="p"; break;
                    case 'р': tmp+="r"; break;
                    case 'с': tmp+="s"; break;
                    case 'т': tmp+="t"; break;
                    case 'у': tmp+="u"; break;
                    case 'ф': tmp+="f"; break;
                    case 'х': tmp+="kh"; break;
                    case 'ц': tmp+="c"; break;
                    case 'ч': tmp+="ch"; break;
                    case 'ш': tmp+="sh"; break;
                    case 'щ': tmp+="sch"; break;
                    case 'ъ': tmp+=""; break;
                    case 'ы': tmp+="y"; break;
                    case 'ь': tmp+="'"; break;
                    case 'э': tmp+="eh"; break;
                    case 'ю': tmp+="yu"; break;
                    case 'я': tmp+="ya"; break;
                    default: tmp+=str[i];
                }
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return tmp;
    }

    /**
     *
     * @param inc - Latin to RUS(UTF-8)
     * @return ruslite str
     */
    @SuppressWarnings("empty-statement")
    public static String toRuslite(String inc)
    {
        String tmp="";
        try {
            char[] str = new String(inc.getBytes("UTF-8"), "UTF-8").toLowerCase().toCharArray();
            for (int i=0;i<str.length;i++)
            {
                if (i<(str.length-2))
                {
                    if (str[i]=='s' && str[i+1]=='c' && str[i+2]=='h') {tmp+="щ"; i+=2; continue;};
                };
                if (i<(str.length-1))
                {
                    if (str[i]=='j' && str[i+1]=='o') {tmp+="ё"; i++; continue;} else
                    if (str[i]=='j' && str[i+1]=='j') {tmp+="й"; i++; continue;} else
                    if (str[i]=='z' && str[i+1]=='h') {tmp+="ж"; i++; continue;} else
                    if (str[i]=='k' && str[i+1]=='h') {tmp+="х"; i++; continue;} else
                    if (str[i]=='c' && str[i+1]=='h') {tmp+="ч"; i++; continue;} else
                    if (str[i]=='s' && str[i+1]=='h') {tmp+="ш"; i++; continue;} else
                    if (str[i]=='e' && str[i+1]=='h') {tmp+="ш"; i++; continue;} else
                    if (str[i]=='y' && str[i+1]=='u') {tmp+="я"; i++; continue;} else
                    if (str[i]=='y' && str[i+1]=='a') {tmp+="ю"; i++; continue;}
                };
                if (i<str.length)
                {
                    if (str[i]=='a') tmp+="а"; else
                    if (str[i]=='b') tmp+="б"; else
                    if (str[i]=='v') tmp+="в"; else
                    if (str[i]=='g') tmp+="г"; else
                    if (str[i]=='d') tmp+="д"; else
                    if (str[i]=='e') tmp+="е"; else
                    if (str[i]=='z') tmp+="з"; else
                    if (str[i]=='i') tmp+="и"; else
                    if (str[i]=='k') tmp+="к"; else
                    if (str[i]=='l') tmp+="л"; else
                    if (str[i]=='m') tmp+="м"; else
                    if (str[i]=='n') tmp+="н"; else
                    if (str[i]=='o') tmp+="о"; else
                    if (str[i]=='p') tmp+="п"; else
                    if (str[i]=='r') tmp+="р"; else
                    if (str[i]=='s') tmp+="с"; else
                    if (str[i]=='t') tmp+="т"; else
                    if (str[i]=='u') tmp+="у"; else
                    if (str[i]=='f') tmp+="ф"; else
                    if (str[i]=='c') tmp+="ц"; else
                    if (str[i]=='y') tmp+="ы"; else
                    if (str[i]=='\'') tmp+="ь";
                    else tmp+=str[i];
                    //if (str[i]=='h') tmp+="х"; else
                    //if (str[i]=='x') tmp+="х";
                    //if (str[i]=='q') tmp+="п";
                    //if (str[i]=='w') tmp+="х";
                }
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return tmp;
    }
    
    public static String detectEncoding(String str){
        UniversalDetector detector = new UniversalDetector(null);
        try {
            
            InputStream is = new ByteArrayInputStream(str.getBytes());

            int nread; byte[] buf = new byte[4096];
            while ((nread=is.read(buf))>0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            String encoding = detector.getDetectedCharset();
            if (GlobalConfig.DEBUG > 1 )
                if (encoding != null) {
                  Logger.put("Detected encoding = " + encoding);
                } else {
                  Logger.put("No encoding detected.");
                }
            detector.reset();
            return ((encoding==null)?Charset.defaultCharset().toString():encoding);
        } catch (Exception ex) {
            Logger.put(ex);
            return Charset.defaultCharset().toString();
        }
        
    }
    
    public static String decodeErrorServerAnswer(Exception ex) {
        if (ex.getLocalizedMessage()==null) return "["+Util.getCurDate()+"] Ошибка";
        if (ex.getLocalizedMessage().contains("401")) {
            return "["+Util.getCurDate()+"] Неверный пароль";
        } else if (ex.getLocalizedMessage().contains("403")) {
            return "["+Util.getCurDate()+"] В данный раздел вам закрыт доступ";
        } else if (ex.getLocalizedMessage().contains("404")) {
            return "["+Util.getCurDate()+"] Сервер отключен\nНе переживайте, скоро он появится!";
        } else if (ex.getLocalizedMessage().contains("500") || ex.getLocalizedMessage().contains("501") || ex.getLocalizedMessage().contains("503")) {
            return "["+Util.getCurDate()+"] Сервер перегружен, повторите попытку через час"+((GlobalConfig.DEBUG>0)?"\n" + ex.getLocalizedMessage():"");
        } else if (ex.getLocalizedMessage().contains("Connection refused: connect")) {
            return "["+Util.getCurDate()+"] Подключение не установлено\nПроверьте настройки подключения к интернету"+((GlobalConfig.DEBUG>0)?"\n" + ex.getLocalizedMessage():"");
        }  else {
            return "["+Util.getCurDate()+"] Сервер сломался\nНе переживайте, скоро он починится!"+((GlobalConfig.DEBUG>0)?"\n" + ex.getLocalizedMessage():"");
        }
    }
    
}