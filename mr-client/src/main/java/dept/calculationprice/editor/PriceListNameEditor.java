package dept.calculationprice.editor;

import by.gomel.freedev.ucframework.ucswing.dialog.BaseEditorDialog;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import dept.calculationprice.PrintForm;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.logging.Level;

/**
 * Created by Andy on 19.10.2015.
 */
public class PriceListNameEditor extends EditingPane {
    private String source;
    private JLabel lblName = new JLabel("Дата");
    private JLabel lblNote = new JLabel("Примечание");
    private JFormattedTextField tfName = new JFormattedTextField();
    private JTextField tfNote = new JTextField();

    public PriceListNameEditor() {
        setLayout(null);
        setPreferredSize(new Dimension(300, 110));
        initPane();
    }

    private void initPane() {
        lblName.setBounds(10, 8, 50, 25);
        lblNote.setBounds(80, 8, 200, 25);
        tfName.setBounds(10, 30, 60, 20);
        tfNote.setBounds(80, 30, 200, 20);
        add(lblName);
        add(lblNote);
        add(tfName);
        add(tfNote);

        MaskFormatter formatter;
        try {
            formatter = new MaskFormatter("20##-##");
            formatter.setPlaceholderCharacter('0');
            formatter.setValidCharacters("0123456789");
            tfName.setFormatterFactory(new DefaultFormatterFactory(formatter));
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(PrintForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        initEvents();
    }

    private void initEvents() {

        tfName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    tfNote.requestFocusInWindow();
                }
            }
        });
    }

    @Override
    public Object getSourceEntity() {
        return String.valueOf(tfName.getValue()) + tfNote.getText();
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object != null) {
            source = (String) object;

        } else {
            source = "";
        }
        defaultFillingData();
    }

    @Override
    public void defaultFillingData() {
        tfName.setText(source.substring(0, 7));
        tfNote.setText(source.substring(7, source.length()));
    }

    @Override
    public boolean verificationData() {
        return true;
    }

    @Override
    public void phaseBeforeShowing() {

        if (getFrameViewPort() != null) {
            BaseEditorDialog frame = getFrameViewPort();
            final JButton btnSave = frame.getBtnSave();
            tfNote.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(final KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        btnSave.doClick();
                    }
                }
            });
        }
    }
}
