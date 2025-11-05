package by.march8.ecs.application.modules.tech;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import dept.MyReportsModule;
import dept.tech.innovation.Production;
import workOO.OpenOffice;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import java.awt.Component;
import java.io.File;

public class TechModule extends Component implements Module {

    private final JMenu menu = new JMenu("Тех отдел");
    private final JMenuItem newProductInPlan = new JMenuItem("Новые изделия в плане");
    private final JMenuItem newProduct = new JMenuItem("Продукция");

    private MainController controller;
    private JFrame ownerFrame;

    @Override
    public void registerModule(MainController mainController) {
        controller = mainController;
        ownerFrame = mainController.getMainForm();
        registerMenu();
    }

    @Override
    public void registerMenu() {
        menu.add(newProductInPlan);
        menu.add(newProduct);
        controller.addModuleMenu(new SectionMenu(MarchSection.TECH_MENU, menu));
        controller.addModuleMenu(new SectionMenu(MarchSection.TECH_NEWPRODUCTINPLAN, newProductInPlan));
        controller.addModuleMenu(new SectionMenu(MarchSection.TECH_NEWPRODUCT, newProduct));
    }

    @Override
    public void registerMenuEvents() {
        newProductInPlan.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(MyReportsModule.dbfPlanPath);
            fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
            fc.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f != null) {
                        return f.isDirectory()
                                || f.getName().endsWith(".DBF") && f.getName().startsWith("PLAN")
                                || f.getName().endsWith(".dbf") && f.getName().startsWith("plan");
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return "PLAN";
                }
            });
            fc.setAcceptAllFileFilterUsed(false);

            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                OpenOffice oo = new OpenOffice();
                oo.createReport("EanPlan.ots");
            }
        });

        newProduct.addActionListener(e -> new Production(ownerFrame, true));
    }
}
