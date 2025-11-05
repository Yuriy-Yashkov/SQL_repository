package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.warehouse.ResponsiblePersons;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andy 13.12.2016.
 */
public class ResponsiblePersonsEditor extends EditingPane {

    private JLabel lblPersonType = new JLabel("Тип");
    private JComboBox cbPersonType = new JComboBox();

    private JLabel lblExportJob = new JLabel("");
    private JCheckBox chbExportJob = new JCheckBox("Специалист ВЭС");

    private JLabel lblPost = new JLabel("Должность");
    private JTextField tfOfficialPosition = new JTextField();

    private JLabel lblName = new JLabel("Наименование");
    private JTextField tfName = new JTextField();

    private JLabel lblTabelNumber = new JLabel("Табельный номер");
    private JTextField tfTabelNumber = new JTextField();

    private ResponsiblePersons source;
    private int personType;
    private boolean exportJob;

    public ResponsiblePersonsEditor(MainController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(430, 200));
        this.setLayout(new MigLayout());
        init();

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }

    public static void comboBoxFiller(final JComboBox comboBox) {
        ArrayList<String> array = new ArrayList<String>();
        array.add("Отпуск разрешил");
        array.add("Сдал грузоотправитель");
        array.add("К перевозке принял");
        array.add("Водитель");
        array.add("Транспортное средство");
        array.add("Доверенность выдана");
        array.add("Примечание");
        comboBox.setModel(new DefaultComboBoxModel(array.toArray()));
        comboBox.setSelectedIndex(0);
    }

    private void init() {
        add(lblPersonType, "width 150:20:150");
        add(cbPersonType, "width 250:20:250, height 20:20,  wrap");

        add(lblExportJob, "width 150:20:150");
        add(chbExportJob, "width 250:20:250, wrap");

        add(lblPost, "width 150:20:150");
        add(tfOfficialPosition, "width 250:20:250, wrap");

        add(lblName, "width 150:20:150");
        add(tfName, "width 250:20:250, wrap");

        add(lblTabelNumber, "width 150:20:150");
        add(tfTabelNumber, "width 250:20:250, wrap");

        comboBoxFiller(cbPersonType);

        initEvents();
    }

    private void initEvents() {
        cbPersonType.addActionListener(a -> {
            int index = cbPersonType.getSelectedIndex();
            if (index > 3) {
                lblName.setText("Наименование");
                chbExportJob.setVisible(false);
                lblPost.setVisible(false);
                tfOfficialPosition.setVisible(false);
            } else {
                lblName.setText("ФИО сотрудника");
                chbExportJob.setVisible(true);
                lblPost.setVisible(true);
                tfOfficialPosition.setVisible(true);
            }
        });
    }

    @Override
    public Object getSourceEntity() {
        source.setName(tfName.getText().trim());
        source.setOfficialPosition(tfOfficialPosition.getText().trim());
        source.setTabelNumber(Integer.valueOf(tfTabelNumber.getText().trim()));
        source.setIsExport(chbExportJob.isSelected());
        source.setType(cbPersonType.getSelectedIndex());
        return source;
    }

    @Override
    public void setSourceEntity(final Object object) {
        if (object == null) {
            source = new ResponsiblePersons();
            cbPersonType.setSelectedIndex(personType);
            chbExportJob.setSelected(exportJob);
            tfName.setText("");
            tfOfficialPosition.setText("");
            tfTabelNumber.setText("");
        } else {
            source = (ResponsiblePersons) object;
            cbPersonType.setSelectedIndex(source.getType());
            chbExportJob.setSelected(source.isExport());
            tfName.setText(source.getName());
            tfOfficialPosition.setText(source.getOfficialPosition());
            tfTabelNumber.setText(Integer.toString(source.getTabelNumber()));
        }
    }

    @Override
    public boolean verificationData() {
        return true;
    }

    public void setPersonType(final int personType) {
        this.personType = personType;
    }

    public void setExportJob(final boolean exportJob) {
        this.exportJob = exportJob;
    }
}
