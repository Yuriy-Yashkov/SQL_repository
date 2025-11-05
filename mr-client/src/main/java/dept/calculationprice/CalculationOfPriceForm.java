/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.calculationprice;

import by.gomel.freedev.ucframework.ucswing.uicontrols.UCTextField;
import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.LogCrutch;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import dept.calculationprice.forms.ProtocolTypeDialog;
import dept.calculationprice.model.ProtocolPreset;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * Используется для расчета калькуляции
 *
 * @author user
 * @version 1.0
 */
@SuppressWarnings("all")
public final class CalculationOfPriceForm extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    LogCrutch log = new LogCrutch();
    /**
     * Объект подключения к базе данных PostgreSql
     */
    private PricePDB ppdb;
    /**
     * Номер прайса
     */
    private int numberPrice;
    /**
     * Тип калькуляции (цены,услуги)
     */
    private int idTypeCalculation;
    /**
     * Статус коэффициентов ( 0 - Новые , 1 - Старые , 2 - Пользовательские )
     */
    private int status;
    /**
     * Выбор алгоритма расчета ( true - расчет Прибыли от Цены , false - расчет
     * Цены от Прибыли )
     */
    private boolean boolCalc;
    /**
     * id Текущей калькуляции
     */
    private int id;
    /**
     * Объект для хранения данных калькуляции
     */
    private ValueCalculation valueCalc;
    /**
     * Ссылкабъект класса {@link CalculationPriceForm}
     */
    private CalculationPriceForm self;
    /**
     * Позиция картеки в текстовом поле ввода
     */
    private int pos;
    /**
     * Проверка при закрытии формы (true - можно закрыть , false - необходимо
     * Отменить либо Сохранить калькуляцию)
     */
    private boolean close = true;
    /**
     * @see CalculationPriceForm#endTable
     */
    private int endTable;
    /**
     * Проверка на дублирование (true - копия , false - оригинал
     */
    private boolean copy = false;

    private boolean beforeDenominationb;

    /**
     * Флаг расчета до деноминации
     */
    private JCheckBox chBeforeDenomination;

    private MainController mainController;
    private JRadioButton jRadioButtonHalf;

    private JLabel lblCredit;
    private UCTextField etCreditPercent;
    private JLabel lblCreditPercentLabel;
    private UCTextField etCostWithCredit;


    private JLabel lblPrimeCostWithCredit;
    private UCTextField etPrimeCostWithCredit;

    private JLabel lblProfitWithCredit;
    private UCTextField etProfitWithCredit;
    private JPanel pPricePanel;
    private JPanel pButtonPanel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalculationProfitability;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDetReset;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnPrintDet;
    private javax.swing.JButton btnPrintForDet;
    private javax.swing.JButton btnPrintSogl;
    private javax.swing.JButton btnRememberToPrint;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveToSogl;
    private javax.swing.ButtonGroup buttonGroup1;
    private UCTextField etCc;
    private UCTextField etCno;
    private UCTextField etCno2;
    private UCTextField etCnr;
    private UCTextField etCnr2;
    private UCTextField etCnv;
    private UCTextField etCnv2;
    private UCTextField etCnoCredit;
    private UCTextField etCno2Credit;
    private UCTextField etCnrCredit;
    private UCTextField etCnr2Credit;
    private UCTextField etCnvCredit;
    private UCTextField etCnv2Credit;
    private javax.swing.JTextField etCnvp;
    private UCTextField etCsr1;
    private UCTextField etCsr2;
    private UCTextField etCsr3;
    private UCTextField etCsr4;
    private UCTextField etCsr5;
    private UCTextField etCsr6;
    private UCTextField etCsr7;
    private UCTextField etCsr8;
    private UCTextField etCsr9;
    private javax.swing.JFormattedTextField etDat;
    private javax.swing.JTextField etFas;
    private UCTextField etHzr;
    private javax.swing.JFormattedTextField etHzrp;
    private UCTextField etKmr;
    private javax.swing.JFormattedTextField etKmrp;
    private javax.swing.JTextField etNar;
    private javax.swing.JTextField etNiz;
    private javax.swing.JTextField etNiz1;
    private javax.swing.JTextField etNsr1;
    private javax.swing.JTextField etNsr2;
    private javax.swing.JTextField etNsr3;
    private javax.swing.JTextField etNsr4;
    private javax.swing.JTextField etNsr5;
    private javax.swing.JTextField etNsr6;
    private javax.swing.JTextField etNsr7;
    private javax.swing.JTextField etNsr8;
    private javax.swing.JTextField etNsr9;
    private javax.swing.JTextField etObr;
    private javax.swing.JTextField etPol;
    private UCTextField etPrb;
    private javax.swing.JFormattedTextField etPrbp;
    private javax.swing.JTextField etPrim;
    private UCTextField etPrr;
    private javax.swing.JFormattedTextField etPrrp;
    private UCTextField etPss;
    private javax.swing.JFormattedTextField etRzmk;
    private javax.swing.JFormattedTextField etRzmn;
    private UCTextField etSm;
    private javax.swing.JFormattedTextField etSmtp;
    private javax.swing.JTextField etSmts;
    private javax.swing.JFormattedTextField etSmvp;
    private javax.swing.JTextField etSmvs;
    private UCTextField etSn;
    private javax.swing.JTextField etSnp;
    private javax.swing.JTextField etSyr1;
    private javax.swing.JTextField etSyr2;
    private UCTextField etTe;
    private UCTextField etTen;
    private UCTextField etTes;
    private UCTextField etUsto;
    private UCTextField etVm;
    //private javax.swing.JFormattedTextField etVms;
    private UCTextField etVms;
    private javax.swing.JFormattedTextField etVmtp;
    private UCTextField etVmts;
    private javax.swing.JFormattedTextField etWsr1;
    private javax.swing.JFormattedTextField etWsr2;
    private javax.swing.JFormattedTextField etWsr3;
    private javax.swing.JFormattedTextField etWsr4;
    private javax.swing.JFormattedTextField etWsr5;
    private javax.swing.JFormattedTextField etWsr6;
    private javax.swing.JFormattedTextField etWsr7;
    private javax.swing.JFormattedTextField etWsr8;
    private javax.swing.JFormattedTextField etWsr9;
    private UCTextField etZp;
    private UCTextField etZpd;
    private UCTextField etZpo;
    private javax.swing.JComboBox jComboBoxPrice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadioButtonNew;
    private javax.swing.JRadioButton jRadioButtonOld;
    private javax.swing.JRadioButton jRadioButtonUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel tvCno;
    private javax.swing.JLabel tvCno2;
    private javax.swing.JLabel tvCnr;
    private javax.swing.JLabel tvCnr2;
    private javax.swing.JLabel tvSum;
    /**
     * Конструктор , в котором создается объект подключения к БД и
     * инициализируются компоненты
     *
     * @param parent   Ссылкабъект класса {@link CalculationPriceForm}
     * @param endTable Значение типа последней таблицы
     */
    public CalculationOfPriceForm(MainController controller, CalculationPriceForm parent, int endTable) {
        super();
        mainController = controller;
        this.endTable = endTable;
        setLocationRelativeTo(parent);
        self = parent;
        try {
            ppdb = new PricePDB();
            ppdb.conn();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        initComponents();
        JScrollPane scroll = new JScrollPane(jPanel1);
        add(scroll);
        setVisible(true);
        //Инициализация радиокнопок
        initRadioButton();
        //Статус кф  СТАРЫЕ
        setStatus(2);
        //Устанавливаем форматоры
        setFormatter();
        //Выбор алгоритма расчета
        setBool(false);
        this.setPreferredSize(new Dimension(1024, 768));
    }
    /**
     * Конструктор для открытия дублированной калькуляции , в котором создается объект подключения к БД и
     * инициализируются компоненты
     *
     * @param parent   Ссылкабъект класса {@link CalculationPriceForm}
     * @param endTable Значение типа последней таблицы
     */
    public CalculationOfPriceForm(MainController controller, CalculationPriceForm parent, int endTable, boolean bool) {
        super();
        mainController = controller;
        this.endTable = endTable;
        setLocationRelativeTo(parent);
        self = parent;
        try {
            ppdb = new PricePDB();
            ppdb.conn();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        initComponents();

        JScrollPane scroll = new JScrollPane(jPanel1);
        add(scroll);
        setVisible(true);
        initRadioButton();
        //Статус кф  СТАРЫЕ
        setStatus(2);
        //Устанавливаем форматоры
        setFormatter();
        //Выбор алгоритма расчета
        setBool(false);

        //Копия
        copy = true;
    }

    /**
     * Устанавливает значение поля для данных WSR
     *
     * @param jftf  Форматированное текстовое поле в которое устанавливается
     *              значение
     * @param value Устанавливаемое значение
     */
    public void setTextWSR(JFormattedTextField jftf, double value) {

        int length = ((int) value + "").length();

        String space = "";
        for (int i = 0; i < 3 - length; i++) {
            space += " ";
        }
        jftf.setValue(space + String.format("%.5f", value).replace(',', '.'));
    }

    /**
     * Устанавливает значение поля для данных CSR
     *
     * @param jftf  Форматированное текстовое поле в которое устанавливается
     *              значение
     * @param value Устанавливаемое значение
     */
    public void setTextCSR(UCTextField jftf, double value) {
        jftf.setValue(value);
    }

    /**
     * Устанавливает значение поля для данных TEN
     *
     * @param jftf  Форматированное текстовое поле в которое устанавливается
     *              значение
     * @param value Устанавливаемое значение
     */
    public void setTextTEN(UCTextField jftf, double value) {
        jftf.setValue(value);
    }

    /**
     * Устанавливает значение поля для данных VMS
     *
     * @param jftf  Форматированное текстовое поле в которое устанавливается
     *              значение
     * @param value Устанавливаемое значение
     */
    public void setTextVMS(UCTextField jftf, double value) {
        jftf.setValue(value);
    }

    /**
     * Устанавливает значение поля для данных ZP
     *
     * @param jftf  Форматированное текстовое поле в которое устанавливается
     *              значение
     * @param value Устанавливаемое значение
     */
    public void setTextZP_(JFormattedTextField jftf, double value) {

        int length = ((int) value + "").length();
        String space = "";
        for (int i = 0; i < 7 - length; i++) {
            space += " ";
        }

        jftf.setValue(space + String.format("%.1f", value).replace(',', '.'));
    }

    /**
     * @param jftf  Форматированное текстовое поле в которое устанавливается
     *              значение
     * @param value Устанавливаемое значение
     */
    public void setTextKf(JFormattedTextField jftf, double value) {

        String valuestring = String.valueOf(value);
        //System.out.println(valuestring);
        String array[] = valuestring.split("\\.");
        int length = array[0].length();
        //int length = ((int) value + "").length();
        String space = "";
        for (int i = 0; i < 3 - length; i++) {
            space += " ";
        }
        jftf.setText(space + String.format("%.2f", value).replace(',', '.'));
    }

    /**
     * @param jftf  Форматированное текстовое поле в которое устанавливается
     *              значение
     * @param value Устанавливаемое значение
     */
    public void setTextUSTO_(JFormattedTextField jftf, double value) {

        int length = ((int) value + "").length();
        String space = "";
        for (int i = 0; i < 6 - length; i++) {
            space += " ";
        }
        jftf.setValue(space + String.format("%.2f", value).replace(',', '.'));
    }

    /**
     * Добавление радиокнопок с коэффициентами
     */
    private void initRadioButton() {
        //Новые
        buttonGroup1.add(jRadioButtonNew);
        //Старые
        buttonGroup1.add(jRadioButtonOld);
        //Пользовательские
        buttonGroup1.add(jRadioButtonUser);
        // 1/2 затрат
        buttonGroup1.add(jRadioButtonHalf);
        //По умолчанию активны Пользовательские кф
        jRadioButtonUser.setSelected(true);
    }

    /**
     * Загрузка данных калькуляции в форму по id
     *
     * @param id - id Калькуляции
     */
    public void loadFieldData(int id) {
        //Устанавливаем id
        setId(id);

        ValueCalculation valueCalculation;

        try {
            //valueCalculation = new ValueCalculation();
            //Получаем из базы данные калькуляции по id
            valueCalculation = ppdb.getDataCalculation(getId());


            /**
             * Установка типа калькуляции.
             * Для калькуляций по услугам скрываем ненужные поля на форме
             *
             */
            setItTypeCalculation(valueCalculation.getId_type_calculation());
            if (getItTypeCalculation() == 2) {
                jLabel7.setVisible(false);
                jLabel8.setVisible(false);
                etRzmk.setVisible(false);
                etRzmn.setVisible(false);

            }

            //Устанавливаем номер прейскуранта
            setNumberPrice(valueCalculation.getNumberPrice());

            /**
             * Проверяем  список прейскурантов по типу калькуляции.
             * Далее  выбираем нужный прейскурант. Если калькуляция без прейскуранта, то в комбике отображается НЕ ВКЛЮЧАТЬ В ПРЕЙСКУРАНТ .
             */
            if (!ppdb.getPrice(valueCalculation.getId_type_calculation()).isEmpty()) {
                jComboBoxPrice.setEnabled(true);
                jComboBoxPrice.setModel(new ComboboxPrice(ppdb.getPrice(valueCalculation.getId_type_calculation())));
                for (int i = 0; i < jComboBoxPrice.getModel().getSize(); i++) {
                    if (jComboBoxPrice.getModel().getElementAt(i).toString() == null ? ppdb.getPriceName(valueCalculation.getNumberPrice()) == null : jComboBoxPrice.getModel().getElementAt(i).toString().equals(ppdb.getPriceName(valueCalculation.getNumberPrice()))) {
                        jComboBoxPrice.setSelectedItem(jComboBoxPrice.getModel().getElementAt(i));
                    }
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Примечание
        etPrim.setText(valueCalculation.getPrim());

        //Полная Себестоимость
        //etCc.setText(String.format("%.2f", valueCalculation.getCc()).replace(',', '.'));
        etCc.setValue(valueCalculation.getCc());
        //Цена реализации без НДС 1С
        //etCno.setText(String.format("%.2f", valueCalculation.getCno()).replace(',', '.'));
        etCno.setValue(valueCalculation.getCno());
        //Цена реализации без НДС 2С
        //etCno2.setText(String.format("%.2f", valueCalculation.getCno2()).replace(',', '.'));
        etCno2.setValue(valueCalculation.getCno2());
        //Цена реализации с НДС 1С
        //etCnr.setText(String.format("%.2f", valueCalculation.getCnr()).replace(',', '.'));
        etCnr.setValue(valueCalculation.getCnr());
        //Цена реализации с НДС 2С
        //etCnr2.setText(String.format("%.2f", valueCalculation.getCnr2()).replace(',', '.'));
        etCnr2.setValue(valueCalculation.getCnr2());
        //Цена реализации без НДС 1С
        tvCno.setText(String.format("%.2f", valueCalculation.getCno()).replace(',', '.'));
        //Цена реализации без НДС 2С
        tvCno2.setText(String.format("%.2f", valueCalculation.getCno2()).replace(',', '.'));
        //Цена реализации c НДС 1С
        tvCnr.setText(String.format("%.2f", valueCalculation.getCnr()).replace(',', '.'));
        //Цена реализации c НДС 2С
        tvCnr2.setText(String.format("%.2f", valueCalculation.getCnr2()).replace(',', '.'));

        //Сумма НДС 1С
        //etCnv.setText(String.format("%.0f", valueCalculation.getCnv()).replace(',', '.'));
        etCnv.setValue(valueCalculation.getCnv());
        //Сумма НДС 2С
        //etCnv2.setText(String.format("%.0f", valueCalculation.getCnv2()).replace(',', '.'));
        etCnv2.setValue(valueCalculation.getCnv2());
        // НДС %
        etCnvp.setText(String.format("%.2f", valueCalculation.getCnvp()).replace(',', '.'));

        // Новые поля 26.08.2019
        // Размер кредита/займа
        etCreditPercent.setValue(valueCalculation.getPercentCredit());
        // Сумма по кредиту/займу
        etCostWithCredit.setValue(valueCalculation.getValueCredit());
        // Себестоимость с кредитом
        etPrimeCostWithCredit.setValue(valueCalculation.getPrimeCostCredit());
        // Прибыль с учетом кредита
        etProfitWithCredit.setValue(valueCalculation.getProfitCredit());


        // Цена реализации без НДС с кредитом
        etCnoCredit.setValue(valueCalculation.getCnoCredit());
        //Цена реализации без НДС 2С с кредитом
        etCno2Credit.setValue(valueCalculation.getCno2Credit());

        // Сумма НДС с кредитом
        etCnvCredit.setValue(valueCalculation.getCnvCredit());
        //Сумма НДС 2С с кредитом
        etCnv2Credit.setValue(valueCalculation.getCnv2Credit());

        //Цена реализации с НДС 1С с кредитом
        etCnrCredit.setValue(valueCalculation.getCnrCredit());
        //Цена реализации с НДС 2С с кредитом
        etCnr2Credit.setValue(valueCalculation.getCnr2Credit());

        //Блок СЫРЬЕ - ЦЕНА
        setTextCSR(etCsr1, valueCalculation.getCsr()[0]);
        setTextCSR(etCsr2, valueCalculation.getCsr()[1]);
        setTextCSR(etCsr3, valueCalculation.getCsr()[2]);
        setTextCSR(etCsr4, valueCalculation.getCsr()[3]);
        setTextCSR(etCsr5, valueCalculation.getCsr()[4]);
        setTextCSR(etCsr6, valueCalculation.getCsr()[5]);
        setTextCSR(etCsr7, valueCalculation.getCsr()[6]);
        setTextCSR(etCsr8, valueCalculation.getCsr()[7]);
        setTextCSR(etCsr9, valueCalculation.getCsr()[8]);

        //Дата
        etDat.setText(valueCalculation.getDat());
        //Модель
        etFas.setText(valueCalculation.getFas());
        //Общехозяйственные расходы
        //etHzr.setText(String.format("%.1f", valueCalculation.getHzr()).replace(',', '.'));
        etHzr.setValue(valueCalculation.getHzr());
        //Коммерческие
        //etKmr.setText(String.format("%.1f", valueCalculation.getKmr()).replace(',', '.'));
        etKmr.setText(String.format("%.4f", valueCalculation.getKmr()));
        //Артикул
        etNar.setText(valueCalculation.getNar());
        //Дополнительные поле , которое находится под Артикулом на форме
        etNiz.setText(valueCalculation.getNiz());
        etNiz1.setText(valueCalculation.getNiz1());

        //Блок СЫРЬЕ - НАИМЕНОВАНИЕ
        etNsr1.setText(valueCalculation.getNsr()[0]);
        etNsr2.setText(valueCalculation.getNsr()[1]);
        etNsr3.setText(valueCalculation.getNsr()[2]);
        etNsr4.setText(valueCalculation.getNsr()[3]);
        etNsr5.setText(valueCalculation.getNsr()[4]);
        etNsr6.setText(valueCalculation.getNsr()[5]);
        etNsr7.setText(valueCalculation.getNsr()[6]);
        etNsr8.setText(valueCalculation.getNsr()[7]);
        etNsr9.setText(valueCalculation.getNsr()[8]);

        //Доп поле под Ед.Размер
        etObr.setText(valueCalculation.getObr());

        //Полотно
        etPol.setText(valueCalculation.getPol());

        //Прибыль
        //etPrb.setText(String.format("%.1f", valueCalculation.getPrb()).replace(',', '.'));
        etPrb.setValue(valueCalculation.getPrb());

        //Устновка формат Прибыль
        setTextKf(etPrbp, valueCalculation.getPrbp());

        //Общепроизводственные расходы
        //etPrr.setText(String.format("%.2f", valueCalculation.getPrr()).replace(',', '.'));
        etPrr.setValue(valueCalculation.getPrr());
        //ПРоизводственная себестоимость
        //etPss.setText(String.format("%.2f", valueCalculation.getPss()).replace(',', '.'));
        etPss.setValue(valueCalculation.getPss());
        //Размер мин
        etRzmk.setText(valueCalculation.getRzmk());
        //Размер макс
        etRzmn.setText(valueCalculation.getRzmn());
        //Основное сырье и материалы
        //etSm.setText(String.format("%.2f", valueCalculation.getSm()).replace(',', '.'));
        etSm.setValue(valueCalculation.getSm());
        //Транспортные расходы
        etSmts.setText(String.format("%.4f", valueCalculation.getSmts()).replace(',', '.'));
        //Возвратные отходы
        etSmvs.setText(String.format("%.4f", valueCalculation.getSmvs()).replace(',', '.'));
        //Отчисления на соц нужды
        //etSn.setText(String.format("%.2f", valueCalculation.getSn()).replace(',', '.'));
        etSn.setValue(valueCalculation.getSn());
        //Ед.Измер
        etSyr1.setText(valueCalculation.getSyr1());
        etSyr2.setText(valueCalculation.getSyr2());
        //Топливо и эенергия
        //etTe.setText(String.format("%.1f", valueCalculation.getTe()).replace(',', '.'));
        etTe.setValue(valueCalculation.getTe());
        //Топливо норматив формат
        setTextTEN(etTen, valueCalculation.getTen());
        //Топливо в рублях
        setTextZP(etTes, valueCalculation.getTes());
        //Вспомогательные материалы
        // etVm.setText(String.format("%.1f", valueCalculation.getVm()).replace(',', '.'));
        etVm.setValue(valueCalculation.getVm());
        //Вспомогательные материалы в рублях
        setTextVMS(etVms, valueCalculation.getVms());
        //Транспортные расходы(вспомогат материалы)
        //etVmts.setText(String.format("%.2f", valueCalculation.getVmts()).replace(',', '.'));
        //System.out.println("Запись в VMTS "+valueCalculation.getVmts());

        etVmts.setText(String.format("%.4f", valueCalculation.getVmts()));

        //Блок СЫРЬЕ -КГ
        setTextWSR(etWsr1, valueCalculation.getWsr()[0]);
        setTextWSR(etWsr2, valueCalculation.getWsr()[1]);
        setTextWSR(etWsr3, valueCalculation.getWsr()[2]);
        setTextWSR(etWsr4, valueCalculation.getWsr()[3]);
        setTextWSR(etWsr5, valueCalculation.getWsr()[4]);
        setTextWSR(etWsr6, valueCalculation.getWsr()[5]);
        setTextWSR(etWsr7, valueCalculation.getWsr()[6]);
        setTextWSR(etWsr8, valueCalculation.getWsr()[7]);
        setTextWSR(etWsr9, valueCalculation.getWsr()[8]);

        //Суммирование кг
        double sum = 0;
        for (int i = 0; i < valueCalculation.getWsr().length; i++) {
            sum = sum + valueCalculation.getWsr()[i];
        }

        //Сумма
        tvSum.setText(String.format("%.5f", sum));

        //Дополнительная ЗП
        setTextZP(etZpd, valueCalculation.getZpd());
        //Основная ЗП
        setTextZP(etZpo, valueCalculation.getZpo());
        //Итого ЗП
        //etZp.setText(String.format("%.1f", valueCalculation.getZp()).replace(',', '.'));
        etZp.setValue(valueCalculation.getZp());
        //Услуги сторонних орг
        setTextUSTO(etUsto, valueCalculation.getUsto());
        // НДС 1С %
        etCnvp.setText(String.format("%.0f", valueCalculation.getCnvp()).replace(',', '.'));
        //ВОзвратные отходы,%
        etSmvp.setText(String.format("%.3f", valueCalculation.getSmvp()).replace(',', '.'));
        //Трансп расходы(вспомогат) , %
        etVmtp.setText(String.format("%.2f", valueCalculation.getVmtp()).replace(',', '.'));
        //Транспортные расходы , %
        etSmtp.setText(String.format("%.2f", valueCalculation.getSmtp()).replace(',', '.'));
        //Общепроизводственные расходы, %
        setTextKf(etPrrp, valueCalculation.getPrrp());
        //Общехозяйственные расходы
        setTextKf(etHzrp, valueCalculation.getHzrp());
        //Коммерческие расходы , %
        setTextKf(etKmrp, valueCalculation.getKmrp());
        //etUsto.setText(String.format("%.1f", valueCalculation.getUsto()).replace(',', '.'));

        //Установим текущую калькуляцию с данными
        setValueCalc(valueCalculation);

        /**
         * Проверка на дублирование. Если нажали дублировать , то  не включаем в прейскурант и рассчитываем по новым кф .
         */
        if (copy) {
            btnNewActionPerformed(null);
            setNumberPrice(0);

            /**
             * Получаем список прейскурантов для комбика для типа калькуляций
             */
            if (!ppdb.getPrice(valueCalculation.getId_type_calculation()).isEmpty()) {
                jComboBoxPrice.setEnabled(true);
                jComboBoxPrice.setModel(new ComboboxPrice(ppdb.getPrice(valueCalculation.getId_type_calculation())));
                for (int i = 0; i < jComboBoxPrice.getModel().getSize(); i++) {
                    if (jComboBoxPrice.getModel().getElementAt(i).toString() == null ? ppdb.getPriceName(valueCalculation.getNumberPrice()) == null : jComboBoxPrice.getModel().getElementAt(i).toString().equals(ppdb.getPriceName(valueCalculation.getNumberPrice()))) {
                        jComboBoxPrice.setSelectedItem(jComboBoxPrice.getModel().getElementAt(i));
                    }
                }

            }
            //copy = false;
        }

    }

    /**
     * Создание новой калькуляции.
     *
     * @param ptk -   код ассортимента , который завязан с группой и видом продукции.
     */
    public void newCalculation(String ptk) {
        try {
            setId(ppdb.newCalcalculation(ptk));
            loadFieldData(getId());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Расчет калькуляции
     *
     * @param id - id калькуляции
     * @return - возвращает true если расчет прошел без ошибок , иначе false .
     */
    public boolean calculation(int id) {
        ValueCalculation valueCalculation = new ValueCalculation();
        close = false;
        try {

            double sm, smWsr;
            Factor factor;
            //КОэффициенты
            factor = new Factor();
            sm = 0;
            smWsr = 0;
            //СЫРЬЕ  - НАЗВАНИЕ
            String[] nsr = new String[9];
            //СЫРЬЕ  - КГ
            double[] wsr = new double[9];
            //СЫРЬЕ  - ЦЕНА
            double[] csr = new double[9];

            /**
             * Устанваливаем коэффициенты в зависимости от выбора радиокнопки
             */
            switch (getStatus()) {

                // Новые коэффициенты
                case 0:
                    try {
                        factor = ppdb.getFactor(getId(), "new");
                        //System.out.println(factor.getSmtp());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    valueCalculation.setSmtp(factor.getSmtp());
                    valueCalculation.setSmvp(factor.getSmvp());
                    valueCalculation.setVmtp(factor.getVmtp());
                    valueCalculation.setPrrp(factor.getPrrp());
                    valueCalculation.setHzrp(factor.getHzrp());
                    valueCalculation.setKmrp(factor.getKmrp());
                    valueCalculation.setCnvp(factor.getCnvp());

                    etCnvp.setText(String.format("%.0f", valueCalculation.getCnvp()).replace(',', '.'));
                    etSmvp.setText(String.format("%.3f", valueCalculation.getSmvp()).replace(',', '.'));
                    etVmtp.setText(String.format("%.2f", valueCalculation.getVmtp()).replace(',', '.'));
                    etSmtp.setText(String.format("%.2f", valueCalculation.getSmtp()).replace(',', '.'));
                    setTextKf(etPrrp, valueCalculation.getPrrp());
                    setTextKf(etHzrp, valueCalculation.getHzrp());
                    setTextKf(etKmrp, valueCalculation.getKmrp());
                    break;
                // Старые коэффициенты
                case 1:
                    try {
                        factor = ppdb.getFactor(getId(), "old");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    valueCalculation.setSmtp(factor.getSmtp());
                    valueCalculation.setSmvp(factor.getSmvp());
                    valueCalculation.setVmtp(factor.getVmtp());
                    valueCalculation.setPrrp(factor.getPrrp());
                    valueCalculation.setHzrp(factor.getHzrp());
                    valueCalculation.setKmrp(factor.getKmrp());
                    valueCalculation.setCnvp(factor.getCnvp());
                    etCnvp.setText(String.format("%.0f", valueCalculation.getCnvp()).replace(',', '.'));
                    etSmvp.setText(String.format("%.3f", valueCalculation.getSmvp()).replace(',', '.'));
                    etVmtp.setText(String.format("%.2f", valueCalculation.getVmtp()).replace(',', '.'));
                    etSmtp.setText(String.format("%.2f", valueCalculation.getSmtp()).replace(',', '.'));
                    setTextKf(etPrrp, valueCalculation.getPrrp());
                    setTextKf(etHzrp, valueCalculation.getHzrp());
                    setTextKf(etKmrp, valueCalculation.getKmrp());
                    break;
                // Пользовательские коэффициенты
                case 2:
                    if (!"".equals(etSmtp.getText()) && !"".equals(etSmvp.getText()) && !"".equals(etVmtp.getText())
                            && !"".equals(etPrrp.getText()) && !"".equals(etHzrp.getText()) && !"".equals(etKmrp.getText())
                            && !"".equals(etCnvp.getText())) {

                        try {
                            valueCalculation.setSmtp(Double.valueOf(etSmtp.getText()));
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Ошибка ввода ТР сырье", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        try {
                            valueCalculation.setSmvp(Double.valueOf(etSmvp.getText()));
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Ошибка ввода ВО сырье", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        try {
                            valueCalculation.setVmtp(Double.valueOf(etVmtp.getText()));
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Ошибка ввода Транспортные расходы ВМ:", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        try {
                            valueCalculation.setCnvp(Double.valueOf(etCnvp.getText()));
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Ошибка ввода НДС:", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                        valueCalculation.setPrrp(Double.valueOf(etPrrp.getText()));
                        valueCalculation.setHzrp(Double.valueOf(etHzrp.getText()));
                        valueCalculation.setKmrp(Double.valueOf(etKmrp.getText()));

                    } else {
                        return false;
                    }
                    break;
                // Половинчатые коэффициенты (новые/2)
                case 3:
                    try {
                        factor = ppdb.getFactor(getId(), "new");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    valueCalculation.setSmtp(factor.getSmtp());
                    valueCalculation.setSmvp(factor.getSmvp());
                    valueCalculation.setVmtp(factor.getVmtp());
                    valueCalculation.setPrrp(new BigDecimal(factor.getPrrp() / 2)
                            .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue()); // Делить на 2
                    valueCalculation.setHzrp(new BigDecimal(factor.getHzrp() / 2)
                            .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue()); // Делить на 2
                    valueCalculation.setKmrp(factor.getKmrp());
                    valueCalculation.setCnvp(factor.getCnvp());

                    etCnvp.setText(String.format("%.0f", valueCalculation.getCnvp()).replace(',', '.'));
                    etSmvp.setText(String.format("%.3f", valueCalculation.getSmvp()).replace(',', '.'));
                    etVmtp.setText(String.format("%.2f", valueCalculation.getVmtp()).replace(',', '.'));
                    etSmtp.setText(String.format("%.2f", valueCalculation.getSmtp()).replace(',', '.'));
                    setTextKf(etKmrp, valueCalculation.getKmrp());
                    setTextKf(etPrrp, valueCalculation.getPrrp());
                    setTextKf(etHzrp, valueCalculation.getHzrp());
                    break;
            }


            /**
             *
             * Обработка блока СЫРЬЕ.
             * Учитываются те строки, в которых заполнено НАИМЕНОВАНИЕ - КГ - ЦЕНА
             * Суммируется КГ и ЦЕНА
             */
            if (!"".equals(etNsr1.getText()) && !"".equals(etWsr1.getText()) && !"".equals(etCsr1.getText())) {
                sm += Double.valueOf(etWsr1.getText().replaceAll(" ", "")) * Double.valueOf(etCsr1.getText().replaceAll(" ", ""));
                smWsr += Double.valueOf(etWsr1.getText().replaceAll(" ", ""));

                nsr[0] = etNsr1.getText();


                try {
                    csr[0] = Double.valueOf(etCsr1.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода цена[1]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    wsr[0] = Double.valueOf(etWsr1.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода кг[1]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

            }
            if (!"".equals(etNsr2.getText()) && !"".equals(etWsr2.getText()) && !"".equals(etCsr2.getText())) {
                sm += Double.valueOf(etWsr2.getText().replaceAll(" ", "")) * Double.valueOf(etCsr2.getText().replaceAll(" ", ""));
                smWsr += Double.valueOf(etWsr2.getText().replaceAll(" ", ""));

                nsr[1] = etNsr2.getText();
                try {
                    csr[1] = Double.valueOf(etCsr2.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода цена[2]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    wsr[1] = Double.valueOf(etWsr2.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода кг[2]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (!"".equals(etNsr3.getText()) && !"".equals(etWsr3.getText()) && !"".equals(etCsr3.getText())) {
                sm += Double.valueOf(etWsr3.getText().replaceAll(" ", "")) * Double.valueOf(etCsr3.getText().replaceAll(" ", ""));
                smWsr += Double.valueOf(etWsr3.getText().replaceAll(" ", ""));

                nsr[2] = etNsr3.getText();
                try {
                    csr[2] = Double.valueOf(etCsr3.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода цена[3]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    wsr[2] = Double.valueOf(etWsr3.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода кг[3]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (!"".equals(etNsr4.getText()) && !"".equals(etWsr4.getText()) && !"".equals(etCsr4.getText())) {
                sm += Double.valueOf(etWsr4.getText().replaceAll(" ", "")) * Double.valueOf(etCsr4.getText().replaceAll(" ", ""));
                smWsr += Double.valueOf(etWsr4.getText().replaceAll(" ", ""));

                nsr[3] = etNsr4.getText();
                try {
                    csr[3] = Double.valueOf(etCsr4.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода цена[4]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    wsr[3] = Double.valueOf(etWsr4.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода кг[4]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (!"".equals(etNsr5.getText()) && !"".equals(etWsr5.getText()) && !"".equals(etCsr5.getText())) {
                sm += Double.valueOf(etWsr5.getText().replaceAll(" ", "")) * Double.valueOf(etCsr5.getText().replaceAll(" ", ""));
                smWsr += Double.valueOf(etWsr5.getText().replaceAll(" ", ""));

                nsr[4] = etNsr5.getText();
                try {
                    csr[4] = Double.valueOf(etCsr5.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода цена[5]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    wsr[4] = Double.valueOf(etWsr5.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(null, "Ошибка ввода кг[5]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (!"".equals(etNsr6.getText()) && !"".equals(etWsr6.getText()) && !"".equals(etCsr6.getText())) {
                sm += Double.valueOf(etWsr6.getText().replaceAll(" ", "")) * Double.valueOf(etCsr6.getText().replaceAll(" ", ""));
                smWsr += Double.valueOf(etWsr6.getText().replaceAll(" ", ""));

                nsr[5] = etNsr6.getText();
                try {
                    csr[5] = Double.valueOf(etCsr6.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ошибка ввода цена[6]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    wsr[5] = Double.valueOf(etWsr6.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(null, "Ошибка ввода кг[6]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (!"".equals(etNsr7.getText()) && !"".equals(etWsr7.getText()) && !"".equals(etCsr7.getText())) {
                sm += Double.valueOf(etWsr7.getText().replaceAll(" ", "")) * Double.valueOf(etCsr7.getText().replaceAll(" ", ""));
                smWsr += Double.valueOf(etWsr7.getText().replaceAll(" ", ""));

                nsr[6] = etNsr7.getText();
                try {
                    csr[6] = Double.valueOf(etCsr7.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(null, "Ошибка ввода цена[7]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    wsr[6] = Double.valueOf(etWsr7.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    log.error("Ошибка ввода кг[7]: " + e);
                    JOptionPane.showMessageDialog(null, "Ошибка ввода кг[7]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (!"".equals(etNsr8.getText()) && !"".equals(etWsr8.getText()) && !"".equals(etCsr8.getText())) {
                sm += Double.valueOf(etWsr8.getText().replaceAll(" ", "")) * Double.valueOf(etCsr8.getText().replaceAll(" ", ""));
                smWsr += Double.valueOf(etWsr8.getText().replaceAll(" ", ""));

                nsr[7] = etNsr8.getText();
                try {
                    csr[7] = Double.valueOf(etCsr8.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    log.error("Ошибка ввода цена[8]: " + e);
                    JOptionPane.showMessageDialog(null, "Ошибка ввода цена[8]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    wsr[7] = Double.valueOf(etWsr8.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    log.error("Ошибка ввода кг[8]: " + e);
                    JOptionPane.showMessageDialog(null, "Ошибка ввода кг[8]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            if (!"".equals(etNsr9.getText()) && !"".equals(etWsr9.getText()) && !"".equals(etCsr9.getText())) {
                sm += Double.valueOf(etWsr9.getText().replaceAll(" ", "")) * Double.valueOf(etCsr9.getText().replaceAll(" ", ""));
                smWsr += Double.valueOf(etWsr9.getText().replaceAll(" ", ""));

                nsr[8] = etNsr9.getText();
                try {
                    csr[8] = Double.valueOf(etCsr9.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    log.error("Ошибка ввода цена[9]: " + e);
                    JOptionPane.showMessageDialog(null, "Ошибка ввода цена[9]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    wsr[8] = Double.valueOf(etWsr9.getText().replaceAll(" ", ""));
                } catch (NumberFormatException e) {
                    log.error("Ошибка ввода кг[9]: " + e);
                    JOptionPane.showMessageDialog(null, "Ошибка ввода кг[9]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            // СУММА ВЕС
            tvSum.setText(String.format("%.5f", smWsr));

            //Массив НАИМЕНОВАНИЙ
            valueCalculation.setNsr(nsr);
            //Массив КГ
            valueCalculation.setWsr(wsr);
            //Массив ЦЕНА
            valueCalculation.setCsr(csr);

            //Проверка Топливо Норматив
            if (!"".equals(etTen.getText())) {
                valueCalculation.setTen(Double.valueOf(etTen.getValue().toString().replaceAll(" ", "")));
            }

            //Топливо в рублях
            if (!"".equals(etTes.getText())) {
                valueCalculation.setTes(Double.valueOf(etTes.getText().replaceAll(" ", "").replaceAll(",", ".")));
            }

            //ПРоверска Расход вспом мат в рублях
            if (!"".equals(etVms.getText())) {
                valueCalculation.setVms(Double.valueOf(etVms.getValue().toString().replaceAll(" ", "")));
            } else {
                return false;
            }

            //Проверка полей Основная ЗП и Дополнительная ЗП
            if (!"".equals(etZpo.getText()) && !"".equals(etZpd.getText())) {
                valueCalculation.setZpo(Double.valueOf(etZpo.getValue().toString().replaceAll(" ", "")));
                valueCalculation.setZpd(Double.valueOf(etZpd.getValue().toString().replaceAll(" ", "")));
            } else {
                return false;
            }

            //Не нуждаются в проверке
            valueCalculation.setFas(etFas.getText());
            valueCalculation.setNar(etNar.getText());
            valueCalculation.setNiz(etNiz.getText());
            valueCalculation.setNiz1(etNiz1.getText());
            valueCalculation.setPol(etPol.getText());
            valueCalculation.setObr(etObr.getText());
            valueCalculation.setRzmn(etRzmn.getText());
            valueCalculation.setRzmk(etRzmk.getText());
            valueCalculation.setDat(etDat.getText());
            valueCalculation.setSyr1(etSyr1.getText());
            valueCalculation.setSyr2(etSyr2.getText());

            //System.out.println(getNumberPrice());

            try {
                if (!ppdb.getPrice(getItTypeCalculation()).isEmpty()) {

                    //Значение прейскуранта
                    setNumberPrice(((ComboboxPrice) jComboBoxPrice.getModel()).getId());
                }

                if (getNumberPrice() == 0) {
                    valueCalculation.setNumberPrice(0);
                } else {
                    valueCalculation.setNumberPrice(getNumberPrice());
                }

                //Услуги сторонних организаций
                valueCalculation.setUsto(Double.valueOf(etUsto.getText().replaceAll(" ", "")));
                valueCalculation.setUsto(new BigDecimal(
                        Double.valueOf(etUsto.getText().replaceAll(" ", ""))).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());

                //Основное сырье и материалы
                valueCalculation.setSmts(new BigDecimal(sm * valueCalculation.getSmtp() / 100).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());
                valueCalculation.setSmvs(new BigDecimal(sm * valueCalculation.getSmvp() / 100).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());

                // 4
                valueCalculation.setSm(new BigDecimal(
                        sm + valueCalculation.getSmts() - valueCalculation.getSmvs()).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());
                //Топливо и энергия
                //valueCalculation.setTe(valueCalculation.getTen() * smWsr + valueCalculation.getTes());

                // 5
                valueCalculation.setTe(new BigDecimal(
                        valueCalculation.getTen() * smWsr + valueCalculation.getTes()).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());
                //Вспомогательные материалы
                // 6
                valueCalculation.setVmts(new BigDecimal(valueCalculation.getVms() * valueCalculation.getVmtp() / 100)
                        .setScale(6, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());
                //valueCalculation.setVm(valueCalculation.getVms() + valueCalculation.getVmts());
                // 7
                valueCalculation.setVm(new BigDecimal(
                        valueCalculation.getVms() + valueCalculation.getVmts()).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());

                //Зарплата
                // valueCalculation.setZp(valueCalculation.getZpo() + valueCalculation.getZpd());
                // 8
                valueCalculation.setZp(new BigDecimal(
                        valueCalculation.getZpo() + valueCalculation.getZpd()).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());
                //Социальные нужды
                // 9
                valueCalculation.setSnp(Double.valueOf(etSnp.getText()));
                //valueCalculation.setSn(valueCalculation.getSnp() * valueCalculation.getZp() / 100);
                // 10
                valueCalculation.setSn(new BigDecimal(
                        valueCalculation.getSnp() * valueCalculation.getZp() / 100).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());
                //Общепроизводственные расходы
                //  valueCalculation.setPrr(valueCalculation.getPrrp() * valueCalculation.getZp() / 100);
                // 11
                valueCalculation.setPrr(new BigDecimal(
                        valueCalculation.getPrrp() * valueCalculation.getZp() / 100).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());
                //Производственная себестоимость
                // 12
                valueCalculation.setPss(new BigDecimal(valueCalculation.getSm()// Основные с и м
                        + valueCalculation.getTe()// Топливо и энергия
                        + valueCalculation.getVm()// Вспом материалы
                        + valueCalculation.getZp()// Зарплата
                        + valueCalculation.getSn()// СоцНужды
                        + valueCalculation.getPrr()// Общепроизв. расходы
                        + valueCalculation.getUsto()).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());

                // Общехозяйственные расходы
                //13
                valueCalculation.setHzr(new BigDecimal(
                        valueCalculation.getHzrp() * valueCalculation.getZp() / 100).setScale(6, RoundingMode.HALF_UP).setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());

                //Коммерческие расходы
                //14
                BigDecimal bd = new BigDecimal(
                        valueCalculation.getKmrp() * valueCalculation.getPss() / 100).setScale(10, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP);

                //System.out.println(String.format("РАСЧЕТНЫЙ %.4f",valueCalculation.getKmrp() * valueCalculation.getPss() / 100));

                valueCalculation.setKmr(bd.doubleValue());


                //Себестоимость
                //15
                valueCalculation.setCc(new BigDecimal(valueCalculation.getPss() + valueCalculation.getHzr() +
                        valueCalculation.getKmr()).setScale(10, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());

                // etKmr.setText(String.valueOf(valueCalculation.getKmr()));
                etVmts.setText(String.format("%.4f", valueCalculation.getKmr()));

            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Проверьте правильность ввода", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException e) {
            log.error("Ошибка : " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (NumberFormatException e) {
            log.error("Ошибка ввода чисел: " + e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка ввода чисел", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //Прибыль и Цена реализации без ндс 1 сорта
        //Расчет прибыли от цены
        if (isBool()) {
            if (!"".equals(etCno.getText())) {
                valueCalculation.setCno(Double.valueOf(etCno.getText()));
                //16
                try {
                    valueCalculation.setPrbp(new BigDecimal(100 * (valueCalculation.getCno() - valueCalculation.getCc()) / valueCalculation.getCc())
                            .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
                } catch (NumberFormatException e) {
                    valueCalculation.setPrb(0);
                }
                //17
                valueCalculation.setPrb(new BigDecimal(valueCalculation.getCc() * valueCalculation.getPrbp() / 100)
                        .setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());
                setTextKf(etPrbp, valueCalculation.getPrbp());
            } else {
                return false;
            }
        }

        //Расчет цены от прибыли
        if (!isBool()) {
            if (!"".equals(etPrbp.getText().replaceAll(" ", ""))) {
                valueCalculation.setPrbp(Double.valueOf(etPrbp.getText().replaceAll(" ", "")));
                // 18
                try {
                    valueCalculation.setPrb(new BigDecimal(valueCalculation.getCc() * valueCalculation.getPrbp() / 100)
                            .setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());
                } catch (NumberFormatException e) {
                    valueCalculation.setPrb(0);
                }
                // 19
                valueCalculation.setCno(new BigDecimal(valueCalculation.getPrb() + valueCalculation.getCc())
                        .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
                //etCno.setText(String.format("%.0f", valueCalculation.getCno()).replace(',', '.'));
                etCno.setValue(valueCalculation.getCno());
            } else {
                return false;
            }
        }

        valueCalculation.setPercentCredit(etCreditPercent.getValueDouble());

        // Расчет кредитных параметров
        valueCalculation.setValueCredit(new BigDecimal(valueCalculation.getCc() * valueCalculation.getPercentCredit() / 100)
                .setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());

        valueCalculation.setPrimeCostCredit(new BigDecimal(valueCalculation.getCc() + valueCalculation.getValueCredit())
                .setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());

        valueCalculation.setProfitCredit(new BigDecimal(valueCalculation.getCno() - valueCalculation.getPrimeCostCredit())
                .setScale(5, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).doubleValue());

        valueCalculation.setCnoCredit(new BigDecimal(valueCalculation.getProfitCredit() + valueCalculation.getPrimeCostCredit())
                .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());

        double koeff = 0.95f;
        double cnoCredit = valueCalculation.getCnoCredit() * koeff;
        double cno2Credit = new BigDecimal(cnoCredit).setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue();
        valueCalculation.setCno2Credit(cno2Credit);

        //НДС 1 сорта
        valueCalculation.setCnvCredit(new BigDecimal(valueCalculation.getCnoCredit() * valueCalculation.getCnvp() / 100)
                .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());

        //НДС 2 сорта
        valueCalculation.setCnv2Credit(new BigDecimal(valueCalculation.getCno2Credit() * valueCalculation.getCnvp() / 100)
                .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //Цена реализации с НДС 1 сорта
        valueCalculation.setCnrCredit(new BigDecimal(valueCalculation.getCnoCredit() + valueCalculation.getCnvCredit())
                .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //Цена реализации с НДС 2 сорта
        valueCalculation.setCnr2Credit(new BigDecimal(valueCalculation.getCno2Credit() + valueCalculation.getCnv2Credit())
                .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
        /**
         * ДЛЯ ЧУЛОЧНО-НОСОЧНЫХ И КРАШЕНИЕ ПРЯЖИ
         */
        if (!"3".equals(ppdb.getDataCalculation(getId()).getGrup()) && !"30".equals(ppdb.getDataCalculation(getId()).getGrup())) {
            //Цена реализации без ндс 2 сорта
            koeff = 0.95f;
            double cno = valueCalculation.getCno() * koeff;
            double cno2 = new BigDecimal(cno).setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue();
            valueCalculation.setCno2(cno2);
            //System.err.println("Расчет ЦНО 2 " + cno2 + " - " + valueCalculation.getCno());

            //НДС 1 сорта
            valueCalculation.setCnv(new BigDecimal(valueCalculation.getCno() * valueCalculation.getCnvp() / 100)
                    .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
            //НДС 2 сорта
            valueCalculation.setCnv2(new BigDecimal(valueCalculation.getCno2() * valueCalculation.getCnvp() / 100)
                    .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
            //Цена реализации с НДС 1 сорта
            valueCalculation.setCnr(new BigDecimal(valueCalculation.getCno() + valueCalculation.getCnv())
                    .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
            //Цена реализации с НДС 2 сорта
            valueCalculation.setCnr2(new BigDecimal(valueCalculation.getCno2() + valueCalculation.getCnv2())
                    .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
        } else {
            valueCalculation.setCno2(0);
            //НДС 1 сорта
            valueCalculation.setCnv(new BigDecimal(valueCalculation.getCno() * valueCalculation.getCnvp() / 100)
                    .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
            //НДС 2 сорта
            valueCalculation.setCnv2(0);
            //Цена реализации с НДС 1 сорта
            valueCalculation.setCnr(new BigDecimal(valueCalculation.getCno() + valueCalculation.getCnv())
                    .setScale(3, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
            //Цена реализации с НДС 2 сорта
            valueCalculation.setCnr2(0);
        }

        valueCalculation.setUsto(Double.valueOf(etUsto.getText().replaceAll(" ", "")));

        //Основное сырье и материалы
        //etSm.setText(String.format("%.2f", valueCalculation.getSm()).replace(',', '.'));
        etSm.setValue(valueCalculation.getSm());
        etSmts.setText(String.format("%.4f", valueCalculation.getSmts()).replace(',', '.'));
        etSmvs.setText(String.format("%.4f", valueCalculation.getSmvs()).replace(',', '.'));
        //Топливо и энергия
        //etTe.setText(String.format("%.1f", valueCalculation.getTe()).replace(',', '.'));
        etTe.setValue(valueCalculation.getTe());
        //Вспомогательные материалы
        // etVm.setText(String.format("%.1f", valueCalculation.getVm()).replace(',', '.'));
        etVm.setValue(valueCalculation.getVm());
        //Зарплата
        //etZp.setText(String.format("%.1f", valueCalculation.getZp()).replace(',', '.'));
        etZp.setValue(valueCalculation.getZp());
        //Общепроизводственные расходы
        //etPrr.setText(String.format("%.1f", valueCalculation.getPrr()).replace(',', '.'));
        etPrr.setValue(valueCalculation.getPrr());
        //Социальные нужды
        //etSn.setText(String.format("%.1f", valueCalculation.getSn()).replace(',', '.'));
        etSn.setValue(valueCalculation.getSn());
        //Производственная себестоимость
        //etPss.setText(String.format("%.1f", valueCalculation.getPss()).replace(',', '.'));
        etPss.setValue(valueCalculation.getPss());
        //Общехозяйственные расходы
        //etHzr.setText(String.format("%.1f", valueCalculation.getHzr()).replace(',', '.'));
        etHzr.setValue(valueCalculation.getHzr());
        //Себестоимость
        //etCc.setText(String.format("%.1f", valueCalculation.getCc()).replace(',', '.'));
        etCc.setValue(valueCalculation.getCc());
        //Коммерческие расходы
        //etKmr.setText(String.format("%.1f", valueCalculation.getKmr()).replace(',', '.'));
        etKmr.setText(String.format("%.4f", valueCalculation.getKmr()));
        //Прибыль и Цена реализации без ндс 1 сорта
        //etCno.setText(String.format("%.0f", valueCalculation.getCno()).replace(',', '.'));
        //etPrb.setText(String.format("%.1f", valueCalculation.getPrb()).replace(',', '.'));
        etPrb.setValue(valueCalculation.getPrb());
        //setTextPrbp( etPrbp ,  valueCalculation.getPrbp());
        //Цена реализации без ндс 2 сорта
        //etCno2.setText(String.format("%.0f", valueCalculation.getCno2()).replace(',', '.'));
        etCno2.setValue(valueCalculation.getCno2());
        //НДС 1 сорта
        //etCnv.setText(String.format("%.0f", valueCalculation.getCnv()).replace(',', '.'));
        etCnv.setValue(valueCalculation.getCnv());
        //НДС 2 сорта
        //etCnv2.setText(String.format("%.0f", valueCalculation.getCnv2()).replace(',', '.'));
        etCnv2.setValue(valueCalculation.getCnv2());
        //Цена реализации с НДС 1 сорта
        //etCnr.setText(String.format("%.0f", valueCalculation.getCnr()).replace(',', '.'));
        etCnr.setValue(valueCalculation.getCnr());
        //Цена реализации с НДС 2 сорта
        //etCnr2.setText(String.format("%.0f", valueCalculation.getCnr2()).replace(',', '.'));
        etCnr2.setValue(valueCalculation.getCnr2());

        etCreditPercent.setValue(valueCalculation.getPercentCredit());

        etCostWithCredit.setValue(valueCalculation.getValueCredit());
        etPrimeCostWithCredit.setValue(valueCalculation.getPrimeCostCredit());
        etProfitWithCredit.setValue(valueCalculation.getProfitCredit());

        etCnoCredit.setValue(valueCalculation.getCnoCredit());
        etCno2Credit.setValue(valueCalculation.getCno2Credit());

        etCnvCredit.setValue(valueCalculation.getCnvCredit());
        etCnv2Credit.setValue(valueCalculation.getCnv2Credit());

        etCnrCredit.setValue(valueCalculation.getCnrCredit());
        etCnr2Credit.setValue(valueCalculation.getCnr2Credit());

        etVmts.setText(String.valueOf(valueCalculation.getVmts()));

        //Заполнение рассчитанных данных
        setValueCalc(valueCalculation);

        double cno = Double.valueOf(etCno.getText());
        if (cno < 1000 & cno != 0.0) {
            etCno.setToolTipText(String.format("Старая цена:%.0f ", cno * 10000));
        } else if (cno > 1000 & cno != 0.0) {
            etCno.setToolTipText(String.format("Новая цена:%.2f ", cno / 10000));
        } else {
            etCno.setToolTipText(null);
        }

        double cno2 = Double.valueOf(etCno2.getText());
        if (cno2 < 1000 & cno2 != 0.0) {
            etCno2.setToolTipText(String.format("Старая цена:%.0f ", cno2 * 10000));
        } else if (cno2 > 1000 & cno2 != 0.0) {
            etCno2.setToolTipText(String.format("Новая цена:%.2f ", cno2 / 10000));
        } else {
            etCno2.setToolTipText(null);
        }

        setBool(false);
        return true;
    }

    /**
     * Проверка при сохранении. Дополнительная проверка данных, если были изменения без расчета.
     *
     * @param id               - id калькуляции
     * @param valueCalculation - объект для заполнения.
     */
    private void update(int id, ValueCalculation valueCalculation) {
        //СЫРЬЕ - НАИМЕНОВАНИЕ
        String[] nsr = new String[9];
        //СЫРЬЕ - КГ
        double[] wsr = new double[9];
        //СЫРЬЕ - ЦЕНА
        double[] csr = new double[9];

        if (!ppdb.getPrice(getItTypeCalculation()).isEmpty()) {
            valueCalculation.setNumberPrice(((ComboboxPrice) jComboBoxPrice.getModel()).getId());
        }
        try {
            valueCalculation.setCc(Double.valueOf(etCc.getText()));
            try {
                valueCalculation.setCno(Double.valueOf(etCno.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода ЦБРНДС 1 СОРТ: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода ЦБРНДС 1 СОРТ", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            valueCalculation.setCno2(Double.valueOf(etCno2.getText()));
            valueCalculation.setCnr(Double.valueOf(etCnr.getText()));
            valueCalculation.setCnr2(Double.valueOf(etCnr2.getText()));
            valueCalculation.setCnv(Double.valueOf(etCnv.getText()));
            valueCalculation.setCnv2(Double.valueOf(etCnv2.getText()));
            valueCalculation.setCnvp(Double.valueOf(etCnvp.getText()));

            try {
                csr[0] = (Double.valueOf(etCsr1.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода цена[1]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода цена[1]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                csr[1] = (Double.valueOf(etCsr2.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода цена[2]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода цена[2]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                csr[2] = (Double.valueOf(etCsr3.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода цена[3]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода цена[3]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                csr[3] = (Double.valueOf(etCsr4.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода цена[4]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода цена[4]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                csr[4] = (Double.valueOf(etCsr5.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода цена[5]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода цена[5]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                csr[5] = (Double.valueOf(etCsr6.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода цена[6]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода цена[6]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                csr[6] = (Double.valueOf(etCsr7.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода цена[7]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода цена[7]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                csr[7] = (Double.valueOf(etCsr8.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода цена[8]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода цена[8]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                csr[8] = (Double.valueOf(etCsr9.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода цена[9]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода цена[9]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            valueCalculation.setCsr(csr);

            valueCalculation.setFas(etFas.getText());
            valueCalculation.setHzr(Double.valueOf(etHzr.getText()));
            valueCalculation.setHzrp(Double.valueOf(etHzrp.getText()));
            valueCalculation.setKmr(Double.valueOf(etKmr.getText()));
            valueCalculation.setKmrp(Double.valueOf(etKmrp.getText()));
            valueCalculation.setNar(etNar.getText());
            valueCalculation.setNiz(etNiz.getText());
            valueCalculation.setNiz1(etNiz1.getText());

            nsr[0] = (etNsr1.getText());
            nsr[1] = (etNsr2.getText());
            nsr[2] = (etNsr3.getText());
            nsr[3] = (etNsr4.getText());
            nsr[4] = (etNsr5.getText());
            nsr[5] = (etNsr6.getText());
            nsr[6] = (etNsr7.getText());
            nsr[7] = (etNsr8.getText());
            nsr[8] = (etNsr9.getText());
            valueCalculation.setNsr(nsr);

            valueCalculation.setPol(etPol.getText());
            valueCalculation.setObr(etObr.getText());

            valueCalculation.setPrb(Double.valueOf(etPrb.getText()));
            valueCalculation.setPrbp(Double.valueOf(etPrbp.getText()));
            valueCalculation.setPrr(Double.valueOf(etPrr.getText()));
            valueCalculation.setPrrp(Double.valueOf(etPrrp.getText()));
            valueCalculation.setPss(Double.valueOf(etPss.getText()));
            valueCalculation.setRzmn(etRzmn.getText());
            valueCalculation.setRzmk(etRzmk.getText());
            valueCalculation.setSm(Double.valueOf(etSm.getText()));

            try {
                valueCalculation.setSmtp(Double.valueOf(etSmtp.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода ТР сырье: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода ТР сырье", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            valueCalculation.setSmts(Double.valueOf(etSmts.getText()));

            try {
                valueCalculation.setSmvp(Double.valueOf(etSmvp.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода ВО сырьё: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода ВО сырье", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            valueCalculation.setSmvs(Double.valueOf(etSmvs.getText()));
            valueCalculation.setSn(Double.valueOf(etSn.getText()));
            valueCalculation.setSnp(Double.valueOf(etSnp.getText()));
            valueCalculation.setSyr1(etSyr1.getText());
            valueCalculation.setSyr2(etSyr2.getText());
            valueCalculation.setTe(Double.valueOf(etTe.getText()));

            try {
                valueCalculation.setTen(Double.valueOf(etTen.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода Топливо норматив: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода Топливо норматив", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                valueCalculation.setTes(Double.valueOf(etTes.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода Топливо в рублях: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода Топливо в рублях", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                valueCalculation.setUsto(Double.valueOf(etUsto.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода УСО: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода УСО", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            valueCalculation.setVm(Double.valueOf(etVm.getText()));

            try {
                valueCalculation.setVms(Double.valueOf(etVms.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода РВМ в рублях: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода РВМ в рублях:", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                valueCalculation.setVmtp(Double.valueOf(etVmtp.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода Транспортные расходы ВМ: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода Транспортные расходы ВМ:", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            valueCalculation.setVmts(Double.valueOf(etVmts.getText()));

            try {
                wsr[0] = (Double.valueOf(etWsr1.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода кг[1]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода кг[1]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                wsr[1] = (Double.valueOf(etWsr2.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода кг[2]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода кг[2]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                wsr[2] = (Double.valueOf(etWsr3.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода кг[3]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода кг[3]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                wsr[3] = (Double.valueOf(etWsr4.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода кг[4]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода кг[4]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                wsr[4] = (Double.valueOf(etWsr5.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода кг[5]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода кг[5]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                wsr[5] = (Double.valueOf(etWsr6.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода кг[6]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода кг[6]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                wsr[6] = (Double.valueOf(etWsr7.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода кг[7]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода кг[7]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                wsr[7] = (Double.valueOf(etWsr8.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода кг[8]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода кг[8]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                wsr[8] = (Double.valueOf(etWsr9.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода кг[9]: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода кг[9]", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            valueCalculation.setWsr(wsr);

            valueCalculation.setZp(Double.valueOf(etZp.getText()));
            try {
                valueCalculation.setZpo(Double.valueOf(etZpo.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода основной ЗП: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода основной ЗП", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                valueCalculation.setZpd(Double.valueOf(etZpd.getText()));
            } catch (NumberFormatException e) {
                log.error("Ошибка ввода доп ЗП: " + e);
                JOptionPane.showMessageDialog(null, "Ошибка ввода доп ЗП", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            valueCalculation.setPrim(etPrim.getText());

        } catch (NumberFormatException e) {
            log.error("Ошибка ввода чисел: " + e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка ввода чисел.Проверьте данные", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            return;

        }
        if (!ppdb.updateCalculation(id, valueCalculation)) {
            JOptionPane.showMessageDialog(null, "Ошибка Сохранения", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
        //Можно закрывать. Все хорошо.
        close = true;
    }

    /**
     * Установка форматтеров для полей.
     */
    private void setFormatter() {

        MaskFormatter doubleFormatterWSR, doubleFormatterCSR, doubleFormatterSMTP,
                doubleFormatterSMVP, doubleFormatterTEN,
                doubleFormatterVMS, doubleFormatterVMTP, doubleFormatterZP,
                doubleFormatterPrbp, doubleFormatterUSTO, doubleFormatterKF;

        try {
            //СЫРЬЕ - КГ
            doubleFormatterWSR = new MaskFormatter("***.*****");
            doubleFormatterWSR.setValidCharacters("0123456789 ");
            doubleFormatterWSR.setPlaceholder("");

            //СЫРЬЕ - ЦЕНА
            doubleFormatterCSR = new MaskFormatter("******.**");
            doubleFormatterCSR.setValidCharacters("0123456789 ");
            doubleFormatterCSR.setPlaceholder("");

            //ТРАСП РАСХ , %
            doubleFormatterSMTP = new MaskFormatter("*.**");
            doubleFormatterSMTP.setValidCharacters("0123456789 ");
            doubleFormatterSMTP.setPlaceholder("");

            //ВОзвратные отходы , %
            doubleFormatterSMVP = new MaskFormatter("*.***");
            doubleFormatterSMVP.setValidCharacters("0123456789 ");
            doubleFormatterSMVP.setPlaceholder("");

            //Норматив Топливо
            doubleFormatterTEN = new MaskFormatter("*****.**");
            doubleFormatterTEN.setValidCharacters("0123456789 ");
            doubleFormatterTEN.setPlaceholder("");

            //Расход всмпом мат в рублях
            doubleFormatterVMS = new MaskFormatter("*****.*");
            doubleFormatterVMS.setValidCharacters("0123456789 ");
            doubleFormatterVMS.setPlaceholder("");

            //Трансп расходы( вспомог мат) , %
            doubleFormatterVMTP = new MaskFormatter("*.**");
            doubleFormatterVMTP.setValidCharacters("0123456789 ");
            doubleFormatterVMTP.setPlaceholder("");

            //Зарплата
            doubleFormatterZP = new MaskFormatter("*******.*");
            doubleFormatterZP.setValidCharacters("0123456789 ");
            doubleFormatterZP.setPlaceholder("");

            //Прибыль
            doubleFormatterPrbp = new MaskFormatter("***.**");
            doubleFormatterPrbp.setValidCharacters("0123456789- ");
            doubleFormatterPrbp.setPlaceholder("");

            //Услуги стор орг
            doubleFormatterUSTO = new MaskFormatter("******.**");
            doubleFormatterUSTO.setValidCharacters("0123456789 ");
            doubleFormatterUSTO.setPlaceholder("");

            //Для кф
            doubleFormatterKF = new MaskFormatter("***.**");
            doubleFormatterKF.setValidCharacters("0123456789 ");
            doubleFormatterKF.setPlaceholder("");

            etWsr1.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterWSR));
            etWsr2.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterWSR));
            etWsr3.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterWSR));
            etWsr4.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterWSR));
            etWsr5.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterWSR));
            etWsr6.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterWSR));
            etWsr7.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterWSR));
            etWsr8.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterWSR));
            etWsr9.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterWSR));

/*            etCsr1.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterCSR));
            etCsr2.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterCSR));
            etCsr3.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterCSR));
            etCsr4.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterCSR));
            etCsr5.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterCSR));
            etCsr6.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterCSR));
            etCsr7.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterCSR));
            etCsr8.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterCSR));
            etCsr9.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterCSR));*/

            etSmtp.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterSMTP));
            etSmvp.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterSMVP));
            //etTen.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterTEN));
            //  etVms.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterVMS));
            etVmtp.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterVMTP));
            //etUsto.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterUSTO));
            //etZpo.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterZP));
            //etZpd.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterZP));
            //etTes.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterZP));
            etPrbp.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterPrbp));

            etPrrp.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterPrbp));
            etHzrp.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterPrbp));
            etKmrp.setFormatterFactory(new DefaultFormatterFactory(doubleFormatterPrbp));

        } catch (ParseException ex) {

        }

    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the boolCalc
     */
    public boolean isBoolCalc() {
        return boolCalc;
    }

    /**
     * @param boolCalc the boolCalc to set
     */
    public void setBoolCalc(boolean boolCalc) {
        this.boolCalc = boolCalc;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pPricePanel = new JPanel();
        pPricePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Цена реализации"));
        //pPricePanel.setBackground(Color.BLACK);
        pPricePanel.setPreferredSize(new Dimension(580, 180));
        pButtonPanel = new JPanel(new MigLayout());

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        etFas = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        etNar = new javax.swing.JTextField();
        etSyr2 = new javax.swing.JTextField();
        etNiz = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        etNiz1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        etSyr1 = new javax.swing.JTextField();
        etPol = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        etObr = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        etDat = new javax.swing.JFormattedTextField();
        etRzmn = new javax.swing.JFormattedTextField();
        etRzmk = new javax.swing.JFormattedTextField();
        jLabel46 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        etNsr1 = new javax.swing.JTextField();
        etNsr2 = new javax.swing.JTextField();
        etNsr3 = new javax.swing.JTextField();
        etNsr4 = new javax.swing.JTextField();
        etNsr5 = new javax.swing.JTextField();
        etNsr6 = new javax.swing.JTextField();
        etNsr7 = new javax.swing.JTextField();
        etNsr8 = new javax.swing.JTextField();
        etNsr9 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        etSmts = new javax.swing.JTextField();
        etSmvs = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        etWsr1 = new javax.swing.JFormattedTextField();
        etWsr2 = new javax.swing.JFormattedTextField();
        etWsr3 = new javax.swing.JFormattedTextField();
        etWsr4 = new javax.swing.JFormattedTextField();
        etWsr5 = new javax.swing.JFormattedTextField();
        etWsr6 = new javax.swing.JFormattedTextField();
        etWsr7 = new javax.swing.JFormattedTextField();
        etWsr8 = new javax.swing.JFormattedTextField();
        etWsr9 = new javax.swing.JFormattedTextField();


        etCsr1 = new UCTextField();
        etCsr2 = new UCTextField();
        etCsr3 = new UCTextField();
        etCsr4 = new UCTextField();
        etCsr5 = new UCTextField();
        etCsr6 = new UCTextField();
        etCsr7 = new UCTextField();
        etCsr8 = new UCTextField();
        etCsr9 = new UCTextField();

        etCsr1.setComponentParams(null, Float.class, 4);
        etCsr2.setComponentParams(null, Float.class, 4);
        etCsr3.setComponentParams(null, Float.class, 4);
        etCsr4.setComponentParams(null, Float.class, 4);
        etCsr5.setComponentParams(null, Float.class, 4);
        etCsr6.setComponentParams(null, Float.class, 4);
        etCsr7.setComponentParams(null, Float.class, 4);
        etCsr8.setComponentParams(null, Float.class, 4);
        etCsr9.setComponentParams(null, Float.class, 4);

        chBeforeDenomination = new JCheckBox("Расчет цен до деноминации");
        chBeforeDenomination.setVisible(false);

        chBeforeDenomination.setForeground(Color.blue);

        etSmtp = new javax.swing.JFormattedTextField();
        etSmvp = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        etVmts = new UCTextField();
        etVmts.setComponentParams(null, Float.class, 4);
        jLabel51 = new javax.swing.JLabel();
        //etVms = new javax.swing.JFormattedTextField();
        etVms = new UCTextField();
        etVms.setComponentParams(null, Float.class, 4);
        etVmtp = new javax.swing.JFormattedTextField();

        etTen = new UCTextField();
        etTen.setComponentParams(null, Float.class, 4);

        etTes = new UCTextField();
        etTes.setComponentParams(null, Float.class, 4);
        tvSum = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        etSm = new UCTextField();
        etSm.setComponentParams(null, Float.class, 4);
        etVm = new UCTextField();
        etVm.setComponentParams(null, Float.class, 4);
        etTe = new UCTextField();
        etTe.setComponentParams(null, Float.class, 4);
        etZp = new UCTextField();
        etZp.setComponentParams(null, Float.class, 4);
        etSnp = new javax.swing.JTextField();
        etSn = new UCTextField();
        etSn.setComponentParams(null, Float.class, 4);
        etPrr = new UCTextField();
        etPrr.setComponentParams(null, Float.class, 4);
        etPss = new UCTextField();
        etPss.setComponentParams(null, Float.class, 4);
        etHzr = new UCTextField();
        etHzr.setComponentParams(null, Float.class, 4);
        etKmr = new UCTextField();
        etKmr.setComponentParams(null, Float.class, 4);
        etCc = new UCTextField();
        etCc.setComponentParams(null, Float.class, 4);
        etPrb = new UCTextField();
        etPrb.setComponentParams(null, Float.class, 4);
        etCnvp = new javax.swing.JTextField();

        etCno = new UCTextField();
        etCno.setComponentParams(null, Float.class, 2);

        etCno2 = new UCTextField();
        etCno2.setComponentParams(null, Float.class, 2);

        etCnv = new UCTextField();
        etCnv.setComponentParams(null, Float.class, 2);

        etCnv2 = new UCTextField();
        etCnv2.setComponentParams(null, Float.class, 2);

        etCnr = new UCTextField();
        etCnr.setComponentParams(null, Float.class, 2);

        etCnr2 = new UCTextField();
        etCnr2.setComponentParams(null, Float.class, 2);

        etCreditPercent = new UCTextField();
        etCreditPercent.setComponentParams(null, Float.class, 2);

        etCostWithCredit = new UCTextField();
        etCostWithCredit.setComponentParams(null, Float.class, 2);

        etPrimeCostWithCredit = new UCTextField();
        etPrimeCostWithCredit.setComponentParams(null, Float.class, 4);
        etProfitWithCredit = new UCTextField();
        etProfitWithCredit.setComponentParams(null, Float.class, 4);


        etCnoCredit = new UCTextField();
        etCnoCredit.setComponentParams(null, Float.class, 2);

        etCno2Credit = new UCTextField();
        etCno2Credit.setComponentParams(null, Float.class, 2);

        etCnvCredit = new UCTextField();
        etCnvCredit.setComponentParams(null, Float.class, 2);

        etCnv2Credit = new UCTextField();
        etCnv2Credit.setComponentParams(null, Float.class, 2);

        etCnrCredit = new UCTextField();
        etCnrCredit.setComponentParams(null, Float.class, 2);

        etCnr2Credit = new UCTextField();
        etCnr2Credit.setComponentParams(null, Float.class, 2);


        jLabel37 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();

        etUsto = new UCTextField();
        etUsto.setComponentParams(null, Float.class, 4);
        etZpo = new UCTextField();
        etZpo.setComponentParams(null, Float.class, 4);

        etZpo.setPreferredSize(new Dimension(100, 22));
        etZpd = new UCTextField();
        etZpd.setComponentParams(null, Float.class, 4);
        tvCno = new javax.swing.JLabel();
        tvCno2 = new javax.swing.JLabel();
        tvCnr = new javax.swing.JLabel();
        tvCnr2 = new javax.swing.JLabel();
        etPrbp = new javax.swing.JFormattedTextField();
        etPrrp = new javax.swing.JFormattedTextField();
        etHzrp = new javax.swing.JFormattedTextField();
        etKmrp = new javax.swing.JFormattedTextField();
        btnCalculationProfitability = new javax.swing.JButton();
        btnRememberToPrint = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();

        jRadioButtonNew = new javax.swing.JRadioButton();
        jRadioButtonOld = new javax.swing.JRadioButton();
        jRadioButtonUser = new javax.swing.JRadioButton();
        jRadioButtonHalf = new javax.swing.JRadioButton();

        lblCredit = new javax.swing.JLabel();
        lblCreditPercentLabel = new JLabel();
        lblPrimeCostWithCredit = new JLabel();
        lblProfitWithCredit = new JLabel();

        lblCredit.setForeground(Color.BLUE);
        lblCreditPercentLabel.setForeground(Color.BLUE);
        lblPrimeCostWithCredit.setForeground(Color.BLUE);
        lblProfitWithCredit.setForeground(Color.BLUE);

        jLabel44 = new javax.swing.JLabel();
        jComboBoxPrice = new javax.swing.JComboBox();
        jLabel45 = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        etPrim = new javax.swing.JTextField();
        btnSaveToSogl = new javax.swing.JButton();
        btnPrintSogl = new javax.swing.JButton();
        btnPrintDet = new javax.swing.JButton();
        btnPrintForDet = new javax.swing.JButton();
        btnDetReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setAutoscrolls(true);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setAutoscrolls(true);
        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Изделие"));

        jLabel1.setText("Модель");

        etFas.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        etFas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etFasKeyPressed(evt);
            }
        });

        jLabel2.setText("Артикул");

        etNar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNarKeyPressed(evt);
            }
        });

        etSyr2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSyr2KeyPressed(evt);
            }
        });

        etNiz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNizKeyPressed(evt);
            }
        });

        jLabel3.setText("/");

        etNiz1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etNiz1ActionPerformed(evt);
            }
        });
        etNiz1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNiz1KeyPressed(evt);
            }
        });

        jLabel4.setText("Полотно");

        etSyr1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSyr1KeyPressed(evt);
            }
        });

        etPol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etPolKeyPressed(evt);
            }
        });

        jLabel6.setText("Оборудование");

        etObr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etObrKeyPressed(evt);
            }
        });

        jLabel7.setText("Размер");

        jLabel8.setText("/");

        jLabel9.setText("Дата");

        etDat.setMaximumSize(new java.awt.Dimension(10, 27));
        etDat.setNextFocusableComponent(etNsr1);
        etDat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etDatKeyPressed(evt);
            }
        });

        etRzmn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etRzmnKeyPressed(evt);
            }
        });

        etRzmk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etRzmkKeyPressed(evt);
            }
        });

        jLabel46.setText("Ед.Измер");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(etObr)
                                .addGap(70, 70, 70)
                                .addComponent(jLabel6)
                                .addGap(448, 448, 448))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel7)
                                                        .addComponent(jLabel9))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(etRzmn, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(15, 15, 15)
                                                                .addComponent(jLabel8)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(etRzmk, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(etDat, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(etPol))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel1)
                                                                        .addComponent(jLabel2)
                                                                        .addComponent(jLabel46))
                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                .addGap(41, 41, 41)
                                                                                .addComponent(etSyr1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(3, 3, 3)
                                                                                .addComponent(etSyr2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(etFas, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                                                                        .addComponent(etNar)))))
                                                        .addComponent(etNiz, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(etNiz1))))
                                .addGap(616, 616, 616))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etFas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(etNar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addComponent(etNiz, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etNiz1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etPol, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etSyr1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etSyr2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel46))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etObr, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)
                                        .addComponent(etRzmn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etRzmk, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addContainerGap())
                                        .addComponent(etDat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Сырье"));

        jLabel30.setText("Наименование");

        jLabel31.setText("Кг");

        jLabel32.setText("Цена");

        etNsr1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNsr1KeyPressed(evt);
            }
        });

        etNsr2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNsr2KeyPressed(evt);
            }
        });

        etNsr3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNsr3KeyPressed(evt);
            }
        });

        etNsr4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNsr4KeyPressed(evt);
            }
        });

        etNsr5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNsr5KeyPressed(evt);
            }
        });

        etNsr6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNsr6KeyPressed(evt);
            }
        });

        etNsr7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNsr7KeyPressed(evt);
            }
        });

        etNsr8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNsr8KeyPressed(evt);
            }
        });

        etNsr9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etNsr9KeyPressed(evt);
            }
        });

        jLabel33.setText("Транспортные расходы");

        jLabel34.setText("Возвратные отходы");

        etSmts.setEditable(false);
        etSmts.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSmtsKeyPressed(evt);
            }
        });

        etSmvs.setEditable(false);
        etSmvs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSmvsKeyPressed(evt);
            }
        });

        jLabel35.setText("%");

        jLabel36.setText("%");

        etWsr1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etWsr1KeyPressed(evt);
            }
        });

        etWsr2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etWsr2KeyPressed(evt);
            }
        });

        etWsr3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etWsr3ActionPerformed(evt);
            }
        });
        etWsr3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etWsr3KeyPressed(evt);
            }
        });

        etWsr4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etWsr4KeyPressed(evt);
            }
        });

        etWsr5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etWsr5KeyPressed(evt);
            }
        });

        etWsr6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etWsr6KeyPressed(evt);
            }
        });

        etWsr7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etWsr7KeyPressed(evt);
            }
        });

        etWsr8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etWsr8KeyPressed(evt);
            }
        });

        etWsr9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etWsr9KeyPressed(evt);
            }
        });

        etCsr1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCsr1KeyPressed(evt);
            }
        });

        etCsr2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCsr2KeyPressed(evt);
            }
        });

        etCsr3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCsr3KeyPressed(evt);
            }
        });

        etCsr4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCsr4KeyPressed(evt);
            }
        });

        etCsr5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCsr5KeyPressed(evt);
            }
        });

        etCsr6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCsr6KeyPressed(evt);
            }
        });

        etCsr7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCsr7KeyPressed(evt);
            }
        });

        etCsr8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCsr8KeyPressed(evt);
            }
        });

        etCsr9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCsr9KeyPressed(evt);
            }
        });

        etSmtp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSmtpKeyPressed(evt);
            }
        });

        etSmvp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSmvpKeyPressed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Топливо"));

        jLabel47.setText("Норматив");

        jLabel48.setText("В рублях");

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Расход вспомогательных материалов"));

        jLabel49.setText("В рублях");

        jLabel50.setText("Транспортные расходы ");

        etVmts.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etVmtsKeyPressed(evt);
            }
        });

        jLabel51.setText("%");

        etVms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etVmsActionPerformed(evt);
            }
        });
        etVms.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etVmsKeyPressed(evt);
            }
        });

        etVmtp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etVmtpKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel49)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(etVms, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel50)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(etVmtp, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(26, 26, 26)
                                .addComponent(jLabel51)
                                .addGap(18, 18, 18)
                                .addComponent(etVmts, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel49)
                                        .addComponent(etVms, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel50)
                                        .addComponent(etVmts, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel51)
                                        .addComponent(etVmtp, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        etTen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etTenKeyPressed(evt);
            }
        });

        etTes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etTesActionPerformed(evt);
            }
        });
        etTes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etTesKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel47)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etTen, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(etTes, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel47)
                                        .addComponent(jLabel48)
                                        .addComponent(etTen, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etTes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(26, Short.MAX_VALUE))
        );

        tvSum.setText("jLabel5");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(jLabel30)
                                                .addGap(94, 94, 94)
                                                .addComponent(jLabel31)
                                                .addGap(143, 143, 143)
                                                .addComponent(jLabel32))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(etNsr9)
                                                        .addComponent(etNsr8)
                                                        .addComponent(etNsr7)
                                                        .addComponent(etNsr2, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(etNsr3, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(etNsr4, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(etNsr5, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(etNsr6, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(etNsr1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(etWsr1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(etWsr7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(etWsr6)
                                                                .addComponent(etWsr5)
                                                                .addComponent(etWsr4, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                                                        .addComponent(etWsr2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(etWsr9, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(etWsr3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(etWsr8, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel34)
                                                        .addComponent(jLabel33))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(etSmvp, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                                                        .addComponent(etSmtp))
                                                                .addGap(4, 4, 4)
                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                                .addComponent(jLabel36)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(etSmvs, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(etCsr4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                                                .addGap(27, 27, 27)
                                                                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                        .addComponent(etCsr1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(etCsr2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                                                                .addGap(28, 28, 28)
                                                                                                .addComponent(etCsr3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                                                                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(etSmts, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                .addComponent(etCsr5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(etCsr6, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(etCsr7, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(etCsr8, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(etCsr9, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                        .addComponent(tvSum, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel30)
                                        .addComponent(jLabel31)
                                        .addComponent(jLabel32))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNsr1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etWsr1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etCsr1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNsr2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etWsr2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etCsr2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNsr3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etWsr3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etCsr3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNsr4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etWsr4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etCsr4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNsr5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etWsr5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etCsr5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNsr6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etWsr6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etCsr6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNsr7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etWsr7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etCsr7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etNsr8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etWsr8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(etCsr8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(etNsr9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(etWsr9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(etCsr9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tvSum)
                                .addGap(3, 3, 3)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(etSmtp, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel35))
                                        .addComponent(jLabel33)
                                        .addComponent(etSmts, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(etSmvp, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel34))
                                        .addComponent(etSmvs, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel36))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Расчет"));

        jLabel10.setText("Основное сырье и материалы");

        jLabel11.setText("Вспомогательные материалы");

        jLabel12.setText("Топливо и энергия");

        jLabel13.setText("Услуги сторонних организаций");

        jLabel14.setText("Основная зарплата");

        jLabel15.setText("Дополнительная зарплата");

        jLabel16.setText("Итого заработная плата");

        jLabel17.setText("Отчисления на соц.нужды");

        jLabel18.setText("Общепроизводственные расходы");

        jLabel19.setText("Производственная себестоимость");

        jLabel20.setText("Общехозяйственные расходы");

        jLabel21.setText("Коммерческие расходы");

        jLabel22.setText("Прибыль");

        lblCredit.setText("% по кредитам и займам");
        lblCreditPercentLabel.setText("%");

        lblPrimeCostWithCredit.setText("Себестоимость с учетом % по кредитам");
        lblProfitWithCredit.setText("Прибыль с учетом % по кредитам");
        jLabel25.setText("Полная себестоимость");


        jLabel23.setText("Цена реализации без НДС 1 сорта");

        jLabel24.setText("Цена реализации без НДС 2 сорта");

        jLabel26.setText("НДС 1 сорта");

        jLabel27.setText("НДС 2 сорта");

        jLabel28.setText("Цена реализации с НДС 1 сорта");

        jLabel29.setText("Цена реализации с НДС 2 сорта");

        etSm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSmKeyPressed(evt);
            }
        });

        etVm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etVmKeyPressed(evt);
            }
        });

        etTe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etTeKeyPressed(evt);
            }
        });

        etZp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etZpKeyPressed(evt);
            }
        });

        etSnp.setEditable(false);
        etSnp.setText("34");
        etSnp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etSnpActionPerformed(evt);
            }
        });
        etSnp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSnpKeyPressed(evt);
            }
        });

        etSn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etSnKeyPressed(evt);
            }
        });

        etPrr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etPrrKeyPressed(evt);
            }
        });

        etPss.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etPssKeyPressed(evt);
            }
        });

        etHzr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etHzrKeyPressed(evt);
            }
        });

        etKmr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etKmrKeyPressed(evt);
            }
        });

        etCc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCcKeyPressed(evt);
            }
        });

        etPrb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etPrbKeyPressed(evt);
            }
        });

        etCreditPercent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etPrb.requestFocusInWindow();
                    etPrb.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    etCostWithCredit.requestFocusInWindow();
                    etCostWithCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    //setBool(false);
                    etCostWithCredit.setCaretPosition(0);
                    etCostWithCredit.requestFocusInWindow();
                    calculation(getId());
                }
            }
        });

        etCostWithCredit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etCreditPercent.requestFocusInWindow();
                    etCreditPercent.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    etPrimeCostWithCredit.requestFocusInWindow();
                    etPrimeCostWithCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculation(getId());
                    etPrimeCostWithCredit.requestFocusInWindow();
                    etPrimeCostWithCredit.setCaretPosition(0);
                }
            }
        });

        etPrimeCostWithCredit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etCostWithCredit.requestFocusInWindow();
                    etCostWithCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    etProfitWithCredit.requestFocusInWindow();
                    etProfitWithCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculation(getId());
                    etProfitWithCredit.requestFocusInWindow();
                    etProfitWithCredit.setCaretPosition(0);
                }
            }
        });

        etProfitWithCredit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etPrimeCostWithCredit.requestFocusInWindow();
                    etPrimeCostWithCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    etCno.requestFocusInWindow();
                    etCno.setCaretPosition(0);
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculation(getId());
                    etCno.requestFocusInWindow();
                    etCno.setCaretPosition(0);
                }
            }
        });

        etCnoCredit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etCnr2.requestFocusInWindow();
                    etCnr2.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    etCno2Credit.requestFocusInWindow();
                    etCno2Credit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculation(getId());
                    etCno2Credit.requestFocusInWindow();
                    etCno2Credit.setCaretPosition(0);
                }
            }
        });

        etCno2Credit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etCnoCredit.requestFocusInWindow();
                    etCnoCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    etCnvCredit.requestFocusInWindow();
                    etCnvCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculation(getId());
                    etCnvCredit.requestFocusInWindow();
                    etCnvCredit.setCaretPosition(0);
                }
            }
        });

        etCnvCredit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etCno2Credit.requestFocusInWindow();
                    etCno2Credit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    etCnv2Credit.requestFocusInWindow();
                    etCnv2Credit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculation(getId());
                    etCnv2Credit.requestFocusInWindow();
                    etCnv2Credit.setCaretPosition(0);
                }
            }
        });

        etCnv2Credit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etCnvCredit.requestFocusInWindow();
                    etCnvCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    etCnrCredit.requestFocusInWindow();
                    etCnrCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculation(getId());
                    etCnrCredit.requestFocusInWindow();
                    etCnrCredit.setCaretPosition(0);
                }
            }
        });

        etCnrCredit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etCnv2Credit.requestFocusInWindow();
                    etCnv2Credit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    etCnr2Credit.requestFocusInWindow();
                    etCnr2Credit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculation(getId());
                    etCnr2Credit.requestFocusInWindow();
                    etCnr2Credit.setCaretPosition(0);
                }
            }
        });

        etCnr2Credit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 38) {
                    etCnrCredit.requestFocusInWindow();
                    etCnrCredit.setCaretPosition(0);
                }

                if (evt.getKeyCode() == 40) {
                    btnCalculationProfitability.requestFocusInWindow();
                }

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    calculation(getId());
                    btnCalculationProfitability.requestFocusInWindow();
                }
            }
        });

        etCno2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCno2KeyPressed(evt);
            }
        });

        //etCnvp.setEditable(false);
        etCnvp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCnvpKeyPressed(evt);
            }
        });

        etCnv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCnvKeyPressed(evt);
            }
        });

        etCnv2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCnv2KeyPressed(evt);
            }
        });

        etCnr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCnrKeyPressed(evt);
            }
        });

        etCnr2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCnr2KeyPressed(evt);
            }
        });

        jLabel37.setText("%");

        jLabel39.setText("%");

        jLabel38.setText("%");

        jLabel42.setText("%");


        etUsto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etUstoKeyPressed(evt);
            }
        });

        etZpo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etZpoActionPerformed(evt);
            }
        });
        etZpo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etZpoKeyPressed(evt);
            }
        });

        etZpd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etZpdKeyPressed(evt);
            }
        });

        etCno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                etCnoFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                etCnoFocusLost(evt);
            }
        });
        etCno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                etCnoActionPerformed(evt);
            }
        });
        etCno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etCnoKeyPressed(evt);
            }
        });

        tvCno.setText("jLabel5");

        tvCno2.setText("jLabel52");

        tvCnr.setText("jLabel5");

        tvCnr2.setText("jLabel52");

        etPrbp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etPrbpKeyPressed(evt);
            }
        });

        etPrrp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etPrrpKeyPressed(evt);
            }
        });

        etHzrp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etHzrpKeyPressed(evt);
            }
        });

        etKmrp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                etKmrpKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel16)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel14)
                                        .addComponent(jLabel15)
                                        .addComponent(jLabel17)

                                        .addComponent(jLabel19)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel18)
                                                        .addComponent(jLabel21)
                                                        .addComponent(jLabel12)
                                                        .addComponent(jLabel20)
                                                        .addComponent(jLabel22)
                                                        .addComponent(lblCredit)
                                                        .addComponent(lblPrimeCostWithCredit)
                                                        .addComponent(lblProfitWithCredit)
                                                        .addComponent(jLabel25))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addComponent(etPrrp, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel42))
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(4, 4, 4)
                                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(etHzrp, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                                                                        .addComponent(etKmrp))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                                                                        .addComponent(jLabel38)))
                                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                        .addComponent(etSnp, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(16, 16, 16)
                                                                        .addComponent(jLabel37))
                                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                        .addComponent(etPrbp, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(jLabel39))
                                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                        .addComponent(etCreditPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(lblCreditPercentLabel))
                                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                        .addComponent(etPrbp, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(lblCreditPercentLabel))
                                                        ))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(etPrb)
                                        .addComponent(etCostWithCredit)
                                        .addComponent(etPrimeCostWithCredit)
                                        .addComponent(etProfitWithCredit)
                                        .addComponent(etCc)
                                        .addComponent(etKmr)
                                        .addComponent(etHzr)
                                        .addComponent(etPss)
                                        .addComponent(etPrr)
                                        .addComponent(etSn)
                                        .addComponent(etZp)
                                        .addComponent(etTe)
                                        .addComponent(etVm)
                                        .addComponent(etSm)
                                        .addComponent(etUsto, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(etZpo)
                                        .addComponent(etZpd)
                                )
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(etSm, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(etVm, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etTe, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(etUsto, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel14)
                                        .addComponent(etZpo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(etZpd, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etZp, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(etSnp, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel17)
                                        .addComponent(etSn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel37))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel18)
                                        .addComponent(etPrr, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel42)
                                        .addComponent(etPrrp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel19)
                                        .addComponent(etPss, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel20)
                                                        .addComponent(etHzr, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(etHzrp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(etKmr, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel21)
                                                        .addComponent(jLabel38)
                                                        .addComponent(etKmrp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(etCc, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel25))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(etPrb, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel22)
                                                        .addComponent(jLabel39)
                                                        .addComponent(etPrbp, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(etCostWithCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblCredit)
                                                        .addComponent(lblCreditPercentLabel)
                                                        .addComponent(etCreditPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblPrimeCostWithCredit)
                                                        .addComponent(etPrimeCostWithCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblProfitWithCredit)
                                                        .addComponent(etProfitWithCredit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                                        ))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        )
        );

        btnCalculationProfitability.setText("Расчет рентабельности");
        btnCalculationProfitability.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculationProfitabilityActionPerformed(evt);
            }
        });
        btnCalculationProfitability.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCalculationProfitabilityKeyPressed(evt);
            }
        });

        btnRememberToPrint.setText("Запомнить для печати");
        btnRememberToPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRememberToPrintActionPerformed(evt);
            }
        });
        btnRememberToPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnRememberToPrintKeyPressed(evt);
            }
        });

        btnPrint.setText("Печать");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        btnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPrintKeyPressed(evt);
            }
        });

        btnCancel.setText("Отменить");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        btnCancel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCancelKeyPressed(evt);
            }
        });

        btnSave.setText("Сохранить");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        btnSave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSaveKeyPressed(evt);
            }
        });

        jLabel43.setText("Коэффициенты");

        jRadioButtonNew.setText("Новые");
        jRadioButtonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonNewActionPerformed(evt);
            }
        });

        jRadioButtonOld.setText("Старые");
        jRadioButtonOld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonOldActionPerformed(evt);
            }
        });

        jRadioButtonUser.setText("Пользовательские");
        jRadioButtonUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonUserActionPerformed(evt);
            }
        });

        jRadioButtonHalf.setText("1/2 Затрат");
        //jRadioButtonHalf.setForeground(Color.BLUE);
        jRadioButtonHalf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonHalfActionPerformed(evt);
            }
        });

        jComboBoxPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPriceActionPerformed(evt);
            }
        });

        jLabel45.setText("Выбор прейскуранта");

        btnClose.setText("Закрыть");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        btnClose.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCloseKeyPressed(evt);
            }
        });

        btnNew.setText("Новые % и кф");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSaveToSogl.setText("Запомнить для соглас");
        btnSaveToSogl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveToSoglActionPerformed(evt);
            }
        });

        btnPrintSogl.setText("Печать согласования");
        btnPrintSogl.setPreferredSize(new java.awt.Dimension(62, 29));
        btnPrintSogl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintSoglActionPerformed(evt);
            }
        });

        btnPrintDet.setText("Печать Детский");
        btnPrintDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintDetActionPerformed(evt);
            }
        });

        btnPrintForDet.setText("Запомнить детский");
        btnPrintForDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintForDetActionPerformed(evt);
            }
        });

        btnDetReset.setText("Сбросить детский");
        btnDetReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jRadioButtonNew)
                                                                        .addComponent(jRadioButtonOld)
                                                                        .addComponent(jRadioButtonUser)
                                                                        .addComponent(jRadioButtonHalf)
                                                                        .addComponent(chBeforeDenomination)
                                                                        .addComponent(etPrim, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(15, 15, 15)
                                                                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jComboBoxPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(8, 8, 8))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
                                                                //.addGap(180, 180, 180)
                                                                .addComponent(pPricePanel, 50, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
                                                                //.addGap(180, 180, 180)
                                                                .addComponent(pButtonPanel, 50, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                )))
                                .addContainerGap())
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 682, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 626, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

