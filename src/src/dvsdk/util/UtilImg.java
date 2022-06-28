package dvsdk.util;

import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.form.IPhoto;
import dvsdkweb.sys.Files;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author develop-dvs
 */
public class UtilImg {
    /**
     * Изменение размера картинки Graphics2D
     *
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage resizeImage2D(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    /**
     * Изменение размера картинки
     *
     * @param originalImage
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {
        if (originalImage == null) {
            return null;
        }



        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        if ((newWidth == originalWidth) || (newHeight == originalHeight)) {
            return originalImage;
        }

        float widthRatio = originalWidth / newWidth;
        float heightRatio = originalHeight / newHeight;

        BufferedImage resizeImage = null;

        if (widthRatio > heightRatio) {
            float newThumbnailHeight = (float) originalHeight / (float) originalWidth * newWidth;
            resizeImage = UtilImg.resizeImage2D(originalImage, newWidth, (int) newThumbnailHeight);
        } else {
            float newThumbnailWidth = (float) originalWidth / (float) originalHeight * newHeight;
            resizeImage = UtilImg.resizeImage2D(originalImage, (int) newThumbnailWidth, newHeight);
        }
        return resizeImage;
    }
    
    public static boolean saveImagesToDir(List<BufferedImage> images, String id, String type) {
        try {
            if (id == null) {
                return false;
            }
            if (images.isEmpty()) {
                return true;
            }

            String currentDir = GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/m" + GlobalConfig.MASTER_INN + "/t" + type + "/" + id;
            if (new File(currentDir).exists()) {
                Util.deleteDir(currentDir);
            }
            new File(currentDir).mkdirs();
            int i = 0;
            for (Iterator<BufferedImage> it = images.iterator(); it.hasNext();) {
                BufferedImage bufferedImage = it.next();
                ImageIO.write(bufferedImage, GlobalConfig.IMG_EXT, new File(currentDir + "/" + i + "." + GlobalConfig.IMG_EXT));
                Logger.put("Image saved: " + currentDir + "/" + i + "." + GlobalConfig.IMG_EXT);
                i++;
            }
            return true;
        } catch (Exception ex) {
            Logger.put(ex);
            return false;
        }
    }
    
    public static void repositionImgDirs(String type) {
        try {
            File oldDir = new File(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/t" + type+ "/m" + GlobalConfig.MASTER_INN);
            File currentDir = new File(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/m" + GlobalConfig.MASTER_INN+"/t" + type);
            //File currentDirName = new File(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/m" + GlobalConfig.MASTER_INN+"/t" + type);

            if (oldDir.exists()) {
                Logger.put("Reposition ["+oldDir.getAbsolutePath()+"] to ["+currentDir.getAbsolutePath()+"] - START");
                // TODO! FIX THAT!
                if (!currentDir.exists()) {
                    FileUtils.moveDirectory(oldDir, currentDir);
                }
                //currentDirName.mkdirs();
                //FileUtils.moveDirectory(currentDirName);
                Logger.put("Reposition ["+oldDir.getAbsolutePath()+"] to ["+currentDir.getAbsolutePath()+"] - END");
            }
        } catch (Exception ex) {
            Logger.put(ex);
        }
    }

    public static List<File> loadImagesFromDir(String id, String ida, String type) {
        return Files.loadFilesFromDir(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/m" + ida + "/t" + type + "/" + id, GlobalConfig.IMG_EXT);
    }
    
    public static int getImageCountFromDir(String id, String ida, String type) {
        List<File> loadImagesFromDir = UtilImg.loadImagesFromDir(id, ida, type);
        if (loadImagesFromDir==null) {
            return 0;
        } else {
            return loadImagesFromDir.size();
        }
    }
    
    /*
     public static boolean massMoveImagesDir(String new_idr, String idr, String ida, String type) {
     return moveDir(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/t" + type + "/m" + ida + "/a" + idr, GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/t" + type + "/m" + ida + "/a" + new_idr);
     }*/
    
    public static List<ImageIcon> generateGalleryImages(int photoMiniSize, List<File> imgs, List<ImageIcon> preImages) {
        preImages.clear();
        List<ImageIcon> orig = new ArrayList<ImageIcon>();
        if (imgs==null) return orig;
        for (Iterator<File> it1 = imgs.iterator(); it1.hasNext();) {
            File file = it1.next();
            ImageIcon tmpIcon = new ImageIcon(file.getPath());
            orig.add(tmpIcon);
            preImages.add(normalizeImage(photoMiniSize, tmpIcon));
            //preImages.add(new ImageIcon((tmpIcon.getImage().getScaledInstance(photoMiniSize, (int)(tmpIcon.getIconHeight()/(tmpIcon.getIconWidth()/photoMiniSize)), Image.SCALE_SMOOTH))));
        }
        return orig;
    }

    public static void generateFullImages(int photoFullSize, List<ImageIcon> origImages, List<ImageIcon> fullImages) {
        fullImages.clear();
        for (Iterator<ImageIcon> it1 = origImages.iterator(); it1.hasNext();) {
            ImageIcon icon = it1.next();
            fullImages.add(normalizeImage(photoFullSize, icon));
        }
    }

    public static ImageIcon normalizeImage(int photoSize, ImageIcon icon) {
        if (icon.getIconWidth() < icon.getIconHeight()) {
            double coef_h = icon.getIconHeight() / photoSize;
            int new_w = (int) (icon.getIconWidth() / coef_h);
            return new ImageIcon((icon.getImage().getScaledInstance(new_w, photoSize, Image.SCALE_SMOOTH)));
        } else {
            double coef_w = icon.getIconWidth() / photoSize;
            int new_h = (int) (icon.getIconHeight() / coef_w);
            return new ImageIcon((icon.getImage().getScaledInstance(photoSize, new_h, Image.SCALE_SMOOTH)));
        }
    }

    public static void fillMiniPhotoGallery(JPanel photoGalleryPanel, int photoSize, List<ImageIcon> preImages, final IPhoto core) {
        photoGalleryPanel.removeAll();
        photoGalleryPanel.repaint();
        int cnt = 0;
        for (Iterator<ImageIcon> it1 = preImages.iterator(); it1.hasNext();) {
            ImageIcon imageIcon = it1.next();

            final JButton button = new JButton(imageIcon);

            button.setToolTipText("Фото");
            button.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            button.setContentAreaFilled(false);
            button.setMargin(new java.awt.Insets(0, 0, 0, 0));
            button.setMaximumSize(new java.awt.Dimension(photoSize, photoSize));
            button.setMinimumSize(new java.awt.Dimension(photoSize, photoSize));
            button.setName(cnt++ + "");
            button.setPreferredSize(new java.awt.Dimension(photoSize, photoSize));
            button.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    core.showBigPhoto(Integer.parseInt(button.getName()));
                }
            });
            photoGalleryPanel.add(button);
        }
        photoGalleryPanel.revalidate();
        photoGalleryPanel.repaint();
    }

    public static void fillFullPhotoInGallery(JButton photoButton, ImageIcon img) {
        photoButton.setIcon(img);
        photoButton.setSize(img.getIconWidth(), img.getIconHeight());
        photoButton.repaint();
    }

    public static void repositionPhotos(String fromId, String toId, String type, String ida) {
        Util.renameFolder(GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/m" + ida + "/t" + type + "/" + fromId, GlobalConfig.LOCATION + "/" + GlobalConfig.IMG_DIR + "/m" + ida + "/t" + type +  "/" + toId);
    }
}
