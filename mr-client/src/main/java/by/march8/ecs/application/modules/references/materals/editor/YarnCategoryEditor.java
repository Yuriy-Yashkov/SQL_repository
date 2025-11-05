package by.march8.ecs.application.modules.references.materals.editor;


import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCController;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.entities.materials.YarnCategory;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Панель редактирования категории пряжи
 *
 */
public class YarnCategoryEditor extends EditingPane {
    private final UCTextField tfName = new UCTextField();
    private final UCTextField tfStandardDTex = new UCTextField();
    private final UCTextField tfStandardTex = new UCTextField();
    private final UCTextField tfStandardNm = new UCTextField();
    private final UCTextField tfStandardNeb = new UCTextField();
    private final UCTextField tfStandardNew = new UCTextField();
    private final UCTextField tfStandardDen = new UCTextField();
    private YarnCategory source;
    private UCController ucController;

    public YarnCategoryEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(430, 230));
        setLayout(new MigLayout());
        final JLabel lName = new JLabel("Наименование *");
        final JLabel lStandardDTex = new JLabel("DTex");
        final JLabel lStandardTex = new JLabel("Tex");
        final JLabel lStandardNm = new JLabel("Nm");
        final JLabel lStandardNeb = new JLabel("Neb");
        final JLabel lStandardNew = new JLabel("New");
        final JLabel lStandardDen = new JLabel("Den");

        // Установка параметров компонентов
        // ссылка на текстовую метку, можно null
        // тип данных (String/Integer/Float/Double)
        // -X/X минимальное/максимальное количество знаков в строке

        tfName.setComponentParams(lName, String.class, 0);
        tfStandardDTex.setComponentParams(lStandardDTex, Float.class, -1);
        tfStandardTex.setComponentParams(lStandardTex, Float.class, -1);
        tfStandardNm.setComponentParams(lStandardNm, Float.class, -1);
        tfStandardNeb.setComponentParams(lStandardNeb, Float.class, -1);
        //tfStandardNew.setComponentParams(lStandardNew, Float.class,-1);// Это поле типа не обязательное. Параметры для его не указал
        tfStandardDen.setComponentParams(lStandardDen, Float.class, -1);

        add(lName, "width 120:20:280");
        add(tfName, "width 280:20:280, wrap");
        add(lStandardDTex);
        add(tfStandardDTex, "width 100:20:100,wrap");
        add(lStandardTex);
        add(tfStandardTex, "width 100:20:100,wrap");
        add(lStandardNm);
        add(tfStandardNm, "width 100:20:100,wrap");
        add(lStandardNeb);
        add(tfStandardNeb, "width 100:20:100,wrap");
        add(lStandardNew);
        add(tfStandardNew, "width 100:20:100,wrap");
        add(lStandardDen);
        add(tfStandardDen, "width 100:20:100,wrap");

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
        tfStandardDTex.addFocusListener(focus);
        tfStandardTex.addFocusListener(focus);
        tfStandardNm.addFocusListener(focus);
        tfStandardNeb.addFocusListener(focus);
        tfStandardNew.addFocusListener(focus);
        tfStandardDen.addFocusListener(focus);

        ucController = new UCController(this);
        ucController.updateComponentList(this);
    }

    @Override
    public Object getSourceEntity() {
        source.setName(tfName.getText());
        source.setStandardDTex(Float.valueOf(tfStandardDTex.getText()));
        source.setStandardTex(Float.valueOf(tfStandardTex.getText()));
        source.setStandardNm(Float.valueOf(tfStandardNm.getText()));
        source.setStandardNeb(Float.valueOf(tfStandardNeb.getText()));
        source.setStandardNew(Float.valueOf(tfStandardNew.getText()));
        source.setStandardDen(Float.valueOf(tfStandardDen.getText()));
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            tfName.setText("");
            source = new YarnCategory();
            tfStandardDTex.setText("0");
            tfStandardTex.setText("0");
            tfStandardNm.setText("0");
            tfStandardNeb.setText("0");
            tfStandardNew.setText("0");
            tfStandardDen.setText("0");
        } else {
            source = (YarnCategory) object;
            tfName.setText(source.getName());
            tfStandardDTex.setText(String.valueOf(source.getStandardDTex()));
            tfStandardTex.setText(String.valueOf(source.getStandardTex()));
            tfStandardNm.setText(String.valueOf(source.getStandardNm()));
            tfStandardNeb.setText(String.valueOf(source.getStandardNeb()));
            tfStandardNew.setText(String.valueOf(source.getStandardNew()));
            tfStandardDen.setText(String.valueOf(source.getStandardDen()));
        }
    }

    @Override
    public UCController getUCController() {
        return ucController;
    }

    @Override
    @SuppressWarnings("all")
    public boolean verificationData() {
        return ucController.verificationDataIsCorrect();
        /*
        if (tfName.getText().equals("")){
            JOptionPane.showMessageDialog(null,
                    "Поле \" Наименование\" не должно быть пустым",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfName.requestFocusInWindow();
            return false ;
        }
        if (!verificationValue(tfStandardDTex, Float.class)) {
            return false;
        }
        if (!verificationValue(tfStandardTex, Float.class)) {
            return false;
        }
        if (!verificationValue(tfStandardNm, Float.class)) {
            return false;
        }
        if (!verificationValue(tfStandardNeb, Float.class)) {
            return false;
        }
        if (!verificationValue(tfStandardNew, Float.class)) {
            return false;
        }

        if (!verificationValue(tfStandardDen, Float.class)) {
            return false;
        }
        */
        //return true;
    }
/*
    @SuppressWarnings("all")
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
    */
}

