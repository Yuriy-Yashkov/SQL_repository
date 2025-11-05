package by.march8.ecs.application.modules.references.company.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCController;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.entities.classifier.EquipmentItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Форма редактирования справочника оборудования
 * Created by Andy on 15.04.2015.
 */
public class EquipmentEditor extends EditingPane {
    private EquipmentItem source;

    private UCTextField tfName = new UCTextField();
    private UCTextField tfDiameter = new UCTextField();
    private UCTextField tfNeedleCount = new UCTextField();
    private UCTextField tfEquipmentClass = new UCTextField();
    private UCTextField tfSystemCount = new UCTextField();

    private UCController ucController;

    public EquipmentEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(430, 160));
        setLayout(new MigLayout());
        final JLabel lName = new JLabel("Наименование *");
        final JLabel lDiameter = new JLabel("Диаметр");
        final JLabel lNeedleCount = new JLabel("Количество игл");
        final JLabel lEquipmentClass = new JLabel("Класс машины");
        final JLabel lSystemCount = new JLabel("Количество систем");


        tfName.setComponentParams(lName, String.class, 0);

        tfDiameter.setComponentParams(lDiameter, String.class, 0);
        tfNeedleCount.setComponentParams(lNeedleCount, String.class, 0);
        tfEquipmentClass.setComponentParams(lEquipmentClass, Integer.class, -1);
        tfSystemCount.setComponentParams(lSystemCount, String.class, 0);


        add(lName, "width 120:20:280");
        add(tfName, "width 280:20:280, wrap");
        add(lDiameter);
        add(tfDiameter, "width 100:20:100,wrap");
        add(lNeedleCount);
        add(tfNeedleCount, "width 100:20:100,wrap");
        add(lEquipmentClass);
        add(tfEquipmentClass, "width 100:20:100,wrap");
        add(lSystemCount);
        add(tfSystemCount, "width 100:20:100,wrap");


        FocusListener focus = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField component = (JTextField) e.getSource();
                component.select(0, component.getText().length());
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField component = (JTextField) e.getSource();
                component.select(0, 0);
            }
        };

        tfName.addFocusListener(focus);
        tfDiameter.addFocusListener(focus);
        tfNeedleCount.addFocusListener(focus);
        tfEquipmentClass.addFocusListener(focus);
        tfSystemCount.addFocusListener(focus);

        ucController = new UCController(this);
        ucController.updateComponentList(this);
    }

    @Override
    public UCController getUCController() {
        return ucController;
    }


    @Override
    public Object getSourceEntity() {
        source.setName(tfName.getText());
        source.setDiametr(tfDiameter.getText());
        source.setNeedleCount(tfNeedleCount.getText());
        source.setEquipeClass(Integer.valueOf(tfEquipmentClass.getText()));
        source.setSystemCount(tfSystemCount.getText());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new EquipmentItem();
            tfName.setText("");
            tfDiameter.setText("0");
            tfNeedleCount.setText("0");
            tfEquipmentClass.setText("0");
            tfSystemCount.setText("0");
        } else {
            source = (EquipmentItem) object;
            tfName.setText(source.getName());
            tfDiameter.setText(source.getDiametr());
            tfNeedleCount.setText(source.getNeedleCount());
            tfEquipmentClass.setText(String.valueOf(source.getEquipeClass()));
            tfSystemCount.setText(source.getSystemCount());
        }
    }

    @Override
    public boolean verificationData() {
        return ucController.verificationDataIsCorrect();
    }
}
