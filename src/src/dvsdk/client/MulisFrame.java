package dvsdk.client;

import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.db.BeanBase;
import dvsdk.form.FEditPlace;
import dvsdk.form.JImageFileChooser;
import dvsdk.form.SearchBoxModel;
import dvsdk.util.UtilSQL;
import dvsdk.util.Util;
import dvsdk.util.UtilImg;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import mulis.photoviewer.GraphBtn;

/**
 *
 * @author develop-dvs
 */
public class MulisFrame extends JFrame {

    protected Window ownerWindow = null;
    //protected IFrameCore frameCore = null;
    protected JPanel photopanel = null;
    private boolean mod = false;
    protected JButton closeButton = null;
    protected JButton saveButton = null;
    protected JTextArea privComment = null;
    private BeanBase binBase = null;
    private JTextField photos = null;
    private JComboBox agentInfo = null;
    private JComboBox streetList = null;
    public MulisTableModel model = null;
    private JTable table = null;
    
    private JFormattedTextField price_a = null;
    private JFormattedTextField price = null;
    private JFormattedTextField auction = null;
    private JFormattedTextField prepaid = null;

    public MulisFrame() {
        photos = new JTextField();
        photos.setName("photos");
        //photos.setText("Фото");
        photos.setVisible(false);
        photos.getAccessibleContext().setAccessibleName("Фото");
        photos.getAccessibleContext().setAccessibleDescription("50");
        this.add(photos);
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                GlobalConfig.getCore().setEnable();
                if (ownerWindow != null) {
                    ownerWindow.setEnabled(true);
                }
            }
        });
    }
    
    InputVerifier inputVerifier = new InputVerifier() {

        @Override
        public boolean verify(JComponent input) {
            JTextField field = (JTextField)input;
            try {
                int num = Integer.parseInt(field.getText());
                return true;
            } catch (Exception ex) { }
            return false;
        }
    };

    public MulisFrame(Window ownerWindow) {
        this();
        this.ownerWindow = ownerWindow;
        GlobalConfig.getCore().setIconMulis(this);
    }
    
    public final void postInitButtons(JButton closeButton, JButton saveButton) {
        this.setCloseButton(closeButton);
        this.setSaveButton(saveButton);
    }
    
    public final void postInitPriceFields(JFormattedTextField price_a, JFormattedTextField price, JFormattedTextField auction, JFormattedTextField prepaid) {
        //price_a.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat())));
       
        //price_a.setFormatterFactory(new DefaultFormatterFactory(new MulisIntFormatter()));
        price.setFormatterFactory(new DefaultFormatterFactory(new MulisIntFormatter()));
        auction.setFormatterFactory(new DefaultFormatterFactory(new MulisIntFormatter()));
        prepaid.setFormatterFactory(new DefaultFormatterFactory(new MulisIntFormatter()));
       
        this.setPrice_a(price_a);
        this.setPrice(price);
        this.setAuction(auction);
        this.setPrepaid(prepaid);
    }
    
    public final void postInit(JPanel photoPanel, JTextArea privComment, JScrollPane scrollPane, JTable table, JComboBox i_agent, JComboBox streetList) {
        this.setLocationRelativeTo(null);
        this.setPhotopanel(photoPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(80);
        this.setAlwaysOnTop(true);
        this.setTable(table);
        this.setAgentInfo(i_agent);
        this.setPrivComment(privComment);
        this.setStreetList(streetList);
        
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editLocation();
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Component[] com = getPhotopanel().getComponents();
                for (Component component : com) {
                    if (component instanceof GraphBtn) {
                        ((GraphBtn) component).setImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                    }
                }
            }
        });
        Util.fillJFrameJComboBox(this);
        initLocationTable();
    }
    
    private void initLocationTable() {
        MulisTableModel mtm = new MulisTableModel();
        mtm.setNumRows(0);
        mtm.addColumn("Тип");
        mtm.addColumn("Название");
        mtm.addColumn("Комментарий");
        table.setModel(mtm);
        setModel(mtm);
    }

    public JTextArea getPrivComment() {
        return privComment;
    }

    public void setPrivComment(JTextArea privComment) {
        this.privComment = privComment;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(JButton saveButton) {
        this.saveButton = saveButton;
    }

    public MulisTableModel getModel() {
        return model;
    }

    public void setModel(MulisTableModel model) {
        this.model = model;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public BeanBase getBinBase() {
        return binBase;
    }

    public void setBinBase(BeanBase binBase) {
        this.binBase = binBase;
    }

    public Window getOwnerWindow() {
        return ownerWindow;
    }

    public void setOwnerWindow(Window ownerWindow) {
        this.ownerWindow = ownerWindow;
    }

    public JPanel getPhotopanel() {
        return photopanel;
    }

    public void setPhotopanel(JPanel photopanel) {
        this.photopanel = photopanel;
    }

    public JComboBox getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(JComboBox agentInfo) {
        this.agentInfo = agentInfo;
        this.agentInfo.setEnabled(false);
        /*
        this.agentInfo.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                //getAgentPhone().setText(Lang.findUserContactByIDA( ((MulisComboBoxModelUsers)getAgentInfo().getModel()).getCurrentElement(getAgentInfo().getSelectedIndex()) ));
            }
        });*/
        // TODO: Дополнительная информация про агента
        /*
        this.agentInfo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (getAgentInfo().getModel() instanceof MulisComboBoxModelUsers)
                    getAgentPhone().setText(Lang.findUserContactByIDA( ((MulisComboBoxModelUsers)getAgentInfo().getModel()).getCurrentElement(getAgentInfo().getSelectedIndex()) ));
            }
        });*/
        
    }
    /*
        jFormattedTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField2FocusLost(evt);
            }
        });*/
        
    //TODO: Refactoring
    public void focusPriceLost(java.awt.event.FocusEvent evt) {
        try {
        if ( price_a!=null) {
                price_a.setValue(
    Integer.parseInt((price.getValue()==null)?"0":price.getValue().toString())+
    Integer.parseInt((auction.getValue()==null)?"0":auction.getValue().toString())+
    Integer.parseInt((prepaid.getValue()==null)?"0":prepaid.getValue().toString())
            );
                price_a.commitEdit();
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }
    
    public void recommitEdit() {
        try {
            if (price!=null && !price.getText().isEmpty()) price.commitEdit();
            if (auction!=null && !auction.getText().isEmpty()) auction.commitEdit();
            if (prepaid!=null && !prepaid.getText().isEmpty()) prepaid.commitEdit();
            if (price_a!=null && !price_a.getText().isEmpty()) price_a.commitEdit();
        
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }


    public boolean isMod() {
        return mod;
    }

    public void setMod(boolean mod) {
        this.mod = mod;
    }

    public String getID() {
        return this.getAccessibleContext().getAccessibleDescription();
    }

    public void setID(String id) {
        this.getAccessibleContext().setAccessibleDescription(id);
    }

    public boolean save() {
        focusPriceLost(null);
        List<BufferedImage> images = getImages();
        /*if (images.size() > 0) {
            photos.setText(""+images.size());
        } else {
            photos.setText("0");
        }*/
        photos.setText(""+images.size());
        if (!UtilSQL.grab(GlobalConfig.getCore().getBase(getName()), this)) {
            return false;
        }
        UtilImg.saveImagesToDir(images, getID(), getName());
        images = null;
        return true;
    }

    public void saveAndExit() {
        if (this.save()) {
            if (isMod()) GlobalConfig.getCore().refreshCurrentRow();
            else GlobalConfig.getCore().refreshCurrentTable(true);
            this.dispose();
        }
    }

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(operation);
        photopanel = null;
    }

    public List<BufferedImage> getImages() {
        List<BufferedImage> ret = new ArrayList<BufferedImage>();
        Component[] components = photopanel.getComponents();
        for (Component component : components) {
            if (component instanceof GraphBtn) {
                ret.add(((GraphBtn) component).getImage());
            }
        }
        return ret;
    }

    public JButton getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(JButton closeButton) {
        this.closeButton = closeButton;
        if (GlobalConfig.DEBUG==0) {
            this.closeButton.setVisible(false);
        }
    }

    public JComboBox getStreetList() {
        return streetList;
    }

    public void setStreetList(JComboBox streetList) {
        this.streetList = streetList;
    }

    public JFormattedTextField getPrice_a() {
        return price_a;
    }

    public void setPrice_a(JFormattedTextField price_a) {
        this.price_a = price_a;
        this.price_a.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                focusPriceLost(evt);
                super.focusLost(evt);
            }

            @Override
            public void focusGained(FocusEvent e) {
                focusPriceLost(e);
                super.focusGained(e);
            }
        });
    }

    public JFormattedTextField getPrice() {
        return price;
    }

    public void setPrice(JFormattedTextField price) {
        this.price = price;
        this.price.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                focusPriceLost(evt);
                super.focusLost(evt);
            }
            @Override
            public void focusGained(FocusEvent e) {
                focusPriceLost(e);
                super.focusGained(e);
            }
        });
        this.price.addKeyListener(new MulisKeyIntListener());
    }

    public JFormattedTextField getAuction() {
        return auction;
    }

    public void setAuction(JFormattedTextField auction) {
        this.auction = auction;
        this.auction.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                focusPriceLost(evt);
                super.focusLost(evt);
            }
            @Override
            public void focusGained(FocusEvent e) {
                focusPriceLost(e);
                super.focusGained(e);
            }
        });
        this.auction.addKeyListener(new MulisKeyIntListener());
    }

    public JFormattedTextField getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(JFormattedTextField prepaid) {
        this.prepaid = prepaid;
        this.prepaid.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                focusPriceLost(evt);
                super.focusLost(evt);
            }
            @Override
            public void focusGained(FocusEvent e) {
                focusPriceLost(e);
                super.focusGained(e);
            }
        });
        this.prepaid.addKeyListener(new MulisKeyIntListener());
    }
    
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        GlobalConfig.getCore().setDisable();
        if (this.getID() == null) {
            this.setID(Integer.toString(UtilSQL.max(GlobalConfig.getCore().getBase(this.getName()), this.getName()) + 1));
        }
        Logger.put("ID: " + this.getID());

        SearchBoxModel sbm = new SearchBoxModel(streetList);
        //set the model on the combobox
        streetList.setModel(sbm);
        //set the model as the item listener also
        streetList.addItemListener(sbm);
        focusPriceLost(null);
        
        if (this.agentInfo.getModel() instanceof MulisComboBoxModelUsers)
            for (int i = 0; i < this.agentInfo.getModel().getSize(); i++) {
                if (binBase==null) break;
                if (((MulisComboBoxModelUsers)this.agentInfo.getModel()).getCurrentElement(i).toString().equals(binBase.getIdr())) {
                    this.agentInfo.setSelectedIndex(i);
                    break;
                }
            }
        
        if (isMod()) {
            recommitEdit();
            closeButton.setVisible(false);
            if (!binBase.getIdr().equals(GlobalConfig.AGENT_INN) || (!binBase.getIda().equals(GlobalConfig.MASTER_INN))) {
                saveButton.setVisible(false);
                if (!GlobalConfig.MASTER_INN.equals(binBase.getIda())) privComment.setVisible(false);
            }
            if (binBase.getIda().equals(GlobalConfig.MASTER_INN) && GlobalConfig.IS_MASTER) saveButton.setVisible(true);
            List<File> images = UtilImg.loadImagesFromDir(getID(), binBase.getIda(), getName());
            if (images != null) {
                for (Iterator<File> it = images.iterator(); it.hasNext();) {
                    File file = it.next();
                    addImgToPhotoPanel(file);
                }
            }
            images = null;
        }
        if (GlobalConfig.IS_MASTER) {
            getAgentInfo().setEnabled(true);
        }
    }

    public void addImgToPhotoPanel(String patch) {
        addImgToPhotoPanel(new File(patch));
    }

    public void addImgToPhotoPanel(File patch) {
        try {
            GraphBtn btn = new GraphBtn(this);
            btn.setImage(UtilImg.resizeImage(ImageIO.read(patch),
            GlobalConfig.THUMBINAL_WIDTH, GlobalConfig.THUMBINAL_HEIGHT));
            //images.add(btn);
            photopanel.add(btn);
            photopanel.repaint();
            btn = null;
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    public void addImageToButton() {
        JImageFileChooser chooser = new JImageFileChooser(JImageFileChooser.lastFilePath);
        chooser.setMultiSelectionEnabled(true);
        if (chooser.showOpenDialog(this) == JImageFileChooser.APPROVE_OPTION) {
            JImageFileChooser.lastFilePath = chooser.getSelectedFile().getPath();
            File[] files = chooser.getSelectedFiles();
            for (int i = 0; i < files.length; i++) {
                try {
                    addImgToPhotoPanel(files[i]);
                    files[i] = null;
                } catch (Exception e) {
                    if (JOptionPane.showConfirmDialog(this, "Не могу загрузить "
                            + files[i] + "\nОшибка " + e.getLocalizedMessage()
                            + "\nПродолжить загружать другие изображения?",
                            "Ошибка", JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
            }
            files = null;
        }
    }

    public void addLocation() {
        FEditPlace fep = new FEditPlace(this, true);

        fep.setTitle("Добавить ориентир");
        if (fep.execute()) {
            String[] d = new String[3];
            d[0] = fep.type;
            d[1] = fep.name;
            d[2] = fep.comment;
            model.addRow(d);

            //model.insertRow(0, d);
        }
    }

    public void deleteLocation() {
        int sel = table.getSelectedRow();
        if (model.getRowCount() > 0) {
            if ((sel >= 0) && (sel < model.getRowCount())) {
                if (JOptionPane.showConfirmDialog(this, "Удалить запись?", "Подтвердите",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    model.removeRow(sel);
                }
            }
        }

    }
    
    public void editLocation() {
        FEditPlace fep = new FEditPlace(this, true);

        int sel = table.getSelectedRow();
        if (model.getRowCount() > 0) {
            if ((sel >= 0) && (sel < model.getRowCount())) {
                if (fep.execute(model.getValueAt(sel, 0).toString(),
                        model.getValueAt(sel, 1).toString(),
                        model.getValueAt(sel, 2).toString())) {
                    model.setValueAt(fep.type, sel, 0);
                    model.setValueAt(fep.name, sel, 1);
                    model.setValueAt(fep.comment, sel, 2);
                }
            }
        }
    }
}