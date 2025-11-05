package dept.upack;

import common.PanelWihtFone;
import common.ProgressBar;
import workDB.DB;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 *
 * @author vova
 */
@SuppressWarnings("all")
public class DataForm2 extends JDialog {

    PanelWihtFone mainForm;
    JLabel lDate;
    JLabel lHead;
    JLabel lModel;
    JLabel lSar;
    JLabel lNar;
    JLabel lSize;
    JLabel lRost;
    JLabel lColor;
    JLabel lSort;
    JLabel lCount;
    JLabel lPrice;
    JLabel lChul;
    JTextField tfModel;
    JTextField tfSar;
    JTextField tfNar;
    JTextField tfSize;
    JTextField tfRost;
    JTextField tfCount;
    JDialog thisForm;

    JCheckBox cbModel;
    JCheckBox cbSar;
    JCheckBox cbSize;
    JCheckBox cbColor;
    JCheckBox cbSort;
    JCheckBox cbPrice;
    JCheckBox cbChul;
    JComboBox listSort;
    JComboBox listColor;
    JComboBox listSize;
    JDialog thisWin;
    String[] sortList = new String[]{"Все", "1,2", "1", "2", "3", "4"};
    String srt = null;
    int nar = -2;
    int rzm = -2;
    int rst = -2;
    int model = -2;
    int count = 0;
    String sar = null;
    String colr = null;
    ProgressBar pb;
    private String text;
    private int x = 10;
    private int y = 10;
    private MaskFormatter formatter;
    private MaskFormatter formatter2;
    private JFormattedTextField ftsDate = new JFormattedTextField();
    private JFormattedTextField fteDate = new JFormattedTextField();
    private JButton bShow;
    private int type;

    public DataForm2(JFrame parent, boolean f, int q) {
        super(parent, f);
        thisForm = this;
        type = q;
        try {
            formatter = new MaskFormatter("##.##.####");
            formatter2 = new MaskFormatter("##.##.####");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e);
        }
        formatter.setPlaceholderCharacter('0');
        formatter2.setPlaceholderCharacter('0');
        formatter.install(ftsDate);
        formatter2.install(fteDate);

        initComponets();

