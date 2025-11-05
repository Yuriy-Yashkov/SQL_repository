package by.march8.ecs.application.modules.art.editor;

//import by.march8.ecs.application.modules.art.reports.ModelSampleReport;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.art.util.UtilArt;
import by.march8.ecs.application.modules.references.product.components.image.ImagePreview;
import by.march8.ecs.application.modules.references.product.utils.UtilImage;
import by.march8.entities.company.VEmployeeArt;
import by.march8.entities.product.ModelSample;
import by.march8.entities.product.ProductKind;
import by.march8.entities.standard.Standard;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by lidashka.
 */

public class ModelSampleEditor extends EditingPane {

    final JButton btnPrint = new JButton();
    private final JTextField tfModel = new JTextField();
    private final JTextField tfName = new JTextField();
    private final JTextField tfSize = new JTextField();
    private final JDateChooser jcDate = new JDateChooser();
    private final JTextPane jtDescription = new JTextPane();
    private final JTextPane jtСanvas = new JTextPane();
    private final JTextPane jtApplied = new JTextPane();
    private final JTextPane jtNote = new JTextPane();
    private final MainController controller;
    private ComboBoxPanel<ProductKind> cbpKind;
    private ComboBoxPanel<VEmployeeArt> cbpPainter;
    private ComboBoxPanel<VEmployeeArt> cbpConstructor;
    private ComboBoxPanel<Standard> cbpStandard;
    private UCToolBar toolBar = null;
    private UCToolBar toolBarImage = null;
    private JPanel imagePanel;
    private byte[] image = null;
    private ProductKind kind = new ProductKind();
    private VEmployeeArt painter = new VEmployeeArt();
    private VEmployeeArt constructor = new VEmployeeArt();
    private Standard standard = new Standard();
    private ModelSample source = null;

    public ModelSampleEditor(final IReference reference) {
        super(reference);
        this.controller = reference.getMainController();

        setPreferredSize(new Dimension(900, 600));

        init();
        initEvents();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        cbpPainter = new ComboBoxPanel<>(true, controller, MarchReferencesType.V_COMPANY_EMPLOYEES_ART);
        cbpConstructor = new ComboBoxPanel<>(true, controller, MarchReferencesType.V_COMPANY_EMPLOYEES_ART);

        cbpKind = new ComboBoxPanel<>(true, controller, MarchReferencesType.PRODUCT_KIND);
        cbpStandard = new ComboBoxPanel<>(true, controller, MarchReferencesType.CODELIST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new MigLayout());

        infoPanel.add(new JLabel("Модель *: "), "alignX right");
        infoPanel.add(tfModel, "growx, wrap");

        infoPanel.add(new JLabel("Наименование *: "), "alignX right");
        infoPanel.add(tfName, "growx, wrap");

        infoPanel.add(new JLabel("Размеры *: "), "alignX right");
        infoPanel.add(tfSize, "growx, wrap");

        infoPanel.add(new JLabel("Вид *:"), "alignX right");
        infoPanel.add(cbpKind, "width 380:20:380,, wrap");

        infoPanel.add(new JLabel("ГОСТ *:"), "alignX right");
        infoPanel.add(cbpStandard, "width 380:20:380,, wrap");

        //confectionPanel.add(new JLabel("Прикладные материалы:"), "wrap");
        //confectionPanel.add(new JScrollPane(jtApplied), "push, grow, wrap");

        JPanel makerPanel = new JPanel();
        makerPanel.setLayout(new MigLayout());

        makerPanel.add(new JLabel("Дата создания *:"), "wrap");
        makerPanel.add(jcDate, "width 160:20:160, wrap");

        makerPanel.add(new JLabel("Художник *:"), "wrap");
        makerPanel.add(cbpPainter, "width 380:20:380,span 2, wrap");

        makerPanel.add(new JLabel("Конструктор *:"), "wrap");
        makerPanel.add(cbpConstructor, "width 380:20:380,span 2, wrap");

        JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
        tabbedPane.addTab("Авторы", makerPanel);
        tabbedPane.addTab("Полотно", new JScrollPane(jtСanvas));
        tabbedPane.addTab("Описание", new JScrollPane(jtDescription));
        infoPanel.add(tabbedPane, "span, grow, height 300, wrap");

        infoPanel.add(new JLabel("Примечание:"), "wrap");
        infoPanel.add(new JScrollPane(jtNote), "span, grow, height 50, wrap");

        JPanel samplePanel = new JPanel();
        samplePanel.setLayout(new BorderLayout());
        samplePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        samplePanel.setPreferredSize(new Dimension(370, 0));
        samplePanel.setBorder(javax.swing.BorderFactory
                .createTitledBorder(
                        null,
                        "Эскиз",
                        TitledBorder.LEFT,
                        TitledBorder.DEFAULT_POSITION,
                        new java.awt.Font("Dialog", Font.ITALIC, 12)));

        imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());

