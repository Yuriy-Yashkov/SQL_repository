package dept.marketing;

import by.march8.ecs.framework.common.LogCrutch;
import common.AutoComplete;
import common.User;
import dept.MyReportsModule;
import workDB.DB;
import workDB.PDB;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

/*
 * MarketingForm.java
 * Created on 23.06.2010, 11:46:54
 * @author lidashka
 */

public class MarketingForm extends javax.swing.JDialog {
    // private static Logger log = new Log().getLoger(MarketingForm.class);
    private static final LogCrutch log = new LogCrutch();
    public static String noteorder = "";
    public static boolean edit = false;
    public static JPanel jPanel2;
    public static String tekDate;
    public static String date;
    private static int size;
    private static ImageLabel tmpLabel;
    private static JFileChooser filechooser, filechoosersv;
    private static int selectedid, artikul = 0;
    private static JPopupMenu popup;
    public DynamicTree treePanel;
    public DynamicTree treeFasonyPanel;
    public DB mydb;
    public PDB pdb;
    Vector model;
    int rzm1 = -1;
    int rzm2 = -1;
    int rst1 = -1;
    int rst2 = -1;
    JComboBox cb = new JComboBox();
    private int idClient = -10;
    private int idOrder;
    private int state;
    private int sar;
    private String name_Client = null;
    private String madeorder;
    private String nar;
    private String nptk;
    private String testtext = "";
    private String constisselected = "";
    private Object textsar;
    private ButtonGroup group = new ButtonGroup();
    private OrdersForm ordersform = null;
    private User user = User.getInstance();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    public MarketingForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        try {
            madeorder = user.getFio();
            if (madeorder != null) {
                edit = false;
                createMarketingForm();

                date = tekDate;

                pdb = new PDB();
                pdb.updateListOrdersPDB();

                setResizable(false);
                setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору. ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            log.error("Ошибка  MarketingForm - Оформление заявки ", ex);
            System.err.println("Ошибка  MarketingForm - Оформление заявки " + ex.getMessage());
        }
    }
    public MarketingForm(javax.swing.JDialog parent, boolean modal, int idorder, int idclient, String nameclient, String date, int state) {
        super(parent, modal);

        madeorder = user.getFio();
        if (madeorder != null) {
            edit = true;
            createMarketingForm();

            if (state == 4) {
                this.state = 2;
                MarketingForm.date = tekDate;
            } else {
                this.state = state;
                MarketingForm.date = date;
            }

            ordersform = (OrdersForm) parent;
            pdb = ordersform.pdb;

            noteorder = pdb.downloadNoteSavedOrder(idorder);

            setNameKlient(nameclient);
            idClient = idclient;
            idOrder = idorder;

            jButton2.setVisible(false);

            setResizable(false);
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Такой логин и пароль не присвоен ни одному пользователю. \n Обратитесь к администратору. ", "Вход", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    protected static ImageIcon createIcon(String path) {
        String imgURL = MyReportsModule.progPath + path;
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Файл не найден " + path);
            return null;
        }
    }

    private void createMarketingForm() {
        mydb = new DB();
        initComponents();
        this.setTitle("Продукция предприятия");

        jPanel5.setLayout(new GridLayout());
        jPanel8.setLayout(new GridLayout());
        jPanel11.setLayout(new GridLayout());
        jPanel2 = new JPanel(new FlowLayout(0, 7, 0));
        jScrollPane3.setViewportView(jPanel2);
        popup = new JPopupMenu();

        Calendar c = Calendar.getInstance();
        tekDate = new String(new SimpleDateFormat("dd.MM.yyyy").format(c.getTime()));
        // int year = Integer.parseInt(String.valueOf(c.get(Calendar.YEAR)).substring(3, 4));

        myPopupMemu();
        cartPanel();
        updateCatalog();
        updateMarketingForm();

        addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                if (!edit)
                    pdb.disConn();
            }

            public void windowOpened(WindowEvent e) {
                if (edit)
                    jLabel1MouseClicked(null);
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
        setLocationRelativeTo(null);
    }

    public void updateMarketingForm() {
        jButton1.setVisible(false);
        jLabel13.setText("");
        jLabel16.setText("");
        jLabel17.setText("");
        jComboBox1.removeAllItems();
        jComboBox3.removeAllItems();
        jComboBox4.removeAllItems();

        jpanelRemove();
        jpanelRepaint();
    }

