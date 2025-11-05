package by.march8.ecs.application.modules.references.product.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.ISubReferences;
import by.gomel.freedev.ucframework.ucswing.uicontrols.ComboBoxPanel;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.product.subeditor.ModelSubSizeValue;
import by.march8.ecs.application.modules.references.product.utils.UtilProduct;
import by.march8.entities.product.ModelProduct;
import by.march8.entities.product.ModelSizeChart;
import by.march8.entities.product.ModelSizeValue;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Панель редактирования размера модели изделия
 * Created by lidashka.
 */
public class ModelSizeEditor extends EditingPane {

    private static final long serialVersionUID = -3954031689774875650L;
    private final MainController controller;
    private JTextField tfHeight;
    private JTextField tfSize;

    //private JButton jbCreatePrintsize;
    //private JTextField tfPrintsize;
    private JTextField tfNote;
    private ComboBoxPanel<ModelProduct> cbpModelProduct;
    private ModelProduct modelProduct = new ModelProduct();
    private ISubReferences srModelSizeValue;
    private ModelSizeChart source = null;
    private boolean FLAG_SUB_EDITOR;


    public ModelSizeEditor(final IReference reference, boolean flag) {
        super(reference);
        this.controller = reference.getMainController();
        this.FLAG_SUB_EDITOR = flag;

        if (FLAG_SUB_EDITOR)
            setPreferredSize(new Dimension(450, 400));
        else
            setPreferredSize(new Dimension(450, 450));

        init();
        initEvents();
    }

    /**
     * Инициализация компонентов
     */
    private void init() {
        setLayout(new MigLayout());

        cbpModelProduct = new ComboBoxPanel<>(true, controller, MarchReferencesType.MODEL_PRODUCT);

        tfHeight = new JTextField();
        tfSize = new JTextField();
        tfNote = new JTextField();

        //tfPrintsize = new JTextField();
        //jbCreatePrintsize = new JButton();
        //jbCreatePrintsize.setText("...");
        //jbCreatePrintsize.setPreferredSize(new Dimension(30,20));

        if (!FLAG_SUB_EDITOR) {
            add(new JLabel("Модель изделия *:"), " wrap");
            add(cbpModelProduct, "width 420:20:420,wrap");
        }

        add(new JLabel("Рост *: "), " wrap");
        add(tfHeight, "width 200:24:200, wrap");

        add(new JLabel("Размер *: "), " wrap");
        add(tfSize, "width 200:24:200, wrap");

        //JPanel printSizePanel = new JPanel(new BorderLayout());
        //printSizePanel.add(tfPrintsize, BorderLayout.CENTER);
        //tfPrintsize.setEnabled(false);
        //printSizePanel.add(jbCreatePrintsize, BorderLayout.EAST);

        //add(new JLabel("Размер для печати *: "), " wrap");
        //add(tfPrintsize,"width 230:20:230, wrap");

        JPanel measurementPanel = new JPanel();
        measurementPanel.setLayout(new BorderLayout());

        srModelSizeValue = new ModelSubSizeValue(reference, measurementPanel);

        add(new JLabel("Обмеры: "), " wrap");
        add(measurementPanel, "width 420:70:420, height 200, wrap");

        add(new JLabel("Примечание: "), " wrap");
        add(tfNote, "width 420:24:420, wrap");
    }

    /**
     * Инициализация событий
     */
    private void initEvents() {
        cbpModelProduct.addButtonActionListener(e -> modelProduct = cbpModelProduct.selectFromReference(false));

        cbpModelProduct.addComboBoxActionListener(e -> modelProduct = cbpModelProduct.getSelectedItem());

        /*
        jbCreatePrintsize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new PrintSizeCreatePane(
                        controller,
                        tfHeight.getText().trim(),
                        tfSize.getText().trim());

                if(UtilProduct.ACTION_BUTT_CREATE_PRINTSIZE){
                    tfPrintsize.setText(UtilProduct.ITEM_PRINTSIZE);
                }
            }
        });
        */
    }

    @Override
    public Object getSourceEntity() {
        if (cbpModelProduct.getSelectedItem() != null) {
            source.setModel(modelProduct);
        }

        try {
            float value = Float.valueOf(tfHeight.getText().trim().replace(",", "."));

            source.setHeight(value);
        } catch (Exception e) {

        }

        try {
            float value = Float.valueOf(tfSize.getText().trim().replace(",", "."));

            source.setSize(value);
        } catch (Exception e) {

        }

        //source.setPrintsize(tfPrintsize.getText().trim());
        source.setNote(tfNote.getText().trim());

        source.setModelSizeValues((Set<ModelSizeValue>) srModelSizeValue.getData());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            source = new ModelSizeChart();

            modelProduct = new ModelProduct();

        } else {
            this.source = (ModelSizeChart) object;

            modelProduct = this.source.getModel();

        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        cbpModelProduct.preset(modelProduct);

        tfHeight.setText(this.source.getHeight() != null ? UtilProduct.getValue(this.source.getHeight()) : "");
        tfSize.setText(this.source.getSize() != null ? UtilProduct.getValue(this.source.getSize()) : "");

        //tfPrintsize.setText(this.source.getPrintsize());
        tfNote.setText(this.source.getNote());

        srModelSizeValue.setData(source.getModelSizeValues());
        srModelSizeValue.setSourceEntity(source);
    }

    @Override
    public boolean verificationData() {
        if (!FLAG_SUB_EDITOR) {
            if (cbpModelProduct.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null,
                        "Необходимо указать модель изделия", "Ошибка!",
                        JOptionPane.ERROR_MESSAGE);
                cbpModelProduct.requestFocusInWindow();
                return false;
            }
        }

        try {
            float value = Float.valueOf(tfHeight.getText().trim().replace(",", "."));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Некорректно задано значение поля \"Рост\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfHeight.requestFocusInWindow();
            return false;
        }

        try {
            float value = Float.valueOf(tfSize.getText().trim().replace(",", "."));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Некорректно задано значение поля \"Размер\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfSize.requestFocusInWindow();
            return false;
        }

        if (srModelSizeValue.getData().size() != 0) {
            if (srModelSizeValue.getData().size() + 2 > 25) {
                JOptionPane.showMessageDialog(null,
                        "Размер для печати большое значение.",
                        "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать обмеры \"Размер для печати\".",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /*
        if(!tfPrintsize.getText().trim().equals("")) {
            if (tfPrintsize.getText().trim().length()>25) {
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
        */

        return true;
    }
}