/*                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(pPricePanel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)*/

                                                .addComponent(pPricePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(11, 11, 11)
                                                                .addComponent(jLabel43)
                                                                .addGap(22, 22, 22)
                                                                .addComponent(jRadioButtonNew)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jRadioButtonOld)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jRadioButtonUser)

                                                                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jRadioButtonHalf)
                                                                .addComponent(chBeforeDenomination)
                                                                .addGap(49, 49, 49)
                                                                .addComponent(jLabel45)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboBoxPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(etPrim, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(10, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 900, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
        );

        pPricePanel.setLayout(new MigLayout());
        JLabel lblCreditSign = new JLabel("С/У Кредита");
        lblCreditSign.setForeground(Color.BLUE);

        /*
        pPricePanel.add(new JLabel(), "height 20:20, width 230:20:230");
        pPricePanel.add(new JLabel(), "height 20:20, width 80:20:80");
        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(new JLabel("Б/У Кредита"), "height 20:20, width 80:20:80");
        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(lblCreditSign, "height 20:20, width 80:20:80, wrap");
        */

        pPricePanel.add(jLabel23, "height 20:20, width 260:20:260");
        pPricePanel.add(tvCno, "height 20:20, width 80:20:80");
        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCno, "height 20:20, width 80:20:80, wrap");
