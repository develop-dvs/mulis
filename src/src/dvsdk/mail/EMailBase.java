package dvsdk.mail;

import dvsdk.Logger;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.swing.JComboBox;

/**
 *
 * @author develop-dvs
 */
public class EMailBase {
    private Vector<EMailBin> EBASE = new Vector<EMailBin>();

    public EMailBase(String file_base) {
        importBase(file_base);
    }
    public EMailBase() {this("");}

    private void importBase(String file_base)  {
        try
        {
            //if (GlobalConfig.EMAIL_FILE_BASE.isEmpty()) return;
            InputStreamReader ir = new InputStreamReader(new FileInputStream(file_base),"UTF-8");
            BufferedReader br = new BufferedReader(ir);
            String ln = "";
            String tmpLn[] = null;
            int n=0;
            while ((ln = br.readLine())!=null)
            {
                tmpLn = ln.split(",");
                if (tmpLn.length>4) {EBASE.add(new EMailBin(tmpLn[2], tmpLn[4])); }
            }
            //MainLog.add("Инициализация: "+"Найдено ["+n+"] из 8 пунктов настроек!");
        //JOptionPane.showMessageDialog(this, "Найдено ["+n+"] записей", "Настройки", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {Logger.put(ex); /*MainLog.add("ОШИБКА! (Инициализация): "+ex.getMessage()); /*JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);*/}
    }
    private EMailBin getElement(int num){
        return (num>=0)?EBASE.get(num):EBASE.get(0);
    }
    public int size() {return EBASE.size();}
    public String getName(int num) {return getElement(num).NAME.trim();}
    public String getEmail(int num) {return getElement(num).EMAIL.trim();}
    public String getNameEmail(int num, String sep) {return getElement(num).NAME.trim()+sep+getElement(num).EMAIL.trim();}
    public String getEmailName(int num, String sep) {return getElement(num).EMAIL.trim()+sep+getElement(num).NAME.trim();}

    public static void fillFastMail(JComboBox comboBox, EMailBase base, String sep, boolean  fim) {
    comboBox.removeAllItems();
    for (int i=0;i<base.size(); i++) {
        comboBox.addItem((fim)?base.getNameEmail(i, sep):base.getEmailName(i, sep));
    }
}

}
