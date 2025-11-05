package by.march8.ecs.application.modules.sales;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.sales.mode.PreOrderSaleDocumentMode;
import by.march8.ecs.application.modules.sales.model.PreOrderControlType;
import by.march8.ecs.application.modules.warehouse.external.shipping.mode.EMailHistory;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.common.BackgroundTask;
import dept.sbit.EditTradeAllowance;
import dept.sbit.SendMailForm;
import dept.sbit.client.form.ClientForm;
import dept.sbit.protocol.ProtocolForm;
import dept.sbit.unloading.EurotorgUnloading;

import javax.swing.*;

public class SalesModule implements Module {

    private final JMenu miUnloadingMenu = new JMenu("Разгрузка");
    private final JMenuItem miSendingInvoices = new JMenuItem("Отправка накладных");
    private final JMenuItem miSetClient = new JMenuItem("Управление клиентами");
    private final JMenuItem miPreOrderInternal = new JMenuItem("Предварительный заказ");
    private final JMenuItem miEurotorg = new JMenuItem("Евроторг");
    private final JMenuItem miEmailHistory = new JMenuItem("История отправки документов");
    private final JMenuItem miChangeInvoices = new JMenuItem("Изменить ТН");
    private final JMenuItem miProtocolOnTheApplication = new JMenuItem("Протокол по заявке");

    private MainController controller;
    private JFrame ownerFrame;
    private String progPath;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = controller.getMainForm();
        progPath = controller.getBootstrap().getRunPath();
        registerMenu();
    }

    @Override
    public void registerMenu() {
        miUnloadingMenu.add(miEurotorg);
        miSendingInvoices.setIcon(new ImageIcon(progPath + "/Img/email.png"));

        controller.addModuleMenu(new SectionMenu(MarchSection.SALES_SENDINGINVOICES, miSendingInvoices));
        controller.addModuleMenu(new SectionMenu(MarchSection.SALES_SETCLIENT, miSetClient));
        controller.addModuleMenu(new SectionMenu(MarchSection.SALES_PREORDERINTERNAL, miPreOrderInternal));
        controller.addModuleMenu(new SectionMenu(MarchSection.SALES_UNLOADING, miUnloadingMenu));
        controller.addModuleMenu(new SectionMenu(MarchSection.SALES_UNLOADING_EUROTORG, miEurotorg));
        controller.addModuleMenu(new SectionMenu(MarchSection.SALES_EMAILHISTORY, miEmailHistory));
        controller.addModuleMenu(new SectionMenu(MarchSection.SALES_CHANGEINVOICES, miChangeInvoices));
        controller.addModuleMenu(new SectionMenu(MarchSection.SALES_PROTOCOL, miProtocolOnTheApplication));
        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miSendingInvoices.addActionListener(a -> new SendMailForm(controller, ownerFrame, true));
        miChangeInvoices.addActionListener(a-> new EditTradeAllowance(ownerFrame));
        miProtocolOnTheApplication.addActionListener( a -> new ProtocolForm(controller, true));
        miEmailHistory.addActionListener(a -> new EMailHistory(controller));
        miEurotorg.addActionListener(a -> new EurotorgUnloading(controller));
        miSetClient.addActionListener(a -> new ClientForm(ownerFrame, true));
        miPreOrderInternal.addActionListener(a -> {
            class Task extends BackgroundTask {
                public Task(final String messageText) {
                    super(messageText);
                }
                @Override
                protected Boolean doInBackground() throws Exception {
                    new PreOrderSaleDocumentMode(controller, PreOrderControlType.INTERNAL );
                    return true;
                }
            }
            Task task = new Task("Запуск журнала заказов...");
            task.executeTask();
        });
    }
}
