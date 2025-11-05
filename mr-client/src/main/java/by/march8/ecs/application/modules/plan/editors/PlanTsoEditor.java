package by.march8.ecs.application.modules.plan.editors;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.plan.PlanItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dpliushchai on 01.12.2014.
 * Панель редактирования для логина Tso(ТСО)
 */
public class PlanTsoEditor extends EditingPane {

    private static final long serialVersionUID = -3954031689774875650L;
    private final MainController mainController;
    private JTextField tfComposition;
    private JLabel lComposition;
    private JTextArea taNote;
    private JLabel lNote;


    private PlanItem source = new PlanItem();

    public PlanTsoEditor(final MainController mainController) {
        this.mainController = mainController;
        setPreferredSize(new Dimension(270, 250));
        tfComposition = new JTextField();
        lComposition = new JLabel("Состав полотна");
        taNote = new JTextArea();
        lNote = new JLabel("Примечание");


        lComposition.setBounds(10, 10, 250, 25);
        tfComposition.setBounds(10, 30, 250, 25);
        lNote.setBounds(10, 60, 250, 25);
        taNote.setBounds(10, 80, 250, 100);
        taNote.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        this.add(lComposition);
        this.add(tfComposition);
        this.add(lNote);
        this.add(taNote);


    }

    @Override
    public Object getSourceEntity() {


        source.setCompositionCanvas(tfComposition.getText().trim());
        source.setNote(taNote.getText());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {

        if (object == null) {
            this.source = new PlanItem();


        } else {
            this.source = (PlanItem) object;


            tfComposition.setText(this.source.getCompositionCanvas());
            taNote.setText(this.source.getNote());

        }
    }

    @Override
    public boolean verificationData() {


        return true;
    }

}
