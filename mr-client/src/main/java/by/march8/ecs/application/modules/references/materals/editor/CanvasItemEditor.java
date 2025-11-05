package by.march8.ecs.application.modules.references.materals.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.ISubReferences;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.interfaces.IGridToolTipEvent;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.application.modules.references.materals.subreference.CanvasComponentSubReference;
import by.march8.ecs.application.modules.references.materals.subreference.CanvasMarkingComponentSubReference;
import by.march8.ecs.framework.helpers.FormatUtils;
import by.march8.entities.classifier.EquipmentItem;
import by.march8.entities.materials.CanvasComponent;
import by.march8.entities.materials.CanvasItem;
import by.march8.entities.materials.CanvasMarkingComposition;
import by.march8.entities.materials.CanvasModifier;
import by.march8.entities.materials.CanvasParameter;
import by.march8.entities.materials.CanvasWeave;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 * Панель редактирования Полотна.
 * <p/>!!!Не адаптирована под новую структуру сущности
 *
 * @author andy-linux
 * @see by.march8.entities.materials.CanvasItem
 * @see by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane
 */
public class CanvasItemEditor extends EditingPane implements IGridToolTipEvent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Color COLOR_HEADER = Color.blue.darker().darker()
            .darker();

    private final JTabbedPane tabPanel = new JTabbedPane();
    private final JLabel lArticle = new JLabel("Артикульный номер : *");
    private final JLabel lCipher = new JLabel("Шифр артикула :");
    private final JLabel lC1 = new JLabel("Усадки");
    private final JLabel lC2 = new JLabel("Пересчета");
    private final JLabel lC3 = new JLabel("Потерь");
    private final JLabel lWeight = new JLabel("Вес 1 м. полотна");
    private final JLabel lWidth = new JLabel("Ширина полотна");
    private final JLabel lQuantity = new JLabel("Кол-во нитей");
    private final JLabel lPercent = new JLabel("Процент нити");
    private final JLabel lWStencil = new JLabel("Трафарет");
    private final JLabel lWHem = new JLabel("Кромка");
    private final JLabel lWPatch = new JLabel("Лоскут");
    private final JLabel lWDefect = new JLabel("Дефекты");
    private final JLabel lWRewind = new JLabel("Перемотка");
    private final JLabel lWColoring = new JLabel("Покраска");
    private final JLabel lWKnitting = new JLabel("Вязание");
    private final JLabel lYarn = new JLabel("Наименование пряжи");
    private final JLabel lCanvasModification = new JLabel("Модификатор полотна");
    private final JLabel lCanvasColorPalette = new JLabel("Цвет полотна");
    private final JLabel lCanvasDecoration = new JLabel("Тип набивки полотна");

    private final JTextField tfArticle = new JTextField("");
    private final JFormattedTextField tfCipher = new JFormattedTextField(
            FormatUtils.getCustomFormat("47######"));


    private final JTextField tfLossTemplates = new JTextField();
    private final JTextField tfLossEdge = new JTextField();
    private final JTextField tfLossFlap = new JTextField();
    private final JTextField tfLossKnitting = new JTextField();
    private final JTextField tfWLossDefect = new JTextField();
    private final JTextField tfLossPainting = new JTextField();
    private final JTextField tfLossRewind = new JTextField();
    private final JTextField tfWeight = new JTextField();
    private final JTextField tfWidth = new JTextField();
    private final JTextField tfStrandsQuantity = new JTextField();
    private final JTextField tfStrandsPercentage = new JTextField();
    private final JTextField tfFactorShrinkage = new JTextField();
    private final JTextField tfFactorConversion = new JTextField();
    private final JTextField tfFactorLoss = new JTextField();

    // private final JComboBox<CanvasModifier> cbCanvasModifier = new JComboBox<>();
    private final ComboBoxPanel<CanvasModifier> cbpCanvasModifier;
    private final ComboBoxPanel<CanvasWeave> cbpCanvasWeave;
    private final ComboBoxPanel<EquipmentItem> cbpEquipment;

    private CanvasItem source = null;
    private CanvasItem parent;
    private CanvasWeave weave;
    private CanvasModifier modifier;
    private EquipmentItem equipment;
    private CanvasParameter canvasParameter;

    private ISubReferences canvasComponentSub;
    private ISubReferences canvasMarkingComponentSub;


    public CanvasItemEditor(final IReference reference) {
        super(reference);
        cbpCanvasModifier = new ComboBoxPanel<CanvasModifier>(reference.getMainController(), MarchReferencesType.MATERIAL_CANVAS_MODIFIER);

        cbpCanvasWeave = new ComboBoxPanel<CanvasWeave>(reference.getMainController(), MarchReferencesType.MATERIAL_CANVAS_WEAVE);
        cbpEquipment = new ComboBoxPanel<>(reference.getMainController(), MarchReferencesType.EQUIPMENT);

        setPreferredSize(new Dimension(700, 440));
        setLayout(new BorderLayout());
        initEditor();
        initEvents();

    }

    @Override
    public Object getSourceEntity() {

        source.setCipher(Integer.parseInt(tfCipher.getText()));
        source.setArticle(tfArticle.getText());

        canvasParameter.setWidth(Integer.valueOf(tfWidth.getText()));
        canvasParameter.setWeight(Integer.valueOf(tfWeight.getText()));

        canvasParameter.setFactorShrinkage(Float.valueOf(tfFactorShrinkage.getText()));
        canvasParameter.setFactorConversion(Float.valueOf(tfFactorConversion.getText()));
        canvasParameter.setFactorLoss(Float.valueOf(tfFactorLoss.getText()));

        canvasParameter.setStrandsQuantity(Integer.valueOf(tfStrandsQuantity.getText()));
        canvasParameter.setStrandsPercentage(Float.valueOf(tfStrandsPercentage.getText()));

        canvasParameter.setLossTemplates(Float.valueOf(tfLossTemplates.getText()));
        canvasParameter.setLossEdge(Float.valueOf(tfLossEdge.getText()));
        canvasParameter.setLossFlap(Float.valueOf(tfLossFlap.getText()));
        canvasParameter.setLossKnitting(Float.valueOf(tfLossKnitting.getText()));
        canvasParameter.setLossDefect(Float.valueOf(tfWLossDefect.getText()));
        canvasParameter.setLossPainting(Float.valueOf(tfLossPainting.getText()));
        canvasParameter.setLossRewind(Float.valueOf(tfLossRewind.getText()));

        source.setCanvasParameter(canvasParameter);
        source.setCanvasModifier(modifier);
        source.setCanvasWeave(weave);
        source.setEquipment(equipment);
        source.setParent(source);
        source.setComponents((Set<CanvasComponent>) canvasComponentSub.getData());
        source.setMarkingComponents((Set<CanvasMarkingComposition>) canvasMarkingComponentSub.getData());
        //source.setYarn(yarn);

        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {

        tabPanel.setSelectedIndex(0);

        if (object == null) {
            source = new CanvasItem();
            modifier = cbpCanvasModifier.getItem(0);
            weave = cbpCanvasWeave.getItem(0);
            equipment = new EquipmentItem();
            canvasParameter = new CanvasParameter();

            tfArticle.setText("");
            tfCipher.setText("0");

            tfWidth.setText("0");
            tfWeight.setText("0");

            tfFactorShrinkage.setText("0.0");
            tfFactorConversion.setText("0.0");
            tfFactorLoss.setText("0.0");

            tfStrandsQuantity.setText("0");
            tfStrandsPercentage.setText("100");

            tfLossTemplates.setText("0.0");
            tfLossEdge.setText("0.0");
            tfLossFlap.setText("0.0");
            tfLossKnitting.setText("0.0");
            tfWLossDefect.setText("0.0");
            tfLossPainting.setText("0.0");
            tfLossRewind.setText("0.0");
            parent = source;
        } else {
            source = (CanvasItem) object;

            canvasParameter = source.getCanvasParameter();

            modifier = source.getCanvasModifier();
            weave = source.getCanvasWeave();
            parent = source.getParent();
            equipment = source.getEquipment();

            tfArticle.setText(source.getArticle());
            tfCipher.setValue(source.getCipher());

            tfWidth.setText(String.valueOf(canvasParameter.getWidth()));
            tfWeight.setText(String.valueOf(canvasParameter.getWeight()));

            tfFactorShrinkage.setText(String.valueOf(canvasParameter.getFactorShrinkage()));
            tfFactorConversion.setText(String.valueOf(canvasParameter.getFactorConversion()));
            tfFactorLoss.setText(String.valueOf(canvasParameter.getFactorLoss()));

            tfStrandsQuantity
                    .setText(String.valueOf(canvasParameter.getStrandsQuantity()));
            tfStrandsPercentage.setText(String.valueOf(canvasParameter.getStrandsPercentage()));

            tfLossTemplates.setText(String.valueOf(canvasParameter.getLossTemplates()));
            tfLossEdge.setText(String.valueOf(canvasParameter.getLossEdge()));
            tfLossFlap.setText(String.valueOf(canvasParameter.getLossFlap()));
            tfLossKnitting.setText(String.valueOf(canvasParameter.getLossKnitting()));
            tfWLossDefect.setText(String.valueOf(canvasParameter.getLossDefect()));
            tfLossPainting.setText(String.valueOf(canvasParameter.getLossPainting()));
            tfLossRewind.setText(String.valueOf(canvasParameter.getLossRewind()));

        }
        presetComboBox();
        canvasComponentSub.setData(source.getComponents());
        canvasComponentSub.setSourceEntity(source);

        canvasMarkingComponentSub.setData(source.getMarkingComponents());
        canvasMarkingComponentSub.setSourceEntity(source);

    }

    @Override
    public boolean verificationData() {

        if (tfArticle.getText().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать артикул полотна", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tfArticle.requestFocusInWindow();
            return false;
        }

        if (!verificationValue(tfWidth, Integer.class)) {
            return false;
        }

        if (!verificationValue(tfWeight, Integer.class)) {
            return false;
        }

        if (!verificationValue(tfStrandsQuantity, Integer.class)) {
            return false;
        }

        if (!verificationValue(tfStrandsPercentage, Float.class)) {
            return false;
        }

        if (!verificationValue(tfFactorShrinkage, Float.class)) {
            return false;
        }

        if (!verificationValue(tfLossTemplates, Float.class)) {
            return false;
        }

        if (!verificationValue(tfLossTemplates, Float.class)) {
            return false;
        }

        if (!verificationValue(tfLossEdge, Float.class)) {
            return false;
        }
        if (!verificationValue(tfLossFlap, Float.class)) {
            return false;
        }
        if (!verificationValue(tfLossKnitting, Float.class)) {
            return false;
        }
        if (!verificationValue(tfWLossDefect, Float.class)) {
            return false;
        }
        if (!verificationValue(tfLossPainting, Float.class)) {
            return false;
        }

        if (!verificationValue(tfLossRewind, Float.class)) {
            return false;
        }

        if (canvasComponentSub.getData().size() < 1) {
            JOptionPane.showMessageDialog(null,
                    "Полотно должно содержать как минимум один вид пряжи", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tabPanel.setSelectedIndex(0);
            return false;
        }

        float totalPercent = 0;
        for (Object object : canvasComponentSub.getData()) {
            totalPercent += ((CanvasComponent) object).getPercent();
        }

        if ((totalPercent < 100) || (totalPercent > 100)) {
            JOptionPane.showMessageDialog(null,
                    "Полное процентное содержание всех компонентов полотна всегда 100%", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tabPanel.setSelectedIndex(0);
            return false;
        }

        if (canvasMarkingComponentSub.getData().size() < 1) {
            JOptionPane.showMessageDialog(null,
                    "Маркировочный состав должен иметь как минимум один вид пряжи", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tabPanel.setSelectedIndex(1);
            return false;
        }

        totalPercent = 0;
        for (Object object : canvasMarkingComponentSub.getData()) {
            totalPercent += ((CanvasMarkingComposition) object).getPercent();
        }

        if (totalPercent > 100) {
            JOptionPane.showMessageDialog(null,
                    "Полное процентное содержание компонентов маркировочного состава полотна всегда 100%", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            tabPanel.setSelectedIndex(1);
            return false;
        }

        return true;
    }

    private boolean verificationValue(JTextField component, Class<?> c) {
        if (c == Float.class) {
            try {
                Float.parseFloat(component.getText());
                return true;
            } catch (NumberFormatException nfe) {
                System.out.println("Ошибка приведения к Float для " + component.getName() + " [" + component.getText() + "]");
            }
        } else if (c == Integer.class) {
            try {
                Integer.parseInt(component.getText());
                return true;
            } catch (NumberFormatException nfe) {
                System.out.println("Ошибка приведения к Integer для " + component.getName() + " [" + component.getText() + "]");
            }
        }
        JOptionPane.showMessageDialog(null,
                "Проверьте введенные данные", "Ошибка ввода!",
                JOptionPane.ERROR_MESSAGE);
        component.requestFocusInWindow();
        return false;
    }

    private void presetComboBox() {
        cbpCanvasModifier.preset(modifier);
        cbpCanvasWeave.preset(weave);
        cbpEquipment.preset(equipment);
    }

    private void initEditor() {
        JPanel pHeader = new JPanel(null);
        pHeader.setPreferredSize(new Dimension(0, 70));
        add(pHeader, BorderLayout.NORTH);

        TitledBorder tbCoefficient, tbWaste, tbGeneral;
        tbCoefficient = BorderFactory.createTitledBorder("Коэффициенты");
        tbWaste = BorderFactory.createTitledBorder("Потери");
        tbGeneral = BorderFactory.createTitledBorder("Основные");

        JPanel pParams = new JPanel(new BorderLayout());
        JPanel pParamsHeader = new JPanel(null);
        pParamsHeader.setPreferredSize(new Dimension(0, 45));
        JPanel pParamsContent = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pParams.add(pParamsHeader, BorderLayout.NORTH);
        pParams.add(pParamsContent, BorderLayout.CENTER);

        JPanel pComponentParent = new JPanel(new BorderLayout());
        JPanel pComponentTools = new JPanel(new MigLayout());

        pComponentTools.setPreferredSize(new Dimension(0, 35));
        pComponentParent.add(pComponentTools, BorderLayout.NORTH);


        pComponentTools.add(new JLabel("Используемое оборудование :"));
        pComponentTools.add(cbpEquipment, "width 315:20:315, height 20:20, wrap");
        cbpEquipment.getSelectButton().setVisible(false);

        JPanel pComponent = new JPanel();
        pComponentParent.add(pComponent, BorderLayout.CENTER);

        JPanel pMarkingComponent = new JPanel();
        JPanel pModifier = new JPanel(null);

        tabPanel.addTab("Производственный состав", pComponentParent);
        tabPanel.addTab("Маркировочный состав", pMarkingComponent);
        tabPanel.addTab("Модификаторы", pModifier);
        tabPanel.addTab("Параметры", pParams);

        System.err.println("canvasComponentSub");
        canvasComponentSub = new CanvasComponentSubReference(reference, pComponent);
        System.err.println("canvasMarkingComponentSub");
        canvasMarkingComponentSub = new CanvasMarkingComponentSubReference(reference, pMarkingComponent);

        add(tabPanel, BorderLayout.CENTER);

        lArticle.setBounds(10, 10, 250, 20);
        lCipher.setBounds(10, 40, 250, 20);

        tfArticle.setBounds(270, 10, 350, 20);
        tfCipher.setBounds(270, 40, 350, 20);

        pHeader.add(lArticle);
        pHeader.add(lCipher);
        pHeader.add(tfArticle);
        pHeader.add(tfCipher);

        tbCoefficient.setTitleColor(COLOR_HEADER);
        tbWaste.setTitleColor(COLOR_HEADER);
        tbGeneral.setTitleColor(COLOR_HEADER);

        final JPanel pGeneral = new JPanel(new GridLayout(11, 2));
        pGeneral.setPreferredSize(new Dimension(200, 250));
        pGeneral.setBorder(tbGeneral);
        pGeneral.add(lWeight);
        pGeneral.add(tfWeight);
        pGeneral.add(lWidth);
        pGeneral.add(tfWidth);
        pGeneral.add(new JLabel());
        pGeneral.add(new JLabel());
        pGeneral.add(lQuantity);
        pGeneral.add(tfStrandsQuantity);
        pGeneral.add(lPercent);
        pGeneral.add(tfStrandsPercentage);
        pParamsContent.add(pGeneral);

        final JPanel pCoefficient = new JPanel(new GridLayout(11, 2));
        pCoefficient.setPreferredSize(new Dimension(210, 250));
        pCoefficient.setBorder(tbCoefficient);
        pCoefficient.add(lC1);
        pCoefficient.add(tfFactorShrinkage);
        pCoefficient.add(lC2);
        pCoefficient.add(tfFactorConversion);
        pCoefficient.add(lC3);
        pCoefficient.add(tfFactorLoss);
        pParamsContent.add(pCoefficient);

        final JPanel pWaste = new JPanel(new GridLayout(11, 2));
        pWaste.setPreferredSize(new Dimension(200, 250));
        pWaste.setBorder(tbWaste);

        pWaste.add(lWStencil);
        pWaste.add(tfLossTemplates);
        pWaste.add(lWHem);
        pWaste.add(tfLossEdge);
        pWaste.add(lWPatch);
        pWaste.add(tfLossFlap);
        pWaste.add(lWKnitting);
        pWaste.add(tfLossKnitting);
        pWaste.add(lWDefect);
        pWaste.add(tfWLossDefect);
        pWaste.add(lWColoring);
        pWaste.add(tfLossPainting);
        pWaste.add(lWRewind);
        pWaste.add(tfLossRewind);

        pParamsContent.add(pWaste);

        lCanvasModification.setBounds(10, 10, 200, 20);
        lCanvasColorPalette.setBounds(10, 40, 200, 20);
        lCanvasDecoration.setBounds(10, 70, 200, 20);

        cbpCanvasModifier.setBounds(210, 10, 405, 20);
        cbpCanvasWeave.setBounds(210, 70, 405, 20);

        pModifier.add(lCanvasModification);
        pModifier.add(lCanvasColorPalette);
        pModifier.add(lCanvasDecoration);
        pModifier.add(cbpCanvasModifier);
        pModifier.add(cbpCanvasWeave);

        canvasComponentSub.getGridViewPort().addToolTipHandler(this);
    }

    /**
     * Инициализация событий на панели
     */
    private void initEvents() {
        cbpCanvasModifier.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modifier = cbpCanvasModifier.getSelectedItem();
            }
        });

        cbpCanvasWeave.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                weave = cbpCanvasWeave.getSelectedItem();
            }
        });

        cbpEquipment.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                equipment = cbpEquipment.getSelectedItem();
            }
        });

        cbpCanvasModifier.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modifier = cbpCanvasModifier.selectFromReference(modifier);
            }
        });

        cbpCanvasWeave.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                weave = cbpCanvasWeave.selectFromReference(weave);
            }
        });

        cbpEquipment.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                equipment = cbpEquipment.selectFromReference(equipment);
            }
        });
    }

    @Override
    public String onToolTipActivated(final Object object, final int row, final int column, final int rowModel) {
        CanvasComponent component = (CanvasComponent) canvasComponentSub.getDataArray().get(rowModel);
        return component.getYarn().getComponentsAsHTML();
    }
}