    public void updateCatalog() {
        treePanel = new DynamicTree(this, true, jCheckBox1.getModel().isSelected(), false);
        treePanel.madeTree();
        jPanel5.removeAll();
        jPanel5.add(treePanel);
        jPanel5.repaint();

        treeFasonyPanel = new DynamicTree(this, true, jCheckBox1.getModel().isSelected(), true);
        treeFasonyPanel.madeTree();
        jPanel8.removeAll();
        jPanel8.add(treeFasonyPanel);
        jPanel8.repaint();
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
                            showBigImage(((ImageLabel) evt.getComponent()).getIdImage());
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
                            selectedid = ((ImageLabel) event.getComponent()).getIdImage();
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
        setVisible(true);
    }

    private void showBigImage(int idpic) throws IOException {
        JLabel bigImage = new JLabel();
        bigImage.setHorizontalAlignment(JLabel.CENTER);
        bigImage.setVerticalAlignment(JLabel.CENTER);
        bigImage.setSize(jPanel3.getWidth(), jPanel3.getHeight());
        bigImage.setIcon(sizeImage(ImageIO.read(new ByteArrayInputStream(pdb.getBigImages(idpic))), 2));
        jPanel3.removeAll();
        jPanel3.add(bigImage);
        jPanel3.repaint();
    }

