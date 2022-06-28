package dvsdk.tmpl;

import dvsdk.client.BeanAddress;
import dvsdk.db.BeanBase;
import dvsdk.db.BeanParam;
import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.util.Lang;
import dvsdk.util.Util;
import dvsdk.util.UtilSec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
//import org.mozilla.intl.chardet.nsDetector;

/**
 *
 * @author develop-dvs
 */
public class MulisPrinter {

    private String tmpl;
    private String type;
    private List<BeanBase> list = new ArrayList<BeanBase>();
    //nsDetector det = null;
    public MulisPrinter(List<BeanBase> list, String type) {
        this.list = list;
        this.type = type;
    }

    public void build() {
        build(true, true, true, true, true, true, false, GlobalConfig.PRINT_FILE);
    }

    public void build(boolean map, boolean ad, boolean photo, boolean contact, boolean id, boolean date, boolean priv, int print_file) {
        switch (print_file) {
            case 0:
            default:
                build(map, ad, photo, contact, id, date, priv, GlobalConfig.PRINT_FILE_TABLE); break;
            case 1:
                build(map, ad, photo, contact, id, date, priv, GlobalConfig.PRINT_FILE); break;
        }
    }

    /**
     * Заполняем таблицу из строки
     * @param value "row|row|row~row|row|row~row|row|row"
     */
    public static String stringToHTMLTable(String value) {
        String result = "";
        String cols[] = value.split("~");
        for (String col : cols) {
            //Vector v = new Vector();
            String rows[] = col.split("\\|");
            if (rows.length == 3) {
                result += "<tr><td colspan=\"2\" class=\"divider\"></td></tr><tr>"
                        + "<td width=\"160\"><b>" + rows[0] + "</b></td>"
                        + "<td>" + rows[1] + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td colspan=\"2\"><small><em>" + rows[2] + "</em></small></td>"
                        + "</tr>";
            }
        }
        return result;
    }
    
    public String getCheckBox(String str) {
        if (str.equals("0") || str.equals("1"))
         return "<input type='checkbox' "+(str.equals("1")?"checked":"")+" />"; //disabled=disabled
        else return str;
    }