        imagePanel.add(new JLabel());
        samplePanel.add(imagePanel, BorderLayout.CENTER);

        toolBarImage = new UCToolBar();
        toolBarImage.setVisibleSearchControls(false);
        toolBarImage.getBtnEditItem().setVisible(false);
        toolBarImage.getBtnReport().setVisible(false);
        toolBarImage.setRight(RightEnum.WRITE);
        toolBarImage.updateButton(0);
        samplePanel.add(toolBarImage, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(infoPanel, BorderLayout.CENTER);
        centerPanel.add(samplePanel, BorderLayout.WEST);

        btnPrint.setIcon(new ImageIcon(MainController.getRunPath() + "/res/printer.png", "Печать"));
        btnPrint.setToolTipText("Печать");

        toolBar = new UCToolBar();
        toolBar.setVisibleSearchControls(false);
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);
        toolBar.getBtnDeleteItem().setVisible(false);
        toolBar.getBtnReport().setVisible(false);
        toolBar.add(btnPrint);
        toolBar.addSeparator();

        //add(toolBar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void initEvents() {
        cbpKind.addButtonActionListener(e -> kind = cbpKind.selectFromReference(false));

        cbpKind.addComboBoxActionListener(e -> kind = cbpKind.getSelectedItem());

        cbpPainter.addButtonActionListener(e -> painter = cbpPainter.selectFromReference(false));

        cbpPainter.addComboBoxActionListener(e -> painter = cbpPainter.getSelectedItem());

        cbpConstructor.addButtonActionListener(e -> constructor = cbpConstructor.selectFromReference(false));

        cbpConstructor.addComboBoxActionListener(e -> constructor = cbpConstructor.getSelectedItem());

        cbpStandard.addButtonActionListener(e -> standard = cbpStandard.selectFromReference(false));

        cbpStandard.addComboBoxActionListener(e -> standard = cbpStandard.getSelectedItem());

        toolBarImage.getBtnNewItem().addActionListener(e -> addImage());

        toolBarImage.getBtnDeleteItem().addActionListener(e -> deleteImage());

