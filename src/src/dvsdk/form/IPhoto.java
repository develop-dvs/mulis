package dvsdk.form;

import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author develop-dvs
 */
public interface IPhoto {
    public void showBigPhoto(int selected);
    public List<ImageIcon> getPreImages();
    public void generateFullImages(int size);
    public List<ImageIcon> getOrigImages();
    public ImageIcon getFullImage(int num);
}
