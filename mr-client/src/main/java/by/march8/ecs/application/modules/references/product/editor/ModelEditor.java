package by.march8.ecs.application.modules.references.product.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.ISubReferences;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.model.GeneralTableModel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.product.components.image.ImageDialog;
import by.march8.ecs.application.modules.references.product.components.image.ImageLabel;
import by.march8.ecs.application.modules.references.product.components.image.ImagePreview;
import by.march8.ecs.application.modules.references.product.subeditor.ModelSubSize;
import by.march8.ecs.application.modules.references.product.utils.UtilImage;
import by.march8.ecs.framework.sdk.reference.Reference;
import by.march8.entities.company.Employee;
import by.march8.entities.product.ModelImage;
import by.march8.entities.product.ModelProduct;
import by.march8.entities.product.ModelSizeChart;
import by.march8.entities.product.ProductBrand;
import by.march8.entities.product.ProductCollection;
import by.march8.entities.product.ProductGroup;
import by.march8.entities.product.ProductKind;
import by.march8.entities.product.ProductRange;
import by.march8.entities.standard.Standard;
import by.march8.entities.standard.Unit;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Панель редактирования модели изделия
 *
 * Created by lidashka.
 */
public class ModelEditor extends by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane {

    private static JFileChooser fileChooser;
    private static ImageLabel imageLabel;
    private final JTextField tfModel = new JTextField();
    private final JTextField tfName = new JTextField();
    private final JTextPane jtDescription = new JTextPane();
    private final JTextPane jtNote = new JTextPane();
    private final ArrayList<Object> dataStandards = new ArrayList<>();
    private final ArrayList<Object> dataComposition = new ArrayList<>();
    private final ArrayList<Object> dataImages = new ArrayList<>();
    private final MainController controller;
    private ComboBoxPanel<ProductKind> cbpKind;
    private ComboBoxPanel<ProductBrand> cbpBrand;
    private ComboBoxPanel<ProductCollection> cbpCollection;
    private ComboBoxPanel<ProductGroup> cbpGroup;
    private ComboBoxPanel<ProductRange> cbpRange;
    private ComboBoxPanel<Employee> cbpPainter;
    private ComboBoxPanel<Employee> cbpConstructor;
    private ComboBoxPanel<Unit> cbpUnit;
    private ProductKind kind = new ProductKind();
    private ProductBrand brand = new ProductBrand();
    private ProductCollection collection = new ProductCollection();
    private ProductGroup group = new ProductGroup();
    private ProductRange range = new ProductRange();
    private Employee painter = new Employee();
    private Employee constructor = new Employee();
    private Unit unit = new Unit();
    private TableModel tModelStandards = null;
    private JTable tableStandards = null;
    private UCToolBar toolBarStandards = null;
    private TableModel tModelComposition = null;
    private JTable tableComposition = null;
    private UCToolBar toolBarComposition = null;
    private UCToolBar toolBarImage = null;
    private JScrollPane imageSmallPane;
    private JPanel imageSmallPanel;
    private JPopupMenu imagePopupMenu;
    private int selectedImageId;
    private ISubReferences srModelSize;
    private ModelProduct source = null;

    public ModelEditor(final IReference reference) {
        super(reference);
        this.controller = reference.getMainController();

        setPreferredSize(new Dimension(650, 555));

        init();
        initEvents();
    }

    /**
     * Инициализация компонентов
     */
    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //*************************************************************************
        // Вкладка Классификация
        //*************************************************************************

        JPanel generalPanel = new JPanel();
        generalPanel.setLayout(new MigLayout());

        generalPanel.add(new JLabel("Вид *:"), " wrap");
        cbpKind = new ComboBoxPanel<>(true, controller, MarchReferencesType.PRODUCT_KIND);
        generalPanel.add(cbpKind, "width 400:20:400, wrap");

        generalPanel.add(new JLabel("Бренд:"), "wrap");
        cbpBrand = new ComboBoxPanel<>(true, controller, MarchReferencesType.PRODUCT_BRAND);
        generalPanel.add(cbpBrand, "width 400:20:400,span 2, wrap");