    public void build(boolean map, boolean ad, boolean photo, boolean contact, boolean id, boolean date, boolean priv, String print_file) {
        try { 
            if (list.isEmpty()) return;
            tmpl = Util.readTextFile(GlobalConfig.PRINT_LOCATION + print_file, GlobalConfig.PRINT_EXTERNAL, getClass());
            //tmpl = new String(tmpl.getBytes(Charset.defaultCharset()), "UTF-8");
            if ("".equals(tmpl)) {
                return;
            }
            String result_header = tmpl.substring(0, tmpl.indexOf("<body>"));
            String result_foother = tmpl.substring(tmpl.indexOf("</body>"), tmpl.length());
            String result_body = "";
            String result_table_head = "";
            int max=0;
            
            if (print_file.equals(GlobalConfig.PRINT_FILE_TABLE)) {
                result_table_head += "<table><thead><tr>";
                if (id) result_table_head += "<th>ID</th>";
                if (date) result_table_head += "<th>Дата</th>";
                for (Iterator<BeanParam> it1 = list.get(0).getParams().iterator(); it1.hasNext();) {
                    BeanParam binParam = it1.next();
                    if (Util.checkExistPFieldsKey(type,binParam.getName())) continue;
                    //binParam.setValue(new String(binParam.getValue().getBytes(Charset.defaultCharset()), "UTF-8"));
                    //System.out.println(Lang.detectEncoding(binParam.getValue()));
                    //binParam.setValue(new String(binParam.getValue().getBytes(Lang.detectEncoding(binParam.getValue())), "UTF-8"));
                    //binParam.setValue(new String(binParam.getValue().getBytes(), "UTF-8"));
                    if (binParam.getName().equals("photos") || binParam.getName().equals("comment") || binParam.getName().equals("comment_private") || binParam.getName().equals("loctab")) continue;
                    String ali = Lang.findAliasByName(binParam.getName());
                    max=((ali.length())>max)?ali.length():max;
                    boolean ln = Lang.findTypeByName(binParam.getName()).equals("1");
                    result_table_head += "<th" + ((ln) ? " class='long' " : "") + ">";
                    result_table_head += ((ln) ? "<div>" : "") + ali + ((ln) ? "</div>" : "")+"</th>";
                }
                if (contact) result_table_head += "<th>Агентство</th><th>Телефон</th>";
                if (priv) result_table_head += "<th>Общая информация</th>";
                if (priv) result_table_head += "<th>Приватная информация</th>";
                result_table_head ="<style>th.long { height:"+((max*GlobalConfig.PRINT_LONG_COEF))+"px !important; }</style>"+result_table_head+"</tr></thead><tbody>";
            }

            for (Iterator<BeanBase> it = list.iterator(); it.hasNext();) {
                BeanBase binBase = it.next();
                String address = "";
                String result = tmpl.substring(tmpl.indexOf("<body>"), tmpl.indexOf("</body>")).replaceAll("<body>", "").replaceAll("</body>", "").trim();
                String addInfo = "";
                List<BeanAddress> addresses = new ArrayList<BeanAddress>();
                
                if (print_file.equals(GlobalConfig.PRINT_FILE)) {
                    result = result.replaceAll("<!--id-->", binBase.getId());
                    result = result.replaceAll("<!--date_add-->", binBase.getDate_add());
                    result = result.replaceAll("<!--date_mod-->", binBase.getDate_mod());
                    if (contact) {
                        result = result.replaceAll("<!--a_name-->", Lang.findNameAgentstvoByIDA(binBase.getIda()));
                        result = result.replaceAll("<!--a_phone-->", Lang.findAgentContactByIDA(binBase.getIda(),binBase.getIdr()));
                    } else {
                        result = result.replaceAll("<!--contact_block-->(.*?)<!--contact_block_end-->", "");
                    }

                    for (Iterator<BeanParam> it1 = binBase.getParams().iterator(); it1.hasNext();) {
                        BeanParam binParam = it1.next();
                        
                        if (Util.checkExistPFieldsKey(type,binParam.getName())) continue;
                        //binParam.setValue(new String(binParam.getValue().getBytes(Lang.detectEncoding(binParam.getValue())), "UTF-8"));
                        //System.out.println(binParam.getValue());
                        //binParam.setValue(new String(binParam.getValue().getBytes(Charset.defaultCharset()), "cp1251"));
                        if (binParam.getName().equals("area")) {
                            addresses.add(new BeanAddress(0, Lang.findByName(binParam.getName(), binParam.getValue()) + ", "));
                        } else if (binParam.getName().equals("city")) {
                            addresses.add(new BeanAddress(1, Lang.findByName(binParam.getName(), binParam.getValue()) + ", "));
                        } else if (binParam.getName().equals("street")) {
                            addresses.add(new BeanAddress(2, Lang.findByName(binParam.getName(), binParam.getValue()) + ", "));
                        } else if (binParam.getName().equals("nhome")) {
                            addresses.add(new BeanAddress(3, binParam.getValue()));
                        } else if (binParam.getName().equals("loc")) {
                            result = result.replaceAll("<!--loc-->", Lang.findByName(binParam.getName(), binParam.getValue()));
                        } else if (binParam.getName().equals("room")) {
                            result = result.replaceAll("<!--room-->", binParam.getValue());
                            if (binParam.getValue().trim().isEmpty()) {
                                result = result.replaceAll("<!--room_block-->(.*?)<!--room_block_end-->", "");
                            }
                        } else if (binParam.getName().equals("s_all")) {
                            if (!binParam.getValue().trim().isEmpty()) {
                                result = result.replaceAll("<!--s_all-->", " " + binParam.getValue() + " m2");
                            }
                        } else if (binParam.getName().equals("s_live")) {
                            if (!binParam.getValue().trim().isEmpty()) {
                                result = result.replaceAll("<!--s_live-->", " | Жилая площадь: " + binParam.getValue() + " m2");
                            }
                        } else if (binParam.getName().equals("s_co")) {
                            if (!binParam.getValue().trim().isEmpty()) {
                                result = result.replaceAll("<!--s_co-->", " | Площадь кухни: " + binParam.getValue() + " m2");
                            }
                        } else if (binParam.getName().equals("s_uch")) {
                            if (!binParam.getValue().trim().isEmpty()) {
                                result = result.replaceAll("<!--s_uch-->", " | Площадь участка: " + binParam.getValue() + " m2");
                            }
                        } else if (binParam.getName().equals("floor")) {
                            result = result.replaceAll("<!--floor-->", binParam.getValue());
                        } else if (binParam.getName().equals("afloor")) {
                            result = result.replaceAll("<!--afloor-->", binParam.getValue());
                        } else if (binParam.getName().equals("comment")) {
                            
                            result = result.replaceAll("<!--comment-->", binParam.getValue());
                            if (binParam.getValue().trim().isEmpty()) {
                                result = result.replaceAll("<!--comment_block-->(.*?)<!--comment_block_end-->", "");
                            /*} else {
                                if (binParam.getValue()!=null ){
                                    //Lang.detectEncoding(binParam.getValue());
                                }*/
                            }
                        } else if (binParam.getName().equals("agent")) {
                            continue;
                            //result = result.replaceAll("<!--a_phone-->", Lang.findByName(binParam.getName(), binParam.getValue()));
                        } else if (binParam.getName().equals("price")) {
                            result = result.replaceAll("<!--price-->", binParam.getValue());
                        } else if (binParam.getName().equals("comment_private")) {
                                result = result.replaceAll("<!--comment_private-->", binParam.getValue());
                            if (binParam.getValue().trim().isEmpty()) {
                                result = result.replaceAll("<!--comment_private_block-->(.*?)<!--comment_private_block_end-->", "");
                            }
                        } else if (binParam.getName().equals("loctab")) {
                            if (!binParam.getValue().trim().isEmpty()) {
                                result = result.replaceAll("<!--loctab-->", stringToHTMLTable(binParam.getValue()));
                            } else {
                                result = result.replaceAll("<!--loctab_block-->(.*?)<!--loctab_block_end-->", "");
                            }
                        } else if (binParam.getName().equals("photos")) {
                            if (!binParam.getValue().equals("0") && !binParam.getValue().equals("") && photo) {
                                result = result.replaceAll("<!--photo-->", "file:///" + GlobalConfig.LOCATION.replace('\\', '/') + "/" + GlobalConfig.IMG_DIR + "/m" + GlobalConfig.MASTER_INN + "/t" + type + "/" + binBase.getId() + "/" + "0." + GlobalConfig.IMG_EXT);
                            } else {
                                result = result.replaceAll("<!--photo_block-->(.*?)<!--photo_block_end-->", "");
                            }
                        } else {
                            if (!ad) {
                                continue;
                            }
                            if (Util.checkExistPFieldsKey(type, binParam.getName())) continue;
                            String tmp = Lang.findByName(binParam.getName(), binParam.getValue());
                            tmp = tmp.equals("0") ? "Нет" : (tmp.equals("1") ? "Есть" : tmp);
                            addInfo += (!tmp.equals("")) ? "<li><strong>" + Lang.findAliasByName(binParam.getName()) + ": </strong> " + tmp + "</li>\n" : "\n";
                        }
                    }
                    Collections.sort(addresses);
                    for (Iterator<BeanAddress> it1 = addresses.iterator(); it1.hasNext();) {
                        BeanAddress binAddress = it1.next();
                        address += binAddress.getData();
                    }
                    result = result.replaceAll("<!--address-->", address);
                    if (map) {
                        result = result.replaceAll("<!--map_link-->", UtilSec.base64Encode(address));
                    } else {
                        result = result.replaceAll("<!--map_block-->(.*?)<!--map_block_end-->", "");
                    }

                    if (ad) {
                        result = result.replaceAll("<!--date_info-->", addInfo);
                    } else {
                        result = result.replaceAll("<!--add_block-->(.*?)<!--add_block_end-->", "");
                    }
                    //System.out.println(Util.base64Encode(address));

                } else if (print_file.equals(GlobalConfig.PRINT_FILE_TABLE)) {
                    result += "<tr>";
                    if (id) result+="<td>"+binBase.getId()+"</td>";
                    if (date) result+="<td>"+binBase.getDate_add()+"</td>";
                    //result+="<tr>"+binBase.getDate_mod()+"<tr>";
                    String comment = "";
                    String comment_private = "";
                    for (Iterator<BeanParam> it1 = binBase.getParams().iterator(); it1.hasNext();) {
                        BeanParam binParam = it1.next();
                        if (Util.checkExistPFieldsKey(type,binParam.getName())) continue;
                        //binParam.setValue(new String(binParam.getValue().getBytes(), "UTF-8"));
                        //binParam.setValue(new String(binParam.getValue().getBytes(Lang.detectEncoding(binParam.getValue())), "UTF-8"));
                        if (binParam.getName().equals("comment")) {
                            comment=binParam.getValue();
                            continue;
                        }
                        if (binParam.getName().equals("comment_private")) {
                            comment_private=binParam.getValue();
                            continue;
                        }
                        if (binParam.getName().equals("photos") || binParam.getName().equals("loctab")) continue;
                        String val = Lang.findByName(binParam.getName(), binParam.getValue());
                        boolean ln = Lang.findTypeByName(binParam.getName()).equals("1");
                        result +="<td>"+ ((ln) ? getCheckBox(val) : val)+"</td>"; //.equals("0")?"Есть":"Нет")
                    }
                    if (contact) {
                        result+="<td>"+Lang.findNameAgentstvoByIDA(binBase.getIda())+"</td>";
                        result+="<td>"+Lang.findAgentContactByIDA(binBase.getIda(),binBase.getIdr())+"</td>";
                    }
                    if (priv) {
                        result+="<td>"+comment+"</td>";
                        result+="<td>"+comment_private+"</td>";
                    }
                    result += "</tr>";
                }
                result_body += result + "\n"+((print_file.equals(GlobalConfig.PRINT_FILE))?"<br style='page-break-before:always'/>"+ "\n":"");
            }
            if (print_file.equals(GlobalConfig.PRINT_FILE_TABLE)) {
                result_body = result_table_head + result_body + "</tbody></table>";
            }
            String out = result_header+ "<body>" + result_body +  result_foother;
            Util.saveTempFile(GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.TMP_DIR + "/out.html", out, true);
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }
}
