package by.march8.ecs.application.modules.references.materals.editor;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.modes.interfaces.IReference;
import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.entities.materials.CanvasComponent;
import by.march8.entities.materials.YarnItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andy 09.01.2015.
 */
public class CanvasComponentEditor extends EditingPane {

    //private final ComboBoxPanel<YarnItem> cbpYarn;
    private final UCTextFieldPanel<YarnItem> tfpYarn;

    private final JTextField tfPercent = new JTextField();
    private final JTextField tfNote = new JTextField();
    private YarnItem yarn;
    private CanvasComponent source = null;

    public CanvasComponentEditor(final IReference reference) {
        super(reference);
        setPreferredSize(new Dimension(400, 190));
        this.setLayout(new MigLayout());

        add(new JLabel("Наименование компонента полотна *"), "wrap");
        //cbpYarn = new ComboBoxPanel<>(reference.getMainController(), MarchReferencesType.MATERIAL_YARN) ;
        tfpYarn = new UCTextFieldPanel<>(reference.getMainController(), MarchReferencesType.MATERIAL_YARN);

//        add(cbpYarn, "width 380:20:380, height 20:20, wrap");
        add(tfpYarn, "width 380:20:380, height 20:20, wrap");

        add(new JLabel("Содержание компонента, % *"), "wrap");
        add(tfPercent, "width 100:24:100, wrap");

        add(new JLabel("Примечание"), "wrap");
        add(tfNote, "width 380:20:380, height 20:20, wrap");

        tfpYarn.addButtonSelectActionListener(e -> yarn = tfpYarn.selectFromReference(yarn));

      /*  cbpYarn.addComboBoxActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                yarn = cbpYarn.getSelectedItem();

                if(yarn!=null) {
                    cbpYarn.getComboBox().setToolTipText(yarn.getComponentsAsHTML());
                }
            }
        });

        cbpYarn.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                yarn = cbpYarn.selectFromReference(false);
            }
        });
        */
    }

    @Override
    public Object getSourceEntity() {
        source.setYarn(yarn);
        source.setPercent(Float.valueOf(tfPercent.getText().trim()));
        source.setNote(tfNote.getText().trim());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new CanvasComponent();
            yarn = new YarnItem();
            tfPercent.setText("0");
        } else {
            source = (CanvasComponent) object;
            tfPercent.setText(String.valueOf(source.getPercent()));
            yarn = source.getYarn();
            tfNote.setText(String.valueOf(source.getNote()));
        }
        // cbpYarn.preset(yarn);
        tfpYarn.preset(yarn);
    }

    @Override
    public boolean verificationData() {
        if (yarn == null) {
            JOptionPane.showMessageDialog(null,
                    "Необходимо указать наименование компонента полотна",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            // cbpYarn.setFocus();
            tfpYarn.setFocus();
            return false;
        }

        try {
            float result = Float.valueOf(tfPercent.getText().trim());
            if ((result <= 0) || (result > 100)) {
                JOptionPane.showMessageDialog(null,
                        "Процент содержания компонента в полотне должен быть в пределах 100%",
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
