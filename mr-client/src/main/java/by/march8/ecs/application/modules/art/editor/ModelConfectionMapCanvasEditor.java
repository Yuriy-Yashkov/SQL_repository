package by.march8.ecs.application.modules.art.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.materials.CanvasItem;
import by.march8.entities.product.ConfectionMapCanvas;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 02.04.2015.
 */
public class ModelConfectionMapCanvasEditor extends EditingPane {

    private ComboBoxPanel<CanvasItem> cbpCanvas;

    private CanvasItem canvasItem = new CanvasItem();

    private JTextField tfNumCanvas;

    private JRadioButton rbKindCanvas0;
    private JRadioButton rbKindCanvas1;

    private ButtonGroup buttonGroup;

    private ConfectionMapCanvas source = null;

    public ModelConfectionMapCanvasEditor(final IReference reference) {
        super(reference);
        this.controller = reference.getMainController();
        init();
        initEvents();
    }

    /**
     * Инициализация компонентов
     */
    private void init() {
        setPreferredSize(new Dimension(450, 200));
        setLayout(new MigLayout());

        tfNumCanvas = new JTextField();
        add(new JLabel("№ полотна *: "), " wrap");
        add(tfNumCanvas, "width 200:24:200, wrap");

        cbpCanvas = new ComboBoxPanel<>(true, controller, MarchReferencesType.MATERIAL_CANVAS);
        add(new JLabel("Полотно *:"), " wrap");
        add(cbpCanvas, "width 420:20:420,wrap");

        rbKindCanvas0 = new JRadioButton();
        rbKindCanvas0.setText("Основное;");
        rbKindCanvas0.setActionCommand("0");
        rbKindCanvas0.setSelected(true);

        rbKindCanvas1 = new JRadioButton();
        rbKindCanvas1.setText("Отделочное;");
        rbKindCanvas1.setActionCommand("1");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rbKindCanvas0);
        buttonGroup.add(rbKindCanvas1);

        JPanel panelKindCanvas = new JPanel();
        panelKindCanvas.setLayout(new GridLayout(0, 2, 5, 5));
        panelKindCanvas.add(rbKindCanvas0);
        panelKindCanvas.add(rbKindCanvas1);

        add(new JLabel("Вид полотна *: "), " wrap");
        add(panelKindCanvas, "width 300:24:300, wrap");
    }

    /**
     * Инициализация событий
     */
    private void initEvents() {
        cbpCanvas.addButtonActionListener(e -> canvasItem = cbpCanvas.selectFromReference(false));

        cbpCanvas.addComboBoxActionListener(e -> canvasItem = cbpCanvas.getSelectedItem());
    }

    @Override
    public Object getSourceEntity() {
        source.setNumber(Integer.valueOf(tfNumCanvas.getText().trim()));

        if (cbpCanvas.getSelectedItem() != null) {
            System.out.println(canvasItem.toString());
            source.setCanvasItem(canvasItem);
        }

        source.setKind(Integer.valueOf(buttonGroup.getSelection().getActionCommand()));

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new ConfectionMapCanvas();

            canvasItem = new CanvasItem();

        } else {
            this.source = (ConfectionMapCanvas) object;

            canvasItem = this.source.getCanvasItem();

        }
    }

    @Override
    public void defaultFillingData() {
        tfNumCanvas.setText(String.valueOf(this.source.getNumber()));

        cbpCanvas.preset(canvasItem);

        switch (this.source.getKind()) {
            case 0:
                rbKindCanvas0.setSelected(true);
                break;
            case 1:
                rbKindCanvas1.setSelected(true);
                break;
            default:
                rbKindCanvas0.setSelected(true);
                break;
        }
    }

    @Override
    public boolean verificationData() {
        try {
            Integer.valueOf(tfNumCanvas.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Некорректно задано значение \"№ полотна\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfNumCanvas.requestFocusInWindow();
            return false;
        }

        if (cbpCanvas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать полотно", "Ошибка!",
                    JOptionPane.ERROR_MESSAGE);
            cbpCanvas.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(buttonGroup.getSelection().getActionCommand());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Некорректно задано значение \"Вид полотна\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            rbKindCanvas0.requestFocusInWindow();
            return false;
        }
        return true;
    }
}
