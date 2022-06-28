package dvsdk.client;

import dvsdk.form.IPhotoGallery;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author develop-dvs
 */
public class PhotoWheelListner extends MouseAdapter implements MouseWheelListener {

    private IPhotoGallery core;
    public PhotoWheelListner(IPhotoGallery core) {
        this.core=core;
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        if (e.getWheelRotation() < 0) {
            core.showNextPhoto();
        } else {
            core.showPrewPhoto();
        }
    }
    
}