    private ImageIcon sizeImage(BufferedImage destImage, int i) {
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

    private void myPopupMemu() {
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
                } catch (IOException ex) {
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

    private void addPicture() {
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
        int returnVal = filechooser.showDialog(MarketingForm.this, "Добавить");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
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

    private void removePicture() {
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

    private void saveAsPicture() throws IOException {
        if (filechoosersv == null) {
            filechoosersv = new JFileChooser();
        }
        int returnVal = filechoosersv.showDialog(MarketingForm.this, "Сохранить как...");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(pdb.getBigImages(selectedid))), "jpg", filechoosersv.getSelectedFile());
        }
        filechoosersv.setSelectedFile(null);
    }

    private void cartPanel() {
        jLabel1.setIcon(createIcon("/Img/shoppingcart.png"));
        jLabel1.setHorizontalAlignment(JLabel.CENTER);
        jLabel1.setVerticalAlignment(JLabel.CENTER);
        jLabel1.setToolTipText("Корзина");

        /*   В jTextField2 можно вводить только 8 символов!   */
        jTextField2.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null)
                    return;
                if ((getLength() + str.length()) < 9) {
                    super.insertString(offset, str, attr);
                }
            }
        });
        jTextField2.setToolTipText("Введите шифр артикула...");

        jTextField3.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null)
                    return;
                if ((getLength() + str.length()) < 4) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        jTextField4.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null)
                    return;
                if ((getLength() + str.length()) < 4) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        jTextField6.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null)
                    return;
                if ((getLength() + str.length()) < 4) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        jTextField7.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null)
                    return;
                if ((getLength() + str.length()) < 4) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        jTextField1.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null)
                    return;
                if ((getLength() + str.length()) < 9) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        jComboBox2.addItem("Поиск по модели...");
        jComboBox2.addItem("Поиск по названию артикула...");
        jComboBox2.addItem("Поиск по шифр артикула...");

        jComboBox2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (jComboBox2.getSelectedItem().equals("Поиск по шифр артикула..."))
                    jTextField2.setToolTipText("Введите шифр артикула...");
                else if (jComboBox2.getSelectedItem().equals("Поиск по названию артикула..."))
                    jTextField2.setToolTipText("Введите артикул...");
                else if (jComboBox2.getSelectedItem().equals("Поиск по модели..."))
                    jTextField2.setToolTipText("Введите модель...");
            }
        });

        jLabel9.setIcon(createIcon("/Img/xmag.png"));
        jLabel9.setHorizontalAlignment(JLabel.CENTER);
        jLabel9.setVerticalAlignment(JLabel.CENTER);

        group.add(jRadioButton1);
        group.add(jRadioButton2);
        jRadioButton1.setSelected(true);

        buttonGroup1.add(jRadioButton3);
        buttonGroup1.add(jRadioButton4);
        jRadioButton3.setSelected(true);

        jLabel10.setText(madeorder);

        ActionListener fasonlistener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jComboBox4.removeAllItems();
                jComboBox3.removeAllItems();
                jButton1.setVisible(false);

                if (jComboBox1.getSelectedItem() == null || jComboBox1.getSelectedItem().equals("выбрать модель")) {
                } else if (!nar.equals("0")) {
                    jComboBox4.setModel(new DefaultComboBoxModel(mydb.getSort(textsar, nar, nptk, Integer.parseInt(jComboBox1.getSelectedItem().toString()), jCheckBox1.getModel().isSelected())));
                    if (jComboBox4.getItemCount() > 0) {
                        jComboBox3.setModel(new DefaultComboBoxModel(mydb.getColor(textsar, nar, nptk, Integer.parseInt(jComboBox1.getSelectedItem().toString()), Integer.parseInt(jComboBox4.getSelectedItem().toString()), jCheckBox1.getModel().isSelected())));
                        if (jComboBox3.getItemCount() > 0) jButton1.setVisible(true);
                    }
                } else {
                    if (jComboBox1.getSelectedItem().toString().trim().length() < 9) {
                        jComboBox4.addItem(1);
                        jComboBox4.addItem(2);
                        jComboBox4.addItem(3);
                        if (jComboBox4.getItemCount() > 0) {
                            jComboBox3.setModel(new DefaultComboBoxModel(mydb.getAllColor().toArray()));
                            jComboBox3.removeItemAt(0);
                            jComboBox3.insertItemAt("неизвестен", 0);
                            jComboBox3.setSelectedIndex(0);
                            if (jComboBox3.getItemCount() > 0) jButton1.setVisible(true);
                        }
                    }
                }
            }
        };

        ActionListener sortlistener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jComboBox3.removeAllItems();
                jButton1.setVisible(false);

                if (jComboBox4.getItemCount() > 0) {
                    if (!nar.equals("0")) {
                        jComboBox3.setModel(new DefaultComboBoxModel((mydb.getColor(textsar, nar, nptk, Integer.parseInt(jComboBox1.getSelectedItem().toString()), Integer.parseInt(jComboBox4.getSelectedItem().toString()), jCheckBox1.getModel().isSelected()))));
                    } else {
                        jComboBox3.setModel(new DefaultComboBoxModel(mydb.getAllColor().toArray()));
                        jComboBox3.removeItemAt(0);
                        jComboBox3.insertItemAt("неизвестен", 0);
                        jComboBox3.setSelectedIndex(0);
                    }
                    if (jComboBox3.getItemCount() > 0) jButton1.setVisible(true);
                }
            }
        };

        /*  Регистрируем слушателей выбора в ComboBox */
        jComboBox1.addActionListener(fasonlistener);
        jComboBox4.addActionListener(sortlistener);

        /* Вкладка 'Новая модель' */
        Object[][] vetkaizd = {{40100000, "Изделия мужские"}, {40200000, "Изделия женские"}, {40300000, "Изделия детские"},
                {40500000, "Спорт. взр."}, {40600000, "Спорт. дет."}, {43000000, "Чулки"}};

        model = new Vector();
        for (int i = 0; i < vetkaizd.length; i++) {
            model.add(new Item(Integer.parseInt(vetkaizd[i][0].toString()), vetkaizd[i][1].toString()));
        }

        for (Iterator itr = model.iterator(); itr.hasNext(); ) {
            Item it = (Item) itr.next();
            jComboBox5.addItem(it.getDescription());
        }

        ActionListener newmodellistener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cb.removeAllItems();
                jPanel11.remove(cb);
                Item itv = (Item) model.get(jComboBox5.getSelectedIndex());
                cb = new AutoComplete(mydb.getNameIzdelie(String.valueOf(itv.getId()).replace("0", "_")));
                cb.setEditable(true);
                jPanel11.add(cb);
            }
        };
        jComboBox5.addActionListener(newmodellistener);
        jComboBox5.setSelectedIndex(0);

        jTabbedPane1.getModel().addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateMarketingForm();
                if (jTabbedPane1.getModel().getSelectedIndex() == 0) {
                    if (jTabbedPane2.getModel().getSelectedIndex() == 0)
                        treePanel.selectedPath();
                    else if (jTabbedPane2.getModel().getSelectedIndex() == 1)
                        treeFasonyPanel.selectedPath();
                }
            }
        });

        jTabbedPane2.getModel().addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateMarketingForm();
                if (jTabbedPane2.getModel().getSelectedIndex() == 0)
                    treePanel.selectedPath();
                else if (jTabbedPane2.getModel().getSelectedIndex() == 1)
                    treeFasonyPanel.selectedPath();
            }
        });
    }

    public void cartDetailsBox(int kl, String shifr) {
        sar = kl;
        nar = shifr;
        nptk = "";
        textsar = sar;
        jComboBox1.removeAllItems();

        jLabel16.setText(shifr);
        jLabel13.setText(Integer.toString(kl));
        jLabel17.setText(mydb.getNameIzdelie(sar, nar));

        jComboBox1.setModel(new DefaultComboBoxModel(mydb.getFason(kl, shifr)));
        jComboBox1.setSelectedIndex(0);
    }

    public void cartDetailsBox2_0(Object fas, Object ppid, String criteriaSearch, String nameppid) {
        sar = Integer.parseInt(ppid.toString().trim());
        nar = "";
        nptk = nameppid.trim();
        jComboBox1.removeAllItems();

        if (criteriaSearch.equals("nar")) nar = getSectedRadioButton3Search(testtext);
        if (criteriaSearch.equals("sar")) sar = Integer.parseInt(testtext);

        if (nameppid.trim().equals("Продукция предприятия")) nptk = "";

        if (criteriaSearch.equals("sar")) textsar = getSectedRadioButton3Search(sar);
        else textsar = sar;

        jLabel16.setText("Все");
        jLabel13.setText("Все");
        jLabel17.setText("-");

        jComboBox1.addItem(fas);
        jComboBox1.setSelectedIndex(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel18 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Образцы продукции", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12))); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Просмотр", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 379, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 317, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Сделать заказ", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 15));
        jLabel2.setText("Заказчик:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 28, 139, 22));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setToolTipText("Корзина");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 11, 159, 130));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 13));
        jLabel3.setText("не выбран");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 62, 490, 20));

        jButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton2.setText("Выбрать");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 21, 117, -1));

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 15));
        jLabel5.setText("Сотрудник: ");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 94, -1, -1));

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 13));
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 114, 490, 20));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Детали", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 12))); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("Цвет:");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 253, 65, -1));

        jLabel7.setText("Модель:");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 174, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 169, 172, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboBox3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 248, 172, -1));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboBox4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 208, 172, -1));

        jLabel8.setText("Сорт:");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 213, -1, -1));

        jLabel12.setText("Шифр артикула:");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 33, -1, -1));

        jLabel13.setFont(new java.awt.Font("Dialog", 2, 14));
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(142, 33, 130, 17));

        jLabel14.setText("Артикул:");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 68, -1, -1));

        jLabel15.setText("Изделие:");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 103, 88, -1));

        jLabel16.setFont(new java.awt.Font("Dialog", 2, 14));
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 68, 170, 17));

        jLabel17.setFont(new java.awt.Font("Dialog", 2, 11));
        jLabel17.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 126, 260, 25));

        jButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton1.setText("Выбрать");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 293, 170, -1));

        jLabel4.setText("Размеры: ");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 299, -1, -1));

        jCheckBox1.setFont(new java.awt.Font("Dialog", 0, 13));
        jCheckBox1.setText("Показать только то, что есть на складе.");
        jCheckBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel6.setPreferredSize(new java.awt.Dimension(200, 76));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 220, -1));

        jComboBox2.setFont(new java.awt.Font("DejaVu Sans", 2, 11));
        jComboBox2.setForeground(java.awt.Color.black);
        jComboBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox2.setPreferredSize(new java.awt.Dimension(41, 17));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jPanel7.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 220, -1));

        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });
        jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 61, 50));

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel11.setText("Вид каталога:");
        jPanel7.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, -1, -1));

        jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton1.setText("краткий;");
        jRadioButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel7.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));

        jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton2.setText("полный;");
        jRadioButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel7.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, -1, -1));

        jLabel18.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel18.setText("Поиск:");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jRadioButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton3.setText("начало строки;");
        jRadioButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel7.add(jRadioButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 20));

        jRadioButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jRadioButton4.setText("любая позиция;");
        jRadioButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel7.add(jRadioButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, 20));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jTabbedPane2.addTab("Подробный каталог", jPanel5);

        jPanel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jTabbedPane2.addTab("Каталог моделей", jPanel8);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Каталог", jPanel6);

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton3.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton3.setText("Создать");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 345, 146, -1));

        jComboBox5.setFont(new java.awt.Font("DejaVu Sans", 2, 13));
        jComboBox5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel9.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 42, 210, -1));

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel19.setText("Ассортимент:");
        jPanel9.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 19, 292, -1));

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel20.setText("Модель:");
        jPanel9.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 160, 76, -1));

        jLabel21.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel21.setText("Шкала размер:");
        jPanel9.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 263, -1, -1));

        jLabel22.setText("-");
        jPanel9.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(197, 297, -1, -1));

        jLabel23.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel23.setText("Существующие модели:");
        jPanel9.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 380, 298, -1));

        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel9.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 180, -1));

        jTextField3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel9.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 292, 90, -1));

        jTextField4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel9.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 292, 90, -1));

        jList1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane1.setViewportView(jList1);

        jPanel9.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 403, 320, 210));

        jLabel24.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel24.setText("Наименование изделия:");
        jPanel9.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 87, 237, -1));

        jLabel25.setFont(new java.awt.Font("Dialog", 0, 13));
        jLabel25.setText("Шкала рост:");
        jPanel9.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 200, -1, -1));
        jPanel9.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 229, 96, -1));

        jLabel26.setText("-");
        jPanel9.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 234, -1, -1));
        jPanel9.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 229, 90, -1));

        jButton4.setFont(new java.awt.Font("Dialog", 0, 13));
        jButton4.setText("Сбросить");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 345, 140, -1));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 250, -1));

        jTabbedPane1.addTab("Новая модель", jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(3, 3, 3))
                                        .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTabbedPane1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jCheckBox1))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new ClientForm(this, true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (getNameKlient().equals("не выбран"))
            JOptionPane.showMessageDialog(null, "Вы не выбрали заказчика!", "Предупреждение", javax.swing.JOptionPane.WARNING_MESSAGE);

        boolean treeFasony = false;
        if (testTreeFasony()) treeFasony = true;

        new ProportionsForm(this, true, textsar, nar, nptk, jLabel17.getText(), Integer.parseInt(jComboBox1.getSelectedItem().toString()), Integer.parseInt(jComboBox4.getSelectedItem().toString()),
                jComboBox3.getSelectedItem().toString(), jCheckBox1.getModel().isSelected(), treeFasony);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        updateCatalog();
        updateMarketingForm();
        setVisible(true);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        if (edit)
            new CartForm(this, true, idOrder, idClient, name_Client, state);
        else {
            if (getNameKlient().equals("не выбран"))
                JOptionPane.showMessageDialog(null, "Вы не выбрали заказчика!", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            else
                new CartForm(this, true, getIdKlient(), getNameKlient());
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        try {
            if (getTextSearch().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Поле для поиска не должно быть пустым!", "Внимание", javax.swing.JOptionPane.WARNING_MESSAGE);
            } else {
                String criteria = "";
                String text = "";
                boolean updateflag = false;

                setSectedRadioButton3Search();

                if (jRadioButton1.isSelected()) DynamicTree.flagSearch = true;

                if (jComboBox2.getSelectedItem().equals("Поиск по названию артикула...")) {
                    if (getTextSearch().equals("0"))
                        text = "like '" + getSectedRadioButton3Search(getTextSearch()) + "С%'";
                    else text = "like '" + getSectedRadioButton3Search(getTextSearch()) + "%'";
                    criteria = "nar";
                } else {
                    text = "like '" + getSectedRadioButton3Search(Integer.parseInt(getTextSearch())) + "%'";
                    if (jComboBox2.getSelectedItem().equals("Поиск по шифр артикула...")) criteria = "sar";
                    else if (jComboBox2.getSelectedItem().equals("Поиск по модели...")) criteria = "fas";
                }

                if (!text.equals(DynamicTree.textSearch)) updateflag = true;

                if (!criteria.equals("") && !text.equals("")) {
                    if (jTabbedPane2.getSelectedIndex() == 0) treePanel.search(criteria, text);
                    else if (jTabbedPane2.getSelectedIndex() == 1) treeFasonyPanel.search(criteria, text);

                    if (criteria.equals("nar") && getTextSearch().equals("0")) testtext = getTextSearch() + "С";
                    else testtext = getTextSearch();
                }

                if (DynamicTree.flagSearch) DynamicTree.flagSearch = false;

                if (updateflag) {
                    if (jTabbedPane2.getSelectedIndex() == 0) treeFasonyPanel.updateTree();
                    else if (jTabbedPane2.getSelectedIndex() == 1) treePanel.updateTree();
                }
                updateMarketingForm();
            }
        } catch (Exception e) {
            if (DynamicTree.flagSearch) DynamicTree.flagSearch = false;
            jTextField2.setText("");
            testtext = getTextSearch();
            setSectedRadioButton3Search();
            treePanel.updateTree();
            treeFasonyPanel.updateTree();
            JOptionPane.showMessageDialog(null, "Введено некорректное значение!\n " + e.getMessage() + "", "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        jLabel9MouseClicked(null);
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        updateMarketingForm();
        rzm1 = -1;
        rzm2 = -1;
        rst1 = -1;
        rst2 = -1;
        jComboBox1.removeAllItems();
        jComboBox3.removeAllItems();
        jComboBox4.removeAllItems();
        try {
            int newFas = Integer.parseInt(jTextField1.getText().trim());
            if (!jTextField6.getText().trim().isEmpty() && !jTextField7.getText().trim().isEmpty()) {
                rst1 = Integer.parseInt(jTextField6.getText().trim());
                rst2 = Integer.parseInt(jTextField7.getText().trim());
            } else {
                jTextField6.setText("");
                jTextField7.setText("");
            }

            if (!jTextField3.getText().trim().isEmpty() && !jTextField4.getText().trim().isEmpty()) {
                rzm1 = Integer.parseInt(jTextField3.getText().trim());
                rzm2 = Integer.parseInt(jTextField4.getText().trim());
            } else {
                jTextField3.setText("");
                jTextField4.setText("");
            }

            jList1.setListData(mydb.testFas(newFas));

            Item itv = (Item) model.get(jComboBox5.getSelectedIndex());
            sar = itv.getId();
            textsar = itv.getId();
            nar = "0";

            jLabel16.setText(nar);
            jLabel13.setText(String.valueOf(sar));

            if (cb.getSelectedItem().toString().length() > 130)
                throw new Exception(" наименование изделия: " + cb.getSelectedItem().toString().substring(0, 10) + "...");

            if (!cb.getSelectedItem().toString().trim().isEmpty())
                jLabel17.setText(cb.getSelectedItem().toString().trim().toUpperCase() + " (новая модель)");
            else jLabel17.setText("Новая модель");

            jComboBox1.addItem(newFas);
            jComboBox1.setSelectedIndex(0);
        } catch (Exception e) {
            updateMarketingForm();
            JOptionPane.showMessageDialog(null, "Некорректна запись: " + e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        updateMarketingForm();
        jTextField1.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        cb.setSelectedIndex(0);
        jTextField6.setText("");
        jTextField7.setText("");
        jList1.setListData(new Vector());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged

    }//GEN-LAST:event_jComboBox2ItemStateChanged
    // End of variables declaration//GEN-END:variables

    public int getIdKlient() {
        return idClient;
    }

    public void setIdKlient(int id) {
        idClient = id;
    }

    public String getNameKlient() {
        return jLabel3.getText();
    }

    public void setNameKlient(String name) {
        jLabel3.setText(name);
        name_Client = name;
    }

    public void getCreateOrderSkladForm() {
        ordersform.createOrderSkladForm();
    }

    public int getRzm1() {
        return rzm1;
    }

    public int getRzm2() {
        return rzm2;
    }

    public int getRst1() {
        return rst1;
    }

    public int getRst2() {
        return rst2;
    }

    public String getTextSearch() {
        return jTextField2.getText().trim().toUpperCase();
    }

    public String setSectedRadioButton3Search() {
        if (!jRadioButton3.isSelected())
            constisselected = "%";
        else constisselected = "";
        return constisselected;
    }

    public String getSectedRadioButton3Search(Object str) {
        if (!jRadioButton3.isSelected())
            str = this.constisselected + str;
        return str.toString();
    }

    public boolean testTreeFasony() {
        boolean test = false;
        if (jTabbedPane2.getSelectedIndex() == 1 && jTabbedPane1.getSelectedIndex() == 0)
            test = true;
        return test;
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

    private class Item {
        private int idSar;
        private String nameSar;

        public Item(int id, String description) {
            this.idSar = id;
            this.nameSar = description;
        }

        public int getId() {
            return idSar;
        }

        public String getDescription() {
            return nameSar;
        }
    }
}
