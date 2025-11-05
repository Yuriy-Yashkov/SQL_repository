package by.march8.ecs.application.modules.wes;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.sales.mode.PreOrderSaleDocumentMode;
import by.march8.ecs.application.modules.sales.model.PreOrderControlType;
import by.march8.ecs.application.shell.model.SectionMenu;
import by.march8.ecs.framework.common.BackgroundTask;
import dept.sbit.SendMailForm;

import javax.swing.*;

public class WesModule implements Module {
    
    private final JMenuItem miSendInvoices = new JMenuItem("Отправка накладных");
    private final JMenuItem miRefunds = new JMenuItem("Накладные на отгрузку/Возвраты");
    private final JMenuItem miPreOrderExport = new JMenuItem("Предварительный заказ");

    private MainController controller;
    private JFrame ownerFrame;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = controller.getMainForm();
        registerMenu();
    }

    @Override
    public void registerMenu() {
        
        controller.addModuleMenu(new SectionMenu(MarchSection.WES_SENDINVOICES, miSendInvoices));
        controller.addModuleMenu(new SectionMenu(MarchSection.WES_INVOICES, miRefunds));
        controller.addModuleMenu(new SectionMenu(MarchSection.WES_PREORDEREXPORT, miPreOrderExport));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miSendInvoices.addActionListener(a -> new SendMailForm(controller, ownerFrame, true, false));
        miRefunds.addActionListener(a -> new dept.ves.Nakladnie(controller, true));
        miPreOrderExport.addActionListener(a -> {
            BackgroundTask<String> task = new BackgroundTask("Запуск журнала заказов...") {
                @Override
                protected Boolean doInBackground() {
                    new PreOrderSaleDocumentMode(controller, PreOrderControlType.EXPORT);
                    return true;
                }
            };
            task.executeTask();
        });
    }
}
