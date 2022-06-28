package dvsdk.form;

import dvsdk.util.Lang;
import javax.swing.JFileChooser;

/**
 *
 * @author develop-dvs
 */
public class JImageFileChooser extends JFileChooser {

    public static String lastFilePath = "";

    public JImageFileChooser(String currentDirectoryPath) {
        super(currentDirectoryPath);
        ImagePreviewPanel preview = new ImagePreviewPanel();
        setAccessory(preview);
        addPropertyChangeListener(preview);
        setAcceptAllFileFilterUsed(false);
        setDialogTitle("Выбор изображения");
        setFileFilter(dvsdk.util.Util.createFileFilter("Все поддерживаемые типы", true, "jpg", "jpeg", "gif", "png"));
        Lang.setUpdateUI(this);
    }
}
