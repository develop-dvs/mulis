package dvsdk.util;

import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.client.BeanModel;
import dvsdk.client.CollectAgents;
import dvsdk.client.CollectModel;
import dvsdk.client.MulisFrame;
import dvsdk.db.BeanUser;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dvsdkweb.sys.Files;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author develop-dvs
 */
public class UtilXML {

    /**
     * Сохранить все данные из стандартных списков в XML файлы
     *
     * @param frame
     */
    public static void frameToXML(MulisFrame frame) {
        List<CollectModel> ret = Util.collectJFrame(frame);
        collectToXML(ret, frame.getName(), false);
        collectToXML(ret, frame.getName(), true);
    }

    /**
     * Генерация структуры базы в XML файл
     *
     * @param frame
     */
    public static void frameToBaseXML(MulisFrame frame) {
        List<CollectModel> ret = Util.collectJFrame(frame);
        collectToBaseXML(ret, frame.getName());
    }

    /**
     * Генерация структуры базы в XML файл
     *
     * @param collectModels
     * @param frameName
     */
    public static void collectToBaseXML(List<CollectModel> collectModels, String frameName) {
        try {
            String outDir = GlobalConfig.LOCATION + "/" + GlobalConfig.TMP_DIR;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Element e = null;
            Node n = null;
            Document xmldoc = null;
            xmldoc = impl.createDocument(null, "base", null);
            Element root = xmldoc.getDocumentElement();
            root.setAttribute("name", frameName);

            for (int i = 0; i < GlobalConfig.STD_FILEDS_C; i++) {
                e = xmldoc.createElementNS(null, "node");
                e.setAttributeNS(null, "type", GlobalConfig.STR_FIELDS_ARR_TYPE[i]);
                e.setAttributeNS(null, "std", "true");
                n = xmldoc.createTextNode(GlobalConfig.STR_FIELDS_ARR[i]);
                e.appendChild(n);
                root.appendChild(e);
            }

            for (Iterator<CollectModel> it = collectModels.iterator(); it.hasNext();) {
                CollectModel collectModel = it.next();

                if (GlobalConfig.isExcludeField(collectModel.getName())) {
                    continue;
                }

                e = xmldoc.createElementNS(null, "node");
                e.setAttributeNS(null, "type", Util.convertIntToType(collectModel.getTypeR1()));
                n = xmldoc.createTextNode(collectModel.getName());
                e.appendChild(n);
                root.appendChild(e);
            }

            for (int i = 0; i < GlobalConfig.STD_FILEDS_ADD_C; i++) {
                e = xmldoc.createElementNS(null, "node");
                e.setAttributeNS(null, "type", GlobalConfig.STR_FIELDS_ARR_ADD_TYPE[i]);
                n = xmldoc.createTextNode(GlobalConfig.STR_FIELDS_ARR_ADD[i]);
                e.appendChild(n);
                root.appendChild(e);
            }

            DOMSource source = new DOMSource(xmldoc);
            new File(outDir).mkdirs();
            StreamResult streamResult = new StreamResult(new File(outDir + frameName + ".xml"));
            //StreamResult streamResult = new StreamResult(new File(outDir+ frameName + collectModel.getName() + ".xml"));
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.transform(source, streamResult);
            Logger.put("GenerateBaseXML: " + outDir + frameName + ".xml");
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    /**
     * Загрузка коллекции из XML
     *
     * @return
     */
    public static List<CollectModel> XMLToCollect() {
        return XMLToCollect(GlobalConfig.LOCATION + "/" + GlobalConfig.XML_DIR);
    }

    /**
     * Загрузка коллекции из XML
     *
     * @param dir
     * @return
     */
    public static List<CollectModel> XMLToCollect(String dir) {
        List<CollectModel> ret = new ArrayList<CollectModel>();
        try {
            List<File> files = Files.loadFilesFromDir(dir, "xml");
            if (files==null || !GlobalConfig.INI) return ret;
            for (Iterator<File> it = files.iterator(); it.hasNext();) {
                File file = it.next();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(file);

                Element root = doc.getDocumentElement();

                List<BeanModel> binModels = new ArrayList<BeanModel>();
                NodeList nodes = doc.getElementsByTagName("node");
                if (nodes.getLength() != 0) {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node node = nodes.item(i);
                        Element e = (Element) node;
                        //String ad=e.getAttribute("add");
                        binModels.add(new BeanModel(e.getAttribute("id").toString(), e.getTextContent(), e.getAttribute("add")));
                    }
                }
                // TODO: Выбрать между... 
                //GlobalConfig.hashMapTypeFields.put(file.getName().replaceAll("\\.xml", ""), ((root.getAttribute("type").isEmpty())?0:Integer.parseInt(root.getAttribute("type"))) );
                ret.add(new CollectModel(root.getAttribute("id"), file.getName().replaceAll("\\.xml", ""), root.getAttribute("alias"), root.getAttribute("settings"), binModels, root.getAttribute("type")));
                Logger.put("LoadFromXML: " + dir + file.getName());
            }

        } catch (Exception ex) {
            Logger.put(ex);
        }

        return ret;
    }

    /**
     * Поиск коллекции по названию
     *
     * @param name
     * @param list
     * @return
     */
    public static CollectModel findCollectByName(String name, List<CollectModel> list) {
        for (Iterator<CollectModel> it = list.iterator(); it.hasNext();) {
            CollectModel collectModel = it.next();
            if ((collectModel.getName() + ".xml").equals(name)) {
                return collectModel;
            }
        }
        return null;
    }

    /**
     * Загрузка пользовательский настроек для полей
     *
     * @param dir
     * @param list
     */
    public static void XMLToPostCollect(String dir, List<CollectModel> list) {
        try {
            List<File> files = Files.loadFilesFromDir(dir, "xml");
            for (Iterator<File> it = files.iterator(); it.hasNext();) {
                File file = it.next();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(file);

                Element root = doc.getDocumentElement();

                CollectModel model = findCollectByName(file.getName(), list);
                if (model != null) {
                    model.setDesc(root.getAttribute("settings"));
                    model.setType(root.getAttribute("type"));
                    Logger.put("PostLoadFromXML: " + dir + file.getName());
                }
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    /**
     * Сохранить списки в XML файл
     *
     * @param collectModels
     */
    public static void collectToXML(List<CollectModel> collectModels, String frameName, boolean postload) {
        try {
            if (collectModels.isEmpty()) return;
            String outDir = (postload) ? GlobalConfig.USER_HOME_LOCATION + "/" + GlobalConfig.USER_DIR + "/" + GlobalConfig.XML_DIR : GlobalConfig.LOCATION + "/" + GlobalConfig.XML_DIR;
            frameName = (postload) ? frameName + "/" : "";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Element e = null;
            Node n = null;
            for (Iterator<CollectModel> it = collectModels.iterator(); it.hasNext();) {
                CollectModel collectModel = it.next();
                Document xmldoc = null;
                xmldoc = impl.createDocument(null, collectModel.getName(), null);
                Element root = xmldoc.getDocumentElement();
                root.setAttribute("alias", collectModel.getAlias());
                root.setAttribute("settings", collectModel.getDesc());
                root.setAttribute("type", collectModel.getType());

                if (!postload) {

                    if (collectModel.getName().equals("loc") || collectModel.getName().equals("street") || collectModel.getName().equals("city") || collectModel.getName().equals("area")) {
                        continue;
                    }
                    ArrayList<BeanModel> list = (ArrayList<BeanModel>) collectModel.getCollect();
                    for (Iterator<BeanModel> it1 = list.iterator(); it1.hasNext();) {
                        BeanModel binModel = it1.next();
                        e = xmldoc.createElementNS(null, "node");
                        //e.setAttributeNS(null, "name", collectModel.getName());
                        e.setAttributeNS(null, "id", binModel.getId());
                        n = xmldoc.createTextNode(binModel.getName());
                        e.appendChild(n);
                        root.appendChild(e);
                    }
                } else {
                    //new File(outDir+frameName).mkdirs();
                }
                DOMSource source = new DOMSource(xmldoc);

                StreamResult streamResult = new StreamResult(new File(outDir + collectModel.getName() + ".xml"));
                //StreamResult streamResult = new StreamResult(new File(outDir+ frameName + collectModel.getName() + ".xml"));
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer serializer = tf.newTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                //serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                serializer.transform(source, streamResult);
                Logger.put("GenerateXML: " + outDir + collectModel.getName() + ".xml");
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    /**
     * Сохранить пользователей в XML файл
     *
     * @param collectModels
     */
    public static void usersToXML(List<BeanUser> binUsers, boolean vcard) {
        try {
            String out = (vcard) ? GlobalConfig.LOCATION + "/" + GlobalConfig.AGT_DIR + "a" + GlobalConfig.MASTER_INN_GLOBAL + ".xml"
                    : GlobalConfig.LOCATION + "/" + GlobalConfig.CFG_DIR + "users.xml";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Element e = null;
            Node n = null;

            Document xmldoc = null;
            xmldoc = impl.createDocument(null, "users", null);
            Element root = xmldoc.getDocumentElement();
            
            //root.setAttribute("id", GlobalConfig.MASTER_INN);
            root.setAttribute("name", GlobalConfig.MASTER_NAME);
            if (!vcard) {
                root.setAttribute("id", GlobalConfig.MASTER_INN);
            } else {
                root.setAttribute("id", GlobalConfig.MASTER_INN_GLOBAL);
                root.setAttribute("email", GlobalConfig.MASTER_EMAIL);
                root.setAttribute("site", GlobalConfig.MASTER_SITE);
                root.setAttribute("info", GlobalConfig.MASTER_INFO);
            }
            for (Iterator<BeanUser> it = binUsers.iterator(); it.hasNext();) {
                BeanUser binUser = it.next();
                e = xmldoc.createElementNS(null, "node");
                e.setAttributeNS(null, "idr", binUser.getIdr());
                e.setAttributeNS(null, "contact", binUser.getContact());
                if (!vcard) {
                    e.setAttributeNS(null, "pwd", binUser.getPwdMD5());
                    e.setAttributeNS(null, "uploader", Boolean.toString(binUser.isUploader()));
                }
                n = xmldoc.createTextNode(binUser.getName());
                e.appendChild(n);
                root.appendChild(e);
            }

            DOMSource source = new DOMSource(xmldoc);
            
            StreamResult streamResult = new StreamResult(new File(out));
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.transform(source, streamResult);
            Logger.put("GenerateUsersXML: " + out);
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    /**
     * Загрузка пользователей из XML
     *
     * @param dir
     * @return
     */
    public static List<BeanUser> XMLToUsers(String dir) {
        List<BeanUser> ret = new ArrayList<BeanUser>();
        try {
            File file = new File(dir + "users.xml");
            if (!file.exists()) {
                return ret;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            Element root = doc.getDocumentElement();
            // TODO: Сохранять везде, или только у пользователя?
            //GlobalConfig.MASTER_INN_GLOBAL = root.getAttribute("id");
            //GlobalConfig.MASTER_PWD_MD5 = root.getAttribute("pwd");
            //GlobalConfig.MASTER_NAME = root.getAttribute("name");

            NodeList nodes = doc.getElementsByTagName("node");
            if (nodes.getLength() != 0) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    Element e = (Element) node;
                    ret.add(new BeanUser(e.getTextContent(), e.getAttribute("idr").toString(), e.getAttribute("contact").toString(), e.getAttribute("pwd").toString(), e.getAttribute("uploader").toString()));
                }
                Logger.put("LoadUsersFromXML: " + dir + file.getName());
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return ret;
    }

    /**
     * Загрузка агентств из XML
     *
     * @param dir
     * @return
     */
    public static List<CollectAgents> XMLToAgents(String dir) {
        List<CollectAgents> ret = new ArrayList<CollectAgents>();
        try {
            List<File> files = Files.loadFilesFromDir(dir, "xml");
            if (files==null) return ret;
            
            for (Iterator<File> it = files.iterator(); it.hasNext();) {
                File file = it.next();
                if (file.getName().equals("a.xml")) { continue; }
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(file);

                Element root = doc.getDocumentElement();

                List<BeanUser> binUsers = new ArrayList<BeanUser>();
                NodeList nodes = doc.getElementsByTagName("node");
                if (nodes.getLength() != 0) {
                    try {
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Node node = nodes.item(i);
                            Element e = (Element) node;
                            //String ad=e.getAttribute("add");
                            binUsers.add(new BeanUser(e.getTextContent(), e.getAttribute("idr").toString(), e.getAttribute("contact").toString()));
                        }
                    } catch (Exception ex) {
                    }
                }
                ret.add(new CollectAgents(root.getAttribute("id"), root.getAttribute("name"), root.getAttribute("email"), root.getAttribute("site"), root.getAttribute("info"), binUsers));
                Logger.put("LoadAgentsFromXML: " + dir + file.getName());
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
        return ret;
    }

    public static void agentsToXML(List<CollectAgents> agents) {
        try {
            for (Iterator<CollectAgents> it = agents.iterator(); it.hasNext();) {
                CollectAgents collectAgents = it.next();
                String out = GlobalConfig.LOCATION + "/" + GlobalConfig.AGT_DIR + "a" + collectAgents.getIda() +".xml";
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                DOMImplementation impl = builder.getDOMImplementation();
                Element e = null;
                Node n = null;

                Document xmldoc = null;
                xmldoc = impl.createDocument(null, "users", null);
                Element root = xmldoc.getDocumentElement();

                root.setAttribute("id", collectAgents.getIda());
                root.setAttribute("name", collectAgents.getName());

                root.setAttribute("email", collectAgents.getEmail());
                root.setAttribute("site", collectAgents.getSite());
                root.setAttribute("info", collectAgents.getInfo());
                
                for (Iterator<BeanUser> it1 = collectAgents.getAgents().iterator(); it1.hasNext();) {
                    BeanUser beanUser = it1.next();
                    e = xmldoc.createElementNS(null, "node");
                    e.setAttributeNS(null, "idr", beanUser.getIdr());
                    e.setAttributeNS(null, "contact", beanUser.getContact());
                    n = xmldoc.createTextNode(beanUser.getName());
                    e.appendChild(n);
                    root.appendChild(e);
                }

                DOMSource source = new DOMSource(xmldoc);
                StreamResult streamResult = new StreamResult(new File(out));
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer serializer = tf.newTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                //serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                serializer.transform(source, streamResult);
                Logger.put("GenerateAgentXML: " + out);
            }

        } catch (Exception ex) {
            Logger.put(ex);
        }
    }
}