/*      pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCnoCredit, "height 20:20, width 80:20:80, wrap");*/

        pPricePanel.add(jLabel24, "height 20:20, width 260:20:260");
        pPricePanel.add(tvCno2, "height 20:20, width 80:20:80");
        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCno2, "height 20:20, width 80:20:80, wrap");
/*        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCno2Credit, "height 20:20, width 80:20:80, wrap");*/

        pPricePanel.add(jLabel26, "height 20:20, width 260:20:260");
        pPricePanel.add(etCnvp, "height 20:20, width 80:20:80");
        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCnv, "height 20:20, width 80:20:80, wrap");
/*        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCnvCredit, "height 20:20, width 80:20:80, wrap");*/

        pPricePanel.add(jLabel27, "height 20:20, width 260:20:260");
        pPricePanel.add(new JLabel(), "height 20:20, width 80:20:80");
        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCnv2, "height 20:20, width 80:20:80, wrap");
/*        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCnv2Credit, "height 20:20, width 80:20:80, wrap");*/

        pPricePanel.add(jLabel28, "height 20:20, width 260:20:260");
        pPricePanel.add(tvCnr, "height 20:20, width 80:20:80");
        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCnr, "height 20:20, width 80:20:80, wrap");
/*        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCnrCredit, "height 20:20, width 80:20:80, wrap");*/

        pPricePanel.add(jLabel29, "height 20:20, width 260:20:260");
        pPricePanel.add(tvCnr2, "height 20:20, width 80:20:80");
        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCnr2, "height 20:20, width 80:20:80, wrap");
