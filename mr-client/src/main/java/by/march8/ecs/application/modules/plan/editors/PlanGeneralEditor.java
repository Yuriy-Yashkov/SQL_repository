package by.march8.ecs.application.modules.plan.editors;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.plan.PlanItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dpliushchai on 22.05.2015.
 * Панель редактирования для логина general(Начальство, Только чтение)
 */
public class PlanGeneralEditor extends EditingPane {
    private static final long serialVersionUID = -3954031689774875650L;
    private final MainController mainController;
    private JTextArea taNote;
    private JLabel lNote;


    private PlanItem source = new PlanItem();

    public PlanGeneralEditor(final MainController mainController) {
        this.mainController = mainController;
        setPreferredSize(new Dimension(270, 190));

        taNote = new JTextArea();
        lNote = new JLabel("Примечание");


        lNote.setBounds(10, 10, 250, 25);
        taNote.setBounds(10, 30, 250, 100);
        taNote.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));


        this.add(lNote);
        this.add(taNote);


    }

    @Override
    public Object getSourceEntity() {


        source.setNote(taNote.getText());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {

        if (object == null) {
            this.source = new PlanItem();


        } else {
            this.source = (PlanItem) object;


            taNote.setText(this.source.getNote());

        }
    }

    @Override
    public boolean verificationData() {


        return true;
    }

}


