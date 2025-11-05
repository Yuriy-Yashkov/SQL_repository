/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sklad;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.march8.api.utils.DateUtils;
import by.march8.ecs.MainController;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.DAOSaleDocumentFactory;
import by.march8.ecs.application.modules.warehouse.external.shipping.dao.interfaces.ISaleDocumentDao;
import by.march8.entities.warehouse.SaleDocumentEntity;
import by.march8.tasks.accounting.OldLineCreator;
import common.ProgressBar;
import dept.component.MyButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author user
 */
public class EksportOtgruzDBF extends JDialog {

    //private JFormattedTextField tfDateS = new JFormattedTextField();
    //private JFormattedTextField tfDateE = new JFormattedTextField();
    private JLabel jlStartDate;
    //private JLabel jlEndDate;
    private JFrame parent;
    private String dfstr;
    private MaskFormatter formatter;
    private MyButton btnOk;
    private MyButton btnCancel;
    private SwingWorker sw;
    private ProgressBar pb;

    private UCDatePicker dpDocumentDate = new UCDatePicker(new Date());

    public EksportOtgruzDBF(JFrame paren) {
        super(paren);
        parent = paren;
        try {
            formatter = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');
        initcomp();
    }


    public void initcomp() {

        this.setLayout(new MigLayout());
        jlStartDate = new JLabel("Дата отчетного месяца: ");
        dpDocumentDate.setDate(new Date());
        //jlEndDate = new JLabel("Дата до: ");


        Calendar cc = Calendar.getInstance();
        dfstr = "";
        int i = cc.get(Calendar.DAY_OF_MONTH);
        if (i < 10)
            dfstr += "0" + Integer.toString(i) + ".";
        else
            dfstr += "" + Integer.toString(i) + ".";
        i = cc.get(Calendar.MONTH) + 1;
        if (i < 10)
            dfstr += "0" + Integer.toString(i) + ".";
        else
            dfstr += "" + Integer.toString(i) + ".";
        dfstr += "" + cc.get(Calendar.YEAR);

        Object strqw = null;
        try {
            strqw = formatter.stringToValue(dfstr);
        } catch (Exception e) {
        }
/*        tfDateE = new JFormattedTextField(formatter);
        tfDateE.setValue(strqw);
        tfDateE.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    btnOk.doClick();
                }
            }
        });*/

        dfstr = "01" + dfstr.substring(2, 10);

        strqw = null;
        try {
            strqw = formatter.stringToValue(dfstr);
        } catch (Exception e) {
            e.printStackTrace();
        }
/*        tfDateS = new JFormattedTextField(formatter);
        tfDateS.setValue(strqw);*/

        btnOk = new MyButton("Сформировать");
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                pb = new ProgressBar(parent, false, "Формироdание DBF...");
                class SWorker extends SwingWorker<HashMap, Object> {
                    @Override
                    protected HashMap doInBackground() throws Exception {
                        String KEY_OLDLINE_EXPORT = "SAVEPATH_OLDLINE_EXPORT";
                        //SkladDB sdb = new SkladDB();
                        //sdb.createDbfFromShipping(tfDateS.getText().trim(),tfDateE.getText().trim());
                        //sdb.disConn();
                        //return new HashMap();
                        //pb.updateValue();

                        FileNameExtensionFilter filter = new FileNameExtensionFilter("DBF файлы", "dbf");
                        JFileChooser dialog = new JFileChooser();
                        String presetDir = MainController.getConfiguration().getProperty(KEY_OLDLINE_EXPORT);
                        if (presetDir != null) {
                            if (!presetDir.trim().equals("")) {
                                dialog.setCurrentDirectory(new File(presetDir));
                            }
                        }

                        dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        dialog.setFileFilter(filter);
                        dialog.setDialogTitle("Сохранить документ как " + "DBF" + " файл");

                        int ret = dialog.showSaveDialog(null);
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            File saveFile = dialog.getSelectedFile();
                            try {
                                createOldLineDBF(saveFile.getAbsolutePath(), pb);
                                MainController.getConfiguration().setProperty(KEY_OLDLINE_EXPORT, saveFile.getAbsolutePath());
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            pb.setVisible(false);
                            pb.dispose();
                        } catch (Exception ex) {
                            System.err.println("Ошибка при создании файла отгрузки. " + ex);
                        }
                    }
                }
                sw = new SWorker();
                sw.execute();
                pb.setVisible(true);
            }
        });

        btnCancel = new MyButton("Закрыть");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.add(jlStartDate, "width 140:20:140");
        add(dpDocumentDate, " width 140:20:140, height 20:20, wrap");

        dpDocumentDate.getEditor().setEditable(false);

        this.add(btnOk);
        this.add(btnCancel);

        this.pack();
        setSize(350, 100);
        setLocationRelativeTo(parent);
        setResizable(false);
        setTitle("Экспорт отгрузки в dbf файл");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

    }

    private void createOldLineDBF(String savePath, ProgressBar pb) {
        Calendar c = Calendar.getInstance();

        // Устанавливаем начальную дату периода отбора
        int daysToDecrement = -31;
        c.add(Calendar.DATE, daysToDecrement);
        Date beginDate = DateUtils.getFirstDay(dpDocumentDate.getDate());
        Date endDate = DateUtils.getLastDay(dpDocumentDate.getDate());

        // Получаем список актуальных документов за прериод
        System.out.println("Processing dates : " + DateUtils.getNormalDateFormat(beginDate) + " - " + DateUtils.getNormalDateFormat(endDate));
        List<SaleDocumentEntity> list = getSaleDocumentListByPeriod(0, beginDate, endDate);
        System.out.println("For a period of " + list.size() + " documents found");

        pb.setMessageValue("Обработано %d из " + list.size() + " документов ...");

        // Создаем конструктор для формирования экспортных DBF файлов
        //new AccountingCreator(false, true, list, endDate, pb);

        new OldLineCreator(savePath, list, endDate, pb);

        System.out.println("Processing completed.....");
    }

    /**
     * Получает список документов для периода
     *
     * @param documentType 0 - все закрытые документы 1 - только закрытые кроме розницы
     * @param dateBegin начало периода отбора
     * @param dateEnd   конец периода отбора
     * @return список актуальных документов за период
     */
    private List<SaleDocumentEntity> getSaleDocumentListByPeriod(int documentType, Date dateBegin, Date dateEnd) {
        List<SaleDocumentEntity> result = null;
        DAOSaleDocumentFactory factory = DAOSaleDocumentFactory.getInstance();
        ISaleDocumentDao dao = factory.getSaleDocumentDao();
        try {
            result = dao.getSaleDocumentByPeriod(documentType, dateBegin, dateEnd);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
