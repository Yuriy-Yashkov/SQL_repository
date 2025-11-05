package by.march8.ecs.application.modules.plan.editors;

import by.gomel.freedev.ucframework.ucswing.uicontrols.EditingPane;
import by.march8.ecs.MainController;
import by.march8.entities.plan.PlanIdItem;
import by.march8.entities.plan.PlanItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dpliushchai on 01.12.2014.
 * Панель редактирования для логина tbshp(ТБШП)
 */
public class PlanTbEditor extends EditingPane {

    private static final long serialVersionUID = -3954031689774875650L;
    private final MainController mainController;
    private JTextField tfNovetly;

    private JTextField tfArticleCanvas;
    private JLabel lArticleCanvas;

    private JLabel lNovetly;


    private JTextArea taNote;
    private JLabel lNote;


    private PlanIdItem planId = new PlanIdItem();


    private PlanItem source = new PlanItem();

    public PlanTbEditor(final MainController mainController) {
        this.mainController = mainController;
        setPreferredSize(new Dimension(270, 280));

        tfArticleCanvas = new JTextField();
        lArticleCanvas = new JLabel("Артикул полотна");
        tfNovetly = new JTextField();
        taNote = new JTextArea();


        lNovetly = new JLabel("Новинка");
        lNote = new JLabel("Примечание");


        lNovetly.setBounds(10, 10, 250, 25);
        tfNovetly.setBounds(10, 30, 250, 25);
        lArticleCanvas.setBounds(10, 60, 250, 25);
        tfArticleCanvas.setBounds(10, 80, 250, 25);
        lNote.setBounds(10, 110, 250, 25);
        taNote.setBounds(10, 130, 250, 100);
        taNote.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));


        this.add(lNovetly);
        this.add(tfNovetly);
        this.add(lArticleCanvas);
        this.add(tfArticleCanvas);
        this.add(lNote);
        this.add(taNote);


        initEvents();

//        this.add(fileChooser);

    }

    @Override
    public Object getSourceEntity() {


        source.setNovetly(tfNovetly.getText());
        source.setNote(taNote.getText());
        source.setArticle_canvas(tfArticleCanvas.getText());

        return source;
    }

    @Override
    public void setSourceEntity(Object object) {
        if (object == null) {
            this.source = new PlanItem();


        } else {
            this.source = (PlanItem) object;


            tfNovetly.setText(this.source.getNovetly());
            taNote.setText(this.source.getNote());
            tfArticleCanvas.setText(this.source.getArticle_canvas());
        }
    }

    @Override
    public boolean verificationData() {


        return true;
    }

    private void initEvents() {


    }
}