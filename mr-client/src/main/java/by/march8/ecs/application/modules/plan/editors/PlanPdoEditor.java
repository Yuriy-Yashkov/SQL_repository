package by.march8.ecs.application.modules.plan.editors;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.plan.PlanItem;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by dpliushchai on 01.12.2014.
 * Панель редактирования для логина Pdo(ПДО)
 */
public class PlanPdoEditor extends EditingPane {

    private static final long serialVersionUID = -3954031689774875650L;
    private final MainController mainController;
    private JTextField tfNumber;
    private JTextField tfAssortmentName;
    private JTextField tfModel;
    private JTextField tfSize;
    private JTextField tfNovetly;
    private JTextField tfArticleCanvas;
    private JTextField tfFactor;
    private JTextField tfCompositionCanvas;
    private JTextField tfColorCanvas;
    private JTextField tfPlan;
    private JTextField tfIssuedClose;
    private JTextField tfClose;
    private JTextField tfShippedContractors;
    private JTextField tfIssuedTailoring;
    private JTextField tfRentedWarehouse;
    private JTextField tfTinctorial;
    private JTextField tfRentedWarehouseContractors;
    private JTextField tfCarryovers;
    private JTextField tfCarryoversContractors;
    private JTextArea tfNote;


    private JLabel lNumber;
    private JLabel lAssortmentName;
    private JLabel lModel;
    private JLabel lSize;
    private JLabel lNovetly;
    private JLabel lArticleCanvas;
    private JLabel lFactor;
    private JLabel lCompositionCanvas;
    private JLabel lColorCanvas;
    private JLabel lPlan;
    private JLabel lIssuedClose;
    private JLabel lClose;
    private JLabel lShippedContractors;
    private JLabel lIssuedTailoring;
    private JLabel lRentedWarehouse;
    private JLabel lTinctorial;
    private JLabel lRentedWarehouseContractors;
    private JLabel lCarryovers;
    private JLabel lCarryoversContractors;
    private JLabel lNote;


    private PlanItem source = new PlanItem();

