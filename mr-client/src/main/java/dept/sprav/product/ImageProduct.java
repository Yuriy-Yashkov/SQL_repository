package dept.sprav.product;

import dept.MyReportsModule;
import dept.marketing.ImagePreview;
import dept.marketing.Image_;
import dept.marketing.MarketingForm;
import workDB.PDB;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Leo
 */
public class ImageProduct {

    public static int selectedid, artikul = 0;
    public static int size;
    public static ImageLabel tmpLabel;
    public static JPopupMenu popup;
    public static JFileChooser filechooser;
    public PDB pdb;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel2;

    public javax.swing.JScrollPane jScrollPane3;

    public static ImageIcon createIcon(String path) {
        String imgURL = MyReportsModule.progPath + path;
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Файл не найден " + path);
            return null;
        }
    }

    public void showSmallImage(int sar) throws IOException {
        Image_[] images = pdb.getSmallImages(sar);
        size = images.length;
        artikul = sar;
        if (size > 0) {
            //--------Вывод первой большой картинки
            Image_ firstImage = new Image_();
            firstImage = images[size - 1];
            showBigImage(firstImage.getId());
            //--------------------------------------
            while (size > 0) {
                Image_ pic = new Image_();
                pic = images[size - 1];
                tmpLabel = new ImageLabel();
                tmpLabel.setIdImage(pic.getId());
                tmpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        try {
                            showBigImage(((MarketingForm.ImageLabel) evt.getComponent()).getIdImage());
                        } catch (IOException ex) {
                            //        Logger.getLogger(MarketingForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent event) {
                        maybeShowPopup(event);
                    }

                    @Override
                    public void mouseReleased(MouseEvent event) {
                        maybeShowPopup(event);
                    }

                    private void maybeShowPopup(MouseEvent event) {
                        if (event.isPopupTrigger()) {
                            popup.show(event.getComponent(), event.getX(), event.getY());
                            selectedid = ((MarketingForm.ImageLabel) event.getComponent()).getIdImage();
                        }
                    }
                });
                tmpLabel.setIcon(sizeImage(ImageIO.read(new ByteArrayInputStream(pic.getImage())), 1));
                tmpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                jPanel2.add(tmpLabel);
                size--;
            }
        } else {
            jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                }

                @Override
                public void mousePressed(MouseEvent event) {
                    maybeShowPopup(event);
                }

                @Override
                public void mouseReleased(MouseEvent event) {
                    maybeShowPopup(event);
                }

                private void maybeShowPopup(MouseEvent event) {
                    if (event.isPopupTrigger()) {
                        popup.show(event.getComponent(), event.getX(), event.getY());
                    }
                }
            });
        }
        jPanel2.repaint();
    }

    public void showBigImage(int idpic) throws IOException {
        JLabel bigImage = new JLabel();
        bigImage.setHorizontalAlignment(JLabel.CENTER);
        bigImage.setVerticalAlignment(JLabel.CENTER);
        bigImage.setSize(jPanel3.getWidth(), jPanel3.getHeight());
        bigImage.setIcon(sizeImage(ImageIO.read(new ByteArrayInputStream(pdb.getBigImages(idpic))), 2));
        jPanel3.removeAll();
        jPanel3.add(bigImage);
        jPanel3.repaint();
    }

    public ImageIcon sizeImage(BufferedImage destImage, int i) {
        int width = 0, height = 0;
        float el_width = 0, el_height = 0;
        float multiple1, multiple2;
        float im_width = destImage.getWidth();
        float im_height = destImage.getHeight();

        switch (i) {
            case 1:
                el_width = jScrollPane3.getWidth();
                el_height = jScrollPane3.getHeight();
                break;
            case 2:
                el_width = jPanel3.getWidth();
                el_height = jPanel3.getHeight();
                break;
            case 3:
                el_width = jScrollPane3.getWidth() + 50;
                el_height = jScrollPane3.getHeight() + 50;
                break;
            default:
                break;
        }

        if (im_width < el_width && im_height < el_height) {
            width = destImage.getWidth();
            height = destImage.getHeight();
        } else {
            multiple1 = im_width / (el_width - 50);
            multiple2 = im_height / (el_height - 50);
            if (multiple1 >= multiple2) {
                width = (int) (im_width / multiple1);
                height = (int) (im_height / multiple1);
            } else if (multiple1 < multiple2) {
                width = (int) (im_width / multiple2);
                height = (int) (im_height / multiple2);
            }
        }
        return new ImageIcon(destImage.getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }

    public void myPopupMemu() {
        ActionListener menuListenerAdd = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addPicture();
            }
        };

        ActionListener menuListenerRemove = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                removePicture();
            }
        };

        ActionListener menuSaveAs = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    saveAsPicture();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "ошибка не удалось сохранить изображение на диске " + ex.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        JMenuItem item;
        popup.add(item = new JMenuItem("Добавить", createIcon("/Img/add.png")));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListenerAdd);
        popup.add(item = new JMenuItem("Удалить", createIcon("/Img/deletered.png")));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListenerRemove);
        popup.addSeparator();
        popup.add(item = new JMenuItem("Сохранить как...", createIcon("/Img/options.png")));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuSaveAs);
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
    }

    public void addPicture() {
        byte[] array = null;
        if (filechooser == null) {
            filechooser = new JFileChooser();
            filechooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    if (f != null)
                        if (f.isDirectory()) return true;

                    return f.getName().toLowerCase().endsWith("jpeg") || f.getName().toLowerCase().endsWith("jpg");
                }

                public String getDescription() {
                    return "*.jpeg; *.jpg;";
                }
            });
            filechooser.setAcceptAllFileFilterUsed(false);

            // Добавление панели предварительного просмотра.
            filechooser.setAccessory(new ImagePreview(filechooser));
        }

        if (filechooser.showDialog(null, "Добавить") == JFileChooser.APPROVE_OPTION) {
            File file = filechooser.getSelectedFile();
            if (file.length() < 2200000) {
                try {
                    ImageIcon scaledImage_ = sizeImage(ImageIO.read(file), 3);
                    Image scaledImage = scaledImage_.getImage();
                    RenderedImage renderedImage;
                    if (scaledImage instanceof RenderedImage) {
                        renderedImage = (RenderedImage) scaledImage;
                    } else {
                        BufferedImage bufferedImage = new BufferedImage(scaledImage_.getIconWidth(), scaledImage_.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                        bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);
                        renderedImage = bufferedImage;
                    }
                    ByteArrayOutputStream out1 = new ByteArrayOutputStream();
                    ImageIO.write(renderedImage, "jpg", out1);
                    array = out1.toByteArray();
                } catch (IOException ex) {
//                    Logger.getLogger(MarketingForm.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (pdb.addImage(file, artikul, array)) {
                    try {
                        jpanelRemove();
                        showSmallImage(artikul);
                        jpanelRepaint();
                    } catch (IOException ex) {
                        //         Logger.getLogger(MarketingForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else
                JOptionPane.showMessageDialog(null, "Файлы размером более 2MB не загрузятся.\nВ случае возникновения проблем попробуйте\nзагрузить изображение меньшего размера.", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        // Сброс выбора файлов.
        filechooser.setSelectedFile(null);
    }

    public void removePicture() {
        if (pdb.deleteImage(selectedid, artikul)) {
            try {
                jpanelRemove();
                showSmallImage(artikul);
                jpanelRepaint();
            } catch (IOException ex) {
//                 Logger.getLogger(MarketingForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void jpanelRemove() {
        jPanel3.removeAll();
        jPanel2.removeAll();
    }

    public void jpanelRepaint() {
        jPanel3.repaint();
        jPanel2.repaint();
    }

    private void saveAsPicture() {
        //  if (filechoosersv == null) {
        //      filechoosersv = new JFileChooser();
        //    }
        /*
        if (filechoosersv.selectArticles(this,"Сохранить как...") == JFileChooser.APPROVE_OPTION) {
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(pdb.getBigImages(selectedid))), "jpg", filechoosersv.getSelectedFile());
        }
        filechoosersv.setSelectedFile(null);
        */
    }

    public class ImageLabel extends JLabel {
        private int idImage;
        private int artikulIzdelie;

        public int getIdImage() {
            return idImage;
        }

        public void setIdImage(int idImage) {
            this.idImage = idImage;
        }

        public int getArtikulIzdelie() {
            return artikulIzdelie;
        }

        public void setArtikulIzdelie(int artikulIzdelie) {
            this.artikulIzdelie = artikulIzdelie;
        }
    }
}
