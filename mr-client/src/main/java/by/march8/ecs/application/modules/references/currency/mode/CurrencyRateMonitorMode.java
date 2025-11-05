package by.march8.ecs.application.modules.references.currency.mode;

//import by.gomel.freedev.currgetter.RateGetter;

import by.gomel.freedev.ucframework.uccore.enums.MarchWindowType;
import by.gomel.freedev.ucframework.uccore.enums.RightEnum;
import by.gomel.freedev.ucframework.uccore.modes.abstracts.AbstractFunctionalMode;
import by.gomel.freedev.ucframework.uccore.viewport.frameviewport.FrameViewPort;
import by.gomel.freedev.ucframework.uccore.viewport.gridviewport.GridViewPort;
import by.gomel.freedev.ucframework.ucdao.DaoFactory;
import by.gomel.freedev.ucframework.ucdao.interfaces.IGenericDao;
import by.gomel.freedev.ucframework.ucdao.model.QueryProperty;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCDatePicker;
import by.gomel.freedev.ucframework.ucswing.uicontrols.UCToolBar;
import by.march8.ecs.MainController;
import by.march8.entities.unknowns.CurrencyRateEntity;
import curen.Getter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Andy 05.08.2016.
 */
public class CurrencyRateMonitorMode extends AbstractFunctionalMode {

    private RightEnum right;
    private ArrayList<Object> rateList;
    private UCDatePicker dpDate = new UCDatePicker(new Date());

    private JButton btnUpdate = new JButton();
    private JButton btnDownload = new JButton();


    public CurrencyRateMonitorMode(MainController mainController) {
        controller = mainController;
        modeName = "Монитор курсов валюты";

        frameViewPort = new FrameViewPort(mainController, MarchWindowType.DIALOGSIMPLE);
        frameViewPort.getFrameControl().setTitleFrame(modeName);
        frameViewPort.getFrameControl().setFrameSize(new Dimension(400, 400));
        frameViewPort.getButtonControl().getOkButton().setVisible(false);
        frameViewPort.getButtonControl().getCancelButton().setText("Закрыть");

        //right = controller.getRight(modeName);
        right = RightEnum.WRITE;
        //frameViewPort.getFrameRegion().getToolBar().setVisible(true);

        final UCToolBar toolBar = frameViewPort.getFrameRegion().getToolBar();

        toolBar.getBtnDeleteItem().setVisible(false);
        toolBar.getBtnNewItem().setVisible(false);
        toolBar.getBtnEditItem().setVisible(false);

        // Комбик выбора типа документа
        JPanel pSearchPanel = new JPanel(null);
        JLabel lblRateDate = new JLabel("Дата");
        lblRateDate.setBounds(5, 4, 40, 20);
        dpDate.setBounds(50, 4, 100, 20);
        dpDate.setDate(new Date());

        pSearchPanel.add(lblRateDate);
        pSearchPanel.add(dpDate);
        pSearchPanel.setPreferredSize(new Dimension(160, 28));
        pSearchPanel.setOpaque(false);

        btnDownload.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/download_24.png", "Получить курсы"));
        btnDownload.setToolTipText("Получить курсы");

        btnUpdate.setIcon(new ImageIcon(MainController.getRunPath() + "/Img/update_data.png", "Обновть данные"));
        btnUpdate.setToolTipText("Обновить данные");

        toolBar.add(btnDownload);
        toolBar.add(pSearchPanel);
        toolBar.add(btnUpdate);

        toolBar.add(new Box(BoxLayout.X_AXIS));
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setPreferredSize(new Dimension(0, 47));

        // Инициализация гридов
        gridViewPort = new GridViewPort(CurrencyRateEntity.class, false);

        // Получаем ссылку на модель данных грида
        rateList = gridViewPort.getDataModel();

        dpDate.addActionListener(a -> updateContent());
        btnUpdate.addActionListener(a -> updateContent());

        btnDownload.addActionListener(a -> updateCurrencyRate());

        frameViewPort.setGridViewPort(gridViewPort);
        gridViewPort.primaryInitialization();

        updateContent();

        frameViewPort.getFrameControl().showFrame();
    }

    public static Double getCurrencyRateByDate(String str, int valu) {

        List<CurrencyRateEntity> rateList = new ArrayList<>();
        DaoFactory<CurrencyRateEntity> factory = DaoFactory.getInstance();
        IGenericDao<CurrencyRateEntity> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        try {
            criteria.add(new QueryProperty("date", new SimpleDateFormat("dd.MM.yyyy").parse(str)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        criteria.add(new QueryProperty("valuta", valu));

        try {
            rateList.clear();
            rateList.addAll(dao.getEntityListByNamedQuery(CurrencyRateEntity.class, "CurrencyRateEntity.findRateByActualDate", criteria));

        } catch (final Exception e) {
            e.printStackTrace();
        }
        AtomicReference<Double> val = new AtomicReference<>((double) 0);
        rateList.forEach(c -> {
            val.set(c.getValueRate());
            System.out.println(val + " hip");
        });
        return val.get();
    }

    private void updateCurrencyRate() {

        Date d = dpDate.getDate();

        if (d != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH) + 1;
            int year = c.get(Calendar.YEAR);

            String dayS = String.valueOf(day);

            String monthS = String.valueOf(month);

            if (day < 10) {
                dayS = "0" + dayS;
            }
            if (month < 10) {
                monthS = "0" + monthS;
            }

            String[] args = {dayS, monthS, String.valueOf(year)};
            //System.out.println(Arrays.toString(args));
            try {
                new Getter(args);
            } catch (IOException ex) {
                System.out.println("Error in method updateCurrencyRate in class CurrencyRateMonitorMode");
            }


            updateContent();
        }
    }

    @Override
    public void updateContent() {
        DaoFactory<CurrencyRateEntity> factory = DaoFactory.getInstance();
        IGenericDao<CurrencyRateEntity> dao = factory.getGenericDao();
        List<QueryProperty> criteria = new ArrayList<>();
        criteria.add(new QueryProperty("date", dpDate.getDate()));

        try {
            rateList.clear();
            rateList.addAll(dao.getEntityListByNamedQuery(CurrencyRateEntity.class, "CurrencyRateEntity.findByActualDate", criteria));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        frameViewPort.updateContent();
        gridViewPort.updateViewPort();
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