        btnPrint.addActionListener(e -> {
            ///new ModelSampleReport(source);
        });
    }

    @Override
    public Object getSourceEntity() {
        source.setModel(tfModel.getText().trim());
        source.setName(tfName.getText().trim().toUpperCase());
        source.setSize(tfSize.getText().trim());

        if (cbpKind.getSelectedItem() != null) {
            System.out.println(kind.toString());
            source.setKind(kind);
        }

        if (cbpStandard.getSelectedItem() != null) {
            System.out.println(standard.toString());
            source.setStandard(standard);
        }

        source.setCanvas(jtСanvas.getText().trim());
        //source.setApplied(jtApplied.getText().trim());

        source.setSampleDescription(jtDescription.getText().trim());

        source.setDate(jcDate.getDate());

        if (cbpPainter.getSelectedItem() != null) {
            System.out.println(painter.toString());
            source.setPainter(painter.getId());
        }

        if (cbpConstructor.getSelectedItem() != null) {
            System.out.println(constructor.toString());
            source.setConstructor(constructor.getId());
        }

        source.setNote(jtNote.getText().trim());

        source.setImageSample(image);

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new ModelSample();

            kind = new ProductKind();
            standard = new Standard();
            painter = new VEmployeeArt();
            constructor = new VEmployeeArt();

            jcDate.setDate(UtilArt.TODAY);

            image = null;

            cbpPainter.preset(painter);
            cbpConstructor.preset(constructor);

        } else {
            this.source = (ModelSample) object;

            kind = this.source.getKind();
            standard = this.source.getStandard();

            jcDate.setDate(source.getDate());

            image = this.source.getImageSample();

            cbpPainter.preset(this.source.getPainter().getId());
            cbpConstructor.preset(this.source.getConstructor().getId());

        }
        defaultFillingData();

    }

    @Override
    public void defaultFillingData() {
        tfModel.setText(this.source.getModel());
        tfName.setText(this.source.getName());
        tfSize.setText(this.source.getSize());

        cbpKind.preset(kind);
        cbpStandard.preset(standard);

        if (this.source.getCanvas() == null) {
            jtСanvas.setText("Основное:\n \n" +
                    "Отделочное:\n");

        } else
            jtСanvas.setText(this.source.getCanvas());

        jtApplied.setText(this.source.getApplied());

        jtDescription.setText(this.source.getSampleDescription());
        jtNote.setText(this.source.getNote());

        showImage();
    }

    @Override
    public boolean verificationData() {
        if (tfModel.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать модель", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tfModel.requestFocusInWindow();
            return false;
        }

        if (tfName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать наименование модели", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }

        if (tfSize.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать размеры", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tfSize.requestFocusInWindow();
            return false;
        }

        if (cbpKind.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать вид модели", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpKind.requestFocusInWindow();
            return false;
        }

        if (cbpStandard.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать ГОСТ", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpStandard.requestFocusInWindow();
            return false;
        }

        if ((jcDate.getDate() == null)
                || (jcDate.getDate().toString().trim().equals(""))) {
            JOptionPane.showMessageDialog(null,
                    "Дата задана некорректно", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            jcDate.requestFocusInWindow();
            return false;
        }

        if (cbpPainter.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать художника", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpPainter.requestFocusInWindow();
            return false;
        }

        if (cbpConstructor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать конструктора", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpConstructor.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private void addImage() {
        JFileChooser fileChooser = null;

        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File file) {
                    if (file != null)
                        if (file.isDirectory()) return true;

                    return file.getName().toLowerCase().endsWith("jpeg")
                            || file.getName().toLowerCase().endsWith("jpg");
                }

                public String getDescription() {
                    return "*.jpeg; *.jpg;";
                }
            });
            fileChooser.setAcceptAllFileFilterUsed(false);

            // Добавление панели предварительного просмотра.
            fileChooser.setAccessory(new ImagePreview(fileChooser));
        }

        int returnVal = fileChooser.showDialog(this, "Добавить");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (file.length() < 15200000) {
                try {
                    ByteArrayOutputStream out1 = UtilImage
                            .compressImage(UtilImage
                                    .resizeImage(ImageIO
                                            .read(file), imagePanel.getWidth(), imagePanel.getHeight()));

                    image = out1.toByteArray();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                showImage();

            } else
                JOptionPane.showMessageDialog(
                        null,
                        "Попробуйте загрузить изображение меньшего размера.",
                        "Ошибка",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        // Сброс выбора файлов.
        fileChooser.setSelectedFile(null);
    }

    private void showImage() {
        imagePanel.removeAll();

        if (image != null) {
            JLabel labelImage = new JLabel();
            labelImage.setHorizontalAlignment(JLabel.CENTER);
            labelImage.setVerticalAlignment(JLabel.CENTER);
            labelImage.setSize(imagePanel.getWidth(), imagePanel.getHeight());

            try {
                labelImage.setIcon(UtilImage
                        .resizeImage(ImageIO.read(new ByteArrayInputStream(image)),
                                imagePanel.getWidth(),
                                imagePanel.getHeight()));

            } catch (Exception e) {
                e.printStackTrace();
            }

            imagePanel.add(labelImage);

            toolBarImage.updateButton(1);
        } else {
            toolBarImage.updateButton(0);
        }

        imagePanel.repaint();
    }

    private void deleteImage() {
        try {
            image = null;
            showImage();

        } catch (Exception ex) {
        }
    }
}
