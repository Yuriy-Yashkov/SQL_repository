package by.march8.ecs.application.modules.marketing.editor;

import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.ecs.MainController;
import by.march8.entities.product.ProductionCatalog;
import dept.sbit.protocol.forms.ArticlePicker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Andy 21.01.2019 - 8:28.
 */
public class ProductionCatalogEditor extends EditingPane {

    private JLabel lblDocumentDate = new JLabel("Дата документа");
    private JLabel lblDocumentNumber = new JLabel("Номер документа");
    private JLabel lblNote = new JLabel("Примечание");
    private UCDatePeriodPicker periodPicker;

    private JCheckBox cbValidity;

    private UCDatePicker dpDocumentDate = new UCDatePicker(new Date());
    private JTextField tfDocumentNumber = new JTextField();
    private JTextField tfNote = new JTextField();

    private ProductionCatalog source = null;

    private UCTextFieldPanel<String> tfArticles;

    private MainController controller;


    public ProductionCatalogEditor(final FrameViewPort frameViewPort) {
        setPreferredSize(new Dimension(450, 250));
        controller = frameViewPort.getController();
        setLayout(new MigLayout());

        init();
        initEvents();
    }

    private void init() {
        periodPicker = new UCDatePeriodPicker();
        periodPicker.setDatePickerBegin(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, +1);
        periodPicker.setDatePickerEnd(calendar.getTime());

        cbValidity = new JCheckBox("Документ имеет срок действия");

        add(lblDocumentDate);
        add(dpDocumentDate, "width 200:20:200, height 20:20, wrap");
        add(lblDocumentNumber);
        add(tfDocumentNumber, "width 200:20:200, height 20:20, wrap");
        add(new JPanel(), "height 10:10,  wrap");

        add(new JLabel("Допустимые артикула"), "span 2, wrap");
        tfArticles = new UCTextFieldPanel<>();
        tfArticles.getEditor().setEditable(false);
        add(tfArticles, "width 370:20:370, height 20:20, span 2, wrap");

        add(new JPanel(), "height 10:10,  wrap");

        add(lblNote);
        add(tfNote, "width 200:20:200, height 20:20, wrap");

        add(new JPanel(), "height 10:10,  wrap");
        add(cbValidity, "span 2, wrap");
        add(periodPicker, "width 350:20:350, height 40:40,span 2, wrap");
    }

    private void initEvents() {
        cbValidity.addActionListener(a -> {
            if (cbValidity.isSelected()) {
                periodPicker.setVisible(true);
            } else {
                periodPicker.setVisible(false);
            }
        });

        tfArticles.addButtonSelectActionListener(a -> {
            ArticlePicker picker = new ArticlePicker(getFrameViewPort(), tfArticles.getText());
            String result = picker.selectArticles();
            if (result != null) {
                tfArticles.setText(result);
                tfArticles.getEditor().setEditable(false);
            }
        });
    }


    @Override
    public Object getSourceEntity() {
        source.setDocumentDate(dpDocumentDate.getDate());
        source.setDocumentNumber(tfDocumentNumber.getText().trim());
        source.setNote(tfNote.getText().trim());
        source.setVisible(true);
        source.setStatus(3);
        source.setArticles(tfArticles.getText());

        if (cbValidity.isSelected()) {
            source.setValidityBegin(periodPicker.getDatePickerBegin());
            source.setValidityEnd(periodPicker.getDatePickerEnd());
        } else {
            source.setValidityBegin(null);
            source.setValidityEnd(null);
        }
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {

        if (object != null) {
            source = (ProductionCatalog) object;
        } else {
            source = new ProductionCatalog();
            source.setDocumentDate(new Date());
            source.setType(0);
            source.setStatus(3);
            cbValidity.setSelected(false);
            periodPicker.setVisible(false);

        }

        cbValidity.setSelected(false);
        periodPicker.setVisible(false);
        tfArticles.setText(source.getArticles());

        defaultFillingData();

    }

    @Override
    public void defaultFillingData() {
        tfArticles.getEditor().setEditable(false);
        tfDocumentNumber.setText(source.getDocumentNumber());
        dpDocumentDate.setDate(source.getDocumentDate());
        tfNote.setText(source.getNote());

        if (source.getValidityBegin() != null || source.getValidityEnd() != null) {
            periodPicker.setVisible(true);
            cbValidity.setSelected(true);

            periodPicker.setDatePickerBegin(source.getValidityBegin());
            periodPicker.setDatePickerEnd(source.getValidityEnd());
        }
    }

    @Override
    public boolean verificationData() {
        if (tfDocumentNumber.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Укажите номер документа", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfDocumentNumber.requestFocusInWindow();
            return false;
        }
        return true;
    }

}