        generalPanel.add(new JLabel("Коллекция:"), "wrap");
        cbpCollection = new ComboBoxPanel<>(true, controller, MarchReferencesType.PRODUCT_COLLECTION);
        generalPanel.add(cbpCollection, "width 400:20:400,span 2, wrap");

        generalPanel.add(new JLabel("Группа *:"), "wrap");
        cbpGroup = new ComboBoxPanel<>(true, controller, MarchReferencesType.PRODUCT_GROUP);
        generalPanel.add(cbpGroup, "width 400:20:400,span 2, wrap");

        generalPanel.add(new JLabel("Ассортимент *:"), "wrap");
        cbpRange = new ComboBoxPanel<>(true, controller, MarchReferencesType.PRODUCT_RANGE);
        generalPanel.add(cbpRange, "width 400:20:400,span 2, wrap");

        generalPanel.add(new JLabel(" Ед.изм. *:"), " wrap");
        cbpUnit = new ComboBoxPanel<>(true, controller, MarchReferencesType.UNIT);
        generalPanel.add(cbpUnit, "width 400:20:400, wrap");

        //*************************************************************************
        // Вкладка Размеры
        //*************************************************************************

        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BorderLayout());
        sizePanel.setPreferredSize(new Dimension(200, 200));

        srModelSize = new ModelSubSize(reference, sizePanel);

        //*************************************************************************
        // Вкладка Состав
        //*************************************************************************

        JPanel compositionPanel = new JPanel();
        compositionPanel.setLayout(new BorderLayout());

        tModelComposition = new GeneralTableModel(dataComposition, ProductRange.class);
        tableComposition = new JTable(tModelComposition);
        new TableFilterHeader(tableComposition, AutoChoices.ENABLED);
        tableComposition.setPreferredScrollableViewportSize(new Dimension(100, 100));
        tableComposition.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        compositionPanel.add(new JScrollPane(tableComposition), BorderLayout.CENTER);

        toolBarComposition = new UCToolBar();
        toolBarComposition.setVisibleSearchControls(false);
        toolBarComposition.getBtnEditItem().setVisible(false);
        toolBarComposition.getBtnReport().setVisible(false);
        toolBarComposition.setRight(RightEnum.WRITE);
        toolBarComposition.updateButton(0);
        compositionPanel.add(toolBarComposition, BorderLayout.NORTH);

        //*************************************************************************
        // Вкладка Стандарты
        //*************************************************************************

        JPanel standardsPanel = new JPanel();
        standardsPanel.setLayout(new BorderLayout());

        tModelStandards = new GeneralTableModel(dataStandards, Standard.class);
        tableStandards = new JTable(tModelStandards);
        new TableFilterHeader(tableStandards, AutoChoices.ENABLED);
        tableStandards.setPreferredScrollableViewportSize(new Dimension(100, 100));
        tableStandards.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        standardsPanel.add(new JScrollPane(tableStandards), BorderLayout.CENTER);

        toolBarStandards = new UCToolBar();
        toolBarStandards.setVisibleSearchControls(false);
        toolBarStandards.getBtnEditItem().setVisible(false);
        toolBarStandards.getBtnReport().setVisible(false);
        toolBarStandards.setRight(RightEnum.WRITE);
        toolBarStandards.updateButton(0);
        standardsPanel.add(toolBarStandards, BorderLayout.NORTH);

        //*************************************************************************
        // Вкладка Авторы
        //*************************************************************************

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new MigLayout());

        infoPanel.add(new JLabel("Художник:"), "wrap");
        cbpPainter = new ComboBoxPanel<>(true, controller, MarchReferencesType.COMPANY_EMPLOYEES);
        infoPanel.add(cbpPainter, "width 400:20:400,span 2, wrap");

        infoPanel.add(new JLabel("Конструктор:"), "wrap");
        cbpConstructor = new ComboBoxPanel<>(true, controller, MarchReferencesType.COMPANY_EMPLOYEES);
        infoPanel.add(cbpConstructor, "width 400:20:400,span 2, wrap");

        //*************************************************************************
        // Вкладка Изображения
        //*************************************************************************

        ActionListener menuListenerRemove = event -> deleteImage();

        ActionListener menuListenerOpen = event -> openImage(((ModelImage) dataImages.get(selectedImageId)));

        imagePopupMenu = new JPopupMenu();

        JMenuItem item;
        imagePopupMenu.add(item = new JMenuItem("Открыть"));
        item.addActionListener(menuListenerOpen);
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        imagePopupMenu.add(item = new JMenuItem("Удалить"));
        item.addActionListener(menuListenerRemove);
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        imagePopupMenu.setBorder(new BevelBorder(BevelBorder.RAISED));

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());

        toolBarImage = new UCToolBar();
        toolBarImage.setVisibleSearchControls(false);
        toolBarImage.getBtnEditItem().setVisible(false);
        toolBarImage.getBtnDeleteItem().setVisible(false);
        toolBarImage.getBtnReport().setVisible(false);
        imagePanel.add(toolBarImage, BorderLayout.NORTH);

        imageSmallPanel = new JPanel();
        imageSmallPanel.setLayout(new FlowLayout(0, 7, 0));
        // imageSmallPanel.setPreferredSize(new Dimension(250,100));

        imageSmallPane = new JScrollPane(imageSmallPanel);
        imageSmallPane.setBorder(javax.swing.BorderFactory
                .createTitledBorder(
                        null,
                        "Галерея",
                        javax.swing.border.TitledBorder.RIGHT,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new java.awt.Font("Dialog", Font.ITALIC, 12)));

        imagePanel.add(imageSmallPane, BorderLayout.CENTER);

        //*************************************************************************
        // Основная панель
        //*************************************************************************

        JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
        tabbedPane.addTab("Классификация", generalPanel);
        tabbedPane.addTab("Размеры", sizePanel);
        tabbedPane.addTab("Состав", compositionPanel);
        tabbedPane.addTab("Описание", new JScrollPane(jtDescription));
        tabbedPane.addTab("Стандарты", standardsPanel);
        tabbedPane.addTab("Авторы", infoPanel);
        tabbedPane.addTab("Изображения", imagePanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new MigLayout());

        centerPanel.add(new JLabel("Модель *: "));
        centerPanel.add(tfModel, "width 250:24:250, wrap");

        centerPanel.add(new JLabel("Наименование *: "));
        centerPanel.add(tfName, "width 500:24:500, wrap");

        centerPanel.add(new JLabel("Примечание:"));
        centerPanel.add(new JScrollPane(jtNote), "width 500:20:500, height 80, wrap");

        add(centerPanel, BorderLayout.CENTER);
        add(tabbedPane, BorderLayout.SOUTH);
    }

    /**
     * Инициализация событий
     */
    private void initEvents() {
        cbpKind.addButtonActionListener(e -> kind = cbpKind.selectFromReference(false));

        cbpKind.addComboBoxActionListener(e -> kind = cbpKind.getSelectedItem());

        cbpBrand.addButtonActionListener(e -> brand = cbpBrand.selectFromReference(false));

        cbpBrand.addComboBoxActionListener(e -> brand = cbpBrand.getSelectedItem());

        cbpCollection.addButtonActionListener(e -> collection = cbpCollection.selectFromReference(false));

        cbpCollection.addComboBoxActionListener(e -> collection = cbpCollection.getSelectedItem());

        cbpGroup.addButtonActionListener(e -> group = cbpGroup.selectFromReference(false));

        cbpGroup.addComboBoxActionListener(e -> group = cbpGroup.getSelectedItem());

        cbpRange.addButtonActionListener(e -> range = cbpRange.selectFromReference(false));

        cbpRange.addComboBoxActionListener(e -> range = cbpRange.getSelectedItem());

        cbpPainter.addButtonActionListener(e -> painter = cbpPainter.selectFromReference(false));

        cbpPainter.addComboBoxActionListener(e -> painter = cbpPainter.getSelectedItem());

        cbpConstructor.addButtonActionListener(e -> constructor = cbpConstructor.selectFromReference(false));

        cbpConstructor.addComboBoxActionListener(e -> constructor = cbpConstructor.getSelectedItem());

        cbpUnit.addButtonActionListener(e -> unit = cbpUnit.selectFromReference(false));

        cbpUnit.addComboBoxActionListener(e -> unit = cbpUnit.getSelectedItem());

        toolBarStandards.getBtnNewItem().addActionListener(e -> addStandardItem());

        toolBarStandards.getBtnDeleteItem().addActionListener(e -> {
            final int row = tableStandards.getSelectedRow();
            if (row < 0) {
                return;
            }
            deleteStandardItem(tableStandards.getSelectedRow());
        });


        toolBarComposition.getBtnNewItem().addActionListener(e -> addCompositionItem());

        toolBarComposition.getBtnDeleteItem().addActionListener(e -> {
            final int row = tableComposition.getSelectedRow();
            if (row < 0) {
                return;
            }
            deleteCompositionItem(tableComposition.getSelectedRow());
        });

        tableComposition.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent me) {
                final JTable table = (JTable) me.getSource();
                final Point p = me.getPoint();
                final int row = table.rowAtPoint(p);
/*                if (row < 0) {
                    return;
                }*/
            }
        });

        toolBarImage.getBtnNewItem().addActionListener(e -> addImage());

        toolBarImage.getBtnDeleteItem().addActionListener(e -> deleteImage());
    }

    @Override
    public Object getSourceEntity() {
        if (cbpKind.getSelectedItem() != null) {
            System.out.println(kind.toString());
            source.setKind(kind);
        }

        source.setBrand(brand);

        source.setCollection(collection);

        if (cbpGroup.getSelectedItem() != null) {
            System.out.println(group.toString());
            source.setGroup(group);
        }

        if (cbpRange.getSelectedItem() != null) {
            System.out.println(range.toString());
            source.setRange(range);
        }

        source.setPainter(painter);

        source.setConstructor(constructor);

        if (cbpUnit.getSelectedItem() != null) {
            System.out.println(unit.toString());
            source.setUnit(unit);
        }

        source.setModel(tfModel.getText().trim());
        source.setName(tfName.getText().trim().toUpperCase());
        source.setDescription(jtDescription.getText().trim());
        source.setNote(jtNote.getText().trim());

        source.setModelSize((Set<ModelSizeChart>) srModelSize.getData());

        source.setComposition(new HashSet(dataComposition));
        source.setStandards(new HashSet(dataStandards));

        source.setModelImages(new HashSet(dataImages));

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new ModelProduct();

            kind = new ProductKind();
            brand = new ProductBrand();
            collection = new ProductCollection();
            group = new ProductGroup();
            range = new ProductRange();
            painter = new Employee();
            constructor = new Employee();
            unit = new Unit();

        } else {
            this.source = (ModelProduct) object;

            kind = this.source.getKind();
            brand = this.source.getBrand();
            collection = this.source.getCollection();
            group = this.source.getGroup();
            range = this.source.getRange();
            painter = this.source.getPainter();
            constructor = this.source.getConstructor();
            unit = this.source.getUnit();

        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        tfModel.setText(this.source.getModel());
        tfName.setText(this.source.getName());

        cbpKind.preset(kind);
        cbpBrand.preset(brand);
        cbpCollection.preset(collection);
        cbpGroup.preset(group);
        cbpRange.preset(range);
        cbpPainter.preset(painter);
        cbpConstructor.preset(constructor);
        cbpUnit.preset(unit);

        jtDescription.setText(this.source.getDescription());
        jtNote.setText(this.source.getNote());

        dataStandards.clear();
        dataStandards.addAll(this.source.getStandards());
        updateTableStandards();

        dataComposition.clear();
        dataComposition.addAll(this.source.getComposition());
        updateTableComposition();

        dataImages.clear();
        dataImages.addAll(this.source.getModelImages());
        showGalleryImages();

        srModelSize.setData(source.getModelSize());
        srModelSize.setSourceEntity(source);
    }

    @Override
    public boolean verificationData() {
        if (tfModel.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать модель изделия", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tfModel.requestFocusInWindow();
            return false;
        }
        if (tfName.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать наименование модели изделия", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false;
        }
        if (cbpKind.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать вид модели изделия", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpKind.requestFocusInWindow();
            return false;
        }
        if (cbpGroup.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать группу модели изделия", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpGroup.requestFocusInWindow();
            return false;
        }
        if (cbpRange.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать ассортимент модели изделия", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpRange.requestFocusInWindow();
            return false;
        }
        if (cbpUnit.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать ед.изм.", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpUnit.requestFocusInWindow();
            return false;
        }
        return true;
    }

    public void updateTableStandards() {
        ((AbstractTableModel) tModelStandards).fireTableDataChanged();
        toolBarStandards.updateButton(dataStandards.size());
    }

    private void addStandardItem() {
        final Reference ref = new Reference(
                controller,
                MarchReferencesType.CODELIST,
                MarchWindowType.PICKFRAME);

        final Standard item = (Standard) ref.showPickFrame();

        if (item != null) {
            dataStandards.add(item);
            updateTableStandards();
        }
    }

    private void deleteStandardItem(int selectedRow) {
        final int answer = JOptionPane.showOptionDialog(
                null,
                "Удалить запись?\n"
                        + dataStandards.get(selectedRow).toString(),
                "Удаление записи",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Да", "Нет"},
                "Нет");

        if (answer == 0) {
            dataStandards.remove(selectedRow);
            updateTableStandards();
        }
    }

    public void updateTableComposition() {
        ((AbstractTableModel) tModelComposition).fireTableDataChanged();
        toolBarComposition.updateButton(dataComposition.size());
    }

    private void addCompositionItem() {
        final Reference ref = new Reference(
                controller,
                MarchReferencesType.PRODUCT_RANGE,
                MarchWindowType.PICKFRAME);

        final ProductRange item = (ProductRange) ref.showPickFrame();

        if (item != null) {
            dataComposition.add(item);
            updateTableComposition();
        }
    }

    private void deleteCompositionItem(int selectedRow) {
        final int answer = JOptionPane.showOptionDialog(
                null,
                "Удалить запись?\n "
                        + dataComposition.get(selectedRow).toString(),
                "Удаление записи",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Да", "Нет"},
                "Нет");

        if (answer == 0) {
            dataComposition.remove(selectedRow);
            updateTableComposition();
        }
    }

    private void addImage() {
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
                                            .read(file), imageSmallPane.getWidth(), imageSmallPane.getHeight()));

                    ByteArrayOutputStream out2 = UtilImage
                            .compressImage(UtilImage
                                    .resizeImage(ImageIO
                                            .read(file), 1024, 1024));

                    final ModelImage newImageFull = new ModelImage();
                    newImageFull.setModel(source);
                    newImageFull.setName(file.getName());
                    newImageFull.setImageSmall(out1.toByteArray());
                    newImageFull.setImageFull(out2.toByteArray());

                    dataImages.add(newImageFull);
                } catch (Exception ex) {
                }

                showGalleryImages();

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

    private void deleteImage() {
        try {
            dataImages.remove(selectedImageId);
            imageSmallPanel.removeAll();
            imageSmallPanel.repaint();
            showGalleryImages();

        } catch (Exception ex) {
        }
    }

    private void openImage(ModelImage modelImage) {
        try {
            new ImageDialog(controller, modelImage);

        } catch (Exception ex) {
        }
    }

    private void showGalleryImages() {
        try {
            imageSmallPanel.removeAll();

            for (int i = 0; i < dataImages.size(); i++) {
                ModelImage imageModel = (ModelImage) dataImages.get(i);

                imageLabel = new ImageLabel();
                imageLabel.setId(i);
                imageLabel.setImageModel(imageModel);
                imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        try {
                            if (evt.getClickCount() == 2) {
                                openImage(((ImageLabel) evt.getComponent()).getImageModel());
                            }
                        } catch (Exception ex) {

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
                            imagePopupMenu.show(event.getComponent(), event.getX(), event.getY());
                            selectedImageId = ((ImageLabel) event.getComponent()).getId();
                        }
                    }
                });

                imageLabel.setIcon(UtilImage
                        .resizeImage(ImageIO
                                        .read(new ByteArrayInputStream(imageModel.getImageSmall())),
                                imageSmallPanel.getWidth(),
                                imageSmallPanel.getHeight()));
                imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                imageSmallPanel.add(imageLabel);
            }

            imageSmallPanel.revalidate();

        } catch (Exception ex) {

        }
    }
}