/*        pPricePanel.add(new JLabel(), "height 20:20, width 20:20:20");
        pPricePanel.add(etCnr2Credit, "height 20:20, width 80:20:80, wrap");*/


        pButtonPanel.add(btnCalculationProfitability, "width 200:20:200");
        pButtonPanel.add(btnNew, "width 180:20:180");
        pButtonPanel.add(btnCancel, "width 100:20:100");
        pButtonPanel.add(btnPrintDet, "width 120:20:120, wrap");

        pButtonPanel.add(btnRememberToPrint, "width 200:20:200");
        pButtonPanel.add(btnSaveToSogl, "width 180:20:180");
        pButtonPanel.add(btnSave, "width 100:20:100");
        pButtonPanel.add(btnPrintForDet, "width 120:20:120, wrap");

        pButtonPanel.add(btnPrint, "width 200:20:200");
        pButtonPanel.add(btnPrintSogl, "width 180:20:180");
        pButtonPanel.add(btnClose, "width 100:20:100");
        pButtonPanel.add(btnDetReset, "width 120:20:120, wrap");


        //pack();
        setSize(new Dimension(1220, 880));
        setLocationRelativeTo(null);
        // etVms.setComponentParams(jLabel49, Float.class, -1);
        //etVms.addKeyListener(new FloatListener(etVms));
    }// </editor-fold>//GEN-END:initComponents

    private void etSnpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etSnpActionPerformed
    }//GEN-LAST:event_etSnpActionPerformed

    /**
     * Коэф  новые. Неизм.
     *
     * @param evt
     */
    private void jRadioButtonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonNewActionPerformed
        setStatus(0);
        //etCnvp.setEnabled(false);
        etSmvp.setEnabled(false);
        etVmtp.setEnabled(false);
        etSmtp.setEnabled(false);
        etPrrp.setEnabled(false);
        etHzrp.setEnabled(false);
        etKmrp.setEnabled(false);
        calculation(getId());
    }//GEN-LAST:event_jRadioButtonNewActionPerformed

    /**
     * Коэф  старые. Неизм.
     *
     * @param evt
     */
    private void jRadioButtonOldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonOldActionPerformed
        setStatus(1);
        //etCnvp.setEnabled(false);
        etSmvp.setEnabled(false);
        etVmtp.setEnabled(false);
        etSmtp.setEnabled(false);
        etPrrp.setEnabled(false);
        etHzrp.setEnabled(false);
        etKmrp.setEnabled(false);
        calculation(getId());
    }//GEN-LAST:event_jRadioButtonOldActionPerformed

    /**
     * Коэф  пользовательски активны для изменения
     *
     * @param evt
     */
    private void jRadioButtonUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonUserActionPerformed
        setStatus(2);
        //etCnvp.setEnabled(false);
        etSmvp.setEnabled(true);
        etVmtp.setEnabled(true);
        etSmtp.setEnabled(true);
        etPrrp.setEnabled(true);
        etHzrp.setEnabled(true);
        etKmrp.setEnabled(true);
    }//GEN-LAST:event_jRadioButtonUserActionPerformed

    private void jRadioButtonHalfActionPerformed(ActionEvent evt) {
        setStatus(3);
        //etCnvp.setEnabled(false);
        etSmvp.setEnabled(true);
        etVmtp.setEnabled(true);
        etSmtp.setEnabled(true);
        etPrrp.setEnabled(true);
        etHzrp.setEnabled(true);
        etKmrp.setEnabled(true);
        calculation(getId());
    }

    /**
     * Кнопка расчет.Проверка на валидность.
     *
     * @param evt
     */
    private void btnCalculationProfitabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculationProfitabilityActionPerformed
        if (!calculation(getId())) {
            jLabel44.setText("Некорректные данные");
        } else {
            jLabel44.setText("");
        }
    }//GEN-LAST:event_btnCalculationProfitabilityActionPerformed

    private void etCnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etCnoActionPerformed
    }//GEN-LAST:event_etCnoActionPerformed

    private void etCnoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_etCnoFocusLost


    }//GEN-LAST:event_etCnoFocusLost

    //Сохранение
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        update(getId(), getValueCalc());
        self.refreshTable(self.getPtk());
    }//GEN-LAST:event_btnSaveActionPerformed

    //Отмена.Удаление дубликата.
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if (copy) {
            ppdb.deleteCalc(getId());
            close = true;
            self.refreshTable(self.getPtk());
            dispose();

            return;
        }

        close = true;
        loadFieldData(getId());
    }//GEN-LAST:event_btnCancelActionPerformed

    //Диалог Печать.
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        PrintForm printForm = new PrintForm(self, false, getId());


    }//GEN-LAST:event_btnPrintActionPerformed

    //Множ Печать
    private void btnRememberToPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRememberToPrintActionPerformed
        if (SaveForPrint.listId.size() < 3) {
            SaveForPrint.listId.add(getId());
        }
        if (SaveForPrint.listId.size() == 3) {
            new PrintForm(self, false, SaveForPrint.listId);
            SaveForPrint.listId = new LinkedList<Integer>();
        }
    }//GEN-LAST:event_btnRememberToPrintActionPerformed

    //Выбор прейскуранта
    private void jComboBoxPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPriceActionPerformed
        jComboBoxPrice.setEnabled(true);
        try {
            if (!ppdb.getPrice(getItTypeCalculation()).isEmpty()) {

                setNumberPrice(((ComboboxPrice) jComboBoxPrice.getModel()).getId());

            }
        } catch (Exception e) {
            log.error("Ошибка при входе в систему: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка получения данных", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_jComboBoxPriceActionPerformed

    //Закрыть
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    /**
     * Обработка нажатий вниз, вверх и клавиши энтер .Установка фокуса на следующий и предыдущий.
     */

    private void etNarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNarKeyPressed
        if (evt.getKeyCode() == 38) {
            etFas.requestFocusInWindow();
            etFas.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNiz.requestFocusInWindow();
            etNiz.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNarKeyPressed

    private void etFasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etFasKeyPressed

        if (evt.getKeyCode() == 38) {
            btnClose.requestFocusInWindow();
        }
        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNar.requestFocusInWindow();
            etNar.setCaretPosition(0);
        }
    }//GEN-LAST:event_etFasKeyPressed

    private void etNizKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNizKeyPressed
        if (evt.getKeyCode() == 38) {
            etNar.requestFocusInWindow();
            etNar.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNiz1.requestFocusInWindow();
            etNiz1.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNizKeyPressed

    private void etPolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etPolKeyPressed
        if (evt.getKeyCode() == 38) {
            etNiz.requestFocusInWindow();
            etNiz.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etSyr1.requestFocusInWindow();
            etSyr1.setCaretPosition(0);
        }
    }//GEN-LAST:event_etPolKeyPressed

    private void etSyr1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSyr1KeyPressed
        if (evt.getKeyCode() == 38) {
            etPol.requestFocusInWindow();
            etPol.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etSyr2.requestFocusInWindow();
            etSyr2.setCaretPosition(0);
        }
    }//GEN-LAST:event_etSyr1KeyPressed

    private void etSyr2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSyr2KeyPressed
        if (evt.getKeyCode() == 38) {
            etSyr1.requestFocusInWindow();
            etSyr1.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etObr.requestFocusInWindow();
            etObr.setCaretPosition(0);
        }
    }//GEN-LAST:event_etSyr2KeyPressed

    private void etObrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etObrKeyPressed
        if (evt.getKeyCode() == 38) {
            etSyr2.requestFocusInWindow();
            etSyr2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etRzmn.requestFocusInWindow();
            etRzmn.setCaretPosition(0);
        }
    }//GEN-LAST:event_etObrKeyPressed

    private void etRzmnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etRzmnKeyPressed
        if (evt.getKeyCode() == 38) {
            etObr.requestFocusInWindow();
            etObr.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etRzmk.requestFocusInWindow();
            etRzmk.setCaretPosition(0);
        }
    }//GEN-LAST:event_etRzmnKeyPressed

    private void etRzmkKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etRzmkKeyPressed
        if (evt.getKeyCode() == 38) {
            etRzmn.requestFocusInWindow();
            etRzmn.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etDat.requestFocusInWindow();
            etDat.setCaretPosition(0);
        }
    }//GEN-LAST:event_etRzmkKeyPressed

    private void etDatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etDatKeyPressed
        if (evt.getKeyCode() == 38) {
            etRzmk.requestFocusInWindow();
            etRzmk.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNsr1.requestFocusInWindow();
            etNsr1.setCaretPosition(0);
        }
    }//GEN-LAST:event_etDatKeyPressed

    private void etSnpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSnpKeyPressed
        if (evt.getKeyCode() == 38) {
            etVmts.requestFocusInWindow();
            etVmts.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etPrrp.requestFocusInWindow();
            etPrrp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etPrrp.requestFocusInWindow();
            etPrrp.setCaretPosition(0);
        }
    }//GEN-LAST:event_etSnpKeyPressed

    private void etCnvpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCnvpKeyPressed
        if (evt.getKeyCode() == 38) {
            etPrbp.requestFocusInWindow();
            etPrbp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etSm.requestFocusInWindow();
            etSm.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etSm.requestFocusInWindow();
            etSm.setCaretPosition(0);
            calculation(getId());
        }
    }//GEN-LAST:event_etCnvpKeyPressed

    private void etSmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSmKeyPressed
        if (evt.getKeyCode() == 38) {
            etCnvp.requestFocusInWindow();
            etCnvp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etVm.requestFocusInWindow();
            etVm.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etVm.requestFocusInWindow();
            etVm.setCaretPosition(0);
        }
    }//GEN-LAST:event_etSmKeyPressed

    private void etVmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etVmKeyPressed
        if (evt.getKeyCode() == 38) {
            etSm.requestFocusInWindow();
            etSm.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etTe.requestFocusInWindow();
            etTe.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etTe.requestFocusInWindow();
            etTe.setCaretPosition(0);
        }
    }//GEN-LAST:event_etVmKeyPressed

    private void etTeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etTeKeyPressed
        if (evt.getKeyCode() == 38) {
            etVm.requestFocusInWindow();
            etVm.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etUsto.requestFocusInWindow();
            etUsto.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etUsto.requestFocusInWindow();
            etUsto.setCaretPosition(0);
        }
    }//GEN-LAST:event_etTeKeyPressed

    /**
     * Обработка нажатий клавиш вверх и вниз для перемещения по ячейкам для цифровых полей.
     * По клавише ENTER мы заполняем ячейку по пожеланием цен
     * <p>
     * P.S. ЛУЧШЕ НЕ ТРОГАТЬ ЭТУ КАРЯГУ.
     */

    private void etUstoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etUstoKeyPressed
        if (evt.getKeyCode() == 38) {
            etTe.requestFocusInWindow();
            etTe.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etZpo.requestFocusInWindow();
            etZpo.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setTextUSTO(etUsto, Double.valueOf(etUsto.getText().replaceAll(" ", "")));

            etZpo.setCaretPosition(0);
            etZpo.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etUsto.getValue().toString().indexOf(".");
            //etUsto.setCaretPosition(temp + 1);
            calculation(getId());
        }


    }//GEN-LAST:event_etUstoKeyPressed

    private void etZpoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etZpoKeyPressed
        if (evt.getKeyCode() == 38) {
            etUsto.requestFocusInWindow();
            etUsto.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etZpd.requestFocusInWindow();
            etZpd.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etZpd.setCaretPosition(0);
            etZpd.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }
        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etZpo.getValue().toString().indexOf(".");
            //etZpo.setCaretPosition(temp + 1);
            calculation(getId());
        }
    }//GEN-LAST:event_etZpoKeyPressed

    private void etZpdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etZpdKeyPressed
        if (evt.getKeyCode() == 38) {
            etZpo.requestFocusInWindow();
            etZpd.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etZp.requestFocusInWindow();
            etZp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            // calculation(getId());

            //setTextZP(etZpd, Double.valueOf(etZpd.getText().replaceAll(" ", "")));
            etZp.requestFocusInWindow();
            etZp.setCaretPosition(0);
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            //int temp = etZpd.getValue().toString().indexOf(".");
            //etZpd.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etZpdKeyPressed

    private void etZpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etZpKeyPressed
        if (evt.getKeyCode() == 38) {
            etZpd.requestFocusInWindow();
            etZpd.setCaretPosition(0);

        }

        if (evt.getKeyCode() == 40) {
            etSn.requestFocusInWindow();
            etSn.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etSn.requestFocusInWindow();
            etSn.setCaretPosition(0);
        }
    }//GEN-LAST:event_etZpKeyPressed

    private void etSnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSnKeyPressed
        if (evt.getKeyCode() == 38) {
            etZp.requestFocusInWindow();
            etZp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etPrr.requestFocusInWindow();
            etPrr.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etPrr.requestFocusInWindow();
            etPrr.setCaretPosition(0);
        }
    }//GEN-LAST:event_etSnKeyPressed

    private void etPrrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etPrrKeyPressed
        if (evt.getKeyCode() == 38) {
            etSn.requestFocusInWindow();
            etSn.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etPss.requestFocusInWindow();
            etPss.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etPss.requestFocusInWindow();
            etPss.setCaretPosition(0);
        }
    }//GEN-LAST:event_etPrrKeyPressed

    private void etPssKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etPssKeyPressed
        if (evt.getKeyCode() == 38) {
            etPrr.requestFocusInWindow();
            etPrr.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etHzr.requestFocusInWindow();
            etHzr.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etHzr.requestFocusInWindow();
            etHzr.setCaretPosition(0);
        }
    }//GEN-LAST:event_etPssKeyPressed

    private void etHzrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etHzrKeyPressed
        if (evt.getKeyCode() == 38) {
            etPss.requestFocusInWindow();
            etPss.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etKmr.requestFocusInWindow();
            etKmr.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etKmr.requestFocusInWindow();
            etKmr.setCaretPosition(0);
        }
    }//GEN-LAST:event_etHzrKeyPressed

    private void etKmrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etKmrKeyPressed
        if (evt.getKeyCode() == 38) {
            etHzr.requestFocusInWindow();
            etHzr.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCc.requestFocusInWindow();
            etCc.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etCc.requestFocusInWindow();
            etCc.setCaretPosition(0);
        }
    }//GEN-LAST:event_etKmrKeyPressed

    private void etCcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCcKeyPressed
        if (evt.getKeyCode() == 38) {
            etKmr.requestFocusInWindow();
            etKmr.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etPrb.requestFocusInWindow();
            etPrb.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etPrb.requestFocusInWindow();
            etPrb.setCaretPosition(0);
        }
    }//GEN-LAST:event_etCcKeyPressed

    private void etPrbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etPrbKeyPressed
        if (evt.getKeyCode() == 38) {
            etCc.requestFocusInWindow();
            etCc.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCno.requestFocusInWindow();
            etCno.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etCno.requestFocusInWindow();
            etCno.setCaretPosition(0);
        }
    }//GEN-LAST:event_etPrbKeyPressed

    /**
     * Расчет ПРИБЫЛИ ОТ СТОИМОСТИ
     *
     * @param evt
     */
    private void etCnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCnoKeyPressed
        if (evt.getKeyCode() == 38) {
            etPrb.requestFocusInWindow();
            etPrb.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCno2.requestFocusInWindow();
            etCno2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setBool(true);
            etCno2.requestFocusInWindow();
            etCno2.setCaretPosition(0);
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            setBool(true);
            calculation(getId());
        }
    }//GEN-LAST:event_etCnoKeyPressed

    /**
     * Обработка переходов по стрелкам и расчет по нажатию клавиши ENTER
     *
     * @param evt
     */

    private void etCno2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCno2KeyPressed
        if (evt.getKeyCode() == 38) {
            etCno.requestFocusInWindow();
            etCno.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCnv.requestFocusInWindow();
            etCnv.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etCnv.requestFocusInWindow();
            etCnv.setCaretPosition(0);
        }
    }//GEN-LAST:event_etCno2KeyPressed

    private void etCnvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCnvKeyPressed
        if (evt.getKeyCode() == 38) {
            etCno2.requestFocusInWindow();
            etCno2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCnv2.requestFocusInWindow();
            etCnv2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etCnv2.requestFocusInWindow();
            etCnv2.setCaretPosition(0);
        }
    }//GEN-LAST:event_etCnvKeyPressed

    private void etCnv2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCnv2KeyPressed
        if (evt.getKeyCode() == 38) {
            etCnv.requestFocusInWindow();
            etCnv.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCnr.requestFocusInWindow();
            etCnr.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etCnr.requestFocusInWindow();
            etCnr.setCaretPosition(0);
        }
    }//GEN-LAST:event_etCnv2KeyPressed

    private void etCnrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCnrKeyPressed
        if (evt.getKeyCode() == 38) {
            etCnv2.requestFocusInWindow();
            etCnv2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCnr2.requestFocusInWindow();
            etCnr2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etCnr2.requestFocusInWindow();
            etCnr2.setCaretPosition(0);
        }
    }//GEN-LAST:event_etCnrKeyPressed

    private void etCnr2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCnr2KeyPressed
        if (evt.getKeyCode() == 38) {
            etCnr.requestFocusInWindow();
            etCnr.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            btnCalculationProfitability.requestFocusInWindow();
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
        }
    }//GEN-LAST:event_etCnr2KeyPressed

    /**
     * Перемещение по стрелкам
     */

    private void btnCalculationProfitabilityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCalculationProfitabilityKeyPressed
        if (evt.getKeyCode() == 38) {
            etCnr2.requestFocusInWindow();
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnRememberToPrint.requestFocusInWindow();
        }

    }//GEN-LAST:event_btnCalculationProfitabilityKeyPressed

    private void btnRememberToPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnRememberToPrintKeyPressed
        if (evt.getKeyCode() == 38) {
            btnCalculationProfitability.requestFocusInWindow();
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPrint.requestFocusInWindow();
        }
    }//GEN-LAST:event_btnRememberToPrintKeyPressed

    private void btnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPrintKeyPressed
        if (evt.getKeyCode() == 38) {
            btnRememberToPrint.requestFocusInWindow();
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnCancel.requestFocusInWindow();
        }
    }//GEN-LAST:event_btnPrintKeyPressed

    private void btnCancelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelKeyPressed
        if (evt.getKeyCode() == 38) {
            btnPrint.requestFocusInWindow();
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSave.requestFocusInWindow();
        }
    }//GEN-LAST:event_btnCancelKeyPressed

    private void btnSaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSaveKeyPressed
        if (evt.getKeyCode() == 38) {
            btnCancel.requestFocusInWindow();
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnClose.requestFocusInWindow();
        }
    }//GEN-LAST:event_btnSaveKeyPressed

    private void btnCloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCloseKeyPressed
        if (evt.getKeyCode() == 38) {
            btnSave.requestFocusInWindow();
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etFas.requestFocusInWindow();
        }
    }//GEN-LAST:event_btnCloseKeyPressed

    private void etNiz1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNiz1KeyPressed
        if (evt.getKeyCode() == 38) {
            etNiz.requestFocusInWindow();
        }

        if (evt.getKeyCode() == 40 || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etPol.requestFocusInWindow();
        }
    }//GEN-LAST:event_etNiz1KeyPressed

    private void etNiz1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etNiz1ActionPerformed
    }//GEN-LAST:event_etNiz1ActionPerformed

    /**
     * Обработка нажатий клавиатуры. Лучше не трогать.
     */

    private void etTenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etTenKeyPressed
        if (evt.getKeyCode() == 38) {
            etSmvs.requestFocusInWindow();
            etSmvs.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etTes.requestFocusInWindow();
            etTes.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etTes.setCaretPosition(0);
            etTes.requestFocusInWindow();
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etTen.getValue().toString().indexOf(".");
            // etTen.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etTenKeyPressed

    private void etVmtpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etVmtpKeyPressed
        if (evt.getKeyCode() == 38) {
            etVms.requestFocusInWindow();
            etVms.setCaretPosition(0
            );
        }

        if (evt.getKeyCode() == 40) {
            etVmts.requestFocusInWindow();
            etVmts.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etVmts.requestFocusInWindow();
            etVmts.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etVmtp.getValue().toString().indexOf(".");
            //etVmtp.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etVmtpKeyPressed

    private void etVmsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etVmsKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etVmtp.setCaretPosition(0);
            etVmtp.requestFocusInWindow();
            calculation(getId());
        }
    }//GEN-LAST:event_etVmsKeyPressed

    private void etVmtsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etVmtsKeyPressed
        if (evt.getKeyCode() == 38) {
            etVmtp.requestFocusInWindow();
            etVmtp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etSnp.requestFocusInWindow();
            etSnp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etSnp.requestFocusInWindow();
            etSnp.setCaretPosition(0);
        }
    }//GEN-LAST:event_etVmtsKeyPressed

    private void etSmvpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSmvpKeyPressed
        if (evt.getKeyCode() == 38) {
            etSmts.requestFocusInWindow();
            etSmts.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etSmvs.requestFocusInWindow();
            etSmvs.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etSmvs.requestFocusInWindow();
            etSmvs.setCaretPosition(0);
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etSmvp.getValue().toString().indexOf(".");
            //etSmvp.setCaretPosition(temp + 1);
            calculation(getId());
        }
    }//GEN-LAST:event_etSmvpKeyPressed

    private void etSmtpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSmtpKeyPressed
        if (evt.getKeyCode() == 38) {
            etCsr9.requestFocusInWindow();
            etCsr9.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etSmts.requestFocusInWindow();
            etSmts.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etSmts.requestFocusInWindow();
            etSmts.setCaretPosition(0);
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etSmtp.getValue().toString().indexOf(".");
            // etSmtp.setCaretPosition(temp + 1);
            calculation(getId());
        }
    }//GEN-LAST:event_etSmtpKeyPressed

    private void etCsr9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCsr9KeyPressed
        if (evt.getKeyCode() == 38) {
            etWsr9.requestFocusInWindow();
            etWsr9.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etSmtp.requestFocusInWindow();
            etSmtp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etSmtp.setCaretPosition(0);
            etSmtp.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etCsr9.getValue().toString().indexOf(".");
            //etCsr9.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etCsr9KeyPressed

    private void etCsr8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCsr8KeyPressed
        if (evt.getKeyCode() == 38) {
            etWsr8.requestFocusInWindow();
            etWsr8.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etNsr9.requestFocusInWindow();
            etNsr9.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNsr9.setCaretPosition(0);
            etNsr9.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etCsr8.getValue().toString().indexOf(".");
            // etCsr8.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etCsr8KeyPressed

    private void etCsr7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCsr7KeyPressed
        if (evt.getKeyCode() == 38) {
            etWsr7.requestFocusInWindow();
            etWsr7.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etNsr8.requestFocusInWindow();
            etNsr8.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNsr8.setCaretPosition(0);
            etNsr8.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            //etCsr7.setCaretPosition(pos);
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etCsr7.getValue().toString().indexOf(".");
            //etCsr7.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etCsr7KeyPressed

    private void etCsr6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCsr6KeyPressed
        if (evt.getKeyCode() == 38) {
            etWsr6.requestFocusInWindow();
            etWsr6.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etNsr7.requestFocusInWindow();
            etNsr7.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNsr7.setCaretPosition(0);
            etNsr7.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            // etCsr6.setCaretPosition(pos);
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etCsr6.getValue().toString().indexOf(".");
            ///etCsr6.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etCsr6KeyPressed

    private void etCsr5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCsr5KeyPressed
        if (evt.getKeyCode() == 38) {
            etWsr5.requestFocusInWindow();
            etWsr5.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etNsr6.requestFocusInWindow();
            etNsr6.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNsr6.setCaretPosition(0);
            etNsr6.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etCsr5.getValue().toString().indexOf(".");
            //etCsr5.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etCsr5KeyPressed

    private void etCsr4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCsr4KeyPressed
        if (evt.getKeyCode() == 38) {
            etWsr4.requestFocusInWindow();
            etWsr4.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etNsr5.requestFocusInWindow();
            etNsr5.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNsr5.setCaretPosition(0);
            etNsr5.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etCsr4.getValue().toString().indexOf(".");
            // etCsr4.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etCsr4KeyPressed

    private void etCsr3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCsr3KeyPressed
        if (evt.getKeyCode() == 38) {
            etWsr3.requestFocusInWindow();
            etWsr3.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etNsr4.requestFocusInWindow();
            etNsr4.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNsr4.setCaretPosition(0);
            etNsr4.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etCsr3.getValue().toString().indexOf(".");
            //etCsr3.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etCsr3KeyPressed

    private void etCsr2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCsr2KeyPressed
        if (evt.getKeyCode() == 38) {
            etWsr2.requestFocusInWindow();
            etWsr2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etNsr3.requestFocusInWindow();
            etNsr3.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNsr3.setCaretPosition(0);
            etNsr3.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etCsr2.getValue().toString().indexOf(".");
            //etCsr2.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etCsr2KeyPressed

    private void etCsr1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etCsr1KeyPressed
        if (evt.getKeyCode() == 38) {
            etWsr1.requestFocusInWindow();
            etWsr1.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etNsr2.requestFocusInWindow();
            etNsr2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            etNsr2.setCaretPosition(0);
            etNsr2.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {
            calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etCsr1.getValue().toString().indexOf(".");
            //etCsr1.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etCsr1KeyPressed

    private void etWsr9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etWsr9KeyPressed
        if (evt.getKeyCode() == 38) {
            etNsr9.requestFocusInWindow();
            etNsr9.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCsr9.requestFocusInWindow();
            etCsr9.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
            StringBuffer sb = new StringBuffer(etWsr9.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr9.getCaretPosition();
            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }
            }

            //System.out.println(sb);
            setTextWSR(etWsr9, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCsr9.setCaretPosition(0);
            etCsr9.requestFocusInWindow();
            calculation(getId());
        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            StringBuffer sb;
            sb = new StringBuffer(etWsr9.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr9.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etWsr9.setValue(sb.toString());

            etWsr9.setCaretPosition(pos);

        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etWsr9.getValue().toString().indexOf(".");
            etWsr9.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etWsr9KeyPressed

    private void etWsr8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etWsr8KeyPressed
        if (evt.getKeyCode() == 38) {
            etNsr8.requestFocusInWindow();
            etNsr8.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCsr8.requestFocusInWindow();
            etCsr8.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
            StringBuffer sb = new StringBuffer(etWsr8.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr8.getCaretPosition();
            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextWSR(etWsr8, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCsr8.setCaretPosition(0);
            etCsr8.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            StringBuffer sb;
            sb = new StringBuffer(etWsr8.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr8.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etWsr8.setValue(sb.toString());
            etWsr8.setCaretPosition(pos);

        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etWsr8.getValue().toString().indexOf(".");
            etWsr8.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etWsr8KeyPressed

    private void etWsr7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etWsr7KeyPressed
        if (evt.getKeyCode() == 38) {
            etNsr7.requestFocusInWindow();
            etNsr7.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCsr7.requestFocusInWindow();
            etCsr7.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
            StringBuffer sb = new StringBuffer(etWsr7.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr7.getCaretPosition();
            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextWSR(etWsr7, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCsr7.setCaretPosition(0);
            etCsr7.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            StringBuffer sb;
            sb = new StringBuffer(etWsr7.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr7.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etWsr7.setValue(sb.toString());
            etWsr7.setCaretPosition(pos);
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etWsr7.getValue().toString().indexOf(".");
            etWsr7.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etWsr7KeyPressed

    private void etWsr6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etWsr6KeyPressed
        if (evt.getKeyCode() == 38) {
            etNsr6.requestFocusInWindow();
            etNsr6.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCsr6.requestFocusInWindow();
            etCsr6.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
            StringBuffer sb = new StringBuffer(etWsr6.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr6.getCaretPosition();
            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextWSR(etWsr6, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCsr6.setCaretPosition(0);
            etCsr6.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            StringBuffer sb;
            sb = new StringBuffer(etWsr6.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr6.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etWsr6.setValue(sb.toString());
            etWsr6.setCaretPosition(pos);
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etWsr6.getValue().toString().indexOf(".");
            etWsr6.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etWsr6KeyPressed

    private void etWsr5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etWsr5KeyPressed
        if (evt.getKeyCode() == 38) {
            etNsr5.requestFocusInWindow();
            etNsr5.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCsr5.requestFocusInWindow();
            etCsr5.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
            StringBuffer sb = new StringBuffer(etWsr5.getText());
            int index = sb.indexOf(".");
            //.println(index + "!");
            pos = etWsr5.getCaretPosition();
            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextWSR(etWsr5, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCsr5.setCaretPosition(0);
            etCsr5.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            StringBuffer sb;
            sb = new StringBuffer(etWsr5.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr5.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etWsr5.setValue(sb.toString());
            etWsr5.setCaretPosition(pos);
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etWsr5.getValue().toString().indexOf(".");
            etWsr5.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etWsr5KeyPressed

    private void etWsr4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etWsr4KeyPressed
        if (evt.getKeyCode() == 38) {
            etNsr4.requestFocusInWindow();
            etNsr4.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCsr4.requestFocusInWindow();
            etCsr4.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
            StringBuffer sb = new StringBuffer(etWsr4.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr4.getCaretPosition();
            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextWSR(etWsr4, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCsr4.setCaretPosition(0);
            etCsr4.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            StringBuffer sb;
            sb = new StringBuffer(etWsr4.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr4.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etWsr4.setValue(sb.toString());

            etWsr4.setCaretPosition(pos);

        }
        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etWsr4.getValue().toString().indexOf(".");
            etWsr4.setCaretPosition(temp + 1);
        }

    }//GEN-LAST:event_etWsr4KeyPressed

    private void etWsr3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etWsr3KeyPressed
        if (evt.getKeyCode() == 38) {
            etNsr3.requestFocusInWindow();
            etNsr3.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCsr3.requestFocusInWindow();
            etCsr3.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
            StringBuffer sb = new StringBuffer(etWsr3.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr3.getCaretPosition();
            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextWSR(etWsr3, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCsr3.setCaretPosition(0);
            etCsr3.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            StringBuffer sb;
            sb = new StringBuffer(etWsr3.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr3.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etWsr3.setValue(sb.toString());
            etWsr3.setCaretPosition(pos);
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etWsr3.getValue().toString().indexOf(".");
            etWsr3.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etWsr3KeyPressed

    private void etWsr3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etWsr3ActionPerformed
    }//GEN-LAST:event_etWsr3ActionPerformed

    private void etWsr2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etWsr2KeyPressed
        if (evt.getKeyCode() == 38) {
            etNsr2.requestFocusInWindow();
            etNsr2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCsr2.requestFocusInWindow();
            etCsr2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
            StringBuffer sb = new StringBuffer(etWsr2.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr2.getCaretPosition();
            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextWSR(etWsr2, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCsr2.setCaretPosition(0);
            etCsr2.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            //calculation(getId());
            StringBuffer sb;
            sb = new StringBuffer(etWsr2.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr2.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etWsr2.setValue(sb.toString());

            etWsr2.setCaretPosition(pos);
            //calculation(getId());

        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etWsr2.getValue().toString().indexOf(".");
            etWsr2.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etWsr2KeyPressed

    private void etWsr1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etWsr1KeyPressed
        if (evt.getKeyCode() == 38) {
            etNsr1.requestFocusInWindow();
            etNsr1.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCsr1.setCaretPosition(0);
            etCsr1.requestFocusInWindow();

        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
            StringBuffer sb = new StringBuffer(etWsr1.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr1.getCaretPosition();
            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextWSR(etWsr1, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCsr1.setCaretPosition(0);
            etCsr1.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            //calculation(getId());
            StringBuffer sb;
            sb = new StringBuffer(etWsr1.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etWsr1.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etWsr1.setValue(sb.toString());

            etWsr1.setCaretPosition(pos);

        }
        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etWsr1.getValue().toString().indexOf(".");
            etWsr1.setCaretPosition(temp + 1);
            calculation(getId());
        }
    }//GEN-LAST:event_etWsr1KeyPressed

    private void etSmvsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSmvsKeyPressed
        if (evt.getKeyCode() == 38) {
            etSmvp.requestFocusInWindow();
            etSmvp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etTen.requestFocusInWindow();
            etTen.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etTen.requestFocusInWindow();
            etTen.setCaretPosition(0);
        }
    }//GEN-LAST:event_etSmvsKeyPressed

    private void etSmtsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etSmtsKeyPressed
        if (evt.getKeyCode() == 38) {
            etSmtp.requestFocusInWindow();
            etSmtp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etSmvp.requestFocusInWindow();
            etSmvp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etSmvp.requestFocusInWindow();
            etSmvp.setCaretPosition(0);
        }
    }//GEN-LAST:event_etSmtsKeyPressed

    private void etNsr9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNsr9KeyPressed
        if (evt.getKeyCode() == 38) {
            etCsr8.requestFocusInWindow();
            etCsr8.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etWsr9.requestFocusInWindow();
            etWsr9.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etWsr9.requestFocusInWindow();
            etWsr9.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNsr9KeyPressed

    private void etNsr8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNsr8KeyPressed
        if (evt.getKeyCode() == 38) {
            etCsr7.requestFocusInWindow();
            etCsr7.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etWsr8.requestFocusInWindow();
            etWsr8.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etWsr8.requestFocusInWindow();
            etWsr8.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNsr8KeyPressed

    private void etNsr7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNsr7KeyPressed
        if (evt.getKeyCode() == 38) {
            etCsr6.requestFocusInWindow();
            etCsr6.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etWsr7.requestFocusInWindow();
            etWsr7.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etWsr7.requestFocusInWindow();
            etWsr7.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNsr7KeyPressed

    private void etNsr6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNsr6KeyPressed
        if (evt.getKeyCode() == 38) {
            etCsr5.requestFocusInWindow();
            etCsr5.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etWsr6.requestFocusInWindow();
            etWsr6.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etWsr6.requestFocusInWindow();
            etWsr6.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNsr6KeyPressed

    private void etNsr5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNsr5KeyPressed
        if (evt.getKeyCode() == 38) {
            etCsr4.requestFocusInWindow();
            etCsr4.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etWsr5.requestFocusInWindow();
            etWsr5.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etWsr5.requestFocusInWindow();
            etWsr5.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNsr5KeyPressed

    private void etNsr4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNsr4KeyPressed
        if (evt.getKeyCode() == 38) {
            etCsr3.requestFocusInWindow();
            etCsr3.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etWsr4.requestFocusInWindow();
            etWsr4.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etWsr4.requestFocusInWindow();
            etWsr4.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNsr4KeyPressed

    private void etNsr3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNsr3KeyPressed
        if (evt.getKeyCode() == 38) {
            etCsr2.requestFocusInWindow();
            etCsr2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etWsr3.requestFocusInWindow();
            etWsr3.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etWsr3.requestFocusInWindow();
            etWsr3.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNsr3KeyPressed

    private void etNsr2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNsr2KeyPressed
        if (evt.getKeyCode() == 38) {
            etCsr1.requestFocusInWindow();
            etCsr1.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etWsr2.requestFocusInWindow();
            etWsr2.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etWsr2.requestFocusInWindow();
            etWsr2.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNsr2KeyPressed

    private void etNsr1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etNsr1KeyPressed
        if (evt.getKeyCode() == 38) {
            etDat.requestFocusInWindow();
            etDat.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etWsr1.requestFocusInWindow();
            etWsr1.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculation(getId());
            etWsr1.requestFocusInWindow();
            etWsr1.setCaretPosition(0);
        }
    }//GEN-LAST:event_etNsr1KeyPressed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setBool(true);
        etCno.setText(tvCno.getText());
        jRadioButtonNew.setSelected(true);
        jRadioButtonNewActionPerformed(evt);
        calculation(getId());
    }//GEN-LAST:event_btnNewActionPerformed

    private void etZpoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etZpoActionPerformed
    }//GEN-LAST:event_etZpoActionPerformed

    private void etCnoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_etCnoFocusGained
        setBool(true);
    }//GEN-LAST:event_etCnoFocusGained

    private void etTesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etTesActionPerformed
    }//GEN-LAST:event_etTesActionPerformed

    private void etTesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etTesKeyPressed
        if (evt.getKeyCode() == 38) {
            etTen.requestFocusInWindow();
            etTen.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etVms.requestFocusInWindow();
            etVms.setCaretPosition(0);
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            calculation(getId());
/*            StringBuffer sb = new StringBuffer(etTes.getText());
            int index = sb.indexOf(".");
            System.out.println(index + "!");
            pos = etTes.getCaretPosition();

            for (int i = pos; i <= 8 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            System.out.println(sb);
            setTextZP(etTes, Double.valueOf(sb.toString().replaceAll(" ", "")));*/

            setTextZP(etTes, Double.valueOf(etTes.getText().trim()));
            etVms.setCaretPosition(0);
            etVms.requestFocusInWindow();

            calculation(getId());
        }

/*        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            // calculation(getId());
            StringBuffer sb;
            sb = new StringBuffer(etTes.getText());
            int index = sb.indexOf(".");
            System.out.println(index + "!");
            pos = etTes.getCaretPosition();
            System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etTes.setValue(sb.toString());

            etTes.setCaretPosition(pos);
            //calculation(getId());

        }*/
        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
/*            int temp = etTes.getValue().toString().indexOf(".");
            etTes.setCaretPosition(temp + 1);*/
            calculation(getId());
        }
    }//GEN-LAST:event_etTesKeyPressed

    private void etPrbpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etPrbpKeyPressed
        if (evt.getKeyCode() == 38) {
            etKmrp.requestFocusInWindow();
            etKmrp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etCnvp.requestFocusInWindow();
            etCnvp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setBool(false);
            //calculation(getId());
            StringBuffer sb = new StringBuffer(etPrbp.getText());
            int index = sb.indexOf(".");
            pos = etPrbp.getCaretPosition();

            for (int i = pos; i <= 5 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }

            setTextKf(etPrbp, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etCnvp.setCaretPosition(0);
            etCnvp.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105) || evt.getKeyCode() == 109) {

            //calculation(getId());
            StringBuffer sb;
            sb = new StringBuffer(etPrbp.getText());
            int index = sb.indexOf(".");
            pos = etPrbp.getCaretPosition();

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etPrbp.setValue(sb.toString());
            etPrbp.setCaretPosition(pos);
            //calculation(getId());

        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etPrbp.getValue().toString().indexOf(".");
            etPrbp.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etPrbpKeyPressed

    private void etPrrpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etPrrpKeyPressed
        if (evt.getKeyCode() == 38) {
            etSnp.requestFocusInWindow();
            etSnp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etHzrp.requestFocusInWindow();
            etHzrp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setBool(false);
            //calculation(getId());
            StringBuffer sb = new StringBuffer(etPrrp.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etPrrp.getCaretPosition();

            for (int i = pos; i <= 5 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextKf(etPrrp, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etHzrp.setCaretPosition(0);
            etHzrp.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            // calculation(getId());
            StringBuffer sb;
            sb = new StringBuffer(etPrrp.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etPrrp.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {

                    sb.setCharAt(i, ' ');

                }
            }
            etPrrp.setValue(sb.toString());

            etPrrp.setCaretPosition(pos);
            //calculation(getId());

        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etPrrp.getValue().toString().indexOf(".");
            etPrrp.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etPrrpKeyPressed

    private void etHzrpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etHzrpKeyPressed
        if (evt.getKeyCode() == 38) {
            etPrrp.requestFocusInWindow();
            etPrrp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etKmrp.requestFocusInWindow();
            etKmrp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setBool(false);
            // calculation(getId());
            StringBuffer sb = new StringBuffer(etHzrp.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etHzrp.getCaretPosition();

            for (int i = pos; i <= 5 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            //System.out.println(sb);
            setTextKf(etHzrp, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etKmrp.setCaretPosition(0);
            etKmrp.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            //calculation(getId());
            StringBuffer sb;
            sb = new StringBuffer(etHzrp.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etHzrp.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {
                    sb.setCharAt(i, ' ');
                }
            }
            etHzrp.setValue(sb.toString());

            etHzrp.setCaretPosition(pos);
            //calculation(getId());

        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etHzrp.getValue().toString().indexOf(".");
            etHzrp.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etHzrpKeyPressed

    private void etKmrpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_etKmrpKeyPressed
        if (evt.getKeyCode() == 38) {
            etHzrp.requestFocusInWindow();
            etHzrp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == 40) {
            etPrbp.requestFocusInWindow();
            etPrbp.setCaretPosition(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setBool(false);
            // calculation(getId());
            StringBuffer sb = new StringBuffer(etKmrp.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etKmrp.getCaretPosition();

            for (int i = pos; i <= 5 && pos != 0; i++) {

                if (i != 0 && i != index && i < index) {
                    sb.setCharAt(i, ' ');
                }

                if (i != 0 && i != index && i > index) {
                    sb.setCharAt(i, '0');
                }

            }
            // System.out.println(sb);
            setTextKf(etKmrp, Double.valueOf(sb.toString().replaceAll(" ", "")));
            etPrbp.setCaretPosition(0);
            etPrbp.requestFocusInWindow();
            calculation(getId());

        }

        if ((evt.getKeyCode() >= 48 && evt.getKeyCode() <= 57) || (evt.getKeyCode() >= 96 && evt.getKeyCode() <= 105)) {

            // calculation(getId());
            StringBuffer sb;
            sb = new StringBuffer(etKmrp.getText());
            int index = sb.indexOf(".");
            //System.out.println(index + "!");
            pos = etKmrp.getCaretPosition();
            //System.out.println(pos);

            if ((pos + 1) < index) {
                for (int i = pos + 1; i < index; i++) {
                    sb.setCharAt(i, ' ');
                }
            }
            etKmrp.setValue(sb.toString());

            etKmrp.setCaretPosition(pos);
            // calculation(getId());
        }

        if (evt.getKeyCode() == 110 || evt.getKeyCode() == 108) {
            int temp = etKmrp.getValue().toString().indexOf(".");
            etKmrp.setCaretPosition(temp + 1);
        }
    }//GEN-LAST:event_etKmrpKeyPressed

    //ЗАпомнить для согл
    private void btnSaveToSoglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveToSoglActionPerformed
        if (SaveForPrint.listSoglId.size() < 3) {
            SaveForPrint.listSoglId.add(getId());
        }

        if (SaveForPrint.listSoglId.size() == 3) {

            //Отобразить форму выбора протокола согласования
            ProtocolTypeDialog dialog = new ProtocolTypeDialog(mainController, this);
            ProtocolPreset preset = new ProtocolPreset();

            double[] currency = ppdb.getKurs();

            preset.setCurrencyRateSet(currency);
            ProtocolPreset resultPreset = dialog.showDialog(preset);

            if (resultPreset != null) {

                //SaveForPrint.listSoglId.add(getId());
                PrintListSogl printListSogl = new PrintListSogl(SaveForPrint.listSoglId);

                try {
                    printListSogl.printListAdvanced(resultPreset);
                } catch (NoSuchElementException ex) {
                    java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WrappedTargetException ex) {
                    java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IndexOutOfBoundsException ex) {
                    java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            SaveForPrint.listSoglId = new LinkedList<Integer>();
        }
    }//GEN-LAST:event_btnSaveToSoglActionPerformed

    //Печать согласования
    private void btnPrintSoglActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintSoglActionPerformed

/*        if (SaveForPrint.listSoglId.size() < 3) {
            SaveForPrint.listSoglId.add(getId());
        }*/

        //Отобразить форму выбора протокола согласования
        ProtocolTypeDialog dialog = new ProtocolTypeDialog(mainController, this);
        ProtocolPreset preset = new ProtocolPreset();

        double[] currency = ppdb.getKurs();

        preset.setCurrencyRateSet(currency);
        ProtocolPreset resultPreset = dialog.showDialog(preset);

        if (resultPreset != null) {
/*
            System.out.println("предустановка тип ("+resultPreset.getProtocolType()+") валюта ("+resultPreset.getCurrencyType()+") курс ("+
                    resultPreset.getCurrencyRate()+")");*/

            //SaveForPrint.listSoglId.add(getId());

            if (SaveForPrint.listSoglId.size() == 0) {
                SaveForPrint.listSoglId.add(getId());
            }

            PrintListSogl printListSogl = new PrintListSogl(SaveForPrint.listSoglId);

            try {
                printListSogl.printListAdvanced(resultPreset);
            } catch (NoSuchElementException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        SaveForPrint.listSoglId = new LinkedList<Integer>();
    }//GEN-LAST:event_btnPrintSoglActionPerformed

    //Печать согл детский
    private void btnPrintDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintDetActionPerformed
        SaveForPrint.listSogDetlId.add(getId());
        PrintListSogl printListSogl = new PrintListSogl(SaveForPrint.listSogDetlId);
        try {
            printListSogl.printListDet();
        } catch (NoSuchElementException ex) {
            java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrappedTargetException ex) {
            java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IndexOutOfBoundsException ex) {
            java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        SaveForPrint.listSogDetlId = new LinkedList<Integer>();
    }//GEN-LAST:event_btnPrintDetActionPerformed

    //Замомнить для согл детский
    private void btnPrintForDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintForDetActionPerformed
        if (SaveForPrint.listSogDetlId.size() < 4) {
            SaveForPrint.listSogDetlId.add(getId());
        }
        if (SaveForPrint.listSogDetlId.size() == 4) {
            PrintListSogl printListSogl = new PrintListSogl(SaveForPrint.listSogDetlId);
            try {
                printListSogl.printListDet();
            } catch (NoSuchElementException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException ex) {
                java.util.logging.Logger.getLogger(CalculationOfPriceForm.class.getName()).log(Level.SEVERE, null, ex);
            }

            SaveForPrint.listSogDetlId = new LinkedList<Integer>();

        }
    }//GEN-LAST:event_btnPrintForDetActionPerformed

    //Сбросить детский
    private void btnDetResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetResetActionPerformed
        SaveForPrint.listSogDetlId = new LinkedList<Integer>();
    }//GEN-LAST:event_btnDetResetActionPerformed

    private void etVmsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_etVmsActionPerformed
    }//GEN-LAST:event_etVmsActionPerformed

    /**
     * @return the bool
     */
    public boolean isBool() {
        return isBoolCalc();
    }
    // End of variables declaration//GEN-END:variables

    /**
     * @param bool the bool to set
     */
    public void setBool(boolean bool) {
        this.setBoolCalc(bool);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the valueCalc
     */
    public ValueCalculation getValueCalc() {
        return valueCalc;
    }

    /**
     * @param valueCalc the valueCalc to set
     */
    public void setValueCalc(ValueCalculation valueCalc) {
        this.valueCalc = valueCalc;
    }

    /**
     * @return the numberPrice
     */
    public int getNumberPrice() {
        return numberPrice;
    }

    /**
     * @param numberPrice the numberPrice to set
     */
    public void setNumberPrice(int numberPrice) {
        this.numberPrice = numberPrice;
    }

    /**
     * @return the itTypeCalculation
     */
    public int getItTypeCalculation() {
        return idTypeCalculation;
    }

    /**
     * @param itTypeCalculation the itTypeCalculation to set
     */
    public void setItTypeCalculation(int itTypeCalculation) {
        this.idTypeCalculation = itTypeCalculation;
    }

    //Закрытие формы с расчетом
    @Override
    public void dispose() {

        if (close) {
            super.dispose();
            self.updateTableEnd(endTable);

        } else {
            JOptionPane.showMessageDialog(null, "Имеются измененные данные!Для закрытия формы сначала  нажмите Сохранить или Отменить", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Устанавливает значение поля для данных ZP
     *
     * @param jftf  Форматированное текстовое поле в которое устанавливается
     *              значение
     * @param value Устанавливаемое значение
     */
    public void setTextZP(UCTextField jftf, double value) {
        jftf.setText(String.valueOf(value));
    }

    /**
     * @param jftf  Форматированное текстовое поле в которое устанавливается
     *              значение
     * @param value Устанавливаемое значение
     */
    public void setTextUSTO(UCTextField jftf, double value) {
        jftf.setText(String.valueOf(value));
    }

    class ListenerKey implements KeyListener {

        JFormattedTextField ftv;

        public ListenerKey(JFormattedTextField ftfield) {
            ftv = ftfield;
        }

        /**
         * Обработка   нажатия точки и запятой.
         *
         * @param e
         */
        @Override
        public void keyReleased(KeyEvent e) {
            Object source = e.getSource();
            if (source == ftv) {
                if (e.getKeyCode() == KeyEvent.VK_COMMA | e.getKeyCode() == KeyEvent.VK_PERIOD | e.getKeyCode() == 110) {
                    String mask = ((MaskFormatter) ftv.getFormatter()).getMask();
                    ftv.setCaretPosition(0);
                }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }
    }
}
