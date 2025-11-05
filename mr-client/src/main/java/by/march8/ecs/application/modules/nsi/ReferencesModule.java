package by.march8.ecs.application.modules.nsi;

import by.gomel.freedev.ucframework.uccore.enums.MarchSection;
import by.gomel.freedev.ucframework.uccore.interfaces.Module;
import by.march8.ecs.MainController;
import by.march8.ecs.application.shell.model.SectionMenu;
import common.ProgressBar;
import dept.nsi.ClientKT;
import dept.nsi.Otgruz;
import dept.nsi.PlanSstoimost;
import dept.nsi.TrudoZat;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ReferencesModule implements Module {

    private final JMenu miReferenceMenu = new JMenu("Справочники");
    private final JMenuItem miUpdateLaborCost = new JMenuItem("Обновить справочник трудозатрат");
    private final JMenuItem miUpdatePlannedCost = new JMenuItem("Обновить плановую себестоимость");
    private final JMenuItem miUpdateCustomerDirectory = new JMenuItem("Обновить справочник клиентов (КТ)");
    private final JMenuItem miUpdateShippingDirectory = new JMenuItem("Обновить справочник отгрузки");
    private final JMenuItem miClearingNsiKldDirectory = new JMenuItem("Очистка справочника NSI_KLD");
    private final JMenuItem miVEKDirectory = new JMenuItem("Справочник ВЭК");
    private final JMenuItem miMovingWithoutMass = new JMenuItem("Перемещение без массы");

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
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_REFERENCES, miReferenceMenu));
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_REFERENCES_LABORCOST, miUpdateLaborCost));
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_REFERENCES_PLANEDCOST, miUpdatePlannedCost));
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_REFERENCES_CUSTOMERDIRECTORY, miUpdateCustomerDirectory));
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_REFERENCES_SHIPPINGDIRECTORY, miUpdateShippingDirectory));
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_REFERENCES_CLEARNING, miClearingNsiKldDirectory));
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_REFERENCES_VEKDIRECTORY, miVEKDirectory));
        controller.addModuleMenu(new SectionMenu(MarchSection.NSI_REFERENCES_MOVINGWITHOUTMASS, miMovingWithoutMass));

        registerMenuEvents();
    }

    @Override
    public void registerMenuEvents() {
        miUpdateLaborCost.addActionListener(e -> new TrudoZat().update());
        miUpdatePlannedCost.addActionListener(e -> {
            JFileChooser fc = new JFileChooser("/nfs/ksl01_D/VED_S16/_DBF/_KR_PLAN/");
            fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
            fc.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f != null) {
                        if (f.isDirectory()) return true;
                        return f.getName().endsWith(".DBF") && f.getName().startsWith("KR")
                                || f.getName().endsWith(".dbf") && f.getName().startsWith("kr");
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return "PLAN";
                }
            });
            fc.setAcceptAllFileFilterUsed(false);

            if (fc.showOpenDialog(ownerFrame) == JFileChooser.APPROVE_OPTION) {
                final String path = fc.getSelectedFile().getPath();
                final ProgressBar pb = new ProgressBar(ownerFrame, false, "Обновление справочника...");
                SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        PlanSstoimost pss = new PlanSstoimost(path);
                        pss.update();
                        return 0;
                    }

                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };

                sw.execute();
                pb.setVisible(true);
            }
        });
        // было miUpdatePlannedCost
        miUpdateCustomerDirectory.addActionListener(e -> {
            JFileChooser fc = new JFileChooser("/nfs/ser01_D/BUD/SV/nsi_pd.dbf");
            fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
            fc.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f != null) {
                        if (f.isDirectory()) return true;
                        return f.getName().endsWith(".dbf") && f.getName().startsWith("nsi")
                                || f.getName().endsWith(".DBF") && f.getName().startsWith("NSI");
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return "PLAN";
                }
            });
            fc.setAcceptAllFileFilterUsed(false);

            if (fc.showOpenDialog(ownerFrame) == JFileChooser.APPROVE_OPTION) {
                final String path = fc.getSelectedFile().getPath();
                final ProgressBar pb = new ProgressBar(ownerFrame, false, "Обновление справочника...");
                SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        ClientKT c = new ClientKT(path);
                        c.update();
                        return 0;
                    }

                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };

                sw.execute();
                pb.setVisible(true);
            }
        });
        miUpdateShippingDirectory.addActionListener(e -> {
            final JFileChooser fc = new JFileChooser("/nfs/ser01/bud/");
            fc.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f != null) {
                        if (f.isDirectory()) {
                            return true;
                        }
                        return f.getName().toLowerCase().endsWith(".dbf");
                    }
                    return false;
                }
                @Override
                public String getDescription() {
                    return "*.dbf";
                }
            });
            fc.setAcceptAllFileFilterUsed(false);

            if (fc.showDialog(ownerFrame, null) == JFileChooser.APPROVE_OPTION) {
                final ProgressBar pb = new ProgressBar(ownerFrame, false, "Обновление справочника...");
                SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        Otgruz c = new Otgruz(fc.getSelectedFile().getPath());
                        c.update();
                        return null;
                    }
                    @Override
                    protected void done() {
                        pb.dispose();
                    }
                };
                sw.execute();
                pb.setVisible(true);
            }
        });
        miClearingNsiKldDirectory.addActionListener(e ->
                new dept.nsi.SpravIzdelie(ownerFrame, true));
        miVEKDirectory.addActionListener(e ->
                new dept.nsi.DirectoryCodeForeignEconomicRelations(ownerFrame, true));
        miMovingWithoutMass.addActionListener(e ->
                new dept.nsi.DateMoveNoMassa(ownerFrame, true));
    }
}
