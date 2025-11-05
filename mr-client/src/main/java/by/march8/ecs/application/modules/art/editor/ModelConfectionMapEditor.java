package by.march8.ecs.application.modules.art.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.ISubReferences;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.art.subeditor.ModelConfectionMapSubCanvas;
import by.march8.ecs.application.modules.art.util.UtilArt;
import by.march8.ecs.framework.sdk.reference.Reference;
import by.march8.entities.product.ConfectionMap;
import by.march8.entities.product.ConfectionMapCanvas;
import by.march8.entities.product.ModelMaterial;
import by.march8.entities.product.ModelProduct;
import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lidashka.
 */
public class ModelConfectionMapEditor extends EditingPane {

    final JDateChooser jcDate = new JDateChooser();
    private final JTextField tfNumbel = new JTextField();
    private final ArrayList<Object> dataMaterials = new ArrayList<>();
    private final MainController controller;
    private ComboBoxPanel<ModelProduct> cbpModelProduct;
    private ModelProduct modelProduct = new ModelProduct();
    private JTextPane jtMaterials = new JTextPane();
    private JTextPane jtNote = new JTextPane();
    private UCToolBar toolBarMaterials = null;
    private ISubReferences srConfectionMap;

    private ConfectionMap source = null;

    public ModelConfectionMapEditor(final IReference reference) {
        super(reference);
        this.controller = reference.getMainController();

        setPreferredSize(new Dimension(600, 600));

        init();
        initEvents();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        cbpModelProduct = new ComboBoxPanel<>(true, controller, MarchReferencesType.MODEL_PRODUCT);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new MigLayout());
        northPanel.add(new JLabel("№ карты *:"));
        //northPanel.add(new JLabel("Дата создания *:"), "gap 30px, wrap");
        northPanel.add(new JLabel("Модель изделия *:"), "gap 30px, wrap");
        northPanel.add(tfNumbel, "width 120:20:120, height 25");
        northPanel.add(cbpModelProduct, "gap 30px, width 420:20:420, wrap");
        //northPanel.add(jcDate, "gap 30px, width 120:20:120, wrap");

        JPanel canvasPanel = new JPanel();
        canvasPanel.setLayout(new BorderLayout());

        srConfectionMap = new ModelConfectionMapSubCanvas(reference, canvasPanel);

        toolBarMaterials = new UCToolBar();
        toolBarMaterials.setVisibleSearchControls(false);
        toolBarMaterials.getBtnEditItem().setVisible(false);
        toolBarMaterials.getBtnDeleteItem().setVisible(false);
        toolBarMaterials.getBtnReport().setVisible(false);
        toolBarMaterials.setRight(RightEnum.WRITE);
        toolBarMaterials.updateButton(0);

        JPanel materialsPanel = new JPanel();
        materialsPanel.setLayout(new BorderLayout());
        materialsPanel.add(new JScrollPane(jtMaterials), BorderLayout.CENTER);
        materialsPanel.add(toolBarMaterials, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
        tabbedPane.addTab("Полотно", canvasPanel);
        tabbedPane.addTab("Прикладные материалы", materialsPanel);

        jtNote.setPreferredSize(new Dimension(50, 50));

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        southPanel.add(new JLabel("Примечание:"), BorderLayout.NORTH);
        southPanel.add(new JScrollPane(jtNote), BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void initEvents() {
        cbpModelProduct.addButtonActionListener(e -> modelProduct = cbpModelProduct.selectFromReference(false));

        cbpModelProduct.addComboBoxActionListener(e -> modelProduct = cbpModelProduct.getSelectedItem());

        toolBarMaterials.getBtnNewItem().addActionListener(e -> addMaterialsItem());
    }

    @Override
    public Object getSourceEntity() {
        source.setNumber(Integer.valueOf(tfNumbel.getText().trim()));

        source.setDate(jcDate.getDate());

        if (cbpModelProduct.getSelectedItem() != null) {
            source.setModel(modelProduct);
        }

        source.setConfectionMapCanvases((Set<ConfectionMapCanvas>) srConfectionMap.getData());

        source.setModelMaterials(new HashSet(dataMaterials));

        source.setNote(jtNote.getText().trim());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new ConfectionMap();

            modelProduct = new ModelProduct();

            jcDate.setDate(UtilArt.TODAY);

        } else {
            this.source = (ConfectionMap) object;

            modelProduct = this.source.getModel();

            jcDate.setDate(source.getDate());
        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        tfNumbel.setText(String.valueOf(this.source.getNumber()));

        cbpModelProduct.preset(modelProduct);

        srConfectionMap.setData(source.getConfectionMapCanvases());
        srConfectionMap.setSourceEntity(source);

        jtNote.setText(this.source.getNote());
    }

    @Override
    public boolean verificationData() {
        try {
            Integer.valueOf(tfNumbel.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Некорректно задано значение \"№ карты\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfNumbel.requestFocusInWindow();
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

        if (cbpModelProduct.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать модель изделия", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpModelProduct.requestFocusInWindow();
            return false;
        }

        return true;
    }

    private void addMaterialsItem() {
        final Reference ref = new Reference(
                controller,
                MarchReferencesType.MODEL_MATERIAL,
                MarchWindowType.PICKFRAME);

        final ModelMaterial item = (ModelMaterial) ref.showPickFrame();

        if (item != null) {
            jtMaterials.setText(jtMaterials.getText() + "\n" + item.getName() + " ");
        }
    }

}