    public PlanPdoEditor(final MainController mainController) {
        this.mainController = mainController;
        setPreferredSize(new Dimension(400, 500));


        //int x = 10;
        //int y = 30;

        setLayout(new MigLayout());

        lTinctorial = new JLabel("Выдано в красильный цех");
        tfTinctorial = new JTextField();
        add(lTinctorial, "width 200:20:200, split 2, wrap");
        add(tfTinctorial, "width 200:20:200, split 2, wrap");

        lIssuedClose = new JLabel("Выдано на закрой");
        tfIssuedClose = new JTextField();
        add(lIssuedClose, "width 200:20:200, split 2, wrap");
        add(tfIssuedClose, "width 200:20:200, split 2, wrap");


        lShippedContractors = new JLabel("Отгружено подрядчикам");
        tfShippedContractors = new JTextField();
        add(lShippedContractors, "width 200:20:200, span 2, wrap");
        add(tfShippedContractors, "width 200:20:200, span 2, wrap");


        lCarryovers = new JLabel("Переходящие остатки");
        tfCarryovers = new JTextField();
        add(lCarryovers, "width 200:20:200, span 2, wrap");
        add(tfCarryovers, "width 200:20:200, span 2, wrap");

        add(new JPanel(), "height 10:10,  wrap");
        lNovetly = new JLabel("Новинка");
        tfNovetly = new JTextField();
        add(lNovetly, "width 100:20:100, span 2, wrap");
        add(tfNovetly, "width 100:20:100, span 2, wrap");

        lArticleCanvas = new JLabel("Артикул полотна");
        tfArticleCanvas = new JTextField();
        lColorCanvas = new JLabel("Цвет полотна");
        tfColorCanvas = new JTextField();

        add(lArticleCanvas, "width 180:20:180, span 2");
        add(lColorCanvas, "width 200:20:200, span 2, wrap");

        add(tfArticleCanvas, "width 180:20:180, span 2");
        add(tfColorCanvas, "width 150:20:150, span 2, wrap");

        lNote = new JLabel("Примечание");
        tfNote = new JTextArea();
        JScrollPane spNote = new JScrollPane(tfNote);
        add(lNote, "width 200:20:200, wrap");
        add(spNote, "width 380:20:380, height 120:120, span 4, wrap");

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        //tfNote.setBorder(border);

/*
      //  tfNumber = new JTextField();
      //  tfAssortmentName = new JTextField();
     //   tfModel = new JTextField();
     //   tfSize = new JTextField();
       // tfFactor = new JTextField();
     //   tfCompositionCanvas = new JTextField();
    //    tfPlan = new JTextField();
     //   tfClose = new JTextField();
      //  tfIssuedTailoring = new JTextField();
     //   tfRentedWarehouse = new JTextField();
     //   tfRentedWarehouseContractors = new JTextField();
      //  tfCarryovers = new JTextField();
      //  tfCarryoversContractors = new JTextField();
       // lNumber = new JLabel("Номер распоряжения");
        //lAssortmentName = new JLabel("Ассортимент");
        //lModel = new JLabel("Модель");
        //lSize = new JLabel("Размерная шкала");
        //lFactor = new JLabel("Кф");
        //lCompositionCanvas = new JLabel("Состав полотна");
        //lPlan = new JLabel("План");
        //lClose = new JLabel("Закроено");
        //lIssuedTailoring = new JLabel("Выдано на пошив");
        //lRentedWarehouse = new JLabel("Сдано на склад из цеха цех");
        //lRentedWarehouseContractors = new JLabel("Сдано на склад подрядчики");
        //lCarryovers = new JLabel("Переходящие остатки от цеха");
        //lCarryoversContractors = new JLabel("Переходящие остатки от  подрядчиков");
       // lNumber.setBounds(10, x, 250, 25);
        //lAssortmentName.setBounds(10, x += 40, 250, 25);
        //lModel.setBounds(10, x += 40, 250, 25);
        //lSize.setBounds(10, x += 40, 250, 25);
      //  lFactor.setBounds(10, x += 40, 250, 25);
      //  lCompositionCanvas.setBounds(10, x += 40, 250, 25);
      //  lPlan.setBounds(10, x += 40, 250, 25);
        lTinctorial.setBounds(10, x , 250, 25);
        lIssuedClose.setBounds(10, x+=40 , 250, 25);
       // lClose.setBounds(10, x += 40, 250, 25);
        lShippedContractors.setBounds(10, x += 40, 250, 25);
      //  lIssuedTailoring.setBounds(10, x += 40, 250, 25);
      //  lRentedWarehouse.setBounds(10, x += 40, 250, 25);
      //  lRentedWarehouseContractors.setBounds(10, x += 40, 250, 25);
       lCarryovers.setBounds(10, x += 40, 250, 25);
        lArticleCanvas.setBounds(10, x += 40, 250, 25);
        lColorCanvas.setBounds(10, x += 40, 250, 25);
     //   lCarryoversContractors.setBounds(10, x += 40, 250, 25);
        lNote.setBounds(10, x += 40, 250, 25);
      //  tfNumber.setBounds(10, y, 250, 25);
      //  tfAssortmentName.setBounds(10, y+=40, 250, 25);
     //   tfModel.setBounds(10, y+=40, 250, 25);
      //  tfSize.setBounds(10, y+=40, 250, 25);
      //  tfFactor.setBounds(10, y+=40, 250, 25);
      //  tfCompositionCanvas.setBounds(10, y+=40, 250, 25);
      //  tfPlan.setBounds(10, y+=40, 250, 25);
        tfTinctorial.setBounds(10, y, 250, 25);
        tfIssuedClose.setBounds(10, y+=40, 250, 25);
      //  tfClose.setBounds(10, y+=40, 250, 25);
        tfShippedContractors.setBounds(10, y+=40, 250, 25);
     //   tfIssuedTailoring.setBounds(10, y+=40, 250, 25);
    //    tfRentedWarehouse.setBounds(10, y+=40, 250, 25);
        lNovetly.setBounds(10, y += 40, 250, 25);
        tfNovetly.setBounds(10, y+=40, 250, 25);
       // tfRentedWarehouseContractors.setBounds(10, y+=40, 250, 25);
     //   tfCarryovers.setBounds(10, y+=40, 250, 25);
     //   tfCarryoversContractors.setBounds(10, y+=40, 250, 25);
        tfCarryovers.setBounds(10, y+=40, 250, 25);
        tfArticleCanvas.setBounds(10, y+=40, 250, 25);
        tfColorCanvas.setBounds(10, y+=40, 250, 25);
        tfNote.setBounds(10, y+=40, 250, 80);
        tfNote.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
      //  this.add(tfNumber);
      //  this.add(tfAssortmentName);
      //  this.add(tfModel);
      //  this.add(tfSize);
     //   this.add(tfNovetly);
        this.add(tfArticleCanvas);
     //   this.add(tfFactor);
     //   this.add(tfCompositionCanvas);
        this.add(tfColorCanvas);
     //   this.add(tfPlan);
        this.add(tfIssuedClose);
     //   this.add(tfClose);
        this.add(tfShippedContractors);
      //  this.add(tfIssuedTailoring);
     //   this.add(tfRentedWarehouse);
        this.add(tfTinctorial);
     //   this.add(tfRentedWarehouseContractors);
        this.add(tfCarryovers);
     //   this.add(tfCarryoversContractors);
        this.add(tfNote);

     //   this.add(lNumber);
      //  this.add(lAssortmentName);
     //   this.add(lModel);
     //   this.add(lSize);
       this.add(lNovetly);
        this.add(lArticleCanvas);
     //   this.add(lFactor);
     //   this.add(lCompositionCanvas);
        this.add(lColorCanvas);
      //  this.add(lPlan);
        this.add(lIssuedClose);
    //   this.add(lClose);
        this.add(lShippedContractors);
    //    this.add(lIssuedTailoring);
   //     this.add(lRentedWarehouse);
        this.add(lTinctorial);
    //    this.add(lRentedWarehouseContractors);
        this.add(lCarryovers);
   //     this.add(lCarryoversContractors);
        this.add(lNote);
*/


    }

