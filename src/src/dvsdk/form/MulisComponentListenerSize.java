package dvsdk.form;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;

/**
 *
 * @author develop-dvs
 */
public class MulisComponentListenerSize implements ComponentListener {
    private int MIN_WIDTH;
    private int MIN_HEIGHT;
    private JFrame frame;


    public MulisComponentListenerSize(int MIN_WIDTH, int MIN_HEIGHT, JFrame frame) {
        this.MIN_WIDTH = MIN_WIDTH;
        this.MIN_HEIGHT = MIN_HEIGHT;
        this.frame = frame;
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        //JFrame frame = (JFrame)e.getComponent();
        int width = frame.getWidth();
        int height = frame.getHeight();
        //we check if either the width
        //or the height are below minimum
        boolean resize = false;
        if (width < MIN_WIDTH) {
            resize = true;
            width = MIN_WIDTH;
        }
        if (height < MIN_HEIGHT) {
            resize = true;
            height = MIN_HEIGHT;
        }
        if (resize) {
           frame.setSize(width, height);
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentShown(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }
    
}