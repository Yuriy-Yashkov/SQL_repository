package by.march8.ecs.application.modules.warehouse.external.shipping.forms;

import by.gomel.freedev.ucframework.ucswing.dialog.BasePickDialog;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.SaleDocumentJDBC;
import by.march8.ecs.application.modules.warehouse.external.shipping.enums.SaleDocumentStatus;
import by.march8.ecs.application.modules.warehouse.external.shipping.manager.SaleDocumentManager;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.FinishedGoodsHelper;
import by.march8.ecs.application.modules.warehouse.external.shipping.model.SaleDocumentView;
import by.march8.entities.warehouse.SaleDocument;
import by.march8.entities.warehouse.SaleDocumentDriving;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * @author Andy 16.01.2019 - 8:21.
 */
public class CreateAdjustmentDocumentDialog extends BasePickDialog {

    private JComboBox<String> cbAdjustmentCause;
    private SaleDocumentView selectedItem;

    public CreateAdjustmentDocumentDialog(MainController controller, SaleDocumentView selectedItem) {
        super(controller);
        this.selectedItem = selectedItem;

        initComponents();
        initEvents();
    }

    private void initComponents() {
        setFrameSize(new Dimension(350, 150));
        getBtnSave().setText("Преобразовать");
        deleteListener(getBtnSave());
        getToolBar().setVisible(false);
        setTitle("Подготовка документа к корректировке данных");
        Container panel = getCenterContentPanel();
        panel.setLayout(new MigLayout());
        cbAdjustmentCause = new JComboBox<>();
        String documentNumber = "";
        if (selectedItem != null) {
            documentNumber = selectedItem.getDocumentNumber();
        }
        panel.add(new JLabel("Причина корректировки документа [" + documentNumber + "]"), "wrap");
        panel.add(cbAdjustmentCause, "height 20:20, width 300:20:300, wrap");


        FinishedGoodsHelper.getAdjustmentCauses(cbAdjustmentCause);
        cbAdjustmentCause.setSelectedIndex(-1);

    }

    private void initEvents() {
        btnSave.addActionListener(e -> {
            final int answer = Dialogs.showQuestionDialog("<html>Вы хотите начать процесс корректировки для документа [<font color=\"red\">"
                    + selectedItem.getDocumentNumber() + "</font>] ?\n" +
                    "Отменить данное действие будет невозможно.", "Процедура корректировки документа");
            if (answer == 0) {
                prepareAdjustmentDocument(selectedItem);
            }
        });
    }

    public boolean showDialog() {
        return showModalFrame();
    }

    private void prepareAdjustmentDocument(SaleDocumentView selectedItem) {
        SaleDocumentManager manager = new SaleDocumentManager();
        // Подготовка номера документа
        SaleDocumentJDBC db = new SaleDocumentJDBC();

        // Ищем документы с таким именем в БД с учетом постфикса "_XX"
        List<String> nameList = db.getSameDocumentNumbers(selectedItem);
        // ФОрмируем новый номер для оригинального документа
        if (nameList != null) {
            String newDocumentNumber = getDocumentNumber(selectedItem.getDocumentNumber(), nameList);
            // Получаем оригинальный документ

            SaleDocument originalDocument = manager.getSaleDocumentById(selectedItem.getId());
            if (originalDocument != null) {
                int originalId = originalDocument.getId();
                // Пересохраняем оригинальный документ с соответствующей маркировкой и постфиксом в номере
                SaleDocumentManager.changeDocumentStatus(selectedItem.getId(), SaleDocumentStatus.PRE_FORMED);
                originalDocument.setAdjustmentType(2); // Как документ, утративший силу
                originalDocument.setDocumentNumber(newDocumentNumber);
                manager.saveSaleDocument(originalDocument);
                SaleDocumentManager.changeDocumentStatus(selectedItem.getId(), SaleDocumentStatus.CLOSED);

                // Сохраняем копию оригинального документа под старым номером
                SaleDocument newDocument = manager.createSaleDocumentCopy(originalDocument);
                newDocument.setDocumentNumber(selectedItem.getDocumentNumber());
                newDocument.setAdjustmentType(1); // Как основной документ
                newDocument.setAdjustmentDocumentId(originalId);
                newDocument.setDocumentStatus(3);
                newDocument = manager.saveSaleDocument(newDocument);

                // ФОрмирование путевого листа на основании оригинала
                SaleDocumentDriving originalDriving = manager.getDrivingDocumentationByDocumentId(originalDocument.getId());
                SaleDocumentDriving newDriving = new SaleDocumentDriving(originalDriving);
                newDriving.setSaleDocumentId(newDocument.getId());
                manager.saveSaleDocumentDriving(newDriving);

                // делаем пометку в таблицу регистрации корректирующих документов
                manager.adjustmentRegistration(originalDocument, newDocument, (String) cbAdjustmentCause.getSelectedItem());

                modalResult = true;
                closeFrame();
            }
        }
    }

    public String getDocumentNumber(String documentNumber, List<String> list) {
        int prefixIndex = 0;
        for (String s : list) {
            if (s != null) {
                String[] seg = s.split("-");
                if (seg.length > 1) {
                    int curr = 0;
                    try {
                        curr = Integer.valueOf(seg[1]);
                        if (curr > prefixIndex) {
                            prefixIndex = curr;
                        }
                    } catch (Exception e) {
                        // Постфикс задан не по формату
                        e.printStackTrace();
                    }
                }
            }
        }
        //Инкремент постфикс-индекса
        prefixIndex++;
        return documentNumber + "-" + prefixIndex;
    }
}
