package by.march8.ecs.application.modules.warehouse.external.shipping.editor;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.entities.readonly.ContractEntity;
import by.march8.entities.warehouse.SaleDocumentContractInformation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Andy 07.08.2018 - 9:07.
 */
public class SaleDocumentContractInformationEditor extends EditingPane {

    private JLabel lblDocNumber = new JLabel("№ документа:");
    private JTextField tfDocNumber = new JTextField();
    private JLabel lblContract = new JLabel("Договор:");
    private JComboBox<ContractEntity> cbContract = new JComboBox<>();

    private SaleDocumentContractInformation source = null;

    public SaleDocumentContractInformationEditor(MainController mainController) {
        controller = mainController;
        setPreferredSize(new Dimension(500, 150));
        this.setLayout(new MigLayout());
        init();

        this.setFocusTraversalKeysEnabled(false);
        Set<AWTKeyStroke> forwardKeySet = new HashSet<>();
        forwardKeySet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeySet);
    }

    private void init() {
        add(lblDocNumber, "");
        add(tfDocNumber, "width 100:20:100, wrap");

        add(lblContract, "");
        add(cbContract, "width 350:20:350, height 20:20, wrap");
    }

    private void getRecipientContracts(int contractorId) {
        SaleDocumentManager documentManager = new SaleDocumentManager();
        cbContract.setModel(new DefaultComboBoxModel(documentManager.getContractsByContractorId(contractorId).toArray()));
        cbContract.setSelectedIndex(-1);
    }

    private void presetContractId(int contractId) {

        for (int i = 0; i < cbContract.getItemCount(); i++) {
            final ContractEntity itemAt = cbContract.getItemAt(i);

            if (itemAt.getId() == contractId) {
                cbContract.setSelectedIndex(i);
                return;
            }
        }
        cbContract.setSelectedIndex(-1);
    }

    @Override
    public Object getSourceEntity() {
        source.setContractId(cbContract.getItemAt(cbContract.getSelectedIndex()).getId());
        source.setDocumentNumber(tfDocNumber.getText().trim());
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object != null) {
            source = (SaleDocumentContractInformation) object;

            getRecipientContracts(source.getContractorId());
            defaultFillingData();
        }
    }

    @Override
    public void defaultFillingData() {
        tfDocNumber.setText(source.getDocumentNumber().trim());
        presetContractId(source.getContractId());
    }

    @Override
    public boolean verificationData() {
        if (tfDocNumber.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null,
                    "Поле \"Номер документа\" не должно быть пустым", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            tfDocNumber.requestFocusInWindow();
            return false;
        }

        if (cbContract.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null,
                    "Укажите номер договора по документу", "Ошибка!!!",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
