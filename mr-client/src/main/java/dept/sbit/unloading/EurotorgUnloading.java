package dept.sbit.unloading;

import by.gomel.freedev.ucframework.uccore.report.api.openoffice.DocumentType;
import by.gomel.freedev.ucframework.uccore.report.api.openoffice.OpenOfficeConnector;
import by.gomel.freedev.ucframework.uccore.report.model.ReportTemplate;
import by.gomel.freedev.ucframework.ucswing.dialog.Dialogs;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import dept.sbit.protocol.UtilProtocol;

import javax.swing.*;
import java.io.File;

/**
 * @author Andy 05.07.2017.
 */
public class EurotorgUnloading {

    private MainController controller;
    private UploadingJDBC db;

    public EurotorgUnloading(MainController mainController) {
        controller = mainController;

        final int answer = Dialogs.showQuestionDialog("Производится выгрузка данных в файл формата \"Евроторг\"\n" +
                "Нажмите [Продолжить], выберите необходимый файл и нажмите [Выгрузить]", "Выгрузка данных формата \"Евроторг\"");
        if (answer == 0) {
            db = new UploadingJDBC();
            openTemplate();
        }
    }

    private void openTemplate() {

        final JFileChooser fc = new JFileChooser(UtilProtocol.FOLDER_SELECT);
        fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f != null) {
                    if (f.isDirectory()) {
                        return true;
                    }
                }
                if (f.getName().toLowerCase().endsWith(".ods")) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".xlsx")) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".xls")) {
                    return true;
                }
                if (f.getName().toLowerCase().endsWith(".csv")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "";
            }
        });

        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showDialog(controller.getMainForm(), null) == JFileChooser.APPROVE_OPTION) {
            if (fc.getSelectedFile().exists()) {

                class Task extends BackgroundTask {
                    public Task(final String messageText) {
                        super(messageText);
                    }

                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            ReportTemplate template = new ReportTemplate();
                            template.setDirectPath(true);
                            template.setDocumentType(DocumentType.DOCUMENT_ODS);
                            template.setTemplateName(fc.getSelectedFile().getPath());
                            template.setVisible(true);

                            OpenOfficeConnector document = new OpenOfficeConnector(template);
                            populateData(document.getXComponent());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                }

                Task task = new Task("Выгрузка данных");
                task.executeTask();
            }
        }
    }

    private boolean populateData(XComponent component) {
        int startRow = 10;
        int keyColumn = 1;
        try {
            XSpreadsheetDocument xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class, component);
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName("Шаблон");
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            XCell xCell = null;
            xCell = xSpreadsheet.getCellByPosition(keyColumn, startRow);
            String keyValue = xCell.getFormula();
            while (!keyValue.trim().equals("")) {

                NSIItem item = db.getTNVDCode(keyValue.trim());

                if (item != null) {
                    xCell = xSpreadsheet.getCellByPosition(8, startRow);
                    xCell.setFormula(item.getTnvdCode());

                    xCell = xSpreadsheet.getCellByPosition(9, startRow);
                    xCell.setValue(item.getPrice());
                    xCell = xSpreadsheet.getCellByPosition(10, startRow);
                    xCell.setValue(item.getVat());

                    xCell = xSpreadsheet.getCellByPosition(14, startRow);
                    xCell.setValue(item.getWeight());
                }

                startRow++;
                xCell = xSpreadsheet.getCellByPosition(keyColumn, startRow);
                keyValue = xCell.getFormula();
            }

            System.out.println("Всего записей :" + (startRow - 10));

        } catch (Exception e) {
            return false;
        }
        return true;
    }

}