        text = new String("Введите период:");
        setTitle("Сдача на склад");
        lDate.setText(text);
        add(mainForm);
        setSize(400, 300);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        thisWin = this;
        setVisible(true);
    }

    private void initComponets() {

        DB db = new DB();
        listColor = new JComboBox(db.getAllColor().toArray());

        mainForm = new PanelWihtFone();

        lHead = new JLabel("Показать сдачу на склад");
        Font font = new Font(null, Font.BOLD, 14);
        lHead.setFont(font);
        lHead.setHorizontalAlignment(SwingConstants.CENTER);
        lHead.setBounds(x + 75, y, 240, 20);
        mainForm.add(lHead);

        cbModel = new JCheckBox();
        cbModel.setSelected(true);
        cbModel.setMargin(new Insets(-1, -2, 0, 0));
        cbModel.setBounds(x - 5, y + 55, 13, 13);
        mainForm.add(cbModel);
        lModel = new JLabel("По модели");
        lModel.setBounds(x + 15, y + 55, 240, 20);
        mainForm.add(lModel);
        tfModel = new JTextField("Все", 5);
        tfModel.setBounds(x + 105, y + 55, 50, 20);
        mainForm.add(tfModel);

        lCount = new JLabel("не менее шт");
        lCount.setBounds(x + 195, y + 55, 100, 20);
        mainForm.add(lCount);
        tfCount = new JTextField("0", 5);
        tfCount.setBounds(x + 295, y + 55, 50, 20);
        mainForm.add(tfCount);

        cbSar = new JCheckBox();
        cbSar.setMargin(new Insets(-1, -2, 0, 0));
        cbSar.setBounds(x - 5, y + 80, 13, 13);
        mainForm.add(cbSar);
        lSar = new JLabel("По артикулу");
        lSar.setBounds(x + 15, y + 80, 240, 20);
        mainForm.add(lSar);
        tfSar = new JTextField("Все", 9);
        tfSar.setBounds(x + 115, y + 80, 80, 20);
        mainForm.add(tfSar);

        lNar = new JLabel("номеру");
        lNar.setBounds(x + 205, y + 80, 240, 20);
        //mainForm.add(lNar);
        tfNar = new JTextField("Все", 9);
        tfNar.setBounds(x + 260, y + 80, 80, 20);
        //mainForm.add(tfNar);
        cbChul = new JCheckBox();
        cbChul.setMargin(new Insets(-1, -2, 0, 0));
        cbChul.setBounds(x + 205, y + 80, 13, 13);
        mainForm.add(cbChul);
        lChul = new JLabel("+ чулки");
        lChul.setBounds(x + 230, y + 75, 240, 20);
        mainForm.add(lChul);

        cbSize = new JCheckBox();
        cbSize.setMargin(new Insets(-1, -2, 0, 0));
        cbSize.setBounds(x - 5, y + 105, 13, 13);
        mainForm.add(cbSize);
        lSize = new JLabel("По размеру");
        lSize.setBounds(x + 15, y + 105, 240, 20);
        mainForm.add(lSize);
        tfSize = new JTextField("Все", 9);
        tfSize.setBounds(x + 115, y + 105, 80, 20);
        mainForm.add(tfSize);
        lRost = new JLabel("росту");
        lRost.setBounds(x + 205, y + 105, 240, 20);
        mainForm.add(lRost);
        tfRost = new JTextField("Все", 9);
        tfRost.setBounds(x + 260, y + 105, 85, 20);
        mainForm.add(tfRost);

        cbColor = new JCheckBox();
        cbColor.setMargin(new Insets(-1, -2, 0, 0));
        cbColor.setBounds(x - 5, y + 155, 13, 13);
        mainForm.add(cbColor);
        lColor = new JLabel("По цвету");
        lColor.setBounds(x + 15, y + 155, 240, 20);
        mainForm.add(lColor);
        listColor.setBounds(x + 95, y + 155, 180, 20);
        mainForm.add(listColor);

        cbSort = new JCheckBox();
        cbSort.setMargin(new Insets(-1, -2, 0, 0));
        cbSort.setBounds(x - 5, y + 180, 13, 13);
        mainForm.add(cbSort);
        lSort = new JLabel("По сорту");
        lSort.setBounds(x + 15, y + 180, 240, 20);
        mainForm.add(lSort);
        listSort = new JComboBox(sortList);
        listSort.setBounds(x + 95, y + 180, 100, 20);
        mainForm.add(listSort);

        cbPrice = new JCheckBox();
        cbPrice.setMargin(new Insets(-1, -2, 0, 0));
        cbPrice.setBounds(x + 260, y + 190, 13, 13);
        mainForm.add(cbPrice);
        lPrice = new JLabel("цена");
        lPrice.setBounds(x + 280, y + 185, 140, 20);
        mainForm.add(lPrice);

        lDate = new JLabel();
        lDate.setBounds(x - 5, y + 30, 140, 20);
        mainForm.add(lDate);

        Calendar c = Calendar.getInstance();
        DateFormat df = new java.text.SimpleDateFormat("dd.MM.yyyy");
        String date = new String(df.format(c.getTime()));
        ftsDate.setText(date);
        ftsDate.setBounds(x + 130, y + 30, 80, 20);
        mainForm.add(ftsDate);

        fteDate.setText(date);
        fteDate.setBounds(x + 220, y + 30, 80, 20);
        mainForm.add(fteDate);

        bShow = new JButton("Показать");
        bShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nar = -2;
                rzm = -2;
                rst = -2;
                model = -2;
                sar = null;
                colr = null;
                srt = null;
                count = 0;

                if (!checkData()) return;

                pb = new ProgressBar(thisForm, false, "Получение остатков...");
                SwingWorker sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        HashMap<String, Boolean> flagMap = new HashMap();
                        flagMap.put("Модель", false);
                        flagMap.put("Артикул", false);
                        flagMap.put("Размер", false);
                        flagMap.put("Цвет", false);
                        flagMap.put("Сорт", false);
                        flagMap.put("Цена", false);

                        ArrayList<String> colums = new ArrayList();
                        ArrayList row = new ArrayList();
                        colums.add("Название");

                        if (cbModel.isSelected()) {
                            colums.add("Модель");
                            flagMap.put("Модель", true);
                        }

                        if (cbSar.isSelected()) {
                            flagMap.put("Артикул", true);
                            colums.add("Артикул");
                            sar = new String(tfSar.getText().trim());
                            if (sar.equals("") || sar == null) sar = new String("Все");
                        } else sar = new String("--none");

                        if (cbSar.isSelected()) {
                            // colums.add("Номер арт");
                        }

                        if (cbSize.isSelected()) {
                            flagMap.put("Размер", true);
                            colums.add("Рост");
                        }


                        if (cbSize.isSelected()) {
                            colums.add("Размер");
                        }

                        if (cbColor.isSelected()) {
                            flagMap.put("Цвет", true);
                            colums.add("Цвет");
                            colr = new String(listColor.getSelectedItem().toString());
                            if (colr.equals("") || colr == null) colr = new String("Все");
                        } else colr = new String("--none");

                        if (cbSort.isSelected()) {
                            flagMap.put("Сорт", true);
                            colums.add("Сорт");
                            srt = new String(listSort.getSelectedItem().toString());
                            if (srt.equals("") || srt == null) srt = new String("Все");
                        } else srt = new String("--none");

                        colums.add("Кол-во");
                        colums.add("Кол-во с комплектностью");

                        if (cbPrice.isSelected()) {
                            colums.add("Цена");
                            flagMap.put("Цена", true);
                            colums.add("Сумма");
                            flagMap.put("Сумма", true);
                        }

                        DB db = new DB();

                        row = db.sdachaNaSklad(ftsDate.getText(), fteDate.getText(), 0, model, sar, nar, colr, rzm, rst, srt, flagMap, cbChul.isSelected());
                        //new TableForm2(thisWin, true, type, ftsDate.getText().trim(), colums, row, db.kol, db.sum);
                        // добавить new TableForm2
                        new TableForm2New(thisWin, true, type, ftsDate.getText().trim(), colums, row, db.kol, db.kolForSdachNaSklad, db.sum);
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
        bShow.setBounds(x + 135, y + 220, 110, 20);
        mainForm.add(bShow);

    }

    private boolean checkDate(String chDate) {
        int d = 0, m = 0, y = 0;
        try {
            d = Integer.parseInt(chDate.substring(0, 2));
            if (d > 31 || d < 1) {
                throw new Exception("недопустимое значение дня");
            }
            m = Integer.parseInt(chDate.substring(3, 5));
            if ((m > 12) || (m < 1)) {
                throw new Exception("недопустимое значение месяца");
            }
            y = Integer.parseInt(chDate.substring(6, 10));
            if (y < 1) {
                throw new Exception("недопустимое значение года");
            }
        } catch (Exception e) {
            System.out.println("Ошибка преобразования даты:\n" + e);
            JOptionPane.showMessageDialog(null, "Ошибка преобразования даты: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean checkData() {

        try {
            try {
                if (cbModel.isSelected() && !tfModel.getText().trim().equals(new String("Все")) && !tfModel.getText().trim().equals("") && tfModel.getText() != null) {
                    model = Integer.parseInt(tfModel.getText().trim());
                } else if (cbModel.isSelected()) model = -1;
            } catch (Exception e) {
                throw new Exception("недопустимое значение модели");
            }

            try {
                if (cbSar.isSelected() && !tfNar.getText().trim().equals("Все") && !tfNar.getText().trim().equals("") && tfNar.getText() != null) {
                    nar = Integer.parseInt(tfNar.getText().trim());
                } else if (cbSar.isSelected()) nar = -1;
            } catch (Exception e) {
                throw new Exception("недопустимое значение номера артикула");
            }

            try {
                if (cbSize.isSelected() && !tfRost.getText().trim().equals("Все") && !tfRost.getText().trim().equals("") && tfRost.getText() != null) {
                    rst = Integer.parseInt(tfRost.getText().trim());
                } else if (cbSize.isSelected()) rst = -1;
            } catch (Exception e) {
                throw new Exception("недопустимое значение роста");
            }

            try {
                if (cbSize.isSelected() && !tfSize.getText().trim().equals("Все") && !tfSize.getText().trim().equals("") && tfSize.getText() != null) {
                    rzm = Integer.parseInt(tfSize.getText());
                } else if (cbSize.isSelected()) rzm = -1;
            } catch (Exception e) {
                throw new Exception("недопустимое значение номера размера");
            }

            try {
                if (!tfCount.getText().trim().equals("") && tfCount.getText() != null) {
                    count = Integer.parseInt(tfCount.getText());
                } else count = 0;
            } catch (Exception e) {
                throw new Exception("недопустимое значение минимального кол-ва");
            }


        } catch (Exception e) {
            System.out.println("Ошибка преобразования данных:\n" + e);
            JOptionPane.showMessageDialog(null, "Ошибка преобразования данных: " + e.getMessage(), "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}