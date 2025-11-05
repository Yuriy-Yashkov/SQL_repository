package by.march8.ecs.application.modules.sales.mode;

import by.gomel.freedev.ucframework.uccore.enums.MarchReferencesType;
import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePeriodPicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextFieldPanel;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.references.general.ContractorDAO;
import by.march8.ecs.application.modules.sales.dao.SalesMonitorJDBC;
import by.march8.ecs.application.modules.sales.manager.ContractorSelectorDialog;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.entities.readonly.ContractorEntityView;
import by.march8.entities.sales.SalesMonitorItemEntity;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class SalesMonitorMode extends AbstractFunctionalMode {

    private UCDatePeriodPicker datePeriodPicker;
    private UCTextField tfModelNumber;
    private GridViewPort<SalesMonitorItemEntity> gvItems;
    private List<SalesMonitorItemEntity> dataList;

    private JButton btnUpdate;
    private SalesMonitorJDBC db;

    private JPanel pHeader;
    private UCTextField tfContractorCode;
    private JLabel footerLabel;

    private UCTextFieldPanel<String> tfpContractor;
    private ContractorEntityView contractor;

    public SalesMonitorMode(MainController controller) {
        this.controller = controller;
        modeName = "Монитор отгрузки";
        init();
        initEvents();
        gvItems.primaryInitialization();
        frameViewPort.getFrameControl().showFrame();
    }

    private void init() {
        frameViewPort = new FrameViewPort(controller, MarchWindowType.INTERNALFRAME);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        frameViewPort.getFrameRegion().getToolBar().setVisible(false);

        btnUpdate = new JButton("Поиск");

        tfModelNumber = new UCTextField();
        tfContractorCode = new UCTextField();


        tfContractorCode.setComponentParams(null, Integer.class, 10);
        tfModelNumber.setComponentParams(null, Integer.class, 10);

        datePeriodPicker = new UCDatePeriodPicker();
        datePeriodPicker.setName("datePeriodPicker");
        datePeriodPicker.setDatePickerEnd(new Date());
        datePeriodPicker.setDatePickerBegin(DateUtils.getFirstDay(datePeriodPicker.getDatePickerEnd()));
        datePeriodPicker.setEditable(true);


        gvItems = new GridViewPort<>(SalesMonitorItemEntity.class, false);
        dataList = gvItems.getDataModel();
        frameViewPort.getFrameRegion().getCenterContentPanel().add(gvItems);

        gvItems.initialFooter();

        Container footer = gvItems.getFooterPanel();
        footer.setLayout(new FlowLayout(FlowLayout.LEFT));
        footerLabel = gvItems.getFooterTextComponent();
        footerLabel.setFont(new Font(footerLabel.getFont().getName(), Font.BOLD, 12));


        pHeader = new JPanel(new MigLayout());
        pHeader.add(new JLabel("Период отбора : "));
        pHeader.add(datePeriodPicker, "width 300:20:300, height 50:50, wrap");

        pHeader.add(new JLabel("Контрагент : "));
        tfpContractor = new UCTextFieldPanel<>(controller, MarchReferencesType.CONTRACTOR_OLD);
        pHeader.add(tfpContractor, "width 300:20:300, height 20:20, wrap");

        pHeader.add(new JLabel("Код контрагента : "));
        pHeader.add(tfContractorCode, "width 100:20:100, height 20:20, wrap");

        pHeader.add(new JLabel("Модель : "));
        pHeader.add(tfModelNumber, "width 100:20:100, height 20:20, wrap");

        pHeader.add(new JLabel(""));
        pHeader.add(btnUpdate, "height 28:28, width 150:20:150");

        frameViewPort.getFrameRegion().getTopContentPanel().add(pHeader);

        db = new SalesMonitorJDBC();
    }

    private void initEvents() {
        btnUpdate.addActionListener(a -> {
            if (tfContractorCode.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Необходимо указать [Код контрагента] для поиска изделий", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                tfContractorCode.requestFocusInWindow();
                return;
            }
            if (tfModelNumber.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Необходимо указать [Номер модели] для поиска изделий", "Ошибка!!!",
                        JOptionPane.ERROR_MESSAGE);
                tfModelNumber.requestFocusInWindow();
                return;
            }

            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }

                @Override
                protected Boolean doInBackground() throws Exception {
                    updateContent();
                    return true;
                }
            }
            Task task = new Task("Поиск данных...");
            task.executeTask();
        });

        tfpContractor.addButtonSelectActionListener(e -> {
            ContractorSelectorDialog dialog = new ContractorSelectorDialog(controller);
            ContractorEntityView select_ = dialog.selectContractor();
            if (select_ != null) {
                contractor = ContractorDAO.getContractorViewById(select_.getId());
                if (contractor != null) {
                    tfpContractor.setText(contractor.getName());
                    tfContractorCode.setText(String.valueOf(contractor.getCode()));
                }
            }
        });
    }

    @Override
    public void updateContent() {
        dataList.clear();
        footerLabel.setText("Укажите критерий поиска");
        try {
            List<SalesMonitorItemEntity> salesByPeriodAndContractorAndModel = db.getSalesByPeriodAndContractorAndModel(
                    datePeriodPicker.getTimeLimitPeriod(),
                    Integer.valueOf(tfContractorCode.getText()),
                    Integer.valueOf(tfModelNumber.getText()));
            if (salesByPeriodAndContractorAndModel != null) {
                dataList.addAll(salesByPeriodAndContractorAndModel);
                int amount = 0;
                for (SalesMonitorItemEntity item : dataList) {
                    amount += item.getAmount();
                }
                footerLabel.setText("Найдено в отгрузке " + amount + " шт. ");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        gvItems.updateViewPort();
    }

    @Override
    public void addRecord() {

    }

    @Override
    public void editRecord() {

    }

    @Override
    public void deleteRecord() {

    }
}
