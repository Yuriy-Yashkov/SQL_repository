package by.march8.ecs.application.modules.plan.editors;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.plan.PlanItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dpliushchai on 01.12.2014.
 * Панель редактирования для логина himlab(хим лабаратория)
 */
public class PlanChemEditor extends EditingPane {


    private static final long serialVersionUID = -3954031689774875650L;
    private final MainController mainController;
    private JTextArea tfColor;
    private JLabel lColor;
    private JTextArea taNote;
    private JLabel lNote;


    private PlanItem source = new PlanItem();


    public PlanChemEditor(final MainController mainController) {
        this.mainController = mainController;
        setPreferredSize(new Dimension(270, 300));
        tfColor = new JTextArea();
        lColor = new JLabel("Цвет полотна");
        taNote = new JTextArea();
        lNote = new JLabel("Примечание");


        lColor.setBounds(10, 10, 250, 25);
        tfColor.setBounds(10, 30, 250, 100);
        lNote.setBounds(10, 130, 250, 25);
        taNote.setBounds(10, 150, 250, 100);
        taNote.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        tfColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        this.add(lColor);
        this.add(tfColor);
        this.add(lNote);
        this.add(taNote);


    }

    @Override
    public Object getSourceEntity() {


        source.setColorCanvas(tfColor.getText());
        source.setNote(taNote.getText());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {

        if (object == null) {
            this.source = new PlanItem();


        } else {
            this.source = (PlanItem) object;


            tfColor.setText(this.source.getColorCanvas());
            taNote.setText(this.source.getNote());

        }
    }

    @Override
    public boolean verificationData() {


        return true;
    }

}