    @Override
    public Object getSourceEntity() {

        source.setIssuedTinctorial(Integer.valueOf(tfTinctorial.getText().trim()));
        source.setIssuedClose(Integer.valueOf(tfIssuedClose.getText().trim()));
        source.setShippedContractors(Integer.valueOf(tfShippedContractors.getText().trim()));
        source.setCarryovers(Integer.valueOf(tfCarryovers.getText().trim()));
        source.setNote(tfNote.getText());
        // source.setNovetly(tfNovetly.getText());

        source.setArticle_canvas(tfArticleCanvas.getText());
        source.setColorCanvas(tfColorCanvas.getText());

        source.setNovetly(tfNovetly.getText());
        /*
        source.setNumber(Integer.valueOf(tfNumber.getText().trim()));
       source.setAssortmentName(tfAssortmentName.getText());
       source.setModel(Integer.valueOf(tfModel.getText().trim()));
        source.setSize(tfSize.getText());
        s

        source.setFactor(Integer.valueOf(tfFactor.getText().trim()));
        source.setCompositionCanvas(tfCompositionCanvas.getText());

        source.setPlan(Integer.valueOf(tfPlan.getText().trim()));
        source.setIssuedClose(Integer.valueOf(tfIssuedClose.getText().trim()));
        source.setClose(Integer.valueOf(tfClose.getText().trim()));
        source.setShippedContractors(Integer.valueOf(tfShippedContractors.getText().trim()));
        source.setIssuedTailoring(Integer.valueOf(tfIssuedTailoring.getText().trim()));

        source.setIssuedTinctorial(Integer.valueOf(tfTinctorial.getText().trim()));
        */
        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            this.source = new PlanItem();


        } else {
            this.source = (PlanItem) object;


            tfIssuedClose.setText(String.valueOf(this.source.getIssuedClose()));
            tfTinctorial.setText(String.valueOf(this.source.getIssuedTinctorial()));
            tfShippedContractors.setText(String.valueOf(this.source.getShippedContractors()));


            tfCarryovers.setText(String.valueOf(this.source.getCarryovers()));
            tfNote.setText(this.source.getNote());

            tfColorCanvas.setText(this.source.getColorCanvas());
            tfArticleCanvas.setText(this.source.getArticle_canvas());

            tfNovetly.setText(this.source.getNovetly());

            /*

            tfNumber.setText(String.valueOf(this.source.getNumber()));
            tfAssortmentName.setText(this.source.getAssortmentName());
            tfModel.setText(String.valueOf(this.source.getModel()));
            tfSize.setText(this.source.getSize());


            tfFactor.setText(String.valueOf(this.source.getFactor()));
            tfCompositionCanvas.setText(this.source.getCompositionCanvas());

            tfPlan.setText(String.valueOf(this.source.getPlan()));

            tfClose.setText(String.valueOf(this.source.getClose()));

            tfIssuedTailoring.setText(String.valueOf(this.source.getIssuedTailoring()));

            */

        }
    }

    @Override
    public boolean verificationData() {


        /*
        try {
            Integer.valueOf(tfNumber.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Номер распоряжения\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfNumber.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(tfModel.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Модель\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfModel.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(tfFactor.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Кф\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfFactor.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(tfPlan.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"План\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfPlan.requestFocusInWindow();
            return false;
        }

*/
        try {
            Integer.valueOf(tfTinctorial.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Выдано в  красильный цех\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfTinctorial.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(tfIssuedClose.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Выдано на закрой\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfIssuedClose.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(tfShippedContractors.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Отгружено подрядчикам\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfShippedContractors.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(tfCarryovers.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Переходящие остатки \" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfCarryovers.requestFocusInWindow();
            return false;
        }

        /*
        try {
            Integer.valueOf(tfClose.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Закроено\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfClose.requestFocusInWindow();
            return false;
        }



        try {
            Integer.valueOf(tfIssuedTailoring.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Выдано на пошив\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfIssuedTailoring.requestFocusInWindow();
            return false;
        }

        try {
            Integer.valueOf(tfRentedWarehouse.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Сдано на склад из цеха\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfRentedWarehouse.requestFocusInWindow();
            return false;
        }



        try {
            Integer.valueOf(tfRentedWarehouseContractors.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Сдано на склад  подрядчиками\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfRentedWarehouseContractors.requestFocusInWindow();
            return false;
        }





        try {
            Integer.valueOf(tfCarryoversContractors.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Введите корректное значение в поле \"Переходящие остатки подрядчики\" ",
                    "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
            tfCarryoversContractors.requestFocusInWindow();
            return false;
        }
*/

        return true;
    }

}
