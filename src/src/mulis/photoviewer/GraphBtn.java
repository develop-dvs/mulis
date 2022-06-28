package mulis.photoviewer;

import dvsdk.Logger;
import dvsdk.util.UtilImg;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author develop-dvs
 */
public class GraphBtn extends JButton {
    
    private BufferedImage image = null;
    private Frame parentFrame = null;
    
    public void setParentFrame(Frame parentFrame) {
        this.parentFrame = parentFrame;
    }
    
    public Frame getParentFrame() {
        return parentFrame;
    }
    
    protected void imgBtnActionPerformed(java.awt.event.ActionEvent evt) {
        
        GraphBtn btn = (GraphBtn) evt.getSource();
        BufferedImage bi = (new DViewer(parentFrame, true).execute(image));
        if (bi!=null) {
            setImage(bi);
        } else {
            // Удаление
            ((JPanel)getParent()).remove(btn);
            if (parentFrame!=null) {
                btn=null; bi=null; image=null;
                parentFrame.repaint();
            }
        }
                
    }
    
    public GraphBtn(Frame parentFrame) {
        this.parentFrame=parentFrame;
        setText("");
        this.setName("test");
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setContentAreaFilled(false);
        setMargin(new java.awt.Insets(0, 0, 0, 0));
        setMaximumSize(new java.awt.Dimension(80, 80));
        setMinimumSize(new java.awt.Dimension(80, 80));
        setPreferredSize(new java.awt.Dimension(80, 80));

        addActionListener(new java.awt.event.ActionListener() {
            @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    imgBtnActionPerformed(evt);
                }
            });
    }
    
    public void setImage(BufferedImage image) {
        if (image==this.image)
            return;
        this.image = image;
        setIcon(new ImageIcon(UtilImg.resizeImage(image, 64, 64)));
        if (parentFrame!=null)
            parentFrame.repaint();
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public void loadImage(String patch) {
        try {
            this.setImage(ImageIO.read(new File(patch)));
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }
}
