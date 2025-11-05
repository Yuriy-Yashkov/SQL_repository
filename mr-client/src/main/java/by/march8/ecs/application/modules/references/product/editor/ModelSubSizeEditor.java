package by.march8.ecs.application.modules.references.product.editor;

import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.product.utils.UtilProduct;
import by.march8.entities.product.ModelSizeChart;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Панель редактирования размера модели изделия в подтаблице
 * Created by lidashka.
 */
public class ModelSubSizeEditor extends EditingPane {

    private static final long serialVersionUID = -3954031689774875650L;
    private final MainController controller;
    private JTextField tfHeight;
    private JTextField tfSize;
    private JTextField tfPrintsize;
    private JTextField tfNote;
    private JButton jbCreatePrintsize;
    private ModelSizeChart source = null;

    public ModelSubSizeEditor(final IReference reference) {
        super(reference);
        this.controller = reference.getMainController();

        setPreferredSize(new Dimension(340, 280));

        init();
        initEvents();
    }

    /**
     * Инициализация компонентов
     */
    private void init() {
        setLayout(new MigLayout());

        tfHeight = new JTextField();
        tfSize = new JTextField();
        tfPrintsize = new JTextField();
        tfNote = new JTextField();

        jbCreatePrintsize = new JButton();
        jbCreatePrintsize.setText("...");

        add(new JLabel("Рост *: "), " wrap");
        add(tfHeight, "width 300:24:300, wrap");

        add(new JLabel("Размер *: "), " wrap");
        add(tfSize, "width 300:24:300, wrap");

        JPanel printSizePanel = new JPanel(new BorderLayout());
        printSizePanel.add(tfPrintsize, BorderLayout.CENTER);
        printSizePanel.add(jbCreatePrintsize, BorderLayout.EAST);

        add(new JLabel("Размер для печати *: "), " wrap");
        add(printSizePanel, "width 300:20:300, wrap");

        add(new JLabel("Примечание: "), " wrap");
        add(tfNote, "width 300:24:300, wrap");
    }

    /**
     * Инициализация событий
     */
    private void initEvents() {

        jbCreatePrintsize.addActionListener(evt -> {
            new PrintSizeCreatePane(
                    controller,
                    tfHeight.getText().trim(),
                    tfSize.getText().trim());

            if (UtilProduct.ACTION_BUTT_CREATE_PRINTSIZE) {
                tfPrintsize.setText(UtilProduct.ITEM_PRINTSIZE);
            }
        });

    }

    @Override
    public Object getSourceEntity() {

        try {
            float value = Float.valueOf(tfHeight.getText().trim());

            source.setHeight(value);
        } catch (Exception e) {

        }

        try {
            float value = Float.valueOf(tfSize.getText().trim());

            source.setSize(value);
        } catch (Exception e) {

        }

        source.setNote(tfNote.getText().trim());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new ModelSizeChart();

        } else {
            this.source = (ModelSizeChart) object;

        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        tfHeight.setText(this.source.getHeight() != null ? String.valueOf(this.source.getHeight()) : "");
        tfSize.setText(this.source.getSize() != null ? String.valueOf(this.source.getSize().toString()) : "");
        tfNote.setText(this.source.getNote());
    }

    @Override
    public boolean verificationData() {
        try {
            float value = Float.valueOf(tfHeight.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Некорректно задано значение поля \"Рост\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfHeight.requestFocusInWindow();
            return false;
        }

        try {
            float value = Float.valueOf(tfSize.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Некорректно задано значение поля \"Размер\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfSize.requestFocusInWindow();
            return false;
        }

        if (!tfPrintsize.getText().trim().equals("")) {
            if (tfPrintsize.getText().trim().length() > 25) {
                JOptionPane.showMessageDialog(null,
                        "Некорректно задано значение поля \"Размер для печати\".",
                        "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                tfPrintsize.requestFocusInWindow();
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать значение поля \"Размер для печати\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfPrintsize.requestFocusInWindow();
            return false;

        }

        return true;
    }
}
