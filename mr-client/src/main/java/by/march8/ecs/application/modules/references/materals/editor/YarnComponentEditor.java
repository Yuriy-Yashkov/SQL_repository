package by.march8.ecs.application.modules.references.materals.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.entities.materials.YarnComponent;
import by.march8.entities.materials.YarnComponentType;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Панель редактирования Состава пряжи
 *
 *
 * @author andy-linux
 * @see by.march8.entities.materials.YarnComponent
 * @see by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane
 */
public class YarnComponentEditor extends EditingPane {

    /**
     *
     */
    private static final long serialVersionUID = 7523404557867362575L;
    private final JTextField tfPercent = new JTextField();
    private ComboBoxPanel<YarnComponentType> cbpYarnType;
    private YarnComponent source = null;
    private YarnComponentType componentType;

    public YarnComponentEditor(final IReference reference) {
        super(reference);
        controller = reference.getMainController();
        initPanel();
        initEvents();
    }

    public YarnComponentEditor(FrameViewPort viewPort) {
        super();
        controller = viewPort.getController();
        initPanel();
        initEvents();
    }

    private void initPanel() {
        setPreferredSize(new Dimension(320, 150));
        this.setLayout(new MigLayout());
        JLabel lName = new JLabel("Наименование компонента пряжи *");
        add(lName, "wrap");
        cbpYarnType = new ComboBoxPanel<YarnComponentType>(controller, MarchReferencesType.MATERIAL_YARN_TYPE);
        add(cbpYarnType, "width 300:20:300, height 20:20, wrap");
        JLabel lPercent = new JLabel("Содержание компонента, % *");
        add(lPercent, "wrap");
        add(tfPercent, "width 100:24:100, wrap");
    }

    private void initEvents() {
        cbpYarnType.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                componentType = cbpYarnType.getSelectedItem();
            }
        });

        cbpYarnType.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.err.println("Try do preset");
                componentType = cbpYarnType.selectFromReference(componentType);
            }
        });
    }

    @Override
    public Object getSourceEntity() {
        source.setYarnType(componentType);
        source.setComponentPercent(Float.valueOf(tfPercent.getText()));
        source.setNote("");
        source.setName("");
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new YarnComponent();
            // cbpYarnType.setSelectedIndex(0);
            tfPercent.setText("0");
        } else {
            source = (YarnComponent) object;
            tfPercent.setText(String.valueOf(source.getComponentPercent()));
            componentType = source.getYarnType();
            cbpYarnType.preset(componentType);
        }
    }

    @Override
    public boolean verificationData() {
        try {
            float result = Float.valueOf(tfPercent.getText().trim());
            if ((result <= 0) || (result > 100)) {
                JOptionPane.showMessageDialog(null,
                        "Процент содержания компонента в пряже должен быть в пределах 100%",
                        "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                tfPercent.requestFocusInWindow();
                return false;
            }
        } catch (final Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"% содержания\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfPercent.requestFocusInWindow();
            return false;
        }
        return true;
    }

}
